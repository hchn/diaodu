package com.jiaxun.sdk.util.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：系统配置工具类，可以选择性的每配置一项就提交，也可以配置完所有项后集中提交
 *
 * @author hubin
 *
 * @Date 2015-3-5
 */
public class ConfigHelper
{
    private final static String TAG = ConfigHelper.class.getName();
    private SharedPreferences mSettings;
    private SharedPreferences.Editor mSettingsEditor;
    private boolean isTestMode;
    private static ConfigHelper configHelper;
    private ConfigHelper(Context context)
    {
        if (context != null)
        {
            mSettings = PreferenceManager.getDefaultSharedPreferences(context);
            mSettingsEditor = mSettings.edit();
        }
        else
        {
            Log.error(TAG, "context is null");
        }
    }

    public boolean isTestMode()
    {
        return isTestMode;
    }

    public void setTestMode(boolean isTestMode)
    {
        this.isTestMode = isTestMode;
    }

    /**
     * #####################configuration Helper#####################
     * 配置保存和获取方法
     */
    private boolean putString(final String entry, String value, boolean commit)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return false;
        }
        mSettingsEditor.putString(entry.toString(), value);
        if (commit)
        {
            return mSettingsEditor.commit();
        }
        return true;
    }

    public boolean putString(final String entry, String value)
    {
        return putString(entry, value, true);
    }

    private boolean putInt(final String entry, int value, boolean commit)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return false;
        }
        mSettingsEditor.putInt(entry.toString(), value);
        if (commit)
        {
            return mSettingsEditor.commit();
        }
        return true;
    }

    public boolean putInt(final String entry, int value)
    {
        return putInt(entry, value, true);
    }

    private boolean putFloat(final String entry, float value, boolean commit)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return false;
        }
        mSettingsEditor.putFloat(entry.toString(), value);
        if (commit)
        {
            return mSettingsEditor.commit();
        }
        return true;
    }

    public boolean putFloat(final String entry, float value)
    {
        return putFloat(entry, value, true);
    }

    private boolean putBoolean(final String entry, boolean value, boolean commit)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return false;
        }
        mSettingsEditor.putBoolean(entry.toString(), value);
        if (commit)
        {
            return mSettingsEditor.commit();
        }
        return true;
    }

    public boolean putBoolean(final String entry, boolean value)
    {
        return putBoolean(entry, value, true);
    }

    public String getString(final String entry, String defaultValue)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return defaultValue;
        }
        try
        {
            return mSettings.getString(entry.toString(), defaultValue);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return defaultValue;
        }
    }

    public int getInt(final String entry, int defaultValue)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return defaultValue;
        }
        try
        {
            return mSettings.getInt(entry.toString(), defaultValue);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return defaultValue;
        }
    }

    public float getFloat(final String entry, float defaultValue)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return defaultValue;
        }
        try
        {
            return mSettings.getFloat(entry.toString(), defaultValue);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return defaultValue;
        }
    }

    public boolean getBoolean(final String entry, boolean defaultValue)
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return defaultValue;
        }
        try
        {
            return mSettings.getBoolean(entry.toString(), defaultValue);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return defaultValue;
        }
    }

    private boolean commit()
    {
        if (mSettingsEditor == null)
        {
            Log.error(TAG, "Settings are null");
            return false;
        }
        return mSettingsEditor.commit();
    }
    public static synchronized ConfigHelper getDefaultConfigHelper(Context context)
    {
    	if(configHelper==null)
    	{
    		configHelper=new ConfigHelper(context);
    	}
    	return configHelper;
    }
}
