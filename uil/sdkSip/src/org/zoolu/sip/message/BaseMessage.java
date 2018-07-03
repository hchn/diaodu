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

package org.zoolu.sip.message;

import org.zoolu.sip.provider.*;
import org.zoolu.sip.header.*;
import org.zoolu.sip.address.*;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.net.UdpPacket;
import java.util.*;

/** Class BaseMessage implements a generic SIP Message. */
public abstract class BaseMessage {
	/** UDP */
	public static final String PROTO_UDP = "udp";
	/** TCP */
	public static final String PROTO_TCP = "tcp";
	/** TLS */
	public static final String PROTO_TLS = "tls";
	/** SCTP */
	public static final String PROTO_SCTP = "sctp";

	/** Maximum receiving packet size */
	protected static int MAX_PKT_SIZE = 8000;

	/** The remote ip address */
	protected String remote_addr;

	/** The remote port */
	protected int remote_port;

	/** Transport protocol */
	protected String transport_proto;

	/** Connection identifier */
	protected ConnectionIdentifier connection_id;

	/** Inits empty Message */
	private void init() { // message="";
		remote_addr = null;
		remote_port = 0;
		transport_proto = null;
		connection_id = null;
	}

	/** Costructs a new empty Message */
	public BaseMessage() {
		init();
	}

	/** Costructs a new Message */
	public BaseMessage(byte[] data, int offset, int len) {
		init();
	}

	/** Costructs a new Message */
	public BaseMessage(UdpPacket packet) {
		init();
	}

	/** Costructs a new Message */
	public BaseMessage(String str) {
		init();
	}

	/** Costructs a new Message */
	public BaseMessage(BaseMessage msg) { // message=new String(msg.message);
		remote_addr = msg.remote_addr;
		remote_port = msg.remote_port;
		transport_proto = msg.transport_proto;
		connection_id = msg.connection_id;
		// packet_length=msg.packet_length;
	}

	/** Creates and returns a clone of the Message */
	abstract public Object clone();

	/** Gets remote ip address */
	public String getRemoteAddress() {
		return remote_addr;
	}

	/** Gets remote port */
	public int getRemotePort() {
		return remote_port;
	}

	/** Gets transport protocol */
	public String getTransportProtocol() {
		return transport_proto;
	}

	/** Gets connection identifier */
	public ConnectionIdentifier getConnectionId() {
		return connection_id;
	}

	/** Sets remote ip address */
	public void setRemoteAddress(String addr) {
		remote_addr = addr;
	}

	/** Sets remote port */
	public void setRemotePort(int port) {
		remote_port = port;
	}

	/** Sets transport protocol */
	public void setTransport(String proto) {
		transport_proto = proto;
	}

	/** Sets connection identifier */
	public void setConnectionId(ConnectionIdentifier conn_id) {
		connection_id = conn_id;
	}

}
