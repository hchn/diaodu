/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
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
 */

package org.zoolu.sip.message;

import java.util.Vector;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.header.*;
import org.zoolu.sip.provider.DialogIdentifier;
import org.zoolu.sip.provider.MethodIdentifier;
import org.zoolu.sip.provider.SipParser;
import org.zoolu.sip.provider.TransactionIdentifier;
import org.zoolu.net.UdpPacket;

/**
 * Class Message extends class sip.message.BaseMessage adding some SIP
 * extensions.
 * <p />
 * Class Message supports all methods and header definened in RFC3261, plus:
 * <ul>
 * <li> method MESSAGE (RFC3428) </>
 * <li> method REFER (RFC3515) </>
 * <li> header Refer-To </>
 * <li> header Referred-By </>
 * <li> header Event </>
 * </ul>
 */
public class Message extends org.zoolu.sip.message.BaseMessageOtp {
	/** Costructs a new empty Message */
	public Message() {
		super();
	}

	/** Costructs a new Message */
	public Message(String str) {
		super(str);
	}

	/** Costructs a new Message */
	public Message(byte[] buff, int offset, int len) {
		super(buff, offset, len);
	}

	/** Costructs a new Message */
	public Message(UdpPacket packet) {
		super(packet);
	}

	/** Costructs a new Message */
	public Message(Message msg) {
		super(msg);
	}

	/** Creates and returns a clone of the Message */
	public Object clone() {
		return new Message(this);
	}

	// ****************************** Extensions
	// *******************************/

	/**
	 * Returns boolean value to indicate if Message is a MESSAGE request
	 * (RFC3428)
	 */
	public boolean isMessage() throws NullPointerException {
		return isRequest(SipMethods.MESSAGE);
	}

	/** Returns boolean value to indicate if Message is a REFER request (RFC3515) */
	public boolean isRefer() throws NullPointerException {
		return isRequest(SipMethods.REFER);
	}

	/**
	 * Returns boolean value to indicate if Message is a NOTIFY request
	 * (RFC3265)
	 */
	public boolean isNotify() throws NullPointerException {
		return isRequest(SipMethods.NOTIFY);
	}

	/**
	 * Returns boolean value to indicate if Message is a SUBSCRIBE request
	 * (RFC3265)
	 */
	public boolean isSubscribe() throws NullPointerException {
		return isRequest(SipMethods.SUBSCRIBE);
	}

	/**
	 * Returns boolean value to indicate if Message is a PUBLISH request
	 * (RFC3903)
	 */
	public boolean isPublish() throws NullPointerException {
		return isRequest(SipMethods.PUBLISH);
	}

	/** Whether the message has the Refer-To header */
	public boolean hasReferToHeader() {
		return hasHeader(SipHeaders.Refer_To);
	}

	/** Gets ReferToHeader */
	public ReferToHeader getReferToHeader() {
		Header h = getHeader(SipHeaders.Refer_To);
		if (h == null)
			return null;
		return new ReferToHeader(h);
	}

	/** Sets ReferToHeader */
	public void setReferToHeader(ReferToHeader h) {
		setHeader(h);
	}

	/** Removes ReferToHeader from Message (if it exists) */
	public void removeReferToHeader() {
		removeHeader(SipHeaders.Refer_To);
	}

	/** Whether the message has the Referred-By header */
	public boolean hasReferredByHeader() {
		return hasHeader(SipHeaders.Refer_To);
	}

	/** Gets ReferredByHeader */
	public ReferredByHeader getReferredByHeader() {
		Header h = getHeader(SipHeaders.Referred_By);
		if (h == null)
			return null;
		return new ReferredByHeader(h);
	}

	/** Sets ReferredByHeader */
	public void setReferredByHeader(ReferredByHeader h) {
		setHeader(h);
	}

	/** Removes ReferredByHeader from Message (if it exists) */
	public void removeReferredByHeader() {
		removeHeader(SipHeaders.Referred_By);
	}

