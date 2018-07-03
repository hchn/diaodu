package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：单呼对象数据承载类
 *
 * @author  hubin
 *
 * @Date 2015-1-8
 */
public class SCallModel extends CallModel implements Parcelable
{
    // 对端号码
    private String peerNum;
    // 对端号码通讯录中的名称
    private String peerName;
    // 对端功能码
    private String peerFuncCode;
    // 静音开关标识
    private boolean isMuteOn;
    // 扬声器开关标识
    private boolean isSpeakerOn;
    // 视频
    private boolean isVideoOn;
    // 本机是否被保持
    private boolean isHolded;
    // 闭铃开关标识
    private boolean isCloseRing;

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

    public String getPeerFuncCode()
    {
        return peerFuncCode;
    }

    public void setPeerFuncCode(String peerFuncCode)
    {
        this.peerFuncCode = peerFuncCode;
    }

    public boolean isMuteOn()
    {
        return isMuteOn;
    }

    public void setMuteOn(boolean isMuteOn)
    {
        this.isMuteOn = isMuteOn;
    }

    public boolean isSpeakerOn()
    {
        return isSpeakerOn;
    }

    public void setSpeakerOn(boolean isSpeakerOn)
    {
        this.isSpeakerOn = isSpeakerOn;
    }

    public boolean isVideo()
    {
        return isVideoOn;
    }

    public void setVideo(boolean isVideo)
    {
        this.isVideoOn = isVideo;
    }

    public boolean isHolded()
    {
        return isHolded;
    }

    public void setHolded(boolean isHolded)
    {
        this.isHolded = isHolded;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(peerNum);
        dest.writeString(peerName);
        dest.writeString(peerFuncCode);
        dest.writeByte((byte) (isMuteOn ? 1 : 0));
        dest.writeByte((byte) (isSpeakerOn ? 1 : 0));
        dest.writeByte((byte) (isVideoOn ? 1 : 0));
        dest.writeByte((byte) (isHolded ? 1 : 0));
        dest.writeByte((byte) (isCloseRing ? 1 : 0));
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<SCallModel> CREATOR = new Creator<SCallModel>()
    {
        public SCallModel createFromParcel(Parcel source)
        {
            SCallModel sCallModel = new SCallModel();
            sCallModel.peerNum = source.readString();
            sCallModel.peerName = source.readString();
            sCallModel.peerFuncCode = source.readString();
            sCallModel.isMuteOn = source.readByte() != 0;
            sCallModel.isSpeakerOn = source.readByte() != 0;
            sCallModel.isVideoOn = source.readByte() != 0;
            sCallModel.isHolded = source.readByte() != 0;
            sCallModel.isCloseRing = source.readByte() != 0;
            return sCallModel;
        }

        public SCallModel[] newArray(int size)
        {
            return new SCallModel[size];
        }
    };

    public String toString()
    {
        return " peerNum:" + peerNum;
    }

    public boolean isCloseRing()
    {
        return isCloseRing;
    }

    public void setCloseRing(boolean isCloseRing)
    {
        this.isCloseRing = isCloseRing;
    }
}
