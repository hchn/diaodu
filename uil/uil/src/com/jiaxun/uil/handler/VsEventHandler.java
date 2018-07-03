package com.jiaxun.uil.handler;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.VsModel;
import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.VsListItem;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：视频监控事件通知处理
 *
 * @author  hubin
 *
 * @Date 2015-9-16
 */
public class VsEventHandler implements SclVsEventListener
{
    private static final String TAG = VsEventHandler.class.getName();

    @Override
    public void onVsStatusChange(String sessionId, int status, VsModel vsModel, int reason)
    {
        Log.info(TAG, "onVsStatusChange:: status:" + status);
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VS_STATUS_CHANGE, sessionId, status, vsModel, reason);
    }

    @Override
    public void onVsVideoReceived(String sessionId, String videoUrl, SurfaceView videoView)
    {
        Log.info(TAG, "onVsVideoReceived::videoUrl" + videoUrl + "sessionId" + sessionId);
        for (VsListItem vsListItem : UiApplication.getVsService().getVsUserList())
        {
            if (sessionId.equals(vsListItem.getVsModel().getSessionId()))
            {
                vsListItem.setVideoUrl(videoUrl);
                vsListItem.setRemoteVideoView(videoView);
            }
        }
        UiUtils.addRemoteVideo(CommonConstantEntry.SESSION_TYPE_VS, sessionId, videoUrl, videoView);
    }

}
