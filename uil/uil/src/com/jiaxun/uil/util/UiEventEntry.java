package com.jiaxun.uil.util;

public class UiEventEntry
{
    public static final int SCREEN_TYPE_AUTO = 0;
    public static final int SCREEN_TYPE_1 = 1;
    public static final int SCREEN_TYPE_4 = 4;
    public static final int SCREEN_TYPE_6 = 6;
    public static final int SCREEN_TYPE_8 = 8;
    public static final int SCREEN_TYPE_9 = 9;

    /** 消息通知：增加联系人  */
    public static final int MESSAGE_EVENT_CONTACT_ADD = 800;
    /** 消息通知：删除联系人  */
    public static final int MESSAGE_EVENT_CONTACT_DELETE = 801;
    /** 消息通知：修改联系人  */
    public static final int MESSAGE_EVENT_CONTACT_MODIFY = 802;
    /** 消息通知：删除多个联系人  */
    public static final int MESSAGE_EVENT_CONTACT_MUTI_DELETE = 803;
    /** 消息通知：添加通话记录  */
    public static final int MESSAGE_EVENT_CALL_RECORD_ADD = 804;
    /** 消息通知：删除通话记录  */
    public static final int MESSAGE_EVENT_CALL_RECORD_DELETE = 805;
    /** 消息通知：修改通话记录  */
    public static final int MESSAGE_EVENT_CALL_RECORD_MODIFY = 806;
    /** 消息通知：删除多个通话记录  */
    public static final int MESSAGE_EVENT_CALL_RECORD_MUTI_DELETE = 807;
    private static int totalEventCount = 0;
//    /** 事件操作：本地视频初始化 */
//    public static final int EVENT_lOCAL_VIDEO_INIT = totalEventCount++;
    /** 事件操作：本地视频显示 */
    public static final int EVENT_LOCAL_VIDEO_SHOW = totalEventCount++;
    /** 事件操作：本地视频隐藏 */
    public static final int EVENT_LOCAL_VIDEO_HIDE = totalEventCount++;
    /** 事件操作：本地视频状态变化 */
    public static final int EVENT_LOCAL_VIDEO_CHANGE = totalEventCount++;
    /** 事件操作：本地视频隐藏 */
    public static final int EVENT_UPDATE_RIGHT_TAB = totalEventCount++;
    /** 消息通知：通话记录事件类型  */
    public static final int EVENT_CALL_RECORD = totalEventCount++;
    /** 事件操作：会议中增加与会成员  */
    public static final int EVENT_CONF_MEMBER_ADD = totalEventCount++;
    /** 消息通知：会议中增加与会成员  */
    public static final int NOTIFY_CONF_MEMBER_ADD = totalEventCount++;
    /** 消息通知：单呼入会  */
    public static final int NOTIFY_CALL_TO_CONF = totalEventCount++;
    /**临时会议*/
    public static final int NOTIFY_CONF_TEMP = totalEventCount++;
    /**单呼转会议*/
    public static final int EVENT_SCALL_TO_CONF = totalEventCount++;
    public static final int NOTIFY_SCALL_TO_CONF = totalEventCount++;
    /**通话列表在不可见的时候展现*/
    public static final int EVENT_SHOW_CALL_LIST = totalEventCount++;
    /**开启远端视频窗口*/
    public static final int NOTIFY_SHOW_REMOTE_VIDEO = totalEventCount++;
    /**隐藏远端视频窗口*/
    public static final int EVENT_SHOW_LEFT_PANE = totalEventCount++;
    /**显示远端视频*/
    public static final int EVENT_ADD_REMOTE_VIDEO = totalEventCount++;
    /**移除远端视频*/
    public static final int EVENT_REMOVE_REMOTE_VIDEO = totalEventCount++;
    public static final int EVENT_RELEASE_PRESENTATION_SCREEN = totalEventCount++;
    public static final int EVENT_REMOVE_PRESENTATION_VIDEO = totalEventCount++;
    public static final int EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW = totalEventCount++;
    public static final int EVENT_ADD_PRESENTATION_VIDEO = totalEventCount++;
    /**会议状态通知*/
//    public static final int NOTIFY_CONF_STATUS_CHANGE = 13;
    /**关闭会议控制界面*/
    public static final int NOTIFY_CLOSE_CONF_CONTROL = totalEventCount++;
    /**从通话列表中移除会议*/
    public static final int NOTIFY_REMOVE_CONF_FROM_CALL_LIST = totalEventCount++;
    /**显示会议控制界面*/
    public static final int NOTIFY_OPEN_CONF_CONTROL = totalEventCount++;
    /**注册响应后回调*/
    public static final int NOTIFY_ATD_LOGIN = totalEventCount++;
    /** 事件操作： 显示联系人详情 */
    public static final int NOTIFY_SHOW_CONTACT_DETAIL = totalEventCount++;
    /** 事件通知： 服务状态 */
    public static final int NOTIFY_SERVICE_STATUS = totalEventCount++;
    /** 事件通知：用户状态通知 */
    public static final int NOTIFY_PRESENCE_USER_STATUS = totalEventCount++;
    /** 事件类型：通话记录--筛选  */
    public static final int EVENT_SEARCH_CALL_RECORD = totalEventCount++;
    /** 消息通知：通话记录--筛选  */
//    public static final int MESSAGE_NOTIFY_SEARCH_CALL_RECORD = totalEventCount++;
    /**监控列表在不可见的时候展现*/
    public static final int NOTIFY_SHOW_VS_LIST = totalEventCount++;
    /** 事件操作：添加监控成员  */
    public static final int EVENT_VS_MEMBER_ADD = totalEventCount++;
    /** 消息通知：添加监控成员通知  */
    public static final int NOTIFY_VS_MEMBER_ADD = totalEventCount++;
    /** 事件类型：通话记录--会议成员列表  */
    public static final int EVENT_CONF_MEMBER_CALL_RECORD = totalEventCount++;
    /** 事件类型：打开云镜控制  */
    public static final int EVENT_CAMERA_CONTROL = totalEventCount++;
//    /** 消息通知：单呼事件通知  */
//    public static final int NOTIFY_SCALL_EVENT = totalEventCount++;
    /** 消息通知：会议事件通知  */
//    public static final int NOTIFY_CONF_EVENT = totalEventCount++;
    /** 消息通知：wifi打开关闭 */
    public static final int NOTIFY_WIFI_EVENT = totalEventCount++;
    /** 消息通知：bluetooth打开关闭 */
    public static final int NOTIFY_BLUETOOTH_EVENT = totalEventCount++;
    /** 消息通知：bluetooth有无配对 */
    public static final int NOTIFY_BLUETOOTH_CONNECT_EVENT = totalEventCount++;
    /** 消息通知：字体大小改变 */
    public static final int NOTIFY_FONT_CHANGE_EVENT = totalEventCount++;
    /** 消息通知：云镜控制号码改变 */
    public static final int NOTIFY_PTZ_NUMBER_CHANGE = totalEventCount++;
    /** 消息通知：左侧video数量改变 */
    public static final int NOTIFY_VIDEO_NUMBER_CHANGE = totalEventCount++;
    /** 消息通知：云镜控制对象改变 */
    public static final int NOTIFY_VIDEO_PTZ_CHANGE = totalEventCount++;
    
