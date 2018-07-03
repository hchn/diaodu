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

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.ToneGenerator;
import android.os.SystemClock;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;

/**
 * RtpStreamPlay is a generic stream receiver. It receives packets from RTP
 * and writes them into an OutputStream.
 */
public class RtpStreamPlay extends Thread
{

    /** Whether working in info mode. */
    boolean debug = false;

    /** Payload type */
    Codecs.Map p_type;

    /** Size of the read buffer */
    final int BUFFER_SIZE = 1024;

    /**
     * Maximum blocking time, spent waiting for reading new bytes [milliseconds]
     */
    final int SO_TIMEOUT = 1000 * 60 * 5;

    /** Whether it is running */
    boolean running = true;
    ContentResolver cr;
    CallRecorder call_recorder = null;
    
    long handle;

    private static String TAG = "RtpStreamPlay";

    /**
     * Constructs a RtpStreamPlay.
     * 
     * @param output_stream
     *            the stream sink
     * @param socket
     *            the local receiver SipdroidSocket
     */
    public RtpStreamPlay(SipdroidSocket socket, Codecs.Map payload_type, CallRecorder rec, long handle)
    {
        this.p_type = payload_type;
        this.call_recorder = rec;
        this.handle = handle;
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

    ToneGenerator ringbackPlayer;
    boolean ringbackPlay = false;//播放状态

    /**
     * 同步控制回铃
     * 
     * @param ringback
     *            是否回铃
     */
    public synchronized void ringback(boolean ringback)
    {
        Log.info(TAG, "ringback:" + ringback);
        
        if(ringbackPlayer == null)
        {// 初始化一次
            ringbackPlayer = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, (int) (ToneGenerator.MAX_VOLUME * 4 * CommonConfigEntry.EARGAIN));
        }

        if (ringback)
        {// 开始播放
            if(ringbackPlay)
            {//正在播放
                ringbackPlayer.stopTone();
            }
            ringbackPlayer.startTone(ToneGenerator.TONE_SUP_RINGTONE);
            ringbackPlay = true;
        }
        else
        {// 停止播放
            if(ringbackPlay)
            {//正在播放
                ringbackPlayer.stopTone();
            }
            ringbackPlay = false;
        }
    }

    private AudioTrack track;
    private int maxjitter;
    private int user;

