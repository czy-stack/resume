plugins {
    id("com.android.library")
}

val extCompileSdk: Int = rootProject.extra["compileSdkVersion"] as Int
val extMinSdk: Int = rootProject.extra["minSdkVersion"] as Int
val extTargetSdk: Int = rootProject.extra["targetSdkVersion"] as Int
val extLitepal: String = rootProject.extra["litepal"] as String
val extGson: String = rootProject.extra["gson"] as String

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

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })
    implementation(project(":common"))

    // common 已收敛为 implementation，这里按实际使用显式声明
    implementation("org.litepal.android:kotlin:$extLitepal")
    implementation("com.google.code.gson:gson:$extGson")
}