	/** Whether the message has the EventHeader */
	public boolean hasEventHeader() {
		return hasHeader(SipHeaders.Event);
	}

	/** Gets EventHeader */
	public EventHeader getEventHeader() {
		Header h = getHeader(SipHeaders.Event);
		if (h == null)
			return null;
		return new EventHeader(h);
	}

	/** Sets EventHeader */
	public void setEventHeader(EventHeader h) {
		setHeader(h);
	}

	/** Removes EventHeader from Message (if it exists) */
	public void removeEventHeader() {
		removeHeader(SipHeaders.Event);
	}

	/** Whether the message has the AllowEventsHeader */
	public boolean hasAllowEventsHeader() {
		return hasHeader(SipHeaders.Allow_Events);
	}

	/** Gets AllowEventsHeader */
	public AllowEventsHeader getAllowEventsHeader() {
		Header h = getHeader(SipHeaders.Allow_Events);
		if (h == null)
			return null;
		return new AllowEventsHeader(h);
	}

	/** Sets AllowEventsHeader */
	public void setAllowEventsHeader(AllowEventsHeader h) {
		setHeader(h);
	}

	/** Removes AllowEventsHeader from Message (if it exists) */
	public void removeAllowEventsHeader() {
		removeHeader(SipHeaders.Allow_Events);
	}

	/** Whether the message has the Subscription-State header */
	public boolean hasSubscriptionStateHeader() {
		return hasHeader(SipHeaders.Subscription_State);
	}

	/** Gets SubscriptionStateHeader */
	public SubscriptionStateHeader getSubscriptionStateHeader() {
		Header h = getHeader(SipHeaders.Subscription_State);
		if (h == null)
			return null;
		return new SubscriptionStateHeader(h);
	}

	/** Sets SubscriptionStateHeader */
	public void setSubscriptionStateHeader(SubscriptionStateHeader h) {
		setHeader(h);
	}

	/** Removes SubscriptionStateHeader from Message (if it exists) */
	public void removeSubscriptionStateHeader() {
		removeHeader(SipHeaders.Subscription_State);
	}

    //
    public void setUserToUserHeader(UserToUserHeader h) {
        setHeader(h);
    }
    public void setTerminateHeader(TerminateHeader h) {
        setHeader(h);
    }
    public void removeUserToUserHeader() {
        removeHeader(SipHeaders.User_To_User);
    }

    public boolean hasUserToUserHeader() {
        return hasHeader(SipHeaders.User_To_User);
    }
    
    public void setCallIdTransferHeader(CallIdTransferHeader h) {
        setHeader(h);
    }

    public void removeCallIdTransferHeader() {
        removeHeader(SipHeaders.CALL_ID_TRANSFER);
    }

    public boolean hasCallIdTransferHeader() {
        return hasHeader(SipHeaders.CALL_ID_TRANSFER);
    }
    

    public UserToUserHeader getUserToUserHeader() {
        Header h = getHeader(SipHeaders.User_To_User);
        if (h == null)
            return null;
        return new UserToUserHeader(h);
    }

    public void setResourcePriorityHeader(ResourcePriorityHeader h) {
        setHeader(h);
    }

    public void removeResourcePriorityHeader() {
        removeHeader(SipHeaders.Resource_Priority);
    }

    public boolean hasResourcePriorityHeader() {
        return hasHeader(SipHeaders.Resource_Priority);
    }

    public ResourcePriorityHeader getResourcePriorityHeader() {
        Header h = getHeader(SipHeaders.Resource_Priority);
        if (h == null)
            return null;
        return new ResourcePriorityHeader(h);
    }

    // **************************** Specific Headers
    // ****************************/

    /** Whether Message has MaxForwardsHeader */
    public boolean hasMaxForwardsHeader() {
        return hasHeader(SipHeaders.Max_Forwards);
    }

