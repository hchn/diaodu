package com.jiaxun.sdk.dcl.util.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-4-3
 */
public class DBConstantValues
{
    public static final String AUTHORITY = "com.jiaxun.uil.t30";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri UPDATE_URI = Uri.withAppendedPath(AUTHORITY_URI, "update");

    public static final String TB_NAME_USER = "User"; // 系统用户表

    public static final String TB_NAME_CUSTOM_CONTACT = "CustomContact"; // 自定义联系人表

    public static final String TB_NAME_GROUP = "Group1"; // 通讯录组 表

    public static final String TB_NAME_CONTACT_GROUP = "ContactGroup"; // 通讯录联系人与组关系表

    public static final String TB_NAME_CONTACT_DATA = "ContactData"; // 通讯录 数据
                                                                     // 用户号码
                                                                     // 移动号码 等
                                                                     // 邮箱 传真

    public static final String TB_NAME_BLACK_WHITE = "BlackWhite"; // 通讯录组 表

    public static final String TB_NAME_CALL_RECORD = "CallRecord"; // 通话记录表

    public static final String TB_NAME_CONF = "Conference"; // 自定义联系人表

    public static final String TB_NAME_OPERATION = "Operation"; // 自定义联系人表

    public static final String TB_NAME_DATA_TYPE = "DataType"; // 数据类型 ：联系人类型

    // ，号码类型

    /** 系统用户信息表 */
    public interface DB_User extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_USER;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_USER;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_USER);

        /** 姓名 */
        public static final String NAME = "name";
        /** 登陆名 */
        public static final String LOGIN = "login";
        /** 密码 */
        public static final String PASSWORD = "password";
        /** 优先级 */
        public static final String PRIORITY = "priority";
        /** 上次登录时间 */
        public static final String LAST_LOGIN_TIME = "lastLoginTime";
        /** 上次登出时间 */
        public static final String LAST_LOGOFF_TIME = "lastLogoffTime";
        /** 登录次数 */
        public static final String LOGIN_TIMES = "loginTimes";
    }

    /** 联系人表 */
    public interface DB_Contact extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CUSTOM_CONTACT;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CUSTOM_CONTACT;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CUSTOM_CONTACT);
        /** 创建者用户ID */
        public static final String USER_ID = "userId";

        public static final String NAME = "name";
        /**联系人类型的Id 关联DataType*/
        public static final String TYPE_NAME = "typeName";
        /**编号*/
        public static final String NUMBER = "number";

        public static final String SUBSCRIBE = "subscribe";
        /**会议号码*/
        public static final String CONF_NUM = "confNum";
    }

    /**通讯录组*/
    public interface DB_Group extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_GROUP;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_GROUP;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_GROUP);
        /**组名称*/
        public static final String GROUP_NAME = "groupName";
        /**编号，按键时用到*/
        public static final String GROUP_NUM = "groupNum";

        public static final String GROUP_TYPE = "groupType";
        /** 父节点ID */
        public static final String PARENT_ID = "parentId";

        public static final String POSITION = "position";
//        public static final String ROOT_ID = "rootId";
    }

    /**联系人  数据 用户号码 移动号码 等 邮箱 传真*/
    public interface DB_Contact_Data extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CONTACT_DATA;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CONTACT_DATA;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CONTACT_DATA);

        public static final String CONTACT_ID = "contactId";

        public static final String DATA_TYPE = "dataType";

        public static final String DATA = "data";

    }

    /**组 联系人 关系表*/
    public interface DB_Contact_Group extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CONTACT_GROUP;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CONTACT_GROUP;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CONTACT_GROUP);

        public static final String GROUP_ID = "groupId";

        public static final String CONTACT_ID = "contactId";
        /**联系人在本组位置*/
        public static final String CONTACT_POSITION = "contactPosition";

    }

    /** 黑白名单表 */
    public interface DB_BlackWhite extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_BLACK_WHITE;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_BLACK_WHITE;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_BLACK_WHITE);
        /** 联系人*/
        public static final String CONTACT_ID = "contactId";
        /** 号码*/
        public static final String NUMBER = "number";
        /**操作类型0 黑联系人 1黑号码 10 白联系人 11白号码*/
        public static final String TYPE = "type";
    }

    /** 会议表  最大支持128个与会成员*/
    public interface DB_Conf extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CONF;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CONF;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CONF);

        /** DB_Contact联系人ID */
        public static final String CONTACT_ID = "contactId";
        /** 单个呼叫号码，null表示对应contactId中的所有号码，即一键多号 */
        public static final String NUMBER = "number";
    }

    /** 呼叫记录表 */
    public interface DB_Call_Record extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CALL_RECORD;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CALL_RECORD;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CALL_RECORD);

        /** 系统用户ID */
//        public static final String ID = "id";
        /** 本端号码 */
        public static final String CALLER_NUM = "callerNum";
        /** 本端名称 */
        public static final String CALLER_NAME = "callerName";
        /** 被叫号码 */
        public static final String PEER_NUM = "peerNum";
        /** 被叫名称 */
        public static final String PEER_NAME = "peerName";
        /** 功能号码 */
        public static final String FUNC_CODE = "funcCode";
        /** 呼叫类型（单呼、组呼等） */
        public static final String CALL_TYPE = "callType";
        /** 呼叫等级（紧急呼叫、普通呼叫等） */
        public static final String CALL_PRIORITY = "callPriority";
        /** 释放原因 */
        public static final String RELEASE_REASON = "releaseReason";
        /** 呼叫开始时间 */
        public static final String CALL_START_TIME = "callStartTime";
        /** 通话开始时间 */
        public static final String CONNECT_START_TIME = "connectStartTime";
        /** 呼叫结束时间 */
        public static final String RELEASE_TIME = "releaseTime";
        /** 通话时长 */
        public static final String DURATION = "duration";
        /** 呼叫方向（呼入呼出） */
        public static final String OUTGOING = "outGoing";
        /** 值班员名字 */
        public static final String ATD_NAME = "atdName";
        /** 是否主席 */
        public static final String CHAIRMAN = "chairMan";
        /** 会议ID */
        public static final String CONF_ID = "confId";
        /** 所属会议名称 */
        public static final String CONFNAME = "confName";
        /** 录音录像ID */
        public static final String RECORD_TASK_ID = "recordTaskId";
        /** 录音录像文件存放服务器地址 */
        public static final String RECORD_SERVER = "recordServer";
        /** 录音文件URI */
        public static final String RECORD_FILE = "recordFile";
        /** 对方的用户类型（监控，用户） */
        public static final String USER_TYPE = "userType";
        /** 是否电路域电话 */
        public static final String CIRCUIT_SWITCH = "circuitSwitch";
    }

    /** 操作日志表 */
    public interface DB_Operation extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_OPERATION;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_OPERATION;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_OPERATION);

        /** 值班员名称 */
        public static final String ATD_NAME = "AtdName";
        /** 操作时间 */
        public static final String TIME = "time";
        /** 功能区域:如：配置、单呼、会议 */
        public static final String FUNCTION = "function";
        /** 操作描述 */
        public static final String OPERATION = "operation";
        /** 操作信息 */
        public static final String INFO = "info";
    }

    public interface DB_DataType extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_DATA_TYPE;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_DATA_TYPE;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_DATA_TYPE);
        /** 数据标识，是用户类型还是号码类型 */
        public static final String DATA_IDENT = "DataIdent";
        /** 类型名称 */
        public static final String TYPE_NAME = "TypeName";
    }
}
