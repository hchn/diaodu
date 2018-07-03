package com.jiaxun.sdk.acl.line.sip.service.version;

import org.zoolu.sip.provider.SipProvider;

import com.jiaxun.sdk.acl.line.sip.ua.XmlMessageHandler;
import com.jiaxun.sdk.util.xml.XmlMessage;



public class VersionHandler extends XmlMessageHandler {
    public static final int RESULT_CODE_ERROR = 0;
    public static final int RESULT_CODE_OK = 1;


    public static final String VERSION_SERVER_NAME = "poc-server";
    
    public VersionHandler(SipProvider provider, String target_url, String contact_url) {
        super(provider, target_url, contact_url);
    }

    @Override
    protected void onXmlMessageResponse(String event, XmlMessage msg) {
        if(VersionEventHandler.VERSION_OPERATION.equals(event)) {
//            String event_type = msg.getEventType();
        }
    }

    @Override
    protected void onXmlMessageNotAcceptable(String event, int code) {
    }

    @Override
    public void onTimeout(String event) {
    }

    
}
