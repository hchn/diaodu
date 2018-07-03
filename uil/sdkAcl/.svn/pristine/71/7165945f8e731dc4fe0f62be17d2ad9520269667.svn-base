package com.jiaxun.sdk.acl.line;

import com.jiaxun.sdk.acl.module.conf.itf.AclConfService;
import com.jiaxun.sdk.acl.module.device.itf.AclDeviceService;
import com.jiaxun.sdk.acl.module.im.itf.AclImService;
import com.jiaxun.sdk.acl.module.presence.itf.AclPresenceService;
import com.jiaxun.sdk.acl.module.scall.itf.AclSCallService;
import com.jiaxun.sdk.acl.module.vs.itf.AclVsService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 说明：线路适配器接口
 *
 * @author  hubin
 *
 * @Date 2015-1-11
 */
public abstract class LineAdapter implements AclSCallService, AclConfService, AclImService, AclPresenceService, AclVsService, AclDeviceService
{
    public int serviceStatus = CommonConstantEntry.SERVICE_STATUS_OFFLINE;

    public int linkStatus = CommonConstantEntry.LINK_STATUS_OK;
    
    /**
     * 方法说明 : 获取服务状态信息
     * @return int
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract int getServiceStatus();

    /**
     * 方法说明 : 获取链路状态信息
     * @return int
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract int getLinkStatus();

    /**
     * 方法说明 : 启动协议引擎：业务和注册
     * @return boolean
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract boolean startService() throws Exception;

    /**
     * 方法说明 : 停止协议引擎：业务和注册
     * @return boolean
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract boolean stopService() throws Exception;

    /**
     * 方法说明 : 服务参数配置
     * @param sipAccount
     * @param sipPassword
     * @param localIp
     * @param sipServerIp
     * @param sipServerName
     * @param sipServerPort
     * @return boolean
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract boolean setServiceProfile(String sipAccount, String sipPassword, String localIp, String sipServerIp, String sipServerName, int sipServerPort)
            throws Exception;

    /**
     * 方法说明 : 发送sip info类型的DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract void sCallSipInfoDTMFSend(String sessionId, char c) throws Exception;
    /**
     * 方法说明 : 发送inband类型的DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract void sCallInbandDTMFSend(String sessionId, char c) throws Exception;

    /**
     * 方法说明 : 设置夜服业务
     * @param nightService 进入/退出夜服  
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    public abstract void setNightService(boolean nightService) throws Exception;
}
