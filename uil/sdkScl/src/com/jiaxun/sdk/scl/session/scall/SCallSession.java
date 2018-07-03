package com.jiaxun.sdk.scl.session.scall;

import java.util.Calendar;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.model.SCallModel;
import com.jiaxun.sdk.scl.session.Session;
import com.jiaxun.sdk.scl.session.scall.state.SCallConnectAckState;
import com.jiaxun.sdk.scl.session.scall.state.SCallConnectState;
import com.jiaxun.sdk.scl.session.scall.state.SCallDialState;
import com.jiaxun.sdk.scl.session.scall.state.SCallHoldAckState;
import com.jiaxun.sdk.scl.session.scall.state.SCallHoldState;
import com.jiaxun.sdk.scl.session.scall.state.SCallIdleState;
import com.jiaxun.sdk.scl.session.scall.state.SCallProceedingState;
import com.jiaxun.sdk.scl.session.scall.state.SCallRetrieveAckState;
import com.jiaxun.sdk.scl.session.scall.state.SCallRingbackState;
import com.jiaxun.sdk.scl.session.scall.state.SCallRingingState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：作为呼叫过程中的数据承载类
 *
 * @author  hubin
 *
 * @Date 2014-1-5
 */
public class SCallSession extends Session implements SCallStateHandler
{
    private static String TAG = SCallSession.class.getName();
    public SCallState idleState;
    public SCallState connectState;
    public SCallState ringingState;
    public SCallState ringbackState;
    public SCallState holdState;
    public SCallState holdAckState;
    public SCallState connectAckState;
    public SCallState dialState;
    public SCallState proceedingState;
    public SCallState retieveAckState;

    public SCallState currentState;
    private SCallModel callModel;
    private CallRecord callRecord;

    private JAudioLauncher audioLauncher;
    private JVideoLauncher videoLauncher;
    
    private int channel;
    private String mediaTag;

    public SCallSession()
    {
        idleState = new SCallIdleState(this);
        connectState = new SCallConnectState(this);
        ringingState = new SCallRingingState(this);
        ringbackState = new SCallRingbackState(this);
        holdState = new SCallHoldState(this);
        holdAckState = new SCallHoldAckState(this);
        connectAckState = new SCallConnectAckState(this);
        dialState = new SCallDialState(this);
        proceedingState = new SCallProceedingState(this);
        retieveAckState = new SCallRetrieveAckState(this);

        currentState = idleState;
    }

    public SCallModel getCallModel()
    {
        return (SCallModel) callModel;
    }

    public void setCallModel(SCallModel callModel)
    {
        this.callModel = callModel;
    }

    public String toString()
    {
        if (callModel != null)
        {
            return callModel.toString();
        }
        return "";
    }

    public void handleException(Exception e)
    {
        Log.exception(TAG, e);
        // 异常清空释放通话
        getCallRecord().setReleaseReason(CommonConstantEntry.CALL_END_CALL_CANCEL);
        getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
        handleCallRecord(getCallRecord());
        callback.onSCallStatusChange(getSessionId(), CommonConstantEntry.SCALL_STATE_IDLE, getCallModel(), CommonConstantEntry.Q850_NOREASON);
        recycle();
    }

    public int getSessionState()
    {
        if (currentState instanceof SCallIdleState)
        {
            return CommonConstantEntry.SCALL_STATE_IDLE;
        }
        else if (currentState instanceof SCallDialState)
        {
            return CommonConstantEntry.SCALL_STATE_DIAL;
        }
        else if (currentState instanceof SCallProceedingState)
        {
            return CommonConstantEntry.SCALL_STATE_PROCEEDING;
        }
        else if (currentState instanceof SCallRingbackState)
        {
            return CommonConstantEntry.SCALL_STATE_RINGBACK;
        }
        else if (currentState instanceof SCallConnectAckState)
        {
            return CommonConstantEntry.SCALL_STATE_CONNECT_ACK;
        }
        else if (currentState instanceof SCallConnectState && callModel.isHolded())
        {
            return CommonConstantEntry.SCALL_STATE_REMOTE_HOLD;
        }
        else if (currentState instanceof SCallConnectState)
        {
            return CommonConstantEntry.SCALL_STATE_CONNECT;
        }
        else if (currentState instanceof SCallRingingState)
        {
            return CommonConstantEntry.SCALL_STATE_RINGING;
        }
        else if (currentState instanceof SCallHoldState && callModel.isHolded())
        {
            return CommonConstantEntry.SCALL_STATE_BOTH_HOLD;
        }
        else if (currentState instanceof SCallHoldState)
        {
            return CommonConstantEntry.SCALL_STATE_HOLD;
        }
        else if (currentState instanceof SCallHoldAckState)
        {
            return CommonConstantEntry.SCALL_STATE_HOLD_ACK;
        }
        else if (currentState instanceof SCallRetrieveAckState)
        {
            return CommonConstantEntry.SCALL_STATE_RETRIEVE_ACK;
        }
        return CommonConstantEntry.SCALL_STATE_IDLE;
    }

    @Override
    public void makeCall(String sessionId, String callNum, String callName, int priority, boolean video, int channel) throws Exception
    {
        currentState.makeCall(sessionId, callNum, callName, priority, video, channel);
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        currentState.releaseCall(sessionId, reason);
    }

