package com.jiaxun.uil.handler;

import java.util.ArrayList;

import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.ConfModel;
import com.jiaxun.sdk.scl.model.SCallModel;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfEventListener;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfUserEventListener;
import com.jiaxun.sdk.scl.module.scall.callback.SclSCallEventListener;
import com.jiaxun.sdk.scl.session.SessionManager;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.model.ConfMemberItem;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：单呼事件通知回调处理
 *
 * @author  hubin
 *
 * @Date 2015-9-15
 */
public class CallEventHandler implements SclSCallEventListener, SclConfEventListener, SclConfUserEventListener
{
    private static final String TAG = CallEventHandler.class.getName();
    private static ArrayList<CallListItem> callList = new ArrayList<CallListItem>();
    private static ArrayList<ConfMemberItem> confMemberList = new ArrayList<ConfMemberItem>();

    @Override
    public void onSCallStatusChange(String sessionId, int status, final SCallModel scallModel, int reason)
    {
        Log.info(TAG, "onSCallStatusChange::");
        switch (status)
        {
        // 空闲状态，包括呼叫挂断，释放，拒绝等呼叫结束场景
            case CommonConstantEntry.SCALL_STATE_IDLE:
                if (scallModel == null)
                {
                    return;
                }
                Log.info(TAG, "SCALL_STATE_IDLE:: number：" + scallModel.getPeerNum() + " reason:" + reason);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CONTACT_MAKE_TURN_CALL, reason);
                if (reason == CommonConstantEntry.SIP_OFFLINE || reason == CommonConstantEntry.CALL_END_EXISTED || reason == CommonConstantEntry.CALL_END_DND)
                {
                    ToastUtil.showToast(scallModel.getPeerNum() + (scallModel.isVideo() ? "视频" : "语音") + (scallModel.isConfMember() ? "会议结束: " : "单呼结束: ")
                            + SdkUtil.parseReleaseReason(reason));
                }
                CallListItem callListItem = getExistedCall(scallModel.getSessionId());
                if (callListItem == null)
                {
                    return;
                }
                final SCallModel sCallModel = (SCallModel) callListItem.getCallModel();
                removeCallFromCallList(sCallModel.getSessionId());
                if (sCallModel.isVideo())
                {
                    UiUtils.removeRemoteVideo(sCallModel.getPeerNum());
                }

                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_SCALL_RELEASE);
                break;
            // 呼出状态
            case CommonConstantEntry.SCALL_STATE_PROCEEDING:
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_PROCEEDING");
                if (scallModel == null)
                {
                    return;
                }
                Log.info(TAG, "new session, add to call list. ");
                callListItem = new CallListItem();
                callListItem.setType(scallModel.isVideo() ? CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO : CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO);
                callListItem.setCallModel(scallModel);
                callListItem.setName(scallModel.getPeerName());
                callListItem.setContent(scallModel.getPeerNum());
                callListItem.setStatus(status);
                callList.add(callListItem);
                callListItem.setStatus(status);

                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_CHANGE);
                break;
            case CommonConstantEntry.SCALL_STATE_RINGBACK:
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_RINGBACK");
                break;
            case CommonConstantEntry.SCALL_STATE_CONNECT_ACK:
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_CONNECT_ACK");
                break;
            // 通话中状态
            case CommonConstantEntry.SCALL_STATE_CONNECT:

                if (scallModel == null)
                {
                    return;
                }
                // 已经通话成功，一键呼叫终止，
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CONTACT_MAKE_TURN_CALL, 999);
                callListItem = getExistedCall(scallModel.getSessionId());
                if (callListItem != null)
                {
                    if (callListItem.isIntoConf())// 如果是来呼入会操作，则先接通在入会
                    {
                        UiApplication.getConfService().confUserAdd(getConfSessionId(), callListItem.getContent());
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_TO_CONF, callListItem.getContent());
                        return;
                    }
                }

                int callPriority = scallModel.getPriority();
                boolean isEmergency = callPriority <= SessionManager.getInstance().getServiceConfig().emergencyCallPriority;
                if (isEmergency)
                {
                    ToastUtil.showToast(scallModel.getPeerNum() + (scallModel.isVideo() ? "视频" : "语音") + "：紧急电话");
                }
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_CONNECT");
                callListItem = getExistedCall(scallModel.getSessionId());
                if (callListItem == null)
                {
                    Log.info(TAG, "new session, add to call list. ");
                    callListItem = new CallListItem();
                    callListItem.setType(scallModel.isVideo() ? CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO : CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO);
                    callListItem.setCallModel(scallModel);
                    callListItem.setName(scallModel.getPeerName());
                    callListItem.setContent(scallModel.getPeerNum());
                    callListItem.setStatus(status);
                    callList.add(callListItem);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_CHANGE);
                }
                else
                {
                    callListItem.setStatus(status);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE, callListItem);
                }

                break;
            // 来呼振铃状态
            case CommonConstantEntry.SCALL_STATE_RINGING:
                if (scallModel == null)
                {
                    return;
                }
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_RINGING");
                callListItem = getExistedCall(scallModel.getSessionId());
                if (callListItem == null)
                {
//                    if (!(UiApplication.getHomeContext() instanceof HomeActivity))
//                    {
//                        ToastUtil.showToast("通知", scallModel.getPeerNum() + ":新呼叫！");
//                    }
                    Log.info(TAG, "new session, add to call list.");
                    callListItem = new CallListItem();
                    callListItem.setType(scallModel.isVideo() ? CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO : CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO);
                    callListItem.setCallModel(scallModel);
                    callListItem.setName(scallModel.getPeerName());
                    callListItem.setContent(scallModel.getPeerNum());
                    callListItem.setStatus(status);
                    callList.add(callListItem);
                }
                callListItem.setStatus(status);
                ToastUtil.showToast(scallModel.getPeerNum() + (scallModel.isVideo() ? "视频" : "语音") + (scallModel.isConfMember() ? "会议呼入" : "单呼呼入"));
                if (!UiApplication.getVsService().isLoopStarted())
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_CHANGE);
                }
                break;
            // 保持状态
            case CommonConstantEntry.SCALL_STATE_HOLD:
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_HOLD");
                callListItem = getExistedCall(sessionId);
                if (callListItem == null)
                {
                    return;
                }
                callListItem.setStatus(status);
                if (scallModel.isVideo())
                {
                    UiUtils.removeRemoteVideo(scallModel.getPeerNum());
                }
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE, callListItem);
                break;
            // 远端保持状态
            case CommonConstantEntry.SCALL_STATE_REMOTE_HOLD:
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_REMOTE_HOLD");
                callListItem = getExistedCall(sessionId);
                if (callListItem == null)
                {
                    return;
                }
                callListItem.setStatus(status);
                if (scallModel.isVideo())
                {
                    UiUtils.removeRemoteVideo(scallModel.getPeerNum());
                }
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE, callListItem);
                break;
            // 双向保持状态
            case CommonConstantEntry.SCALL_STATE_BOTH_HOLD:
                Log.info(TAG, "number：" + scallModel.getPeerNum() + "SCALL_STATE_BOTH_HOLD");
                callListItem = getExistedCall(sessionId);
                if (callListItem == null)
                {
                    return;
                }
                callListItem.setStatus(status);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE, callListItem);
                break;
            default:
                break;
        }

        if (UiApplication.getCurrentContext() instanceof HomeActivity)
        {
            ((HomeActivity) UiApplication.getCurrentContext()).refreshContactStatus(scallModel.getPeerNum(), status, true);
        }
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
    }

    @Override
    public void onSCallremoteVideoStarted(String sessionId, String number, SurfaceView remoteVideoView)
    {
        Log.info(TAG, "onSCallremoteVideoStarted:: number: " + number);
        CallListItem callListItem = getExistedCall(sessionId);
        // 如果是来呼入会操作，则先不处理视频
        if (callListItem != null && callListItem.isIntoConf())
        {
            return;
        }
        UiUtils.addRemoteVideo(CommonConstantEntry.SESSION_TYPE_SCALL, sessionId, number, remoteVideoView);
    }

    @Override
    public void onSCallremoteVideoStoped(String sessionId, String number)
    {
        Log.info(TAG, "onSCallremoteVideoStoped:: number: " + number);
        UiUtils.removeRemoteVideo(number);
    }

    @Override
    public void onSclCallAudioEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "onSclCallAudioEnable:: enable:" + enable);
        CallListItem callListItem = getExistedCall(sessionId);
        if (callListItem != null)
        {
            callListItem.setAudioEnabled(enable);
        }
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_SCALL_AUDIO_CHANGE, sessionId, enable);
    }

    public static ArrayList<CallListItem> getCallList()
    {
        return callList;
    }

    public static ArrayList<ConfMemberItem> getConfMemberItems()
    {
        return confMemberList;
    }

    public static String getConfSessionId()
    {
        String sessionId = null;
        for (CallListItem callItem : callList)
        {
            if (callItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {
                sessionId = callItem.getCallModel().getSessionId();
            }
        }
        return sessionId;
    }

    private CallListItem getExistedCall(String sessionId)
    {
        if (TextUtils.isEmpty(sessionId))
        {
            return null;
        }
        for (CallListItem callListItem : callList)
        {
            if (sessionId.equals(callListItem.getCallModel().getSessionId()))
            {
                return callListItem;
            }
        }
        return null;
    }

    private void removeCallFromCallList(String sessionId)
    {
        if (TextUtils.isEmpty(sessionId))
        {
            return;
        }
        for (CallListItem callListItem : callList)
        {
            if (sessionId.equals(callListItem.getCallModel().getSessionId()))
            {
                callList.remove(callListItem);
                Log.info(TAG, "session finished, remove from call list. callList.size:" + callList.size());
                return;
            }
        }
    }

    @Override
    public void onConfStatusChange(String sessionId, int status, ConfModel confModel, int reason)
    {
        Log.info(TAG, "onConfStatusChange:: sessionId:" + sessionId + " status:" + status);
        switch (status)
        {
            case CommonConstantEntry.CONF_STATE_CONNECT:
                Log.info(TAG, "CommonConstantEntry.CONF_STATE_CONNECT");
                if (confModel == null)
                {
                    return;
                }
                CallListItem confItem = getExistedCall(confModel.getSessionId());
                if (confItem != null)
                {
                    confItem.setStatus(status);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE, confItem);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_STATUS, true);
                    return;
                }
                confItem = new CallListItem();
                if (confModel.isVideo())
                {
                    confItem.setName("视频会议");
                    confItem.setType(CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO);
                }
                else
                {
                    confItem.setName("语音会议");
                    confItem.setType(CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO);
                }
                confItem.setContent(confModel.getChairmanNum());
                confItem.setType(CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO);
                confItem.setCallModel(confModel);
                confItem.setStatus(status);
                callList.add(confItem);
                confMemberList.clear();
                for (ConfMemModel confMemModel : confModel.getMemList())
                {
                    ConfMemberItem confMemberItem = new ConfMemberItem();
                    confMemberItem.setConfId(sessionId);
                    confMemberItem.setConfMemModel(confMemModel);
                    confMemberItem.setIconRes(R.drawable.usericon);
                    confMemberItem.setName(confMemModel.getName());
                    confMemberItem.setContent(confMemModel.getNumber());
                    confMemberList.add(confMemberItem);
                }
                if (callList.size() == 1)
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_OPEN_CONF_CONTROL, confModel, confItem.getStatus());
                }
                else
                {
                    // FIXME:目前只针对支持单个会议场景
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_CHANGE);
                }
                break;
            case CommonConstantEntry.CONF_STATE_EXIT:
                Log.info(TAG, "CommonConstantEntry.CONF_STATE_EXIT");
                if (confModel == null)
                {
                    return;
                }
                confItem = getExistedCall(confModel.getSessionId());
                if (confItem == null)
                {
                    return;
                }
                confItem.setStatus(CommonConstantEntry.CONF_STATE_EXIT);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE, confItem);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_STATUS, false);
                break;
            case CommonConstantEntry.CONF_STATE_EXIT_ACK:
                Log.info(TAG, "CommonConstantEntry.CONF_STATE_EXIT_ACK");
                break;
            case CommonConstantEntry.CONF_STATE_IDLE:
                Log.info(TAG, "CommonConstantEntry.CONF_STATE_IDLE" + " callList.size:" + callList.size());
                confItem = getExistedCall(confModel.getSessionId());
                if (confItem == null)
                {
                    return;
                }

                removeCallFromCallList(confModel.getSessionId());
                confMemberList.clear();