    /** Gets MaxForwardsHeader of Message */
    public MaxForwardsHeader getMaxForwardsHeader() {
        Header h = getHeader(SipHeaders.Max_Forwards);
        if (h == null)
            return null;
        else
            return new MaxForwardsHeader(h);
    }

    /** Sets MaxForwardsHeader of Message */
    public void setMaxForwardsHeader(MaxForwardsHeader mfh) {
        setHeader(mfh);
    }

    /** Removes MaxForwardsHeader from Message */
    public void removeMaxForwardsHeader() {
        removeHeader(SipHeaders.Max_Forwards);
    }

    /** Whether Message has FromHeader */
    public boolean hasFromHeader() {
        return hasHeader(SipHeaders.From);
    }

    /** Gets FromHeader of Message */
    public FromHeader getFromHeader() {
        Header h = getHeader(SipHeaders.From);
        if (h == null)
            return null;
        else
            return new FromHeader(h);
    }

    /** Sets FromHeader of Message */
    public void setFromHeader(FromHeader fh) {
        setHeader(fh);
    }

    /** Removes FromHeader from Message */
    public void removeFromHeader() {
        removeHeader(SipHeaders.From);
    }

    /** Whether Message has ToHeader */
    public boolean hasToHeader() {
        return hasHeader(SipHeaders.To);
    }

    /** Gets ToHeader of Message */
    public ToHeader getToHeader() {
        Header h = getHeader(SipHeaders.To);
        if (h == null)
            return null;
        else
            return new ToHeader(h);
    }

    /** Sets ToHeader of Message */
    public void setToHeader(ToHeader th) {
        setHeader(th);
    }

    /** Removes ToHeader from Message */
    public void removeToHeader() {
        removeHeader(SipHeaders.To);
    }

    /** Whether Message has ContactHeader */
    public boolean hasContactHeader() {
        return hasHeader(SipHeaders.Contact);
    }

    /**
     * <b>Deprecated</b>. Gets ContactHeader of Message. Use getContacts
     * instead.
     */
    public ContactHeader getContactHeader() { // Header
        // h=getHeader(SipHeaders.Contact);
        // if (h==null) return null; else return new ContactHeader(h);
        MultipleHeader mh = getContacts();
        if (mh == null)
            return null;
        return new ContactHeader(mh.getTop());
    }

    /** Adds ContactHeader */
    public void addContactHeader(ContactHeader ch, boolean top) {
        addHeader(ch, top);
    }

    /** Sets ContactHeader */
    public void setContactHeader(ContactHeader ch) {
        if (hasContactHeader())
            removeContacts();
        addHeader(ch, false);
    }

    /** Gets a MultipleHeader of Contacts */
    public MultipleHeader getContacts() {
        Vector<Header> v = getHeaders(SipHeaders.Contact);
        if (v.size() > 0)
            return new MultipleHeader(v);
        else
            return null;
    }

    /** Adds Contacts */
    public void addContacts(MultipleHeader contacts, boolean top) {
        addHeaders(contacts, top);
    }

    /** Sets Contacts */
    public void setContacts(MultipleHeader contacts) {
        if (hasContactHeader())
            removeContacts();
        addContacts(contacts, false);
    }

    /** Removes ContactHeaders from Message */
    public void removeContacts() {
        removeAllHeaders(SipHeaders.Contact);
    }

    /** Whether Message has ViaHeaders */
    public boolean hasViaHeader() {
        return hasHeader(SipHeaders.Via);
    }

    /** Adds ViaHeader at the top */
    public void addViaHeader(ViaHeader vh) {
        addHeader(vh, true);
    }

    /** Gets the first ViaHeader */
    public ViaHeader getViaHeader() { // Header h=getHeader(SipHeaders.Via);
        // if (h==null) return null; else return new ViaHeader(h);
        MultipleHeader mh = getVias();
        if (mh == null)
            return null;
        return new ViaHeader(mh.getTop());
    }

