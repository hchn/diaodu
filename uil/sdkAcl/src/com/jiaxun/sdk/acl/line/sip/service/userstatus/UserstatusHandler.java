package com.jiaxun.sdk.acl.line.sip.service.userstatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.acl.line.sip.ua.RegisterAgent;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;
import com.jiaxun.sdk.util.xml.userstatus.UsItem;

/**
 * 说明：用户状态订阅处理
 *
 * @author  fuluo
 *
 * @Date 2015-5-12
 */
public class UserstatusHandler
{
    private static String TAG = "UserstatusHandler";
    private RegisterAgent registerAgent;
    private SipAdapter mSipAdapter;

    public UserstatusHandler(RegisterAgent registerAgent, SipAdapter sipAdapter)
    {
        this.registerAgent = registerAgent;
        this.mSipAdapter = sipAdapter;
    }

    /**
     * 清空用户状态
     */
    public boolean clearUserstatus()
    {
        try
        {
            String xmlBody = XmlMessageFactory.createClearUserMsg();
            registerAgent.surbscriber(xmlBody);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 添加用户状态
     */
    public boolean addUserstatus(String[] users)
    {
        if (users == null || users.length == 0)
            return false;
        Log.info(TAG, "addUserstatus::users" + Arrays.toString(users));
        return sendAddOrDeleteUserMsg(users, true);
    }

    /**
     * 删除用户状态
     */
    public boolean deleteUserstatus(String[] users)
    {
        if (users == null || users.length == 0)
            return false;
        Log.info(TAG, "deleteUserstatus::users" + Arrays.toString(users));
        return sendAddOrDeleteUserMsg(users, false);
    }

    /**
     * 分包发送添加/删除用户消息
     */
    private boolean sendAddOrDeleteUserMsg(final String[] users, final boolean isAdd)
    {
        if (users == null || users.length == 0)
            return false;
        Log.info(TAG, "sendAddOrDeleteUserMsg::users" + Arrays.toString(users));

        try
        {
            if (users.length > 10)
            {// 分包发送
                int packet = 10;// 分包数量
                int lastPacket = 0;// 剩余包数量
                int sends = 0;// 已经发送用户数
                String[] temps = new String[packet];
                String xmlBody = null;
                while (sends < users.length)
                {// 分包
                    lastPacket = users.length - sends;
                    if (lastPacket > packet)
                    {
                        System.arraycopy(users, sends, temps, 0, packet);
                        xmlBody = XmlMessageFactory.createAddOrDeleteUserMsg(temps, isAdd);
                        registerAgent.surbscriber(xmlBody);
                        sends += packet;
                    }
                    else
                    {// 最后一个包
                        temps = new String[lastPacket];
                        System.arraycopy(users, sends, temps, 0, lastPacket);
                        xmlBody = XmlMessageFactory.createAddOrDeleteUserMsg(temps, isAdd);
                        registerAgent.surbscriber(xmlBody);
                        sends += lastPacket;
                    }
                }
            }
            else
            {
                String xmlBody = XmlMessageFactory.createAddOrDeleteUserMsg(users, isAdd);
                registerAgent.surbscriber(xmlBody);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 订阅通知消息
     */
    public void onUserstatusNotifyReceive(String body)
    {
        Log.info(TAG, "onUserstatusNotifyReceive::body:" + body);
        XmlMessage msg = XmlMessageFactory.parseUsXml(body);
        if (msg == null || msg.getEventType() == null && msg.getUsUserList() != null)
            return;
        if (mSipAdapter.getPresenceEventListener() != null && msg.getEventType().equals(XmlMessage.EVENT_TYPE_USER_STATE_NOTIFY))
        {
            List<UsItem> usItems = msg.getUsUserList().getUsItemList();
            ArrayList<HashMap<String, Integer>> presenceMaps = new ArrayList<HashMap<String, Integer>>();
            for (UsItem usItem : usItems)
            {
                HashMap<String, Integer> presenceMap = new HashMap<String, Integer>();
                if (UsItem.STATUS_ONLINE.equals(usItem.getStatus()))
                {
                    presenceMap.put(usItem.getId(), CommonConstantEntry.USER_STATUS_ONLINE);
                }
                else
                {
                    presenceMap.put(usItem.getId(), CommonConstantEntry.USER_STATUS_OFFLINE);
                }
                presenceMaps.add(presenceMap);
            }
            mSipAdapter.getPresenceEventListener().onPresenceUserStatus(presenceMaps);
        }
    }

}
