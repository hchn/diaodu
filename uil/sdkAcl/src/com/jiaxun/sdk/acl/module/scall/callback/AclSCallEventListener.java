package com.jiaxun.sdk.acl.module.scall.callback;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;

/**
 * 说明：单呼事件监听器
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public interface AclSCallEventListener
{
    /**
     * 方法说明 : 新呼叫到达
     * 适用场景: 从网络收到新的呼入，提示用户新的呼入。
     * @param sessionId 会话ID
     * @param callPriority 呼叫优先级
     * @param callerNum 主叫号码
     * @param callerName 主叫名称
     * @param funcCode GSM-R功能码
     * @param calleeNum 被叫号码
     * @param channel 通道号（1,2）
     * @param isConf 会议标识，用于作为会议成员的单呼标识
     * @param video 支持视频
     * @author hubin
     * @Date 2015-1-23
     */
    void onSCallIncoming(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean isConf, boolean video);

    /**
     * 方法说明 : 媒体流信息通知
     * 适用场景: 收到reinvite，或者音视频开启或关闭。
     * @param sessionId 会话ID
     * @param audio 支持语音
     * @param audioCodec 语音编码
     * @param audioPktLen 语音打包长度
     * @param audioTx 语音发送
     * @param audioRx 语音接收
     * @param video 支持视频
     * @param videoCodec 视频编码
     * @param videoSize 视频分辨率 1:QCIF 2:CIF 3:4CIF 5:720P 6:1080P
     * @param videoFrameSize 视频帧率
     * @param videoTx 视频发送
     * @param videoRx 视频接收
     * @author hubin
     * @Date 2015-1-23
     */
//    void onSCallMediaInfo(String sessionId, boolean audio, int audioCodec, int audioPktLen, boolean audioTx, boolean audioRx, boolean video, int videoCodec, int videoSize, int videoFrameSize, boolean videoTx, boolean videoRx);
    void onSCallMediaInfo(String sessionId, boolean isConfMember, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort,
            String remoteAddress, Map codec);

    /**
     * 方法说明 : 呼出收到证实（100）
     * 适用场景：发起新呼叫，并成功发出invite
     * @param sessionId 会话ID
     * @param channel 通道号（1,2）
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallOutgoingAck(String sessionId, int channel);

    /**
     * 方法说明 : 收到回铃
     * 适用场景：收到180或183消息场景
     * 
     * @param sessionId 会话ID
     * @param ringNum 振铃号码
     * @param channel 通道号（1,2）
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallRingback(String sessionId, String ringNum, int channel);

    /**
     * 方法说明 : 呼叫对端应答
     * 适用场景：发起新呼叫后，收到对端200OK消息
     * 
     * @param sessionId 会话ID
     * @param callPriority 呼叫优先级
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallConnect(String sessionId, int callPriority);

    /**
     * 方法说明 : 通话确认
     * 适用场景：对新入呼发送200 OK后，收到ACK确认消息
     * @param sessionId 会话ID
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallConnectAck(String sessionId);

    /**
     * 方法说明 :呼叫释放
     * 适用场景：收到bye，cancel，refuse等，或呼叫处理失败需要释放呼叫的情况
     * @param sessionId 会话ID
     * @param reason 释放原因：1：系统忙，2：挂机，3: 拒绝
     * 失败原因，ServiceConstant.CALL_FAILED_TIMEOUT：呼叫超时（无人接听）；
     *                      ServiceConstant.CALL_FAILED_REFUSE：拒绝接听；
     *                      ServiceConstant.CALL_FAILED_BUSY：呼叫忙；
     *                      ServiceConstant.CALL_FAILED_OFFLINE：不在线；
     *                      ServiceConstant.SCALL_FAILED_UNREACHABLE：对方不可及（无法建立）；
     *                      ServiceConstant.CALL_FAILED_NOACCOUNT：空号；
     *                      ServiceConstant.CALL_FAILED_FORBID：没有权限；
     *                      ServiceConstant.CALL_FAILED_PREEMPTED：呼叫被抢占；
     *                      ServiceConstant.CALL_FAILED_PREEMPTFAILED：呼叫抢占失败；
     *                      ServiceConstant.CALL_FAILED_ACTIVEGROUP：加入呼叫中组失败；
     *                      ServiceConstant.CALL_FAILED_CELLID_NOTEXIST：没有小区号；
     *                      ServiceConstant.CALL_FAILED_FN_NOTEXIST：无主叫功能号；
     *                      ServiceConstant.CALL_FAILED_FN_FORBID：主叫功能号无权限
     *                      ServiceConstant.CALL_FAILED_GROUP_NOTEXIST：没有配置该群组。
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallRelease(String sessionId, int reason);

    /**
     * 方法说明 : 收到保持证实
     * 适用场景：发出invite（保持对端），并收到200 OK
     * 
     * @param sessionId 会话ID
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallHoldAck(String sessionId);

    /**
     * 方法说明 : 收到结束保持证实
     * 适用场景：发送invite（恢复对端），并收到200 OK
     * 
     * @param sessionId 会话ID
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallRetrieveAck(String sessionId);

    /**
     * 方法说明 : 收到远端保持
     * 适用场景：收到invite（对端保持），发送200 OK
     * 
     * @param sessionId 会话ID
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallRemoteHold(String sessionId);

    /**
     * 方法说明 :  收到远端解除保持
     * 适用场景：收到invite（对端恢复），发送200 OK
     * 
     * @param sessionId 会话ID
     * @author hubin
     * @Date 2015-1-14
     */
    void onSCallRemoteRetrieve(String sessionId);

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

    /**
     * 方法说明 : 被远端声音控制
     * @param sessionId
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-5-28
     */
    void onAudioEnable(String sessionId, boolean enable);

    /**
     * 方法说明 : 被远端视频控制
     * @param sessionId
     * @param enable
     * @return void
     * @author hubin
     * @Date 2015-5-28
     */
    void onVideoEnable(String sessionId, boolean enable);

    /**
     * 方法说明 : 被远端推送视频
     * @param sessionId
     * @param enable
     * @param videoNum
     * @param tag
     * @return void
     * @author hubin
     * @Date 2015-5-29
     */
    void onVideoShareReceived(String sessionId, boolean enable, String videoNum, String tag);

    /**
     * 方法说明 : 作为会议成员时媒体信息通知
     * @param sessionId
     * @param mediaTag
     * @return void
     * @author hubin
     * @Date 2015-5-29
     */
    void onMultiMediaInfoNotify(String sessionId, String mediaTag);
}
