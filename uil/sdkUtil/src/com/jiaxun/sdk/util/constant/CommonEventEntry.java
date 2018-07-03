package com.jiaxun.sdk.util.constant;

/**
 * 说明：
 *
 * @author  hubin
 *
 * @Date 2015-1-7
 */
public class CommonEventEntry
{
    /** 通话记录：未接  */
    public static final int CALL_LOG_TYPE_MISSED = 0;
    /** 通话记录：已接  */
    public static final int CALL_LOG_TYPE_RECEIVED = 1;
    /** 通话记录：已拨  */
    public static final int CALL_LOG_TYPE_DIALED = 2;
    /** 通话记录：所有  */
    public static final int CALL_LOG_TYPE_ALL = 3;

    /** 联系人类型：企业  */
    public static final int CONTACT_TYPE_COMPANY = 0;
    /** 联系人类型：本地  */
    public static final int CONTACT_TYPE_LOCAL = 1;
    /** 联系人类型：SIM卡 */
    public static final int CONTACT_TYPE_SIM = 2;

    /** 群组：固定/临时群组    */
    public static final int GROUP_TYPE_FIXED = 0;
    /** 群组：呼叫中组    */
    public static final int GROUP_TYPE_ACTIVE = 1;

    /** 来呼通知：终止播放视频    */
    public static final String EVENT_TYPE_CALL = "EVENT_TYPE_CALL";
    /** 来呼消息：   */
    public static final String MESSAGE_TYPE_CALL = "MESSAGE_TYPE_CALL";

    /** 进度通知：进度提示界面：进度改变    */
    public static final String EVENT_PROGRESS_CHANGE = "EVENT_PROGRESS_CHANGE";
    /** 进度通知： 进度最大值      */
    public static final String PARAM_PROGRESS_MAX = "PARAM_PROGRESS_MAX";
    /** 进度通知：进度值     */
    public static final String PARAM_PROGRESS = "PARAM_PROGRESS";
    /** 进度通知：文件ID     */
    public static final String DOWNLOAD_ID = "DOWNLOAD_ID";

    /** 在线状态：上线     */
    public static final String EVENT_ONLINE = "EVENT_ONLINE";
    /** 在线状态：下线     */
    public static final String EVENT_OFFLINE = "EVENT_OFFLINE";
    /** 下线原因：无帐号配置     */
    public static final String OFFLINE_REASON_NOCONFIG = "OFFLINE_REASON_NOCONFIG";

    public static final String EVENT_BACK_TO_HOME = "EVENT_BACK_TO_HOME";

    public static final int OPRATION_RESULT_EVENT_SUCCESS = 2000;
    public static final int OPRATION_RESULT_EVENT_NO_AVAILABLE_SESSION = 2001;

    /** 消息通知：系统事件类型  */
    public static final int MESSAGE_TYPE_COMMON = 100;
    /** 消息通知：单呼事件类型  */
    public static final int MESSAGE_TYPE_SCALL = 101;
    /** 消息通知：会议事件类型  */
    public static final int MESSAGE_TYPE_CONF = 102;
    /** 消息通知：视频监控事件类型  */
    public static final int MESSAGE_TYPE_VS = 103;
    /** 消息通知：即时消息事件类型  */
    public static final int MESSAGE_TYPE_IM = 104;
    /** 消息通知：用户状态事件类型  */
    public static final int MESSAGE_TYPE_PRESENCE = 105;
    /** 消息通知：通话记录事件类型  */
    public static final int MESSAGE_TYPE_CALL_RECORD = 106;
    /** 消息通知：联系人事件类型  */
    public static final int MESSAGE_TYPE_CONTACT = 107;
    /** 消息通知：设备控制 */
    public static final int MESSAGE_TYPE_DEVICE = 108;

