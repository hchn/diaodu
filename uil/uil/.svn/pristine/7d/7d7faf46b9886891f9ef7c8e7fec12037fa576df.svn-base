package com.jiaxun.uil.ui.screen;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.ui.view.TopStatusPaneView;
import com.jiaxun.uil.ui.view.presentation.DefaultPresentation;
import com.jiaxun.uil.ui.view.presentation.VideoPresentation;
import com.jiaxun.uil.util.FgManager;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiUtils;

/**
 * 说明：父类的Activity
 *
 * @author  wangxue
 *
 * @Date 2015-2-28
 */
public abstract class BaseActivity extends Activity implements OnClickListener
{
    private static final String TAG = BaseActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        initViewData();
        initComponentViews();
        UiApplication.initPresentation(this);
    }

    @Override
    protected void onResume()
    {
//        initTopStatusView();
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        return;
    }

    public void initTopStatusView()
    {
        ViewParent viewParent = TopStatusPaneView.getInstance().getView().getParent();
        if (viewParent != null)
        {
            ((FrameLayout) viewParent).removeAllViews();
        }
        ((FrameLayout) findViewById(R.id.layout_top)).addView(TopStatusPaneView.getInstance().getView());
    }

    public void removeTopStatusView()
    {
        ((FrameLayout) findViewById(R.id.layout_top)).removeAllViews();
    }

    public void turnToNewFragment(int containerViewId, Class<? extends Fragment> toFragmentClass, Bundle args)
    {
        FgManager.turnToNewFragment(getFragmentManager(), containerViewId, toFragmentClass, false, args);
    }

    public void turnToNewFragment(int containerViewId, Class<? extends Fragment> toFragmentClass, boolean addBackStack, Bundle args)
    {
        FgManager.turnToNewFragment(getFragmentManager(), containerViewId, toFragmentClass, addBackStack, args);
    }

    public void turnToFragmentStack(int containerViewId, Class<? extends Fragment> toFragmentClass, Bundle args)
    {
        FgManager.turnToFragmentStack(getFragmentManager(), containerViewId, toFragmentClass, args);
    }

    public void turnToFragmentStack(int containerViewId, Class<? extends Fragment> toFragmentClass)
    {
        FgManager.turnToFragmentStack(getFragmentManager(), containerViewId, toFragmentClass, null);
    }

    public void clearFragmentStack(int containerViewId)
    {
        FgManager.clearFragmentStack(containerViewId);
        ((FrameLayout) findViewById(containerViewId)).removeAllViews();
    }

    public void backToPreFragment(int containerViewId)
    {
        try
        {
            Log.info("BaseActivity", "backToPreFragment::");
            FgManager.backToPreFragment(getFragmentManager(), containerViewId);
        }
        catch (Exception e)
        {
            Log.exception("BaseActivity", e);
        }
    }

    public void removeFragmentFromBackStack(int containerViewId, Class<? extends Fragment> toFragmentClass)
    {
        try
        {
            Log.info("BaseActivity", "removeFragmentFromBackStack::");
            FgManager.removeFragmentFromBackStack(getFragmentManager(), containerViewId, toFragmentClass);
        }
        catch (Exception e)
        {
            Log.exception("BaseActivity", e);
        }
    }

    /**
     * 方法说明 : 初始化页面显示数据
     * @return void
     * @author hubin
     * @Date 2015-3-17
     */
    public abstract void initViewData();

    /**
     * 方法说明 : 初始化页面控件的布局
     * @return void
     * @author hubin
     * @Date 2015-3-17
     */
    public abstract void initComponentViews();

    /**
     * 方法说明 : 初始化页面的布局
     * @return void
     * @author hubin
     * @Date 2015-3-17
     */
    public abstract int getLayoutView();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        UiUtils.startLockScreenServer();
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        Log.info(TAG, "onKeyDown::keyCode: " + keyCode);
        boolean isCalling = false;
        ArrayList<CallListItem> callList = ServiceUtils.getCurrentCallList();
        AudioManager am = UiApplication.getAudioManager();
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_VOLUME_UP:
                for (CallListItem callListItem : callList)
                {
                    if ((callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
                            && callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
                    {
                        isCalling = true;
                    }
                    else if ((callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                            && callListItem.getStatus() == CommonConstantEntry.CONF_STATE_CONNECT)
                    {
                        isCalling = true;
                    }
                }

                if (isCalling)
                {
                    am.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                else
                {
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                }
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                for (CallListItem callListItem : callList)
                {
                    if ((callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
                            && callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
                    {
                        isCalling = true;
                    }
                    else if ((callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                            && callListItem.getStatus() == CommonConstantEntry.CONF_STATE_CONNECT)
                    {
                        isCalling = true;
                    }
                }

                if (isCalling)
                {
                    am.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }
                else
                {
                    am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }

                return true;

            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
