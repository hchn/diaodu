package com.jiaxun.setting.wifi.util;

import java.util.List;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.text.TextUtils;

import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-7-13
 */
public class WifiUtils
{
    private static final String TAG = WifiUtils.class.getName();

    public static final int SECURITY_NONE = 0;
    public static final int SECURITY_WEP = 1;
    public static final int SECURITY_WPA = 2;

    public static final int SIGNAL_LEVEL_BEST = 0;
    public static final int SIGNAL_LEVEL_BETTER = 1;
    public static final int SIGNAL_LEVEL_AVERAGE = 2;
    public static final int SIGNAL_LEVEL_WEAK = 3;
    public static final int SIGNAL_LEVEL_TERRIBLE = 4;

    private static WifiUtils mInstance;
    // 定义一个WifiManager对象
    private WifiManager mWifiManager;
    // 定义一个WifiInfo对象
    private WifiInfo mWifiInfo;
    // 定义一个DhcpInfo对象
    private DhcpInfo mDhcpInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 网络连接列表
    private List<WifiConfiguration> mWifiConfigurations;
    WifiLock mWifiLock;

    public WifiUtils(Context context)
    {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
        // 取得DhcpInfo对象
        mDhcpInfo = mWifiManager.getDhcpInfo();
    }

//    public static synchronized WifiUtils getInstance(Context context)
//    {
//        if (mInstance == null)
//        {
//            mInstance = new WifiUtils(context);
//        }
//        
//        return mInstance;
//    }

