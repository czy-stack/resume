package com.android.common.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import kotlin.system.exitProcess

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class CrashHandler private constructor() : Thread.UncaughtExceptionHandler {
    companion object {
        val instance: CrashHandler by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            CrashHandler()
        }
        const val CRASH_FILE_FOLDER = "crash_files"
    }

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        e?.let {
            it.printStackTrace()
            handleException(it)
            Thread.sleep(10)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10)
        }
    }

    private fun handleException(exception: Throwable) {
        saveFile(getCrashReport(context, exception))
    }

    private fun saveFile(crashReport: String): File {
        val fileName =
            StringBuilder("crash-").append(System.currentTimeMillis()).append(".txt").toString()
        val dir = File(context.cacheDir.absolutePath + File.separator + CRASH_FILE_FOLDER)
        if (!dir.exists())
            dir.mkdirs()

        val file = File(dir, fileName)
        val fos = FileOutputStream(file)
        fos.write(crashReport.toByteArray())
        fos.close()
        return file
    }

    private fun getCrashReport(context: Context, throwable: Throwable): String {
        getPackageInfo(context).let {
            return StringBuilder().apply {
                append("Version: ").append(it.versionName).append("(")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    append(it.longVersionCode)
                }
                append(")\n")
                    .append("Android: ").append(Build.VERSION.RELEASE).append("(")
                    .append(Build.MODEL).append(")\n")
                append("Exception: ").append(throwable.message).append(")\n")
                for (item in throwable.stackTrace) {
                    append(item).append("\n")
                }
            }.toString()
        }
    }

    private fun getPackageInfo(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(
            context.packageName, 0
        )
    }
}