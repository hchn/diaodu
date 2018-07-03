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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * RtpVideoSender is a generic stream sender. It takes an InputStream and sends
 * it through RTP.
 */
public class RtpVideoSender extends Thread
{
    /** Whether working in info mode. */
    boolean info = false;

    /** Whether it is running */
    boolean running = true;

    long lastTime = 0;

    /** The RtpSocket */
    RtpSocket rtp_socket = null;

    // /** Whether it is running */
    // boolean running = false;

    private static String TAG = "RtpVideoSender";

    /**
     * Constructs a RtpStreamSender.
     */
    public RtpVideoSender(RtpSocket rtp_socket, int width, int height, int frameRate, int bitRate)
    {
        this.rtp_socket = rtp_socket;
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.bitRate = bitRate;
        try
        {
            encoder = new VideoEncoder(width, height, frameRate, bitRate);
        }
        catch (Exception e1)
        {
            Log.exception(TAG, e1);
        }
        if (test)
        {
            try
            {
                File encodeFile = new File(CommonConfigEntry.LOG_FILEPATH + "encode.h264");
                encodeFile.delete();
                encodeFile.createNewFile();
                encodeFos = new FileOutputStream(encodeFile);
                File sendFile = new File(CommonConfigEntry.LOG_FILEPATH + "send.h264");
                sendFile.delete();
                sendFile.createNewFile();
                sendFos = new FileOutputStream(sendFile);
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
    }

    private int width = 320;// 352 640 1280
    private int height = 240;// 288 480 720
    private int frameRate = 15;
    private int bitRate = 128000;

    int rtpHeadSize = 12;
    private int maxRtpPayloadLen = 1000;// 最大RTP包长度
    byte[] h264Tmp = new byte[maxRtpPayloadLen + rtpHeadSize + 100];
    RtpPacket rtp_packet = new RtpPacket(h264Tmp, h264Tmp.length);
    int seqn = 0;
    int size = 0;// 分包：剩余字节数
    int copySize = 0;// 分包：已拷贝发送字节数
    byte type = 0;
    byte nri = 0;
    long startTimestamp = System.currentTimeMillis();
    long timestamp = 0;
    int payloadType = 109;
    private byte[] sps, pps = null;
    int frameLength = 265344;
    private byte[] h264frame = new byte[frameLength];
    private byte[] h264Data = new byte[frameLength];
    private final byte[] head = new byte[] { 0, 0, 0, 1 };
    private final byte[] head3 = new byte[] { 0, 0, 1, 0x65 };
    private FileOutputStream sendFos = null;
    private FileOutputStream encodeFos = null;
    private boolean test = false;
    private VideoEncoder encoder;
    /** 消息队列 */
    public BlockingQueue<byte[]> dataQueue = new LinkedBlockingQueue<byte[]>();
    private byte[] data = null;

    // public void run(final byte[] data)
    public void run()
    {
        while (running)
        {
            data = dataQueue.poll();
            if (data == null)
            {// 没有视频数据
                try
                {
                    Thread.sleep(10);
                    continue;
                }
                catch (Exception e)
                {
                }
            }

            try
            {
                int length = encoder.encode(data, h264Data);
                // Log.error(TAG, "length:" + length);
                if (length <= 0)
                    continue;

                // byte[] bs = new byte[length];
                // System.arraycopy(h264Data, 0, bs, 0, length);
                // Log.error(TAG, Arrays.toString(bs));
                if (test)
                    encodeFos.write(h264Data, 0, length);

                length -= 4;
                System.arraycopy(h264Data, 4, h264frame, 0, length);
                // System.arraycopy(h264Data, 0, h264frame, 0, length);
                // Log.error(TAG, "h264frame::h264frame[0]:" + h264frame[0]
                // + "length:" + length);

                long ssrc = rtp_packet.getSscr();
                timestamp = getTimeStamp(startTimestamp);

                if (h264frame[0] == 0x67)
                {// SPS和PPS帧/SPS和PPS和I帧
                    if (sps == null)
                    {// 第一个包，获取sps和pps
                        int head2Index = 0;
                        for (int i = 0; i < h264frame.length; i++)
                        {
                            if (head[0] == h264frame[i] && head[1] == h264frame[i + 1] && head[2] == h264frame[i + 2] && head[3] == h264frame[i + 3])
                            {
                                head2Index = i;
                                break;
                            }
                        }
                        sps = new byte[head2Index];
                        System.arraycopy(h264frame, 0, sps, 0, head2Index);
                        pps = new byte[length - head2Index - 4];
                        System.arraycopy(h264frame, head2Index + 4, pps, 0, pps.length);
                    }

                    // SPS
                    System.arraycopy(sps, 0, h264Tmp, rtpHeadSize, sps.length);
                    rtp_packet.setSscr(ssrc);
                    rtp_packet.setPayloadType(payloadType);
                    rtp_packet.setSequenceNumber(seqn);
                    seqn++;
                    rtp_packet.setTimestamp(timestamp);
                    rtp_packet.setPayloadLength(sps.length);
                    rtp_packet.setMarker(false);
                    try
                    {
                        rtp_socket.send(rtp_packet);
                        if (test)
                            sendFos.write(rtp_packet.getPayload());
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                    }

                    // PPS
                    System.arraycopy(pps, 0, h264Tmp, rtpHeadSize, pps.length);
                    rtp_packet.setSscr(ssrc);
                    rtp_packet.setPayloadType(payloadType);
                    rtp_packet.setSequenceNumber(seqn);
                    seqn++;
                    rtp_packet.setTimestamp(timestamp);
                    rtp_packet.setPayloadLength(pps.length);
                    rtp_packet.setMarker(false);
                    try
                    {
                        rtp_socket.send(rtp_packet);
                        if (test)
                            sendFos.write(rtp_packet.getPayload());
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                    }

                    int iLength = sps.length + pps.length + 4;
                    if (length == iLength)
                    {// 第一帧（SPS和PPS帧）
                        continue;
                    }
                    else if (length > iLength)
                    {// SPS和PPS和I帧
                        length -= iLength + 4;
                        System.arraycopy(h264Data, iLength + 8, h264frame, 0, length);
                    }
                }

                if (length <= maxRtpPayloadLen)
                {// 单包
                    System.arraycopy(h264frame, 0, h264Tmp, rtpHeadSize, length);
                    rtp_packet.setSscr(ssrc);
                    rtp_packet.setPayloadType(payloadType);
                    rtp_packet.setSequenceNumber(seqn);
                    seqn++;
                    if (seqn == 3)
                        rtp_packet.setTimestamp(timestamp);
                    else
                        rtp_packet.setTimestamp(getTimeStamp(startTimestamp));
                    rtp_packet.setPayloadLength(length);
                    rtp_packet.setMarker(true);
                    try
                    {
                        rtp_socket.send(rtp_packet);
                        if (test)
                            sendFos.write(rtp_packet.getPayload());
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                    }
                }
                else
                {// 分包
                    type = (byte) (h264frame[0] & 0x1F);
                    nri = (byte) (h264frame[0] & 0x60);

                    System.arraycopy(h264frame, 1, h264Tmp, 14, maxRtpPayloadLen - 1);
                    size = length - maxRtpPayloadLen;
                    copySize = maxRtpPayloadLen;

                    // 第一个包
                    h264Tmp[12] = 28;
                    h264Tmp[12] |= nri;
                    h264Tmp[13] = type;
                    h264Tmp[13] |= 1 << 7;
                    // Log.error(TAG, "h264frame::h264Tmp[13]1:" +
                    // h264Tmp[13]);

                    rtp_packet.setPayloadLength(maxRtpPayloadLen + 1);

                    rtp_packet.setSscr(ssrc);
                    rtp_packet.setPayloadType(payloadType);
                    rtp_packet.setSequenceNumber(seqn);
                    seqn++;
                    rtp_packet.setTimestamp(timestamp);
                    rtp_packet.setMarker(false);
                    try
                    {
                        rtp_socket.send(rtp_packet);
                        if (test)
                            sendFos.write(rtp_packet.getPayload());
                    }
                    catch (Exception e)
                    {
                        Log.exception(TAG, e);
                    }

                    while (size > maxRtpPayloadLen)
                    {// 按顺序分包
                        System.arraycopy(h264frame, copySize, h264Tmp, 14, maxRtpPayloadLen);
                        size -= maxRtpPayloadLen;
                        copySize += maxRtpPayloadLen;

                        // 中间包
                        h264Tmp[12] = 28;
                        h264Tmp[12] |= nri;
                        h264Tmp[13] = type;
                        h264Tmp[13] |= 1 << 7;

                        h264Tmp[13] &= ~(1 << 7);
                        // Log.error(TAG, "h264frame::h264Tmp[13]z:" +
                        // h264Tmp[13]);

                        rtp_packet.setPayloadLength(maxRtpPayloadLen + 2);

                        rtp_packet.setSscr(ssrc);
                        rtp_packet.setPayloadType(payloadType);
                        rtp_packet.setSequenceNumber(seqn);
                        seqn++;
                        rtp_packet.setTimestamp(timestamp);
                        rtp_packet.setMarker(false);
                        try
                        {
                            rtp_socket.send(rtp_packet);
                            if (test)
                                sendFos.write(rtp_packet.getPayload());
                        }
                        catch (Exception e)
                        {
                            Log.exception(TAG, e);
                        }
                    }

                    if (size > 0)
                    {
                        // 最后一个包
                        System.arraycopy(h264frame, copySize, h264Tmp, 14, size);

                        h264Tmp[12] = 28;
                        h264Tmp[12] |= nri;
                        h264Tmp[13] = type;
                        h264Tmp[13] |= 1 << 6;
                        // Log.error(TAG, "h264frame::h264Tmp[13]zz:" +
                        // h264Tmp[13]);

                        rtp_packet.setSscr(ssrc);
                        rtp_packet.setPayloadType(payloadType);
                        rtp_packet.setSequenceNumber(seqn);
                        seqn++;
                        rtp_packet.setTimestamp(timestamp);
                        rtp_packet.setPayloadLength(size + 2);
                        rtp_packet.setMarker(true);
                        try
                        {
                            rtp_socket.send(rtp_packet);
                            if (test)
                                sendFos.write(rtp_packet.getPayload());
                        }
                        catch (Exception e)
                        {
                            Log.exception(TAG, e);
                        }
                    }

                    // 还原
                    size = 0;
                    copySize = 0;
                }
                
              Log.info(TAG, "seqn:" + rtp_packet.getSequenceNumber());
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
        
        if(encoder != null)
            encoder.close();
        
        try
        {
            if (sendFos != null)
                sendFos.close();
            if (encodeFos != null)
                encodeFos.close();
        }
        catch (IOException e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * 传输：时间戳
     */
    private long getTimeStamp(long timestamp)
    {
        return (System.currentTimeMillis() - timestamp) * 90;
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

}
