/*
 * Copyright (C) 2009 The Sipdroid Open Source Project
 * 
 * This file is part of Sipdroid (http://www.sipdroid.org)
 * 
 * Sipdroid is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.jiaxun.uil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.devicestatus.DeviceStatusInfo;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.ui.view.TopStatusPaneView;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

public class GlobalReceiver extends BroadcastReceiver
{
    private static String TAG = GlobalReceiver.class.getName();
    
    /** �����ְ���*/
    private final static String ACTION_LEFTMODULE_NUMBER_DOWN = "jiaxun.action.leftmodule.number.down";
    /** �����ְ���*/
    private final static String ACTION_RIGHTMODULE_NUMBER_DOWN = "jiaxun.action.rightmodule.number.down";
    /** ��ģ�����*/
    private final static String ACTION_LEFTMODULE_INSERT = "jiaxun.action.leftmodule.insert";
    /** ��ģ��γ�*/
    private final static String ACTION_LEFTMODULE_PULLOUT = "jiaxun.action.leftmodule.pullout";
    /** �����ᰴ��*/
    private final static String ACTION_LEFTMODULE_HANDSFREE_DOWN = "jiaxun.action.leftmodule.handsfree.down";
    /** �������ɿ�*/
    private final static String ACTION_LEFTMODULE_HANDSFREE_UP = "jiaxun.action.leftmodule.handsfree.up";
    /** ���ֱ�����*/
    private final static String ACTION_LEFTMODULE_HANDLE_INSERT = "jiaxun.action.leftmodule.handle.insert";
    /** ���ֱ��γ�*/
    private final static String ACTION_LEFTMODULE_HANDLE_PULLOUT = "jiaxun.action.leftmodule.handle.pullout";
    /** ��������*/
    private final static String ACTION_LEFTMODULE_MUTE_DOWN = "jiaxun.action.leftmodule.mute.down";
    /** �����ɿ�*/
    private final static String ACTION_LEFTMODULE_MUTE_UP = "jiaxun.action.leftmodule.mute.up";
    /** ���������*/
    private final static String ACTION_LEFTMODULE_CABLEHEADSET_INSERT = "jiaxun.action.leftmodule.cableheadset.insert";
    /** ������γ�*/
    private final static String ACTION_LEFTMODULE_CABLEHEADSET_PULLOUT = "jiaxun.action.leftmodule.cableheadset.pullout";
    
    /** ��ģ�����*/
    private final static String ACTION_RIGHTMODULE_INSERT = "jiaxun.action.rightmodule.insert";
    /** ��ģ��γ�*/
    private final static String ACTION_RIGHTMODULE_PULLOUT = "jiaxun.action.rightmodule.pullout";
    /** �����ᰴ��*/
    private final static String ACTION_RIGHTMODULE_HANDSFREE_DOWN = "jiaxun.action.rightmodule.handsfree.down";
    /** �������ɿ�*/
    private final static String ACTION_RIGHTMODULE_HANDSFREE_UP = "jiaxun.action.rightmodule.handsfree.up";
    /** ���ֱ�����*/
    private final static String ACTION_RIGHTMODULE_HANDLE_INSERT = "jiaxun.action.rightmodule.handle.insert";
    /** ���ֱ��γ�*/
    private final static String ACTION_RIGHTMODULE_HANDLE_PULLOUT = "jiaxun.action.rightmodule.handle.pullout";
    /** �Ҿ�������*/
    private final static String ACTION_RIGHTMODULE_MUTE_DOWN = "jiaxun.action.rightmodule.mute.down";
    /** �Ҿ����ɿ�*/
    private final static String ACTION_RIGHTMODULE_MUTE_UP = "jiaxun.action.rightmodule.mute.up";
    /** �Ҷ�������*/
    private final static String ACTION_RIGHTMODULE_CABLEHEADSET_INSERT = "jiaxun.action.rightmodule.cableheadset.insert";
    /** �Ҷ����γ�*/
    private final static String ACTION_RIGHTMODULE_CABLEHEADSET_PULLOUT = "jiaxun.action.rightmodule.cableheadset.pullout";
    
    
    /** ���ְ���*/
    private final static String ACTION_MODULE_NUMBER_DOWN = "jiaxun.action.module.number.down";
    
    
    public static boolean shutdownFlag;// true:�ػ�,false:����
   
    @Override
    public void onReceive(final Context context, Intent intent)
    {
        String intentAction = intent.getAction();
        Log.info(TAG, "intentAction: " + intentAction);
      
        if (intentAction.equals(Intent.ACTION_BOOT_COMPLETED))
        {// ����
            Intent newIntent = new Intent(UiApplication.getInstance(), HomeActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            UiApplication.getInstance().startActivity(newIntent);
        }
        else if (intentAction.equals(Intent.ACTION_SHUTDOWN) || intentAction.equals(Intent.ACTION_REBOOT))
        {// �ػ�
            UiApplication.getCommonService().stopSclService();
            shutdownFlag = true;
        }
        else if (intentAction.equals(ACTION_LEFTMODULE_NUMBER_DOWN))
        {// ��ͨ��ģ�鲦��
            String number = intent.getStringExtra("value");
            Log.info(TAG, intentAction + "::value:" + number);
            
            loadDial(context,intentAction,number,"0");
        }
        else if (intentAction.equals(ACTION_LEFTMODULE_NUMBER_DOWN))
        {// ��ͨ��ģ�鲦��
            String number = intent.getStringExtra("value");
            Log.info(TAG, intentAction + "::value:" + number);
            
            loadDial(context,intentAction,number,"0");
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_NUMBER_DOWN))
        {// ��ͨ��ģ�鲦��
            String number = intent.getStringExtra("value");
            Log.info(TAG, intentAction + "::value:" + number);
            loadDial(context,intentAction,"0",number);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_HANDLE_INSERT))
        {// ���ֱ�����
            DeviceStatusInfo.LEFTMODULE_HANDLE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_HANDLE:" + DeviceStatusInfo.LEFTMODULE_HANDLE);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_HANDLE_PULLOUT))
        {// ���ֱ��γ�
            DeviceStatusInfo.LEFTMODULE_HANDLE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_HANDLE:" + DeviceStatusInfo.LEFTMODULE_HANDLE);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_INSERT))
        {// ��ģ�����
            DeviceStatusInfo.LEFTMODULE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE:" + DeviceStatusInfo.LEFTMODULE);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_PULLOUT))
        {// ��ģ��γ�
            DeviceStatusInfo.LEFTMODULE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE:" + DeviceStatusInfo.LEFTMODULE);
        }
        else if (intentAction.equals(ACTION_LEFTMODULE_HANDSFREE_DOWN))
        {// �����ᰴ��
            DeviceStatusInfo.LEFTMODULE_HANDSFREE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_HANDSFREE:" + DeviceStatusInfo.LEFTMODULE_HANDSFREE);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_HANDSFREE_UP))
        {// �������ɿ�
            DeviceStatusInfo.LEFTMODULE_HANDSFREE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_HANDSFREE:" + DeviceStatusInfo.LEFTMODULE_HANDSFREE);
        }
        else if (intentAction.equals(ACTION_LEFTMODULE_MUTE_DOWN))
        {// ��������
            DeviceStatusInfo.LEFTMODULE_MUTE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_MUTE:" + DeviceStatusInfo.LEFTMODULE_MUTE);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_MUTE_UP))
        {// �����ɿ�
            DeviceStatusInfo.LEFTMODULE_MUTE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_MUTE:" + DeviceStatusInfo.LEFTMODULE_MUTE);
        }
        else if (intentAction.equals(ACTION_LEFTMODULE_CABLEHEADSET_INSERT))
        {// ���������
            DeviceStatusInfo.LEFTMODULE_CABLEHEADSET = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_CABLEHEADSET:" + DeviceStatusInfo.LEFTMODULE_CABLEHEADSET);

        }
        else if (intentAction.equals(ACTION_LEFTMODULE_CABLEHEADSET_PULLOUT))
        {// ������γ�
            DeviceStatusInfo.LEFTMODULE_CABLEHEADSET = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.LEFTMODULE_CABLEHEADSET:" + DeviceStatusInfo.LEFTMODULE_CABLEHEADSET);
        }
        //TODO ��ģ�����
        else if (intentAction.equals(ACTION_RIGHTMODULE_HANDLE_INSERT))
        {// ���ֱ�����
            DeviceStatusInfo.RIGHTMODULE_HANDLE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_HANDLE:" + DeviceStatusInfo.RIGHTMODULE_HANDLE);
            
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_HANDLE_PULLOUT))
        {// ���ֱ��γ�
            DeviceStatusInfo.RIGHTMODULE_HANDLE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_HANDLE:" + DeviceStatusInfo.RIGHTMODULE_HANDLE);
            
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_INSERT))
        {// ��ģ�����
            DeviceStatusInfo.RIGHTMODULE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE:" + DeviceStatusInfo.RIGHTMODULE);
            
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_PULLOUT))
        {// ��ģ��γ�
            DeviceStatusInfo.RIGHTMODULE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE:" + DeviceStatusInfo.RIGHTMODULE);
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_HANDSFREE_DOWN))
        {// �����ᰴ��
            DeviceStatusInfo.RIGHTMODULE_HANDSFREE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_HANDSFREE:" + DeviceStatusInfo.RIGHTMODULE_HANDSFREE);
            
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_HANDSFREE_UP))
        {// �������ɿ�
            DeviceStatusInfo.RIGHTMODULE_HANDSFREE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_HANDSFREE:" + DeviceStatusInfo.RIGHTMODULE_HANDSFREE);
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_MUTE_DOWN))
        {// �Ҿ�������
            DeviceStatusInfo.RIGHTMODULE_MUTE = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_MUTE:" + DeviceStatusInfo.RIGHTMODULE_MUTE);
            
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_MUTE_UP))
        {// �Ҿ����ɿ�
            DeviceStatusInfo.RIGHTMODULE_MUTE = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_MUTE:" + DeviceStatusInfo.RIGHTMODULE_MUTE);
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_CABLEHEADSET_INSERT))
        {// �Ҷ�������
            DeviceStatusInfo.RIGHTMODULE_CABLEHEADSET = true;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_CABLEHEADSET:" + DeviceStatusInfo.RIGHTMODULE_CABLEHEADSET);
            
        }
        else if (intentAction.equals(ACTION_RIGHTMODULE_CABLEHEADSET_PULLOUT))
        {// �Ҷ����γ�
            DeviceStatusInfo.RIGHTMODULE_CABLEHEADSET = false;
            Log.info(TAG, intentAction + "::DeviceStatusInfo.RIGHTMODULE_CABLEHEADSET:" + DeviceStatusInfo.RIGHTMODULE_CABLEHEADSET);
        }
        else if (intentAction.equals(Intent.ACTION_HEADSET_PLUG))
        {// ����״̬
            if (intent.hasExtra("state"))
            {
                if (intent.getIntExtra("state", 0) == 0)
                {
                    Log.info(TAG, intentAction + ":: headset not connected");
                    TopStatusPaneView.getInstance().setHeadsetNofity(-1);
                }
                else if (intent.getIntExtra("state", 0) == 1)
                {
                    Log.info(TAG, intentAction + "::headset connected");
                    TopStatusPaneView.getInstance().setHeadsetNofity(R.drawable.headset);
                }

            }

        }
        else if (intentAction.equals(ConnectivityManager.CONNECTIVITY_ACTION))
        {// ����仯
            onReceiverNetworkChange();
        } 
    }
    
    /**
     * �������緢���仯
     */
    private void onReceiverNetworkChange()
    {
        Log.info(TAG, "onReceiverNetworkChange::CONNECTIVITY_ACTION");
        if (!shutdownFlag)
        {// �ǹػ�
            if(UiApplication.getAtdService().isAtdLogin() && !UiApplication.isCallServerOnline)
            {// ����̨�û���½��û������
                ServiceUtils.startSdkService();// ��������
            }
        }
    }
    
    /**
     * ��ʾ������
     * ����˵�� :
     * @param action
     * @param leftnumber
     * @param rightnumber
     * @author chaimb
     * @Date 2015-7-2
     */
    
    private void loadDial(Context context,String action, String leftnumber,String rightnumber)
    {
        //TODO ��ת��������
        Bundle data = new Bundle();
        data.putString(ACTION_MODULE_NUMBER_DOWN, action);
        data.putString("leftnumber", leftnumber);
        data.putString("rightnumber", rightnumber);
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.ACTION_MOUBLE_NUMBER, data);
    }


}
