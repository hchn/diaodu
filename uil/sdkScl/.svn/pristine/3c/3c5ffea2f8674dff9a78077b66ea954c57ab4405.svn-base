/**
 * 
 */
package com.jiaxun.sdk.scl.media.video;

import com.jiaxun.sdk.util.log.Log;

import android.app.ActionBar.Tab;
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * 说明：本地视频SurfaceView
 *
 * @author hubin
 *
 * @Date 2015-4-29
 */
public class LocalVideoView extends SurfaceView implements Callback
{
    private static final String TAG = LocalVideoView.class.getName();
    private final SurfaceHolder mHolder;
    private final int width;
    private final int height;
    private final int frameRate;
    private final int bitRate;
    private VideoPreview videoPreview;

    public LocalVideoView(Context context, int width, int height, int frameRate, int bitRate)
    {
        super(context);
        mHolder = getHolder();
        mHolder.addCallback(this);
        this.width = width;
        this.height = height;
        this.frameRate = frameRate;
        this.bitRate = bitRate;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
//        if (mHolder != null)
//        {
//            mHolder.addCallback(this);
//        }
//        videoPreview.initCamera(this);
        Log.info(TAG, "surfaceCreated::");
        this.videoPreview = new VideoPreview(width, height, frameRate, bitRate);
        videoPreview.initCamera(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        Log.info(TAG, "surfaceChanged::");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.info(TAG, "surfaceDestroyed::");
//        if (mHolder != null)
//        {
//            mHolder.removeCallback(this);
//        }
//        
        if (videoPreview != null)
        {
            videoPreview.releaseCamera();
            videoPreview = null;
        }
    }

//    public void stopVideo()
//    {
//        if (mHolder != null)
//        {
//            mHolder.removeCallback(this);
//        }
//        
//        if (videoPreview != null)
//        {
//            videoPreview.releaseCamera();
//        }
//    }

    public VideoPreview getVideoPreview()
    {
        return videoPreview;
    }

}
