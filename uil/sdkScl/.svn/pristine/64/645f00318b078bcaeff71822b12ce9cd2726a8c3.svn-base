package com.jiaxun.sdk.scl.module.scall.handler;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：单呼事件通知接口
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public class SclSCallEventHandler implements AclSCallEventListener
{
    private String TAG = SclSCallEventHandler.class.getName();
    private SessionLooperHandler sclHandler;

    public SclSCallEventHandler()
    {
        sclHandler = SessionLooperHandler.getInstance();
    }

    @Override
    public void onSCallConnectAck(String sessionId)
    {
        Log.info(TAG, "onSCallConnectAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_CONNECT_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallRelease(String sessionId, int reason)
    {
        Log.info(TAG, "onSCallRelease:: sessionId:" + sessionId + " reason:" + reason);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_RELEASE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_REASON, reason);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallHoldAck(String sessionId)
    {
        Log.info(TAG, "onSCallHoldAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_HOLD_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallRemoteHold(String sessionId)
    {
        Log.info(TAG, "onSCallRemoteHold:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_REMOTE_HOLD;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallRemoteRetrieve(String sessionId)
    {
        Log.info(TAG, "onSCallRemoteRetrieve:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_REMOTE_RETRIEVE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallRetrieveAck(String sessionId)
    {
        Log.info(TAG, "onSCallRetrieveAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_RETRIEVE_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallIncoming(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean isConf, boolean video)
    {
        Log.info(TAG, "onSCallIncoming:: sessionId:" + sessionId + ", callerNum:" + callerNum + ", szCalleeUN:" + calleeNum + ", callPriority:" + callPriority
                + ", isConf:" + isConf + ", video:" + video);
        if (TextUtils.isEmpty(sessionId))
        {// 验证参数
            return;
        }
        if (TextUtils.isEmpty(callerNum) && TextUtils.isEmpty(calleeNum))
        {// 验证参数，主叫号码和功能码全部没有
            return;
        }
        if (callPriority < CommonConfigEntry.PRIORITY_MIN || callPriority > CommonConfigEntry.PRIORITY_MAX)
        {// 有效优先级：0,1,2,3,4，-1是默认不带优先级
            return;
        }

//        SCallModel newCall = new SCallModel();
//        newCall.setPeerNum(callerNum);
//        newCall.setCallStatus(CommonConstantEntry.CALL_STATE_INCOMING);
//        newCall.setCallType(CommonConstantEntry.CALL_TYPE_SINGLE);// TODO
//        newCall.setPriority(callPriority);
//        newCall.setStarter(false);
//        newCall.setPeerName("");

        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECEIVE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
//        data.putParcelable(CommonConstantEntry.DATA_OBJECT, newCall);
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, callPriority);
        data.putString(CommonConstantEntry.DATA_NUMBER, callerNum);
        data.putBoolean(CommonConstantEntry.DATA_IS_CONF_MEMBER, isConf);
        data.putBoolean(CommonConstantEntry.DATA_VIDEO, video);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallMediaInfo(String sessionId, boolean isConfMember, int localAudioPort, int remoteAudioPort, int localVideoPort, int remoteVideoPort,
            String remoteAddress, Map codec)
    {
        Log.info(TAG, "onSCallMediaInfo:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_MEDIA_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_IS_CONF_MEMBER, isConfMember);
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
    public void onSCallOutgoingAck(String sessionId, int channel)
    {
        Log.info(TAG, "onSCallOutgoingAck:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_OUTGOING_ACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_TYPE, channel);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallRingback(String sessionId, String ringNum, int channel)
    {
        Log.info(TAG, "onSCallRingback:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(ringNum))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_RING_BACK;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, ringNum);
        data.putInt(CommonConstantEntry.DATA_TYPE, channel);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onSCallConnect(String sessionId, int callPriority)
    {
        Log.info(TAG, "onSCallConnect:: sessionId:" + sessionId);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_CONNECT;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putInt(CommonConstantEntry.DATA_PRIORITY, callPriority);
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
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECORD_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_TASK_ID, taskId);
        data.putString(CommonConstantEntry.DATA_REMOTE_ADDRESS, server);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onAudioEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "onAudioEnable:: sessionId:" + sessionId + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_AUDIO_ENABLE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onVideoEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "onVideoEnable:: sessionId:" + sessionId + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_VIDEO_ENABLE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onVideoShareReceived(String sessionId, boolean enable, String videoNum, String tag)
    {
        Log.info(TAG, "onVideoReceived:: sessionId:" + sessionId + " enable:" + enable);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(videoNum) || TextUtils.isEmpty(tag))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_VIDEO_SHARE_RECEIVED;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        data.putString(CommonConstantEntry.DATA_NUMBER, videoNum);
        data.putString(CommonConstantEntry.DATA_MEDIA_TAG, tag);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onMultiMediaInfoNotify(String sessionId, String mediaTag)
    {
        Log.info(TAG, "onMemberMediaInfoNotify:: sessionId:" + sessionId + " mediaTag:" + mediaTag);
        if (TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(mediaTag))
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SCALL_MEMBER_MEDIA_INFO;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_SCALL;
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_MEDIA_TAG, mediaTag);
        message.setData(data);
        sclHandler.sendMessage(message);
    }
}
