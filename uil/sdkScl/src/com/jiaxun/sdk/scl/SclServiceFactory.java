package com.jiaxun.sdk.scl;

import com.jiaxun.sdk.scl.module.common.impl.SclCommonServiceImpl;
import com.jiaxun.sdk.scl.module.common.itf.SclCommonService;
import com.jiaxun.sdk.scl.module.conf.impl.SclConfServiceImpl;
import com.jiaxun.sdk.scl.module.conf.itf.SclConfService;
import com.jiaxun.sdk.scl.module.device.impl.SclDeviceServiceImpl;
import com.jiaxun.sdk.scl.module.device.itf.SclDeviceService;
import com.jiaxun.sdk.scl.module.im.impl.SclImServiceImpl;
import com.jiaxun.sdk.scl.module.im.itf.SclImService;
import com.jiaxun.sdk.scl.module.presence.impl.SclPresenceServiceImpl;
import com.jiaxun.sdk.scl.module.presence.itf.SclPresenceService;
import com.jiaxun.sdk.scl.module.scall.impl.SclSCallServiceImpl;
import com.jiaxun.sdk.scl.module.scall.itf.SclSCallService;
import com.jiaxun.sdk.scl.module.vs.impl.SclVsServiceImpl;
import com.jiaxun.sdk.scl.module.vs.itf.SclVsService;

/**
 * 说明：业务控制层向上层提供的业务功能服务接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class SclServiceFactory
{
    public static SclCommonService getSclCommonService()
    {
        return SclCommonServiceImpl.getInstance();
    }

    public static SclConfService getSclConfService()
    {
        return SclConfServiceImpl.getInstance();
    }

    public static SclImService getSclImService()
    {
        return SclImServiceImpl.getInstance();
    }

    public static SclPresenceService getSclPresenceService()
    {
        return SclPresenceServiceImpl.getInstance();
    }

    public static SclSCallService getSclSCallService()
    {
        return SclSCallServiceImpl.getInstance();
    }

    public static SclVsService getSclVsService()
    {
        return SclVsServiceImpl.getInstance();
    }
    
    public static SclDeviceService getSclDeviceService()
    {
        return SclDeviceServiceImpl.getInstance();
    }
    
}
