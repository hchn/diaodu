package com.jiaxun.sdk.acl.line.sip.service.nightservice;

import org.zoolu.sip.provider.SipProvider;

import com.jiaxun.sdk.acl.line.LineManager;
import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.acl.line.sip.ua.XmlMessageHandler;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.nightservice.NsItem;

/**
 * 说明：夜服功能
 *
 * @author  fuluo
 *
 * @Date 2015-6-8
 */
public class NightServiceHandler extends XmlMessageHandler
{
    private static String TAG = "NightServiceHandler";
    private boolean enableOpr;

    public NightServiceHandler(Boolean enableOpr, SipProvider sip_provider, String target_url, String contact_url, SipAdapter sipAdapter)
    {
        super(sip_provider, target_url, contact_url);
        this.enableOpr = enableOpr;
    }

    @Override
    public boolean isSubscribed(String topic)
    {
        return XmlMessage.NIGHTSERVICE_RESPONSE.equals(topic);
    }

    @Override
    protected void onXmlMessageResponse(String event, XmlMessage message)
    {
        String event_type = message.getEventType();
        Log.info(TAG, "onXmlMessageResponse:: event_type:" + event_type);
        if (XmlMessage.NIGHTSERVICE_RESPONSE.equals(event_type) && LineManager.getInstance().getCommonEventListener() != null)
        {
            NsItem nsItem = message.getNsItem();
            Log.info(TAG, "onXmlMessageResponse:: ns_event:" + nsItem.getEvent() + " ns_value:" + nsItem.getValue());
            if (nsItem.getEvent().equals("open"))
            {// 打开夜服
                if (nsItem.getValue().equals("succ"))
                {// 成功
                    LineManager.getInstance().getCommonEventListener().onNightServiceAck(enableOpr, CommonConstantEntry.RESPONSE_SUCCESS);
                }
                else if (nsItem.getValue().equals("failed"))
                {// 失败
                    LineManager.getInstance().getCommonEventListener().onNightServiceAck(enableOpr, CommonConstantEntry.RESPONSE_FAILED);
                }
            }
            else if (nsItem.getEvent().equals("close"))
            {// 关闭夜服
                if (nsItem.getValue().equals("succ"))
                {// 成功
                    LineManager.getInstance().getCommonEventListener().onNightServiceAck(enableOpr, CommonConstantEntry.RESPONSE_SUCCESS);
                }
                else if (nsItem.getValue().equals("failed"))
                {// 失败
                    LineManager.getInstance().getCommonEventListener().onNightServiceAck(enableOpr, CommonConstantEntry.RESPONSE_FAILED);
                }
            }
        }
    }

    @Override
    protected void onXmlMessageNotAcceptable(String event, int code)
    {
        Log.info(TAG, "onXmlMessageNotAcceptable:: event:" + event + " code:" + code);
        LineManager.getInstance().getCommonEventListener().onNightServiceAck(enableOpr, CommonConstantEntry.RESPONSE_FAILED);
    }

    @Override
    public void onTimeout(String event)
    {
        Log.info(TAG, "onTimeout:: event:" + event);
        LineManager.getInstance().getCommonEventListener().onNightServiceAck(enableOpr, CommonConstantEntry.RESPONSE_FAILED);
    }

}
