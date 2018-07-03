package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.R.integer;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.view.presentation.VideoFrameView;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：扩展屏控制类
 *
 * @author  HeZhen
 *
 * @Date 2015-7-15
 */
public class VideoPrstCtrlFragment extends BaseFragment implements NotificationCenterDelegate, OnClickListener, OnCheckedChangeListener
{
    private static final String TAG = VideoPrstCtrlFragment.class.getName();
    private RadioGroup screenSelector;
    private VideoFrameView vf;
    // 用于切换窗口时非法，重置回原窗口个数
    private boolean isRechecked;
    private static ArrayList<SurfaceView> videoList = new ArrayList<SurfaceView>();
    private int screenType;

    public VideoPrstCtrlFragment()
    {
        screenType = UiApplication.getConfigService().getPresentationWindowCount();
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CLOSE_PRESENTATION_CONTROL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_RELEASE_PRESENTATION_SCREEN);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_ADD_PRESENTATION_VIDEO);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_presentation_control;
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");
        LinearLayout container = ((LinearLayout) view.findViewById(R.id.relay_main));
        vf = new VideoFrameView(getActivity());
        Point p = new Point();
        p.x = UiUtils.homeRightContainerW;
        p.y = UiUtils.homeRightContainerW / 3 * 2;
        vf.init(p);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.topMargin = 100;
        lp.width = p.x - 10 * 2;
        lp.height = p.y;
        vf.setLayoutParams(lp);
        vf.setBackgroundResource(R.color.black);
        vf.setMini(true);
        vf.setRelationVideoFrame(UiApplication.presentation.getVf());
        UiApplication.presentation.getVf().setRelationVideoFrame(vf);
        container.addView(vf);
        screenSelector = (RadioGroup) view.findViewById(R.id.screen_selector);
        view.findViewById(R.id.btn_back).setOnClickListener(this);
        view.findViewById(R.id.btn_cloud_camera).setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_UPDATE_RIGHT_TAB, UiEventEntry.TAB_PRESENTATION);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume()
    {
        Log.info(TAG, "onResume::");
        initScreenSelector();
        for (SurfaceView surfaceView : videoList)
        {
            vf.addVideoItem(surfaceView);
        }
        super.onResume();
    }

    public void setScreenType(int type)
    {
        if (vf.isFullScreen())
        {
            vf.fullScreenToNormal(type);
            if (vf.getRelationVideoFrame() != null)
            {
                vf.getRelationVideoFrame().fullScreenToNormal(type);
            }
        }
        else
        {
            vf.setScreenSize(type);
            if (vf.getRelationVideoFrame() != null)
            {
                vf.getRelationVideoFrame().setScreenSize(type);
            }
        }
    }

    private void initScreenSelector()
    {
        if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
        {
            setScreenType(UiEventEntry.SCREEN_TYPE_1);
        }
        else
        {
            setScreenType(screenType);
        }
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
//            case UiEventEntry.SCREEN_TYPE_8:
//                screenSelector.check(R.id.screen_8);
//                break;
            case UiEventEntry.SCREEN_TYPE_9:
                screenSelector.check(R.id.screen_9);
                break;
            default:
                break;
        }
        screenSelector.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDestroy()
    {
//        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CLOSE_PRESENTATION_CONTROL);
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_back:
                if (UiApplication.presentation != null && UiApplication.presentation.isShowing())
                {
//                    UiApplication.presentation.dismiss();
//                    UiApplication.presentation.cancel();
                    videoList.clear();
                    vf.cleanVideoItems();
                    if (vf.getRelationVideoFrame() != null)
                    {
                        vf.getRelationVideoFrame().cleanVideoItems();
                        // 还原扩展屏窗口分屏
                        vf.getRelationVideoFrame().setScreenSize(screenType);
                    }
                    parentActivity.backToPreFragment(R.id.container_right_content);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.EVENT_RELEASE_PRESENTATION_SCREEN);
                }
                break;
            case R.id.btn_cloud_camera:
                break;
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.CLOSE_PRESENTATION_CONTROL)
        {
            Log.info(TAG, "CLOSE_PRESENTATION_CONTROL");
            parentActivity.backToPreFragment(R.id.container_right_content);
        }
        else if (id == UiEventEntry.EVENT_RELEASE_PRESENTATION_SCREEN)
        {
            Log.info(TAG, "EVENT_RELEASE_PRESENTATION_SCREEN: ");
            videoList.clear();
            vf.cleanVideoItems();
            if (vf.getRelationVideoFrame() != null)
            {
                vf.getRelationVideoFrame().cleanVideoItems();
            }
        }
        else if (id == UiEventEntry.EVENT_ADD_PRESENTATION_VIDEO)
        {
            Log.info(TAG, "EVENT_ADD_PRESENTATION_VIDEO");
            SurfaceView surfaceView = (SurfaceView) args[0];
            addVideo(surfaceView);
        }
        else if (id == UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO || id == UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW)
        {
            String videoNum = (String) args[0];
            Log.info(TAG, "EVENT_REMOVE_PRESENTATION_VIDEO | EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW");
            if (removeVideo(videoNum))
            {
                vf.removeVideoItem(videoNum);
                if (vf.getRelationVideoFrame() != null)
                {
                    vf.getRelationVideoFrame().removeVideoItem(videoNum);
                }
                // 自动分屏状态调整分屏窗口
                if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
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
            UiApplication.getConfigService().setPresentationWindowCount(screenType);
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
            ToastUtil.showToast("已打开的视频个数大于分屏个数，请关闭关闭视频后再做操作！");
        }
        else
        {
            screenType = tmp;
            UiApplication.getConfigService().setPresentationWindowCount(screenType);
            setScreenType(screenType);
        }
    }

    private boolean removeVideo(String number)
    {
        Log.info(TAG, "removeVideo::number: " + number);
        if (TextUtils.isEmpty(number))
        {
            return false;
        }
        for (SurfaceView surfaceView : videoList)
        {
            if (number.equals(surfaceView.getTag(R.id.call_number)))
            {
                videoList.remove(surfaceView);
                Log.info(TAG, "removeVideo:: videoList.size(): " + videoList.size());
                return true;
            }
        }
        return false;
    }

    public static int getAvailablePrstScreenSize()
    {
        int screenType = UiApplication.getConfigService().getPresentationWindowCount();
        if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
        {
            return UiEventEntry.SCREEN_TYPE_9 - videoList.size();
        }
        else
        {
            return screenType - videoList.size();
        }
    }

    private void addVideo(SurfaceView video)
    {
        if (video == null)
        {
            return;
        }
        Log.info(TAG, "addVideo:: screenType: " + screenType + " userNum:" + video.getTag(R.id.call_number) + " videoList.size():" + videoList.size());
        if (screenType != UiEventEntry.SCREEN_TYPE_AUTO && videoList.size() == screenType)
        {
            ToastUtil.showToast(String.format("扩展屏超出最大视频个数，%s视频显示失败", video.getTag(R.id.call_number)));
            return;
        }

        if (screenType == UiEventEntry.SCREEN_TYPE_AUTO)
        {
            int screenSize = vf.getVideoItems().size();
            Log.info(TAG, "videoList.size(): " + videoList.size() + " screenSize:" + screenSize);
            // 当视频总是达到窗口个数时触发自动适应屏幕
            if (screenSize == videoList.size())
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
                    ToastUtil.showToast(String.format("扩展屏超出最大视频个数，%s视频显示失败", video.getTag(R.id.call_number)));
                    return;
                }
            }
            vf.addVideoItem(video);
            if (vf.getRelationVideoFrame() != null)
            {
                vf.getRelationVideoFrame().addVideoItem(video);
            }
        }
        else
        {
            vf.addVideoItem(video);
            if (vf.getRelationVideoFrame() != null)
            {
                vf.getRelationVideoFrame().addVideoItem(video);
            }
        }
        videoList.add(video);
    }
}
