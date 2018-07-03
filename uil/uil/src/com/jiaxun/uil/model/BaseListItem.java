package com.jiaxun.uil.model;

import java.io.Serializable;

/**
 * 作为列表中元素的最基本抽象类
 * 
 * @author hubin
 *
 */
public class BaseListItem implements Serializable, Cloneable
{
    private static final long serialVersionUID = 6076256592120368795L;

    public static final int TYPE_CONTACT = 0;
    public static final int TYPE_DEP = 1;

    private int id;

    private String name;

//    private int number; // 编号
    // 位置
    private int position = -1;

    private String content;
    // 全拼
    private String nameLetters;
    // 全拼数组
    private String[] nameLettersArray;
    // 头像资源ID
    private int iconRes;
    // 用于删除时是否被选中标志
    private boolean isChecked = false;
    // 是否是已被选中
    private boolean isSelected = false;
    // 显示数据拼音的首字母
    private String sortLetters = "#";
    // 类型 联系人，组
    private int type = -1;
    // 子类型
    private int subType = -1;
    
    private String data1 = null;
    private String data2 = null;

    public int getId()
    {
        return id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getSubType()
    {
        return subType;
    }

    public void setSubType(int subType)
    {
        this.subType = subType;
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelected(boolean isSelected)
    {
        this.isSelected = isSelected;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getSortLetters()
    {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters)
    {
        this.sortLetters = sortLetters;
    }

//    public int getNumber()
//    {
//        return number;
//    }
//
//    public void setNumber(int number)
//    {
//        this.number = number;
//    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getIconRes()
    {
        return iconRes;
    }

    public void setIconRes(int iconRes)
    {
        this.iconRes = iconRes;
    }

    public void triggerSelected()
    {
        isSelected = !isSelected;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean isChecked)
    {
        this.isChecked = isChecked;
    }

    public String getNameLetters()
    {
        return nameLetters;
    }

    public void setNameLetters(String nameLetters)
    {
        this.nameLetters = nameLetters;
    }

    public String[] getNameLettersArray()
    {
        return nameLettersArray;
    }

    public void setNameLettersArray(String[] nameLettersArray)
    {
        this.nameLettersArray = nameLettersArray;
    }

    @Override
    public BaseListItem clone()
    {
        try
        {
            return (BaseListItem) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String getData1()
    {
        return data1;
    }

    public void setData1(String data1)
    {
        this.data1 = data1;
    }

    public String getData2()
    {
        return data2;
    }

    public void setData2(String data2)
    {
        this.data2 = data2;
    }

//    @Override
//    public int describeContents()
//    {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags)
//    {
//
//        dest.writeInt(id);
//        dest.writeString(name);
//        dest.writeInt(number);
//        dest.writeInt(position);
//        dest.writeString(content);
//        dest.writeString(nameLetters);
//
//        if (nameLettersArray == null)
//        {
//            dest.writeString("");
//        }
//        else
//        {
//            dest.writeInt(nameLettersArray.length);
//            dest.writeStringArray(nameLettersArray);
//        }
//        dest.writeInt(iconRes);
//        dest.writeByte((byte) (isChecked ? 1 : 0));
//        dest.writeByte((byte) (isSelected ? 1 : 0));
//        dest.writeString(sortLetters);
//
//    }
//
//    public static final Parcelable.Creator<BaseListItem> CREATOR = new Creator<BaseListItem>()
//    {
//
//        @Override
//        public BaseListItem[] newArray(int size)
//        {
//            return new BaseListItem[size];
//        }
//
//        @Override
//        public BaseListItem createFromParcel(Parcel source)
//        {
//            BaseListItem baseListItem = new BaseListItem();
//            baseListItem.id = source.readInt();
//            baseListItem.name = source.readString();
//            baseListItem.number = source.readInt();
//            baseListItem.position = source.readInt();
//            baseListItem.content = source.readString();
//            baseListItem.nameLetters = source.readString();
//            int length = source.readInt();
//            String[] nameLettersArray = null;
//            if (length > 0)
//            {
//                nameLettersArray = new String[length];
//                source.readStringArray(nameLettersArray);
//            }
//            baseListItem.nameLettersArray = nameLettersArray;
//            baseListItem.iconRes = source.readInt();
//            baseListItem.isSelected = source.readByte() != 0;
//            baseListItem.isChecked = source.readByte() != 0;
//            baseListItem.sortLetters = source.readString();
//            
//            return baseListItem;
//        }
//    };

}
