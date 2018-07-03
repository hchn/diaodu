package com.jiaxun.sdk.scl.module.common.impl;

import android.os.Bundle;
import android.os.Message;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.model.ServiceConfig;
import com.jiaxun.sdk.scl.module.common.callbcak.SclCommonEventListener;
import com.jiaxun.sdk.scl.module.common.handler.SclCommonEventHandler;
import com.jiaxun.sdk.scl.module.common.itf.SclCommonService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：提供公共接口方法
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class SclCommonServiceImpl implements SclCommonService
{
    private static final String TAG = SclCommonServiceImpl.class.getName();
    
    private SessionLooperHandler sclHandler;

    private static SclCommonServiceImpl instance;

    private SclCommonEventListener callback;

    private SclCommonServiceImpl()
    {
        sclHandler = SessionLooperHandler.getInstance();
//        // 对ACL层注册监听
        AclServiceFactory.getAclCommonService().regCommonEventListener(new SclCommonEventHandler());
    }

    public static SclCommonServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclCommonServiceImpl();
        }
        return instance;
    }

    @Override
    public int sclRegCommonEventListener(SclCommonEventListener callback)
    {
        this.callback = callback;
        return 0;
    }

    public SclCommonEventListener getSclCommonEventListener()
    {
        return callback;
    }

    @Override
    public int setNightService(boolean nightService)
    {
        Log.info(TAG, "setNightService::nightService:" + nightService);
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_NIGHT_SERVICE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, nightService);
        message.setData(data);
        sclHandler.sendMessage(message);
        return 0;
    }
//
//
//    @Override
//    public int setRingMute(boolean ringMute)
//    {
//        Message message = sclHandler.obtainMessage();
//        message.what = CommonEventEntry.MESSAGE_EVENT_RING_MUTE;
//        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
//        Bundle data = new Bundle();
//        data.putBoolean(CommonConstantEntry.DATA_ENABLE, ringMute);
//        message.setData(data);
//        sclHandler.sendMessage(message);
//        return 0;
//    }
//
//    @Override
//    public int setDnd(boolean dnd)
//    {
//        Message message = sclHandler.obtainMessage();
//        message.what = CommonEventEntry.MESSAGE_EVENT_DND;
//        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
//        Bundle data = new Bundle();
//        data.putBoolean(CommonConstantEntry.DATA_ENABLE, dnd);
//        message.setData(data);
//        sclHandler.sendMessage(message);
//        return 0;
//    }
//
//    @Override
//    public int setAutoAnswer(boolean autoAnswer)
//    {
//        Message message = sclHandler.obtainMessage();
//        message.what = CommonEventEntry.MESSAGE_EVENT_AUTO_ANSWER;
//        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
//        Bundle data = new Bundle();
//        data.putBoolean(CommonConstantEntry.DATA_ENABLE, autoAnswer);
//        message.setData(data);
//        sclHandler.sendMessage(message);
//        return 0;
//    }

    @Override
    public int updateAcountConfig(AccountConfig accountConfig)
    {
        Log.info(TAG, "updateAcountConfig::");
        if (accountConfig == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_UPDATE_ACCOUNT_CONFIG;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
        data.putParcelable(CommonConstantEntry.DATA_OBJECT, accountConfig);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }
    
//    @Override
//    public int updateBasicConfig(ServiceConfig config)
//    {
//        Log.info(TAG, "updateBasicConfig::config:" + config);
//        Message message = sclHandler.obtainMessage();
//        message.what = CommonEventEntry.MESSAGE_EVENT_UPDATE_BASIC_CONFIG;
//        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
//        Bundle data = new Bundle();
//        data.putParcelable(CommonConstantEntry.DATA_OBJECT, config);
//        message.setData(data);
//        sclHandler.sendMessage(message);
//        return 0;
//    }

    @Override
    public int startSclService(AccountConfig config)
    {
        Log.info(TAG, "startSclService::");
        if (config == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SERVICE_START;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
        data.putParcelable(CommonConstantEntry.DATA_OBJECT, config);

//        data.putSerializable(CommonEventEntry.DATA_OBJECT, config);
//        data.putInt("line", config.line[0]);
//        data.putString("localIp", config.localIp);
//        data.putString("sipAccount", config.sipAccount[0]);
//        data.putString("sipPassword", config.sipPassword[0]);
//        data.putString("sipServerIp", config.sipServerIp[0]);
//        data.putString("sipServerName", config.sipServerName[0]);
//        data.putString("serverSipPort", config.serverSipPort[0]);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int stopSclService()
    {
        Log.info(TAG, "stopSclService");
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SERVICE_STOP;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int loopTest(boolean audio, boolean video, int loopType)
    {
        // TODO Auto-generated method stub
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int updateMediaConfig(MediaConfig mediaConfig)
    {
        Log.info(TAG, "updateMediaConfig::");
        if (mediaConfig == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_UPDATE_MEDIA_CONFIG;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
        data.putParcelable(CommonConstantEntry.DATA_OBJECT, mediaConfig);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int updateServiceConfig(ServiceConfig serviceConfig)
    {
        Log.info(TAG, "updateServiceConfig::");
        if (serviceConfig == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_UPDATE_SERVICE_CONFIG;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
        data.putParcelable(CommonConstantEntry.DATA_OBJECT, serviceConfig);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }
}
