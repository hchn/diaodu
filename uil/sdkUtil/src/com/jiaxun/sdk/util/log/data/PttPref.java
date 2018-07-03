package com.jiaxun.sdk.util.log.data;

/**
 * ptt性能数据
 * 
 * @author fuluo
 */
public class PttPref
{
    private String groupNumber;//组呼号码
    
    private String callNumber;//号码
    
    private long pttDown;//ptt按下时间戳
    
    private long pttUp;//ptt释放时间戳
    
    private long pttRequest;//ptt按下方法执行时间戳
    
    private long pttRelease;//ptt释放方法执行时间戳
    
    private long sendPttRequest;//发送ptt申请话权消息时间戳
    
    private long sendPttRelease;//发送ptt释放话权消息时间戳
    
    private long receivePttRequestReponse;//接收ptt申请话权回应消息时间戳
    
    private long receivePttReleaseReponse;//接收ptt释放话权回应消息时间戳
    
    private long pttStartTalk;//ptt开始说话时间戳
    
    private long pttStopTalk;//ptt停止说话时间戳

    public String getGroupNumber()
    {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber)
    {
        this.groupNumber = groupNumber;
    }

    public String getCallNumber()
    {
        return callNumber;
    }

    public void setCallNumber(String callNumber)
    {
        this.callNumber = callNumber;
    }

    public long getPttDown()
    {
        return pttDown;
    }

    public void setPttDown(long pttDown)
    {
        this.pttDown = pttDown;
    }

    public long getPttUp()
    {
        return pttUp;
    }

    public void setPttUp(long pttUp)
    {
        this.pttUp = pttUp;
    }

    public long getPttRequest()
    {
        return pttRequest;
    }

    public void setPttRequest(long pttRequest)
    {
        this.pttRequest = pttRequest;
    }

    public long getPttRelease()
    {
        return pttRelease;
    }

    public void setPttRelease(long pttRelease)
    {
        this.pttRelease = pttRelease;
    }

    public long getSendPttRequest()
    {
        return sendPttRequest;
    }

    public void setSendPttRequest(long sendPttRequest)
    {
        this.sendPttRequest = sendPttRequest;
    }

    public long getSendPttRelease()
    {
        return sendPttRelease;
    }

    public void setSendPttRelease(long sendPttRelease)
    {
        this.sendPttRelease = sendPttRelease;
    }

    public long getReceivePttRequestReponse()
    {
        return receivePttRequestReponse;
    }

    public void setReceivePttRequestReponse(long receivePttRequestReponse)
    {
        this.receivePttRequestReponse = receivePttRequestReponse;
    }

    public long getReceivePttReleaseReponse()
    {
        return receivePttReleaseReponse;
    }

    public void setReceivePttReleaseReponse(long receivePttReleaseReponse)
    {
        this.receivePttReleaseReponse = receivePttReleaseReponse;
    }

    public long getPttStartTalk()
    {
        return pttStartTalk;
    }

    public void setPttStartTalk(long pttStartTalk)
    {
        this.pttStartTalk = pttStartTalk;
    }

    public long getPttStopTalk()
    {
        return pttStopTalk;
    }

    public void setPttStopTalk(long pttStopTalk)
    {
        this.pttStopTalk = pttStopTalk;
    }

}
