plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

// gradle.properties 里的 API_DEV_HOST，等价于原 Groovy 的 "${API_DEV_HOST}"
val apiDevHost: String = providers.gradleProperty("API_DEV_HOST").get()

android {
    namespace = "com.android.common"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        viewBinding = true
        // AGP 8 起 BuildConfig 默认不生成，buildConfigField 需要显式开启
        buildConfig = true
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            buildConfigField("String", "API_HOST", "\"$apiDevHost\"")
        }
        debug {
            buildConfigField("String", "API_HOST", "\"$apiDevHost\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)

    api(libs.androidx.appcompat)
    api(libs.androidx.core.ktx)
    api(libs.androidx.constraintlayout)
    api(libs.material)
    api(libs.androidx.annotation)

    api(libs.glide)

    api(libs.rxjava)
    api(libs.rxandroid)
    api(libs.okhttp3)
    api(libs.okhttp.logging)
    api(libs.retrofit2)
    api(libs.retrofit.converter.gson)
    api(libs.retrofit.converter.scalars)
    api(libs.retrofit.adapter.rxjava2)
    api(libs.rxrelay)
    api(libs.agentweb)

    api(libs.litepal)
    api(libs.eventbus)

    testImplementation(libs.junit4)
}
