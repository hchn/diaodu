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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 * Nitin Khanna, Hughes Systique Corp. (Reason: Android specific change, optmization, bug fix) 
 */

package org.zoolu.sip.dialog;

import java.util.Hashtable;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.authentication.DigestAuthentication;
import org.zoolu.sip.header.AuthorizationHeader;
import org.zoolu.sip.header.ExpiresHeader;
import org.zoolu.sip.header.RequestLine;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.header.ViaHeader;
import org.zoolu.sip.header.WwwAuthenticateHeader;
import org.zoolu.sip.message.BaseMessageFactory;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.sip.message.SipResponses;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.sip.provider.TransactionIdentifier;
import org.zoolu.sip.transaction.Transaction;
import org.zoolu.sip.transaction.TransactionClient;
import org.zoolu.sip.transaction.TransactionServer;

import com.jiaxun.sdk.util.log.Log;

/**
 * Class ExtendedInviteDialog can be used to manage extended invite dialogs.
 * <p>
 * An ExtendedInviteDialog allows the user: <br>
 * - to handle authentication <br>
 * - to handle refer/notify <br>
 * - to capture all methods within the dialog
 */
public class ExtendedInviteDialog extends org.zoolu.sip.dialog.InviteDialog {

	/** Max number of registration attempts. */
	static final int MAX_ATTEMPTS = 3;

	/** ExtendedInviteDialog listener. */
	ExtendedInviteDialogListener dialog_listener;

	/** Acive transactions. */
	/* HSC CHANGES START */
	Hashtable<TransactionIdentifier, Transaction> transactions;
	/* HSC CHANGES END */
	/** User name. */
	String username;

	/** User name. */
	String realm;

	/** User's passwd. */
	String passwd;

	/** Nonce for the next authentication. */
	String next_nonce;

	/** Qop for the next authentication. */
	String qop;

	/** Number of authentication attempts. */
	int attempts;

	private final static String TAG = ExtendedInviteDialog.class.getName();

	/** Creates a new ExtendedInviteDialog. */
	public ExtendedInviteDialog(SipProvider provider,
			ExtendedInviteDialogListener listener) {
		super(provider, listener);
		init(listener);
	}

	/** Creates a new ExtendedInviteDialog. */
	public ExtendedInviteDialog(SipProvider provider, String username,
			String realm, String passwd, ExtendedInviteDialogListener listener) {
		super(provider, listener);
		init(listener);
		this.username = username;
		this.realm = realm;
		this.passwd = passwd;
	}

	/** Inits the ExtendedInviteDialog. */
	private void init(ExtendedInviteDialogListener listener) {
		this.dialog_listener = listener;
		this.transactions = new Hashtable<TransactionIdentifier, Transaction>();
		this.username = null;
		this.realm = null;
		this.passwd = null;
		this.next_nonce = null;
		this.qop = null;
		this.attempts = 0;
	}

	/** Sends a new request within the dialog 
	 * @throws Exception */
	public void request(Message req) throws Exception {
		TransactionClient t = new TransactionClient(sip_provider, req, this);
		transactions.put(t.getTransactionId(), t);
		t.request();
	}

	/** Sends a new REFER within the dialog 
	 * @throws Exception */
	public void refer(NameAddress refer_to) throws Exception {
		refer(refer_to, null);
	}

	/** 发送消息 
	 * @throws Exception */
	public void pushMessage(Message req) throws Exception {
	    Log.info(TAG, "pushMessage");
        //会话内消息，最大超时为2秒
        TransactionClient t = new TransactionClient(sip_provider, req, this, 2000);
        transactions.put(t.getTransactionId(), t);
        t.request();
	}

	/** info消息发送dtmf 
	 * @throws Exception */
	public void info(char c, int duration) throws Exception
	{
		Message req = BaseMessageFactory.createRequest(this, SipMethods.INFO,
				null);
		req.setBody("application/dtmf-relay", "Signal=" + c + "\r\n+Duration="
				+ duration);
		request(req);
	}

	/** Sends a new REFER within the dialog 
	 * @throws Exception */
	public void refer(NameAddress refer_to, NameAddress referred_by) throws Exception {
		Message req = MessageFactory.createReferRequest(this, refer_to,
				referred_by);
		request(req);
	}

	/** Sends a new NOTIFY within the dialog 
	 * @throws Exception */
	public void notify(int code, String reason) throws Exception {
		notify((new StatusLine(code, reason)).toString());
	}

