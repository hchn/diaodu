package com.jiaxun.setting.ui.fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.adapter.SettingAdapter;
import com.jiaxun.setting.model.PrefItemType;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsBooleanItem;
import com.jiaxun.setting.model.PrefsGroupItem;
import com.jiaxun.setting.model.PrefsGroupRadioItem;
import com.jiaxun.setting.model.PrefsRightItem;
import com.jiaxun.setting.model.PrefsSeekBarItem;
import com.jiaxun.setting.model.PrefsSelectFileItem;
import com.jiaxun.setting.model.PrefsTextItem;
import com.jiaxun.setting.model.RadioGroupType;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.module.config.itf.ConfigService;
import com.jiaxun.uil.ui.screen.BaseActivity;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.SystemBrightManager;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.UilConstantEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：系统配置
 * 
 * @author zhangxd
 * 
 * @Date 2015-6-2
 */
public class PrefsServiceAccountFragment extends ListFragment
{
    private static final String TAG = PrefsServiceAccountFragment.class.getName();
    protected BaseActivity parentActivity;
    private SettingAdapter settingAdapter = null;
    private List<PrefsBaseItem> settingData = null;
    private Configuration mconfig = new Configuration();
//    private String[][] language = { { "LANGUAGE_CHINESE", "简体中文" }, { "LANGUAGE_ENGLISH", "英文" } };
    private String[][] language = { { "LANGUAGE_CHINESE", "简体中文" } };
    private String[][] fontSize = { { "FONT_SMALL", "小" }, { "FONT_MIDDLE", "中" }, { "FONT_LARGE", "大" } };

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(getResources().getDrawable(R.drawable.divider));
        initData();
        settingAdapter = new SettingAdapter(getActivity(), settingData);
        setListAdapter(settingAdapter);
        // 获得当前位置
        if (getArguments() != null)
        {

            int position = getArguments().getInt("position");
            if (position != 0)
            {
                getListView().setSelection(position);
                settingAdapter.notifyDataSetInvalidated();

            }
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        parentActivity = (BaseActivity) activity;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        PrefsBaseItem item = settingData.get(position);
        ((SettingActivity) getActivity()).onPrefsItemSelected(item);
    }

