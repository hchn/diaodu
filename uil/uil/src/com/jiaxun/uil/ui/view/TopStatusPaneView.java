package com.jiaxun.uil.ui.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.MemerInfo;
import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.setting.wifi.util.WifiUtils;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.TopInfoItem;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.ui.widget.TopInfoWindow;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：标题栏 常驻内存。（待定 依据设计需求变更）
 *
 * @author  HeZhen
 *
 * @Date 2015-4-30
 */
public class TopStatusPaneView implements OnClickListener, NotificationCenterDelegate
{
    private static TopStatusPaneView topView;
    private static final String TAG = TopStatusPaneView.class.getName();
    private static final int LOWMEMORY = 1;
    private View view;

    private TextView systemName;
    private TextView systemTime;
    private TextView accountNameTV;
    private TextView serviceNumberTV;

    private ImageView notifyMsgImg;
    private ImageView notifyCallImg;
    private ImageView notifySpeakerImg;
    private ImageView notifyNightServiceImg;
    private ImageView notifyCameraImg;
    private ImageView notifyServiceServerImg;
    private ImageView notifyHeadsetImg;
    private ImageView notifyBluetoothImg;
    private ImageView notifyWifiImg;
    private ImageView notifySignalImg;

    private ImageView settingImage;
    private ImageView companyLogoImage;

    private Runnable mTimeUpdateThread;

    private Drawable statusIcon;

    private Context mContext;

    private TopInfoWindow topInfoPopupWindow;
    private String accountName = "";
    private String serviceNum = "离线";

    private static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    private LinearLayout systemInfoLayout;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothA2dp a2dp;
    private WifiUtils wifiUtils;
    private List<TopInfoItem> topDataItemsNotify;

    BluetoothProfile.ServiceListener bs = new BluetoothProfile.ServiceListener()
    {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy)
        {
            try
            {
                if (profile == BluetoothProfile.A2DP)
                {
                    a2dp = (BluetoothA2dp) proxy;
                    setBlueImg();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(int profile)
        {
        }
    };
    private boolean isUpDate;
    private String loginTime = "";

    public TopStatusPaneView()
    {

        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_WIFI_EVENT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_BLUETOOTH_EVENT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_BLUETOOTH_CONNECT_EVENT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SYSTME_NAME_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_ATD_LOGIN_DATETIME);

        getView();

        systemInfoLayout = (LinearLayout) findViewById(R.id.system_info_linearlayout);

        statusIcon = UiApplication.getInstance().getResources().getDrawable(R.drawable.statueusericon);
        statusIcon.setBounds(0, 0, statusIcon.getMinimumWidth(), statusIcon.getMinimumHeight());
        systemName = (TextView) findViewById(R.id.system_name);
        systemTime = (TextView) findViewById(R.id.system_time);
        accountNameTV = (TextView) findViewById(R.id.account_name);
        serviceNumberTV = (TextView) findViewById(R.id.service_number);
        notifyCallImg = (ImageView) findViewById(R.id.top_statue_pane_phone);
        notifyBluetoothImg = (ImageView) findViewById(R.id.top_statue_pane_bluetooth);
        notifyCameraImg = (ImageView) findViewById(R.id.top_statue_pane_carmera);
        notifyServiceServerImg = (ImageView) findViewById(R.id.top_statue_pane_service_server);
        notifyHeadsetImg = (ImageView) findViewById(R.id.top_statue_pane_headset);
        notifyMsgImg = (ImageView) findViewById(R.id.top_statue_pane_message);
        notifySignalImg = (ImageView) findViewById(R.id.top_statue_pane_signal);
        notifyNightServiceImg = (ImageView) findViewById(R.id.top_status_pane_night_service);
        notifyWifiImg = (ImageView) findViewById(R.id.top_statue_pane_wifi);
        settingImage = (ImageView) findViewById(R.id.setting_btn);
        companyLogoImage = (ImageView) findViewById(R.id.company_logo);

//        findViewById(R.id.setting_btn).setOnClickListener(this);
        mContext = UiApplication.getInstance().getCurrentContext();
        topInfoPopupWindow = new TopInfoWindow(mContext);
        topDataItemsNotify = new ArrayList<TopInfoItem>();
        wifiUtils = new WifiUtils(mContext);

        Log.info(TAG, "wifiUtils.isWifiEnable()::" + wifiUtils.isWifiEnable());

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null)
        {
            bluetoothAdapter.getProfileProxy(mContext, bs, BluetoothProfile.A2DP);
        }
        setWifiImg();

        initTimer();

        lowMemory();

        setListener();

        Log.info(TAG, "systemInfoLayout.getWidth()::" + systemInfoLayout.getWidth());

        ConfigHelper configHelper = ConfigHelper.getDefaultConfigHelper(mContext);
        systemName.setText(configHelper.getString(UiConfigEntry.SYSTEM_NAME, UiConfigEntry.SYSTEM_DISPLAY_NAME));

        isUpDate = ServiceUtils.isVersionUpdate(mContext);

        if (isUpDate)
        {
            setMsgNofity(R.drawable.message);
        }
    }

