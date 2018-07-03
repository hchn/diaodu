package com.jiaxun.sdk.acl.module.scall.itf;

import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;

/**
 * 说明：单呼业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclSCallService
{
    /**
     * 方法说明 : 注册单呼事件回调
     * @param callback
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallRegEventListener(AclSCallEventListener callback);
    
    /**
     * 方法说明 : 发起新的呼出
     * 适用场景: 用户主动发起呼叫。
     * @param sessionId 呼叫会话ID
     * @param callPriority 呼叫优先级，范围：0-4，-1表示不带优先级
     * @param callerNum 主叫号码
     * @param callerName 主叫名称（值班员）
     * @param funcCode GSM-R功能码
     * @param calleeNum 被叫号码
     * @param channel 呼出通道号（1,2）
     * @param audio 支持音频
     * @param video 支持视频
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallMake(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel, boolean audio, boolean video) throws Exception;

    /**
     * 方法说明 : 发送振铃通知
     * 适用场景: 用户收到新呼叫后，向对端告知振铃状态
     * @param sessionId 呼叫会话ID
     * @param name 值班员或终端名称
     * @param channel 通道号（1,2）
     * @param sendRbt 本地送回铃：发183
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallAlerting(String sessionId, String name, int channel, boolean sendRbt) throws Exception;
    
    /**
     * 方法说明 : 应答呼叫
     * 适用场景：用户响应来呼，进行接听操作。
     * @param sessionId 呼叫会话ID
     * @param name 值班员或终端名称
     * @param channel 呼出通道号（1,2）
     * @param audio 支持音频
     * @param video 支持视频
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallAnswer(String sessionId, String name, int channel, boolean audio, boolean video) throws Exception;

    /**
     * 方法说明 : 结束通话
     * 适用场景：呼叫挂断、呼叫取消。呼叫拒接等。
     * @param sessionId 呼叫会话ID
     * @param reason 释放原因：1:系统忙,2:挂机,3:拒绝,4:免打扰
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallRelease(String sessionId, int reason) throws Exception;

    /**
     * 方法说明 : 呼叫保持
     * 适用场景: 用户保持某个呼叫
     * @param sessionId 呼叫会话ID
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
     int sCallHold(String sessionId) throws Exception;

    /**
     * 方法说明 : 呼叫恢复
     * 适用场景: 用户恢复某个呼叫。
     * @param sessionId 呼叫会话ID
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
   int sCallRetrieve(String sessionId) throws Exception;
   
   /**
    * 方法说明 : 发送sip info类型的DTMF
    * @param sessionId
    * @param c
    * @throws Exception
    * @return void
    * @author hubin
    * @Date 2015-2-5
    */
   void sCallSipInfoDTMFSend(String sessionId, char c) throws Exception;
   /**
    * 方法说明 : 发送inband类型的DTMF
    * @param sessionId
    * @param c
    * @throws Exception
    * @return void
    * @author hubin
    * @Date 2015-2-5
    */
    void sCallInbandDTMFSend(String sessionId, char c) throws Exception;
    
//    /**
//     * 方法说明 : 远端保持证实
//     * 适用场景: 收到远端保持请求，发送保持响应。
//     * @param sessionId 呼叫会话ID
//     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
//     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
//     *         CommonConstantEntry.PARAM_ERROR：参数错误。
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    int sCallRemoteHoldAck(String sessionId) throws Exception;
//
//    /**
//     * 方法说明 : 远端解除保持证实
//     * 适用场景: 收到远端解除保持请求，发送解除保持响应。
//     * @param sessionId 呼叫会话ID
//     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
//     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
//     *         CommonConstantEntry.PARAM_ERROR：参数错误。
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    int sCallRemoteRetrieveAck(String sessionId) throws Exception;
}
