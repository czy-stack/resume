package com.android.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @作者 陈忠岳
 * @主要功能 崩溃日志生成
 * @创建日期 2022/6/27
 */

public class CrashHandlerOptic implements Thread.UncaughtExceptionHandler{
    /** 崩溃日志存储*/
    public static final String CRASH_REPORT_FILE_STORE_KEY = "CRASH_REPORT_FILE_STORE_KEY";
    /** 崩溃文件存储文件夹*/
    public static final String CRASH_FILE_FOLDER = "Crash";

//    错误报告文件的扩展名
//    public static final String CRASH_REPORTER_EXTENSION = ".cr";

    /** CrashHandler实例 */
//    private volatile static CrashHandler instance = null;
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** 程序的Context对象 */
    private Context mContext;
    /** 正在执行中标识 */
    private boolean processing;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandlerOptic() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public synchronized static CrashHandlerOptic getInstance() {
//        if (instance == null) {
//            instance = new CrashHandler();
//        }
//        return instance;
        return new CrashHandlerOptic();
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     */
    public void init(Context context) {
        processing = false;
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ex.printStackTrace();
        processing = true;
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            // Sleep一会后结束程序
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
//				MHLog.e(TAG, "Error : ", e);
            }
            android.os.Process.killProcess(android.os.Process.myPid());
            processing = false;
            System.exit(10);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param exception Throwable
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable exception) {
        if (exception == null || mContext == null)
            return false;
        // 使用Toast来显示异常信息
        final String crashReport = getCrashReport(mContext, exception);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常,正在收集日志，即将退出", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();
        // 保存日志文件
        saveFile(crashReport);
        return true;
    }

    private File saveFile(String crashReport) {
        // TODO Auto-generated method stub
//        PreferencesUtils.putString(mContext,CRASH_REPORT_FILE_STORE_KEY,crashReport);
        String fileName = "crash-" + System.currentTimeMillis() + ".txt";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + CRASH_FILE_FOLDER);
                if (!dir.exists())
                    dir.mkdir();
                File file = new File(mContext.getCacheDir(), fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(crashReport.toString().getBytes());
                fos.close();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取APP崩溃异常报告
     *
     * @param exception Throwable
     * @return 崩溃异常报告
     */
    private String getCrashReport(Context context, Throwable exception) {
        PackageInfo pinfo = getPackageInfo(context);
        StringBuilder exceptionStr = new StringBuilder();
        exceptionStr.append("Version: ").append(pinfo.versionName).append("(").append(pinfo.versionCode).append(")\n");
        exceptionStr.append("Android: ").append(android.os.Build.VERSION.RELEASE).append("(").append(android.os.Build.MODEL).append(")\n");
        exceptionStr.append("Exception: ").append(exception.getMessage()).append("\n");
        StackTraceElement[] elements = exception.getStackTrace();
        for (StackTraceElement element : elements) {
            exceptionStr.append(element.toString()).append("\n");
        }
        return exceptionStr.toString();
    }

    /**
     * 获取App安装包信息
     *
     * @return 安装包信息
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
}
