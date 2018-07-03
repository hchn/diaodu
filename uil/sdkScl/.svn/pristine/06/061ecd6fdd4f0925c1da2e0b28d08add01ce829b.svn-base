package com.jiaxun.sdk.scl.session.scall;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;
import com.jiaxun.sdk.util.timer.TimerListener;

/**
 * 说明：单呼状态机功能接口与通知接口
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class SCallState implements TimerListener, SCallStateHandler
{
    public String TAG;
    protected Timer timer;
    protected SCallSession callSession;
    protected int timeout = -1;

    public SCallState(SCallSession callSession)
    {
        this.callSession = callSession;
    }

    public void startTimer()
    {
        if (timeout >= 0)
        {
            Log.info(TAG, "startTimer:: timeout:" + timeout);
            timer = new Timer(timeout, this);
            timer.start();
        }
    }

    public void haltTimer()
    {
        if (timer != null)
        {
            Log.info(TAG, "haltTimer:: timeout:" + timeout);
            timer.halt();
        }
    }

    public void pauseAudioVideoMedia()
    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
                // 停止音频收发
                JAudioLauncher audioLauncher = callSession.getAudioLauncher();
                if (audioLauncher != null)
                {
                    audioLauncher.setMute(true);
                    audioLauncher.setReceive(false);
                }

                // 停止视频发送
                JVideoLauncher videoLauncher = callSession.getVideoLauncher();
                if (videoLauncher != null)
                {
                    videoLauncher.stopLocalVideoSender();
                }
//            }
//        }).start();
    }

    public void changeCallState(SCallState state)
    {
        if (callSession != null)
        {
            callSession.currentState.haltTimer();
            callSession.currentState = state;
            callSession.currentState.startTimer();
            callSession.setStatus(callSession.getSessionState());
            callback.onSCallStatusChange(callSession.getSessionId(), callSession.getStatus(), callSession.getCallModel(), CommonConstantEntry.Q850_NOREASON);
        }
    }

    public void changeCallState(SCallState state, int reason)
    {
        if (callSession != null)
        {
            callSession.currentState.haltTimer();
            callSession.currentState = state;
            callSession.currentState.startTimer();
            callSession.setStatus(callSession.getSessionState());
            callback.onSCallStatusChange(callSession.getSessionId(), callSession.getStatus(), callSession.getCallModel(), reason);
        }
    }

    @Override
    public void onTimeout(Timer t)
    {
        try
        {
            Log.info(TAG, "onTimeout:: time:" + t.getTime());
            if (callSession != null)
            {
                releaseCall(callSession.getSessionId(), CommonConstantEntry.CALL_END_TIMEOUT);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onSCallIncoming(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean audio, boolean video)
    {
    }

    @Override
    public void onSCallMediaInfo(String sessionId, boolean isConfMember, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort,
            String remoteAddress, Map codec)
    {
    }

    @Override
    public void onSCallOutgoingAck(String sessionId, int channel)
    {
    }

    @Override
    public void onSCallRingback(String sessionId, String ringNum, int channel)
    {
    }

    @Override
    public void onSCallConnect(String sessionId, int callPriority)
    {
    }

    @Override
    public void onSCallConnectAck(String sessionId)
    {
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        Log.info(TAG, "onSCallRelease:: sessionId:" + sessionId + " reason:" + reason);
        VoiceUtil.stopHold();
        VoiceUtil.playCallRelease(callSession.getChannel(), reason);
        if (callSession.getCallModel().isCloseRing())
        {
            SessionManager.getInstance().setCloseRing(false);
        }
    }

    @Override
    public void onSCallHoldAck(String sessionId)
    {
    }

    @Override
    public void onSCallRetrieveAck(String sessionId)
    {
    }

    @Override
    public void onSCallRemoteHold(String sessionId)
    {
    }

    @Override
    public void onSCallRemoteRetrieve(String sessionId)
    {
    }

    @Override
    public void makeCall(String sessionId, String callNum, String callName, int priority, boolean video, int channel) throws Exception
    {
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        Log.info(TAG, "releaseCall:: sessionId:" + sessionId + " reason:" + reason);
        VoiceUtil.stopHold();
        if (callSession.getCallModel().isCloseRing())
        {
            SessionManager.getInstance().setCloseRing(false);
        }
    }

    @Override
    public void answerCall(String sessionId, String name, int channel, boolean video) throws Exception
    {
    }

    @Override
    public void holdCall(String sessionId) throws Exception
    {
    }

    @Override
    public void retrieveCall(String sessionId) throws Exception
    {
    }

    @Override
    public void setAudioMute(boolean on) throws Exception
    {
    }

    @Override
    public void setAudioSpeaker(boolean on) throws Exception
    {
    }

    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        callSession.getCallRecord().setRecordTaskId(taskId);
        callSession.getCallRecord().setRecordServer(server);
    }

    @Override
    public void onAudioEnable(String sessionId, boolean enable)
    {
    }

    @Override
    public void onVideoEnable(String sessionId, boolean enable)
    {
    }

    @Override
    public void onVideoShareReceived(String sessionId, boolean enable, String videoNum, String tag)
    {
    }

    @Override
    public void onMultiMediaInfoNotify(String sessionId, String mediaTag)
    {
    }

    @Override
    public void setCloseRing(String sessionId, boolean enable) throws Exception
    {
    }

    @Override
    public void sendDtmf(String sessionId, char dtmf) throws Exception
    {
        switch (SessionManager.getInstance().getMediaConfig().dtmfMode)
        {
            case CommonConstantEntry.DTMF_MODE_INBAND:
                sCallService.sCallInbandDTMFSend(sessionId, dtmf);
                break;
            case CommonConstantEntry.DTMF_MODE_RFC2833:
                if (callSession.getAudioLauncher() != null)
                {
                    callSession.getAudioLauncher().sendDTMF(dtmf);
                }
                break;
            case CommonConstantEntry.DTMF_MODE_SIP_INFO:
                sCallService.sCallSipInfoDTMFSend(sessionId, dtmf);
                break;

            default:
                break;
        }
    }

    public int transferReason(int reason)
    {
        int result = reason;
        switch (reason)
        {
            case CommonConstantEntry.CALL_FAILED_FORBID:
                result = CommonConstantEntry.CALL_END_FORBID;
                break;
            case CommonConstantEntry.CALL_FAILED_REFUSE:
                result = CommonConstantEntry.CALL_END_PEER_BUSY;
                break;
            case CommonConstantEntry.SIP_CALL_DND:
                result = CommonConstantEntry.CALL_END_DND;
                break;
            case CommonConstantEntry.CALL_FAILED_PRESIDENT_RELEASE:
                result = CommonConstantEntry.CALL_END_PRESIDENT_RELEASE;
                break;
            default:
                break;
        }
        return result;
    }

}