    /** Removes the top ViaHeader */
    public void removeViaHeader() { // removeHeader(SipHeaders.Via);
        MultipleHeader mh = getVias();
        mh.removeTop();
        setVias(mh);
    }

    /** Gets all Vias */
    public MultipleHeader getVias() {
        Vector<Header> v = getHeaders(SipHeaders.Via);
        if (v.size() > 0)
            return new MultipleHeader(v);
        else
            return null;
    }

    /** Adds Vias */
    public void addVias(MultipleHeader vias, boolean top) {
        addHeaders(vias, top);
    }

    /** Sets Vias */
    public void setVias(MultipleHeader vias) {
        if (hasViaHeader())
            removeVias();
        addContacts(vias, true);
    }

    /** Removes ViaHeaders from Message (if any exists) */
    public void removeVias() {
        removeAllHeaders(SipHeaders.Via);
    }

    /** Whether Message has RouteHeader */
    public boolean hasRouteHeader() {
        return hasHeader(SipHeaders.Route);
    }

    /** Adds RouteHeader at the top */
    public void addRouteHeader(RouteHeader h) {
        addHeaderAfter(h, SipHeaders.Via);
    }

    /** Adds multiple Route headers at the top */
    public void addRoutes(MultipleHeader routes) {
        addHeadersAfter(routes, SipHeaders.Via);
    }

    /** Gets the top RouteHeader */
    public RouteHeader getRouteHeader() { // Header
        // h=getHeader(SipHeaders.Route);
        // if (h==null) return null; else return new RouteHeader(h);
        MultipleHeader mh = getRoutes();
        if (mh == null)
            return null;
        return new RouteHeader(mh.getTop());
    }

    /** Gets the whole route */
    public MultipleHeader getRoutes() {
        Vector<Header> v = getHeaders(SipHeaders.Route);
        if (v.size() > 0)
            return new MultipleHeader(v);
        else
            return null;
    }

    /** Removes the top RouteHeader */
    public void removeRouteHeader() { // removeHeader(SipHeaders.Route);
        MultipleHeader mh = getRoutes();
        mh.removeTop();
        setRoutes(mh);
    }

    /** Removes all RouteHeaders from Message (if any exists) */
    public void removeRoutes() {
        removeAllHeaders(SipHeaders.Route);
    }

    /** Sets the whole route */
    public void setRoutes(MultipleHeader routes) {
        if (hasRouteHeader())
            removeRoutes();
        addRoutes(routes);
    }

    /** Whether Message has RecordRouteHeader */
    public boolean hasRecordRouteHeader() {
        return hasHeader(SipHeaders.Record_Route);
    }

    /** Adds RecordRouteHeader at the top */
    public void addRecordRouteHeader(RecordRouteHeader rr) { // addHeaderAfter(rr,SipHeaders.Via);
        addHeaderAfter(rr, SipHeaders.CSeq);
    }

    /** Adds multiple RecordRoute headers at the top */
    public void addRecordRoutes(MultipleHeader routes) { // addHeadersAfter(routes,SipHeaders.Via);
        addHeadersAfter(routes, SipHeaders.CSeq);
    }

    /** Gets the top RecordRouteHeader */
    public RecordRouteHeader getRecordRouteHeader() { // Header
        // h=getHeader(SipHeaders.Record_Route);
        // if (h==null) return null; else return new RecordRouteHeader(h);
        MultipleHeader mh = getRecordRoutes();
        if (mh == null)
            return null;
        return new RecordRouteHeader(mh.getTop());
    }

    /** Gets the whole RecordRoute headers */
    public MultipleHeader getRecordRoutes() {
        Vector<Header> v = getHeaders(SipHeaders.Record_Route);
        if (v.size() > 0)
            return new MultipleHeader(v);
        else
            return null;
    }

    /** Removes the top RecordRouteHeader */
    public void removeRecordRouteHeader() { // removeHeader(SipHeaders.Record_Route);
        MultipleHeader mh = getRecordRoutes();
        mh.removeTop();
        setRecordRoutes(mh);
    }

