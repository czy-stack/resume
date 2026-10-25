plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
}

val extCompileSdk: Int = rootProject.extra["compileSdkVersion"] as Int
val extMinSdk: Int = rootProject.extra["minSdkVersion"] as Int
val extTargetSdk: Int = rootProject.extra["targetSdkVersion"] as Int

// gradle.properties 里的 API_DEV_HOST，等价于 Groovy 里的 "${API_DEV_HOST}"
val apiDevHost: String = providers.gradleProperty("API_DEV_HOST").get()

val extCoroutines: String = rootProject.extra["coroutines"] as String
val extAppcompat: String = rootProject.extra["appcompat"] as String
val extKtx: String = rootProject.extra["ktx"] as String
val extConstraintlayout: String = rootProject.extra["constraintlayout"] as String
val extMaterial: String = rootProject.extra["material"] as String
val extGlide: String = rootProject.extra["glide"] as String
val extRxjava: String = rootProject.extra["rxjava"] as String
val extRxandroid: String = rootProject.extra["rxandroid"] as String
val extOkhttp: String = rootProject.extra["okhttp"] as String
val extRetrofit: String = rootProject.extra["retrofit"] as String
val extRxdelay: String = rootProject.extra["rxdelay"] as String
val extAgentweb: String = rootProject.extra["agentweb"] as String
val extLitepal: String = rootProject.extra["litepal"] as String
val extEvent: String = rootProject.extra["event"] as String
val extJunit: String = rootProject.extra["junit"] as String

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
            buildConfigField("String", "API_HOST", "\"$apiDevHost\"")
        }
        debug {
            buildConfigField("String", "API_HOST", "\"$apiDevHost\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(fileTree("libs") { include("*.jar") })

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$extCoroutines")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:$extCoroutines")

    api("androidx.appcompat:appcompat:$extAppcompat")
    api("androidx.core:core-ktx:$extKtx")
    api("androidx.constraintlayout:constraintlayout:$extConstraintlayout")
    api("com.google.android.material:material:$extMaterial")
    api("androidx.annotation:annotation:$extAppcompat")

    api("com.github.bumptech.glide:glide:$extGlide")

    api("io.reactivex.rxjava2:rxjava:$extRxjava")
    api("io.reactivex.rxjava2:rxandroid:$extRxandroid")
    api("com.squareup.okhttp3:okhttp:$extOkhttp")
    api("com.squareup.okhttp3:logging-interceptor:$extOkhttp")
    api("com.squareup.retrofit2:retrofit:$extRetrofit")
    api("com.squareup.retrofit2:converter-gson:$extRetrofit")
    api("com.squareup.retrofit2:converter-scalars:$extRetrofit")
    api("com.squareup.retrofit2:retrofit-adapters:2.8.0")
    api("com.squareup.retrofit2:adapter-rxjava2:$extRetrofit")
    api("com.jakewharton.rxrelay2:rxrelay:$extRxdelay")
    api("com.just.agentweb:agentweb:$extAgentweb")

    api("org.litepal.android:kotlin:$extLitepal")
    api("org.greenrobot:eventbus:$extEvent")

    testImplementation("junit:junit:$extJunit")
}
