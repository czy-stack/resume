package com.android.resume.activity.splash

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import com.android.common.constants.Constants
import com.android.common.http.RxSimpleObserver
import com.android.common.lifecycle.LifecyclePresenter
import com.android.common.utils.LogUtils
import com.android.common.utils.RxUtils
import com.android.resume.BuildConfig
import com.example.kotlin.http.ApiClient
import com.example.jetpack.MainActivity
import com.example.kotlin.activity.camera.CameraActivity
import com.example.kotlin.activity.test.TestActivity
import com.example.kotlin.bean.Record
import com.example.sign.activity.SignActivity
import com.example.sudo.ui.activegame.ActiveGameActivity
import org.litepal.LitePal
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能 启动页：按 flavor 进入对应的子应用
 * @创建日期  2019-11-21
 *
 * 本项目是「一个壳 + 多个 flavor」的打包方式，每个 flavor 指向不同的入口 Activity。
 * start() 直接跳转；saveInfoAll() 用于先同步行情字典再跳转。
 */
class SplashPresenter(
    private val context: Context,
    private val view: SplashContract.View,
    lifecycleOwner: LifecycleOwner
) : LifecyclePresenter(lifecycleOwner), SplashContract.Presenter {

    override fun start() {
        navigateToEntry()
    }

    override fun saveInfoAll() {
        ApiClient.saveInfoAll(object : RxSimpleObserver<List<Record>>(context) {
            override fun onSuccess(resultBean: List<Record>) {
                LitePal.saveAllAsync(resultBean).listen {
                    startTimer()
                }
            }
        })
    }

    private fun startTimer() {
        addDisposable(
            RxUtils.timerUI(1, TimeUnit.SECONDS) {
                LogUtils.d(msg = "字典同步完成，进入主页面")
                navigateToEntry()
            }
        )
    }

    private fun navigateToEntry() {
        val entry: Class<out Activity> = when (BuildConfig.FLAVOR_app) {
            Constants.SIGN -> SignActivity::class.java
            Constants.JETPACK -> MainActivity::class.java
            Constants.KOTLIN -> TestActivity::class.java
            Constants.SUDO -> ActiveGameActivity::class.java
            Constants.CAMERA -> CameraActivity::class.java
            else -> TestActivity::class.java
        }
        context.startActivity(Intent(context, entry))
        view.finish()
    }
}
