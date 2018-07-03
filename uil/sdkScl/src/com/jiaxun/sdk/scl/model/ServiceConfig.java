package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ˵����ҵ������
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public class ServiceConfig implements Parcelable
{
    /** �Զ�Ӧ�𿪹�   */
    public boolean autoAnswer;
//    /** ���忪��   */
//    public boolean isCloseRingEnable;
    /** ����״̬����   */
    public boolean isEmergencyEnable;
    /** ����ſ���   */
    public boolean isDndEnable;
    /** ҹ������   */
    public boolean isNightServiceEnable;
    /** ��Ƶ���п���   */
    public boolean isVideoCall;
    /** ����¼������   */
    public boolean isAudioRecord;
    /** �����������ȼ�   */
    public int emergencyCallPriority;
    /** ��ͨ�������ȼ�   */
    public int callPriority;
    /** ��ǰֵ��Ա */
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
