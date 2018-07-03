package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：收到(100 trying)证实，等待(180/183)回铃
 *
 * @author  hubin
 *
 * @Date 2015-2-6
 */
public class SCallProceedingState extends SCallState
{
    public SCallProceedingState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallProceedingState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_PROCEEDING;
    }

    @Override
    public void onSCallRingback(String sessionId, String ringNum, int channel)
    {
        Log.info(TAG, "onSCallRingback:: sessionId:" + sessionId);
        changeCallState(callSession.ringbackState);
        callSession.setChannel(channel);
        VoiceUtil.playRingback(channel);
    }

    @Override
    public void onSCallConnect(String sessionId, int callPriority)
    {
        // 该场景为对端自动应答，未发送180的场景
        Log.info(TAG, "onSCallConnect:: sessionId:" + sessionId);
        callSession.getCallModel().setPriority(callPriority);
        callSession.getCallRecord().setCallPriority(callPriority);
        callSession.getCallModel().setConnectTime(Calendar.getInstance().getTimeInMillis());
        callSession.getCallRecord().setConnectTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.connectState);
        // TODO:playMedia
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        if (reason <= 0)
        {
            reason = CommonConstantEntry.CALL_END_SPACE_OR_USER_NO_ONLINE;
        }

        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        // 播放呼叫超时提示
        if (reason == CommonConstantEntry.CALL_END_TIMEOUT)
        {
            VoiceUtil.playCallRelease(callSession.getChannel(), reason);
        }
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, CommonConstantEntry.CALL_END_CALL_CANCEL);
        sCallService.sCallRelease(sessionId, reason);
        // 通话记录中失败原因重新定义
        reason = CommonConstantEntry.CALL_END_CALL_CANCEL;
        callSession.getCallRecord().setReleaseReason(CommonConstantEntry.CALL_END_CALL_CANCEL);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

}
