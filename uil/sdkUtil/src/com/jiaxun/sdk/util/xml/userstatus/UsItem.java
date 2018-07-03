package com.jiaxun.sdk.util.xml.userstatus;
/**
 * 说明：用户状态订阅-用户消息
 *
 * @author  fuluo
 *
 * @Date 2015-5-11
 */
public class UsItem
{
    public final static String TAG = "item";
    
    public final static String ATTRIBUTE_ID = "id";
    public final static String ATTRIBUTE_OPERATE = "operate";
    public final static String ATTRIBUTE_STATUS = "status";
    
    public final static String OPERATE_ADD = "add";
    public final static String OPERATE_DELETE = "delete";
    
    public final static String STATUS_ONLINE = "online";
    public final static String STATUS_OFFLINE = "offline";
    
    private String id;
    
    private String status;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}
