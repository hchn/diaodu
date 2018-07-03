package com.jiaxun.sdk.scl.media.video;

import java.nio.ByteBuffer;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import com.jiaxun.sdk.util.log.Log;

public class VideoEncoder
{
    private static String TAG = "VideoEncoder";
    private MediaCodec encoder;
    private int mWidth, mHeight;
    private byte[] yuv420 = null;

    public VideoEncoder(int width, int height, int framerate, int bitrate)
    {
        mWidth = width;
        mHeight = height;

        yuv420 = new byte[width * height * 3 / 2];
        encoder = MediaCodec.createEncoderByType("video/avc");
        MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
        // mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1400);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitrate);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, framerate);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar); // <2>
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        try
        {
            encoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            Log.info(TAG, "startEncoder...");
            encoder.start();
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
            Log.info(TAG, "stopEncoder ...");
            encoder.stop();
            Log.info(TAG, "stopEncoder success.");
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        
        try
        {
            Log.info(TAG, "releaseEncoder ...");
            encoder.release();
            Log.info(TAG, "releaseEncoder success.");
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    public int encode(byte[] input, byte[] output)
    {
        int length = 0;
        try
        {
            swapYV12toI420(input, yuv420, mWidth, mHeight);

            ByteBuffer[] inputBuffers = encoder.getInputBuffers();
            ByteBuffer[] outputBuffers = encoder.getOutputBuffers();
            int inputBufferIndex = encoder.dequeueInputBuffer(0);
            if (inputBufferIndex >= 0)
            {
                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
                inputBuffer.clear();
                inputBuffer.put(yuv420);
                encoder.queueInputBuffer(inputBufferIndex, 0, yuv420.length, 0, 0);
            }

            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            int outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
            if (outputBufferIndex >= 0)
            {
                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
                outputBuffer.get(output, 0, bufferInfo.size);
                length = bufferInfo.size;
                encoder.releaseOutputBuffer(outputBufferIndex, false);
            }
        }
        catch (Exception e)
        {
            // Log.exception(TAG, e);
        }
        return length;
    }

    // yv12 ת yuv420p yvu -> yuv
    private void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height)
    {
        System.arraycopy(yv12bytes, 0, i420bytes, 0, width * height);
        System.arraycopy(yv12bytes, width * height + width * height / 4, i420bytes, width * height, width * height / 4);
        System.arraycopy(yv12bytes, width * height, i420bytes, width * height + width * height / 4, width * height / 4);
    }

}
