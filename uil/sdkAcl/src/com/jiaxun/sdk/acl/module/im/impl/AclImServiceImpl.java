package com.jiaxun.sdk.acl.module.im.impl;

import java.net.URI;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.module.im.callback.AclImEventListener;
import com.jiaxun.sdk.acl.module.im.itf.AclImService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * 说明：即时消息业务功能接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-29
 */
public class AclImServiceImpl implements AclImService
{
    private static AclImServiceImpl instance;

    private LineManager lineManager;

    private AclImServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclImServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclImServiceImpl();
        }
        return instance;
    }

    @Override
    public int imRegEventListener(AclImEventListener callback)
    {
        lineManager.regImEventListener(callback);
        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public int imSendMsg(String sessionId, int msgPriority, String callerNum, String callerName, String calleeNum, int msgType, String text, URI uri)
            throws Exception
    {
        return lineManager.getActiveEngine().imSendMsg(sessionId, msgPriority, callerNum, callerName, calleeNum, msgType, text, uri);
    }

}
