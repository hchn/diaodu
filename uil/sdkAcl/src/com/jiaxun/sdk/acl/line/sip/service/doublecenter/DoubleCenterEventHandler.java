package com.jiaxun.sdk.acl.line.sip.service.doublecenter;

import com.jiaxun.sdk.acl.line.sip.event.SubscribeEventListener;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.Element;
import com.jiaxun.sdk.util.xml.XmlMessage;

/**
 * ˵����˫�����л�֪ͨ
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
                    //�����ĵ�ַ
                    String pJxActiveNetworkId = pJxActiveNetworkIdE.getAttribute("value").value;
                    //��¼��־
                    Log.info(TAG, "pJxActiveNetworkId:" + pJxActiveNetworkId);
                    if(pJxActiveNetworkId != null && !pJxActiveNetworkId.equals(""))
                    {
                        //TODO:����������
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
