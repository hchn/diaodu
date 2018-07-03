/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * 
 * This file is part of MjSip (http://www.mjsip.org)
 * 
 * MjSip is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * MjSip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 * Nitin Khanna, Hughes Systique Corp. (Reason: Android specific change,
 * optmization, bug fix)
 */

package org.zoolu.sip.call;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.dialog.InviteDialog;
import org.zoolu.sip.dialog.InviteDialogListener;
import org.zoolu.sip.header.MultipleHeader;
import org.zoolu.sip.header.ResourcePriorityHeader;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.SipResponses;
import org.zoolu.sip.provider.SipProvider;

import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;

/* HSC CHANGES START */
// import org.zoolu.sdp.*;
// import java.util.Vector;
/* HSC CHANGES END */

/**
 * Class Call implements SIP calls.
 * <p>
 * It handles both outgoing or incoming calls.
 * <p>
 * Both offer/answer models are supported, that is: <br>
 * i) offer/answer in invite/2xx, or <br>
 * ii) offer/answer in 2xx/ack
 */
public class Call implements InviteDialogListener
{
    /** Event logger. */
    Log log;

    /** The SipProvider used for the call */
    protected SipProvider sip_provider;

    /** The invite dialog (sip.dialog.InviteDialog) */
    protected InviteDialog dialog;

    /** The user url */
    protected String from_url;

    /** The user contact url */
    protected String contact_url;

    /** The local sdp */
    protected String local_sdp;

    /** The remote sdp */
    protected String remote_sdp;

    /** The call listener (sipx.call.CallListener) */
    CallListener listener;

    int call_state = CommonConstantEntry.CALL_STATE_IDLE;// 呼叫模式

    /** Creates a new Call. */
    final static int PRIORITY_DEFAULT_LEVEL = 4;

    int call_priority = PRIORITY_DEFAULT_LEVEL;
    // 是否外呼
    boolean callingPart = true;

    protected String callerNumber;// 主叫号码

    protected String callerFn;// 主叫功能码

    protected String calleeNumber;// 被叫号码，包括群组ID、MSISDN/ISDN、功能号码

    protected String activeNumber; // 当用户为组呼发起者时，用于呼叫中组方案中的组呼号码，增加了SA

    protected String createrFn;// 组呼发起者功能码

    protected boolean callHeartBeatRunning = true;// 呼叫心跳监测

    private Thread callHeartBeatThread = null;// 呼叫心跳监测线程

    long heartbeatTime = 0;

    private boolean isReceiveAck = false;// 是否接收到呼叫确认ACK消息

    private boolean isCallOkMonitor = true;// 呼叫建立成功监测

    private final static String TAG = "Call";

    protected int callType = CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO;// 呼叫类型

    private Thread checkCallOkThread = null;// 呼叫成功监测线程

    /** Creates a new Call. */
    public Call(SipProvider sip_provider, String from_url, String contact_url, CallListener call_listener)
    {
        this.sip_provider = sip_provider;

        this.listener = call_listener;
        this.from_url = from_url;
        this.contact_url = contact_url;
        this.dialog = null;
        this.local_sdp = null;
        this.remote_sdp = null;
    }

    /** Creates a new Call specifing the sdp */
    /*
     * public Call(SipProvider sip_provider, String from_url, String
     * contact_url, String sdp, CallListener call_listener) {
     * this.sip_provider=sip_provider; this.log=sip_provider.getLog();
     * this.listener=call_listener; this.from_url=from_url;
     * this.contact_url=contact_url; local_sdp=sdp; }
     */

    /** Gets the current invite dialog */
    /*
     * public InviteDialog getInviteDialog() { return dialog; }
     */

    /**
     * Changes the call state
     */
    public synchronized void changeStatus(int state)
    {
        call_state = state;
        if (state == CommonConstantEntry.CALL_STATE_IDLE)
        {// 空闲即挂断
            callHeartBeatRunning = false;// 停止呼叫心跳监测
            isCallOkMonitor = false;// 停止呼叫成功监测
        }
        else if (state == CommonConstantEntry.CALL_STATE_IN_CALL)
        {// 通话中
            isCallOkMonitor = false;// 停止呼叫成功监测
            checkCallHeartBeatTimeout();
        }
        else if (state == CommonConstantEntry.CALL_STATE_INCOMING || state == CommonConstantEntry.CALL_STATE_OUTGOING)
        {// 呼出或呼入
            checkCallIsOk();
        }
    }

    /**
     * 方法说明 :监测呼叫是否成功建立
     *
     * @author fuluo
     * @Date 2014-4-14
     */
    private void checkCallIsOk()
    {
        if (checkCallOkThread != null)
        {// 保证一个呼叫只启动一个呼叫成功监测线程
            return;
        }
        final Call call = this;
        checkCallOkThread = new Thread()
        {
            @Override
            public void run()
            {
                int times = 0;
                // FIXME：规避异常场景，单呼时如果存在前转，超时时间应该大于服务器单路通话超时时间90ms，此处设为100ms；
                // 组呼服务器超时时长为60ms，为了不应答时产生中组，此处设为50ms
                int timeout = (callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO) ? 100 : 50;
                while (isCallOkMonitor)
                {
                    try
                    {
                        Thread.sleep(1000);
                        if (call.getStatus() == CommonConstantEntry.CALL_STATE_IN_CALL)
                        {// 通话中
                            break;// 退出
                        }
                        times++;

                        if (times == timeout)
                        {
                            if (call.getStatus() != CommonConstantEntry.CALL_STATE_IN_CALL)
                            {// 不在通话中
                                Log.error(TAG, "onCallTimeout::Call Status:" + call.getStatus());
                                if (listener != null)
                                {
                                    // 来电超时未接的组呼需计入中组
                                    if (call_state == CommonConstantEntry.CALL_STATE_INCOMING)
                                    {
                                        if (timeout == 50)
                                        {// 组呼情况，终端提前10s超时，以拒绝形式
                                            refuse(CommonConstantEntry.Q850_NOREASON);// 拒绝
                                        }
//                                        ((UserAgent) listener).addActiveGroup((ExtendedCall) call, ServiceConstant.CALL_REASON_SEIZEFAILED);
                                    }
                                    listener.onCallTimeout(call);// 呼叫超时处理
                                }
                            }
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                        break;// 退出
                    }
                }
            }
        };
        checkCallOkThread.start();
    }

    /**
     * 监测呼叫心跳是否中断
     */
    private void checkCallHeartBeatTimeout()
    {
        if (callHeartBeatThread != null)
        {// 保证一个呼叫只启动一个心跳监测线程
            return;
        }
        final Call call = this;
        heartbeatTime = System.currentTimeMillis();

        callHeartBeatThread = new Thread()
        {
            @Override
            public void run()
            {
                int timeoutTime = 90000;// 呼叫心跳超时时间：90s
                int sleepTime = 10000;// 监测周期：10s
                callHeartBeatRunning = true;// 开始呼叫心跳监测
                while (callHeartBeatRunning)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                        if ((System.currentTimeMillis() - heartbeatTime) > timeoutTime)
                        {
                            Log.error(TAG, "onCallTimeout::CallHeartBeat Timeout.");
                            if (listener != null)
                                listener.onCallTimeout(call, CommonConstantEntry.CALL_END_HEARTBEAT_TIMEOUT);// 呼叫心跳超时处理
                            break;// 退出
                        }
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                        break;// 退出
                    }
                }
            }
        };
        callHeartBeatThread.start();
    }

    /**
     * 检测是否接收到呼叫建立ACK确认消息，如果没有则呼叫超时
     * 
     * 方法说明 :
     * @author fuluo
     * @Date 2014-2-28
     */
    public void checkIsReceiveAck()
    {
        final Call call = this;
        new Thread()
        {
            @Override
            public void run()
            {
                int times = 0;
                while (!isReceiveAck)
                {
                    try
                    {
                        Thread.sleep(1000);
                        if (times == 6)
                        {// 超过最长等待时间6秒
                            Log.error(TAG, "onCallTimeout::Not Receive Ack.");
                            if (listener != null)
                                listener.onCallTimeout(call);// 呼叫超时处理
                            break;// 退出
                        }
                        times++;
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                        break;// 退出
                    }
                }
            }
        }.start();
    }

    public void setRecieveAck()
    {
        isReceiveAck = true;
    }

    /**
     * Checks the call state
     */
    public synchronized boolean statusIs(int state)
    {
        return (call_state == state);
    }

    public synchronized int getStatus()
    {
        return call_state;
    }

    /** Gets the current local session descriptor */
    public String getLocalSessionDescriptor()
    {
        return local_sdp;
    }

    /** Sets a new local session descriptor */
    public void setLocalSessionDescriptor(String sdp)
    {
        local_sdp = sdp;
    }

    /** Gets the current remote session descriptor */
    public String getRemoteSessionDescriptor()
    {
        return remote_sdp;
    }

    /** Whether the call is on (active). */
    public boolean isOnCall()
    {
        Log.info(TAG, "isOnCall =" + dialog.isSessionActive());
        return dialog.isSessionActive();
    }

    public int getPriority()
    {
        return call_priority;
    }

    /** Waits for an incoming call */
    public void listen()
    {
        dialog = new InviteDialog(sip_provider, this);
        dialog.listen();
    }

    /** Starts a new call, inviting a remote user (<i>callee</i>) 
     * @throws Exception */
    public void call(String callId, String callee) throws Exception
    {
        call(callId, callee, null, null, null, null, PRIORITY_DEFAULT_LEVEL, null, false);
    }

    /** Starts a new call, inviting a remote user (<i>callee</i>) 
     * @throws Exception */
    public void call(String callId, String callee, String sdp, String icsi) throws Exception
    { // modified by mandrajg
        call(callId, callee, null, null, sdp, icsi, PRIORITY_DEFAULT_LEVEL, null, false);
    }

    /** Starts a new call, inviting a remote user (<i>callee</i>) 
     * @throws Exception */
    public boolean call(String callId, String callee, String from, String contact, String sdp, String icsi, int priority, String funNumber, boolean activeGroup)
            throws Exception
    { // modified by mandrajg
        Log.info(TAG, "call:: callId: " + callId + "callee:" + callee + " from:" + from + " contact:" + contact + " sdp:" + sdp + " priority:" + priority
                + " funNumber" + funNumber);

        if (from == null)
            from = from_url;
        if (contact == null)
            contact = contact_url;
        if (sdp != null)
            local_sdp = sdp;

        call_priority = priority;

        dialog = new InviteDialog(sip_provider, this);
        if (local_sdp != null)
            return dialog.invite(callee, from, contact, local_sdp, icsi, priority, funNumber, callId, activeGroup);
        else
            return dialog.inviteWithoutOffer(callee, from, contact, priority, funNumber, callId, activeGroup);
    }

    /** Starts a new call with the <i>invite</i> message request 
     * @throws Exception */
    public void call(Message invite) throws Exception
    {
        dialog = new InviteDialog(sip_provider, this);
        local_sdp = invite.getBody();
        if (local_sdp != null)
            dialog.invite(invite);
        else
            dialog.inviteWithoutOffer(invite);
    }

    /**
    * 从Invite消息中获取通话优先级，如果没有返回默认优先级：4。
    * @param invite 从SIP服务器收到的INVITE消息。
    * @return 显示给用户的主叫信息。
    */
    public static int getCallPriority(Message invite)
    {
        if (invite == null)
        {
            return PRIORITY_DEFAULT_LEVEL;
        }

        ResourcePriorityHeader header = invite.getResourcePriorityHeader();
        if (header != null)
        {
            int priority = header.getPriority();

            if (priority >= 0 && priority < 5)
            {
                return priority;
            }
        }

        return PRIORITY_DEFAULT_LEVEL;
    }

    /** Answers at the 2xx/offer (in the ack message) 
     * @throws Exception */
    public void ackWithAnswer(String sdp) throws Exception
    {
        local_sdp = sdp;
        dialog.ackWithAnswer(contact_url, sdp);
    }

    /** Rings back for the incoming call 
     * @throws Exception */
    public void ring(String sdp) throws Exception
    { // modified
        local_sdp = sdp;
        Log.info(TAG, "-----try ringing...");

        if (dialog != null)
            dialog.ring(sdp);
    }

    /** Respond to a incoming call (invite) with <i>resp</i> 
     * @throws Exception */
    public void respond(Message resp) throws Exception
    {
        if (dialog != null)
            dialog.respond(resp);
    }

    /** Accepts the incoming call */
    /*
     * public void accept() { accept(local_sdp); }
     */

    /** Accepts the incoming call 
     * @throws Exception */
    public void accept(String sdp) throws Exception
    {
        local_sdp = sdp;
        if (dialog != null)
            dialog.accept(contact_url, local_sdp);
    }

    public void acceptReInvite(String sdp) throws Exception
    {
        local_sdp = sdp;
        if (dialog != null)
            dialog.accept(contact_url, local_sdp);
    }

    /** Redirects the incoming call 
     * @throws Exception */
    public void redirect(String redirect_url) throws Exception
    {
        if (dialog != null)
            dialog.redirect(302, "Moved Temporarily", redirect_url);
    }

    /** Refuses the incoming call 
     * @throws Exception */
    public boolean refuse(int releaseReason) throws Exception
    {
        if (dialog == null)
        {
            return false;
        }

        if (call_state == CommonConstantEntry.CALL_STATE_INCOMING)
        {// 来电尚未接听 //添加抢占
         // 主叫A与被叫B正在通话中，此时C呼B，且级别更高，B将与A的呼叫结束。如果A、B的呼叫正在建立中(1XX)，B给A回486。
            if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
                return dialog.busy(releaseReason);
            else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
                return dialog.busy(releaseReason);// 抢占失败，主叫A与被叫B正在通话中，此时C呼A，但A-B通话级别更高，A给C回486：
            else if (CommonConstantEntry.SIP_CALL_DND == releaseReason)
                return dialog.refuse(480, SipResponses.reasonOf(480), releaseReason);// 免打扰模式
            else
                return dialog.refuse();
        }
        return false;
    }

    /** Cancels the outgoing call 
     * @throws Exception */
    public boolean cancel(int releaseReason) throws Exception
    {
        if (dialog == null)
        {
            return false;
        }

        Log.info(TAG, "callstatus: " + getStatus() + " dialog.isConfirmed():" + dialog.isConfirmed());
        if (call_state == CommonConstantEntry.CALL_STATE_OUTGOING || call_state == CommonConstantEntry.CALL_STATE_RING_BACK)
        {// 去电尚未接听 //添加抢占
         // 主叫A与被叫B正在通话中，此时C呼A，且级别更高，A将与B的呼叫结束。如果A、B的呼叫正在建立中(1XX)，A给B发CANCEL。
            if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
            {
                return dialog.cancel(releaseReason);
            }
            else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
            {
                // 抢占失败，主叫A与被叫B正在通话中，此时C呼A，但A-B通话级别更高，A给C回486：
                return dialog.busy(releaseReason);
            }
            else if (dialog.isConfirmed())
            {
                Log.info(TAG, "cancel but call is confirmed, so bye");
                return dialog.bye(releaseReason);
            }
            else
            {
                return dialog.cancel();
            }
        }
        return false;
    }

    /** Close the ongoing call 
     * @throws Exception */
    public boolean bye(int releaseReason) throws Exception
    {
        if (dialog == null)
        {
            return false;
        }
        if (call_state == CommonConstantEntry.CALL_STATE_IN_CALL || call_state == CommonConstantEntry.CALL_STATE_HOLD
                || call_state == CommonConstantEntry.CALL_STATE_HOLDED)
        {// 通话过程中，或保持状态
         // 添加抢占 c) 如果A、B的呼叫已建立，给对方发BYE
            if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
                return dialog.bye(releaseReason);
            else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
                return dialog.busy(releaseReason);// d)
                                                  // 抢占失败，主叫A与被叫B正在通话中，此时C呼A，但A-B通话级别更高，A给C回486：
            else
                return dialog.bye();
        }
        return false;
    }

    /** Modify the current call 
     * @throws Exception */
    public void modify(String contact, String sdp) throws Exception
    {
        local_sdp = sdp;
        if (dialog != null)
            dialog.reInvite(contact, local_sdp);
    }

    /**
     * Closes an ongoing or incoming/outgoing call
     * <p>
     * It trys to fires refuse(), cancel(), and bye() methods
     * @throws Exception 
     */
    public void hangup() throws Exception
    {
        hangup(CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * Closes an ongoing or incoming/outgoing call
     * <p>
     * It trys to fires refuse(), cancel(), and bye() methods
     * @throws Exception 
     */
    public boolean hangup(int releaseReason) throws Exception
    {
        Log.error(TAG, "hangup:: call_state:" + call_state);

        if (dialog != null)
        { // try dialog.refuse(), cancel(), and bye()
          // methods..
            bye(releaseReason);
            refuse(releaseReason);
            cancel(releaseReason);
            return true;
        }
        return false;
    }

    /**
     * 退出组呼
     * @throws Exception 
     */
    public boolean quitcall() throws Exception
    {
        return quitcall(CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * 退出组呼
     * @throws Exception 
     */
    public boolean quitcall(int releaseReason) throws Exception
    {
        Log.info(TAG, "quitcall");

        if (dialog == null)
        {
            return false;
        }
        return dialog.quit(releaseReason);
    }

    public void busy() throws Exception
    {
        if (dialog != null)
            dialog.busy(); // modified
    }

    // ************** Inherited from InviteDialogListener **************

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallIncoming()).
     * @throws Exception 
     */
    public void onDlgInvite(InviteDialog d, NameAddress callee, NameAddress caller, String sdp, Message msg) throws Exception
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgInvite:: NOT the current dialog");
            return;
        }
        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;

        call_priority = getCallPriority(msg);
        callingPart = false;

        Log.info(TAG, "onDlgInvite:: number:" + caller.getDisplayName() + " priority:" + call_priority);

        if (listener != null)
            listener.onCallIncoming(this, callee, caller, sdp, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallModifying()).
     * @throws Exception 
     */
    public void onDlgReInvite(InviteDialog d, String sdp, Message msg) throws Exception
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgReInvite:: NOT the current dialog");
            return;
        }
        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;
        if (listener != null)
            listener.onCallModifying(this, sdp, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallRinging()).
     */
    public void onDlgInviteProvisionalResponse(InviteDialog d, int code, String reason, String sdp, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgInviteProvisionalResponse:: NOT the current dialog");
            return;
        }
        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;
        if (code == 180 || code == 183)
        { // modified
            if (listener != null)
                listener.onCallRinging(this, msg);

            call_priority = getCallPriority(msg);// 设置呼叫优先级
        }
        else if (code == 100)
        {
            // --------------------------------呼叫发起性能测试日志记录
            // Begin-------------------------------------------
            if (CommonConfigEntry.TEST_CALL)
            {
                PerfTestHelper.CALLPREF.setReceive100Trying(System.currentTimeMillis());
            }
            // --------------------------------呼叫发起性能测试日志记录
            // End----------------------------------------------
        }
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallAccepted()).
     */
    public void onDlgInviteSuccessResponse(InviteDialog d, int code, String reason, String sdp, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgInviteSuccessResponse:: NOT the current dialog");
            return;
        }
        if (statusIs(CommonConstantEntry.CALL_STATE_IDLE))// 20121031 Added by
        // zhoujy
        {
            return;
        }
        // When the outgoing call is VGCS, No 180 Ring Message is Received, Only
        // 200 OK message. So get the priority from here.
        call_priority = getCallPriority(msg);

        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;
        if (listener != null)
            listener.onCallAccepted(this, sdp, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallRedirection()).
     * @throws Exception 
     */
    public void onDlgInviteRedirectResponse(InviteDialog d, int code, String reason, MultipleHeader contacts, Message msg) throws Exception
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgInviteRedirectResponse:: NOT the current dialog");
            return;
        }
        if (listener != null)
            listener.onCallRedirection(this, reason, contacts.getValues(), msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallRefused()).
     */
    public void onDlgInviteFailureResponse(InviteDialog d, int code, String reason, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgInviteFailureResponse:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallRefused(this, reason, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallTimeout()).
     */
    public void onDlgTimeout(InviteDialog d)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgTimeout:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallTimeout(this);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it.
     */
    public void onDlgReInviteProvisionalResponse(InviteDialog d, int code, String reason, String sdp, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgReInviteProvisionalResponse:: NOT the current dialog");
            return;
        }
        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallReInviteAccepted()).
     */
    public void onDlgReInviteSuccessResponse(InviteDialog d, int code, String reason, String sdp, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgReInviteSuccessResponse:: NOT the current dialog");
            return;
        }

        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;
        if (listener != null)
            listener.onCallReInviteAccepted(this, sdp, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallReInviteRedirection()).
     */
    // public void onDlgReInviteRedirectResponse(InviteDialog d, int code,
    // String reason, MultipleHeader contacts, Message msg)
    // { if (d!=dialog) { printLog("NOT the current dialog",LogLevel.HIGH);
    // return; }
    // if (listener!=null)
    // listener.onCallReInviteRedirection(this,reason,contacts.getValues(),msg);
    // }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallReInviteRefused()).
     */
    public void onDlgReInviteFailureResponse(InviteDialog d, int code, String reason, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgReInviteFailureResponse:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallReInviteRefused(this, reason, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallReInviteTimeout()).
     */
    public void onDlgReInviteTimeout(InviteDialog d)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgReInviteTimeout:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallReInviteTimeout(this);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallConfirmed()).
     */
    public void onDlgAck(InviteDialog d, String sdp, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgAck:: NOT the current dialog");
            return;
        }
        if (sdp != null && sdp.length() != 0)
            remote_sdp = sdp;
        if (listener != null)
            listener.onCallConfirmed(this, sdp, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onCallClosing()).
     */
    public void onDlgCancel(InviteDialog d, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgCancel:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallCanceling(this, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onClosing()).
     */
    public void onDlgBye(InviteDialog d, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgBye:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallClosing(this, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onClosed()).
     */
    public void onDlgByeFailureResponse(InviteDialog d, int code, String reason, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgByeFailureResponse:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallClosed(this, msg);
    }

    /**
     * Inherited from class InviteDialogListener and called by an InviteDialag.
     * Normally you should not use it. Use specific callback methods instead
     * (e.g. onClosed()).
     */
    public void onDlgByeSuccessResponse(InviteDialog d, int code, String reason, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgByeSuccessResponse:: NOT the current dialog");
            return;
        }

        if (listener != null)
            listener.onCallClosed(this, msg);
    }

    // -----------------------------------------------------

    /** When an incoming INVITE is accepted */
    // public void onDlgAccepted(InviteDialog dialog) {}
    /** When an incoming INVITE is refused */
    // public void onDlgRefused(InviteDialog dialog) {}
    /** When the INVITE handshake is successful terminated 
     * @throws Exception */
    public void onDlgCall(InviteDialog dialog) throws Exception
    {
        Log.info(TAG, "onDlgCall::status:" + call_state);
        ExtendedCall extendedCall = (ExtendedCall) this;
        if (call_state == CommonConstantEntry.CALL_STATE_IDLE)
        {// 取消已经将状态改为结束，但是呼叫已经成功建立
            Log.info(TAG, "onDlgCall::call.bye:: callid:" + extendedCall.getCallId() + " status:" + call_state);
            extendedCall.changeStatus(CommonConstantEntry.CALL_STATE_IN_CALL);
            extendedCall.bye(CommonConstantEntry.Q850_NOREASON);// 结束
        }
    }

    /** When an incoming Re-INVITE is accepted */
    // public void onDlgReInviteAccepted(InviteDialog dialog) {}
    /** When an incoming Re-INVITE is refused */
    // public void onDlgReInviteRefused(InviteDialog dialog) {}
    /** When a BYE request traqnsaction has been started */
    // public void onDlgByeing(InviteDialog dialog) {}
    /** When the dialog is finally closed */
    public void onDlgClose(InviteDialog dialog)
    {
    }

    @Override
    public void onDlgHeartbeat(Message msg)
    {
        if (listener != null)
            listener.onHeartbeatResponse(msg);// 服务器响应终端心跳
    }

    public String getCallerNumber()
    {
        return callerNumber;
    }

    public void setCallerNumber(String callerNumber)
    {
        this.callerNumber = callerNumber;
    }

    public String getCallerFn()
    {
        return callerFn;
    }

    public void setCallerFn(String fn)
    {
        this.callerFn = fn;
    }

    public String getCalleeNumber()
    {
        return calleeNumber;
    }

    public void setCalleeNumber(String calleeNumber)
    {
        this.calleeNumber = calleeNumber;
    }

    public String getActiveNumber()
    {
        return activeNumber;
    }

    public void setActiveNumber(String activeNumber)
    {
        this.activeNumber = activeNumber;
    }

    public String getCreaterFn()
    {
        return createrFn;
    }

    public void setCreaterFn(String createrFn)
    {
        this.createrFn = createrFn;
    }

    public int getCallType()
    {
        return callType;
    }

    public void setCallType(int callType)
    {
        this.callType = callType;
    }
}
