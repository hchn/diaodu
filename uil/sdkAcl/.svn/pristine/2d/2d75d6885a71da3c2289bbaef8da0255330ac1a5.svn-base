package com.jiaxun.sdk.acl.line;

import org.zoolu.net.IpAddress;

import android.os.SystemClock;

import com.jiaxun.sdk.acl.line.dpcp.DpcpAdapter;
import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.acl.module.common.callback.AclCommonEventListener;
import com.jiaxun.sdk.acl.module.common.itf.AclCommonService;
import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;
import com.jiaxun.sdk.acl.module.device.callback.AclDeviceEventListener;
import com.jiaxun.sdk.acl.module.im.callback.AclImEventListener;
import com.jiaxun.sdk.acl.module.presence.callback.AclPresenceEventListener;
import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;
import com.jiaxun.sdk.acl.module.vs.callback.AclVsEventListener;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：双中心（主备模式，主中心做业务，备中心只续约）
 * 
 * @author hubin
 * 
 * @Date 2014-3-27
 */
public class LineManager implements AclCommonService
{
    private final static String TAG = LineManager.class.getName();
    /** Sip中心1 */
    private static LineAdapter line1 = null;
    /** Sip中心2 */
    private static LineAdapter line2 = null;

    private static LineManager instance;

    private AccountConfig serviceConfig;

    private AclCommonEventListener commonEventListener;

    public AclSCallEventListener sCallEventListener;

    public AclConfEventListener confEventListener;

    public AclImEventListener imEventListener;

    public AclVsEventListener vsEventListener;

    public AclPresenceEventListener presenceEventListener;

    public AclDeviceEventListener deviceEventListener;

    private LineManager()
    {
    }

    public static LineManager getInstance()
    {
        if (instance == null)
        {
            instance = new LineManager();
        }
        return instance;
    }

//    private void initListener()
//    {
//        this.commonEventListener = AclCommonServiceImpl.getInstance().getAclCommonEventListener();
//    }

    public void regScallEventListener(AclSCallEventListener callback)
    {
        sCallEventListener = callback;
    }

    public void regConfEventListener(AclConfEventListener callback)
    {
        confEventListener = callback;
    }

    public void regVsEventListener(AclVsEventListener callback)
    {
        vsEventListener = callback;
    }

    public void regDeviceEventListener(AclDeviceEventListener callback)
    {
        deviceEventListener = callback;
    }

    public void regPresenceEventListener(AclPresenceEventListener callback)
    {
        presenceEventListener = callback;
    }

    public void regImEventListener(AclImEventListener callback)
    {
        imEventListener = callback;
    }

    private LineAdapter initLineAdapter(int lineType, String localIp, int port)
    {
        LineAdapter accessAdapter;
        switch (lineType)
        {
            case CommonConstantEntry.LINE_TYPE_SIP:
                accessAdapter = new SipAdapter(localIp, port, this);
                break;
            case CommonConstantEntry.LINE_TYPE_SLOTA:
                accessAdapter = new DpcpAdapter();
                break;
            case CommonConstantEntry.LINE_TYPE_SLOTB:
                accessAdapter = new DpcpAdapter();
                break;
            default:
                accessAdapter = new SipAdapter(localIp, port, this);
                break;
        }
        return accessAdapter;
    }

