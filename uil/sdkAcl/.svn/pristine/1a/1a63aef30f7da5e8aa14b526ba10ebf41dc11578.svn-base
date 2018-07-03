/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
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

package com.jiaxun.sdk.acl.line.sip.ua;

import java.util.Vector;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.authentication.DigestAuthentication;
import org.zoolu.sip.dialog.SubscriberDialog;
import org.zoolu.sip.dialog.SubscriberDialogListener;
import org.zoolu.sip.header.AuthorizationHeader;
import org.zoolu.sip.header.CallIdHeader;
import org.zoolu.sip.header.ContactHeader;
import org.zoolu.sip.header.ExpiresHeader;
import org.zoolu.sip.header.Header;
import org.zoolu.sip.header.PJxActiveNetWorkIdHeader;
import org.zoolu.sip.header.ProxyAuthenticateHeader;
import org.zoolu.sip.header.ProxyAuthorizationHeader;
import org.zoolu.sip.header.RefreshFromHeader;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.header.UserAgentHeader;
import org.zoolu.sip.header.ViaHeader;
import org.zoolu.sip.header.WwwAuthenticateHeader;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.sip.provider.TransactionIdentifier;
import org.zoolu.sip.transaction.TransactionClient;
import org.zoolu.sip.transaction.TransactionClientListener;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.acl.line.sip.service.userstatus.UserstatusHandler;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * Register User Agent. It registers (one time or periodically) a contact
 * address with a registrar server.
 */
public class RegisterAgent implements TransactionClientListener, SubscriberDialogListener
{
    /**
     * Max number of registration attempts.
     */
    static final int MAX_ATTEMPTS = 3;
    /* States for the RegisterAgent Module */
    public static final int UNDEFINED = 0;
    public static final int UNREGISTERED = 1;
    public static final int REGISTERING = 2;
    public static final int REGISTERED = 3;
    public static final int DEREGISTERING = 4;

    /**
     * SipProvider 
     */
    SipProvider sip_provider;

    /**
     * User's URI with the fully qualified domain name of the registrar server.
     */
    NameAddress target;

    /**
     * Q value for this registration (added by mandrajg)
     */
    String qvalue;

    /**
     * IMS Communication Service Identifier for this registration (currently only one supported)(added by mandrajg)
     */
    String icsi;

    /**
     * call-id.
     */
    String call_id;
    /**
     * Nonce for the next authentication.
     */
    String next_nonce;

    /**
     * Qop for the next authentication.
     */
    String qop;

    /**
     * User's contact address.
     */
    NameAddress contact;

    /**
     * Expiration time.
     */
    int expire_time;

    /**
     * Whether keep on registering.
     */
    boolean loop;

    /**
     * Current State of the registrar component
     */
    public int currentState;
    private boolean isRegistered;

    SubscriberDialog sd;
    TransactionClient t;

    Message currentSubscribeMessage;
    public int SUBSCRIPTION_EXPIRES = 60;

    String lastLocalIp;// 网络切换时(IP改变)前的IP地址

    public long lastRegisterTime;// 上次注册时间

    private long lastRegisterSuccTime;// 上次注册成功时间

    private int regsterTimeout = 26000;// 注册消息重传周期，单位：ms

    private static String TAG = "RegisterAgent";

    private RenewRegister renewRegister;// 续约定时器

    private SipAdapter mSipAdapter;

    private UserstatusHandler userstatus;// 用户状态订阅处理
    private RenewSubscriber renewSubscriber;// 续订定时器

    /**
     * Creates a new RegisterAgent with authentication credentials (i.e.
     * username, realm, and passwd).
     */
    public RegisterAgent(SipProvider sip_provider, SipAdapter sipAdapter)
    {
        this.sip_provider = sip_provider;
        this.mSipAdapter = sipAdapter;
        // use the from_url as the target_url.
        this.target = new NameAddress(mSipAdapter.username, new SipURL(mSipAdapter.from_url));
        this.contact = new NameAddress(mSipAdapter.contact_url);
        this.expire_time = SipStack.default_expires;

        // authentication
        this.next_nonce = null;
        this.qop = null;

        // IMS specifics (added by mandrajg)
        this.qvalue = mSipAdapter.qvalue;
        if (mSipAdapter.mmtel)
        {
            this.icsi = "\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\"";
        }

        sd = new SubscriberDialog(sip_provider, "presence", "", this);
        sip_provider.addSipProviderListener(new TransactionIdentifier(SipMethods.NOTIFY), sd);

        renewRegister = new RenewRegister(this);
        renewRegister.monitorRegister();// 启动续约（注册）监听
        isRegistered = false;

        renewSubscriber = new RenewSubscriber(this);
        renewSubscriber.monitorSubscriber();// 启动续订监听
    }

