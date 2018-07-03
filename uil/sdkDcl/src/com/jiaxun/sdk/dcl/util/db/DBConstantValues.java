package com.jiaxun.sdk.dcl.util.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * ˵����
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

    public static final String TB_NAME_USER = "User"; // ϵͳ�û���

    public static final String TB_NAME_CUSTOM_CONTACT = "CustomContact"; // �Զ�����ϵ�˱�

    public static final String TB_NAME_GROUP = "Group1"; // ͨѶ¼�� ��

    public static final String TB_NAME_CONTACT_GROUP = "ContactGroup"; // ͨѶ¼��ϵ�������ϵ��

    public static final String TB_NAME_CONTACT_DATA = "ContactData"; // ͨѶ¼ ����
                                                                     // �û�����
                                                                     // �ƶ����� ��
                                                                     // ���� ����

    public static final String TB_NAME_BLACK_WHITE = "BlackWhite"; // ͨѶ¼�� ��

    public static final String TB_NAME_CALL_RECORD = "CallRecord"; // ͨ����¼��

    public static final String TB_NAME_CONF = "Conference"; // �Զ�����ϵ�˱�

    public static final String TB_NAME_OPERATION = "Operation"; // �Զ�����ϵ�˱�

    public static final String TB_NAME_DATA_TYPE = "DataType"; // �������� ����ϵ������

    // ����������

    /** ϵͳ�û���Ϣ�� */
    public interface DB_User extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_USER;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_USER;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_USER);

        /** ���� */
        public static final String NAME = "name";
        /** ��½�� */
        public static final String LOGIN = "login";
        /** ���� */
        public static final String PASSWORD = "password";
        /** ���ȼ� */
        public static final String PRIORITY = "priority";
        /** �ϴε�¼ʱ�� */
        public static final String LAST_LOGIN_TIME = "lastLoginTime";
        /** �ϴεǳ�ʱ�� */
        public static final String LAST_LOGOFF_TIME = "lastLogoffTime";
        /** ��¼���� */
        public static final String LOGIN_TIMES = "loginTimes";
    }

    /** ��ϵ�˱� */
    public interface DB_Contact extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CUSTOM_CONTACT;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CUSTOM_CONTACT;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CUSTOM_CONTACT);
        /** �������û�ID */
        public static final String USER_ID = "userId";

        public static final String NAME = "name";
        /**��ϵ�����͵�Id ����DataType*/
        public static final String TYPE_NAME = "typeName";
        /**���*/
        public static final String NUMBER = "number";

        public static final String SUBSCRIBE = "subscribe";
        /**�������*/
        public static final String CONF_NUM = "confNum";
    }

    /**ͨѶ¼��*/
    public interface DB_Group extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_GROUP;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_GROUP;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_GROUP);
        /**������*/
        public static final String GROUP_NAME = "groupName";
        /**��ţ�����ʱ�õ�*/
        public static final String GROUP_NUM = "groupNum";

        public static final String GROUP_TYPE = "groupType";
        /** ���ڵ�ID */
        public static final String PARENT_ID = "parentId";

        public static final String POSITION = "position";
