package com.jiaxun.sdk.scl.session.conf.state;

import java.util.Calendar;

import android.text.TextUtils;

import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.media.video.RemoteVideoView;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.conf.ConfState;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;

/**
 * 说明：入会请求后，等待确认
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public class ConfReturnAckState extends ConfState
{
    public ConfReturnAckState(ConfSession confSession)
    {
        super(confSession);
        TAG = ConfReturnAckState.class.getName();
        timeout = CommonConstantEntry.CONF_TIMEOUT_RETURN_ACK;
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
    public void onConfReturnOk(String sessionId)
    {
        Log.info(TAG, "onConfReturnOk:: sessionId:" + sessionId);
        changeConfState(confSession.connectState);
        final MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // 开启音频收发
                JAudioLauncher audioLauncher = confSession.getAudioLauncher();
                if (audioLauncher != null)
                {
                    audioLauncher.setMute(false);
                    audioLauncher.setReceive(true);
                }

                // 开启视频收发
                JVideoLauncher videoLauncher = confSession.getVideoLauncher();
                if (videoLauncher != null && mediaConfig != null)
                {
                    videoLauncher.startLocalVideoSender(mediaConfig.videoWidth, mediaConfig.videoHeight, mediaConfig.videoFrameRate, mediaConfig.videoBitRate);
                }
            }
        }).start();

        for (ConfMemModel confMemModel : confSession.getConfModel().getMemList())
        {
            if (confMemModel.getStatus() != CommonConstantEntry.CONF_MEMBER_STATE_IDLE)
            {
                confMemModel.setAudioEnabled(true);
                userCallback.onConfUserAudioChanged(confSession.getSessionId(), confMemModel.getNumber(), true);
            }
            
            if (confMemModel.getStatus() != CommonConstantEntry.CONF_MEMBER_STATE_IDLE && !confMemModel.isVideoEnabled()
                    && !TextUtils.isEmpty(confMemModel.getMediaTag()))
            {
                confMemModel.setVideoEnabled(true);
                RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                        mediaConfig.videoFrameRate, mediaConfig.videoBitRate, confMemModel.getMediaTag(), confSession.getVideoLauncher());
                userCallback.onConfUserVideoStarted(confSession.getSessionId(), confMemModel.getNumber(), remoteVideoView);
            }
        }

    }

    @Override
    public void onConfReturnFail(String sessionId)
    {
        Log.info(TAG, "onConfReturnFail:: sessionId:" + sessionId);
        changeConfState(confSession.exitState);
    }

    @Override
    public void onConfClose(String sessionId)
    {
        Log.info(TAG, "onConfClose:: sessionId:" + sessionId);
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
        changeConfState(confSession.exitState);
    }
}
