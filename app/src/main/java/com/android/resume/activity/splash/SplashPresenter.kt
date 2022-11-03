package com.android.resume.activity.splash

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import com.android.common.http.RxSimpleObserver
import com.android.common.lifecycle.LifecyclePresenter
import com.android.common.utils.RxUtils
import com.android.resume.http.ApiClient
import com.example.jetpack.MainActivity
import com.example.jetpack.ui.activity.word.WordActivity
import com.example.kotlin.activity.pqpo.CameraTestActivity
import com.example.kotlin.activity.test.TestActivity
import com.example.kotlin.bean.Record
import com.example.sudo.ui.activegame.ActiveGameActivity
import org.litepal.LitePal
import java.util.concurrent.TimeUnit


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class SplashPresenter(
    private val context: Context,
    private val view: SplashContract.View,
    lifecycleOwner: LifecycleOwner
) : LifecyclePresenter(
    lifecycleOwner
), SplashContract.Presenter {
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
            RxUtils.timer(1, TimeUnit.SECONDS
            ) {
                context.startActivity(Intent(context, TestActivity::class.java))
                view.finish()
            }
        )
    }

    override fun start() {
        context.startActivity(Intent(context, MainActivity::class.java))
        view.finish()
    }

}