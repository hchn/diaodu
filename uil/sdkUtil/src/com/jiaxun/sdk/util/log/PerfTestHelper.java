package com.jiaxun.sdk.util.log;

import java.util.Hashtable;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.data.CallPref;
import com.jiaxun.sdk.util.log.data.PttPref;
import com.jiaxun.sdk.util.log.data.XinlingPerf;

/**
 * 性能测试帮助类
 * 
 * @author fuluo
 */
public class PerfTestHelper
{
    //呼叫性能对象
    public static CallPref CALLPREF = new CallPref();
    
    //PTT性能对象
    public static PttPref PTTPREF = new PttPref();
    
    //信令性能对象map
    public static Hashtable<String, XinlingPerf> XINLINGMAP = new Hashtable<String, XinlingPerf>(10);
    
    private static String TAG = PerfTestHelper.class.getName();
    
    /**
     * 记录主叫性能数据
     */
    public static void logCallerPerf()
    {
        //呼叫发起-建立时间戳
        StringBuffer perf = new StringBuffer();
        perf.append("performance.caller")
        	.append(":caller:")
            .append(CALLPREF.getCallerNumber())
            .append(":callee:")
            .append(CALLPREF.getCalleeNumber())
            .append(":pressCall:")
            .append(CALLPREF.getPressCall())
            .append(":makeCall:")
            .append(CALLPREF.getMakeCall())
            .append(":sendInvite:")
            .append(CALLPREF.getSendInvite())
            .append(":receive100Trying:")
            .append(CALLPREF.getReceive100Trying());
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
        	perf.append(":receive180:")
            	.append(CALLPREF.getReceive180());
        }
        perf.append(":receive200Ok:")
            .append(CALLPREF.getReceive200Ok())
            .append(":sendAck:")
            .append(CALLPREF.getSendAck())
            .append(":audioReceiveStart:")
            .append(CALLPREF.getAudioReceiveStart())
            .append(":audioReceiveOk:")
            .append(CALLPREF.getAudioReceiveOk())
            .append(":audioSendStart:")
            .append(CALLPREF.getAudioSendStart())
            .append(":audioSendOk:")
            .append(CALLPREF.getAudioSendOk())
            .append(":audioReadyStart:")
            .append(CALLPREF.getAudioReadyStart())
            .append(":audioReadyOk:");
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            perf.append(CALLPREF.getAudioReadyOkSingle());
        }
        else
        {//组呼
            perf.append(CALLPREF.getAudioReadyOkGroup());
        }
        perf.append(":callReadyOk:")
            .append(CALLPREF.getCallReadyOk());
        Log.info(TAG, perf.toString());
        
        //呼叫发起-建立耗时
        perf.delete(0, perf.length());
        perf.append("performance:caller.time")
        	.append(":caller:")
            .append(CALLPREF.getCallerNumber())
            .append(":callee:")
            .append(CALLPREF.getCalleeNumber())
            .append(":makeCall-pressCall:")
            .append(CALLPREF.getMakeCall() - CALLPREF.getPressCall())
            .append(":sendInvite-makeCall:")
            .append(CALLPREF.getSendInvite() - CALLPREF.getMakeCall())
            .append(":receive100Trying-sendInvite:")
            .append(CALLPREF.getReceive100Trying() - CALLPREF.getSendInvite());
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
        	perf.append(":receive180-receive100Trying:")
	            .append(CALLPREF.getReceive180() - CALLPREF.getReceive100Trying())
	            .append(":receive200Ok-receive180:")
	            .append(CALLPREF.getReceive200Ok() - CALLPREF.getReceive180())
	            .append(":sendAck-receive200Ok:")
	            .append(CALLPREF.getSendAck() - CALLPREF.getReceive200Ok());
        }
        else
        {//组呼|广播
        	perf.append(":receive200Ok-receive100Trying:")
	            .append(CALLPREF.getReceive200Ok() - CALLPREF.getReceive100Trying())
	            .append(":sendAck-receive200Ok:")
	            .append(CALLPREF.getSendAck() - CALLPREF.getReceive200Ok());
        }
        perf.append(":audioReceiveOk-audioReceiveStart:")
            .append(CALLPREF.getAudioReceiveOk() - CALLPREF.getAudioReceiveStart())
            .append(":audioSendOk-audioSendStart:")
            .append(CALLPREF.getAudioSendOk() - CALLPREF.getAudioSendStart())
            .append(":audioReadyOk-audioReadyStart:");
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            perf.append(CALLPREF.getAudioReadyOkSingle() - CALLPREF.getAudioReadyStart());
        }
        else
        {//组呼
            perf.append(CALLPREF.getAudioReadyOkGroup() - CALLPREF.getAudioReadyStart());
        }
            
        perf.append(":msgTotal:")
            .append(CALLPREF.getSendAck() - CALLPREF.getSendInvite())
            .append(":speakTotal:")
            .append(CALLPREF.getAudioSendOk() - CALLPREF.getMakeCall())
            .append(":infoTotal:")
            .append(CALLPREF.getCallReadyOk() - CALLPREF.getMakeCall())
            .append(":listenTotal:");
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            perf.append(CALLPREF.getAudioReadyOkSingle() - CALLPREF.getMakeCall());
        }
        else
        {//组呼
            perf.append(CALLPREF.getAudioReadyOkGroup() - CALLPREF.getMakeCall());
        }
        Log.info(TAG, perf.toString());
        
        //呼叫结束
        logCallClosePerf(perf);
        
        CALLPREF = new CallPref();//重新创建对象
    }
    
    /**
     * 记录呼叫结束性能数据
     */
    private static void logCallClosePerf(StringBuffer perf)
    {
        //呼叫结束时间戳
        perf.delete(0, perf.length());
        perf.append("performance.close")
            .append(":caller:")
            .append(CALLPREF.getCallerNumber())
            .append(":callee:")
            .append(CALLPREF.getCalleeNumber())
            .append(":closeCall:")
            .append(CALLPREF.getCloseCall())
            .append(":sendBye:")
            .append(CALLPREF.getSendBye())
            .append(":receiveBye:")
            .append(CALLPREF.getReceiveBye())
            .append(":receiveBye200Ok:")
            .append(CALLPREF.getReceiveBye200Ok());
        Log.info(TAG, perf.toString());
    }
    
    /**
     * 记录被叫性能数据
     */
    public static void logCalleePerf()
    {
        //来呼-建立
        StringBuffer perf = new StringBuffer();
        perf.append("performance.callee")
        	.append(":caller:")
            .append(CALLPREF.getCallerNumber())
            .append(":callee:")
            .append(CALLPREF.getCalleeNumber())
            .append(":receiveInvite:")
            .append(CALLPREF.getReceiveInvite())
            .append(":send100Trying:")
            .append(CALLPREF.getSend100Trying());
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
        	perf.append(":send180:")
            	.append(CALLPREF.getSend180());
        }
        perf.append(":send200Ok:")
            .append(CALLPREF.getSend200Ok())
            .append(":receiveAck:")
            .append(CALLPREF.getReceiveAck())
            .append(":audioReceiveStart:")
            .append(CALLPREF.getAudioReceiveStart())
            .append(":audioReceiveOk:")
            .append(CALLPREF.getAudioReceiveOk())
            .append(":audioSendStart:")
            .append(CALLPREF.getAudioSendStart())
            .append(":audioSendOk:")
            .append(CALLPREF.getAudioSendOk())
            .append(":audioReadyStart:")
            .append(CALLPREF.getAudioReadyStart())
            .append(":audioReadyOk:");
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            perf.append(CALLPREF.getAudioReadyOkSingle());
        }
        else
        {//组呼
            perf.append(CALLPREF.getAudioReadyOkGroup());
        }
        Log.info(TAG, perf.toString());
        
        //来呼-建立耗时
        perf.delete(0, perf.length());
        perf.append("performance:callee.time:")
        	.append(":caller:")
            .append(CALLPREF.getCallerNumber())
            .append(":callee:")
            .append(CALLPREF.getCalleeNumber())
            .append(":send100Trying-receiveInvite:")
            .append(CALLPREF.getSend100Trying() - CALLPREF.getReceiveInvite());
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
        	perf.append(":send180-send100Trying:")
        	    .append(CALLPREF.getSend180() - CALLPREF.getSend100Trying())
	            .append(":send200Ok-send180:")
	            .append(CALLPREF.getSend200Ok() - CALLPREF.getSend180());
        }
        else
        {//组呼|广播
        	perf.append(":send200Ok-send100Trying:")
            	.append(CALLPREF.getSend200Ok() - CALLPREF.getSend100Trying());
        }
        perf.append(":receiveAck-send200Ok:")
            .append(CALLPREF.getReceiveAck() - CALLPREF.getSend200Ok())
            .append(":audioSendOk-audioSendStart:")
            .append(CALLPREF.getAudioSendOk() - CALLPREF.getAudioSendStart())
            .append(":audioReadyOk-audioReadyStart:");
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            perf.append(CALLPREF.getAudioReadyOkSingle() - CALLPREF.getAudioReadyStart());
        }
        else
        {//组呼
            perf.append(CALLPREF.getAudioReadyOkGroup() - CALLPREF.getAudioReadyStart());
        }
        perf.append(":msgTotal:")
            .append(CALLPREF.getReceiveAck() - CALLPREF.getReceiveInvite())
            .append(":speakTotal:")
            .append(CALLPREF.getAudioSendOk() - CALLPREF.getReceiveInvite())
            .append(":infoTotal:")
            .append(CALLPREF.getCallReadyOk() - CALLPREF.getReceiveInvite())
            .append(":listenTotal:");
        if(CALLPREF.getCallType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            perf.append(CALLPREF.getAudioReadyOkSingle() - CALLPREF.getReceiveInvite());
        }
        else
        {//组呼
            perf.append(CALLPREF.getAudioReadyOkGroup() - CALLPREF.getReceiveInvite());
        }
        Log.info(TAG, perf.toString());
        
        //呼叫结束
        logCallClosePerf(perf);
        
        CALLPREF = new CallPref();//重新创建对象
    }
    
    /**
     * 记录PTT申请性能数据
     */
    public static void logPttPressPerf()
    {
        //PTT申请话权时间戳
        StringBuffer perf = new StringBuffer();
        perf.append("performance.pttPress")
            .append(":groupNumber:")
            .append(PTTPREF.getGroupNumber())
            .append(":number:")
            .append(PTTPREF.getCallNumber())
            .append(":pttDown:")
            .append(PTTPREF.getPttDown())
            .append(":pttRequest:")
            .append(PTTPREF.getPttRequest())
            .append(":sendPttRequest:")
            .append(PTTPREF.getSendPttRequest())
            .append(":receivePttRequestReponse:")
            .append(PTTPREF.getReceivePttRequestReponse())
            .append(":pttStartTalk:")
            .append(PTTPREF.getPttStartTalk());
        Log.info(TAG, perf.toString());
        
        //PTT申请话权耗时
        perf.delete(0, perf.length());
        perf.append("performance.pttPress.time:")
            .append(":pttRequest-pttDown:")
            .append(PTTPREF.getPttRequest() - PTTPREF.getPttDown())
            .append(":sendPttRequest-pttRequest:")
            .append(PTTPREF.getSendPttRequest() - PTTPREF.getPttRequest())
            .append(":receivePttRequestReponse-sendPttRequest:")
            .append(PTTPREF.getReceivePttRequestReponse() - PTTPREF.getSendPttRequest())
            .append(":pttStartTalk-receivePttRequestReponse:")
            .append(PTTPREF.getPttStartTalk() - PTTPREF.getReceivePttRequestReponse())
            .append(":msgTotal:")
            .append(PTTPREF.getReceivePttRequestReponse() - PTTPREF.getSendPttRequest())
            .append(":total:")
            .append(PTTPREF.getPttStartTalk() - PTTPREF.getPttDown());
        Log.info(TAG, perf.toString());
    }
    
    /**
     * 记录PTT释放性能数据
     */
    public static void logPttReleasePerf()
    {
        //PTT释放话权时间戳
        StringBuffer perf = new StringBuffer();
        perf.append("performance.pttRelease")
            .append(":groupNumber:")
            .append(PTTPREF.getGroupNumber())
            .append(":number:")
            .append(PTTPREF.getCallNumber())
            .append(":pttUp:")
            .append(PTTPREF.getPttUp())
            .append(":pttRelease:")
            .append(PTTPREF.getPttRelease())
            .append(":sendPttRelease:")
            .append(PTTPREF.getSendPttRelease())
            .append(":receivePttReleaseReponse:")
            .append(PTTPREF.getReceivePttReleaseReponse())
            .append(":pttStopTalk:")
            .append(PTTPREF.getPttStopTalk());
        Log.info(TAG, perf.toString());
        
        //PTT释放话权耗时
        perf.delete(0, perf.length());
        perf.append("performance.pttRelease.time:")
            .append(":pttRelease-pttUp:")
            .append(PTTPREF.getPttRelease() - PTTPREF.getPttUp())
            .append(":sendPttRelease-pttRelease:")
            .append(PTTPREF.getSendPttRelease() - PTTPREF.getPttRelease())
            .append(":receivePttReleaseReponse-sendPttRelease:")
            .append(PTTPREF.getReceivePttReleaseReponse() - PTTPREF.getSendPttRelease())
            .append(":pttStopTalk-receivePttReleaseReponse:")
            .append(PTTPREF.getPttStopTalk() - PTTPREF.getReceivePttReleaseReponse())
            .append(":msgTotal:")
            .append(PTTPREF.getReceivePttReleaseReponse() - PTTPREF.getSendPttRelease())
            .append(":total:")
            .append(PTTPREF.getPttStopTalk() - PTTPREF.getPttUp());
        Log.info(TAG, perf.toString());
    }
    
    /**
     * 记录信令性能数据
     */
    public static void logXinlingPerf(String callId)
    {
        if(callId == null)
        {
            return;
        }
        
        StringBuffer perf = new StringBuffer();
        
        XinlingPerf xinlingPerf = XINLINGMAP.get(callId);
        if(xinlingPerf != null)
        {
        	XINLINGMAP.remove(callId);
            perf.append("performance.xinling")
                .append(":callId:")
                .append(callId)
                .append(":request:")
                .append(xinlingPerf.getRequest())
                .append(":reponse:")
                .append(xinlingPerf.getReponse())
                .append(":reponse-request:")
                .append(xinlingPerf.getReponse() - xinlingPerf.getRequest());
            Log.info(TAG, perf.toString());
        }
    }

}
