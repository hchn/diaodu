package com.jiaxun.sdk.util.xml;

import com.jiaxun.sdk.util.xml.Parser;

/**
 * User: jiaxun
 * Date: 12-5-17
 * Time: ÉÏÎç10:59
 */
public class XmlParser extends Parser {
    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String TAG_XML = "?xml";
    public static final String TAG_SIP_XML = "SIP_XML";
    public static final String TAG_PARAM = "param";
    public static final String TAG_MSISDN = "MSISDN";
    public static final String TAG_FN = "FN";
    public static final String TAG_State = "State";

    public static final String ATTRIBUTE_VERSION = "version";
    public static final String ATTRIBUTE_EVENT_TYPE = "EventType";
    public static final String ATTRIBUTE_REQUEST_ID = "requestID";

    public static final String CRLF = "\r\n";

    public XmlParser(String s) {
        super(s);
    }

    public XmlParser(String s, int i) {
        super(s, i);
    }

    public XmlParser(StringBuffer sb) {
        super(sb);
    }

    public XmlParser(StringBuffer sb, int i) {
        super(sb, i);
    }

    public boolean hasXmlHeader() {
        return str != null && startsWith("<" + TAG_XML);
    }

    public XmlParser skipXmlHeader() {
        if(hasXmlHeader()) {
            String end = "?>";
            goTo(end).skipN(end.length()).skipCRLF();
        }

        return this;
    }

    public XmlParser skipTagName(String tag) {
        if(!tag.startsWith("<")) {
            tag = "<" + tag;
        }

        int index = indexOf(tag);

        if(index > -1) {
            setPos(index + tag.length());
        }

        return this;
    }

    public String getTagName() {
        skipChars(new char[] {' ', '\r', '\n', '/', '>'});

        if(!hasMore()) return null;

        char c = nextChar();
        if(c == '<') {
            skipChar();

            c = nextChar();
            if(c == '/') {
                goTo('>');

                return getTagName();
            } else {
                int index = indexOf(new char[] {' ', '=', '/', '>'});
                if(index > -1) {
                    return getString(index - getPos());
                }
            }
        }

        return null;
    }


    public String getAttributeName() {
        skipWSP();

        if(!hasMore()) return null;

        char ch = nextChar();
        if(ch == '<' || ch == '/' || ch == '>') return null;

        int index = indexOf('=');
        if(index > -1) {
            return getString(index - getPos()).trim();
        }

        return null;
    }

    public String getAttributeValue() {
        skipChars(new char[]{'='});

        char c = nextChar();
        if(c == '"') {
            skipChar();
            int index = indexOf('"');

            if(index > -1) {
                String str = getString(index - getPos());
                skipChar();

                return str;
            }
        } else {
            int index = indexOf(new char[]{' ', '>', '/'});

            if(index > -1) {
                String str = getString(index - getPos());
                skipChar();

                return str;
            }
        }

        return null;
    }

    public String getTagContent() {
        return null;
    }
}