    void write(short a[], int b, int c)
    {
        synchronized (this)
        {
            try
            {
                user += track.write(a, b, c);
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }
    }

//    private ToneGenerator tg;

    /**
     * 打开播放相关设备
     */
    private void init()
    {
        try
        {
//            tg = new ToneGenerator(AudioManager.STREAM_VOICE_CALL, (int) (ToneGenerator.MAX_VOLUME * 4 * CommonConfigEntry.EARGAIN));
            p_type.codec.init();
            maxjitter = AudioTrack.getMinBufferSize(p_type.codec.samp_rate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            Log.info(TAG, "maxjitter:" + maxjitter);
            track = new AudioTrack(AudioManager.STREAM_VOICE_CALL, p_type.codec.samp_rate(), AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                    maxjitter, AudioTrack.MODE_STREAM);
            AudioManager am = (AudioManager) SdkUtil.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, (int) (am.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)), 0);
            Log.info(TAG, "AudioPlay open.");
            
            checkDeiveStatus();
            openCount = 0;// 正常打开
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

    }

    private int openTimes = 3;// 采集打开失败尝试总数
    private int openCount = 0;// 采集打开失败尝试次数

    /**
     * 监测播放设备打开状态
     */
    private void checkDeiveStatus()
    {
        if (track != null && track.getState() != AudioRecord.STATE_INITIALIZED)
        {// 没有初始化
            Log.info(TAG, "ReStart Play Device, status:" + track.getState());
            try
            {
                track.stop();
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
            try
            {
                track.release();
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

    /** Runs it in a new Thread. */
    public void run()
    {
        // 设置最高优先级
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioReadyStart(System.currentTimeMillis());
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------
        
        init();//初始化播放
        
        byte[] buffer;
        short lin[] = new short[80];
        int len = 80;
        int couts = 0;//语音流每个包字节数
        int packetCounts = 2;//语音流包数
        
        // ----------------------------------------OPUS
        // Begin--------------------------------
//        long decoderHandle = 0;
//        if (p_type.codec.number() == CommonConfigEntry.AUDIO_PAYLOADTYPE_OPUS)
//        {// OPUS
//            if (p_type.codec.isEnabled())
//            {
//                try
//                {
//                    decoderHandle = OPUS.initDecoder();
//                }
//                catch (Exception e)
//                {
//                    Log.exception(TAG, e);
//                }
//                if (decoderHandle == 0)
//                {
//                    Log.error(TAG, "OPUS init_encoder failed");
//                    return;
//                }
//            }
//            else
//            {
//                Log.error(TAG, "OPUS load failed");
//                return;
//            }
//        }
        // ----------------------------------------OPUS

        if (CommonConfigEntry.TEST_AUDIO_LOG)
        {// 已经开启开启语音包打点
            debug = true;// 开启记录语音打点
        }
        else
        {
            debug = false;
        }

        long begin = 0;
        long decoderBegin = 0;
        long decoderEnd = 0;
        long writeEnd = 0;
        long end = 0;
        StringBuffer info = new StringBuffer();
        long playTime = 35;// 播放间隔时间
        long maxPlayTime = 45;// 最大播放时间间隔
        long minPlayTime = 25;// 最小播放时间间隔
        int maxRtpSize = CommonConfigEntry.RTP_CACHE_PACKETS * 2;// 语音包最多缓存范围
        int minRtpSize = CommonConfigEntry.RTP_CACHE_PACKETS;// 语音包最小缓存范围
        int lastReq = 0;// 上一个包序号
        Log.info(TAG, "rtpCachePackets:" + CommonConfigEntry.RTP_CACHE_PACKETS);

        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioReadyOkGroup(System.currentTimeMillis());// 组呼
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------

        // --------------------------------呼叫发起性能测试日志记录
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setAudioReadyOkSingle(System.currentTimeMillis());// 单呼
        }
        // --------------------------------呼叫发起性能测试日志记录
        // End----------------------------------------------
        
        int result = -1;
        final long sleep = 10;// 10ms读取一次
        long sleepTime = 10;

        while (running)
        {// 呼叫结束时将缓存语音包全部播放完毕
            try
            {
                begin = SystemClock.elapsedRealtime();

                checkDeiveStatus();// 监测播放设备状态
                try
                {
                    if (track.getPlayState() == AudioTrack.PLAYSTATE_STOPPED)
                    {// 已经停止播放
                        Log.info(TAG, "play...");
                        track.play();
                        Log.info(TAG, "play success.");
                    }
                }
                catch (Exception e1)
                {
                    Log.exception(TAG, e1);
                }

                decoderBegin = SystemClock.elapsedRealtime();
//                Log.info(TAG, "run::RecOut::begin");
                result = AudioNetEq.RecOut(handle, lin);
//                result = poc.RecOut(handle, lin);
//                Log.info(TAG, "run::RecOut::result:" + result);
//                Log.info(TAG, "RecOut::result:" + result + " lin:" + Arrays.toString(lin));
                if(result < 0)
                    continue;
                decoderEnd = SystemClock.elapsedRealtime();

                if (running)
                    write(lin, 0, len);// 写入播放
                writeEnd = SystemClock.elapsedRealtime();

                if (running && call_recorder != null)// 20130711 zhoujy
                {
                    call_recorder.writeIncoming(lin, 0, len);// 录音：接收语音
                }
                end = SystemClock.elapsedRealtime();

                sleepTime = sleep - (end - begin);
//                Log.info(TAG, "sleepTime:" + sleepTime + ":decode time:" + (decoderEnd - decoderBegin) + " write time:" + (writeEnd - decoderEnd) + " total time:" + (end - begin));
//                if(sleepTime > 0)
//                    Thread.sleep(sleepTime);// 间隔读取一次
                
//                if (debug)
//                {// 开启语音打点
//                    if ((rtp_packet.getSequenceNumber() % 100) == 0)
//                    {// 每100个包记录一次
//                        info.append("seqn:")
//                          .append(rtp_packet.getSequenceNumber())
//                          .append(":write tick:")
//                          .append(writeEnd)
//                          .append(":decode time:")
//                          .append(decoderEnd - decoderBegin)// 记录解码时间
//                          .append(":write time:")
//                          .append(writeEnd - decoderEnd)// 记录写入播放时间
//                          .append(":total time:")
//                          .append(end - begin)// 记录一次接收解码播放时间
//                          .append(":playTime:")
//                          .append(playTime)// 记录播放间隔
//                          .append(":size:");
//                     Log.info(TAG, info.toString());
//                     info.delete(0, info.length());
//                 }
//                }
            }
            catch (Exception ex)
            {
                Log.exception(TAG, ex);
            }
        }

        try
        {
            Log.info(TAG, "stopTrack ...");
            track.stop();
            Log.info(TAG, "stopTrack success.");
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        try
        {
            Log.info(TAG, "releaseTrack ...");
            track.release();
            Log.info(TAG, "releaseTrack success.");
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

//        try
//        {
//            tg.stopTone();
//        }
//        catch (Exception ex)
//        {
//            Log.exception(TAG, ex);
//        }

//        if (p_type.codec.number() == CommonConfigEntry.AUDIO_PAYLOADTYPE_OPUS)// OPUS
//        {
//            if (decoderHandle != 0)
//            {
//                try
//                {
//                    OPUS.freeDecoder(decoderHandle);
//                }
//                catch (Exception e)
//                {
//                    Log.exception(TAG, e);
//                }
//            }
//        }

        try
        {
            p_type.codec.close();
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

        // Call recording: stop incoming receive.
        if (call_recorder != null)
        {
            call_recorder.stopIncoming();
            call_recorder = null;
        }
        
//        result = poc.Release(handle);// 
        result = AudioNetEq.Release(handle);// 
        Log.info(TAG, "run::result:" + result);
        
        System.gc();
    }

    public static int byte2int(byte b)
    { // return (b>=0)? b : -((b^0xFF)+1);
      // return (b>=0)? b : b+0x100;
        return (b + 0x100) % 0x100;
    }

    public static int byte2int(byte b1, byte b2)
    {
        return (((b1 + 0x100) % 0x100) << 8) + (b2 + 0x100) % 0x100;
    }

}
