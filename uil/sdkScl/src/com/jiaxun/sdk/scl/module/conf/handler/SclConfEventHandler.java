package com.jiaxun.sdk.scl.module.conf.handler;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：会议事件通知处理
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public class SclConfEventHandler implements AclConfEventListener
{
    private String TAG = SclConfEventHandler.class.getName();
    private SessionLooperHandler sclHandler = SessionLooperHandler.getInstance();

    @Override
    public void onConfCreateAck(String sessionId)
    {
        Log.info(TAG, "onConfCreateAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_CREATE_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfCreateConnect(String sessionId, int priority)
    {
        Log.info(TAG, "onConfCreateConnect:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_CREATE_CONNECT;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, priority);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfClose(String sessionId)
    {
        Log.info(TAG, "onConfClose:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_CLOSE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfExitOk(String sessionId)
    {
        Log.info(TAG, "onConfExitOk:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_EXIT_OK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfExitFail(String sessionId)
    {
        Log.info(TAG, "onConfExitFail:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_EXIT_FAIL;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfReturnOk(String sessionId)
    {
        Log.info(TAG, "onConfReturnOk:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_RETURN_OK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfReturnFail(String sessionId)
    {
        Log.info(TAG, "onConfReturnFail:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_RETURN_FAIL;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfBgmAck(String sessionId, boolean enable)
    {
        Log.info(TAG, "onConfBgmAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_BGM_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfUserRing(String sessionId, String userNum)
    {
        Log.info(TAG, "onConfUserRing:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_RING;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfUserAnswer(String sessionId, String userNum, String mediaTag)
    {
        Log.info(TAG, "onConfUserAnswer:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_ANSWER;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putString(CommonConstantEntry.DATA_MEDIA_TAG, mediaTag);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfUserRelease(String sessionId, String userNum, int reason)
    {
        Log.info(TAG, "onConfUserRelease:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_RELEASE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putInt(CommonConstantEntry.DATA_REASON, reason);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfUserAudioEnableAck(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "onConfUserAudioEnableAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_AUDIO_ENABLE_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfUserVideoEnableAck(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "onConfUserVideoEnableAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_VIDEO_ENABLE_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfUserVideoShareAck(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "onConfUserVideoShareAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(userNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_VIDEO_SHARE_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, userNum);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onConfMediaInfo(String sessionId, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort, String remoteAddress,
            Map codec)
    {
        Log.info(TAG, "onConfMediaInfo:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(remoteAddress))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_MEDIA_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_AUDIO_LOCAL_PORT, localAudioPort);
        data.putInt(CommonConstantEntry.DATA_AUDIO_REMOTE_PORT, remoteAudioPort);
        data.putInt(CommonConstantEntry.DATA_VIDEO_LOCAL_PORT, localVideoPort);
        data.putInt(CommonConstantEntry.DATA_VIDEO_REMOTE_PORT, remoteVideoPort);
        data.putString(CommonConstantEntry.DATA_REMOTE_ADDRESS, remoteAddress);
        data.putSerializable(CommonConstantEntry.DATA_CODEC, codec);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        Log.info(TAG, "onRecordInfo:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(taskId) || TextUtils.isEmpty(server))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_CONF_RECORD_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_CONF;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_TASK_ID, taskId);
        data.putString(CommonConstantEntry.DATA_REMOTE_ADDRESS, server);
        message.setData(data);
        sclHandler.sendMessage(message);
    }
}
