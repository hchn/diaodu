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

package org.zoolu.sip.header;

import org.zoolu.tools.Parser;

/**
 * 双中心注册标识
 */
public class PJxActiveNetWorkIdHeader extends Header {
    
	/** Creates a PJxActiveNetWorkIdHeader with value <i>value</i> */
	public PJxActiveNetWorkIdHeader(String value) {
		super(SipHeaders.P_JXACTIVE_NETWORK_ID, value);
	}

	/** Creates a new PJxActiveNetWorkIdHeader equal to PJxActiveNetWorkIdHeader <i>id</i> */
	public PJxActiveNetWorkIdHeader(Header id) {
		super(id);
	}

	/** Gets P-JXActive-Network-ID of PJxActiveNetWorkIdHeader */
	public String getPJxActiveNetWorkId() {
		return (new Parser(value)).getString();
	}

	/** Sets P-JXActive-Network-ID of PJxActiveNetWorkIdHeader */
	public void setPJxActiveNetWorkId(String pJxActiveNetWorkId) {
		value = pJxActiveNetWorkId;
	}
}
