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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.jiaxun.sdk.acl.line.sip.ua;

import org.zoolu.net.SocketAddress;
import org.zoolu.net.UdpPacket;
import org.zoolu.net.UdpSocket;

import com.jiaxun.sdk.acl.line.LineAdapter;
import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * KeepAliveUdp thread, for keeping the connection up toward a target node (e.g.
 * toward the serving proxy/gw or a remote UA). It periodically sends
 * keep-alive tokens in order to refresh NAT UDP session timeouts.
 * <p>
 * It can be used for both signaling (SIP) or data plane (RTP/UDP).
 */
public class KeepAliveUdp extends Thread
{
    /** Destination socket address (e.g. the registrar server) */
    protected SocketAddress target;

    /** Time between two keep-alive tokens [millisecs] */
    protected long delta_time;

    /** UdpSocket */
    UdpSocket udp_socket;

    /** Udp packet */
    UdpPacket udp_packet = null;

    /** Expiration date [millisecs] */
    long expire = 0;

    /** Whether it is running */
    boolean stop = false;

    private long lastHeartbeat = 0;// 上次心跳回复时间

    // 心跳有效最大间隔时间，单呼：ms
    private long validateTime = 0;
    /** 是否有心跳回复       */
    private boolean isResponse = false;

    private LineAdapter lineAdapter;

    /** Creates a new KeepAliveUdp daemon */
    protected KeepAliveUdp(SocketAddress target, long delta_time, LineAdapter lineAdapter)
    {
        this.target = target;
        this.delta_time = delta_time;
        this.lineAdapter = lineAdapter;
        validateTime = this.delta_time + 32000;// 心跳有效最大间隔时间
    }

    /** Creates a new KeepAliveUdp daemon */
    public KeepAliveUdp(UdpSocket udp_socket, SocketAddress target, long delta_time, LineAdapter lineAdapter)
    {
        this.target = target;
        this.delta_time = delta_time;
        this.lineAdapter = lineAdapter;
        init(udp_socket, null);
        start();
    }

    /** Creates a new KeepAliveUdp daemon */
    public KeepAliveUdp(UdpSocket udp_socket, SocketAddress target, UdpPacket udp_packet, long delta_time)
    {
        this.target = target;
        this.delta_time = delta_time;
        init(udp_socket, udp_packet);
        start();
    }

    /** Inits the KeepAliveUdp */
    private void init(UdpSocket udp_socket, UdpPacket udp_packet)
    {
        this.udp_socket = udp_socket;
        if (udp_packet == null)
        {
            byte[] buff = { (byte) '\r', (byte) '\n' };
            udp_packet = new UdpPacket(buff, 0, buff.length);
        }
        if (target != null)
        {
            udp_packet.setIpAddress(target.getAddress());
            udp_packet.setPort(target.getPort());
        }
        this.udp_packet = udp_packet;
    }

    /** Whether the UDP relay is running */
    public boolean isRunning()
    {
        return !stop;
    }

    /** Sets the time (in milliseconds) between two keep-alive tokens */
    public void setDeltaTime(long delta_time)
    {
        this.delta_time = delta_time;
    }

    /** Gets the time (in milliseconds) between two keep-alive tokens */
    public long getDeltaTime()
    {
        return delta_time;
    }

    /** Sets the destination SocketAddress */
    public void setDestSoAddress(SocketAddress soaddr)
    {
        target = soaddr;
        if (udp_packet != null && target != null)
        {
            udp_packet.setIpAddress(target.getAddress());
            udp_packet.setPort(target.getPort());
        }

    }

    /** Gets the destination SocketAddress */
    public SocketAddress getDestSoAddress()
    {
        return target;
    }

    /** Sets the expiration time (in milliseconds) */
    public void setExpirationTime(long time)
    {
        if (time == 0)
            expire = 0;
        else
            expire = System.currentTimeMillis() + time;
    }

    /** Stops sending keep-alive tokens */
    public void halt()
    {
        stop = true;
    }

    /** Sends the kepp-alive packet now. 
     * @throws Exception */
    public void sendToken() throws java.io.IOException, Exception
    { // do send?
        if (!stop && target != null && udp_socket != null)
        {
            udp_socket.send(udp_packet);
        }
    }

    /** Main thread. */
    public void run()
    {
        try
        {
            while (!stop)
            {
                sendToken();
                // System.out.print(".");

                int time = 0;
                int sleepTime = 100;
                while (time < 50)
                {// 循环监测是否收到心跳回复
                    sleep(sleepTime);
                    time++;

                    if ((SdkUtil.getRealTime() - lastHeartbeat) < validateTime)
                    {// 收到上次心跳回复
                        delta_time = CommonConfigEntry.HEARTBEAT_SERVER_TIME;// 心跳检测周期
                        validateTime = this.delta_time + 32000;// 心跳有效最大间隔时间
                        isResponse = true;// 已经回复
                        lineAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_ACTIVE;
                        // 通知接口：通知链路的状态
                        LineManager.getInstance().onRegisterStateChanged();
                        break;// 退出
                    }
                }

                if (!isResponse)
                {// 没有收到上次心跳回复
                    delta_time = 40000;// 心跳检测周期
                    validateTime = this.delta_time + 32000;// 心跳有效最大间隔时间
                    isResponse = false;// 回复超时
                    lineAdapter.serviceStatus = CommonConstantEntry.SERVICE_STATUS_HEARTBEAT_HALT;
                    // 通知接口：通知链路的状态
                    LineManager.getInstance().onRegisterStateChanged();
                }

                sleep(delta_time - time * sleepTime);// 心跳时间间隔需要减去循环检测时间

                if (expire > 0 && SdkUtil.getRealTime() > expire)
                    halt();
            }
        }
        catch (Exception e)
        {
            Log.exception("KeepAliveUdp", e);
        }
        // System.out.println("o");
        udp_socket = null;
    }

    /** Gets a String representation of the Object */
    public String toString()
    {
        String str = null;
        if (udp_socket != null)
        {
            str = "udp:" + udp_socket.getLocalAddress() + ":" + udp_socket.getLocalPort() + "-->" + target.toString();
        }
        return str + " (" + delta_time + "ms)";
    }

    public long getLastHeartbeat()
    {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat)
    {
        this.lastHeartbeat = lastHeartbeat;
    }

}