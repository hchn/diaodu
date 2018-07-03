package com.jiaxun.sdk.scl.media.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：音频文件播放
 * 
 * @author fuluo
 * 
 * @Date 2015-5-18
 */
public class VoicePlayer
{
    private static String TAG = "VoicePlayer";

    private static MediaPlayer mediaPlayer;

    private static VoicePlayer instance;

    private VoicePlayer()
    {
        mediaPlayer = new MediaPlayer();
    }

    public static VoicePlayer getInstance()
    {
        if (instance == null)
        {
            instance = new VoicePlayer();
        }
        return instance;
    }

    /**
     * 方法说明 : 停止播放音频文件
     * 
     * @author hubin
     * @Date 2014-1-21
     */
    public boolean stopPlay()
    {
        Log.info(TAG, "stopPlay");
        try
        {
            if (mediaPlayer != null)
            {
                // FIXME: 规避异常at
                // android.media.MediaPlayer$EventHandler.handleMessage(MediaPlayer.java:2244)
//                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                return true;
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        return false;
    }

    /**
     * 方法说明 : 播放声音文件
     * 
     * @param mediaPlayer
     *            媒体播放器
     * @param voiceFile
     *            声音文件
     * @param antiEcho
     *            true表示播放声音文件之前开启静音模式，防止声音传到对端；false表示正常播放
     * @param isLooping
     *            true表示循环播放；false表示只播放一次
     * @param playListener
     *            播放回调
     * 
     * @return long 整个播放持续的时间
     * 
     * @author hubin
     * @Date 2014-6-24
     */
    public long playVoiceFile(String voiceFile, int streamtype, final boolean antiEcho, boolean isLooping, boolean isSystemAutio)
    {
        Log.info(TAG, "playVoiceFile");
        long duration = 0;
        if (TextUtils.isEmpty(voiceFile))
        {
            return duration;
        }

        try
        {
            if (antiEcho)
            {
                // 播放声音文件之前开启静音模式，防止声音传到对端
                setMute(true);
            }
            mediaPlayer = new MediaPlayer();
            AssetFileDescriptor fileDescriptor = null;
            if (isSystemAutio)
            {
                AssetManager assetManager = SdkUtil.getApplicationContext().getAssets();
                fileDescriptor = assetManager.openFd(voiceFile);
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            }
            else
            {
                mediaPlayer.setDataSource(voiceFile);
            }
            mediaPlayer.setAudioStreamType(streamtype);
            AudioManager am = (AudioManager) SdkUtil.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            am.setStreamVolume(streamtype, (int) (am.getStreamVolume(streamtype)), 0);
            if (fileDescriptor != null)
            {
                fileDescriptor.close();
            }
            mediaPlayer.prepare();
            mediaPlayer.setLooping(isLooping);
            mediaPlayer.start();
            duration = mediaPlayer.getDuration();
            Log.info(TAG, "playVoiceFile.isPlaying");

            if (isLooping)
            {// 循环播放
                return duration;
            }

//            final long durationTemp = duration;
//            new Thread()
//            {
//                @Override
//                public void run()
//                {
//                    try
//                    {
//                        SystemClock.sleep(durationTemp + 70);// 延长休眠时间，避免声音传到对方
//                        Log.info(TAG, "playVoiceFile::durationTemp:" + durationTemp);
//                        if (mediaPlayer!= null && mediaPlayer.isPlaying())
//                        {
//                            mediaPlayer.stop();
//                            mediaPlayer.release();
//                        }
//                        if (antiEcho)
//                        {
//                            // 媒体文件播放完毕，关闭静音
//                            setMute(false);
//                        }
//                    }
//                    catch (Exception ex)
//                    {
//                        Log.exception(TAG, ex);
//                    }
//                }
//            }.start();
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
        }
        return duration;
    }

    /**
     * 方法说明 : 设置静音
     * 
     * @param muteOn
     * @author hubin
     * @Date 2013-12-12
     */
    private static void setMute(boolean muteOn)
    {
        Log.info(TAG, "setMute::muteOn:" + muteOn);
    }

    public interface PlayListener
    {
        public void onPlayFinish();// 播放完毕回调
    }

}
