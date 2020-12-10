package com.example.sign.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.IBinder
import com.android.common.utils.DateUtils
import com.android.common.utils.LogUtils
import com.android.common.utils.RxUtils
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能  定时开启应用
 * @创建日期  2020/12/9
 */
class SignService : Service() {
    companion object {
        var temp1: Int = 8
        var temp2: Int = 58
    }

    private val url = "com.alibaba.android.rimet"  //包名

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        RxUtils.interval(0, 30, TimeUnit.SECONDS,
            Consumer {
                val hour = DateUtils.getHour()
                val min = DateUtils.getMinute()
                LogUtils.i(msg = "$hour   $min   $temp1    $temp2   " + DateUtils.isWeek())
                if (hour == temp1 && min == temp2 && DateUtils.isWeek()) {
                    open(context = this)
                }
            })
        return super.onStartCommand(intent, flags, startId)
    }

    private fun open(packageName: String = url, context: Context) {
        val packageManager = context.packageManager
        val resolveInfo = packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN, null)
            .let {
                it.addCategory(Intent.CATEGORY_LAUNCHER)
                it.setPackage(packageManager.getPackageInfo(url, 0).packageName)
            }, 0).iterator().next()
        if (resolveInfo != null) {
            val className = resolveInfo.activityInfo.name
            Intent(Intent.ACTION_MAIN).let {
                it.addCategory(Intent.CATEGORY_LAUNCHER)
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                it.component = ComponentName(packageName, className)
                context.startActivity(it)
            }
        }
    }
}