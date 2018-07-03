package com.jiaxun.uil.model;

import android.view.View.OnClickListener;

import com.jiaxun.sdk.scl.model.CallModel;

/**
 * 说明：呼叫列表单元
 *
 * @author  hubin
 *
 * @Date 2015-4-2
 */
public class CallListItem extends BaseListItem
{
    private static final long serialVersionUID = 1L;

    private CallModel callModel;

    private int status;

    private int type;
    // 是否入会操作
    private boolean isIntoConf = false;
    
    // 单呼转会议
    private boolean isTransferToConf = false;

    // 当作为会议成员时，被主席方语音控制的状态
    private boolean isAudioEnabled = true;

    private OnClickListener answerListener; // 接听
    private OnClickListener hangupListener; // 挂断
    private OnClickListener holdListener; // 保持
    private OnClickListener muteListener; // 静音
    private OnClickListener speakerListener; // 免提
    private OnClickListener intoConfListener; // 入会
    private OnClickListener callTransConfListener; // 单呼转会
    private OnClickListener closeBellListener; // 闭铃

    private OnClickListener answerConfListener; // 会议接听
    private OnClickListener hangupConfListener; // 会议挂断

//    private SurfaceView remoteVideoView;

    public OnClickListener getAnswerListener()
    {
        return answerListener;
    }

    public boolean isIntoConf()
    {
        return isIntoConf;
    }

    public void setIntoConf(boolean isIntoConf)
    {
        this.isIntoConf = isIntoConf;
    }

    public OnClickListener getAnswerConfListener()
    {
        return answerConfListener;
    }

    public void setAnswerConfListener(OnClickListener answerConfListener)
    {
        this.answerConfListener = answerConfListener;
    }

    public OnClickListener getHangupConfListener()
    {
        return hangupConfListener;
    }

    public void setHangupConfListener(OnClickListener hangupConfListener)
    {
        this.hangupConfListener = hangupConfListener;
    }

    public void setAnswerListener(OnClickListener answerListener)
    {
        this.answerListener = answerListener;
    }

    public OnClickListener getHangupListener()
    {
        return hangupListener;
    }

    public void setHangupListener(OnClickListener hangupListener)
    {
        this.hangupListener = hangupListener;
    }

    public OnClickListener getHoldListener()
    {
        return holdListener;
    }

    public void setHoldListener(OnClickListener holdListener)
    {
        this.holdListener = holdListener;
    }

    public OnClickListener getMuteListener()
    {
        return muteListener;
    }

    public void setMuteListener(OnClickListener muteListener)
    {
        this.muteListener = muteListener;
    }

    public OnClickListener getSpeakerListener()
    {
        return speakerListener;
    }

    public void setSpeakerListener(OnClickListener speakerListener)
    {
        this.speakerListener = speakerListener;
    }

    public OnClickListener getIntoConfListener()
    {
        return intoConfListener;
    }

    public void setIntoConfListener(OnClickListener intoConfListener)
    {
        this.intoConfListener = intoConfListener;
    }

    public OnClickListener getCallTransConfListener()
    {
        return callTransConfListener;
    }

    public void setCallTransConfListener(OnClickListener callTransConfListener)
    {
        this.callTransConfListener = callTransConfListener;
    }

    public OnClickListener getCloseBellListener()
    {
        return closeBellListener;
    }

    public void setCloseBellListener(OnClickListener closeBellListener)
    {
        this.closeBellListener = closeBellListener;
    }

    public CallModel getCallModel()
    {
        return callModel;
    }

    public void setCallModel(CallModel callModel)
    {
        this.callModel = callModel;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public boolean isAudioEnabled()
    {
        return isAudioEnabled;
    }

    public void setAudioEnabled(boolean isAudioEnabled)
    {
        this.isAudioEnabled = isAudioEnabled;
    }

    public boolean isTransferToConf()
    {
        return isTransferToConf;
    }

    public void setTransferToConf(boolean isTransferToConf)
    {
        this.isTransferToConf = isTransferToConf;
    }

//    public SurfaceView getRemoteVideoView()
//    {
//        return remoteVideoView;
//    }
//
//    public void setRemoteVideoView(SurfaceView remoteVideoView)
//    {
//        this.remoteVideoView = remoteVideoView;
//    }

}