    /** 事件通知： 呼叫状态 */
    public static final int MESSAGE_NOTIFY_CALL_STATUS = 901;
    /** 事件通知： 夜服 */
    public static final int MESSAGE_NOTIFY_NIGHT_SERVICE = 902;
    /** 事件通知： 服务状态 */
    public static final int MESSAGE_NOTIFY_SERVICE_STATUS = 903;
    /** 事件通知： 通话记录 */
    public static final int MESSAGE_NOTIFY_CALL_RECORD = 904;
    /** 事件通知：入呼单呼  */
    public static final int MESSAGE_NOTIFY_SCALL_RECEIVE = 905;
    /** 事件通知： 视频控制通知 */
    public static final int MESSAGE_NOTIFY_SCALL_VIDEO = 906;
    /** 事件通知： 通话确认 */
    public static final int MESSAGE_NOTIFY_SCALL_CONNECT_ACK = 907;
    /** 事件通知： 媒体改变 */
    public static final int MESSAGE_NOTIFY_SCALL_MEDIA_INFO = 908;
    /** 事件通知： 发起新呼叫确认 */
    public static final int MESSAGE_NOTIFY_SCALL_OUTGOING_ACK = 909;
    /** 事件通知： 播放回铃 */
    public static final int MESSAGE_NOTIFY_SCALL_RING_BACK = 910;
    /** 事件通知： 呼叫对端应答 */
    public static final int MESSAGE_NOTIFY_SCALL_CONNECT = 911;
    /** 事件通知： 通话释放 */
    public static final int MESSAGE_NOTIFY_SCALL_RELEASE = 912;
    /** 事件通知： 保持对端成功确认 */
    public static final int MESSAGE_NOTIFY_SCALL_HOLD_ACK = 913;
    /** 事件通知： 远端保持确认 */
    public static final int MESSAGE_NOTIFY_SCALL_REMOTE_HOLD = 914;
    /** 事件通知： 远端恢复确认 */
    public static final int MESSAGE_NOTIFY_SCALL_REMOTE_RETRIEVE = 915;
    /** 事件通知： 恢复成功确认 */
    public static final int MESSAGE_NOTIFY_SCALL_RETRIEVE_ACK = 916;
    /** 事件通知： 单呼记录信息通知 */
    public static final int MESSAGE_NOTIFY_SCALL_RECORD_INFO = 917;
    /** 事件通知： 会议成员声音控制*/
    public static final int MESSAGE_NOTIFY_SCALL_AUDIO_ENABLE = 918;
    /** 事件通知： 会议成员视频控制 */
    public static final int MESSAGE_NOTIFY_SCALL_VIDEO_ENABLE = 919;
    /** 事件通知： 会议成员视频广播推送 */
    public static final int MESSAGE_NOTIFY_SCALL_VIDEO_SHARE_RECEIVED = 920;
    /** 事件通知： 会议成员视频信息推送 */
    public static final int MESSAGE_NOTIFY_SCALL_MEMBER_MEDIA_INFO = 921;
    /** 事件操作： 夜服 */
    public static final int MESSAGE_EVENT_NIGHT_SERVICE = 922;
//    /** 事件操作： 闭铃 */
//    public static final int MESSAGE_EVENT_RING_MUTE = 923;
//    /** 事件操作： 免打扰 */
//    public static final int MESSAGE_EVENT_DND = 924;
//    /** 事件操作： 自动应答 */
//    public static final int MESSAGE_EVENT_AUTO_ANSWER = 925;

    /** 事件操作：更新账户配置  */
    public static final int MESSAGE_EVENT_UPDATE_ACCOUNT_CONFIG = 999;
    /** 事件操作：更新媒体配置  */
    public static final int MESSAGE_EVENT_UPDATE_MEDIA_CONFIG = 1000;
    /** 事件操作：更新业务配置  */
    public static final int MESSAGE_EVENT_UPDATE_SERVICE_CONFIG = 1001;
    /** 事件操作：发起单呼  */
    public static final int MESSAGE_EVENT_SCALL_MAKE = 1002;
    /** 事件操作：挂断单呼  */
    public static final int MESSAGE_EVENT_SCALL_HANGUP = 1003;
    /** 事件操作：应答单呼  */
    public static final int MESSAGE_EVENT_SCALL_ANSWER = 1004;
    /** 事件操作：注册SIP账号  */
    public static final int MESSAGE_EVENT_SERVICE_START = 1005;
    /** 事件操作：注销SIP账号  */
    public static final int MESSAGE_EVENT_SERVICE_STOP = 1006;
    /** 事件操作：保持单呼  */
    public static final int MESSAGE_EVENT_SCALL_HOLD = 1007;
    /** 事件操作：恢复单呼  */
    public static final int MESSAGE_EVENT_SCALL_RETRIEVE = 1008;
    /** 事件操作：单呼响铃  */
    public static final int MESSAGE_EVENT_SCALL_RING = 1009;
    /** 事件操作：单呼静音  */
    public static final int MESSAGE_EVENT_SCALL_MUTE = 1010;
    /** 事件操作：发送DTMF */
    public static final int MESSAGE_EVENT_SCALL_DTMF_SEND = 1011;
    /** 事件操作：单呼视频控制 */
    public static final int MESSAGE_EVENT_SCALL_VIDEO_CONTROL = 1012;
    /** 事件操作：闭铃控制 */
    public static final int MESSAGE_EVENT_SCALL_CLOSE_RING = 1013;

