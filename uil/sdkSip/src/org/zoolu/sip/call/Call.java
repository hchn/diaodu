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

    int call_state = CommonConstantEntry.CALL_STATE_IDLE;// ����ģʽ

    /** Creates a new Call. */
    final static int PRIORITY_DEFAULT_LEVEL = 4;

    int call_priority = PRIORITY_DEFAULT_LEVEL;
    // �Ƿ����
    boolean callingPart = true;

    protected String callerNumber;// ���к���

    protected String callerFn;// ���й�����

    protected String calleeNumber;// ���к��룬����Ⱥ��ID��MSISDN/ISDN�����ܺ���

    protected String activeNumber; // ���û�Ϊ���������ʱ�����ں������鷽���е�������룬������SA

    protected String createrFn;// ��������߹�����

    protected boolean callHeartBeatRunning = true;// �����������

    private Thread callHeartBeatThread = null;// ������������߳�

    long heartbeatTime = 0;

    private boolean isReceiveAck = false;// �Ƿ���յ�����ȷ��ACK��Ϣ

    private boolean isCallOkMonitor = true;// ���н����ɹ����

    private final static String TAG = "Call";

    protected int callType = CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO;// ��������

    private Thread checkCallOkThread = null;// ���гɹ�����߳�

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
        {// ���м��Ҷ�
            callHeartBeatRunning = false;// ֹͣ�����������
            isCallOkMonitor = false;// ֹͣ���гɹ����
        }
        else if (state == CommonConstantEntry.CALL_STATE_IN_CALL)
        {// ͨ����
            isCallOkMonitor = false;// ֹͣ���гɹ����
            checkCallHeartBeatTimeout();
        }
        else if (state == CommonConstantEntry.CALL_STATE_INCOMING || state == CommonConstantEntry.CALL_STATE_OUTGOING)
        {// ���������
            checkCallIsOk();
        }
    }

    /**
     * ����˵�� :�������Ƿ�ɹ�����
     *
     * @author fuluo
     * @Date 2014-4-14
     */
    private void checkCallIsOk()
    {
        if (checkCallOkThread != null)
        {// ��֤һ������ֻ����һ�����гɹ�����߳�
            return;
        }
        final Call call = this;
        checkCallOkThread = new Thread()
        {
            @Override
            public void run()
            {
                int times = 0;
                // FIXME������쳣����������ʱ�������ǰת����ʱʱ��Ӧ�ô��ڷ�������·ͨ����ʱʱ��90ms���˴���Ϊ100ms��
                // �����������ʱʱ��Ϊ60ms��Ϊ�˲�Ӧ��ʱ�������飬�˴���Ϊ50ms
                int timeout = (callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO) ? 100 : 50;
                while (isCallOkMonitor)
                {
                    try
                    {
                        Thread.sleep(1000);
                        if (call.getStatus() == CommonConstantEntry.CALL_STATE_IN_CALL)
                        {// ͨ����
                            break;// �˳�
                        }
                        times++;

                        if (times == timeout)
                        {
                            if (call.getStatus() != CommonConstantEntry.CALL_STATE_IN_CALL)
                            {// ����ͨ����
                                Log.error(TAG, "onCallTimeout::Call Status:" + call.getStatus());
                                if (listener != null)
                                {
                                    // ���糬ʱδ�ӵ�������������
                                    if (call_state == CommonConstantEntry.CALL_STATE_INCOMING)
                                    {
                                        if (timeout == 50)
                                        {// ���������ն���ǰ10s��ʱ���Ծܾ���ʽ
                                            refuse(CommonConstantEntry.Q850_NOREASON);// �ܾ�
                                        }
//                                        ((UserAgent) listener).addActiveGroup((ExtendedCall) call, ServiceConstant.CALL_REASON_SEIZEFAILED);
                                    }
                                    listener.onCallTimeout(call);// ���г�ʱ����
                                }
                            }
                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                        break;// �˳�
                    }
                }
            }
        };
        checkCallOkThread.start();
    }

    /**
     * �����������Ƿ��ж�
     */
    private void checkCallHeartBeatTimeout()
    {
        if (callHeartBeatThread != null)
        {// ��֤һ������ֻ����һ����������߳�
            return;
        }
        final Call call = this;
        heartbeatTime = System.currentTimeMillis();

        callHeartBeatThread = new Thread()
        {
            @Override
            public void run()
            {
                int timeoutTime = 90000;// ����������ʱʱ�䣺90s
                int sleepTime = 10000;// ������ڣ�10s
                callHeartBeatRunning = true;// ��ʼ�����������
                while (callHeartBeatRunning)
                {
                    try
                    {
                        Thread.sleep(sleepTime);
                        if ((System.currentTimeMillis() - heartbeatTime) > timeoutTime)
                        {
                            Log.error(TAG, "onCallTimeout::CallHeartBeat Timeout.");
                            if (listener != null)
                                listener.onCallTimeout(call, CommonConstantEntry.CALL_END_HEARTBEAT_TIMEOUT);// ����������ʱ����
                            break;// �˳�
                        }
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                        break;// �˳�
                    }
                }
            }
        };
        callHeartBeatThread.start();
    }

    /**
     * ����Ƿ���յ����н���ACKȷ����Ϣ�����û������г�ʱ
     * 
     * ����˵�� :
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
                        {// ������ȴ�ʱ��6��
                            Log.error(TAG, "onCallTimeout::Not Receive Ack.");
                            if (listener != null)
                                listener.onCallTimeout(call);// ���г�ʱ����
                            break;// �˳�
                        }
                        times++;
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                        break;// �˳�
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
    * ��Invite��Ϣ�л�ȡͨ�����ȼ������û�з���Ĭ�����ȼ���4��
    * @param invite ��SIP�������յ���INVITE��Ϣ��
    * @return ��ʾ���û���������Ϣ��
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
        {// ������δ���� //�����ռ
         // ����A�뱻��B����ͨ���У���ʱC��B���Ҽ�����ߣ�B����A�ĺ��н��������A��B�ĺ������ڽ�����(1XX)��B��A��486��
            if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
                return dialog.busy(releaseReason);
            else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
                return dialog.busy(releaseReason);// ��ռʧ�ܣ�����A�뱻��B����ͨ���У���ʱC��A����A-Bͨ��������ߣ�A��C��486��
            else if (CommonConstantEntry.SIP_CALL_DND == releaseReason)
                return dialog.refuse(480, SipResponses.reasonOf(480), releaseReason);// �����ģʽ
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
        {// ȥ����δ���� //�����ռ
         // ����A�뱻��B����ͨ���У���ʱC��A���Ҽ�����ߣ�A����B�ĺ��н��������A��B�ĺ������ڽ�����(1XX)��A��B��CANCEL��
            if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
            {
                return dialog.cancel(releaseReason);
            }
            else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
            {
                // ��ռʧ�ܣ�����A�뱻��B����ͨ���У���ʱC��A����A-Bͨ��������ߣ�A��C��486��
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
        {// ͨ�������У��򱣳�״̬
         // �����ռ c) ���A��B�ĺ����ѽ��������Է���BYE
            if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
                return dialog.bye(releaseReason);
            else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
                return dialog.busy(releaseReason);// d)
                                                  // ��ռʧ�ܣ�����A�뱻��B����ͨ���У���ʱC��A����A-Bͨ��������ߣ�A��C��486��
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
     * �˳����
     * @throws Exception 
     */
    public boolean quitcall() throws Exception
    {
        return quitcall(CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * �˳����
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

            call_priority = getCallPriority(msg);// ���ú������ȼ�
        }
        else if (code == 100)
        {
            // --------------------------------���з������ܲ�����־��¼
            // Begin-------------------------------------------
            if (CommonConfigEntry.TEST_CALL)
            {
                PerfTestHelper.CALLPREF.setReceive100Trying(System.currentTimeMillis());
            }
            // --------------------------------���з������ܲ�����־��¼
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
        {// ȡ���Ѿ���״̬��Ϊ���������Ǻ����Ѿ��ɹ�����
            Log.info(TAG, "onDlgCall::call.bye:: callid:" + extendedCall.getCallId() + " status:" + call_state);
            extendedCall.changeStatus(CommonConstantEntry.CALL_STATE_IN_CALL);
            extendedCall.bye(CommonConstantEntry.Q850_NOREASON);// ����
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
            listener.onHeartbeatResponse(msg);// ��������Ӧ�ն�����
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
