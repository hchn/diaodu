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
 */

package org.zoolu.sip.dialog;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.CSeqHeader;
import org.zoolu.sip.header.ContactHeader;
import org.zoolu.sip.header.ExtraMethordHeader;
import org.zoolu.sip.header.ReasonHeader;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.sip.message.SipResponses;
import org.zoolu.sip.provider.ConnectionIdentifier;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipProviderListener;
import org.zoolu.sip.transaction.AckTransactionClient;
import org.zoolu.sip.transaction.AckTransactionServer;
import org.zoolu.sip.transaction.AckTransactionServerListener;
import org.zoolu.sip.transaction.InviteTransactionClient;
import org.zoolu.sip.transaction.InviteTransactionServer;
import org.zoolu.sip.transaction.InviteTransactionServerListener;
import org.zoolu.sip.transaction.TransactionClient;
import org.zoolu.sip.transaction.TransactionClientListener;
import org.zoolu.sip.transaction.TransactionServer;

import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;

/**
 * Class InviteDialog can be used to manage invite dialogs. An InviteDialog can
 * be both client or server. (i.e. generating an INVITE request or responding to
 * an incoming INVITE request).
 * <p>
 * An InviteDialog can be in state inviting/waiting/invited, accepted/refused,
 * call, byed/byeing, and close.
 * <p>
 * InviteDialog supports the offer/answer model for the sip body, with the
 * following rules: <br> - both INVITE-offer/2xx-answer and 2xx-offer/ACK-answer
 * modes for incoming calls <br> - INVITE-offer/2xx-answer mode for outgoing
 * calls.
 */
