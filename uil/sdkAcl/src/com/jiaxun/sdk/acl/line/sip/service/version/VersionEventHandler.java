package com.jiaxun.sdk.acl.line.sip.service.version;

import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.acl.line.sip.event.SubscribeEventListener;
import com.jiaxun.sdk.util.xml.Element;
import com.jiaxun.sdk.util.xml.XmlMessage;

/**
 * User: liuyh
 * Date: 12-5-22
 * Time: ÏÂÎç8:52
 */
public class VersionEventHandler implements SubscribeEventListener {
    public static final String VERSION_OPERATION = "Version-Operator";
    public static final String VERSION_QUERY_NOTIFY = "Version-Query-Request";
    
    private SipAdapter sipPocEngine;
    
    public VersionEventHandler() {
    }
    
    @Override
    public boolean isSubscribed(String topic) {
        return VERSION_QUERY_NOTIFY.equalsIgnoreCase(topic);
    }

    @Override
    public void handle(String topic, XmlMessage msg) throws Exception {
        String eventType = msg.getEventType();
        if(VERSION_QUERY_NOTIFY.equalsIgnoreCase(eventType)) {
            
        	sipPocEngine.sendVersionInformation(msg.getRequestId());
            
            Element msisdn = msg.getParameter("MSISDN");
            if(msisdn != null) {
                if(msisdn.getValue() != null) {
//                    fireFnForceDeregisterEvent(msisdn.getValue());
                }
            } else {
//                    printLog("No State value in FN-Register-Response. ", LogLevel.HIGH);
            }
        } else {
//                printLog("Not corresponding event-type: " + event_type, LogLevel.MEDIUM);
        }
    }

    @Override
    public void onTimeout(String event) {

    }
    
    public void setSipPocEngine(SipAdapter sipPocEngine) {
		this.sipPocEngine = sipPocEngine;
	}

}
