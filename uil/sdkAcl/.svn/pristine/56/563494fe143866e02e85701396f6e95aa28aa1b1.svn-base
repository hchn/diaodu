package com.jiaxun.sdk.acl.module.common.impl;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.acl.module.common.callback.AclCommonEventListener;
import com.jiaxun.sdk.acl.module.common.itf.AclCommonService;

/**
 * 说明：公共业务功能接口实现
 *
 * @author  hubin
 *
 * @Date 2015-1-29
 */
public class AclCommonServiceImpl implements AclCommonService
{
    private static AclCommonServiceImpl instance;

    private LineManager lineManager;

    private AclCommonServiceImpl()
    {
        lineManager = LineManager.getInstance();
    }

    public static AclCommonServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new AclCommonServiceImpl();
        }
        return instance;
    }

    @Override
    public int regCommonEventListener(AclCommonEventListener callback)
    {
        return lineManager.regCommonEventListener(callback);
    }

    @Override
    public int setNightService(boolean nightService) throws Exception
    {
        return lineManager.setNightService(nightService);
    }

    @Override
    public int updateAccountConfig(AccountConfig config) throws Exception
    {
        return lineManager.updateAccountConfig(config);
    }

    @Override
    public int startAclService(AccountConfig config) throws Exception
    {
        return lineManager.startAclService(config);
    }

    @Override
    public int stopAclService() throws Exception
    {
        return lineManager.stopAclService();
    }
}
