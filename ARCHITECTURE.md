# 架构说明

这份文档回答三个问题：**代码怎么分层、数据怎么流动、哪些地方是妥协**。

---

## 1. 模块与依赖方向

```
app ──► sign ──┐
 │              ├──► common
 └──► kotlin ───┘
      java ──────┘（仅依赖 common，作为平行对照）
      jetpak ────┘（未使用，Compose/Room 试验田）
```

规则：

1. **依赖严格单向**，`common` 不反向依赖任何业务模块。
2. `common` 只把「出现在公开签名里的类型」声明为 `api`：
   `BaseView` / `RxObserver` 的签名里出现 RxJava 类型，所以 `rxjava` / `rxandroid` 必须是 `api`；
   其余（Retrofit、Glide、LitePal、AgentWeb）一律 `implementation`。
   早期版本把 15 个依赖全用 `api` 导出，导致任何模块都能间接用到未声明的库，
   依赖图名义单向、实际耦合。
3. 业务模块之间**不互相依赖**。`app` 负责编排，`sign` / `kotlin` 互不感知。

---

## 2. 分层与职责

```
┌─────────────────────────────────────────────┐
│ View（Activity / Fragment / Adapter）        │
│ · 只做三件事：转发交互、渲染列表、订阅事件     │
│ · 不持有 Repository，不写数据库               │
└───────────────┬─────────────────────────────┘
                │ 接口：XxxContract.View
┌───────────────▼─────────────────────────────┐
│ Presenter                                    │
│ · 持有 View 接口 + Repository 接口            │
│ · 承载状态（当前关键词、当前市场、上次查询）    │
│ · 调度器可注入，便于测试                      │
└───────────────┬─────────────────────────────┘
                │ 接口：ShareRepository
┌───────────────▼─────────────────────────────┐
│ Data                                         │
│ LitePalShareRepository /（测试）FakeRepository│
└─────────────────────────────────────────────┘
```

`BaseActivity` 负责固定编排顺序，子类只填空：

```kotlin
onCreate()
  ├─ binding = setBinding()      // 子类提供 ViewBinding
  ├─ setContentView(binding.root)
  └─ init()
       ├─ initView()             // 子类在这里创建 Presenter 与 Adapter
       ├─ presenter.start()      // Presenter 首次拉数据
       ├─ initData()             // 静态数据、初始选中态
       └─ initListener()         // 事件绑定
```

顺序很关键：`presenter.start()` 会立刻回调 `view.showShares(...)`，
所以 Adapter 必须在 `initView()` 里就绪。早期版本把 Adapter 放在 `initData()`，
依靠 `start()` 是空实现才没有崩溃——这是典型的「顺序耦合」，已在重构中消除。

---

## 3. 一次搜索请求的完整链路

```
用户输入
   │
   ▼
SearchView.onQueryTextChange
   │  presenter.onKeywordChanged(text)
   ▼
TestPresenter.queryStream.onNext(Query(keyword, type))
   │
   ├─ debounce(800ms)           ← 计时器用注入的 timerScheduler
   ├─ observeOn(uiScheduler)    ← 回调切主线程
   ├─ filter(keyword != last)   ← 相同关键词不重复查
   ▼
repository.search(type, keyword)
   │  LitePalShareRepository → LitePal.where(...).find()
   ▼
view.showShares(list, fromHistory = false)
   │
   ▼
adapter.submitList(list) → RecyclerView 渲染
```

**为什么把防抖放在 Presenter 而不是 Activity？**
因为它是业务规则（「用户停顿 800ms 才认为输入完成」），不是视图细节。
放在 Presenter 后，可以用 `TestScheduler` 精确推进时间来做断言，
不需要 Robolectric，也不需要真机。
`TestPresenterTest` 里 4 个用例专门锁这段逻辑。

**为什么用注入 Scheduler 而不是 mock 静态方法？**
`AndroidSchedulers.mainThread()` 是静态单例，测试里没法替换。
把调度器作为构造参数传入，业务代码默认用真调度器，测试传 `TestScheduler`，
两边共用同一份逻辑，不需要 `if (isTest)` 之类的分支。

---

## 4. 事件总线（RxBus）

### 结构

