package com.jiaxun.sdk.scl.media.player;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;

import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：提供声音相关工具方法
 * 
 * @author hubin
 * 
 * @Date 2014-6-5
 */
public class VoiceUtil
{
    private static String TAG = "VoiceUtil";

    /**
     * 播放通话保持音
     */
    public static void playHold(int channel)
    {
        Log.info(TAG, "playHold:: channel:" + channel);
        play(SdkUtil.getApplicationContext(), AudioManager.STREAM_MUSIC, channel, "hold.wav", true, true, true);
    }

    /**
     * 停止播放保持音
     */
    public static void stopHold()
    {
        Log.info(TAG, "stopHold::");
        stopPlay();
    }

//    public static void stopAllVoice()
//    {
//        Log.info(TAG, "stopAllVoice::");
//        for (VoicePlayer voicePlayer : playerMap.values())
//        {
//            voicePlayer.stopPlay();
//        }
//    }

    /**
     * 播放通话中播放来呼提示音
     */
    public static void playIncallRing(int channel, boolean emergency)
    {
        Log.info(TAG, "playInCallRing::");
        String voiceFile = null;
        if (emergency)
        {// 紧急呼叫
            voiceFile = "incall_emergency.wav";
        }
        else
        {
            voiceFile = "incall.wav";
        }
        play(SdkUtil.getApplicationContext(), AudioManager.STREAM_VOICE_CALL, channel, voiceFile, true, false, true);
    }

    /**
     * 播放通话释放提示音
     */
    public static void playCallRelease(int channel, int releaseReason)
    {
        Log.info(TAG, "playCallRelease:: channel:" + channel + " releaseReason:" + releaseReason);
        String voiceFile = null;
        switch (releaseReason)
        {
            case CommonConstantEntry.Q850_NOREASON:
                break;
            case CommonConstantEntry.Q850_PREEMPTION:
                // 被抢占
                voiceFile = "preempted.wav";
                break;
            case CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED:
                // 抢占失败
                voiceFile = "preemptefail.wav";
                break;
            case CommonConstantEntry.Q850_CALL_REJECTED:
                break;
            case CommonConstantEntry.SIP_CELLID_NOTEXIST:
                break;
            case CommonConstantEntry.SIP_FN_NOTEXIST:
                break;
            case CommonConstantEntry.SIP_FN_FORBID:
                break;
            case CommonConstantEntry.SIP_GROUP_NOTEXIST:
                break;
            case CommonConstantEntry.CALL_END_HEARTBEAT_TIMEOUT:
                break;
            case CommonConstantEntry.SIP_KEEPALIVE_RELEASE:
                // 心跳结束
                break;
            case CommonConstantEntry.SIP_OFFLINE:
                // 对端离线
                break;
            case CommonConstantEntry.CALL_END_TIMEOUT:
                // 呼叫超时
                voiceFile = "caller_config_error.wav";
                break;

            case CommonConstantEntry.CALL_FAILED_TIMEOUT:
            case CommonConstantEntry.CALL_FAILED_CALLEE_ACK_LOCK:
                // 被叫无应答超时
                voiceFile = "no_answer.wav";
                break;
            case CommonConstantEntry.CALL_FAILED_REFUSE:
                // 拒绝
                voiceFile = "refuse.wav";
                break;
            case CommonConstantEntry.CALL_FAILED_BUSY:
                // 被叫忙
                voiceFile = "busy.wav";
                break;
            case CommonConstantEntry.CALL_FAILED_OFFLINE:
                // 不在线
                voiceFile = "callee_unregister.wav";
                break;
            case CommonConstantEntry.CALL_FAILED_NOACCOUNT:
                // 空号
                voiceFile = "callee_notinservice.wav";
                break;
            case CommonConstantEntry.CALL_FAILED_FORBID:
                // 主叫无权限
                voiceFile = "caller_notauth.wav";
                break;
            case CommonConstantEntry.CALL_FAILED_PRESIDENT_RELEASE:
                // 主席释放
                break;
            case CommonConstantEntry.CALL_FAILED_UNREACHABLE_450:
                break;
            default:
//                if (releaseReason >= 500 || releaseReason < 700)
//                {// 其他服务器故障，无法接通，过滤掉487（cancle成功）
//                    voiceFile = "caller_config_error.wav";
//                }
                break;
        }
//        if (CommonConstantEntry.CALL_FAILED_PREEMPTED.equals(releaseReason))
//        {
//            // 被抢占
//            voiceFile = "preempted.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_PREEMPTFAILED.equals(releaseReason))
//        {
//            // 抢占失败
//            voiceFile = "preemptefail.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_TIMEOUT.equals(releaseReason))
//        {
//            // 被叫无应答超时
//            voiceFile = "no_answer.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_REFUSE.equals(releaseReason))
//        {
//            // 拒绝
//            voiceFile = "refuse.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_BUSY.equals(releaseReason))
//        {
//            // 被叫忙
//            voiceFile = "busy.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_OFFLINE.equals(releaseReason))
//        {
//            // 不在线
//            voiceFile = "callee_unregister.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_NOACCOUNT.equals(releaseReason))
//        {
//            // 空号
//            voiceFile = "callee_notinservice.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_FORBID.equals(releaseReason))
//        {
//            // 主叫无权限
//            voiceFile = "caller_notauth.wav";
//        }
//        else if (CommonConstantEntry.CALL_FAILED_UNREACHABLE.equals(releaseReason))
//        {
//            // 服务器故障，无法接通：您好，此呼叫无法建立,请联系配置管理员
//            voiceFile = "caller_config_error.wav";
//        }

        play(SdkUtil.getApplicationContext(), AudioManager.STREAM_MUSIC, channel, voiceFile, false, false, true);
    }

