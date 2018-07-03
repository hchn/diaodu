package com.jiaxun.sdk.scl.module.conf.callback;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.ConfMemModel;

/**
 * 说明：会议成员事件通知接口
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclConfUserEventListener
{
    /**
     * 方法说明 : 会议成员状态改变通知
     * @param sessionId 会话Id
     * @param status IDLE / RING / CONNECT 
     * @param info 号码等
     * @param reason 原因
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onConfUserStatusChange(String sessionId, int status, ConfMemModel info, int reason);
    
    /**
     * 方法说明 : 会议成员视频通知
     * @param sessionId 会话Id
     * @param remoteVideoView 远端视频窗口
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onConfUserVideoStarted(String sessionId, String userNum, SurfaceView userVideoView);
    
    /**
     * 方法说明 : 成员发言控制改变
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 开 / 关
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserAudioChanged(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 成员视频控制改变
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 开 / 关
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserVideoChanged(String sessionId, String userNum, boolean enable);

    /**
     * 方法说明 : 成员视频分享（广播）改变
     * @param sessionId 会话Id
     * @param userNum 用户号码
     * @param enable 开 / 关
     * @author hubin
     * @Date 2015-1-23
     */
    void onConfUserVideoShareChanged(String sessionId, String userNum, boolean enable);
    
}