    private void initData()
    {
        settingData = new ArrayList<PrefsBaseItem>();

        if (UiApplication.getAtdService() != null && UiApplication.getAtdService().isAtdAdminLogin())
        {// 管理员帐号登录
         // 用户账户参数设置
            settingData.add(new PrefsGroupItem("帐户设置"));
            // settingData.add(new PrefsTextItem(ItemType.TEXT,
            // "终端显示名称","PREF_SERVICE_ACCOUNT_DISPLAY_NAME",KeyType.STRING,"请输入长度少于28位的数字",
            // R.drawable.ic_launcher, 1, 28, 0, 0,true, false, false));
            settingData.add(new PrefsTextItem("号码", -1, PrefItemType.TEXT, UiConfigEntry.PREF_SERVICE_ACCOUNT_NUMBER, UilConstantEntry.ACCOUNT_OR_PASSWORD,
                    "用户名1~20个字符，支持数字、字母、下划线、@符号", new PrefsBaseItem.ItemCallBack()
                    {
                        @Override
                        public void onCallBackResult(boolean result)
                        {
//                            ServiceUtils.startSdkService();
                            ServiceUtils.updateAccountConfig();
                        }
                    }));
            // settingData.add(new PrefsTextItem(ItemType.TEXT,
            // "注册账号","PREF_SERVICE_ACCOUNT_NAME",KeyType.STRING,"请输入长度少于28位的字符",
            // R.drawable.ic_launcher, 1, 28, 0, 0,true, false, false));
            settingData.add(new PrefsTextItem("密码", -1, PrefItemType.PASSWORD, UiConfigEntry.PREF_SERVICE_ACCOUNT_PASSWORD,
                    UilConstantEntry.ACCOUNT_OR_PASSWORD, "密码1~20个字符，支持数字、字母、下划线、@符号", new PrefsBaseItem.ItemCallBack()
                    {
                        @Override
                        public void onCallBackResult(boolean result)
                        {
//                            ServiceUtils.startSdkService();
                            ServiceUtils.updateAccountConfig();
                        }
                    }));
            // 主用服务器参数设置
            // settingData.add(new PrefsGroupItem(ItemType.GROUP, "主服务器设置"));
            settingData.add(new PrefsTextItem("主服务器地址", -1, PrefItemType.SERVER_ADDRESS, UiConfigEntry.PREF_SERVICE_MASTER_SERVER_IP,
                    UilConstantEntry.IP_ADDRESS_REGEX, "请输入有效的服务器IP地址或域名", new PrefsBaseItem.ItemCallBack()
                    {
                        @Override
                        public void onCallBackResult(boolean result)
                        {
//                            ServiceUtils.startSdkService();
                            ServiceUtils.updateAccountConfig();
                        }
                    }));
            // settingData.add(new PrefsTextItem(ItemType.TEXT, "端口",
            // "PREF_SERVICE_MASTER_SERVER_PORT", KeyType.STRING,
            // "请输入范围在1-65535范围内 的有效端口号", R.drawable.ic_launcher, 1, 5, 1,
            // 65535,
            // false, false, false));
            // 备用服务器参数设置
            // settingData.add(new PrefsGroupItem(ItemType.GROUP, "备服务器设置"));
            settingData.add(new PrefsTextItem("备用服务器地址", -1, PrefItemType.SERVER_ADDRESS, UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_IP,
                    UilConstantEntry.IP_ADDRESS_REGEX, "请输入有效的服务器IP地址或域名", new PrefsBaseItem.ItemCallBack()
                    {
                        @Override
                        public void onCallBackResult(boolean result)
                        {
//                            ServiceUtils.startSdkService();
                            ServiceUtils.updateAccountConfig();
                        }
                    }));
            // settingData.add(new PrefsTextItem(ItemType.TEXT, "端口",
            // "PREF_SERVICE_SLAVE_SERVER_PORT", KeyType.INT,
            // "请输入范围在1-65535范围内 的有效端口号",
            // R.drawable.ic_launcher, 1, 5, 1, 65535, false, false, false));
        }

        // // 会议成员追呼
        // settingData.add(new PrefsGroupItem("会议成员追呼"));
        // settingData.add(new PrefsBooleanItem("开启追呼", R.drawable.ic_launcher,
        // CommonConfigEntry.PREF_CONF_RECALL, false));
        // String[][] times = { { "1", "1次" }, { "2", "2次" }, { "3", "3次" } };
        // settingData.add(new PrefsGroupRadioItem("追呼次数",
        // R.drawable.ic_launcher, CommonConfigEntry.PREF_CONF_RECALL_TIMES,
        // times, null));
        // String[][] timeInterval = { { "15", "15s" }, { "30", "30s" }, { "60",
        // "60s" } };
        // settingData.add(new PrefsGroupRadioItem("追呼时间间隔",
        // R.drawable.ic_launcher, CommonConfigEntry.PREF_CONF_RECALL_INTERVAL,
        // timeInterval, null));

        // 声音设置
//        settingData.add(new PrefsGroupItem("声音设置"));
//        settingData.add(new PrefsSeekBarItem("音量大小", R.drawable.ic_launcher, "PREF_SERVICE_VOLUME", "音量大小", 1, 5, null, 1));
//        settingData.add(new PrefsSeekBarItem("单呼提示音", R.drawable.ic_launcher, "PREF_SERVICE_RING", "单呼提示音", 1, 5, null, 1));
//        settingData.add(new PrefsSeekBarItem("会议提示音", R.drawable.ic_launcher, "PREF_SERVICE_CONF_RING", "会议提示音", 1, 5, null, 1));

        // settingData.add(new PrefsGroupItem(ItemType.GROUP,
        // "本地端口"));
        // settingData.add(new PrefsTextItem(ItemType.TEXT,
        // "RTP音频端口", "PREF_SERVICE_AUDIO_PORT", KeyType.INT,
        // "请输入范围在1-65535范围内 的有效端口号",
        // R.drawable.ic_launcher, 1, 5, 1, 65535, false, false, false));
        // settingData.add(new PrefsTextItem(ItemType.TEXT,
        // "RTP视频端口", "PREF_SERVICE_VIDEO_PORT", KeyType.INT,
        // "请输入范围在1-65535范围内 的有效端口号",
        // R.drawable.ic_launcher, 1, 5, 1, 65535, false, false, false));

        // TODO
        settingData.add(new PrefsGroupItem("系统名称设置"));
        // settingData.add(new PrefsTextItem(ItemType.TEXT,
        // "终端显示名称","PREF_SERVICE_ACCOUNT_DISPLAY_NAME",KeyType.STRING,"请输入长度少于28位的数字",
        // R.drawable.ic_launcher, 1, 28, 0, 0,true, false, false));
        // 保存默认值
        ConfigHelper configHelper = ConfigHelper.getDefaultConfigHelper(parentActivity);
        String systemName = configHelper.getString(UiConfigEntry.SYSTEM_NAME, "");
        if (TextUtils.isEmpty(systemName))
        {
            configHelper.putString(UiConfigEntry.SYSTEM_NAME, UiConfigEntry.SYSTEM_DISPLAY_NAME);
        }
        settingData.add(new PrefsTextItem("系统名称", -1, PrefItemType.TEXT, UiConfigEntry.SYSTEM_NAME, "", UiConfigEntry.SYSTEM_DISPLAY_NAME,
                new PrefsBaseItem.ItemCallBack()
                {
                    @Override
                    public void onCallBackResult(boolean result)
                    {
                    }

                }));
        // 音频设置
        settingData.add(new PrefsGroupItem("音频设置"));
        String[][] autioOptions = { { CommonConstantEntry.DTMF_MODE_INBAND + "", "带内语音" }, { CommonConstantEntry.DTMF_MODE_SIP_INFO + "", "SIP INFO" },
                { CommonConstantEntry.DTMF_MODE_RFC2833 + "", "RFC2833" } };
        settingData.add(new PrefsGroupRadioItem("DTMF模式", -1, UiConfigEntry.PREF_DTMF_MODE, UiConfigEntry.DEFAULT_DTMF_MODE, autioOptions,
                new PrefsBaseItem.ItemCallBack()
                {
                    @Override
                    public void onCallBackResult(boolean result)
                    {
                    }
                }));
        String taCallVoice = ConfigHelper.getDefaultConfigHelper(getActivity()).getString(UiConfigEntry.PREF_INCOMING_CALL_VOICE,
                UiConfigEntry.DEFAULT_INCOMING_CALL_VOICE);
        if (!(UiConfigEntry.DEFAULT_INCOMING_CALL_VOICE.equals(taCallVoice)))
        {
            int index = taCallVoice.lastIndexOf("/");
            taCallVoice = taCallVoice.substring(index + 1, taCallVoice.length());
        }
        settingData.add(new PrefsSelectFileItem("来呼提示音", -1, UiConfigEntry.PREF_INCOMING_CALL_VOICE, taCallVoice, 2, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
//                        ServiceUtils.updateServiceConfig();
            }
        }));

        // 视频设置
        settingData.add(new PrefsGroupItem("视频设置"));
        String[][] videoOptions = { { "VIDEO_LOW", "低清" }, { "VIDEO_STANDARD", "标清" }, { "VIDEO_HIGH", "高清" }, { "SUPER_HIGH", "超清" },
                { "VIDEO_CUSTOM", "自定义" } };

        settingData.add(new PrefsGroupRadioItem("视频选项", -1, "PREF_VIDEO_OPTIONS", videoOptions[0][0], videoOptions, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                ConfigService configService = UiApplication.getConfigService();
                String videoOption = ConfigHelper.getDefaultConfigHelper(getActivity()).getString("PREF_VIDEO_OPTIONS", "VIDEO_LOW");
                if ("VIDEO_LOW".equals(videoOption))
                {// 低清
                    configService.setVideoBitRate("128000");
                    configService.setVideoFrameRate("15");
                    configService.setVideoHeight(240);
                    configService.setVideoWidth(320);
                    configService.setVideoIFrameInterval("1");
                    ConfigHelper.getDefaultConfigHelper(getActivity()).putString("PREF_VIDEO_SIZE", "320*240");
                }
                else if ("VIDEO_STANDARD".equals(videoOption))
                {// 标清
                    configService.setVideoBitRate("256000");
                    configService.setVideoFrameRate("15");
                    configService.setVideoHeight(480);
                    configService.setVideoWidth(640);
                    configService.setVideoIFrameInterval("1");
                    ConfigHelper.getDefaultConfigHelper(getActivity()).putString("PREF_VIDEO_SIZE", "640*480");
                }
                else if ("VIDEO_HIGH".equals(videoOption))
                {// 高清
                    configService.setVideoBitRate("1024000");
                    configService.setVideoFrameRate("15");
                    configService.setVideoHeight(720);
                    configService.setVideoWidth(1280);
                    configService.setVideoIFrameInterval("1");
                    ConfigHelper.getDefaultConfigHelper(getActivity()).putString("PREF_VIDEO_SIZE", "1280*720");
                }
                else if ("SUPER_HIGH".equals(videoOption))
                {// 超清
                    configService.setVideoBitRate("2048000");
                    configService.setVideoFrameRate("15");
                    configService.setVideoHeight(1280);
                    configService.setVideoWidth(1920);
                    configService.setVideoIFrameInterval("1");
                    ConfigHelper.getDefaultConfigHelper(getActivity()).putString("PREF_VIDEO_SIZE", "1920*1280");
                }
                else if ("VIDEO_CUSTOM".equals(videoOption))
                {// 自定义

                }
            }
        }));

