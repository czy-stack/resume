package com.android.common.utils

import android.content.Context
import android.os.Build
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * @作者 陈忠岳
 * @主要功能 崩溃日志落盘
 * @创建日期  2019-11-21
 *
 * 相对早期实现的修正：
 * 1. 处理完成后把异常交回系统默认 handler，保留系统/三方平台的崩溃上报能力，
 *    原实现直接 kill 进程，其它 SDK 收不到崩溃；
 * 2. 用 `handling` 标记防止处理过程中再次崩溃导致递归；
 * 3. 日志统一落在 `cacheDir/crash_files`，原 Java 版本创建了外部目录却写进 cacheDir，两者不一致；
 * 4. 用 `use {}` 保证流关闭。
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {

    companion object {
        val instance: CrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }

        /** 崩溃日志目录（位于应用私有缓存，无需存储权限） */
        const val CRASH_DIR = "crash_files"
    }

    private var appContext: Context? = null
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    @Volatile
    private var handling = false

    fun init(context: Context) {
        appContext = context.applicationContext
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            ?.takeIf { it !== this }
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        if (!handling) {
            handling = true
            runCatching { saveCrashLog(throwable) }
        }
        // 交回系统，避免抢走其它崩溃上报 SDK 的处理机会
        defaultHandler?.uncaughtException(thread, throwable)
    }

    /** 已保存的崩溃日志，按时间倒序 */
    fun crashLogs(): List<File> {
        val dir = crashDir() ?: return emptyList()
        return dir.listFiles()
            ?.filter { it.isFile }
            ?.sortedByDescending { it.lastModified() }
            ?: emptyList()
    }

    private fun saveCrashLog(throwable: Throwable) {
        val dir = crashDir() ?: return
        val name = "crash-${System.currentTimeMillis()}.txt"
        File(dir, name).outputStream().use { it.write(buildReport(throwable).toByteArray()) }
    }

    private fun crashDir(): File? {
        val context = appContext ?: return null
        return File(context.cacheDir, CRASH_DIR).apply { if (!exists()) mkdirs() }
    }

    private fun buildReport(throwable: Throwable): String = buildString {
        val context = appContext
        if (context != null) {
            val info = runCatching {
                context.packageManager.getPackageInfo(context.packageName, 0)
            }.getOrNull()
            val versionName = info?.versionName ?: "unknown"
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info?.longVersionCode ?: 0L
            } else {
                @Suppress("DEPRECATION")
                (info?.versionCode ?: 0).toLong()
            }
            append("Version: $versionName($versionCode)\n")
        }
        append("Android: ${Build.VERSION.RELEASE}(${Build.MODEL})\n")
        append("Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())}\n")
        append("Exception: ${throwable.javaClass.name}: ${throwable.message}\n")
        append(throwable.stackTraceToString())
    }
}
