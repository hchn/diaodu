package com.jiaxun.sdk.scl.module.scall.callback;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.SCallModel;

/**
 * 说明：单呼事件通知接口
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclSCallEventListener
{
    /**
     * 方法说明 : 单呼状态改变通知
     * @param sessionId 会话Id
     * @param status 呼出状态：DIALP/ROCEEDING/RINGBACK/CONNECT
     *               呼入状态：RINGING/CONNECT
     *               呼入呼出：IDLE/HOLDACK/HOLD/REMOTEHOLD
     * @param info 主叫号码、被叫号码、呼叫优先级、主叫功能码、被叫功能码、振铃号码、应答号码、释放原因、通道信息等
     * @param reason 原因
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onSCallStatusChange(String sessionId, int status, SCallModel info, int reason);

    /**
     * 方法说明 : 本地视频启动通知
     * @param sessionId 会话Id
     * @param localVideoView 本地视频窗口
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
//    void onSCallLocalVideoStarted(String sessionId, SurfaceView localVideoView);

    /**
     * 方法说明 : 远端视频启动通知
     * @param sessionId 会话Id
     * @param remoteVideoView 远端视频窗口
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onSCallremoteVideoStarted(String sessionId, String number, SurfaceView remoteVideoView);
    
    /**
     * 方法说明 : 远端视频关闭通知
     * @param sessionId 会话Id
     * @param remoteVideoView 远端视频窗口
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onSCallremoteVideoStoped(String sessionId, String number);
    
    /**
     * 方法说明 : 远端声音控制通知
     * @param sessionId 会话Id
     * @param enable 开/关
     * @return void
     * @author hubin
     * @Date 2015-9-21
     */
    void onSclCallAudioEnable(String sessionId, boolean enable);

}