//        String[][] bitRateOptions = { { "128000", "128K" }, { "256000", "256K" }, { "512000", "512K" }, { "1024000", "1M" }, { "2048000", "2M" } };
//        settingData.add(new PrefsGroupRadioItem("码率", R.drawable.ic_launcher, UiConfigEntry.PREF_VIDEO_BIT_RATE, bitRateOptions, RadioGroupType.DATA_STRING,
//                null));
//
//        String[][] frameRateOptions = { { "15", "15" }, { "20", "20" }, { "25", "25" }, { "30", "30" } };
//        settingData.add(new PrefsGroupRadioItem("帧率", R.drawable.ic_launcher, UiConfigEntry.PREF_VIDEO_FRAME_RATE, frameRateOptions,
//                RadioGroupType.DATA_STRING, null));
//
//        String[][] videoSizeOptions = { { "320*240", "320*240" }, { "640*480", "640*480" }, { "1280*720", "1280*720" }, { "1920*1280", "1920*1280" } };
//        settingData.add(new PrefsGroupRadioItem("分辨率", R.drawable.ic_launcher, "PREF_VIDEO_SIZE", videoSizeOptions, RadioGroupType.DATA_STRING, null));
//
//        String[][] iFrameIntervalOptions = { { "1", "1s" }, { "2", "2s" }, { "3", "3s" } };
//        settingData.add(new PrefsGroupRadioItem("关键帧间隔", R.drawable.ic_launcher, UiConfigEntry.PREF_VIDEO_IFRAME_INTERVAL, iFrameIntervalOptions,
//                RadioGroupType.DATA_STRING, null));
        String videoBg = ConfigHelper.getDefaultConfigHelper(getActivity()).getString(UiConfigEntry.PREF_VIDEO_BG, UiConfigEntry.DEFAULT_VIDEO_BG);
        if (!(UiConfigEntry.DEFAULT_VIDEO_BG.equals(videoBg)))
        {
            int index = videoBg.lastIndexOf("/");
            videoBg = videoBg.substring(index + 1, videoBg.length());
        }
        settingData.add(new PrefsSelectFileItem("视频背景图", -1, UiConfigEntry.PREF_VIDEO_BG, videoBg, 3, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
            }
        }));

        // 操作台用户设置
        // settingData.add(new PrefsGroupItem(ItemType.GROUP,
        // "操作台用户设置"));

        // 时间日期设置
        settingData.add(new PrefsGroupItem("日期和时间"));
        settingData.add(new PrefsBooleanItem("自动确定日期和时间", -1, UiConfigEntry.PREF_SERVICE_AUTO_DATATIME, false, new PrefsBaseItem.ItemCallBack()
        {

            @Override
            public void onCallBackResult(boolean result)
            {

            }
        }));
        settingData.add(new PrefsRightItem("设置日期", -1, 0));
        settingData.add(new PrefsRightItem("设置时间", -1, 1));
