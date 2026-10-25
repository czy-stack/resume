import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        mavenLocal()
        maven("https://jitpack.io")
        // 原 Groovy 写法是 mavenCentral() { url "https://jcenter.bintray.com/" }，
        // 本质是把 mavenCentral 的地址替换成 jcenter。jcenter 已于 2021 年停止服务，
        // 这里保留同一个 URL 以维持原有行为不变。
        maven("https://jcenter.bintray.com/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        mavenLocal()
        maven("https://jitpack.io")
        maven("https://jcenter.bintray.com/")
    }
}

rootProject.name = "resume"

include(":app")
include(":common")
include(":sign")
include(":kotlin")
include(":java")
include(":jetpack")
include(":sudo")
