package com.jiaxun.setting.wifi.adapter;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.setting.wifi.util.WifiUtils;
import com.jiaxun.uil.R;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-7-13
 */
public class PreWifiInfoAdapter extends BaseAdapter
{

    private List<ScanResult> wifiList;
    private Context context;
    private LayoutInflater inflater;
    
    private WifiUtils wifiUtils;

    public PreWifiInfoAdapter(Context context, List<ScanResult> wifiList)
    {
        this.context = context;
        this.wifiList = wifiList;
        inflater = LayoutInflater.from(context);
        wifiUtils = new WifiUtils(context);
    }

    @Override
    public int getCount()
    {
        if (wifiList != null && wifiList.size() > 0)
        {
            return wifiList.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_wifisetting_item, null);
            holder.wifiImageView = (ImageView) convertView.findViewById(R.id.wifi_level_imageview);
            holder.wifiSSIDTextView = (TextView) convertView.findViewById(R.id.wifi_ssid_textview);
            holder.wifiStatusTextView = (TextView) convertView.findViewById(R.id.wifi_status_textview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = wifiList.get(position);
        holder.wifiSSIDTextView.setText(scanResult.SSID);
        holder.wifiStatusTextView.setText(scanResult.capabilities);
        
        int level = scanResult.level;
        if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_BEST)
        {
            holder.wifiImageView.setImageResource(R.drawable.setting_wifi_three_icon);
        }
        else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_BETTER)
        {
            holder.wifiImageView.setImageResource(R.drawable.setting_wifi_three_icon);
        }
        else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_AVERAGE)
        {
            holder.wifiImageView.setImageResource(R.drawable.setting_wifi_two_icon);
        }
        else if (wifiUtils.getWifiLevel(level) == WifiUtils.SIGNAL_LEVEL_WEAK)
        {
            holder.wifiImageView.setImageResource(R.drawable.setting_wifi_one_icon);
        }
        else
        {
            holder.wifiImageView.setImageResource(R.drawable.setting_wifi_one_icon);
        }
        return convertView;
    }

    class ViewHolder
    {

        ImageView wifiImageView;
        TextView wifiSSIDTextView;
        TextView wifiStatusTextView;
    }

    /**
     * 更新wifi列表
     * 方法说明 :
     * @param wifiList
     * @author chaimb
     * @Date 2015-7-13
     */
    public void addData(List<ScanResult> wifiList)
    {
        if (wifiList != null && wifiList.size() > 0)
        {
            this.wifiList = wifiList;
            notifyDataSetChanged();
        }
        else
        {
            return;
        }
    }

    /**
     * 清空wifi列表
     * 方法说明 :
     * @param wifiList
     * @author chaimb
     * @Date 2015-7-13
     */
    public void clearData()
    {
        if (wifiList == null && wifiList.size() == 0)
        {
            return;
        }
        this.wifiList.clear();
        notifyDataSetChanged();
    }

}
