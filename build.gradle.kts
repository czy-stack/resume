plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    // protobuf 插件必须在根项目声明版本，子模块才能生成 protobuf {} 的类型安全访问器
    id("com.google.protobuf") version "0.8.12" apply false
}

// ---------------------------------------------------------------------------
// SDK / 构建常量
// 用 `by extra(...)` 声明，模块侧通过 `val x: T by rootProject.extra` 读取，
// 这样保留了原 Groovy 版 `rootProject.ext.xxx` 的语义，且类型安全。
// ---------------------------------------------------------------------------
val compileSdkVersion: Int by extra(32)
val versionCode: Int by extra(1)
val versionName: String by extra("1.0.0")
val minSdkVersion: Int by extra(28)
val targetSdkVersion: Int by extra(32)
val jvmTarget: String by extra("1.8")

// ---------------------------------------------------------------------------
// Android 依赖版本
// ---------------------------------------------------------------------------
val appcompat: String by extra("1.4.0")
val ktx: String by extra("1.7.0")
val constraintlayout: String by extra("2.1.3")
val material: String by extra("1.5.0")
val support: String by extra("1.0.0")
val coroutines: String by extra("1.6.1")
val accompanist: String by extra("0.23.0")
val archLifecycle: String by extra("2.4.1")
val compose: String by extra("1.2.1")
val room: String by extra("2.4.3")
val navigation: String by extra("2.4.2")

// ---------------------------------------------------------------------------
// 测试
// ---------------------------------------------------------------------------
val junit: String by extra("4.13.1")
val runner: String by extra("1.3.0")
val espresso: String by extra("3.3.0")

// ---------------------------------------------------------------------------
// RxJava  https://github.com/ReactiveX/RxJava  https://github.com/ReactiveX/RxAndroid
// ---------------------------------------------------------------------------
val rxjava: String by extra("2.2.10")
val rxandroid: String by extra("2.1.1")
val rxdelay: String by extra("2.1.1")

// ---------------------------------------------------------------------------
// OkHttp & Retrofit
// ---------------------------------------------------------------------------
val retrofit: String by extra("2.9.0")
val okhttp: String by extra("4.2.2")
// gson：java 模块的 Record 需要 @SerializedName，显式声明版本
val gson: String by extra("2.9.0")

// 轻量级 WebView
val agentweb: String by extra("4.1.3")
// Glide  https://github.com/bumptech/glide
val glide: String by extra("4.10.0")
// 数据库框架  https://github.com/LitePalFramework/LitePal
val litepal: String by extra("3.0.0")
// LeakCanary
val leakcanary: String by extra("leakcanary-android:2.7")
// 热修复
val hotfix: String by extra("3.2.8")
// EventBus
val event: String by extra("3.1.1")
// SmartRefreshLayout
val refresh: String by extra("2.0.0-alpha-1")
// Socket  https://github.com/Zvirtuey/AndroidSocket
val socket: String by extra("1.0.6")
