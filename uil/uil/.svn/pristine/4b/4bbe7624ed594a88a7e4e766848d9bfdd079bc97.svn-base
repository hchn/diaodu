package com.jiaxun.setting.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：配置项基本抽象类
 * 
 * @author zhangxd
 * 
 * @Date 2015-6-1
 */
public class PrefsBaseItem implements Parcelable
{
    public interface ItemCallBack
    {
        void onCallBackResult(boolean result);
    }

    protected int setType;//0:日期设置 1:时间设置
    protected String name;// 名字
    protected String key;// key值
    protected String hint;// 提示用户输入限制
    protected int imageId = 0;// 图片资源id
    protected int itemType = 0;// 类型
    private boolean enabled = true;// 是否可用
    protected ItemCallBack itemCallBack;// 保存成功/失败回调

    
    public int getSetType()
    {
        return setType;
    }
    public void setSetType(int setType)
    {
        this.setType = setType;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public int getImageId()
    {
        return imageId;
    }

    public void setImageId(int imageId)
    {
        this.imageId = imageId;
    }

    public String getHint()
    {
        return hint;
    }

    public void setHint(String hint)
    {
        this.hint = hint;
    }

    public int getItemType()
    {
        return itemType;
    }

    public void setItemType(int itemType)
    {
        this.itemType = itemType;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public void setItemCallBack(ItemCallBack itemCallBack)
    {
        this.itemCallBack = itemCallBack;
    }

    public ItemCallBack getItemCallBack()
    {
        return itemCallBack;
    }

    
    public PrefsBaseItem()
    {    
    }
    
    //下面是序列化
    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags)
    {
        if (name != null)
            out.writeString(name);
        if (key != null)
            out.writeString(key);
        if (hint != null)
            out.writeString(hint);
        out.writeInt(imageId);
        out.writeInt(itemType);
        out.writeBooleanArray(new boolean[] { enabled });
    }

    public static final Parcelable.Creator<PrefsBaseItem> CREATOR = new Parcelable.Creator<PrefsBaseItem>()
    {
        public PrefsBaseItem createFromParcel(Parcel in)
        {
            return new PrefsBaseItem(in);
        }

        public PrefsBaseItem[] newArray(int size)
        {
            return new PrefsBaseItem[size];
        }
    };

    public PrefsBaseItem(Parcel in)
    {
        name=in.readString();
        key=in.readString();
        hint=in.readString();
        imageId=in.readInt();
        itemType=in.readInt();
        boolean[]booleanArray = new boolean[1];
        in.readBooleanArray(booleanArray);
        enabled=booleanArray[0];
        
    }
    
}
