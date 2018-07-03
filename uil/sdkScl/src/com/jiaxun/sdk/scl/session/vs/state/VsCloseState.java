package com.jiaxun.sdk.scl.session.vs.state;

import java.util.Calendar;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.scl.model.VsModel;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.vs.VsSession;
import com.jiaxun.sdk.scl.session.vs.VsState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：监控关闭状态
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class VsCloseState extends VsState
{
    public VsCloseState(VsSession vsSession)
    {
        super(vsSession);
        TAG = VsCloseState.class.getName();
    }

    @Override
    public void openVs(String sessionId, String videoNum, int priority) throws Exception
    {
        try
        {
            Log.info(TAG, "openVs:: videoDeviceUrl:" + videoNum + " priority:" + priority);
            // 初始化会议对象
            VsModel vsModel = new VsModel();
            vsModel.setSessionId(sessionId);
            vsModel.setPriority(priority);
            vsModel.setVideoNum(videoNum);
            vsSession.setVsModel(vsModel);

            // 初始化记录对象
            CallRecord vsRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0], SessionManager.getInstance().getAccountConfig().account[0]);
            vsRecord.setOutGoing(true);
            vsRecord.setCallPriority(priority);
            vsRecord.setCallType(CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE);
            vsRecord.setPeerNum(videoNum);
            vsRecord.setPeerName(vsSession.getContactNameByNumber(videoNum));
            vsRecord.setStartTime(Calendar.getInstance().getTimeInMillis());
            Log.info(TAG, "setStartTime::" + vsRecord.getStartTime());
            vsSession.setVsRecord(vsRecord);

            changeVsState(vsSession.openAckState);
            vsService.vsOpen(sessionId, priority, videoNum);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

}
