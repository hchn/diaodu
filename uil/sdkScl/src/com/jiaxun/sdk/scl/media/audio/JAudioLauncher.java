/**
 * Copyright (C) 2009 The Sipdroid Open Source Project
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


import java.text.SimpleDateFormat;

import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/** Audio launcher based on javax.sound */
public class JAudioLauncher implements MediaLauncher
{
    /** 日期格式    */
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    /** Sample rate [bytes] */
    int sample_rate = 8000;
    /** Sample size [bytes] */
    int sample_size = 1;
    /** Frame size [bytes] */
    int frame_size = 160;
    /** Frame rate [frames per second] */
    boolean signed = false;
    boolean big_endian = false;

    // String filename="audio.wav";

    /** Test tone */
    public static final String TONE = "TONE";

    /** Test tone frequency [Hz] */
    public static int tone_freq = 100;
    /** Test tone ampliture (from 0.0 to 1.0) */
    public static double tone_amp = 1.0;

    /** Runtime media process */
    Process media_process = null;

    int dir; // duplex= 0, recv-only= -1, send-only= +1;

    SipdroidSocket socket = null;
    // 语音收发播放线程

    RtpStreamSender sender_Android = null;
    RtpStreamPlay play_Android = null;

    RtpStreamReceiver receiver = null;

    // 远端环回测试
    RtpStreamSenderTest senderTest = null;
    RtpStreamReceiverTest receiverTest = null;

    // change DTMF
    boolean useDTMF = false; // zero means not use outband DTMF

    private static String TAG = "JAudioLauncher";

    /** Costructs the audio launcher */
    public JAudioLauncher(int local_port, int direction, Codecs.Map payload_type, int dtmf_pt, long callStartTime)
    {
        useDTMF = (dtmf_pt != 0);
        try
        {
            CallRecorder call_recorder = null;

            if (SessionManager.getInstance().getServiceConfig().isAudioRecord)
            {// 上下行录音|上行录音|下行录音
                call_recorder = new CallRecorder(sdf.format(callStartTime), payload_type.codec.samp_rate());
            }
            Log.info(TAG, "JAudioLauncher new socket local_port=" + local_port);
            socket = new SipdroidSocket(local_port);

            dir = direction;
            // sender
            if (dir >= 0)
            {
                Log.info(TAG, "new audio sender");

                if (CommonConfigEntry.TEST_REMOTE_LOOKBACK)
                {// 远端环回
                    senderTest = new RtpStreamSenderTest(payload_type);
                }
                else
                {
                    sender_Android = new RtpStreamSender(true, payload_type, call_recorder);
                    sender_Android.setSyncAdj(2);
                    sender_Android.setDTMFpayloadType(dtmf_pt);
                }
            }

            // receiver
            if (dir <= 0)
            {
                Log.info(TAG, "new audio receiver");

                if (CommonConfigEntry.TEST_REMOTE_LOOKBACK)
                {// 远端环回
                    receiverTest = new RtpStreamReceiverTest(socket, payload_type, call_recorder);
                }
                else
                {
                    // 网络适应初始化
                    long handle = AudioNetEq.Init();
//                    long handle = poc.Init();
                    Log.info(TAG, "JAudioLauncher::handle:" + handle);
                    receiver = new RtpStreamReceiver(socket, payload_type, handle);
                    play_Android = new RtpStreamPlay(socket, payload_type, call_recorder, handle);
                }
            }
        }
        catch (Exception e)
        {
            if (socket != null)
            {
                socket.disconnect();
                socket.close();
            }

            Log.exception(TAG, e);
        }
    }

    /**
     * 设置对端地址和端口
     */
    public void setRemote(String remote_addr, int remote_port)
    {
        Log.info(TAG, "new audio sender to " + remote_addr + ":" + remote_port);
        // sender
        if (dir >= 0)
        {
            if (CommonConfigEntry.TEST_REMOTE_LOOKBACK)
            {// 远端环回
                if (senderTest != null)
                    senderTest.setRemote(socket, remote_addr, remote_port);
            }
            else
            {
                if (sender_Android != null)
                    sender_Android.setRemote(socket, remote_addr, remote_port);
            }
        }
    }

    /** Starts media application */
    public boolean startMedia()
    {
        Log.info(TAG, "starting java audio..");

        if (sender_Android != null)
        {
            Log.info(TAG, "start sending");
            sender_Android.start();
        }
        if (senderTest != null)
        {
            Log.info(TAG, "start test sending");
            senderTest.start();
        }
        if (receiver != null)
        {
            Log.info(TAG, "start receiving");
            receiver.start();
        }
        if (play_Android != null)
        {
            Log.info(TAG, "start playing");
            play_Android.start();
        }
        if (receiverTest != null)
        {
            Log.info(TAG, "start test receiving");
            receiverTest.start();
        }

        return true;
    }

    /** Stops media application */
    public boolean stopMedia()
    {
        try
        {
            if (sender_Android != null)
            {
                sender_Android.halt();
                Log.info(TAG, "stopMedia sender");
            }
            if (senderTest != null)
            {
                senderTest.halt();
                Log.info(TAG, "stopMedia senderTest");
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

        try
        {
            if (receiver != null)
            {
                receiver.halt();
                Log.info(TAG, "stopMedia receiver");
            }
            if (play_Android != null)
            {
                play_Android.halt();
                Log.info(TAG, "stopMedia play");
            }
            if (receiverTest != null)
            {
                receiverTest.halt();
                Log.info(TAG, "stopMedia receiverTest");
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        try
        {
            if (socket != null)
            {
                socket.close();
                Log.info(TAG, "close socket");
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

        return true;
    }

    public boolean halt()
    {
        try
        {
            if (sender_Android != null)
            {
                sender_Android.halt();
                sender_Android = null;
                Log.info(TAG, "halt sender");
            }
            if (senderTest != null)
            {
                senderTest.halt();
                senderTest = null;
                Log.info(TAG, "halt senderTest");
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }

        try
        {
            if (receiver != null)
            {
                receiver.halt();
                receiver = null;
                Log.info(TAG, "halt receiver");
            }
            if (play_Android != null)
            {
                play_Android.halt();
                play_Android = null;
                Log.info(TAG, "halt play");
            }
            if (receiverTest != null)
            {
                receiverTest.halt();
                receiverTest = null;
                Log.info(TAG, "halt receiverTest");
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        try
        {
            if (socket != null)
            {
                socket.close();
                socket = null;
                Log.info(TAG, "close socket");
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        return true;
    }
    
    /**
     * 控制本路是否静音
     */
    public boolean setMute(boolean mute)
    {
        boolean isMute = false;
        if (sender_Android != null)
            isMute = sender_Android.setMute(mute);
        return isMute;
    }
    
    public void setReceive(boolean receive)
    {
        receiver.setReceive(receive);
    }

    // change DTMF
    /** Send outband DTMF packets **/
    public boolean sendDTMF(char c)
    {
        if (!useDTMF)
            return false;
        if (sender_Android != null)
            sender_Android.sendDTMF(c);
        return true;
    }

    /**
     * 控制回铃
     * 
     * @param ringback
     *            是否回铃
     */
    public void ringback(boolean ringback)
    {
        if (play_Android != null)
            play_Android.ringback(ringback);
    }

}