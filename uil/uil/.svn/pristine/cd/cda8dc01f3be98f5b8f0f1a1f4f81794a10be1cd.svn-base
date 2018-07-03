package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.version.VersionClient;
import com.jiaxun.setting.model.DeviceInfo;
import com.jiaxun.setting.ui.adapter.PreDeviceInfoAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.ServiceUtils;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-6-5
 */
public class PrefsDeviceInfoFragment extends Fragment
{

//    "1.设备名称：T**30                           
//    2.公司信息：佳讯公司名，LOGO                
//    3.版权：版权信息                        
//    4.操作系统信息：操作系统版本                    
//    5.软件版本信息软件版本信息：T30_Vxx_YYMMDD    
//    6.硬件信息：硬件版本信息（根据硬件设计修订）
//    7.数据存储信息：显示存储空间大小、可用空间大小  
//    8.产品序号：显示产品序号"

    private ListView deviceInfoListView;
//    private boolean isUpDate = false;
    private UpDateVersionReceiver upDateVersionReceiver;
    private IntentFilter filter;
    private String versionName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        versionClient = new VersionClient(getActivity());
        upDateVersionReceiver = new UpDateVersionReceiver();
        filter = new IntentFilter(CommonEventEntry.EVENT_PROGRESS_CHANGE);
        getActivity().registerReceiver(upDateVersionReceiver, filter);
        versionName = UiApplication.getInstance().getAppVersionName();
        initView(view);

        return view;
    }

    @Override
    public void onDestroy()
    {
        getActivity().unregisterReceiver(upDateVersionReceiver);
        super.onDestroy();
    }

    @Override
    public void onStart()
    {
        // 进入界面获取有没有版本更新
        isUpDate();
        super.onStart();
    }

    private void initData()
    {
        List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

        DeviceInfo devicesInfo = new DeviceInfo();
        devicesInfo.setType("设备名称");
        devicesInfo.setName("T30");
        deviceList.add(devicesInfo);

        DeviceInfo devicesInfo1 = new DeviceInfo();
        devicesInfo1.setType("公司信息");
        devicesInfo1.setName("北京佳讯飞鸿电气股份有限公司");
        deviceList.add(devicesInfo1);

        DeviceInfo devicesInfo2 = new DeviceInfo();
        devicesInfo2.setType("版权");
        devicesInfo2.setName(getString(R.string.app_copy_right));
        deviceList.add(devicesInfo2);

        DeviceInfo devicesInfo3 = new DeviceInfo();
        devicesInfo3.setType("操作系统信息");
        devicesInfo3.setName("操作系统版本");
        deviceList.add(devicesInfo3);

        DeviceInfo devicesInfo4 = new DeviceInfo();
        devicesInfo4.setType("软件信息");
        devicesInfo4.setName("软件版本：" + versionName);
        deviceList.add(devicesInfo4);

        DeviceInfo devicesInfo5 = new DeviceInfo();
        devicesInfo5.setType("硬件信息");
        devicesInfo5.setName("硬件版本信息");
        deviceList.add(devicesInfo5);

        DeviceInfo devicesInfo6 = new DeviceInfo();
        devicesInfo6.setType("数据存储信息");
        devicesInfo6.setName("已用空间:0G,可用空间：0G");
        deviceList.add(devicesInfo6);

        DeviceInfo devicesInfo7 = new DeviceInfo();
        devicesInfo7.setType("产品序号");
        devicesInfo7.setName("2015060562455588");
        deviceList.add(devicesInfo7);

        madapter = new PreDeviceInfoAdapter(getActivity(), deviceList, versionClient, deviceInfoListView);
        deviceInfoListView.setAdapter(madapter);

    }

    private void initView(View view)
    {
        deviceInfoListView = (ListView) view.findViewById(R.id.device_info_listview);
        initData();

    }

    private VersionClient versionClient;
    private String newVersion = "";
    private PreDeviceInfoAdapter madapter;

    /**
     * 判断有没有最新版本
     * 方法说明 :
     * @author chaimb
     * @Date 2015-6-11
     */
    private void isUpDate()
    {

        boolean isUpDate = ServiceUtils.isVersionUpdate(getActivity());
        newVersion = ServiceUtils.serverVersionName;
        madapter.setUpDate(isUpDate);
        if (!(TextUtils.isEmpty(newVersion)))
        {
            madapter.setNewVersion(newVersion);
        }

    }

    private class UpDateVersionReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int fileLength = intent.getExtras().getInt(CommonEventEntry.PARAM_PROGRESS_MAX);
            int offset = intent.getExtras().getInt(CommonEventEntry.PARAM_PROGRESS);
            if (madapter != null)
            {
                madapter.updetaProgressBar(fileLength, offset);
            }
        }

    }
}
