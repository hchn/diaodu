package com.jiaxun.sdk.util.exception;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.httpserver.HttpServerService;

/***
 * 
 * @author Chengang
 * 
 */
public class AppExcepitonHandler implements UncaughtExceptionHandler
{
    private static AppExcepitonHandler mInstance;

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private static Context mContext = null;
    private static String mActivityPath = null;

    /** 日期格式    */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static AppExcepitonHandler getInstance(Context context, String activityPath)
    {
        mContext = context;
        mActivityPath = activityPath;
        if (mInstance == null)
        {
            mInstance = new AppExcepitonHandler();
        }
        return mInstance;
    }

    public void init()
    {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mInstance);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex)
    {
        Intent intent = new Intent(SdkUtil.getApplicationContext(), HttpServerService.class);
        SdkUtil.getApplicationContext().stopService(intent);// 停止服务
        ex.printStackTrace();
        if (!handleException(ex))
        {
            mDefaultHandler.uncaughtException(thread, ex);
        }
        else
        {
            try
            {
                Thread.sleep(3000);
            }
            catch (InterruptedException e)
            {
            }
        }

        restartApp();
    }

    /**
     * 重启app
     */
    public void restartApp()
    {
        Intent intent = new Intent();
        // 参数1：包名，参数2：程序入口的activity
        intent.setClassName(mContext.getPackageName(), mActivityPath);
        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 500, restartIntent); // 500毫秒后重启应用

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    /**
     * 记录异常到文件
     */
    private void postReport(String message)
    {
        Looper.prepare();
        Toast.makeText(mContext, "异常日志已保存在exception.log，请提供给开发人员，谢谢！" + message, Toast.LENGTH_LONG).show();

        File file = new File(CommonConfigEntry.LOG_FILEPATH + "exception.log");
        try
        {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            fileOutputStream.write("\r\n\r\n".getBytes("GBK"));

            StringBuffer logsb = new StringBuffer();
            logsb.append(sdf.format(new Date())).append(": ");
            logsb.append(System.currentTimeMillis()).append(": ");
            logsb.append(message);

            fileOutputStream.write(logsb.toString().getBytes("GBK"));
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Looper.loop();
    }

    /**
     * 处理记录异常信息
     */
    private boolean handleException(final Throwable ex)
    {
        if (ex == null)
        {
            return false;
        }
        new Thread()
        {

            @Override
            public void run()
            {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintStream st = new PrintStream(bos);
                ex.printStackTrace(st);
                postReport(new String(bos.toByteArray()));
                try
                {
                    bos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }.start();
        return true;
    }

}