    /**注册响应后回得到登录时间*/
    public static final int NOTIFY_ATD_LOGIN_DATETIME = totalEventCount++;
    
//    /** 消息通知：会议成员事件通知  */
//    public static final int NOTIFY_CONF_USER_EVENT = totalEventCount++;

    // //----------hz----- ----///////
//  public static final int SHOW_SETTING = 100;
    public static final int SETTING_ADD_NEW_CONTACT = totalEventCount++;
    public static final int SETTING_ADD_NEW_GROUP = totalEventCount++;
    public static final int SETTING_MODIFY_CONTACT = totalEventCount++;
    public static final int SETTING_MODIFY_GROUP = totalEventCount++;
    public static final int SETTING_CONTACT_CANCEL = totalEventCount++;
    
    // 选中联系人后
    public static final int SETTING_KEYMEMADD_SELECTE_CONTACT = totalEventCount++;
    public static final int SETTING_SUBSCRIBE_SELECTE_CONTACT = totalEventCount++;
    public static final int SETTING_KEY_ADDED = totalEventCount++;
//    public static final int SETTING_KEYORKEYGROUP_REMOV = totalEventCount++;
    public static final int SETTING_KEY_EDIT = totalEventCount++;
    public static final int SETTING_KEY_REMOVE = totalEventCount++;
    public static final int SETTING_SHOW_CONTACT_DETAIL = totalEventCount++;
    public static final int ADD_BLACK_WHITE_DATA = totalEventCount++;
    public static final int BLACK_WHITE_ADD_CONTACT = totalEventCount++;
    public static final int BLACK_WHITE_DELETE = totalEventCount++;
    public static final int BLACK_WHITE_ADD_RECORD = totalEventCount++;
    
