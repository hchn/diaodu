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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.view.SurfaceView;

import com.jiaxun.sdk.util.log.Log;

/**
 * RtpVideoSender is a generic stream sender. It takes an InputStream and sends
 * it through RTP.
 */
public class VideoPreview implements PreviewCallback
{
    private static String TAG = "VideoPreview";

    private int width = 320;// 352 640 1280
    private int height = 240;// 288 480 720
    private int frameRate = 15;
    private int bitRate = 128000;
    private Camera mCamera = null; // Camera对象，相机预览
    // 初始化SurfaceView
    private SurfaceView mSurfaceView;
    private RtpVideoSender sender;
    // 单个线程队列
    private ExecutorService sendThread = Executors.newSingleThreadExecutor();

    /**
     * Constructs a VideoPreview.
     */
    public VideoPreview(int width, int height, int frameRate, int bitRate)
    {
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.bitRate = bitRate;
    }

    /**
     * 相机预览
     * */
    public void initCamera(SurfaceView surfaceView)
    {
        this.mSurfaceView = surfaceView;
        try
        {
            Log.info(TAG, "initCamera::");
            int cameraSize = Camera.getNumberOfCameras();
            if(cameraSize > 1)
            {
                mCamera = Camera.open(1);
            }
            else
            {
                mCamera = Camera.open(0);
            }
            if (mCamera == null)
            {
                return;
            }
            mCamera.setPreviewDisplay(mSurfaceView.getHolder());

            /* Camera Service settings */
            Camera.Parameters parameters = mCamera.getParameters();
            // 自动对焦
            // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            parameters.setRecordingHint(false);
            // 影像稳定
            if (parameters.isVideoStabilizationSupported())
                parameters.setVideoStabilization(true);

            List<Size> sizeList = parameters.getSupportedPreviewSizes();
            StringBuffer sb = new StringBuffer();
            for (Size size : sizeList)
            {
                sb.append(size.width + ":" + size.height + ";");
            }
            Log.info(TAG, sb.toString());

            // 分辨率
            parameters.setPreviewSize(width, height);
            Log.info(TAG, "previewFormat:" + parameters.getPreviewFormat());
            // 视频格式
            parameters.setPreviewFormat(ImageFormat.YV12);

            // 设定配置参数
            mCamera.setParameters(parameters);
            // 旋转角度
            // mCamera.setDisplayOrientation(180);

            mCamera.setPreviewCallback(this);
            mCamera.startPreview();

            // mCamera.unlock();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    // 释放照相机资源
    public void releaseCamera()
    {
        Log.info(TAG, "releaseCamera::");
        if (mCamera != null)
        {
            mCamera.setPreviewCallback(null);
            // mCamera.lock();
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera)
    {
        if (sender != null && data != null)
        {
            // sendThread.execute(new Runnable()
            // {
            // @Override
            // public void run()
            // {
            // sender.run(data);
            // }
            // });
            sender.dataQueue.add(data);
            // Log.info(TAG, "sender.dataQueue.size:" +
            // sender.dataQueue.size());
        }
    }

    public void setSender(RtpVideoSender sender)
    {
        this.sender = sender;
    }

}