    /** 事件操作：会议创建 */
    public static final int MESSAGE_EVENT_CONF_CREATE = 1100;
    /** 事件操作：会议关闭 */
    public static final int MESSAGE_EVENT_CONF_CLOSE = 1101;
    /** 事件操作：进入会议 */
    public static final int MESSAGE_EVENT_CONF_ENTER = 1102;
    /** 事件操作：离开会议 */
    public static final int MESSAGE_EVENT_CONF_LEAVE = 1103;
    /** 事件操作：会议成员添加 */
    public static final int MESSAGE_EVENT_CONF_USER_ADD = 1104;
    /** 事件操作：会议成员请离 */
    public static final int MESSAGE_EVENT_CONF_USER_DELETE = 1105;
    /** 事件操作：会议成员发言控制 */
    public static final int MESSAGE_EVENT_CONF_USER_AUDIO_ENABLE = 1106;
    /** 事件操作：会议成员视频控制 */
    public static final int MESSAGE_EVENT_CONF_USER_VIDEO_ENABLE = 1107;
    /** 事件操作：会议成员视频分享 */
    public static final int MESSAGE_EVENT_CONF_USER_VIDEO_SHARE = 1108;
    /** 事件操作：会议背景音乐控制 */
    public static final int MESSAGE_EVENT_CONF_BGM_ENABLE = 1109;
    /** 事件操作：会议静音 */
    public static final int MESSAGE_EVENT_CONF_AUDIO_MUTE = 1110;
    /** 事件操作：会议视频镜像 */
    public static final int MESSAGE_EVENT_CONF_VIDEO_MUTE = 1111;
    /** 事件操作：会议视频调整分辨率 */
    public static final int MESSAGE_EVENT_CONF_VIDEO_SIZE = 1112;
    /** 事件操作：会议视频窗口调整 */
    public static final int MESSAGE_EVENT_CONF_VIDEO_WINDOW = 1113;

    /** 事件通知：会议创建证实 */
    public static final int MESSAGE_NOTIFY_CONF_CREATE_ACK = 1120;
    /** 事件通知：会议创建应答 */
    public static final int MESSAGE_NOTIFY_CONF_CREATE_CONNECT = 1121;
    /** 事件通知：会议关闭通知 */
    public static final int MESSAGE_NOTIFY_CONF_CLOSE = 1122;
    /** 事件通知：离开会议成功证实 */
    public static final int MESSAGE_NOTIFY_CONF_EXIT_OK = 1123;
    /** 事件通知：离开会议失败证实 */
    public static final int MESSAGE_NOTIFY_CONF_EXIT_FAIL = 1124;
    /** 事件通知：进入会议成功证实 */
    public static final int MESSAGE_NOTIFY_CONF_RETURN_OK = 1125;
    /** 事件通知：进入会议失败证实 */
    public static final int MESSAGE_NOTIFY_CONF_RETURN_FAIL = 1126;
    /** 事件通知：背景音乐证实 */
    public static final int MESSAGE_NOTIFY_CONF_BGM_ACK = 1127;
    /** 事件通知：会议成员振铃中 */
    public static final int MESSAGE_NOTIFY_CONF_USER_RING = 1128;
    /** 事件通知：会议成员应答 */
    public static final int MESSAGE_NOTIFY_CONF_USER_ANSWER = 1129;
    /** 事件通知：会议成员释放 */
    public static final int MESSAGE_NOTIFY_CONF_USER_RELEASE = 1130;
    /** 事件通知：成员发言控制证实 */
    public static final int MESSAGE_NOTIFY_CONF_USER_AUDIO_ENABLE_ACK = 1131;
    /** 事件通知：成员视频控制证实 */
    public static final int MESSAGE_NOTIFY_CONF_USER_VIDEO_ENABLE_ACK = 1132;
    /** 事件通知：成员视频分享（广播）证实 */
    public static final int MESSAGE_NOTIFY_CONF_USER_VIDEO_SHARE_ACK = 1133;
    /** 事件通知：媒体流信息通知 */
    public static final int MESSAGE_NOTIFY_CONF_MEDIA_INFO = 1134;
    /** 事件通知：记录信息通知 */
    public static final int MESSAGE_NOTIFY_CONF_RECORD_INFO = 1135;

