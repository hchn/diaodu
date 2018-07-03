package com.jiaxun.uil.util;

import java.util.ArrayList;

import org.zoolu.net.IpAddress;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.util.CharacterParser;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.ConfModel;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.model.SCallModel;
import com.jiaxun.sdk.scl.model.ServiceConfig;
import com.jiaxun.sdk.scl.model.VsModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.version.VersionClient;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.handler.CallEventHandler;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.model.ConfMemberItem;
import com.jiaxun.uil.model.VsListItem;
import com.jiaxun.uil.module.config.itf.ConfigService;
import com.jiaxun.uil.ui.fragment.CallRecordListFragment;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：服务业务工具类
 *
 * @author  chaimb
 *
 * @Date 2015-7-15
 */
public class ServiceUtils
{
    private static final String TAG = ServiceUtils.class.getName();

    public static final int DEFAULT_CALL_TYPE = 0;
    /**
     * 单呼入会 ，直接入会
     */
    public static final int CALL2CONF1 = 1;

    /**
     * 单呼入会。 手动拉入会议
     */
    public static final int CALL2CONF2 = 2;

    private static DispatchQueue serviceQueue = new DispatchQueue("serviceQueue");

    private static boolean isUpDate = false;

    public static String serverVersionName = "";

    private static DispatchQueue getServiceQueue()
    {
        if (serviceQueue == null)
        {
            serviceQueue = new DispatchQueue("serviceQueue");
        }
        return serviceQueue;
    }

    /**
     * 方法说明 : 更新媒体配置信息
     * @return void
     * @author hubin
     * @Date 2015-6-24
     */
    public static void updateMediaConfig()
    {
        Log.info(TAG, "updateMediaConfig::");
        ConfigService configService = UiApplication.getConfigService();
        MediaConfig mediaConfig = new MediaConfig();
        mediaConfig.audioPort = configService.getAudioLocalPort();
        mediaConfig.audioMaxPort = configService.getAudioMaxLocalPort();
        mediaConfig.audioCodecs = new int[] {};
        mediaConfig.audioPacketTime = new int[] {};
        mediaConfig.videoPort = configService.getVideoLocalPort();
        mediaConfig.videoMaxPort = configService.getVideoMaxLocalPort();
        mediaConfig.videoWidth = configService.getVideoWidth();
        mediaConfig.videoHeight = configService.getVideoHeight();
        mediaConfig.videoFrameRate = configService.getVideoFrameRate();
        mediaConfig.videoBitRate = configService.getVideoBitRate();
        mediaConfig.videoCodecs = new int[] {};
        mediaConfig.dtmfMode = configService.getDtmfMode();
        mediaConfig.incomingCallVoice = configService.getIncomingCallVoice();
        UiApplication.getCommonService().updateMediaConfig(mediaConfig);
    }

    /**
     * 更新系统配置
     */
    public static void updateServiceConfig()
    {
        Log.info(TAG, "updateServiceConfig::");
        ConfigService configService = UiApplication.getConfigService();
        ServiceConfig serviceConfig = new ServiceConfig();
        if (UiApplication.getAtdService().getLoginedAttendant() != null)
        {
            serviceConfig.atd = UiApplication.getAtdService().getLoginedAttendant().getLogin();
        }
        serviceConfig.emergencyCallPriority = configService.getEmergencyCallPriority();
        serviceConfig.autoAnswer = configService.isAutoAnswer();
//        serviceConfig.isCloseRingEnable = configService.isCloseRingEnabled();
        serviceConfig.isDndEnable = configService.isDndEnabled();
        serviceConfig.isEmergencyEnable = configService.isEmergencyEnabled();
        serviceConfig.isNightServiceEnable = configService.isNightService();
        serviceConfig.isVideoCall = configService.isVideoCallEnabled();
        serviceConfig.isAudioRecord = configService.isAudioRecordEnabled();
        serviceConfig.emergencyCallPriority = configService.getEmergencyCallPriority();
        serviceConfig.callPriority = configService.getCallPriority();

        UiApplication.getCommonService().updateServiceConfig(serviceConfig);
    }

    /**
     * 启动服务
     */
    public static void startSdkService()
    {
        Log.info(TAG, "startSdkService::");
        AccountConfig accountConfig = initAccountConfig();
        if (UiApplication.isCallServerOnline)
        {// 在线
            UiApplication.getCommonService().stopSclService();
        }
        UiApplication.getCommonService().startSclService(accountConfig);
    }