    private void setBlueImg()
    {
        // 暂时屏蔽设置蓝牙通知图标
//        if (bluetoothAdapter.isEnabled())
//        {
//            setBlutoothNotify(R.drawable.bluetooth);
//            Set<BluetoothDevice> bd = bluetoothAdapter.getBondedDevices();
//            for (BluetoothDevice bluetoothDevice : bd)
//            {
//                if (a2dp != null)
//                {
//                    int state = a2dp.getConnectionState(bluetoothDevice);
//                    if (state == BluetoothProfile.STATE_CONNECTED)
//                    {
//                        topDataItemsNotify.add(new TopInfoItem(3, bluetoothDevice.getName(), -1, 1));
//                        break;
//                    }
//                }
//            }
//        }
//        else
//        {
//            setBlutoothNotify(-1);
//        }

    }

    /**
     * 方法说明 :在TopView设置wifi图标
     * @author chaimb
     * @Date 2015-8-24
     */
    private void setWifiImg()
    {
        // wifi是否打开
        boolean isOpen = wifiUtils.isWifiEnable();
        // wifi是否连接
        boolean isConnect = wifiUtils.isConnected(mContext);

        if (isOpen)
        {
            wifiUtils.startScan();
            if (isConnect)
            {// wifi已连接
                String Ssid = wifiUtils.getSSID();
                topDataItemsNotify.add(new TopInfoItem(3, Ssid, -1, 0));
                List<ScanResult> wifiList = wifiUtils.getWifiList();
                int level = 1;
                for (ScanResult scanResult : wifiList)
                {
                    if (Ssid.equals("\"" + scanResult.SSID + "\""))
                    {
                        level = scanResult.level;
                        break;
                    }
                }

                setWifiNofity(1, level);

            }
            else
            {// wifi未连接
                List<ScanResult> wifiList = wifiUtils.getWifiList();
                List<WifiConfiguration> wifiConfigurations = wifiUtils.getConfiguration();
                if (wifiList != null && wifiList.size() > 0)
                {// 有扫描到的wifi
                    if (wifiConfigurations != null && wifiConfigurations.size() > 0)
                    {// 有保存的wifi
                        List<ScanResult> resultList = new ArrayList<ScanResult>();
                        for (ScanResult scanResult : wifiList)
                        {
                            for (WifiConfiguration wifiConfiguration : wifiConfigurations)
                            {
                                if (scanResult.SSID.equals(wifiConfiguration.SSID))
                                {
                                    resultList.add(scanResult);
                                }
                            }
                        }

                        if (resultList != null && resultList.size() > 0)
                        {// 得到相同的结果集，即：扫描到的wifi有本地保存的，可以直接连接
                            List<ScanResult> list = wifiUtils.sortByLevel(resultList);
                            ScanResult scanResult = list.get(0);
                            for (WifiConfiguration wifiConfiguration : wifiConfigurations)
                            {
                                if (scanResult.SSID.equals(wifiConfiguration.SSID))
                                {
                                    wifiUtils.addNetWork(wifiConfiguration.networkId);
                                    setWifiNofity(0, scanResult.level);
                                    break;
                                }
                            }
                        }
                        else
                        {// 没有得到相同的结果集
                            Log.info(TAG, "no resultList::(scan and save)");
                        }
                    }
                    else
                    {// 没有保存的wifi
                        Log.info(TAG, "no wifi save!!");
                        setWifiNofity(-1, 0);
                    }
                }
                else
                {// 没有扫描到wifi
                    setWifiNofity(-1, 0);
                    Log.info(TAG, "no wifi list");
                }

            }

        }
        else
        {// 未打开
            setWifiNofity(-1, 0);
        }

    }

