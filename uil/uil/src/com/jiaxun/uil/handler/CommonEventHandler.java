package com.jiaxun.uil.handler;

import com.jiaxun.sdk.scl.module.common.callbcak.SclCommonEventListener;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * ËµÃ÷£º
 *
 * @author  hubin
 *
 * @Date 2015-5-6
 */
public class CommonEventHandler implements SclCommonEventListener
{
    private static final String TAG = CommonEventHandler.class.getName();

    public CommonEventHandler()
    {
    }

    @Override
    public void onSclNightServiceAck(boolean enable, int result)
    {
        Log.info(TAG, "onSclNightServiceAck:enable:" + enable + " result:" + result);
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_NIGHT_SERVICE, enable, result);
    }

    @Override
    public void onSclLineStatusChange(int[] linkStatus, int[] serviceStatus)
    {
        Log.info(TAG, "onSclLineStatusChange");
//        Message message = uiEventHandler.obtainMessage();
//        message.what = CommonEventEntry.MESSAGE_NOTIFY_SERVICE_STATUS;
//        Bundle data = new Bundle();
//        // data.putIntArray(CommonEventEntry.DATA_STATUS, linkStatus);
//        data.putIntArray(CommonConstantEntry.DATA_STATUS, serviceStatus);
//        message.setData(data);
//        uiEventHandler.sendMessage(message);
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_SERVICE_STATUS, linkStatus, serviceStatus);
    }

}
