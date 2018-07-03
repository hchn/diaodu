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
 * ˵����
 *
 * @author  chaimb
 *
 * @Date 2015-6-5
 */
public class PrefsDeviceInfoFragment extends Fragment
{

//    "1.�豸���ƣ�T**30                           
//    2.��˾��Ϣ����Ѷ��˾����LOGO                
//    3.��Ȩ����Ȩ��Ϣ                        
//    4.����ϵͳ��Ϣ������ϵͳ�汾                    
//    5.����汾��Ϣ����汾��Ϣ��T30_Vxx_YYMMDD    
//    6.Ӳ����Ϣ��Ӳ���汾��Ϣ������Ӳ������޶���
//    7.���ݴ洢��Ϣ����ʾ�洢�ռ��С�����ÿռ��С  
//    8.��Ʒ��ţ���ʾ��Ʒ���"

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
        // ��������ȡ��û�а汾����
        isUpDate();
        super.onStart();
    }

    private void initData()
    {
        List<DeviceInfo> deviceList = new ArrayList<DeviceInfo>();

        DeviceInfo devicesInfo = new DeviceInfo();
        devicesInfo.setType("�豸����");
        devicesInfo.setName("T30");
        deviceList.add(devicesInfo);

        DeviceInfo devicesInfo1 = new DeviceInfo();
        devicesInfo1.setType("��˾��Ϣ");
        devicesInfo1.setName("������Ѷ�ɺ�����ɷ����޹�˾");
        deviceList.add(devicesInfo1);

        DeviceInfo devicesInfo2 = new DeviceInfo();
        devicesInfo2.setType("��Ȩ");
        devicesInfo2.setName(getString(R.string.app_copy_right));
        deviceList.add(devicesInfo2);

        DeviceInfo devicesInfo3 = new DeviceInfo();
        devicesInfo3.setType("����ϵͳ��Ϣ");
        devicesInfo3.setName("����ϵͳ�汾");
        deviceList.add(devicesInfo3);

        DeviceInfo devicesInfo4 = new DeviceInfo();
        devicesInfo4.setType("�����Ϣ");
        devicesInfo4.setName("����汾��" + versionName);
        deviceList.add(devicesInfo4);

        DeviceInfo devicesInfo5 = new DeviceInfo();
        devicesInfo5.setType("Ӳ����Ϣ");
        devicesInfo5.setName("Ӳ���汾��Ϣ");
        deviceList.add(devicesInfo5);

        DeviceInfo devicesInfo6 = new DeviceInfo();
        devicesInfo6.setType("���ݴ洢��Ϣ");
        devicesInfo6.setName("���ÿռ�:0G,���ÿռ䣺0G");
        deviceList.add(devicesInfo6);

        DeviceInfo devicesInfo7 = new DeviceInfo();
        devicesInfo7.setType("��Ʒ���");
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
     * �ж���û�����°汾
     * ����˵�� :
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
