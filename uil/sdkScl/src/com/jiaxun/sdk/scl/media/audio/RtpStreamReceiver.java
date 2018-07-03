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
import android.os.SystemClock;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;

/**
 * RtpStreamReceiver is a generic stream receiver. It receives packets from RTP
 * and writes them into an OutputStream.
 */
public class RtpStreamReceiver extends Thread
{

    /** Whether working in info mode. */
    public static boolean debug = false;

    /** Payload type */
    Codecs.Map p_type;

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
    
    private static String TAG = "RtpStreamReceiver";
    
    private boolean isReceive = true;
    
    private long handle;

    /**
     * Constructs a RtpStreamReceiver.
     * 
     * @param output_stream
     *            the stream sink
     * @param socket
     *            the local receiver SipdroidSocket
     */
    public RtpStreamReceiver(SipdroidSocket socket, Codecs.Map payload_type, long handle)
    {
        init(socket);
        if(p_type == null || p_type.codec.samp_rate() != payload_type.codec.samp_rate())
        {//如果采样率为空，或者发生变化
            p_type = payload_type;
        }
        p_type = payload_type;
        this.handle = handle;
    }

    /** Inits the RtpStreamReceiver */
    private void init(SipdroidSocket socket)
    {
        if (socket != null)
            rtp_socket = new RtpSocket(socket);
    }

//    /** Whether is running */
//    public boolean isRunning()
//    {
//        return running;
//    }

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
        
        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioReceiveStart(System.currentTimeMillis());
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------
        
        byte[] buffer;
        if(CommonConfigEntry.TEST_AUDIO_LOG)
        {//已经开启开启语音包打点
            debug = true;//开启记录语音打点
        }
        else
        {
            debug = false;
        }
        
        long begin = 0;
        long receiveEnd = 0;
        StringBuffer info = new StringBuffer();
        int seqLast = 0;//记录丢失的语音包序列号，输出到日志
        int bufferSize  = p_type.codec.frame_size() + 12;
        Log.info(TAG, "bufferSize:" + bufferSize);
        int maxRtpPackets = CommonConfigEntry.RTP_CACHE_PACKETS * 3;
        Log.info(TAG, "maxRtpPackets:" + maxRtpPackets);
        
        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioReceiveOk(System.currentTimeMillis());
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------

        buffer = new byte[bufferSize];
        rtp_packet = new RtpPacket(buffer, 0);
        int result = -1;
        
        while (running)
        {
            while (running && !isReceive)
            {
                SystemClock.sleep(20);
            }
            begin = System.currentTimeMillis();
            
            try
            {
                rtp_socket.receive(rtp_packet);//接收语音包
//                Log.info(TAG, "RecIn begin length:" + rtp_packet.getLength());
                byte[] bs = rtp_packet.getPacket();
//                Log.info(TAG, "RecIn begin array:" + Arrays.toString(bs));
//                result = poc.RecIn(handle, bs, rtp_packet.getLength());
                result = AudioNetEq.RecIn(handle, bs, rtp_packet.getLength());
//                Log.info(TAG, "RecIn end result:" + result);
            }
            catch (Exception ex)
            {
                Log.error(TAG, "receive error.");
                continue;
            }
            
            receiveEnd = System.currentTimeMillis();
                                
            if(debug)
            {
                if((rtp_packet.getSequenceNumber() % 100) == 0)
                {//每100个包或时延大于80ms记录一次
                    info.append("seqn:")
                        .append(rtp_packet.getSequenceNumber())
                        .append(":receive tick:")
                        .append(receiveEnd)
                        .append(":receive time:")
                        .append(receiveEnd - begin);//记录接收包时间
                    Log.info(TAG, info.toString());
                    info.delete(0, info.length());
                }
            }
            
            if((rtp_packet.getSequenceNumber() - seqLast) != 0 && (rtp_packet.getSequenceNumber() - seqLast) != 1)
            {//根据序列号判断是否丢包，并记录丢失包语音包
                Log.info(TAG, "lastSeq:" + seqLast + ":curSeq:" + rtp_packet.getSequenceNumber());
            }
            seqLast = rtp_packet.getSequenceNumber();
        }
        
        System.gc();
    }

    public void setReceive(boolean isReceive)
    {
        this.isReceive = isReceive;
    }
    
    public long getHandle()
    {
        return this.handle;
    }

}