    /**
     * 方法说明 : 停止续约
     * @return void
     * @author hubin
     * @Date 2015-4-22
     */
    public void stopRenewRegister()
    {
        Log.info(TAG, "stopRenewRegister");
        if (renewRegister != null)
        {
            renewRegister.setRenewRegister(false);
        }
    }

    public void halt()
    {
        Log.info(TAG, "halt");
        sip_provider.removeSipProviderListener(new TransactionIdentifier(SipMethods.NOTIFY));
        call_id = null;
        isRegistered = false;
        if (t != null)
        {
            t.terminate();
        }
        if (renewRegister != null)
            renewRegister.setRenewRegister(false);
        if (renewSubscriber != null)
            renewSubscriber.setRenewSubscriber(false);
    }

    /**
     * Whether it is periodically registering.
     */
    public boolean isRegistered()
    {
        return isRegistered;
    }

    /**
     * Registers with the registrar server.
     * 
     * @param lastLocalIp
     *               原本地IP地址，在网络切换且本地IP地址改变时传入
     * @throws Exception 
     */
    public boolean register(String lastLocalIp) throws Exception
    {
        return register(expire_time, lastLocalIp);
    }

    /**
     * Registers with the registrar server for <i>expire_time</i> seconds.
     * 
     * @param lastLocalIp
     *               原本地IP地址，在网络切换且本地IP地址改变时传入
     * @throws Exception 
     */
    private synchronized boolean register(int expire_time, String lastLocalIp) throws Exception
    {
        this.lastLocalIp = lastLocalIp;
        Log.info(TAG, "register::expire_time:" + expire_time + ", lastHost:" + lastLocalIp);
        if (expire_time > 0)
        {
            this.expire_time = expire_time;
            currentState = REGISTERING;
        }
        else
        {
            expire_time = 0;
            currentState = DEREGISTERING;
        }

        // Create register message
        Message req = MessageFactory.createRegisterRequest(sip_provider, target, target, new NameAddress(mSipAdapter.contact_url), qvalue, icsi);
        // add User-Agent header field
        if (SipStack.ua_info != null)
            req.setUserAgentHeader(new UserAgentHeader(SipStack.ua_info));
        req.setExpiresHeader(new ExpiresHeader(String.valueOf(expire_time)));

        if (lastLocalIp != null && !lastLocalIp.equals(""))
        {// 网络切换且本地IP地址改变
            req.addHeader(new RefreshFromHeader(lastLocalIp), false);
        }

        Log.info(TAG, "register::call_id:" + call_id + ", isRegistered:" + isRegistered);
        // use the same call-d during the entire life-cycle.
        if (call_id == null || !isRegistered)
        {// 第一次注册或重新注册时，重新为call_id赋值
            call_id = sip_provider.pickCallId();
//            call_id = sip_provider.pickRegCallId(username);
        }
        req.setCallIdHeader(new CallIdHeader(call_id));

        // create and fill the authentication params this is done when
        // the UA has been challenged by the registrar or intermediate UA
        if (next_nonce != null)
        {
            AuthorizationHeader ah = new AuthorizationHeader("Digest");

            ah.addUsernameParam(mSipAdapter.username);
            ah.addRealmParam(mSipAdapter.realm);
            ah.addNonceParam(next_nonce);
            ah.addUriParam(req.getRequestLine().getAddress().toString());
            ah.addQopParam(qop);
            String response = (new DigestAuthentication(SipMethods.REGISTER, ah, null, mSipAdapter.passwd)).getResponse();
            ah.addResponseParam(response);
            req.setAuthorizationHeader(ah);
        }

        if (expire_time > 0)
        {
            lastRegisterTime = System.currentTimeMillis();
            Log.info(TAG, "register::Registering contact " + contact + " (it expires in " + expire_time + " secs)");
        }
        else
        {
            Log.error(TAG, "register::Unregistering contact " + contact);
        }

        if (t != null)
        {
            t.terminate();
        }// 20121115

        t = new TransactionClient(sip_provider, req, this, regsterTimeout);
        t.request();

        return true;
    }

