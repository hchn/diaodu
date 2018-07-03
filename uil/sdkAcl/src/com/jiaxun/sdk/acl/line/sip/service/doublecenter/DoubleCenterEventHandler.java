package com.jiaxun.sdk.acl.line.sip.service.doublecenter;

import com.jiaxun.sdk.acl.line.sip.event.SubscribeEventListener;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.Element;
import com.jiaxun.sdk.util.xml.XmlMessage;

/**
 * 说明：双中心切换通知
 *
 * @author  fuluo
 *
 * @Date 2014-4-12
 */
public class DoubleCenterEventHandler implements SubscribeEventListener
{
    public static final String EVENT_TYPE_PUSH_SWITCH_HOME = "Switch_Home_Notify";
    
    private final static String TAG = DoubleCenterEventHandler.class.getName();

    public DoubleCenterEventHandler()
    {
    }

    @Override
    public boolean isSubscribed(String topic)
    {
        return EVENT_TYPE_PUSH_SWITCH_HOME.equals(topic);
    }

    @Override
    public void handle(String topic, XmlMessage msg)
    {
        if ("switch-home".equals(topic))
        {
            String event_type = msg.getEventType();

            if (EVENT_TYPE_PUSH_SWITCH_HOME.equals(event_type))
            {
                Element pJxActiveNetworkIdE = msg.getParameter("P_JXActive_Network_ID");
                if(pJxActiveNetworkIdE != null)
                {
                    //主中心地址
                    String pJxActiveNetworkId = pJxActiveNetworkIdE.getAttribute("value").value;
                    //记录日志
                    Log.info(TAG, "pJxActiveNetworkId:" + pJxActiveNetworkId);
                    if(pJxActiveNetworkId != null && !pJxActiveNetworkId.equals(""))
                    {
                        //TODO:设置主中心
//                        LineManager.getInstance().switchMaster(pJxActiveNetworkId);
                    }
                }
            }
            else
            {
                Log.error(TAG, "Not corresponding event-type: " + event_type);
            }
        }
    }

    @Override
    public void onTimeout(String event)
    {
    }

}
