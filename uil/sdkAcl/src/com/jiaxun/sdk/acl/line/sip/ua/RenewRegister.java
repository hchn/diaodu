package com.jiaxun.sdk.acl.line.sip.ua;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：续约（注册）
 *
 * @author hubin
 *
 * @Date 2015-2-4
 */
public class RenewRegister
{
    private static String TAG = RenewRegister.class.getName();
    private RegisterAgent registerAgent;
//    private AlarmManager alarmManager;// 闹钟定时器
    private PowerManager powerManager;// 电源管理器

//    private BroadcastReceiver alarmReceiver;// 时钟定期唤醒接受者
//    private PendingIntent operation;
    private final static String RENEW_REGISTER_ALARM = "RENEW_REGISTER_ALARM";
    private static boolean isRenewRegister = false;// 是否继续注册（续约）
    private WakeLock wakeLock;
    private long registerTime;// 注册时间
    
    private ScheduledExecutorService scheduledThread = Executors.newScheduledThreadPool(1);

    public RenewRegister(RegisterAgent registerAgent)
    {
        this.registerAgent = registerAgent;
    }

    /**
     * 方法说明 : 定时监测注册（续约），通过android系统时钟实现
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public void monitorRegister()
    {
        if (isRenewRegister)
        {// 正在注册，控制只有一个定时器
            return;
        }
        isRenewRegister = true;
        Log.info(TAG, "monitorRegister");

//        if (alarmReceiver != null)
//            return;
//        alarmReceiver = new BroadcastReceiver()
//        {
//            @Override
//            public void onReceive(Context context, Intent intent)
//            {
//                if (intent == null)
//                    return;
//                String action = intent.getAction();
//
//                if (action.equals(RENEW_REGISTER_ALARM))
//                {
//                    if (!isRenewRegister)
//                    {// 停止续约
//                        return;
//                    }
//                    long time = SystemClock.elapsedRealtime();
//                    if ((time - registerTime) < 2000)
//                    {// 连续两秒收到唤醒广播
//                        return;
//                    }
//                    registerTime = time;
//
//                    Log.info(TAG, "action:" + action);
//                    acquireWakeLock();// 锁定不深度休眠
//                    new Thread()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            try
//                            {
//                                Log.info(TAG, "renewRegister::");
//                                registerAgent.register(null);// 续约（注册）
//                            }
//                            catch (Exception e)
//                            {
//                                Log.exception(TAG, e);
//                            }
//                        }
//                    }.start();
//                }
//            }
//        };
//
        Context context = SdkUtil.getApplicationContext();
        if (context == null)
            return;
//        if (alarmManager == null)
//        {
//            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        }
        if (powerManager == null)
        {
            // 获取电源管理器对象
            powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        }
//
//        Intent alarmIntent = new Intent();
//        alarmIntent.setAction(RENEW_REGISTER_ALARM);
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(RENEW_REGISTER_ALARM);
//        context.registerReceiver(alarmReceiver, filter);
//
//        operation = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        
        acquireWakeLock();// 锁定不深度休眠
    }

    /**
     * 方法说明 : 锁定不深度休眠
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    private void acquireWakeLock()
    {
        try
        {
            Log.info(TAG, "keep System wakeLock");
            // 获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            if (powerManager != null)
            {
                wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
                wakeLock.acquire();
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * 方法说明 : 释放不深度休眠锁定
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    private void releaseWakeLock()
    {
        try
        {
            Log.info(TAG, "release System wakeLock");
            // 释放wakeLock
            if (powerManager != null && wakeLock != null && wakeLock.isHeld())
            {
                wakeLock.release();
            }
            wakeLock = null;
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * 方法说明 : 启动定时器
     * @param time 定时周期
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public void schedule(long time)
    {
//        if (alarmManager == null || time <= 0)
//            return;
//        long realtime = SystemClock.elapsedRealtime();
//        long nextRealtime = realtime + time;
//        if(registerTime > 0)
//        {
//            nextRealtime -= realtime - registerTime;
//        }
//        Log.info(TAG, "nextRealtime:" + nextRealtime + " realtime:" + realtime);
//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, nextRealtime, operation);
//        releaseWakeLock();// 释放阻止深度休眠锁
        Log.info(TAG, "schedule::time:" + time);
        scheduledThread.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Log.info(TAG, "renewRegister::");
                    registerAgent.register(null);// 续约（注册）
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
            }
        }, time, TimeUnit.MILLISECONDS);
    }

    public void setRenewRegister(boolean renewRegister)
    {
        Log.info(TAG, "setRenewRegister::renewRegister:" + renewRegister);
        isRenewRegister = renewRegister;
//        if (!renewRegister && alarmManager != null)
        if (!renewRegister)
        {// 停止续约,取消服务器定时器
//            alarmManager.cancel(operation);
            scheduledThread.shutdownNow();
        }
    }
}