    /**
     * 打开wifi
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void openWifi()
    {
        if (!mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭wifi
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void closeWifi()
    {
        if (mWifiManager.isWifiEnabled())
        {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     *  
     * 方法说明 :返回当前wifi状态
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public boolean isWifiEnable()
    {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 检查当前wifi状态
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public int checkState()
    {
        return mWifiManager.getWifiState();
    }

    /**
     * 锁定wifiLock
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void acquireWifiLock()
    {
        mWifiLock.acquire();
    }

    /**
     * 解锁wifiLock
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void releaseWifiLock()
    {
        // 判断是否锁定
        if (mWifiLock.isHeld())
        {
            mWifiLock.acquire();
        }
    }

    /**
     * 创建一个wifiLock
     * 方法说明 :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void createWifiLock()
    {
        mWifiLock = mWifiManager.createWifiLock("test");
    }

    /**
     *  得到配置好的网络
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public List<WifiConfiguration> getConfiguration()
    {
        return mWifiConfigurations;
    }

    /**
     * 指定配置好的网络进行连接
     * 方法说明 :
     * @param index
     * @author chaimb
     * @Date 2015-7-13
     */
    public void connetionConfiguration(int index)
    {
        if (index > mWifiConfigurations.size())
        {
            return;
        }
        // 连接配置好指定ID的网络
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    public void startScan()
    {
        mWifiManager.startScan();
        // 得到扫描结果
        mWifiList = mWifiManager.getScanResults();
        // 得到配置好的网络连接
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    /**
     * 得到网络列表
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public List<ScanResult> getWifiList()
    {
//        mWifiList = sortByLevel(mWifiList);
        return mWifiList;
    }

    /**
     * 
     * 方法说明 :根据扫描结果将wifi按照level的轻度排序（由高到低）
     * @param list
     * @return
     * @author chaimb
     * @Date 2015-7-16
     */
    public List<ScanResult> sortByLevel(List<ScanResult> list)
    {
        if (list != null && list.size() > 0)
        {// 通过冒泡排序list

            for (int i = 0; i < list.size(); i++)
                for (int j = 1; j < list.size(); j++)
                {
                    if (list.get(i).level < list.get(j).level) // level属性即为强度
                    {
                        ScanResult temp = null;
                        temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            return list;
        }
        else
        {
            Log.info(TAG, "wifilist is null");
            return null;
        }
    }

    /**
     * 查看扫描结果
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public StringBuffer lookUpScan()
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mWifiList.size(); i++)
        {
            sb.append("Index_" + new Integer(i + 1).toString() + ":");
            // 将ScanResult信息转换成一个字符串包
            // 其中把包括：BSSID、SSID、capabilities、frequency、level
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }

    /**
     * 
     * 方法说明 :获取网关
     * @return
     * @author chaimb
     * @Date 2015-7-23
     */
    public String getWay()
    {
        return (mDhcpInfo == null) ? "NULL" : intToString(mDhcpInfo.gateway);
    }

    /**
     * 
     * 方法说明 :获取子网掩码
     * @return
     * @author chaimb
     * @Date 2015-7-23
     */
    public String getNetMaskIpS()
    {
        return (mDhcpInfo == null) ? "NULL" : intToString(mDhcpInfo.netmask);
    }

    public String getMacAddress()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    public String getSSID()
    {

        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    public String getBSSID()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public int getIpAddress()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    public String getIpv4Address()
    {
        return (mWifiInfo == null) ? "0" : intToString(mWifiInfo.getIpAddress());
    }

    /**
     * 得到连接的ID
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public int getNetWordId()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * 得到wifiInfo的所有信息
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public String getWifiInfo()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * 添加一个网络并连接
     * 方法说明 :
     * @param configuration
     * @author chaimb
     * @Date 2015-7-13
     */
    public void addNetWork(int wifiId)
    {
        mWifiManager.enableNetwork(wifiId, true);
    }

    /**
     * 断开指定ID的网络
     * 方法说明 :
     * @param netId
     * @author chaimb
     * @Date 2015-7-13
     */
    public void disConnectionWifi(int netId)
    {

        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 不保存指定ID的网络
     * 方法说明 :
     * @param netId
     * @author chaimb
     * @Date 2015-7-13
     */
    public void removeConnectionWifi(int netId)
    {

        mWifiManager.removeNetwork(netId);
        mWifiManager.disconnect();
    }

    /**
     * 根据SSID获取安全性
     * 方法说明 :
     * @param ssid
     * @return
     * @author chaimb
     * @Date 2015-7-16
     */
    public int getCipherType(String ssid)
    {

        for (ScanResult scResult : mWifiList)
        {

            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid))
            {
                String capabilities = scResult.capabilities;

                if (!TextUtils.isEmpty(capabilities))
                {

                    if (capabilities.contains("WPA") || capabilities.contains("wpa"))
                    {
                        return SECURITY_WPA;
                    }
                    else if (capabilities.contains("WEP") || capabilities.contains("wep"))
                    {
                        return SECURITY_WEP;
                    }
                    else
                    {
                        return SECURITY_NONE;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 方法说明 :判断wifi是否连接
     * @param context
     * @return
     * @author chaimb
     * @Date 2015-7-20
     */
    public boolean isConnected(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        Log.info(TAG, "ni.getState()::" + ni.getState());
        Log.info(TAG, "ni.getDetailedState()::" + ni.getDetailedState());
        if (ni != null && ni.getState() == State.CONNECTED)
            return true;
        return false;
    }

    /**
     * 方法说明 :创建一个WifiConfiguration
     * @param SSID
     * @param passWord
     * @param type
     * @return
     * @author chaimb
     * @Date 2015-7-23
     */
    public WifiConfiguration createWifiInfo(String SSID, String passWord, int type)
    {
        Log.info(TAG, "SSID:" + SSID + ",password:" + passWord);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";

        WifiConfiguration tempConfig = this.isExsits(SSID);

        if (tempConfig != null)
        {
            mWifiManager.removeNetwork(tempConfig.networkId);
        }
        else
        {
            Log.info(TAG, "IsExsits is null.");
        }

        if (type == SECURITY_NONE) // WIFICIPHER_NOPASS
        {
//            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//            config.wepTxKeyIndex = 0;
        }
        if (type == SECURITY_WEP) // WIFICIPHER_WEP
        {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + passWord + "\"";
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        if (type == SECURITY_WPA) // WIFICIPHER_WPA
        {

            config.preSharedKey = "\"" + passWord + "\"";

            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    /**
     * 方法说明 :根据level 获取wifi信号级别
     * @param level
     * @return
     * @author chaimb
     * @Date 2015-8-24
     */
    public int getWifiLevel(int level)
    {
        int result = -1;
        if (level <= 0 && level >= -50)
        {
            result = SIGNAL_LEVEL_BEST;
        }
        else if (level < -50 && level >= -70)
        {
            result = SIGNAL_LEVEL_BETTER;
        }
        else if (level < -70 && level >= -80)
        {
            result = SIGNAL_LEVEL_AVERAGE;
        }
        else if (level < -80 && level >= -100)
        {
            result = SIGNAL_LEVEL_WEAK;
        }
        else
        {
            result = SIGNAL_LEVEL_TERRIBLE;
        }

        return result;

    }

    /**
     * 返回创建好的wifi ID
     * 方法说明 :
     * @param configuration
     * @return
     * @author chaimb
     * @Date 2015-7-23
     */
    public int getWifiId(WifiConfiguration configuration)
    {
        int wifiId = -1;
        wifiId = mWifiManager.addNetwork(configuration);

        if (wifiId != -1)
        {
            return wifiId;
        }
        return wifiId;
    }

    /**
     * 
     * 方法说明 : 查看以前是否已经配置过该SSID
     * @param SSID
     * @return
     * @author chaimb
     * @Date 2015-7-16
     */
    public WifiConfiguration isExsits(String SSID)
    {
        for (WifiConfiguration existingConfig : mWifiConfigurations)
        {
            if (existingConfig.SSID.equals("\"" + SSID + "\""))
            {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 方法说明 :int类型的ip地址转换为String类型的
     * @param ipADD int类型的IP地址
     * @return
     * @author chaimb
     * @Date 2015-7-23
     */
    private String intToString(int ipADD)
    {
        StringBuffer sb = new StringBuffer();
        int tempIp = (ipADD >> 0) & 0xff;
        sb.append(tempIp + ".");
        tempIp = (ipADD >> 8) & 0xff;
        sb.append(tempIp + ".");
        tempIp = (ipADD >> 16) & 0xff;
        sb.append(tempIp + ".");
        tempIp = (ipADD >> 24) & 0xff;
        sb.append(tempIp);
        return sb.toString();
    }
}
