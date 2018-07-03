package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jiaxun.setting.adapter.SettingAdapter;
import com.jiaxun.setting.model.EthernetItem;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsBaseItem.ItemCallBack;
import com.jiaxun.setting.model.PrefsRightItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.util.UiConfigEntry;

/**
 * 说明：
 *
 * @author  zhangxd
 *
 * @Date 2015-7-9
 */
public class WirelessFragment extends ListFragment
{
    private static final String TAG = WirelessFragment.class.getName();
    private SettingAdapter settingAdapter = null;
    private List<PrefsBaseItem> settingData = null;
    protected SettingActivity parentActivity;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(getResources().getDrawable(R.drawable.divider));
        initData();
        settingAdapter = new SettingAdapter(getActivity(), settingData);
        setListAdapter(settingAdapter);
        parentActivity = (SettingActivity) getActivity();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        super.onListItemClick(listView, view, position, id);

        PrefsBaseItem item = settingData.get(position);
        if (item != null && item.getItemCallBack() != null)
            item.getItemCallBack().onCallBackResult(true);
    }

    public void initData()
    {
        settingData = new ArrayList<PrefsBaseItem>();
        settingData.add(new EthernetItem("以太网口1设置", -1,UiConfigEntry.PREF_ETHERNET1, new ItemCallBack()
        {

            @Override
            public void onCallBackResult(boolean result)
            {
                // TODO Auto-generated method stub
                parentActivity.loadNetworkSettingView(settingData.get(0));
            }
        }));
        settingData.add(new EthernetItem("以太网口2设置", -1,UiConfigEntry.PREF_ETHERNET2, new ItemCallBack()
        {

            @Override
            public void onCallBackResult(boolean result)
            {
                // TODO Auto-generated method stub
                parentActivity.loadNetworkSettingView(settingData.get(1));
            }
        }));

//        settingData.add(new PrefsRightItem("WIFI设置", R.drawable.ic_launcher, new ItemCallBack()
//        {
//
//            @Override
//            public void onCallBackResult(boolean result)
//            {
//                // TODO Auto-generated method stub
//                 parentActivity.loadWifiSettingView();
//            }
//        }));
//        settingData.add(new PrefsRightItem("蓝牙设置", R.drawable.ic_launcher, new ItemCallBack()
//        {
//
//            @Override
//            public void onCallBackResult(boolean result)
//            {
//                parentActivity.loadBluetoothView();
//            }
//        }));
    }

}