    /**
     * Deregister with the registrar server
     * @throws Exception 
     */
    public boolean deregister() throws Exception
    {
        return register(0, null);
    }

    /**
     * 订阅消息
     */
    public boolean surbscriber(String body)
    {
        try
        {
            Log.info(TAG, "surbscriber::");

            sd.subscribe(getSubscribeMessage( body));
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    Message getSubscribeMessage(String body)
    {
        String empty = null;
        Message req = null;
        
        if(currentSubscribeMessage == null)
        {
            currentSubscribeMessage = MessageFactory.createSubscribeRequest(sip_provider, target.getAddress(), target, target, new NameAddress(mSipAdapter.contact_url),
                    sd.getEvent(), sd.getId(), empty, empty);
            req = currentSubscribeMessage;
        }
        else
        {
            req = currentSubscribeMessage;
            req.setCSeqHeader(req.getCSeqHeader().incSequenceNumber());
        }
        req.setExpiresHeader(new ExpiresHeader(SUBSCRIPTION_EXPIRES));
//        req.setHeader(new AcceptHeader("application/simple-message-summary"));
        req.setBody("application/psap+xml", body);
        currentSubscribeMessage = req;

        return req;
    }

    // **************** Subscription callback functions *****************
    public void onDlgSubscriptionSuccess(SubscriberDialog dialog, int code, String reason, Message resp)
    {
        Log.info(TAG, "onDlgSubscriptionSuccess");
        
        int expires;
        if (resp.hasExpiresHeader())
        {
            expires = resp.getExpiresHeader().getDeltaSeconds();
            if (0 == expires)
            {
                // 服务器停止订阅，终端停止订阅
                renewSubscriber.setRenewSubscriber(false);                
                return;
            }
            else
            {
                SUBSCRIPTION_EXPIRES = expires;
            }
        }
        else
        {
            expires = SUBSCRIPTION_EXPIRES;
        }
        
        renewSubscriber.schedule((SUBSCRIPTION_EXPIRES - 10) * 1000);// 启动下次续订
    }

    public void onDlgSubscriptionFailure(SubscriberDialog dialog, int code, String reason, Message resp)
    {
        Log.error(TAG, "onDlgSubscriptionFailure");
        
        renewSubscriber.schedule((SUBSCRIPTION_EXPIRES - 10) * 1000);// 启动下次续订
    }

    public void onDlgSubscribeTimeout(SubscriberDialog dialog)
    {
        Log.error(TAG, "onDlgSubscribeTimeout");
        
        renewSubscriber.schedule((SUBSCRIPTION_EXPIRES - 10) * 1000);// 启动下次续订
    }

    public void onDlgSubscriptionTerminated(SubscriberDialog dialog)
    {
        Log.error(TAG, "onDlgSubscriptionTerminated");

        surbscriber(null);// 启动续订
    }

    public void onDlgNotify(SubscriberDialog dialog, NameAddress target, NameAddress notifier, NameAddress contact, String state, String content_type,
            String body, Message msg)
    {
        Log.info(TAG, "onDlgNotify");

        if (body != null)
            userstatus.onUserstatusNotifyReceive(body);// 用户状态处理
    }

    // **************** Transaction callback functions *****************

    /** Callback function called when client sends back a failure response. */

    /**
     * Callback function called when client sends back a provisional response.
     */
    public void onTransProvisionalResponse(TransactionClient transaction, Message resp)
    { // do nothing..
    }

    /**
     * Callback function called when client sends back a success response.
     */
    public void onTransSuccessResponse(TransactionClient transaction, Message resp)
    {
        if (transaction.getTransactionMethod().equals(SipMethods.REGISTER))
        {
            if (resp.hasAuthenticationInfoHeader())
            {
                next_nonce = resp.getAuthenticationInfoHeader().getNextnonceParam();
            }

            StatusLine status = resp.getStatusLine();
            String result = status.getCode() + " " + status.getReason();

            int expires = 0;
            if (resp.hasExpiresHeader())
            {
                expires = resp.getExpiresHeader().getDeltaSeconds();
            }
            else if (resp.hasContactHeader())
            {
                Vector<Header> contacts = resp.getContacts().getHeaders();
                for (int i = 0; i < contacts.size(); i++)
                {
                    int exp_i = (new ContactHeader(contacts.elementAt(i))).getExpires();
                    if (exp_i > 0 && (expires == 0 || exp_i < expires))
                        expires = exp_i;
                }
            }

            Log.info(TAG, "onTransSuccessResponse::result:" + result + " currentState:" + currentState + " host:" + mSipAdapter.realm + " expires:" + expires);

            if (currentState == REGISTERING && expires > 0)
            {// 注册
                currentState = REGISTERED;// 已注册
                isRegistered = true;// 已注册

                if (expires >= CommonConfigEntry.MIN_RENEW_TIMES)
                {// 超出默认续约时间
                 // 在续约时间范围内续约
                    int reg_times = expires * 1000;
                    renewRegister.schedule(reg_times);
                    if (expires < 30)
                    {// 小于30s
                        regsterTimeout = 6000;// 设置短注册消息超时时间，最小支持10s续约周期
                    }
                    else
                    {
                        regsterTimeout = 26000;
                    }
                }

                // 记录注册成功
                Log.info(TAG, "register ok. user_name:" + mSipAdapter.username + " host:" + mSipAdapter.realm);

                // 双中心网络标识
                PJxActiveNetWorkIdHeader pJxActiveNetWorkIdHeader = resp.getPJxActiveNetWorkIdHeader();
                if (pJxActiveNetWorkIdHeader != null && mSipAdapter.realm.equals(pJxActiveNetWorkIdHeader.getPJxActiveNetWorkId()))
                {// 存在
                    Log.info(TAG, "result:" + result + " server:" + mSipAdapter.realm + " Active server::" + pJxActiveNetWorkIdHeader.getPJxActiveNetWorkId());
                    mSipAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_ACTIVE;
                }
                else
                {
                    Log.info(TAG, "result:" + result + " server:" + mSipAdapter.realm);
                    mSipAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_STANDBY;
                }
                LineManager.getInstance().onRegisterStateChanged();
                lastRegisterSuccTime = System.currentTimeMillis();
            }
            else if (currentState == DEREGISTERING  && expires == 0)
            {// 注销
                dealUnRegisterSuccess();
            }
        }
    }

    /**
     * Callback function called when client sends back a failure response.
     */
    public void onTransFailureResponse(TransactionClient transaction, Message resp)
    {
        if (transaction.getTransactionMethod().equals(SipMethods.REGISTER))
        {
            StatusLine status = resp.getStatusLine();
            int code = status.getCode();
            try
            {
                if (!processAuthenticationResponse(transaction, resp, code))
                {// 认证失败
                    // get the expires time
                    int expires = 0;
                    if (resp.hasExpiresHeader())
                    {
                        expires = resp.getExpiresHeader().getDeltaSeconds();
                    }
                    else if (resp.hasContactHeader())
                    {
                        Vector<Header> contacts = resp.getContacts().getHeaders();
                        for (int i = 0; i < contacts.size(); i++)
                        {
                            int exp_i = (new ContactHeader(contacts.elementAt(i))).getExpires();
                            if (exp_i > 0 && (expires == 0 || exp_i < expires))
                                expires = exp_i;
                        }
                    }

                    Log.info(TAG, "onTransFailureResponse::result:" + code);

                    dealTransFailure(code);// 处理注册流程交互失败
                }
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
    }

    /**
     * 处理注册流程交互失败
     */
    private void dealTransFailure(int result)
    {
        // Since the transactions are atomic, we rollback to the
        // previous state
        if (currentState == REGISTERING)
        {// 注册
            currentState = UNREGISTERED;// 注册失败
            isRegistered = false;// 注册失败
            // 恢复默认值
            renewRegister.schedule(CommonConfigEntry.DEFAULT_RENEW_TIMES);
            // 记录注册失败
            Log.info(TAG, "register fail. user_name:" + mSipAdapter.username + " host:" + mSipAdapter.realm + " result:" + result);
            switch (result)
            {
                case 403:
                    mSipAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_FORBIDUPLINE;
                    break;
                case 404:
                    mSipAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_FORBIDUSER;
                    break;
                default:
                    mSipAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_OFFLINE;
                    break;
            }
            LineManager.getInstance().onRegisterStateChanged();
        }
        else
        {// 注销
            dealUnRegisterSuccess();
        }
    }

    /**
     * 处理注销成功
     */
    private void dealUnRegisterSuccess()
    {
        currentState = UNREGISTERED;// 注册失败
        isRegistered = false;// 注册失败

        // 记录注销成功
        Log.info(TAG, "unregister ok. user_name:" + mSipAdapter.username + " host:" + mSipAdapter.realm);
        mSipAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_OFFLINE;
        LineManager.getInstance().onRegisterStateChanged();
    }

    /**
     * 407:需要认证：需要密码认证的主叫
     */
    private boolean generateRequestWithProxyAuthorizationHeader(Message resp, Message req)
    {
        if (resp.hasProxyAuthenticateHeader() && resp.getProxyAuthenticateHeader().getRealmParam().length() > 0)
        {
            ProxyAuthenticateHeader pah = resp.getProxyAuthenticateHeader();
            String qop_options = pah.getQopOptionsParam();

            Log.info("RegisterAgent.generateRequestWithProxyAuthorizationHeader", "info: qop-options: " + qop_options);

            qop = (qop_options != null) ? "auth" : null;

            ProxyAuthorizationHeader ah = (new DigestAuthentication(req.getTransactionMethod(), req.getRequestLine().getAddress().toString(), pah, qop, null,
                    mSipAdapter.username, mSipAdapter.passwd)).getProxyAuthorizationHeader();
            req.setProxyAuthorizationHeader(ah);

            return true;
        }
        return false;
    }

    /**
     * 401:需要认证：注册需要用户名密码认证
     */
    private boolean generateRequestWithWwwAuthorizationHeader(Message resp, Message req)
    {
        if (resp.hasWwwAuthenticateHeader() && resp.getWwwAuthenticateHeader().getRealmParam().length() > 0)
        {
            WwwAuthenticateHeader wah = resp.getWwwAuthenticateHeader();
            String qop_options = wah.getQopOptionsParam();

            Log.info("RegisterAgent.generateRequestWithWwwAuthorizationHeader", "info: qop-options: " + qop_options);

            qop = (qop_options != null) ? "auth" : null;

            AuthorizationHeader ah = (new DigestAuthentication(req.getTransactionMethod(), req.getRequestLine().getAddress().toString(), wah, qop, null,
                    mSipAdapter.username, mSipAdapter.passwd)).getAuthorizationHeader();
            req.setAuthorizationHeader(ah);
            return true;
        }
        return false;
    }

    /**
     * 根据返回码处理
     */
    private boolean handleAuthentication(int respCode, Message resp, Message req)
    {
        switch (respCode)
        {
            case 407:
                return generateRequestWithProxyAuthorizationHeader(resp, req);
            case 401:
                return generateRequestWithWwwAuthorizationHeader(resp, req);
        }
        return false;
    }

    /**
     * 处理注册账号认证消息
     * @throws Exception 
     */
    private boolean processAuthenticationResponse(TransactionClient transaction, Message resp, int respCode) throws Exception
    {
        Message req = transaction.getRequestMessage();
        req.setCSeqHeader(req.getCSeqHeader().incSequenceNumber());
        ViaHeader vh = req.getViaHeader();
        String newbranch = SipProvider.pickBranch();
        vh.setBranch(newbranch);
        req.removeViaHeader();
        req.addViaHeader(vh);

        if (handleAuthentication(respCode, resp, req))
        {// 认证
            if (t != null)
            {
                t.terminate();
            }// 20121115
            t = new TransactionClient(sip_provider, req, this, regsterTimeout);
            t.request();
            return true;
        }
        return false;
    }

    /**
     * Callback function called when client expires timeout.
     */
    public void onTransTimeout(TransactionClient transaction)
    {
        if (transaction == null)
            return;
        if (transaction.getTransactionMethod().equals(SipMethods.REGISTER))
        {
            Log.error("RegisterAgent", "onTransTimeout");

            dealTransFailure(CommonConstantEntry.SERVICE_STATUS_TIMEOUT);// 处理注册流程交互失败
        }
    }

    public void setUserstatus(UserstatusHandler userstatus)
    {
        this.userstatus = userstatus;
    }

}
