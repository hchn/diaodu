package com.jiaxun.uil.module.surveillance.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Toast;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.scl.SclServiceFactory;
import com.jiaxun.sdk.scl.model.VsModel;
import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;
import com.jiaxun.sdk.scl.module.vs.itf.SclVsService;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.VsListItem;
import com.jiaxun.uil.module.surveillance.itf.UilVsService;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：视频监控业务功能接口实现
 *
 * @author  zhangxd
 *
 * @Date 2015-6-1
 */
public class UilVsServiceImpl implements UilVsService
{
    private static final String TAG = UilVsServiceImpl.class.getName();
    private ArrayList<VsListItem> vsList = new ArrayList<VsListItem>();// 监控用户列表，保存用户号码，轮巡过程不会被清空
    private static UilVsServiceImpl instance;
    private boolean startedLoop = false;
    private ScheduledExecutorService pool = null;
    private int windowsCount = 0;
    private int openedUserCount = 0;
    private int currentLoopTime = 0;
    private int loopTimes = 0;
    private VsListItem vsListItem;
    private VsModel vsModel;
    private int closeSuccess = 1;
    private SclVsService sclVsService;
    private Runnable loopRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            // 循环位置为0
            if (currentLoopTime == 0)
            {
                for (int i = 0; i < windowsCount; i++)
                {
                    // 要关id
                    int closeId = windowsCount * (loopTimes - 1) + i;
                    closeSuccess = 0;
                    if (closeId < vsList.size())
                    {
                        vsListItem = vsList.get(closeId);
                        // 当打开时候才关闭
                        if (vsListItem.isOpened())
                        {
                            vsModel = vsListItem.getVsModel();
                            // 移除视频窗口
                            UiUtils.removeRemoteVideo(vsModel.getVideoNum());
                            // 关闭
                            closeSuccess = sclVsService.vsClose(vsModel.getSessionId());
                        }
                    }
                    if (closeSuccess == CommonConstantEntry.METHOD_SUCCESS)
                    {
                        vsListItem = vsList.get(i);
                        vsModel = vsListItem.getVsModel();
                        vsListItem.setOpening(true);
                        sclVsService.vsOpen(UiApplication.getConfigService().getCallPriority(), vsModel.getVideoNum());
                        // 睡眠保证一个关闭一个打开
                        SystemClock.sleep(500);
                    }
                }
                currentLoopTime++;
            }
            else
            {
                if (currentLoopTime < loopTimes)
                {
                    for (int i = 0; i < windowsCount; i++)
                    {
                        // 要关id
                        int closeId = windowsCount * (currentLoopTime - 1) + i;
                        vsListItem = vsList.get(closeId);
                        closeSuccess = 0;
                        // 关闭视频窗口
                        UiUtils.removeRemoteVideo(vsListItem.getVsModel().getVideoNum());
                        closeSuccess = sclVsService.vsClose(vsListItem.getVsModel().getSessionId());
                        if (closeSuccess == CommonConstantEntry.METHOD_SUCCESS)
                        {
                            // 要打开id
                            int openId = windowsCount * currentLoopTime + i;
                            if (openId < vsList.size())
                            {
                                vsListItem = vsList.get(openId);
                                vsListItem.setOpening(true);
                                sclVsService.vsOpen(CommonConfigEntry.PRIORITY_DEF, (vsListItem.getVsModel()).getVideoNum());
                            }
                            // 睡眠保证一个关闭一个打开
                            SystemClock.sleep(500);
                        }
                    }
                    currentLoopTime++;
                    if (currentLoopTime == loopTimes)
                    {
                        currentLoopTime = 0;
                    }
                }
                else
                {
                    currentLoopTime = 0;
                }
            }
        }
    };

    public static UilVsServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new UilVsServiceImpl();
        }
        return instance;
    }

    private UilVsServiceImpl()
    {
        sclVsService = SclServiceFactory.getSclVsService();
    }

    @Override
    public void vsLoopStop()
    {
        Log.info(TAG, "vsLoopStop");
        startedLoop = false;
        pool.shutdownNow();
        pool = null;
    }

    @Override
    public void vsLoopStart(int period)
    {
        Log.info(TAG, "vsLoopStart");
        currentLoopTime = 0;
        windowsCount = UiApplication.getConfigService().getVideoWindowCount();
        // 自适应屏幕按9分屏
        if (windowsCount == UiEventEntry.SCREEN_TYPE_AUTO)
        {
            windowsCount = UiEventEntry.SCREEN_TYPE_9;
        }
     
            if (vsList.size() > windowsCount)
            {
                // 计算要循环多少次
                if (vsList.size() % windowsCount == 0)
                {
                    loopTimes = vsList.size() / windowsCount;
                }
                else
                {
                    loopTimes = vsList.size() / windowsCount + 1;
                }
                startedLoop = true;
                if (pool == null)
                {
                    pool = new ScheduledThreadPoolExecutor(1);
                    pool.scheduleAtFixedRate(loopRunnable, 0, period, TimeUnit.SECONDS);
                }
            }
            else
            {
                Toast.makeText(UiApplication.getInstance(), "用户数量不多于可用窗口数,不用轮巡", Toast.LENGTH_SHORT).show();
            } 
    }

    @Override
    public ArrayList<VsListItem> getVsUserList()
    {
        return vsList;
    }

    @Override
    public void addVsUser(String number)
    {
        Log.info(TAG, "openVs");
        if (!UiApplication.isCallServerOnline)
        {
            ToastUtil.showToast("离线状态，无法发起业务");
            return;
        }
        if (!TextUtils.isEmpty(number))
        {
            addVsUser(number, true);
        }
    }

    private void addVsUser(String number, boolean openVsFlag)
    {
        VsListItem vsListItem = null;
        boolean contain = false;
        for (VsListItem vsItem : vsList)
        {
            if (number.equals(vsItem.getVsModel().getVideoNum()))
            {
                vsListItem = vsItem;
                contain = true;
                break;
            }
        }
        if (contain)
        {
            // 已经存在 ,但没有打开
            if (!vsListItem.isOpened())
            {
                if (openVsFlag)
                {
                    vsListItem.setOpening(true);
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.UPDATE_VSLIST, vsListItem);
                    sclVsService.vsOpen(CommonConfigEntry.PRIORITY_DEF, number);
                }
            }
            else
            {
                // 已经存在 ,打开
                ToastUtil.showToast("监控已发起");
            }
        }
        else
        {
            vsListItem = new VsListItem();
            VsModel vsModel = new VsModel();
            // 根据号码获取联系人对象
            ContactModel contactEnity = UiApplication.getContactService().getContactByPhoneNum(number);
            if (contactEnity == null)
            {
                // 如果联系人中没有这个号码
                contactEnity = new ContactModel();
                contactEnity.setName(number);
                contactEnity.setStatus(0);
            }
            vsModel.setVideoNum(number);
            // 设置上线、离线状态
            vsListItem.setStatus(contactEnity.getStatus());
            vsListItem.setVsModel(vsModel);
            vsListItem.setUserName(contactEnity.getName());
            vsList.add(vsListItem);
            if (openVsFlag)
            {
                vsListItem.setOpening(true);
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.UPDATE_VSLIST, vsListItem);
                if (sclVsService.vsOpen(CommonConfigEntry.PRIORITY_DEF, number) == CommonConstantEntry.METHOD_SUCCESS)
                {
                    openedUserCount++;
                }
            }
        }
    }

    @Override
    public boolean isLoopStarted()
    {
        return startedLoop;
    }

    @Override
    public void addVsUsers(ArrayList<String> openUserNumberList)
    {
        Log.info(TAG, "openVsList");
        if (!UiApplication.isCallServerOnline)
        {
            ToastUtil.showToast("离线状态，无法发起业务");
            return;
        }
        if ((openUserNumberList == null) || (openUserNumberList.size() == 0))
        {
            return;
        }
        windowsCount = UiApplication.getConfigService().getVideoWindowCount();

        if (windowsCount == UiEventEntry.SCREEN_TYPE_AUTO)
        {
            windowsCount = UiEventEntry.SCREEN_TYPE_9;
        }
        openedUserCount = getOpenVsCount();
        for (String number : openUserNumberList)
        {
            if (openedUserCount < windowsCount)
            {
                addVsUser(number, true);
            }
            else
            {
                addVsUser(number, false);
            }
        }
    }
    @Override
    public int getOpenVsCount()
    {
        int count = 0;
        for (VsListItem vsListItem : vsList)
        {
            if (vsListItem.isOpened())
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public void deleteVsUser(String UserNumber)
    {
        Log.info(TAG, "deleteVsUser");
        if (!TextUtils.isEmpty(UserNumber))
        {
            return;
        }
        for (VsListItem vSItem : vsList)
        {
            VsModel vsModel = vSItem.getVsModel();
            // 如果监控列表中存在这个号码就从列表中移除，同时关闭视频监控
            if ((vsModel.getVideoNum()).equals(UserNumber))
            {
                if (vSItem.isOpened())
                {
                    // 关闭视频窗口
                    UiUtils.removeRemoteVideo(vsModel.getVideoNum());
                    // 关闭视频
                    sclVsService.vsClose(vsModel.getSessionId());
                }
                vsList.remove(vSItem);
            }
        }
    }

    @Override
    public void deleteVsUsers(ArrayList<String> NumberList)
    {
        Log.info(TAG, "deleteVsUsers");
        if ((NumberList == null) || (NumberList.size() == 0))
        {
            return;
        }
        for (String number : NumberList)
        {
            for (VsListItem vSItem : vsList)
            {
                VsModel vsModel = vSItem.getVsModel();
                // 如果监控列表中存在这个号码就从列表中移除，同时关闭视频监控
                if ((vsModel.getVideoNum()).equals(number))
                {
                    if (vSItem.isOpened())
                    {
                        // 关闭视频窗口
                        UiUtils.removeRemoteVideo(vsModel.getVideoNum());
                        // 关闭视频
                        sclVsService.vsClose(vsModel.getSessionId());
                    }
                    // 从列表中移除
                    vsList.remove(vSItem);
                    break;
                }
            }
        }
    }

    @Override
    public void deleteAllVsUsers()
    {
        Log.info(TAG, "deleteAllVsUsers");
        for (VsListItem vSItem : vsList)
        {
            if (vSItem.isOpened())
            {
                VsModel vsModel = vSItem.getVsModel();
                // 关闭视频窗口
                UiUtils.removeRemoteVideo(vsModel.getVideoNum());
                // 关闭视频
                sclVsService.vsClose(vsModel.getSessionId());
            }
        }

        Iterator<VsListItem> it = vsList.iterator();
        while (it.hasNext())
        {
            VsListItem vsListItem = it.next();
            if (vsListItem.isChecked() == true)
            {
                it.remove();
            }
        }
    }

    @Override
    public void closeVs(String sessionId)
    {
        sclVsService.vsClose(sessionId);
    }

    @Override
    public void regVsEventListener(SclVsEventListener callback)
    {
        sclVsService.vsRegEventListener(callback);
    }

    @Override
    public void openVs(String videoNum)
    {
        sclVsService.vsOpen(UiApplication.getConfigService().getCallPriority(), videoNum);
    }

}
