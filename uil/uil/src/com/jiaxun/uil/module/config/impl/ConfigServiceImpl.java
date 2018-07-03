package com.jiaxun.uil.module.config.impl;

import android.content.Context;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.uil.module.config.itf.ConfigService;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiConfigEntry;

/**
 * 说明：配置相关服务接口
 *
 * @author  hubin
 *
 * @Date 2015-3-4
 */
public class ConfigServiceImpl implements ConfigService
{
    private static ConfigServiceImpl instance;

    private ConfigHelper configHelper;

    private ConfigServiceImpl(Context context)
    {
        configHelper = ConfigHelper.getDefaultConfigHelper(context);
    }

    public static ConfigServiceImpl getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new ConfigServiceImpl(context);
        }
        return instance;
    }

    /**
     * 方法说明 : 账户显示名称
     * @param name
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAccountDisplayName(String name)
    {
        configHelper.putString(UiConfigEntry.PREF_SERVICE_ACCOUNT_DISPLAY_NAME, name);
    }

    /**
     * 方法说明 : 账户显示名称
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getAccountDisplayName()
    {
        return configHelper.getString(UiConfigEntry.PREF_SERVICE_ACCOUNT_DISPLAY_NAME, UiConfigEntry.DEFAULT_SERVICE_ACCOUNT_DISPLAY_NAME);
    }

    /**
     * 方法说明 : 账户名称
     * @param name
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAccountName(String name)
    {
        configHelper.putString(UiConfigEntry.PREF_SERVICE_ACCOUNT_NAME, name);
    }

    /**
     * 方法说明 : 账户名称
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getAccountName()
    {
        return configHelper.getString(UiConfigEntry.PREF_SERVICE_ACCOUNT_NAME, UiConfigEntry.DEFAULT_SERVICE_ACCOUNT_NAME);
    }

    /**
     * 方法说明 : 账户号码
     * @param number
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAccountNumber(String number)
    {
        configHelper.putString(UiConfigEntry.PREF_SERVICE_ACCOUNT_NUMBER, number);
    }

    /**
     * 方法说明 : 账户号码
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getAccountNumber()
    {
        return configHelper.getString(UiConfigEntry.PREF_SERVICE_ACCOUNT_NUMBER, UiConfigEntry.DEFAULT_SERVICE_ACCOUNT_NUMBER);
    }

    /**
     * 方法说明 : 账户密码
     * @param password
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAccountPassword(String password)
    {
        configHelper.putString(UiConfigEntry.PREF_SERVICE_ACCOUNT_PASSWORD, password);
    }

    /**
     * 方法说明 : 账户密码
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getAccountPassword()
    {
        return configHelper.getString(UiConfigEntry.PREF_SERVICE_ACCOUNT_PASSWORD, UiConfigEntry.DEFAULT_SERVICE_ACCOUNT_PASSWORD);
    }

    /**
     * 方法说明 : 主服务器IP地址
     * @param ip
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setMasterServerIp(String ip)
    {
        configHelper.putString(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_IP, ip);
    }

    /**
     * 方法说明 : 主服务器IP地址
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getMasterServerIp()
    {
        return configHelper.getString(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_IP, UiConfigEntry.DEFAULT_SERVICE_MASTER_SERVER_IP);
    }

    /**
     * 方法说明 : 主服务器端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setMasterServerPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_PORT, port);
    }

    /**
     * 方法说明 : 主服务器端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getMasterServerPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_PORT, UiConfigEntry.DEFAULT_SERVICE_MASTER_SERVER_PORT);
    }

    /**
     * 方法说明 : 备服务器IP地址
     * @param ip
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setSlaveServerIp(String ip)
    {
        configHelper.putString(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_IP, ip);
    }

    /**
     * 方法说明 : 备服务器IP地址
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getSlaveServerIp()
    {
        return configHelper.getString(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_IP, UiConfigEntry.DEFAULT_SERVICE_SLAVE_SERVER_IP);
    }

    /**
     * 方法说明 : 主服务器类型（slot1， slot2，sip）
     * @param type
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setMasterServerType(int type)
    {
        configHelper.putInt(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_TYPE, type);
    }

    /**
     * 方法说明 : 主服务器类型（slot1， slot2，sip）
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getMasterServerType()
    {
        return configHelper.getInt(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_TYPE, UiConfigEntry.DEFAULT_SERVICE_MASTER_SERVER_TYPE);
    }

    /**
     * 方法说明 : 备服务器类型（slot1， slot2，sip）
     * @param type
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setSlaveServerType(int type)
    {
        configHelper.putInt(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_TYPE, type);
    }

    /**
     * 方法说明 : 备服务器类型（slot1， slot2，sip）
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getSlaveServerType()
    {
        return configHelper.getInt(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_TYPE, UiConfigEntry.DEFAULT_SERVICE_SLAVE_SERVER_TYPE);
    }

    /**
     * 方法说明 : 备服务器端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setSlaveServerPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_PORT, port);
    }

    /**
     * 方法说明 : 备服务器端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getSlaveServerPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_PORT, UiConfigEntry.DEFAULT_SERVICE_SLAVE_SERVER_PORT);
    }

    /**
     * 方法说明 : 本地主端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setMasterLocalPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_MASTER_LOCAL_PORT, port);
    }

    /**
     * 方法说明 : 本地主端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getSlaveLocalPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_SLAVE_LOCAL_PORT, UiConfigEntry.DEFAULT_SLAVE_LOCAL_SIP_PORT);
    }

    /**
     * 方法说明 : 本地备端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setSlaveLocalPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_SLAVE_LOCAL_PORT, port);
    }

    /**
     * 方法说明 : 本地备端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getMasterLocalPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_MASTER_LOCAL_PORT, UiConfigEntry.DEFAULT_MASTER_LOCAL_SIP_PORT);
    }

    /**
     * 方法说明 : 本地音频起始端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAudioLocalPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_AUDIO_PORT, port);
    }

    /**
     * 方法说明 : 本地音频起始端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getAudioLocalPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_AUDIO_PORT, UiConfigEntry.DEFAULT_AUDIO_PORT);
    }

    /**
     * 方法说明 : 本地音频最大端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAudioMaxLocalPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_AUDIO_PORT_MAX, port);
    }

    /**
     * 方法说明 : 本地音频最大端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getAudioMaxLocalPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_AUDIO_PORT_MAX, UiConfigEntry.DEFAULT_AUDIO_PORT_MAXIMAL);
    }

    /**
     * 方法说明 : 本地视频起始端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoLocalPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_VIDEO_PORT, port);
    }

    /**
     * 方法说明 : 本地视频起始端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoLocalPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_VIDEO_PORT, UiConfigEntry.DEFAULT_VIDEO_PORT);
    }

    /**
     * 方法说明 : 本地视频最大端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoMaxLocalPort(int port)
    {
        configHelper.putInt(UiConfigEntry.PREF_VIDEO_PORT_MAX, port);
    }

    /**
     * 方法说明 : 本地视频最大端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoMaxLocalPort()
    {
        return configHelper.getInt(UiConfigEntry.PREF_VIDEO_PORT_MAX, UiConfigEntry.DEFAULT_VIDEO_PORT_MAXIMAL);
    }

    /**
     * 方法说明 : 视频分辨率宽
     * @param width
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoWidth(int width)
    {
        configHelper.putInt(UiConfigEntry.PREF_VIDEO_WIDTH, width);
    }

    /**
     * 方法说明 : 视频分辨率宽
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoWidth()
    {
        return configHelper.getInt(UiConfigEntry.PREF_VIDEO_WIDTH, UiConfigEntry.DEFAULT_VIDEO_WIDTH);
    }

    /**
     * 方法说明 : 视频分辨率高
     * @param height
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoHeight(int height)
    {
        configHelper.putInt(UiConfigEntry.PREF_VIDEO_HEIGHT, height);
    }

    /**
     * 方法说明 : 视频分辨率高
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoHeight()
    {
        return configHelper.getInt(UiConfigEntry.PREF_VIDEO_HEIGHT, UiConfigEntry.DEFAULT_VIDEO_HEIGHT);
    }

    /**
     * 方法说明 : 视频帧率
     * @param frameRate
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoFrameRate(String frameRate)
    {
        configHelper.putString(UiConfigEntry.PREF_VIDEO_FRAME_RATE, frameRate);
    }

    /**
     * 方法说明 : 视频帧率
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoFrameRate()
    {
        String frameRateStr = configHelper.getString(UiConfigEntry.PREF_VIDEO_FRAME_RATE, UiConfigEntry.DEFAULT_VIDEO_FRAME_RATE);
        int frameRate = 12800;
        try
        {
            frameRate = Integer.parseInt(frameRateStr);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return frameRate;
    }

    /**
     * 方法说明 : 视频码率
     * @param bitRate
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoBitRate(String bitRate)
    {
        configHelper.putString(UiConfigEntry.PREF_VIDEO_BIT_RATE, bitRate);
    }

    /**
     * 方法说明 : 视频码率
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoBitRate()
    {
        String bitRateStr = configHelper.getString(UiConfigEntry.PREF_VIDEO_BIT_RATE, UiConfigEntry.DEFAULT_VIDEO_BIT_RATE);
        int bitRate = 12800;
        try
        {
            bitRate = Integer.parseInt(bitRateStr);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return bitRate;
    }

    /**
     * 方法说明 : 视频I帧间隔
     * @param iFrameInterval
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setVideoIFrameInterval(String iFrameInterval)
    {
        configHelper.putString(UiConfigEntry.PREF_VIDEO_IFRAME_INTERVAL, iFrameInterval);
    }

    /**
     * 方法说明 : 视频I帧间隔
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    public int getVideoIFrameInterval()
    {

        String iFrameIntervalStr = configHelper.getString(UiConfigEntry.PREF_VIDEO_BIT_RATE, UiConfigEntry.DEFAULT_VIDEO_BIT_RATE);
        int iFrameInterval = 12800;
        try
        {
            iFrameInterval = Integer.parseInt(iFrameIntervalStr);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
        return iFrameInterval;
    }

    /**
     * 方法说明 : 自动应答
     * @param on
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    public void setAutoAnswer(boolean on)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_CALL_AUTO_ANSWER, on);
        ServiceUtils.updateServiceConfig();
    }

    /**
     * 方法说明 : 自动应答
     * @return boolean
     * @author hubin
     * @Date 2015-4-30
     */
    public boolean isAutoAnswer()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_CALL_AUTO_ANSWER, UiConfigEntry.DEFAULT_CALL_AUTO_ANSWER);
    }

    /**
     * 方法说明 : 设置功能密码
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    public String getConfigPassword()
    {
        return configHelper.getString(UiConfigEntry.CONFIG_PASSWORD, UiConfigEntry.DEFAULT_CONFIG_PASSWORD);
    }

    /**
     * 方法说明 : 设置功能密码
     * @param configPassword
     * @return boolean
     * @author hubin
     * @Date 2015-4-30
     */
    public void setConfigPassword(String configPassword)
    {
        configHelper.putString(UiConfigEntry.CONFIG_PASSWORD, configPassword);
    }

