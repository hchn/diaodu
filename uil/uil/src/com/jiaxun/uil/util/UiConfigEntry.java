package com.jiaxun.uil.util;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 系统配置参数
 */
public class UiConfigEntry
{
    /**
     * 打开夜服亮度值
     */
    public static final int NIGHT_ON = 50;
    /**
     * 关闭夜服亮度值
     */
    public static final int NIGHT_OFF = 150;
    /**
     * sdk最低级别
     */
    public static final int SDK_LOW_LEVEL = 17;
    /**
     * 系统名称长度限制
     */
    public static final int SYSTEM_LENGTH_MAX = 10;
    /**
     * 组最大层级
     */
    public static final int GROUP_LEVEL_MAX = 5;

    /**
     * 组名最大长度
     */
    public static final int GROUP_NAME_MAX = 20;
    /**
     * 操作台用户名最大长度（字符）
     */
    public static final int ATTENDANT_NAME_MAX = 20;
    /**
     * 用户名最大长度
     */
    public static final int CONTACT_NAME_MAX = 20;
    /**
     * 联系人最大电话数
     */
    public static final int PHONE_NUM_MAX = 5;

    /**
     * 会议联系人最大电话数
     */
    public static final int CONF_PHONE_NUM_MAX = 63;

    /**
     * 按键默认空位个数
     */
    public static final int KEY_PLACE_DEFAULT = 48;

    /**
     * 联系人最大数
     */
    public static final int CONTACT_MAX = 5000;

    /**
     * 部门最大数
     */
    public static final int DEPARTMENT_MAX = 500;

    /**
     * 按键最大数
     */
    public static final int HOTKEY_MAX = 1000;

    /**
     * 一个按键最大成员数
     */
    public static final int HOTKEY_MEM_MAX = 64;

    /**
     * 扩展屏开关
     */
    public static final boolean PRESEMTATION_SWITCH = true;

    /**
     * 夜服模式屏幕亮度（总范围：120-255）
     */
    public static final int NIGHT_SERVICE_BRIGHTNESS = 150;

    /** 系统名称    */
    public static final String SYSTEM_NAME = "SYSTEM_NAME";
    public static final String SYSTEM_DISPLAY_NAME = "综合指挥调度系统";

    /** 终端显示名称    */
    public static final String PREF_SERVICE_ACCOUNT_DISPLAY_NAME = "PREF_SERVICE_ACCOUNT_DISPLAY_NAME";
    public static final String DEFAULT_SERVICE_ACCOUNT_DISPLAY_NAME = "null";
    /** 服务账户名称    */
    public static final String PREF_SERVICE_ACCOUNT_NAME = "PREF_SERVICE_ACCOUNT_NAME";
    public static final String DEFAULT_SERVICE_ACCOUNT_NAME = "";
    /** 服务账户号码    */
    public static final String PREF_SERVICE_ACCOUNT_NUMBER = "PREF_SERVICE_ACCOUNT_NUMBER";
    public static final String DEFAULT_SERVICE_ACCOUNT_NUMBER = "";
    /** 服务账户密码   */
    public static final String PREF_SERVICE_ACCOUNT_PASSWORD = "PREF_SERVICE_ACCOUNT_PASSWORD";
    public static final String DEFAULT_SERVICE_ACCOUNT_PASSWORD = "";
    /** 服务账户主服务器IP地址   */
    public static final String PREF_SERVICE_MASTER_SERVER_TYPE = "PREF_SERVICE_MASTER_SERVER_TYPE";
    public static final int DEFAULT_SERVICE_MASTER_SERVER_TYPE = CommonConstantEntry.LINE_TYPE_SIP;
    /** 服务账户主服务器IP地址   */
    public static final String PREF_SERVICE_MASTER_SERVER_IP = "PREF_SERVICE_MASTER_SERVER_IP";
    public static final String DEFAULT_SERVICE_MASTER_SERVER_IP = "";
    /** 服务账户主服务器端口号   */
    public static final String PREF_SERVICE_MASTER_SERVER_PORT = "PREF_SERVICE_MASTER_SERVER_PORT";
    public static final int DEFAULT_SERVICE_MASTER_SERVER_PORT = 5060;
    /** 服务账户备服务器IP地址   */
    public static final String PREF_SERVICE_SLAVE_SERVER_TYPE = "PREF_SERVICE_SLAVE_SERVER_TYPE";
    public static final int DEFAULT_SERVICE_SLAVE_SERVER_TYPE = CommonConstantEntry.LINE_TYPE_SIP;
    /** 服务账户备服务器IP地址   */
    public static final String PREF_SERVICE_SLAVE_SERVER_IP = "PREF_SERVICE_SLAVE_SERVER_IP";
    public static final String DEFAULT_SERVICE_SLAVE_SERVER_IP = "";
    /** 服务账户备服务器端口号   */
    public static final String PREF_SERVICE_SLAVE_SERVER_PORT = "PREF_SERVICE_SLAVE_SERVER_PORT";
    public static final int DEFAULT_SERVICE_SLAVE_SERVER_PORT = 5060;

