package com.jiaxun.sdk.scl.module.scall.itf;

import com.jiaxun.sdk.scl.module.scall.callback.SclSCallEventListener;

/**
 * 说明：单呼业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface SclSCallService
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
    int sCallRegEventListener(SclSCallEventListener callback);

    /**
     * 方法说明 : 发起新的呼出
     * 适用场景: 用户主动发起呼叫。
     * @param callPriority 呼叫优先级，范围：0-4，-1表示不带优先级
     * @param callerNum 主叫号码
     * @param callerName 主叫名称（值班员）
     * @param funcCode GSM-R功能码
     * @param calleeNum 被叫号码
     * @param channel 呼出通道号（1,2）
     * @param video 支持视频
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallMake(int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, String calleeName, int channel, boolean video);

    /**
     * 方法说明 : 应答呼叫
     * 适用场景：用户响应来呼，进行接听操作。
     * @param sessionId 呼叫会话ID
     * @param name 值班员或终端名称
     * @param channel 呼出通道号（1,2）
     * @param video 支持视频
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallAnswer(String sessionId, String name, int channel, boolean video);

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
    int sCallRelease(String sessionId, int reason);

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
    int sCallHold(String sessionId);

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
    int sCallRetrieve(String sessionId);

    /**
     * 方法说明 : 允许/禁止声音
     * @param sessionId 呼叫会话ID
     * @param enable 打开/ 关闭
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int sCallAudioEnable(String sessionId, boolean enable);

    /**
     * 方法说明 : 允许/禁止视频
     * @param sessionId 呼叫会话ID
     * @param enable 打开/ 关闭
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int sCallVideoEnable(String sessionId, boolean enable);

    /**
     * 方法说明 : 发送dtmf
     * @param sessionId 呼叫会话ID
     * @param dtmf 按键
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int sCallDtmfSend(String sessionId, char dtmf);

    /**
     * 方法说明 : 闭铃控制
     * @param sessionId 呼叫会话ID
     * @param enable 开关
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-9-9
     */
    int sCallCloseRing(String sessionId, boolean enable);
}
