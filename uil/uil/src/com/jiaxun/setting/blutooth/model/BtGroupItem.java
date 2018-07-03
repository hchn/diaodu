package com.jiaxun.setting.blutooth.model;

import android.bluetooth.BluetoothDevice;

/**
 * ËµÃ÷£º
 *
 * @author  zhangxd
 *
 * @Date 2015-7-20
 */
public class BtGroupItem extends BtBaseItem
{
    private String name;
    public BtGroupItem(String name )
    {
        super(BtItemType.BTGROUP);
        // TODO Auto-generated constructor stub
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
    
}
