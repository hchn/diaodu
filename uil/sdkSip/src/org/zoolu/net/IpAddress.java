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

package org.zoolu.net;

import java.net.BindException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.zoolu.demo.DiscoveryTest;

import com.jiaxun.sdk.util.log.Log;

/**
 * IpAddress is an IP address.
 */
public class IpAddress
{

    /** The host address/name */
    String address;

    /** The InetAddress */
    InetAddress inet_address;

    /** Local IP address */
    public static String localIpAddress = "127.0.0.1";
    
    private static String TAG = "IpAddress";

    // ********************* Protected *********************

    /** Creates an IpAddress */
    IpAddress(InetAddress iaddress)
    {
        init(null, iaddress);
    }

    /** Inits the IpAddress */
    private void init(String address, InetAddress iaddress)
    {
        this.address = address;
        this.inet_address = iaddress;
    }

    /** Gets the InetAddress */
    InetAddress getInetAddress()
    {
        if (inet_address == null)
            try
            {
                inet_address = InetAddress.getByName(address);
            }
            catch (java.net.UnknownHostException e)
            {
                inet_address = null;
            }
        return inet_address;
    }

    // ********************** Public ***********************

    /** Creates an IpAddress */
    public IpAddress(String address)
    {
        init(address, null);
    }

    /** Creates an IpAddress */
    public IpAddress(IpAddress ipaddr)
    {
        init(ipaddr.address, ipaddr.inet_address);
    }

    /** Gets the host address */
    /*
     * public String getAddress() { if (address==null)
     * address=inet_address.getHostAddress(); return address; }
     */

    /** Makes a copy */
    public Object clone()
    {
        return new IpAddress(this);
    }

    /** Wthether it is equal to Object <i>obj</i> */
    public boolean equals(Object obj)
    {
        try
        {
            IpAddress ipaddr = (IpAddress) obj;
            if (!toString().equals(ipaddr.toString()))
                return false;
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /** Gets a String representation of the Object */
    public String toString()
    {
        if (address == null && inet_address != null)
            address = inet_address.getHostAddress();
        return address;
    }

    // *********************** Static ***********************

    /** Gets the IpAddress for a given fully-qualified host name. */
    public static IpAddress getByName(String host_addr) throws java.net.UnknownHostException
    {
        InetAddress iaddr = InetAddress.getByName(host_addr);
        return new IpAddress(iaddr);
    }

    /** Sets the local IP address into the variable <i>localIpAddress</i> */
    public static void setLocalIpAddress(String ip)
    {
        Log.info(TAG, "setLocalIpAddress:" + ip);
        if(ip != null)
        {
            localIpAddress = ip;
        }
        // localIpAddress = "0.0.0.0";
    }
    
    /** Gets the local IP address into the variable <i>localIpAddress</i> */
    public static String getLocalIpAddress()
    {
        Log.info(TAG, "getLocalIpAddress");
        String ip = getLocalIpAddress(false, null, 0);
        if(ip != null)
        {
            localIpAddress = ip;
        }
        return localIpAddress;
        // localIpAddress = "0.0.0.0";
    }
    
    /** Gets the local IP address into the variable <i>localIpAddress</i> */
    public static String getLocalIpAddress(String stunServer)
    {
        String ip = getLocalIpAddress(true, stunServer, 0);
        if(ip != null)
        {
            return ip;
        }
        return localIpAddress;
        // localIpAddress = "0.0.0.0";
    }

    /** Sets the local IP address into the variable <i>localIpAddress</i> */
    private static String getLocalIpAddress(boolean stun, String stunServer, int stunPort)
    {
        Log.info(TAG, "getLocalIpAddress::stunServer:" + stunServer + " stunPort:" + stunPort);
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress())
                    {
                        if (inetAddress.getHostAddress().contains(":"))
                            continue;
                        if (!stun)
                        {
                            return inetAddress.getHostAddress();
                        }
                        else
                        {
                            try
                            {
                                DiscoveryTest StunDiscover = new DiscoveryTest(inetAddress, stunServer, stunPort);

                                // call out to stun server
                                StunDiscover.test();
                                return StunDiscover.di.getPublicIP().getHostAddress();
                            }
                            catch (BindException be)
                            {
                                Log.info(TAG, inetAddress.toString());
                                Log.exception(TAG, be);
                            }
                            catch (Exception e)
                            {
                                Log.exception(TAG, e);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        return null;
    }

}
