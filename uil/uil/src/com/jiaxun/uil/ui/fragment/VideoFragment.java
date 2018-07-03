package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.graphics.PixelFormat;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.handler.CallEventHandler;
import com.jiaxun.uil.model.ConfMemberItem;
import com.jiaxun.uil.model.VsListItem;
import com.jiaxun.uil.ui.view.presentation.VideoFrameView;
import com.jiaxun.uil.ui.view.presentation.VideoItem;
import com.jiaxun.uil.util.FgManager;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：视频展示碎片
 *
 * @author  hubin
 *
 * @Date 2015-4-15
 */
public class VideoFragment extends BaseFragment implements OnCheckedChangeListener, NotificationCenterDelegate, OnClickListener
{
    private static final String TAG = VideoFragment.class.getName();
    // 显示Video界面
    private VideoFrameView vf;
    private RadioGroup screenSelector;
    private int screenType;
    private static ArrayList<SurfaceView> videoList = new ArrayList<SurfaceView>();

    // 用于切换窗口时非法，重置回原窗口个数
    private boolean isRechecked;

    public VideoFragment()
    {
        screenType = UiApplication.getConfigService().getVideoWindowCount();
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_ADD_REMOTE_VIDEO);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_REMOVE_REMOTE_VIDEO);
    }

    private boolean removeVideo(String number)
    {
        if (TextUtils.isEmpty(number))
        {
            return false;
        }
        for (SurfaceView surfaceView : videoList)
        {
            if (number.equals(surfaceView.getTag(R.id.call_number)))
            {
                videoList.remove(surfaceView);
                if (!UiApplication.getVsService().isLoopStarted())
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VIDEO_NUMBER_CHANGE);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestroy()
    {
        Log.info(TAG, "onDestroy::");
//        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_ADD_REMOTE_VIDEO);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_VIDEO_SWITCH);
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CLOSE_PTZ_VIEW);
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VIDEO_PTZ_CHANGE);
        super.onDestroy();
    }

    public static ArrayList<SurfaceView> getVideoList()
    {
        return videoList;
    }

    public void setScreenType(int type)
    {
        if (vf.isFullScreen())
        {
            vf.fullScreenToNormal(type);
        }
        else
        {
            vf.setScreenSize(type);
        }
    }

    @Override
    public void initComponentViews(View view)
    {
        // FIXME：解决问题：在fragment中加载SurfaceView，屏幕会闪一下(黑色)
        Log.info(TAG, "initComponentViews::");
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_VIDEO_SWITCH);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_VIDEO_PTZ_CHANGE);
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        vf = (VideoFrameView) view.findViewById(R.id.screen_container);
        screenSelector = (RadioGroup) view.findViewById(R.id.screen_selector);
        view.findViewById(R.id.btn_video_all_presentation).setOnClickListener(this);

        LinearLayout.LayoutParams conLayoutParams = new LinearLayout.LayoutParams(UiUtils.homeLeftContainerW, UiUtils.homeContainerH);
        view.setLayoutParams(conLayoutParams);
    }

    @Override
    public void onStart()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_RELEASE_PRESENTATION_SCREEN);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW);
        super.onStart();
    }

    @Override
    public void onStop()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_RELEASE_PRESENTATION_SCREEN);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW);
        super.onStop();
    }

    @Override
    public void onResume()
    {
        Log.info(TAG, "onResume::");
        // 第一次加载后Fragment后续不执行oncreate，所以如下需在onresume中执行
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
                {
                    if (videoList.size() <= 1)
                    {
                        setScreenType(UiEventEntry.SCREEN_TYPE_1);
                    }
                    else if (videoList.size() <= 4)
                    {
                        setScreenType(UiEventEntry.SCREEN_TYPE_4);
                    }
                    else if (videoList.size() <= 6)
                    {
                        setScreenType(UiEventEntry.SCREEN_TYPE_6);
                    }
                    else
                    {
                        setScreenType(UiEventEntry.SCREEN_TYPE_9);
                    }
                }
                else
                {
                    setScreenType(screenType);
                }
            }
        }, 100);
        switch (screenType)
        {
            case UiEventEntry.SCREEN_TYPE_AUTO:
                screenSelector.check(R.id.screen_auto);
                break;
            case UiEventEntry.SCREEN_TYPE_1:
                screenSelector.check(R.id.screen_1);
                break;
            case UiEventEntry.SCREEN_TYPE_4:
                screenSelector.check(R.id.screen_4);
                break;
            case UiEventEntry.SCREEN_TYPE_6:
                screenSelector.check(R.id.screen_6);
                break;
            case UiEventEntry.SCREEN_TYPE_9:
                screenSelector.check(R.id.screen_9);
                break;
            default:
                break;
        }
        screenSelector.setOnCheckedChangeListener(this);
        super.onResume();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_video;
    }

    private void removeRemoteVideo(String userNum)
    {
        Log.info(TAG, "removeRemoteVideo::userNum:" + userNum + " videoMap.size(): " + videoList.size());
        // 移除对应view，并保证移除成功后才会处理后续业务
        if (removeVideo(userNum))
        {
            vf.removeVideoItem(userNum);
            // 通知扩展屏移除视频窗口
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO, userNum);
            if (videoList.isEmpty())
            {
                Log.info(TAG, "videoList.isEmpty()");
                if (parentActivity != null)
                {
                    if (!(FgManager.getPreFragment(R.id.container_left_content) instanceof LoginFragment))
                    {
                        parentActivity.backToPreFragment(R.id.container_left_content);
                    }
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_SHOW_LEFT_PANE);
                }
            }
            // 自动分屏状态调整分屏窗口
            else if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
            {
                if (videoList.size() == UiEventEntry.SCREEN_TYPE_1)
                {
                    setScreenType(UiEventEntry.SCREEN_TYPE_1);
                }
                else if (videoList.size() == UiEventEntry.SCREEN_TYPE_4)
                {
                    setScreenType(UiEventEntry.SCREEN_TYPE_4);
                }
                else if (videoList.size() == UiEventEntry.SCREEN_TYPE_6)
                {
                    setScreenType(UiEventEntry.SCREEN_TYPE_6);
                }
            }
        }
    }

    private void addRemoteVideo(final String sessionId, final String userNum, final SurfaceView remoteView)
    {
        Log.info(TAG, "addRemoteVideo:: screenType: " + screenType + " userNum:" + userNum + " videoList.size():" + videoList.size());
        if (screenType != UiEventEntry.SCREEN_TYPE_AUTO && videoList.size() == screenType)
        {
            ToastUtil.showToast(String.format("超出最大视频个数，%s视频显示失败", userNum));
            UiUtils.closeVideo(remoteView);
            return;
        }
        videoList.add(remoteView);
        // 首次打开视频窗口等待500ms直到视频窗口打开后再启动视频
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Log.info(TAG, "addRemoteVideo:: start add view");
                if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
                {
                    int screenSize = vf.getVideoItems().size();
                    Log.info(TAG, "videoList.size(): " + (videoList.size() - 1) + " screenSize:" + screenSize);
                    // 当视频总是达到窗口个数时触发自动适应屏幕
                    if (screenSize == videoList.size() - 1)
                    {
                        if (screenSize == UiEventEntry.SCREEN_TYPE_1)
                        {
                            setScreenType(UiEventEntry.SCREEN_TYPE_4);
                        }
                        else if (screenSize == UiEventEntry.SCREEN_TYPE_4)
                        {
                            setScreenType(UiEventEntry.SCREEN_TYPE_6);
                        }
                        else if (screenSize == UiEventEntry.SCREEN_TYPE_6)
                        {
                            setScreenType(UiEventEntry.SCREEN_TYPE_9);
                        }
                        else if (screenSize == UiEventEntry.SCREEN_TYPE_9)
                        {
                            ToastUtil.showToast(String.format("超出最大视频个数，%s视频显示失败", userNum));
                            videoList.remove(remoteView);
                            UiUtils.closeVideo(remoteView);
                            return;
                        }
                    }
                }
                vf.addVideoItem(remoteView);
                if (!UiApplication.getVsService().isLoopStarted())
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VIDEO_NUMBER_CHANGE);
                }
            }
        }, 500);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        if (isRechecked)
        {
            isRechecked = false;
            return;
        }
        int tmp = -1;
        switch (checkedId)
        {
            case R.id.screen_auto:
                tmp = UiEventEntry.SCREEN_TYPE_AUTO;
                break;
            case R.id.screen_1:
                tmp = UiEventEntry.SCREEN_TYPE_1;
                break;
            case R.id.screen_4:
                tmp = UiEventEntry.SCREEN_TYPE_4;
                break;
            case R.id.screen_6:
                tmp = UiEventEntry.SCREEN_TYPE_6;
                break;
            case R.id.screen_9:
                tmp = UiEventEntry.SCREEN_TYPE_9;
                break;
            default:
                break;
        }
        if (tmp == UiEventEntry.SCREEN_TYPE_AUTO)
        {
            screenType = tmp;
            UiApplication.getConfigService().setVideoWindowCount(screenType);
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.VIDEO_WINDOW_CHANGE, screenType);
            int size = videoList.size();
            if (size <= UiEventEntry.SCREEN_TYPE_1)
            {
                tmp = UiEventEntry.SCREEN_TYPE_1;
            }
            else if (size <= UiEventEntry.SCREEN_TYPE_4)
            {
                tmp = UiEventEntry.SCREEN_TYPE_4;
            }
            else if (size <= UiEventEntry.SCREEN_TYPE_6)
            {
                tmp = UiEventEntry.SCREEN_TYPE_6;
            }
            else if (size <= UiEventEntry.SCREEN_TYPE_9)
            {
                tmp = UiEventEntry.SCREEN_TYPE_9;
            }
            setScreenType(tmp);
        }
        else if (videoList.size() > tmp)
        {
            if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
            {
                isRechecked = true;
                screenSelector.check(R.id.screen_auto);
            }
            else if (screenType == UiEventEntry.SCREEN_TYPE_4)
            {
                isRechecked = true;
                screenSelector.check(R.id.screen_4);
            }
            else if (screenType == UiEventEntry.SCREEN_TYPE_6)
            {
                isRechecked = true;
                screenSelector.check(R.id.screen_6);
            }
            else if (screenType == UiEventEntry.SCREEN_TYPE_9)
            {
                isRechecked = true;
                screenSelector.check(R.id.screen_9);
            }
            ToastUtil.showToast("已打开的视频个数大于分屏个数，请关闭视频后再做操作！");
        }
        else
        {
            screenType = tmp;
            UiApplication.getConfigService().setVideoWindowCount(screenType);
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.VIDEO_WINDOW_CHANGE, screenType);
            setScreenType(screenType);
        }
    }

    @Override
    public void didReceivedNotification(final int id, final Object... args)
    {
        try
        {
            if (id == UiEventEntry.EVENT_ADD_REMOTE_VIDEO)
            {
                SurfaceView videoView = (SurfaceView) args[0];
                String sessionId = (String) videoView.getTag(R.id.session_id);
                String videoNum = (String) videoView.getTag(R.id.call_number);
                if (ServiceUtils.isCallExist(sessionId) || isVsExist(sessionId))
                {
                    for (SurfaceView surfaceView : videoList)
                    {
                        if (videoNum.equals(surfaceView.getTag(R.id.call_number)))
                        {
                            return;
                        }
                    }
                    Log.info(TAG, "EVENT_ADD_REMOTE_VIDEO: startRemoteVideo");
                    addRemoteVideo(sessionId, videoNum, videoView);
                }
//                            else
//                            {
//                                Log.info(TAG, "EVENT_ADD_REMOTE_VIDEO: stopRemoteVideo");
//                                removeRemoteVideo(sessionId);
//                            }
            }
            else if (id == UiEventEntry.EVENT_REMOVE_REMOTE_VIDEO)
            {
                Log.info(TAG, "EVENT_REMOVE_REMOTE_VIDEO: ");
                removeRemoteVideo((String) args[0]);
            }
            else if (id == UiEventEntry.NOTIFY_VIDEO_SWITCH)
            {
                Log.info(TAG, "NOTIFY_VIDEO_SWITCH: position:" + (Integer) args[2]);
                int postion = (Integer) args[2];
                int type = (Integer) args[3];
                String number = "";
                String confId = "";
                switch (type)
                {
                    case CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO:
                        ConfMemberItem confMemberItem = CallEventHandler.getConfMemberItems().get(postion);
                        number = confMemberItem.getName();
                        confId = confMemberItem.getConfId();
                        // 如果视频已发起，则直接替换视频
                        for (SurfaceView surfaceView : videoList)
                        {
                            if (number.equals(surfaceView.getTag(R.id.call_number)))
                            {
                                vf.onSwitchVideo((Integer) args[0], (Integer) args[1], surfaceView, number);
                                return;
                            }
                        }
                        // 通知目标位置视频更新
                        if (vf.onSwitchVideo((Integer) args[0], (Integer) args[1], null, number))
                        {
                            // 重新发起新视频
                            if (confMemberItem.getConfMemModel().getStatus() == CommonConstantEntry.CONF_MEMBER_STATE_CONNECT)
                            {
                                UiApplication.getConfService().confUserVideoEnable(confId, number, true);
                            }
                            else if (confMemberItem.getConfMemModel().getStatus() == CommonConstantEntry.CONF_MEMBER_STATE_IDLE)
                            {
                                UiApplication.getConfService().confUserAdd(confId, number);
                            }
                        }
                        break;
                    case CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE:
                        number = UiApplication.getVsService().getVsUserList().get(postion).getUserName();
                        // 如果视频已发起，则直接替换视频
                        for (SurfaceView surfaceView : videoList)
                        {
                            if (number.equals(surfaceView.getTag(R.id.call_number)))
                            {
                                vf.onSwitchVideo((Integer) args[0], (Integer) args[1], surfaceView, number);
                                return;
                            }
                        }
                        // 通知目标位置视频更新
                        if (vf.onSwitchVideo((Integer) args[0], (Integer) args[1], null, number))
                        {
                            // 重新发起新视频
                            UiApplication.getVsService().openVs(number);
                        }
                        break;

                    default:
                        break;
                }
            }
            else if (id == UiEventEntry.EVENT_RELEASE_PRESENTATION_SCREEN)
            {
                Log.info(TAG, "EVENT_RELEASE_PRESENTATION_SCREEN: videoList.size():" + videoList.size());
//                            vf.cleanVideoItems();
                for (SurfaceView surfaceView : videoList)
                {
                    surfaceView.setTag(R.id.video_status, true);
                    vf.addVideoItem(surfaceView);
                }
            }
            else if (id == UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW)
            {
                Log.info(TAG, "EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW: ");
                String videoNum = (String) args[0];
                if (!TextUtils.isEmpty(videoNum))
                {
                    for (SurfaceView surfaceView : videoList)
                    {
                        if (videoNum.equals(surfaceView.getTag(R.id.call_number)))
                        {
                            vf.addVideoItem(surfaceView);
                        }
                    }
                }
            }
            else if (id == UiEventEntry.NOTIFY_VIDEO_PTZ_CHANGE)
            {
                Log.info(TAG, "NOTIFY_VIDEO_PTZ_CHANGE: ");
                if (args.length > 0)
                {
                    String videoNum = (String) args[0];
                    vf.resetSelectedItem(videoNum);
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_video_all_presentation:
                Log.info(TAG, "all video presentation:: ");
                if (UiApplication.presentation != null && videoList.size() > 0)
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.OPEN_PRESENTATION_CONTROL);
                    for (final VideoItem videoItem : vf.getVideoItems())
                    {
                        if (videoItem.getSurFaceView() != null)
                        {
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    if (VideoPrstCtrlFragment.getAvailablePrstScreenSize() > 0)
                                    {
                                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_ADD_PRESENTATION_VIDEO,
                                                videoItem.getSurFaceView());
                                        videoItem.setSurFaceView(null);
                                    }
                                }
                            }, 300);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private boolean isVsExist(String sessionId)
    {
        for (VsListItem vsItem : UiApplication.getVsService().getVsUserList())
        {
            if (vsItem.isOpened() && sessionId.equals(vsItem.getVsModel().getSessionId()))
            {
                return true;
            }
        }
        return false;
    }
}
