package com.jiaxun.setting.blutooth.model;

import android.bluetooth.BluetoothDevice;

/**
 * ˵����
 *
 * @author  zhangxd
 *
 * @Date 2015-7-20
 */
public class BtBondedNoneItem extends BtBaseItem
{

    public BtBondedNoneItem(BluetoothDevice bluetoothDevice)
    {
        super(bluetoothDevice, BtItemType.BOND_NONE);
        // TODO Auto-generated constructor stub
    }

}
