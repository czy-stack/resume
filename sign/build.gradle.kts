plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
}

val extCompileSdk: Int = rootProject.extra["compileSdkVersion"] as Int
val extMinSdk: Int = rootProject.extra["minSdkVersion"] as Int
val extTargetSdk: Int = rootProject.extra["targetSdkVersion"] as Int
val extJvmTarget: String = rootProject.extra["jvmTarget"] as String

android {
    compileSdk = extCompileSdk

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        minSdk = extMinSdk
        targetSdk = extTargetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = extJvmTarget
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(project(":common"))
}
