package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：业务配置
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public class ServiceConfig implements Parcelable
{
    /** 自动应答开关   */
    public boolean autoAnswer;
//    /** 闭铃开关   */
//    public boolean isCloseRingEnable;
    /** 紧急状态开关   */
    public boolean isEmergencyEnable;
    /** 免打扰开关   */
    public boolean isDndEnable;
    /** 夜服开关   */
    public boolean isNightServiceEnable;
    /** 视频呼叫开关   */
    public boolean isVideoCall;
    /** 本地录音开关   */
    public boolean isAudioRecord;
    /** 紧急呼叫优先级   */
    public int emergencyCallPriority;
    /** 普通呼叫优先级   */
    public int callPriority;
    /** 当前值班员 */
    public String atd;

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeByte((byte) (autoAnswer ? 1 : 0));
//        dest.writeByte((byte) (isCloseRingEnable ? 1 : 0));
        dest.writeByte((byte) (isDndEnable ? 1 : 0));
        dest.writeByte((byte) (isEmergencyEnable ? 1 : 0));
        dest.writeByte((byte) (isNightServiceEnable ? 1 : 0));
        dest.writeByte((byte) (isVideoCall ? 1 : 0));
        dest.writeByte((byte) (isAudioRecord ? 1 : 0));
        dest.writeInt(emergencyCallPriority);
        dest.writeInt(callPriority);
        dest.writeString(atd);

    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<ServiceConfig> CREATOR = new Creator<ServiceConfig>()
    {
        public ServiceConfig createFromParcel(Parcel source)
        {
            ServiceConfig config = new ServiceConfig();
            config.autoAnswer = source.readByte() != 0;
//            config.isCloseRingEnable = source.readByte() != 0;
            config.isDndEnable = source.readByte() != 0;
            config.isEmergencyEnable = source.readByte() != 0;
            config.isNightServiceEnable = source.readByte() != 0;
            config.isVideoCall = source.readByte() != 0;
            config.isAudioRecord = source.readByte() != 0;
            config.emergencyCallPriority = source.readInt();
            config.callPriority = source.readInt();
            config.atd = source.readString();
            return config;
        }

        public ServiceConfig[] newArray(int size)
        {
            return new ServiceConfig[size];
        }
    };

}