//        settingData.add(new PrefsBooleanItem("使用24小时格式", R.drawable.ic_launcher, "PREF_SERVICE_TIME24", false));

        // 显示设置
        settingData.add(new PrefsGroupItem("显示设置"));

        settingData.add(new PrefsGroupRadioItem("字体大小", -1, UiConfigEntry.PREF_FONT_SIZE, fontSize[0][0], fontSize, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
              //  EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.Font_SIZE_CHANGED);
                UiApplication.FontChanged = true;
                parentActivity.recreate();
            }
        }));

        settingData.add(new PrefsSeekBarItem("屏幕亮度", -1, UiConfigEntry.PREF_SERVICE_SCREEN_BRIGHTNESS, "请输入长度少于5位的数字", 50, 255, SystemBrightManager
                .getBrightness(getActivity()), null));

        // 语言设置
        settingData.add(new PrefsGroupItem("语言设置"));
        // 业务设置
        settingData.add(new PrefsGroupRadioItem("语言选择", -1, UiConfigEntry.PREF_LANGUAGE, language[0][0], language, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                setLanguage();
            }
        }));

        // 锁屏设置
        settingData.add(new PrefsGroupItem("自动锁屏设置"));
        settingData.add(new PrefsBooleanItem("锁屏开关", -1, UiConfigEntry.PREF_LOCK_ENABLED, UiConfigEntry.DEFAULT_LOCK_ENABLED, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                UiUtils.screenLockOpen = result;
                if (result)
                {
                    UiUtils.startLockScreenServer();
                }
                else
                {
                    UiUtils.cancelLockScreenServer();
                }
            }
        }));
        settingData.add(new PrefsGroupRadioItem("锁屏时间", -1, UiConfigEntry.PREF_LOCK_TIME, UiUtils.lockScreenTime[0][0], UiUtils.lockScreenTime,
                new PrefsBaseItem.ItemCallBack()
                {
                    @Override
                    public void onCallBackResult(boolean result)
                    {
                        UiUtils.startLockScreenServer();
                    }
                }));

        // 一键呼叫设置
        settingData.add(new PrefsGroupItem("一键呼叫设置"));
