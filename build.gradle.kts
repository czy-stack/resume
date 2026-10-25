plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.protobuf) apply false
}

// 版本已全部收敛到 gradle/libs.versions.toml，此处不再维护 ext 常量。