    /**
     * 停止播放通话释放提示音
     */
    public static void stopCallRelease(String callId)
    {
        Log.info(TAG, "stopCallRelease::callId:" + callId);
        stopPlay();
    }

    public static void playRingback(int channel)
    {
        Log.info(TAG, "playRingback::");
        play(SdkUtil.getApplicationContext(), AudioManager.STREAM_MUSIC, channel, "ringBack.wav", false, true, true);
    }

    public static void stopRingback()
    {
        Log.info(TAG, "stopRingback::");
        stopPlay();
    }

    /**
     * 播放自动应答提示音
     */
    public static void playAutoAnswer(int channel, boolean isConf, boolean emergency)
    {
        Log.info(TAG, "playAutoAnswer::channel:" + channel + " isConf:" + isConf + " emergency:" + emergency);
        String voiceFile = null;
        if (emergency)
        {
//            if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
//                voiceFile = "auto_answer_conf.wav";
//            else
//                voiceFile = "auto_answer_single.wav";
            voiceFile = "emergency.wav";
        }
        else
        {
            voiceFile = "auto_answer.wav";
        }
        play(SdkUtil.getApplicationContext(), AudioManager.STREAM_MUSIC, channel, voiceFile, false, false, true);
    }

    /**
     * 播放来电振铃音
     */
    public static void playIncomingCallRing(int channel, boolean emergency)
    {
        Log.info(TAG, "playIncomingCallRing:: channel:" + channel + " emergency:" + emergency);
        String voiceFile = null;
        if (emergency)
            voiceFile = "emergency.wav";
        else
            voiceFile = SessionManager.getInstance().getMediaConfig().incomingCallVoice;

        boolean isSystemAudio = false;
        if ("ring.wav".equals(voiceFile))
        {
            isSystemAudio = true;
        }
        else
        {
            isSystemAudio = false;
        }

        play(SdkUtil.getApplicationContext(), AudioManager.STREAM_MUSIC, channel, voiceFile, false, true, isSystemAudio);
    }

    /**
     * 停止播放通话释放提示音
     */
    public static void stopIncomingCallRing()
    {
        Log.info(TAG, "stopIncomingCallRing::");
        stopPlay();
    }

    /**
     * 播放来电振铃音
     */
    private static void play(final Context context, final int streamtype, final int channel, final String voiceFile, final boolean antiEcho,
            final boolean isLooping, final boolean isSystemAudio)
    {
        Log.info(TAG, "play::channel:" + channel + " voiceFile:" + voiceFile + " antiEcho:" + antiEcho + " isLooping:" + isLooping);
        if (TextUtils.isEmpty(voiceFile))
        {
            return;
        }
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (VoicePlayer.getInstance())
                {
                    VoicePlayer.getInstance().playVoiceFile(voiceFile, streamtype, antiEcho, isLooping, isSystemAudio);
                }
            }
        }).start();
    }

    /**
     * 停止播放通话释放提示音
     */
    private static void stopPlay()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                synchronized (VoicePlayer.getInstance())
                {
                    VoicePlayer.getInstance().stopPlay();
                }
            }
        }).start();
    }

    public static void setSpeaker(boolean enable)
    {
        Log.info(TAG, "setSpeaker::" + enable);
        AudioManager am = (AudioManager) SdkUtil.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        if (enable)
        {// 打开扬声器
            if (!am.isSpeakerphoneOn())
            {
                am.setSpeakerphoneOn(true);// 打开扬声器
            }
        }
        else
        {// 关闭扬声器
            if (am.isSpeakerphoneOn())
            {
                am.setSpeakerphoneOn(false);// 关闭扬声器
            }
        }
    }

}