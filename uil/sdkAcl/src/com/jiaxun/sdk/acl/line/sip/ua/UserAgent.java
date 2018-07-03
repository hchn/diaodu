/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.jiaxun.sdk.acl.line.sip.ua;

import java.util.Hashtable;
import java.util.Vector;

import org.zoolu.net.IpAddress;
import org.zoolu.sdp.AttributeField;
import org.zoolu.sdp.ConnectionField;
import org.zoolu.sdp.MediaDescriptor;
import org.zoolu.sdp.MediaField;
import org.zoolu.sdp.SessionDescriptor;
import org.zoolu.sdp.TimeField;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.call.Call;
import org.zoolu.sip.call.CallListenerAdapter;
import org.zoolu.sip.call.ExtendedCall;
import org.zoolu.sip.header.CallTypeHeader;
import org.zoolu.sip.header.Header;
import org.zoolu.sip.header.MultipleHeader;
import org.zoolu.sip.header.ReasonHeader;
import org.zoolu.sip.header.SipHeaders;
import org.zoolu.sip.header.StatusLine;
import org.zoolu.sip.header.UserToUserHeader;
import org.zoolu.sip.message.Message;
import org.zoolu.sip.provider.SipProvider;

import android.R.integer;
import android.text.TextUtils;

import com.jiaxun.sdk.acl.line.sip.SipAdapter;
import com.jiaxun.sdk.acl.line.sip.codecs.Codec;
import com.jiaxun.sdk.acl.line.sip.codecs.Codecs;
import com.jiaxun.sdk.acl.line.sip.service.conference.ConferenceHandler;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.log.PerfTestHelper;
import com.jiaxun.sdk.util.xml.XmlMessage;
import com.jiaxun.sdk.util.xml.XmlMessageFactory;
import com.jiaxun.sdk.util.xml.conference.ConfMedia;

/**
 * Simple SIP user agent (UA). It includes audio/video applications.
 * <p/>
 * It can use external audio/video tools as media applications. Currently only
 * RAT (Robust Audio Tool) and VIC are supported as external applications.
 */
public class UserAgent extends CallListenerAdapter
{
    private final String VIDEO = "video";
    private final String AUDIO = "audio";
    private final String SENDRECV = "sendrecv";
    private final String RECVONLY = "recvonly";
    private final String SENDONLY = "sendonly";
    private final String INACTIVE = "inactive";
    private final String RTPMAP = "rtpmap";
    private final String FMTP = "fmtp";
    private final String RTPMAP_FORMAT = "%d %s/%d";
    private final String RTP_AVP = "RTP/AVP";
    private final String TEL_EVENT_FORMAT = "%d telephone-event/%d";
    private final String FMTP_FORMAT = "%d 0-15";
    private final String CODEC_H264 = "H264";
    private final String MULTIPLEX16 = "multiplex:16";

    /**
     * SipProvider
     */
    protected SipProvider sip_provider;

    /**
     * Call
     */
    protected ExtendedCall listening_call;

    /**
     * Call Forward
     */
    protected ExtendedCall call_ForwardX;

    public static final int UA_STATE_HANGUP_CHANGED = 11;
    public static final int UA_STATE_GCNUM = 12;

    String realm;

    /** ����<callid:���ж���> */
    private static Hashtable<String, ExtendedCall> callCache = new Hashtable<String, ExtendedCall>();

    /** ����ǰת */
    private static Hashtable<ExtendedCall, ExtendedCall> Forward_original_cache = new Hashtable<ExtendedCall, ExtendedCall>();

    private SipAdapter mSipAdapter;

//    private static Hashtable<String, String> callIdMap;

    /**
     * ������Ϣ����
     */
    private ConferenceHandler conference;

    private final static String TAG = "UserAgent";

    /**
     * Costructs a UA with a default media port
     */
    public UserAgent(SipProvider sip_provider, SipAdapter sipAdapter)
    {
        this.sip_provider = sip_provider;
        this.mSipAdapter = sipAdapter;
        realm = mSipAdapter.realm;
        // if no contact_url and/or from_url has been set, create it now
        mSipAdapter.initContactAddress(sip_provider);
    }

