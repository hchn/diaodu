package com.jiaxun.uil.handler;

import java.util.ArrayList;
import java.util.HashMap;

import com.jiaxun.sdk.scl.module.presence.callback.SclPresenceEventListener;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * ËµÃ÷£ºÓÃ»§×´Ì¬¶©ÔÄ¼àÌýÆ÷
 *
 * @author  hubin
 *
 * @Date 2015-5-13
 */
public class PresenceEventHandler implements SclPresenceEventListener
{
    private static final String TAG = PresenceEventHandler.class.getName();
    
    public PresenceEventHandler()
    {
    }

    @Override
    public void onSubscribeAck(String sessionId, int result)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPresenceUserStatus(ArrayList<HashMap<String, Integer>> presenceMap)
    {
        Log.info(TAG, "onPresenceUserStatus");
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_PRESENCE_USER_STATUS, presenceMap);
    }

}
