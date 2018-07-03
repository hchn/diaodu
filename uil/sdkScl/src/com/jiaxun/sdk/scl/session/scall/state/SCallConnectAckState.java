package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：应答对方（200 OK），等待对端证实（ACK）
 *
 * @author  hubin
 *
 * @Date 2015-2-6
 */
public class SCallConnectAckState extends SCallState
{
    public SCallConnectAckState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallConnectAckState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_CONNECT_ACK;
    }

    @Override
    public void onSCallConnectAck(String sessionId)
    {
        Log.info(TAG, "onSCallConnectAck:: sessionId:" + sessionId);
        callSession.getCallModel().setConnectTime(Calendar.getInstance().getTimeInMillis());
        callSession.getCallRecord().setConnectTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.connectState);
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        if (reason <= 0)
        {
            reason = CommonConstantEntry.CALL_END_NO_RESPONSE;
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
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        sCallService.sCallRelease(sessionId, reason);
        // 通话记录中失败原因重新定义
        reason = CommonConstantEntry.CALL_END_PEER_NO_RESPONSE;
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }
}
