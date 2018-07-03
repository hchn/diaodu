package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：通话对象
 *
 * @author  hubin
 *
 * @Date 2015-4-1
 */
public class CallModel implements Parcelable
{
    // 通话ID
    private String sessionId;
    // 本机是否是通话发起者
    private boolean isStarter;
    // 本呼叫的优先级
    private int priority = -1;
    // 呼叫开始时间
    private long startTime;
    // 通话建立时间
    private long connectTime;
    // 呼叫结束时间
    private long endTime;
    // 视频标识
    private boolean isVideo;
    // 会议成员标识，用于作为会议成员的单呼标识
    private boolean isConfMember;

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public boolean isStarter()
    {
        return isStarter;
    }

    public void setStarter(boolean isStarter)
    {
        this.isStarter = isStarter;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
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

    public long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(sessionId);
        dest.writeByte((byte) (isStarter ? 1 : 0));
        dest.writeInt(priority);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(connectTime);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeByte((byte) (isConfMember ? 1 : 0));
    }

    public boolean isVideo()
    {
        return isVideo;
    }

    public void setVideo(boolean isVideo)
    {
        this.isVideo = isVideo;
    }

    public boolean isConfMember()
    {
        return isConfMember;
    }

    public void setConfMember(boolean isConfMember)
    {
        this.isConfMember = isConfMember;
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<CallModel> CREATOR = new Creator<CallModel>()
    {
        public CallModel createFromParcel(Parcel source)
        {
            CallModel callModel = new CallModel();
            callModel.sessionId = source.readString();
            callModel.isStarter = source.readByte() != 0;
            callModel.priority = source.readInt();
            callModel.startTime = source.readLong();
            callModel.endTime = source.readLong();
            callModel.connectTime = source.readLong();
            callModel.isVideo = source.readByte() != 0;
            callModel.isConfMember = source.readByte() != 0;
            return callModel;
        }

        public CallModel[] newArray(int size)
        {
            return new CallModel[size];
        }
    };
}
