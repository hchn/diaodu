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

/* HSC CHANGES START */
// import org.zoolu.sip.call.*;
// import org.zoolu.sip.provider.SipStack;
/* HSC CHANGES END */
import java.util.Vector;

import org.zoolu.net.IpAddress;
import org.zoolu.sdp.AttributeField;
import org.zoolu.sdp.ConnectionField;
import org.zoolu.sdp.MediaDescriptor;
import org.zoolu.sdp.MediaField;
import org.zoolu.sdp.SessionDescriptor;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.message.Message;

/**
 * Class CallListenerAdapter implements CallListener interface providing a dummy
 * implementation of all Call callback functions used to capture Call events.
 * <p>
 * CallListenerAdapter can be extended to manage basic SIP calls. The callback
 * methods defined in this class have basically a void implementation. This
 * class exists as convenience for creating call listener objects. <br>
 * You can extend this class overriding only methods corresponding to events you
 * want to handle.
 * <p>
 * <i>onCallIncoming(NameAddress,String)</i> is the only non-empty method. It
 * signals the receiver the ring status (by using method Call.ring()), adapts
 * the sdp body and accepts the call (by using method Call.accept(sdp)).
 */
public abstract class CallListenerAdapter implements ExtendedCallListener
{

    // ************************** Costructors ***************************

    /** Creates a new dummy call listener */
    protected CallListenerAdapter()
    {
    }

    // ************************* Static methods *************************

    /**
     * Changes the current session descriptor specifing the receiving RTP/UDP
     * port number, the AVP format, the codec, and the clock rate
     */
    /*
     * public static String audioSession(int port, int avp, String codec, int
     * rate) { SessionDescriptor sdp=new SessionDescriptor(); sdp.addMedia(new
     * MediaField("audio ",port,0,"RTP/AVP",String.valueOf(avp)),new
     * AttributeField("rtpmap",avp+" "+codec+"/"+rate)); return sdp.toString();
     * }
     */

    /**
     * Changes the current session descriptor specifing the receiving RTP/UDP
     * port number, the AVP format, the codec, and the clock rate
     */
    /*
     * public static String audioSession(int port) { return
     * audioSession(port,0,"PCMU",8000); }
     */

