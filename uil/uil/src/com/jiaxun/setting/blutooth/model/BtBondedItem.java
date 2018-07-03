package com.jiaxun.setting.blutooth.model;

import android.bluetooth.BluetoothDevice;

/**
 * ËµÃ÷£º
 *
 * @author  zhangxd
 *
 * @Date 2015-7-20
 */
public class BtBondedItem extends BtBaseItem
{
    private boolean a2dpConnected=false;

    public BtBondedItem(BluetoothDevice bluetoothDevice,Boolean a2dpConnected)
    {
        super(bluetoothDevice, BtItemType.BOND_BONDED);
        this.a2dpConnected=a2dpConnected;
        // TODO Auto-generated constructor stub
    }
    public boolean getA2dpConnected()
    {
        return a2dpConnected;
    }
    
    public void setA2dpConnected(boolean a2dpConnected)
    {
        this.a2dpConnected=a2dpConnected;
    }
}