    /** 事件操作：用户状态订阅：上线、下线 */
    public static final int MESSAGE_EVENT_PRESENCE_SUBSCRIBE = 1150;
    /** 事件操作：取消所有用户状态订阅 */
    public static final int MESSAGE_EVENT_PRESENCE_CANCEL_ALL = 1151;
    /** 事件通知：用户状态通知 */
    public static final int MESSAGE_NOTIFY_PRESENCE_USER_STATUS = 1160;
    
    /** 事件操作：打开视频监控*/
    public static final int MESSAGE_EVENT_VS_OPEN = 1170;
    /** 事件操作：关闭视频监控 */
    public static final int MESSAGE_EVENT_VS_CLOSE = 1171;
    /** 事件通知：开启监控，收到对端证实 */
    public static final int MESSAGE_NOTIFY_VS_OPEN_ACK = 1173;
    /** 事件通知：关闭监控*/
    public static final int MESSAGE_NOTIFY_VS_CLOSE = 1174;
    /** 事件通知：监控记录信息*/
    public static final int MESSAGE_NOTIFY_VS_RECORD_INFO = 1175;
    /** 事件通知：监控媒体信息*/
    public static final int MESSAGE_NOTIFY_VS_MEDIA_INFO = 1176;
    
    /** 事件操作：云镜控制*/
    public static final int MESSAGE_EVENT_DEVICE_CAMERA_CONTROL = 1180;
    /** 事件操作：初始化本地摄像头*/
    public static final int MESSAGE_EVENT_DEVICE_CAMERA_INIT = 1181;

    // ------hz add 2000 - 2100
//    public static final int MESSAGE_EVENT_DB_ADD_GROUP = 2000;
//
//    public static final int MESSAGE_EVENT_DB_REMOVE_GROUP_AND_CONTACT = 2001;
//
//    public static final int MESSAGE_EVENT_DB_MODIFY_GROUP = 2002;
//
//    public static final int MESSAGE_EVENT_DB_ADD_CONTACT = 2003;

//    public static final int MESSAGE_EVENT_DB_REMOVE_CONTACT = 2004;

//    public static final int MESSAGE_EVENT_DB_MODIFY_CONTACT = 2005;
//
//    public static final int MESSAGE_EVENT_DB_ADD_KEY = 2006;
//
//    public static final int MESSAGE_EVENT_DB_REMOVE_KEY = 2007;
//
//    public static final int MESSAGE_EVENT_DB_MODIFY_KEY = 2008;
    // -----------

    /** 进度通知：进度提示界面：进度改变    */
    public static final int MESSAGE_NOTIFY_PROGRESS_CHANGE = 2010;
    
