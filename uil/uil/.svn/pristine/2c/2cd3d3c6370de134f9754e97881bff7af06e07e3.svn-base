package com.jiaxun.uil.handler;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.module.callRecord.callback.DclCallRecordEventListener;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：通话记录事件回调
 *
 * @author  hubin
 *
 * @Date 2015-7-25
 */
public class CallRecordEventHandler implements DclCallRecordEventListener
{
    private static final String TAG = CallRecordEventHandler.class.getName();

    @Override
    public void onDclCallRecordAdd(CallRecord callRecord)
    {
        Log.info(TAG, "onSclCallRecordAdd::");
        // 判断是不是会议
        int callType = callRecord.getCallType();
        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {
            if (!(callRecord.isChairman()))
            {// 不是主席
            }
            else
            {// 是会议更新记录
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CALL_RECORD_ADD, callRecord);
            }
        }
        else
        {
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CALL_RECORD_ADD, callRecord);
        }
    }

    @Override
    public void onDclCallRecordClear()
    {
        Log.info(TAG, "onSclCallRecordClear::");
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CALL_RECORD_CLEAR);
    }

    @Override
    public void onDclCallRecordExport(int result)
    {
        Log.info(TAG, "onSclCallRecordExport::result:" + result);
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CALL_RECORD_EXPORT, result);
    }

}
