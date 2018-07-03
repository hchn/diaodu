package com.jiaxun.sdk.scl.session.conf;

import java.util.ArrayList;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;
import com.jiaxun.sdk.acl.module.conf.itf.AclConfService;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfEventListener;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfUserEventListener;
import com.jiaxun.sdk.scl.module.conf.impl.SclConfServiceImpl;

/**
 * 说明：会议状态处理
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public interface ConfStateHandler extends AclConfEventListener
{
    AclConfService confService = AclServiceFactory.getAclConfService();
    SclConfEventListener confCallback = SclConfServiceImpl.getInstance().getSclConfEventListener();
    SclConfUserEventListener userCallback = SclConfServiceImpl.getInstance().getSclConfUserEventListener();
    
    /**
     * 方法说明 : 创建会议
     * @param sessionId 会话Id
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
    void createConf(String sessionId, String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList);

    /**
     * 方法说明 : 关闭会议
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    void closeConf(String sessionId);

    /**
     * 方法说明 : 进入会议
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    void returnConf(String sessionId);

    /**
     * 方法说明 : 退出会议
     * @param sessionId 会话Id 
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    void exitConf(String sessionId);

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
    void addUser(String sessionId, String userNum);

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
    void deleteUser(String sessionId, String userNum);

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
    void enableUserAudio(String sessionId, String userNum, boolean enable);

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
    void enableUserVideo(String sessionId, String userNum, boolean enable);

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
    void shareUserVideo(String sessionId, String userNum, boolean enable);

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
    void enableConfBgm(String sessionId, boolean enable);

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
    void setAudioMute(String sessionId, boolean mute);

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
    void setVideoMute(String sessionId, boolean mute);
    
    /**
     * 方法说明 : 二次拨号
     * @param sessionId
     * @param dtmf
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-9-7
     */
    void sendDtmf(String sessionId, char dtmf) throws Exception;

}
