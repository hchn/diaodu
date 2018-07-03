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
 * ˵����
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
    // ����һ��WifiManager����
    private WifiManager mWifiManager;
    // ����һ��WifiInfo����
    private WifiInfo mWifiInfo;
    // ����һ��DhcpInfo����
    private DhcpInfo mDhcpInfo;
    // ɨ��������������б�
    private List<ScanResult> mWifiList;
    // ���������б�
    private List<WifiConfiguration> mWifiConfigurations;
    WifiLock mWifiLock;

    public WifiUtils(Context context)
    {
        // ȡ��WifiManager����
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // ȡ��WifiInfo����
        mWifiInfo = mWifiManager.getConnectionInfo();
        // ȡ��DhcpInfo����
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
     * ��wifi
     * ����˵�� :
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
     * �ر�wifi
     * ����˵�� :
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
     * ����˵�� :���ص�ǰwifi״̬
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public boolean isWifiEnable()
    {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * ��鵱ǰwifi״̬
     * ����˵�� :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public int checkState()
    {
        return mWifiManager.getWifiState();
    }

    /**
     * ����wifiLock
     * ����˵�� :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void acquireWifiLock()
    {
        mWifiLock.acquire();
    }

    /**
     * ����wifiLock
     * ����˵�� :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void releaseWifiLock()
    {
        // �ж��Ƿ�����
        if (mWifiLock.isHeld())
        {
            mWifiLock.acquire();
        }
    }

    /**
     * ����һ��wifiLock
     * ����˵�� :
     * @author chaimb
     * @Date 2015-7-13
     */
    public void createWifiLock()
    {
        mWifiLock = mWifiManager.createWifiLock("test");
    }

    /**
     *  �õ����úõ�����
     * ����˵�� :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public List<WifiConfiguration> getConfiguration()
    {
        return mWifiConfigurations;
    }

    /**
     * ָ�����úõ������������
     * ����˵�� :
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
        // �������ú�ָ��ID������
        mWifiManager.enableNetwork(mWifiConfigurations.get(index).networkId, true);
    }

    public void startScan()
    {
        mWifiManager.startScan();
        // �õ�ɨ����
        mWifiList = mWifiManager.getScanResults();
        // �õ����úõ���������
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
    }

    /**
     * �õ������б�
     * ����˵�� :
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
     * ����˵�� :����ɨ������wifi����level����������ɸߵ��ͣ�
     * @param list
     * @return
     * @author chaimb
     * @Date 2015-7-16
     */
    public List<ScanResult> sortByLevel(List<ScanResult> list)
    {
        if (list != null && list.size() > 0)
        {// ͨ��ð������list

            for (int i = 0; i < list.size(); i++)
                for (int j = 1; j < list.size(); j++)
                {
                    if (list.get(i).level < list.get(j).level) // level���Լ�Ϊǿ��
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
     * �鿴ɨ����
     * ����˵�� :
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
            // ��ScanResult��Ϣת����һ���ַ�����
            // ���аѰ�����BSSID��SSID��capabilities��frequency��level
            sb.append((mWifiList.get(i)).toString()).append("\n");
        }
        return sb;
    }

    /**
     * 
     * ����˵�� :��ȡ����
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
     * ����˵�� :��ȡ��������
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
     * �õ����ӵ�ID
     * ����˵�� :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public int getNetWordId()
    {
        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    /**
     * �õ�wifiInfo��������Ϣ
     * ����˵�� :
     * @return
     * @author chaimb
     * @Date 2015-7-13
     */
    public String getWifiInfo()
    {
        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    /**
     * ���һ�����粢����
     * ����˵�� :
     * @param configuration
     * @author chaimb
     * @Date 2015-7-13
     */
    public void addNetWork(int wifiId)
    {
        mWifiManager.enableNetwork(wifiId, true);
    }

    /**
     * �Ͽ�ָ��ID������
     * ����˵�� :
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
     * ������ָ��ID������
     * ����˵�� :
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
     * ����SSID��ȡ��ȫ��
     * ����˵�� :
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
     * ����˵�� :�ж�wifi�Ƿ�����
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
     * ����˵�� :����һ��WifiConfiguration
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
     * ����˵�� :����level ��ȡwifi�źż���
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
     * ���ش����õ�wifi ID
     * ����˵�� :
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
     * ����˵�� : �鿴��ǰ�Ƿ��Ѿ����ù���SSID
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
     * ����˵�� :int���͵�ip��ַת��ΪString���͵�
     * @param ipADD int���͵�IP��ַ
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
