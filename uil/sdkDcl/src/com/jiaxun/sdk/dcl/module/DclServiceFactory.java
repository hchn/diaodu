package com.jiaxun.sdk.dcl.module;

import com.jiaxun.sdk.dcl.module.attendant.impl.DclAtdServiceImpl;
import com.jiaxun.sdk.dcl.module.attendant.itf.DclAtdService;
import com.jiaxun.sdk.dcl.module.blackWhite.impl.DclBlackListServiceImpl;
import com.jiaxun.sdk.dcl.module.blackWhite.itf.DclBlackWhiteListService;
import com.jiaxun.sdk.dcl.module.callRecord.impl.DclCallRecordServiceImpl;
import com.jiaxun.sdk.dcl.module.callRecord.itf.DclCallRecordService;
import com.jiaxun.sdk.dcl.module.contact.impl.DclContactServiceImpl;
import com.jiaxun.sdk.dcl.module.contact.itf.DclContactService;

/**
 * 说明：业务控制层向上层提供的业务功能服务接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class DclServiceFactory
{
    public static DclCallRecordService getDclCallRecordService()
    {
        return DclCallRecordServiceImpl.getInstance();
    }

    public static DclContactService getDclContactService()
    {
        return DclContactServiceImpl.getInstance();
    }

    public static DclAtdService getDclAtdService()
    {
        return DclAtdServiceImpl.getInstance();
    }

    public static DclBlackWhiteListService getDclBlackListService()
    {
        return DclBlackListServiceImpl.getInstance();
    }
}
