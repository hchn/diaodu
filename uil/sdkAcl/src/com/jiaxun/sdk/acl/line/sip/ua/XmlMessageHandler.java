/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * 
 * This file is part of MjSip (http://www.mjsip.org)
 * 
 * MjSip is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * MjSip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 * Nitin Khanna, Hughes Systique Corp. (Reason: Android specific change,
 * optmization, bug fix)
 */

/*
 * Modified by:
 * Daina Interrante (daina.interrante@studenti.unipr.it)
 */

package com.jiaxun.sdk.acl.line.sip.ua;

import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.MessageFactory;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.transaction.TransactionClient;
import org.zoolu.sip.transaction.TransactionClientListener;

import com.jiaxun.sdk.acl.line.sip.event.SubscribeEventListener;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.XmlMessage;

/**
 * NotifierDialog.
 */
public abstract class XmlMessageHandler implements TransactionClientListener, SubscribeEventListener
{
    public static final String CONTENT_TYPE_XML = "Application/xml";
    public static final String CONTENT_TYPE_MSAP = "Application/msap+xml";// 20130625
                                                                          // zhoujy
    public static final String CONTENT_TYPE_CCCP = "Application/cccp+xml";
    public static final String PARAM_STATE = "State";
    public static final String PARAM_FN = "FN";
    public static final String PARAM_RESULT = "result";// 结果
    public static final String PARAM_VALUE = "value";// 值
    public static final String PARAM_REASAON = "reason";// 原因
    public static final String RESULT_SUCC = "succ";// 成功
    public static final String RESULT_FAIL = "fail";// 失败

    SipProvider provider;

    NameAddress to;
    NameAddress from;
    NameAddress contact;

    /** The current XML request message */
    Message request;

    /** The current notify request transaction */
    TransactionClient request_transaction;

    /** The event name */
    String event;

    String requestId;

    Log log;

    public static final int STATE_INIT = 0;
    public static final int STATE_WAITING = 1;
    public static final int STATE_END = 2;
    int state = STATE_INIT;

    /** Creates a new NotifierDialog. */
    public XmlMessageHandler(SipProvider sip_provider, String target_url, String contact_url)
    {
        provider = sip_provider;
        // @todo dismiss log now.
        // log = sip_provider.getLog();
        to = new NameAddress(target_url);
        from = new NameAddress(contact_url);
        contact = new NameAddress(contact_url);
        this.request_transaction = null;
        this.request = null;
        this.event = null;
    }

    /** Gets event type. */
    protected String getEvent()
    {
        return event;
    }

    /**
     * 发送Message业务消息
     */
    public void send(String event_type, XmlMessage xmlMsg) throws Exception
    {
        send(CONTENT_TYPE_MSAP, event_type, xmlMsg);
    }

    /**
     * 方法说明 :发送Message业务消息
     *
     * @author fuluo
     * @Date 2014-4-10
     *
     * @param xml_type
     *                 XML消息类型
     * @param event_type
     *                 消息类型
     * @param xmlMsg
     *                 xml消息
     * @throws Exception 
     */
    public void send(String xml_type, String event_type, XmlMessage xmlMsg) throws Exception
    {
        request = MessageFactory.createMessageRequest(provider, to, from, contact, event_type, null, xml_type, xmlMsg.toString());
        requestId = xmlMsg.getRequestId();
        event = event_type;
        state = STATE_WAITING;
        request_transaction = new TransactionClient(provider, request, this);
        request_transaction.request();
    }

    // Modified by hubin at 20131031 群组队列，呼叫中组
    public String getRequestId()
    {
        return requestId;
    }

    /** Terminates the subscription (subscription goes into 'terminated' state). */
    protected void terminate()
    {
        if (request_transaction != null)
            request_transaction.terminate();
    }

    /**
     * When the TransactionClient is (or goes) in "Proceeding" state and
     * receives a new 1xx provisional response
     */
    public void onTransProvisionalResponse(TransactionClient tc, Message resp)
    {
        Log.info("XmlMessageHandler", "onTransProvisionalResponse");
        // do nothing.
    }

    /**
     * When the TransactionClient goes into the "Completed" state receiving a
     * 2xx response
     */
    public void onTransSuccessResponse(TransactionClient tc, Message resp)
    {
        // the 200 OK response is expected.
        Log.info("XmlMessageHandler", "onTransSuccessResponse");

        state = STATE_WAITING;
    }

    /**
     * When the TransactionClient goes into the "Completed" state receiving a
     * 300-699 response
     */
    public void onTransFailureResponse(TransactionClient tc, Message resp)
    {
        state = STATE_END;
        // no failure response is received normally.
        Log.error("XmlMessageHandler", "onTransFailureResponse");

        StatusLine status_line = resp.getStatusLine();

        onXmlMessageNotAcceptable(event, status_line.getCode());
    }

    /**
     * When the TransactionClient goes into the "Terminated" state, caused by
     * transaction timeout
     */
    public void onTransTimeout(TransactionClient tc)
    {
        Log.info("XmlMessageHandler", "onTransTimeout");

        onTimeout(event);
    }

    public boolean isSubscribed(String topic)
    {
        return (state == STATE_WAITING && event.equalsIgnoreCase(topic));
    }

    public void handle(String event, XmlMessage msg)
    {
        Log.info("XmlMessageHandler.handle", "event:" + event);

        String reqId = msg.getRequestId();

        // if received the expected response.
        // FIXME: 夜服特殊处理
        if (event.equals(XmlMessage.NIGHTSERVICE_RESPONSE) || (reqId != null && reqId.equals(requestId)))
        {
            // notify invoker.
            onXmlMessageResponse(event, msg);
        }
        else
        {
            Log.error("XmlMessageHandler", "Not corresponding request-id: " + reqId);
        }
    }

    protected abstract void onXmlMessageResponse(String event, XmlMessage message);

    protected abstract void onXmlMessageNotAcceptable(String event, int code);
}
