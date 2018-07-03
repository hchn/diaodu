package com.jiaxun.sdk.scl.handler;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.acl.module.common.itf.AclCommonService;
import com.jiaxun.sdk.acl.module.device.itf.AclDeviceService;
import com.jiaxun.sdk.acl.module.presence.itf.AclPresenceService;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.model.ServiceConfig;
import com.jiaxun.sdk.scl.module.common.callbcak.SclCommonEventListener;
import com.jiaxun.sdk.scl.module.common.impl.SclCommonServiceImpl;
import com.jiaxun.sdk.scl.module.presence.callback.SclPresenceEventListener;
import com.jiaxun.sdk.scl.module.presence.impl.SclPresenceServiceImpl;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.vs.VsSession;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：SCL层业务集中处理，使用消息队列异步单线程处理，保证业务处理线程安全
 *
 * @author  hubin
 *
 * @Date 2015-1-7
 */
public class SessionLooperHandler extends Handler
{
    private static String TAG = SessionLooperHandler.class.getName();
    private static SessionLooperHandler instance;

    private AclCommonService aclCommonService;

    private AclPresenceService presenceService;

    private AclDeviceService deviceService;

    private SclCommonEventListener sclCommonEventListener;

    private SclPresenceEventListener presenceEventListener;

    private boolean isInitUpline = false;// 保证只执行一次初始化上线
//    private boolean isUpdateConfig = false;

    private SessionLooperHandler()
    {
        super();
    }

    private SessionLooperHandler(Looper looper)
    {
        super(looper);
        aclCommonService = AclServiceFactory.getAclCommonService();
        presenceService = AclServiceFactory.getAclPresenceService();
        deviceService = AclServiceFactory.getAclDeviceService();
//        sclCommonEventListener = SclCommonServiceImpl.getInstance().getSclCommonEventListener();
    }

    public static SessionLooperHandler getInstance()
    {
        if (instance == null)
        {
            HandlerThread sessionThread = new HandlerThread("SessionLooperHandler", Process.THREAD_PRIORITY_BACKGROUND);
            sessionThread.start();
            instance = new SessionLooperHandler(sessionThread.getLooper());
        }
        return instance;
    }

    /**
     * 方法说明 : 检查网络连接性
     * @return boolean
     * @author hubin
     * @Date 2015-10-10
     */
    private boolean verifyConnection()
    {
        AccountConfig accountConfig = SessionManager.getInstance().getAccountConfig();
        if (accountConfig != null)
        {
            Log.info(TAG, "startService:: curIp:" + accountConfig.localIp);
            sclCommonEventListener = SclCommonServiceImpl.getInstance().getSclCommonEventListener();
            // 验证是否已启动，帐号，密码，服务器地址
            if (isInitUpline || TextUtils.isEmpty(accountConfig.account[0]) || TextUtils.isEmpty(accountConfig.password[0])
                    || (TextUtils.isEmpty(accountConfig.serverIp[0]) && TextUtils.isEmpty(accountConfig.serverIp[1])))
            {
                return false;
            }
            isInitUpline = true;
            int times = 0;// 尝试次数
            while (!checkNetWork(SessionManager.getInstance().getAccountConfig().serverIp))
            {
                SystemClock.sleep(6000);// 等待6s，再次尝试
                times++;
                if (times == 10)
                {// 尝试10次失败通知无帐号配置下线
                    times = 0;
                    if (sclCommonEventListener != null)
                    {
                        sclCommonEventListener.onSclLineStatusChange(
                                new int[] { CommonConstantEntry.LINK_STATUS_E1_LOS, CommonConstantEntry.LINK_STATUS_E1_LOS }, new int[] {
                                        CommonConstantEntry.SERVICE_STATUS_NETWORK_DISABLED, CommonConstantEntry.SERVICE_STATUS_NETWORK_DISABLED });
                    }
                }
            }
            return true;
        }
        return false;
    }

