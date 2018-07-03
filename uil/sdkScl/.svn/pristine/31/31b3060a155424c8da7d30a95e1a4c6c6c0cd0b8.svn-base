package com.jiaxun.sdk.scl.session.conf;

import java.util.ArrayList;
import java.util.Calendar;

import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.scl.model.ConfMemModel;
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
public class ConfState implements TimerListener, ConfStateHandler
{
    public String TAG;
    protected Timer timer;
    protected ConfSession confSession;
    protected int timeout = -1;

    public ConfState(ConfSession confSession)
    {
        this.confSession = confSession;
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

    public void changeConfState(ConfState state)
    {
        if (confSession != null)
        {
            confSession.currentState.haltTimer();
            confSession.currentState = state;
            confSession.currentState.startTimer();
            confSession.setStatus(confSession.getSessionState());
            confCallback.onConfStatusChange(confSession.getSessionId(), confSession.getStatus(), confSession.getConfModel(), CommonConstantEntry.Q850_NOREASON);
        }
    }
    
    public void changeConfState(ConfState state, int reason)
    {
        if (confSession != null)
        {
            confSession.currentState.haltTimer();
            confSession.currentState = state;
            confSession.currentState.startTimer();
            confSession.setStatus(confSession.getSessionState());
            confCallback.onConfStatusChange(confSession.getSessionId(), confSession.getStatus(), confSession.getConfModel(), reason);
        }
    }

    @Override
    public void onTimeout(Timer t)
    {
    }

    @Override
    public void onConfCreateAck(String sessionId)
    {
    }

    @Override
    public void onConfCreateConnect(String sessionId, int priority)
    {
    }

    @Override
    public void onConfClose(String sessionId)
    {
        for (String number : confSession.getMemberRecordsMap().keySet())
        {
            confSession.getMemberRecordsMap().get(number).setReleaseTime(Calendar.getInstance().getTimeInMillis());
            if (!TextUtils.isEmpty(confSession.getConfRecord().getRecordTaskId()))
            {
                confSession.getMemberRecordsMap().get(number).setRecordTaskId(confSession.getConfRecord().getRecordTaskId());
                confSession.getMemberRecordsMap().get(number).setRecordServer(confSession.getConfRecord().getRecordServer());
            }
//            confCallback.onSclCallRecordReport(confSession.getMemberRecordsMap().get(number));
            confSession.handleCallRecord(confSession.getMemberRecordsMap().get(number));
        }
        confSession.getMemberRecordsMap().clear();
        confSession.getConfRecord().setReleaseReason(CommonConstantEntry.CALL_END_PEER_RELEASE);
    }

    @Override
    public void onConfExitOk(String sessionId)
    {
    }

    @Override
    public void onConfExitFail(String sessionId)
    {
    }

    @Override
    public void onConfReturnOk(String sessionId)
    {
    }

    @Override
    public void onConfReturnFail(String sessionId)
    {
    }

    @Override
    public void onConfBgmAck(String sessionId, boolean enable)
    {
    }

    @Override
    public void onConfUserRing(String sessionId, String userNum)
    {
    }

    @Override
    public void onConfUserAnswer(String sessionId, String userNum, String mediaTag)
    {
    }

    @Override
    public void onConfUserRelease(String sessionId, String userNum, int reason)
    {
    }

    @Override
    public void onConfUserAudioEnableAck(String sessionId, String userNum, boolean enable)
    {
    }

    @Override
    public void onConfUserVideoEnableAck(String sessionId, String userNum, boolean enable)
    {
    }

    @Override
    public void onConfUserVideoShareAck(String sessionId, String userNum, boolean enable)
    {
    }

    @Override
    public void onConfMediaInfo(String sessionId, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort, String remoteAddress,
            Map codec)
    {
    }

    @Override
    public void createConf(String sessionId, String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList)
    {
    }

    @Override
    public void closeConf(String sessionId)
    {
        for (String number : confSession.getMemberRecordsMap().keySet())
        {
            confSession.getMemberRecordsMap().get(number).setReleaseTime(Calendar.getInstance().getTimeInMillis());
            if (!TextUtils.isEmpty(confSession.getConfRecord().getRecordTaskId()))
            {
                confSession.getMemberRecordsMap().get(number).setRecordTaskId(confSession.getConfRecord().getRecordTaskId());
                confSession.getMemberRecordsMap().get(number).setRecordServer(confSession.getConfRecord().getRecordServer());
            }
//            confCallback.onSclCallRecordReport(confSession.getMemberRecordsMap().get(number));
            confSession.handleCallRecord(confSession.getMemberRecordsMap().get(number));
        }
        confSession.getMemberRecordsMap().clear();
        confSession.getConfRecord().setReleaseReason(CommonConstantEntry.CALL_END_CALLER_RELEASE);
    }

    @Override
    public void returnConf(String sessionId)
    {
    }

    @Override
    public void exitConf(String sessionId)
    {
    }

    @Override
    public void addUser(String sessionId, String userNum)
    {
    }

    @Override
    public void deleteUser(String sessionId, String userNum)
    {
    }

    @Override
    public void enableUserAudio(String sessionId, String userNum, boolean enable)
    {
    }

    @Override
    public void enableUserVideo(String sessionId, String userNum, boolean enable)
    {
    }

    @Override
    public void shareUserVideo(String sessionId, String userNum, boolean enable)
    {
    }

    @Override
    public void enableConfBgm(String sessionId, boolean enable)
    {
    }

    @Override
    public void setAudioMute(String sessionId, boolean mute)
    {
    }

    @Override
    public void setVideoMute(String sessionId, boolean mute)
    {
    }

    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        Log.info(TAG, "onRecordInfo:: sessionId:" + sessionId + " taskId:" + taskId + " server:" + server);
        confSession.getConfRecord().setRecordTaskId(taskId);
        confSession.getConfRecord().setRecordServer(server);
    }

    @Override
    public void sendDtmf(String sessionId, char dtmf) throws Exception
    {
        switch (SessionManager.getInstance().getMediaConfig().dtmfMode)
        {
            case CommonConstantEntry.DTMF_MODE_INBAND:
                confService.confInbandDTMFSend(sessionId, dtmf);
                break;
            case CommonConstantEntry.DTMF_MODE_RFC2833:
                if (confSession.getAudioLauncher() != null)
                {
                    confSession.getAudioLauncher().sendDTMF(dtmf);
                }
                break;
            case CommonConstantEntry.DTMF_MODE_SIP_INFO:
                confService.confSipInfoDTMFSend(sessionId, dtmf);
                break;

            default:
                break;
        }
    }

}
