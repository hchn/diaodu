package com.jiaxun.sdk.scl.session.vs;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.scl.media.video.JVideoLauncher;
import com.jiaxun.sdk.scl.model.CallModel;
import com.jiaxun.sdk.scl.model.VsModel;
import com.jiaxun.sdk.scl.session.Session;
import com.jiaxun.sdk.scl.session.vs.state.VsCloseState;
import com.jiaxun.sdk.scl.session.vs.state.VsOpenAckState;
import com.jiaxun.sdk.scl.session.vs.state.VsOpenState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：作为呼叫过程中的数据承载类
 *
 * @author  hubin
 *
 * @Date 2014-1-5
 */
public class VsSession extends Session implements VsStateHandler
{
    private static String TAG = VsSession.class.getName();
    public VsState closeState;
    public VsState openState;
    public VsState openAckState;

    public VsState currentState;
    private CallModel vsModel;
    private CallRecord vsRecord;

    private JVideoLauncher videoLauncher;

    public VsSession()
    {
        closeState = new VsCloseState(this);
        openState = new VsOpenState(this);
        openAckState = new VsOpenAckState(this);

        currentState = closeState;
    }

    public VsModel getVsModel()
    {
        return (VsModel) vsModel;
    }

    public void setVsModel(VsModel vsModel)
    {
        this.vsModel = vsModel;
    }

    public String toString()
    {
        if (vsModel != null)
        {
            return vsModel.toString();
        }
        return "";
    }

    public void handleException(Exception e)
    {
        Log.exception(TAG, e);
    }

    public int getSessionState()
    {
        if (currentState instanceof VsCloseState)
        {
            return CommonConstantEntry.VS_STATE_CLOSE;
        }
        else if (currentState instanceof VsOpenState)
        {
            return CommonConstantEntry.VS_STATE_OPEN;
        }
        else if (currentState instanceof VsOpenAckState)
        {
            return CommonConstantEntry.VS_STATE_OPEN_ACK;
        }
        return CommonConstantEntry.VS_STATE_CLOSE;
    }

    @Override
    public void recycle()
    {
        if (videoLauncher != null)
        {
            videoLauncher.halt();
        }
        super.recycle();
    }

    public JVideoLauncher getVideoLauncher()
    {
        return videoLauncher;
    }

    public void setVideoLauncher(JVideoLauncher videoLauncher)
    {
        this.videoLauncher = videoLauncher;
    }

    public CallRecord getVsRecord()
    {
        return vsRecord;
    }

    public void setVsRecord(CallRecord callRecord)
    {
        this.vsRecord = callRecord;
    }

    @Override
    public void onVsOpenAck(String sessionId, int priority)
    {
        currentState.onVsOpenAck(sessionId, priority);
    }

    @Override
    public void onVsMediaInfo(String sessionId, int localVideoPort, int remoteVideoPort, String remoteAddress)
    {
        currentState.onVsMediaInfo(sessionId, localVideoPort, remoteVideoPort, remoteAddress);
    }

    @Override
    public void openVs(String sessionId, String videoNum, int priority) throws Exception
    {
        currentState.openVs(sessionId, videoNum, priority);
    }

    @Override
    public void closeVs(String sessionId) throws Exception
    {
        currentState.closeVs(sessionId);
    }

    @Override
    public void onVsClosed(String sessionId, int reason)
    {
        currentState.onVsClosed(sessionId, reason);
    }

    @Override
    public void onRecordInfo(String sessionId, String taskId, String server)
    {
        currentState.onRecordInfo(sessionId, taskId, server);
    }
}
