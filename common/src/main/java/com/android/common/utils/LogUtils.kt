package com.android.common.utils

import android.util.Log
import com.android.common.BuildConfig

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
object LogUtils {
    private const val TOP_BORDER =
        "╔══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val LEFT_BORDER = "║ "
    private const val BOTTOM_BORDER =
        "╚══════════════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val TAG = "LogUtils"
    private const val CHUNK_SIZE = 106 //设置字节数

    private var debug: Boolean = BuildConfig.DEBUG//是否打印log

    fun v(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.VERBOSE)
    fun d(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.DEBUG)
    fun i(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.INFO)
    fun w(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.WARN)
    fun e(tag: String = TAG, msg: String) = debug.debugLog(tag, msg, Log.ERROR)


    private fun targetStackTraceMSg(): String {
        val targetStackTraceElement = getTargetStackTraceElement()
        return if (targetStackTraceElement != null) {
            "at ${targetStackTraceElement.className}.${targetStackTraceElement.methodName}(${targetStackTraceElement.fileName}:${targetStackTraceElement.lineNumber})"
        } else {
            ""
        }
    }

    private fun getTargetStackTraceElement(): StackTraceElement? {
        var targetStackTrace: StackTraceElement? = null
        var shouldTrace = false
        val stackTrace = Thread.currentThread().stackTrace
        for (stackTraceElement in stackTrace) {
            val isLogMethod = stackTraceElement.className == LogUtils::class.java.name
            if (shouldTrace && !isLogMethod) {
                targetStackTrace = stackTraceElement
                break
            }
            shouldTrace = isLogMethod
        }
        return targetStackTrace
    }

    private fun Boolean.debugLog(tag: String, msg: String, type: Int) {
        if (!this) {
            return
        }
        val newMsg = msgFormat(msg)
        when (type) {
            Log.VERBOSE -> Log.v(tag, newMsg)
            Log.DEBUG -> Log.d(tag, newMsg)
            Log.INFO -> Log.i(tag, newMsg)
            Log.WARN -> Log.w(tag, newMsg)
            Log.ERROR -> Log.e(tag, newMsg)
        }
    }

    private fun msgFormat(msg: String): String {
        val bytes: ByteArray = msg.toByteArray()
        val length = bytes.size
        var newMsg = "$TOP_BORDER\n$LEFT_BORDER\t${""}\n$LEFT_BORDER\t${targetStackTraceMSg()}"
        if (length > CHUNK_SIZE) {
            var i = 0
            while (i < length) {
                val count = (length - i).coerceAtMost(CHUNK_SIZE)
                val tempStr = String(bytes, i, count)
                newMsg += "\n$LEFT_BORDER\t$tempStr"
                i += CHUNK_SIZE
            }
        } else {
            newMsg += "\n$LEFT_BORDER\t$msg"
        }
        newMsg += "\n$BOTTOM_BORDER"
        return newMsg
    }
}