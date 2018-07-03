package com.jiaxun.uil;

import android.app.Application;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.view.Display;

import com.jiaxun.sdk.dcl.module.DclServiceFactory;
import com.jiaxun.sdk.dcl.module.attendant.itf.DclAtdService;
import com.jiaxun.sdk.dcl.module.callRecord.itf.DclCallRecordService;
import com.jiaxun.sdk.scl.SclServiceFactory;
import com.jiaxun.sdk.scl.module.common.itf.SclCommonService;
import com.jiaxun.sdk.scl.module.conf.itf.SclConfService;
import com.jiaxun.sdk.scl.module.device.itf.SclDeviceService;
import com.jiaxun.sdk.scl.module.im.itf.SclImService;
import com.jiaxun.sdk.scl.module.presence.itf.SclPresenceService;
import com.jiaxun.sdk.scl.module.scall.itf.SclSCallService;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.handler.CallEventHandler;
import com.jiaxun.uil.handler.CallRecordEventHandler;
import com.jiaxun.uil.handler.CommonEventHandler;
import com.jiaxun.uil.handler.DeviceEventHandler;
import com.jiaxun.uil.handler.PresenceEventHandler;
import com.jiaxun.uil.handler.VsEventHandler;
import com.jiaxun.uil.module.blacklist.itf.UilBlackListService;
import com.jiaxun.uil.module.config.itf.ConfigService;
import com.jiaxun.uil.module.contact.itf.UilContactService;
import com.jiaxun.uil.module.surveillance.itf.UilVsService;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.ui.view.presentation.DefaultPresentation;
import com.jiaxun.uil.ui.view.presentation.VideoPresentation;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiConfigEntry;

/**
 *  replace the Application
 *  put the global shared variables into it.
 */
public class UiApplication extends Application
{
    public static boolean FontChanged = false;
    private static String TAG = UiApplication.class.getName();
    private static UiApplication mInstance;
    public static boolean isCallServerOnline;// 业务是否在线
    public static boolean isAtdOnline;// 值班员是否在线
    public static String atdName;// 值班员名称
    public static boolean isServiceStarted;
    private static SclCommonService commonService;
    private static SclSCallService sCallService;
    private static SclConfService confService;
    private static SclPresenceService presenceService;
    private static SclImService imService;
    private static SclDeviceService deviceService;
    private static ConfigService configService;

    private static DclAtdService accountService;

    private static UilContactService contactService;

    private static DclCallRecordService callRecordService;

    private static UilVsService vsService;

    private static UilBlackListService blackListService;
    private static AudioManager audioManager;
    private static DisplayManager displayManager;
    private static Context currentContext;
//    private static KeyguardManager keyguardManager;
//
//    private static PowerManager powerManager;
//
//    private static PowerManager.WakeLock wakeLockManager;
//
//    private static AudioManager am;

    public static VideoPresentation presentation;

    public static DefaultPresentation defaultPresentation;

    public static volatile Handler applicationHandler = null;

    public UiApplication()
    {
        mInstance = this;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        // 初始化日志
        Log.startLogService(CommonConfigEntry.LOG_FILEPATH, CommonConfigEntry.LOG_NAME, CommonConfigEntry.LOG_MAXSIZE);
        Log.info(TAG, "init UiApplication");
        SdkUtil.setApplicationContext(this);
        applicationHandler = new Handler(this.getMainLooper());
    }

