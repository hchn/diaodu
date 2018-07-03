package com.jiaxun.uil.module.blacklist.impl;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;
import com.jiaxun.sdk.dcl.module.DclServiceFactory;
import com.jiaxun.sdk.dcl.module.blackWhite.itf.DclBlackWhiteListService;
import com.jiaxun.sdk.dcl.util.EnumBWType;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.module.blacklist.itf.UilBlackListService;
import com.jiaxun.uil.util.DispatchQueue;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * ËµÃ÷£º
 *
 * @author  HeZhen
 *
 * @Date 2015-7-9
 */
public class UilBlackListServiceImpl implements UilBlackListService
{
    private static final String TAG = UilBlackListServiceImpl.class.getName();
    private static DclBlackWhiteListService dclBlackListService;
    private static UilBlackListServiceImpl instance;

    public DispatchQueue storageQueue = new DispatchQueue("storageQueue1");

    public UilBlackListServiceImpl()
    {
        dclBlackListService = DclServiceFactory.getDclBlackListService();
        loadBWList();
    }

    public static UilBlackListServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new UilBlackListServiceImpl();
        }
        return instance;
    }

    @Override
    public void addNumToBWList(final BlackWhiteModel blackWhiteEntity)
    {
        Log.info(TAG, "addNumToBWList::blackWhiteEntity:" + blackWhiteEntity);
        if (blackWhiteEntity == null)
        {
            return;
        }
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                boolean result = false;
                if (blackWhiteEntity.getType() == EnumBWType.BLACK_LIST)
                {
                    result = dclBlackListService.addBlack(blackWhiteEntity);
                }
                else
                {
                    result = dclBlackListService.addWhite(blackWhiteEntity);
                }
                if (result)
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.ADD_BLACK_WHITE_DATA);
                }
            }

        });
    }

    @Override
    public void removeBlack(final int blackId)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                BlackWhiteModel blackWhiteEntity = dclBlackListService.getBlackById(blackId);
                if (blackWhiteEntity == null)
                {
                    return;
                }
                if (dclBlackListService.removeBlack(blackWhiteEntity))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.BLACK_WHITE_DELETE);
                }
            }

        });
    }

    @Override
    public void removeWhite(final int whiteId)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                BlackWhiteModel blackWhiteEntity = dclBlackListService.getWhiteById(whiteId);
                if (dclBlackListService.removeWhite(blackWhiteEntity))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.BLACK_WHITE_DELETE);
                }
            }

        });
    }

    @Override
    public void removeAllBlack()
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {

            }

        });
    }

    @Override
    public void removeAllWhite()
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {

            }

        });
    }

    public void loadBWList()
    {
    }

    @Override
    public ArrayList<BlackWhiteModel> getBWList(boolean black)
    {
        return black ? dclBlackListService.getBLackList() : dclBlackListService.getWhiteList();
    }

    @Override
    public void addNumToBWList(final ArrayList<BlackWhiteModel> blackWhiteList)
    {
        Log.info(TAG, "addNumToBWList::blackWhiteList:" + blackWhiteList);
        if (blackWhiteList == null || blackWhiteList.size() == 0)
        {
            return;
        }
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                dclBlackListService.addBlacks(blackWhiteList);
            }

        });
    }
}