    /**
     * ��������SDP
     */
    private SessionDescriptor createOffer(ExtendedCall call)
    {
        SessionDescriptor sdp = new SessionDescriptor(mSipAdapter.from_url, sip_provider.getViaAddress());

        sdp = addMediaDescriptor(sdp, AUDIO, mSipAdapter.getAudioPort(), null);
        sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(new AttributeField(SENDRECV)), null));
        if (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO
                || call.getCallType() == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
        {// ��Ƶ��������Ƶ���飬��Ƶ���
            sdp = addMediaDescriptor(sdp, VIDEO, mSipAdapter.getVideoPort(), mSipAdapter.video_avp, CODEC_H264, 90000);
            sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(new AttributeField(SENDRECV)), null));
            sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(new AttributeField(MULTIPLEX16)), null));
        }

        // Update the local SDP along with offer/answer
        if (call != null)
            call.setLocalSessionDescriptor(sdp.toString());

        return sdp;
    }

    /**
     * ����Ӧ��SDP
     */
    private SessionDescriptor createAnswer(ExtendedCall call, SessionDescriptor remote_sdp)
    {
        Codecs.Map c = Codecs.getCodec(remote_sdp);
        if (c == null)
        {
            try
            {
                hangup(call);
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }// �Ҷ�
            throw new RuntimeException("Failed to get CODEC: AVAILABLE : " + remote_sdp);
        }

        int localAudioPort = mSipAdapter.getAudioPort();
        call.setLocalAudioPort(localAudioPort);

        SessionDescriptor local_sdp = new SessionDescriptor(mSipAdapter.from_url, sip_provider.getViaAddress());
        local_sdp = addMediaDescriptor(local_sdp, AUDIO, localAudioPort, c);
        local_sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(new AttributeField(SENDRECV)), null));

        // We will have at least one media line, and it will be audio
        if (remote_sdp == null)
        {// ������ƵSDP
            if (call != null)
                call.setLocalSessionDescriptor(local_sdp.toString());
            return local_sdp;
        }
        else
        {
            call.setRemoteAudioPort(remote_sdp.getMediaDescriptor(AUDIO).getMedia().getPort());
            call.setRemoteMediaAddress(remote_sdp.getConnection().getAddress());
            if (remote_sdp.getMediaDescriptor(VIDEO) != null)
            {// ����Ƶ
                call.setRemoteVideoPort(remote_sdp.getMediaDescriptor(VIDEO).getMedia().getPort());
                int localVideoPort = mSipAdapter.getVideoPort();
                call.setLocalVideoPort(localVideoPort);
                local_sdp = addMediaDescriptor(local_sdp, VIDEO, localVideoPort, mSipAdapter.video_avp, "H264", 90000);
                local_sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(new AttributeField(SENDRECV)), null));
                // ��Ӷ�·��Ƶ��ʶ
                if (call.isConferenceMember())
                {
                    local_sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(new AttributeField(MULTIPLEX16)), null));
                }
            }
            if (call != null)
                call.setLocalSessionDescriptor(local_sdp.toString());
            return local_sdp;
        }
    }

    /**
     * Adds a single media to the SDP
     */
    private SessionDescriptor addMediaDescriptor(SessionDescriptor sdp, String media, int port, int avp, String codec, int rate)
    {
        String attr_param = String.valueOf(avp);

        if (codec != null)
        {
            attr_param += " " + codec + "/" + rate;
        }

        sdp.addMedia(new MediaField(media, port, 0, RTP_AVP, String.valueOf(avp)), new AttributeField(RTPMAP, attr_param));
        return sdp;
    }

    /**
     * Adds a set of media to the SDP
     */
    private SessionDescriptor addMediaDescriptor(SessionDescriptor sdp, String media, int port, Codecs.Map c)
    {
        Vector<String> avpvec = new Vector<String>();
        Vector<AttributeField> afvec = new Vector<AttributeField>();
        if (c == null)
        {
            // offer all known codecs
            for (int i : Codecs.getCodecs())
            {
                Codec codec = Codecs.get(i);
                if (i == 0)
                    codec.init();
                avpvec.add(String.valueOf(i));
                if (codec.number() == 9) // kludge for G722. See RFC3551.
                    afvec.add(new AttributeField(RTPMAP, String.format(RTPMAP_FORMAT, i, codec.userName(), 8000)));
                else
                    afvec.add(new AttributeField(RTPMAP, String.format(RTPMAP_FORMAT, i, codec.userName(), codec.samp_rate())));
            }
        }
        else
        {
            c.codec.init();
            avpvec.add(String.valueOf(c.number));
            if (c.codec.number() == 9) // kludge for G722. See RFC3551.
                afvec.add(new AttributeField(RTPMAP, String.format(RTPMAP_FORMAT, c.number, c.codec.userName(), 8000)));
            else
                afvec.add(new AttributeField(RTPMAP, String.format(RTPMAP_FORMAT, c.number, c.codec.userName(), c.codec.samp_rate())));
        }
//        if (mSipAdapter.dtmf_avp != 0)
//        {
//            avpvec.add(String.valueOf(mSipAdapter.dtmf_avp));
//            afvec.add(new AttributeField(RTPMAP, String.format(TEL_EVENT_FORMAT, mSipAdapter.dtmf_avp, mSipAdapter.audio_sample_rate)));
        afvec.add(new AttributeField(FMTP, String.format(FMTP_FORMAT, mSipAdapter.dtmf_avp)));
//        }

        sdp.addMedia(new MediaField(media, port, 0, RTP_AVP, avpvec), afvec);
        return sdp;
    }

    /**
     * remove call from call cache and close media application.
     * 
     * @param callId
     */
    protected void removeCallFromCache(String callId)
    {
        if (callId != null)
        {
            Log.info(TAG, "removeCallFromCache:: callId:" + callId + " host:" + mSipAdapter.realm);
            callCache.remove(callId);
//            callIdMap.remove(mSipAdapter.getSessionId(callId));
        }
    }

    /**
     * ɾ�����������е绰����
     */
    protected void removeAllCallFromCache()
    {
        Log.info(TAG, "removeAllCallFromCache:: host:" + mSipAdapter.realm);
        callCache.clear();
//        callIdMap.clear();
    }

    /**
     * Makes a new call (acting as UAC).
     */
    public boolean call(String callId, String target_url, int priority, int callType)
    {
        try
        {
            Log.info(TAG, "call:: priority:" + priority + " target_url:" + target_url + " callId:" + callId + " callType:" + callType + " host:"
                    + mSipAdapter.realm);

            if (checkCallIsExist(target_url))
            {// �Ѿ����ڴ˺���
                Log.error(TAG, "call:: call existed, number:" + target_url);
                // ֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
                if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
                {
//                    if (mSipAdapter.getConfEventListener() != null)
//                    {
//                        mSipAdapter.getConfEventListener().onConfClose(mSipAdapter.getSessionId(callId));
//                    }
                }
                else if (callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
                {
                    if (mSipAdapter.getSCallEventListener() != null)
                    {
                        mSipAdapter.getSCallEventListener().onSCallRelease(callId, CommonConstantEntry.CALL_END_EXISTED);
                    }
                    return false;
                }
                else if (callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
                {
                    if (mSipAdapter.getVsEventListener() != null)
                    {
                        mSipAdapter.getVsEventListener().onVsClosed(callId, CommonConstantEntry.CALL_END_EXISTED);
                    }
                    return false;
                }
            }

            final ExtendedCall call = new ExtendedCall(sip_provider, mSipAdapter.from_url, mSipAdapter.contact_url, mSipAdapter.username, mSipAdapter.realm,
                    mSipAdapter.passwd, this, null);
            call.setCalleeNumber(target_url);// ���к���
            call.setCallerNumber(mSipAdapter.username);// ���к���
            // call.setCallerFn(funNumber);// ���й�����
            call.setCallType(callType);// ��������
            call.changeStatus(CommonConstantEntry.CALL_STATE_OUTGOING);// ����״̬
            if (call.getCallType() == CommonConstantEntry.CALL_TYPE_GROUP || call.getCallType() == CommonConstantEntry.CALL_TYPE_BROADCAST)
            {// �����㲥
                call.setSpeakerOn(true);// ��¼��������
            }

            // --------------------------------���з������ܲ�����־��¼
            // Begin-------------------------------------------
            if (CommonConfigEntry.TEST_CALL)
            {
                PerfTestHelper.CALLPREF.setCallType(call.getCallType());
            }
            // --------------------------------���з������ܲ�����־��¼
            // End----------------------------------------------

            // in case of incomplete url (e.g. only 'user' is present), try to
            // complete it
            if (!target_url.contains("@"))
            {
                if (mSipAdapter.realm.equals(""))
                {
                    target_url = "&" + target_url;
                }
                target_url = target_url + "@" + realm; // modified
            }
            target_url = sip_provider.completeNameAddress(target_url).toString();

            // MMTel addition to define MMTel ICSI to be included in INVITE
            // (added by mandrajg)
            String icsi = null;

            if (mSipAdapter.mmtel)
            {
                icsi = "\"urn%3Aurn-7%3A3gpp-service.ims.icsi.mmtel\"";
            }

            call.setCallId(callId);

            // change start multi codecs
            SessionDescriptor local_sdp = createOffer(call);

            boolean callOut = false;
            if (mSipAdapter.no_offer)
            {
                callOut = call.call(target_url, null, null, null, null, priority, "", false);
            }
            else
            {
                callOut = call.call(target_url, null, null, local_sdp.toString(), icsi, priority, "", false);
            }

            if (callOut)
            {// ���гɹ�
                callCache.put(callId, call);
                Log.info(TAG, "call::callCache.size:" + callCache.size() + " callid:" + call.getCallId());

                // ֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
                if (call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO
                        || call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
                {
                    if (mSipAdapter.getConfEventListener() != null)
                    {
                        mSipAdapter.getConfEventListener().onConfCreateAck(callId);
                    }
                }
                else if (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
                {
                    if (mSipAdapter.getSCallEventListener() != null)
                    {
                        mSipAdapter.getSCallEventListener().onSCallOutgoingAck(callId, 1);
                    }
                }
            }
            else
            {// ����ʧ��
                Log.error(TAG, "call::call failed.");
                // ֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
                notifyReleaseEvent(call.getCallType(), callId, CommonConstantEntry.Q850_NOREASON);
            }
        }
        catch (Exception ex)
        {
            Log.exception(TAG, ex);
            return false;
        }
        return true;
    }

    /**
     * ����˵�� :����Ƿ��Ѿ����ڸú���
     * 
     * @param calleeNumber
     *            ���к���
     * 
     * @author fuluo
     * @Date 2014-3-26
     * 
     * @return true������
     */
    private boolean checkCallIsExist(String calleeNumber)
    {
        if (callCache.size() > 0)
        {
            for (ExtendedCall call : callCache.values())
            {
                String peerNum = call.isCallingPart() ? call.getCalleeNumber() : call.getCallerNumber();
                if (peerNum.equals(calleeNumber) || (!call.isCallingPart() && call.getCallerFn() != null && call.getCallerFn().equals(calleeNumber)))
                {// ����
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ����DTMF-INFO��Ϣ��ʽ
     * 
     * @param callid
     * @param c
     * @param duration
     * @throws Exception
     */
    public void info(String callid, char c, int duration) throws Exception
    {
        ExtendedCall call = callCache.get(callid);

        if (call == null)
        {
            Log.error(TAG + "info", "The call doesn't exist. callid=" + callid);
            return;
        }
        else
        {
            call.info(c, duration);
        }
    }

    /**
     * ת�������ͷ�ԭ��
     * 
     * @param releaseReason
     *            �ͷ�ԭ��
     * @return
     */
    private int convertReleaseReason(String releaseReason)
    {
        if (releaseReason == null || releaseReason.equals(""))
        {
            return CommonConstantEntry.Q850_NOREASON;
        }
        else if (releaseReason.equals(CommonConstantEntry.CALL_REASON_SEIZED))
        {// ����ռ
            return CommonConstantEntry.Q850_PREEMPTION;
        }
        else if (releaseReason.equals(CommonConstantEntry.CALL_REASON_SEIZEFAILED))
        {// ��ռʧ��
            return CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED;
        }
        else if (releaseReason.equals("" + CommonConstantEntry.SIP_CALL_DND))
        {// �����ģʽ
            return CommonConstantEntry.SIP_CALL_DND;
        }

        return CommonConstantEntry.Q850_NOREASON;
    }

    /**
     * ȡ����������
     * 
     * @param callId
     *            ���б��
     * @param releaseReason
     *            �ͷ�ԭ��
     * 
     * */
    public boolean cancelCall(String callId, String releaseReason)
    {
        Log.info(TAG, "cancelCall:: callID:" + callId + " releaseReason:" + releaseReason);

        boolean cancel = false;
        try
        {
            ExtendedCall call = callCache.get(callId);
            if (call == null)
            {
                return cancel;
            }

            // ֹͣ������������ȡ������ʱ����/������û����Ӧ����»�����û��ֹͣ
            ringback(false, callId);// ��ɾ������֮ǰֹͣ
            removeCallFromCache(callId);// ��ɾ���˺���

            int callStauts = call.getStatus();
            if (callStauts == CommonConstantEntry.CALL_STATE_OUTGOING || callStauts == CommonConstantEntry.CALL_STATE_RING_BACK)
            {// ����
                cancel = call.cancel(convertReleaseReason(releaseReason));
            }

            if (callStauts == CommonConstantEntry.CALL_STATE_IN_CALL)
            {// �Է����������������
                cancel = call.bye(convertReleaseReason(releaseReason));
            }
            else
            {
                changeCallStatus(call, CommonConstantEntry.CALL_STATE_IDLE, CommonConstantEntry.Q850_NOREASON);
            }
        }
        catch (Exception ex)
        {
            Log.exception("UserAgent.cancelCall", ex);
        }
        return cancel;
    }

    /**
     * �ر�ͨ���к���
     * 
     * @param callId
     *            ���б��
     * @param releaseReason
     *            �ͷ�ԭ��
     * 
     * */
    public boolean closeCall(String callId, String releaseReason)
    {
        Log.info(TAG, "closeCall:: callID:" + callId + " releaseReason:" + releaseReason);

        boolean close = false;
        try
        {
            ExtendedCall call = callCache.get(callId);
            Log.info(TAG, "closeCall:: callCache:" + callCache.size() + " call:" + call);
            if (call == null)
            {
                return close;
            }

            close = call.bye(convertReleaseReason(releaseReason));

            removeCallFromCache(callId);
            changeCallStatus(call, CommonConstantEntry.CALL_STATE_IDLE, CommonConstantEntry.Q850_NOREASON);
        }
        catch (Exception ex)
        {
            Log.exception("UserAgent.closeCall", ex);
        }
        return close;
    }

    /**
     * �ܾ�����
     * 
     * @param callId
     *            ���б��
     * @param releaseReason
     *            �ͷ�ԭ��
     * 
     * */
    public boolean refuseCall(String callId, String releaseReason)
    {
        Log.info(TAG, "refuseCall:: callID:" + callId + " releaseReason:" + releaseReason);

        boolean refuse = false;
        try
        {
            ExtendedCall call = callCache.get(callId);
            if (call == null)
            {
                return refuse;
            }
            refuse = call.refuse(convertReleaseReason(releaseReason));

            removeCallFromCache(callId);
            changeCallStatus(call, CommonConstantEntry.CALL_STATE_IDLE, CommonConstantEntry.Q850_NOREASON);
        }
        catch (Exception ex)
        {
            Log.exception("UserAgent.refuseCall", ex);
        }
        return refuse;
    }

    /**
     * Waits for an incoming call (acting as UAS).
     */
    public boolean listen()
    {
        Log.info(TAG, "listen::Waits for an incoming call");
        // no call or the listen call is in call.
        if (listening_call == null || !listening_call.statusIs(CommonConstantEntry.CALL_STATE_IDLE))
        {
            listening_call = new ExtendedCall(sip_provider, mSipAdapter.from_url, mSipAdapter.contact_url, mSipAdapter.username, mSipAdapter.realm,
                    mSipAdapter.passwd, this, null);

            listening_call.listen();
        }

        return true;
    }

    /**
     * Closes an ongoing, incoming, or pending call
     */
    public void hangup(String callID)
    {
        hangup(callID, CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * Closes an ongoing, incoming, or pending call
     */
    public boolean hangup(String callID, int releaseReason)
    {
        Log.info(TAG, "refuseCall:: callID:" + callID);

        boolean hangup = false;
        try
        {
            ExtendedCall call = callCache.get(callID);
            hangup = hangup(call, releaseReason);// ��Ӻ����ͷ�ԭ��
            changeCallStatus(call, CommonConstantEntry.CALL_STATE_IDLE, releaseReason);
        }
        catch (Exception ex)
        {
            Log.exception("UserAgent.hangup", ex);
        }
        return hangup;
    }

    /**
     * �˳����
     * 
     * @param callId
     *            ���б��
     */
    public boolean quitcall(String callId, String releaseReason)
    {
        Log.info(TAG, "quitcall:: callID:" + callId);

        boolean quit = false;
        try
        {
            ExtendedCall call = callCache.get(callId);
            if (call == null)
            {
                return quit;
            }
            // if (call.getCallType() == CommonConstantEntry.CALL_TYPE_GROUP &&
            // call.isPtt())
            // {// ���ڽ������л�Ȩ
            // call.pushInfo(callID, 1, "");// �ͷŻ�Ȩ
            // }
            quit = call.quitcall(convertReleaseReason(releaseReason));

            removeCallFromCache(callId);
            changeCallStatus(call, CommonConstantEntry.CALL_STATE_IDLE, convertReleaseReason(releaseReason));

            // // ��ӵ����������б�
            // addActiveGroup(call, releaseReason);
        }
        catch (Exception ex)
        {
            Log.exception("UserAgent.hangup", ex);
        }
        return quit;
    }

    /**
     * �Ҷϣ��ܾ�����������
     * 
     * @throws Exception
     */
    protected void hangup(ExtendedCall call) throws Exception
    {
        hangup(call, CommonConstantEntry.Q850_NOREASON);
    }

    /**
     * �Ҷϣ��ܾ�����������
     * 
     * @throws Exception
     */
    protected boolean hangup(ExtendedCall call, int releaseReason) throws Exception
    {
        boolean hangup = false;
        if (call != null)
        {
            hangup = call.hangup(releaseReason);
            removeCallFromCache(call.getCallId());
            changeCallStatus(call, CommonConstantEntry.CALL_STATE_IDLE, releaseReason);

            return hangup;
        }
        else
        {
            Log.error("hangup(ExtendedCall call)", "No Call Found to hangup.");
            return false;
        }
    }

    /**
     * close all the ongoing, incoming, or pending call.
     * 
     * @throws Exception
     */
    public void hangupAll() throws Exception
    {
        Log.info(TAG, "hangupAll");

        for (ExtendedCall call : callCache.values())
        {
            hangup(call);
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                Log.exception("UserAgent.hangup", e);
            }
        }

        // remove all the hangup call.
        removeAllCallFromCache();
    }

    /**
     * Accepts an incoming call
     * 
     * @param callId
     *            ���б��
     * @throws Exception
     */
    public boolean accept(String callId) throws Exception
    {
        Log.info(TAG, "accept:: callID:" + callId);

        ExtendedCall call = callCache.get(callId);
        if (call == null)
        {// û�к���
            Log.error(TAG, "accept:: call is null.");
            // FIXME:֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
//            if (mSipAdapter.getSCallEventListener() != null)
//            {
//                mSipAdapter.getSCallEventListener().onSCallRelease(callId, CommonConstantEntry.Q850_NOREASON);
//            }
            return false;
        }
        else
        {// ��ͨ��
            call.accept(call.getLocalSessionDescriptor());// ����
            call.checkIsReceiveAck();// ����Ƿ���յ�����ȷ��ACK
            return true;
        }
    }

    /**
     * �ı����״̬��֪ͨ�ӿ�
     */
    private void changeCallStatus(ExtendedCall call, int callStatus, int reason)
    {
        if (call == null)
        {
            return;
        }
        Log.info(TAG, "changeCallStatus::");
        call.changeStatus(callStatus);

        // ֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
        switch (callStatus)
        {
            case CommonConstantEntry.CALL_STATE_IDLE:
                notifyReleaseEvent(call.getCallType(), call.getCallId(), reason);
                break;

            default:
                break;
        }
    }

    private void notifyReleaseEvent(int callType, String callId, int reason)
    {
        Log.info(TAG, "notifyReleaseEvent:: callType:" + callType);
        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
        {
            if (mSipAdapter.getConfEventListener() != null)
            {
                mSipAdapter.getConfEventListener().onConfClose(callId);
            }
        }
        else if (callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
        {
            if (mSipAdapter.getSCallEventListener() != null)
            {
                Log.info(TAG, "notifyReleaseEvent:: reason:" + reason);
                mSipAdapter.getSCallEventListener().onSCallRelease(callId, reason);
            }
        }
        else if (callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
        {
            Log.info(TAG, "notifyReleaseEvent:: reason:" + reason);
            if (mSipAdapter.getVsEventListener() != null)
            {
                mSipAdapter.getVsEventListener().onVsClosed(callId, reason);
            }
        }
    }

    /**
     * ��ȡ���н�ɫ
     * 
     * @param call
     *            ���ж���
     */
    private int getCallRole(ExtendedCall call)
    {
        // ���н�ɫ
        int callRole = CommonConstantEntry.CALL_ROLE_MEMBER;// Ĭ�ϣ�������
        if (call.isCallingPart())
        {// ������
            callRole = CommonConstantEntry.CALL_ROLE_CREATE;// ������
        }
        return callRole;
    }

//    /**
//     * �ı����״̬��֪ͨ�ӿ�
//     */
//    private void changeCallStatus(ExtendedCall call, int callStatus, int code, int reasonValue)
//    {
//        if (call == null)
//        {
//            return;
//        }
//
//        try
//        {
//            String reason = "";
//
//            if (reasonValue != CommonConstantEntry.Q850_NOREASON)
//            {
//                // ��codeΪ486��BYE,Cancelʱ����Ϣ���к����ͷ�ԭ��ʱ���в���
//                if (reasonValue == CommonConstantEntry.Q850_PREEMPTION)
//                {// ����ռ
//                    reason = CommonConstantEntry.CALL_FAILED_PREEMPTED;
//                }
//                else if (reasonValue == CommonConstantEntry.Q850_PRECEDENCE_CALL_BLOCKED)
//                {// ��ռʧ��
//                    reason = CommonConstantEntry.CALL_FAILED_PREEMPTFAILED;
//                }
//                else if (reasonValue == CommonConstantEntry.Q850_CALL_REJECTED)
//                {// �ܾ����������ܾ������������
//                    reason = CommonConstantEntry.CALL_FAILED_ACTIVEGROUP;
//                }
//                else if (reasonValue == CommonConstantEntry.SIP_CELLID_NOTEXIST)
//                {// û��С����
//                    reason = CommonConstantEntry.CALL_FAILED_CELLID_NOTEXIST;
//                }
//                else if (reasonValue == CommonConstantEntry.SIP_FN_NOTEXIST)
//                {// �����й��ܺ�
//                    reason = CommonConstantEntry.CALL_FAILED_FN_NOTEXIST;
//                }
//                else if (reasonValue == CommonConstantEntry.SIP_FN_FORBID)
//                {// ���й��ܺ���Ȩ��
//                    reason = CommonConstantEntry.CALL_FAILED_FN_FORBID;
//                }
//                else if (reasonValue == CommonConstantEntry.SIP_GROUP_NOTEXIST)
//                {// û�����ø�Ⱥ��
//                    reason = CommonConstantEntry.CALL_FAILED_GROUP_NOTEXIST;
//                }
//            }
//            else if (code == 408)
//            {// ������Ӧ��ʱ
//                reason = CommonConstantEntry.CALL_FAILED_TIMEOUT;
//            }
//            else if (code == 406)
//            {// �ܾ�
//                reason = CommonConstantEntry.CALL_FAILED_REFUSE;
//            }
//            else if (code == 486)
//            {// ����æ
//                reason = CommonConstantEntry.CALL_FAILED_BUSY;
//            }
//            else if (code == 480)
//            {// ������
//                reason = CommonConstantEntry.CALL_FAILED_OFFLINE;
//            }
//            else if (code == 404)
//            {// �պ�
//                reason = CommonConstantEntry.CALL_FAILED_NOACCOUNT;
//            }
//            else if (code == 403)
//            {// ������Ȩ��
//                reason = CommonConstantEntry.CALL_FAILED_FORBID;
//            }
//            else if (code == 441 || code == 442 || code == 444 || code == 445 || code == 446 || code == 447 || code == 448 || code == 449 || code == 450)
//            {// ���������ϣ��޷���ͨ
//                reason = CommonConstantEntry.CALL_FAILED_UNREACHABLE;
//            }
//            else if ((code >= 400 || code < 700) && code != 487)
//            {// �������������ϣ��޷���ͨ�����˵�487��cancle�ɹ���
//                reason = CommonConstantEntry.CALL_FAILED_UNREACHABLE;
//            }
//
//            if (call.getStatus() == CommonConstantEntry.CALL_STATE_RING_BACK)
//            {// ����״̬
//                ringback(false, call.getCallId());// ֹͣ����
//            }
//            call.changeStatus(callStatus);
////            int callRole = getCallRole(call);// ���н�ɫ
//        }
//        catch (Exception e)
//        {
//            Log.exception(TAG, e);
//        }
//    }

    /**
     * Redirects an incoming call
     * 
     * @throws Exception
     */
    public void redirect(String callid, String redirection) throws Exception
    {
        ExtendedCall call = callCache.get(callid);

        if (call != null)
        {
            call.redirect(redirection);
        }
    }

    /**
     * �������Ƿ񳬹�����
     */
    protected boolean checkCallCount()
    {
        synchronized (callCache)
        {
            return callCache.size() > CommonConfigEntry.MAX_IN_CALL_COUNT;
        }
    }

    /**
     * ����˵�� : �ж�ͨ���Ƿ��Ѵ���
     * 
     * @param callerNum
     * @param calleeNum
     * @param callerFn
     * @return
     * @return boolean
     * @author hubin
     * @Date 2014-11-25
     */
    private boolean isCallExist(String callerNum, String calleeNum, String callerFn)
    {
        if (callerNum == null || calleeNum == null)
        {
            return false;
        }
        for (ExtendedCall call : callCache.values())
        {
            if ((callerNum.equals(call.getCallerNumber()) && calleeNum.equals(call.getCalleeNumber()))
                    || (callerNum.equals(call.getCalleeNumber()) && calleeNum.equals(call.getCallerNumber())))
            {
                return true;
            }
            else if (callerFn != null
                    && ((callerFn.equals(call.getCallerNumber()) && calleeNum.equals(call.getCalleeNumber())) || (callerFn.equals(call.getCalleeNumber()) && calleeNum
                            .equals(call.getCallerNumber()))))
            {
                return true;
            }

        }
        return false;
    }

    public boolean ring(String callId)
    {
        try
        {
            Log.info(TAG, "-----------send 180........");
            ExtendedCall call = callCache.get(callId);
            if (call == null)
            {
                return false;
            }
            String sdp = call.getRemoteSessionDescriptor();
            // SDP
            SessionDescriptor local_sdp;
            SessionDescriptor remote_sdp = null;
            if (sdp == null)
            {
                local_sdp = createOffer(call);
            }
            else
            {
                remote_sdp = new SessionDescriptor(sdp);
                local_sdp = createAnswer(call, remote_sdp);
            }

            call.ring(local_sdp.toString());
            return true;
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
    }

    // ********************** Call callback functions **********************

    /**
     * Callback function called when arriving a new INVITE method (incoming
     * call)
     */
    public void onCallIncoming(Call call, NameAddress callee, NameAddress caller, final String sdp, Message invite)
    {
        try
        {
            Log.info(TAG, "onCallIncoming:: caller:" + caller + ", callee:" + callee + " host:" + mSipAdapter.realm);
            if (checkCallCount())
            {// �����и���������
                Log.error(TAG, "onCallIncoming::Exceeds the max count: " + CommonConfigEntry.MAX_IN_CALL_COUNT);
                call.busy();
                call.listen();
                return;
            }

            // the call is listening call and becomes IN-CALL, refuse the
            // incoming call and make sure listening the call.
            if (call.isOnCall())
            {
                call.busy();
                call.listen();
                return;
            }

            // the function number.
            String fn = null;
            UserToUserHeader header = invite.getUserToUserHeader();
            if (header != null)
            {
                fn = header.getCode();
                call.setCallerFn(fn);// ���й�����
            }
            // �ж��Ƿ�ͨ���Ѵ��ڣ���Ҫ����Ѵ���һ·���У�ʹ�öԶ˹�������б��н���������ʹ�öԶ˺�����г����ϲ��ѹ��
            if (isCallExist(caller.getNumber(), callee.getNumber(), fn))
            {
                Log.error(TAG, "onCallIncoming::call exist, caller:" + caller.getNumber() + " callee:" + callee.getNumber());
                call.busy();
                call.listen();
                return;
            }

            ExtendedCall extendedCall = (ExtendedCall) call;
            String callId = extendedCall.getCallId();
            // �ж�ͨ���Ƿ���ڣ������ڶ�Call�����changeStatus��������֮ǰ����Ȼ���ͨ���������Ҳ�ᴥ��������ʱ�Ĵ���
            if (callCache.get(callId) != null)
            {// �����Ѿ�����
                Log.info(TAG, "onCallIncoming call already exist::callid:" + callId);
                call.listen();
                return;
            }

            extendedCall.setCalleeNumber(callee.getNumber());// ���к���
            extendedCall.setCallerNumber(caller.getNumber());// ���к���
            extendedCall.changeStatus(CommonConstantEntry.CALL_STATE_INCOMING);
            // make sure there is one call listening the Invite message from
            // Server.
            listen();

            callCache.put(callId, extendedCall);
//            callIdMap.put(sessionId, callId);
            Log.info(TAG, "onCallIncoming::callid:" + callId + " size:" + callCache.size() + " call:" + callCache.get(callId));
            // SDP
            SessionDescriptor local_sdp;
            SessionDescriptor remote_sdp = null;
            if (sdp == null)
            {
                local_sdp = createOffer(extendedCall);
            }
            else
            {
                remote_sdp = new SessionDescriptor(sdp);
                try
                {
                    local_sdp = createAnswer(extendedCall, remote_sdp);
                }
                catch (Exception e)
                {
                    changeCallStatus(extendedCall, CommonConstantEntry.CALL_STATE_IDLE, CommonConstantEntry.Q850_NOREASON);
                    // only known exception is no codec
                    Log.exception(TAG + ".onCallIncoming", e);
                    return;
                }
            }

            CallTypeHeader callTypeHeader = invite.getCallTypeHeader();
            if (callTypeHeader != null)
            {
                if (callTypeHeader.getCallType().equals("conference"))
                    extendedCall.setConferenceMember(true);// ��ʶ�����Ա
                else
                    extendedCall.setConferenceMember(false);
            }
            boolean video = false;
            if (local_sdp.getMediaDescriptor(VIDEO) != null)
                video = true;
            int callType = SdkUtil.getCallType(extendedCall.getCallerNumber(), video);// ��������
            extendedCall.setCallType(callType);// ��������
            extendedCall.setCallingPart(false);// ����
            setCallPart(extendedCall, invite);// �����Ƿ��Ƿ�����
            extendedCall.setCreaterFn(getCreater(invite));// ���÷����߹�����
            if (extendedCall.getCallType() == CommonConstantEntry.CALL_TYPE_GROUP || extendedCall.getCallType() == CommonConstantEntry.CALL_TYPE_BROADCAST)
            {// �����㲥
                extendedCall.setSpeakerOn(true);// ��¼��������
            }

            // ��ȡcontact�ֶ��е��������
            String activeNumber = invite.getContactHeader().getNameAddress().getNumber();
            Log.info(TAG, "UserAgent.onCallIncoming::activeNumber:" + activeNumber);
            extendedCall.setActiveNumber(activeNumber);

            if (fn == null)
            {// ���й��ܺ���Ϊ��
                fn = extendedCall.getCreaterFn();
            }

            // --------------------------------���з������ܲ�����־��¼
            // Begin-------------------------------------------
            if (CommonConfigEntry.TEST_CALL)
            {
                PerfTestHelper.CALLPREF.setCalleeNumber(call.getCalleeNumber());
                PerfTestHelper.CALLPREF.setCallerNumber(call.getCallerNumber());
                PerfTestHelper.CALLPREF.setCallType(callType);
                PerfTestHelper.CALLPREF.setSend180(System.currentTimeMillis());
            }
            // --------------------------------���з������ܲ�����־��¼
            // End----------------------------------------------

//            int callRole = getCallRole(extendedCall);// ���н�ɫ
            // ֪ͨ�ӿڣ�֪ͨ���µĺ��롱
            if (mSipAdapter.getSCallEventListener() != null)
            {
                mSipAdapter
                        .getSCallEventListener()
                        .onSCallIncoming(
                                callId,
                                call.getPriority(),
                                call.getCallerNumber(),
                                null,
                                fn,
                                call.getCalleeNumber(),
                                1,
                                extendedCall.isConferenceMember(),
                                (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO));
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function called when arriving a new Re-INVITE method
     * (re-inviting/call modify)
     */
    public void onCallModifying(Call call, String sdp, Message invite)
    {
        try
        {
            ExtendedCall extendedCall = (ExtendedCall) call;
            Log.info(TAG, "onCallModifying callee:" + call.getCalleeNumber() + " host:" + mSipAdapter.realm);
            int callRole = getCallRole(extendedCall);// ���н�ɫ

            if (sdp.contains(SENDONLY) || sdp.contains(INACTIVE))
            {// ��������
                if (sdp.contains(SENDONLY))
                {
                    super.onCallModifying(call, sdp, invite, new AttributeField(RECVONLY));

                }
                else if (sdp.contains(INACTIVE))
                {
                    super.onCallModifying(call, sdp, invite, new AttributeField(INACTIVE));
                }

                extendedCall.setHolded(true);
//                setMute(extendedCall.getCallId(), true);// ֹͣ˵�����򿪾���

                // ֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallRemoteHold(extendedCall.getCallId());
                }
            }
            else if (sdp.contains(SENDRECV))
            {// �ָ�����
             // �ı��ͨ��״̬
                extendedCall.setHolded(false);
                if (call.getStatus() != CommonConstantEntry.CALL_STATE_HOLD)
                {// û�б���
                    super.onCallModifying(call, sdp, invite, new AttributeField(SENDRECV));
//                    setMute(extendedCall.getCallId(), false);// ��ʼ˵�����رվ���
                    call.changeStatus(CommonConstantEntry.CALL_STATE_IN_CALL);
                }
                else
                {// ����+������ ��һ�λָ� �� sendonly
                    super.onCallModifying(call, sdp, invite, new AttributeField(SENDONLY));
                }
                // ֪ͨ�ӿڣ�֪ͨ������״̬�ı䡱
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallRemoteRetrieve(extendedCall.getCallId());
                }
            }
            else
            {
                super.onCallModifying(call, sdp, invite);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function that may be overloaded (extended). Called when arriving
     * a 180 Ringing or a 183 Session progress with SDP
     */
    public void onCallRinging(Call call, Message resp)
    {
        Log.info(TAG, "onCallRinging");

        // --------------------------------���з������ܲ�����־��¼
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setReceive180(System.currentTimeMillis());
        }

        // --------------------------------���з������ܲ�����־��¼
        // End----------------------------------------------

        ExtendedCall extendedCall = (ExtendedCall) call;
        changeCallStatus(extendedCall, CommonConstantEntry.CALL_STATE_RING_BACK, CommonConstantEntry.Q850_NOREASON);
        SessionDescriptor local_sdp = new SessionDescriptor(call.getLocalSessionDescriptor());
        String remote_session = call.getRemoteSessionDescriptor();
        Log.info(TAG, "onCallRinging::remote_sdp:" + remote_session);
        if (remote_session == null || remote_session.length() == 0)
        {// 180��Ϣʱ�����ű�������
            if (!call.statusIs(CommonConstantEntry.CALL_STATE_IDLE))
            {// TODO:����������Ϣ����֮ǰ�Ҷϵ����
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallRingback(extendedCall.getCallId(), extendedCall.getCalleeNumber(), 1);
                }
                ringback(true, extendedCall.getCallId());
            }
        }
        else
        {// 183��Ϣʱ�����ŷ���������
            if (!mSipAdapter.no_offer)
            {
            }
        }
    }

    /**
     * Callback function called when arriving a 2xx (call accepted)
     */
    public void onCallAccepted(Call call, String sdp, Message resp)
    {
        try
        {
            ExtendedCall extendedCall = (ExtendedCall) call;
            String callId = extendedCall.getCallId();

            Log.info(TAG, "onCallAccepted::callid:" + callId + " priority:" + call.getPriority() + " host:" + mSipAdapter.realm);

            if (callId == null || callId.equals(""))// δȡ������ʱ��������
            {
                call.bye(convertReleaseReason(CommonConstantEntry.CALL_REASON_HANDLE));// ����
                return;
            }

            if (call.getStatus() == CommonConstantEntry.CALL_STATE_IDLE)
            {// ȡ���Ѿ���״̬��Ϊ���������Ǻ����Ѿ��ɹ�����
                Log.info(TAG, "onCallAccepted::call.bye:: callid:" + extendedCall.getCallId() + " status:" + call.getStatus());
                call.changeStatus(CommonConstantEntry.CALL_STATE_IN_CALL);
                call.bye(convertReleaseReason(CommonConstantEntry.CALL_REASON_HANDLE));// ����
            }

            extendedCall.setCallingPart(true);// ���
            setCallPart(extendedCall, resp);// �����Ƿ��Ƿ�����
            extendedCall.setCreaterFn(getCreater(resp));// ���÷����߹�����
            // ��ȡcontact�ֶ��е��������
            String activeNumber = resp.getContactHeader().getNameAddress().getNumber();
            Log.info(TAG, "UserAgent.onCallAccepted::activeNumber:" + activeNumber);
            extendedCall.setActiveNumber(activeNumber);

            if (call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
            {
                if (mSipAdapter.getConfEventListener() != null)
                {
                    mSipAdapter.getConfEventListener().onConfCreateConnect(callId, extendedCall.getPriority());
                }
            }
            else if (call.getCallType() == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
            {
                if (mSipAdapter.getVsEventListener() != null)
                {
                    mSipAdapter.getVsEventListener().onVsOpenAck(callId, extendedCall.getPriority());
                }
            }
            else if (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
            {
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallConnect(callId, extendedCall.getPriority());
                }
            }
            changeCallStatus(extendedCall, CommonConstantEntry.CALL_STATE_IN_CALL, CommonConstantEntry.Q850_NOREASON);
            SessionDescriptor local_sdp = null;
            SessionDescriptor remote_sdp = new SessionDescriptor(sdp);
            if (mSipAdapter.no_offer)
            {
                // answer with the local sdp
                local_sdp = createAnswer(extendedCall, remote_sdp);
                call.ackWithAnswer(local_sdp.toString());
            }
            else
            {
                String local_session = call.getLocalSessionDescriptor();
                local_sdp = new SessionDescriptor(local_session);
                if (call.getCallType() != CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
                {
                    extendedCall.setLocalAudioPort(local_sdp.getMediaDescriptor(AUDIO).getMedia().getPort());
                    extendedCall.setRemoteAudioPort(remote_sdp.getMediaDescriptor(AUDIO).getMedia().getPort());
                }
                extendedCall.setRemoteMediaAddress(remote_sdp.getConnection().getAddress());
                if (remote_sdp.getMediaDescriptor(VIDEO) != null)
                {// ����Ƶ
                    extendedCall.setLocalVideoPort(local_sdp.getMediaDescriptor(VIDEO).getMedia().getPort());
                    extendedCall.setRemoteVideoPort(remote_sdp.getMediaDescriptor(VIDEO).getMedia().getPort());
                }
            }
            startMediaService(local_sdp, extendedCall);

            // if the call is Forward call.
            if (Forward_original_cache.containsKey(call))
            {// ����ǰת
             // notify Forwardor the result.
                ExtendedCall original = Forward_original_cache.get(call);
                StatusLine status_line = resp.getStatusLine();
                int code = status_line.getCode();
                String reason = status_line.getReason();
                original.notify(code, reason);// ����ǰת��Ϣ
                callCache.put(original.getCallId(), extendedCall);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * ����ý�崫��
     */
    private void startMediaService(SessionDescriptor local_sdp, ExtendedCall call)
    {
        Log.info(TAG, "startMediaService::LocalAudioPort" + call.getLocalAudioPort() + " RemoteAudioPort:" + call.getRemoteAudioPort() + " LocalVideoPort:"
                + call.getLocalVideoPort() + " RemoteVideoPort:" + call.getRemoteVideoPort() + " RemoteMediaAddress:" + call.getRemoteMediaAddress());
        if (call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || call.getCallType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
        {
            if (mSipAdapter.getConfEventListener() != null)
            {
                Log.info(TAG, "startMediaService:: CALL_TYPE_CONFERENCE");
                mSipAdapter.getConfEventListener().onConfMediaInfo(call.getCallId(), call.getLocalAudioPort(), call.getRemoteAudioPort(),
                        call.getLocalVideoPort(), call.getRemoteVideoPort(), call.getRemoteMediaAddress(), Codecs.getCodec(local_sdp));
            }
        }
        else if (call.getCallType() == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
        {
            if (mSipAdapter.getVsEventListener() != null)
            {
                Log.info(TAG, "startMediaService:: CALL_TYPE_VIDEO_SURVEILLANCE");
                mSipAdapter.getVsEventListener().onVsMediaInfo(call.getCallId(), call.getLocalVideoPort(), call.getRemoteVideoPort(),
                        call.getRemoteMediaAddress());
            }
        }
        else if (call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO || call.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
        {
            if (mSipAdapter.getSCallEventListener() != null)
            {
                Log.info(TAG, "startMediaService:: CALL_TYPE_SINGLE");
                if (call.isConferenceMember())
                {
                    mSipAdapter.getSCallEventListener().onSCallMediaInfo(call.getCallId(), true, call.getLocalAudioPort(), call.getRemoteAudioPort(),
                            call.getLocalVideoPort(), call.getRemoteVideoPort(), call.getRemoteMediaAddress(), Codecs.getCodec(local_sdp));
                }
                else
                {
                    mSipAdapter.getSCallEventListener().onSCallMediaInfo(call.getCallId(), false, call.getLocalAudioPort(), call.getRemoteAudioPort(),
                            call.getLocalVideoPort(), call.getRemoteVideoPort(), call.getRemoteMediaAddress(), Codecs.getCodec(local_sdp));
                }
            }
        }
    }

    /**
     * ����˵�� :��ȡ��������߹��ܺ���
     * 
     * @author fuluo
     * @Date 2014-3-24
     * 
     * @return ��������߹��ܺ���
     */
    private String getCreater(Message resp)
    {
        String createrStr = "creater";
        String params[] = null;
        String creaters[] = null;

        MultipleHeader contacts = resp.getContacts();
        for (String contact : contacts.getValues())
        {// ����contact
            if (contact.indexOf(createrStr) > -1)
            {// ����creater
                params = contact.split(";");
                for (String param : params)
                {// ��������
                    if (param.indexOf(createrStr) > -1)
                    {// ����creater
                        creaters = param.split("=");
                        if (creaters.length == 2)
                        {// ���ڷ�����
                            return creaters[1];
                        }
                    }
                }
            }
        }

        return "";
    }

    /**
     * ���ú����Ƿ��Ƿ�����
     */
    private void setCallPart(ExtendedCall call, Message resp)
    {
        Header callPartHeader = resp.getHeader(SipHeaders.Extra_Methord);
        if (callPartHeader != null)
        {
            String callPart = callPartHeader.getValue();
            if (callPart != null && callPart.equals("join"))
            {// Extra-Methord: xxx,(xxx=join��ʾ���룬xxx=create��ʾ����)
                call.setCallingPart(false);
            }
            else
            {
                call.setCallingPart(true);
            }
        }
    }

    /**
     * Callback function called when arriving an ACK method (call confirmed)
     */
    public void onCallConfirmed(Call call, String sdp, Message ack)
    {
        try
        {
            Log.info(TAG, "onCallConfirmed:: callee:" + call.getCalleeNumber() + " host:" + mSipAdapter.realm);

            ExtendedCall extendedCall = (ExtendedCall) call;
            if (call.getStatus() == CommonConstantEntry.CALL_STATE_INCOMING)
            {// ��������ȷ��
                call.setRecieveAck();// ȷ���Ѿ����յ�����ȷ��ACK��Ϣ
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallConnectAck(extendedCall.getCallId());
                }

                // --------------------------------���з������ܲ�����־��¼
                // Begin-------------------------------------------
                if (CommonConfigEntry.TEST_CALL)
                {
                    PerfTestHelper.CALLPREF.setReceiveAck(System.currentTimeMillis());
                }

                // --------------------------------���з������ܲ�����־��¼
                // End----------------------------------------------

                changeCallStatus(extendedCall, CommonConstantEntry.CALL_STATE_IN_CALL, CommonConstantEntry.Q850_NOREASON);
                startMediaService(new SessionDescriptor(call.getLocalSessionDescriptor()), extendedCall);
            }
            else if (call.getStatus() == CommonConstantEntry.CALL_STATE_IDLE)
            {// �ܾ��Ѿ���״̬��Ϊ���������Ǻ����Ѿ��ɹ�����
                Log.info(TAG, "onCallConfirmed::call.bye:: callid:" + extendedCall.getCallId() + " status:" + call.getStatus());
                call.changeStatus(CommonConstantEntry.CALL_STATE_IN_CALL);
                call.bye(convertReleaseReason(CommonConstantEntry.CALL_REASON_HANDLE));// ����
            }

            if (mSipAdapter.hangup_time > 0)
            {
                automaticHangup((ExtendedCall) call, mSipAdapter.hangup_time);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * ChenGang 20131216
     * ���ֺͻָ��ӵ����ر���֮��Ž���״̬�ı䡣<br>
     * Callback function called when arriving a 2xx (re-invite/modify accepted)
     */
    public void onCallReInviteAccepted(Call callValue, String sdp, Message resp)
    {
        try
        {
            ExtendedCall call = (ExtendedCall) callValue;
            String callId = call.getCallId();
            Log.info(TAG, "onCallReInviteAccepted:: callee=" + call.getCalleeNumber() + " curr status:" + call.getStatus() + " host:" + mSipAdapter.realm);

            if (sdp.contains(RECVONLY) || sdp.contains(INACTIVE))
            {// ������Ӧ
             // ֪ͨ�ӿڣ�֪ͨ�����б��ֵĲ��������
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallHoldAck(callId);
                }
                if (call.statusIs(CommonConstantEntry.CALL_STATE_IN_CALL))
                {
//                    setMute(call.getCallId(), true);// ֹͣ˵�����򿪾���
                    changeCallStatus(call, CommonConstantEntry.CALL_STATE_HOLD, CommonConstantEntry.Q850_NOREASON);
                }
            }
            else if (sdp.contains(SENDONLY) || sdp.contains(SENDRECV))
            {// �ָ���Ӧ
             // ֪ͨ�ӿڣ�֪ͨ�����лָ��Ĳ��������
                if (mSipAdapter.getSCallEventListener() != null)
                {
                    mSipAdapter.getSCallEventListener().onSCallRetrieveAck(callId);
                }
                if (call.statusIs(CommonConstantEntry.CALL_STATE_HOLD))
                {
                    if (!call.isHolded())
                    {// û�б�����
//                        setMute(call.getCallId(), false);// ��ʼ˵�����رվ���
                        changeCallStatus(call, CommonConstantEntry.CALL_STATE_IN_CALL, CommonConstantEntry.Q850_NOREASON);
                    }
                    else
                    {
                        call.changeStatus(CommonConstantEntry.CALL_STATE_IN_CALL);
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function called when arriving a 4xx (re-invite/modify failure)
     */
    public void onCallReInviteRefused(Call call, String reason, Message resp)
    {
        try
        {
            Log.info("UserAgent.onCallReInviteRefused", "reason" + reason + " host:" + mSipAdapter.realm);

            if (call.getStatus() == CommonConstantEntry.CALL_STATE_IN_CALL)
            {// ������Ӧ
            }
            else if (call.getStatus() == CommonConstantEntry.CALL_STATE_HOLD || ((ExtendedCall) call).isHolded())
            {// �ָ���Ӧ
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function called when arriving a 4xx (call failure)
     */
    public void onCallRefused(Call call, String reason, Message resp)
    {
        try
        {
            Log.info(TAG, "onCallRefused::reason:" + reason);
            dealCallClose(call, resp);
            if (Forward_original_cache.containsKey(call))
            {
                StatusLine status_line = resp.getStatusLine();
                int code = status_line.getCode();
                ExtendedCall original = Forward_original_cache.get(call);
                original.notify(code, reason);
                Forward_original_cache.remove(call);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function called when arriving a 3xx (call redirection)
     */
    public void onCallRedirection(Call call, String reason, Vector<String> contact_list, Message resp)
    {
        Log.info(TAG, "onCallRedirection:: reason:" + reason);
        try
        {
            call.call(((ExtendedCall) call).getCallId(), contact_list.elementAt(0));
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function that may be overloaded (extended). Called when arriving
     * a CANCEL request
     */
    public void onCallCanceling(Call call, Message cancel)
    {
        Log.info(TAG, "onCallClosing:: callee:" + call.getCalleeNumber() + " host:" + mSipAdapter.realm);
        dealCallClose(call, cancel);
    }

    /**
     * Callback function called when arriving a BYE request
     */
    public void onCallClosing(Call call, Message bye)
    {
        ExtendedCall extendedCall = (ExtendedCall) call;
        Log.info(TAG, "onCallClosing:: callid:" + extendedCall.getCallId() + " host:" + mSipAdapter.realm);

        // --------------------------------���з������ܲ�����־��¼
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setReceiveBye(System.currentTimeMillis());
            if (extendedCall.isCallingPart())
            {// ����
                PerfTestHelper.logCallerPerf();
            }
            else
            {// ����
                PerfTestHelper.logCalleePerf();
            }
        }
        // --------------------------------���з������ܲ�����־��¼
        // End----------------------------------------------

        dealCallClose(call, bye);
        String callid = extendedCall.getCallId();

        ExtendedCall Forward = Forward_original_cache.get(callid);
        if (Forward != null)
        {
            callCache.put(callid, Forward);
        }
    }

    /**
     * Callback function called when arriving a response after a BYE request
     * (call closed)
     */
    public void onCallClosed(Call call, Message resp)
    {
        ExtendedCall extendedCall = (ExtendedCall) call;
        Log.info(TAG, "onCallClosed:: callid:" + extendedCall.getCallId() + " host:" + mSipAdapter.realm);

        // --------------------------------���з������ܲ�����־��¼
        // Begin-------------------------------------------
        if (CommonConfigEntry.TEST_CALL)
        {
            PerfTestHelper.CALLPREF.setReceiveBye200Ok(System.currentTimeMillis());
            if (extendedCall.isCallingPart())
            {// ����
                PerfTestHelper.logCallerPerf();
            }
            else
            {// ����
                PerfTestHelper.logCalleePerf();
            }
        }
        // --------------------------------���з������ܲ�����־��¼
        // End----------------------------------------------
        dealCallClose(call, resp);
    }

    /**
     * ����������ص����н���
     */
    private void dealCallClose(Call call, Message msg)
    {
        ExtendedCall extendedCall = (ExtendedCall) call;
        String callid = extendedCall.getCallId();
        if (callCache.get(callid) != null)
        {// ������δ�ͷ�
         // ��Ӻ����ͷ�ԭ���ж�
//                int reasonValue = CommonConstantEntry.Q850_NOREASON;
            int reasonValue = CommonConstantEntry.Q850_NOREASON;
            try
            {
                if (msg.getHeader(SipHeaders.Reason) != null)
                {
                    int offset = msg.getHeader(SipHeaders.Reason).getValue().indexOf("=");
                    reasonValue = Integer.valueOf(msg.getHeader(SipHeaders.Reason).getValue().substring(offset + 1, offset + 4));
                }
                else if (msg.getStatusLine() != null)
                {
                    reasonValue = msg.getStatusLine().getCode();
                }
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
            changeCallStatus(extendedCall, CommonConstantEntry.CALL_STATE_IDLE, reasonValue);

            removeCallFromCache(callid);// ����˺���
        }
    }

    /**
     * Callback function called when the invite expires
     */
    public void onCallTimeout(final Call call)
    {
        onCallTimeout(call, CommonConstantEntry.CALL_FAILED_TIMEOUT);
    }

    public void onCallTimeout(Call call, int reasonCode)
    {
        try
        {
            ExtendedCall curCall = (ExtendedCall) call;
            if (getCallCache().get(curCall.getCallId()) == null)
            {
                return;
            }
            Log.error(TAG, "onCallTimeout:callid:" + curCall.getCallId() + " reasoncode:" + reasonCode);
            ringback(false, curCall.getCallId());
            removeCallFromCache(curCall.getCallId());
            changeCallStatus(curCall, CommonConstantEntry.CALL_STATE_IDLE, reasonCode);

            // the call is Forward call
            if (Forward_original_cache.containsKey(call))
            {
                ExtendedCall original = Forward_original_cache.get(curCall);

                // notify Forwardor the result.
                if (original != null)
                {
                    int code = 408;
                    String failReason = "Request Timeout";
                    original.notify(code, failReason);
                    Forward_original_cache.remove(call);
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * ���ƻ���
     * 
     * @param ringback
     *            �Ƿ����
     * @param callid
     */
    public void ringback(boolean ringback, String callid)
    {
        Log.info(TAG, "ringback::ringback:" + ringback + " callid:" + callid);
    }

    // ****************** ExtendedCall callback functions ******************

    /**
     * Callback function called when arriving a new REFER method (Forward
     * request)
     */
    public void onCallForward(ExtendedCall call, NameAddress refer_to, NameAddress refered_by, Message refer)
    {
        Log.info(TAG, "onCallForward:: Forward to " + refer_to.toString());

        try
        {
            call.acceptForward();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        // Added by liuyh, 12-5-31,
        ExtendedCall Forward = new ExtendedCall(sip_provider, mSipAdapter.from_url, mSipAdapter.contact_url, this);
        // replace the original call by Forward call.
        String callid = call.getCallId();
        callCache.put(callid, Forward);
        try
        {
            Forward.call(call.getCallId(), refer_to.toString(), call.getLocalSessionDescriptor(), null);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function called when a call Forward is accepted.
     */
    public void onCallForwardAccepted(ExtendedCall call, Message resp)
    {
        Log.info(TAG, "onCallForwardAccepted");
    }

    /**
     * Callback function called when a call Forward is refused.
     */
    public void onCallForwardRefused(ExtendedCall call, String reason, Message resp)
    {
        Log.info(TAG, "onCallForwardRefused");
    }

    /**
     * Callback function called when a call Forward is successfully completed
     */
    public void onCallForwardSuccess(ExtendedCall call, Message notify)
    {
        Log.info(TAG, "onCallForwardSuccess");

        try
        {
            call.hangup();
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    /**
     * Callback function called when a call Forward is NOT sucessfully completed
     */
    public void onCallForwardFailure(ExtendedCall call, String reason, Message notify)
    {
        Log.error(TAG, "onCallForwardFailure");
    }

    /**
     * ���ֺ���
     * 
     * @param caller
     *            ����
     */
    public boolean holdCall(String callID)
    {
        try
        {
            ExtendedCall call = callCache.get(callID);

            if (call == null)
            {
                Log.error(TAG, "_holdCall:: call is null.");
                return false;
            }
            Log.info(TAG, "holdCall :: callID=" + callID + " status=" + call.getStatus());
            if (call.statusIs(CommonConstantEntry.CALL_STATE_IN_CALL))
            {
                if (call.isHolded())// ��ʱ�ѱ����֣��ٷ��𱣳֣�Ϊinactive
                    reInvite(call, "0.0.0.0", 0, new AttributeField(INACTIVE));
                else
                    reInvite(call, "0.0.0.0", 0, new AttributeField(SENDONLY));
                Log.info("UserAgent._holdCall", "reInvite");
                return true;
            }
            else if (call.statusIs(CommonConstantEntry.CALL_STATE_HOLD))
            {
                changeCallStatus(call, CommonConstantEntry.CALL_STATE_HOLD, CommonConstantEntry.Q850_NOREASON);
                return true;
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        return false;
    }

    /**
     * activate the call identified by call-info, and make sure all the others
     * call is hold.
     * 
     * @param callID
     *            the call-id about the call.
     */
    public boolean retrieveCall(String callID)
    {
        try
        {
            Log.info(TAG, "retrieveCall:: callID=" + callID);
            ExtendedCall call = callCache.get(callID);

            if (call == null)
            {
                Log.error(TAG, "_retrieveCall:: call is null.");
                return false;
            }

            if (call.statusIs(CommonConstantEntry.CALL_STATE_HOLD))
            {
                Log.info(TAG, "_retrieveCall:: reInvite");
                reInvite(call, IpAddress.localIpAddress, 0, new AttributeField(SENDRECV));
                return true;
            }
            else if (call.statusIs(CommonConstantEntry.CALL_STATE_IN_CALL))
            {
                if (!call.isHolded())
                {// û�б�����
                    changeCallStatus(call, CommonConstantEntry.CALL_STATE_IN_CALL, CommonConstantEntry.Q850_NOREASON);
                }
                return true;
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
        return false;
    }

    /**
     * Schedules a re-inviting event after <i>delay_time</i> secs.
     * 
     * @throws Exception
     */
    void reInvite(final ExtendedCall call, String address, final int delay_time, AttributeField attr) throws Exception
    {
        SessionDescriptor sdp = new SessionDescriptor(call.getLocalSessionDescriptor());
        sdp.IncrementOLine();

        SessionDescriptor new_sdp = new SessionDescriptor(sdp.getOrigin(), sdp.getSessionName(), new ConnectionField("IP4", address), new TimeField());

        // ɾ������һ�ε�Media Attribute��ֵ����������
        MediaDescriptor mediaDescriptor = sdp.getMediaDescriptor(AUDIO);
        if (mediaDescriptor != null)
        {
            mediaDescriptor.removeAttribute(RECVONLY);
            mediaDescriptor.removeAttribute(INACTIVE);
            mediaDescriptor.removeAttribute(SENDRECV);
            mediaDescriptor.removeAttribute(SENDONLY);

            new_sdp.addMediaDescriptor(mediaDescriptor);
            new_sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(attr), null));
        }

        mediaDescriptor = sdp.getMediaDescriptor(VIDEO);
        if (mediaDescriptor != null)
        {
            mediaDescriptor.removeAttribute(RECVONLY);
            mediaDescriptor.removeAttribute(INACTIVE);
            mediaDescriptor.removeAttribute(SENDRECV);
            mediaDescriptor.removeAttribute(SENDONLY);

            new_sdp.addMediaDescriptor(mediaDescriptor);
            new_sdp.addMediaDescriptor(new MediaDescriptor(new MediaField(attr), null));
        }

        runReInvite(call, new_sdp.toString(), delay_time);
    }

    /**
     * Re-invite.
     * 
     * @throws Exception
     */
    private void runReInvite(ExtendedCall call, String body, int delay_time) throws Exception
    {
        try
        {
            if (delay_time > 0)
            {
                Thread.sleep(delay_time * 1000);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".runReInvite", e);
        }
        Log.info(TAG, "runReInvite:: call=" + call + " call.isOnCall()=" + call.isOnCall());

        if (call != null && call.isOnCall())
        {
            Log.info(TAG, "runReInvite:: REFER/Forward");
            call.modify(null, body);
        }
    }

    /**
     * Schedules a call-Forward event after <i>delay_time</i> secs.
     */
    void callForward(final String caller, final String Forward_to, final int delay_time)
    {
        // in case of incomplete url (e.g. only 'user' is present), try to
        // complete it
        final String target_url;
        if (!Forward_to.contains("@"))
            target_url = Forward_to + "@" + realm; // modified
        else
            target_url = Forward_to;

        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    runCallForward(caller, target_url, delay_time);
                }
                catch (Exception ex)
                {
                    Log.exception("UserAgent.callForward", ex);
                }
            }

        }.start();
    }

    /**
     * Call-Forward.
     * 
     * @throws Exception
     */
    private void runCallForward(String callid, String Forward_to, int delay_time) throws Exception
    {
        try
        {
            if (delay_time > 0)
            {
                Thread.sleep(delay_time * 1000);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".runCallForward", e);
        }

        ExtendedCall call = callCache.get(callid);
        if (call != null && call.isOnCall())
        {
            Log.info(TAG, "runCallForward:: REFER/Forward");
            call.Forward(Forward_to);
        }
    }

    /**
     * Schedules an automatic answer event after <i>delay_time</i> secs.
     */
    void automaticAccept(final String callid, final int delay_time)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    runAutomaticAccept(callid, delay_time);
                }
                catch (Exception ex)
                {
                    Log.exception(TAG + ".automaticAccept", ex);
                }
            }

        }.start();
    }

    /**
     * Automatic answer.
     * 
     * @throws Exception
     */
    private void runAutomaticAccept(String callid, int delay_time) throws Exception
    {
        try
        {
            if (delay_time > 0)
            {
                Thread.sleep(delay_time * 1000);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".runAutomaticAccept", e);
        }

        Log.info(TAG, "runAutomaticAccept:: AUTOMATIC-ANSWER");
        accept(callid);
    }

    /**
     * Schedules an automatic hangup event after <i>delay_time</i> secs.
     */
    void automaticHangup(final ExtendedCall call, final int delay_time)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    runAutomaticHangup(call, delay_time);
                }
                catch (Exception ex)
                {
                    Log.exception(TAG + ".automaticHangup", ex);
                }
            }

        }.start();
    }

    /**
     * Automatic hangup.
     */
    private void runAutomaticHangup(ExtendedCall call, int delay_time)
    {
        try
        {
            if (delay_time > 0)
            {
                Thread.sleep(delay_time * 1000);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG + ".runAutomaticHangup", e);
        }
        if (call != null && call.isOnCall())
        {
            Log.info(TAG, "AUTOMATIC-HANGUP");

            hangup(call.getCallId());
        }

    }

    @Override
    public void onCallInfoResponse(ExtendedCall call, int code, String reason, String body, Message msg)
    {
        Log.info(TAG, "onCallInfoResponse:: code:" + code + " reason:" + reason + " body:" + body);
        XmlMessage xmlMsg = XmlMessageFactory.parseConfXml(body);
        if (xmlMsg == null || xmlMsg.getEventType() == null || xmlMsg.getConfMessage() == null)
            return;

        if (xmlMsg.getEventType().equals(XmlMessage.EVENT_TYPE_CONFERENCE_REPORT))
        {// ������Ϣ
            conference.onConferenceInfoReponse(call, xmlMsg);// ������Ϣ��¼��¼����Ϣ
        }
        else if (xmlMsg.getEventType().equals(XmlMessage.EVENT_TYPE_CONFERENCE_REQUEST))
        {// �����Ա������Ϣ
            Log.info(TAG, "EVENT_TYPE_CONFERENCE_REQUEST");
            ConfMedia audio = xmlMsg.getConfMessage().getItem().getMediaInfo().getAudio();
            ConfMedia video = xmlMsg.getConfMessage().getItem().getMediaInfo().getVideo();
            if (audio != null && mSipAdapter.getSCallEventListener() != null)
            {
                if (ConfMedia.STATUS_RECVONLY.equals(audio.getStatus()))
                {
                    mSipAdapter.getSCallEventListener().onAudioEnable(call.getCallId(), false);
                }
                else if (ConfMedia.STATUS_SENDRECV.equals(audio.getStatus()))
                {
                    mSipAdapter.getSCallEventListener().onAudioEnable(call.getCallId(), true);
                }
            }
            if (video != null && mSipAdapter.getSCallEventListener() != null)
            {
                if (ConfMedia.STATUS_RECVONLY.equals(video.getStatus()) && TextUtils.isEmpty(video.getBroadcast()))
                {
                    Log.info(TAG, "STATUS_RECVONLY");
                    mSipAdapter.getSCallEventListener().onVideoEnable(call.getCallId(), false);
                }
                else if (ConfMedia.STATUS_SENDRECV.equals(video.getStatus()) && TextUtils.isEmpty(video.getBroadcast()))
                {
                    Log.info(TAG, "STATUS_SENDRECV");
                    mSipAdapter.getSCallEventListener().onVideoEnable(call.getCallId(), true);
                }
            }
        }
        else if (xmlMsg.getEventType().equals(XmlMessage.EVENT_TYPE_RECORD_REPORT) && xmlMsg.getConfMessage() != null)
        {// ¼��¼����Ϣ
            switch (call.getCallType())
            {
                case CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO:
                case CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO:
                    if (mSipAdapter.getSCallEventListener() != null)
                    {
                        mSipAdapter.getSCallEventListener().onRecordInfo(call.getCallId(), xmlMsg.getConfMessage().getItem().getTask().getId(),
                                xmlMsg.getConfMessage().getItem().getTask().getServer());
                    }
                    break;
                case CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO:
                case CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO:
                    if (mSipAdapter.getConfEventListener() != null)
                    {
                        mSipAdapter.getConfEventListener().onRecordInfo(call.getCallId(), xmlMsg.getConfMessage().getItem().getTask().getId(),
                                xmlMsg.getConfMessage().getItem().getTask().getServer());
                    }
                    break;

                case CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE:
                    if (mSipAdapter.getVsEventListener() != null)
                    {
                        mSipAdapter.getVsEventListener().onRecordInfo(call.getCallId(), xmlMsg.getConfMessage().getItem().getTask().getId(),
                                xmlMsg.getConfMessage().getItem().getTask().getServer());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onCallReInviteTimeout(Call call)
    {
        Log.info(TAG, "onCallReInviteTimeout:: call=" + call);

    }

    @Override
    public void onHeartbeatResponse(Message msg)
    {// ��������Ӧ�ն�����
        ((KeepAliveUdp) this.mSipAdapter.getKeepAlive()).setLastHeartbeat(SdkUtil.getRealTime());
    }

    public void setSipEngine(SipAdapter sipAdapter)
    {
        this.mSipAdapter = sipAdapter;
//        UserAgent.callIdMap = sipAdapter.getCallIdMap();
    }

    public Hashtable<String, ExtendedCall> getCallCache()
    {
        return callCache;
    }

    public void setConference(ConferenceHandler conference)
    {
        this.conference = conference;
    }

}
