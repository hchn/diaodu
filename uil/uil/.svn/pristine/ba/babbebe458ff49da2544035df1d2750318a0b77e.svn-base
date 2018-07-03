package com.jiaxun.setting.blutooth.adapter;

import java.util.List;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.setting.blutooth.model.BtBaseItem;
import com.jiaxun.setting.blutooth.model.BtBondedItem;
import com.jiaxun.setting.blutooth.model.BtGroupItem;
import com.jiaxun.setting.blutooth.model.BtItemType;
import com.jiaxun.uil.R;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 蓝牙适配器
 * 
 * @author zhangxd
 * 
 * @Date 2015-5-22
 */
public class BtAdapter extends BaseAdapter
{
    private List<BtBaseItem> mData = null;
    private Context context;
    private LayoutInflater mInflater = null;

    public BtAdapter(Context context, List<BtBaseItem> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        mData = data;
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub

        if (mData == null)
        {
            return 0;
        }
        return this.mData.size();
    }

    @Override
    public boolean isEnabled(int position)
    {
        if ((this.getItemViewType(position) == BtItemType.BTGROUP))
        {
            return false;
        }
        return true;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getViewTypeCount()
    {
        return BtItemType.ITEM_TYPE_MAX_COUNT;
    }

    @Override
    public int getItemViewType(int position)
    {
        return mData.get(position).getItemType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        
        BtBaseItem btBaseItem = mData.get(position);
        // Log.i("itemType", itemType+"btBaseItem"+btBaseItem.getItemType());
        switch (btBaseItem.getItemType())
        {
            case BtItemType.BTGROUP:
                convertView = btGroupItem(convertView, btBaseItem);
                break;
            case BtItemType.BOND_BONDED:
                convertView = btBoundedItem(convertView, btBaseItem);
                break;
            case BtItemType.BOND_NONE:
                convertView = btBoundedNoneItem(convertView, btBaseItem);
                break;
            default:
                break;
        }
        return convertView;
    }

    private class ViewHolder
    {
        // public static final int ITEM_VIEW_TYPE = 1;
        public ImageView deviceIv = null;
        public TextView deviceName = null;
        public TextView deviceConnected = null;
        public ImageView devicePaired = null;
    }

    private View btGroupItem(View convertView, BtBaseItem item)
    {
        View groupView = null;
        ViewHolder viewHolderTextItem = null;
        if (convertView == null)
        {
            viewHolderTextItem = new ViewHolder();
            groupView = this.mInflater.inflate(R.layout.adapter_setting_list_item_group, null, false);
            // viewHolderTextItem.deviceIv = (ImageView)
            // groupView.findViewById(R.id.deviceIv);
            viewHolderTextItem.deviceName = (TextView) groupView.findViewById(R.id.textViewGroup);
            // viewHolderTextItem.devicePaired = (ImageView)
            // groupView.findViewById(R.id.devicePaired);
            groupView.setTag(viewHolderTextItem);
            convertView = groupView;
        }
        else
        {
            viewHolderTextItem = (ViewHolder) convertView.getTag();
        }
        viewHolderTextItem.deviceName.setText(((BtGroupItem) item).getName());
        return convertView;
    }
    private View btBoundedItem(View convertView, BtBaseItem item)
    {
        BluetoothDevice btBluetoothDevice = item.getBtDevice();
        View boundedView = null;
        ViewHolder viewHolderBounded = null;
        if (convertView == null)
        {
            viewHolderBounded = new ViewHolder();
            boundedView = this.mInflater.inflate(R.layout.adapter_bt_item, null, false);
            viewHolderBounded.deviceIv = (ImageView) boundedView.findViewById(R.id.deviceIv);
            viewHolderBounded.deviceName = (TextView) boundedView.findViewById(R.id.deviceName);
            viewHolderBounded.devicePaired = (ImageView) boundedView.findViewById(R.id.devicePaired);
            viewHolderBounded.deviceConnected = (TextView) boundedView.findViewById(R.id.deviceConnected);
            boundedView.setTag(viewHolderBounded);
            convertView = boundedView;
        }
        else
        {
            viewHolderBounded = (ViewHolder) convertView.getTag();
        }
        //如果没有名字显示mac地址
        if(TextUtils.isEmpty(btBluetoothDevice.getName()))
        {
            viewHolderBounded.deviceName.setText(btBluetoothDevice.getAddress());
        }else
        {
            viewHolderBounded.deviceName.setText(btBluetoothDevice.getName());
        }
       
        viewHolderBounded.devicePaired.setVisibility(View.VISIBLE);
        switch (btBluetoothDevice.getBluetoothClass().getDeviceClass())
        {
            case BluetoothClass.Device.COMPUTER_DESKTOP:
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
            case BluetoothClass.Device.COMPUTER_LAPTOP:
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
            case BluetoothClass.Device.COMPUTER_SERVER:
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.pc);
                break;
            case BluetoothClass.Device.PHONE_SMART:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.telephone);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.headset1);
                break;
            default:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.ic_launcher);
        }
        if (((BtBondedItem) item).getA2dpConnected())
        {
             viewHolderBounded.deviceConnected.setVisibility(View.VISIBLE);
        }
        else
        {

            viewHolderBounded.deviceConnected.setVisibility(View.GONE);
        }
        return convertView;
    }

    private View btBoundedNoneItem(View convertView, BtBaseItem item)
    {
        BluetoothDevice btBluetoothDevice = item.getBtDevice();
        View boundedView = null;
        ViewHolder viewHolderBounded = null;
        if (convertView == null)
        {
            viewHolderBounded = new ViewHolder();
            boundedView = this.mInflater.inflate(R.layout.adapter_bt_item, null, false);
            viewHolderBounded.deviceIv = (ImageView) boundedView.findViewById(R.id.deviceIv);
            viewHolderBounded.deviceName = (TextView) boundedView.findViewById(R.id.deviceName);
            viewHolderBounded.devicePaired = (ImageView) boundedView.findViewById(R.id.devicePaired);
            viewHolderBounded.deviceConnected = (TextView) boundedView.findViewById(R.id.deviceConnected);
            boundedView.setTag(viewHolderBounded);
            convertView = boundedView;
        }
        else
        {
            viewHolderBounded = (ViewHolder) convertView.getTag();
        }
        //娌℃湁鍚嶅瓧鏄剧ずmac鍦板潃
        if(TextUtils.isEmpty(btBluetoothDevice.getName()))
        {
            viewHolderBounded.deviceName.setText(btBluetoothDevice.getAddress());
        }else
        {
            viewHolderBounded.deviceName.setText(btBluetoothDevice.getName());
        }
        viewHolderBounded.devicePaired.setVisibility(View.INVISIBLE);
        viewHolderBounded.deviceConnected.setVisibility(View.GONE);
        switch (btBluetoothDevice.getBluetoothClass().getDeviceClass())
        {
            case BluetoothClass.Device.COMPUTER_DESKTOP:
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
            case BluetoothClass.Device.COMPUTER_LAPTOP:
            case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA:
            case BluetoothClass.Device.COMPUTER_SERVER:
            case BluetoothClass.Device.COMPUTER_UNCATEGORIZED:
            case BluetoothClass.Device.COMPUTER_WEARABLE:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.pc);
                break;
            case BluetoothClass.Device.PHONE_SMART:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.telephone);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.headset1);
                break;
            default:
                viewHolderBounded.deviceIv.setImageResource(R.drawable.ic_launcher);
                break;
        }
        return convertView;
    }
}
