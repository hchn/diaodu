package com.jiaxun.sdk.scl.session.vs.state;

import java.util.Calendar;

import com.jiaxun.sdk.scl.session.vs.VsSession;
import com.jiaxun.sdk.scl.session.vs.VsState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：监控开启得到证实状态
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class VsOpenAckState extends VsState
{
    public VsOpenAckState(VsSession vsSession)
    {
        super(vsSession);
        TAG = VsOpenAckState.class.getName();
        timeout = CommonConstantEntry.VS_TIMEOUT_OPEN_ACK;
    }

    @Override
    public void onVsOpenAck(String sessionId, int priority)
    {
        Log.info(TAG, "onVsOpenAck:: sessionId:" + sessionId);
        vsSession.getVsModel().setConnectTime(Calendar.getInstance().getTimeInMillis());
        vsSession.getVsRecord().setConnectTime(Calendar.getInstance().getTimeInMillis());
        vsSession.getVsModel().setPriority(priority);
        vsSession.getVsRecord().setCallPriority(priority);
        changeVsState(vsSession.openState);
    }

    @Override
    public void closeVs(String sessionId) throws Exception
    {
        try
        {
            super.closeVs(sessionId);
            Log.info(TAG, "closeVs:: sessionId:" + sessionId);
            vsService.vsClose(sessionId);
            changeVsState(vsSession.closeState, CommonConstantEntry.CALL_END_CALL_CANCEL);
            vsSession.getVsRecord().setReleaseReason(CommonConstantEntry.CALL_END_CALL_CANCEL);
            vsSession.getVsRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//            callback.onSclCallRecordReport(vsSession.getVsRecord());
            vsSession.handleCallRecord(vsSession.getVsRecord());
            vsSession.recycle();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    @Override
    public void onVsClosed(String sessionId, int reason)
    {
        super.onVsClosed(sessionId, reason);
        Log.info(TAG, "onVsClosed:: sessionId:" + sessionId + " reason: " + reason);
        if (reason <= 0)
        {
            reason = CommonConstantEntry.CALL_END_SPACE_OR_USER_NO_ONLINE;
        }
        changeVsState(vsSession.closeState, reason);
        vsSession.getVsRecord().setReleaseReason(reason);
        vsSession.getVsRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//        callback.onSclCallRecordReport(vsSession.getVsRecord());
        vsSession.handleCallRecord(vsSession.getVsRecord());
        vsSession.recycle();
    }

}