    /** SIP通信本地主端口号   */
    public static final String PREF_MASTER_LOCAL_PORT = "PREF_MASTER_LOCAL_PORT";
    public static final int DEFAULT_MASTER_LOCAL_SIP_PORT = 6666;
    /** SIP通信本地备端口号   */
    public static final String PREF_SLAVE_LOCAL_PORT = "PREF_SLAVE_LOCAL_PORT";
    public static final int DEFAULT_SLAVE_LOCAL_SIP_PORT = 6667;
    /** RTP音频端口号   */
    public static final String PREF_AUDIO_PORT = "PREF_SERVICE_AUDIO_PORT";
    public static final int DEFAULT_AUDIO_PORT = 21000;
    /** RTP最大音频端口号   */
    public static final String PREF_AUDIO_PORT_MAX = "PREF_AUDIO_PORT_MAXIMAL";
    public static final int DEFAULT_AUDIO_PORT_MAXIMAL = 21100;
    /** RTP视频端口号   */
    public static final String PREF_VIDEO_PORT = "PREF_SERVICE_VIDEO_PORT";
    public static final int DEFAULT_VIDEO_PORT = 21002;
    /** RTP最大视频端口号   */
    public static final String PREF_VIDEO_PORT_MAX = "PREF_VIDEO_PORT_MAXIMAL";
    public static final int DEFAULT_VIDEO_PORT_MAXIMAL = 21100;
    /** 视频宽度  */
    public static final String PREF_VIDEO_WIDTH = "PREF_SERVICE_VIDEO_WIDTH";
    public static final int DEFAULT_VIDEO_WIDTH = 320;
    /** 视频高度  */
    public static final String PREF_VIDEO_HEIGHT = "PREF_SERVICE_VIDEO_HEIGHT";
    public static final int DEFAULT_VIDEO_HEIGHT = 240;
    /** 视频帧率  */
    public static final String PREF_VIDEO_FRAME_RATE = "PREF_VIDEO_FRAME_RATE";
    public static final String DEFAULT_VIDEO_FRAME_RATE = "15";
    /** 视频码率  */
    public static final String PREF_VIDEO_BIT_RATE = "PREF_VIDEO_BIT_RATE";
    public static final String DEFAULT_VIDEO_BIT_RATE = "128000";
    /** 视频关键帧间隔  */
    public static final String PREF_VIDEO_IFRAME_INTERVAL = "PREF_VIDEO_IFRAME_INTERVAL";
    public static final int DEFAULT_VIDEO_IFRAME_INTERVAL = 1;

    /** 自动应答   */
    public static final String PREF_CALL_AUTO_ANSWER = "PREF_CALL_AUTO_ANSWER";
    public static final boolean DEFAULT_CALL_AUTO_ANSWER = false;

    /** 紧急呼叫优先级   */
    public static final String PREF_CALL_EMERGENCY_PRIORITY = "PREF_CALL_EMERGENCY_PRIORITY";
    public static final int DEFAULT_CALL_EMERGENCY_PRIORITY = 2;

    /** 普通呼叫默认优先级   */
    public static final String PREF_CALL_PRIORITY = "PREF_CALL_PRIORITY";
    public static final int DEFAULT_CALL_PRIORITY = -1;

    /** 闭铃业务开关   */
    public static final String PREF_FUNC_CLOSE_RING = "PREF_FUNC_CLOSE_RING";
    public static final boolean DEFAULT_FUNC_CLOSE_RING = false;

    /** 紧急呼叫业务状态   */
    public static final String PREF_FUNC_EMERGENCY_CALL = "PREF_FUNC_EMERGENCY_CALL";
    public static final boolean DEFAULT_FUNC_EMERGENCY_CALL = false;

    /** 免打扰业务状态   */
    public static final String PREF_FUNC_DONT_DISTURB = "PREF_FUNC_DONT_DISTURB";
    public static final boolean DEFAULT_FUNC_DONT_DISTURB = false;

    /** 夜服业务状态   */
    public static final String PREF_FUNC_NIGHT_SERVICE = "PREF_FUNC_NIGHT_SERVICE";
    public static final boolean DEFAULT_FUNC_NIGHT_SERVICE = false;

    /** 系统静音状态   */
    public static final String PREF_FUNC_SYSTEM_MUTE = "PREF_FUNC_SYSTEM_MUTE";
    public static final boolean DEFAULT_FUNC_SYSTEM_MUTE = false;

    /** 自动锁屏   */
    public static final String PREF_FUNC_LOCK_SCREEN = "PREF_FUNC_LOCK_SCREEN";
    public static final boolean DEFAULT_FUNC_LOCK_SCREEN = false;

