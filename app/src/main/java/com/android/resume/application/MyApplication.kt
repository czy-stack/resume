package com.android.resume.application

import android.content.MutableContextWrapper
import android.webkit.WebView
import com.android.common.utils.CrashHandler
import com.android.resume.BuildConfig
import com.example.jetpack.WordsApplication
import io.reactivex.plugins.RxJavaPlugins
import org.litepal.LitePal

/**
 * @作者 陈忠岳
 * @主要功能 应用入口：全局初始化
 * @创建日期  2019-11-21
 *
 * 继承 jetpack 模块的 WordsApplication，复用其协程作用域与数据库初始化。
 */
class MyApplication : WordsApplication() {

    override fun onCreate() {
        super.onCreate()

        // 首次初始化 WebView 会加载内核，耗时明显；提前在启动阶段预热，
        // 后续创建 WebView 可省去这部分开销。实例本身用完即可被回收。
        WebView(MutableContextWrapper(this))

        LitePal.initialize(this)

        if (BuildConfig.DEBUG) {
            // RxJava 的全局异常默认会走 uncaught handler 导致崩溃，这里吞掉仅打印
            RxJavaPlugins.setErrorHandler { throwable ->
                throwable.printStackTrace()
            }
            CrashHandler.instance.init(this)
        }

        // 热修复：启动时拉取补丁（需要阿里云控制台配置，演示环境未启用）
        // SophixManager.getInstance().queryAndLoadNewPatch()
    }
}