    /** Removes all RecordRouteHeader from Message (if any exists) */
    public void removeRecordRoutes() {
        removeAllHeaders(SipHeaders.Record_Route);
    }

    /** Sets the whole RecordRoute headers */
    public void setRecordRoutes(MultipleHeader routes) {
        if (hasRecordRouteHeader())
            removeRecordRoutes();
        addRecordRoutes(routes);
    }

    /** Whether Message has CSeqHeader */
    public boolean hasCSeqHeader() {
        return hasHeader(SipHeaders.CSeq);
    }

    /** Gets CSeqHeader of Message */
    public CSeqHeader getCSeqHeader() {
        Header h = getHeader(SipHeaders.CSeq);
        if (h == null)
            return null;
        else
            return new CSeqHeader(h);
    }

    /** Sets CSeqHeader of Message */
    public void setCSeqHeader(CSeqHeader csh) {
        setHeader(csh);
    }
    
    /** Gets ReasonHeader of Message *///chengang add for get release reason header
    public ReasonHeader getReasonHeader() {
        Header h = getHeader(SipHeaders.Reason);
        if (h == null)
            return null;
        else
            return new ReasonHeader(h);
    }

    /** Sets CSeqHeader of Message */
    public void setReasonHeader(ReasonHeader rsh) {
        setHeader(rsh);
    }

    /** Removes CSeqHeader from Message */
    public void removeCSeqHeader() {
        removeHeader(SipHeaders.CSeq);
    }

    /** Whether has CallIdHeader */
    public boolean hasCallIdHeader() {
        return hasHeader(SipHeaders.Call_ID);
    }

    /** Sets CallIdHeader of Message */
    public void setCallIdHeader(CallIdHeader cih) {
        setHeader(cih);
    }

    /** Gets CallIdHeader of Message */
    public CallIdHeader getCallIdHeader() {
        Header h = getHeader(SipHeaders.Call_ID);
        if (h == null)
            return null;
        else
            return new CallIdHeader(h);
    }

    /** Removes CallIdHeader from Message */
    public void removeCallIdHeader() {
        removeHeader(SipHeaders.Call_ID);
    }
    
    /** Sets PJxActiveNetWorkIdHeader of Message */
    public void setPJxActiveNetWorkIdHeader(PJxActiveNetWorkIdHeader cih) {
        setHeader(cih);
    }

    /** Gets PJxActiveNetWorkIdHeader of Message */
    public PJxActiveNetWorkIdHeader getPJxActiveNetWorkIdHeader() {
        Header h = getHeader(SipHeaders.P_JXACTIVE_NETWORK_ID);
        if (h == null)
            return null;
        else
            return new PJxActiveNetWorkIdHeader(h);
    }
    
    /** Gets call-type of CallTypeHeader */
    public CallTypeHeader getCallTypeHeader() {
        Header h = getHeader(SipHeaders.CALL_TYPE);
        if (h == null)
            return null;
        else
            return new CallTypeHeader(h);
    }

    /** Sets call-type of CallTypeHeader */
    public void setCallTypeHeader(CallTypeHeader cih) {
        setHeader(cih);
    }

    /** Whether Message has SubjectHeader */
    public boolean hasSubjectHeader() {
        return hasHeader(SipHeaders.Subject);
    }

    /** Sets SubjectHeader of Message */
    public void setSubjectHeader(SubjectHeader sh) {
        setHeader(sh);
    }

    /** Gets SubjectHeader of Message */
    public SubjectHeader getSubjectHeader() {
        Header h = getHeader(SipHeaders.Subject);
        if (h == null)
            return null;
        else
            return new SubjectHeader(h);
    }

    /** Removes SubjectHeader from Message */
    public void removeSubjectHeader() {
        removeHeader(SipHeaders.Subject);
    }

    /** Whether Message has DateHeader */
    public boolean hasDateHeader() {
        return hasHeader(SipHeaders.Date);
    }

