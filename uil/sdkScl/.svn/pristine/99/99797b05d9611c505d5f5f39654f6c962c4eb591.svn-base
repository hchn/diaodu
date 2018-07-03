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
package com.jiaxun.sdk.scl.media.video;

import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.util.net.RtpSocket;
import com.jiaxun.sdk.scl.util.net.SipdroidSocket;
import com.jiaxun.sdk.util.log.Log;

/** Audio launcher based on javax.sound */
public class JVideoLauncher
{

    /** The RtpSocket */
    RtpSocket rtp_socket = null;
    SipdroidSocket socket = null;

    RtpVideoSender sender = null;
    VideoPreview preview = null;

    RtpVideoReceiver receiver = null;

    /** tag:视频播放对象 */
    ConcurrentHashMap<Integer, RtpVideoPlay> playMap = new ConcurrentHashMap<Integer, RtpVideoPlay>();

    private static String TAG = "JVideoLauncher";

    /** 
     * 本地视频预览构造
     **/
    public JVideoLauncher(int localVideoPort, int remoteVideoPort, String remoteMediaAddress, VideoPreview preview)
    {
        try
        {
            Log.info(TAG, "new socket local_port=" + localVideoPort + " dest_addr:" + remoteMediaAddress + " dest_port:" + remoteVideoPort);
            socket = new SipdroidSocket(localVideoPort);
            rtp_socket = new RtpSocket(socket, InetAddress.getByName(remoteMediaAddress), remoteVideoPort);
            this.preview = preview;
            receiver = new RtpVideoReceiver(rtp_socket, playMap);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            if (socket != null)
            {
                socket.close();
            }
        }
    }

    /**
     * 启动本地视频发送
     */
    public void startLocalVideoSender(int width, int height, int frameRate, int bitRate)
    {
        Log.info(TAG, "startLocalVideoSender::width:" + width + " height:" + height + " frameRate:" + frameRate + " bitRate:" + bitRate);
        if (sender == null)
            sender = new RtpVideoSender(rtp_socket, width, height, frameRate, bitRate);
        if (preview != null)
        {
            preview.setSender(sender);
        }
        sender.start();
    }

    /**
     * 停止本地视频发送
     */
    public void stopLocalVideoSender()
    {
        Log.info(TAG, "stopLocalVideoSender");
        if (preview != null)
        {
            preview.setSender(null);
        }
        if (sender != null)
        {
            sender.halt();
            sender = null;
        }
    }

    /**
     * 启动远程视频接收播放
     */
    public void startRemoteVideo(String tag, int width, int height, int frameRate, int bitRate, SurfaceView remote)
    {
        Log.info(TAG, "startRemoteVideo::tag:" + tag + " width:" + width + " height:" + height + " frameRate:" + frameRate + " bitRate:" + bitRate);
        if (tag == null || tag.equals("") || remote == null)
            return;

        try
        {
            int tagI = Integer.parseInt(tag);
            RtpVideoPlay play = playMap.get(tagI);
            if (play == null || (!play.isAlive() && !play.isRunning()))
            {
                play = new RtpVideoPlay(width, height, frameRate, bitRate, remote);
                playMap.put(tagI, play);
            }

            // 单呼视频
            if (tagI == 0)
                receiver.setTag(tagI);

            if (!receiver.isAlive())
            {
                receiver.start();
            }
            if (!play.isAlive())
            {
                play.start();
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

    }

    /**
     * 停止远程视频接收播放
     */
    public void stopRemoteVideo(String tag)
    {
        Log.info(TAG, "stopRemoteVideo::tag:" + tag);
        try
        {
            int tag1 = Integer.parseInt(tag);
            RtpVideoPlay play = playMap.get(tag1);
            if (play != null)
            {
                Log.info(TAG, "play.halt");
                play.halt();
                playMap.remove(tag1);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /** Stops video */
    public void halt()
    {
        Log.info(TAG, "halt");
        try
        {
            receiver.halt();
            stopLocalVideoSender();

            for (int tag : playMap.keySet())
            {
                Log.info(TAG, "stop remote");
                if (playMap.get(tag) != null)
                {
                    playMap.get(tag).halt();
                }
            }
            playMap.clear();

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
    }

}