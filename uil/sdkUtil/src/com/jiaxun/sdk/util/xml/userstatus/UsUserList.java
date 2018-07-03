package com.jiaxun.sdk.util.xml.userstatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：用户状态订阅-用户列表消息
 *
 * @author  fuluo
 *
 * @Date 2015-5-11
 */
public class UsUserList
{
    public final static String TAG = "userlist";
    
    public final static String ATTRIBUTE_REQUEST = "request";
    
    public final static String REQUEST_CLEAR = "clear";
    public final static String REQUEST_UPDATE = "update";
    
    private String request;
    
    private List<UsItem> usItemList = new ArrayList<UsItem>();

    public String getRequest()
    {
        return request;
    }

    public void setRequest(String request)
    {
        this.request = request;
    }

    public List<UsItem> getUsItemList()
    {
        return usItemList;
    }
    
    public void addUsItem(UsItem usItem)
    {
        usItemList.add(usItem);
    }

}
