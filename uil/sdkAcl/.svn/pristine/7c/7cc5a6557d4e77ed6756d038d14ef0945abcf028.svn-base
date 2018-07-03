package com.jiaxun.sdk.acl.line.sip.event;

import java.util.Hashtable;

import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.sip.message.SipResponses;
import org.zoolu.sip.provider.Identifier;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipProviderListener;
import org.zoolu.sip.provider.TransactionIdentifier;

import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.sip.ua.XmlMessageHandler;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;

/**
 * User: liuyh
 * Date: 12-5-22
 * Time: ÏÂÎç8:25
 */
public class NotifyEventHandler implements SipProviderListener
{
    static final Identifier identifier = new TransactionIdentifier(SipMethods.MESSAGE);
    SipProvider provider;
    Hashtable<String, SubscribeEventListener> listeners = new Hashtable<String, SubscribeEventListener>();

    public NotifyEventHandler(SipProvider provider)
    {
        this.provider = provider;
        provider.removeSipProviderListener(identifier);
        provider.addSipProviderListener(identifier, this);
    }

    public void subscribe(String topic, SubscribeEventListener listener)
    {
        if (listeners.containsKey(topic))
        {
            Log.info("NotifyEventHandler.subscribe", "The listener exists for topic: " + topic);
        }

        listeners.put(topic, listener);
    }

    public void halt()
    {
        Hashtable<Identifier, SipProviderListener> lstns = provider.getListeners();
        if (lstns.get(identifier) != null && lstns.get(identifier).equals(this))
        {
            provider.removeSipProviderListener(identifier);
        }
    }

    /** ACK the request (sends a "200 OK" response). 
     * @throws Exception */
    protected void ack(Message message) throws Exception
    {
        Message resp = MessageFactory.createResponse(message, 200, SipResponses.reasonOf(200), null);
        provider.sendMessage(resp);
    }

    @Override
    public void onReceivedMessage(SipProvider sip_provider, Message message)
    {
        Log.info("NotifyEventHandler", "onReceivedMessage:" + message.toString());
        if (message.isMessage())
        {
            // send the ACK back.
            try
            {
                ack(message);
            }
            catch (Exception e)
            {
                Log.exception("onReceivedMessage", e);
            }

            String content_type = message.getContentTypeHeader().getContentType();
            if (XmlMessageHandler.CONTENT_TYPE_MSAP.equalsIgnoreCase(content_type))
            {
                String body = message.getBody();
                if (body != null && !body.equals(""))
                {
                    XmlMessage xml = XmlMessageFactory.parse(body);

                    // invoke the listener by request-id.
                    String eventType = message.getEventHeader().getEvent();
                    String topic = xml.getRequestId();
                    try
                    {
                        fireListener(topic, eventType, xml, true);

                        // invoke the listener by EventType defined in XML
                        // message.
                        topic = xml.getEventType();
                        fireListener(topic, eventType, xml, false);
                    }
                    catch (Exception e)
                    {
                        Log.exception("onReceivedMessage", e);
                    }
                }
                else
                {
                    Log.error("NotifyEventHandler.onReceivedMessage", "The message body is null.");
                }
            }
            else if (XmlMessageHandler.CONTENT_TYPE_CCCP.equalsIgnoreCase(content_type))
            {
                String body = message.getBody();
                if (!TextUtils.isEmpty(body))
                {
                    // invoke the listener by request-id.
                    if (body.contains(XmlMessage.NIGHTSERVICE_RESPONSE))
                    {
                        Log.info("NotifyEventHandler.onReceivedMessage", "eventType:" + XmlMessage.NIGHTSERVICE_RESPONSE);
                        XmlMessage xml = XmlMessageFactory.parseNightServiceXml(body);
                        String topic = xml.getRequestId();
                        try
                        {
                            // invoke the listener by EventType defined in XML
                            // message.
                            topic = xml.getEventType();
                            fireListener(topic, XmlMessage.NIGHTSERVICE_RESPONSE, xml, true);
                        }
                        catch (Exception e)
                        {
                            Log.exception("onReceivedMessage", e);
                        }
                    }
                }
                else
                {
                    Log.error("NotifyEventHandler.onReceivedMessage", "The message body is null.");
                }
            }
        }
    }

    protected void fireListener(String topic, String eventType, XmlMessage msg, boolean removable) throws Exception
    {
        Log.info("NotifyEventHandler.fireListener", "topic:" + topic + " eventType:" + eventType + " removable:" + removable);
        SubscribeEventListener listener = listeners.get(topic);
        // remove the listener for this topic first and then notify the
        // listener.
        if (removable)
        {
            listeners.remove(topic);
        }

        if (listener != null)
        {
            listener.handle(eventType, msg);
        }
    }

}
