package com.jiaxun.sdk.scl.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ˵��������������ݳ�����
 *
 * @author  hubin
 *
 * @Date 2015-1-8
 */
public class ConfModel extends CallModel implements Parcelable
{
    // ������ϯ����
    private String chairmanNum;
    // ������ϯ����
    private String chairmanName;
    // ��������
    private String confName;
    // �����Ա�б�
    private ArrayList<ConfMemModel> memList;
    // ����
    private boolean isAudioMute;
    // ����
    private boolean isVideoMute;
    
    public ConfModel()
    {
        memList = new ArrayList<ConfMemModel>();
    }

    public String getChairmanNum()
    {
        return chairmanNum;
    }

    public void setChairmanNum(String chairmanNum)
    {
        this.chairmanNum = chairmanNum;
    }

    public String getChairmanName()
    {
        return chairmanName;
    }

    public void setChairmanName(String chairmanName)
    {
        this.chairmanName = chairmanName;
    }

    public String getConfName()
    {
        return confName;
    }

    public void setConfName(String confName)
    {
        this.confName = confName;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(chairmanNum);
        dest.writeString(chairmanName);
        dest.writeString(confName);
        dest.writeByte((byte) (isAudioMute ? 1 : 0));
        dest.writeByte((byte) (isVideoMute ? 1 : 0));
        dest.writeList(memList);
    }

    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Parcelable.Creator<ConfModel> CREATOR = new Creator<ConfModel>()
    {
        public ConfModel createFromParcel(Parcel source)
        {
            ConfModel confModel = new ConfModel();
            confModel.chairmanNum = source.readString();
            confModel.chairmanName = source.readString();
            confModel.confName = source.readString();
            confModel.isAudioMute = source.readByte() != 0;
            confModel.isVideoMute = source.readByte() != 0;
            confModel.memList = source.readArrayList(ConfMemModel.class.getClassLoader());
            return confModel;
        }

        public ConfModel[] newArray(int size)
        {
            return new ConfModel[size];
        }
    };

    public String toString()
    {
        return " chairmanNum:" + chairmanNum;
    }

    public ArrayList<ConfMemModel> getMemList()
    {
        return memList;
    }

    public void setMemList(ArrayList<ConfMemModel> memList)
    {
        if(memList != null)
        {
            this.memList = memList;    
        }
    }

    public boolean isAudioMute()
    {
        return isAudioMute;
    }

    public void setAudioMute(boolean isAudioMute)
    {
        this.isAudioMute = isAudioMute;
    }

    public boolean isVideoMute()
    {
        return isVideoMute;
    }

    public void setVideoMute(boolean isVideoMute)
    {
        this.isVideoMute = isVideoMute;
    }
}
