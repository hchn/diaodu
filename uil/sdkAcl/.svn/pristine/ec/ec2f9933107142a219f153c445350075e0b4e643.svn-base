package com.jiaxun.sdk.acl.module.vs.callback;


/**
 * 说明：视频监控事件监听器
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public interface AclVsEventListener
{
    /**
     * 方法说明 : 视频监控关闭
     * @param sessionId
     * @param reason
     * @return void
     * @author hubin
     * @Date 2015-5-25
     */
    void onVsClosed(String sessionId, int reason);
    
    /**
     * 方法说明 : 打开视频监控证实
     * @param sessionId 会话Id
     * @param priority 会话优先级
     * @author hubin
     * @Date 2015-1-23
     */
    void onVsOpenAck(String sessionId, int priority);

    /**
     * 方法说明 : 媒体流信息通知
     * @param sessionId 会话Id
     * @param audio 支持语音
     * @param audioCodec 语音编码
     * @param audioPktLen 语音打包长度
     * @param audioTx 语音发送
     * @param audioRx 语音接收
     * @param video 支持视频
     * @param videoCodec 视频编码
     * @param videoSize 视频分辨率 1:QCIF 2:CIF 3:4CIF 5:720P 6:1080P
     * @param videoFrameRate 视频帧率
     * @param videoTx 视频发送
     * @param videoRx 视频接收
     * @author hubin
     * @Date 2015-1-23
     */
//    void onVsMediaInfo(String sessionId, boolean audio, int audioCodec, int audioPktLen, boolean audioTx, boolean audioRx, boolean video, int videoCodec,
//            int videoSize, int videoFrameRate, boolean videoTx, boolean videoRx);
    void onVsMediaInfo(String sessionId, int localVideoPort, int remoteVideoPort, String remoteAddress);
    
    /**
     * 方法说明 : 通话记录信息通知
     * @param sessionId
     * @param taskId
     * @param server
     * @return void
     * @author hubin
     * @Date 2015-5-26
     */
    void onRecordInfo(String sessionId, String taskId, String server);
    
}
