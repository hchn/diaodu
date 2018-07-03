package com.jiaxun.sdk.scl.session.conf.state;

import java.util.Calendar;

import android.os.SystemClock;
import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.conf.ConfState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：退会状态
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public class ConfExitState extends ConfState
{
    public ConfExitState(ConfSession confSession)
    {
        super(confSession);
        TAG = ConfExitState.class.getName();
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
    public void returnConf(String sessionId)
    {
        try
        {
            Log.info(TAG, "enterConf:: sessionId:" + sessionId);
//            if (SessionManager.getInstance().handleConnectedCalls(sessionId))//hz delete
//            {
//                SystemClock.sleep(500);
//            }
            confService.confEnter(sessionId);
            changeConfState(confSession.returnAckState);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }
    
    @Override
    public void addUser(String sessionId, String userNum)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "addUser:: userNum:" + userNum);
            confService.confUserAdd(sessionId, userNum);
            // 添加成员通话记录
            CallRecord memberRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0], SessionManager.getInstance().getAccountConfig().account[0]);
            memberRecord.setConfId(sessionId);
            memberRecord.setPeerName(confSession.getContactNameByNumber(userNum));
            memberRecord.setPeerNum(userNum);
            memberRecord.setChairman(false);
            memberRecord.setOutGoing(true);
            memberRecord.setStartTime(Calendar.getInstance().getTimeInMillis());
            memberRecord.setCallType(CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO);
            confSession.getMemberRecordsMap().put(userNum, memberRecord);

            // 重复成员不再添加
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    return;
                }
            }

            ConfMemModel confmember = new ConfMemModel();
            confmember.setNumber(userNum);
            confmember.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_IDLE);
            confSession.getConfModel().getMemList().add(confmember);
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
    public void onConfUserRing(String sessionId, String userNum)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserRing:: sessionId:" + sessionId);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_RING);
                    userCallback.onConfUserStatusChange(sessionId, member.getStatus(), member, CommonConstantEntry.Q850_NOREASON);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserAnswer(final String sessionId, final String userNum, final String mediaTag)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserAnswer:: userNum:" + userNum + " mediaTag:" + mediaTag);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_CONNECT);
                    confSession.getMemberRecordsMap().get(member.getNumber()).setConnectTime(Calendar.getInstance().getTimeInMillis());
                    userCallback.onConfUserStatusChange(sessionId, member.getStatus(), member, CommonConstantEntry.Q850_NOREASON);
                    member.setMediaTag(mediaTag);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onConfUserRelease(String sessionId, String userNum, int reason)
    {
        try
        {
            if (TextUtils.isEmpty(userNum))
            {
                return;
            }
            Log.info(TAG, "onConfUserRelease:: userNum:" + userNum);
            for (ConfMemModel member : confSession.getConfModel().getMemList())
            {
                if (userNum.equals(member.getNumber()))
                {
                    member.setStatus(CommonConstantEntry.CONF_MEMBER_STATE_IDLE);
                    confSession.getMemberRecordsMap().get(userNum).setReleaseTime(Calendar.getInstance().getTimeInMillis());
                    if (!TextUtils.isEmpty(confSession.getConfRecord().getRecordTaskId()))
                    {
                        confSession.getMemberRecordsMap().get(userNum).setRecordTaskId(confSession.getConfRecord().getRecordTaskId());
                        confSession.getMemberRecordsMap().get(userNum).setRecordServer(confSession.getConfRecord().getRecordServer());
                    }
//                    confCallback.onSclCallRecordReport(confSession.getMemberRecordsMap().get(userNum));
                    confSession.handleCallRecord(confSession.getMemberRecordsMap().get(userNum));
                    confSession.getMemberRecordsMap().remove(userNum);
                    userCallback.onConfUserStatusChange(sessionId, member.getStatus(), member, reason);
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

}
