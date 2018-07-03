package com.jiaxun.sdk.dcl.model;

import com.jiaxun.sdk.dcl.module.DclServiceFactory;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * 说明：通话记录对象数据承载类
 *
 * @author  hubin
 *
 * @Date 2015-2-9
 */
public class CallRecord implements Parcelable
{
    // 本端号码
    private String accountNum;
    // 本端名称
    private String accountName;
    // 对端号码
    private String peerNum;
    // 对端名称
    private String peerName;
    // 功能号码
    private String funcCode;
    // 呼叫类型（单呼、组呼等）
    private int callType;
    // 呼叫等级（紧急呼叫、普通呼叫等）
    private int callPriority;
    // 释放原因
    private int releaseReason;
    // 呼叫开始时间
    private long startTime;
    // 通话开始时间
    private long connectTime;
    // 呼叫结束时间
    private long releaseTime;
//    // 通话时长
//    private long duration;
    // 呼叫方向（呼入呼出）
    private boolean isOutGoing;
    // 值班员名字
    private String atdName;
    // 是否主席
    private boolean isChairman;
    // 所属会议ID
    private String confId;
    // 所属会议名称
    private String confName;
    // 录音录像ID
    private String recordTaskId;
    // 录音录像文件存放服务器地址
    private String recordServer;
    // 录音文件URI
    private String recordFile;
    // 对方的用户类型（监控，用户）
    private int userType;
    // 是否电路域电话
    private boolean isCircuitSwitch;

    public CallRecord(String accountName, String accountNum)
    {
        if (TextUtils.isEmpty(accountName))
        {
            setCallerName(accountNum);
        }
        else
        {
            setCallerName(accountName);
        }
        setCallerNum(accountNum);

        // 从服务器获取录音录像基本路径（下载和在线播放路径需要自己拼接）
//        String recordTaskId = callRecord.getRecordTaskId();
//        String recordServer = callRecord.getRecordServer();
//        if (recordTaskId != null && recordServer != null && !("".equals(recordTaskId)) && !("".equals(recordServer)) && peerNum != null)
//        {
//            Log.info("recordTaskId===>>", recordTaskId);
//            Log.info("recordServer===>>", recordServer);
//            String recordFile = callRecordService.getRecordFileUrl(recordTaskId, recordServer, peerNum);
//            callRecord.setRecordFile(recordFile);
//
//        }

        // 得到值班员名称
        if (DclServiceFactory.getDclAtdService().getLoginedAttendant() != null)
        {
            setAtdName(DclServiceFactory.getDclAtdService().getLoginedAttendant().getLogin());
        }
    }

    public String getPeerNum()
    {
        return peerNum;
    }

    public void setPeerNum(String peerNum)
    {
        this.peerNum = peerNum;
    }

    public String getPeerName()
    {
        return peerName;
    }

    public void setPeerName(String peerName)
    {
        this.peerName = peerName;
    }

    

    public String getFuncCode()
    {
        return funcCode;
    }

    public void setFuncCode(String funcCode)
    {
        this.funcCode = funcCode;
    }

    public int getCallType()
    {
        return callType;
    }

    public void setCallType(int callType)
    {
        this.callType = callType;
    }

    public int getCallPriority()
    {
        return callPriority;
    }

    public void setCallPriority(int callPriority)
    {
        this.callPriority = callPriority;
    }

    public int getReleaseReason()
    {
        return releaseReason;
    }