	/** Sends a new NOTIFY within the dialog 
	 * @throws Exception */
	public void notify(String sipfragment) throws Exception {
		Message req = MessageFactory.createNotifyRequest(this, "refer", null,
				sipfragment);
		request(req);
	}

	/** Responds with <i>resp</i> 
	 * @throws Exception */
	public boolean respond(Message resp) throws Exception {
		String method = resp.getCSeqHeader().getMethod();
		Log.info(TAG, "respond:: method=" + method);
		if (method.equals(SipMethods.INVITE)
				|| method.equals(SipMethods.CANCEL)
				|| method.equals(SipMethods.BYE)) {
			return super.respond(resp);
		} else {
			TransactionIdentifier transaction_id = resp.getTransactionId();
			Log.info(TAG, "respond:: transaction-id=" + transaction_id);
			if (transactions.containsKey(transaction_id)) {
				TransactionServer t = (TransactionServer) transactions
						.get(transaction_id);
				t.respondWith(resp);
				return true;
			} else
			    Log.info("ExtendedInviteDialog.respond", "transaction server not found; message discarded");
		}
		return false;
	}

	/** Accept a REFER 
	 * @throws Exception */
	public void acceptRefer(Message req) throws Exception {
	    Log.info(TAG, "acceptRefer");
	    
		Message resp = MessageFactory.createResponse(req, 202,
				SipResponses.reasonOf(200), null);
		respond(resp);
	}

	/** Refuse a REFER 
	 * @throws Exception */
	public void refuseRefer(Message req) throws Exception {
	    Log.info(TAG, "refuseRefer");
	    
		Message resp = MessageFactory.createResponse(req, 603,
				SipResponses.reasonOf(603), null);
		respond(resp);
	}

	/** Inherited from class SipProviderListener. 
	 * @throws Exception */
	public void onReceivedMessage(SipProvider provider, Message msg) throws Exception {
	    Log.info(TAG, "onReceivedMessage");
	    
		if (msg.isResponse()) {
			super.onReceivedMessage(provider, msg);
		} else if (msg.isInvite() || msg.isAck() || msg.isCancel()
				|| msg.isBye()) {
			super.onReceivedMessage(provider, msg);
		} else {

			if (msg.isRefer()) { // Message
			    //仅 REFER 与 isNOTIFY使用 TransactionServer 故从上面移下来。解决内存泄露问题
			    TransactionServer t = new TransactionServer(sip_provider, msg, this);
	            transactions.put(t.getTransactionId(), t);
				NameAddress refer_to = msg.getReferToHeader().getNameAddress();
				NameAddress referred_by = null;
				if (msg.hasReferredByHeader())
					referred_by = msg.getReferredByHeader().getNameAddress();
				dialog_listener.onDlgRefer(this, refer_to, referred_by, msg);
			} else if (msg.isNotify()) {
			  //仅 REFER 与 isNOTIFY使用 TransactionServer 故从上面移下来。解决内存泄露问题
			    TransactionServer t = new TransactionServer(sip_provider, msg, this);
	            transactions.put(t.getTransactionId(), t);
				Message resp = MessageFactory.createResponse(msg, 200,
						SipResponses.reasonOf(200), null);
				respond(resp);
				String event = msg.getEventHeader().getValue();
				String sipfragment = msg.getBody();
				dialog_listener.onDlgNotify(this, event, sipfragment, msg);
			} else if (msg.isInfo()) {
			    int code = 0;
                String reason = "";
                StatusLine status_line = msg.getStatusLine();
                if(status_line != null)
                {
                    code = status_line.getCode();
                    reason = status_line.getReason();
                }
                dialog_listener.onDlgInfoResponse(this, code, reason, msg.getBody(), msg);
			} else {
			    Log.info("ExtendedInviteDialog.onReceivedMessage", "Received alternative request "
                        + msg.getRequestLine().getMethod());
				dialog_listener.onDlgAltRequest(this, msg.getRequestLine()
						.getMethod(), msg.getBody(), msg);
			}
		}
	}

	protected void firePushToTalkNotify(final int result, final String number) {

	}

	/** ACK the request (sends a "200 OK" response). 
	 * @throws Exception */
	public void ack(Message msg) throws Exception {
	    Log.info(TAG, "ack");

		Message resp = MessageFactory.createResponse(msg, 200,
				SipResponses.reasonOf(200), null);
		resp.setExpiresHeader(new ExpiresHeader(SipStack.default_expires));

		sip_provider.sendMessage(resp);
	}

