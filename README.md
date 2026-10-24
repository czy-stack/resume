# Resume · Android 工程实践样例

[![Android CI](https://github.com/czy-stack/resume/actions/workflows/android.yml/badge.svg)](https://github.com/czy-stack/resume/actions/workflows/android.yml)

一个用于面试展示的 Android 多模块工程：覆盖模块化拆分、MVP 分层、自研注解式事件总线、
RxJava 数据流、本地数据缓存，以及相机 / 扫码等系统能力接入。

> **关于本仓库的边界**：为保护原项目业务，实际业务处理模块已被移除，保留的是架构骨架、
> 核心实现与工程配置。因此部分页面是「可编译、可跳转、但不承载完整业务」的骨架。
> 这一点在[已知限制](#已知限制)里有明确说明，不做夸大。

---

## 快速开始

| 依赖 | 版本 | 说明 |
| --- | --- | --- |
| JDK | 17 | AGP 7.4.1 要求 JDK 11+，推荐 17 |
| Gradle | 7.5 | 由 `gradlew` 自带 |
| AGP / Kotlin | 7.4.1 / 1.8.10 | 见根 `build.gradle` 的 plugins 块 |
| Android SDK | compileSdk 32 | 需安装 `platforms;android-32` |
| Build-Tools | 30.0.3 | AGP 7.4.1 的默认版本 |
| minSdk | 28 | Android 9.0 |

```bash
# 1. 指定本地 SDK（不要提交到仓库）
echo "sdk.dir=/your/path/to/Android/Sdk" > local.properties

# 2. 打包调试包（flavor 见下方说明，当前可用 kotlin / sign / sudo / jetpack / camera / ocr）
./gradlew :app:assembleKotlinProductionDebug

# 3. 跑单元测试（38 个用例，纯 JVM，无需真机）
./gradlew test

# 4. 静态检查
./gradlew :app:lintKotlinProductionDebug
```

产物位于 `app/build/outputs/apk/<flavor><Environment>/debug/`。

> 若提示缺少 `platforms;android-32` 或 `build-tools;30.0.3`，用 Android Studio 的 SDK Manager
> 或 `sdkmanager` 补齐即可。

### 一个壳，多个 App

`app` 模块通过 `flavorDimensions "app","environment"` 打成多个独立应用，
`SplashActivity` 按 `BuildConfig.FLAVOR_app` 跳到对应模块的入口：

| flavor | 入口 Activity | 来源模块 |
| --- | --- | --- |
| `kotlin` | `TestActivity`（行情搜索） | `kotlin` |
| `sign` | `SignActivity`（定时签到） | `sign` |
| `jetpack` | `MainActivity`（Compose/Room 单词本） | `jetpack` |
| `sudo` | `ActiveGameActivity`（数独） | `sudo` |
| `camera` | `CameraActivity`（拍照 + BLE） | `kotlin` |
| `ocr` | 预留 | — |

---

## 模块结构

```
┌───────────────────────────────────────────────────────────┐
│ app（application 壳）                                     │
│   SplashActivity ──► 按 flavor 分发到各子应用入口           │
└──┬──────────┬──────────┬──────────┬──────────┬────────────┘
   │          │          │          │          │
┌──▼───────┐┌─▼───────┐┌─▼───────┐┌─▼──────┐┌──▼──────────┐
│ kotlin   ││ sign    ││ jetpack ││ sudo   ││ java        │
│ 行情搜索 ││ 定时签到 ││ Compose ││ 数独   ││ Java 对照版 │
│ 相机/BLE ││         ││ + Room  ││        ││             │
└──┬───────┘└─┬───────┘└─┬───────┘└─┬──────┘└──┬──────────┘
   │          │          │          │          │
┌──▼──────────▼──────────▼──────────▼──────────▼──────────┐
│ common（library）                                        │
│  BaseActivity / BaseFragment / BaseAdapter               │
│  RxBus（注解式事件总线）· RxObserver · ApiHttp            │
│  工具：Log / Date / Number / View / Image / Uri           │
└──────────────────────────────────────────────────────────┘
```

依赖方向严格单向：`app → {kotlin, sign, jetpack, sudo, java} → common`，
`common` 不反向依赖任何业务模块。

### 各模块职责

- **common**：与业务无关的基础设施。基类、网络层、事件总线、通用工具。
- **kotlin**：行情搜索主流程（输入防抖 → 本地检索 → 列表渲染）、系统相机、BLE 设备列表。
- **sign**：定时唤起目标 App 的设置页与轮询服务。
- **jetpack**：Compose + Room 的单词本示例，`MyApplication` 继承其 `WordsApplication`。
- **sudo**：数独游戏。
- **java**：与 `kotlin` 结构平行的 Java 实现，用来对比两种语言在同类场景下的写法差异。
- **app**：壳工程，负责启动流程、flavor 分发与应用配置。

---

## 技术选型与取舍

选型不是「用了什么」，而是「为什么用它、代价是什么」。

| 主题 | 选择 | 取舍说明 |
| --- | --- | --- |
| 架构 | MVP + Lifecycle | 原项目历史包袱决定的。如今新项目会用 MVVM + Flow；这里保留 MVP 是为了展示对分层的理解，同时把 Presenter 做成可注入依赖、可单测的形态。 |
| 事件总线 | 自研 `RxBus` | 想练手「注解 + 反射 + RxJava 调度」这套组合。**第一版是错的**（`newInstance()` 派发），详见[事件总线](#事件总线rxbus)。已修复并接入真实场景。 |
| 本地存储 | LitePal | 声明式 ORM，建表零配置，适合「自选列表」这种弱结构化数据。代价是同步查询、社区活跃度下降；数据量上来会换 Room。 |
| 网络 | Retrofit + RxJava2 | 与原项目保持一致。RxJava2 现在偏老，新项目会用 Retrofit + 协程。 |
| 视图绑定 | ViewBinding | 替代已废弃的 `kotlin-android-extensions`，基类统一封装，子类只实现 `setBinding()`。 |
| 依赖可见性 | `api` 全量导出（现状） | `common` 把三方库都用 `api` 导出，各模块无需重复声明；代价是依赖图名义单向、实际耦合，改库版本会波及全仓。收紧为 `implementation` 需要逐个模块补齐声明，列入待办。 |

---

## 事件总线（RxBus）

`common/event/RxBus.kt` 是一个模仿 EventBus 用法的轻量实现，核心只有三件事：
**反射扫描 `@Subscribe` 方法 → 按事件类型过滤 → 按 `ThreadMode` 切线程**。

它在第一版里有三个致命问题，现在已全部修复，这也是我最愿意被追问的部分：

| 问题 | 现象 | 修复 |
| --- | --- | --- |
| 派发对象错误 | `register(targetClass: Class<*>)` 只能拿到 Class，派发时用 `targetClass.newInstance()` 创建**新实例**，事件永远到不了真正的订阅者 | 改为 `register(subscriber: Any)`，持有实例引用 |
| 无法精确反注册 | 只按 Class 记录，同类多实例互相覆盖 | 以实例为 key，支持同类多实例并存 |
| 潜在内存泄漏 | Disposable 强引用订阅者 | `WeakReference` 持有订阅者，Disposable 侧不做强引用；配合 `unregister` 双保险 |

另外补上了：继承链扫描（父类的 `@Subscribe` 也能被发现）、反射结果缓存、
同一实例重复注册自动去重、派发异常带上「类名#方法名」上下文。

```kotlin
class TestActivity : BaseActivity<...>(), TestContract.View {

    @Subscribe
    fun onEvent(event: Event) {
        if (event.type == EventConstants.FUTURES_TAB) {
            showToastView("已选择 ${event.message}")
        }
    }

    override fun onDestroy() {
        rxUnregister()
        super.onDestroy()
    }
}
```

---

## 分层落地：一次具体的重构

原实现里，列表点击直接写数据库、直接发事件：

```kotlin
// 重构前：Adapter 跨越 Presenter 直接操作数据层
holder.itemView.setOnClickListener {
    DBManager.save(item)
    Event(type = EventConstants.FUTURES_TAB, message = item.name).post()
}
```

```kotlin
// 重构后：Adapter 只回传意图，写库与广播由 Presenter 负责
class SearchAdapter(private val onItemClick: (ShareData) -> Unit) : BaseAdapter<...>()

// Presenter 依赖接口而非具体实现，测试时可注入内存 Fake
class TestPresenter(
    private val view: TestContract.View,
    lifecycleOwner: LifecycleOwner? = null,
    private val repository: ShareRepository = LitePalShareRepository(),
    private val timerScheduler: Scheduler = Schedulers.computation(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val debounceMillis: Long = DEFAULT_DEBOUNCE_MILLIS
) : LifecyclePresenter(lifecycleOwner), TestContract.Presenter
```

收益：搜索防抖、关键词去重、市场切换这些**真正有价值的交互逻辑**第一次变得可测试。

---

## 测试

```bash
./gradlew test
```

38 个用例，全部为纯 JVM 测试（无 Robolectric、无真机），约 0.3 秒跑完：

| 测试类 | 用例数 | 覆盖内容 |
| --- | --- | --- |
| `RxBusTest` | 9 | 事件派发到真实实例、反注册、同类多实例隔离、父类订阅、重复注册去重、无订阅方法时报错 |
| `TestPresenterTest` | 12 | 输入防抖、相同关键词去重、提交立即查询、市场切换重查、历史倒序、删除/清空刷新 |
| `ErrorCodesTest` | 5 | 30 个业务码全覆盖、重复定义检查、未知码返回 null |
| `NumberExtTest` | 6 | 浮点尾差、银行家舍入、除零异常 |
| `DateUtilsTest` | 6 | 共享 Calendar 缺陷回归、时间格式、工作日判定 |

关键手法：**把调度器作为构造参数注入**，测试传 `TestScheduler` 精确控制时间，
业务代码仍然默认使用 Android 主线程 / computation，不需要任何 BuildConfig 分支。

---

## 工程化

- **CI**：`.github/workflows/android.yml`，PR 与主干推送触发 `assembleDebug + test + lint`，失败即红。
- **混淆**：`app/proguard-rules.pro` 针对 LitePal / Gson / RxBus 的反射依赖补了 keep 规则。
  未加规则时，R8 会把 `ShareData.symbol` 重命名为 `g`，导致 LitePal 列名错乱、Gson 反序列化全空。
- **构建变体**：`app` 维度（resume / resumetest / ocr / camera）× `environment` 维度（production / envtest），
  APK 命名带版本、渠道与时间戳。

---

## 已知限制

诚实清单，避免误读：

1. **业务逻辑被剥离**：搜索只查本地缓存，`ApiClient` 保留了真实请求地址但未接入完整链路。
2. **release 未配置正式签名**：产物为 unsigned，演示可用 debug 包；发布前需补 `signingConfigs`。
3. **`sign` 模块的轮询服务未做前台服务改造**：Android 8+ 后台 Service 会被系统限制，
   生产环境应改用 `WorkManager` 或 `AlarmManager`。
4. **未升级到 targetSdk 35**：当前 `compileSdk 32`（AGP 7.4.1 / Gradle 7.5），
   升到 35 需要动 AGP 8.x + Gradle 8.x 整条链路，属于独立任务。
5. **`common` 的依赖仍用 `api` 全量导出**：为了让各模块免于重复声明版本，
   代价是耦合偏松；收紧为 `implementation` 是明确的待办。
6. **测试只覆盖纯逻辑**：事件总线、Presenter 交互、错误码映射、金额与时间工具已有 38 个用例；
   `LitePalShareRepository` 与 Activity/Adapter 渲染层尚未覆盖（需要 Robolectric 或仪器测试）。
7. **UI 未做视觉打磨**：重点在工程结构与逻辑正确性，界面沿用早期样式。
