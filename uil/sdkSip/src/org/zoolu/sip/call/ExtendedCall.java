/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * 
 * This file is part of MjSip (http://www.mjsip.org)
 * 
 * MjSip is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * MjSip is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MjSip; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 * Nitin Khanna, Hughes Systique Corp. (Reason: Android specific change,
 * optmization, bug fix)
 */

package org.zoolu.sip.call;

/* HSC CHANGES BEGIN */
// import org.zoolu.sip.call.*;
/* HSC CHANGES END */
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.dialog.ExtendedInviteDialog;
import org.zoolu.sip.dialog.ExtendedInviteDialogListener;
import org.zoolu.sip.dialog.InviteDialog;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.message.BaseMessageFactory;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.message.SipMethods;
import org.zoolu.sip.provider.SipProvider;

import android.text.TextUtils;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;

/**
 * Class ExtendedCall extends basic SIP calls.
 * <p>
 * It implements: <br>- call Forward (REFER/NOTIFY methods)
 */
public class ExtendedCall extends Call implements ExtendedInviteDialogListener
{

    ExtendedCallListener xcall_listener;

    Message refer;

    /** User name. */
    String username;

    /** User name. */
    String realm;

    /** User's passwd. */
    String passwd;

    /** Nonce for the next authentication. */
    String next_nonce;

    /** Qop for the next authentication. */
    String qop;

    // public static final String CONTENT_TYPE_MBCP = "Application/mbcp";
    public static final String CONTENT_TYPE_XML = "Application/xml";
    public static final String CONTENT_TYPE_MBCP = "Application/msap+xml";
    public static final String CONTENT_TYPE_CCCP = "Application/cccp+xml";

    // whether get the talk priority. ptt means push to talk.
    private boolean ptt = false;// 是否有话权

    private String originalCallNumber;// 呼叫转移时主叫号码（发起者）

    private boolean isHolded = false;// 是否被保持

    private boolean isSpeakerOn = false;// 是否打开扬声器

    private String call_id;

    private boolean saStatus = false;// true：离开SA

    private int lastCountRequestId = -1;// 上次组呼人数请求编号
    
    private int localAudioPort;// 本地音频端口

    private int remoteAudioPort;// 对端音频端口

    private int localVideoPort;// 本地视频端口

    private int remoteVideoPort;// 对端视频端口

    private String remoteMediaAddress;// 对端媒体地址
    
    private boolean conferenceMember = false;// 会议成员标识

    private final static String TAG = "ExtendedCall";

    /** Creates a new ExtendedCall. */
    public ExtendedCall(SipProvider sip_provider, String from_url, String contact_url, ExtendedCallListener call_listener)
    {
        super(sip_provider, from_url, contact_url, call_listener);
        this.xcall_listener = call_listener;
        this.refer = null;
        this.username = null;
        this.realm = null;
        this.passwd = null;
        this.next_nonce = null;
        this.qop = null;
    }

    public ExtendedCall()
    {
        super(null, null, null, null);

        this.refer = null;
        this.username = null;
        this.realm = null;
        this.passwd = null;
        this.next_nonce = null;
        this.qop = null;
    }

    /** Creates a new ExtendedCall specifing the sdp. */
    /*
     * public ExtendedCall(SipProvider sip_provider, String from_url, String
     * contact_url, String sdp, ExtendedCallListener call_listener) {
     * super(sip_provider,from_url,contact_url,sdp,call_listener);
     * xcall_listener=call_listener; }
     */

    /** Creates a new ExtendedCall. */
    public ExtendedCall(SipProvider sip_provider, String from_url, String contact_url, String username, String realm, String passwd,
            ExtendedCallListener call_listener, String originalCall)
    {
        super(sip_provider, from_url, contact_url, call_listener);
        this.xcall_listener = call_listener;
        this.refer = null;
        this.username = username;
        this.realm = realm;
        this.passwd = passwd;
        this.next_nonce = null;
        this.qop = null;
        this.originalCallNumber = originalCall;
    }

    /** Waits for an incoming call */
    public void listen()
    {
        if (username != null)
            dialog = new ExtendedInviteDialog(sip_provider, username, realm, passwd, this);
        else
            dialog = new ExtendedInviteDialog(sip_provider, this);
        dialog.listen();
    }

    /** 
     * Starts a new call, inviting a remote user (<i>r_user</i>) 
     * @throws Exception 
     * */
    public boolean call(final String r_user, String from, String contact, String sdp, final String icsi, final int priority, final String funNumber,
            final boolean activeGroup) throws Exception
    {
        Log.info(TAG, "call:: r_user:" + r_user + " from:" + from + " contact:" + contact + " sdp:" + sdp + " priority:" + priority + " funNumber" + funNumber
                + " activeGroup:" + activeGroup);

        call_priority = priority;
        callingPart = true;// 外呼状态

        if (username != null)
            dialog = new ExtendedInviteDialog(sip_provider, username, realm, passwd, this);
        else
            dialog = new ExtendedInviteDialog(sip_provider, this);
        if (from == null)
            from = from_url;
        if (contact == null)
            contact = contact_url;
        if (sdp != null)
            local_sdp = sdp;

        if (local_sdp != null)
            return dialog.invite(r_user, from, contact, local_sdp, icsi, priority, funNumber, call_id, activeGroup);
        else
            return dialog.inviteWithoutOffer(r_user, from, contact, priority, funNumber, call_id, activeGroup);
    }

    /** Starts a new call with the <i>invite</i> message request 
     * @throws Exception */
    public void call(Message invite) throws Exception
    {
        dialog = new ExtendedInviteDialog(sip_provider, this);
        local_sdp = invite.getBody();
        if (local_sdp != null)
            dialog.invite(invite);
        else
            dialog.inviteWithoutOffer(invite);
    }

//
//    /**
//     * 抢占|释放话权
//     * 
//     * PTT retrieve or release 1）原有消息格式为 command=0 2）现有格式为 command=0
//     * speaker="86112233441" 3）申请PTT的格式和后台回复给终端的格式都一样
//     * 4）speaker后面的号码：如果申请PTT的用户注册了功能号，则携带功能号，否则携带其isdn号
//     * 5）后台将在command为1、2、3时携带讲者的号码（讲者申请时携带的号码）。其他情况下，号码将为空
//     * 
//     * @param groupId
//     *              组呼号码
//     * @param seize
//     *              0：抢占|1：释放
//     * @param functionCode
//     *              功能号码
//     */
//    public void pushInfo(String groupId, int seize, String functionCode)
//    {
//        Log.info(TAG, "pushInfo::groupId" + groupId + " seize:" + seize + " functionCode:" + functionCode);
//        
//        XmlMessage msg = null;
//        if(seize == 1)
//        {//释放消息
//            msg = XmlMessageFactory.createPttReleaseMsg(functionCode);
//        }
//        else
//        {//申请消息
//            msg = XmlMessageFactory.createPttRequestMsg(functionCode);
//        }
//        
//        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
//        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.INFO,
//                null);
//        req.setBody(CONTENT_TYPE_MBCP, msg.toString());
//        
//        // --------------------------------组呼抢占性能测试日志记录
//        // Begin--------------------------------------------------------
//        if (ConfigParam.TEST_PTT)
//        {
//            if (seize == 0)
//            {// 申请
//                PerfTestHelper.PTTPREF.setSendPttRequest(System.currentTimeMillis());
//            }
//            else
//            {// 释放
//                PerfTestHelper.PTTPREF.setSendPttRelease(System.currentTimeMillis());
//            }
//        }
//        // --------------------------------组呼抢占性能测试日志记录
//        // End------------------------------------------------------
//        
//        eDialog.pushInfo(req);
//    }
    
    /** msg消息发送xml */
    public void pushMsg(String xmlBody) throws Exception
    {
        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.MESSAGE, null);
        req.setBody(CONTENT_TYPE_XML, xmlBody);
        eDialog.pushMessage(req);
    }
    
    /** msg消息发送xml */
    public void pushMsg(String xmlBody, String target_url) throws Exception
    {
        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
        NameAddress to = new NameAddress(target_url);
        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.MESSAGE, to, null);
        req.setBody(CONTENT_TYPE_XML, xmlBody);
        eDialog.pushMessage(req);
    }

    /** info消息发送xml */
    public void pushInfo(String xmlBody) throws Exception
    {
        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.INFO, null);
        req.setBody(CONTENT_TYPE_MBCP, xmlBody);
        eDialog.pushMessage(req);
    }

    /** info消息发送dtmf 
     * @throws Exception */
    public void info(char c, int duration) throws Exception
    {
        ((ExtendedInviteDialog) dialog).info(c, duration);
    }

    /** Requests a call Forward 
     * @throws Exception */
    public void Forward(String Forward_to) throws Exception
    {
        ((ExtendedInviteDialog) dialog).refer(new NameAddress(Forward_to));
    }

    /** Accepts a call Forward request 
     * @throws Exception */
    public void acceptForward() throws Exception
    {
        ((ExtendedInviteDialog) dialog).acceptRefer(refer);
    }

    /** Refuses a call Forward request 
     * @throws Exception */
    public void refuseForward() throws Exception
    {
        ((ExtendedInviteDialog) dialog).refuseRefer(refer);
    }

    /** Notifies the satus of an other call 
     * @throws Exception */
    public void notify(int code, String reason) throws Exception
    {
        ((ExtendedInviteDialog) dialog).notify(code, reason);
    }

    // ************** Inherited from InviteDialogListener **************

    /** When an incoming REFER request is received within the dialog */
    public void onDlgRefer(org.zoolu.sip.dialog.InviteDialog d, NameAddress refer_to, NameAddress referred_by, Message msg)
    {
        if (d != dialog)
        {
            Log.error("ExtendedCall.onDlgRefer", "NOT the current dialog");
            return;
        }
        Log.info("call.onDlgRefer", "onDlgRefer(" + refer_to.toString() + ")");

        refer = msg;
        if (xcall_listener != null)
            xcall_listener.onCallForward(this, refer_to, referred_by, msg);
    }

    /** When a response is received for a REFER request within the dialog */
    public void onDlgReferResponse(org.zoolu.sip.dialog.InviteDialog d, int code, String reason, Message msg)
    {
        if (d != dialog)
        {
            Log.error("ExtendedCall.onDlgReferResponse", "NOT the current dialog");
            return;
        }
        Log.info("ExtendedInviteDialog", "onDlgReferResponse(" + code + " " + reason + ")");

        if (code >= 200 && code < 300)
        {
            if (xcall_listener != null)
                xcall_listener.onCallForwardAccepted(this, msg);
        }
        else if (code >= 300)
        {
            if (xcall_listener != null)
                xcall_listener.onCallForwardRefused(this, reason, msg);
        }
    }

    /** When an incoming NOTIFY request is received within the dialog */
    public void onDlgNotify(org.zoolu.sip.dialog.InviteDialog d, String event, String sipfragment, Message msg)
    {
        if (d != dialog)
        {
            Log.error(TAG, "onDlgNotify:: NOT the current dialog");
            return;
        }
        Log.info(TAG, "onDlgNotify");

        if (event.equals("refer"))
        {
            Message fragment = new Message(sipfragment);
            Log.error(TAG, "onDlgNotify:: Notify: " + sipfragment);
            if (fragment.isResponse())
            {
                StatusLine status_line = fragment.getStatusLine();
                int code = status_line.getCode();
                String reason = status_line.getReason();
                if (code >= 200 && code < 300)
                {
                    Log.info(TAG, "Call successfully Forwardred");
                    if (xcall_listener != null)
                        xcall_listener.onCallForwardSuccess(this, msg);
                }
                else if (code >= 300)
                {
                    Log.info(TAG, "Call NOT Forwardred");
                    if (xcall_listener != null)
                        xcall_listener.onCallForwardFailure(this, reason, msg);
                }
            }
        }
    }

    /**
     * When an incoming request is received within the dialog different from
     * INVITE, CANCEL, ACK, BYE
     */
    public void onDlgAltRequest(org.zoolu.sip.dialog.InviteDialog d, String method, String body, Message msg)
    {
    }

    /**
     * When a response is received for a request within the dialog different
     * from INVITE, CANCEL, ACK, BYE
     */
    public void onDlgAltResponse(org.zoolu.sip.dialog.InviteDialog d, String method, int code, String reason, String body, Message msg)
    {
    }

    @Override
    public void onDlgInfoResponse(InviteDialog dialog, int code, String reason, String body, Message msg) throws Exception
    {
        Log.info(TAG, "onDlgInfoResponse:: Ptt code=" + code + " body=" + body + " call state:" + call_state);
        if (call_state == CommonConstantEntry.CALL_STATE_IN_CALL || call_state == CommonConstantEntry.CALL_STATE_HOLD || isHolded)
        {// 通话中
         // send the ACK back.
            ((ExtendedInviteDialog) dialog).ack(msg);
        }

        if (TextUtils.isEmpty(body))
        {
            // 接收到心跳，此处需要处理，任何呼叫，只要有几次未收到心跳，则需要关闭当前通话。
            onCallHeartBeatResponse(dialog, msg);
        }
        else
        {
//            XmlMessage xml = XmlMessageFactory.parse(body);
//            String eventType = xml.getEventType();
//            if (eventType == null)
//            {// 解析不了的消息退出
//                Log.error(TAG, "eventType == null");
//                return;
//            }

//            if(eventType.equals(PushToTalkHandler.MBCP_REQUEST_RESPONSE))
//            {//PTT申请话权
//                onPttPress(xml);
//            }
//            else if(eventType.equals(PushToTalkHandler.MBCP_RELEASE_RESPONSE))
//            {//PTT释放话权
//                onPttRelease(xml);
//            }
//            else if(eventType.equals(PushToTalkHandler.MBCP_NOTIFY))
//            {//PTT通知
//                onPttNotify(xml);
//            }
        }

        if (xcall_listener != null)
            xcall_listener.onCallInfoResponse(this, code, reason, body, msg);
    }

