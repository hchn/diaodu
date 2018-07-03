package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.video.RemoteVideoView;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;

/**
 * ˵�����ָ��ȴ�ȷ��״̬
 *
 * @author  hubin
 *
 * @Date 2015-1-5
 */
public class SCallRetrieveAckState extends SCallState
{
    public SCallRetrieveAckState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallRetrieveAckState.class.getName();
        timeout = CommonConstantEntry.SCALL_TIMEOUT_RETRIEVE_ACK;
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        sCallService.sCallRelease(sessionId, reason);
        changeCallState(callSession.idleState, reason);
        // ͨ����¼��ʧ��ԭ�����¶���
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
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
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
    public void onSCallRetrieveAck(final String sessionId)
    {
        Log.info(TAG, "onSCallRetrieveAck:: sessionId:" + sessionId);
        changeCallState(callSession.connectState);
        if (!callSession.getCallModel().isHolded())
        {
            // ������Ƶ�շ�
            JAudioLauncher audioLauncher = callSession.getAudioLauncher();
            if (audioLauncher != null)
            {
                audioLauncher.setMute(false);
                audioLauncher.setReceive(true);
            }
            // ���ֱ��ָ�����������
            MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
            if (callSession.getVideoLauncher() != null && callSession.getCallModel().isVideo() && mediaConfig != null)
            {
                RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                        mediaConfig.videoFrameRate, mediaConfig.videoBitRate, callSession.getMediaTag(), callSession.getVideoLauncher());
                callback.onSCallremoteVideoStarted(sessionId, callSession.getCallModel().getPeerNum(), remoteVideoView);
            }
        }
    }

    @Override
    public void onTimeout(Timer t)
    {
        Log.info(TAG, "onTimeout:: sessionId:" + callSession.getSessionId());
        changeCallState(callSession.holdState);
    }
}