//        public static final String ROOT_ID = "rootId";
    }

    /**��ϵ��  ���� �û����� �ƶ����� �� ���� ����*/
    public interface DB_Contact_Data extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CONTACT_DATA;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CONTACT_DATA;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CONTACT_DATA);

        public static final String CONTACT_ID = "contactId";

        public static final String DATA_TYPE = "dataType";

        public static final String DATA = "data";

    }

    /**�� ��ϵ�� ��ϵ��*/
    public interface DB_Contact_Group extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CONTACT_GROUP;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CONTACT_GROUP;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CONTACT_GROUP);

        public static final String GROUP_ID = "groupId";

        public static final String CONTACT_ID = "contactId";
        /**��ϵ���ڱ���λ��*/
        public static final String CONTACT_POSITION = "contactPosition";

    }

    /** �ڰ������� */
    public interface DB_BlackWhite extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_BLACK_WHITE;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_BLACK_WHITE;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_BLACK_WHITE);
        /** ��ϵ��*/
        public static final String CONTACT_ID = "contactId";
        /** ����*/
        public static final String NUMBER = "number";
        /**��������0 ����ϵ�� 1�ں��� 10 ����ϵ�� 11�׺���*/
        public static final String TYPE = "type";
    }

    /** �����  ���֧��128������Ա*/
    public interface DB_Conf extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CONF;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CONF;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CONF);

        /** DB_Contact��ϵ��ID */
        public static final String CONTACT_ID = "contactId";
        /** �������к��룬null��ʾ��ӦcontactId�е����к��룬��һ����� */
        public static final String NUMBER = "number";
    }

    /** ���м�¼�� */
    public interface DB_Call_Record extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_CALL_RECORD;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_CALL_RECORD;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_CALL_RECORD);

        /** ϵͳ�û�ID */
//        public static final String ID = "id";
        /** ���˺��� */
        public static final String CALLER_NUM = "callerNum";
        /** �������� */
        public static final String CALLER_NAME = "callerName";
        /** ���к��� */
        public static final String PEER_NUM = "peerNum";
        /** �������� */
        public static final String PEER_NAME = "peerName";
        /** ���ܺ��� */
        public static final String FUNC_CODE = "funcCode";
        /** �������ͣ�����������ȣ� */
        public static final String CALL_TYPE = "callType";
        /** ���еȼ����������С���ͨ���еȣ� */
        public static final String CALL_PRIORITY = "callPriority";
        /** �ͷ�ԭ�� */
        public static final String RELEASE_REASON = "releaseReason";
        /** ���п�ʼʱ�� */
        public static final String CALL_START_TIME = "callStartTime";
        /** ͨ����ʼʱ�� */
        public static final String CONNECT_START_TIME = "connectStartTime";
        /** ���н���ʱ�� */
        public static final String RELEASE_TIME = "releaseTime";
        /** ͨ��ʱ�� */
        public static final String DURATION = "duration";
        /** ���з��򣨺�������� */
        public static final String OUTGOING = "outGoing";
        /** ֵ��Ա���� */
        public static final String ATD_NAME = "atdName";
        /** �Ƿ���ϯ */
        public static final String CHAIRMAN = "chairMan";
        /** ����ID */
        public static final String CONF_ID = "confId";
        /** ������������ */
        public static final String CONFNAME = "confName";
        /** ¼��¼��ID */
        public static final String RECORD_TASK_ID = "recordTaskId";
        /** ¼��¼���ļ���ŷ�������ַ */
        public static final String RECORD_SERVER = "recordServer";
        /** ¼���ļ�URI */
        public static final String RECORD_FILE = "recordFile";
        /** �Է����û����ͣ���أ��û��� */
        public static final String USER_TYPE = "userType";
        /** �Ƿ��·��绰 */
        public static final String CIRCUIT_SWITCH = "circuitSwitch";
    }

    /** ������־�� */
    public interface DB_Operation extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_OPERATION;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_OPERATION;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_OPERATION);

        /** ֵ��Ա���� */
        public static final String ATD_NAME = "AtdName";
        /** ����ʱ�� */
        public static final String TIME = "time";
        /** ��������:�磺���á����������� */
        public static final String FUNCTION = "function";
        /** �������� */
        public static final String OPERATION = "operation";
        /** ������Ϣ */
        public static final String INFO = "info";
    }

    public interface DB_DataType extends BaseColumns
    {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + TB_NAME_DATA_TYPE;

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + TB_NAME_DATA_TYPE;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, TB_NAME_DATA_TYPE);
        /** ���ݱ�ʶ�����û����ͻ��Ǻ������� */
        public static final String DATA_IDENT = "DataIdent";
        /** �������� */
        public static final String TYPE_NAME = "TypeName";
    }
}
