import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        viewBinding = true
    }

    // APK 命名：原 Groovy 版本先用 apkName 拼了 versionName/flavorName，
    // 但紧接着又被 `apkName = "_release" + time + ".apk"` 覆盖，实际输出始终是
    // "LitePal_release|debug<时间>.apk"。这里按实际生效的行为实现。
    applicationVariants.all {
        outputs.all {
            val output = this as? BaseVariantOutputImpl ?: return@all
            val time = SimpleDateFormat("_YYYYMMddHH", Locale.getDefault()).format(Date())
            output.outputFileName = "LitePal_${buildType.name}$time.apk"
        }
    }

    defaultConfig {
        applicationId = "com.android.resume"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        ndk {
            abiFilters += listOf("armeabi", "armeabi-v7a", "arm64-v8a")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isZipAlignEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isZipAlignEnabled = true
        }
    }

    flavorDimensions += listOf("app", "environment")

    productFlavors {
        create("production") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"http://183.134.216.111:18003/\"")
        }
        create("envtest") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"http://183.134.216.111:18003/\"")
        }
        create("ocr") {
            dimension = "app"
            applicationId = "com.android.ocr"
        }
        create("camera") {
            dimension = "app"
            applicationId = "com.android.camera"
        }
        create("sign") {
            dimension = "app"
            applicationId = "com.android.sign"
        }
        create("sudo") {
            dimension = "app"
            applicationId = "com.android.sudo"
        }
        create("kotlin") {
            dimension = "app"
            applicationId = "com.android.kotlin"
        }
        create("jetpack") {
            dimension = "app"
            applicationId = "com.android.jetpack"
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
    implementation(project(":sign"))
    implementation(project(":kotlin"))
    implementation(project(":java"))
    implementation(project(":jetpack"))
    implementation(project(":sudo"))

    debugImplementation(libs.leakcanary)
    implementation(libs.hotfix)
}
