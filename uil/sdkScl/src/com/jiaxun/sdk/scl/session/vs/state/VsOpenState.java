package com.jiaxun.sdk.scl.session.vs.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.media.video.RemoteVideoView;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.vs.VsSession;
import com.jiaxun.sdk.scl.session.vs.VsState;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * ˵������ؿ���״̬
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class VsOpenState extends VsState
{
    public VsOpenState(VsSession vsSession)
    {
        super(vsSession);
        TAG = VsOpenState.class.getName();
    }

    @Override
    public void closeVs(String sessionId) throws Exception
    {
        try
        {
            super.closeVs(sessionId);
            Log.info(TAG, "closeVs:: sessionId:" + sessionId);
            vsService.vsClose(sessionId);
            changeVsState(vsSession.closeState, CommonConstantEntry.CALL_END_CALLER_RELEASE);
            vsSession.getVsRecord().setReleaseReason(CommonConstantEntry.CALL_END_CALLER_RELEASE);
            vsSession.getVsRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//            callback.onSclCallRecordReport(vsSession.getVsRecord());
            vsSession.handleCallRecord(vsSession.getVsRecord());
            vsSession.recycle();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onVsClosed(String sessionId, int reason)
    {
        Log.info(TAG, "onVsClosed:: sessionId:" + sessionId + " reason:" + reason);
        super.onVsClosed(sessionId, reason);
        changeVsState(vsSession.closeState, CommonConstantEntry.CALL_END_PEER_RELEASE);
        vsSession.getVsRecord().setReleaseReason(CommonConstantEntry.CALL_END_PEER_RELEASE);
        vsSession.getVsRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(vsSession.getVsRecord());
        vsSession.handleCallRecord(vsSession.getVsRecord());
        vsSession.recycle();
    }

    @Override
    public void onVsMediaInfo(final String sessionId, final int localVideoPort, final int remoteVideoPort, final String remoteAddress)
    {
        Log.info(TAG, "onVsMediaInfo:: sessionId:" + sessionId + " remoteVideoPort:" + remoteVideoPort + " remoteAddress:" + remoteAddress);
        if (localVideoPort != 0)
        {
            // ��ʼ��Զ����Ƶ
            if (vsSession.getVideoLauncher() == null)
            {
//                Thread thread = new Thread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
                        JVideoLauncher videoLauncher = new JVideoLauncher(localVideoPort, remoteVideoPort, remoteAddress, null);
                        vsSession.setVideoLauncher(videoLauncher);
//                videoLauncher.startLocalVideoSender(SessionManager.getInstance().getServiceConfig().videoWidth,
//                        SessionManager.getInstance().getServiceConfig().videoHeight, SessionManager.getInstance().getServiceConfig().videoFrameRate,
//                        SessionManager.getInstance().getServiceConfig().videoBitRate);
//                    }
//                });
//                thread.start();
//                try
//                {
//                    thread.join(500);
                    MediaConfig mediaConfig = SessionManager.getInstance().getMediaConfig();
                    if (vsSession.getVideoLauncher() != null && mediaConfig != null)
                    {
                        RemoteVideoView remoteVideoView = new RemoteVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight,
                                mediaConfig.videoFrameRate, mediaConfig.videoBitRate, "0", vsSession.getVideoLauncher());
                        callback.onVsVideoReceived(sessionId, vsSession.getVsModel().getVideoNum(), remoteVideoView);
                    }
//                }
//                catch (InterruptedException e)
//                {
//                    Log.exception(TAG, e);
//                }
            }
        }
    }
}
