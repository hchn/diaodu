package com.jiaxun.setting.model;

/**
 * ˵���������������fragment������������
 * 
 * @author zhangxd
 * 
 * @Date 2015-6-4
 */
public class PrefsLeftItem extends PrefsBaseItem
{

    public PrefsLeftItem(String name, int imageId, ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.itemCallBack = itemCallBack;
        this.itemType = PrefItemType.LEFT;
    }

}
