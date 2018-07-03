package com.jiaxun.sdk.scl.session.conf.state;

import java.util.Calendar;

import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.dcl.model.CallRecord;
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

/**
 * 说明：会议进行中状态
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public class ConfConnectState extends ConfState
{
    public ConfConnectState(ConfSession confSession)
    {
        super(confSession);
        TAG = ConfConnectState.class.getName();
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
    public void exitConf(String sessionId)
    {
        try
        {
            Log.info(TAG, "exitConf:: sessionId:" + sessionId);
            confService.confLeave(sessionId);
            changeConfState(confSession.exitAckState);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
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
    public void addUser(String sessionId, String userNum)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "addUser:: userNum:" + userNum);
            confService.confUserAdd(sessionId, userNum);
            // 添加成员通话记录
            CallRecord memberRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0], SessionManager.getInstance()
                    .getAccountConfig().account[0]);
            memberRecord.setConfId(sessionId);
            memberRecord.setPeerName(confSession.getContactNameByNumber(userNum));
            memberRecord.setPeerNum(userNum);
            memberRecord.setChairman(false);
            memberRecord.setOutGoing(true);
            memberRecord.setStartTime(Calendar.getInstance().getTimeInMillis());
            memberRecord.setCallType(CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO);
            confSession.getMemberRecordsMap().put(userNum, memberRecord);

            // 重复成员不再添加
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    return;
                }
            }

            ConfMemModel confmember = new ConfMemModel();
            confmember.setNumber(userNum);
            confmember.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_IDLE);
            confSession.getConfModel().getMemList().add(confmember);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void deleteUser(String sessionId, String userNum)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "deleteUser:: userNum:" + userNum);
            confService.confUserDelete(sessionId, userNum);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void enableUserAudio(String sessionId, String userNum, boolean enable)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "enableUserAudio:: userNum:" + userNum);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setAudioEnabled(enable);
                    confService.confUserAudioEnable(sessionId, userNum, enable);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void enableUserVideo(String sessionId, String userNum, boolean enable)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "enableUserVideo:: userNum:" + userNum);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setVideoEnabled(enable);
                    confService.confUserVideoEnable(sessionId, userNum, enable);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void shareUserVideo(String sessionId, String userNum, boolean enable)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "shareUserVideo:: userNum:" + userNum);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setVideoShared(enable);
                    confService.confUserVideoShare(sessionId, userNum, member.getMediaTag(), enable);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void enableConfBgm(String sessionId, boolean enable)
    {
        try
        {
            confService.confBgmEnable(sessionId, enable);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        super.enableConfBgm(sessionId, enable);
    }

    @Override
    public void setAudioMute(String sessionId, boolean mute)
    {
        Log.info(TAG, "setAudioMute:: sessionId:" + sessionId);
        if (confSession.getAudioLauncher() != null)
        {
            // 开启/关闭静音
            confSession.getAudioLauncher().setMute(mute);
            // 记录状态
            confSession.getConfModel().setAudioMute(mute);
        }
    }

    @Override
    public void setVideoMute(String sessionId, boolean mute)
    {
        // TODO Auto-generated method stub
        Log.info(TAG, "setVideoMute:: sessionId:" + sessionId);
        confSession.getConfModel().setVideoMute(mute);
    }

    @Override
    public void onConfBgmAck(String sessionId, boolean enable)
    {
        confCallback.onConfBgmEnable(sessionId, enable);
        super.onConfBgmAck(sessionId, enable);
    }

    @Override
    public void onConfUserRing(String sessionId, String userNum)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserRing:: sessionId:" + sessionId);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_RING);
                    userCallback.onConfUserStatusChange(sessionId, member.getStatus(), member, CommonConstantEntry.Q850_NOREASON);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserAnswer(final String sessionId, final String userNum, final String mediaTag)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserAnswer:: userNum:" + userNum + " mediaTag:" + mediaTag);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_CONNECT);
                    confSession.getMemberRecordsMap().get(member.getNumber()).setConnectTime(Calendar.getInstance().getTimeInMillis());
                    userCallback.onConfUserStatusChange(sessionId, member.getStatus(), member, CommonConstantEntry.Q850_NOREASON);
                    MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                    if (confSession.getVideoLauncher() != null && mediaConfig != null)
                    {
                        RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                                mediaConfig.videoFrameRate, mediaConfig.videoBitRate, mediaTag, confSession.getVideoLauncher());
                        userCallback.onConfUserVideoStarted(sessionId, userNum, remoteVideoView);
                        member.setMediaTag(mediaTag);
                        member.setVideoEnabled(true);
                    }
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserRelease(String sessionId, String userNum, int reason)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserRelease:: userNum:" + userNum);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_IDLE);
                    confSession.getMemberRecordsMap().get(userNum).setReleaseTime(Calendar.getInstance().getTimeInMillis());
                    if (!TextUtils.isEmpty(confSession.getConfRecord().getRecordTaskId()))
                    {
                        confSession.getMemberRecordsMap().get(userNum).setRecordTaskId(confSession.getConfRecord().getRecordTaskId());
                        confSession.getMemberRecordsMap().get(userNum).setRecordServer(confSession.getConfRecord().getRecordServer());
                    }
//                    confCallback.onSclCallRecordReport(confSession.getMemberRecordsMap().get(userNum));
                    confSession.handleCallRecord(confSession.getMemberRecordsMap().get(userNum));
                    confSession.getMemberRecordsMap().remove(userNum);
                    userCallback.onConfUserStatusChange(sessionId, member.getStatus(), member, reason);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserAudioEnableAck(String sessionId, String userNum, boolean enable)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserAudioEnableAck:: userNum:" + userNum);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setAudioEnabled(enable);
                    userCallback.onConfUserAudioChanged(sessionId, userNum, enable);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserVideoEnableAck(final String sessionId, final String userNum, final boolean enable)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserVideoEnableAck:: userNum:" + userNum);
            for (final ConfMemModel member : confSession.getConfModel().getMemList())
            {
                MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                if (userNum.equals(member.getNumber()) && mediaConfig != null)
                {
                    member.setVideoEnabled(enable);
                    if (enable && confSession.getVideoLauncher() != null)
                    {
                        RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                                mediaConfig.videoFrameRate, mediaConfig.videoBitRate, member.getMediaTag(), confSession.getVideoLauncher());
                        userCallback.onConfUserVideoStarted(sessionId, userNum, remoteVideoView);
                    }
                    else
                    {
                        userCallback.onConfUserVideoChanged(sessionId, userNum, enable);
                    }
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserVideoShareAck(String sessionId, String userNum, boolean enable)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserVideoShareAck:: sessionId:" + sessionId);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setVideoShared(enable);
                    userCallback.onConfUserVideoShareChanged(sessionId, userNum, enable);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfMediaInfo(String sessionId, final int localAudioPort, final int remoteAudioPort, final int localVideoPort, final int remoteVideoPort,
            final String remoteAddress, final Map codec)
    {
        Log.info(TAG, "onConfMediaInfo:: localAudioPort:" + localAudioPort + " remoteAudioPort:" + remoteAudioPort + " localVideoPort:" + localVideoPort
                + " remoteVideoPort:" + remoteVideoPort);
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
                if (localAudioPort != 0)
                {
                    JAudioLauncher audioLauncher = confSession.getAudioLauncher();
                    if (audioLauncher == null)
                    {
                        audioLauncher = new JAudioLauncher(localAudioPort, 0, codec, 0, confSession.getConfModel().getConnectTime());
                        confSession.setAudioLauncher(audioLauncher);
                    }
                    audioLauncher.setMute(true);// 静音
                    audioLauncher.startMedia();// 启动线程
                    if (remoteAudioPort != 0)
                    {
                        ((JAudioLauncher) audioLauncher).setRemote(remoteAddress, remoteAudioPort);
                    }
                    audioLauncher.setMute(false);// 开始采集
                }

                if (remoteVideoPort != 0 && SessionManager.getInstance().getServiceConfig().isVideoCall)
                {
                    // 初始化远端视频
                    JVideoLauncher videoLauncher = confSession.getVideoLauncher();
                    MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                    if (videoLauncher == null && mediaConfig != null)
                    {
                        if (SessionManager.getInstance().getLoacalCameraView() == null)
                        {
                            videoLauncher = new JVideoLauncher(localVideoPort, remoteVideoPort, remoteAddress, null);
                        }
                        else
                        {
                            videoLauncher = new JVideoLauncher(localVideoPort, remoteVideoPort, remoteAddress, SessionManager.getInstance()
                                    .getLoacalCameraView().getVideoPreview());
                        }
                        confSession.setVideoLauncher(videoLauncher);
                        videoLauncher.startLocalVideoSender(mediaConfig.videoWidth, mediaConfig.videoHeight, mediaConfig.videoFrameRate,
                                mediaConfig.videoBitRate);
                    }
                }
//            }
//        }).start();
    }
}
