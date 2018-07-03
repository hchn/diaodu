package com.jiaxun.setting.model;

/**
 * ˵����
 *
 * @author  chaimb
 *
 * @Date 2015-9-17
 */
public class PrefsSelectFileItem extends PrefsBaseItem
{

    private String defaultValue;// Ĭ��ֵ
    private int fileType;

    public PrefsSelectFileItem(String name, int imageId, String key, String defaultValue, int fileType, ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.itemType = PrefItemType.SELECT_FILE;
        this.key = key;
        this.defaultValue = defaultValue;
        this.fileType = fileType;

    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public int getFileType()
    {
        return fileType;
    }

    public void setFileType(int fileType)
    {
        this.fileType = fileType;
    }

    
}
