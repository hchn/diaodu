package com.jiaxun.uil.module.config.itf;

/**
 * 说明：配置相关服务接口
 * 
 * @author hubin
 * 
 * @Date 2015-3-4
 */
/**
 * 说明：
 *
 * @author hubin
 *
 * @Date 2015-6-8
 */
public interface ConfigService
{
    /**
     * 方法说明 : 账户显示名称
     * @param name
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAccountDisplayName(String name);

    /**
     * 方法说明 : 账户显示名称
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getAccountDisplayName();

    /**
     * 方法说明 : 账户名称
     * @param name
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAccountName(String name);

    /**
     * 方法说明 : 账户名称
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getAccountName();

    /**
     * 方法说明 : 账户号码
     * @param number
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAccountNumber(String number);

    /**
     * 方法说明 : 账户号码
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getAccountNumber();

    /**
     * 方法说明 : 账户密码
     * @param password
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAccountPassword(String password);

    /**
     * 方法说明 : 账户密码
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getAccountPassword();

    /**
     * 方法说明 : 主服务器IP地址
     * @param ip
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setMasterServerIp(String ip);

    /**
     * 方法说明 : 主服务器IP地址
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getMasterServerIp();

    /**
     * 方法说明 : 主服务器端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setMasterServerPort(int port);

    /**
     * 方法说明 : 主服务器端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getMasterServerPort();

    /**
     * 方法说明 : 备服务器IP地址
     * @param ip
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setSlaveServerIp(String ip);

    /**
     * 方法说明 : 备服务器IP地址
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getSlaveServerIp();

    /**
     * 方法说明 : 主服务器类型（slot1， slot2，sip）
     * @param type
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setMasterServerType(int type);

    /**
     * 方法说明 : 主服务器类型（slot1， slot2，sip）
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getMasterServerType();

    /**
     * 方法说明 : 备服务器类型（slot1， slot2，sip）
     * @param type
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setSlaveServerType(int type);

    /**
     * 方法说明 : 备服务器类型（slot1， slot2，sip）
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getSlaveServerType();

    /**
     * 方法说明 : 备服务器端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setSlaveServerPort(int port);

    /**
     * 方法说明 : 备服务器端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getSlaveServerPort();

    /**
     * 方法说明 : 本地主端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setMasterLocalPort(int port);

    /**
     * 方法说明 : 本地主端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getMasterLocalPort();

    /**
     * 方法说明 : 本地备端口号
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setSlaveLocalPort(int port);

    /**
     * 方法说明 : 本地备端口号
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getSlaveLocalPort();

    /**
     * 方法说明 : 本地音频起始端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAudioLocalPort(int port);

    /**
     * 方法说明 : 本地音频起始端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getAudioLocalPort();

    /**
     * 方法说明 : 本地音频最大端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAudioMaxLocalPort(int port);

    /**
     * 方法说明 : 本地音频最大端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getAudioMaxLocalPort();

    /**
     * 方法说明 : 本地视频起始端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoLocalPort(int port);

    /**
     * 方法说明 : 本地视频起始端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoLocalPort();

    /**
     * 方法说明 : 本地视频最大端口
     * @param port
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoMaxLocalPort(int port);

    /**
     * 方法说明 : 本地视频最大端口
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoMaxLocalPort();

    /**
     * 方法说明 : 视频分辨率宽
     * @param width
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoWidth(int width);

    /**
     * 方法说明 : 视频分辨率宽
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoWidth();

    /**
     * 方法说明 : 视频分辨率高
     * @param height
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoHeight(int height);

    /**
     * 方法说明 : 视频分辨率高
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoHeight();

    /**
     * 方法说明 : 视频帧率
     * @param frameRate
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoFrameRate(String frameRate);

    /**
     * 方法说明 : 视频帧率
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoFrameRate();

    /**
     * 方法说明 : 视频码率
     * @param bitRate
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoBitRate(String bitRate);

    /**
     * 方法说明 : 视频码率
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoBitRate();

    /**
     * 方法说明 : 视频I帧间隔
     * @param iFrameInterval
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setVideoIFrameInterval(String iFrameInterval);

    /**
     * 方法说明 : 视频I帧间隔
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getVideoIFrameInterval();

    /**
     * 方法说明 : 自动应答
     * @param on
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setAutoAnswer(boolean on);

    /**
     * 方法说明 : 自动应答
     * @return boolean
     * @author hubin
     * @Date 2015-4-30
     */
    boolean isAutoAnswer();

