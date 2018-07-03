package com.jiaxun.sdk.scl.module.vs.callback;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.VsModel;


/**
 * 说明：视频监控事件通知接口
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclVsEventListener
{
    /**
     * 方法说明 : 视频监控业务状态改变通知
     * @param sessionId 会话Id
     * @param status  IDLE / DIAL / CONNECT 
     * @param vsModel 监控对象 
     * @param reason 原因
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onVsStatusChange(String sessionId, int status, VsModel vsModel, int reason);
    
    /**
     * 方法说明 : 视频流接收通知
     * @param sessionId 会话Id
     * @param videoUrl
     * @param videoView
     * @return void
     * @author hubin
     * @Date 2015-5-25
     */
    void onVsVideoReceived(String sessionId, String videoUrl, SurfaceView videoView);

//    /**
//     * 方法说明 : 呼叫记录通知
//     * @param callRecord 通话详细记录
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    void onSclCallRecordReport(CallRecord callRecord);
}
