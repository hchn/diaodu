package com.jiaxun.sdk.acl.line.sip.service.callforward;

import org.zoolu.sip.provider.SipProvider;

import com.jiaxun.sdk.acl.line.sip.ua.XmlMessageHandler;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.Element;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;

/**
 * ����ǰת��ѯ
 * 
 * User: liuyh
 * Date: 12-5-22
 * Time: ����8:50
 */
public class CallForwardQueryHandler extends XmlMessageHandler
{
    public static final int RESULT_CODE_ERROR = 1;
    public static final int RESULT_CODE_OK = 10;

    public static final String EVENT_TYPE_CALL_FORWARD_OPERATE = "cf-operator";

    // ǰת����
    int cfType = 0;

    public CallForwardQueryHandler(SipProvider provider, String target_url, String contact_url)
    {
        super(provider, target_url, contact_url);
    }

    public void setCallForwardType(int type)
    {
        cfType = type;
    }

    @Override
    protected void onXmlMessageResponse(String event, XmlMessage msg)
    {
        Log.info("CallForwardQueryHandler", "onXmlMessageResponse");
        if (CallForwardQueryHandler.EVENT_TYPE_CALL_FORWARD_OPERATE.equals(event))
        {
            String event_type = msg.getEventType();

            if ("CallForward-Query-Response".equals(event_type))
            {
                String target = null;
                int time = 0;

                String alwaysNumber = "";
                String noResponseNumber = "";
                String busyNumber = "";
                String noReachNumber = "";

                // CALL_FORWARD_TYPE_CFU = 0
                Element e1 = msg.getParameter("CFU");
                if (e1 != null)
                {
                    alwaysNumber = e1.getValue();
                }

                // CALL_FORWARD_TYPE_CFNA
                e1 = msg.getParameter("CFNA");
                if (e1 != null)
                {
                    noResponseNumber = e1.getValue();
                }

                // CALL_FORWARD_TYPE_CFB
                e1 = msg.getParameter("CFB");
                if (e1 != null)
                {
                    busyNumber = e1.getValue();
                }

                // CALL_FORWARD_TYPE_CFF
                e1 = msg.getParameter("CFF");
                if (e1 != null)
                {
                    noReachNumber = e1.getValue();
                }

                e1 = msg.getParameter("Timeout");
                if (e1 != null)
                {
                    try
                    {
                        time = Integer.parseInt(e1.getValue());
                    }
                    catch (NumberFormatException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (target == null)
                {
                    target = "";
                }
                int enable = XmlMessageFactory.CALL_FORWARD_DISABLE;
                if (!"".equals(target))
                {
                    enable = XmlMessageFactory.CALL_FORWARD_ENABLE;
                }

                //֪ͨ�ӿڣ�֪ͨ����ѯ����ǰת���õĴ�������
//                ServiceNotify.fireCallForwardSetQueryResponse(cfType, enable, target, time, RESULT_CODE_OK, "");
            }
            else
            {
                Log.info("CallForwardQueryHandler.onXmlMessageResponse", 
                        "Not corresponding event-type in CallForwardQueryHandler: " + event_type);
            }
        }
    }

    @Override
    protected void onXmlMessageNotAcceptable(String event, int code)
    {
        //֪ͨ�ӿڣ�֪ͨ����ѯ����ǰת���õĴ�������
//        ServiceNotify.fireCallForwardSetQueryResponse(cfType, 0, "", 0, RESULT_CODE_ERROR, "");
    }

    @Override
    public void onTimeout(String event)
    {
        //֪ͨ�ӿڣ�֪ͨ����ѯ����ǰת���õĴ�������
//        ServiceNotify.fireCallForwardSetQueryResponse(cfType, 0, "", 0, RESULT_CODE_ERROR, "");
    }

}