    /** Gets DateHeader of Message */
    public DateHeader getDateHeader() {
        Header h = getHeader(SipHeaders.Date);
        if (h == null)
            return null;
        else
            return new DateHeader(h);
    }

    /** Sets DateHeader of Message */
    public void setDateHeader(DateHeader dh) {
        setHeader(dh);
    }

    /** Removes DateHeader from Message (if it exists) */
    public void removeDateHeader() {
        removeHeader(SipHeaders.Date);
    }

    /** Whether has UserAgentHeader */
    public boolean hasUserAgentHeader() {
        return hasHeader(SipHeaders.User_Agent);
    }

    /** Sets UserAgentHeader */
    public void setUserAgentHeader(UserAgentHeader h) {
        setHeader(h);
    }

    /** Gets UserAgentHeader */
    public UserAgentHeader getUserAgentHeader() {
        Header h = getHeader(SipHeaders.User_Agent);
        if (h == null)
            return null;
        else
            return new UserAgentHeader(h);
    }

    /** Removes UserAgentHeader */
    public void removeUserAgentHeader() {
        removeHeader(SipHeaders.User_Agent);
    }

    /** Whether has ServerHeader */
    public boolean hasServerHeader() {
        return hasHeader(SipHeaders.Server);
    }

    /** Sets ServerHeader */
    public void setServerHeader(ServerHeader h) {
        setHeader(h);
    }

    /** Gets ServerHeader */
    public ServerHeader getServerHeader() {
        Header h = getHeader(SipHeaders.Server);
        if (h == null)
            return null;
        else
            return new ServerHeader(h);
    }

    /** Removes ServerHeader */
    public void removeServerHeader() {
        removeHeader(SipHeaders.Server);
    }

    /** Sets AcceptContactHeader */
    public void setAcceptContactHeader(AcceptContactHeader h) {     // added by mandrajg
        setHeader(h);
    }
    
    /** Whether has AcceptHeader */
    public boolean hasAcceptHeader() {
        return hasHeader(SipHeaders.Accept);
    }

    /** Sets AcceptHeader */
    public void setAcceptHeader(AcceptHeader h) {
        setHeader(h);
    }

    /** Gets AcceptHeader */
    public AcceptHeader getAcceptHeader() {
        Header h = getHeader(SipHeaders.Accept);
        if (h == null)
            return null;
        else
            return new AcceptHeader(h);
    }

    /** Removes AcceptHeader */
    public void removeAcceptHeader() {
        removeHeader(SipHeaders.Accept);
    }

    /** Whether has AlertInfoHeader */
    public boolean hasAlertInfoHeader() {
        return hasHeader(SipHeaders.Alert_Info);
    }

    /** Sets AlertInfoHeader */
    public void setAlertInfoHeader(AlertInfoHeader h) {
        setHeader(h);
    }

    /** Gets AlertInfoHeader */
    public AlertInfoHeader getAlertInfoHeader() {
        Header h = getHeader(SipHeaders.Alert_Info);
        if (h == null)
            return null;
        else
            return new AlertInfoHeader(h);
    }

    /** Removes AlertInfoHeader */
    public void removeAlertInfoHeader() {
        removeHeader(SipHeaders.Alert_Info);
    }

    /** Whether has AllowHeader */
    public boolean hasAllowHeader() {
        return hasHeader(SipHeaders.Allow);
    }

    /** Sets AllowHeader */
    public void setAllowHeader(AllowHeader h) {
        setHeader(h);
    }

    /** Gets AllowHeader */
    public AllowHeader getAllowHeader() {
        Header h = getHeader(SipHeaders.Allow);
        if (h == null)
            return null;
        else
            return new AllowHeader(h);
    }

    /** Removes AllowHeader */
    public void removeAllowHeader() {
        removeHeader(SipHeaders.Allow);
    }

    /** Whether Message has ExpiresHeader */
    public boolean hasExpiresHeader() {
        return hasHeader(SipHeaders.Expires);
    }