public class InviteDialog extends Dialog implements TransactionClientListener, InviteTransactionServerListener, AckTransactionServerListener,
        SipProviderListener
{
    /** The last invite message */
    Message invite_req;
    /** The last ack message */
    Message ack_req;

    /** The InviteTransactionServer. */
    InviteTransactionServer invite_ts;
    /** The InviteTransactionClient 20121123*/
    InviteTransactionClient invite_tc;
    /** The AckTransactionServer. */
    AckTransactionServer ack_ts;
    /** The BYE TransactionServer. */
    TransactionServer bye_ts;

    /** The InviteDialog listener */
    InviteDialogListener listener;

    /** Whether offer/answer are in INVITE/200_OK */
    boolean invite_offer;

    protected static final int D_INIT = 0;
    protected static final int D_WAITING = 1;
    protected static final int D_INVITING = 2;
    protected static final int D_INVITED = 3;
    protected static final int D_REFUSED = 4;
    protected static final int D_ACCEPTED = 5;
    protected static final int D_CALL = 6;

    protected static final int D_ReWAITING = 11;
    protected static final int D_ReINVITING = 12;
    protected static final int D_ReINVITED = 13;
    protected static final int D_ReREFUSED = 14;
    protected static final int D_ReACCEPTED = 15;

    protected static final int D_BYEING = 7;
    protected static final int D_BYED = 8;
    protected static final int D_CLOSE = 9;

    protected static final int D_CANCELING = 10;

    private final static String TAG = "InviteDialog";

    /** Gets the dialog state */
    protected String getStatusDescription()
    {
        switch (status)
        {
            case D_INIT:
                return "D_INIT";
            case D_WAITING:
                return "D_WAITING";
            case D_INVITING:
                return "D_INVITING";
            case D_INVITED:
                return "D_INVITED";
            case D_REFUSED:
                return "D_REFUSED";
            case D_ACCEPTED:
                return "D_ACCEPTED";
            case D_CALL:
                return "D_CALL";
            case D_ReWAITING:
                return "D_ReWAITING";
            case D_ReINVITING:
                return "D_ReINVITING";
            case D_ReINVITED:
                return "D_ReINVITED";
            case D_ReREFUSED:
                return "D_ReREFUSED";
            case D_ReACCEPTED:
                return "D_ReACCEPTED";
            case D_BYEING:
                return "D_BYEING";
            case D_BYED:
                return "D_BYED";
            case D_CLOSE:
                return "D_CLOSE";
            default:
                return null;
        }
    }

    protected int getStatus()
    {
        return status;
    }

    // ************************** Public methods **************************

    /** Whether the dialog is in "early" state. */
    public boolean isEarly()
    {
        return status < D_ACCEPTED;
    }

    /** Whether the dialog is in "confirmed" state. */
    public boolean isConfirmed()
    {
        return status >= D_ACCEPTED && status < D_CLOSE;
    }

    /** Whether the dialog is in "terminated" state. */
    public boolean isTerminated()
    {
        return status == D_CLOSE;
    }

    /** Whether the session is "active". */
    public boolean isSessionActive()
    {
        Log.info("InviteDialog", "isSessionActive status =" + status);
        // carls add status == D_ReINVITING || status == D_ReINVITED
        return (status == D_CALL);
    }

    /** Gets the invite message */
    public Message getInviteMessage()
    {
        return invite_req;
    }

    /** Creates a new InviteDialog. */
    public InviteDialog(SipProvider sip_provider, InviteDialogListener listener)
    {
        super(sip_provider);
        init(listener);
    }

    /**
     * Creates a new InviteDialog for the already received INVITE request
     * <i>invite</i>.
     * @throws Exception 
     */
    public InviteDialog(SipProvider sip_provider, Message invite, InviteDialogListener listener) throws Exception
    {
        super(sip_provider);
        init(listener);

        changeStatus(D_INVITED);
        invite_req = invite;
        invite_ts = new InviteTransactionServer(sip_provider, invite_req, this);
        update(Dialog.UAS, invite_req);
    }

    /** Inits the InviteDialog. */
    private void init(InviteDialogListener listener)
    {
        this.listener = listener;
        this.invite_req = null;
        this.ack_req = null;
        this.invite_offer = true;
        changeStatus(D_INIT);
    }

    /** Starts a new InviteTransactionServer. */
    public void listen()
    {
        if (!statusIs(D_INIT))
            return;
        // else
        changeStatus(D_WAITING);
        invite_ts = new InviteTransactionServer(sip_provider, this);
        invite_ts.listen();
    }

    /**
     * Starts a new InviteTransactionClient and initializes the dialog state
     * information.
     * 
     * @param callee
     *            the callee url (and display name)
     * @param caller
     *            the caller url (and display name)
     * @param contact
     *            the contact url OR the contact username
     * @param session_descriptor
     *            SDP body
     * @param icsi
     *            the ICSI for this session
     * @throws Exception 
     */
    public boolean invite(String callee, String caller, String contact, String session_descriptor, String icsi, int priority, String funcNumber, String callId,
            boolean activeGroup) throws Exception
    {
        Log.info(TAG, "invite:: callee:" + callee + " caller:" + caller + " contact:" + contact + " priority:" + priority + " funcNumber:" + funcNumber
                + " callId:" + callId + " activeGroup:" + activeGroup);
        if (!statusIs(D_INIT))
            return false;
        // else
        NameAddress to_url = new NameAddress(callee);
        NameAddress from_url = new NameAddress(caller);
        SipURL request_uri = to_url.getAddress();

        NameAddress contact_url = null;
        if (contact != null)
        {
            if (contact.contains("sip:"))
                contact_url = new NameAddress(contact);
            else
                contact_url = new NameAddress(new SipURL(contact, sip_provider.getViaAddress(), sip_provider.getPort()));
        }
        else
            contact_url = from_url;

        Message invite = MessageFactory.createInviteRequest(sip_provider, request_uri, to_url, from_url, contact_url, session_descriptor, icsi, priority,
                funcNumber, callId, activeGroup);
        // do invite
        return invite(invite);
    }

    /**
     * Starts a new InviteTransactionClient and initializes the dialog state
     * information
     * 
     * @param invite
     *            the INVITE message
     * @throws Exception 
     */
    public boolean invite(Message invite) throws Exception
    {
        Log.info(TAG, "invite msg.");

        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setSendInvite(System.currentTimeMillis());
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------

        if (!statusIs(D_INIT))
        {
            return false;
        }
        // else
        changeStatus(D_INVITING);
        invite_req = invite;
        update(Dialog.UAC, invite_req);
        invite_tc = new InviteTransactionClient(sip_provider, invite_req, this);
        invite_tc.request();
        return true;
    }

    /**
     * Starts a new InviteTransactionClient with offer/answer in 2xx/ack and
     * initializes the dialog state information
     * @throws Exception 
     */
    public boolean inviteWithoutOffer(String callee, String caller, String contact, int priority, String funcNumber, String callId, boolean activeGroup)
            throws Exception
    {
        Log.info(TAG, "inviteWithoutOffer");
        invite_offer = false;
        return invite(callee, caller, contact, null, null, priority, funcNumber, callId, activeGroup);
    }

    /**
     * Starts a new InviteTransactionClient with offer/answer in 2xx/ack and
     * initializes the dialog state information
     * @throws Exception 
     */
    public void inviteWithoutOffer(Message invite) throws Exception
    {
        invite_offer = false;
        invite(invite);
    }

    /**
     * Re-invites the remote user.
     * <p>
     * Starts a new InviteTransactionClient and changes the dialog state
     * information
     * <p>
     * Parameters: <br>- contact : the contact url OR the contact username; if
     * null, the previous contact is used <br>- session_descriptor : the
     * message body
     * @throws Exception 
     */
    public void reInvite(String contact, String session_descriptor) throws Exception
    {
        Log.info(TAG, "reInvite:: contact:" + contact);
        if (!statusIs(D_CALL) && !statusIs(D_ReINVITING))
            return;
        // else
        Message invite = MessageFactory.createInviteRequest(this, session_descriptor);
        if (contact != null)
        {
            NameAddress contact_url;
            if (contact.indexOf("sip:") >= 0)
                contact_url = new NameAddress(contact);
            else
                contact_url = new NameAddress(new SipURL(contact, sip_provider.getViaAddress(), sip_provider.getPort()));
            invite.setContactHeader(new ContactHeader(contact_url));
        }
        reInvite(invite);
    }

    /**
     * Re-invites the remote user.
     * <p>
     * Starts a new InviteTransactionClient and changes the dialog state
     * information
     * @throws Exception 
     */
    public void reInvite(Message invite) throws Exception
    {
        Log.info(TAG, "reInvite:: msg.");
        if (!statusIs(D_CALL) && !statusIs(D_ReINVITING))
            return;
        // else
        changeStatus(D_ReINVITING);
        invite_req = invite;
        update(Dialog.UAC, invite_req);
        InviteTransactionClient invite_tc = new InviteTransactionClient(
        /*
         * if(invite_tc != null)//20121123
         * {
         * invite_tc.terminate();
         * }
         * invite_tc = new InviteTransactionClient(
         */
        sip_provider, invite_req, this/* , 4000 */);
        invite_tc.request();
    }

    /**
     * Re-invites the remote user with offer/answer in 2xx/ack
     * <p>
     * Starts a new InviteTransactionClient and changes the dialog state
     * information
     * @throws Exception 
     */
    public void reInviteWithoutOffer(Message invite) throws Exception
    {
        invite_offer = false;
        reInvite(invite);
    }

    /**
     * Re-invites the remote user with offer/answer in 2xx/ack
     * <p>
     * Starts a new InviteTransactionClient and changes the dialog state
     * information
     * @throws Exception 
     */
    public void reInviteWithoutOffer(String contact, String session_descriptor) throws Exception
    {
        invite_offer = false;
        reInvite(contact, session_descriptor);
    }

    /** Sends the ack when offer/answer is in 2xx/ack 
     * @throws Exception */
    public void ackWithAnswer(String contact, String session_descriptor) throws Exception
    {
        if (contact != null)
            setLocalContact(new NameAddress(contact));
        Message ack = MessageFactory.create2xxAckRequest(this, session_descriptor);
        ackWithAnswer(ack);
    }

    /** Sends the ack when offer/answer is in 2xx/ack 
     * @throws Exception */
    public void ackWithAnswer(Message ack) throws Exception
    {
        ack_req = ack;
        // reset the offer/answer flag to the default value
        invite_offer = true;
        AckTransactionClient ack_tc = new AckTransactionClient(sip_provider, ack, null);
        ack_tc.request();
    }

    /**
     * Responds with <i>resp</i>. This method can be called when the
     * InviteDialog is in D_INVITED or D_BYED states.
     * <p>
     * If the CSeq method is INVITE and the response is 2xx, it moves to state
     * D_ACCEPTED, adds a new listener to the SipProviderListener, and creates
     * new AckTransactionServer
     * <p>
     * If the CSeq method is INVITE and the response is not 2xx, it moves to
     * state D_REFUSED, and sends the response.
     * @throws Exception 
     */
    public boolean respond(Message resp) throws Exception
    {
        String method = resp.getCSeqHeader().getMethod();
        Log.info(TAG, "respond:: method:" + method);
        if (method.equals(SipMethods.INVITE))
        {
            if (!verifyStatus(statusIs(D_INVITED) || statusIs(D_ReINVITED)))
            {
                Log.error("InviteDialog.respond", "InviteDialog not in (re)invited state: No response now");
                return false;
            }

            int code = resp.getStatusLine().getCode();
            Log.info("InviteDialog.respond", "code:" + code);

            // 1xx provisional responses
            if (code >= 100 && code < 200)
            {
                invite_ts.respondWith(resp);
                return true;
            }
            // For all final responses establish the dialog
            if (code >= 200)
            { // changeStatus(D_ACCEPTED);
                update(Dialog.UAS, resp);
            }
            // 2xx success responses
            if (code >= 200 && code < 300)
            {
                // --------------------------------呼叫发起性能测试日志记录
                // Begin-------------------------------------------
                if (CommonConfigEntry.TEST_CALL)
                {
                    PerfTestHelper.CALLPREF.setSend200Ok(System.currentTimeMillis());
                }
                // --------------------------------呼叫发起性能测试日志记录
                // End----------------------------------------------
                if (statusIs(D_INVITED))
                    changeStatus(D_ACCEPTED);
                else
                    changeStatus(D_ReACCEPTED);
                // terminates the INVITE Transaction server and activates an ACK
                // Transaction server
                invite_ts.terminate();
                ConnectionIdentifier conn_id = invite_ts.getConnectionId();
                ack_ts = new AckTransactionServer(sip_provider, conn_id, resp, this);
                ack_ts.respond();
                return true;
            }
            else
            // 300-699 failure responses
            {
                if (statusIs(D_INVITED))
                    changeStatus(D_REFUSED);
                else
                    changeStatus(D_ReREFUSED);
                invite_ts.respondWith(resp);
                return true;
            }
        }
        if (method.equals(SipMethods.BYE))
        {
            if (!verifyStatus(statusIs(D_BYED)))
                return false;
            bye_ts.respondWith(resp);
            return true;
        }
        return false;
    }

    /**
     * Responds with <i>code</i> and <i>reason</i>. This method can be called
     * when the InviteDialog is in D_INVITED, D_ReINVITED states
     * @throws Exception 
     */
    public void respond(int code, String reason, String contact, String sdp) throws Exception
    {
        respond(code, reason, CommonConstantEntry.Q850_NOREASON, contact, sdp);
    }

    /**
     * Responds with <i>code</i> and <i>reason</i>. This method can be called
     * when the InviteDialog is in D_INVITED, D_ReINVITED states
     * @throws Exception 
     */
    public boolean respond(int code, String reason, int releaseReason, String contact, String sdp) throws Exception
    {
        Log.info(TAG, "respond:: code:" + code + " reason:" + reason + " status=" + status);

        if (statusIs(D_INVITED) || statusIs(D_ReINVITED))
        {
            NameAddress contact_address = null;
            if (contact != null)
                contact_address = new NameAddress(contact);
            Message resp = MessageFactory.createResponse(invite_req, code, reason, contact_address);
            resp = addHeaderForReleaseReason(resp, releaseReason);
            resp.setBody(sdp);

            return respond(resp);
        }
        else
            Log.error("InviteDialog.respond", "Dialog isn't in \"invited\" state: cannot respond (" + code + "/" + getStatus() + "/" + getDialogID() + ")");
        return false;
    }

    /**
     * Signals that the phone is ringing. This method should be called when the
     * InviteDialog is in D_INVITED or D_ReINVITED state
     * @throws Exception 
     */
    public void ring(String sdp) throws Exception
    { // modified
        Log.info(TAG, "respond ring 180 msg.");
        respond(180, SipResponses.reasonOf(180), null, sdp);
    }

    /**
     * Accepts the incoming call. This method should be called when the
     * InviteDialog is in D_INVITED or D_ReINVITED state
     * @throws Exception 
     */
    public void accept(String contact, String sdp) throws Exception
    {
        Log.info(TAG, "accept:: contact:" + contact + " sdp:" + sdp);
        respond(200, SipResponses.reasonOf(200), contact, sdp);
    }

    /**
     * Refuses the incoming call. This method should be called when the
     * InviteDialog is in D_INVITED or D_ReINVITED state
     * @throws Exception 
     */
    public boolean refuse(int code, String reason) throws Exception
    {
        return refuse(code, reason, CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * Refuses the incoming call. This method should be called when the
     * InviteDialog is in D_INVITED or D_ReINVITED state
     * @throws Exception 
     */
    public boolean refuse(int code, String reason, int releaseReason) throws Exception
    {
        Log.info(TAG, "refuse(" + code + "," + reason + ")");
        return respond(code, reason, releaseReason, null, null);
    }

    /**
     * Refuses the incoming call. This method should be called when the
     * InviteDialog is in D_INVITED or D_ReINVITED state
     * @throws Exception 
     */
    public boolean refuse() throws Exception
    {
        Log.info(TAG, "refuse");
        return refuse(406, SipResponses.reasonOf(406));
    }

    public void busy() throws Exception
    {
        busy(CommonConstantEntry.Q850_NOREASON);
    }

    public boolean busy(int releaseReason) throws Exception
    {
        return refuse(486, SipResponses.reasonOf(486), releaseReason); // modified
    }

    /**
     * Termiante the call. This method should be called when the InviteDialog is
     * in D_CALL state
     * <p>
     * Increments the Cseq, moves to state D_BYEING, and creates new BYE
     * TransactionClient
     * @throws Exception 
     */
    public boolean bye() throws Exception
    {
        return bye(CommonConstantEntry.Q850_NOREASON);
    }

    /**判断释放原因并加入 reason字段*/
    public Message addHeaderForReleaseReason(Message msg, int releaseReason)
    {
        if (CommonConstantEntry.Q850_NOREASON == releaseReason)
            return msg;

        if (CommonConstantEntry.Q850_PREEMPTION == releaseReason)
        {
            msg.addHeader(new ReasonHeader(ReasonHeader.Q850_8_Preemption), false);
        }
        else if (CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED == releaseReason)
        {
            msg.addHeader(new ReasonHeader(ReasonHeader.Q850_46_Precedence), false);
        }
        // Modified by hubin at 20131031 退出群组
        else if (CommonConstantEntry.Q850_QUIT_GROUP_CALL == releaseReason)
        {
            msg.addHeader(new ReasonHeader(ReasonHeader.Q850_112_QUIT_GROUP_CALL), false);
        }
        else if (CommonConstantEntry.SIP_CALL_DND == releaseReason)
        {
            msg.addHeader(new ReasonHeader(ReasonHeader.SIP_CALL_DND), false);
        }

        return msg;
    }

    /**
     * Termiante the call. This method should be called when the InviteDialog is
     * in D_CALL state
     * <p>
     * Increments the Cseq, moves to state D_BYEING, and creates new BYE
     * TransactionClient
     * @throws Exception 
     */
    public boolean bye(int releaseReason) throws Exception
    {
        Message bye = MessageFactory.createByeRequest(this);
        bye = addHeaderForReleaseReason(bye, releaseReason);
        return bye(bye);
    }

    /**
     * 退出组呼
     * @throws Exception 
     */
    public boolean quit() throws Exception
    {
        return quit(CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * 退出组呼
     * @throws Exception 
     */
    public boolean quit(int releaseReason) throws Exception
    {
        Log.info("InviteDialog", "quit");
        Message bye = MessageFactory.createByeRequest(this);
        bye.addHeader(new ExtraMethordHeader("quit"), false);
        bye = addHeaderForReleaseReason(bye, releaseReason);
        return bye(bye);
    }

    /**
     * Termiante the call. This method should be called when the InviteDialog is
     * in D_CALL state
     * <p>
     * Increments the Cseq, moves to state D_BYEING, and creates new BYE
     * TransactionClient
     * @throws Exception 
     */
    public boolean bye(Message bye) throws Exception
    {
        Log.info(TAG, "bye:: msg.");
        changeStatus(D_BYEING);

        // 性能测试日志
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setSendBye(System.currentTimeMillis());
        }

        TransactionClient tc = new TransactionClient(sip_provider, bye, this);
        tc.request();
        return true;
    }

    /**
     * Cancel the ongoing call request or a call listening. This method should
     * be called when the InviteDialog is in D_INVITING or D_ReINVITING state or
     * in the D_WAITING state
     * @throws Exception 
     */
    public boolean cancel() throws Exception
    {
        return cancel(CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * Cancel the ongoing call request or a call listening. This method should
     * be called when the InviteDialog is in D_INVITING or D_ReINVITING state or
     * in the D_WAITING state
     * @throws Exception 
     */
    public boolean cancel(int releaseReason) throws Exception
    {
        Log.info(TAG, "cancel:: releaseReason:" + releaseReason);
        if (statusIs(D_INVITING) || statusIs(D_ReINVITING))
        {
            Message cancel = MessageFactory.createCancelRequest(invite_req, this); // modified
            cancel = addHeaderForReleaseReason(cancel, releaseReason);
            return cancel(cancel);
        }
        else if (statusIs(D_WAITING) || statusIs(D_ReWAITING))
        {
            invite_ts.terminate();
        }
        return false;
    }

    /**
     * Cancel the ongoing call request or a call listening. This method should
     * be called when the InviteDialog is in D_INVITING or D_ReINVITING state or
     * in the D_WAITING state
     * @throws Exception 
     */
    public boolean cancel(Message cancel) throws Exception
    {
        Log.info(TAG, "cancel");
        if (statusIs(D_INVITING) || statusIs(D_ReINVITING))
        {
            changeStatus(D_CANCELING);
            TransactionClient tc = new TransactionClient(sip_provider, cancel, null);
            tc.request();
            return true;
        }
        else if (statusIs(D_WAITING) || statusIs(D_ReWAITING))
        {
            invite_ts.terminate();
        }
        return false;
    }

    /**
     * Redirects the incoming call , specifing the <i>code</i> and <i>reason</i>.
     * This method can be called when the InviteDialog is in D_INVITED or
     * D_ReINVITED state
     * @throws Exception 
     */
    public void redirect(int code, String reason, String contact) throws Exception
    {
        Log.info(TAG, "redirect:: code:" + code + " reason:" + reason + " contact:" + contact);
        respond(code, reason, contact, null);
    }

    // ************** Inherited from SipProviderListener **************

    /**
     * Inherited from class SipProviderListener. Called when a new message is
     * received (out of any ongoing transaction) for the current InviteDialog.
     * Always checks for out-of-date methods (CSeq header sequence number).
     * <p>
     * If the message is ACK(2xx/INVITE) request, it moves to D_CALL state, and
     * fires <i>onDlgAck(this,body,msg)</i>.
     * <p>
     * If the message is 2xx(INVITE) response, it create a new
     * AckTransactionClient
     * <p>
     * If the message is BYE, it moves to D_BYED state, removes the listener
     * from SipProvider, fires onDlgBye(this,msg) then it responds with 200 OK,
     * moves to D_CLOSE state and fires onDlgClose(this)
     * @throws Exception 
     */
    public void onReceivedMessage(SipProvider sip_provider, Message msg) throws Exception
    {
        Log.info(TAG, "onReceivedMessage");

        if (msg.isRequest() && !(msg.isAck() || msg.isCancel()) && msg.getCSeqHeader().getSequenceNumber() <= getRemoteCSeq())
        {
            Log.error("InviteDialog", "Request message is too late (CSeq too small): Message discarded");
            return;
        }
        // invite received
        if (msg.isRequest() && msg.isInvite())
        {
            verifyStatus(statusIs(D_INIT) || statusIs(D_CALL));
            // NOTE: if the invite_ts.listen() is used, you should not arrive
            // here with the D_INIT state..
            // however state D_INIT has been included for robustness against
            // further changes.
            if (statusIs(D_INIT))
                changeStatus(D_INVITED);
            else
                changeStatus(D_ReINVITED);
            invite_req = msg;
            invite_ts = new InviteTransactionServer(sip_provider, invite_req, this);
            // ((TransactionServer)transaction).listen();
            update(Dialog.UAS, invite_req);
            if (statusIs(D_INVITED))
                listener.onDlgInvite(this, invite_req.getToHeader().getNameAddress(), invite_req.getFromHeader().getNameAddress(), invite_req.getBody(),
                        invite_req);
            else
                listener.onDlgReInvite(this, invite_req.getBody(), invite_req);
        }
        else
        // ack (of 2xx of INVITE)
        if (msg.isRequest() && msg.isAck())
        {
            if (!verifyStatus(statusIs(D_ACCEPTED) || statusIs(D_ReACCEPTED)))
                return;
            changeStatus(D_CALL);
            // terminates the AckTransactionServer
            ack_ts.terminate();
            listener.onDlgAck(this, msg.getBody(), msg);
            listener.onDlgCall(this);
        }
        else
        // keep sending ACK (if already sent) for any "200 OK" received
        if (msg.isResponse())
        {
            CSeqHeader cSeqHeader = msg.getCSeqHeader();
            if (cSeqHeader.getValue().endsWith(SipMethods.OPTIONS))
            {// 心跳消息
                listener.onDlgHeartbeat(msg);
            }
            if (!verifyStatus(statusIs(D_CALL)))
                return;
            int code = msg.getStatusLine().getCode();
            verifyThat(code >= 200 && code < 300, "code 2xx was expected");
            if (ack_req != null)
            {
                AckTransactionClient ack_tc = new AckTransactionClient(sip_provider, ack_req, null);
                ack_tc.request();
            }
        }
        else
        // bye received
        if (msg.isRequest() && msg.isBye())
        {

            changeStatus(D_BYED);
            bye_ts = new TransactionServer(sip_provider, msg, this);
            // automatically sends a 200 OK
            Message resp = MessageFactory.createResponse(msg, 200, SipResponses.reasonOf(200), null);
            respond(resp);
            listener.onDlgBye(this, msg);
            changeStatus(D_CLOSE);
            listener.onDlgClose(this);
        }
        else
        // cancel received
        if (msg.isRequest() && msg.isCancel())
        {
            TransactionServer ts = new TransactionServer(sip_provider, msg, null);
            ts.respondWith(MessageFactory.createResponse(msg, 200, SipResponses.reasonOf(200), null));
            // automatically sends a 487 Cancelled
            Message resp = MessageFactory.createResponse(invite_req, 487, SipResponses.reasonOf(487), null);
            respond(resp);
            listener.onDlgCancel(this, msg);
        }
        else
        // any other request received
        if (msg.isRequest())
        {
            TransactionServer ts = new TransactionServer(sip_provider, msg, null);
            // ts.listen();
            ts.respondWith(MessageFactory.createResponse(msg, 405, SipResponses.reasonOf(405), null));
        }
    }

    // ************** Inherited from InviteTransactionClientListener
    // **************

    /**
     * Inherited from TransactionClientListener. When the
     * TransactionClientListener is in "Proceeding" state and receives a new 1xx
     * response
     * <p>
     * For INVITE transaction it fires
     * <i>onFailureResponse(this,code,reason,body,msg)</i>.
     */
    public void onTransProvisionalResponse(TransactionClient tc, Message msg)
    {
        Log.info(TAG, "onTransProvisionalResponse");

        if (tc.getTransactionMethod().equals(SipMethods.INVITE))
        {
            StatusLine statusline = msg.getStatusLine();
            listener.onDlgInviteProvisionalResponse(this, statusline.getCode(), statusline.getReason(), msg.getBody(), msg);
        }
    }

    /**
     * Inherited from TransactionClientListener. When the
     * TransactionClientListener goes into the "Completed" state, receiving a
     * failure response
     * <p>
     * If called for a INVITE transaction, it moves to D_CLOSE state, removes
     * the listener from SipProvider.
     * <p>
     * If called for a BYE transaction, it moves to D_CLOSE state, removes the
     * listener from SipProvider, and fires <i>onClose(this,msg)</i>.
     * @throws Exception 
     */
    public void onTransFailureResponse(TransactionClient tc, Message msg) throws Exception
    {
        Log.error(TAG, "onTransFailureResponse");

        if (tc.getTransactionMethod().equals(SipMethods.INVITE))
        {
            if (!verifyStatus(statusIs(D_INVITING) || statusIs(D_ReINVITING)))
                return;
            StatusLine statusline = msg.getStatusLine();
            int code = statusline.getCode();
            verifyThat(code >= 300 && code < 700, "error code was expected");
            if (statusIs(D_ReINVITING))
            {
                changeStatus(D_CALL);
                listener.onDlgReInviteFailureResponse(this, code, statusline.getReason(), msg);
            }
            else
            {
                changeStatus(D_CLOSE);
                if (code >= 300 && code < 400)
                    listener.onDlgInviteRedirectResponse(this, code, statusline.getReason(), msg.getContacts(), msg);
                else
                    listener.onDlgInviteFailureResponse(this, code, statusline.getReason(), msg);
                listener.onDlgClose(this);
            }
        }
        else if (tc.getTransactionMethod().equals(SipMethods.BYE))
        {
            if (!verifyStatus(statusIs(D_BYEING)))
                return;
            StatusLine statusline = msg.getStatusLine();
            int code = statusline.getCode();
            verifyThat(code >= 300 && code < 700, "error code was expected");
            changeStatus(InviteDialog.D_CALL);
            listener.onDlgByeFailureResponse(this, code, statusline.getReason(), msg);
        }
    }

    /**
     * Inherited from TransactionClientListener. When an
     * TransactionClientListener goes into the "Terminated" state, receiving a
     * 2xx response
     * <p>
     * If called for a INVITE transaction, it updates the dialog information,
     * moves to D_CALL state, add a listener to the SipProvider, creates a new
     * AckTransactionClient(ack,this), and fires
     * <i>onSuccessResponse(this,code,body,msg)</i>.
     * <p>
     * If called for a BYE transaction, it moves to D_CLOSE state, removes the
     * listener from SipProvider, and fires <i>onClose(this,msg)</i>.
     */
    public void onTransSuccessResponse(TransactionClient tc, final Message msg)
    {
        Log.info(TAG, "onTransSuccessResponse");

        if (tc.getTransactionMethod().equals(SipMethods.INVITE))
        {
            if (!verifyStatus(statusIs(D_INVITING) || statusIs(D_ReINVITING)))
                return;
            if (statusIs(D_CANCELING))
                return;// 呼叫已经取消不处理
            final StatusLine statusline = msg.getStatusLine();
            final int code = statusline.getCode();
            if (!verifyThat(code >= 200 && code < 300 && msg.getTransactionMethod().equals(SipMethods.INVITE), "2xx for invite was expected"))
                return;

            // --------------------------------呼叫发起性能测试日志记录
            // Begin-------------------------------------------
            if (CommonConfigEntry.TEST_CALL)
            {
                PerfTestHelper.CALLPREF.setReceive200Ok(System.currentTimeMillis());
            }
            // --------------------------------呼叫发起性能测试日志记录
            // End----------------------------------------------

            final InviteDialog inviteDialog = this;

            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    changeStatus(D_CALL);
                    update(Dialog.UAC, msg);
                    if (invite_offer)
                    {
                        // --------------------------------呼叫发起性能测试日志记录
                        // Begin-------------------------------------------
                        if (CommonConfigEntry.TEST_CALL)
                        {
                            PerfTestHelper.CALLPREF.setSendAck(System.currentTimeMillis());
                        }
                        // --------------------------------呼叫发起性能测试日志记录
                        // End----------------------------------------------

                        ack_req = MessageFactory.create2xxAckRequest(InviteDialog.this, null);
                        AckTransactionClient ack_tc = new AckTransactionClient(sip_provider, ack_req, null);
                        try
                        {
                            ack_tc.request();
                        }
                        catch (Exception e)
                        {
                            Log.exception(TAG, e);
                        }

                        final boolean re_inviting = statusIs(D_ReINVITING);
                        if (!re_inviting)
                        {
                            try
                            {
                                listener.onDlgCall(inviteDialog);
                            }
                            catch (Exception e)
                            {
                                Log.exception(TAG, e);
                            }// 用于判断呼叫是否结束
                        }
                    }
                }
            }).start();

            final boolean re_inviting = statusIs(D_ReINVITING);
            if (!re_inviting)
            {
                listener.onDlgInviteSuccessResponse(this, code, statusline.getReason(), msg.getBody(), msg);
            }
            else
            {
                listener.onDlgReInviteSuccessResponse(this, code, statusline.getReason(), msg.getBody(), msg);
            }

        }
        else if (tc.getTransactionMethod().equals(SipMethods.BYE))
        {
            if (!verifyStatus(statusIs(D_BYEING)))
                return;
            StatusLine statusline = msg.getStatusLine();
            int code = statusline.getCode();
            verifyThat(code >= 200 && code < 300, "2xx for bye was expected");
            changeStatus(D_CLOSE);
            listener.onDlgByeSuccessResponse(this, code, statusline.getReason(), msg);
            listener.onDlgClose(this);
        }
    }

    /**
     * Inherited from TransactionClientListener. When the TransactionClient goes
     * into the "Terminated" state, caused by transaction timeout
     * @throws Exception 
     */
    public void onTransTimeout(TransactionClient tc) throws Exception
    {
        Log.info(TAG, "current status is:" + status);

        if (tc.getTransactionMethod().equals(SipMethods.INVITE))
        {
            if (!verifyStatus(statusIs(D_INVITING) || statusIs(D_ReINVITING)))
                return;
            if (statusIs(D_ReINVITING))
            {
                listener.onDlgReInviteTimeout(this);
            }
            else
            {
                cancel(); // modified
                changeStatus(D_CLOSE);
                listener.onDlgTimeout(this);
                listener.onDlgClose(this);
            }
        }
        else if (tc.getTransactionMethod().equals(SipMethods.BYE))
        {
            if (!verifyStatus(statusIs(D_BYEING)))
                return;
            changeStatus(D_CLOSE);
            listener.onDlgClose(this);
        }
    }

    // ************** Inherited from InviteTransactionServerListener
    // **************

    /**
     * Inherited from TransactionServerListener. When the TransactionServer goes
     * into the "Trying" state receiving a request
     * <p>
     * If called for a INVITE transaction, it initializes the dialog
     * information, <br>
     * moves to D_INVITED state, and add a listener to the SipProvider, <br>
     * and fires <i>onInvite(caller,body,msg)</i>.
     * @throws Exception 
     */
    public void onTransRequest(TransactionServer ts, Message req) throws Exception
    {
        Log.info(TAG, "onTransRequest");

        if (ts.getTransactionMethod().equals(SipMethods.INVITE))
        {
            if (!verifyStatus(statusIs(D_WAITING)))
                return;
            changeStatus(D_INVITED);
            invite_req = req;
            update(Dialog.UAS, invite_req);
            listener.onDlgInvite(this, invite_req.getToHeader().getNameAddress(), invite_req.getFromHeader().getNameAddress(), invite_req.getBody(), invite_req);
        }
    }

    /**
     * Inherited from InviteTransactionServerListener. When an
     * InviteTransactionServer goes into the "Confirmed" state receining an ACK
     * for NON-2xx response
     * <p>
     * It moves to D_CLOSE state and removes the listener from SipProvider.
     */
    public void onTransFailureAck(InviteTransactionServer ts, Message msg)
    {
        Log.info(TAG, "onTransFailureAck");

        if (!verifyStatus(statusIs(D_REFUSED) || statusIs(D_ReREFUSED)))
            return;
        if (statusIs(D_ReREFUSED))
        {
            changeStatus(D_CALL);
        }
        else
        {
            changeStatus(D_CLOSE);
            listener.onDlgClose(this);
        }
    }

    // ************ Inherited from AckTransactionServerListener ************

    /**
     * When the AckTransactionServer goes into the "Terminated" state, caused by
     * transaction timeout
     */
    public void onTransAckTimeout(AckTransactionServer ts)
    {
        Log.error(TAG, "onAckSrvTimeout");

        if (!verifyStatus(statusIs(D_ACCEPTED) || statusIs(D_ReACCEPTED) || statusIs(D_REFUSED) || statusIs(D_ReREFUSED)))
            return;
        Log.error("InviteDialog", "No ACK received..");
        changeStatus(D_CLOSE);
        listener.onDlgClose(this);
    }

}
