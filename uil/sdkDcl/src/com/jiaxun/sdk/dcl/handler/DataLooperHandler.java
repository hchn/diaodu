package com.jiaxun.sdk.dcl.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.module.callRecord.impl.DclCallRecordServiceImpl;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：SCL层数据集中处理，使用消息队列异步单线程处理，保证业务处理线程安全
 * 
 *  //hz add ：联系人相关 数据库操作的处理
 *
 * @author  hubin
 *
 * @Date 2015-1-7
 */
public class DataLooperHandler extends Handler
{

    private static final String TAG = DataLooperHandler.class.getName();
    private static DataLooperHandler instance;

    private DataLooperHandler()
    {
        super();
    }

    private DataLooperHandler(Looper looper)
    {
        super(looper);
    }

    public static DataLooperHandler getInstance()
    {
        if (instance == null)
        {
            HandlerThread dataThread = new HandlerThread("DataLooperHandler", Process.THREAD_PRIORITY_BACKGROUND);
            dataThread.start();
            instance = new DataLooperHandler(dataThread.getLooper());
        }
        return instance;
    }

    public void handleMessage(Message msg)
    {
        Bundle data = msg.getData();
        switch (msg.what)
        {
            case CommonEventEntry.MESSAGE_TYPE_CALL_RECORD:
                Log.info(TAG, "MESSAGE_TYPE_CALL_RECORD");
                CallRecord callRecord = (CallRecord) data.getParcelable(CommonConstantEntry.DATA_OBJECT);
                DclCallRecordServiceImpl.getInstance().insertCallRecord(callRecord);
                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }
}