//        settingData.add(new PrefsBooleanItem("间隔时间开关", R.drawable.ic_launcher, UiConfigEntry.PREF_SERVICE_SCREEN_LOCK, false, new PrefsBaseItem.ItemCallBack()
//        {
//            @Override
//            public void onCallBackResult(boolean result)
//            {
//                UiUtils.screenLockOpen = result;
//                if (result)
//                {
//                    UiUtils.startLockScreenServer();
//                }
//                else
//                {
//                    UiUtils.cancelLockScreenServer();
//                }
//            }
//        }));
        settingData.add(new PrefsGroupRadioItem("间隔时间", -1, UiConfigEntry.PREF_ONEKEY_MULTNUM, UiUtils.oneKeyMultNums[0][0], UiUtils.oneKeyMultNums,
                new PrefsBaseItem.ItemCallBack()
                {
                    @Override
                    public void onCallBackResult(boolean result)
                    {
                    }
                }));

        // 会议设置
        settingData.add(new PrefsGroupItem("会议设置"));
        settingData.add(new PrefsBooleanItem("追呼开关", -1, UiConfigEntry.PREF_CONF_RECALL, false, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {

            }
        }));
        String[][] confAddCallTime = { { "1", "1次" }, { "2", "2次" }, { "3", "3次" } };

        settingData.add(new PrefsGroupRadioItem("追呼次数", -1, UiConfigEntry.PREF_CONF_RECALL_TIMES, Integer.parseInt(confAddCallTime[0][0]), confAddCallTime,
                new PrefsBaseItem.ItemCallBack()
                {
                    @Override
                    public void onCallBackResult(boolean result)
                    {

                    }
                }));
        String[][] confAddCallTimeSpace = { { "15", "15s" }, { "30", "30s" }, { "60", "1m" } };
        settingData.add(new PrefsGroupRadioItem("追呼时间间隔", -1, UiConfigEntry.PREF_CONF_RECALL_INTERVAL, Integer.parseInt(confAddCallTimeSpace[0][0]),
                confAddCallTimeSpace, new PrefsBaseItem.ItemCallBack()
                {
                    @Override
                    public void onCallBackResult(boolean result)
                    {

                    }
                }));

        // 网络设置