    public static void initSdkServices()
    {
        try
        {
            Log.info(TAG, "initSdkServices::");
            commonService = SclServiceFactory.getSclCommonService();
            sCallService = SclServiceFactory.getSclSCallService();
            confService = SclServiceFactory.getSclConfService();
            presenceService = SclServiceFactory.getSclPresenceService();
            imService = SclServiceFactory.getSclImService();
            deviceService = SclServiceFactory.getSclDeviceService();
            configService = UilServiceFactory.getConfigService(getInstance());
            accountService = DclServiceFactory.getDclAtdService();
            contactService = UilServiceFactory.getContactService(getInstance());
            callRecordService = DclServiceFactory.getDclCallRecordService();
            vsService = UilServiceFactory.getUilSurveillanceService();
            blackListService = UilServiceFactory.getUilBlackListService();

            CallRecordEventHandler callRecordEventHandler = new CallRecordEventHandler();
            callRecordService.regCallRecordEventListener(callRecordEventHandler);
            CallEventHandler callEventHandler = new CallEventHandler();
            sCallService.sCallRegEventListener(callEventHandler);
            confService.confRegEventListener(callEventHandler);
            confService.confUserRegEventListener(callEventHandler);
            VsEventHandler vsEventHandler = new VsEventHandler();
            vsService.regVsEventListener(vsEventHandler);

            // 注册系统公共服务时间监听
            CommonEventHandler commonEventHandler = new CommonEventHandler();
            commonService.sclRegCommonEventListener(commonEventHandler);
            PresenceEventHandler presenceEventHandler = new PresenceEventHandler();
            presenceService.presenceRegEventListener(presenceEventHandler);
            DeviceEventHandler deviceEventHandler = new DeviceEventHandler();
            deviceService.deviceRegEventListener(deviceEventHandler);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    public static void initSystemConfig()
    {
        ServiceUtils.updateMediaConfig();
        ServiceUtils.updateServiceConfig();
    }

//    private void initConfig()
//    {
//        // 初始化日志
//        LogSystem.startLogService(CommonConfigEntry.LOG_FILEPATH, 40);
//        // 初始化业务模块默认配置
//        ServiceConfig serviceConfig = new ServiceConfig();
//        serviceConfig.localIp = IpAddress.getLocalIpAddress();
//        serviceConfig.localSipPort = new int[]{6666, 6667};
//        serviceConfig.sipAccount = new String[]{"14686245009"};
//        serviceConfig.sipPassword = new String[]{"123"};
//        serviceConfig.sipServerIp = new String[]{"10.10.1.245"};
//        commonService.setSclConfig(serviceConfig);
//    }

    public static UiApplication getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new UiApplication();
        }
        return mInstance;
    }

    public static void setCurrentContext(Context context)
    {
        currentContext = context;
    }

    public static Context getCurrentContext()
    {
        return currentContext;
    }

//
//    public static Context getContext()
//    {
//        return getInstance();
//    }
    public static UilBlackListService getBlackListService()
    {
        if (blackListService == null)
        {
            blackListService = UilServiceFactory.getUilBlackListService();
            ;
        }
        return blackListService;
    }

    public static UilVsService getVsService()
    {
        if (vsService == null)
        {
            vsService = UilServiceFactory.getUilSurveillanceService();
            VsEventHandler vsEventHandler = new VsEventHandler();
            vsService.regVsEventListener(vsEventHandler);
        }
        return vsService;
    }

    public static SclCommonService getCommonService()
    {
        if (commonService == null)
        {
            commonService = SclServiceFactory.getSclCommonService();
            CommonEventHandler commonEventHandler = new CommonEventHandler();
            UiApplication.getCommonService().sclRegCommonEventListener(commonEventHandler);
        }
        return commonService;
    }

    public static SclSCallService getSCallService()
    {
        if (sCallService == null)
        {
            sCallService = SclServiceFactory.getSclSCallService();
            CallEventHandler callEventHandler = new CallEventHandler();
            sCallService.sCallRegEventListener(callEventHandler);
        }
        return sCallService;
    }

    public static SclConfService getConfService()
    {
        if (confService == null)
        {
            confService = SclServiceFactory.getSclConfService();
            CallEventHandler callEventHandler = new CallEventHandler();
            confService.confRegEventListener(callEventHandler);
            confService.confUserRegEventListener(callEventHandler);
        }
        return confService;
    }

    public static SclImService getIMService()
    {
        if (imService == null)
        {
            imService = SclServiceFactory.getSclImService();
        }
        return imService;
    }

    public static SclDeviceService getDeviceService()
    {
        if (deviceService == null)
        {
            deviceService = SclServiceFactory.getSclDeviceService();
            DeviceEventHandler deviceEventHandler = new DeviceEventHandler();
            deviceService.deviceRegEventListener(deviceEventHandler);
        }
        return deviceService;
    }

