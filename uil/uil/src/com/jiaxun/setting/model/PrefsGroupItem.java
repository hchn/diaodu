package com.jiaxun.setting.model;

import android.os.Parcel;

/**
 * ËµÃ÷£º
 *
 * @author  zhangxd
 *
 * @Date 2015-6-4
 */
public class PrefsGroupItem extends PrefsBaseItem
{
    public PrefsGroupItem()
    {
    }

    public PrefsGroupItem(String name)
    {
        this.name = name;
        this.itemType = PrefItemType.GROUP;
    }
    
}
