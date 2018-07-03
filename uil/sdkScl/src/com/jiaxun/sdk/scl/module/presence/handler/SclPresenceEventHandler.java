package com.jiaxun.sdk.scl.module.presence.handler;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;

import com.jiaxun.sdk.acl.module.presence.callback.AclPresenceEventListener;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：
 *
 * @author  hubin
 *
 * @Date 2015-1-14
 */
public class SclPresenceEventHandler implements AclPresenceEventListener
{
    private static final String TAG = SclPresenceEventHandler.class.getName();
    private SessionLooperHandler sclHandler = SessionLooperHandler.getInstance();
    @Override
    public void onSubscribeAck(String sessionId, int result)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onPresenceUserStatus(ArrayList<HashMap<String, Integer>> presenceMap)
    {
        Log.info(TAG, "onPresenceUserStatus::");
        if (presenceMap == null || presenceMap.isEmpty())
        {
            return;
        }
        // 消息队列异步处理通知
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_PRESENCE_USER_STATUS;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_PRESENCE;
        message.obj = presenceMap;
        sclHandler.sendMessage(message);
    }

}
