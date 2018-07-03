package com.jiaxun.sdk.acl.module.scall.impl;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;
import com.jiaxun.sdk.acl.module.scall.itf.AclSCallService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 说明：单呼业务功能接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-29
 */
public class AclSCallServiceImpl implements AclSCallService
{
    private static AclSCallServiceImpl instance;

    private LineManager lineManager;

    private AclSCallServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclSCallServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclSCallServiceImpl();
        }
        return instance;
    }

    @Override
    public int sCallRegEventListener(AclSCallEventListener callback)
    {
        lineManager.regScallEventListener(callback);
        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public int sCallMake(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean audio, boolean video) throws Exception
    {
        return lineManager.getActiveEngine().sCallMake(sessionId, callPriority, callerNum, callerName, funcCode, calleeNum, channel, audio, video);
    }

    @Override
    public int sCallAlerting(String sessionId, String name, int channel, boolean sendRbt) throws Exception
    {
        return lineManager.getActiveEngine().sCallAlerting(sessionId, name, channel, sendRbt);
    }

    @Override
    public int sCallAnswer(String sessionId, String name, int channel, boolean audio, boolean video) throws Exception
    {
        return lineManager.getActiveEngine().sCallAnswer(sessionId, name, channel, audio, video);
    }

    @Override
    public int sCallRelease(String sessionId, int reason) throws Exception
    {
        return lineManager.getActiveEngine().sCallRelease(sessionId, reason);
    }

    @Override
    public int sCallHold(String sessionId) throws Exception
    {
        return lineManager.getActiveEngine().sCallHold(sessionId);
    }

    @Override
    public int sCallRetrieve(String sessionId) throws Exception
    {
        return lineManager.getActiveEngine().sCallRetrieve(sessionId);
    }

    @Override
    public void sCallSipInfoDTMFSend(String sessionId, char c) throws Exception
    {
        lineManager.getActiveEngine().sCallSipInfoDTMFSend(sessionId, c);
    }

    @Override
    public void sCallInbandDTMFSend(String sessionId, char c) throws Exception
    {
        lineManager.getActiveEngine().sCallInbandDTMFSend(sessionId, c);
    }
}
