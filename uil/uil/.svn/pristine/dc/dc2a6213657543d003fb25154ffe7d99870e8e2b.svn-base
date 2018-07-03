package com.jiaxun.setting.blutooth.model;

import android.bluetooth.BluetoothDevice;

/**
 * 说明：
 *
 * @author  zhangxd
 *
 * @Date 2015-7-20
 */
public class BtBaseItem
{
    protected BluetoothDevice bluetoothDevice;
    protected int itemType = -1;// 类型
    public BtBaseItem()
    {
         
    }
    public BtBaseItem(int itemType)
    {
        this.itemType=itemType; 
    }
    public BtBaseItem(BluetoothDevice bluetoothDevice,int itemType)
    {
        this.bluetoothDevice=bluetoothDevice;
        this.itemType=itemType;
    }
    public BluetoothDevice getBtDevice()
    {
        return bluetoothDevice;
    }
    public void setBtDevice(BluetoothDevice bluetoothDevice)
    {
        this.bluetoothDevice=bluetoothDevice;
    }
    public int getItemType()
    {
        return itemType;
    }
    public void setItemType(int itemType)
    {
        this.itemType=itemType;
    }
}
