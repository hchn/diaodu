package com.jiaxun.setting.wifi.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.setting.wifi.adapter.PreWifiInfoAdapter;
import com.jiaxun.setting.wifi.util.WifiUtils;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-7-13
 */
public class PrefsWifiSettingFragment extends BaseFragment implements OnCheckedChangeListener, OnItemClickListener, OnClickListener
{
    private final String TAG = PrefsWifiSettingFragment.class.getName();

    public static int WIFI_OBJECT = 0;

    private CheckBox wifiControlStateBox;
    private ListView wifiListView;
    private PreWifiInfoAdapter wifiAdapter;
    private List<ScanResult> wifiList;

    private WifiUtils wifiUtils;
    private WifiStateReceiver receiver;

    private boolean isConnected;

    private Button cancelButton;

    private TextView whichEthernet;

    private Button saveButton;

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_wifisetting;
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");
        // 注册wifi监听
        registReceiver();

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        
        saveButton.setText("扫描");
        whichEthernet = (TextView) view.findViewById(R.id.textViewWhichInfo);
        whichEthernet.setText("WIFI设置");
        wifiList = new ArrayList<ScanResult>();
        wifiUtils = new WifiUtils(getActivity());
        wifiControlStateBox = (CheckBox) view.findViewById(R.id.wifi_control_checkbox);
        wifiListView = (ListView) view.findViewById(R.id.wifi_listview);

        wifiAdapter = new PreWifiInfoAdapter(getActivity(), wifiList);
        wifiListView.setAdapter(wifiAdapter);

        initData();
        setListener();

        isConnected = wifiUtils.isConnected(getActivity());

    }

    private void initData()
    {
        boolean isWifiEnabled = wifiUtils.isWifiEnable();

        if (isWifiEnabled)
        {// 当前wifi状态：打开
            wifiControlStateBox.setChecked(true);
            wifiUtils.startScan();
            wifiList = wifiUtils.getWifiList();

            Log.info(TAG, "isConnected::" + isConnected);
            if (isConnected)
            {
//                ToastUtil.showToast(getActivity(), "", "网络已连接" + wifiUtils.getSSID());
                Log.info(TAG, wifiUtils.getSSID());
                String SSID = wifiUtils.getSSID();

                for (ScanResult scanResult : wifiList)
                {
                    String ssid = "\"" + scanResult.SSID.trim() + "\"";

                    if (SSID.equals(ssid))
                    {
                        wifiList.remove(scanResult);
                        wifiList.add(0, scanResult);
                        wifiAdapter.addData(wifiList);
                        break;
                    }
                }
            }
            else
            {
                wifiAdapter.addData(wifiList);
            }

        }
        else
        {// 当前wifi状态：关闭
            wifiControlStateBox.setChecked(false);
            if (wifiList != null && wifiList.size() > 0)
            {
                wifiAdapter.clearData();
            }
        }
    }

    private void setListener()
    {
        wifiControlStateBox.setOnCheckedChangeListener(this);
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);

        wifiListView.setOnItemClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {

        if (isChecked)
        {
            Log.info(TAG, "open wifi");
            wifiUtils.openWifi();
        }
        else
        {
            Log.info(TAG, "close wifi");
            wifiAdapter.clearData();
            wifiUtils.closeWifi();
        }

    }

    private void registReceiver()
    {
        receiver = new WifiStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        getActivity().registerReceiver(receiver, filter);
    }

    private class WifiStateReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction()))
            {// 这个监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                Log.info(TAG, "wifiState" + wifiState);
                switch (wifiState)
                {
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.info(TAG, "WIFI_STATE_DISABLING");
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.info(TAG, "WIFI_STATE_DISABLED");
                        saveButton.setEnabled(false);
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_WIFI_EVENT, false);
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.info(TAG, "WIFI_STATE_ENABLING");
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.info(TAG, "WIFI_STATE_ENABLED");
                        saveButton.setEnabled(true);
//                        ToastUtil.showToast(getActivity(), "", "wifi已经打开");
                        setWifiList();

                        break;

                    default:
                        break;
                }
            }
            // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
            // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
//            if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction()))
//            {
//                Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//                if (null != parcelableExtra)
//                {
//                    NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
//                    State state = networkInfo.getState();
//                    boolean isConnected = state == State.CONNECTED;// 当然，这边可以更精确的确定状态
//                    Log.info(TAG, "isConnected" + isConnected);
//                    if (isConnected)
//                    {
//                        ToastUtil.showToast(getActivity(), "", "wifi已连接");
//                    }
//                    else
//                    {
//                        ToastUtil.showToast(getActivity(), "", "wifi未连接");
//                    }
//                }
//            }

        }

    }

    private void setWifiList()
    {
        Log.info(TAG, "setWifiList::");
        try
        {// 如果wifi已经打开，睡眠1秒在开始扫描，要是立马执行扫描不到
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            Log.exception(TAG, e);
        }

        wifiUtils.startScan();
        wifiList = wifiUtils.getWifiList();
        wifiAdapter.addData(wifiList);
    }

    @Override
    public void onDestroy()
    {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable("wifiInfo", wifiList.get(position));
        parentActivity.turnToFragmentStack(R.id.container_setting_right, PrefsConnectWifiFragment.class, bundle);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cancelButton:
                ((SettingActivity) getActivity()).backWireSettingFragment(v.getId());
                break;
            case R.id.saveButton:
                wifiUtils.startScan();
                wifiList = wifiUtils.getWifiList();
                wifiAdapter.addData(wifiList);
                break;

            default:
                break;
        }

    }

}
