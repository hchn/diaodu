package com.jiaxun.sdk.scl.module.im.impl;

import java.net.URI;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.scl.module.im.callback.SclImEventListener;
import com.jiaxun.sdk.scl.module.im.handler.SclImEventHandler;
import com.jiaxun.sdk.scl.module.im.itf.SclImService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * ËµÃ÷£º
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class SclImServiceImpl implements SclImService
{
    private static SclImServiceImpl instance;

    private SclImServiceImpl()
    {
        // ¶ÔACL²ã×¢²á¼àÌý
        AclServiceFactory.getAclImService().imRegEventListener(new SclImEventHandler());
    }

    public static SclImServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclImServiceImpl();
        }
        return instance;
    }

    @Override
    public int imRegEventListener(SclImEventListener callback)
    {
        // TODO Auto-generated method stub
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int imSendMsg(String sessionId, int msgPriority, String callerNum, String callerName, String calleeNum, int msgType, String text, URI uri)
    {
        // TODO Auto-generated method stub
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }
}
