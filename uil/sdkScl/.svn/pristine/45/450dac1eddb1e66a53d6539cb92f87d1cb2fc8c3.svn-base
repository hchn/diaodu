package com.jiaxun.sdk.scl.module.common.handler;

import java.util.Arrays;

import android.os.Bundle;
import android.os.Message;

import com.jiaxun.sdk.acl.module.common.callback.AclCommonEventListener;
import com.jiaxun.sdk.scl.handler.SessionLooperHandler;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 系统服务公共方法类
 * 
 * @author fuluo
 * 
 */
public class SclCommonEventHandler implements AclCommonEventListener
{
    private final static String TAG = SclCommonEventHandler.class.getName();

    private SessionLooperHandler sclHandler;

    public SclCommonEventHandler()
    {
        sclHandler = SessionLooperHandler.getInstance();
    }

    @Override
    public void onNightServiceAck(boolean enable, int result)
    {
        Log.info(TAG, "onNightServiceAck: enable" + enable + "result: " + result);
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_NIGHT_SERVICE;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
        data.putBoolean(CommonConstantEntry.DATA_ENABLE, enable);
        data.putInt(CommonConstantEntry.DATA_RESULT, result);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onLineStatusChange(int[] linkStatus, int[] serviceStatus)
    {
        Log.info(TAG, "linkStatus: " + Arrays.toString(linkStatus) + "serviceStatus: " + Arrays.toString(serviceStatus));
        Message message = sclHandler.obtainMessage();
        message.what = CommonEventEntry.MESSAGE_NOTIFY_SERVICE_STATUS;
        message.arg1 = CommonEventEntry.MESSAGE_TYPE_COMMON;
        Bundle data = new Bundle();
//        data.putInt(CommonEventEntry.DATA_STATUS, nStatus);
//        data.putString(CommonEventEntry.DATA_REASON, szReason);
        data.putIntArray(CommonConstantEntry.DATA_LINK_STATUS, linkStatus);
        data.putIntArray(CommonConstantEntry.DATA_STATUS, serviceStatus);
        message.setData(data);
        sclHandler.sendMessage(message);
    }

    @Override
    public void onRecordIdNotify(String sessionId, int recordId)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLineSwitch(int activeLine)
    {
        // TODO Auto-generated method stub

    }

}
