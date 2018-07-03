package com.jiaxun.sdk.acl.module.conf.impl;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;
import com.jiaxun.sdk.acl.module.conf.itf.AclConfService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 说明：ACL层会议业务功能接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-29
 */
public class AclConfServiceImpl implements AclConfService
{
    private static AclConfServiceImpl instance;

    private LineManager lineManager;

    private AclConfServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclConfServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclConfServiceImpl();
        }
        return instance;
    }

    @Override
    public int confRegEventListener(AclConfEventListener callback)
    {
        lineManager.regConfEventListener(callback);
        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public int confCreate(String sessionId, int callPriority, int confType, boolean video) throws Exception
    {
        return lineManager.getActiveEngine().confCreate(sessionId, callPriority, confType, video);
    }

    @Override
    public int confClose(String sessionId) throws Exception
    {
        return lineManager.getActiveEngine().confClose(sessionId);
    }

    @Override
    public int confEnter(String sessionId) throws Exception
    {
        return lineManager.getActiveEngine().confEnter(sessionId);
    }

    @Override
    public int confLeave(String sessionId) throws Exception
    {
        return lineManager.getActiveEngine().confLeave(sessionId);
    }

    @Override
    public int confUserAdd(String sessionId, String userNum) throws Exception
    {
        return lineManager.getActiveEngine().confUserAdd(sessionId, userNum);
    }

    @Override
    public int confUserDelete(String sessionId, String userNum) throws Exception
    {
        return lineManager.getActiveEngine().confUserDelete(sessionId, userNum);
    }

    @Override
    public int confUserAudioEnable(String sessionId, String userNum, boolean enable) throws Exception
    {
        return lineManager.getActiveEngine().confUserAudioEnable(sessionId, userNum, enable);
    }

    @Override
    public int confUserVideoEnable(String sessionId, String userNum, boolean enable) throws Exception
    {
        return lineManager.getActiveEngine().confUserVideoEnable(sessionId, userNum, enable);
    }

    @Override
    public int confUserVideoShare(String sessionId, String userNum, String tag, boolean enable) throws Exception
    {
        return lineManager.getActiveEngine().confUserVideoShare(sessionId, userNum, tag, enable);
    }

    @Override
    public int confBgmEnable(String sessionId, boolean enable) throws Exception
    {
        return lineManager.getActiveEngine().confBgmEnable(sessionId, enable);
    }

    @Override
    public void confSipInfoDTMFSend(String sessionId, char c) throws Exception
    {
        lineManager.getActiveEngine().sCallSipInfoDTMFSend(sessionId, c);
    }

    @Override
    public void confInbandDTMFSend(String sessionId, char c) throws Exception
    {
        lineManager.getActiveEngine().sCallInbandDTMFSend(sessionId, c);
    }
}
