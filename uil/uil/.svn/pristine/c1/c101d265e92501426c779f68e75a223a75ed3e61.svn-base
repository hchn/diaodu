package com.jiaxun.uil;

import android.content.Context;

import com.jiaxun.uil.module.blacklist.impl.UilBlackListServiceImpl;
import com.jiaxun.uil.module.blacklist.itf.UilBlackListService;
import com.jiaxun.uil.module.config.impl.ConfigServiceImpl;
import com.jiaxun.uil.module.config.itf.ConfigService;
import com.jiaxun.uil.module.contact.impl.UilContactServiceImpl;
import com.jiaxun.uil.module.contact.itf.UilContactService;
import com.jiaxun.uil.module.surveillance.impl.UilVsServiceImpl;

/**
 * 说明：UI层服务接口工厂
 *
 * @author  hubin
 *
 * @Date 2015-1-20
 */
public class UilServiceFactory
{
    public static UilVsServiceImpl getUilSurveillanceService()
    {
        return UilVsServiceImpl.getInstance();
    }

    public static UilBlackListService getUilBlackListService()
    {
        return UilBlackListServiceImpl.getInstance();
    }
    
    public static ConfigService getConfigService(Context context)
    {
        return ConfigServiceImpl.getInstance(context);
    }
    public static UilContactService getContactService(Context context)
    {
        return UilContactServiceImpl.getInstance(context);
    }
}
