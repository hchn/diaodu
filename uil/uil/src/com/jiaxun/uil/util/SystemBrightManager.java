package com.jiaxun.uil.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

/**
 * ��װϵͳ����
 * @author why
 */
public class SystemBrightManager {
    
    // �ж��Ƿ������Զ�����ģʽ
    public static boolean isAutoBrightness(Context context) {
        boolean autoBrightness = false;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            autoBrightness = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
        return autoBrightness;
    }
    
    // ��ȡ��ǰϵͳ����
    public static int getBrightness(Context context) {
        int brightValue = 0; 
        ContentResolver contentResolver = context.getContentResolver();
        try {
            brightValue = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }  
        return brightValue;
    }
    
    // �ı���Ļ����
    public static void setBrightness(Activity activity, int brightValue) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = (brightValue <= 0 ? -1.0f : brightValue / 255f);
        activity.getWindow().setAttributes(lp);
    }
    
    
    
    //�����Զ�����ģʽ
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");  
        context.getContentResolver().notifyChange(uri, null);  
    }
    
    // ֹͣ�Զ�����ģʽ
    public static void stopAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");  
        context.getContentResolver().notifyChange(uri, null);  
    }
    
     /** 
     * ���õ�ǰ��Ļ���ȵ�ģ��
    * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 Ϊ�Զ�������Ļ����
    * SCREEN_BRIGHTNESS_MODE_MANUAL=0 Ϊ�ֶ�������Ļ����
    */  
    public static void setBrightnessMode(Context context, int brightMode)
    {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, brightMode); 
    }

    public static void saveBrightness(Context context, int brightValue)
    {
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
        android.provider.Settings.System.putInt(context.getContentResolver(), "screen_brightness", brightValue);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        context.getContentResolver().notifyChange(uri, null);

    }
}