    /**
     * 方法说明 : 设置功能密码
     * @return String
     * @author hubin
     * @Date 2015-4-30
     */
    String getConfigPassword();

    /**
     * 方法说明 : 设置功能密码
     * @param configPassword
     * @return boolean
     * @author hubin
     * @Date 2015-4-30
     */
    void setConfigPassword(String configPassword);

    /**
     * 方法说明 : 紧急呼叫优先级
     * @param priority
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setEmergencyCallPriority(int priority);

    /**
     * 方法说明 : 紧急呼叫优先级
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getEmergencyCallPriority();

    /**
     * 方法说明 : 呼叫优先级
     * @param priority
     * @return void
     * @author hubin
     * @Date 2015-4-30
     */
    void setCallPriority(int priority);

    /**
     * 方法说明 : 呼叫优先级
     * @return int
     * @author hubin
     * @Date 2015-4-30
     */
    int getCallPriority();

//    /**
//     * 方法说明 : 闭铃业务状态
//     * @param enable
//     * @return void
//     * @author hubin
//     * @Date 2015-4-30
//     */
//    void setCloseRingEnabled(boolean enable);
//
//    /**
//     * 方法说明 : 闭铃业务状态
//     * @return boolean
//     * @author hubin
//     * @Date 2015-4-30
//     */
//    boolean isCloseRingEnabled();

    /**
     * 方法说明 : 紧急呼叫业务状态
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-6-8
     */
    void setEmergencyEnabled(boolean enable);

    /**
     * 方法说明 : 紧急呼叫业务状态
     * @return boolean
     * @author hubin
     * @Date 2015-6-8
     */
    boolean isEmergencyEnabled();

    /**
     * 方法说明 : 免打扰业务状态
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-6-8
     */
    void setDndEnabled(boolean enable);

    /**
     * 方法说明 : 免打扰业务状态
     * @return boolean
     * @author hubin
     * @Date 2015-6-8
     */
    boolean isDndEnabled();

    /**
     * 方法说明 : 夜服业务状态
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-6-8
     */
    void setNightService(boolean enable);

    /**
     * 方法说明 : 夜服业务状态
     * @return boolean
     * @author hubin
     * @Date 2015-6-8
     */
    boolean isNightService();

    /**
     * 方法说明 : 系统静音状态
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-6-8
     */
    void setSystemMute(boolean enable);

    /**
     * 方法说明 : 系统静音状态
     * @return boolean
     * @author hubin
     * @Date 2015-6-8
     */
    boolean isSystemMute();

    /**
     * 方法说明 :设置锁屏
     * @author HeZhen
     * @Date 2015-6-9
     */
    void setScreenLocked(boolean enable);

    /**
     * 方法说明 :锁屏状态
     * @return
     * @author HeZhen
     * @Date 2015-6-9
     */
    boolean isScreenLocked();

    /**
     * 方法说明 : 适配窗口数
     * @return void
     * @author hubin
     * @Date 2015-6-9
     */
    void setVideoWindowCount(int count);

    /**
     * 方法说明 : 适配窗口数
     * @return int
     * @author hubin
     * @Date 2015-6-9
     */
    int getVideoWindowCount();

    /**
     * 方法说明 : 设置扩展屏窗口数
     * @return int
     * @author hubin
     * @Date 2015-6-9
     */
    void setPresentationWindowCount(int count);

