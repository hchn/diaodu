package com.jiaxun.sdk.util.xml;

import java.util.ArrayList;
import java.util.List;

import com.jiaxun.sdk.util.xml.conference.ConfMessage;
import com.jiaxun.sdk.util.xml.nightservice.NsItem;
import com.jiaxun.sdk.util.xml.userstatus.UsUserList;

/**
 * User: jiaxun
 * Date: 12-5-17
 * Time: 上午11:12
 */
public class XmlMessage {
    public static final String TAG_UTF8 = "utf-8";
    public static final String TAG_SIP_XML = "SIP_XML";
    public static final String TAG_State = "State";

    public static final String ATTRIBUTE_VERSION = "version";
    public static final String ATTRIBUTE_EVENT_TYPE = "EventType";
    public static final String ATTRIBUTE_REQUEST_ID = "requestID";
    public static final String ATTRIBUTE_VALUE = "value";
    
    public static final String VERSION_VALUE1 = "1.0";
    
    //会议消息事件
    public static final String EVENT_TYPE_CONFERENCE_REQUEST = "Conference-Request";
    public static final String EVENT_TYPE_CONFERENCE_REPORT = "Conference-Report";
    
    // 录音录像通知
    public static final String EVENT_TYPE_RECORD_REPORT = "Record-Report";
    
    //用户状态订阅消息事件
    public static final String EVENT_TYPE_USER_STATE_SUBSCRIBE = "User_State_Subscribe";
    public static final String EVENT_TYPE_USER_STATE_NOTIFY = "User_State_Notify";
    
    // night service
    public static final String NIGHTSERVICE_RESPONSE = "NightService-Response";
    
    String eventType = null;
    String requestId = null;
    String version = null;//20130625 zhoujy
    
    ArrayList<Element> parameters = new ArrayList<Element>();
    
    ConfMessage confMessage;
    
    UsUserList usUserList;
    NsItem nsItem;

    public XmlMessage() {
    }

    public String getVersion() {
        return eventType;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String type) {
        eventType = type;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String id) {
        requestId = id;
    }

    public Element getParameter(String name) {
        for(Element e : parameters) {
            if(e.getId() != null && e.getId().equals(name)) {
                return e;
            }
        }

        return null;
    }

    public List<Element> getParameters(String name) {
        List<Element> list = new ArrayList<Element>();

        for(Element e : parameters) {
            if(e.getId() != null && e.getId().equals(name)) {
                list.add(e);
            }
        }

        return list;
    }

    public List<Element> getParameters() {
        return parameters;
    }

    public void addParameter(Element parameter) {
        parameters.add(parameter);
    }

    public ConfMessage getConfMessage()
    {
        return confMessage;
    }

    public void setConfMessage(ConfMessage confMessage)
    {
        this.confMessage = confMessage;
    }
    
    public UsUserList getUsUserList()
    {
        return usUserList;
    }

    public void setUsUserList(UsUserList usUserList)
    {
        this.usUserList = usUserList;
    }
    
    public void setNsItem(NsItem nsItem)
    {
        this.nsItem = nsItem;
    }
    
    public NsItem getNsItem()
    {
        return nsItem;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(512);

        sb.append(XmlParser.XML_HEADER).append(XmlParser.CRLF);
        sb.append("<").append(XmlParser.TAG_SIP_XML).append(" ").append("version=\"0.1\"").append(" ").append(XmlParser.ATTRIBUTE_EVENT_TYPE).append("=\"").append(getEventType()).append("\" ").append(XmlParser.ATTRIBUTE_REQUEST_ID).append("=\"").append(getRequestId()).append("\">").append(XmlParser.CRLF);
        List<Element> list = getParameters();

        for(Element e : list) {
            sb.append("<").append(e.getId());

            List<Element.Attribute> attrList = e.getAttributes();
            ArrayList<Element> elements = e.getElements();
            for(Element.Attribute attr : attrList) {
                sb.append(" ").append(attr.name).append("=\"").append(attr.value).append("\"");
            }
            if(e.getValue() == null && e.getReason() == null && elements.isEmpty())
            {
            	sb.append("/>").append(XmlParser.CRLF);
            }else
            {
            	sb.append(">");
            	
            	if(e.getValue()==null && elements.isEmpty())
            	{
            		sb.append("<reason>").append(e.getReason()).append("</reason>");//20130625 zhoujy
            	}else
            	{
            		sb.append(e.getValue());
            		
            		// 针对二级element的临时解决方案
            		if (!elements.isEmpty())
                    {
            		    sb.append(XmlParser.CRLF);
            		    for (Element element : elements)
            		    {
            		        sb.append("<").append(e.getId());
            		        List<Element.Attribute> attributes = element.getAttributes();
            		        for(Element.Attribute attr : attributes) {
            		            sb.append(" ").append(attr.name).append("=\"").append(attr.value).append("\"");
            		        }
            		        sb.append(">");
            		        sb.append("</").append(element.getId()).append(">").append(XmlParser.CRLF);
            		    }
                    }
            	}
            	
            	sb.append("</").append(e.getId()).append(">").append(XmlParser.CRLF);
            }
        }

        sb.append("</").append(XmlParser.TAG_SIP_XML).append(">").append(XmlParser.CRLF);

        return sb.toString();
    }

}
