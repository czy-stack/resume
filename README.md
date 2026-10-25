# Resume · Android 多模块工程实践

[![Android CI](https://github.com/czy-stack/resume/actions/workflows/android.yml/badge.svg)](https://github.com/czy-stack/resume/actions/workflows/android.yml)

一个 Android 多模块工程示例，覆盖**模块化拆分、MVP 分层、注解式事件总线、RxJava 数据流、
Compose + Room、系统能力接入（相机 / BLE）**，并配套完整的构建约束与单元测试。

工程本身也是一个「一个壳打多个独立 App」的实践：`app` 通过两个 flavor 维度
组合出多个可独立安装的应用，共用同一套基础设施。

---

## 快速开始

| 依赖 | 版本 | 说明 |
| --- | --- | --- |
| JDK | 17 | AGP 8.6 要求 JDK 17 |
| Gradle | 8.7 | 由 `gradlew` 自带，走腾讯镜像 |
| AGP / Kotlin | 8.6.1 / 2.0.21 | 版本集中在 `gradle/libs.versions.toml` |
| Android SDK | compileSdk / targetSdk 35 | 需安装 `platforms;android-35` |
| Build-Tools | 35.0.0 | |
| minSdk | 28 | Android 9.0 |

```bash
# 1. 指定本地 SDK（不要提交到仓库）
echo "sdk.dir=/your/path/to/Android/Sdk" > local.properties

# 2. 打包调试包
./gradlew :app:assembleKotlinProductionDebug

# 3. 单元测试（40 个用例，纯 JVM，无需真机）
./gradlew test

# 4. 静态检查
./gradlew :app:lintKotlinProductionDebug
```

产物位于 `app/build/outputs/apk/<flavor><Environment>/debug/`，
文件名格式为 `LitePal_<buildType>_<yyyyMMddHH>.apk`。

### 一个壳，多个 App

`app` 模块通过 `flavorDimensions("app", "environment")` 打成多个独立应用，
`SplashActivity` 按 `BuildConfig.FLAVOR_app` 分发到对应模块入口：

| flavor | 入口 Activity | 来源模块 | 功能 |
| --- | --- | --- | --- |
| `kotlin` | `TestActivity` | `kotlin` | 行情搜索、历史记录 |
| `sign` | `SignActivity` | `sign` | 定时唤起目标应用设置页 |
| `jetpack` | `MainActivity` | `jetpack` | Compose + Room 单词本 |
| `sudo` | `ActiveGameActivity` | `sudo` | 数独（Compose） |
| `camera` | `CameraActivity` | `kotlin` | 拍照 + BLE 设备扫描 |
| `ocr` | — | — | 预留位，用于演示新增 flavor 的成本 |

`environment` 维度（`production` / `envtest`）负责切换 `BASE_URL`、`API_HOST` 等构建常量，
不同环境可同时安装在同一台设备上。

---

## 模块结构

```
┌───────────────────────────────────────────────────────────┐
│ app（application 壳）                                      │
│   SplashActivity ──► 按 flavor 分发到各子应用入口            │
└──┬──────────┬──────────┬──────────┬──────────┬────────────┘
   │          │          │          │          │
┌──▼───────┐┌─▼───────┐┌─▼───────┐┌─▼──────┐┌──▼──────────┐
│ kotlin   ││ sign    ││ jetpack ││ sudo   ││ java        │
│ 行情搜索 ││ 定时签到 ││ Compose ││ 数独   ││ Java 实现版 │
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
- **sign**：定时唤起目标 App 的设置页。
- **jetpack**：Compose + Room 的单词本，演示声明式 UI 与持久层的组合。
- **sudo**：数独游戏，Compose 状态驱动的典型场景。
- **java**：与 `kotlin` 平行的 Java 实现，用于对比两种语言在同类场景下的写法。
- **app**：壳工程，负责启动流程、flavor 分发与应用配置。

---

## 技术栈

| 主题 | 选择 | 说明 |
| --- | --- | --- |
| 架构 | MVP + Lifecycle | Presenter 通过构造参数注入 View、Repository 与 Scheduler，可直接单测；生命周期由基类统一托管 |
| 事件总线 | 自研 `RxBus` | 注解 + 反射 + RxJava 调度，见[下文](#事件总线rxbus) |
| 视图绑定 | ViewBinding | 替代 `kotlin-android-extensions`，基类统一封装，子类只实现 `setBinding()` |
| 本地存储 | LitePal / Room | `common` 侧用 LitePal 处理弱结构化的自选列表；`jetpack` 侧用 Room 演示编译期校验 |
| 网络 | Retrofit + RxJava2 | 统一错误码映射与线程调度封装在 `RxObserver` |
| UI | View 体系 + Compose | 存量页面用 View + ViewBinding；新页面用 Compose，两套体系在同一工程内共存 |
| 构建 | Kotlin DSL + Version Catalog | 全部脚本为 `.gradle.kts`，版本集中在 `libs.versions.toml` |

---

## 工程化实践

**版本集中管理。** 所有依赖、插件、SDK 版本收敛到 `gradle/libs.versions.toml`，
模块侧只写 `libs.xxx` 访问器。升级一个库只需改一处，且 IDE 能对未使用的条目给出提示。

```toml
[versions]
agp = "8.6.1"
kotlin = "2.0.21"
compileSdk = "35"

[libraries]
androidx-appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }
```

**构建约束。** 每个模块显式声明 `namespace`；`buildConfig` 按需开启（AGP 8 起默认关闭）；
Java 与 Kotlin 的 JVM target 统一为 17，避免编译器校验失败。

**CI 门禁。** `.github/workflows/android.yml` 在 PR 与主干推送时执行
`assembleDebug + test + lint`，任一失败即红。

**混淆。** `app/proguard-rules.pro` 为 LitePal、Gson、RxBus 的反射依赖配置了 keep 规则，
保证实体字段名与注解在 R8 处理后保持稳定。

---

## 事件总线（RxBus）

`common/event/RxBus.kt` 是一个模仿 EventBus 用法的轻量实现，核心只有三件事：
**反射扫描 `@Subscribe` 方法 → 按事件类型过滤 → 按 `ThreadMode` 切线程**。

设计要点：

| 关注点 | 做法 |
| --- | --- |
| 订阅者识别 | `register(subscriber: Any)` 持有实例引用，以实例为 key 存储，同一 Class 的多个实例互不干扰 |
| 生命周期 | 订阅者用 `WeakReference` 持有，配合基类的 `rxUnregister()` 双重保障，避免因忘记反注册导致泄漏 |
| 方法发现 | 扫描继承链，父类声明的 `@Subscribe` 同样生效 |
| 性能 | 反射结果按 Class 缓存，重复注册自动去重 |
| 可观测性 | 派发失败时把「类名#方法名」写进异常上下文，便于定位 |

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

## 分层实践

Adapter 只回传用户意图，写库与广播由 Presenter 负责，避免视图层直接触达数据层：

```kotlin
// Adapter 只暴露回调
class SearchAdapter(private val onItemClick: (ShareData) -> Unit) : BaseAdapter<...>()

// Presenter 依赖接口而非实现，测试时可注入内存 Fake 与 TestScheduler
class TestPresenter(
    private val view: TestContract.View,
    lifecycleOwner: LifecycleOwner? = null,
    private val repository: ShareRepository = LitePalShareRepository(),
    private val timerScheduler: Scheduler = Schedulers.computation(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val debounceMillis: Long = DEFAULT_DEBOUNCE_MILLIS
) : LifecyclePresenter(lifecycleOwner), TestContract.Presenter
```

这样搜索防抖、关键词去重、市场切换这些交互逻辑都能脱离 Android 环境验证。

---

## 测试

```bash
./gradlew test
```

40 个用例，全部为纯 JVM 测试（无 Robolectric、无真机），约 0.3 秒跑完：

| 测试类 | 用例数 | 覆盖内容 |
| --- | --- | --- |
| `RxBusTest` | 9 | 事件派发到真实实例、反注册、同类多实例隔离、父类订阅、重复注册去重、无订阅方法时报错 |
| `TestPresenterTest` | 12 | 输入防抖、相同关键词去重、提交立即查询、市场切换重查、历史倒序、删除/清空刷新 |
| `ErrorCodesTest` | 5 | 业务码全覆盖、重复定义检查、未知码返回 null |
| `NumberExtTest` | 6 | 浮点尾差、银行家舍入、除零异常 |
| `DateUtilsTest` | 6 | 共享 Calendar 隔离、时间格式、工作日判定 |
| 其他模块 | 2 | 各模块的构建连通性冒烟用例 |

关键手法：**把调度器作为构造参数注入**，测试传 `TestScheduler` 精确控制时间，
业务代码仍默认使用主线程 / computation，不需要任何 `BuildConfig` 分支。

---

## 目录速览

```
resume/
├── gradle/
│   ├── libs.versions.toml        # 版本目录：全部依赖与插件的唯一来源
│   └── wrapper/                  # Gradle 8.7（all 发行版）
├── app/                          # 壳工程：启动流程、flavor 分发、打包配置
├── common/                       # 基类、网络、事件总线、工具
├── kotlin/                       # 行情搜索、相机、BLE
├── sign/                         # 定时签到
├── jetpack/                      # Compose + Room
├── sudo/                         # 数独（Compose + Proto DataStore）
├── java/                         # Java 对照实现
└── .github/workflows/android.yml # CI：assemble + test + lint
```
