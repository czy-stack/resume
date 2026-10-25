plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

val extCompileSdk: Int = rootProject.extra["compileSdkVersion"] as Int
val extMinSdk: Int = rootProject.extra["minSdkVersion"] as Int
val extTargetSdk: Int = rootProject.extra["targetSdkVersion"] as Int
val extJvmTarget: String = rootProject.extra["jvmTarget"] as String
val extRoom: String = rootProject.extra["room"] as String
val extArchLifecycle: String = rootProject.extra["archLifecycle"] as String
val extAppcompat: String = rootProject.extra["appcompat"] as String
val extCompose: String = rootProject.extra["compose"] as String
val extAccompanist: String = rootProject.extra["accompanist"] as String
val extNavigation: String = rootProject.extra["navigation"] as String

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
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")

    // Architecture Components
    implementation("androidx.room:room-runtime:$extRoom")
    annotationProcessor("androidx.room:room-compiler:$extRoom")
    kapt("androidx.room:room-compiler:$extRoom")
    implementation("androidx.room:room-ktx:$extRoom")
    implementation("androidx.room:room-rxjava2:$extRoom")
    implementation("androidx.room:room-paging:$extRoom")
    androidTestImplementation("androidx.room:room-testing:$extRoom")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$extArchLifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$extArchLifecycle")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$extArchLifecycle")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$extArchLifecycle")

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:$extAppcompat")
    implementation("androidx.compose.material:material:$extCompose")
    implementation("androidx.compose.animation:animation:$extCompose")
    implementation("androidx.compose.ui:ui-tooling-preview:$extCompose")
    implementation("com.google.accompanist:accompanist-appcompat-theme:$extAccompanist")
    implementation("com.google.accompanist:accompanist-swiperefresh:$extAccompanist")
    implementation("androidx.navigation:navigation-fragment-ktx:$extNavigation")
    implementation("androidx.navigation:navigation-ui-ktx:$extNavigation")
    implementation("androidx.navigation:navigation-compose:$extNavigation")

    debugImplementation("androidx.compose.ui:ui-tooling:$extCompose")
    debugImplementation("androidx.compose.ui:ui-test-manifest:$extCompose")
}