    /** Gets ExpiresHeader of Message */
    public ExpiresHeader getExpiresHeader() {
        Header h = getHeader(SipHeaders.Expires);
        if (h == null)
            return null;
        else
            return new ExpiresHeader(h);
    }

    /** Sets ExpiresHeader of Message */
    public void setExpiresHeader(ExpiresHeader eh) {
        setHeader(eh);
    }

    /** Removes ExpiresHeader from Message (if it exists) */
    public void removeExpiresHeader() {
        removeHeader(SipHeaders.Expires);
    }

    // **************************** Authentication ****************************/

    /** Whether has AuthenticationInfoHeader */
    public boolean hasAuthenticationInfoHeader() {
        return hasHeader(SipHeaders.Authentication_Info);
    }

    /** Sets AuthenticationInfoHeader */
    public void setAuthenticationInfoHeader(AuthenticationInfoHeader h) {
        setHeader(h);
    }

    /** Gets AuthenticationInfoHeader */
    public AuthenticationInfoHeader getAuthenticationInfoHeader() {
        Header h = getHeader(SipHeaders.Authentication_Info);
        if (h == null)
            return null;
        else
            return new AuthenticationInfoHeader(h);
    }

    /** Removes AuthenticationInfoHeader */
    public void removeAuthenticationInfoHeader() {
        removeHeader(SipHeaders.Authentication_Info);
    }

    /** Whether has AuthorizationHeader */
    public boolean hasAuthorizationHeader() {
        return hasHeader(SipHeaders.Authorization);
    }

    /** Sets AuthorizationHeader */
    public void setAuthorizationHeader(AuthorizationHeader h) {
        setHeader(h);
    }

    /** Gets AuthorizationHeader */
    public AuthorizationHeader getAuthorizationHeader() {
        Header h = getHeader(SipHeaders.Authorization);
        if (h == null)
            return null;
        else
            return new AuthorizationHeader(h);
    }

    /** Removes AuthorizationHeader */
    public void removeAuthorizationHeader() {
        removeHeader(SipHeaders.Authorization);
    }

    /** Whether has WwwAuthenticateHeader */
    public boolean hasWwwAuthenticateHeader() {
        return hasHeader(SipHeaders.WWW_Authenticate);
    }

    /** Sets WwwAuthenticateHeader */
    public void setWwwAuthenticateHeader(WwwAuthenticateHeader h) {
        setHeader(h);
    }

    /** Gets WwwAuthenticateHeader */
    public WwwAuthenticateHeader getWwwAuthenticateHeader() {
        Header h = getHeader(SipHeaders.WWW_Authenticate);
        if (h == null)
            return null;
        else
            return new WwwAuthenticateHeader(h);
    }

    /** Removes WwwAuthenticateHeader */
    public void removeWwwAuthenticateHeader() {
        removeHeader(SipHeaders.WWW_Authenticate);
    }

    /** Whether has ProxyAuthenticateHeader */
    public boolean hasProxyAuthenticateHeader() {
        return hasHeader(SipHeaders.Proxy_Authenticate);
    }

    /** Sets ProxyAuthenticateHeader */
    public void setProxyAuthenticateHeader(ProxyAuthenticateHeader h) {
        setHeader(h);
    }

    /** Gets ProxyAuthenticateHeader */
    public ProxyAuthenticateHeader getProxyAuthenticateHeader() {
        Header h = getHeader(SipHeaders.Proxy_Authenticate);
        if (h == null)
            return null;
        else
            return new ProxyAuthenticateHeader(h);
    }

    /** Removes ProxyAuthenticateHeader */
    public void removeProxyAuthenticateHeader() {
        removeHeader(SipHeaders.Proxy_Authenticate);
    }

    /** Whether has ProxyAuthorizationHeader */
    public boolean hasProxyAuthorizationHeader() {
        return hasHeader(SipHeaders.Proxy_Authorization);
    }

