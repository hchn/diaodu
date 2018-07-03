package com.jiaxun.sdk.acl.line.sip.event;

import com.jiaxun.sdk.util.xml.XmlMessage;


/**
 * User: liuyh
 * Date: 12-5-22
 * Time: обнГ8:40
 */
public interface SubscribeEventListener
{
    public boolean isSubscribed(String topic);

    public void handle(String topic, XmlMessage msg) throws Exception;

    public void onTimeout(String event);
}
