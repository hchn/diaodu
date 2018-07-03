package com.jiaxun.sdk.acl.module.vs.impl;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.module.vs.callback.AclVsEventListener;
import com.jiaxun.sdk.acl.module.vs.itf.AclVsService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 说明：视频监控业务功能接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-29
 */
public class AclVsServiceImpl implements AclVsService
{
    private static AclVsServiceImpl instance;

    private LineManager lineManager;

    private AclVsServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclVsServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclVsServiceImpl();
        }
        return instance;
    }

    @Override
    public int vsRegEventListener(AclVsEventListener callback)
    {
        lineManager.regVsEventListener(callback);
        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public int vsOpen(String sessionId, int priority, String videoNum) throws Exception
    {
        return lineManager.getActiveEngine().vsOpen(sessionId, priority, videoNum);
    }

    @Override
    public int vsClose(String sessionId) throws Exception
    {
        return lineManager.getActiveEngine().vsClose(sessionId);
    }

}
