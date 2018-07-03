package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import android.os.Handler;
import android.os.Looper;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.scl.media.audio.JAudioLauncher;
import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.media.video.RemoteVideoView;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：通话状态
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class SCallConnectState extends SCallState
{
    public SCallConnectState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallConnectState.class.getName();
    }

    @Override
    public void releaseCall(String sessionId, int reason) throws Exception
    {
        super.releaseCall(sessionId, reason);
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
        changeCallState(callSession.idleState, reason);
        sCallService.sCallRelease(sessionId, reason);
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
    public void holdCall(String sessionId) throws Exception
    {
        Log.info(TAG, "holdCall:: sessionId:" + sessionId);
        changeCallState(callSession.holdAckState);
        sCallService.sCallHold(sessionId);
    }

    @Override
    public void onSCallMediaInfo(final String sessionId, final boolean isConfMember, final int localAudioPort, final int remoteAudioPort,
            final int localVideoPort, final int remoteVideoPort, final String remoteAddress, final Map codec)
    {
        Log.info(TAG, "onSCallMediaInfo:: sessionId:" + sessionId + " localVideoPort:" + localVideoPort + " remoteVideoPort:" + remoteVideoPort);
        if (localAudioPort != 0)
        {
//            new Thread(new Runnable()
//            {
//                @Override
//                public void run()
//                {
                    JAudioLauncher audioLauncher = callSession.getAudioLauncher();
                    if (audioLauncher == null)
                    {
                        audioLauncher = new JAudioLauncher(localAudioPort, 0, codec, 0, callSession.getCallModel().getConnectTime());
                        callSession.setAudioLauncher(audioLauncher);
                    }
                    audioLauncher.setMute(true);// 静音
                    audioLauncher.startMedia();// 启动线程
                    if (remoteAudioPort != 0)
                    {
                        ((JAudioLauncher) audioLauncher).setRemote(remoteAddress, remoteAudioPort);
                    }
                    audioLauncher.setMute(false);// 开始采集
//                }
//            }).start();
        }
        if (localVideoPort != 0 && SessionManager.getInstance().getServiceConfig() != null && SessionManager.getInstance().getServiceConfig().isVideoCall)
        {
            // 初始化远端视频
            MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
            if (callSession.getVideoLauncher() == null && mediaConfig != null)
            {
                Log.info(TAG, "callSession.getVideoLauncher() == null");
//                Thread thread = new Thread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
                        JVideoLauncher videoLauncher;
                        if (SessionManager.getInstance().getLoacalCameraView() == null)
                        {
                            videoLauncher = new JVideoLauncher(localVideoPort, remoteVideoPort, remoteAddress, null);
                        }
                        else
                        {
                            videoLauncher = new JVideoLauncher(localVideoPort, remoteVideoPort, remoteAddress, SessionManager.getInstance()
                                    .getLoacalCameraView().getVideoPreview());
                        }
                        callSession.setVideoLauncher(videoLauncher);
//                        final MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                        videoLauncher.startLocalVideoSender(mediaConfig.videoWidth, mediaConfig.videoHeight, mediaConfig.videoFrameRate,
                                mediaConfig.videoBitRate);

                        if (!isConfMember && callSession.getVideoLauncher() != null)
                        {
//                            new Handler(Looper.getMainLooper()).post(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
                                    RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth,
                                            mediaConfig.videoHeight, mediaConfig.videoFrameRate, mediaConfig.videoBitRate, "0", callSession.getVideoLauncher());
                                    callback.onSCallremoteVideoStarted(sessionId, callSession.getCallModel().getPeerNum(), remoteVideoView);
                                    callSession.setMediaTag("0");
//                                }
//                            });
                        }
//                    }
//                });
//                thread.start();
//                try
//                {
//                    thread.join(500);
//                    if (!isConfMember && callSession.getVideoLauncher() != null)
//                    {
//                        RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
//                                mediaConfig.videoFrameRate, mediaConfig.videoBitRate, "0", callSession.getVideoLauncher());
//                        callback.onSCallremoteVideoStarted(sessionId, callSession.getCallModel().getPeerNum(), remoteVideoView);
//                        callSession.setMediaTag("0");
//                    }
//                }
//                catch (InterruptedException e)
//                {
//                    Log.exception(TAG, e);
//                }
            }
        }

    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        super.onSCallRelease(sessionId, reason);
        reason = transferReason(reason);
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
        callSession.getCallModel().setEndTime(Calendar.getInstance().getTimeInMillis());
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
            callback.onSCallStatusChange(sessionId, callSession.getSessionState(), callSession.getCallModel(), CommonConstantEntry.Q850_NOREASON);
            pauseAudioVideoMedia();
        }
    }

    @Override
    public void onSCallRemoteRetrieve(final String sessionId)
    {
        Log.info(TAG, "onSCallRemoteRetrieve:: sessionId:" + sessionId);
        try
        {
            if (callSession.getCallModel().isHolded())
            {
                callSession.getCallModel().setHolded(false);

                // 恢复媒体之前，先对已通话中的会议进行保持操作
//                if (SessionManager.getInstance().handleConnectedCalls(sessionId))//hz delete
//                {
//                    SystemClock.sleep(500);
//                }

                callback.onSCallStatusChange(sessionId, callSession.getSessionState(), callSession.getCallModel(), CommonConstantEntry.Q850_NOREASON);
                // 开启音频收发
                JAudioLauncher audioLauncher = callSession.getAudioLauncher();
                if (audioLauncher != null)
                {
                    audioLauncher.setMute(false);
                    audioLauncher.setReceive(true);
                }

                final JVideoLauncher videoLauncher = callSession.getVideoLauncher();
                final MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                if (videoLauncher != null && mediaConfig != null)
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            videoLauncher.startLocalVideoSender(mediaConfig.videoWidth, mediaConfig.videoHeight, mediaConfig.videoFrameRate,
                                    mediaConfig.videoBitRate);
                        }
                    }).start();
                    RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                            mediaConfig.videoFrameRate, mediaConfig.videoBitRate, callSession.getMediaTag(), callSession.getVideoLauncher());
                    callback.onSCallremoteVideoStarted(sessionId, callSession.getCallModel().getPeerNum(), remoteVideoView);
                }
                
                VoiceUtil.stopHold();
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void setAudioMute(boolean on) throws Exception
    {
        Log.info(TAG, "setAudioMute::" + on);
        if (callSession.getAudioLauncher() != null)
        {
            // 开启/关闭静音
            callSession.getAudioLauncher().setMute(on);
            // 记录状态
            callSession.getCallModel().setMuteOn(on);
        }
    }

    @Override
    public void setAudioSpeaker(boolean on) throws Exception
    {
        Log.info(TAG, "setAudioSpeaker::" + on);
        VoiceUtil.setSpeaker(on);
    }

    @Override
    public void onAudioEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "onAudioEnable::enable: " + enable);
        if (callSession.getAudioLauncher() != null)
        {
            // 开启/关闭静音
            callSession.getAudioLauncher().setMute(enable);
            // 记录状态
            callSession.getCallModel().setMuteOn(enable);
            callback.onSclCallAudioEnable(sessionId, enable);
        }
        super.onAudioEnable(sessionId, enable);
    }

    @Override
    public void onVideoEnable(String sessionId, final boolean enable)
    {
        Log.info(TAG, "onVideoEnable::enable: " + enable);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                if (callSession.getVideoLauncher() != null)
                {
                    MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                    if (enable && mediaConfig != null)
                    {
                        callSession.getVideoLauncher().startLocalVideoSender(mediaConfig.videoWidth, mediaConfig.videoHeight, mediaConfig.videoFrameRate,
                                mediaConfig.videoBitRate);
                    }
                    else
                    {
                        callSession.getVideoLauncher().stopLocalVideoSender();
                    }
                }
            }
        }).start();
        super.onVideoEnable(sessionId, enable);
    }

    @Override
    public void onVideoShareReceived(final String sessionId, final boolean enable, final String videoNum, final String tag)
    {
        Log.info(TAG, "onVideoShareReceived:: videoNum:" + videoNum + " tag:" + tag);
        // 广播自身视频时不处理
        if (!videoNum.equals(SessionManager.getInstance().getAccountConfig().account[0]))
        {
            MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
            if (enable && callSession.getVideoLauncher() != null && mediaConfig != null)
            {
                RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                        mediaConfig.videoFrameRate, mediaConfig.videoBitRate, tag, callSession.getVideoLauncher());
                callback.onSCallremoteVideoStarted(sessionId, videoNum, remoteVideoView);
            }
            else
            {
                callback.onSCallremoteVideoStoped(sessionId, videoNum);
            }
        }
        super.onVideoShareReceived(sessionId, enable, videoNum, tag);
    }

    @Override
    public void onMultiMediaInfoNotify(final String sessionId, final String mediaTag)
    {
        MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
        if (callSession.getVideoLauncher() != null && SessionManager.getInstance().getServiceConfig().isVideoCall && mediaConfig != null)
        {
            RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                    mediaConfig.videoFrameRate, mediaConfig.videoBitRate, mediaTag, callSession.getVideoLauncher());
            callback.onSCallremoteVideoStarted(sessionId, callSession.getCallModel().getPeerNum(), remoteVideoView);
            callSession.setMediaTag(mediaTag);
        }
        super.onMultiMediaInfoNotify(sessionId, mediaTag);
    }

    @Override
    public void setCloseRing(String sessionId, boolean enable) throws Exception
    {
        super.setCloseRing(sessionId, enable);
        Log.info(TAG, "setCloseRing:: enable" + enable);
        callSession.getCallModel().setCloseRing(enable);
        SessionManager.getInstance().setCloseRing(enable);
    }

}
