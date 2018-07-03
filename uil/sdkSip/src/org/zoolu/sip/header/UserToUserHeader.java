package org.zoolu.sip.header;

import org.zoolu.tools.Parser;

import com.jiaxun.sdk.util.SdkUtil;

/**
 * User: liuyh
 * Date: 12-5-29
 * Time: ÏÂÎç2:57
 */
public class UserToUserHeader extends ParametricHeader {
    public static final String FUNCTION_CODE_PREFIX = "0005";
    /** State delimiters. */
    private static final char[] delim = { ',', ';', ' ', '\t', '\n', '\r' };

    /** Costructs a new UserToUserHeader. */
    public UserToUserHeader(String code) {
        super(SipHeaders.User_To_User, SdkUtil.vFunctionNumber2UUIE(code));
    }

    /** Costructs a new UserToUserHeader. */
    public UserToUserHeader(Header hd) {
        super(hd);
    }

    /** Gets the user code. */
    public String getCode() {
         String res = new Parser(value).skipN(FUNCTION_CODE_PREFIX.length()).getWord(delim);
         if (res.contains("encoding")) {
             res = res.substring(0, res.indexOf("encoding"));
         }
         return SdkUtil.vUUIE2FunctionNumber(res);
    }

    /** Sets the 'encoding' param. */
    public UserToUserHeader setEncoding(String encoding) {
        setParameter("encoding", encoding);
        return this;
    }

    /** Whether there is the 'encoding' param. */
    public boolean hasEncoding() {
        return hasParameter("encoding");
    }

    /** Gets the 'encoding' param. */
    public String getEncoding() {
        return getParameter("encoding");
    }

    /** Sets the 'content' param. */
    public UserToUserHeader setContent(String content) {
        setParameter("content", content);
        return this;
    }

    /** Whether there is the 'content' param. */
    public boolean hasContent() {
        return hasParameter("content");
    }

    /** Gets the 'content' param. */
    public String getContent() {
        return getParameter("content");
    }
}
