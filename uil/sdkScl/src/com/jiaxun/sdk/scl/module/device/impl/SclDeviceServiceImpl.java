package com.jiaxun.sdk.scl.module.device.impl;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.scl.module.device.callback.SclDeviceEventListener;
import com.jiaxun.sdk.scl.module.device.handler.SclDeviceEventHandler;
import com.jiaxun.sdk.scl.module.device.itf.SclDeviceService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * ËµÃ÷£º
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class SclDeviceServiceImpl implements SclDeviceService
{
    private static String TAG = SclDeviceServiceImpl.class.getName();
    private SessionLooperHandler sclHandler;
    
    private static SclDeviceServiceImpl instance;
    
    private SclDeviceEventListener deviceEventListener;
    
    private SclDeviceServiceImpl()
    {
        sclHandler = SessionLooperHandler.getInstance();
        AclServiceFactory.getAclDeviceService().deviceRegEventListener(new SclDeviceEventHandler());
    }

    public static SclDeviceServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclDeviceServiceImpl();
        }
        return instance;
    }
    
    public SclDeviceEventListener getSclDeviceEventListener()
    {
        return deviceEventListener;
    }

    @Override
    public int deviceRegEventListener(SclDeviceEventListener callback)
    {
        Log.info(TAG, "deviceRegEventListener::");
        if (callback == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        deviceEventListener = callback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int remoteCameraControl(String sessionId, String deviceNum, int command, int commandPara1, int commandPara2, int commandPara3)
    {
        Log.info(TAG, "cameraControl::sessionId:" + sessionId + " deviceNum:" + deviceNum + " command:" + command + " commandPara1:" + commandPara1 + " commandPara2:" + commandPara2 + " commandPara3:" + commandPara3);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(deviceNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_DEVICE_CAMERA_CONTROL;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_DEVICE;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, deviceNum);
        data.putInt(CommonConstantEntry.DATA_COMMAND, command);
        data.putInt(CommonConstantEntry.DATA_COMMAND_PARA1, commandPara1);
        data.putInt(CommonConstantEntry.DATA_COMMAND_PARA2, commandPara2);
        data.putInt(CommonConstantEntry.DATA_COMMAND_PARA3, commandPara3);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int initLocalCamera()
    {
        Log.info(TAG, "initLocalCamera::");
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_DEVICE_CAMERA_INIT;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_DEVICE;
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

}
