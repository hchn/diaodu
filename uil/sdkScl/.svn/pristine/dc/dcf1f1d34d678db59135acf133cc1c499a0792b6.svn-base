package com.jiaxun.sdk.scl.session.conf;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.ConfModel;
import com.jiaxun.sdk.scl.session.Session;
import com.jiaxun.sdk.scl.session.conf.state.ConfConnectState;
import com.jiaxun.sdk.scl.session.conf.state.ConfExitAckState;
import com.jiaxun.sdk.scl.session.conf.state.ConfExitState;
import com.jiaxun.sdk.scl.session.conf.state.ConfIdleState;
import com.jiaxun.sdk.scl.session.conf.state.ConfProceedingState;
import com.jiaxun.sdk.scl.session.conf.state.ConfReturnAckState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：作为会议过程中的数据承载类
 *
 * @author  hubin
 *
 * @Date 2015-1-8
 */
public class ConfSession extends Session implements ConfStateHandler
{
    public static final String TAG = ConfSession.class.getName();
    public ConfState connectState;
    public ConfState exitAckState;
    public ConfState exitState;
    public ConfState idleState;
    public ConfState proceedingState;
    public ConfState returnAckState;

    public ConfState currentState;

    private ConfModel confModel;
    private CallRecord confRecord;
    private HashMap<String, CallRecord> memberRecordsMap = new HashMap<String, CallRecord>();

    private JAudioLauncher audioLauncher;
    private JVideoLauncher videoLauncher;

    public ConfSession()
    {
        this.connectState = new ConfConnectState(this);
        this.exitAckState = new ConfExitAckState(this);
        this.exitState = new ConfExitState(this);
        this.idleState = new ConfIdleState(this);
        this.proceedingState = new ConfProceedingState(this);
        this.returnAckState = new ConfReturnAckState(this);

        this.currentState = idleState;
    }

    public int getSessionState()
    {
        if (currentState instanceof ConfIdleState)
        {
            return CommonConstantEntry.CONF_STATE_IDLE;
        }
        else if (currentState instanceof ConfConnectState)
        {
            return CommonConstantEntry.CONF_STATE_CONNECT;
        }
        else if (currentState instanceof ConfExitAckState)
        {
            return CommonConstantEntry.CONF_STATE_EXIT_ACK;
        }
        else if (currentState instanceof ConfExitState)
        {
            return CommonConstantEntry.CONF_STATE_EXIT;
        }
        else if (currentState instanceof ConfProceedingState)
        {
            return CommonConstantEntry.CONF_STATE_PROCEEDING;
        }
        else if (currentState instanceof ConfReturnAckState)
        {
            return CommonConstantEntry.CONF_STATE_RETURN_ACK;
        }
        return CommonConstantEntry.CONF_STATE_IDLE;
    }

    @Override
    public void onConfCreateAck(String sessionId)
    {
        currentState.onConfCreateAck(sessionId);
    }

    @Override
    public void onConfCreateConnect(String sessionId, int priority)
    {
        currentState.onConfCreateConnect(sessionId, priority);
    }

    @Override
    public void onConfExitOk(String sessionId)
    {
        currentState.onConfExitOk(sessionId);
    }

    @Override
    public void onConfExitFail(String sessionId)
    {
        currentState.onConfExitFail(sessionId);
    }

    @Override
    public void onConfReturnOk(String sessionId)
    {
        currentState.onConfReturnOk(sessionId);
    }

    @Override
    public void onConfReturnFail(String sessionId)
    {
        currentState.onConfReturnFail(sessionId);
    }

    @Override
    public void onConfBgmAck(String sessionId, boolean enable)
    {
        currentState.onConfBgmAck(sessionId, enable);
    }

    @Override
    public void onConfUserRing(String sessionId, String userNum)
    {
        currentState.onConfUserRing(sessionId, userNum);
    }

    @Override
    public void onConfUserAnswer(String sessionId, String userNum, String mediaTag)
    {
        currentState.onConfUserAnswer(sessionId, userNum, mediaTag);
    }