//    @Override
//    public void setCloseRingEnabled(boolean enable)
//    {
//        configHelper.putBoolean(UiConfigEntry.PREF_FUNC_CLOSE_RING, enable);
//        ServiceUtils.updateServiceConfig();
//    }
//
//    @Override
//    public boolean isCloseRingEnabled()
//    {
//        return configHelper.getBoolean(UiConfigEntry.PREF_FUNC_CLOSE_RING, UiConfigEntry.DEFAULT_FUNC_CLOSE_RING);
//    }

    @Override
    public void setEmergencyEnabled(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_FUNC_EMERGENCY_CALL, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public boolean isEmergencyEnabled()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_FUNC_EMERGENCY_CALL, UiConfigEntry.DEFAULT_FUNC_EMERGENCY_CALL);
    }

    @Override
    public void setDndEnabled(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_FUNC_DONT_DISTURB, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public boolean isDndEnabled()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_FUNC_DONT_DISTURB, UiConfigEntry.DEFAULT_FUNC_DONT_DISTURB);
    }

    @Override
    public void setNightService(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_FUNC_NIGHT_SERVICE, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public boolean isNightService()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_FUNC_NIGHT_SERVICE, UiConfigEntry.DEFAULT_FUNC_NIGHT_SERVICE);
    }

    @Override
    public void setSystemMute(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_FUNC_SYSTEM_MUTE, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public boolean isSystemMute()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_FUNC_SYSTEM_MUTE, UiConfigEntry.DEFAULT_FUNC_SYSTEM_MUTE);
    }

    @Override
    public void setScreenLocked(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_FUNC_LOCK_SCREEN, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public boolean isScreenLocked()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_FUNC_LOCK_SCREEN, UiConfigEntry.DEFAULT_FUNC_LOCK_SCREEN);
    }

    @Override
    public void setVideoWindowCount(int count)
    {
        configHelper.putInt(UiConfigEntry.PREF_VIDEO_WINDOW_COUNT, count);
    }

    @Override
    public int getVideoWindowCount()
    {
        return configHelper.getInt(UiConfigEntry.PREF_VIDEO_WINDOW_COUNT, UiConfigEntry.DEFAULT_VIDEO_WINDOW_COUNT);
    }

    @Override
    public void setPresentationWindowCount(int count)
    {
        configHelper.putInt(UiConfigEntry.PREF_PRESENTATION_WINDOW_COUNT, count);
    }

    @Override
    public int getPresentationWindowCount()
    {
        return configHelper.getInt(UiConfigEntry.PREF_PRESENTATION_WINDOW_COUNT, UiConfigEntry.DEFAULT_PRESENTATION_WINDOW_COUNT);
    }

    @Override
    public void setConfRecallEnable(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_CONF_RECALL, enable);
    }

    @Override
    public boolean isConfRecallEnabled()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_CONF_RECALL, UiConfigEntry.DEFAULT_CONF_RECALL);
    }

    @Override
    public void setConfRecallTimes(int times)
    {
        configHelper.putInt(UiConfigEntry.PREF_CONF_RECALL_TIMES, times);
    }

    @Override
    public int getConfRecallTimes()
    {
        return configHelper.getInt(UiConfigEntry.PREF_CONF_RECALL_TIMES, UiConfigEntry.DEFAULT_CONF_RECALL_TIMES);
    }

    @Override
    public void setConfRecallInterval(int interval)
    {
        configHelper.putInt(UiConfigEntry.PREF_CONF_RECALL_INTERVAL, interval);
    }

    @Override
    public int getConfRecallInterval()
    {
        return configHelper.getInt(UiConfigEntry.PREF_CONF_RECALL_INTERVAL, UiConfigEntry.DEFAULT_CONF_RECALL_INTERVAL);
    }

    @Override
    public int getPtzSpeed()
    {
        return configHelper.getInt(UiConfigEntry.PREF_CONF_PTZ_SPEED, UiConfigEntry.DEFAULT_PREF_CONF_PTZ_SPEED);
    }

    @Override
    public void setPtzSpeed(int ptzSpeed)
    {
        configHelper.putInt(UiConfigEntry.PREF_CONF_PTZ_SPEED, ptzSpeed);
    }

    @Override
    public boolean isLocalCameralVisible()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_LOCAL_CAMERAL_VISIBLE, UiConfigEntry.DEFAULT_LOCAL_CAMERAL_VISIBLE);
    }

    @Override
    public void setLocalCameralVisible(boolean visible)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_LOCAL_CAMERAL_VISIBLE, visible);
    }

    @Override
    public boolean isVideoCallEnabled()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_VIDEO_CALL_ENABLED, UiConfigEntry.DEFAULT_VIDEO_CALL_ENABLED);
    }

    @Override
    public void setVideoCallEnabled(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_VIDEO_CALL_ENABLED, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public void setCallPriority(int priority)
    {
        configHelper.putInt(UiConfigEntry.PREF_CALL_PRIORITY, priority);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public int getCallPriority()
    {
        return configHelper.getInt(UiConfigEntry.PREF_CALL_PRIORITY, UiConfigEntry.DEFAULT_CALL_PRIORITY);
    }

    @Override
    public void setEmergencyCallPriority(int priority)
    {
        configHelper.putInt(UiConfigEntry.PREF_CALL_EMERGENCY_PRIORITY, priority);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public int getEmergencyCallPriority()
    {
        return configHelper.getInt(UiConfigEntry.PREF_CALL_EMERGENCY_PRIORITY, UiConfigEntry.DEFAULT_CALL_EMERGENCY_PRIORITY);
    }

    @Override
    public boolean isAudioRecordEnabled()
    {
        return configHelper.getBoolean(UiConfigEntry.PREF_LOCAL_AUDIO_RECORD, UiConfigEntry.DEFAULT_LOCAL_AUDIO_RECORD);
    }

    @Override
    public void setAudioRecordEnabled(boolean enable)
    {
        configHelper.putBoolean(UiConfigEntry.PREF_LOCAL_AUDIO_RECORD, enable);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public void setDtmfMode(int mode)
    {
        configHelper.putInt(UiConfigEntry.PREF_DTMF_MODE, mode);
    }

    @Override
    public int getDtmfMode()
    {
        return configHelper.getInt(UiConfigEntry.PREF_DTMF_MODE, UiConfigEntry.DEFAULT_DTMF_MODE);
    }

    @Override
    public void setIncomingCallVoice(String fileName)
    {
        configHelper.putString(UiConfigEntry.PREF_INCOMING_CALL_VOICE, fileName);
        ServiceUtils.updateServiceConfig();
    }

    @Override
    public String getIncomingCallVoice()
    {
        return configHelper.getString(UiConfigEntry.PREF_INCOMING_CALL_VOICE, UiConfigEntry.DEFAULT_INCOMING_CALL_VOICE);
    }

    @Override
    public void setVideoBg(String fileName)
    {
        configHelper.putString(UiConfigEntry.PREF_VIDEO_BG, fileName);
    }

    @Override
    public String getVideoBg()
    {
        return configHelper.getString(UiConfigEntry.PREF_VIDEO_BG, UiConfigEntry.DEFAULT_VIDEO_BG);
    }
}
