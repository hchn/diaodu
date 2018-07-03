package com.jiaxun.sdk.dcl.module.attendant.impl;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.Attendant;
import com.jiaxun.sdk.dcl.module.attendant.itf.DclAtdService;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues;
import com.jiaxun.sdk.dcl.util.db.DBHelper;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：调度台本地操作用户服务接口实现
 * 
 * @author hubin
 * 
 * @Date 2015-3-16
 */
public class DclAtdServiceImpl implements DclAtdService
{
    private static final String TAG = DclAtdServiceImpl.class.getName();

    private static DclAtdServiceImpl instance;

    private DBHelper dbHelper;
    // 是否登陆
    private boolean isLogin = false;
    // 是否管理员
    private boolean isAdmin = false;
    // 已经登录用户
    private Attendant attendant = null;

    private DclAtdServiceImpl()
    {
        dbHelper = DBHelper.getInstance();
    }

    public static DclAtdServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new DclAtdServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean isAtdLogin()
    {
        Log.info(TAG, "isAtdLogin::isLogin:" + isLogin);
        return isLogin;
    }

    @Override
    public boolean isAtdAdminLogin()
    {
        Log.info(TAG, "isAtdAdminLogin::isLogin:" + (isLogin && isAdmin));
        return isLogin && isAdmin;
    }

    @Override
    public boolean isAtdNameValid(String login)
    {
        Log.info(TAG, "isAtdNameValid::login:" + login);
        if (login == null || login.equals(""))
            return false;

        boolean auth = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        synchronized (dbHelper)
        {
            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_USER + " where " + DBConstantValues.DB_User.LOGIN + "='" + login + "'",
                    null);
            if (cursor != null)
            {
                if (cursor.moveToNext())
                {
                    auth = true;
                }
                cursor.close();
            }
        }

        return auth;
    }

    @Override
    public boolean isAtdLoginedPasswordValid(String password)
    {
        Log.info(TAG, "isAtdLoginedPasswordValid::password:" + password);
        if (TextUtils.isEmpty(password))
        {
            return false;
        }

        if (!isLogin || attendant == null)
        {
            return false;
        }

        if (password.equals(attendant.getPassword()))
        {// 验证登陆密码
            return true;
        }

        return false;
    }

    @Override
    public boolean isAtdAuthorized(String login, String password)
    {
        Log.info(TAG, "isAtdAuthorized::login:" + login + " password:" + password);
        if (login == null || login.equals("") || password == null || password.equals(""))
            return false;

        boolean auth = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        synchronized (dbHelper)
        {
            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_USER + " where " + DBConstantValues.DB_User.LOGIN + "='" + login + "' and "
                    + DBConstantValues.DB_User.PASSWORD + "='" + password + "'", null);
            if (cursor != null)
            {
                if (cursor.moveToNext())
                {
                    auth = true;

                    attendant = new Attendant();
                    attendant.setName(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User.NAME)));
                    attendant.setLogin(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User.LOGIN)));
                    attendant.setPassword(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User.PASSWORD)));
                    attendant.setPriority(cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_User.PRIORITY)));
                    Log.info(TAG, attendant.toString());

                    if (attendant.getPriority() == 1)// 管理员
                        isAdmin = true;
                    else
                        isAdmin = false;
                }
                cursor.close();
            }
        }

        isLogin = auth;
        return auth;
    }

    @Override
    public int addAtd(Attendant user)
    {
        Log.info(TAG, "addAtd::user:" + user);
        if (user == null)
        {
            return CommonConstantEntry.METHOD_FAILED;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from " + DBConstantValues.TB_NAME_USER, null);
        int count = 0;
        if (cursor.moveToFirst())
        {
            count = cursor.getInt(0);
        }
        Log.info("user's count::", count + "");
        if (count >= CommonConfigEntry.ATTEND_COUNT_MAX)
        {
            return CommonConstantEntry.OUT_ATTEND_COUNT;
        }
        else
        {

            synchronized (dbHelper)
            {
                String insertSql = "insert into " + DBConstantValues.TB_NAME_USER + "(" + DBConstantValues.DB_User.NAME + ", " + DBConstantValues.DB_User.LOGIN
                        + ", " + DBConstantValues.DB_User.PASSWORD + "," + DBConstantValues.DB_User.PRIORITY + ") values(?,?,?,?)";
                Log.info(TAG, insertSql);
                try
                {
                    db.execSQL(insertSql, new Object[] { user.getName(), user.getLogin(), user.getPassword(), user.getPriority() });
                }
                catch (SQLException e)
                {
                    Log.exception(TAG, e);
                    return CommonConstantEntry.METHOD_FAILED;
                }
            }
        }

        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public boolean removeAtd(String login)
    {
        Log.info(TAG, "removeAtd::login:" + login);
        if (TextUtils.isEmpty(login))
        {
            return false;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        synchronized (dbHelper)
        {
            String deleteSql = "delete from " + DBConstantValues.TB_NAME_USER + " where " + DBConstantValues.DB_User.LOGIN + "='" + login + "'";
            Log.info(TAG, deleteSql);
            try
            {
                db.execSQL(deleteSql);
            }
            catch (SQLException e)
            {
                Log.exception(TAG, e);
                return false;
            }
        }
        return true;
    }

    @Override
    public int modifyAtdInfo(Attendant attendant)
    {
        Log.info(TAG, "modifyAtdInfo::attendant:" + attendant);
        if (attendant == null)
        {
            return CommonConstantEntry.METHOD_FAILED;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        synchronized (dbHelper)
        {
            db = dbHelper.getWritableDatabase();
            String updateSql = "update " + DBConstantValues.TB_NAME_USER + " set " + DBConstantValues.DB_User.NAME + " = '" + attendant.getName() + "', "
                    + DBConstantValues.DB_User.LOGIN + " = '" + attendant.getLogin() + "', " + DBConstantValues.DB_User.PASSWORD + " = '"
                    + attendant.getPassword() + "', " + DBConstantValues.DB_User.PRIORITY + " = " + attendant.getPriority() + ", "
                    + DBConstantValues.DB_User.LAST_LOGIN_TIME + " = '', " + DBConstantValues.DB_User.LAST_LOGOFF_TIME + " = '', "
                    + DBConstantValues.DB_User.LOGIN_TIMES + " = '' where " + DBConstantValues.DB_User._ID + "='" + attendant.getId() + "'";
            Log.info(TAG, updateSql);
            try
            {
                db.execSQL(updateSql);
            }
            catch (SQLException e)
            {
                Log.exception(TAG, e);
                return CommonConstantEntry.METHOD_FAILED;
            }
        }

        return CommonConstantEntry.METHOD_SUCCESS;
    }

    @Override
    public ArrayList<Attendant> getAttendants()
    {
        Log.info(TAG, "getAttendants");
        ArrayList<Attendant> attendants = new ArrayList<Attendant>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        synchronized (dbHelper)
        {
            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_USER, null);
            if (cursor != null)
            {
                Attendant attendant = null;
                while (cursor.moveToNext())
                {
                    attendant = new Attendant();
                    int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User._ID)));
                    attendant.setId(id);
                    attendant.setName(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User.NAME)));
                    attendant.setLogin(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User.LOGIN)));
                    attendant.setPassword(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_User.PASSWORD)));
                    attendant.setPriority(cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_User.PRIORITY)));
                    Log.info(TAG, attendant.toString());
                    attendants.add(attendant);
                }
                cursor.close();
            }
        }

        return attendants;
    }

    @Override
    public Attendant getLoginedAttendant()
    {
        return attendant;
    }

}
