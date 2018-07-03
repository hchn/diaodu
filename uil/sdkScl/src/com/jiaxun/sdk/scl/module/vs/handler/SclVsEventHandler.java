package com.jiaxun.sdk.scl.module.vs.handler;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.module.vs.callback.AclVsEventListener;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：视频监控事件监听器
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public class SclVsEventHandler implements AclVsEventListener
{
    private String TAG = SclVsEventHandler.class.getName();
    private SessionLooperHandler sclHandler = SessionLooperHandler.getInstance();

    @Override
    public void onVsOpenAck(String sessionId, int priority)
    {
        Log.info(TAG, "onVsOpenAck:: sessionId:" + sessionId + " priority:" + priority);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_VS_OPEN_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_VS;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, priority);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onVsClosed(String sessionId, int reason)
    {
        Log.info(TAG, "onVsClosed:: sessionId:" + sessionId + " reason:" + reason);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_VS_CLOSE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_VS;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_REASON, reason);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onVsMediaInfo(String sessionId, int localVideoPort, int remoteVideoPort, String remoteAddress)
    {
        Log.info(TAG, "onVsMediaInfo:: remoteVideoPort:" + remoteVideoPort + " remoteAddress:" + remoteAddress);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(remoteAddress))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_VS_MEDIA_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_VS;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_VIDEO_LOCAL_PORT, localVideoPort);
        data.putInt(CommonConstantEntry.DATA_VIDEO_REMOTE_PORT, remoteVideoPort);
        data.putString(CommonConstantEntry.DATA_REMOTE_ADDRESS, remoteAddress);
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
        message.what = CommonEventEntry.MESSAGE_NOTIFY_VS_RECORD_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_VS;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_TASK_ID, taskId);
        data.putString(CommonConstantEntry.DATA_REMOTE_ADDRESS, server);
        message.setData(data);
        sclHandler.sendMessage(message);
    }
}
