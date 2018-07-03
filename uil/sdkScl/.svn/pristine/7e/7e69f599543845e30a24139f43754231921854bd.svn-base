package com.jiaxun.sdk.scl.media.video;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * ËµÃ÷£ºÔ¶¶ËÊÓÆµSurfaceView
 *
 * @author  hubin
 *
 * @Date 2015-4-29
 */
public class RemoteVideoView extends SurfaceView implements Callback
{
    private final SurfaceHolder mHolder;
    private final int width;
    private final int height;
    private final int frameRate;
    private final int bitRate;
    private final String videoSource;
    private JVideoLauncher videoLauncher;

    public RemoteVideoView(Context context, int width, int height, int frameRate, int bitRate, String videoSorce, JVideoLauncher videoLauncher)
    {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.bitRate = bitRate;
        this.videoSource = videoSorce;
        this.videoLauncher = videoLauncher;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (videoLauncher != null)
        {
            videoLauncher.startRemoteVideo(videoSource, width, height, frameRate, bitRate, this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        stopVideo();
    }

    public void stopVideo()
    {
        if (videoLauncher != null)
        {
            videoLauncher.stopRemoteVideo(videoSource);
        }
    }

}