//                ToastUtil.showToast("会议结束");
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_RELEASE);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CLOSE_CONF_CONTROL);
                break;
            case CommonConstantEntry.CONF_STATE_PROCEEDING:
                Log.info(TAG, "CommonConstantEntry.CONF_STATE_PROCEEDING");
                break;
            case CommonConstantEntry.CONF_STATE_RETURN_ACK:
                Log.info(TAG, "CommonConstantEntry.CONF_STATE_RETURN_ACK");
                break;

            default:
                break;
        }
    }

    @Override
    public void onConfBgmEnable(String sessionId, boolean enable)
    {
        Log.info(TAG, "onConfBgmEnable:: sessionId:" + sessionId + " enable" + enable);
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_BGM, enable, sessionId);
    }

    @Override
    public void onConfUserStatusChange(final String sessionId, int status, final ConfMemModel info, int reason)
    {
        Log.info(TAG, "onConfUserStatusChange:: sessionId:" + sessionId + " status:" + status + " member:" + info.getNumber() + " reason:" + reason);
        if (getExistedCall(sessionId) == null)
        {
            return;
        }
        for (ConfMemberItem confMemberItem : confMemberList)
        {
            if (info.getNumber().equals(confMemberItem.getContent()))
            {
                confMemberItem.setConfMemModel(info);
                if (status == CommonConstantEntry.CONF_MEMBER_STATE_IDLE)
                {
                    // 如果追加成员失败时，启动追呼检查
                    if (reason == CommonConstantEntry.CONF_MEMBER_END_FAILED && UiApplication.getConfigService().isConfRecallEnabled())
                    {
                        confMemberItem.setRecallTimes(confMemberItem.getRecallTimes() + 1);
                        // 追呼此时小于配置次数时，开启追呼，否则结束追呼
                        if (confMemberItem.getRecallTimes() > UiApplication.getConfigService().getConfRecallTimes())
                        {
                            UiUtils.removeRemoteVideo(info.getNumber());
                            confMemberItem.getConfMemModel().setAudioEnabled(false);
                            ToastUtil.showToast("通知", info.getNumber() + SdkUtil.parseReleaseReason(reason));
                        }
                        else
                        {
                            Log.info(TAG, "conf member recall times: " + confMemberItem.getRecallTimes());
                            new Handler().postDelayed(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    UiApplication.getConfService().confUserAdd(sessionId, info.getNumber());
                                }
                            }, UiApplication.getConfigService().getConfRecallInterval() * 1000);
                        }
                    }
                    else
                    {
                        UiUtils.removeRemoteVideo(info.getNumber());
                        confMemberItem.getConfMemModel().setAudioEnabled(false);
                        ToastUtil.showToast("通知", info.getNumber() + SdkUtil.parseReleaseReason(reason));
                    }
                }
                else if (status == CommonConstantEntry.CONF_MEMBER_STATE_CONNECT)
                {
                    ToastUtil.showToast("通知", info.getNumber() + "成员入会");
                }
                else
                {
                    confMemberItem.getConfMemModel().setAudioEnabled(true);
                }
                confMemberItem.getConfMemModel().setVideoEnabled(false);
                confMemberItem.getConfMemModel().setVideoShared(false);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE, confMemberItem);
                break;
            }
        }
    }

    @Override
    public void onConfUserVideoStarted(String sessionId, String userNum, SurfaceView userVideoView)
    {
        Log.info(TAG, "onConfUserVideoStarted:: sessionId:" + sessionId + " userNum: " + userNum);
        if (getExistedCall(sessionId) == null)
        {
            return;
        }
        UiUtils.addRemoteVideo(CommonConstantEntry.SESSION_TYPE_CONF, sessionId, userNum, userVideoView);
        for (ConfMemberItem confMemberItem : confMemberList)
        {
            if (userNum.equals(confMemberItem.getName()))
            {
                confMemberItem.getConfMemModel().setVideoEnabled(true);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE, confMemberItem);
                break;
            }
        }
    }

    @Override
    public void onConfUserAudioChanged(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "onConfUserAudioChanged:: sessionId:" + sessionId + " userNum: " + userNum + " enable:" + enable);
        if (getExistedCall(sessionId) == null)
        {
            return;
        }
        for (ConfMemberItem confMemberItem : confMemberList)
        {
            if (userNum.equals(confMemberItem.getName()))
            {
                confMemberItem.getConfMemModel().setAudioEnabled(enable);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE, confMemberItem);
                break;
            }
        }
    }

    @Override
    public void onConfUserVideoChanged(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "onConfUserVideoChanged:: sessionId:" + sessionId + " userNum: " + userNum + " enable:" + enable);
        if (getExistedCall(sessionId) == null)
        {
            return;
        }
        UiUtils.removeRemoteVideo(userNum);
        for (ConfMemberItem confMemberItem : confMemberList)
        {
            if (userNum.equals(confMemberItem.getName()))
            {
                confMemberItem.getConfMemModel().setVideoEnabled(enable);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE, confMemberItem);
                break;
            }
        }
    }

    @Override
    public void onConfUserVideoShareChanged(String sessionId, String userNum, boolean enable)
    {
        Log.info(TAG, "onConfUserVideoShareChanged:: sessionId:" + sessionId + " userNum: " + userNum + " enable:" + enable);
        if (getExistedCall(sessionId) == null)
        {
            return;
        }
        for (ConfMemberItem confMemberItem : confMemberList)
        {
            if (userNum.equals(confMemberItem.getName()))
            {
                confMemberItem.getConfMemModel().setVideoShared(enable);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE, confMemberItem);
                break;
            }
        }
        // TODO Auto-generated method stub
    }

}
