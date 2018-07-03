package com.jiaxun.sdk.scl.module.vs.impl;

import java.util.UUID;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;
import com.jiaxun.sdk.scl.module.vs.handler.SclVsEventHandler;
import com.jiaxun.sdk.scl.module.vs.itf.SclVsService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：视频监控业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class SclVsServiceImpl implements SclVsService
{
    private static String TAG = SclVsServiceImpl.class.getName();
    private SessionLooperHandler sclHandler;

    private static SclVsServiceImpl instance;

    private SclVsEventListener vsEventListener;

    private SclVsServiceImpl()
    {
        sclHandler = SessionLooperHandler.getInstance();
        AclServiceFactory.getAclVsService().vsRegEventListener(new SclVsEventHandler());
    }

    public static SclVsServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclVsServiceImpl();
        }
        return instance;
    }

    public SclVsEventListener getSclVsEventListener()
    {
        return vsEventListener;
    }

    @Override
    public int vsRegEventListener(SclVsEventListener callback)
    {
        vsEventListener = callback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int vsOpen(int priority, String videoNum)
    {
        Log.info(TAG, "vsOpen::priority:" + priority + " videoNum:" + videoNum);
        if (TextUtils.isEmpty(videoNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_VS_OPEN;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_VS;
        Bundle data = new Bundle();
        String sessionId = UUID.randomUUID().toString();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, priority);
        data.putString(CommonConstantEntry.DATA_NUMBER, videoNum);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int vsClose(String sessionId)
    {
        Log.info(TAG, "vsClose::sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_VS_CLOSE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_VS;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

}
