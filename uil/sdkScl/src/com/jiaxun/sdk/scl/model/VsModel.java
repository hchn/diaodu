package com.jiaxun.sdk.scl.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：视频监控对象数据承载类
 *
 * @author  hubin
 *
 * @Date 2015-5-25
 */
public class VsModel extends CallModel implements Parcelable
{
    // 监控号码
    private String videoNum;

    public String getVideoNum()
    {
        return videoNum;
    }

    public void setVideoNum(String videoNum)
    {
        this.videoNum = videoNum;
    }
    
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(videoNum);
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<VsModel> CREATOR = new Creator<VsModel>()
    {
        public VsModel createFromParcel(Parcel source)
        {
            VsModel vsModel = new VsModel();
            vsModel.videoNum = source.readString();
            return vsModel;
        }

        public VsModel[] newArray(int size)
        {
            return new VsModel[size];
        }
    };

    public String toString()
    {
        return " videoDeviceUrl:" + videoNum;
    }
}
