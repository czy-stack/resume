plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-kapt")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.example.sudo"
    compileSdk = libs.versions.compileSdk.get().toInt()

    buildFeatures {
        viewBinding = true
        compose = true
    }

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
}

dependencies {
    // Proto DataStore
    implementation(libs.datastore)
    implementation(libs.protobuf.javalite)

    // Compose stuff
    implementation(libs.composeConstraintlayout)
    implementation(libs.composeUi)
    implementation(libs.composeActivity)
    implementation(libs.composeUiTooling)
    implementation(libs.composeMaterial)
    implementation(libs.composeMaterialIconsExtended)
    implementation(project(":common"))

    // 模板测试依赖 JUnit，原先缺失导致 testDebugUnitTest 直接编译失败
    testImplementation(libs.junit4)
}

// protobuf-gradle-plugin 0.8.12 的 Kotlin DSL 入口是顶层扩展函数，
// 必须显式 import（见文件顶部），否则 protobuf {} / protoc {} 无法解析。
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protoc.get()}"
    }

    // 为本项目中的 .proto 生成 protobuf-lite 的 Java 代码
    // 参考 https://github.com/google/protobuf-gradle-plugin#customizing-protobuf-compilation
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}
