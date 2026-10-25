import com.google.protobuf.gradle.builtins
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.protobuf")
}

val extCompileSdk: Int = rootProject.extra["compileSdkVersion"] as Int
val extMinSdk: Int = rootProject.extra["minSdkVersion"] as Int
val extTargetSdk: Int = rootProject.extra["targetSdkVersion"] as Int
val extJvmTarget: String = rootProject.extra["jvmTarget"] as String
val extCompose: String = rootProject.extra["compose"] as String
val extJunit: String = rootProject.extra["junit"] as String

android {
    compileSdk = extCompileSdk

    buildFeatures {
        viewBinding = true
        compose = true
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {
    // Proto DataStore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("com.google.protobuf:protobuf-javalite:3.14.0")

    // Compose stuff
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.0")
    implementation("androidx.compose.ui:ui:$extCompose")
    implementation("androidx.activity:activity-compose:1.4.0")
    implementation("androidx.compose.ui:ui-tooling:$extCompose")
    implementation("androidx.compose.material:material:$extCompose")
    implementation("androidx.compose.material:material-icons-extended:$extCompose")
    implementation(project(":common"))

    // 模板测试依赖 JUnit，原先缺失导致 testDebugUnitTest 直接编译失败
    testImplementation("junit:junit:$extJunit")
}

// protobuf-gradle-plugin 0.8.12 的 Kotlin DSL 入口是顶层扩展函数，
// 必须显式 import（见文件顶部），否则 protobuf {} / protoc {} 无法解析。
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.10.0"
    }

    // 为本项目中的 .proto 生成 protobuf-lite 的 Java 代码
    // 参考 https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                id("java") {
                    option("lite")
                }
            }
        }
    }
}