    /**
     * 方法说明 : 默认扩展屏窗口数
     * @return int
     * @author hubin
     * @Date 2015-6-9
     */
    int getPresentationWindowCount();

    /**
     * 方法说明 : 会议追呼
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-6-10
     */
    void setConfRecallEnable(boolean enable);

    /**
     * 方法说明 : 会议追呼
     * @return boolean
     * @author hubin
     * @Date 2015-6-10
     */
    boolean isConfRecallEnabled();

    /**
     * 方法说明 : 会议追呼次数
     * @param times
     * @return void
     * @author hubin
     * @Date 2015-6-10
     */
    void setConfRecallTimes(int times);

    /**
     * 方法说明 : 会议追呼次数
     * @return int
     * @author hubin
     * @Date 2015-6-10
     */
    int getConfRecallTimes();

    /**
     * 方法说明 : 会议追呼间隔
     * @param interval
     * @return void
     * @author hubin
     * @Date 2015-6-10
     */
    void setConfRecallInterval(int interval);

    /**
     * 方法说明 : 会议追呼间隔
     * @return int
     * @author hubin
     * @Date 2015-6-10
     */
    int getConfRecallInterval();

    /**
     * 方法说明 : 获取云镜横纵向速度
     * @return 云镜横纵向速度
     * @author zhangxd
     * @Date 2015-6-16
     */
    int getPtzSpeed();

    /**
     * 方法说明 : 设置云镜横纵向速度
     * @param ptzSpeed
     * @return void
     * @author hubin
     * @Date 2015-6-18
     */
    void setPtzSpeed(int ptzSpeed);

    /**
     * 方法说明 : 本地视频是否打开
     * @return boolean
     * @author hubin
     * @Date 2015-6-18
     */
    boolean isLocalCameralVisible();

    /**
     * 方法说明 : 设置本地视频是否默认打开
     * @param visible
     * @return void
     * @author hubin
     * @Date 2015-6-18
     */
    void setLocalCameralVisible(boolean visible);

    /**
     * 方法说明 : 是否支持视频呼叫
     * @return boolean
     * @author hubin
     * @Date 2015-6-18
     */
    boolean isVideoCallEnabled();

    /**
     * 方法说明 : 是否支持视频呼叫
     * @param visible
     * @return void
     * @author hubin
     * @Date 2015-6-18
     */
    void setVideoCallEnabled(boolean enable);

    /**
     * 方法说明 : 是否支持本地录音
     * @return boolean
     * @author hubin
     * @Date 2015-6-18
     */
    boolean isAudioRecordEnabled();

    /**
     * 方法说明 : 是否支持本地录音
     * @param visible
     * @return void
     * @author hubin
     * @Date 2015-6-18
     */
    void setAudioRecordEnabled(boolean enable);

    /**
     * 方法说明 : 设置DTMF模式
     * @param mode
     * @return void
     * @author hubin
     * @Date 2015-9-7
     */
    void setDtmfMode(int mode);

    /**
     * 方法说明 : 获取DTMF模式
     * @return int
     * @author hubin
     * @Date 2015-9-7
     */
    int getDtmfMode();

    /**
     * 方法说明 : 设置来呼提示音
     * @param mode
     * @return void
     * @author chaimb
     * @Date 2015-9-23
     */
    void setIncomingCallVoice(String fileName);

    /**
     * 方法说明 : 获取来呼提示音
     * @return String
     * @author chaimb
     * @Date 2015-9-23
     */
    String getIncomingCallVoice();
    
    /**
     * 方法说明 : 设置视频窗口默认背景
     * @param fileName
     * @return void
     * @author hubin
     * @Date 2015-10-8
     */
    void setVideoBg(String fileName);
    
    /**
     * 方法说明 : 获取视频窗口默认背景
     * @return String
     * @author hubin
     * @Date 2015-10-8
     */
    String getVideoBg();
}
