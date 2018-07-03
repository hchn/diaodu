package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.adapter.SettingAdapter;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsGroupItem;
import com.jiaxun.setting.model.PrefsLeftItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;

/**
 * 说明：用于显示配置目录列表
 * 
 * @author hubin
 * 
 * @Date 2015-3-4
 */
public class PrefsListFragment extends ListFragment
{
    private static final String TAG = PrefsListFragment.class.getSimpleName();
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    // private static ArrayList<String> prefsList = new ArrayList<String>();
    private SettingAdapter settingAdapter = null;
    private List<PrefsBaseItem> settingData = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.info(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        initData();
    }

    @SuppressLint("NewApi")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
        {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
        settingAdapter = new SettingAdapter(getActivity(), settingData);

        getListView().setDivider(getResources().getDrawable(R.drawable.divider));
        setListAdapter(settingAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        super.onListItemClick(listView, view, position, id);

        PrefsBaseItem item = settingData.get(position);
        if (item != null && item.getItemCallBack() != null)
            item.getItemCallBack().onCallBackResult(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION)
        {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick)
    {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position)
    {
        if (position == ListView.INVALID_POSITION)
        {
            getListView().setItemChecked(mActivatedPosition, false);
        }
        else
        {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void initData()
    {
        settingData = new ArrayList<PrefsBaseItem>();

        settingData.add(new PrefsGroupItem("设置"));
        settingData.add(new PrefsLeftItem("设备信息", -1, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                ((SettingActivity) getActivity()).turnToNewFragment(R.id.container_setting_right, PrefsDeviceInfoFragment.class, null);
            }
        }));
//        settingData.add(new PrefsLeftItem("无线和网络", -1, new PrefsBaseItem.ItemCallBack()
        settingData.add(new PrefsLeftItem("网络设置", -1, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                ((SettingActivity) getActivity()).turnToNewFragment(R.id.container_setting_right, WirelessFragment.class, null);
            }
        }));
        settingData.add(new PrefsLeftItem("系统设置", -1, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                ((SettingActivity) getActivity()).turnToFragmentStack(R.id.container_setting_right, PrefsServiceAccountFragment.class, null);
            }
        }));
        settingData.add(new PrefsLeftItem("操作台用户", -1, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
                ((SettingActivity) getActivity()).turnToNewFragment(R.id.container_setting_right, AttendantlistFragment.class, null);
            }
        }));
//        settingData.add(new PrefsLeftItem("修改设置密码", -1, new PrefsBaseItem.ItemCallBack()
//        {
//            @Override
//            public void onCallBackResult(boolean result)
//            {
//                ((SettingActivity) getActivity()).turnToNewFragment(R.id.container_setting_right, ConfigPasswordFragment.class, null);
//            }
//        }));
        settingData.add(new PrefsLeftItem("通讯录配置", -1, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
//                ((SettingActivity) getActivity()).turnToNewFragment(R.id.container_setting_right, ContactEditListFragment.class, null);
                ((SettingActivity) getActivity()).showSettingContactView();
            }
        }));
        settingData.add(new PrefsLeftItem("通话记录", -1, new PrefsBaseItem.ItemCallBack()
        {
            @Override
            public void onCallBackResult(boolean result)
            {
//                ((SettingActivity) getActivity()).turnToNewFragment(R.id.container_setting_right, CallRecordScreenFrament.class, null);
                ((SettingActivity) getActivity()).turnToFragmentStack(R.id.container_setting_right, CallRecordScreenFrament.class, null);
            }
        }));
//        settingData.add(new PrefsLeftItem("黑白名单", -1, new PrefsBaseItem.ItemCallBack()
//        {
//            @Override
//            public void onCallBackResult(boolean result)
//            {
//                ((SettingActivity) getActivity()).turnToFragmentStack(R.id.container_setting_right, BlackWhiteListFragment.class, null);
//            }
//        }));
    }

}