    public static final int CONTACT_SELECTEADD_CONTACT = totalEventCount++;
    public static final int CONTACT_SELECTEADD_DIAL = totalEventCount++;
    
    public static final int CONTACT_MAKE_TURN_CALL = totalEventCount++;
    public static final int CONTACT_TEMPCONF_SHOW = totalEventCount++;
    
    public static final int CONTACT_SELECT_BELONG_DEP = totalEventCount++;
    
    public static final int CALL_RECORD_EXPORT = totalEventCount++;
    public static final int CALL_RECORD_ADD = totalEventCount++;
    
    public static final int CALL_RECORD_CLEAR = totalEventCount++;
    
    public static final int FILE_SELECT_OVER = totalEventCount++;
    
    public static final int FILE_IMPORT_OVER = totalEventCount++;
    
    public static final int FILE_EXPORT_OVER = totalEventCount++;
    
    public static final int HOME_LEFTGROUP_SHOWCONTACT = totalEventCount++;

    public static final int HOME_LEFTGROUP_SHOWKEYAREA = totalEventCount++;
    
    public static final int VIDEO_WINDOW_CHANGE = totalEventCount++;
 
    public static final int UPDATE_VSLIST = totalEventCount++;
 
    public static final int REFRESH_CONTACT_VIEW = totalEventCount++;
    public static final int CLOSE_PTZ_VIEW = totalEventCount++;
    
    public static final int OPEN_PRESENTATION_CONTROL = totalEventCount++;
    public static final int CLOSE_PRESENTATION_CONTROL = totalEventCount++;
    public static final int CALL_RECORD_MISSED_COUNT = totalEventCount++;
    public static final int Font_SIZE_CHANGED = totalEventCount++;
    
    public static final int MANUAINPUT_OVER = totalEventCount++;
    public static final int ACTION_MOUBLE_NUMBER = totalEventCount++;
    public static final int ADD_INPUT_NUMBER = totalEventCount++;
    
    public static final int CLOSE_DIAL = totalEventCount++;
    public static final int NOTIFY_NIGHT_SERVICE = totalEventCount++;
    
    public static final int NOTIFY_CALL_LIST_ITEM_CHANGE = totalEventCount++;
    public static final int NOTIFY_CALL_LIST_CHANGE = totalEventCount++;
    public static final int NOTIFY_CONF_MEMBER_ITEM_CHANGE = totalEventCount++;
    public static final int NOTIFY_CONF_MEMBER_CHANGE = totalEventCount++;
    public static final int NOTIFY_CONF_STATUS = totalEventCount++;
    public static final int NOTIFY_CONF_BGM = totalEventCount++;
    public static final int NOTIFY_SCALL_RELEASE = totalEventCount++;
    public static final int NOTIFY_SCALL_AUDIO_CHANGE = totalEventCount++;
    public static final int NOTIFY_CONF_RELEASE = totalEventCount++;
    
    public static final int NOTIFY_VS_STATUS_CHANGE = totalEventCount++;
    
    public static final int NOTIFY_SYSTME_NAME_CHANGE = totalEventCount++;
    public static final int NOTIFY_CAMERA_ERADY = totalEventCount++;
    
    public static final int NOTIFY_EXIT_SYSYTEM = totalEventCount++;
    public static final int NOTIFY_VIDEO_SWITCH = totalEventCount++;
  
    
    // --------------100-150-----------------
    
    public static final int TAB_CALL_LIST = 0;
    // public static final int TAB_COF_LIST = 1;
    public static final int TAB_VS_LIST = 1;
    public static final int TAB_CALL_RECORD = 2;
    public static final int TAB_PRESENTATION = 3;
    public static final int TAB_DIAL = 4;
    
    /** 通话记录类型*/
    public static final String  CALLRECORD_TYPE = "CALLRECORD_TYPE";
    /** 相邻通话记录的次数*/
    public static final String  CALLRECORD_COUNT = "CALLRECORD_COUNT";
    //visiblePosition
    public static final String  CALLRECORD_VISIBLEPOSITION = "CALLRECORD_VISIBLEPOSITION";
    
}
