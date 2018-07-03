package com.jiaxun.sdk.acl.line.sip.util;

import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * poc-sdk心跳保活
 * 
 * @author fuluo
 */
public class KeepAliveSdk extends Thread
{
    //保活运行控制
    private boolean running = true;
    
    private SipAdapter sipEngine;
    
    public KeepAliveSdk(SipAdapter sipEngine)
    {
        this.sipEngine = sipEngine;
    }

    @Override
    public void run()
    {
        this.running = true;//开始保活
        try
        {
            while(running)
            {
                Thread.sleep(CommonConfigEntry.HEARTBEAT_SDK_TIME);
                //TODO 通知接口：通知“心跳”
//                ServiceNotify.fireHeartbeatEvent();
            }
        }
        catch (Exception e)
        {
            Log.exception("KeepAliveSdk", e);
        }
    }
    
    public void halt()
    {
        running = false;
    }

}
