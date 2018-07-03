package com.jiaxun.sdk.dcl.module.callRecord.impl;

import java.util.ArrayList;
import java.util.List;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.file.FileManager;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-7-17
 */
public class ExportCallRecord
{
    private FileManager fm;

    public ExportCallRecord()
    {
        fm = FileManager.getInstance();
    }

    public boolean exportToCSV(ArrayList<CallRecord> callRecords, String url, String fileName)
    {
        if (callRecords == null || callRecords.size() == 0)
        {
            return false;
        }

        List<String> rows = new ArrayList<String>();

        // 添加列名
        rows.add("主叫号码," + "主叫名称," + "被叫号码," + "被叫名称," + "呼叫类型," + "呼叫开始时间," + "通话开始时间," + "呼叫结束时间," + "呼叫级别," + "释放原因," + "通话时长," + "呼叫状态," + "呼叫方向,"
                + "值班员名称");

        for (CallRecord callRecord : callRecords)
        {
//            String callerNum = "";
//            String callerName = "";
//            String peerNum = "";
//            String peerName = "";
//            String callOutGoing = "";
//            String callTypeS = "";
//            String callPrority = callRecord.getCallPriority() + "";
//            String releaseReason = ServiceUtils.parseReleaseReason(callRecord.getReleaseReason());
//            String atdName = callRecord.getAtdName();
//            String callStatus = "";
//
//            String startTime = DateUtils.formatStartTime(callRecord.getStartTime());
//            String connectTime = "";
//            String releaseTime = DateUtils.formatStartTime(callRecord.getReleaseTime());
//
//            // 时间
//            String duration = "0秒";
//            if (callRecord.getConnectTime() == 0)
//            {
//                duration = "0秒";
//                if (!(callRecord.isOutGoing()))
//                {
//                    callStatus = "未接电话";
//                }
//                else
//                {
//                    callStatus = "已拨电话";
//                }
//            }
//            else
//            {
//                long durationTime = (callRecord.getReleaseTime() - callRecord.getConnectTime()) / 1000;
//                duration = DateUtils.formatDurationTime(durationTime);
//                connectTime = DateUtils.formatStartTime(callRecord.getConnectTime());
//                if (!(callRecord.isOutGoing()))
//                {
//                    callStatus = "已接电话";
//                }
//                else
//                {
//                    callStatus = "已拨电话";
//                }
//
//            }
//
//            if (callRecord.isOutGoing())
//            {
//                callerNum = callRecord.getCallerNum();
//                callerName = callRecord.getCallerName();
//                peerNum = callRecord.getPeerNum();
//                peerName = callRecord.getPeerName();
//                callOutGoing = "呼出";
//            }
//            else
//            {
//                callerNum = callRecord.getPeerNum();
//                callerName = callRecord.getPeerName();
//                peerNum = callRecord.getCallerNum();
//                peerName = callRecord.getCallerName();
//                callOutGoing = "呼入";
//            }

            int callType = callRecord.getCallType();
            if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {// 会议
                rows.add(getRowString(callRecord));
                String confId = callRecord.getConfId();
                ArrayList<CallRecord> confList = DclCallRecordServiceImpl.getInstance().getConfCallRecordList(confId);
                for (CallRecord confCallRecord : confList)
                {
                    
                    rows.add(getRowString(confCallRecord));
                }
            }

            else if (callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
            {// 单呼
                rows.add(getRowString(callRecord));
            }
            else
            {// 监控
                rows.add(getRowString(callRecord));
            }

        }

        return fm.writeFile(rows, url, fileName + ".csv");
    }

    private String getRowString(CallRecord callRecord)
    {
        StringBuffer sb = new StringBuffer();

        String callerNum = "";
        String callerName = "";
        String peerNum = "";
        String peerName = "";
        String callOutGoing = "";
        String callTypeS = "";
        String callPrority = callRecord.getCallPriority() + "";
        String releaseReason = SdkUtil.parseReleaseReason(callRecord.getReleaseReason());
        String atdName = callRecord.getAtdName();
        String callStatus = "";

        String startTime = DateUtils.formatStartTime(callRecord.getStartTime());
        String connectTime = "";
        String releaseTime = DateUtils.formatStartTime(callRecord.getReleaseTime());

        // 时间
        String duration = "0秒";
        if (callRecord.getConnectTime() == 0)
        {
            connectTime = DateUtils.formatStartTime(callRecord.getStartTime());
            duration = "0秒";
            if (!(callRecord.isOutGoing()))
            {
                callStatus = "未接电话";
            }
            else
            {
                callStatus = "已拨电话";
            }
        }
        else
        {
            long durationTime = (callRecord.getReleaseTime() - callRecord.getConnectTime()) / 1000;
            duration = DateUtils.formatDurationTime(durationTime);
            connectTime = DateUtils.formatStartTime(callRecord.getConnectTime());
            if (!(callRecord.isOutGoing()))
            {
                callStatus = "已接电话";
            }
            else
            {
                callStatus = "已拨电话";
            }

        }

        if (callRecord.isOutGoing())
        {
            callerNum = callRecord.getCallerNum();
            callerName = callRecord.getCallerName();
            peerNum = callRecord.getPeerNum();
            peerName = callRecord.getPeerName();
            callOutGoing = "呼出";
        }
        else
        {
            callerNum = callRecord.getPeerNum();
            callerName = callRecord.getPeerName();
            peerNum = callRecord.getCallerNum();
            peerName = callRecord.getCallerName();
            callOutGoing = "呼入";
        }
        
        int callType = callRecord.getCallType();
        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {// 会议
            boolean isChairMan = callRecord.isChairman();
            if (isChairMan)
            {
                callTypeS = "会议";
            }
            else
            {
                callTypeS = "会议成员";
            }
        }

        else if (callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {// 单呼
            callTypeS = "单呼";
        }
        else
        {// 监控
            callTypeS = "监控";
        }

        sb.append(callerNum).append(",").append(callerName).append(",").append(peerNum).append(",").append(peerName).append(",").append(callTypeS).append(",")
                .append(startTime).append(",").append(connectTime).append(",").append(releaseTime).append(",").append(callPrority).append(",")
                .append(releaseReason).append(",").append(duration).append(",").append(callStatus).append(",").append(callOutGoing).append(",").append(atdName);

        return sb.toString();
    }

}