    private Runnable connectServer = new Runnable()
    {
        @Override
        public void run()
        {
            Log.info(TAG, "connectServer::");
            if (verifyConnection())
            {
                // 启动服务
                try
                {
                    aclCommonService.startAclService(SessionManager.getInstance().getAccountConfig());
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
                isInitUpline = false;// 完成初始化上线
            }
        }
    };

    private Runnable reconnectServer = new Runnable()
    {
        @Override
        public void run()
        {
            Log.info(TAG, "reconnectServer::");
            if (verifyConnection())
            {
                // 重新启动服务
                try
                {
                    aclCommonService.updateAccountConfig(SessionManager.getInstance().getAccountConfig());
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
                isInitUpline = false;// 完成初始化上线
            }
        }
    };

    public void handleMessage(Message msg)
    {
        super.handleMessage(msg);
        try
        {
            final int priority;
            final String number;
            final int channel;
            final int reason;
//            final boolean audio;
            final boolean video;
            final boolean isConfMember;
            boolean enable;
            Bundle data = msg.getData();
            final String sessionId;
            switch (msg.arg1)
            {
                case CommonEventEntry.MESSAGE_TYPE_COMMON:
                    switch (msg.what)
                    {
                        case CommonEventEntry.MESSAGE_EVENT_UPDATE_ACCOUNT_CONFIG:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_UPDATE_ACCOUNT_CONFIG");
                            AccountConfig accountConfig = (AccountConfig) data.getParcelable(CommonConstantEntry.DATA_OBJECT);
                            if (accountConfig != null)
                            {
                                SessionManager.getInstance().setAccountConfig(accountConfig);
                                new Thread(reconnectServer).start();
                            }
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_UPDATE_MEDIA_CONFIG:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_UPDATE_MEDIA_CONFIG");
                            MediaConfig meidaConfig = (MediaConfig) data.getParcelable(CommonConstantEntry.DATA_OBJECT);
                            if (meidaConfig != null)
                            {
                                SessionManager.getInstance().setMediaConfig(meidaConfig);
                            }
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_UPDATE_SERVICE_CONFIG:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_UPDATE_SERVICE_CONFIG");
                            ServiceConfig serviceConfig = (ServiceConfig) data.getParcelable(CommonConstantEntry.DATA_OBJECT);
                            if (serviceConfig != null)
                            {
                                SessionManager.getInstance().setServiceConfig(serviceConfig);
                            }
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_SERVICE_START:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SERVICE_START");
                            accountConfig = (AccountConfig) data.getParcelable(CommonConstantEntry.DATA_OBJECT);
                            if (accountConfig != null)
                            {
                                SessionManager.getInstance().setAccountConfig(accountConfig);
                                new Thread(connectServer).start();
                            }
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_SERVICE_STOP:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SERVICE_STOP");
                            aclCommonService.stopAclService();
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_NIGHT_SERVICE:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_NIGHT_SERVICE");
                            enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                            aclCommonService.setNightService(enable);
                            break;
                        case CommonEventEntry.MESSAGE_NOTIFY_NIGHT_SERVICE:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_NIGHT_SERVICE");
                            enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                            int result = data.getInt(CommonConstantEntry.DATA_RESULT);
                            if (sclCommonEventListener != null)
                            {
                                sclCommonEventListener.onSclNightServiceAck(enable, result);
                            }
                            break;
                        case CommonEventEntry.MESSAGE_NOTIFY_SERVICE_STATUS:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SERVICE_STATUS");
                            if (data == null)
                            {
                                return;
                            }
                            int[] linkStatus = data.getIntArray(CommonConstantEntry.DATA_LINK_STATUS);
                            int[] serviceStatus = data.getIntArray(CommonConstantEntry.DATA_STATUS);
//                        String reason = data.getString(CommonEventEntry.DATA_REASON);
                            if (sclCommonEventListener != null)
                            {
                                sclCommonEventListener.onSclLineStatusChange(linkStatus, serviceStatus);// 通知在线
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case CommonEventEntry.MESSAGE_TYPE_SCALL:
                    if (data == null)
                    {
                        return;
                    }
                    Log.info(TAG, "CommonEventEntry.MESSAGE_TYPE_SCALL");
                    sessionId = data.getString(CommonConstantEntry.DATA_SESSION_ID);
                    final SCallSession callSession;
                    // 发起呼叫和接收呼叫创建新session，其余情况使用以后session
                    if (msg.what == CommonEventEntry.MESSAGE_EVENT_SCALL_MAKE || msg.what == CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECEIVE)
                    {
                        callSession = (SCallSession) SessionManager.getInstance().obtain(CommonConstantEntry.SESSION_TYPE_SCALL, sessionId);
                    }
                    else
                    {
                        callSession = (SCallSession) SessionManager.getInstance().getActiveSession(sessionId);
                    }

                    if (callSession != null)
                    {
                        switch (msg.what)
                        {
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_MAKE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_MAKE");
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
                                String name = data.getString(CommonConstantEntry.DATA_OBJECT);
                                video = data.getBoolean(CommonConstantEntry.DATA_VIDEO);
                                channel = data.getInt(CommonConstantEntry.DATA_CHANNEL);
                                // 发起呼叫
                                callSession.makeCall(sessionId, number, name, priority, video, channel);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_ANSWER:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_ANSWER");
                                channel = data.getInt(CommonConstantEntry.DATA_CHANNEL);
                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
                                video = data.getBoolean(CommonConstantEntry.DATA_VIDEO);
                                callSession.answerCall(sessionId, number, channel, video);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_HANGUP:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_HANGUP");
                                int releaseReason = data.getInt(CommonConstantEntry.DATA_REASON);
                                callSession.releaseCall(sessionId, releaseReason);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_HOLD:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_HOLD");
                                callSession.holdCall(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_RETRIEVE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_RETRIEVE");
                                callSession.retrieveCall(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_MUTE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_MUTE");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                callSession.setAudioMute(enable);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_VIDEO_CONTROL:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_VIDEO_CONTROL");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_CLOSE_RING:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_CLOSE_RING");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                callSession.setCloseRing(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECEIVE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECEIVE");
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
                                video = data.getBoolean(CommonConstantEntry.DATA_VIDEO);
                                isConfMember = data.getBoolean(CommonConstantEntry.DATA_IS_CONF_MEMBER);
                                callSession.onSCallIncoming(sessionId, priority, number, null, null, null, 1, isConfMember, video);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_CONNECT:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_CONNECT");
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
//                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
//                                channel = data.getInt(CommonConstantEntry.DATA_TYPE);
                                callSession.onSCallConnect(sessionId, priority);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_CONNECT_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_CONNECT_ACK");
                                callSession.onSCallConnectAck(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_HOLD_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_HOLD_ACK");
                                callSession.onSCallHoldAck(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_MEDIA_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_MEDIA_INFO");
                                isConfMember = data.getBoolean(CommonConstantEntry.DATA_IS_CONF_MEMBER);
                                int localAudioPort = data.getInt(CommonConstantEntry.DATA_AUDIO_LOCAL_PORT);
                                int remoteAudioPort = data.getInt(CommonConstantEntry.DATA_AUDIO_REMOTE_PORT);
                                int localVideoPort = data.getInt(CommonConstantEntry.DATA_VIDEO_LOCAL_PORT);
                                int remoteVideoPort = data.getInt(CommonConstantEntry.DATA_VIDEO_REMOTE_PORT);
                                String remoteAddress = data.getString(CommonConstantEntry.DATA_REMOTE_ADDRESS);
                                Map codec = (Map) data.getSerializable(CommonConstantEntry.DATA_CODEC);
                                callSession.onSCallMediaInfo(sessionId, isConfMember, localAudioPort, remoteAudioPort, localVideoPort, remoteVideoPort,
                                        remoteAddress, codec);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_OUTGOING_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_OUTGOING_ACK");
                                channel = data.getInt(CommonConstantEntry.DATA_TYPE);
                                callSession.onSCallOutgoingAck(sessionId, channel);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_RELEASE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_RELEASE");
                                releaseReason = data.getInt(CommonConstantEntry.DATA_REASON);
                                callSession.onSCallRelease(sessionId, releaseReason);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_REMOTE_HOLD:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_REMOTE_HOLD");
                                callSession.onSCallRemoteHold(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_REMOTE_RETRIEVE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_REMOTE_RETRIEVE");
                                callSession.onSCallRemoteRetrieve(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_RETRIEVE_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_RETRIEVE_ACK");
                                callSession.onSCallRetrieveAck(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_RING_BACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_RING_BACK");
                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
                                channel = data.getInt(CommonConstantEntry.DATA_TYPE);
                                callSession.onSCallRingback(sessionId, number, channel);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECORD_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_RECORD_INFO");
                                String taskId = data.getString(CommonConstantEntry.DATA_TASK_ID);
                                remoteAddress = data.getString(CommonConstantEntry.DATA_REMOTE_ADDRESS);
                                callSession.onRecordInfo(sessionId, taskId, remoteAddress);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_AUDIO_ENABLE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_AUDIO_ENABLE");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                callSession.onAudioEnable(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_VIDEO_ENABLE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_VIDEO_ENABLE");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                callSession.onVideoEnable(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_VIDEO_SHARE_RECEIVED:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_VIDEO_SHARE_RECEIVED");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
                                String mediaTag = data.getString(CommonConstantEntry.DATA_MEDIA_TAG);
                                callSession.onVideoShareReceived(sessionId, enable, number, mediaTag);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_SCALL_MEMBER_MEDIA_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_SCALL_MEMBER_MEDIA_INFO");
                                mediaTag = data.getString(CommonConstantEntry.DATA_MEDIA_TAG);
                                callSession.onMultiMediaInfoNotify(sessionId, mediaTag);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_SCALL_DTMF_SEND:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_SCALL_DTMF_SEND");
                                char dtmf = data.getChar(CommonConstantEntry.DATA_NUMBER);
                                callSession.sendDtmf(sessionId, dtmf);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case CommonEventEntry.MESSAGE_TYPE_CONF:
                    if (data == null)
                    {
                        return;
                    }
                    String userNum;
                    String mediaTag;
                    ArrayList<ConfMemModel> memberNumberList;
                    Log.info(TAG, "CommonEventEntry.MESSAGE_TYPE_CONF");
                    sessionId = data.getString(CommonConstantEntry.DATA_SESSION_ID);
                    final ConfSession confSession;
                    // 发起会议创建新session，其余情况使用已有session
                    if (msg.what == CommonEventEntry.MESSAGE_EVENT_CONF_CREATE)
                    {
                        confSession = (ConfSession) SessionManager.getInstance().obtain(CommonConstantEntry.SESSION_TYPE_CONF, sessionId);
                    }
                    else
                    {
                        confSession = (ConfSession) SessionManager.getInstance().getActiveSession(sessionId);
                    }
                    if (confSession != null)
                    {
                        switch (msg.what)
                        {
                            case CommonEventEntry.MESSAGE_EVENT_CONF_CREATE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_CREATE");
                                String confName = data.getString(CommonConstantEntry.DATA_NUMBER);
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
                                memberNumberList = data.getParcelableArrayList(CommonConstantEntry.DATA_MEMBER_LIST);
                                video = data.getBoolean(CommonConstantEntry.DATA_VIDEO);
                                confSession.createConf(sessionId, confName, priority, 1, 1, video, memberNumberList);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_CLOSE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_CLOSE");
                                confSession.closeConf(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_ENTER:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_ENTER");
                                confSession.returnConf(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_LEAVE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_LEAVE");
                                confSession.exitConf(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_USER_ADD:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_USER_ADD");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                confSession.addUser(sessionId, userNum);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_USER_DELETE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_USER_DELETE");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                confSession.deleteUser(sessionId, userNum);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_USER_AUDIO_ENABLE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_USER_AUDIO_ENABLE");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.enableUserAudio(sessionId, userNum, enable);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_USER_VIDEO_ENABLE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_USER_VIDEO_ENABLE");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.enableUserVideo(sessionId, userNum, enable);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_USER_VIDEO_SHARE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_USER_VIDEO_SHARE");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.shareUserVideo(sessionId, userNum, enable);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_BGM_ENABLE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_BGM_ENABLE");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.enableConfBgm(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_AUDIO_MUTE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_AUDIO_MUTE");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.setAudioMute(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_CONF_VIDEO_MUTE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_CONF_VIDEO_MUTE");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.setVideoMute(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_CREATE_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_CREATE_ACK");
                                confSession.onConfCreateAck(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_BGM_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_BGM_ACK");
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.onConfBgmAck(sessionId, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_CLOSE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_CLOSE");
                                confSession.onConfClose(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_CREATE_CONNECT:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_CREATE_CONNECT");
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
                                confSession.onConfCreateConnect(sessionId, priority);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_EXIT_FAIL:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_EXIT_FAIL");
                                confSession.onConfExitFail(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_EXIT_OK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_EXIT_OK");
                                confSession.onConfExitOk(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_MEDIA_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_MEDIA_INFO");
                                int localAudioPort = data.getInt(CommonConstantEntry.DATA_AUDIO_LOCAL_PORT);
                                int remoteAudioPort = data.getInt(CommonConstantEntry.DATA_AUDIO_REMOTE_PORT);
                                int localVideoPort = data.getInt(CommonConstantEntry.DATA_VIDEO_LOCAL_PORT);
                                int remoteVideoPort = data.getInt(CommonConstantEntry.DATA_VIDEO_REMOTE_PORT);
                                String remoteAddress = data.getString(CommonConstantEntry.DATA_REMOTE_ADDRESS);
                                Map codec = (Map) data.getSerializable(CommonConstantEntry.DATA_CODEC);
                                confSession.onConfMediaInfo(sessionId, localAudioPort, remoteAudioPort, localVideoPort, remoteVideoPort, remoteAddress, codec);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_RETURN_FAIL:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_RETURN_FAIL");
                                confSession.onConfReturnFail(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_RETURN_OK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_RETURN_OK");
                                confSession.onConfReturnOk(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_ANSWER:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_ANSWER");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                mediaTag = data.getString(CommonConstantEntry.DATA_MEDIA_TAG);
                                confSession.onConfUserAnswer(sessionId, userNum, mediaTag);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_AUDIO_ENABLE_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_AUDIO_ENABLE_ACK");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.onConfUserAudioEnableAck(sessionId, userNum, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_RELEASE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_RELEASE");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                reason = data.getInt(CommonConstantEntry.DATA_REASON);
                                confSession.onConfUserRelease(sessionId, userNum, reason);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_RING:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_RING");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                confSession.onConfUserRing(sessionId, userNum);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_VIDEO_ENABLE_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_VIDEO_ENABLE_ACK");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.onConfUserVideoEnableAck(sessionId, userNum, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_VIDEO_SHARE_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_USER_VIDEO_SHARE_ACK");
                                userNum = data.getString(CommonConstantEntry.DATA_NUMBER);
                                enable = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                                confSession.onConfUserVideoShareAck(sessionId, userNum, enable);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_CONF_RECORD_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_CONF_RECORD_INFO");
                                String taskId = data.getString(CommonConstantEntry.DATA_TASK_ID);
                                remoteAddress = data.getString(CommonConstantEntry.DATA_REMOTE_ADDRESS);
                                confSession.onRecordInfo(sessionId, taskId, remoteAddress);
                                break;

                            default:
                                break;
                        }
                    }
                    break;
                case CommonEventEntry.MESSAGE_TYPE_PRESENCE:
                    if (data == null)
                    {
                        return;
                    }
                    Log.info(TAG, "MESSAGE_TYPE_PRESENCE");
                    String[] user;
                    boolean on;
                    switch (msg.what)
                    {
                        case CommonEventEntry.MESSAGE_EVENT_PRESENCE_SUBSCRIBE:
                            Log.info(TAG, "MESSAGE_EVENT_PRESENCE_SUBSCRIBE");
                            user = data.getStringArray(CommonConstantEntry.DATA_MEMBER_LIST);
                            on = data.getBoolean(CommonConstantEntry.DATA_ENABLE);
                            presenceService.presenceSubscribe(user, on);
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_PRESENCE_CANCEL_ALL:
                            Log.info(TAG, "MESSAGE_EVENT_PRESENCE_CANCEL_ALL");
                            presenceService.cancelAllSubscribe();
                            break;
                        case CommonEventEntry.MESSAGE_NOTIFY_PRESENCE_USER_STATUS:
                            Log.info(TAG, "MESSAGE_NOTIFY_PRESENCE_USER_STATUS");
                            presenceEventListener = SclPresenceServiceImpl.getInstance().getPresenceEventListener();
                            if (presenceEventListener != null)
                            {
                                presenceEventListener.onPresenceUserStatus((ArrayList<HashMap<String, Integer>>) msg.obj);
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case CommonEventEntry.MESSAGE_TYPE_IM:
                    break;
                case CommonEventEntry.MESSAGE_TYPE_VS:
                    if (data == null)
                    {
                        return;
                    }
                    Log.info(TAG, "MESSAGE_TYPE_VS");
                    sessionId = data.getString(CommonConstantEntry.DATA_SESSION_ID);
                    VsSession vsSession;
                    // 发起视频监控创建新session，其余情况使用以后session
                    if (msg.what == CommonEventEntry.MESSAGE_EVENT_VS_OPEN)
                    {
                        vsSession = (VsSession) SessionManager.getInstance().obtain(CommonConstantEntry.SESSION_TYPE_VS, sessionId);
                    }
                    else
                    {
                        vsSession = (VsSession) SessionManager.getInstance().getActiveSession(sessionId);
                    }

                    if (vsSession != null)
                    {
                        switch (msg.what)
                        {
                            case CommonEventEntry.MESSAGE_EVENT_VS_OPEN:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_VS_OPEN");
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
                                number = data.getString(CommonConstantEntry.DATA_NUMBER);
                                vsSession.openVs(sessionId, number, priority);
                                break;
                            case CommonEventEntry.MESSAGE_EVENT_VS_CLOSE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_VS_CLOSE");
                                vsSession.closeVs(sessionId);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_VS_OPEN_ACK:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_VS_OPEN_ACK");
                                priority = data.getInt(CommonConstantEntry.DATA_PRIORITY);
                                vsSession.onVsOpenAck(sessionId, priority);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_VS_CLOSE:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_VS_CLOSE");
                                reason = data.getInt(CommonConstantEntry.DATA_REASON);
                                vsSession.onVsClosed(sessionId, reason);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_VS_RECORD_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_VS_RECORD_INFO");
                                String taskId = data.getString(CommonConstantEntry.DATA_TASK_ID);
                                String address = data.getString(CommonConstantEntry.DATA_REMOTE_ADDRESS);
                                vsSession.onRecordInfo(sessionId, taskId, address);
                                break;
                            case CommonEventEntry.MESSAGE_NOTIFY_VS_MEDIA_INFO:
                                Log.info(TAG, "CommonEventEntry.MESSAGE_NOTIFY_VS_MEDIA_INFO");
                                int localVideoPort = data.getInt(CommonConstantEntry.DATA_VIDEO_LOCAL_PORT);
                                int remoteVideoPort = data.getInt(CommonConstantEntry.DATA_VIDEO_REMOTE_PORT);
                                String remoteAddress = data.getString(CommonConstantEntry.DATA_REMOTE_ADDRESS);
                                vsSession.onVsMediaInfo(sessionId, localVideoPort, remoteVideoPort, remoteAddress);
                                break;
                            default:
                                break;
                        }
                    }
                    break;
                case CommonEventEntry.MESSAGE_TYPE_DEVICE:
                    if (data == null)
                    {
                        return;
                    }
                    Log.info(TAG, "MESSAGE_TYPE_DEVICE");
                    sessionId = data.getString(CommonConstantEntry.DATA_SESSION_ID);
                    switch (msg.what)
                    {
                        case CommonEventEntry.MESSAGE_EVENT_DEVICE_CAMERA_CONTROL:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_DEVICE_CAMERA_CONTROL");
                            number = data.getString(CommonConstantEntry.DATA_NUMBER);
                            int command = data.getInt(CommonConstantEntry.DATA_COMMAND);
                            int commandPara1 = data.getInt(CommonConstantEntry.DATA_COMMAND_PARA1);
                            int commandPara2 = data.getInt(CommonConstantEntry.DATA_COMMAND_PARA2);
                            int commandPara3 = data.getInt(CommonConstantEntry.DATA_COMMAND_PARA3);
                            deviceService.remoteCameraControl(sessionId, number, command, commandPara1, commandPara2, commandPara3);
                            break;
                        case CommonEventEntry.MESSAGE_EVENT_DEVICE_CAMERA_INIT:
                            Log.info(TAG, "CommonEventEntry.MESSAGE_EVENT_DEVICE_CAMERA_INIT");
                            SessionManager.getInstance().initLocalCamera();
                            break;

                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }

            Log.info(TAG, "handleMessage finished");
        }
        catch (Exception e)
        {
            handleException(e);
        }
    }

    /**
     * 检测网络是否连接
     * 
     * @return true：网络已经连接
     */
    private boolean checkNetWork(String[] ipAddress)
    {
        try
        {
            for (String ip : ipAddress)
            {
                Log.info(TAG, "checkNetWork ip: " + ip);
                if (SdkUtil.isConnect(ip))
                {
                    return true;
                }
            }
//            String server = CommonConfigEntry.ACCOUNT_INFO_MASTER_ADDRESS;
//            isNetWork = SdkUtil.isConnect(server);
//
//            if (!isNetWork)
//            {// 连接不到主服务器，检测备服务器
//                Log.info(TAG, "cann't connected master server, try slaver server...");
//                server = CommonConfigEntry.ACCOUNT_INFO_SLAVE_ADDRESS;
//                isNetWork = SdkUtil.isConnect(server);
//            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        return false;
    }

    private void handleException(Exception e)
    {
        Log.exception(TAG, e);
    }
}
