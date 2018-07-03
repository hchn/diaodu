package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import android.content.Context;
import android.media.AudioManager;

import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：保持对方
 *
 * @author  hubin
 *
 * @Date 2015-1-5
 */
public class SCallHoldState extends SCallState
{
    public SCallHoldState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallHoldState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_HOLD;
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        sCallService.sCallRelease(sessionId, reason);
        changeCallState(callSession.idleState, reason);
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
    public void retrieveCall(String sessionId) throws Exception
    {
        super.retrieveCall(sessionId);
//        if (SessionManager.getInstance().handleConnectedCalls(sessionId)) //hz delete
//        {
//            SystemClock.sleep(500);
//        }
        changeCallState(callSession.retieveAckState);
        sCallService.sCallRetrieve(sessionId);
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        // 通话记录中失败原因重新定义
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
        changeCallState(callSession.idleState, reason);
        callSession.getCallRecord().setReleaseReason(reason);
        callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(callSession.getCallRecord());
        callSession.handleCallRecord(callSession.getCallRecord());
        callSession.recycle();
    }

    @Override
    public void onSCallRemoteHold(String sessionId)
    {
        Log.info(TAG, "onSCallRemoteHold:: sessionId:" + sessionId);
        if (!callSession.getCallModel().isHolded())
        {
            callSession.getCallModel().setHolded(true);
            VoiceUtil.playHold(callSession.getChannel());
            callback.onSCallStatusChange(sessionId, CommonConstantEntry.SCALL_STATE_BOTH_HOLD, callSession.getCallModel(), CommonConstantEntry.Q850_NOREASON);
        }
    }

    @Override
    public void onSCallRemoteRetrieve(String sessionId)
    {
        Log.info(TAG, "onSCallRemoteRetrieve:: sessionId:" + sessionId);
        if (callSession.getCallModel().isHolded())
        {
            callSession.getCallModel().setHolded(false);
            callback.onSCallStatusChange(sessionId, CommonConstantEntry.SCALL_STATE_HOLD, callSession.getCallModel(), CommonConstantEntry.Q850_NOREASON);
            VoiceUtil.stopHold();
        }
    }

    @Override
    public void setAudioSpeaker(boolean on) throws Exception
    {
        Log.info(TAG, "setAudioSpeaker::" + on);
        AudioManager am = (AudioManager) SdkUtil.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (on)
        {// 打开扬声器
            if (!am.isSpeakerphoneOn())
            {
                am.setSpeakerphoneOn(true);// 打开扬声器
            }
        }
        else
        {// 关闭扬声器
            if (am.isSpeakerphoneOn())
            {
                am.setSpeakerphoneOn(false);// 关闭扬声器
            }
        }
    }

}