//        settingData.add(new PrefsGroupItem("网络设置"));
        // settingData.add(new PrefsGroupItem(ItemType.GROUP, "业务"));
        // settingData.add(new
        // PrefsBooleanItem(ItemType.BOOLEAN, "自动应答",
        // "PREF_CALL_AUTO_ANSWER", R.drawable.ic_launcher, false));
        // settingData.add(new PrefsTextItem(ItemType.TEXT,
        // "紧急呼叫优先级", "PREF_CALL_EMERGENCY_PRIORITY", KeyType.INT,
        // "请输入范围在0-4范围内的紧急呼叫优先级",
        // R.drawable.ic_launcher, 1, 1, 1, 4, false, false, false));
        // 配置方式说明
        // settingData.add(new PrefsGroupItem(ItemType.GROUP,
        // "配置方式说明"));

    }

    /**
     * 设置语言
     */
    public void setLanguage()
    {
        // ConfigService configService =
        // UiApplication.getConfigService();
        String languageSetting = ConfigHelper.getDefaultConfigHelper(getActivity()).getString(UiConfigEntry.PREF_LANGUAGE, UiConfigEntry.DEFAULT_PREF_LANGUAGE);
        Object am = null, config = null;
        try
        {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Log.info("amnType", activityManagerNative.toString());

            am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            Log.info("amType", am.getClass().toString());

            config = am.getClass().getMethod("getConfiguration").invoke(am);
            Log.info("configType", config.getClass().toString());

        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        // 设置语言
        try
        {
            if ("LANGUAGE_CHINESE".equals(languageSetting))
            {// 简体中文
                config.getClass().getDeclaredField("locale").set(config, Locale.CHINA);
            }
            else
            {// 英文
                config.getClass().getDeclaredField("locale").set(config, Locale.ENGLISH);

            }
            config.getClass().getDeclaredField("userSetLocale").setBoolean(config, true);
            am.getClass().getMethod("updateConfiguration", android.content.res.Configuration.class).invoke(am, config);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * 设置字体大小
     */
    public void setFontSize()
    {
        Method method;
        try
        {
            Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
            Object am = activityManagerNative.getMethod("getDefault").invoke(activityManagerNative);
            method = am.getClass().getMethod("updateConfiguration", android.content.res.Configuration.class);
            method.invoke(am, mconfig);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }
}
