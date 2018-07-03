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

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * RtpStreamPlay is a generic video stream player.
 */
public class RtpVideoPlay extends Thread
{

    /** Whether working in debug mode. */
    boolean DEBUG = false;

    /** Whether it is running */
    boolean running = true;

    private static String TAG = "RtpVideoPlay";
    
 // 初始化SurfaceView
    private SurfaceView mSurfaceView;

    /**
     * Constructs a RtpStreamPlay.
     */
    public RtpVideoPlay(int width, int height, int frameRate, int bitRate, SurfaceView surfaceView)
    {
        this.mSurfaceView = surfaceView;
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.bitRate = bitRate;
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
   
    /** 消息队列 */
    public BlockingQueue<RtpPacket> rtpQueue = new LinkedBlockingQueue<RtpPacket>();
    
    private int width = 320;// 352 640 1280
    private int height = 240;// 288 480 720
    private int frameRate = 15;
    private int bitRate = 128000;
    
    private boolean test = false;

    /** Runs it in a new Thread. */
    public void run()
    {
     // 设置最高优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        
        FileOutputStream receiveFos = null;
        FileOutputStream decodeFos = null;
        if(test)
        {
            try
            {
                File receiveFile = new File(CommonConfigEntry.LOG_FILEPATH + "receive.h264");
                receiveFile.delete();
                receiveFile.createNewFile();
                receiveFos = new FileOutputStream(receiveFile);
                File decodeFile = new File(CommonConfigEntry.LOG_FILEPATH + "decode.h264");
                decodeFile.delete();
                decodeFile.createNewFile();
                decodeFos = new FileOutputStream(decodeFile);
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }

        VideoDecoder decoder = new VideoDecoder(width, height, frameRate, bitRate, mSurfaceView.getHolder().getSurface());
        RtpPacket rtp_packet = null;
        byte[] head = new byte[] { 0, 0, 0, 1 };
        int frameLength = 530688;
        byte[] data = new byte[frameLength];
        int dataLength = 0;
        byte[] payload = null;
        int payloadLength = 0;

        while (running)
        {
            try
            {
                rtp_packet = rtpQueue.poll();
                if (rtp_packet == null)
                {// 没有语音包
                    try
                    {
                        Thread.sleep(20);
                        continue;
                    }
                    catch (Exception e)
                    {
                    }
                }
                
                payload = rtp_packet.getPayload();
                payloadLength = rtp_packet.getPayloadLength();
                if(test)
                    receiveFos.write(payload);
                // Log.error(TAG, "payload[0]:" + payload[0] +
                // "payload[1]:" + payload[1]);
                // Log.error(TAG, "seqn:" +
                // rtp_receive_packet.getSequenceNumber() +
                // " payloadLength:" + payloadLength +
                // " hasMarker():" +
                // rtp_receive_packet.hasMarker());
                if (payload[0] == 0x67)
                {// SPS
                    System.arraycopy(head, 0, data, 0, head.length);
                    dataLength += head.length;
                    System.arraycopy(payload, 0, data, dataLength, payloadLength);
                    dataLength += payloadLength;
                }
                else if (payload[0] == 0x68)
                {// PPS
                    System.arraycopy(head, 0, data, dataLength, head.length);
                    dataLength += head.length;
                    System.arraycopy(payload, 0, data, dataLength, payloadLength);
                    dataLength += payloadLength;
                    decoder.decode(data, dataLength);
                    if(test)
                        decodeFos.write(data, 0, dataLength);
                    dataLength = 0;
                }
                else
                {
                    if (rtp_packet.hasMarker())
                    {
                        if (dataLength == 0)
                        {// 单帧
                            System.arraycopy(head, 0, data, 0, head.length);
                            dataLength += head.length;
                            System.arraycopy(payload, 0, data, dataLength, payloadLength);
                            dataLength += payloadLength;
                            decoder.decode(data, dataLength);
                            if(test)
                                decodeFos.write(data, 0, dataLength);
                            dataLength = 0;
                        }
                        else
                        {// 分包最后一个包
                            if(payload[0] == 0x65 || payload[0] == 0x41)
                            {// I帧/P帧
                                System.arraycopy(head, 0, data, dataLength, head.length);
                                dataLength += head.length;
                                
                                System.arraycopy(payload, 0, data, dataLength, payloadLength);
                                dataLength += payloadLength;
                            }
                            else
                            {//fu-a
                                System.arraycopy(payload, 2, data, dataLength, payloadLength - 2);
                                dataLength += payloadLength - 2;
                            }
                            
                            // byte[] bs = new byte[dataLength];
                            // System.arraycopy(data, 0, bs, 0,
                            // dataLength);
                            // Log.error(TAG, Arrays.toString(bs));
                            
                            decoder.decode(data, dataLength);
                            if(test)
                                decodeFos.write(data, 0, dataLength);
                            dataLength = 0;
                        }
                    }
                    else
                    {// 分包非最后一个包
                        if(payload[0] == 0x65 || payload[0] == 0x41)
                        {// I帧/P帧
                            System.arraycopy(head, 0, data, dataLength, head.length);
                            dataLength += head.length;
                            
                            System.arraycopy(payload, 0, data, dataLength, payloadLength);
                            dataLength += payloadLength;
                        }
                        else
                        {//fu-a
                            // 第一个包标识帧类型
                            if (payload[1] == -123)
                            {// I帧
                                System.arraycopy(head, 0, data, 0, head.length);
                                data[head.length] = 0x65;
                                dataLength = 0;
                                dataLength += head.length + 1;
                            }
                            else if (payload[1] == -127)    
                            {// P帧
                                System.arraycopy(head, 0, data, 0, head.length);
                                data[head.length] = 0x41;
                                dataLength = 0;
                                dataLength += head.length + 1;
                            }
                            
                            if(dataLength < data.length)
                            {// 缓冲区范围内
                                System.arraycopy(payload, 2, data, dataLength, payloadLength - 2);
                                dataLength += payloadLength - 2;
                            }
                            else
                            {// 超出缓冲区限制
                                dataLength = 0;
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
    
        if(decoder != null)
            decoder.close();
        try
        {
            if(receiveFos != null)
                receiveFos.close();
            if(decodeFos != null)
                decodeFos.close();
        }
        catch (IOException e)
        {
            Log.exception(TAG, e);
        }
    }

}