    /**
     * 初始化帐户配置
     */
    private static AccountConfig initAccountConfig()
    {
        Log.info(TAG, "initAccountConfig::");
        ConfigService configService = UiApplication.getConfigService();
        AccountConfig accountConfig = new AccountConfig();

        // TODO:本地IP地址获取方法
        accountConfig.localIp = IpAddress.getLocalIpAddress();
//        accountConfig.localIp = "192.168.60.90";
        accountConfig.localPort = new int[] { configService.getMasterLocalPort(), configService.getSlaveLocalPort() };
        accountConfig.serverType = new int[] { configService.getMasterServerType(), configService.getSlaveServerType() };
        accountConfig.serverIp = new String[] { configService.getMasterServerIp(), configService.getSlaveServerIp() };
        accountConfig.serverDomain = new String[] { "", "" };
        accountConfig.serverPort = new int[] { configService.getMasterServerPort(), configService.getSlaveServerPort() };
        accountConfig.account = new String[] { configService.getAccountNumber(), configService.getAccountNumber() };
        accountConfig.password = new String[] { configService.getAccountPassword(), configService.getAccountPassword() };
        return accountConfig;
    }

    /**
     * 更新配置
     */
    public static void updateAccountConfig()
    {
        AccountConfig accountConfig = initAccountConfig();
        UiApplication.getCommonService().updateAcountConfig(accountConfig);
    }

//    /**
//     * 方法说明 :发起一个单呼
//     * @param contactId
//     * @return
//     * @author HeZhen
//     * @Date 2015-4-24
//     */
//    public static void makeCall(Context context, ContactModel ContactModel)
//    {
//        String calleeNum = ContactModel.getPhoneNumList().get(0).getNumber();
//        makeCall(context, calleeNum);
//    }