    private void setListener()
    {
        accountNameTV.setOnClickListener(this);
        serviceNumberTV.setOnClickListener(this);
        systemTime.setOnClickListener(this);
        settingImage.setOnClickListener(this);
        systemInfoLayout.setOnClickListener(this);
    }

    /**
     * 磁盘空间不足告警，默认三分钟执行一次
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-8
     */
    private void lowMemory()
    {
        // 监听磁盘空间不足的计时器
        TimerTask timerTask = new TimerTask()
        {
            public void run()
            {
                Log.info(TAG, "Memory Occupancy:" + MemerInfo.getMemoryPer());
                Log.info(TAG, "cpu Occupancy:" + MemerInfo.getCpuPer());
                Log.info(TAG, "Free Memory:" + MemerInfo.getAppMemory());
                boolean isLow = MemerInfo.getSDPer(mContext);
                if (isLow)
                {
//                    Message msg = myHandler.obtainMessage();
//                    msg.what = LOWMEMORY;
//                    myHandler.sendMessage(msg);
                    new Handler(UiApplication.getInstance().getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setMsgNofity(R.drawable.message);

                            if (topDataItemsNotify != null && topDataItemsNotify.size() > 0)
                            {

                                for (TopInfoItem topInfoItem : topDataItemsNotify)
                                {
                                    if (topInfoItem.getNotifyType() == 2)
                                    {
                                        topDataItemsNotify.remove(topInfoItem);
                                    }
                                }
                            }

                            TopInfoItem topInfoItem = new TopInfoItem(3, "磁盘空间不足百分之八十,请及时删除文件", -1, 2);
                            topDataItemsNotify.add(topInfoItem);
                            ToastUtil.showToast("磁盘空间不足");
                        }
                    });
                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 1000, 300000);
    }

