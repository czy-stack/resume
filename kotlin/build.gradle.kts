plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
}

val extCompileSdk: Int = rootProject.extra["compileSdkVersion"] as Int
val extMinSdk: Int = rootProject.extra["minSdkVersion"] as Int
val extTargetSdk: Int = rootProject.extra["targetSdkVersion"] as Int
val extJunit: String = rootProject.extra["junit"] as String

android {
    compileSdk = extCompileSdk

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        minSdk = extMinSdk
        targetSdk = extTargetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        // 纯 JVM 单测会间接调用 android.util.Log 等桩方法，
        // 默认实现是抛 "not mocked"，这里改为返回默认值
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(project(":common"))

    testImplementation("junit:junit:$extJunit")
}
