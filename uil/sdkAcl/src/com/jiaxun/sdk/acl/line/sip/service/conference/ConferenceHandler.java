package com.jiaxun.sdk.acl.line.sip.service.conference;

import org.zoolu.sip.call.ExtendedCall;

import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;
import com.jiaxun.sdk.util.xml.conference.ConfCall;
import com.jiaxun.sdk.util.xml.conference.ConfMedia;
import com.jiaxun.sdk.util.xml.conference.ConfMediaInfo;
import com.jiaxun.sdk.util.xml.conference.ConfMessage;
import com.jiaxun.sdk.util.xml.conference.ConfToneInfo;

/**
 * 说明：语音与视频会议
 *
 * @author hubin
 *
 * @Date 2015-4-27
 */
public class ConferenceHandler
{
    private final static String TAG = ConferenceHandler.class.getName();

    private final String AT = "@";

    private SipAdapter sipAdapter;

    public ConferenceHandler(SipAdapter sipAdapter)
    {
        this.sipAdapter = sipAdapter;
    }

    /**
     * 主席添加成员
     */
    public boolean addConfMember(ExtendedCall call, String confUrl, String userUrl)
    {
        try
        {
            String xmlBody = XmlMessageFactory.createJoinUserMsg(confUrl, userUrl);
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 主席释放成员
     */
    public boolean removeConfMember(ExtendedCall call, String confUrl, String userUrl)
    {
        try
        {
            String xmlBody = XmlMessageFactory.createUnJoinUserMsg(confUrl, userUrl);
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 成员振铃通知主席
     */
    public boolean memberRing(ExtendedCall call, String confUrl, String userUrl)
    {
        try
        {
            String xmlBody = "";
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 主席临时退会/临时退会后再入会
     */
    public boolean tempJoinOrUnjoinConf(ExtendedCall call, String confUrl, boolean join)
    {
        try
        {
            String xmlBody = null;
            if (join)
                xmlBody = XmlMessageFactory.createStepOutOrComebackMsg(confUrl, ConfMessage.CODE_STEPOUT);
            else
                xmlBody = XmlMessageFactory.createStepOutOrComebackMsg(confUrl, ConfMessage.CODE_COMEOUT);
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 允许/禁止成员发言
     */
    public boolean controlMemberVoice(ExtendedCall call, String confUrl, String userUrl, boolean mute)
    {
        try
        {
            String xmlBody = null;
            if (mute)
                xmlBody = XmlMessageFactory.createMediaControlMsg(confUrl, userUrl, ConfMedia.ID_AUDIO, ConfMedia.STATUS_SENDRECV, null, null);
            else
                xmlBody = XmlMessageFactory.createMediaControlMsg(confUrl, userUrl, ConfMedia.ID_AUDIO, ConfMedia.STATUS_RECVONLY, null, null);
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 打开成员视频
     */
    public boolean controlMemberVideoSwitch(ExtendedCall call, String confUrl, String userUrl, boolean open)
    {
        try
        {
            String xmlBody = null;
            if (open)
                xmlBody = XmlMessageFactory.createMediaControlMsg(confUrl, userUrl, ConfMedia.ID_VIDEO, ConfMedia.STATUS_SENDRECV, null, null);
            else
                xmlBody = XmlMessageFactory.createMediaControlMsg(confUrl, userUrl, ConfMedia.ID_VIDEO, ConfMedia.STATUS_RECVONLY, null, null);
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 成员视频广播
     */
    public boolean controlMemberVideoBroadcast(ExtendedCall call, String confUrl, String userUrl, String tag, boolean broadcast)
    {
        try
        {
            String xmlBody = null;
            if (broadcast)
                xmlBody = XmlMessageFactory.createMediaControlMsg(confUrl, userUrl, ConfMedia.ID_VIDEO, ConfMedia.STATUS_SENDRECV, ConfMedia.ENABLE_ON, tag);
            else
                xmlBody = XmlMessageFactory.createMediaControlMsg(confUrl, userUrl, ConfMedia.ID_VIDEO, ConfMedia.STATUS_SENDRECV, ConfMedia.ENABLE_OFF, tag);
            call.pushInfo(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }
    
    /**
     * 会议提示音
     */
    public boolean meetToneInfo(ExtendedCall call, String confUrl, boolean play)
    {
        try
        {
            call.pushInfo(XmlMessageFactory.createToneInfoMsg(confUrl, play));
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 会议info返回消息
     */
    public void onConferenceInfoReponse(ExtendedCall call, XmlMessage msg)
    {
        Log.info(TAG, "onConferenceInfoReponse::");
        ConfMessage confMessage = msg.getConfMessage();
        if (confMessage.getCode() == null)
            onMemberInfoNotify(call, confMessage);
        else if (confMessage.getCode().equals(ConfMessage.CODE_JOINUSER) || confMessage.getCode().equals(ConfMessage.CODE_UNJOINUSER))
        {// 加入成员回复
            String callStatus = confMessage.getItem().getCall().getStatus();
            String userNumber = confMessage.getItem().getUserUri().split(AT)[0];
            String mediaTag = confMessage.getItem().getMediaInfo().getTag();
            onAddConfMember(callStatus, call.getCallId(), userNumber, mediaTag);
        }
        else if (confMessage.getCode().equals(ConfMessage.CODE_STEPOUT))
        {// 会议主席临时退会成功
            if (sipAdapter.getConfEventListener() != null)
            {
                String callStatus = confMessage.getItem().getCall().getStatus();
                if (ConfMessage.CODE_STEPOUT.equals(callStatus))
                {
                    sipAdapter.getConfEventListener().onConfExitOk(call.getCallId());
                }
                else
                {
                    sipAdapter.getConfEventListener().onConfExitFail(call.getCallId());
                }
            }
        }
        else if (confMessage.getCode().equals(ConfMessage.CODE_COMEOUT))
        {// 会议主席临时入会成功
            if (sipAdapter.getConfEventListener() != null)
            {
                String callStatus = confMessage.getItem().getCall().getStatus();
                if (ConfCall.STATE_ONLINE.equals(callStatus))
                {
                    sipAdapter.getConfEventListener().onConfReturnOk(call.getCallId());
                }
                else
                {
                    sipAdapter.getConfEventListener().onConfReturnFail(call.getCallId());
                }
            }
        }
        else if (confMessage.getCode().equals(ConfMessage.CODE_MEDIACONTROL))// 媒体控制回复
            onMediaControl(call, confMessage);
        else if(confMessage.getCode().equals(ConfMessage.CODE_TONESENDING))// 会议提示音
            onConfToneInfo(call, confMessage);
    }

    /**
     * 媒体控制回复
     */
    public void onMediaControl(ExtendedCall call, ConfMessage confMessage)
    {
        Log.info(TAG, "onMediaControl::");
        ConfMedia audio = confMessage.getItem().getMediaInfo().getAudio();
        ConfMedia video = confMessage.getItem().getMediaInfo().getVideo();
        String sessionId = call.getCallId();
        String userNumber = confMessage.getItem().getUserUri().split(AT)[0];
        if ((call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                && sipAdapter.getConfEventListener() != null)
        {
            if (audio != null)
            {// 音频控制
                if (ConfMedia.STATUS_RECVONLY.equals(audio.getStatus()) && ConfMedia.ENABLE_ON.equals(audio.getEnable()))
                {
                    sipAdapter.getConfEventListener().onConfUserAudioEnableAck(sessionId, userNumber, false);
                }
                else if (ConfMedia.STATUS_SENDRECV.equals(audio.getStatus()) && ConfMedia.ENABLE_ON.equals(audio.getEnable()))
                {
                    sipAdapter.getConfEventListener().onConfUserAudioEnableAck(sessionId, userNumber, true);
                }
            }
            if (video != null)
            {// 视频控制
                if (ConfMedia.STATUS_RECVONLY.equals(video.getStatus()) && ConfMedia.ENABLE_ON.equals(video.getEnable()))
                {
                    sipAdapter.getConfEventListener().onConfUserVideoEnableAck(sessionId, userNumber, false);
                }
                else if (ConfMedia.STATUS_SENDRECV.equals(video.getStatus()) && ConfMedia.ENABLE_ON.equals(video.getEnable()))
                {
                    if (ConfMedia.ENABLE_ON.equals(video.getBroadcast()))
                    {
                        sipAdapter.getConfEventListener().onConfUserVideoShareAck(sessionId, userNumber, true);
                    }
                    else if (ConfMedia.ENABLE_OFF.equals(video.getBroadcast()))
                    {
                        sipAdapter.getConfEventListener().onConfUserVideoShareAck(sessionId, userNumber, false);
                    }
                    else
                    {
                        sipAdapter.getConfEventListener().onConfUserVideoEnableAck(sessionId, userNumber, true);
                    }
                }
            }
        }
        else if (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO && sipAdapter.getSCallEventListener() != null)
        {
            if (video != null)
            {// 视频控制
                Log.info(TAG, "confMessage.getItem().getMediaInfo().getTag():" + confMessage.getItem().getMediaInfo().getTag());
                if (ConfMedia.ENABLE_ON.equals(video.getBroadcast()))
                {
                    sipAdapter.getSCallEventListener().onVideoShareReceived(sessionId, true, userNumber, confMessage.getItem().getMediaInfo().getTag());
                }
                else if (ConfMedia.ENABLE_OFF.equals(video.getBroadcast()))
                {
                    sipAdapter.getSCallEventListener().onVideoShareReceived(sessionId, false, userNumber, confMessage.getItem().getMediaInfo().getTag());
                }
            }
        }
    }

    /**
     * 加入成员回复
     */
    private void onAddConfMember(String callStatus, String sessionId, String userNumber, String mediaTag)
    {
        Log.info(TAG, "onAddConfMember::callStatus:" + callStatus + " userNumber:" + userNumber);
        if (sipAdapter.getConfEventListener() != null)
        {
            if (ConfCall.STATE_FAILED.equals(callStatus))
            {// 成员加入失败
                sipAdapter.getConfEventListener().onConfUserRelease(sessionId, userNumber, CommonConstantEntry.CONF_MEMBER_END_FAILED);
            }
            else if (ConfCall.STATE_RINGING.equals(callStatus))
            {// 成员振铃
                sipAdapter.getConfEventListener().onConfUserRing(sessionId, userNumber);
            }
            else if (ConfCall.STATE_ONLINE.equals(callStatus))
            {// 成员加入
                sipAdapter.getConfEventListener().onConfUserAnswer(sessionId, userNumber, mediaTag);
            }
            else if (ConfCall.STATE_OFFLINE.equals(callStatus))
            {// 成员释放
                sipAdapter.getConfEventListener().onConfUserRelease(sessionId, userNumber, CommonConstantEntry.CONF_MEMBER_END_OFFLINE);
            }
        }
    }

    /**
     * 成员入会后，通知此成员主席相关的信息
     */
    public void onMemberInfoNotify(ExtendedCall call, ConfMessage confMessage)
    {
        if (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO && sipAdapter.getSCallEventListener() != null
                && !ConfCall.STATE_OFFLINE.equals(confMessage.getItem().getCall().getStatus()))
        {
            ConfMediaInfo mediaInfo = confMessage.getItem().getMediaInfo();
            String sessionId = call.getCallId();
            if (mediaInfo != null)
            {// 视频控制
                sipAdapter.getSCallEventListener().onMultiMediaInfoNotify(sessionId, mediaInfo.getTag());
            }
        }
    }
    
    /**
     * 会议提示音
     */
    public void onConfToneInfo(ExtendedCall call, ConfMessage confMessage)
    {
        Log.info(TAG, "onMeetInfo::");
        String event = confMessage.getItem().getToneInfo().getEvent();
        String status = confMessage.getItem().getToneInfo().getStatus();
        if (sipAdapter.getConfEventListener()!=null)
        {
            if(event.equals(ConfToneInfo.EVENT_PLAY))
            {// 播放
                if(status.equals(ConfToneInfo.STATUS_SUCCESS))
                {// 成功
                    sipAdapter.getConfEventListener().onConfBgmAck(call.getCallId(), true);
                }
                else
                {
                    sipAdapter.getConfEventListener().onConfBgmAck(call.getCallId(), false);
                }
            }
            else if(event.equals(ConfToneInfo.EVENT_STOP))
            {// 停止播放
                if(status.equals(ConfToneInfo.STATUS_SUCCESS))
                {// 成功
                    sipAdapter.getConfEventListener().onConfBgmAck(call.getCallId(), false);
                }
                else
                {
                    sipAdapter.getConfEventListener().onConfBgmAck(call.getCallId(), true);
                }
            }
        }
    }

}
