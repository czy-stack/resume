package com.example.sign.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import com.android.common.utils.DateUtils
import com.android.common.utils.LogUtils
import com.android.common.utils.RxUtils
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能 定时唤起目标 App（用于每日签到）
 * @创建日期 2020/12/9
 *
 * 相对早期实现的修正：
 * 1. 轮询的 Disposable 被保存并在 onDestroy 释放，原实现启动后无法停止；
 * 2. `queryIntentActivities(...).iterator().next()` 在目标 App 未安装时会抛
 *    NoSuchElementException，改用 getLaunchIntentForPackage 并判空；
 * 3. `getPackageInfo` 会抛 NameNotFoundException，这里不再依赖它；
 * 4. 静态可变字段改成语义化命名并做范围约束（由配置页写入）。
 *
 * 已知局限：Android 8.0 起后台 Service 会被系统限制，长时间驻留需要前台服务 +
 * 通知，或改用 WorkManager / AlarmManager。演示项目保留 Service 以突出逻辑本身。
 */
class SignService : Service() {

    companion object {
        /** 目标 App 包名（钉钉） */
        private const val TARGET_PACKAGE = "com.alibaba.android.rimet"

        private const val CHECK_INTERVAL_SECOND = 30L
        private const val DEFAULT_HOUR = 8
        private const val DEFAULT_MINUTE = 58

        /** 触发时间，默认 08:58，由 SignActivity 写入 */
        @Volatile
        var hour: Int = DEFAULT_HOUR

        @Volatile
        var minute: Int = DEFAULT_MINUTE

        fun reset() {
            hour = DEFAULT_HOUR
            minute = DEFAULT_MINUTE
        }
    }

    private var pollTask: Disposable? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPolling()
        return START_STICKY
    }

    private fun startPolling() {
        pollTask?.dispose()
        pollTask = RxUtils.interval(0, CHECK_INTERVAL_SECOND, TimeUnit.SECONDS) {
            val nowHour = DateUtils.getHour()
            val nowMinute = DateUtils.getMinute()
            if (nowHour == hour && nowMinute == minute && DateUtils.isWeek()) {
                LogUtils.i(msg = "触发唤起：$nowHour:$nowMinute")
                launchTargetApp(this)
            }
        }
    }

    private fun launchTargetApp(context: Context) {
        val launchIntent = context.packageManager.getLaunchIntentForPackage(TARGET_PACKAGE)
        if (launchIntent == null) {
            LogUtils.w(msg = "$TARGET_PACKAGE 未安装，跳过本次唤起")
            return
        }
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        runCatching { context.startActivity(launchIntent) }
            .onFailure { LogUtils.e(msg = "唤起失败：${it.message}") }
    }

    override fun onDestroy() {
        pollTask?.dispose()
        pollTask = null
        super.onDestroy()
    }
}
