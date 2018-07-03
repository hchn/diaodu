package com.jiaxun.sdk.scl.session.conf.state;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.SystemClock;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.ConfModel;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.conf.ConfState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：会议空闲状态
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public class ConfIdleState extends ConfState
{
    public ConfIdleState(ConfSession confSession)
    {
        super(confSession);
        TAG = ConfIdleState.class.getName();
    }

    @Override
    public void createConf(String sessionId, String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList)
    {
        try
        {
            Log.info(TAG, "createConf:: sessionId:" + sessionId);
//            if (SessionManager.getInstance().handleConnectedCalls(sessionId))//hz delete
//            {
//                SystemClock.sleep(500);
//            }
            long time = Calendar.getInstance().getTimeInMillis();
            // 初始化会议对象
            ConfModel confModel = new ConfModel();
            confModel.setSessionId(sessionId);
            confModel.setConfName(confName);
            confModel.setPriority(callPriority);
            confModel.setMemList(memberList);
            confModel.setVideo(video);
            confModel.setStartTime(time);
            confModel.setChairmanName(SessionManager.getInstance().getAccountConfig().account[0]);
            confModel.setChairmanNum(SessionManager.getInstance().getAccountConfig().account[0]);
            confSession.setConfModel(confModel);

            // 初始化记录对象
            CallRecord confRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0], SessionManager.getInstance().getAccountConfig().account[0]);
            confRecord.setOutGoing(true);
            confRecord.setCallPriority(callPriority);
            confRecord.setCallType(video ? CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO : CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO);
            confRecord.setStartTime(time);
            confRecord.setChairman(true);
            confRecord.setConfId(sessionId);
            confRecord.setConfName(confName);
            confSession.setConfRecord(confRecord);

            changeConfState(confSession.proceedingState);
            confService.confCreate(sessionId, callPriority, confType, video);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }
}
