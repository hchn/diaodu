package com.jiaxun.setting.model;
/**
 * 说明： 单选
 *
 * @author  zhangxd
 *
 * @Date 2015-6-4
 */
public class PrefsGroupRadioItem extends PrefsBaseItem
{
    private String[][] radios;
    private int storeType;
    private int defaultValueInt;
    private String defaultValueString;
    
    /**
     * @param name 名字
     * @param imageId 图片id
     * @param key key
     * @param defaultValueString 字符串默认值
     * @param radios 显示选项
     * @param itemCallBack 回调
     */
    public PrefsGroupRadioItem(String name, int imageId, String key,String defaultValueString, String[][] radios,ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.key = key;
        this.radios = radios;
        this.itemCallBack = itemCallBack;
        this.itemType = PrefItemType.RADIO_GROUP;
        this.storeType=RadioGroupType.DATA_STRING;
        this.defaultValueString=defaultValueString;
    }
    /**
     * @param name 名字
     * @param imageId 图片id
     * @param key key 
     * @param defaultValueInt int默认值
     * @param radios 选型
     * @param itemCallBack 回调
     */
    public PrefsGroupRadioItem(String name, int imageId, String key,Integer defaultValueInt, String[][] radios,ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.key = key;
        this.radios = radios;
        this.itemCallBack = itemCallBack;
        this.itemType = PrefItemType.RADIO_GROUP;
        this.storeType=RadioGroupType.DATA_INTEGER;
        this.defaultValueInt=defaultValueInt;
    }

    public String[][] getRadioItems()
    {
        return radios;
    }

    public void setRadioItems(String[][] radioItems)
    {
        this.radios = radioItems;
    }

    public int getStoreType()
    {
        return storeType;
    }

    public int getDefaultValueInt()
    {
        return defaultValueInt;
    }

    public void setDefaultValueInt(int defaultValueInt)
    {
        this.defaultValueInt = defaultValueInt;
    }

    public String getDefaultValueString()
    {
        return defaultValueString;
    }

    public void setDefaultValueString(String defaultValueString)
    {
        this.defaultValueString = defaultValueString;
    }

}
