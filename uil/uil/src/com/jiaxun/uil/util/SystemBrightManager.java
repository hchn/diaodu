package com.jiaxun.uil.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.WindowManager;

/**
 * 封装系统亮度
 * @author why
 */
public class SystemBrightManager {
    
    // 判断是否开启了自动亮度模式
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
    
    // 获取当前系统亮度
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
    
    // 改变屏幕亮度
    public static void setBrightness(Activity activity, int brightValue) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = (brightValue <= 0 ? -1.0f : brightValue / 255f);
        activity.getWindow().setAttributes(lp);
    }
    
    
    
    //开启自动亮度模式
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");  
        context.getContentResolver().notifyChange(uri, null);  
    }
    
    // 停止自动亮度模式
    public static void stopAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");  
        context.getContentResolver().notifyChange(uri, null);  
    }
    
     /** 
     * 设置当前屏幕亮度的模屏
    * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
    * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
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
