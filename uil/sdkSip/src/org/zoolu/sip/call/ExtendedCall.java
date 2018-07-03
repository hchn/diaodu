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
    private boolean ptt = false;// �Ƿ��л�Ȩ

    private String originalCallNumber;// ����ת��ʱ���к��루�����ߣ�

    private boolean isHolded = false;// �Ƿ񱻱���

    private boolean isSpeakerOn = false;// �Ƿ��������

    private String call_id;

    private boolean saStatus = false;// true���뿪SA

    private int lastCountRequestId = -1;// �ϴ��������������
    
    private int localAudioPort;// ������Ƶ�˿�

    private int remoteAudioPort;// �Զ���Ƶ�˿�

    private int localVideoPort;// ������Ƶ�˿�

    private int remoteVideoPort;// �Զ���Ƶ�˿�

    private String remoteMediaAddress;// �Զ�ý���ַ
    
    private boolean conferenceMember = false;// �����Ա��ʶ

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
        callingPart = true;// ���״̬

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
//     * ��ռ|�ͷŻ�Ȩ
//     * 
//     * PTT retrieve or release 1��ԭ����Ϣ��ʽΪ command=0 2�����и�ʽΪ command=0
//     * speaker="86112233441" 3������PTT�ĸ�ʽ�ͺ�̨�ظ����ն˵ĸ�ʽ��һ��
//     * 4��speaker����ĺ��룺�������PTT���û�ע���˹��ܺţ���Я�����ܺţ�����Я����isdn��
//     * 5����̨����commandΪ1��2��3ʱЯ�����ߵĺ��루��������ʱЯ���ĺ��룩����������£����뽫Ϊ��
//     * 
//     * @param groupId
//     *              �������
//     * @param seize
//     *              0����ռ|1���ͷ�
//     * @param functionCode
//     *              ���ܺ���
//     */
//    public void pushInfo(String groupId, int seize, String functionCode)
//    {
//        Log.info(TAG, "pushInfo::groupId" + groupId + " seize:" + seize + " functionCode:" + functionCode);
//        
//        XmlMessage msg = null;
//        if(seize == 1)
//        {//�ͷ���Ϣ
//            msg = XmlMessageFactory.createPttReleaseMsg(functionCode);
//        }
//        else
//        {//������Ϣ
//            msg = XmlMessageFactory.createPttRequestMsg(functionCode);
//        }
//        
//        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
//        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.INFO,
//                null);
//        req.setBody(CONTENT_TYPE_MBCP, msg.toString());
//        
//        // --------------------------------�����ռ���ܲ�����־��¼
//        // Begin--------------------------------------------------------
//        if (ConfigParam.TEST_PTT)
//        {
//            if (seize == 0)
//            {// ����
//                PerfTestHelper.PTTPREF.setSendPttRequest(System.currentTimeMillis());
//            }
//            else
//            {// �ͷ�
//                PerfTestHelper.PTTPREF.setSendPttRelease(System.currentTimeMillis());
//            }
//        }
//        // --------------------------------�����ռ���ܲ�����־��¼
//        // End------------------------------------------------------
//        
//        eDialog.pushInfo(req);
//    }
    
    /** msg��Ϣ����xml */
    public void pushMsg(String xmlBody) throws Exception
    {
        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.MESSAGE, null);
        req.setBody(CONTENT_TYPE_XML, xmlBody);
        eDialog.pushMessage(req);
    }
    
    /** msg��Ϣ����xml */
    public void pushMsg(String xmlBody, String target_url) throws Exception
    {
        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
        NameAddress to = new NameAddress(target_url);
        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.MESSAGE, to, null);
        req.setBody(CONTENT_TYPE_XML, xmlBody);
        eDialog.pushMessage(req);
    }

    /** info��Ϣ����xml */
    public void pushInfo(String xmlBody) throws Exception
    {
        ExtendedInviteDialog eDialog = (ExtendedInviteDialog) dialog;
        Message req = BaseMessageFactory.createRequest(eDialog, SipMethods.INFO, null);
        req.setBody(CONTENT_TYPE_MBCP, xmlBody);
        eDialog.pushMessage(req);
    }

    /** info��Ϣ����dtmf 
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
        {// ͨ����
         // send the ACK back.
            ((ExtendedInviteDialog) dialog).ack(msg);
        }

        if (TextUtils.isEmpty(body))
        {
            // ���յ��������˴���Ҫ�����κκ��У�ֻҪ�м���δ�յ�����������Ҫ�رյ�ǰͨ����
            onCallHeartBeatResponse(dialog, msg);
        }
        else
        {
//            XmlMessage xml = XmlMessageFactory.parse(body);
//            String eventType = xml.getEventType();
//            if (eventType == null)
//            {// �������˵���Ϣ�˳�
//                Log.error(TAG, "eventType == null");
//                return;
//            }

//            if(eventType.equals(PushToTalkHandler.MBCP_REQUEST_RESPONSE))
//            {//PTT���뻰Ȩ
//                onPttPress(xml);
//            }
//            else if(eventType.equals(PushToTalkHandler.MBCP_RELEASE_RESPONSE))
//            {//PTT�ͷŻ�Ȩ
//                onPttRelease(xml);
//            }
//            else if(eventType.equals(PushToTalkHandler.MBCP_NOTIFY))
//            {//PTT֪ͨ
//                onPttNotify(xml);
//            }
        }

        if (xcall_listener != null)
            xcall_listener.onCallInfoResponse(this, code, reason, body, msg);
    }

//    /**
//     * PTT��Ȩ֪ͨ
//     */
//    public void onPttNotify(XmlMessage xml)
//    {
//        int code = ServiceConstant.PTT_IDEL;//PTT_MB_IDLE
//        Element speaker = xml.getParameter("Speaker");
//        String speakerValue = speaker.getAttribute("value").value;
//        if(speaker.getAttribute("status").value.equals("on"))
//        {
//            code = ServiceConstant.PTT_TALK;//�л�Ȩ
//            if(callerFn != null && isCallingPart() && (speakerValue.equals(callerFn) || speakerValue.equals(callerNumber)))
//            {//�����Լ�����
//            	setPtt(true);//Ԥ�ڻ�Ȩ
//            }
//        }
//        else
//        {
//        	setPtt(false);//����
//        }
//        
//        //֪ͨ�ӿڣ�֪ͨ����Ȩ״̬��
//        ServiceNotify.firePttStatusNotify(getCallId(), code, speakerValue);
//    }
//    
//    /**
//     * PTT��Ȩ�ͷŴ���
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
//        // ʧ�ܵĳ���ֻ������û�л�Ȩʱ�ͷŻ�Ȩ������ͳһ���޻�Ȩ����
//        setPtt(false);//�ͷŻ�Ȩ�ɹ�
//        
//        // --------------------------------�����ռ���ܲ�����־��¼
//        // Begin--------------------------------------------------------
//        if(ConfigParam.TEST_PTT)
//        {
//            PerfTestHelper.PTTPREF.setReceivePttReleaseReponse(System.currentTimeMillis());
//        }
//        // --------------------------------�����ռ���ܲ�����־��¼
//        // End------------------------------------------------------
//        
//        //֪ͨ�ӿڣ�֪ͨ������/�ͷŻ�Ȩ�Ĵ�������
//        ServiceNotify.firePttOperateResponse(getCallId(), ServiceConstant.PTT_RELEASE, code, "");
//    }
//    
//    /**
//     * PTT��Ȩ���봦��
//     */
//    public void onPttPress(XmlMessage xml)
//    {
//        Element result = xml.getParameter("result");
//        int code = ServiceConstant.RESPONSE_FAILED;
//        if(result.getAttribute("value").value.equals("succ"))
//        {
//            code = ServiceConstant.RESPONSE_SUCCESS;//PTT_MB_GRANTED
//            setPtt(true);//���뵽��Ȩ
//        }
//        
//        // --------------------------------�����ռ���ܲ�����־��¼
//        // Begin--------------------------------------------------------
//        if(ConfigParam.TEST_PTT)
//        {
//            PerfTestHelper.PTTPREF.setReceivePttRequestReponse(System.currentTimeMillis());
//        }
//        // --------------------------------�����ռ���ܲ�����־��¼
//        // End------------------------------------------------------
//        
//        //֪ͨ�ӿڣ�֪ͨ������/�ͷŻ�Ȩ�Ĵ�������
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
//        {// ����л�Ȩ
//            if (xcall_listener != null)
//                xcall_listener.setMute(getCallId(), false);
//        }
//        else
//        {// ����޻�Ȩ
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
     * �Ƿ����
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
     * ÿ�ν��յ�����ʱ���´�ʱ�䣬������CallScreen���жϴ˺��е�״̬���Ӷ�ȷ���䳬ʱʱ�䡣
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
