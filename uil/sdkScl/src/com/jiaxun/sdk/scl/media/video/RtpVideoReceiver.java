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

package com.jiaxun.sdk.scl.media.video;

import java.util.Map;

import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.util.log.Log;

/**
 * RtpVideoReceiver is a generic stream receiver. It receives packets from RTP
 * and writes them into an OutputStream.
 */
public class RtpVideoReceiver extends Thread
{

    /** Whether working in debug mode. */
    boolean DEBUG = false;

    /** Whether it is running */
    boolean running = true;

    /** The RtpSocket */
    RtpSocket rtp_socket = null;

    /** tag:视频播放对象 */
    private Map<Integer, RtpVideoPlay> playMap = null;
    
    private int tag = -1;// 单呼视频tag

    private static String TAG = "RtpVideoReceiver";

    /**
     * Constructs a RtpStreamPlay.
     */
    public RtpVideoReceiver(RtpSocket rtp_socket, Map<Integer, RtpVideoPlay> playMap)
    {
        this.rtp_socket = rtp_socket;
        this.playMap = playMap;
    }

    /** Whether is running */
    public boolean isRunning()
    {
        return running;
    }

    /** Stops running */
    public void halt()
    {
        running = false;
    }

    /** Runs it in a new Thread. */
    public void run()
    {
        // 设置最高优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        byte[] receiveH264 = new byte[2200];
        RtpPacket rtp_receive_packet = new RtpPacket(receiveH264, receiveH264.length);
        int rtpTag = 0;

        RtpVideoPlay videoPlay;
        while (running)
        {
            try
            {
                rtp_socket.receive(rtp_receive_packet);
                if(tag == 0)
                {// 单路视频
                    videoPlay = playMap.get(tag);
                }
                else
                {//多路视频
                    rtpTag = rtp_receive_packet.getExtTag();
                    videoPlay = playMap.get(rtpTag);
                }
                if (videoPlay != null)
                {
                    videoPlay.rtpQueue.put(rtp_receive_packet.clone());
                }
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
    }

    public void setTag(int tag)
    {
        this.tag = tag;
    }

}