    @Override
    public void answerCall(String sessionId, String name, int channel, boolean video) throws Exception
    {
        currentState.answerCall(sessionId, name, channel, video);
    }

    @Override
    public void holdCall(String sessionId) throws Exception
    {
        currentState.holdCall(sessionId);
    }

    @Override
    public void retrieveCall(String sessionId) throws Exception
    {
        currentState.retrieveCall(sessionId);
    }

    @Override
    public void onSCallIncoming(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean isConf, boolean video)
    {
        currentState.onSCallIncoming(sessionId, callPriority, callerNum, callerName, funcCode, calleeNum, channel, isConf, video);

    }

    @Override
    public void onSCallMediaInfo(String sessionId, boolean isConfMember, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort, String remoteAddress,
            Map codec)
    {
        currentState.onSCallMediaInfo(sessionId, isConfMember, localAudioPort, remoteAudioPort, localVideoPort, remoteVideoPort, remoteAddress, codec);

    }

    @Override
    public void onSCallOutgoingAck(String sessionId, int channel)
    {
        currentState.onSCallOutgoingAck(sessionId, channel);

    }

    @Override
    public void onSCallRingback(String sessionId, String ringNum, int channel)
    {
        currentState.onSCallRingback(sessionId, ringNum, channel);

    }

    @Override
    public void onSCallConnect(String sessionId, int callPriority)
    {
        currentState.onSCallConnect(sessionId, callPriority);
    }

    @Override
    public void onSCallConnectAck(String sessionId)
    {
        currentState.onSCallConnectAck(sessionId);
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        currentState.onSCallRelease(sessionId, reason);
    }

    @Override
    public void onSCallHoldAck(String sessionId)
    {
        currentState.onSCallHoldAck(sessionId);
    }

    @Override
    public void onSCallRetrieveAck(String sessionId)
    {
        currentState.onSCallRetrieveAck(sessionId);
    }

    @Override
    public void onSCallRemoteHold(String sessionId)
    {
        currentState.onSCallRemoteHold(sessionId);
    }

    @Override
    public void onSCallRemoteRetrieve(String sessionId)
    {
        currentState.onSCallRemoteRetrieve(sessionId);
    }

    @Override
    public void setAudioMute(boolean on) throws Exception
    {
        currentState.setAudioMute(on);
    }

    @Override
    public void setAudioSpeaker(boolean on) throws Exception
    {
        currentState.setAudioSpeaker(on);
    }
    
    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        currentState.onRecordInfo(sessionId, taskId, server);
    }
    

    @Override
    public void onAudioEnable(String sessionId, boolean enable)
    {
        currentState.onAudioEnable(sessionId, enable);
    }

    @Override
    public void onVideoEnable(String sessionId, boolean enable)
    {
        currentState.onVideoEnable(sessionId, enable);
    }
    
    @Override
    public void onVideoShareReceived(String sessionId, boolean enable, String videoNum, String tag)
    {
        currentState.onVideoShareReceived(sessionId, enable, videoNum, tag);
    }

    @Override
    public void onMultiMediaInfoNotify(String sessionId, String mediaTag)
    {
        currentState.onMultiMediaInfoNotify(sessionId, mediaTag);
    }
    
    @Override
    public void sendDtmf(String sessionId, char dtmf) throws Exception
    {
        currentState.sendDtmf(sessionId, dtmf);
    }
    
    @Override
    public void setCloseRing(String sessionId, boolean enable) throws Exception
    {
        currentState.setCloseRing(sessionId, enable);
    }
    
    @Override
    public void recycle()
    {
        if (audioLauncher != null)
        {
            audioLauncher.halt();
        }

        if (videoLauncher != null)
        {
            videoLauncher.halt();
        }
        super.recycle();
    }

    public JAudioLauncher getAudioLauncher()
    {
        return audioLauncher;
    }

    public void setAudioLauncher(JAudioLauncher audioLauncher)
    {
        this.audioLauncher = audioLauncher;
    }

//
//    public LocalVideoView getLocalVideoView()
//    {
//        return localVideoView;
//    }
//
//    public void setLocalVideoView(LocalVideoView localVideoView)
//    {
//        this.localVideoView = localVideoView;
//    }
//
//    public RemoteVideoView getRemoteVideoView()
//    {
//        return remoteVideoView;
//    }
//
//    public void setRemoteVideoView(RemoteVideoView remoteVideoView)
//    {
//        this.remoteVideoView = remoteVideoView;
//    }

    public JVideoLauncher getVideoLauncher()
    {
        return videoLauncher;
    }

    public void setVideoLauncher(JVideoLauncher videoLauncher)
    {
        this.videoLauncher = videoLauncher;
    }

    public CallRecord getCallRecord()
    {
        return callRecord;
    }

    public void setCallRecord(CallRecord callRecord)
    {
        this.callRecord = callRecord;
    }

    public int getChannel()
    {
        return channel;
    }

    public void setChannel(int channel)
    {
        this.channel = channel;
    }

    public String getMediaTag()
    {
        return mediaTag;
    }

    public void setMediaTag(String mediaTag)
    {
        this.mediaTag = mediaTag;
    }

}
