/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2008 Hughes Systique Corporation, USA (http://www.hsc.com)
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.jiaxun.sdk.acl.line.sip;

import java.net.URI;
import java.util.Hashtable;

import org.zoolu.net.IpAddress;
import org.zoolu.sip.call.ExtendedCall;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;

import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.LineAdapter;
import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.line.sip.event.NotifyEventHandler;
import com.jiaxun.sdk.acl.line.sip.service.callforward.CallForwardQueryHandler;
import com.jiaxun.sdk.acl.line.sip.service.callforward.CallForwardSettingHandler;
import com.jiaxun.sdk.acl.line.sip.service.conference.ConferenceHandler;
import com.jiaxun.sdk.acl.line.sip.service.doublecenter.DoubleCenterEventHandler;
import com.jiaxun.sdk.acl.line.sip.service.nightservice.NightServiceHandler;
import com.jiaxun.sdk.acl.line.sip.service.userstatus.UserstatusHandler;
import com.jiaxun.sdk.acl.line.sip.service.version.VersionEventHandler;
import com.jiaxun.sdk.acl.line.sip.service.version.VersionHandler;
import com.jiaxun.sdk.acl.line.sip.ua.KeepAliveSip;
import com.jiaxun.sdk.acl.line.sip.ua.RegisterAgent;
import com.jiaxun.sdk.acl.line.sip.ua.UserAgent;
import com.jiaxun.sdk.acl.line.sip.util.KeepAliveSdk;
import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;
import com.jiaxun.sdk.acl.module.device.callback.AclDeviceEventListener;
import com.jiaxun.sdk.acl.module.im.callback.AclImEventListener;
import com.jiaxun.sdk.acl.module.presence.callback.AclPresenceEventListener;
import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;
import com.jiaxun.sdk.acl.module.vs.callback.AclVsEventListener;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;

/**
 * handle all the interface requests.
 */
public class SipAdapter extends LineAdapter
{
    private final static String TAG = SipAdapter.class.getName();
    /**
     * User Agent
     */
    public UserAgent userAgent;

    /**
     * Register Agent
     */
    public RegisterAgent regAgent;

    private KeepAliveSip keepAlive;

    /**
     * UserAgentProfile
     */
//    public UserAgentProfile user_profile;

    public SipProvider sip_provider;

    String lastmsg;
//    Profile serviceProfile;// sip账户信息

    NotifyEventHandler eventHandler;

//    private boolean registerSuccessed = false;// 是否注册成功

    private KeepAliveSdk keepAliveSdk;// poc心跳线程

    private LineManager lineManager;

    /**
     * 会议消息处理
     */
    private ConferenceHandler conference;

    /**
     * 用户状态订阅处理
     */
    private UserstatusHandler userstatus;

    // ********************** user configurations *********************
    /**
     * User's AOR (Address Of Record), used also as From URL. <p/> The AOR is
     * the SIP address used to register with the user's registrar server (if
     * requested). <br/> The address of the registrar is taken from the hostport
     * field of the AOR, i.e. the value(s) host[:port] after the '@' character.
     * <p/> If not defined (default), it equals the <i>contact_url</i>
     * attribute.
     */
    public String from_url = null;
    /**
     * Contact URL. If not defined (default), it is formed by
     * sip:local_user@host_address:host_port
     */
    public String contact_url = null;
    /** User's name (used to build the contact_url if not explitely defined) */
    public String username = null;
    /** User's realm. */
    public String realm = null, realm_orig = null;
    /** User's passwd. */
    public String passwd = null;
    public int serverPort = 5060;

    /** Expires time (in seconds). */
    public int expires = 3600;

    /**
     * Redirect incoming call to the secified url. Use value 'NONE' for not
     * redirecting incoming calls (or let it undefined).
     */
    public String redirect_to = null;

    /**
     * Forward calls to the secified url. Use value 'NONE' for not Forwardring
     * calls (or let it undefined).
     */
    public String Forward_to = null;

    /** No offer in the invite */
    public boolean no_offer = false;

    // IMS MMTel settings (added by mandrajg)
    /** q value used at registration */
    public String qvalue = null;
    /** MMTel flavor used */
    public boolean mmtel = false;

    /** Whether using audio */
    public boolean audio = true; // modified
    /** Whether using video */
    public boolean video = false; // modified

    /** Audio port */
    private static int audio_port = 21000;
    public int[] audio_codecs = { 3, 8, 0 };
    public int dtmf_avp = 121; // zero means no use of outband DTMF
    /** Audio sample rate */
    public int audio_sample_rate = 8000;
    /** Audio sample size */
    public int audio_sample_size = 1;
    /** Audio frame size */
    public int audio_frame_size = 160;

    /** Video port */
    private static int video_port = 21002;
    /** Video avp */
    public int video_avp = 109;
    /**
     * Automatic hangup time (call duartion) in seconds; time<=0 corresponds to
     * manual hangup mode.
     */
    public int hangup_time = -1;

    private String localIp;// 本地地址

    public SipAdapter(String localIp, int port, LineManager lineManager)
    {
        try
        {
            this.localIp = localIp;
            // 启动sip引擎
            Log.info(TAG, "startEngine");
            this.lineManager = lineManager;
            SipStack.init(null);
            SipStack.default_transport_protocols = new String[] { "udp" };
            // 终端型号 + 空格 + POC-SDK版本
            SipStack.ua_info = SdkUtil.getModel() + CommonConfigEntry.SDK_VERSION;

            if (sip_provider == null)
            {
                sip_provider = new SipProvider(IpAddress.localIpAddress, port);
                sip_provider.setViaAddress(localIp);// 设置发送消息的地址
            }
            else
            {
                sip_provider.halt();// 去初始化（不关闭网络连接）
            }

            if (CommonConfigEntry.HEARTBEAT_SERVER_)
            {// 开启服务器心跳
                if (keepAlive != null)
                {
                    try
                    {
                        keepAlive.halt();
                    }
                    catch (Exception ex)
                    {
                        Log.exception(TAG, ex);
                    }
                }
                keepAlive = new KeepAliveSip(sip_provider, CommonConfigEntry.HEARTBEAT_SERVER_TIME, this);
            }

            if (eventHandler != null)
            {
                try
                {
                    eventHandler.halt();
                }
                catch (Exception ex)
                {
                    Log.exception(TAG, ex);
                }

            }
            eventHandler = new NotifyEventHandler(sip_provider);
//            FnForceDeregisterEventListener fnForceDeregisterEventListener = new FnForceDeregisterEventListener();
//            fnForceDeregisterEventListener.setSipPocEngine(this);
//            // 功能码强制注销监听
//            eventHandler.subscribe(FnForceDeregisterEventListener.TOPIC_FN_FORCE_UNREG_NOTIFY, fnForceDeregisterEventListener);
            // 版本查询监听
            eventHandler.subscribe(VersionEventHandler.VERSION_QUERY_NOTIFY, new VersionEventHandler());
//
//            // Modified by hubin at 20131030 群组队列，呼叫中组
//            // 呼叫中组监听
//            PushGroupListEventListener pushCurrentGroupListEventListener = new PushGroupListEventListener();
//            pushCurrentGroupListEventListener.setSipPocEngine(this);
//            eventHandler.subscribe(PushGroupListEventListener.EVENT_TYPE_PUSH_CURRENT_GROUP_CALL_LIST, pushCurrentGroupListEventListener);
//            // 群组列表监听
//            PushGroupListEventListener pushGroupListEventListener = new PushGroupListEventListener();
//            pushGroupListEventListener.setSipPocEngine(this);
//            eventHandler.subscribe(PushGroupListEventListener.EVENT_TYPE_PUSH_GROUP_CALL_LIST, pushGroupListEventListener);
            // 双中心主用通知监听
            DoubleCenterEventHandler doubleCenterEventListener = new DoubleCenterEventHandler();
            eventHandler.subscribe(DoubleCenterEventHandler.EVENT_TYPE_PUSH_SWITCH_HOME, doubleCenterEventListener);
//            // 组呼人数和离开SA监听
//            GroupCallEventListener groupCallEventListener = new GroupCallEventListener();
//            groupCallEventListener.setSipPocEngine(this);
//            eventHandler.subscribe(GroupCallEventListener.GROUPCALLNUMBER_EVENTTYPE, groupCallEventListener);
//            eventHandler.subscribe(GroupCallEventListener.GROUPCALLSA_EVENTTYPE, groupCallEventListener);
//
//            // 重联组呼变化监听
//            DoubleHeadEventListener listener = new DoubleHeadEventListener();
//            listener.setSipPocEngine(this);
//            eventHandler.subscribe(DoubleHeadEventListener.EVENT_TYPE_UNBIND_NOTIFY, listener);

            if (CommonConfigEntry.HEARTBEAT_POC)
            {// 开启poc心跳
                if (keepAliveSdk != null)
                {
                    keepAliveSdk.halt();
                }
                keepAliveSdk = new KeepAliveSdk(this);
                keepAliveSdk.start();// poc心跳启动
            }

//            renewRegister.setRenewRegister(true);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Get a increased audio port,
     * from 21000 ~ 21100, even number only.
     * @return
     */
    public int getAudioPort()
    {
        audio_port += 4;
        if (audio_port > 21100)
        {
            audio_port = 21000;
        }
        return audio_port;
    }

    /**
     * Get a increased video port,
     * from 21000 ~ 21100, even number only.
     * @return
     */
    public int getVideoPort()
    {
        video_port += 4;
        if (video_port > 21100)
        {
            video_port = 21002;
        }
        return video_port;
    }

    @Override
    public boolean setServiceProfile(String sipAccount, String sipPassword, String localIp, String sipServerIp, String sipServerName, int sipServerPort)
    {
        if (sip_provider != null)
        {
            sip_provider.setViaAddress(localIp);// 设置发送消息的地址
        }
        this.localIp = localIp;
        // 初始化配置文件
//        user_profile = new UserAgentProfile();
        username = sipAccount; // modified
        passwd = sipPassword;
        if (TextUtils.isEmpty(sipServerName))
        {
            realm = sipServerIp;
        }
        else
        {
            realm = sipServerName;
        }
        realm_orig = realm;
        from_url = username;
        serverPort = sipServerPort;
        setContactURL();
        return true;
    }

    /**
     * Sets contact_url and from_url with transport information. <p/> This
     * method actually sets contact_url and from_url only if they haven't still
     * been explicitly initialized.
     */
    public void initContactAddress(SipProvider sip_provider)
    { // contact_url
        if (contact_url == null)
        {
            contact_url = "sip:" + username + "@" + sip_provider.getViaAddress();
            if (sip_provider.getPort() != SipStack.default_port)
            {
                contact_url += ":" + sip_provider.getPort();
            }
            if (!sip_provider.getDefaultTransport().equals(SipProvider.PROTO_UDP))
            {
                contact_url += ";transport=" + sip_provider.getDefaultTransport();
            }
        }
        // from_url
        if (from_url == null)
        {
            from_url = contact_url;
        }
    }

    private void setContactURL()
    {
        contact_url = getContactURL(username);
        Log.info("SipPocEngine", "setContactURL::user_profile.contact_url" + contact_url);

        if (!from_url.contains("@"))
        {
            from_url += "@" + realm;
        }
    }

    private String getContactURL(String username)
    {
        int i = username.indexOf("@");
        if (i != -1)
        {
            // if the username already contains a @
            // strip it and everthing following it
            username = username.substring(0, i);
        }

        return username + "@" + sip_provider.getViaAddress() + (sip_provider.getPort() != 0 ? ":" + sip_provider.getPort() : "") + ";transport="
                + sip_provider.getDefaultTransport();
    }

    /**
     * 清除所有呼叫
     */
    public void clearCall()
    {
        Log.info(TAG, "clearCall::");
        if (userAgent != null)
        {
            try
            {
                int callSize = userAgent.getCallCache().size();
                Log.info(TAG, "clearCall:: callSize:" + callSize);
                if (callSize > 0)
                {
                    userAgent.hangupAll();
//                    callIdMap.clear();
                }
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }
    }

    /**
     * 注册服务器
     * 
     * @param lastLocalIp
     *            原本地IP地址，在网络切换且本地IP地址改变时传入
     * 
     * @return true:注册成功
     */
    private boolean register(String lastLocalIp)
    {
        try
        {
            Log.info(TAG, "register::lastLocalIp:" + lastLocalIp + " host:" + realm);
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(realm))
            {
                Log.info(TAG, "register::username:" + username + "realm:" + realm);
                return false;
            }
            contact_url = getContactURL(from_url);

            return regAgent.register(lastLocalIp);// 注册
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
            return false;
        }
    }

    /**
     * 注册服务器
     * 
     * @return true:注册成功
     */
    public boolean register()
    {
        return register(null);
    }

    /**
     * 注销
     * 
     * @return true:注销成功
     * @throws Exception 
     */
    public boolean deregister() throws Exception
    {
        Log.info(TAG, "deregister:: host:" + realm);

        if (regAgent != null && regAgent.deregister())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 是否已经注册
     */
    public boolean isRegistered()
    {
        return regAgent != null && regAgent.isRegistered();
    }

    /**
     * 获取当前所有呼叫
     */
    public Hashtable<String, ExtendedCall> getAllCall()
    {
        if (userAgent != null && userAgent.getCallCache() != null)
        {
            return userAgent.getCallCache();
        }
        else
        {
            return null;
        }
    }

    /**
     * Receives incoming calls (auto accept)
     */
    private void listen()
    {
        if (userAgent != null)
        {
            Log.info("SipPocEngine.listen", "UAS: WAITING FOR INCOMING CALL");

            if (!audio && !video)
            {
                Log.info("SipPocEngine.listen", "ONLY SIGNALING, NO MEDIA");
            }

            userAgent.listen();
        }
    }

//    private String parseListToString(ArrayList<String> arrayList)
//    {
//        StringBuffer string = new StringBuffer();
//        for (int i = 0; i < arrayList.size(); i++)
//        {
//            if (i == 0)
//            {
//                string.append(arrayList.get(i));
//            }
//            else
//            {
//                string.append(",").append(arrayList.get(i));
//            }
//        }
//        return string.toString();
//    }

    /**
     * 设置呼叫前转
     */
    public boolean setCallForward(int type, int enable, String number, int time)
    {
        try
        {
            setContactURL();// 避免contact_url为空的情况 需要研究
            CallForwardSettingHandler handler = new CallForwardSettingHandler(sip_provider, "sip-server@" + realm_orig, contact_url);
            handler.setCallForwardType(type);
            handler.setEnabled(enable);
            handler.setForwardee(number);
            handler.setTime(time);

            XmlMessage msg = XmlMessageFactory.createSetCallForwardMsg(from_url.split("@")[0], type, enable, number, time);
            handler.send(CallForwardSettingHandler.EVENT_TYPE_CALL_FORWARD_OPERATE, msg);
            eventHandler.subscribe(msg.getRequestId(), handler);

            return true;
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".setCallForward", e);
            return false;
        }
    }

    /**
     * 设置呼叫前转
     */
    public boolean setCallForward(String alwaysNumber, String noResponseNumber, int noResponseTime, String busyNumber, String noReachNumber)
    {
        try
        {
            setContactURL();// 避免contact_url为空的情况 需要研究
            CallForwardSettingHandler handler = new CallForwardSettingHandler(sip_provider, "sip-server@" + realm_orig, contact_url);

            XmlMessage msg = XmlMessageFactory.createSetCallForwardMsg(from_url.split("@")[0], alwaysNumber, noResponseNumber, noResponseTime, busyNumber,
                    noReachNumber);
            handler.send(CallForwardSettingHandler.EVENT_TYPE_CALL_FORWARD_OPERATE, msg);
            eventHandler.subscribe(msg.getRequestId(), handler);

            return true;
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".setCallForward", e);
            return false;
        }
    }

    /**
     * 查询呼叫前转
     */
    public boolean queryCallForward(int type)
    {
        try
        {
            if (contact_url == null)
            {
                contact_url = getContactURL(username);
            }

            Log.info("queryCallForward", "user_profile.contact_url:" + contact_url + " user_profile.from_url:" + from_url + " user_profile.username:"
                    + username);

            CallForwardQueryHandler handler = new CallForwardQueryHandler(sip_provider, "sip-server@" + realm_orig, contact_url);
            handler.setCallForwardType(type);

            XmlMessage msg = XmlMessageFactory.createQueryCallForwardMsg(from_url.split("@")[0]);
            handler.send(CallForwardQueryHandler.EVENT_TYPE_CALL_FORWARD_OPERATE, msg);
            eventHandler.subscribe(msg.getRequestId(), handler);

            return true;
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".queryCallForward", e);
            return false;
        }
    }

    /**
     * 停止sip引擎
     * @throws Exception 
     */
    public void halt() throws Exception
    {
        Log.error(TAG, "halt::");

        try
        {
            clearCall();// 清除呼叫
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        try
        {
            if (isRegistered())
            {// 已经注册
                deregister();// 注销
                int times = 0;
                while (serviceStatus != CommonConstantEntry.SERVICE_STATUS_OFFLINE)
                {// 监测是否已经下线
                    times++;
                    Thread.sleep(100);
                    if (times == 10)
                    {// 超出最多次数
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        if (keepAlive != null)
        {
            keepAlive.halt();
        }

        if (regAgent != null)
        {
            regAgent.halt();
        }
        if (userAgent != null)
            userAgent.hangupAll();
        if (sip_provider != null)
            sip_provider.halt();

        if (keepAliveSdk != null)
            keepAliveSdk.halt();

        serviceStatus = CommonConstantEntry.SERVICE_STATUS_OFFLINE;
    }

    /**
     * 切换网络时本地IP地址改变，实现：不下线，通话不断开
     * 
     * @param lastLocalIp
     *            原本地IP地址
     * @param currentIp
     *            当前本机IP地址
     */
    public boolean switchForIpChange(String lastLocalIp, String currentIp)
    {
        Log.info("SipPocEngine.switchHost", "lastLocalIp:" + lastLocalIp + " currentIp:" + currentIp + " host:" + realm);
        if (lastLocalIp == null)
        {// 验证参数
            return false;
        }

        try
        {
            sip_provider.setViaAddress(currentIp);// 更改IP地址
            return register(lastLocalIp);// 带原IP地址注册
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
    }

    public int sendVersionInformation(String requestId) throws Exception
    {
        setContactURL();// 避免contact_url为空的情况 需要研究
        VersionHandler handler = new VersionHandler(sip_provider, VersionHandler.VERSION_SERVER_NAME + "@" + realm_orig, contact_url);

        String version = CommonConfigEntry.SDK_VERSION;
        // String os = SdkUtil.getOsVersion();
        // String mobile = SdkUtil.getMobileModuel();

        // XmlMessage msg =
        // XmlMessageFactory.createVersionResponseMsg(requestId, version, os,
        // mobile);
        XmlMessage msg = XmlMessageFactory.createVersionResponseMsg(requestId, version, "", "");
        handler.send(VersionEventHandler.VERSION_OPERATION, msg);

        return 0;
    }

    public KeepAliveSip getKeepAlive()
    {
        return keepAlive;
    }

    public AclSCallEventListener getSCallEventListener()
    {
        if (serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {
            return lineManager.sCallEventListener;
        }
        else
        {
            return null;
        }
    }

    public AclConfEventListener getConfEventListener()
    {
        if (serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {
            return lineManager.confEventListener;
        }
        else
        {
            return null;
        }
    }

    public AclVsEventListener getVsEventListener()
    {
        if (serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {
            return lineManager.vsEventListener;
        }
        else
        {
            return null;
        }
    }

    public AclImEventListener getImEventListener()
    {
        if (serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {
            return lineManager.imEventListener;
        }
        else
        {
            return null;
        }
    }

    public AclPresenceEventListener getPresenceEventListener()
    {
        if (serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {
            return lineManager.presenceEventListener;
        }
        else
        {
            return null;
        }
    }

    public AclDeviceEventListener getDeviceEventListener()
    {
        if (serviceStatus == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus == CommonConstantEntry.SERVICE_STATUS_STANDBY)
        {
            return lineManager.deviceEventListener;
        }
        else
        {
            return null;
        }
    }

    @Override
    public int getServiceStatus()
    {
        return serviceStatus;
    }

    @Override
    public int getLinkStatus()
    {
        return linkStatus;
    }

    public LineManager getLineManager()
    {
        return lineManager;
    }

    @Override
    public int sCallRegEventListener(AclSCallEventListener callback)
    {
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallMake(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean audio, boolean video) throws Exception
    {
        Log.info(TAG, "sCallMake::sessionId" + sessionId + " calleeNum:" + calleeNum + " priority:" + callPriority);

        if (userAgent == null)
        {
            Log.error(TAG, "userAgent is null");
            return CommonConstantEntry.RESPONSE_FAILED;
        }

        int callType = CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO;
        if (video)
            callType = CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO;
        userAgent.call(sessionId, calleeNum, callPriority, callType);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallAlerting(String sessionId, String name, int channel, boolean sendRbt) throws Exception
    {
        Log.info(TAG, "ring::callId" + sessionId);
        if (userAgent == null)
        {
            Log.error(TAG, "userAgent is null");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        userAgent.ring(sessionId);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallAnswer(String sessionId, String name, int channel, boolean audio, boolean video) throws Exception
    {
        Log.info(TAG, "answercall:: callID" + sessionId + " host:" + realm);
        // 记录性能测试日志
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAnswerCall(System.currentTimeMillis());
        }

        userAgent.accept(sessionId);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallRelease(String sessionId, int reason) throws Exception
    {
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "accept:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            switch (call.getStatus())
            {
                case CommonConstantEntry.CALL_STATE_INCOMING:
                case CommonConstantEntry.CALL_STATE_RING:
                    userAgent.refuseCall(sessionId, "" + reason);
                    break;
                case CommonConstantEntry.CALL_STATE_OUTGOING:
                case CommonConstantEntry.CALL_STATE_RING_BACK:
                    userAgent.cancelCall(sessionId, "" + reason);
                    break;

                default:
                    userAgent.hangup(sessionId, reason);
                    break;
            }
        }
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallHold(String sessionId) throws Exception
    {
        Log.info(TAG, "holdCall::callID:" + sessionId + " host:" + realm);
        userAgent.holdCall(sessionId);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int sCallRetrieve(String sessionId) throws Exception
    {
        Log.info(TAG, "retrieveCall::callID:" + sessionId + " host:" + realm);
        userAgent.retrieveCall(sessionId);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confRegEventListener(AclConfEventListener callback)
    {
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confCreate(String sessionId, int callPriority, int confType, boolean video) throws Exception
    {
        Log.info(TAG, "confCreate:: sessionId:" + sessionId + " priority:" + callPriority);

        if (userAgent == null)
        {
            Log.error(TAG, "userAgent is null");
            return CommonConstantEntry.RESPONSE_FAILED;
        }

        int callType = CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO;
        if (video)
            callType = CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO;
        boolean result = userAgent.call(sessionId, CommonConfigEntry.CONFCALLEE, callPriority, callType);
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confClose(String sessionId) throws Exception
    {
        Log.info(TAG, "confClose::sessionId:" + sessionId);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confClose:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            switch (call.getStatus())
            {
                case CommonConstantEntry.CALL_STATE_OUTGOING:
                case CommonConstantEntry.CALL_STATE_RING_BACK:
                    userAgent.cancelCall(sessionId, "");
                    break;

                default:
                    userAgent.closeCall(sessionId, "");
                    break;
            }
        }
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int confEnter(String sessionId) throws Exception
    {
        Log.info(TAG, "confEnter::sessionId:" + sessionId);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confEnter:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.tempJoinOrUnjoinConf(call, username + "@" + localIp, false);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confLeave(String sessionId) throws Exception
    {
        Log.info(TAG, "confLeave::sessionId:" + sessionId);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confLeave:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.tempJoinOrUnjoinConf(call, username + "@" + localIp, true);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confUserAdd(String sessionId, String userNum) throws Exception
    {
        Log.info(TAG, "confUserAdd::sessionId:" + sessionId);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confClose:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.addConfMember(call, username + "@" + localIp, userNum + "@" + realm);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confUserDelete(String sessionId, String userNum) throws Exception
    {
        Log.info(TAG, "confUserDelete::sessionId:" + sessionId + " userNum:" + userNum);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confUserDelete:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.removeConfMember(call, username + "@" + localIp, userNum + "@" + realm);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confUserAudioEnable(String sessionId, String userNum, boolean enable) throws Exception
    {
        Log.info(TAG, "confUserAudioEnable::sessionId:" + sessionId + " userNum:" + userNum);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confUserAudioEnable:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.controlMemberVoice(call, username + "@" + localIp, userNum + "@" + realm, enable);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confUserVideoEnable(String sessionId, String userNum, boolean enable) throws Exception
    {
        Log.info(TAG, "confUserVideoEnable::sessionId:" + sessionId + " userNum:" + userNum);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confUserVideoEnable:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.controlMemberVideoSwitch(call, username + "@" + localIp, userNum + "@" + realm, enable);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confUserVideoShare(String sessionId, String userNum, String tag, boolean enable) throws Exception
    {
        Log.info(TAG, "confUserVideoShare::sessionId:" + sessionId + " userNum:" + userNum);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confUserVideoShare:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.controlMemberVideoBroadcast(call, username + "@" + localIp, userNum + "@" + realm, tag, enable);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int confBgmEnable(String sessionId, boolean enable) throws Exception
    {
        Log.info(TAG, "confBgmEnable::sessionId:" + sessionId + " enable:" + enable);
        ExtendedCall call = userAgent.getCallCache().get(sessionId);
        boolean result = false;
        if (call == null)
        {// 没有呼叫
            Log.error(TAG, "confUserVideoShare:: call is null.");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        else
        {
            result = conference.meetToneInfo(call, username + "@" + localIp, enable);
        }
        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public int imRegEventListener(AclImEventListener callback)
    {
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int imSendMsg(String sessionId, int msgPriority, String callerNum, String callerName, String calleeNum, int msgType, String text, URI uri)
            throws Exception
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int presenceRegEventListener(AclPresenceEventListener callback)
    {
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int presenceSubscribe(String[] user, boolean on) throws Exception
    {
        Log.info(TAG, "presenceSubscribe:: User:" + user + " on:" + on);
        if (user == null)
            return CommonConstantEntry.RESPONSE_FAILED;

        boolean result = false;
        if (on)
            result = userstatus.addUserstatus(user);
        else
            result = userstatus.deleteUserstatus(user);

        if (result)
            return CommonConstantEntry.RESPONSE_SUCCESS;
        else
            return CommonConstantEntry.RESPONSE_FAILED;
    }

    @Override
    public boolean cancelAllSubscribe()
    {
        userstatus.clearUserstatus();
        return true;
    }

    @Override
    public int vsRegEventListener(AclVsEventListener callback)
    {
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int vsOpen(String sessionId, int priority, String videoNum) throws Exception
    {
        Log.info(TAG, "vsOpen::callId" + sessionId + " videoNum:" + videoNum + " priority:" + priority);
        if (userAgent == null)
        {
            Log.error(TAG, "userAgent is null");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        userAgent.call(sessionId, videoNum, priority, CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int vsClose(String sessionId) throws Exception
    {
        Log.info(TAG, "vsClose::callId" + sessionId);
        if (userAgent == null)
        {
            Log.error(TAG, "userAgent is null");
            return CommonConstantEntry.RESPONSE_FAILED;
        }
        userAgent.hangup(sessionId);
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public boolean startService() throws Exception
    {
        Log.info(TAG, "startService");
        // 启动服务：业务和注册
        if (userAgent != null)
        {
            try
            {
                userAgent.hangupAll();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }

        sip_provider.setDestAddr(realm_orig);
        sip_provider.setOrigAddr(from_url);
        sip_provider.setDestPort(serverPort);
        conference = new ConferenceHandler(this);// 会议消息处理
        userAgent = new UserAgent(sip_provider, this);
        userAgent.setSipEngine(this);
        userAgent.setConference(conference);

        if (regAgent != null)
        {
            try
            {
                regAgent.halt();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }
        regAgent = new RegisterAgent(sip_provider, this);
        userstatus = new UserstatusHandler(regAgent, this);// 用户状态订阅处理
        regAgent.setUserstatus(userstatus);

        listen();// 监听来呼
        register();
        return true;
    }

    @Override
    public boolean stopService() throws Exception
    {
        Log.info(TAG, "stopService");
        try
        {
            clearCall();// 清除呼叫
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        // 停止续约
        if (regAgent != null)
        {
            regAgent.stopRenewRegister();
        }
        return deregister();
    }

    @Override
    public void sCallSipInfoDTMFSend(String sessionId, char c) throws Exception
    {
        Log.info(TAG, "sendSipInfoDTMF:: c:" + c);
        userAgent.info(sessionId, c, 0);
    }

    @Override
    public void sCallInbandDTMFSend(String sessionId, char c) throws Exception
    {
        // TODO Auto-generated method stub
        Log.info(TAG, "sendInbandDTMF:: c:" + c);
    }

    @Override
    public void confSipInfoDTMFSend(String sessionId, char c) throws Exception
    {
        Log.info(TAG, "sendSipInfoDTMF:: c:" + c);
        userAgent.info(sessionId, c, 0);
    }

    @Override
    public void confInbandDTMFSend(String sessionId, char c) throws Exception
    {
        // TODO Auto-generated method stub
        Log.info(TAG, "sendInbandDTMF:: c:" + c);
    }

    @Override
    public void setNightService(boolean nightService) throws Exception
    {
        Log.info(TAG, "setNightService::nightService:" + nightService);
        NightServiceHandler handler = new NightServiceHandler(nightService, sip_provider, "sip-server@" + realm_orig, contact_url, this);
        eventHandler.subscribe(XmlMessage.NIGHTSERVICE_RESPONSE, handler);
        XmlMessage xmlMsg = XmlMessageFactory.createNightServiceMsg(username, nightService);
        try
        {
            handler.send(NightServiceHandler.CONTENT_TYPE_CCCP, "NightService-Request", xmlMsg);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public int deviceRegEventListener(AclDeviceEventListener callback)
    {
        return CommonConstantEntry.RESPONSE_SUCCESS;
    }

    @Override
    public int remoteCameraControl(String sessionId, String deviceNum, int command, int commandPara1, int commandPara2, int commandPara3)
    {
        try
        {
            Log.info(TAG, "cameraControl::deviceNum" + deviceNum + " command:" + command + " commandPara1:" + commandPara1 + " commandPara2:" + commandPara2
                    + " commandPara3:" + commandPara3);

            XmlMessage msg = XmlMessageFactory.createCameraControlMsg(contact_url, deviceNum + "@" + realm_orig, command + "", commandPara1 + "", commandPara2
                    + "" + "", commandPara3 + "");

            ExtendedCall call = userAgent.getCallCache().get(sessionId);
            if (call == null)
            {// 没有呼叫
                Log.error(TAG, "cameraControl:: call is null.");
                return CommonConstantEntry.RESPONSE_FAILED;
            }
            else
            {
                call.pushMsg(msg.toString(), deviceNum + "@" + realm_orig);
                return CommonConstantEntry.RESPONSE_SUCCESS;
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return CommonConstantEntry.RESPONSE_FAILED;
        }
    }

}