    public void setReleaseReason(int releaseReason)
    {
        this.releaseReason = releaseReason;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public long getConnectTime()
    {
        return connectTime;
    }

    public void setConnectTime(long connectTime)
    {
        this.connectTime = connectTime;
    }

    public long getReleaseTime()
    {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime)
    {
        this.releaseTime = releaseTime;
    }

//
//    public long getDuration()
//    {
//        return duration;
//    }
//
//    public void setDuration(long duration)
//    {
//        this.duration = duration;
//    }

    public boolean isOutGoing()
    {
        return isOutGoing;
    }

    public void setOutGoing(boolean outGoing)
    {
        this.isOutGoing = outGoing;
    }

    public String getAtdName()
    {
        return atdName;
    }

    public void setAtdName(String atdName)
    {
        this.atdName = atdName;
    }

    public boolean isChairman()
    {
        return isChairman;
    }

    public void setChairman(boolean chairman)
    {
        this.isChairman = chairman;
    }

    public String getCallerNum()
    {
        return accountNum;
    }

    public void setCallerNum(String callerNum)
    {
        this.accountNum = callerNum;
    }

    public String getCallerName()
    {
        return accountName;
    }

    public void setCallerName(String callerName)
    {
        this.accountName = callerName;
    }

    public String getConfId()
    {
        return confId;
    }

    public void setConfId(String confId)
    {
        this.confId = confId;
    }

    public String getRecordTaskId()
    {
        return recordTaskId;
    }

    public void setRecordTaskId(String recordTaskId)
    {
        this.recordTaskId = recordTaskId;
    }

    public String getRecordServer()
    {
        return recordServer;
    }

    public void setRecordServer(String recordServer)
    {
        this.recordServer = recordServer;
    }

    public String getRecordFile()
    {
        return recordFile;
    }

    public void setRecordFile(String recordFile)
    {
        this.recordFile = recordFile;
    }

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public boolean isCircuitSwitch()
    {
        return isCircuitSwitch;
    }

    public void setCircuitSwitch(boolean circuitSwitch)
    {
        this.isCircuitSwitch = circuitSwitch;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(accountName);
        dest.writeString(accountNum);
        dest.writeString(peerName);
        dest.writeString(peerNum);
        dest.writeString(funcCode);
        dest.writeInt(callType);
        dest.writeInt(callPriority);
        dest.writeInt(releaseReason);
        dest.writeLong(startTime);
        dest.writeLong(connectTime);
        dest.writeLong(releaseTime);
//        dest.writeLong(duration);
        dest.writeByte((byte) (isOutGoing ? 1 : 0));
        dest.writeString(atdName);
        dest.writeByte((byte) (isChairman ? 1 : 0));
        dest.writeString(confId);
        dest.writeString(confName);
        dest.writeString(recordTaskId);
        dest.writeString(recordServer);
        dest.writeString(recordFile);
        dest.writeInt(userType);
        dest.writeByte((byte) (isCircuitSwitch ? 1 : 0));
    }

    public String getConfName()
    {
        return confName;
    }

    public void setConfName(String confName)
    {
        this.confName = confName;
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<CallRecord> CREATOR = new Creator<CallRecord>()
    {
        public CallRecord createFromParcel(Parcel source)
        {
            CallRecord callRecord = new CallRecord(source.readString(), source.readString());
            callRecord.accountName = source.readString();
            callRecord.accountNum = source.readString();
            callRecord.peerName = source.readString();
            callRecord.peerNum = source.readString();
            callRecord.funcCode = source.readString();
            callRecord.callType = source.readInt();
            callRecord.callPriority = source.readInt();
            callRecord.releaseReason = source.readInt();
            callRecord.startTime = source.readLong();
            callRecord.connectTime = source.readLong();
            callRecord.releaseTime = source.readLong();
//            callRecord.duration = source.readLong();
            callRecord.isOutGoing = source.readByte() != 0;
            callRecord.atdName = source.readString();
            callRecord.isChairman = source.readByte() != 0;
            callRecord.confId = source.readString();
            callRecord.confName = source.readString();
            callRecord.recordTaskId = source.readString();
            callRecord.recordServer = source.readString();
            callRecord.recordFile = source.readString();
            callRecord.userType = source.readInt();
            callRecord.isCircuitSwitch = source.readByte() != 0;
            return callRecord;
        }

        public CallRecord[] newArray(int size)
        {
            return new CallRecord[size];
        }
    };
}
