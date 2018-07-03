package com.jiaxun.sdk.acl.module.conf.itf;

import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;

/**
 * 说明：会议业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclConfService
{
    /**
     * 方法说明 : 注册会议业务事件回调
     * @param callback 会议事件
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confRegEventListener(AclConfEventListener callback);

    /**
     * 方法说明 : 创建会议
     * @param sessionId 会话Id
     * @param callPriority 会议优先级
     * @param confType 1：全双工 2：通播 3：广播 
     * @param video 支持视频
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confCreate(String sessionId, int callPriority, int confType, boolean video) throws Exception;

    /**
     * 方法说明 : 关闭会议
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confClose(String sessionId) throws Exception;

    /**
     * 方法说明 : 进入会议
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confEnter(String sessionId) throws Exception;

    /**
     * 方法说明 : 退出会议
     * @param sessionId 会话Id 
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confLeave(String sessionId) throws Exception;

    /**
     * 方法说明 : 加入成员
     * @param sessionId 会话Id
     * @param userNum 号码
     * @param userType TERMINAL_USER = 0, 终端用户
     *                 MOBILEPHONE_USER, 移动用户
     *                 TELPHONE_USER, 固定用户
     *                 VIDEOPHONE_USER, 可视电话用户
     *                 BXPHONE_USER, 便携设备用户
     *                 MONITOR_USER, 监控用户
     *                 CARPHONE_USER, 车载设备用户
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserAdd(String sessionId,  String userNum) throws Exception;

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
    int confUserDelete(String sessionId, String userNum) throws Exception;

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
    int confUserAudioEnable(String sessionId, String userNum, boolean enable) throws Exception;

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
    int confUserVideoEnable(String sessionId, String userNum, boolean enable) throws Exception;

    /**
     * 方法说明 : 成员视频分享(广播)
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param tag 视频标识
     * @param enable 广播 / 不广播
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserVideoShare(String sessionId, String userNum, String tag, boolean enable) throws Exception;

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
    int confBgmEnable(String sessionId, boolean enable) throws Exception;
    
    /**
     * 方法说明 : 发送sip info类型的DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    void confSipInfoDTMFSend(String sessionId, char c) throws Exception;
    /**
     * 方法说明 : 发送inband类型的DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
     void confInbandDTMFSend(String sessionId, char c) throws Exception;
}
