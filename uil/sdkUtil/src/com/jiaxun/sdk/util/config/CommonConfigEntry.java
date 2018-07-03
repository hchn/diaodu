package com.jiaxun.sdk.util.config;

/**
 * ˵����������Ϣ����
 *
 * @author  hubin
 *
 * @Date 2015-1-7
 */
public class CommonConfigEntry
{
    // SDK�汾��
    public final static String SDK_VERSION = "T30_SDK_V0.8_D20151013";

    public static int LINE1_DEFAULT_PORT = 6666;

    public static int LINE2_DEFAULT_PORT = 6667;

    public static final int FIXED_THREAD_NUM = 10;// �߳�����
    public static final String URGENCY_CALLER = "299";// ��·��������
    public static final String GROUP_NUMBER = "50";// �������
    public static final String BROADCAST_NUMBER = "51";// �㲥����
    public static final float EARGAIN = (float) 0.25;
    public static final float HMICGAIN = (float) 1.0;
    public final static String CONFCALLEE = "conference_factory";// ����

    // ��������
    public static boolean TEST_CALL = false;// ���У�����|��������
    public static boolean TEST_PTT = false;// PTT���
    public static boolean TEST_XINLING = false;// ������

    public static boolean LOG_LOGCAT = true;// ��־���������̨
    public static boolean LOG_DEBUG = true;// ��¼������־
    public static boolean LOG_OUTFILE = true;// ��־������ļ�
    public static int LOG_MAXSIZE = 40;// ��־�����������λ��m(1024 * 1024)
    public static String LOG_FILEPATH = "/mnt/sdcard/T30/";// ��־���·��
    public static String LOG_NAME = "t30.log";// ��־����
    public static String LOG_SYSTEM_NAME = "system.log";// ϵͳ��־����

    public static boolean TEST_REMOTE_LOOKBACK = false;// ����Զ�˻���
    public static boolean TEST_LOCAL_LOOKBACK = false;// �������ػ���
    public static boolean TEST_RECORDEINGS = false;// ����¼�����������к�����
    public static boolean TEST_RECORDEINGS_RECORD = false;// ��������¼��
    public static boolean TEST_RECORDEINGS_TRACK = false;// ��������¼��
    public static boolean TEST_AUDIO_LOG = false;// �������������
    public static String TEST_RECORDEINGS_PATH = LOG_FILEPATH + "/recordings/";// ¼���ļ�·��
    public static String TEST_RECORD_FILE_PATH = LOG_FILEPATH + "/recordFile/";// ¼��¼���ļ�·��

    public static final int MAX_IN_CALL_COUNT = 10;// ���绰·��
    public static int DEFAULT_RENEW_TIMES = 60000;// ����1ע����Լʱ�䣨��ע��200ok��Ϣ���»�ȡ������λ��ms
    public static int REG2_TIMES = 60000;// ����2ע����Լʱ�䣨��ע��200ok��Ϣ���»�ȡ������λ��ms
    public static int MIN_RENEW_TIMES = 10;// ��С��Լ���ڣ���λ��s

    public static int RTP_CACHE_PACKETS = 2;// �������������
    public static int HEARTBEAT_SERVER_TIME = 180000;// �ն�-�������������ڣ���λ��ms
    public static int HEARTBEAT_SDK_TIME = 5000;// SDK��-Ӧ�ò��������ڣ���λ��ms

    public static boolean diagnosis = false;// ��Ͽ��أ�true����

    public static int AUDIO_BITSPERSECOND = 12000;// �����������ʣ�����
    public static int AUDIO_SEND_SAMPLES = 160;// ����������λ����20ms�ɼ�һ��
    public static final int AUDIO_PAYLOADTYPE_OPUS = 121;// ������OPUS���������
    // ��ʱ���
    public static int TMP_GROUP_NUMBER_MAX = 100;// ϵͳ��ʱ���������<=100��
    public static int TMP_GROUP_MEMBER_NUMBER_MAX = 30;// ÿ��ʱ�����Ա������<=30
    public static int TMP_GROUP_LIFESPAN_MAX = 8760;// ��ʱȺ�������������ڣ���СʱΪ��λ�����Ϊ8760Сʱ��

    public static boolean HEARTBEAT_SERVER_ = false;// ����������
    public static boolean HEARTBEAT_POC = false;// poc�������أ�����̨�������ֳ�̨�ر�

    public static String DOWNLOAD_PATH = LOG_FILEPATH;// ���ش���Ŀ¼

    public static int HTTP_PORT = 8080;// HTTP����˿�

    /** ������086    */
    public static final String FN086 = "086";
    /** �Ƿ��086    */
    public static final String PREF_KEY_086 = "KEY_086";
    /** ��������ʷ��¼����50����¼       */
    public static final int MAX_FUNC_CODE_LOG_COUNT = 50;
    /**������ GSM-R �汾*/
    public final static String FN_VERSION = "FN_VERSION";
    public static final int DEF_FN_VERSION = 2;

    /**����̨�û���������*/
    public static final int ATTEND_COUNT_MAX = 100;
    /** ͨ����¼����100����¼        */
    public static final int MAX_CALL_LOG_COUNT_IN_CACHE = 100;
    /** ͨ����¼���ݿ������10000����¼      */
    public static final int MAX_CALL_LOG_COUNT_IN_DB = 10000;
    /** ����ͨ����¼���������ʾ����      */
    public static final int MAX_CALL_LOG_DETAIL_COUNT = 20;

    /**�������ȼ���0,1,2,3,4��-1��Ĭ�ϲ������ȼ�**********************************************************************/

    /** �������ȼ�����С���ȼ�    */
    public static final int PRIORITY_MIN = -1;
    /** �������ȼ���Ĭ�����ȼ�    */
    public static final int PRIORITY_DEF = 3;
    /** �������ȼ���������ȼ�    */
    public static final int PRIORITY_MAX = 4;

    /**�������ȼ�**********************************************************************/

    /** ���ݿ�ͨ����¼���У������������� */
    public static final int CALLRECORD_NUM_MAX = 10000;
}
