package com.jiaxun.sdk.acl.module.presence.impl;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.module.presence.callback.AclPresenceEventListener;
import com.jiaxun.sdk.acl.module.presence.itf.AclPresenceService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：用户状态订阅业务功能接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-29
 */
public class AclPresenceServiceImpl implements AclPresenceService
{
    private static final String TAG = AclPresenceServiceImpl.class.getName();
    private static AclPresenceServiceImpl instance;

    private LineManager lineManager;

    private AclPresenceServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclPresenceServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclPresenceServiceImpl();
        }
        return instance;
    }

    @Override
    public int presenceRegEventListener(AclPresenceEventListener callback)
    {
        lineManager.regPresenceEventListener(callback);
        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public int presenceSubscribe(String[] user, boolean on) throws Exception
    {
        Log.info(TAG, "presenceSubscribe:: user: " + user + " on: " + on);
        return lineManager.getActiveEngine().presenceSubscribe(user, on);
    }

    @Override
    public boolean cancelAllSubscribe()
    {
        Log.info(TAG, "cancelAllSubscribe::");
        return lineManager.getActiveEngine().cancelAllSubscribe();
    }
}
