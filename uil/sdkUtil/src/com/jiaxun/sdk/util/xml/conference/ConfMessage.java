package com.jiaxun.sdk.util.xml.conference;


/**
 * 说明：会议消息
 * 
 * @author fuluo
 * 
 * @Date 2015-4-29
 */
public class ConfMessage
{
    public final static String TAG = "conference";
    public final static String ATTRIBUTE_CONFERENCEURI = "conferenceURI";
    public final static String ATTRIBUTE_CODE = "code";
    
    public final static String CODE_JOINUSER = "JoinUser";
    public final static String CODE_UNJOINUSER = "UnJoinUser";
    public final static String CODE_STEPOUT = "step_out";
    public final static String CODE_COMEOUT = "come_back";
    public final static String CODE_MEDIACONTROL = "MediaControl";
    public final static String CODE_TONESENDING = "tone-sending";
    
    private String conferenceURI;
    private String code;
    private ConfItem item = new ConfItem();

    public String getConferenceURI()
    {
        return conferenceURI;
    }

    public void setConferenceURI(String conferenceURI)
    {
        this.conferenceURI = conferenceURI;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public ConfItem getItem()
    {
        return item;
    }

    public void setItem(ConfItem item)
    {
        this.item = item;
    }

}
