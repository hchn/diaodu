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

package com.jiaxun.sdk.scl.media.audio;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * RtpStreamSender is a generic stream sender. It takes an InputStream and sends it through RTP.
 */
public class RtpStreamSenderTest extends Thread
{
    /** Whether working in info mode. */
    public static boolean info = false;

    long lastTime = 0;

    /** The RtpSocket */
    RtpSocket rtp_socket = null;

    /** Payload type */
    private Map p_type;

    /** Number of frame per second */
    int frame_rate;

    /** Number of bytes per frame */
    int frame_size;

    /** Whether it is running */
    boolean running = true;

    private static String TAG = "RtpStreamSenderTest";

    /**
     * Constructs a RtpStreamSender.
     * 
     * @param input_stream the stream to be sent
     * @param do_sync whether time synchronization must be performed by the RtpStreamSender, or it is performed by the
     * InputStream (e.g. the system audio input)
     * @param payload_type the payload type
     * @param frame_rate the frame rate, i.e. the number of frames that should be sent per second; it is used to
     * calculate the nominal packet time and,in case of do_sync==true, the next departure time
     * @param frame_size the size of the payload
     * @param src_socket the socket used to send the RTP packet
     * @param dest_addr the destination address
     * @param dest_port the destination port
     */
    public RtpStreamSenderTest(Codecs.Map payload_type)
    {
        init(payload_type);
    }

    /** Inits the RtpStreamSender */
    private void init(Codecs.Map payload_type)
    {
    	this.p_type = payload_type;
    	this.frame_rate = payload_type.codec.samp_rate() / payload_type.codec.frame_size();
        this.frame_size = payload_type.codec.frame_size();
    }
    
    /**
     * 设置对端地址和端口
     */
    public void setRemote(SipdroidSocket src_socket, String dest_addr, int dest_port)
    {
    	try
        {
            rtp_socket = new RtpSocket(src_socket, InetAddress.getByName(dest_addr), dest_port);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
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

    /** 消息队列    */
    public static BlockingQueue<RtpPacket> rtpQueue = new LinkedBlockingQueue<RtpPacket>();

    /** Runs it in a new Thread. */
    public void run()
    {
        //设置最高优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        
        int OPUSSF = frame_size / CommonConfigEntry.AUDIO_SEND_SAMPLES;
        Log.info(TAG, "OPUSSF:" + OPUSSF);
        byte[] buffer = new byte[frame_size + 12];
        RtpPacket rtp_packet = new RtpPacket(buffer, 0);
        rtp_packet.setPayloadType(p_type.number);
        Log.info(TAG, "Sample rate  = " + p_type.codec.samp_rate());
        Log.info(TAG, "Frame size = " + frame_size);
        Log.info(TAG, "Buffer size = " + buffer.length);
        RtpPacket rtp_packet_rec = null;//接收语音包

        while (running)
        {
            rtp_packet_rec = rtpQueue.poll();
            if(rtp_packet_rec == null)
            {//没有语音包
                try
                {
                    Thread.sleep(40);
                    continue;
                }
                catch (Exception e)
                {
                }
            }
            
            rtp_packet_rec.setSscr(rtp_packet.getSscr());

            try
            {
                rtp_socket.send(rtp_packet_rec);
            }
            catch (Exception ex)
            {
                Log.error(TAG, "send error.");
            }
            
//            Log.info(TAG, "seqn:" + rtp_packet.getSequenceNumber());
        }
        
        System.gc();
    }

}
