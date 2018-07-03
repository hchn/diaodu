package com.jiaxun.sdk.acl.line.sip.service.callforward;

import org.zoolu.sip.provider.SipProvider;

import com.jiaxun.sdk.acl.line.sip.ua.XmlMessageHandler;
import com.jiaxun.sdk.util.xml.XmlMessage;

/**
 * ����ǰת����
 * 
 * User: liuyh
 * Date: 12-5-22
 * Time: ����8:50
 */
public class CallForwardSettingHandler extends XmlMessageHandler
{
    public static final int RESULT_CODE_ERROR = 0;
    public static final int RESULT_CODE_OK = 1;

    public static final String EVENT_TYPE_CALL_FORWARD_OPERATE = "cf-operator";

    // ǰת����
    int cfType = 0;
    int enable = 0;
    String Forwardee;
    int time = 15;

    public CallForwardSettingHandler(SipProvider provider, String target_url, String contact_url)
    {
        super(provider, target_url, contact_url);
    }

    public void setCallForwardType(int type)
    {
        cfType = type;
    }

    public void setEnabled(int enable)
    {
        this.enable = enable;
    }

    public void setForwardee(String number)
    {
        Forwardee = number;
    }

    public void setTime(int time)
    {
        this.time = time;
    }

    @Override
    protected void onXmlMessageResponse(String event, XmlMessage msg)
    {
        //֪ͨ�ӿڣ�֪ͨ������ǰת�Ĳ��������
//        ServiceNotify.fireCallForwardSetResponse(cfType, enable, "");
    }

    @Override
    protected void onXmlMessageNotAcceptable(String event, int code)
    {
        //֪ͨ�ӿڣ�֪ͨ������ǰת�Ĳ��������
//        ServiceNotify.fireCallForwardSetResponse(cfType, enable, "");
    }

    @Override
    public void onTimeout(String event)
    {
        //֪ͨ�ӿڣ�֪ͨ������ǰת�Ĳ��������
//        ServiceNotify.fireCallForwardSetResponse(cfType, enable, "");
    }

}