    /**
     * 方法说明 : 发起一个单呼
     * @param calleeNum
     * @return boolean
     * @author hubin
     * @Date 2015-6-18
     */
    public static void makeCall(Context context, final String calleeNum)
    {
        Log.info(TAG, "makeCall:: calleeNum:" + calleeNum);
        if (!UiApplication.isCallServerOnline)
        {
            ToastUtil.showToast("离线状态，无法发起业务");
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CONTACT_MAKE_TURN_CALL, CommonConstantEntry.CALL_END_OFFLINE);
            return;
        }
        if (TextUtils.isEmpty(calleeNum))
        {
            ToastUtil.showToast("号码不能为空");
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CONTACT_MAKE_TURN_CALL, -100);
            return;
        }
        else if (calleeNum.equals(UiApplication.getConfigService().getAccountNumber()))
        {
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.CONTACT_MAKE_TURN_CALL, -100);
            ToastUtil.showToast("拒绝呼出本机号码");
            return;
        }
        getServiceQueue().postRunnable(new Runnable()
        {
            @Override
            public void run()
            {

                for (CallListItem callListItem : getCurrentCallList())
                {
                    if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO
                            || callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
                    {
                        if (calleeNum.equals(((SCallModel) callListItem.getCallModel()).getPeerNum()))
                        {
                            ToastUtil.showUiToast(calleeNum + "用户忙，请稍后再拨");
                            return;
                        }
                        else if (callListItem.getCallModel().isConfMember() && callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
                        {
                            ToastUtil.showUiToast("正在开会中，请先退出会议再呼出");
                            return;
                        }
                    }
                }
                if (handleConnectedCalls(null, CALL2CONF1, calleeNum))
                {
                    return;
                }
                ;
                ArrayList<VsListItem> vslist = UiApplication.getVsService().getVsUserList();
                for (VsListItem vsListItem : vslist)
                {
                    if (calleeNum.equals(((VsModel) vsListItem.getVsModel()).getVideoNum()) && vsListItem.isOpened())
                    {
                        ToastUtil.showUiToast("视频监控已发起");
                        return;
                    }
                }
                UiApplication.getSCallService().sCallMake(
                        UiApplication.getConfigService().isEmergencyEnabled() ? UiApplication.getConfigService().getEmergencyCallPriority() : UiApplication
                                .getConfigService().getCallPriority(), null, null, null, calleeNum, getContactNameByNumber(calleeNum), 0,
                        UiApplication.getConfigService().isVideoCallEnabled());
            }
        });

    }

    private static String getContactNameByNumber(String number)
    {
        ContactModel contact = UiApplication.getContactService().getContactByPhoneNum(number);
        if (contact != null)
        {
            return contact.getName();
        }
        else
        {
            return number;
        }
    }

    /**
     * 方法说明 : 发起会议
     * @param context
     * @param numberList
     * @return void
     * @author hubin
     * @Date 2015-9-8
     */
    public static void makeConf(Context context, String confName, ArrayList<String> numberList)
    {
        Log.info(TAG, "makeConf::");
        if (!UiApplication.isCallServerOnline)
        {
            ToastUtil.showToast("离线状态，无法发起业务");
            return;
        }
        for (CallListItem callListItem : getCurrentCallList())
        {
            if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO
                    || callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {
                ToastUtil.showToast("会议已发起");
                return;
            }
            // 如果已经作为会议成员正在通话中，则不能够再发起会议
            else if (callListItem.getCallModel().isConfMember() && callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
            {
                ToastUtil.showToast("正在开会中，请先退出会议再呼出");
                return;
            }
        }
        if (handleConnectedCalls(null, DEFAULT_CALL_TYPE))
        {
            return;
        }
        ConfModel mConfModel = new ConfModel();
        ArrayList<ConfMemModel> memberList = new ArrayList<ConfMemModel>();
        for (String number : numberList)
        {
            if (number.equals(UiApplication.getConfigService().getAccountNumber()))
            {
                ToastUtil.showToast("已过滤非法会议成员号码");
                continue;
            }
            ConfMemModel confMemModel = new ConfMemModel();
            confMemModel.setNumber(number);
            confMemModel.setName(getContactNameByNumber(number));
            memberList.add(confMemModel);
        }
        mConfModel.setMemList(memberList);
        UiApplication.getConfService().confCreate(
                confName,
                UiApplication.getConfigService().isEmergencyEnabled() ? UiApplication.getConfigService().getEmergencyCallPriority() : UiApplication
                        .getConfigService().getCallPriority(), 1, 1, UiApplication.getConfigService().isVideoCallEnabled(), mConfModel.getMemList());
    }

    /**
     * 方法说明 :有新呼叫（来呼或去呼），处理其他路呼叫
     * @param sessionId 过滤掉的通话，一般为自身
     * @param call2Conf true ：单呼入会  
     * @param args
     * @author HeZhen
     * @Date 2015-9-15
     */
    public static boolean handleConnectedCalls(String sessionId, int callType, Object... args)
    {
        for (CallListItem callListItem : getCurrentCallList())
        {
            // 过滤
            // FIXME:callListItem.isTransferToConf()条件成立的前提是CallListFragment中resetListData的clone目前作用效果有限
            if (!TextUtils.isEmpty(sessionId) && sessionId.equals(callListItem.getCallModel().getSessionId()) || callListItem.isTransferToConf())
            {
                continue;
            }
            if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
            {
                // 在通话状态则先保持
                if (callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT
                        || callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_REMOTE_HOLD)
                {
                    UiApplication.getSCallService().sCallHold(callListItem.getCallModel().getSessionId());
                    if (compareStatus(callListItem, CommonConstantEntry.SCALL_STATE_HOLD, 0))
                    {
                        break;
                    }
                }// 如果在振铃状态，则挂断
                else if (callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_RINGBACK)
                {
                    UiApplication.getSCallService().sCallRelease(callListItem.getCallModel().getSessionId(), CommonConstantEntry.Q850_PREEMPTION);
                }
            }
            else if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO
                    || callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
            {
                // 会议在进行状态
                if (callListItem.getStatus() == CommonConstantEntry.CONF_STATE_CONNECT)
                {
                    if (callType == CALL2CONF1)
                    {// 呼出，则直接入会
                        String calleeNum = (String) args[0];
                        // 对于会议中已有成员，如果成员状态空闲，则再次呼叫入会
                        for (ConfMemberItem confMemberItem : CallEventHandler.getConfMemberItems())
                        {
                            if (calleeNum.equals(confMemberItem.getContent()) && confMemberItem.getConfMemModel().getStatus() != CommonConstantEntry.CONF_MEMBER_STATE_IDLE)
                            {
                                return true;
                            }
                        }
                        UiApplication.getConfService().confUserAdd(callListItem.getCallModel().getSessionId(), calleeNum);
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_TO_CONF, calleeNum);
                        return true;
                    }
                    else if (callType == CALL2CONF2)
                    {// 来呼，点击入会后，添加成员 add ：在接通后 状态那处理 ，此处不做处理
//                        CallListItem item = (CallListItem) args[0];
//                        UiApplication.getConfService().confUserAdd(callListItem.getCallModel().getSessionId(), item.getContent());
//                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CONF_MEMBER_ADD, ConfControlFragment.CALL_TO_CONF,
//                                item.getContent());
                    }
                    else
                    {// 其他则退会
                        UiApplication.getConfService().confLeave(callListItem.getCallModel().getSessionId());
                        if (compareStatus(callListItem, CommonConstantEntry.CONF_STATE_EXIT, 0))
                        {
                            break;
                        }
                    }
                }
                else if (callListItem.getStatus() == CommonConstantEntry.CONF_STATE_EXIT)// 退会状态下处理
                {
                    if (callType == CALL2CONF2)
                    {// 来呼，点击入会后，添加成员
                        CallListItem item = (CallListItem) args[0];
//                        if (compareStatus(item, CommonConstantEntry.SCALL_STATE_CONNECT, 0))// 此处肯定是接通，不然入会走 未接听直接入会流程
//                                                                                            
//                        {
                        UiApplication.getConfService().confUserAdd(callListItem.getCallModel().getSessionId(), item.getContent());
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CALL_TO_CONF, item.getContent());
//                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 方法说明 :堵塞线程,等待服务器回执，刷新状态
     * @param callListItem
     * @param toState
     * @param timeCount 查询次数记录
     * @return
     * @author HeZhen
     * @Date 2015-9-15
     */
    private static boolean compareStatus(CallListItem callListItem, int toState, int timeCount)
    {
        Log.info(TAG, "callListItem.getStatus() == " + callListItem.getStatus() + ",,toState == " + toState + ",,timeCount == " + timeCount);
        if (callListItem.getStatus() == toState)// 等于目标状态则返回true;
        {
            return true;
        }
        else
        {
            try
            {
                // 每次休眠时间
                timeCount++;
                Thread.sleep(400l);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            // 最多可查询次数限制
            if (timeCount > 3)
            {
                return false;
            }
            return compareStatus(callListItem, toState, timeCount);
        }
    }

    public static ArrayList<CallListItem> getCurrentCallList()
    {
        return CallEventHandler.getCallList();
    }

    public static boolean isCallExist(String sessionId)
    {
        if (TextUtils.isEmpty(sessionId))
        {
            return false;
        }
        for (CallListItem callItem : getCurrentCallList())
        {
            if (sessionId.equals(callItem.getCallModel().getSessionId()))
            {
                return true;
            }
        }
        return false;
    }

//    /**
//    * 方法说明 :根据号码获取联系人
//    * @param phoneNum
//    * @return
//    * @author HeZhen
//    * @Date 2015-7-21
//    */
//    public static ContactModel getContactByPhone(String phoneNum)
//    {
//        if (TextUtils.isEmpty(phoneNum))
//        {
//            return null;
//        }
//        ContactModel contact = null;
//        for (ContactModel ContactModel : UiApplication.getContactService().getContactList())
//        {
//            if (ContactModel.getPhoneNumList().size() <= 0)
//            {
//                break;
//            }
//            ContactNum contactNum = ContactModel.getPhoneNumList().get(0);
//            if (contactNum != null && contactNum.getNumber().equals(phoneNum))
//            {
//                contact = ContactModel.clone();
//            }
//        }
//        return contact;
//    }

    public static ArrayList<CallRecordListItem> getCallRecordListItems(ArrayList<CallRecordListItem> callRecordListItems)
    {
        Log.info(TAG, "getCallRecordListItems::callRecordListItems::" + callRecordListItems.size());
        ArrayList<CallRecordListItem> caList = new ArrayList<CallRecordListItem>();
        ArrayList<CallRecordListItem> catempList = new ArrayList<CallRecordListItem>();
        int j = 0;
        int i = 0;
        int count = 1;

        for (CallRecordListItem callRecordListItem : callRecordListItems)
        {
            int callType = callRecordListItem.getCallRecord().getCallType();
            if (callType != CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO && callType != CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {
                catempList.add(callRecordListItem);
            }
        }

        if (catempList != null && catempList.size() == 1)
        {
            return callRecordListItems;
        }

        for (j = i + 1; j < callRecordListItems.size(); j++)
        {

            int callType = callRecordListItems.get(i).getCallRecord().getCallType();
            if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {
                caList.add(callRecordListItems.get(i));
                i++;
                j++;
            }
            else
            {
                String peer1 = callRecordListItems.get(i).getCallRecord().getPeerNum();
                String peer2 = callRecordListItems.get(j).getCallRecord().getPeerNum();
                if (peer1.equals(peer2))
                {
                    count++;
                }
                else
                {
                    callRecordListItems.get(i).setCount(count);
                    caList.add(callRecordListItems.get(i));
                    i = j;
                    count = 1;
                }
                if (j == callRecordListItems.size() - 1)
                {
                    callRecordListItems.get(j).setCount(count);
                    caList.add(callRecordListItems.get(j));
                    break;
                }
            }

        }
        return caList;
    }

    public static boolean isVersionUpdate(final Context context)
    {

        Thread thread = new Thread()
        {

            @Override
            public void run()
            {
                super.run();
                VersionClient versionClient = new VersionClient(context);
                String server = UiApplication.getConfigService().getMasterServerIp();// 临时取主服务器地址
                Log.info(TAG, "server::" + server);
                if (TextUtils.isEmpty(server))
                {
                    isUpDate = false;
                }
                else
                {

                    // 后续该模块名称为t30
                    String newVersion = versionClient.getLatestVersion(server, 50021, "t30");
                    serverVersionName = newVersion;
                    if (!(TextUtils.isEmpty(newVersion)))
                    {
                        isUpDate = versionClient.compareVersion(UiApplication.getInstance().getAppVersionName(), newVersion);
                    }
                    else
                    {
                        isUpDate = false;
                    }
                }
            }
        };
        thread.start();

//        try
//        {
//            thread.join();
//        }
//        catch (InterruptedException e)
//        {
//            Log.exception(TAG, e);
//        }
        return isUpDate;

    }

    /**
     * 方法说明 :根据keyCode得到数字或符号
     * @param keyCode
     * @return
     * @author chaimb
     * @Date 2015-9-21
     */
    public static String getDialNum(int keyCode)
    {
        String dialNum = "";
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_0:
                dialNum = "0";
                break;
            case KeyEvent.KEYCODE_1:
                dialNum = "1";
                break;
            case KeyEvent.KEYCODE_2:
                dialNum = "2";
                break;
            case KeyEvent.KEYCODE_3:
                dialNum = "3";
                break;
            case KeyEvent.KEYCODE_4:
                dialNum = "4";
                break;
            case KeyEvent.KEYCODE_5:
                dialNum = "5";
                break;
            case KeyEvent.KEYCODE_6:
                dialNum = "6";
                break;
            case KeyEvent.KEYCODE_7:
                dialNum = "7";
                break;
            case KeyEvent.KEYCODE_8:
                dialNum = "8";
                break;
            case KeyEvent.KEYCODE_9:
                dialNum = "9";
                break;
            case KeyEvent.KEYCODE_STAR:
                dialNum = "*";
                break;
            case KeyEvent.KEYCODE_POUND:
                dialNum = "#";
                break;

            default:
                dialNum = "";
                break;
        }

        return dialNum;
    }

    /**
     * 方法说明 :按"#"拨打电话以及按"*"退出系统
     * @param number
     * @author chaimb
     * @Date 2015-9-17
     */
    public static void callNumber(Context mContext, String number)
    {
        if (number.endsWith("#"))
        {
            String callNum = number.substring(0, number.length() - 1);
            if (!TextUtils.isEmpty(callNum))
            {
                ContactModel contactModel = UiApplication.getContactService().getContactByPhoneNum(callNum);

                if (contactModel != null)
                {
                    if (ContactUtil.isVsByContactType(contactModel.getTypeName()))
                    {
                        UiApplication.getVsService().addVsUser(callNum);
                    }
                    else
                    {

                        ServiceUtils.makeCall(mContext, callNum);
                    }
                }
                else
                {

                    ServiceUtils.makeCall(mContext, callNum);
                }
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CLOSE_DIAL, true);
            }
        }
        else if ("*999999*".equals(number))
        {// 拨号盘命令退出系统
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_EXIT_SYSYTEM);
        }
        else
        {
            Log.info(TAG, "no # endwith");
        }
    }

    /**
     * 方法说明 :对获取的通话记录进行全拼的设置
     * @param callRecord 得到的通话记录的对象
     * @author chaimb
     * @Date 2015-7-15
     */
    public static CallRecordListItem setCallRecordList(CallRecord callRecord)
    {
        if (callRecord == null)
        {
            return null;
        }

        CharacterParser cp = new CharacterParser();
        CallRecordListItem callRecordListItem = new CallRecordListItem();
        callRecordListItem.setCallRecord(callRecord);
        // 设置记录的全拼及全拼数组
        if (callRecord.getPeerName() != null)
        {
            cp.setResource(callRecord.getPeerName());
            String pinyin = cp.getSpelling();
            callRecordListItem.setName(callRecord.getPeerName());
            callRecordListItem.setNameLetters(pinyin);
            callRecordListItem.setNameLettersArray(cp.getSpellings());
        }

        return callRecordListItem;

    }
}
