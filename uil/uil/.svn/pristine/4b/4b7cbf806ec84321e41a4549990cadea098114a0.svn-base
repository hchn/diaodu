package com.jiaxun.setting.wifi.ui.fragment;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jiaxun.setting.wifi.util.WifiUtils;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.StaticIpSet;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-7-15
 */
public class PrefsConnectWifiFragment extends BaseFragment implements OnCheckedChangeListener, OnItemSelectedListener, OnClickListener
{

    private final String TAG = PrefsConnectWifiFragment.class.getName();

    private WifiUtils wifiUtils;

    private Spinner seniorSpinner;
    private CheckBox seniorSettingCheckbox;
    private LinearLayout seniorLayout;
    private LinearLayout staticLayout;
    private LinearLayout passwordLayout;

    private Button connectButton;
    private Button cancleButton;
    private Button removeButton;

    private TextView signalTextView;
    private TextView securityTextView;
    private EditText wifiPassEditText;

    private EditText ipAddEditText;
    private EditText getWayEditText;

    private ScanResult scanResult;

    private TextView whichEthernet;

    private boolean isConnected = false;

    private int security;

    private TextView passTextview;

    private WifiConfiguration wifiConfiguration;

    private int level;

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_connect_wifi;
    }

    @Override
    public void initComponentViews(View view)
    {

        Bundle bundle = getArguments();
        wifiUtils = new WifiUtils(getActivity());
        wifiUtils.startScan();
        scanResult = bundle.getParcelable("wifiInfo");
        seniorSpinner = (Spinner) view.findViewById(R.id.wifi_option_spinner);
        seniorSettingCheckbox = (CheckBox) view.findViewById(R.id.senior_setting_checkbox);
        seniorLayout = (LinearLayout) view.findViewById(R.id.senior_linearlayout);
        staticLayout = (LinearLayout) view.findViewById(R.id.static_linearlayout);
        passwordLayout = (LinearLayout) view.findViewById(R.id.password_layout);
        connectButton = (Button) view.findViewById(R.id.saveButton);
        removeButton = (Button) view.findViewById(R.id.remove_button);
        cancleButton = (Button) view.findViewById(R.id.cancelButton);
        whichEthernet = (TextView) view.findViewById(R.id.textViewWhichInfo);
        passTextview = (TextView) view.findViewById(R.id.pass_textview);
        whichEthernet.setText(scanResult.SSID);

        signalTextView = (TextView) view.findViewById(R.id.signal_textview);
        securityTextView = (TextView) view.findViewById(R.id.security_textview);
        wifiPassEditText = (EditText) view.findViewById(R.id.wifi_pass_edittext);

        ipAddEditText = (EditText) view.findViewById(R.id.ip_add_edittext);
        getWayEditText = (EditText) view.findViewById(R.id.getway_edittext);

        wifiConfiguration = wifiUtils.isExsits(scanResult.SSID);

        isConnected = wifiUtils.isConnected(getActivity());

        if (("\"" + scanResult.SSID + "\"").equals(wifiUtils.getSSID()) && isConnected)
        {// 选择网络是已连接网络，隐藏密码框
            passwordLayout.setVisibility(View.GONE);
            connectButton.setText("断开网络");
            removeButton.setText("不保存");
            removeButton.setVisibility(View.VISIBLE);
//            connectButton.setEnabled(false);
            ipAddEditText.setHint(wifiUtils.getIpv4Address());
            getWayEditText.setHint(wifiUtils.getWay());
        }
        else
        {// 选择网络不是已连接网络，显示密码框
            removeButton.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.VISIBLE);
            connectButton.setEnabled(true);
            connectButton.setText("连接");

            if (wifiConfiguration != null)
            {// 以前配置过此网络
                passTextview.setText("已保存此网络，可直接连接!");
                wifiPassEditText.setVisibility(View.GONE);
            }
            else
            {
                if (security == WifiUtils.SECURITY_NONE)
                {// 无密码，也没保存此网络
                    passTextview.setText("无密码，请直接连接！！！");
                    wifiPassEditText.setVisibility(View.GONE);
                }
                else
                {
                    passTextview.setText("密码");
                    wifiPassEditText.setVisibility(View.VISIBLE);
                }

            }
        }

        security = wifiUtils.getCipherType(scanResult.SSID);

        if (security == WifiUtils.SECURITY_NONE)
        {
            securityTextView.setText("NONE");
        }
        else if (security == WifiUtils.SECURITY_WEP)
        {
            securityTextView.setText("WEP");
        }
        else
        {
            securityTextView.setText("WPA");
        }

        level = scanResult.level;
        if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_BEST)
        {
            signalTextView.setText("强");
        }
        else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_BETTER)
        {
            signalTextView.setText("较强");
        }
        else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_AVERAGE)
        {
            signalTextView.setText("一般");
        }
        else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_WEAK)
        {
            signalTextView.setText("弱");
        }
        else
        {
            signalTextView.setText("无信号");
        }

        initData();
        setListener();
    }

    private void setListener()
    {
        seniorSettingCheckbox.setOnCheckedChangeListener(this);
        seniorSpinner.setOnItemSelectedListener(this);

        connectButton.setOnClickListener(this);
        cancleButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
    }

    private void initData()
    {
        seniorSettingCheckbox.setChecked(false);
        String[] optionItems = getResources().getStringArray(R.array.wifi_senior_setting);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.wifi_simple_spinner_item, optionItems);
        // 绑定 Adapter到控件
        seniorSpinner.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        if (isChecked)
        {
            seniorLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            seniorLayout.setVisibility(View.GONE);
            staticLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String status = parent.getItemAtPosition(position).toString();
        if (status.equals("静态"))
        {
            staticLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            staticLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.saveButton:
                if (isConnected)
                {
                    wifiUtils.disConnectionWifi(wifiUtils.getNetWordId());
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_WIFI_EVENT, false);
                    parentActivity.backToPreFragment(R.id.container_setting_right);
                }
                else
                {
                    String passWord = wifiPassEditText.getText().toString().trim();
                    int wifiId = -1;
                    if (wifiConfiguration != null)
                    {
                        wifiId = wifiUtils.getWifiId(wifiConfiguration);
                    }
                    else
                    {
                        if (staticLayout.getVisibility() == View.VISIBLE)
                        {// 静态设置ip地址
                            WifiConfiguration wifiConfiguration = wifiUtils.createWifiInfo(scanResult.SSID, passWord, security);
                            String ipAddress = ipAddEditText.getText().toString().trim();
                            String getWay = getWayEditText.getText().toString().trim();

                            if (TextUtils.isEmpty(ipAddress))
                            {
                                ToastUtil.showToast("ip地址不能为空");
                                return;
                            }
                            if (TextUtils.isEmpty(getWay))
                            {
                                ToastUtil.showToast("网关地址不能为空");
                                return;
                            }

                            StaticIpSet staticIpSet = new StaticIpSet(getActivity(), ipAddress, 24, getWay, getWay);
                            staticIpSet.confingStaticIp(wifiConfiguration);

                            wifiId = wifiUtils.getWifiId(wifiConfiguration);
                        }
                        else
                        {// 自动获取ip
                            WifiConfiguration configuration = wifiUtils.createWifiInfo(scanResult.SSID, passWord, security);

                            wifiId = wifiUtils.getWifiId(configuration);
                        }
                    }

                    if (wifiId == -1)
                    {
                        ToastUtil.showToast("网络连接错误！");
                    }
                    else
                    {
                        wifiUtils.addNetWork(wifiId);

                        wifiPassEditText.setText("");
                        parentActivity.backToPreFragment(R.id.container_setting_right);
                        // TODO 设置wifi图标
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_WIFI_EVENT, true, scanResult.SSID, level);

                    }

                }

                break;
            case R.id.cancelButton:
                parentActivity.backToPreFragment(R.id.container_setting_right);
                break;
            case R.id.remove_button:
                wifiUtils.removeConnectionWifi(wifiUtils.getNetWordId());
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_WIFI_EVENT, false);
                parentActivity.backToPreFragment(R.id.container_setting_right);
                break;

            default:
                break;
        }
    }

}