//    /**
//     * PTT话权通知
//     */
//    public void onPttNotify(XmlMessage xml)
//    {
//        int code = ServiceConstant.PTT_IDEL;//PTT_MB_IDLE
//        Element speaker = xml.getParameter("Speaker");
//        String speakerValue = speaker.getAttribute("value").value;
//        if(speaker.getAttribute("status").value.equals("on"))
//        {
//            code = ServiceConstant.PTT_TALK;//有话权
//            if(callerFn != null && isCallingPart() && (speakerValue.equals(callerFn) || speakerValue.equals(callerNumber)))
//            {//主叫自己本身
//            	setPtt(true);//预授话权
//            }
//        }
//        else
//        {
//        	setPtt(false);//空闲
//        }
//        
//        //通知接口：通知“话权状态”
//        ServiceNotify.firePttStatusNotify(getCallId(), code, speakerValue);
//    }
//    
//    /**
//     * PTT话权释放处理
//     */
//    public void onPttRelease(XmlMessage xml)
//    {
//        Element result = xml.getParameter("result");
//        int code = ServiceConstant.RESPONSE_FAILED;//PTT_MB_DENY
//        if(result.getAttribute("value").value.equals("succ"))
//        {
//            code = ServiceConstant.RESPONSE_SUCCESS;//PTT_MB_IDLE
//        }
//        
//        // 失败的场景只存在于没有话权时释放话权，所以统一做无话权处理
//        setPtt(false);//释放话权成功
//        
//        // --------------------------------组呼抢占性能测试日志记录
//        // Begin--------------------------------------------------------
//        if(ConfigParam.TEST_PTT)
//        {
//            PerfTestHelper.PTTPREF.setReceivePttReleaseReponse(System.currentTimeMillis());
//        }
//        // --------------------------------组呼抢占性能测试日志记录
//        // End------------------------------------------------------
//        
//        //通知接口：通知“申请/释放话权的处理结果”
//        ServiceNotify.firePttOperateResponse(getCallId(), ServiceConstant.PTT_RELEASE, code, "");
//    }
//    
//    /**
//     * PTT话权申请处理
//     */
//    public void onPttPress(XmlMessage xml)
//    {
//        Element result = xml.getParameter("result");
//        int code = ServiceConstant.RESPONSE_FAILED;
//        if(result.getAttribute("value").value.equals("succ"))
//        {
//            code = ServiceConstant.RESPONSE_SUCCESS;//PTT_MB_GRANTED
//            setPtt(true);//申请到话权
//        }
//        
//        // --------------------------------组呼抢占性能测试日志记录
//        // Begin--------------------------------------------------------
//        if(ConfigParam.TEST_PTT)
//        {
//            PerfTestHelper.PTTPREF.setReceivePttRequestReponse(System.currentTimeMillis());
//        }
//        // --------------------------------组呼抢占性能测试日志记录
//        // End------------------------------------------------------
//        
//        //通知接口：通知“申请/释放话权的处理结果”
//        ServiceNotify.firePttOperateResponse(getCallId(), ServiceConstant.PTT_PRESS, code, "");
//    }

    public boolean isPtt()
    {
        return ptt;
    }

