package com.android.resume.application

import android.app.Activity
import android.app.Application
import android.content.MutableContextWrapper
import android.os.Bundle
import android.webkit.WebView
import com.android.common.utils.CrashHandlerTemp
import com.android.resume.BuildConfig
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.taobao.sophix.SophixManager
import io.reactivex.plugins.RxJavaPlugins
import org.litepal.LitePal

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        /*WebView创建初始化
首次初始化WebView会比第二次初始化慢很多。初始化后，即使WebView已释放，但一些多WebView共用的全局服务/资源对想仍未释放，而第二次初始化不需要生成，因此初始化变快。*/
        WebView(MutableContextWrapper(this))
        //LitePal初始化
        LitePal.initialize(this)

        registerActivityLifecycleCallbacks(this)

//        SophixManager.getInstance().queryAndLoadNewPatch()

        if (BuildConfig.DEBUG) {
            RxJavaPlugins.setErrorHandler {
            }
            //CrashHandler.instance.init(this)
            CrashHandlerTemp.getInstance().init(this)
        }
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            //            layout.setPrimaryColorsId(
//                R.color.colorLess, R.color.colorMore
//            )
            return@setDefaultRefreshHeaderCreator MaterialHeader(context).apply {
                //                setColorSchemeResources(R.color.colorAccent)
//                setProgressBackgroundColorSchemeResource(R.color.colorMore)
//                setBackgroundColor(R.color.colorLess)
            }
        }
        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
//            return@setDefaultRefreshFooterCreator ClassicsFooter(context).apply {
//                setPrimaryColorId(R.color.colorWrite)
//                setAccentColorId(R.color.colorBlack)
//            }
//        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

}