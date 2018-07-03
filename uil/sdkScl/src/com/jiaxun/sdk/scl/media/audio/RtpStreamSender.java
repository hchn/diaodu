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
import java.util.HashMap;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.SystemClock;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.acl.line.sip.codecs.Codecs.Map;
import com.jiaxun.sdk.acl.line.sip.codecs.OPUS;
import com.jiaxun.sdk.scl.util.net.RtpPacket;
import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;

/**
 * RtpStreamSender is a generic stream sender. It takes an InputStream and sends
 * it through RTP.
 */
public class RtpStreamSender extends Thread
{
    /** Whether working in info mode. */
    boolean debug = false;

    long lastTime = 0;

    /** The RtpSocket */
    RtpSocket rtp_socket = null;

    /** Payload type */
    Map p_type;

    /** Number of frame per second */
    int frame_rate;

    /** Number of bytes per frame */
    int frame_size;

    /**
     * Whether it works synchronously with a local clock, or it it acts as slave
     * of the InputStream
     */
    boolean do_sync = true;

    /**
     * Synchronization correction value, in milliseconds. It accellarates the
     * sending rate respect to the nominal value,
     * in order to compensate program latencies.
     */
    int sync_adj = 0;

    /** Whether it is running */
    boolean running = true;
    /** 全局静音开关 */
    static boolean muted = false;
    /** 单路呼叫静音开关 */
    boolean mute = false;

    // DTMF change
    String dtmf = "";
    int dtmf_payload_type = 101;

    private static HashMap<Character, Byte> rtpEventMap = new HashMap<Character, Byte>()
    {
        {
            put('0', (byte) 0);
            put('1', (byte) 1);
            put('2', (byte) 2);
            put('3', (byte) 3);
            put('4', (byte) 4);
            put('5', (byte) 5);
            put('6', (byte) 6);
            put('7', (byte) 7);
            put('8', (byte) 8);
            put('9', (byte) 9);
            put('*', (byte) 10);
            put('#', (byte) 11);
            put('A', (byte) 12);
            put('B', (byte) 13);
            put('C', (byte) 14);
            put('D', (byte) 15);
        }
    };
    // DTMF change

    CallRecorder call_recorder = null;

    private static String TAG = "RtpStreamSender";

    /**
     * Constructs a RtpStreamSender.
     * 
     * @param input_stream
     *            the stream to be sent
     * @param do_sync
     *            whether time synchronization must be performed by the
     *            RtpStreamSender, or it is performed by the
     *            InputStream (e.g. the system audio input)
     * @param payload_type
     *            the payload type
     * @param frame_rate
     *            the frame rate, i.e. the number of frames that should be sent
     *            per second; it is used to
     *            calculate the nominal packet time and,in case of
     *            do_sync==true, the next departure time
     * @param frame_size
     *            the size of the payload
     * @param src_socket
     *            the socket used to send the RTP packet
     * @param dest_addr
     *            the destination address
     * @param dest_port
     *            the destination port
     */
    public RtpStreamSender(boolean do_sync, Codecs.Map payload_type, CallRecorder rec)
    {
        init(do_sync, payload_type);
        call_recorder = rec;
    }

