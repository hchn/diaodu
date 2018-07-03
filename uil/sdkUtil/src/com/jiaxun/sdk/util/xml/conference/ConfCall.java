package com.jiaxun.sdk.util.xml.conference;

/**
 * 说明：成员呼叫状态
 * 
 * @author fuluo
 * 
 * @Date 2015-5-4
 */
public class ConfCall
{
    public final static String TAG = "call";
    public static final String ATTRIBUTE_STATUS = "status";
    public static final String TAG_REASON = "reason";
    
    public static final String STATE_FAILED = "failed";
    public static final String STATE_RINGING = "ringing";
    public static final String STATE_ONLINE = "online";
    public static final String STATE_OFFLINE = "offline";

    private String status;
    private String reason;

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

}
