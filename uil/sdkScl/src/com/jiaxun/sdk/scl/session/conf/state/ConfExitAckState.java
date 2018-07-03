package com.jiaxun.sdk.scl.session.conf.state;

import java.util.Calendar;

import android.text.TextUtils;

import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.conf.ConfState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;

/**
 * 说明：发出退会请求，等待确认
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public class ConfExitAckState extends ConfState
{
    public ConfExitAckState(ConfSession confSession)
    {
        super(confSession);
        TAG = ConfExitAckState.class.getName();
        timeout = CommonConstantEntry.CONF_TIMEOUT_EXIT_ACK;
    }

    @Override
    public void closeConf(String sessionId)
    {
        try
        {
            Log.info(TAG, "closeConf:: sessionId:" + sessionId);
            super.closeConf(sessionId);
            confService.confClose(sessionId);
            changeConfState(confSession.idleState);
            confSession.getConfRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//            confCallback.onSclCallRecordReport(confSession.getConfRecord());
            confSession.handleCallRecord(confSession.getConfRecord());
            confSession.recycle();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfExitOk(String sessionId)
    {
        Log.info(TAG, "onConfLeaveOk:: sessionId:" + sessionId);
        changeConfState(confSession.exitState);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // 停止音频收发
                JAudioLauncher audioLauncher = confSession.getAudioLauncher();
                if (audioLauncher != null)
                {
                    audioLauncher.setMute(true);
                    audioLauncher.setReceive(false);
                    for (ConfMemModel confMemModel : confSession.getConfModel().getMemList())
                    {
                        if (confMemModel.getStatus() != CommonConstantEntry.CONF_MEMBER_STATE_IDLE)
                        {
                            confMemModel.setAudioEnabled(false);
                            userCallback.onConfUserAudioChanged(confSession.getSessionId(), confMemModel.getNumber(), false);
                        }
                    }
                }

                // 停止视频收发
                JVideoLauncher videoLauncher = confSession.getVideoLauncher();
                if (videoLauncher != null)
                {
                    videoLauncher.stopLocalVideoSender();
                    for (ConfMemModel confMemModel : confSession.getConfModel().getMemList())
                    {
                        if (confMemModel.getStatus() != CommonConstantEntry.CONF_MEMBER_STATE_IDLE && confMemModel.isVideoEnabled()
                                && !TextUtils.isEmpty(confMemModel.getMediaTag()))
                        {
                            confMemModel.setVideoEnabled(false);
                            userCallback.onConfUserVideoChanged(confSession.getSessionId(), confMemModel.getNumber(), false);
                        }
                    }
                }
            }
        }).start();

    }

    @Override
    public void onConfExitFail(String sessionId)
    {
        Log.info(TAG, "onConfLeaveFail:: sessionId:" + sessionId);
        changeConfState(confSession.connectState);
    }

    @Override
    public void onConfClose(String sessionId)
    {
        Log.info(TAG, "closeConf:: sessionId:" + sessionId);
        super.onConfClose(sessionId);
        changeConfState(confSession.idleState);
        confSession.getConfRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        confCallback.onSclCallRecordReport(confSession.getConfRecord());
        confSession.handleCallRecord(confSession.getConfRecord());
        confSession.recycle();
    }

    @Override
    public void onTimeout(Timer t)
    {
        Log.info(TAG, "onTimeout:: sessionId:" + confSession.getSessionId());
        changeConfState(confSession.connectState);
    }

}
