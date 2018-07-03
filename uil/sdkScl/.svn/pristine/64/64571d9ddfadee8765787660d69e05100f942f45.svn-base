package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：呼出(invite)，等待证实(100 trying)
 *
 * @author  hubin
 *
 * @Date 2015-2-6
 */
public class SCallDialState extends SCallState
{
    public SCallDialState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallDialState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_DIAL;
    }

    @Override
    public void onSCallOutgoingAck(String sessionId, int channel)
    {
        Log.info(TAG, "onSCallOutgoingAck:: sessionId:" + sessionId);
        changeCallState(callSession.proceedingState);
        callSession.setChannel(channel);
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        if (reason <= 0)
        {
            reason = CommonConstantEntry.CALL_END_PEER_BUSY;
        }
        changeCallState(callSession.idleState, reason);
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        sCallService.sCallRelease(sessionId, reason);
        // 通话记录中失败原因重新定义
        reason = CommonConstantEntry.CALL_END_CALL_CANCEL;
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }
}