	/**
	 * Inherited from TransactionClientListener. When the
	 * TransactionClientListener goes into the "Completed" state, receiving a
	 * failure response
	 * @throws Exception 
	 */
	public void onTransFailureResponse(TransactionClient tc, Message msg) throws Exception {
		String method = tc.getTransactionMethod();
		StatusLine status_line = msg.getStatusLine();
		int code = status_line.getCode();
		String reason = status_line.getReason();

		Log.error(TAG, "onTransFailureResponse:: method:" + method + " code:" + code);
	        
		boolean isErr401 = false;
		boolean isErr407 = false;

		// AUTHENTICATION-BEGIN
		if (attempts < MAX_ATTEMPTS) {
			switch (code) {
			case 401:
				if (msg.hasWwwAuthenticateHeader()) {
					realm = msg.getWwwAuthenticateHeader().getRealmParam();
					isErr401 = true;
				}
				break;

			case 407:
				if (msg.hasProxyAuthenticateHeader()) {
					realm = msg.getProxyAuthenticateHeader().getRealmParam();
					isErr407 = true;
				}
			}
		}

		if (isErr401 | isErr407) {
			attempts++;
			Message req = tc.getRequestMessage();
			req.setCSeqHeader(req.getCSeqHeader().incSequenceNumber());
			ViaHeader vh = req.getViaHeader();
			String newbranch = SipProvider.pickBranch();
			vh.setBranch(newbranch);
			req.removeViaHeader();

			req.addViaHeader(vh);
			WwwAuthenticateHeader wah;
			if (code == 401)
				wah = msg.getWwwAuthenticateHeader();
			else
				wah = msg.getProxyAuthenticateHeader();
			String qop_options = wah.getQopOptionsParam();
			qop = (qop_options != null) ? "auth" : null;
			RequestLine rl = req.getRequestLine();
			DigestAuthentication digest = new DigestAuthentication(
					rl.getMethod(), rl.getAddress().toString(), wah, qop, null,
					username, passwd);
			AuthorizationHeader ah;
			if (code == 401)
				ah = digest.getAuthorizationHeader();
			else
				ah = digest.getProxyAuthorizationHeader();
			req.setAuthorizationHeader(ah);
			transactions.remove(tc.getTransactionId());
			tc = new TransactionClient(sip_provider, req, this);
			transactions.put(tc.getTransactionId(), tc);
			tc.request();
			invite_req = req; // modified
		} else
		// AUTHENTICATION-END
		if (method.equals(SipMethods.INVITE)
				|| method.equals(SipMethods.CANCEL)
				|| method.equals(SipMethods.BYE)) {
			super.onTransFailureResponse(tc, msg);
		} else if (tc.getTransactionMethod().equals(SipMethods.REFER)) {
			transactions.remove(tc.getTransactionId());
			dialog_listener.onDlgReferResponse(this, code, reason, msg);
		} else {
			String body = msg.getBody();
			transactions.remove(tc.getTransactionId());
			dialog_listener.onDlgAltResponse(this, method, code, reason, body,
					msg);
		}
	}

	/**
	 * Inherited from TransactionClientListener. When an
	 * TransactionClientListener goes into the "Terminated" state, receiving a
	 * 2xx response
	 */
	public void onTransSuccessResponse(TransactionClient t, Message msg) {
		attempts = 0;
		String method = t.getTransactionMethod();
		StatusLine status_line = msg.getStatusLine();
		int code = status_line.getCode();
		String reason = status_line.getReason();
		
		Log.info(TAG, "onTransSuccessResponse:: method:" + method + " code:" + code);

		if (method.equals(SipMethods.INVITE)
				|| method.equals(SipMethods.CANCEL)
				|| method.equals(SipMethods.BYE)) {
			super.onTransSuccessResponse(t, msg);
		} else if (t.getTransactionMethod().equals(SipMethods.REFER)) {
			transactions.remove(t.getTransactionId());
			dialog_listener.onDlgReferResponse(this, code, reason, msg);
		} else {
			String body = msg.getBody();
			transactions.remove(t.getTransactionId());
			dialog_listener.onDlgAltResponse(this, method, code, reason, body,
					msg);
		}
	}

	/**
	 * Inherited from TransactionClientListener. When the TransactionClient goes
	 * into the "Terminated" state, caused by transaction timeout
	 * @throws Exception 
	 */
	public void onTransTimeout(TransactionClient t) throws Exception {
		String method = t.getTransactionMethod();

		Log.info(TAG, "onTransTimeout:: method:" + method);

		if (method.equals(SipMethods.INVITE) || method.equals(SipMethods.BYE)) {
			super.onTransTimeout(t);
		} else { // do something..
			transactions.remove(t.getTransactionId());
		}
	}

}
