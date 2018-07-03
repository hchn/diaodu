package com.jiaxun.sdk.scl.module.conf.impl;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfEventListener;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfUserEventListener;
import com.jiaxun.sdk.scl.module.conf.handler.SclConfEventHandler;
import com.jiaxun.sdk.scl.module.conf.itf.SclConfService;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：会议业务功能接口
 * 
 * @author hubin
 * 
 * @Date 2015-1-16
 */
public class SclConfServiceImpl implements SclConfService
{
    private static String TAG = SclConfServiceImpl.class.getName();
    private static SclConfServiceImpl instance;

    private SessionLooperHandler sclHandler;

    private SclConfEventListener confEventListener;

    private SclConfUserEventListener confUserEventListener;

    private SclConfServiceImpl()
    {
        sclHandler = SessionLooperHandler.getInstance();
        // 对ACL层注册监听
        AclServiceFactory.getAclConfService().confRegEventListener(new SclConfEventHandler());
    }

    public static SclConfServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclConfServiceImpl();
        }
        return instance;
    }

    public SclConfEventListener getSclConfEventListener()
    {
        return confEventListener;
    }

    public SclConfUserEventListener getSclConfUserEventListener()
    {
        return confUserEventListener;
    }

    @Override
    public int confRegEventListener(SclConfEventListener confCallback)
    {
        Log.info(TAG, "confRegEventListener");
        if (confCallback == null)
        {
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        confEventListener = confCallback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confUserRegEventListener(SclConfUserEventListener userCallback)
    {
        Log.info(TAG, "confUserRegEventListener");
        if (userCallback == null)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        confUserEventListener = userCallback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confCreate(String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList)
    {
        Log.info(TAG, "confCreate::callPriority:" + callPriority + " confType:" + confType + " channel:" + channel + " video:" + video + " memberList:"
                + memberList);
        if (callPriority < CommonConfigEntry.PRIORITY_MIN || callPriority > CommonConfigEntry.PRIORITY_MAX)
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_CREATE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        String sessionId = UUID.randomUUID().toString();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, confName);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, callPriority);
        data.putParcelableArrayList(CommonConstantEntry.DATA_MEMBER_LIST, memberList);
        data.putBoolean(CommonConstantEntry.DATA_VIDEO, video);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confClose(String sessionId)
    {
        Log.info(TAG, "confClose::sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_CLOSE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confEnter(String sessionId)
    {
        Log.info(TAG, "confEnter::sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_ENTER;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confLeave(String sessionId)
    {
        Log.info(TAG, "confLeave::sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_LEAVE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confUserAdd(String sessionId, String userNum)
    {
        Log.info(TAG, "confUserAdd::sessionId:" + sessionId + " userNum:" + userNum);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_USER_ADD;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confUserDelete(String sessionId, String userNum)
    {
        Log.info(TAG, "confUserDelete::sessionId:" + sessionId + " userNum:" + userNum);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_USER_DELETE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confUserAudioEnable(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "confUserAudioEnable::sessionId:" + sessionId + " userNum:" + userNum + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_USER_AUDIO_ENABLE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confUserVideoEnable(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "confUserVideoEnable::sessionId:" + sessionId + " userNum:" + userNum + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_USER_VIDEO_ENABLE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confUserVideoShare(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "confUserVideoShare::sessionId:" + sessionId + " userNum:" + userNum + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_USER_VIDEO_SHARE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confBgmEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "confBgmEnable::sessionId:" + sessionId + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_BGM_ENABLE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confAudioMute(String sessionId, boolean mute)
    {
        Log.info(TAG, "confAudioMute::sessionId:" + sessionId + " mute:" + mute);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_AUDIO_MUTE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, mute);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confVideoMute(String sessionId, boolean mute)
    {
        Log.info(TAG, "confVideoMute::sessionId:" + sessionId + " mute:" + mute);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_CONF_VIDEO_MUTE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, mute);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

}
