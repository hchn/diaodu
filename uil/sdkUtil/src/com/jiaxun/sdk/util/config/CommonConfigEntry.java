package com.jiaxun.sdk.util.config;

/**
 * 说明：配置信息常量
 *
 * @author  hubin
 *
 * @Date 2015-1-7
 */
public class CommonConfigEntry
{
    // SDK版本号
    public final static String SDK_VERSION = "T30_SDK_V0.8_D20151013";

    public static int LINE1_DEFAULT_PORT = 6666;

    public static int LINE2_DEFAULT_PORT = 6667;

    public static final int FIXED_THREAD_NUM = 10;// 线程数量
    public static final String URGENCY_CALLER = "299";// 铁路紧急呼叫
    public static final String GROUP_NUMBER = "50";// 组呼号码
    public static final String BROADCAST_NUMBER = "51";// 广播号码
    public static final float EARGAIN = (float) 0.25;
    public static final float HMICGAIN = (float) 1.0;
    public final static String CONFCALLEE = "conference_factory";// 会议

    // 测试配置
    public static boolean TEST_CALL = false;// 呼叫（单呼|组呼）打点
    public static boolean TEST_PTT = false;// PTT打点
    public static boolean TEST_XINLING = false;// 信令打点

    public static boolean LOG_LOGCAT = true;// 日志输出到控制台
    public static boolean LOG_DEBUG = true;// 记录调试日志
    public static boolean LOG_OUTFILE = true;// 日志输出到文件
    public static int LOG_MAXSIZE = 40;// 日志最大容量，单位：m(1024 * 1024)
    public static String LOG_FILEPATH = "/mnt/sdcard/T30/";// 日志输出路径
    public static String LOG_NAME = "t30.log";// 日志名称
    public static String LOG_SYSTEM_NAME = "system.log";// 系统日志名称

    public static boolean TEST_REMOTE_LOOKBACK = false;// 开启远端环回
    public static boolean TEST_LOCAL_LOOKBACK = false;// 开启本地环回
    public static boolean TEST_RECORDEINGS = false;// 开启录音，包括上行和下行
    public static boolean TEST_RECORDEINGS_RECORD = false;// 开启上行录音
    public static boolean TEST_RECORDEINGS_TRACK = false;// 开启下行录音
    public static boolean TEST_AUDIO_LOG = false;// 开启语音包打点
    public static String TEST_RECORDEINGS_PATH = LOG_FILEPATH + "/recordings/";// 录音文件路径
    public static String TEST_RECORD_FILE_PATH = LOG_FILEPATH + "/recordFile/";// 录音录像文件路径

    public static final int MAX_IN_CALL_COUNT = 10;// 最多电话路数
    public static int DEFAULT_RENEW_TIMES = 60000;// 中心1注册续约时间（从注册200ok消息重新获取），单位：ms
    public static int REG2_TIMES = 60000;// 中心2注册续约时间（从注册200ok消息重新获取），单位：ms
    public static int MIN_RENEW_TIMES = 10;// 最小续约周期，单位：s

    public static int RTP_CACHE_PACKETS = 2;// 语音包缓存个数
    public static int HEARTBEAT_SERVER_TIME = 180000;// 终端-服务器心跳周期，单位：ms
    public static int HEARTBEAT_SDK_TIME = 5000;// SDK层-应用层心跳周期，单位：ms

    public static boolean diagnosis = false;// 诊断开关，true：打开

    public static int AUDIO_BITSPERSECOND = 12000;// 语音：比特率，流量
    public static int AUDIO_SEND_SAMPLES = 160;// 语音：发送位数，20ms采集一次
    public static final int AUDIO_PAYLOADTYPE_OPUS = 121;// 语音：OPUS编解码类型
    // 临时组呼
    public static int TMP_GROUP_NUMBER_MAX = 100;// 系统临时组呼数量：<=100；
    public static int TMP_GROUP_MEMBER_NUMBER_MAX = 30;// 每临时组呼成员数量：<=30
    public static int TMP_GROUP_LIFESPAN_MAX = 8760;// 临时群组的最大生存周期（以小时为单位，最大为8760小时）

    public static boolean HEARTBEAT_SERVER_ = false;// 服务器心跳
    public static boolean HEARTBEAT_POC = false;// poc心跳开关，机车台开启，手持台关闭

    public static String DOWNLOAD_PATH = LOG_FILEPATH;// 下载存在目录

    public static int HTTP_PORT = 8080;// HTTP服务端口

    /** 功能码086    */
    public static final String FN086 = "086";
    /** 是否加086    */
    public static final String PREF_KEY_086 = "KEY_086";
    /** 功能码历史记录保持50条记录       */
    public static final int MAX_FUNC_CODE_LOG_COUNT = 50;
    /**功能码 GSM-R 版本*/
    public final static String FN_VERSION = "FN_VERSION";
    public static final int DEF_FN_VERSION = 2;

    /**操作台用户数量限制*/
    public static final int ATTEND_COUNT_MAX = 100;
    /** 通话记录保持100条记录        */
    public static final int MAX_CALL_LOG_COUNT_IN_CACHE = 100;
    /** 通话记录数据库中最大10000条记录      */
    public static final int MAX_CALL_LOG_COUNT_IN_DB = 10000;
    /** 单个通话记录详情最大显示条数      */
    public static final int MAX_CALL_LOG_DETAIL_COUNT = 20;

    /**呼叫优先级：0,1,2,3,4，-1是默认不带优先级**********************************************************************/

    /** 呼叫优先级：最小优先级    */
    public static final int PRIORITY_MIN = -1;
    /** 呼叫优先级：默认优先级    */
    public static final int PRIORITY_DEF = 3;
    /** 呼叫优先级：最大优先级    */
    public static final int PRIORITY_MAX = 4;

    /**呼叫优先级**********************************************************************/

    /** 数据库通话记录表中，保存的最多条数 */
    public static final int CALLRECORD_NUM_MAX = 10000;
}
