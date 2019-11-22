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
 * Created by mc on 2019/1/21.
 */

public class CrashHandlerTemp implements Thread.UncaughtExceptionHandler {
    /** 崩溃日志存储*/
    public static final String CRASH_REPORT_FILE_STORE_KEY = "CRASH_REPORT_FILE_STORE_KEY";
    /** 崩溃文件存储文件夹*/
    public static final String CREASH_FILE_FOLDER = "RESUME";

    /** Debug Log Tag */
//    public static final String TAG = "CrashHandler";
    /** 是否开启日志输出, 在Debug状态下开启, 在Release状态下关闭以提升程序性能 */
    public static final boolean DEBUG = true;
//    public static final String VERSION_NAME = "versionName";
//    public static final String VERSION_CODE = "versionCode";
//    public static final String STACK_TRACE = "STACK_TRACE";
    /** 错误报告文件的扩展名 */
//    public static final String CRASH_REPORTER_EXTENSION = ".cr";

    /** CrashHandler实例 */
    private static CrashHandlerTemp instance;
    /** 程序的Context对象 */
    private Context mContext;
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** 正在执行中标识 */
    private boolean processing;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandlerTemp() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public synchronized static CrashHandlerTemp getInstance() {
        if (instance == null) {
            instance = new CrashHandlerTemp();
        }
        return instance;
    }

    /**
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param ctx
     */
    public void init(Context ctx) {
        processing = false;
        mContext = ctx;
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
     * @param exception
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
        save2File(crashReport);
        return true;
    }

    private File save2File(String crashReport) {
        // TODO Auto-generated method stub
//        PreferencesUtils.putString(mContext,CRASH_REPORT_FILE_STORE_KEY,crashReport);
        String fileName = "crash-" + System.currentTimeMillis() + ".txt";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + CREASH_FILE_FOLDER);
                if (!dir.exists())
                    dir.mkdir();
                File file = new File(dir, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(crashReport.toString().getBytes());
                fos.close();
                return file;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取APP崩溃异常报告
     *
     * @param ex
     * @return
     */
    private String getCrashReport(Context context, Throwable ex) {
        PackageInfo pinfo = getPackageInfo(context);
        StringBuffer exceptionStr = new StringBuffer();
        exceptionStr.append("Version: " + pinfo.versionName + "("
                + pinfo.versionCode + ")\n");
        exceptionStr.append("Android: " + android.os.Build.VERSION.RELEASE
                + "(" + android.os.Build.MODEL + ")\n");
        exceptionStr.append("Exception: " + ex.getMessage() + "\n");
        StackTraceElement[] elements = ex.getStackTrace();
        for (int i = 0; i < elements.length; i++) {
            exceptionStr.append(elements[i].toString() + "\n");
        }
        return exceptionStr.toString();
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    private PackageInfo getPackageInfo(Context context) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }
}
