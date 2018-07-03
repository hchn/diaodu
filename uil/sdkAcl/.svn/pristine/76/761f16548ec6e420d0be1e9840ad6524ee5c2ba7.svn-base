package com.jiaxun.sdk.acl.module.device.impl;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.module.device.callback.AclDeviceEventListener;
import com.jiaxun.sdk.acl.module.device.itf.AclDeviceService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 说明：设备控制接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class AclDeviceServiceImpl implements AclDeviceService
{
    
    private static AclDeviceServiceImpl instance;
    
    private LineManager lineManager;
    
    private AclDeviceServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclDeviceServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclDeviceServiceImpl();
        }
        return instance;
    }
    
    @Override
    public int deviceRegEventListener(AclDeviceEventListener callback)
    {
        lineManager.regDeviceEventListener(callback);
        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public int remoteCameraControl(String sessionId, String deviceNum, int command, int commandPara1, int commandPara2, int commandPara3)
    {
        return lineManager.getActiveEngine().remoteCameraControl(sessionId, deviceNum, command, commandPara1, commandPara2, commandPara3);
    }

}
