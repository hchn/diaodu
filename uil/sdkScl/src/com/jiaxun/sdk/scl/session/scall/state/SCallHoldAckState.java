package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;

/**
 * 说明：保持等待响应
 *
 * @author  hubin
 *
 * @Date 2015-1-5
 */
public class SCallHoldAckState extends SCallState
{
    public SCallHoldAckState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallHoldAckState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_HOLD_ACK;
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState);
        sCallService.sCallRelease(sessionId, reason);
        // 通话记录中失败原因重新定义
        if (callSession.getCallRecord().isOutGoing())
        {
            reason = CommonConstantEntry.CALL_END_CALLER_RELEASE;
        }
        else
        {
            reason = CommonConstantEntry.CALL_END_PEER_RELEASE;
        }
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        if (reason <= 0)
        {
            if (callSession.getCallRecord().isOutGoing())
            {
                reason = CommonConstantEntry.CALL_END_PEER_RELEASE;
            }
            else
            {
                reason = CommonConstantEntry.CALL_END_CALLER_RELEASE;
            }
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
    public void onSCallHoldAck(String sessionId)
    {
        Log.info(TAG, "onSCallHoldAck:: sessionId:" + sessionId);
        // 释放闭铃状态
        if (callSession.getCallModel().isCloseRing())
        {
            callSession.getCallModel().setCloseRing(false);
            SessionManager.getInstance().setCloseRing(false);
        }
        changeCallState(callSession.holdState);
        if (!callSession.getCallModel().isHolded())
        {
            pauseAudioVideoMedia();
        }
        
    }

    @Override
    public void onTimeout(Timer t)
    {
        Log.info(TAG, "onTimeout:: sessionId:" + callSession.getSessionId());
        changeCallState(callSession.connectState);
    }
}
