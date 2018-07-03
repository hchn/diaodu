package com.jiaxun.setting.blutooth.fragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jiaxun.setting.blutooth.adapter.BtAdapter;
import com.jiaxun.setting.blutooth.model.BtBaseItem;
import com.jiaxun.setting.blutooth.model.BtBondedItem;
import com.jiaxun.setting.blutooth.model.BtBondedNoneItem;
import com.jiaxun.setting.blutooth.model.BtGroupItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：蓝牙配置界面
 *
 * @author  zhangxd
 *
 * @Date 2015-7-13
 */
public class BtFragment extends BaseFragment implements OnClickListener, OnItemClickListener, OnItemLongClickListener
{
    private Button bluetoothButtton;
    private ListView listView;
    /**
     * 配对组(已经配对设备)是否存在
     */
    private boolean bounedGroupExist = false;
    /**
     * 未配对组(可用设备)是否存在
     */
    private boolean unBounedGroupExist = false;
    private List<BtBaseItem> btDeviceList;
    /**
     * 已经 配对设备
     */
    private Set<BluetoothDevice> boundedSet = null;
    /**
     * 搜索到未配对设备
     */
    private List<BluetoothDevice> btDevices = new ArrayList<BluetoothDevice>();
    private BluetoothAdapter bluetoothAdapter;
    private Button btSerch;
    private Button cancelButton;
    private BtAdapter btAdapter;
    private BluetoothDevice selectedBtDevice;
    private BluetoothA2dp a2dp;
    private IntentFilter discoveryFilter;
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
//                    btDeviceList.clear();
//                    initData();
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

    public BtFragment()
    {
        if(BluetoothAdapter.getDefaultAdapter().isEnabled())
        {
            BluetoothAdapter.getDefaultAdapter().getProfileProxy(getActivity(), bs, BluetoothProfile.A2DP);
        }
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_bluetooth;
    }

