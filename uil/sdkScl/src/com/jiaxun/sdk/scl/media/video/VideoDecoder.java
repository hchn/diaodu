package com.jiaxun.sdk.scl.media.video;

import java.nio.ByteBuffer;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.view.Surface;

import com.jiaxun.sdk.util.log.Log;

public class VideoDecoder
{
    private static String TAG = "VideoDecoder";
    private MediaCodec decoder;
    private int mWidth, mHeight;

    public VideoDecoder(int width, int height, int framerate, int bitrate, Surface surface)
    {
        Log.info(TAG, "VideoDecoder::");
        mWidth = width;
        mHeight = height;

        decoder = MediaCodec.createDecoderByType("video/avc");
        MediaFormat decodeFormat = MediaFormat.createVideoFormat("video/avc", width, height);
        decodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        decodeFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
        decodeFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar); // <2>
        decodeFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        try
        {
            decoder.configure(decodeFormat, surface, null, 0);
            Log.info(TAG, "startEncoder...");
            decoder.start();
            Log.info(TAG, "startEncoder success.");
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    public void close()
    {
        try
        {
            Log.info(TAG, "stopDecoder ...");
            decoder.stop();
            Log.info(TAG, "stopDecoder success.");
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        
        try
        {
            Log.info(TAG, "releaseDecoder ...");
            decoder.release();
            Log.info(TAG, "releaseDecoder success.");
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    public int decode(byte[] input, int len)
    {
        int length = 0;
        try
        {
            ByteBuffer[] inputBuffers = decoder.getInputBuffers();
            int inputBufferIndex = decoder.dequeueInputBuffer(0);
            if (inputBufferIndex >= 0)
            {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(input, 0, len);
                decoder.queueInputBuffer(inputBufferIndex, 0, len, 0, 0);
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
            if (outputBufferIndex >= 0)
            {
                decoder.releaseOutputBuffer(outputBufferIndex, true); // surface
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        return length;
    }

}
