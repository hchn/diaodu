package com.jiaxun.sdk.util.xml;

import java.io.StringReader;
import java.io.StringWriter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import android.text.TextUtils;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.Element.Attribute;
import com.jiaxun.sdk.util.xml.conference.ConfCall;
import com.jiaxun.sdk.util.xml.conference.ConfItem;
import com.jiaxun.sdk.util.xml.conference.ConfMedia;
import com.jiaxun.sdk.util.xml.conference.ConfMediaInfo;
import com.jiaxun.sdk.util.xml.conference.ConfMessage;
import com.jiaxun.sdk.util.xml.conference.ConfTask;
import com.jiaxun.sdk.util.xml.conference.ConfToneInfo;
import com.jiaxun.sdk.util.xml.nightservice.NsItem;
import com.jiaxun.sdk.util.xml.nightservice.NsReason;
import com.jiaxun.sdk.util.xml.userstatus.UsItem;
import com.jiaxun.sdk.util.xml.userstatus.UsUserList;

/**
 * User: jiaxun
 * Date: 12-5-17
 * Time: 下午1:57
 */
public class XmlMessageFactory
{
    private static long requestId = 1;

    private static synchronized long createRequestId()
    {
        return requestId++;
    }

    /**
     * pull解析会议xml
     */
    public static XmlMessage parseConfXml(String body)
    {
        if (TextUtils.isEmpty(body))
        {
            return null;
        }
        XmlMessage msg = null;
        ConfMessage confMessage = null;
        ConfItem confItem = null;
        ConfMediaInfo confMediaInfo = null;
        ConfMedia confMedia = null;
        ConfCall confCall = null;
        ConfTask confTask = null;
        ConfToneInfo confToneInfo = null;

        StringReader sr = new StringReader(body);
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(sr);

            String elementName = null;
            String namespace = null;
            int eventType = xpp.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        elementName = xpp.getName();
                        namespace = xpp.getNamespace();
                        if (XmlMessage.TAG_SIP_XML.equals(elementName))
                        {
                            msg = new XmlMessage();
                            msg.setEventType(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_EVENT_TYPE));
                            msg.setRequestId(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_REQUEST_ID));
                        }
                        else if (ConfMessage.TAG.equals(elementName))
                        {
                            confMessage = new ConfMessage();
                            confMessage.setConferenceURI(xpp.getAttributeValue(namespace, ConfMessage.ATTRIBUTE_CONFERENCEURI));
                            confMessage.setCode(xpp.getAttributeValue(namespace, ConfMessage.ATTRIBUTE_CODE));
                            msg.setConfMessage(confMessage);
                        }
                        else if (ConfItem.TAG.equals(elementName))
                        {
                            confItem = new ConfItem();
                            confItem.setUserCallID(xpp.getAttributeValue(namespace, ConfItem.ATTRIBUTE_USERCALLID));
                            confItem.setUserUri(xpp.getAttributeValue(namespace, ConfItem.ATTRIBUTE_USERURI));
                            confMessage.setItem(confItem);
                        }
                        else if (ConfMediaInfo.TAG.equals(elementName))
                        {
                            confMediaInfo = new ConfMediaInfo();
                            confItem.setMediaInfo(confMediaInfo);
                        }
                        else if (ConfMediaInfo.TAG_ENDPOINT.equals(elementName))
                        {
                            confMediaInfo.setTag(xpp.getAttributeValue(namespace, ConfMediaInfo.ATTRIBUTE_TAG));
                        }
                        else if (ConfMedia.TAG.equals(elementName))
                        {
                            confMedia = new ConfMedia();
                            if (TextUtils.isEmpty(xpp.getAttributeValue(namespace, ConfMedia.ATTRIBUTE_ID)))
                            {
                                confMedia.setId(xpp.getAttributeValue(namespace, ConfMedia.ATTRIBUTE_ID_BIG));
                            }
                            else
                            {
                                confMedia.setId(xpp.getAttributeValue(namespace, ConfMedia.ATTRIBUTE_ID));
                            }
                            confMedia.setStatus(xpp.getAttributeValue(namespace, ConfMedia.ATTRIBUTE_STATUS));
                            confMedia.setEnable(xpp.getAttributeValue(namespace, ConfMedia.ATTRIBUTE_ENABLE));
                            confMedia.setBroadcast(xpp.getAttributeValue(namespace, ConfMedia.ATTRIBUTE_BROADCAST));
                            confMediaInfo.addMedia(confMedia);
                            String tag = xpp.getAttributeValue(namespace, ConfMediaInfo.ATTRIBUTE_TAG);
                            if (!TextUtils.isEmpty(tag))
                            {
                                confMediaInfo.setTag(tag);
                            }
                        }
                        else if (ConfCall.TAG.equals(elementName))
                        {
                            confCall = new ConfCall();
                            confCall.setStatus(xpp.getAttributeValue(namespace, ConfCall.ATTRIBUTE_STATUS));
                            confItem.setCall(confCall);
                        }
                        else if (ConfCall.TAG_REASON.equals(elementName))
                        {
                            confCall.setReason(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_VALUE));
                        }
                        else if (ConfTask.TAG.equals(elementName))
                        {
                            confTask = new ConfTask();
                            confItem.setTask(confTask);
                            confTask.setId(xpp.getAttributeValue(namespace, ConfTask.ATTRIBUTE_ID));
                            confTask.setServer(xpp.getAttributeValue(namespace, ConfTask.ATTRIBUTE_SERVER));
                        }
                        else if (ConfToneInfo.TAG_TONE.equals(elementName))
                        {
                            confToneInfo = new ConfToneInfo();
                            confItem.setToneInfo(confToneInfo);
                            confToneInfo.setId(xpp.getAttributeValue(namespace, ConfToneInfo.ATTRIBUTE_ID));
                            confToneInfo.setEvent(xpp.getAttributeValue(namespace, ConfToneInfo.ATTRIBUTE_EVENT));
                            confToneInfo.setStatus(xpp.getAttributeValue(namespace, ConfToneInfo.ATTRIBUTE_STATUS));
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType = xpp.next(); // 下一个事件类型
            }
        }
        catch (Exception e)
        {
            Log.exception("XmlMessageFactory", e);
        }

        return msg;
    }
    
    public static XmlMessage parseNightServiceXml(String body)
    {
        if (TextUtils.isEmpty(body))
        {
            return null;
        }
        XmlMessage msg = null;
        NsItem nsItem = null;
        NsReason nsReason = null;

        StringReader sr = new StringReader(body);
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(sr);

            String elementName = null;
            String namespace = null;
            int eventType = xpp.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        elementName = xpp.getName();
                        namespace = xpp.getNamespace();
                        if (XmlMessage.TAG_SIP_XML.equals(elementName))
                        {
                            msg = new XmlMessage();
                            msg.setEventType(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_EVENT_TYPE));
                            msg.setRequestId(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_REQUEST_ID));
                        }
                        else if (NsItem.TAG.equals(elementName))
                        {
                            nsItem = new NsItem();
                            nsItem.setUserUrl(xpp.getAttributeValue(namespace, NsItem.ATTRIBUTE_USER_URI));
                            nsItem.setEvent(xpp.getAttributeValue(namespace, NsItem.ATTRIBUTE_EVENT));
                            nsItem.setValue(xpp.getAttributeValue(namespace, NsItem.ATTRIBUTE_VALUE));
                            msg.setNsItem(nsItem);
                        }
                        else if (NsReason.TAG.equals(elementName))
                        {
                            nsReason = new NsReason();
                            nsReason.setValue(xpp.getAttributeValue(namespace, NsReason.ATTRIBUTE_VALUE));
                            nsItem.setNsReason(nsReason);
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType = xpp.next(); // 下一个事件类型
            }
        }
        catch (Exception e)
        {
            Log.exception("XmlMessageFactory", e);
        }

        return msg;
    }

    /**
     * pull解析用户状态xml
     */
    public static XmlMessage parseUsXml(String body)
    {
        if (TextUtils.isEmpty(body))
        {
            return null;
        }
        XmlMessage msg = null;
        UsUserList usUserList = null;
        UsItem usItem = null;

        StringReader sr = new StringReader(body);
        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(sr);

            String elementName = null;
            String namespace = null;
            int eventType = xpp.getEventType();
            while (XmlPullParser.END_DOCUMENT != eventType)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_TAG:
                        elementName = xpp.getName();
                        namespace = xpp.getNamespace();
                        if (XmlMessage.TAG_SIP_XML.equals(elementName))
                        {
                            msg = new XmlMessage();
                            msg.setEventType(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_EVENT_TYPE));
                            msg.setRequestId(xpp.getAttributeValue(namespace, XmlMessage.ATTRIBUTE_REQUEST_ID));
                        }
                        else if (UsUserList.TAG.equals(elementName))
                        {
                            usUserList = new UsUserList();
                            msg.setUsUserList(usUserList);
                        }
                        else if (UsItem.TAG.equals(elementName))
                        {
                            usItem = new UsItem();
                            usItem.setId(xpp.getAttributeValue(namespace, UsItem.ATTRIBUTE_ID));
                            usItem.setStatus(xpp.getAttributeValue(namespace, UsItem.ATTRIBUTE_STATUS));
                            usUserList.addUsItem(usItem);
                        }

                        break;

                    case XmlPullParser.END_TAG:
                        break;
                }

                eventType = xpp.next(); // 下一个事件类型
            }
        }
        catch (Exception e)
        {
            Log.exception("XmlMessageFactory", e);
        }

        return msg;
    }

    public static XmlMessage parse(String body)
    {
        XmlMessage msg = new XmlMessage();

        XmlParser parser = new XmlParser(body);
        parser.skipXmlHeader();

        parser.skipTagName(XmlParser.TAG_SIP_XML);

        String version = parser.getAttributeName();// 20130625 zhoujy
        if (XmlParser.ATTRIBUTE_VERSION.equals(version))
        {
            msg.setVersion(parser.getAttributeValue());
        }
        else
        {
            System.out.println("Invalid Attribute Name: " + version);
        }

        String type = parser.getAttributeName();
        if (XmlParser.ATTRIBUTE_EVENT_TYPE.equals(type))
        {
            msg.setEventType(parser.getAttributeValue());
        }
        else
        {
            System.out.println("Invalid Attribute Name: " + type);
        }

        String request = parser.getAttributeName();
        if (XmlParser.ATTRIBUTE_REQUEST_ID.equals(request))
        {
            msg.setRequestId(parser.getAttributeValue());
        }
        else
        {
            System.out.println("Invalid Attribute Name: " + request);
        }

        String element;

        while ((element = parser.getTagName()) != null)
        {
            Element attr = new Element(element, null);

            String attrName;

            while ((attrName = parser.getAttributeName()) != null)
            {
                attr.addAttribute(new Element.Attribute(attrName, parser.getAttributeValue()));
            }

            if (parser.nextChar() == '/')// 20130625 zhoujy
            {

            }
            else
            {
                String tmp = parser.getTagName();
                if (tmp != null && tmp.equals("reason"))
                {
                    parser.skipChars(new char[] { ' ', '\r', '\n', '>' });

                    int index = parser.indexOf("</reason>");

                    if (index > -1)
                    {
                        String str = parser.getString(index - parser.getPos());

                        attr.addReason(str);
                    }

                    index = parser.indexOf("</" + element + ">");
                    parser.setPos(index + ("</" + element + ">").length());
                }
                else
                {
                    parser.skipChars(new char[] { ' ', '\r', '\n', '>' });

                    int index = parser.indexOf("</" + element + ">");

                    if (index > -1)
                    {
                        String str = parser.getString(index - parser.getPos());

                        attr.setValue(str);
                    }
                }
            }

            msg.addParameter(attr);
        }

        return msg;
    }

    /**
     * 方法说明 :功能码注册消息
     * 
     * @author fuluo
     * @Date 2014-4-8
     * 
     * @param msisdn
     *            帐号
     * @param fn
     *            功能码
     * 
     * @return
     */
    public static XmlMessage createFnRegMsg(String msisdn, String fn)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setEventType("FN_Register_Request");
        msg.setRequestId("" + requestId);

        Element x = new Element("FN", null);
        x.addAttribute(new Attribute("value", fn));
        msg.addParameter(x);

        return msg;
    }

    /**
     * 方法说明 :功能码强制注销消息
     * 
     * @author fuluo
     * @Date 2014-4-8
     * 
     * @param msisdn
     *            自身帐号
     * @param num
     *            注销用户帐号
     * @param fn
     *            注销功能码
     * 
     * @return
     */
    public static XmlMessage createFnForceDeregMsg(String msisdn, String num, String fn)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setEventType("FN-Force-Unregister-Request");
        msg.setRequestId("" + requestId);

        Element e = new Element("requester", null);
        e.addAttribute("value", msisdn);
        msg.addParameter(e);

        e = new Element("MSISDN", null);
        e.addAttribute("value", num);
        msg.addParameter(e);

        e = new Element("FN", null);
        e.addAttribute("value", fn);
        msg.addParameter(e);

        return msg;
    }

    /**
     * 方法说明 :功能码注销消息
     * 
     * @author fuluo
     * @Date 2014-4-8
     * 
     * @param msisdn
     *            帐号
     * @param fn
     *            功能码
     * 
     * @return
     */
    public static XmlMessage createFnDeregMsg(String msisdn, String fn)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setEventType("FN-Unregister-Request");
        msg.setRequestId("" + requestId);

        msg.addParameter(new Element("FN", fn));

        return msg;
    }

    /**
     * 方法说明 : 功能码单个查询消息
     * 
     * @author fuluo
     * @Date 2014-4-8
     * 
     * @param msisdn
     *            帐号
     * @param fn
     *            功能号码
     * 
     * @return
     */
    public static XmlMessage createFnSingleQueryMsg(String msisdn, String fn)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setEventType("FN-Query-Single-Request");
        msg.setRequestId("" + requestId);

        Element e = new Element("requester", null);
        e.addAttribute("value", msisdn);
        msg.addParameter(e);

        e = new Element("FN", null);
        e.addAttribute("value", fn);
        msg.addParameter(e);

        return msg;
    }

    /**
     * 方法说明 : 功能码列表查询消息
     * 
     * @author fuluo
     * @Date 2014-4-8
     * 
     * @param msisdn
     *            帐号
     * 
     * @return
     */
    public static XmlMessage createFnQueryMsg(String msisdn)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setEventType("FN-Query-Request");
        msg.setRequestId("" + requestId);

        return msg;
    }

    public static XmlMessage createRspNotifyMsg(String eventType, String requestId, String result, String reason)
    {
        XmlMessage msg = new XmlMessage();
        msg.setRequestId(requestId);
        msg.setEventType(eventType);
        Element e = new Element("result", null);
        e.addAttribute("value", result);
        e.addAttribute("reason", reason);
        msg.addParameter(e);

        return msg;
    }

    /*
     * 0：无条件，1：无应答，2：遇忙，3：不可达
     */

    public static final int CALL_FORWARD_TYPE_CFU = 0;
    public static final int CALL_FORWARD_TYPE_CFNA = 1;
    public static final int CALL_FORWARD_TYPE_CFB = 2;
    public static final int CALL_FORWARD_TYPE_CFF = 3;

    public static final int CALL_FORWARD_ENABLE = 0;
    public static final int CALL_FORWARD_DISABLE = 1;

    public static XmlMessage createSetCallForwardMsg(String original, String alwaysNumber, String noResponseNumber, int noResponseTime, String busyNumber,
            String noReachNumber)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setRequestId("" + requestId);

        msg.setEventType("CallForward-Set-Request");
        msg.addParameter(new Element("MSISDN", original));

        msg.addParameter(new Element("CFU", alwaysNumber));
        msg.addParameter(new Element("CFNA", noResponseNumber));
        msg.addParameter(new Element("CFB", busyNumber));
        msg.addParameter(new Element("CFF", noReachNumber));
        msg.addParameter(new Element("Timeout", "" + noResponseTime));

        return msg;
    }

    public static XmlMessage createSetCallForwardMsg(String original, int type, int enable, String number, int time)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setRequestId("" + requestId);

        msg.setEventType("CallForward-Set-Request");
        msg.addParameter(new Element("MSISDN", original));

        if (type == CALL_FORWARD_TYPE_CFU)
        {
            if (enable == CALL_FORWARD_ENABLE)
            {
                msg.addParameter(new Element("CFU", number));
            }
            else
            {
                msg.addParameter(new Element("CFU", ""));
            }

            msg.addParameter(new Element("CFNA", ""));
            msg.addParameter(new Element("CFB", ""));
            msg.addParameter(new Element("CFF", ""));
        }
        else if (type == CALL_FORWARD_TYPE_CFNA)
        {
            if (enable == CALL_FORWARD_ENABLE)
            {
                msg.addParameter(new Element("CFNA", number));
            }
            else
            {
                msg.addParameter(new Element("CFNA", ""));
            }

            msg.addParameter(new Element("CFU", ""));
            msg.addParameter(new Element("CFB", ""));
            msg.addParameter(new Element("CFF", ""));
            msg.addParameter(new Element("Timeout", "" + time));
        }
        else if (type == CALL_FORWARD_TYPE_CFB)
        {
            if (enable == CALL_FORWARD_ENABLE)
            {
                msg.addParameter(new Element("CFB", number));
            }
            else
            {
                msg.addParameter(new Element("CFB", ""));
            }

            msg.addParameter(new Element("CFU", ""));
            msg.addParameter(new Element("CFNA", ""));
            msg.addParameter(new Element("CFF", ""));
        }
        else if (type == CALL_FORWARD_TYPE_CFF)
        {
            if (enable == CALL_FORWARD_ENABLE)
            {
                msg.addParameter(new Element("CFF", number));
            }
            else
            {
                msg.addParameter(new Element("CFF", ""));
            }

            msg.addParameter(new Element("CFU", ""));
            msg.addParameter(new Element("CFNA", ""));
            msg.addParameter(new Element("CFB", ""));
        }
        else
        {
            if (enable == CALL_FORWARD_ENABLE)
            {
                msg.addParameter(new Element("CFU", number));
            }
            else
            {
                msg.addParameter(new Element("CFU", ""));
            }

            msg.addParameter(new Element("CFNA", ""));
            msg.addParameter(new Element("CFB", ""));
            msg.addParameter(new Element("CFF", ""));
        }

        return msg;
    }

    public static XmlMessage createQueryCallForwardMsg(String original)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setRequestId("" + requestId);
        msg.setEventType("CallForward-Query-Request");
        msg.addParameter(new Element("MSISDN", original));

        return msg;
    }

    public static XmlMessage createVersionResponseMsg(String requestId, String softwareVer, String osVersion, String mobileModule)
    {
        // requestId++;

        XmlMessage msg = new XmlMessage();
        msg.setRequestId("" + requestId);
        msg.setEventType("Version-Query-Response");
        msg.addParameter(new Element("Software-Ver", softwareVer));
        msg.addParameter(new Element("Os-Ver", osVersion));
        msg.addParameter(new Element("Mobile-Phone-Model", mobileModule));

        return msg;
    }

    /**
     * 主席加入成员消息
     */
    public static String createJoinUserMsg(String confUrl, String userUrl) throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_CONFERENCE_REQUEST);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);

        xmlSerializer.startTag(null, ConfMessage.TAG);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CONFERENCEURI, confUrl);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CODE, ConfMessage.CODE_JOINUSER);

        xmlSerializer.startTag(null, ConfItem.TAG);
        xmlSerializer.attribute(null, ConfItem.ATTRIBUTE_USERURI, userUrl);

        xmlSerializer.startTag(null, ConfItem.TAG_ROLES);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_VALUE, ConfItem.ROLES_ATTENDEE);
        xmlSerializer.endTag(null, ConfItem.TAG_ROLES);

        xmlSerializer.startTag(null, ConfItem.TAG_JOININGMETHOD);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_VALUE, ConfItem.JOININGMETHOD_DIALED_OUT);
        xmlSerializer.endTag(null, ConfItem.TAG_JOININGMETHOD);

        xmlSerializer.startTag(null, ConfMediaInfo.TAG);

        xmlSerializer.startTag(null, ConfMedia.TAG);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_ID, ConfMedia.ID_VIDEO);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_STATUS, ConfMedia.STATUS_SENDRECV);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_ENABLE, ConfMedia.ENABLE_OFF);
        xmlSerializer.endTag(null, ConfMedia.TAG);

        xmlSerializer.startTag(null, ConfMedia.TAG);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_ID, ConfMedia.ID_AUDIO);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_STATUS, ConfMedia.STATUS_SENDRECV);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_ENABLE, ConfMedia.ENABLE_ON);
        xmlSerializer.endTag(null, ConfMedia.TAG);

        xmlSerializer.endTag(null, ConfMediaInfo.TAG);

        xmlSerializer.endTag(null, ConfItem.TAG);

        xmlSerializer.endTag(null, ConfMessage.TAG);
        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }

    /**
     * 主席释放成员消息
     */
    public static String createUnJoinUserMsg(String confUrl, String userUrl) throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_CONFERENCE_REQUEST);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);

        xmlSerializer.startTag(null, ConfMessage.TAG);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CONFERENCEURI, confUrl);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CODE, ConfMessage.CODE_UNJOINUSER);

        xmlSerializer.startTag(null, ConfItem.TAG);
        xmlSerializer.attribute(null, ConfItem.ATTRIBUTE_USERURI, userUrl);
        xmlSerializer.endTag(null, ConfItem.TAG);

        xmlSerializer.endTag(null, ConfMessage.TAG);
        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }

    /**
     * 主席临时退会/临时退会后再入会消息
     */
    public static String createStepOutOrComebackMsg(String confUrl, String stepOutOrComeback) throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_CONFERENCE_REQUEST);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);

        xmlSerializer.startTag(null, ConfMessage.TAG);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CONFERENCEURI, confUrl);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CODE, stepOutOrComeback);

        xmlSerializer.startTag(null, ConfItem.TAG);
        xmlSerializer.attribute(null, ConfItem.ATTRIBUTE_USERURI, "");
        xmlSerializer.endTag(null, ConfItem.TAG);

        xmlSerializer.endTag(null, ConfMessage.TAG);

        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }

    /**
     * 媒体控制消息：允许/禁止成员发言，视频打开/关闭
     */
    public static String createMediaControlMsg(String confUrl, String userUrl, String id, String status, String broadcast, String tag) throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_CONFERENCE_REQUEST);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_VERSION, XmlMessage.VERSION_VALUE1);

        xmlSerializer.startTag(null, ConfMessage.TAG);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CONFERENCEURI, confUrl);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CODE, ConfMessage.CODE_MEDIACONTROL);

        xmlSerializer.startTag(null, ConfItem.TAG);
        xmlSerializer.attribute(null, ConfItem.ATTRIBUTE_USERURI, userUrl);
        xmlSerializer.startTag(null, ConfMediaInfo.TAG);

        xmlSerializer.startTag(null, ConfMedia.TAG);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_ID_BIG, id);
        xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_STATUS, status);
        if (broadcast != null)
        {
            xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_BROADCAST, broadcast);
            xmlSerializer.attribute(null, ConfMedia.ATTRIBUTE_TAG, tag);
        }
        xmlSerializer.endTag(null, ConfMedia.TAG);

        xmlSerializer.endTag(null, ConfMediaInfo.TAG);
        xmlSerializer.endTag(null, ConfItem.TAG);

        xmlSerializer.endTag(null, ConfMessage.TAG);
        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }
    
    /**
     * 会议提示音播放/关闭消息
     */
    public static String createToneInfoMsg(String confUrl, boolean play) throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_CONFERENCE_REQUEST);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);

        xmlSerializer.startTag(null, ConfMessage.TAG);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CONFERENCEURI, confUrl);
        xmlSerializer.attribute(null, ConfMessage.ATTRIBUTE_CODE, ConfMessage.CODE_TONESENDING);

        xmlSerializer.startTag(null, ConfItem.TAG);
        xmlSerializer.attribute(null, ConfItem.ATTRIBUTE_USERURI, ConfItem.USERCALLID_ALL);
        
        xmlSerializer.startTag(null, ConfToneInfo.TAG);
        
        xmlSerializer.startTag(null, ConfToneInfo.TAG_TONE);
        xmlSerializer.attribute(null, ConfToneInfo.ATTRIBUTE_ID, ConfToneInfo.ID_MEETINGCREATING);
        if(play)
            xmlSerializer.attribute(null, ConfToneInfo.ATTRIBUTE_EVENT, ConfToneInfo.EVENT_PLAY);
        else
            xmlSerializer.attribute(null, ConfToneInfo.ATTRIBUTE_EVENT, ConfToneInfo.EVENT_STOP);
        xmlSerializer.endTag(null, ConfToneInfo.TAG_TONE);
        
        xmlSerializer.endTag(null, ConfToneInfo.TAG);
        
        xmlSerializer.endTag(null, ConfItem.TAG);

        xmlSerializer.endTag(null, ConfMessage.TAG);

        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }

    /**
     * 订阅用户状态-清空用户消息
     */
    public static String createClearUserMsg() throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_USER_STATE_SUBSCRIBE);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_VERSION, XmlMessage.VERSION_VALUE1);

        xmlSerializer.startTag(null, UsUserList.TAG);
        xmlSerializer.attribute(null, UsUserList.ATTRIBUTE_REQUEST, UsUserList.REQUEST_CLEAR);
        xmlSerializer.endTag(null, UsUserList.TAG);

        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }

    /**
     * 订阅用户状态-添加/删除用户消息
     */
    public static String createAddOrDeleteUserMsg(String[] users, boolean isAdd) throws Exception
    {
        long requestId = createRequestId();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = factory.newSerializer();
        StringWriter writer = new StringWriter();
        xmlSerializer.setOutput(writer);

        xmlSerializer.startDocument(XmlMessage.TAG_UTF8, null);

        xmlSerializer.startTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_EVENT_TYPE, XmlMessage.EVENT_TYPE_USER_STATE_SUBSCRIBE);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_REQUEST_ID, "" + requestId);
        xmlSerializer.attribute(null, XmlMessage.ATTRIBUTE_VERSION, XmlMessage.VERSION_VALUE1);

        xmlSerializer.startTag(null, UsUserList.TAG);
        xmlSerializer.attribute(null, UsUserList.ATTRIBUTE_REQUEST, UsUserList.REQUEST_UPDATE);

        for (String user : users)
        {
            xmlSerializer.startTag(null, UsItem.TAG);
            xmlSerializer.attribute(null, UsItem.ATTRIBUTE_ID, user);
            if (isAdd)
                xmlSerializer.attribute(null, UsItem.ATTRIBUTE_OPERATE, UsItem.OPERATE_ADD);
            else
                xmlSerializer.attribute(null, UsItem.ATTRIBUTE_OPERATE, UsItem.OPERATE_DELETE);
            xmlSerializer.endTag(null, UsItem.TAG);
        }

        xmlSerializer.endTag(null, UsUserList.TAG);

        xmlSerializer.endTag(null, XmlMessage.TAG_SIP_XML);
        xmlSerializer.endDocument();

        return writer.toString();
    }

    /**
     * 云镜控制消息
     */
    public static XmlMessage createCameraControlMsg(String confUri, String userUri, String command, String para1, String para2, String para3)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setRequestId(requestId + "");
        msg.setEventType("Control_Camera");
        Element element = new Element("item", "");
        element.addAttribute(new Attribute("conferenceURI", confUri));
        element.addAttribute(new Attribute("userURI", userUri));
        element.addAttribute(new Attribute("Command", command));
        element.addAttribute(new Attribute("CommandPara1", para1));
        element.addAttribute(new Attribute("CommandPara2", para2));
        element.addAttribute(new Attribute("CommandPara3", para3));
        msg.addParameter(element);

        return msg;
    }
    
    /**
     * 方法说明 :夜服打开消息
     * 
     * @author fuluo
     * @Date 2015-6-8
     * 
     * @param msisdn
     *            帐号
     * 
     * @return
     */
    public static XmlMessage createNightServiceMsg(String msisdn, boolean open)
    {
        long requestId = createRequestId();

        XmlMessage msg = new XmlMessage();
        msg.setEventType("NightService-Request");
        msg.setRequestId("" + requestId);

        Element x = new Element("item", null);
        x.addAttribute(new Attribute("userNum", msisdn));
        if(open)
            x.addAttribute(new Attribute("event", "open"));
        else
            x.addAttribute(new Attribute("event", "close"));
        msg.addParameter(x);

        return msg;
    }

}
