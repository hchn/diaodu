package com.jiaxun.setting.model;

import com.jiaxun.setting.model.PrefsBaseItem.ItemCallBack;

/**
 * 
 * 说明：boolean开关类型
 *
 * @author  zhangxd
 *
 * @Date 2015-6-1
 */
public class PrefsBooleanItem extends PrefsBaseItem
{
    private boolean checked = false;

    public PrefsBooleanItem(String name, int imageId, String key, Boolean checked, ItemCallBack itemCallBack)
    {
        this.name = name;
        this.imageId = imageId;
        this.key = key;
        this.itemCallBack = itemCallBack;
        this.checked = checked;
        this.itemType = PrefItemType.BOOLEAN;
    }

    public PrefsBooleanItem(String name, int imageId, String key, Boolean checked)
    {
        this(name, imageId, key, checked, null);
    }

    public boolean getChecked()
    {
        return checked;
    }

    public void setChecked(boolean checked)
    {
        this.checked = checked;
    }

}