    /** 光圈关停止*/
    public static final int CAMERA_CONTROL_APERTURE_CLOSE_STOP = 0x0101;
    /* 光圈关 */
    public static final int CAMERA_CONTROL_APERTURE_CLOSE = 0x0102;
    /* 光圈开 */
    public static final int CAMERA_CONTROL_APERTURE_OPEN = 0x0103;
    /* 光圈开停止 */
    public static final int CAMERA_CONTROL_APERTURE_OPEN_STOP = 0x0104;
    /* 近聚焦停止 */
    public static final int CAMERA_CONTROL_PROXIMITY_FOCUSED_STOP = 0x0201;
    /* 近聚焦 */
    public static final int CAMERA_CONTROL_PROXIMITY_FOCUSED = 0x0202;
    /* 远聚焦停止 */
    public static final int CAMERA_CONTROL_TELEFOCUS_STOP = 0x0203;
    /* 远聚焦 */
    public static final int CAMERA_CONTROL_TELEFOCUS = 0x0204;
    /* 缩小停止 */
    public static final int CAMERA_CONTROL_ZOOM_OUT_STOP = 0x0301;
    /* 缩小 */
    public static final int CAMERA_CONTROL_ZOOM_OUT = 0x0302;
    /* 放大停止 */
    public static final int CAMERA_CONTROL_ZOOM_IN_STOP = 0x0303;
    /* 放大 */
    public static final int CAMERA_CONTROL_ZOOM_IN = 0x0304;
    /* 向上停止 */
    public static final int CAMERA_CONTROL_UPWARD_STOP = 0x0401;
    /* 向上 */
    public static final int CAMERA_CONTROL_UPWARD = 0x0402;
    /* 向下停止 */
    public static final int CAMERA_CONTROL_DOWNWARD_STOP = 0x0403;
    /* 向下 */
    public static final int CAMERA_CONTROL_DOWNWARD = 0x0404;
    /* 右转停止 */
    public static final int CAMERA_CONTROL_TURN_RIGHT_STOP = 0x0501;
    /* 右转 */
    public static final int CAMERA_CONTROL_TURN_RIGHT = 0x0502;
    /* 左转停止 */
    public static final int CAMERA_CONTROL_TURN_LEFT_STOP = 0x0503;
    /* 左转 */
    public static final int CAMERA_CONTROL_TURN_LEFT = 0x0504;
    /* 预置位保存 */
    public static final int CAMERA_CONTROL_PRESETTING_BIT_SAVE = 0x0601;
    /* 预置位调用 */
    public static final int CAMERA_CONTROL_PRESETTING_BIT_CALL = 0x0602;
    /* 预置位删除 */
    public static final int CAMERA_CONTROL_PRESETTING_BIT_DELETE = 0x0603;
    /* 左上方向运动停止 */
    public static final int CAMERA_CONTROL_LEFT_UP_MOVE_STOP = 0x0701;
    /* 左上方向运动 */
    public static final int CAMERA_CONTROL_LEFT_UP_MOVE = 0x0702;
    /* 左下方向运动停止 */
    public static final int CAMERA_CONTROL_LEFT_DOWN_MOVE_STOP = 0x0703;
    /* 左下方向运动*/
    public static final int CAMERA_CONTROL_LEFT_DOWN_MOVE = 0x0704;
    /* 右上方向运动停止 */
    public static final int CAMERA_CONTROL_RIGHT_UP_MOVE_STOP = 0x0801;
    /* 右上方向运动*/
    public static final int CAMERA_CONTROL_RIGHT_UP_MOVE = 0x0802;
    /* 右下方向运动停止*/
    public static final int CAMERA_CONTROL_RIGHT_DOWN_MOVE_STOP = 0x0803;
    /* 右下方向运动*/
    public static final int CAMERA_CONTROL_RIGHT_DOWN_MOVE = 0x0804;
    /* 停止当前动作*/
    public static final int CAMERA_CONTROL_CURRENT_ACTION_STOP = 0x0901;
    /* 雨刷开*/
    public static final int CAMERA_CONTROL_WINDSHIELD_WIPER_ON = 0x0a01;
    /* 雨刷关*/
    public static final int CAMERA_CONTROL_WINDSHIELD_WIPER_OFF = 0x0a02;
    /* 灯亮*/
    public static final int CAMERA_CONTROL_LCD_ON = 0x0b01;
    /* 灯灭*/
    public static final int CAMERA_CONTROL_LCD_OFF = 0x0b02;
    /* 加热开*/
    public static final int CAMERA_CONTROL_HEATING_ON = 0x0c01;
    /* 加热关*/
    public static final int CAMERA_CONTROL_HEATING_OFF = 0x0c02;
    /* 红外开*/
    public static final int CAMERA_CONTROL_INFRARED_ON = 0x0d01;
    /* 红外关*/
    public static final int CAMERA_CONTROL_INFRARED_OFF = 0x0d02;
    /* 线性扫描开始*/
    public static final int CAMERA_CONTROL_LINER_SCAN_START = 0x0e01;
    /* 线性扫描停止*/
    public static final int CAMERA_CONTROL_LINER_SCAN_STOP = 0x0e02;
    /* 轨迹巡航开始*/
    public static final int CAMERA_CONTROL_LIGHT_TRACE_START = 0x0f01;
    /* 轨迹巡航停止*/
    public static final int CAMERA_CONTROL_LIGHT_TRACE_STOP = 0x0f02;
    /* 预置位巡航开始*/
    public static final int CAMERA_CONTROL_PRESET_TOUR_START = 0x1001;
    /* 预置位巡航停止*/
    public static final int CAMERA_CONTROL_PRESET_TOUR_STOP = 0x1002;
    /* 云台锁定*/
    public static final int CAMERA_CONTROL_PTZ_LOCK = 0x1101;
    /* 云台解锁*/
    public static final int CAMERA_CONTROL_PTZ_UNLOCK = 0x1102;

}