//    public void setPtt(boolean value)
//    {
//        ptt = value;
//
//        if (callType == CommonConstantEntry.CALL_TYPE_GROUP && ptt)
//        {// 组呼有话权
//            if (xcall_listener != null)
//                xcall_listener.setMute(getCallId(), false);
//        }
//        else
//        {// 组呼无话权
//            if (xcall_listener != null)
//                xcall_listener.setMute(getCallId(), true);
//        }
//    }

    public String getCallId()
    {
        if (call_id != null)
        {
            return call_id;
        }
        else if (dialog != null)
        {
            return dialog.getCallID();
        }

        return null;
    }

    public void setCallId(String callId)
    {
        call_id = callId;
    }

    public String getOriginalCallNumber()
    {
        return originalCallNumber;
    }

    public boolean isHolded()
    {
        return isHolded;
    }

    public void setHolded(boolean isHolded)
    {
        this.isHolded = isHolded;
    }

    /**
     * 是否外呼
     */
    public boolean isCallingPart()
    {
        return callingPart;
    }

    public void setCallingPart(boolean callingPart)
    {
        this.callingPart = callingPart;
    }

    /**
     * 每次接收到心跳时更新此时间，用于在CallScreen中判断此呼叫的状态，从而确定其超时时间。
     * */
    public void onCallHeartBeatResponse(InviteDialog dialog, Message msg)
    {
        heartbeatTime = System.currentTimeMillis();
        Log.info(TAG, "onCallHeartBeatResponse::heartbeatTime:" + heartbeatTime);
    }

    public boolean isSpeakerOn()
    {
        return isSpeakerOn;
    }

    public void setSpeakerOn(boolean isSpeakerOn)
    {
        this.isSpeakerOn = isSpeakerOn;
    }

    public boolean isSaStatus()
    {
        return saStatus;
    }

    public void setSaStatus(boolean saStatus)
    {
        this.saStatus = saStatus;
    }

    public int getLastCountRequestId()
    {
        return lastCountRequestId;
    }

    public void setLastCountRequestId(int lastCountRequestId)
    {
        this.lastCountRequestId = lastCountRequestId;
    }

    public int getLocalVideoPort()
    {
        return localVideoPort;
    }

    public void setLocalVideoPort(int localVideoPort)
    {
        this.localVideoPort = localVideoPort;
    }

    public int getRemoteVideoPort()
    {
        return remoteVideoPort;
    }

    public void setRemoteVideoPort(int remoteVideoPort)
    {
        this.remoteVideoPort = remoteVideoPort;
    }

    public String getRemoteMediaAddress()
    {
        return remoteMediaAddress;
    }

    public void setRemoteMediaAddress(String remoteVideoAddress)
    {
        this.remoteMediaAddress = remoteVideoAddress;
    }

    public int getLocalAudioPort()
    {
        return localAudioPort;
    }

    public void setLocalAudioPort(int localAudioPort)
    {
        this.localAudioPort = localAudioPort;
    }

    public int getRemoteAudioPort()
    {
        return remoteAudioPort;
    }

    public void setRemoteAudioPort(int remoteAudioPort)
    {
        this.remoteAudioPort = remoteAudioPort;
    }

    public boolean isConferenceMember()
    {
        return conferenceMember;
    }

    public void setConferenceMember(boolean conferenceMember)
    {
        this.conferenceMember = conferenceMember;
    }

}
