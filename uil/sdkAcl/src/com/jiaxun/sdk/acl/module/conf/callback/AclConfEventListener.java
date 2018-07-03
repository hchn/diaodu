package com.jiaxun.sdk.acl.module.conf.callback;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;

/**
 * 说明：会议事件监听器
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public interface AclConfEventListener
{
    /**
     * 方法说明 : 会议创建证实
     * @param sessionId 会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfCreateAck(String sessionId);

    /**
     * 方法说明 : 会议创建应答
     * @param sessionId 会议会话Id
     * @param priority 会议会话优先级
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfCreateConnect(String sessionId, int priority);
    
    /**
     * 方法说明 : 会议关闭通知
     * @param sessionId 会议会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfClose(String sessionId);

    /**
     * 方法说明 : 离开会议成功证实
     * @param sessionId 会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfExitOk(String sessionId);
    
    /**
     * 方法说明 : 离开会议失败证实
     * @param sessionId 会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfExitFail(String sessionId);

    /**
     * 方法说明 : 进入会议成功证实
     * @param sessionId 会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfReturnOk(String sessionId);
    
    /**
     * 方法说明 : 进入会议失败证实
     * @param sessionId 会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfReturnFail(String sessionId);

    /**
     * 方法说明 : 背景音乐证实
     * @param sessionId 会话Id
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfBgmAck(String sessionId, boolean enable);

    /**
     * 方法说明 : 会议成员振铃中
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserRing(String sessionId, String userNum);

    /**
     * 方法说明 : 会议成员应答
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param mediaTag 媒体标识
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserAnswer(String sessionId, String userNum, String mediaTag);

    /**
     * 方法说明 : 会议成员释放
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserRelease(String sessionId, String userNum, int reason);

    /**
     * 方法说明 : 成员发言控制证实
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 开 / 关
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserAudioEnableAck(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 成员视频控制证实
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 开 / 关
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserVideoEnableAck(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 成员视频分享（广播）证实
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 开 / 关
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserVideoShareAck(String sessionId, String userNum, boolean enable);

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
//    void onConfMediaInfo(String sessionId, boolean audio, int audioCodec, int audioPktLen, boolean audioTx, boolean audioRx, boolean video, int videoCodec,
//            int videoSize, int videoFrameRate, boolean videoTx, boolean videoRx);
    void onConfMediaInfo(String sessionId, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort, String remoteAddress, Map codec);

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