    /** Sets ProxyAuthorizationHeader */
    public void setProxyAuthorizationHeader(ProxyAuthorizationHeader h) {
        setHeader(h);
    }

    /** Gets ProxyAuthorizationHeader */
    public ProxyAuthorizationHeader getProxyAuthorizationHeader() {
        Header h = getHeader(SipHeaders.Proxy_Authorization);
        if (h == null)
            return null;
        else
            return new ProxyAuthorizationHeader(h);
    }

    /** Removes ProxyAuthorizationHeader */
    public void removeProxyAuthorizationHeader() {
        removeHeader(SipHeaders.Proxy_Authorization);
    }

    // **************************** RFC 2543 Legacy
    // ****************************/

    /**
     * Checks whether the next Route is formed according to RFC2543 Strict Route
     * and adapts the message.
     */
    public void rfc2543RouteAdapt() {
        if (hasRouteHeader()) {
            MultipleHeader mrh = getRoutes();
            // RouteHeader rh = new RouteHeader(mrh.getTop());
            if (!(new RouteHeader(mrh.getTop())).getNameAddress().getAddress()
                    .hasLr()) { // re-format the message according to the
                // RFC2543 Strict Route rule
                SipURL next_hop = (new RouteHeader(mrh.getTop()))
                        .getNameAddress().getAddress();
                SipURL recipient = getRequestLine().getAddress();
                mrh.removeTop();
                mrh.addBottom(new RouteHeader(new NameAddress(recipient)));
                setRoutes(mrh);
                setRequestLine(new RequestLine(getRequestLine().getMethod(),
                        next_hop));
            }
        }
    }

    /**
     * Changes form RFC2543 Strict Route to RFC3261 Lose Route.
     * <p>
     * The Request-URI is replaced with the last value from the Route header,
     * and that value is removed from the Route header.
     */
    public void rfc2543toRfc3261RouteUpdate() { // the message is formed
        // according with RFC2543 strict
        // route
        // the next hop is the request-uri
        // the recipient of the message is the last Route value
        RequestLine request_line = getRequestLine();
        SipURL next_hop = request_line.getAddress();
        MultipleHeader mrh = getRoutes();
        SipURL target = (new RouteHeader(mrh.getBottom())).getNameAddress()
                .getAddress();
        mrh.removeBottom();
        next_hop.addLr();
        mrh.addTop(new RouteHeader(new NameAddress(next_hop)));
        removeRoutes();
        addRoutes(mrh);
        setRequestLine(new RequestLine(request_line.getMethod(), target));
    }

    /** Gets the inique DialogIdentifier for an INCOMING message */
    public DialogIdentifier getDialogId() {
        String call_id = getCallIdHeader().getCallId();
        String local_tag, remote_tag;
        if (isRequest()) {
            local_tag = getToHeader().getTag();
            remote_tag = getFromHeader().getTag();
        } else {
            local_tag = getFromHeader().getTag();
            remote_tag = getToHeader().getTag();
        }
        return new DialogIdentifier(call_id, local_tag, remote_tag);
    }

    /** Gets the unique TransactionIdentifier */
    public TransactionIdentifier getTransactionId() {
        String call_id = getCallIdHeader().getCallId();
        ViaHeader top_via = getViaHeader();
        String branch = null;
        if (top_via != null && top_via.hasBranch())
            branch = top_via.getBranch();
//      String sent_by = top_via.getSentBy();
        CSeqHeader cseqh = getCSeqHeader();
        long seqn = cseqh.getSequenceNumber();
        String method = cseqh.getMethod();
        return new TransactionIdentifier(call_id, seqn, method, null /* sent_by modified */, branch);
    }

    /** Gets the MethodIdentifier */
    public MethodIdentifier getMethodId() {
        String method = getCSeqHeader().getMethod();
        return new MethodIdentifier(method);
    }

    /** Returns the transaction method */
    public String getTransactionMethod() {
        return getCSeqHeader().getMethod();
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