    /**
     * 方法说明 : 线路状态变化时上报变化信息
     * @return void
     * @author hubin
     * @throws Exception 
     * @Date 2015-2-5
     */
    public void onRegisterStateChanged()
    {
        try
        {
            Log.info(TAG, "onRegisterStateChanged::");
            if (commonEventListener != null)
            {
                if (line1 != null && line2 != null)
                {
                    commonEventListener.onLineStatusChange(new int[] { line1.getLinkStatus(), line2.getLinkStatus() }, new int[] { line1.getServiceStatus(),
                            line2.getServiceStatus() });
                }
                else if (line1 != null)
                {
                    commonEventListener.onLineStatusChange(new int[] { line1.getLinkStatus() }, new int[] { line1.getServiceStatus() });
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * 方法说明 : 获取当前正在使用的中心
     * @throws Exception
     * @return LineAdapter
     * @author hubin
     * @Date 2015-2-5
     */
    public LineAdapter getActiveEngine()
    {
        if (line1 != null
                && (line1.getServiceStatus() == CommonConstantEntry.SERVICE_STATUS_ACTIVE || line1.getServiceStatus() == CommonConstantEntry.SERVICE_STATUS_STANDBY))
        {
            return line1;
        }
        else if (line2 != null
                && (line2.getServiceStatus() == CommonConstantEntry.SERVICE_STATUS_ACTIVE || line2.getServiceStatus() == CommonConstantEntry.SERVICE_STATUS_STANDBY))
        {
            return line2;
        }
        return line1;
    }

    public LineAdapter getMasterEngine()
    {
        return line1;
    }

    public LineAdapter getSlaveEngine()
    {
        return line2;
    }

    public AclCommonEventListener getCommonEventListener()
    {
        return commonEventListener;
    }

    @Override
    public int regCommonEventListener(AclCommonEventListener callback)
    {
        Log.info(TAG, "aclRegCommonEventListener::");
        commonEventListener = callback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int setNightService(boolean nightService) throws Exception
    {
        getActiveEngine().setNightService(nightService);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int updateAccountConfig(final AccountConfig config) throws Exception
    {
        Log.info(TAG, "updateAccountConfig::");
        if (serviceConfig == null)
        {// 配置为空
            startAclService(config);// 走启动服务器流程
        }
//        else if (serviceConfig.localIp.equals(config.localIp))
//        {// 本地IP地址没有发送改变
//            serviceConfig = config;
//        }
        else
        {// 本地IP地址改变,切换本地IP
            if (config.serverType.length == 1 && line1 != null)
            {
                Log.info(TAG, "line1:: account:" + config.account[0] + " password:" + config.password[0] + " localIp:" + config.localIp + " serverIp:"
                        + config.serverIp[0] + " serverDomain:" + config.serverDomain[0] + " serverPort:" + config.serverPort[0]);
                if (line1.serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || line1.serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
                {
                    // 注销当前服务
                    line1.stopService();
                    // FIXME 等待一定时长，待注销完成，重新发起新注册
                    int times = 0;
                    while (((SipAdapter) line1).isRegistered())
                    {// 监测是否已经下线
                        times++;
                        SystemClock.sleep(100);
                        if (times == 10)
                        {// 超出最多次数
                            break;
                        }
                    }
                    Log.info(TAG, "offline:: try times:" + times);
                }
                line1.setServiceProfile(config.account[0], config.password[0], config.localIp, config.serverIp[0], config.serverDomain[0], config.serverPort[0]);
                line1.startService();
            }
            else if (config.serverType.length == 2)
            {
                if (line1 != null)
                {
                    Log.info(TAG, "line1:: account:" + config.account[0] + " password:" + config.password[0] + " localIp:" + config.localIp + " serverIp:"
                            + config.serverIp[0] + " serverDomain:" + config.serverDomain[0] + " serverPort:" + config.serverPort[0]);
                    if (line1.serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || line1.serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
                    {
                        // 注销当前服务
                        line1.stopService();
                        // FIXME 等待一定时长，待注销完成，重新发起新注册
                        int times = 0;
                        while (((SipAdapter) line1).isRegistered())
                        {// 监测是否已经下线
                            times++;
                            SystemClock.sleep(100);
                            if (times == 10)
                            {// 超出最多次数
                                break;
                            }
                        }
                        Log.info(TAG, "offline:: try times:" + times);
                    }
                    line1.setServiceProfile(config.account[0], config.password[0], config.localIp, config.serverIp[0], config.serverDomain[0],
                            config.serverPort[0]);
                    line1.startService();
                }
                if (line2 != null)
                {
                    Log.info(TAG, "line2:: account:" + config.account[1] + " password:" + config.password[1] + " localIp:" + config.localIp + " serverIp:"
                            + config.serverIp[1] + " serverDomain:" + config.serverDomain[1] + " serverPort:" + config.serverPort[1]);
                    if (line2.serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || line2.serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
                    {
                        // 注销当前服务
                        line2.stopService();
                        // FIXME 等待一定时长，待注销完成，重新发起新注册
                        int times = 0;
                        while (((SipAdapter) line2).isRegistered())
                        {// 监测是否已经下线
                            times++;
                            SystemClock.sleep(100);
                            if (times == 10)
                            {// 超出最多次数
                                break;
                            }
                        }
                        Log.info(TAG, "offline:: try times:" + times);
                    }
                    line2.setServiceProfile(config.account[1], config.password[1], config.localIp, config.serverIp[1], config.serverDomain[1],
                            config.serverPort[1]);
                    line2.startService();
                }
            }
            serviceConfig = config;
            // 设置本地IP地址
            IpAddress.setLocalIpAddress(config.localIp);
        }

        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int startAclService(AccountConfig config) throws Exception
    {
        Log.info(TAG, "startAclService::");
        serviceConfig = config;
        // 设置本地IP地址
        IpAddress.setLocalIpAddress(config.localIp);
        if (config.serverType.length == 1)
        {
            if (line1 == null)
                line1 = initLineAdapter(config.serverType[0], config.localIp, config.localPort[0]);
            line1.setServiceProfile(config.account[0], config.password[0], config.localIp, config.serverIp[0], "", config.serverPort[0]);
            line1.startService();
        }
        else if (config.serverType.length == 2)
        {
            if (line1 == null)
                line1 = initLineAdapter(config.serverType[0], config.localIp, config.localPort[0]);
            line1.setServiceProfile(config.account[0], config.password[0], config.localIp, config.serverIp[0], config.serverDomain[0], config.serverPort[0]);
            line1.startService();
            if (line2 == null)
                line2 = initLineAdapter(config.serverType[1], config.localIp, config.localPort[1]);
            line2.setServiceProfile(config.account[1], config.password[1], config.localIp, config.serverIp[1], config.serverDomain[1], config.serverPort[1]);
            line2.startService();
        }
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int stopAclService() throws Exception
    {
        if (line1 != null)
        {
            line1.stopService();// 停止主中心引擎
        }
        if (line2 != null)
        {
            line2.stopService();// 停止备中心引擎
        }
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    public AccountConfig getServiceConfig()
    {
        return serviceConfig;
    }
}
