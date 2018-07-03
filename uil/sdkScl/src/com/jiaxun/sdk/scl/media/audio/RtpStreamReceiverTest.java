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

import java.net.SocketException;

import android.content.ContentResolver;
import android.media.AudioManager;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * RtpStreamReceiver is a generic stream receiver. It receives packets from RTP
 * and writes them into an OutputStream.
 */
public class RtpStreamReceiverTest extends Thread
{

    /** Whether working in debug mode. */
    public static boolean DEBUG = false;

    /** Payload type */
    static Codecs.Map p_type;

    /** Size of the read buffer */
    public static final int BUFFER_SIZE = 1024;

    /**
     * Maximum blocking time, spent waiting for reading new bytes [milliseconds]
     */
    public static final int SO_TIMEOUT = 1000 * 60 * 5;

    /** The RtpSocket */
    RtpSocket rtp_socket = null;

    /** Whether it is running */
    boolean running = true;
    AudioManager am;
    ContentResolver cr;
    public static int speakermode = -1;
    public static boolean bluetoothmode = false;//蓝牙模式
    CallRecorder call_recorder = null;

    private static String TAG = "RtpStreamReceiverTest";

    /**
     * Constructs a RtpStreamReceiver.
     * 
     * @param output_stream
     *            the stream sink
     * @param socket
     *            the local receiver SipdroidSocket
     */
    public RtpStreamReceiverTest(SipdroidSocket socket, Codecs.Map payload_type, CallRecorder rec)
    {
        init(socket);
        if(p_type == null || p_type.codec.samp_rate() != payload_type.codec.samp_rate())
        {//如果采样率为空，或者发生变化
            p_type = payload_type;
        }
        p_type = payload_type;
        call_recorder = rec;
    }

    /** Inits the RtpStreamReceiver */
    private void init(SipdroidSocket socket)
    {
        if (socket != null)
            rtp_socket = new RtpSocket(socket);
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

    void empty()
    {
        try
        {
            rtp_socket.getDatagramSocket().setSoTimeout(1);
            for (;;)
                rtp_socket.receive(rtp_packet);
        }
        catch (SocketException e2)
        {
            // if (!Sipdroid.release) e2.printStackTrace();
        }
        catch (Exception e)
        {
        }
        try
        {
            rtp_socket.getDatagramSocket().setSoTimeout(SO_TIMEOUT);
        }
        catch (SocketException e2)
        {
        }
        catch (Exception e)
        {
        }
    }

    private RtpPacket rtp_packet;

    /** Runs it in a new Thread. */
    public void run()
    {
        if (rtp_socket == null)
        {
            Log.error(TAG, "ERROR: RTP socket is null");
            return;
        }

        //设置最高优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        
        byte[] buffer;

        while (running)
        {
            buffer = new byte[p_type.codec.frame_size() + 12];
            rtp_packet = new RtpPacket(buffer, 0);
            
            try
            {
                rtp_socket.receive(rtp_packet);
                RtpStreamSenderTest.rtpQueue.add(rtp_packet);
            }
            catch (Exception ex)
            {
                Log.error(TAG, "receive error.");
            }
            
//            Log.info(TAG, "seqn:" + rtp_packet.getSequenceNumber());
        }

        // Call recording: stop incoming receive.
        if (call_recorder != null)
        {
            call_recorder.stopIncoming();
            call_recorder = null;
        }
        
        System.gc();
    }

}