    public static SclPresenceService getPresenceService()
    {
        if (presenceService == null)
        {
            presenceService = SclServiceFactory.getSclPresenceService();
            PresenceEventHandler presenceEventHandler = new PresenceEventHandler();
            presenceService.presenceRegEventListener(presenceEventHandler);
        }
        return presenceService;
    }

    public static ConfigService getConfigService()
    {
        if (configService == null)
        {
            configService = UilServiceFactory.getConfigService(getInstance());
        }
        return configService;
    }

    public static DclAtdService getAtdService()
    {
        if (accountService == null)
        {
            accountService = DclServiceFactory.getDclAtdService();
        }
        return accountService;
    }

    public static DclCallRecordService getCallRecordService()
    {
        if (callRecordService == null)
        {
            callRecordService = DclServiceFactory.getDclCallRecordService();
            CallRecordEventHandler callRecordEventHandler = new CallRecordEventHandler();
            callRecordService.regCallRecordEventListener(callRecordEventHandler);
        }
        return callRecordService;
    }

    public static UilContactService getContactService()
    {
        if (contactService == null)
        {
            contactService = UilServiceFactory.getContactService(getInstance());
        }
        return contactService;
    }

    public static AudioManager getAudioManager()
    {
        if (audioManager == null)
        {
            audioManager = (AudioManager) UiApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }

    public static DisplayManager getDisplayManager()
    {
        if (displayManager == null)
        {
            displayManager = (DisplayManager) UiApplication.getInstance().getSystemService(Context.DISPLAY_SERVICE);
            displayManager.registerDisplayListener(new DisplayListener()
            {
                @Override
                public void onDisplayRemoved(int displayId)
                {
                    Log.info(TAG, "onDisplayRemoved:: displayId" + displayId);
                    if (presentation != null)
                    {
                        presentation = null;
                    }
                    if (defaultPresentation != null)
                    {
                        defaultPresentation = null;
                    }
                }

                @Override
                public void onDisplayChanged(int displayId)
                {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onDisplayAdded(int displayId)
                {
                    Log.info(TAG, "onDisplayAdded:: displayId" + displayId);
                    UiApplication.initPresentation(UiApplication.getCurrentContext());
                }
            }, null);
        }
        return displayManager;
    }

    public static void initPresentation(Context context)
    {
        if (Build.VERSION.SDK_INT >= UiConfigEntry.SDK_LOW_LEVEL)
        {// DisplayManager 是Android4.2才引入，在4.2以下的系统中会导致崩溃
            Display[] presentationDisplays = UiApplication.getDisplayManager().getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            if (presentationDisplays.length > 0)
            {
                Log.info(TAG, "init presentation");
                if (context instanceof HomeActivity && isServiceStarted)
                {
                    if (UiApplication.presentation == null)
                    {
                        UiApplication.presentation = new VideoPresentation(context, presentationDisplays[0]);
                        UiApplication.presentation.show();
                        if (UiApplication.defaultPresentation != null)
                        {
                            UiApplication.defaultPresentation.dismiss();
                        }
                    }
                }
                else
                {
                    UiApplication.defaultPresentation = new DefaultPresentation(context, presentationDisplays[0]);
                    UiApplication.defaultPresentation.show();
                }
            }
        }
        else
        {
            Log.info(TAG, "can not be expand");
        }
    }

//    public static AudioManager getAudioManager()
//    {
//        if (am == null)
//        {
//            am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
//        }
//        return am;
//    }
//
//    public static KeyguardManager getKeyguardManager()
//    {
//        if (keyguardManager == null)
//        {
//            keyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
//        }
//        return keyguardManager;
//    }
//
//    public static PowerManager getPowerManager()
//    {
//        if (powerManager == null)
//        {
//            powerManager = (PowerManager) getContext().getSystemService(POWER_SERVICE);
//        }
//        return powerManager;
//    }

    public String getAppVersionName()
    {
        try
        {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(pkName, 0).versionName;
            int versionCode = this.getPackageManager().getPackageInfo(pkName, 0).versionCode;
            return versionName;
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        return "";
    }

}
