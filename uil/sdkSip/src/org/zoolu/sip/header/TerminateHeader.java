package org.zoolu.sip.header;


/**
 * User: liuyh
 * Date: 12-5-29
 * Time: ����2:57
 */
public class TerminateHeader extends ParametricHeader {
    


    /** Costructs a new UserToUserHeader. */
    public TerminateHeader(String code) {
        super(SipHeaders.Terminate_Priority, code);
    }





}
