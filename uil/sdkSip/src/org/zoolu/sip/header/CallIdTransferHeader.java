package org.zoolu.sip.header;

import org.zoolu.tools.Parser;

/**
 * User: liuyh
 * Date: 12-5-29
 * Time: ÏÂÎç2:57
 */
public class CallIdTransferHeader extends ParametricHeader {
    public static final String CALL_ID_TRANSFER = "CallID-Transfer";
 
    /** Costructs a new UserToUserHeader. */
    public CallIdTransferHeader(String code) {
        super(CALL_ID_TRANSFER, code);
    }

    /** Costructs a new UserToUserHeader. */
    public CallIdTransferHeader(Header hd) {
        super(hd);
    }

    /** Gets Call-Id of CallIdHeader */
    public String getCallIdTransfer() {
        return (new Parser(value)).getString();
    }

    /** Sets Call-Id of CallIdHeader */
    public void setCallIdTransfer(String callId) {
        value = callId;
    }
}
