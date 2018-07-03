package com.jiaxun.sdk.scl.module.conf.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfEventListener;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfUserEventListener;

/**
 * 说明：会议业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface SclConfService
{
    /**
     * 方法说明 : 注册会议业务事件回调
     * @param confCallback 会议事件
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confRegEventListener(SclConfEventListener confCallback);
    
    /**
     * 方法说明 : 注册会议成员业务事件回调
     * @param userCallback 会议成员事件
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserRegEventListener(SclConfUserEventListener userCallback);

    /**
     * 方法说明 : 创建会议
     * @param confName 会议名称
     * @param callPriority 会议优先级
     * @param confType 1：全双工 2：通播 3：广播 
     * @param channel 呼出通道号（1,2）
     * @param video 支持视频
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confCreate(String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList);

    /**
     * 方法说明 : 关闭会议
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confClose(String sessionId);

    /**
     * 方法说明 : 进入会议
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confEnter(String sessionId);

    /**
     * 方法说明 : 退出会议
     * @param sessionId 会话Id 
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confLeave(String sessionId);

    /**
     * 方法说明 : 加入成员
     * @param sessionId 会话Id
     * @param userNum 号码
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserAdd(String sessionId, String userNum);

    /**
     * 方法说明 : 删除成员
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserDelete(String sessionId, String userNum);

    /**
     * 方法说明 : 成员发言控制
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 允许 / 禁止
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserAudioEnable(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 成员视频控制
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 显示 / 不显示
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserVideoEnable(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 成员视频分享(广播)
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 广播 / 不广播
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserVideoShare(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 会议背景音乐控制
     * @param sessionId 会话Id
     * @param enable 播放 / 停止
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confBgmEnable(String sessionId, boolean enable);

    /**
     * 方法说明 :  静音/解除
     * @param sessionId 会话Id
     * @param mute 静音/解除
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int confAudioMute(String sessionId, boolean mute);
    
    /**
     * 方法说明 :  视频静像
     * @param sessionId 会话Id
     * @param mute 静像/解除
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-30
     */
    int confVideoMute(String sessionId, boolean mute);
    
}
