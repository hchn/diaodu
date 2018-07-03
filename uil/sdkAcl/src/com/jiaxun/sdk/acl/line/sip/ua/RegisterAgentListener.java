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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.jiaxun.sdk.acl.line.sip.ua;


/** Listener of RegisterAgent */
public interface RegisterAgentListener {

    /** When a UA has been successfully (un)registered. 
     * @param result Active, Standby*/
	public void onUaRegistrationSuccess(int result);

    public void onMWIUpdate(RegisterAgent ra, boolean voicemail, int number, String vmacc);

	/** When a UA failed on (un)registering. 
	 * @param reason TODO*/
	public void onUaRegistrationFailure(int reason);
	/**
	 * 方法说明 :超出默认续约时间
	 * @param realm 服务器地址
	 * @param reg_times 续约周期
	 * @author hubin
	 * @Date 2015-1-13
	 */
	public void onUaRegOverRenewTime(String realm, int reg_times);
}