    @Override
    public void onConfUserRelease(String sessionId, String userNum, int reason)
    {
        currentState.onConfUserRelease(sessionId, userNum, reason);
    }

    @Override
    public void onConfUserAudioEnableAck(String sessionId, String userNum, boolean enable)
    {
        currentState.onConfUserAudioEnableAck(sessionId, userNum, enable);
    }

    @Override
    public void onConfUserVideoEnableAck(String sessionId, String userNum, boolean enable)
    {
        currentState.onConfUserVideoEnableAck(sessionId, userNum, enable);
    }

    @Override
    public void onConfUserVideoShareAck(String sessionId, String userNum, boolean enable)
    {
        currentState.onConfUserVideoShareAck(sessionId, userNum, enable);
    }

    @Override
    public void onConfMediaInfo(String sessionId, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort, String remoteAddress,
            Map codec)
    {
        currentState.onConfMediaInfo(sessionId, localAudioPort, remoteAudioPort, localVideoPort, remoteVideoPort, remoteAddress, codec);
    }

    @Override
    public void createConf(String sessionId, String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList)
    {
        currentState.createConf(sessionId, confName, callPriority, confType, channel, video, memberList);
    }

    @Override
    public void closeConf(String sessionId)
    {
        currentState.closeConf(sessionId);
    }

    @Override
    public void returnConf(String sessionId)
    {
        currentState.returnConf(sessionId);
    }

    @Override
    public void exitConf(String sessionId)
    {
        currentState.exitConf(sessionId);
    }

    @Override
    public void addUser(String sessionId, String userNum)
    {
        currentState.addUser(sessionId, userNum);
    }

    @Override
    public void deleteUser(String sessionId, String userNum)
    {
        currentState.deleteUser(sessionId, userNum);
    }

    @Override
    public void enableUserAudio(String sessionId, String userNum, boolean enable)
    {
        currentState.enableUserAudio(sessionId, userNum, enable);
    }

    @Override
    public void enableUserVideo(String sessionId, String userNum, boolean enable)
    {
        currentState.enableUserVideo(sessionId, userNum, enable);
    }

    @Override
    public void shareUserVideo(String sessionId, String userNum, boolean enable)
    {
        currentState.shareUserVideo(sessionId, userNum, enable);
    }

    @Override
    public void enableConfBgm(String sessionId, boolean enable)
    {
        Log.info(TAG, "state:" + getStatus());
        currentState.enableConfBgm(sessionId, enable);
    }

    @Override
    public void setAudioMute(String sessionId, boolean mute)
    {
        currentState.setAudioMute(sessionId, mute);
    }

    @Override
    public void setVideoMute(String sessionId, boolean mute)
    {
        currentState.setVideoMute(sessionId, mute);
    }

    @Override
    public void onConfClose(String sessionId)
    {
        currentState.onConfClose(sessionId);
    }
    
    @Override
    public void sendDtmf(String sessionId, char dtmf) throws Exception
    {
        currentState.sendDtmf(sessionId, dtmf);
    }

    public ConfModel getConfModel()
    {
        return confModel;
    }

    public void setConfModel(ConfModel confModel)
    {
        this.confModel = confModel;
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

    public CallRecord getConfRecord()
    {
        return confRecord;
    }

    public void setConfRecord(CallRecord confRecord)
    {
        this.confRecord = confRecord;
    }

    public JAudioLauncher getAudioLauncher()
    {
        return audioLauncher;
    }

    public void setAudioLauncher(JAudioLauncher audioLauncher)
    {
        this.audioLauncher = audioLauncher;
    }

    public JVideoLauncher getVideoLauncher()
    {
        return videoLauncher;
    }

    public void setVideoLauncher(JVideoLauncher videoLauncher)
    {
        this.videoLauncher = videoLauncher;
    }

    public HashMap<String, CallRecord> getMemberRecordsMap()
    {
        return memberRecordsMap;
    }

    public void setMemberRecordsMap(HashMap<String, CallRecord> memberRecordsMap)
    {
        this.memberRecordsMap = memberRecordsMap;
    }

    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        currentState.onRecordInfo(sessionId, taskId, server);
    }

}
