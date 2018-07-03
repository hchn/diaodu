package com.jiaxun.sdk.scl.session.vs;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.timer.Timer;
import com.jiaxun.sdk.util.timer.TimerListener;

/**
 * 说明：单呼状态机功能接口与通知接口
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class VsState implements TimerListener, VsStateHandler
{
    public String TAG;
    protected Timer timer;
    protected VsSession vsSession;
    protected int timeout = -1;

    public VsState(VsSession vsSession)
    {
        this.vsSession = vsSession;
    }

    public void startTimer()
    {
        if (timeout >= 0)
        {
            Log.info(TAG, "startTimer:: timeout:" + timeout);
            timer = new Timer(timeout, this);
            timer.start();
        }
    }

    public void haltTimer()
    {
        if (timer != null)
        {
            Log.info(TAG, "haltTimer:: timeout:" + timeout);
            timer.halt();
        }
    }

    public void changeVsState(VsState state)
    {
        if (vsSession != null)
        {
            vsSession.currentState.haltTimer();
            vsSession.currentState = state;
            vsSession.currentState.startTimer();
            vsSession.setStatus(vsSession.getSessionState());
            callback.onVsStatusChange(vsSession.getSessionId(), vsSession.getStatus(), vsSession.getVsModel(), CommonConstantEntry.Q850_NOREASON);
        }
    }
    public void changeVsState(VsState state, int reason)
    {
        if (vsSession != null)
        {
            vsSession.currentState.haltTimer();
            vsSession.currentState = state;
            vsSession.currentState.startTimer();
            vsSession.setStatus(vsSession.getSessionState());
            callback.onVsStatusChange(vsSession.getSessionId(), vsSession.getStatus(), vsSession.getVsModel(), reason);
        }
    }

    @Override
    public void onTimeout(Timer t)
    {
        try
        {
            if (vsSession != null)
            {
                closeVs(vsSession.getSessionId());
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }
    
    @Override
    public void openVs(String sessionId, String videoNum, int priority) throws Exception
    {
    }
    
    @Override
    public void closeVs(String sessionId) throws Exception
    {
        vsSession.getVsRecord().setReleaseReason(CommonConstantEntry.CALL_END_CALLER_RELEASE);
    }

    @Override
    public void onVsOpenAck(String sessionId, int priority)
    {
    }

    @Override
    public void onVsMediaInfo(String sessionId, int localVideoPort, int remoteVideoPort, String remoteAddress)
    {
    }

    @Override
    public void onVsClosed(String sessionId, int reason)
    {
        vsSession.getVsRecord().setReleaseReason(CommonConstantEntry.CALL_END_PEER_RELEASE);
    }

    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        vsSession.getVsRecord().setRecordTaskId(taskId);
        vsSession.getVsRecord().setRecordServer(server);
    }
    
}
