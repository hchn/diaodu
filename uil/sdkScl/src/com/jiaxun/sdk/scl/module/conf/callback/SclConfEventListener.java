package com.jiaxun.sdk.scl.module.conf.callback;

import com.jiaxun.sdk.scl.model.ConfModel;

/**
 * 说明：会议事件通知接口
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclConfEventListener
{
    /**
     * 方法说明 : 会议状态改变通知
     * @param sessionId 会话Id
     * @param status IDLE / DIAL / CONNECT/ LEAVEACK / LEAVE /ENTERACK
     * @param info 主席号码、成员号码、呼叫优先级、通道信息等
     * @param reason 原因
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onConfStatusChange(String sessionId, int status, ConfModel info, int reason);
    
    /**
     * 方法说明 : 会议提示音开关响应
     * @param sessionId 会话Id
     * @param enable 开 / 关
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    void onConfBgmEnable(String sessionId, boolean enable);
    

//    /**
//     * 方法说明 : 呼叫记录通知
//     * @param callRecord 通话详细记录
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    void onSclCallRecordReport(CallRecord callRecord);
}
