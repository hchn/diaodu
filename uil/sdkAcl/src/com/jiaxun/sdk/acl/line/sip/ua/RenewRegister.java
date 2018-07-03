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
 * ˵������Լ��ע�ᣩ
 *
 * @author hubin
 *
 * @Date 2015-2-4
 */
public class RenewRegister
{
    private static String TAG = RenewRegister.class.getName();
    private RegisterAgent registerAgent;
//    private AlarmManager alarmManager;// ���Ӷ�ʱ��
    private PowerManager powerManager;// ��Դ������

//    private BroadcastReceiver alarmReceiver;// ʱ�Ӷ��ڻ��ѽ�����
//    private PendingIntent operation;
    private final static String RENEW_REGISTER_ALARM = "RENEW_REGISTER_ALARM";
    private static boolean isRenewRegister = false;// �Ƿ����ע�ᣨ��Լ��
    private WakeLock wakeLock;
    private long registerTime;// ע��ʱ��
    
    private ScheduledExecutorService scheduledThread = Executors.newScheduledThreadPool(1);

    public RenewRegister(RegisterAgent registerAgent)
    {
        this.registerAgent = registerAgent;
    }

    /**
     * ����˵�� : ��ʱ���ע�ᣨ��Լ����ͨ��androidϵͳʱ��ʵ��
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public void monitorRegister()
    {
        if (isRenewRegister)
        {// ����ע�ᣬ����ֻ��һ����ʱ��
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
//                    {// ֹͣ��Լ
//                        return;
//                    }
//                    long time = SystemClock.elapsedRealtime();
//                    if ((time - registerTime) < 2000)
//                    {// ���������յ����ѹ㲥
//                        return;
//                    }
//                    registerTime = time;
//
//                    Log.info(TAG, "action:" + action);
//                    acquireWakeLock();// �������������
//                    new Thread()
//                    {
//                        @Override
//                        public void run()
//                        {
//                            try
//                            {
//                                Log.info(TAG, "renewRegister::");
//                                registerAgent.register(null);// ��Լ��ע�ᣩ
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
            // ��ȡ��Դ����������
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
        
        acquireWakeLock();// �������������
    }

    /**
     * ����˵�� : �������������
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    private void acquireWakeLock()
    {
        try
        {
            Log.info(TAG, "keep System wakeLock");
            // ��ȡPowerManager.WakeLock���󣬺���Ĳ���|��ʾͬʱ��������ֵ�������ǵ����õ�Tag
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
     * ����˵�� : �ͷŲ������������
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    private void releaseWakeLock()
    {
        try
        {
            Log.info(TAG, "release System wakeLock");
            // �ͷ�wakeLock
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
     * ����˵�� : ������ʱ��
     * @param time ��ʱ����
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
//        releaseWakeLock();// �ͷ���ֹ���������
        Log.info(TAG, "schedule::time:" + time);
        scheduledThread.schedule(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Log.info(TAG, "renewRegister::");
                    registerAgent.register(null);// ��Լ��ע�ᣩ
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
        {// ֹͣ��Լ,ȡ����������ʱ��
//            alarmManager.cancel(operation);
            scheduledThread.shutdownNow();
        }
    }
}
