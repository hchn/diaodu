package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：收到回铃(180/183)，等待应答(200 OK)
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class SCallRingbackState extends SCallState
{
    public SCallRingbackState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallRingbackState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_RINGBACK;
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        VoiceUtil.stopRingback();
        sCallService.sCallRelease(sessionId, reason);
        // 通话记录中失败原因重新定义
        reason = CommonConstantEntry.CALL_END_CALL_CANCEL;
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

    @Override
    public void onSCallConnect(String sessionId, int callPriority)
    {
        Log.info(TAG, "onSCallConnect:: sessionId:" + sessionId + " callPriority:" + callPriority);
        callSession.getCallModel().setPriority(callPriority);
        callSession.getCallModel().setConnectTime(Calendar.getInstance().getTimeInMillis());
        callSession.getCallRecord().setConnectTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.connectState);
        VoiceUtil.stopRingback();
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        VoiceUtil.stopRingback();
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        if (reason <= 0)
        {
            reason = CommonConstantEntry.CALL_END_PEER_BUSY;
        }
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }
}