```
post(event)
   │
   ▼
PublishRelay<Any>（支持背压语义，线程安全）
   │
   ├─ ofType(EventA) → observeOn(threadMode) → invoke(subscriber, event)
   └─ ofType(EventB) → observeOn(threadMode) → invoke(subscriber, event)
```

### 关键设计

| 决策 | 原因 |
| --- | --- |
| 以**实例**为注册单位 | 原实现用 `Class` + `newInstance()`，事件永远到不了真实对象。这是最核心的修复。 |
| `WeakReference` 持有订阅者 | Disposable 的闭包若强引用 subscriber，会形成 `WeakHashMap.value → key` 的强引用环，弱引用失效。所以闭包里只持有 `WeakReference`。 |
| 反射结果按 Class 缓存 | 每次 `register` 都扫一遍 `declaredMethods` 没有必要，且页面频繁创建时开销可见。 |
| 沿继承链扫描 | 原注释写着「and its super classes」，实现却只扫当前类。现在补上了。 |
| `ThreadMode → Scheduler` 在 RxBus 内解析 | 便于测试注入主线程替代品（如 `Schedulers.trampoline()`）。 |

### 与 EventBus 的关系

重构后项目内**只保留 RxBus**，EventBus 依赖已移除。
选择自研而不是直接用 EventBus 的原因：需要在派发链路上做更细的控制（按事件类型分流、
自定义线程模式），同时也是对「注解 + 反射 + Rx 调度」的一次完整实践。
代价是要自己处理弱引用、缓存和异常——这些成本在设计时是被明确接受的。

---

## 5. 错误处理

网络层有两条订阅路径：

- `RxObserver<T>`：区分 HTTP 异常、解析异常、网络异常、超时，统一翻译成用户文案。
- `RxSimpleObserver<T>`：只看业务码，适合返回结构统一的接口。

异常到文案的映射收敛在 `Throwable.toUserMessage(context)`，
业务错误码到文案的映射收敛在 `ErrorCodes`。
早期实现里这两处各有 30 行重复的 `when`，且 `RxSimpleObserver` 用
「整个响应体字符串 == "-1"」来判断错误——永远不成立，提示分支是死代码。

---

## 6. 测试策略

原则：**只测能稳定测的东西，不为了覆盖率写测试**。

| 层次 | 手段 | 是否覆盖 |
| --- | --- | --- |
| 事件总线 / 错误码 / 金额计算 / 时间工具 | 纯 JVM + JUnit4 | ✅ 全量 |
| Presenter 交互逻辑 | 注入 `TestScheduler` + 内存 `FakeRepository` | ✅ 核心路径 |
| Repository（LitePal） | 需要 Android 运行时 | ❌ 未覆盖，靠集成测试 |
| Activity / Adapter 渲染 | 需要 Robolectric 或仪器测试 | ❌ 未覆盖 |
| 相机 / 蓝牙 | 需要硬件 | ❌ 未覆盖 |

未覆盖的部分不是「不知道要测」，而是「当前依赖下性价比不高」。
若要继续补：Repository 层引入 Robolectric，UI 层引入 Compose Test 或 Espresso。

---

## 7. 已知技术债

按「值得修但本次不做」排序：

1. **targetSdk 32**：需升级 AGP 8.x + Gradle 8.x + JDK 17 整条链路，并处理 `statusBarColor` 等 API 变更。
2. **RxJava2 已停止新特性**：迁移到协程 + Flow 是更大的重构，涉及 Presenter 全量改造。
3. **LitePal 同步查询在主线程**：数据量小时无感，量大需要切线程或换 Room。
4. **`sign` 模块后台轮询**：Android 8+ 需前台服务或 WorkManager。
5. **UI 层缺少空态/错误态的统一封装**：目前分散在各 Activity 里。
6. **没有依赖版本目录（version catalog）**：版本仍集中在根 `build.gradle` 的 `ext` 块。

---

## 8. 如果要继续演进

```
现在                          下一步
─────────────────────────────────────────────────────────
MVP + Lifecycle        →      MVVM + StateFlow + 单向数据流
手写 Repository        →      Repository + UseCase 分层
LitePal 同步查询       →      Room + Flow，或协程 IO
JUnit4 + TestScheduler →      + Turbine（Flow 测试）+ Robolectric
根 ext 版本管理        →      libs.versions.toml
compileSdk 32          →      compileSdk 35（配 AGP 8.x）
```
