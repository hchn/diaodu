package com.jiaxun.sdk.acl.line.sip.service.cameracontrol;

import org.zoolu.sip.provider.SipProvider;

import com.jiaxun.sdk.acl.line.sip.ua.XmlMessageHandler;
import com.jiaxun.sdk.util.xml.XmlMessage;

/**
 * ˵�����ƾ����ƣ�û�з�����Ϣ������Ҫ����
 *
 * @author  fuluo
 *
 * @Date 2015-5-13
 */
public class CameraControlHandler extends XmlMessageHandler
{

    public CameraControlHandler(SipProvider sip_provider, String target_url, String contact_url)
    {
        super(sip_provider, target_url, contact_url);
    }

    @Override
    public void onTimeout(String event)
    {
    }

    @Override
    protected void onXmlMessageResponse(String event, XmlMessage message)
    {
    }

    @Override
    protected void onXmlMessageNotAcceptable(String event, int code)
    {
    }

}