    // *********************** Callback functions ***********************
    /**
     * Accepts an incoming call. Callback function called when arriving a new
     * INVITE method (incoming call)
     * @throws Exception 
     */
    public void onCallIncoming(Call call, NameAddress callee, NameAddress caller, String sdp, Message invite) throws Exception
    { // printLog("INCOMING");
        String local_session;
        if (sdp != null && sdp.length() > 0)
        {
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            SessionDescriptor local_sdp = new SessionDescriptor(call.getLocalSessionDescriptor());
            SessionDescriptor new_sdp = new SessionDescriptor(remote_sdp.getOrigin(), remote_sdp.getSessionName(), local_sdp.getConnection(),
                    local_sdp.getTime());
            new_sdp.addMediaDescriptors(local_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpMediaProduct(new_sdp, remote_sdp.getMediaDescriptors());
            new_sdp = SdpTools.sdpAttirbuteSelection(new_sdp, "rtpmap");
            local_session = new_sdp.toString();
        }
        else
            local_session = call.getLocalSessionDescriptor();
        call.ring(local_session);
        // accept immediatly
        call.accept(local_session);
    }

    /**
     * Changes the call when remotly requested. Callback function called when
     * arriving a new Re-INVITE method (re-inviting/call modify)
     * @throws Exception 
     */
    public void onCallModifying(Call call, String sdp, Message invite, AttributeField attr) throws Exception
    {
        String local_session;
        if (sdp != null && sdp.length() > 0)
        {
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            SessionDescriptor local_sdp = new SessionDescriptor(call.getLocalSessionDescriptor());
            local_sdp.setConnection(new ConnectionField("IP4", IpAddress.localIpAddress));
            SessionDescriptor new_sdp = new SessionDescriptor(remote_sdp.getOrigin(), remote_sdp.getSessionName(), local_sdp.getConnection(),
                    local_sdp.getTime());

            // 删除掉上一次的Media Attribute的值，重新设置
            MediaDescriptor audioDescriptor = local_sdp.getMediaDescriptor("audio");
            if (audioDescriptor != null)
            {
                audioDescriptor.removeAttribute("recvonly");
                audioDescriptor.removeAttribute("inactive");
                audioDescriptor.removeAttribute("sendrecv");
                audioDescriptor.removeAttribute("sendonly");
                audioDescriptor.addAttribute(attr);
            }

            MediaDescriptor videoDescriptor = local_sdp.getMediaDescriptor("video");
            if (videoDescriptor != null)
            {
                videoDescriptor.removeAttribute("recvonly");
                videoDescriptor.removeAttribute("inactive");
                videoDescriptor.removeAttribute("sendrecv");
                videoDescriptor.removeAttribute("sendonly");
                videoDescriptor.addAttribute(attr);
            }
            new_sdp.addMediaDescriptors(local_sdp.getMediaDescriptors());

            local_session = new_sdp.toString();
        }
        else
            local_session = call.getLocalSessionDescriptor();

        call.acceptReInvite(local_session);// Add by zhoujy at 20121022
                                           // reInvite消息不应修改通话状态
    }

    /**
     * Changes the call when remotly requested. Callback function called when
     * arriving a new Re-INVITE method (re-inviting/call modify)
     * @throws Exception 
     */
    public void onCallModifying(Call call, String sdp, Message invite) throws Exception
    { // printLog("RE-INVITE/MODIFY");
        onCallModifying(call, sdp, invite, null);
    }

    /**
     * Does nothing. Callback function called when arriving a 180 Ringing
     */
    public void onCallRinging(Call call, Message resp)
    { // printLog("RINGING");
    }

    /**
     * Does nothing. Callback function called when arriving a 2xx (call
     * accepted)
     */
    public void onCallAccepted(Call call, String sdp, Message resp)
    { // printLog("ACCEPTED/CALL");
    }

    /**
     * Does nothing. Callback function called when arriving a 4xx (call failure)
     */
    public void onCallRefused(Call call, String reason, Message resp)
    { // printLog("REFUSED
        // ("+reason+")");
    }

    /**
     * Redirects the call when remotly requested. Callback function called when
     * arriving a 3xx (call redirection)
     * @throws Exception 
     */
    public void onCallRedirection(Call call, String reason, Vector<String> contact_list, Message resp) throws Exception
    { // printLog("REDIRECTION
        // ("+reason+")");
        call.call(((ExtendedCall) call).getCallId(), contact_list.elementAt(0));
    }

    /**
     * Does nothing. Callback function called when arriving an ACK method (call
     * confirmed)
     */
    public void onCallConfirmed(Call call, String sdp, Message ack)
    { // printLog("CONFIRMED/CALL");
    }

    /**
     * Does nothing. Callback function called when the invite expires
     */
    public void onCallTimeout(Call call)
    { // printLog("TIMEOUT/CLOSE");
    }

    /**
     * Does nothing. Callback function called when arriving a 2xx
     * (re-invite/modify accepted)
     */
    public void onCallReInviteAccepted(Call call, String sdp, Message resp)
    { // printLog("RE-INVITE-ACCEPTED/CALL");
    }

    /**
     * Does nothing. Callback function called when arriving a 4xx
     * (re-invite/modify failure)
     */
    public void onCallReInviteRefused(Call call, String reason, Message resp)
    { // printLog("RE-INVITE-REFUSED
        // ("+reason+")/CALL");
    }

    /**
     * Does nothing. Callback function called when a re-invite expires
     */
    public void onCallReInviteTimeout(Call call)
    { // printLog("RE-INVITE-TIMEOUT/CALL");
    }

    /**
     * Does nothing. Callback function called when arriving a CANCEL request
     */
    public void onCallCanceling(Call call, Message cancel)
    { // printLog("CANCELING");
    }

    /**
     * Does nothing. Callback function that may be overloaded (extended). Called
     * when arriving a BYE request
     */
    public void onCallClosing(Call call, Message bye)
    { // printLog("CLOSING");
    }

    /**
     * Does nothing. Callback function that may be overloaded (extended). Called
     * when arriving a response for a BYE request (call closed)
     */
    public void onCallClosed(Call call, Message resp)
    { // printLog("CLOSED");
    }

    /**
     * Does nothing. Callback function called when arriving a new REFER method
     * (Forward request)
     */
    public void onCallForward(ExtendedCall call, NameAddress refer_to, NameAddress refered_by, Message refer)
    { // printLog("REFER-TO/Forward");
    }

    /**
     * Does nothing. Callback function called when a call Forward is accepted.
     */
    public void onCallForwardAccepted(ExtendedCall call, Message resp)
    {
    }

    /**
     * Does nothing. Callback function called when a call Forward is refused.
     */
    public void onCallForwardRefused(ExtendedCall call, String reason, Message resp)
    {
    }

    /**
     * Does nothing. Callback function called when a call Forward is
     * successfully completed
     */
    public void onCallForwardSuccess(ExtendedCall call, Message notify)
    { // printLog("Forward
        // SUCCESS");
    }

    /**
     * Does nothing. Callback function called when a call Forward is NOT
     * sucessfully completed
     */
    public void onCallForwardFailure(ExtendedCall call, String reason, Message notify)
    { // printLog("Forward FAILURE");
    }

}
