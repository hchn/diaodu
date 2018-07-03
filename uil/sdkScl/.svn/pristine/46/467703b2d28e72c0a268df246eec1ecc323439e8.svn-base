package com.jiaxun.sdk.scl.session.conf.state;

import java.util.Calendar;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.module.DclServiceFactory;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.conf.ConfState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;

/**
 * 说明：发出开会请求，等待确认
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public class ConfProceedingState extends ConfState
{
    public ConfProceedingState(ConfSession confSession)
    {
        super(confSession);
        TAG = ConfProceedingState.class.getName();
        timeout = CommonConstantEntry.CONF_TIMEOUT_PROCEEDING;
    }

    @Override
    public void closeConf(String sessionId)
    {
        try
        {
            Log.info(TAG, "closeConf:: sessionId:" + sessionId);
            super.closeConf(sessionId);
            confService.confClose(sessionId);
            changeConfState(confSession.idleState);
            confSession.getConfRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//            confCallback.onSclCallRecordReport(confSession.getConfRecord());
            confSession.handleCallRecord(confSession.getConfRecord());
            confSession.recycle();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfCreateConnect(String sessionId, int priority)
    {
        try
        {
            Log.info(TAG, "onConfCreateConnect:: sessionId:" + sessionId + "member size: " + confSession.getConfModel().getMemList());
            long time = Calendar.getInstance().getTimeInMillis();
            confSession.getConfModel().setPriority(priority);
            confSession.getConfRecord().setCallPriority(priority);
            confSession.getConfModel().setConnectTime(time);
            confSession.getConfRecord().setConnectTime(time);
            changeConfState(confSession.connectState);
            // 添加会议成员
            for (ConfMemModel confMember : confSession.getConfModel().getMemList())
            {
                confService.confUserAdd(confSession.getSessionId(), confMember.getNumber());
                // 添加成员通话记录
                CallRecord memberRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0], SessionManager.getInstance()
                        .getAccountConfig().account[0]);
                memberRecord.setConfId(sessionId);
                memberRecord.setPeerNum(confMember.getNumber());
                memberRecord.setPeerName(confSession.getContactNameByNumber(confMember.getNumber()));

                memberRecord.setChairman(false);
                memberRecord.setOutGoing(true);
                memberRecord.setStartTime(Calendar.getInstance().getTimeInMillis());
                memberRecord.setCallType(CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO);
                confSession.getMemberRecordsMap().put(confMember.getNumber(), memberRecord);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfClose(String sessionId)
    {
        Log.info(TAG, "closeConf:: sessionId:" + sessionId);
        super.onConfClose(sessionId);
        changeConfState(confSession.idleState);
        confSession.getConfRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        confCallback.onSclCallRecordReport(confSession.getConfRecord());
        confSession.handleCallRecord(confSession.getConfRecord());
        confSession.recycle();
    }

    @Override
    public void onTimeout(Timer t)
    {
        Log.info(TAG, "onTimeout:: sessionId:" + confSession.getSessionId());
        changeConfState(confSession.idleState);
    }
}