    public void setServiceNumber(int serviceStatu, String serviceNum)
    {
        Drawable drawable = null;
        String serviceStatue = "";

        if (serviceStatu == CommonConstantEntry.SERVICE_STATUS_ACTIVE)
        {// 主用在线
            drawable = mContext.getResources().getDrawable(R.drawable.slave_online);
//            serviceStatue = "主用后台在线";
            serviceStatue = "主在线:";

        }
        else if (serviceStatu == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {// 备用在线
            drawable = mContext.getResources().getDrawable(R.drawable.main_online);
            serviceStatue = "备在线:";
        }
        else
        {// 离线
            drawable = mContext.getResources().getDrawable(R.drawable.main_online);
            serviceStatue = "";
        }
        this.serviceNum = serviceStatue + serviceNum;
//        serviceNumberTV.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
//        serviceNumberTV.setCompoundDrawablePadding(5);// 设置图片和text之间的间距
        serviceNumberTV.setText(serviceStatue + serviceNum);
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
        accountNameTV.setText(accountName);
        accountNameTV.setCompoundDrawables(statusIcon, null, null, null);
        topInfoPopupWindow.setWidth(500);
    }

    private void setNotifyIcon(ImageView imageView, int imgRes)
    {
        if (imgRes < 0)
        {
            imageView.setVisibility(View.GONE);
        }
        else
        {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(imgRes);
        }
    }

    public void setCallNotify(int imgRes)
    {
        setNotifyIcon(notifyCallImg, imgRes);
    }

    public void setBlutoothNotify(int imgRes)
    {
        setNotifyIcon(notifyBluetoothImg, imgRes);
    }

    public void setCameraNotify(int imgRes)
    {
        setNotifyIcon(notifyCameraImg, imgRes);
    }

    public void setServiceServerNotify(int imgRes)
    {
        setNotifyIcon(notifyServiceServerImg, imgRes);
    }

    public void setHeadsetNofity(int imgRes)
    {
        setNotifyIcon(notifyHeadsetImg, imgRes);
    }

    public void setMsgNofity(int imgRes)
    {
        setNotifyIcon(notifyMsgImg, imgRes);
    }

    public void setSignalNofity(int imgRes)
    {
        setNotifyIcon(notifySignalImg, imgRes);
    }

    public void setWifiNofity(int imgRes, int level)
    {
        // 屏蔽wifi通知图标显示
//        if (imgRes == -1)
//        {
//            imgRes = -1;
//        }
//        else
//        {
//            if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_BEST)
//            {
//                imgRes = R.drawable.wifi_best;
//            }
//            else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_BETTER)
//            {
//                imgRes = R.drawable.wifi_best;
//            }
//            else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_AVERAGE)
//            {
//                imgRes = R.drawable.wifi_avrange;
//            }
//            else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_WEAK)
//            {
//                imgRes = R.drawable.wifi_terrible;
//            }
//            else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_TERRIBLE)
//            {
//                imgRes = R.drawable.wifi_terrible;
//            }
//        }
//
//        setNotifyIcon(notifyWifiImg, imgRes);
    }

    public void setSpeakerNofity(int imgRes)
    {
        setNotifyIcon(notifySpeakerImg, imgRes);
    }

    public void setNightServiceNofity(int imgRes)
    {
        setNotifyIcon(notifyNightServiceImg, imgRes);
    }

    private View findViewById(int id)
    {
        return getView().findViewById(id);
    }

    public static TopStatusPaneView getInstance()
    {
        if (topView == null)
        {
            topView = new TopStatusPaneView();
        }
        return topView;
    }

    public static TopStatusPaneView getInstanceNew()
    {
        topView = null;
        topView = new TopStatusPaneView();
        return topView;
    }

    public View getView()
    {
        if (view == null)
        {
            view = LayoutInflater.from(UiApplication.getCurrentContext()).inflate(R.layout.pane_top_status, null);
        }
        return view;
    }

    /**
     * 方法说明 : 开启一个线程，用来实时显示时间   //非子线程 递归原理
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    private void initTimer()
    {
        final SimpleDateFormat timeFormat = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        mTimeUpdateThread = new Runnable()
        {
            @Override
            public void run()
            {
                new Handler(UiApplication.getInstance().getMainLooper()).postDelayed(mTimeUpdateThread, 1000);
                systemTime.setText(timeFormat.format(Calendar.getInstance().getTime()));
            }
        };
        new Handler(UiApplication.getInstance().getMainLooper()).post(mTimeUpdateThread);
    }

    public void hidePop()
    {
        if (topInfoPopupWindow != null)
        {
            topInfoPopupWindow.dismiss();
        }
    }
    
    public void setSettingImg(int imgRes)
    {
        settingImage.setImageResource(imgRes);
    }
    
    public void setCompanyLogoImg(int imgRes)
    {
        companyLogoImage.setImageResource(imgRes);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.setting_btn:
                if (UiApplication.getCurrentContext() instanceof HomeActivity)
                {
                    if (ServiceUtils.getCurrentCallList().size() == 0 && UiApplication.getVsService().getOpenVsCount() == 0)
                    {
                        Intent intent = new Intent(UiApplication.getCurrentContext(), SettingActivity.class);
                        UiApplication.getCurrentContext().startActivity(intent);
//                        settingImage.setImageResource(R.drawable.ic_top_home);
                    }
                    else
                    {
                        ToastUtil.showToast("请先结束正在进行的呼叫业务");
                    }
                }
                else if (UiApplication.getCurrentContext() instanceof SettingActivity)
                {
                    Intent intent = new Intent(UiApplication.getCurrentContext(), HomeActivity.class);
                    intent.putExtra("isExit", false);
                    UiApplication.getCurrentContext().startActivity(intent);
//                    settingImage.setImageResource(R.drawable.ic_top_setting);
                    ((SettingActivity) UiApplication.getCurrentContext()).finish();
                }
                break;

            case R.id.account_name:
                // 操作台用户

                List<TopInfoItem> topDataItemsName = new ArrayList<TopInfoItem>();
                topDataItemsName.add(new TopInfoItem(1, accountName + "\n登录时间:" + loginTime, -1));
                topInfoPopupWindow.setData(topDataItemsName);

                topInfoPopupWindow.showAsDropDown(v);

                break;
            case R.id.service_number:
                // 用户号码

                List<TopInfoItem> topDataItemsNum = new ArrayList<TopInfoItem>();
                topDataItemsNum.add(new TopInfoItem(2, serviceNum, -1));
                topInfoPopupWindow.setData(topDataItemsNum);

                topInfoPopupWindow.showAsDropDown(v);

                break;
            case R.id.system_time:
                // 系统时间

                List<TopInfoItem> topDataItemsTime = new ArrayList<TopInfoItem>();
                String time = DateUtils.getNowFormatTime();
                topDataItemsTime.add(new TopInfoItem(3, time, -1));
                topInfoPopupWindow.setData(topDataItemsTime);

                topInfoPopupWindow.showAsDropDown(v);

                break;
            case R.id.system_info_linearlayout:

                topInfoPopupWindow.setData(topDataItemsNotify);

                topInfoPopupWindow.showAsDropDown(v);

                break;
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.NOTIFY_WIFI_EVENT)
        {
            boolean isConnect = (Boolean) args[0];
            if (isConnect)
            {
//                setWifiImg();
                Log.info(TAG, "wifi is connect!!");
                if (topDataItemsNotify != null && topDataItemsNotify.size() > 0)
                {
                    for (TopInfoItem topDataItem : topDataItemsNotify)
                    {
                        if (topDataItem.getNotifyType() == 0)
                        {
                            topDataItemsNotify.remove(topDataItem);
                            break;
                        }
                    }
                }
                String SSID = (String) args[1];
                int level = (Integer) args[2];
                setWifiNofity(0, level);
                topDataItemsNotify.add(new TopInfoItem(3, SSID, -1, 0));
            }
            else
            {
                setWifiNofity(-1, 0);
            }
        }
        else if (id == UiEventEntry.NOTIFY_BLUETOOTH_EVENT)
        {
            boolean isConnect = (Boolean) args[0];
            if (isConnect)
            {
                setBlutoothNotify(R.drawable.bluetooth);
            }
            else
            {
                setBlutoothNotify(-1);
            }
        }
        else if (id == UiEventEntry.NOTIFY_BLUETOOTH_CONNECT_EVENT)
        {
            boolean isConnect = (Boolean) args[0];
            if (isConnect)
            {
                setBlutoothNotify(R.drawable.bluetooth);
                String name = (String) args[1];
                if (topDataItemsNotify != null && topDataItemsNotify.size() > 0)
                {
                    for (TopInfoItem topDataItem : topDataItemsNotify)
                    {
                        if (topDataItem.getNotifyType() == 1)
                        {
                            topDataItemsNotify.remove(topDataItem);
                            break;
                        }
                    }
                }
                if (TextUtils.isEmpty(name))
                {
                    Log.info(TAG, "bluelooth not connect");

                }
                else
                {
                    topDataItemsNotify.add(new TopInfoItem(3, name, -1, 1));
                }
            }
            else
            {
                setBlutoothNotify(-1);
            }
        }
        else if (id == UiEventEntry.NOTIFY_SYSTME_NAME_CHANGE)
        {
            String name = (String) args[0];
            if (!(TextUtils.isEmpty(name)))
            {
                systemName.setText(name);
            }
        }
        else if (id == UiEventEntry.NOTIFY_ATD_LOGIN_DATETIME)
        {
            loginTime = (String) args[0];
        }

    }
}