    @Override
    public void initComponentViews(View view)
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 获取a2dp
        // bluetoothAdapter.getProfileProxy(getActivity(), bs,
        // BluetoothProfile.A2DP);
        btSerch = (Button) view.findViewById(R.id.btSerch);
        btSerch.setOnClickListener(this);
        if (!bluetoothAdapter.isEnabled())
        {
            btSerch.setEnabled(false);
        }else if(bluetoothAdapter.isDiscovering())
        {
            btSerch.setText("正在搜索");
        }
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);
        bluetoothButtton = (Button) view.findViewById(R.id.bluetoothButtton);
        bluetoothButtton.setOnClickListener(this);
        listView = (ListView) view.findViewById(R.id.btListView);
        if (bluetoothAdapter.isEnabled())
        {
            bluetoothButtton.setBackgroundResource(R.drawable.boolean_on);
        }
        else
        {
            bluetoothButtton.setBackgroundResource(R.drawable.boolean_off);
        }
        if (bluetoothAdapter.isEnabled())
        {
            boundedSet = bluetoothAdapter.getBondedDevices();
        }
        selectedBtDevice = null;

        initData();
        btAdapter = new BtAdapter(getActivity(), btDeviceList);
        listView.setAdapter(btAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    public void onAttach(Activity activity)
    {
        /** 注册广播 */
        discoveryFilter = new IntentFilter();
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoveryFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        discoveryFilter.addAction(BluetoothAdapter.EXTRA_CONNECTION_STATE);
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        discoveryFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        discoveryFilter.addAction(BluetoothDevice.ACTION_FOUND);
        discoveryFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        discoveryFilter.addAction(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED);
        // 注册BroadcastReceiver
        getActivity().registerReceiver(discoveryReceiver, discoveryFilter);
        super.onAttach(activity);
    }

    // 蓝牙搜索广播的接收器
    private BroadcastReceiver discoveryReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            // 获取广播的Action
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                // 开始搜索
                btSerch.setText("正在搜索");
            }
            else if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                // 发现远程蓝牙设备
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE)
                {
                    discoverNewBt(bluetoothDevice);
                }
                btAdapter.notifyDataSetChanged();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                // 扫描完成
                btSerch.setText("搜索设备");
            }
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
            {
                // 配对状态改变
                if (selectedBtDevice != null)
                {
                    if (selectedBtDevice.getBondState() == BluetoothDevice.BOND_BONDED)
                    {
                        ToastUtil.showToast("提示", "配对成功");
                        if (bluetoothAdapter.isEnabled())
                        {
                            boundedSet = bluetoothAdapter.getBondedDevices();
                        }
                        clear();
                        //把配对设备从可用设备中移除
                        btDevices.remove(selectedBtDevice);
                        initData();
                        btAdapter.notifyDataSetChanged();
                    }
                    if (selectedBtDevice.getBondState() == BluetoothDevice.BOND_BONDING)
                    {
                        ToastUtil.showToast("提示", "正在配对");
                    }
                    if (selectedBtDevice.getBondState() == BluetoothDevice.BOND_NONE)
                    {
                        if (bluetoothAdapter.isEnabled())
                        {
                            boundedSet = bluetoothAdapter.getBondedDevices();
                        }
                        ToastUtil.showToast("提示", "取消配对");
                        clear();
                        initData();
                        btAdapter.notifyDataSetChanged();
                    }
                }
            }
            else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                // 蓝牙状态改变
                if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
                {
                    if (btAdapter == null)
                    {
                        btAdapter = new BtAdapter(getActivity(), btDeviceList);
                    }
                    if(a2dp==null)
                    {
                        BluetoothAdapter.getDefaultAdapter().getProfileProxy(getActivity(), bs, BluetoothProfile.A2DP);
                    }
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_BLUETOOTH_EVENT, true);
                    btSerch.setEnabled(true);
                    clear();
                    boundedSet = bluetoothAdapter.getBondedDevices();
                    initData();
                    bluetoothButtton.setBackgroundResource(R.drawable.boolean_on);
                }
                else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF)
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_BLUETOOTH_EVENT, false);
                    btDevices.clear();
                    clear();
                    btAdapter.notifyDataSetChanged();
                    btSerch.setEnabled(false);
                    bluetoothButtton.setBackgroundResource(R.drawable.boolean_off);
                }
            }
            else if (BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED.equals(action))
            {
                SystemClock.sleep(500);
                if ((a2dp != null)&&(bluetoothAdapter.isEnabled()))
                {
                    for (BtBaseItem bt : btDeviceList)
                    {
                        BluetoothDevice btDevice = bt.getBtDevice();
                        if ((btDevice != null) && (btDevice.getBondState() == BluetoothDevice.BOND_BONDED))
                        {
                            int state = a2dp.getConnectionState(btDevice);
                            switch (state)
                            {
                                case BluetoothProfile.STATE_CONNECTED:
                                    ((BtBondedItem) bt).setA2dpConnected(true);
                                    btAdapter.notifyDataSetChanged();
                                    break;
                                case BluetoothProfile.STATE_CONNECTING:
                                    break;
                                case BluetoothProfile.STATE_DISCONNECTED:
                                    ((BtBondedItem) bt).setA2dpConnected(false);
                                    btAdapter.notifyDataSetChanged();
                                    break;
                                case BluetoothProfile.STATE_DISCONNECTING:
                                    // ToastUtil.showToast(getActivity(),
                                    // "正在断开连接");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onDestroy()
    {
        getActivity().unregisterReceiver(discoveryReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.cancelButton:
                ((SettingActivity) getActivity()).backWireSettingFragment(v.getId());
                break;
            case R.id.btSerch:
                if (bluetoothAdapter.isEnabled())
                {
                    bluetoothAdapter.startDiscovery();
                }
                break;
            case R.id.bluetoothButtton:
                bluetoothButtton.setEnabled(false);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // TODO Auto-generated method stub
                        bluetoothButtton.setEnabled(true);
                    }
                }, 1000);
                if (bluetoothAdapter.isEnabled())
                {
                    bluetoothAdapter.disable();

                }
                else
                {
                    // listView.setVisibility(View.VISIBLE);
                    bluetoothAdapter.enable();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        BtBaseItem btBaseItem = btDeviceList.get(position);
        selectedBtDevice = btBaseItem.getBtDevice();
        if (selectedBtDevice != null)
        {
            if (selectedBtDevice.getBondState() == BluetoothDevice.BOND_NONE)
            {
                // 还没配对的话配对
                // bluetoothDevice.createBond();
//                selectedBtDevice.createBond();
            }
            else if (selectedBtDevice.getBondState() == BluetoothDevice.BOND_BONDED)
            {
                bluetoothAdapter.cancelDiscovery();

                if ((a2dp != null))
                {
                    int state = a2dp.getConnectionState(selectedBtDevice);
                    try
                    {
                        switch (state)
                        {
                            case BluetoothProfile.STATE_CONNECTED:
                                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_BLUETOOTH_CONNECT_EVENT, true, "");
                                a2dp.getClass().getMethod("disconnect", BluetoothDevice.class).invoke(a2dp, selectedBtDevice);
                                break;
                            case BluetoothProfile.STATE_CONNECTING:

                                break;
                            case BluetoothProfile.STATE_DISCONNECTED:
                                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_BLUETOOTH_CONNECT_EVENT, true,
                                        selectedBtDevice.getName());
                                a2dp.getClass().getMethod("connect", BluetoothDevice.class).invoke(a2dp, selectedBtDevice);
                                // bluetoothAdapter.getProfileProxy(getActivity(),
                                // bs, BluetoothProfile.A2DP);
                                break;
                            case BluetoothProfile.STATE_DISCONNECTING:

                                break;
                            default:
                                break;
                        }
                    }
                    catch (IllegalAccessException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (IllegalArgumentException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (InvocationTargetException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (NoSuchMethodException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

        }
    }

    private void initData()
    {
        if (btDeviceList == null)
        {
            btDeviceList = new ArrayList<BtBaseItem>();
        }
        if ((boundedSet != null) && (boundedSet.size() > 0))
        {
            if (!bounedGroupExist)
            {
                bounedGroupExist = true;
                btDeviceList.add(new BtGroupItem("已经配对设备"));
                for (BluetoothDevice btDevice : boundedSet)
                {
                    if ((a2dp != null))
                    {
                        // 已经连接
                        if ((a2dp.getConnectionState(btDevice) == BluetoothProfile.STATE_CONNECTED))
                        {
                            btDeviceList.add(new BtBondedItem(btDevice, true));
                        }
                        else
                        {
                            // 未连接
                            btDeviceList.add(new BtBondedItem(btDevice, false));
                        }
                    }
                    else
                    {
                        BluetoothAdapter.getDefaultAdapter().getProfileProxy(getActivity(), bs, BluetoothProfile.A2DP);
                        btDeviceList.add(new BtBondedItem(btDevice, false));
                    }
                }
            }
        }
        if (bluetoothAdapter.isEnabled())
        {
            if (!unBounedGroupExist)
            {
                unBounedGroupExist = true;
                btDeviceList.add(new BtGroupItem("可用设备"));
                if (btDevices.size() > 0)
                {
                    for (BluetoothDevice btBluetoothDevice : btDevices)
                    {
                        btDeviceList.add(new BtBondedNoneItem(btBluetoothDevice));
                        String name = btBluetoothDevice.getName();
                        String jj = name;
                    }
                }
            }

        }
        if (btAdapter != null)
        {
            btAdapter.notifyDataSetChanged();
        }
    }

    private void discoverNewBt(BluetoothDevice bluetoothDevice)
    {
        if (btDevices.indexOf(bluetoothDevice) == -1)// 防止重复添加
        {
            btDevices.add(bluetoothDevice); // 获取设

            btDeviceList.add(new BtBondedNoneItem(bluetoothDevice));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        BtBaseItem btBaseItem = btDeviceList.get(position);
        selectedBtDevice = btBaseItem.getBtDevice();
        if (selectedBtDevice.getBondState() == BluetoothDevice.BOND_BONDED)
        {
            // ClsUtils.printAllInform(selectedBtDevice.getClass());
            Method removeBondMethod;
            try
            {
                removeBondMethod = selectedBtDevice.getClass().getMethod("removeBond");
                try
                {
                    Boolean returnValue = (Boolean) removeBondMethod.invoke(selectedBtDevice);
                }
                catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            catch (NoSuchMethodException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         
        }
        return false;
    }

    /**
     * 方法说明 :清空listview数据列表
     * @author zhangxd
     * @Date 2015-8-13
     */
    private void clear()
    {
        if (btDeviceList != null)
        {
            btDeviceList.clear();
            if (boundedSet != null)
            {
                boundedSet = null;
            }

        }
        bounedGroupExist = false;
        unBounedGroupExist = false;
    }
}