    /** Inits the RtpStreamSender */
    private void init(boolean do_sync, Codecs.Map payload_type)
    {
        this.frame_rate = payload_type.codec.samp_rate() / payload_type.codec.frame_size();
        this.frame_size = payload_type.codec.frame_size();
        this.do_sync = do_sync;
        this.p_type = payload_type;
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

    /** Sets the synchronization adjustment time (in milliseconds). */
    public void setSyncAdj(int millisecs)
    {
        sync_adj = millisecs;
    }

//    /** Whether is running */
//    public boolean isRunning()
//    {
//        return running;
//    }

    /**
     * 控制是否全局静音
     */
    public static boolean setMuted(boolean value)
    {
        Log.info(TAG, "setMuted:: value =" + value);
        muted = value;
        return muted;
    }

    /**
     * 控制是否单路呼叫静音
     */
    public boolean setMute(boolean value)
    {
        Log.info(TAG, "setMute:: value =" + value);
        mute = value;
        return mute;
    }

    public int delay = 0;
    public boolean init = false; // each thread should be initialized.

    /** Stops running */
    public void halt()
    {
        running = false;
    }

    private int openTimes = 3;// 采集打开失败尝试总数
    private int openCount = 0;// 采集打开失败尝试次数
    private int minBufferSize;

    /**
     * 初始化采集设备
     */
    private void init()
    {
        try
        {
            synchronized (rtpEventMap)
            {
                minBufferSize = AudioRecord.getMinBufferSize(p_type.codec.samp_rate(), AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
                Log.info(TAG, "MinBuffer size = " + minBufferSize);

                record = new AudioRecord(MediaRecorder.AudioSource.MIC, p_type.codec.samp_rate(), AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
                        minBufferSize);
                Log.info(TAG, "AudioRecord open.");

                checkDeiveStatus();
                openCount = 0;// 正常打开
            }
        }
        catch (Exception ex)
        {
            Log.exception("RtpStreamSender.startRecord", ex);
        }
    }

    /**
     * 监测采集打开状态
     */
    private void checkDeiveStatus()
    {
        if (record != null && record.getState() != AudioRecord.STATE_INITIALIZED)
        {// 没有初始化
            Log.info(TAG, "ReStart Record Device, status:" + record.getState());
            try
            {
                record.stop();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
            try
            {
                record.release();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
            try
            {
                Thread.sleep(500);
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
            if (running && openCount < openTimes)
            {
                openCount++;
                init();
            }
        }
    }

    AudioRecord record = null;

    /** Runs it in a new Thread. */
    public void run()
    {
        // 设置最高优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioSendStart(System.currentTimeMillis());
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------

        init();// 初始化采集

        int seqn = 0;
        long time = 0;
        long last_tx_time = 0;
        long next_tx_delay;
        long now;
        frame_rate = p_type.codec.samp_rate() / frame_size;
        long frame_period = 1000 / frame_rate;
        frame_rate *= 1.5;
        short[] lin = new short[frame_size * (frame_rate + 1)];
        int num = -1, ring = 0, pos;
        int dtframesize = 4;

        // ----------------------------------------OPUS
        // Begin--------------------------------
        // 获得OPUS发送频率
        int OPUSSF = frame_size / CommonConfigEntry.AUDIO_SEND_SAMPLES;
        Log.info(TAG, "OPUSSF:" + OPUSSF);
        // 初始化压缩算法
        long encoderHandle = 0;
        if (p_type.codec.number() == CommonConfigEntry.AUDIO_PAYLOADTYPE_OPUS)
        {// OPUS
            if (p_type.codec.isEnabled())
            {
                try
                {
                    encoderHandle = OPUS.initEncoder(false, CommonConfigEntry.AUDIO_BITSPERSECOND, CommonConfigEntry.AUDIO_SEND_SAMPLES);
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
                if (encoderHandle == 0)
                {
                    Log.error(TAG, "OPUS init_encoder failed");
                    return;
                }
                else
                {
                    Log.info(TAG, "OPUS init_encoder succ:" + encoderHandle);
                }
            }
            else
            {
                Log.error(TAG, "OPUS load failed");
                return;
            }
        }
        // ----------------------------------------OPUS
        // End--------------------------------

        byte[] buffer = new byte[frame_size + 12];
        RtpPacket rtp_packet = null;
        rtp_packet = new RtpPacket(buffer, 0);
        rtp_packet.setPayloadType(p_type.number);
        Log.info(TAG, "Sample rate  = " + p_type.codec.samp_rate());
        Log.info(TAG, "Frame size = " + frame_size);
        Log.info(TAG, "Buffer size = " + buffer.length);
        final long timestamp = frame_size;
        long timestampTemp = 0;

        if (CommonConfigEntry.TEST_AUDIO_LOG)
        {// 已经开启语音包打点
            debug = true;// 开启记录语音打点
        }
        else
        {
            debug = false;
        }

        long begin = 0;
        long readBegin = 0;
        long readEnd = 0;
        long encoderBegin = 0;
        long encoderEnd = 0;
        long senderBegin = 0;
        long end = 0;
        StringBuffer info = new StringBuffer();

        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioSendOk(System.currentTimeMillis());
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------

        while (running)
        {
            if (muted || mute)
            {// 静音|组呼无话权|广播参与者
                synchronized (rtpEventMap)
                {
                    if (record != null)
                    {
                        try
                        {
                            record.stop();
                        }
                        catch (Exception ex)
                        {
                            Log.exception(TAG, ex);
                        }
                    }
                }
                while (running && (muted || mute))
                {
                    try
                    {
                        sleep(20);
                    }
                    catch (InterruptedException e1)
                    {
                    }
                }
            }

            checkDeiveStatus();// 监测采集设备状态
            if (record.getRecordingState() == AudioRecord.RECORDSTATE_STOPPED)
            {// 已经停止采集
                try
                {
                    Log.info(TAG, "starRecording...");
                    record.startRecording();// 启动采集
                    Log.info(TAG, "starRecording success.");
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
            }

            // DTMF change start
            if (dtmf.length() != 0)
            {
                byte[] dtmfbuf = new byte[dtframesize + 12];
                RtpPacket dt_packet = new RtpPacket(dtmfbuf, 0);
                dt_packet.setPayloadType(dtmf_payload_type);
                dt_packet.setPayloadLength(dtframesize);
                dt_packet.setSscr(rtp_packet.getSscr());
                long dttime = time;
                int duration;

                for (int i = 0; i < 6; i++)
                {
                    time += 160;
                    duration = (int) (time - dttime);
                    dt_packet.setSequenceNumber(seqn++);
                    dt_packet.setTimestamp(dttime);
                    dtmfbuf[12] = rtpEventMap.get(dtmf.charAt(0));
                    dtmfbuf[13] = (byte) 0x0a;
                    dtmfbuf[14] = (byte) (duration >> 8);
                    dtmfbuf[15] = (byte) duration;
                    try
                    {
                        rtp_socket.send(dt_packet);
                        sleep(20);
                    }
                    catch (Exception e1)
                    {
                    }
                }
                for (int i = 0; i < 3; i++)
                {
                    duration = (int) (time - dttime);
                    dt_packet.setSequenceNumber(seqn);
                    dt_packet.setTimestamp(dttime);
                    dtmfbuf[12] = rtpEventMap.get(dtmf.charAt(0));
                    dtmfbuf[13] = (byte) 0x8a;
                    dtmfbuf[14] = (byte) (duration >> 8);
                    dtmfbuf[15] = (byte) duration;
                    try
                    {
                        rtp_socket.send(dt_packet);
                    }
                    catch (Exception e1)
                    {
                    }
                }
                time += 160;
                seqn++;
                dtmf = dtmf.substring(1);
            }
            // DTMF change end

            begin = System.currentTimeMillis();
            // 非静音|组呼有话权|广播发起者发送语音
            if (running)
            {
                if (frame_size < 960)
                {
                    now = System.currentTimeMillis();
                    next_tx_delay = frame_period - (now - last_tx_time);
                    last_tx_time = now;
                    if (next_tx_delay > 0)
                    {
                        try
                        {
                            sleep(next_tx_delay);
                        }
                        catch (InterruptedException e1)
                        {
                        }
                        last_tx_time += next_tx_delay - sync_adj;
                    }
                }
                pos = (ring + delay * frame_rate * frame_size) % (frame_size * (frame_rate + 1));
                try
                {
                    readBegin = System.currentTimeMillis();
                    if (running)
                        num = record.read(lin, pos, frame_size);// 读取采集语音数据
                    readEnd = System.currentTimeMillis();
                }
                catch (Exception e)
                {
                    Log.error(TAG, "read error.");
                    break;
                }

                if (num <= 0)
                {
                    Log.error(TAG, "record.read num:" + num);
                    continue;
                }

                if (!p_type.codec.isValid())
                {
                    Log.error(TAG, "p_type.codec.isNotValid");
                    continue;
                }

                seqn++;// 语音包序列号

                if (running && call_recorder != null)// 20130711 zhoujy
                {
                    call_recorder.writeOutgoing(lin, pos, num);// 录音：采集到的语音
                }

                try
                {
                    encoderBegin = System.currentTimeMillis();
                    if (p_type.codec.number() == CommonConfigEntry.AUDIO_PAYLOADTYPE_OPUS)// OPUS
                    {
                        for (int i = 0; i < OPUSSF; i++)
                        {
                            if (running)
                                num = OPUS.encoder(encoderHandle, lin, (pos) + i * CommonConfigEntry.AUDIO_SEND_SAMPLES, buffer, i);
                        }
                    }
                    else
                    {
                        if (running)
                            num = p_type.codec.encode(lin, pos, buffer, num);
                    }
                    encoderEnd = System.currentTimeMillis();
                }
                catch (Exception ex)
                {
                    Log.exception(TAG, ex);
                    continue;
                }

                ring += frame_size;
                senderBegin = System.currentTimeMillis();
                if (p_type.codec.number() == CommonConfigEntry.AUDIO_PAYLOADTYPE_OPUS)// OPUS
                {
                    rtp_packet.setSequenceNumber(seqn);
                    rtp_packet.setTimestamp(timestampTemp);
                    timestampTemp += timestamp;
                    rtp_packet.setPayloadLength(num * OPUSSF);
                    now = SystemClock.elapsedRealtime();
                    try
                    {
                        if (running)
                            rtp_socket.send(rtp_packet);
                    }
                    catch (Exception ex)
                    {
                        Log.error(TAG, "send error..." + ex.toString());
                    }
                    time += num * OPUSSF;
                }
                else
                {
                    rtp_packet.setSequenceNumber(seqn);
                    rtp_packet.setTimestamp(timestampTemp);
                    timestampTemp += timestamp;
                    rtp_packet.setPayloadLength(num);
                    now = SystemClock.elapsedRealtime();
                    try
                    {
                        if (running)
                            rtp_socket.send(rtp_packet);
                    }
                    catch (Exception ex)
                    {
                        Log.error(TAG, "send error.");
                    }
                    if (p_type.codec.number() == 9)
                        time += frame_size / 2;
                    else
                        time += frame_size;
                }
                end = System.currentTimeMillis();

                if (debug)
                {
                    if ((seqn % 100) == 0)
                    {// 每100个包记录一次
                        info.append("seqn:").append(seqn).append(":read tick:").append(readBegin).append(":read time:").append(readEnd - readBegin)// 记录读取时间
                                .append(":encode time:").append(encoderEnd - encoderBegin)// 记录编码时间
                                .append(":send time:").append(end - senderBegin)// 记录发送时间
                                .append(":total time:").append(end - begin);// 记录一次语音采集编码发送总时间
                        Log.info(TAG, info.toString());
                        info.delete(0, info.length());
                    }
                }
            }
        }

        synchronized (rtpEventMap)
        {
            try
            {
                Log.info(TAG, "stopRecording ...");
                record.stop();
                Log.info(TAG, "stopRecording success.");
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
            try
            {
                Log.info(TAG, "releaseRecording ...");
                record.release();
                Log.info(TAG, "releaseRecording success.");
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }

        if (p_type.codec.number() == CommonConfigEntry.AUDIO_PAYLOADTYPE_OPUS)// OPUS
        {
            if (encoderHandle != 0)
            {
                OPUS.freeEncoder(encoderHandle);
            }
        }

        p_type.codec.close();

        // Call recorder: stop recording outgoing.
        if (call_recorder != null)
        {
            call_recorder.stopOutgoing();
            call_recorder = null;
        }

        System.gc();
    }

    /** Set RTP payload type of outband DTMF packets. **/
    public void setDTMFpayloadType(int payload_type)
    {
        dtmf_payload_type = payload_type;
    }

    /** Send outband DTMF packets */
    public void sendDTMF(char c)
    {
        dtmf = dtmf + c; // will be set to 0 after sending tones
    }
    // DTMF change

}
