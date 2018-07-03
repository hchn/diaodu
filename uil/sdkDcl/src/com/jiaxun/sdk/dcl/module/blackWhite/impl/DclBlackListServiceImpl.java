package com.jiaxun.sdk.dcl.module.blackWhite.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;
import com.jiaxun.sdk.dcl.module.blackWhite.itf.DclBlackWhiteListService;
import com.jiaxun.sdk.dcl.util.EnumBWType;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues;
import com.jiaxun.sdk.dcl.util.db.DBHelper;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_BlackWhite;
import com.jiaxun.sdk.util.SdkUtil;

/**
 * ËµÃ÷£º
 *
 * @author  HeZhen
 *
 * @Date 2015-8-27
 */
public class DclBlackListServiceImpl implements DclBlackWhiteListService
{
    private Map<Integer, BlackWhiteModel> blackMap = new HashMap<Integer, BlackWhiteModel>();
    private Map<Integer, BlackWhiteModel> whiteMap = new HashMap<Integer, BlackWhiteModel>();
    private ArrayList<BlackWhiteModel> blackList = new ArrayList<BlackWhiteModel>();
    private ArrayList<BlackWhiteModel> whiteList = new ArrayList<BlackWhiteModel>();
    private ContentResolver contentResolver;
    private static DclBlackListServiceImpl instance;

    public DclBlackListServiceImpl()
    {
        this.contentResolver = SdkUtil.getApplicationContext().getContentResolver();
        loadData();
    }

    public static DclBlackListServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new DclBlackListServiceImpl();
        }
        return instance;
    }

    private void loadData()
    {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        blackMap.clear();
        blackList.clear();
        whiteMap.clear();
        whiteList.clear();
        Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_BLACK_WHITE, null);
        while (cursor.moveToNext())
        {
            BlackWhiteModel balckWhiteEntity = new BlackWhiteModel();
            balckWhiteEntity.setId(cursor.getInt(cursor.getColumnIndex(DB_BlackWhite._ID)));
            balckWhiteEntity.setContactId((cursor.getInt(cursor.getColumnIndex(DB_BlackWhite.CONTACT_ID))));
            balckWhiteEntity.setPhoneNum((cursor.getString(cursor.getColumnIndex(DB_BlackWhite.NUMBER))));
            balckWhiteEntity.setType(cursor.getInt(cursor.getColumnIndex(DB_BlackWhite.TYPE)));
            if (balckWhiteEntity.getType() == EnumBWType.BLACK_LIST)
            {
                blackMap.put(balckWhiteEntity.getId(), balckWhiteEntity);
                blackList.add(balckWhiteEntity);
            }
            else
            {
                whiteMap.put(balckWhiteEntity.getId(), balckWhiteEntity);
                whiteList.add(balckWhiteEntity);
            }
        }
        cursor.close();
    }

    @Override
    public boolean addBlack(BlackWhiteModel blackWhiteEntity)
    {
        ArrayList<BlackWhiteModel> blackWhiteList = new ArrayList<BlackWhiteModel>();
        blackWhiteList.add(blackWhiteEntity);
        return addBlacks(blackWhiteList);
    }

    @Override
    public boolean addBlacks(ArrayList<BlackWhiteModel> blackWhiteList)
    {
        if (blackWhiteList == null || blackWhiteList.size() == 0)
        {
            return false;
        }
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for (BlackWhiteModel blackWhiteEntity : blackWhiteList)
            {
                ContentValues value = new ContentValues();
                value.put(DB_BlackWhite.CONTACT_ID, blackWhiteEntity.getContactId());
                value.put(DB_BlackWhite.NUMBER, blackWhiteEntity.getPhoneNum());
                value.put(DB_BlackWhite.TYPE, blackWhiteEntity.getType());
                ops.add(ContentProviderOperation.newInsert(DB_BlackWhite.CONTENT_URI).withValues(value).withYieldAllowed(true).build());
            }
            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);

            loadData();

            return true;
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeBlack(BlackWhiteModel blackWhiteEntity)
    {
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            ops.add(ContentProviderOperation.newDelete(DB_BlackWhite.CONTENT_URI)
                    .withSelection(DB_BlackWhite._ID + "=?", new String[] { "" + blackWhiteEntity.getId() }).withYieldAllowed(true).build());

            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);

            blackList.remove(blackMap.get(blackWhiteEntity.getId()));
            blackMap.remove(blackWhiteEntity.getId());
            return true;
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAllBlack()
    {
        return false;
    }

    @Override
    public ArrayList<BlackWhiteModel> getBLackList()
    {
        return blackList;
    }

    @Override
    public boolean addWhite(BlackWhiteModel blackWhiteEntity)
    {
        ArrayList<BlackWhiteModel> blackWhiteList = new ArrayList<BlackWhiteModel>();
        blackWhiteList.add(blackWhiteEntity);
        return addWhites(blackWhiteList);
    }

    @Override
    public boolean addWhites(ArrayList<BlackWhiteModel> blackWhiteList)
    {
        if (blackWhiteList == null || blackWhiteList.size() == 0)
        {
            return false;
        }
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for (BlackWhiteModel blackWhiteEntity : blackWhiteList)
            {
                ContentValues value = new ContentValues();
                value.put(DB_BlackWhite.CONTACT_ID, blackWhiteEntity.getContactId());
                value.put(DB_BlackWhite.NUMBER, blackWhiteEntity.getPhoneNum());
                value.put(DB_BlackWhite.TYPE, blackWhiteEntity.getType());
                ops.add(ContentProviderOperation.newInsert(DB_BlackWhite.CONTENT_URI).withValues(value).withYieldAllowed(true).build());
            }
            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);

            loadData();
            return true;
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeWhite(BlackWhiteModel blackWhiteEntity)
    {
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            ops.add(ContentProviderOperation.newDelete(DB_BlackWhite.CONTENT_URI)
                    .withSelection(DB_BlackWhite._ID + "=?", new String[] { "" + blackWhiteEntity.getId() }).withYieldAllowed(true).build());

            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            whiteList.remove(blackWhiteEntity);
            whiteMap.remove(blackWhiteEntity.getId());
            return true;
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAllWhite()
    {
        return false;
    }

    @Override
    public ArrayList<BlackWhiteModel> getWhiteList()
    {
        return whiteList;
    }

    @Override
    public boolean removeAllBlackWhite()
    {
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            ops.add(ContentProviderOperation.newDelete(DB_BlackWhite.CONTENT_URI).withYieldAllowed(true).build());
            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            return true;
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public BlackWhiteModel getBlackById(int id)
    {
        return blackMap.get(id);
    }

    @Override
    public BlackWhiteModel getWhiteById(int id)
    {
        return whiteMap.get(id);
    }
}
