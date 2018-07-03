package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：呼入振铃(发送180/183后)
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class SCallRingingState extends SCallState
{
    public SCallRingingState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallRingingState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_RINGING;
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        sCallService.sCallRelease(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        VoiceUtil.stopIncomingCallRing();
        // 通话记录中失败原因重新定义
        reason = CommonConstantEntry.CALL_END_PEER_RELEASE;
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//            callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

    @Override
    public void answerCall(String sessionId, String name, int channel, boolean video) throws Exception
    {
        Log.info(TAG, "answerCall:: sessionId:" + sessionId + " name:" + name);
//        if (SessionManager.getInstance().handleConnectedCalls(sessionId))//hz delete
//        {
//            SystemClock.sleep(500);
//        }
        changeCallState(callSession.connectAckState);
        VoiceUtil.stopIncomingCallRing();
        callSession.getCallModel().setVideo(video);
        sCallService.sCallAnswer(sessionId, name, channel, true, video);
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        if (reason <= 0)
        {
            reason = CommonConstantEntry.CALL_END_CALLER_RELEASE;
        }
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        VoiceUtil.stopIncomingCallRing();
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }
}
