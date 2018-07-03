package com.jiaxun.sdk.scl.module.scall.impl;

import java.util.UUID;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.scl.module.scall.callback.SclSCallEventListener;
import com.jiaxun.sdk.scl.module.scall.handler.SclSCallEventHandler;
import com.jiaxun.sdk.scl.module.scall.itf.SclSCallService;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：单呼业务功能接口
 * 
 * @author hubin
 * 
 * @Date 2015-1-16
 */
public class SclSCallServiceImpl implements SclSCallService
{
    private static String TAG = SclSCallServiceImpl.class.getName();
    private SessionLooperHandler sclHandler;

    private static SclSCallServiceImpl instance;

    private SclSCallEventListener sclSCallEventListener;
    private SessionManager sessionMgr;

    private SclSCallServiceImpl()
    {
        sclHandler = SessionLooperHandler.getInstance();
        sessionMgr = SessionManager.getInstance();
        // 对ACL层注册监听
        AclServiceFactory.getAclSCallService().sCallRegEventListener(new SclSCallEventHandler());
    }

    public static SclSCallServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new SclSCallServiceImpl();
        }
        return instance;
    }

    public SclSCallEventListener getSclSCallEventListener()
    {
        return sclSCallEventListener;
    }

    @Override
    public int sCallRegEventListener(SclSCallEventListener callback)
    {
        sclSCallEventListener = callback;
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallRelease(final String sessionId, final int reason)
    {
        Log.info(TAG, "sCallRelease::sessionId:" + sessionId + " reason:" + reason);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_HANGUP;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_REASON, reason);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallHold(String sessionId)
    {
        Log.info(TAG, "sCallHold::sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_HOLD;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallRetrieve(String sessionId)
    {
        Log.info(TAG, "sCallRetrieve::sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_RETRIEVE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallMake(int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, String calleeName, int channel, boolean video)
    {
        Log.info(TAG, "sCallMake::callPriority: " + callPriority + " callerNum:" + callerNum + " callerName:" + callerName + " funcCode:" + funcCode
                + " calleeNum:" + calleeNum + " channel:" + channel + "video:" + video);
        if (callPriority < CommonConfigEntry.PRIORITY_MIN || callPriority > CommonConfigEntry.PRIORITY_MAX || TextUtils.isEmpty(calleeNum))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_MAKE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        String sessionId = UUID.randomUUID().toString();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, callPriority);
        data.putInt(CommonConstantEntry.DATA_CHANNEL, channel);
        data.putString(CommonConstantEntry.DATA_NUMBER, calleeNum);
        data.putString(CommonConstantEntry.DATA_OBJECT, calleeName);
        data.putBoolean(CommonConstantEntry.DATA_VIDEO, video);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallAnswer(String sessionId, String name, int channel, boolean video)
    {
        Log.info(TAG, "sCallAnswer::sessionId: " + sessionId + " name:" + name + " channel:" + channel + "video:" + video);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_ANSWER;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, name);
        data.putInt(CommonConstantEntry.DATA_CHANNEL, channel);
        data.putBoolean(CommonConstantEntry.DATA_VIDEO, video);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallAudioEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "sCallAudioMute::sessionId:" + sessionId + " mute:" + enable);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_MUTE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallVideoEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "sCallVideoEnable::sessionId:" + sessionId + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_VIDEO_CONTROL;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallDtmfSend(String sessionId, char dtmf)
    {
        Log.info(TAG, "sCallDtmfSend::sessionId:" + sessionId + " dtmf:" + dtmf);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_DTMF_SEND;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putChar(CommonConstantEntry.DATA_NUMBER, dtmf);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallCloseRing(String sessionId, boolean enable)
    {
        Log.info(TAG, "sCallCloseRing::sessionId:" + sessionId + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId))
        {
            return CommonConstantEntry.PARAM_ERROR;
        }
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_EVENT_SCALL_CLOSE_RING;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

}
