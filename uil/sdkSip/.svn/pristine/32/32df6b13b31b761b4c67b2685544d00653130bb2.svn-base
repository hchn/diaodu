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
 * Nitin Khanna, Hughes Systique Corp. (Reason: Android specific change, optmization, bug fix) 
 */

package org.zoolu.sip.call;

/* HSC CHANGES START */
// import org.zoolu.sip.call.*;
/* HSC CHANGES END */
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.message.Message;

/* HSC CHANGES START */
// import org.zoolu.sdp.*;
// import java.util.Vector;
/* HSC CHANGES END */

/**
 * Interface ExtendedCallListener can be implemented to manage exteded SIP calls
 * (sipx.call.ExtendedCall).
 * <p>
 * Objects of class ExtendedCall use ExtendedCallListener callback methods to
 * signal specific call events.
 */
public interface ExtendedCallListener extends CallListener {
	/**
	 * Callback function called when arriving a new REFER method (Forward
	 * request).
	 */
	public void onCallForward(ExtendedCall call, NameAddress refer_to,
			NameAddress refered_by, Message refer);

	/** Callback function called when a call Forward is accepted. */
	public void onCallForwardAccepted(ExtendedCall call, Message resp);

	/** Callback function called when a call Forward is refused. */
	public void onCallForwardRefused(ExtendedCall call, String reason,
			Message resp);

	/** Callback function called when a call Forward is successfully completed. */
	public void onCallForwardSuccess(ExtendedCall call, Message notify);

	/**
	 * Callback function called when a call Forward is NOT sucessfully
	 * completed.
	 */
	public void onCallForwardFailure(ExtendedCall call, String reason,
			Message notify);

	/**
	 * on call info response.
	 */
	public void onCallInfoResponse(ExtendedCall call, int code, String reason,
	        String body, Message msg);
	
//	/**
//	 * 设置本路是否静音
//	 */
//	public boolean setMute(String callID, boolean mute);
}
