package com.jiaxun.sdk.scl.session.scall.state;

import java.util.Calendar;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.util.DclUtils;
import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.model.SCallModel;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.scall.SCallState;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：单呼空闲状态
 *
 * @author  hubin
 *
 * @Date 2014-10-23
 */
public class SCallIdleState extends SCallState
{
    public SCallIdleState(SCallSession callSession)
    {
        super(callSession);
        TAG = SCallIdleState.class.getName();
    }

    @Override
    public void makeCall(String sessionId, String callNum, String callName, int priority, boolean video, int channel) throws Exception
    {
        try
        {
            Log.info(TAG, "makeCall:: callNum:" + callNum + " priority:" + priority);
//            if (SessionManager.getInstance().handleConnectedCalls(sessionId))//hz delete
//            {
//                SystemClock.sleep(500);
//            }
            // 初始化通话对象
            SCallModel newCall = new SCallModel();
            newCall.setSessionId(sessionId);
            newCall.setPeerNum(callNum);
            newCall.setPeerName(callName);
            newCall.setPriority(priority);
            newCall.setConfMember(false);
            newCall.setVideo(video);
            newCall.setStartTime(Calendar.getInstance().getTimeInMillis());
            Log.info(TAG, "setStartTime::" + newCall.getStartTime());
            newCall.setStarter(true);
            callSession.setCallModel(newCall);
            // 初始化记录对象
            CallRecord callRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0],
                    SessionManager.getInstance().getAccountConfig().account[0]);
            callRecord.setOutGoing(true);
            callRecord.setPeerNum(callNum);
            callRecord.setPeerName(callName);
            callRecord.setCallPriority(priority);
            callRecord.setCallType(video ? CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO : CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO);
            callRecord.setStartTime(Calendar.getInstance().getTimeInMillis());
            Log.info(TAG, "setStartTime::" + callRecord.getStartTime());
            callSession.setCallRecord(callRecord);

            changeCallState(callSession.dialState);
            callSession.setChannel(channel);
            sCallService.sCallMake(sessionId, priority, null, null, null, callNum, channel, true, video);
        }
        catch (Exception e)
        {
            callSession.handleException(e);
        }
    }

    @Override
    public void onSCallIncoming(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel,
            boolean isConf, boolean video)
    {
        try
        {
            Log.info(TAG, "onSCallIncoming:: callerNum:" + callerNum + " callPriority:" + callPriority + " video:" + video);
            // 黑白名单验证
            if (!DclUtils.verifyBlackList(callerNum))
            {
                sCallService.sCallRelease(sessionId, CommonConstantEntry.Q850_NOREASON);
                callSession.recycle();
                return;
            }
            // 支持无主叫号码呼叫
            if (callerNum.startsWith("anonymous"))
            {
                callerNum = "未知用户";
            }
            // 初始化通话对象
            SCallModel newCall = new SCallModel();
            newCall.setSessionId(sessionId);
            newCall.setPeerNum(callerNum);
            newCall.setPriority(callPriority);
            newCall.setPeerName(callSession.getContactNameByNumber(callerNum));
            newCall.setConfMember(isConf);
            newCall.setVideo(video);
            newCall.setStartTime(Calendar.getInstance().getTimeInMillis());
            newCall.setStarter(false);
            callSession.setCallModel(newCall);

            // 初始化记录对象
            CallRecord callRecord = new CallRecord(SessionManager.getInstance().getAccountConfig().account[0],
                    SessionManager.getInstance().getAccountConfig().account[0]);
            callRecord.setOutGoing(false);
            callRecord.setPeerNum(callerNum);
            callRecord.setPeerName(newCall.getPeerName());

            callRecord.setCallPriority(callPriority);
            callRecord.setCallType(video ? CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO : CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO);
            callRecord.setStartTime(Calendar.getInstance().getTimeInMillis());
            callSession.setCallRecord(callRecord);

            if (SessionManager.getInstance().getServiceConfig().isDndEnable)
            {
                Log.info(TAG, "playIncomingCallRing::isDndEnabled");
                sCallService.sCallRelease(sessionId, CommonConstantEntry.SIP_CALL_DND);
                callSession.getCallRecord().setReleaseReason(CommonConstantEntry.CALL_END_DND);
                callSession.getCallRecord().setReleaseTime(Calendar.getInstance().getTimeInMillis());
//                callback.onSclCallRecordReport(callSession.getCallRecord());
                callSession.handleCallRecord(callSession.getCallRecord());
                callSession.recycle();
                return;
            }

            // 获取开启自动应答设置
            Log.info(TAG, "autoAnswer: " + SessionManager.getInstance().getServiceConfig().autoAnswer);
            callSession.setChannel(channel);

            boolean isEmergency = callPriority <= SessionManager.getInstance().getServiceConfig().emergencyCallPriority;
            Log.info(TAG, "isEmergency: " + isEmergency);
            // 已有通话中会话，播放通话中来呼提示音，进入振铃状态
            if (SessionManager.getInstance().getActiveSessions().size() > 1)
            {
                changeCallState(callSession.ringingState);
                sCallService.sCallAlerting(sessionId, callerNum, channel, false);
                SessionManager.getInstance().updateSessionState(sessionId);
                // 不在闭铃状态需要声音提示
                if (!SessionManager.getInstance().isCloseRing())
                {
                    if (SessionManager.getInstance().hasIncallSession)
                    {
                        VoiceUtil.playIncallRing(channel, isEmergency);
                    }
                    else if (SessionManager.getInstance().hasRingbackSession)
                    {
                        // TODO
                    }
                    else if (SessionManager.getInstance().hasRingingSession)
                    {
                        // TODO
                    }
                    else if (SessionManager.getInstance().hasHoldSession)
                    {
                        // TODO
                    }
                    else if (SessionManager.getInstance().hasRemoteHoldSession)
                    {
                        // TODO
                    }
                    else if (SessionManager.getInstance().hasVsSession)
                    {
                        VoiceUtil.playIncomingCallRing(channel, isEmergency);
                    }
                }
            }
            // 已有通话中会话，播放通话中来呼提示音，进入振铃状态
            else if (SessionManager.getInstance().getServiceConfig().autoAnswer || isEmergency)
            {
                callSession.getCallModel().setConnectTime(callSession.getCallModel().getStartTime());
                callSession.getCallRecord().setConnectTime(callSession.getCallRecord().getStartTime());
                changeCallState(callSession.connectState);
                sCallService.sCallAnswer(sessionId, callerNum, channel, true, video);
                // 不在闭铃状态需要声音提示
                if (!SessionManager.getInstance().isCloseRing())
                {
                    VoiceUtil.playAutoAnswer(channel, isConf, isEmergency);
                }
            }
            else
            {
                changeCallState(callSession.ringingState);
                sCallService.sCallAlerting(sessionId, callerNum, channel, false);
                // 不在闭铃状态需要声音提示
                if (!SessionManager.getInstance().isCloseRing())
                {
                    VoiceUtil.playIncomingCallRing(channel, isEmergency);
                }
            }
        }
        catch (Exception e)
        {
            callSession.handleException(e);
        }

    }
}
