package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：会议成员数据承载类
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public class ConfMemModel implements Parcelable
{
    // 成员状态：IDEL,RING,CONNECT
    private int status;
    // 成员名称
    private String name;
    // 成员号码
    private String number;
    // 成员类型：TERMINAL_USER = 0, 终端用户 MOBILEPHONE_USER, 移动用户 TELPHONE_USER, 固定用户
    // VIDEOPHONE_USER, 可视电话用户 BXPHONE_USER, 便携设备用户 MONITOR_USER, 监控用户
    // CARPHONE_USER, 车载设备用户
    private int type;
    // 成员静音标识
    private boolean isAudioEnabled;
    // 成员视频显示标识
    private boolean isVideoEnabled;
    // 成员视频分享标识
    private boolean isVideoShared;
    // 视频媒体标记
    private String mediaTag;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public boolean isAudioEnabled()
    {
        return isAudioEnabled;
    }

    public void setAudioEnabled(boolean isAudioEnabled)
    {
        this.isAudioEnabled = isAudioEnabled;
    }

    public boolean isVideoShared()
    {
        return isVideoShared;
    }

    public void setVideoShared(boolean isVideoShared)
    {
        this.isVideoShared = isVideoShared;
    }

    public boolean isVideoEnabled()
    {
        return isVideoEnabled;
    }

    public void setVideoEnabled(boolean isVideoEnabled)
    {
        this.isVideoEnabled = isVideoEnabled;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(status);
        dest.writeString(name);
        dest.writeString(number);
        dest.writeInt(type);
        dest.writeByte((byte) (isAudioEnabled ? 1 : 0));
        dest.writeByte((byte) (isVideoEnabled ? 1 : 0));
        dest.writeByte((byte) (isVideoShared ? 1 : 0));
        dest.writeString(mediaTag);
//        dest.writeInt(recallTimes);
//        dest.writeInt(recallInterval);
    }

    public String getMediaTag()
    {
        return mediaTag;
    }

    public void setMediaTag(String mediaTag)
    {
        this.mediaTag = mediaTag;
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<ConfMemModel> CREATOR = new Creator<ConfMemModel>()
    {
        public ConfMemModel createFromParcel(Parcel source)
        {
            ConfMemModel memberModel = new ConfMemModel();
            memberModel.status = source.readInt();
            memberModel.name = source.readString();
            memberModel.number = source.readString();
            memberModel.type = source.readInt();
            memberModel.isAudioEnabled = source.readByte() != 0;
            memberModel.isVideoEnabled = source.readByte() != 0;
            memberModel.isVideoShared = source.readByte() != 0;
            memberModel.mediaTag = source.readString();
//            memberModel.recallTimes = source.readInt();
//            memberModel.recallInterval = source.readInt();
            return memberModel;
        }

        public ConfMemModel[] newArray(int size)
        {
            return new ConfMemModel[size];
        }
    };
}
