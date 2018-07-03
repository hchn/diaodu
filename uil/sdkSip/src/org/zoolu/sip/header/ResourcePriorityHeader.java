package org.zoolu.sip.header;

import org.zoolu.tools.Parser;

/**
 * User: liuyh
 * Date: 12-5-29
 * Time: ÏÂÎç3:12
 */
public class ResourcePriorityHeader extends ParametricHeader {
    private static final String PRIORITY_PREFIX = "q735";
    /** State delimiters. */
    private static final char[] delim = { ',', ';', ' ', '\t', '\n', '\r' };

    /** Costructs a new ResourcePriorityHeader. */
    public ResourcePriorityHeader(int priority) {
        super(SipHeaders.Resource_Priority, PRIORITY_PREFIX + "." + priority);
    }

    /** Costructs a new ResourcePriorityHeader. */
    public ResourcePriorityHeader(Header hd) {
        super(hd);
    }

    /** Gets the priority. */
    public int getPriority() {
        int priority = 4;

        String s = new Parser(value).skipN(PRIORITY_PREFIX.length() + 1).getWord(delim);
        if(s != null && !s.equals("")) {
            try {
                priority = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        return priority;
    }

    /** Sets the 'priority' param. */
    public ResourcePriorityHeader setPriority(int priority) {
        setValue(PRIORITY_PREFIX + "." + priority);
        return this;
    }
}