    /** 临时会议   */
    public static final String PREF_FUNC_TEMP_CONF = "PREF_FUNC_TEMP_CONF";
    public static final boolean DEFAULT_FUNC_TEMP_CONF = false;

    /** 视频窗口个数   */
    public static final String PREF_VIDEO_WINDOW_COUNT = "PREF_VIDEO_WINDOW_COUNT";
    public static final int DEFAULT_VIDEO_WINDOW_COUNT = 4;

    /** 视频窗口个数   */
    public static final String PREF_PRESENTATION_WINDOW_COUNT = "PREF_PRESENTATION_WINDOW_COUNT";
    public static final int DEFAULT_PRESENTATION_WINDOW_COUNT = 0;

    /** 会议追呼开关   */
    public static final String PREF_CONF_RECALL = "PREF_CONF_RECALL";
    public static final boolean DEFAULT_CONF_RECALL = false;

    /** 会议追呼次数   */
    public static final String PREF_CONF_RECALL_TIMES = "PREF_CONF_RECALL_TIMES";
    public static final int DEFAULT_CONF_RECALL_TIMES = 3;

    /** 会议追呼时间间隔   */
    public static final String PREF_CONF_RECALL_INTERVAL = "PREF_CONF_RECALL_INTERVAL";
    public static final int DEFAULT_CONF_RECALL_INTERVAL = 15;

    /** 本地视频开关   */
    public static final String PREF_LOCAL_CAMERAL_VISIBLE = "PREF_LOCAL_CAMERAL_VISIBLE";
    public static final boolean DEFAULT_LOCAL_CAMERAL_VISIBLE = true;

    /** 本地录音开关   */
    public static final String PREF_LOCAL_AUDIO_RECORD = "PREF_LOCAL_AUDIO_RECORD";
    public static final boolean DEFAULT_LOCAL_AUDIO_RECORD = false;

    /** 是否支持视频呼叫   */
    public static final String PREF_VIDEO_CALL_ENABLED = "PREF_VIDEO_CALL_ENABLED";
    public static final boolean DEFAULT_VIDEO_CALL_ENABLED = true;

    /** 进入设置界面密码 */
    public static final String CONFIG_PASSWORD = "CONFIG_PASSWORD";
    public static final String DEFAULT_CONFIG_PASSWORD = "admin";

    /** 云镜控制速度 */
    public static final String PREF_CONF_PTZ_SPEED = "PREF_CONF_PTZ_SPEED";
    public static final int DEFAULT_PREF_CONF_PTZ_SPEED = 9;

    /** 字体大小 */
    public static final String PREF_FONT_SIZE = "PREF_FONT_SIZE";
    public static final String DEFAULT_PREF_FONT_SIZE = "FONT_SMALL";

    /** 语言设置 */
    public static final String PREF_LANGUAGE = "PREF_LANGUAGE";
    public static final String DEFAULT_PREF_LANGUAGE = "LANGUAGE_CHINESE";

    /** 亮度设置 */
    public static final String PREF_SERVICE_SCREEN_BRIGHTNESS = "PREF_SERVICE_SCREEN_BRIGHTNESS";
    public static final int DEFAULT_PREF_SERVICE_SCREEN_BRIGHTNESS = 0;
    /**以太网口一 */
    public static final String PREF_ETHERNET1 = "PREF_ETHERNET1";
    public static final String DEFAULT_PREF_ETHERNET1 = "";
    /**以太网口一 */
    public static final String PREF_ETHERNET2 = "PREF_ETHERNET2";
    public static final String DEFAULT_PREF_ETHERNET2 = "";
    /**自动获取时间日期 */
    public static final String PREF_SERVICE_AUTO_DATATIME = "PREF_SERVICE_AUTO_DATATIME";

    /**锁屏开关设置 */
    public static final String PREF_LOCK_ENABLED = "PREF_LOCK_ENABLED";
    public static final boolean DEFAULT_LOCK_ENABLED = false;
    public static final String PREF_LOCK_TIME = "PREF_LOCK_TIME";
    public static final String DEFAULT_LOCK_TIME = "TIME_30S";

    /**一键多号开关设置 */
    public static final String PREF_ONEKEY_MULTNUM = "PREF_ONEKEY_MULTNUM";

    /**二次拨号类型设置 */
    public static final String PREF_DTMF_MODE = "PREF_DTMF_MODE";
    public static final int DEFAULT_DTMF_MODE = CommonConstantEntry.DTMF_MODE_RFC2833;

    /** 来呼提示音    */
    public static final String PREF_INCOMING_CALL_VOICE = "PREF_INCOMING_CALL_VOICE";
    public static final String DEFAULT_INCOMING_CALL_VOICE = "ring.wav";

    /** 视频背景图设置    */
    public static final String PREF_VIDEO_BG = "PREF_VIDEO_BG";
    public static final String DEFAULT_VIDEO_BG = "player_background.png";

}
