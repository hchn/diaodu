package com.jiaxun.sdk.util.xml.nightservice;

/**
 * 说明：夜服操作失败原因
 *
 * @author  hubin
 *
 * @Date 2015-9-10
 */
public class NsReason
{
    public final static String TAG = "reason";

    public final static String ATTRIBUTE_VALUE = "value";
    
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
