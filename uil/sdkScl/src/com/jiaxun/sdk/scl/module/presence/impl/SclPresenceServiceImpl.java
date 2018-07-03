package com.jiaxun.sdk.scl.module.presence.impl;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.module.presence.itf.AclPresenceService;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.scl.module.presence.callback.SclPresenceEventListener;
import com.jiaxun.sdk.scl.module.presence.handler.SclPresenceEventHandler;
import com.jiaxun.sdk.scl.module.presence.itf.SclPresenceService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：用户状态订阅业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class SclPresenceServiceImpl implements SclPresenceService
{
    private static String TAG = SclPresenceServiceImpl.class.getName();
    private static SclPresenceServiceImpl instance;
    
    private SessionLooperHandler sclHandler;
    
    private SclPresenceEventListener presenceEventListener;

    private SclPresenceServiceImpl()
    {
        sclHandler = SessionLooperHandler.getInstance();
        AclServiceFactory.getAclPresenceService().presenceRegEventListener(new SclPresenceEventHandler());
    }

    public static SclPresenceServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclPresenceServiceImpl();
        }
        return instance;
    }

    @Override
    public int presenceRegEventListener(SclPresenceEventListener callback)
    {
        this.presenceEventListener = callback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }
    
    public SclPresenceEventListener getPresenceEventListener()
    {
        return presenceEventListener;
    }

    @Override
    public int presenceSubscribe(String[] user, boolean on)
    {
        Log.info(TAG, "presenceSubscribe::user" + user + " on:" + on);
        if (user == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_PRESENCE_SUBSCRIBE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_PRESENCE;
        Bundle data = new Bundle();
        data.putStringArray(CommonConstantEntry.DATA_MEMBER_LIST, user);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, on);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int removeAllSubscribe()
    {
        Log.info(TAG, "cancelAllSubscribe");
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_PRESENCE_CANCEL_ALL;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_PRESENCE;
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }
}
