package com.jiaxun.sdk.dcl.util.db;

import java.util.ArrayList;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_BlackWhite;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Call_Record;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Conf;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Contact;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Contact_Data;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Contact_Group;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_DataType;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Group;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-4-3
 */
public class DBProvider extends ContentProvider
{
    public static final int CONF = 1;
    public static final int GROUP = 2;
    public static final int CONTACT = 3;
    public static final int CONTACT_GROUP = 4;
    public static final int CALL_RECORD = 5;
    public static final int CONTACT_DATA = 6;
    public static final int BLACK_WHITE = 7;
    public static final int DATA_TYPE = 8;
    private static final UriMatcher uriMatcher;
    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_CONF, CONF);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_GROUP, GROUP);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_CUSTOM_CONTACT, CONTACT);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_CONTACT_GROUP, CONTACT_GROUP);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_CALL_RECORD, CALL_RECORD);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_CONTACT_DATA, CONTACT_DATA);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_BLACK_WHITE, BLACK_WHITE);
        uriMatcher.addURI(DBConstantValues.AUTHORITY, DBConstantValues.TB_NAME_DATA_TYPE, DATA_TYPE);
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException
    {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        db.beginTransaction();
        try
        {
            ContentProviderResult[] results = super.applyBatch(operations);
            db.setTransactionSuccessful();
            return results;
        }
        finally
        {
            db.endTransaction();
        }
    }

    @Override
    public boolean onCreate()
    {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case CONF:
            case GROUP:
            case CONTACT:
            case CONTACT_GROUP:
            case CALL_RECORD:
            case CONTACT_DATA:
            case BLACK_WHITE:
            case DATA_TYPE:
                cursor = db.query(getTable(uri), projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
//            case USER_ITEM:
//                String id = uri.getPathSegments().get(1);
//                cursor = db.query(getTable(uri), projection, BaseColumns._ID
//                        + "="
//                        + id
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
//                                + ')' : ""), selectionArgs, null, null,
//                        sortOrder);
//                break;
            default:
                //super.query(uri, projection, selection, selectionArgs, sortOrder, null);
                throw new IllegalArgumentException("Unknown URI : " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case CONF:
                return DB_Conf.CONTENT_TYPE;
            case GROUP:
                return DB_Group.CONTENT_TYPE;
            case CONTACT:
                return DB_Contact.CONTENT_TYPE;
            case CONTACT_GROUP:
                return DB_Contact_Group.CONTENT_TYPE;
            case CALL_RECORD:
                return DB_Call_Record.CONTENT_TYPE;
            case CONTACT_DATA:
                return DB_Contact_Data.CONTENT_TYPE;
            case BLACK_WHITE:
                return DB_BlackWhite.CONTENT_TYPE;
            case DATA_TYPE:
                return DB_DataType.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        long rowid;
        switch (uriMatcher.match(uri))
        {
            case CONF:
            case GROUP:
            case CONTACT:
            case CONTACT_GROUP:
            case CALL_RECORD:
            case CONTACT_DATA:
            case BLACK_WHITE:
            case DATA_TYPE:
                rowid = db.insert(getTable(uri), BaseColumns._ID, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
        if (rowid > 0)
        {
            Uri noteUri = ContentUris.withAppendedId(uri, rowid);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new SQLException("Failed to insert row into " + uri);

    }

    // 获取表
    private String getTable(Uri uri)
    {
        switch (uriMatcher.match(uri))
        {
            case CONF:
                return DBConstantValues.TB_NAME_CONF;
            case GROUP:
                return DBConstantValues.TB_NAME_GROUP;
            case CONTACT:
                return DBConstantValues.TB_NAME_CUSTOM_CONTACT;
            case CONTACT_GROUP:
                return DBConstantValues.TB_NAME_CONTACT_GROUP;
            case CALL_RECORD:
                return DBConstantValues.TB_NAME_CALL_RECORD;
            case CONTACT_DATA:
                return DBConstantValues.TB_NAME_CONTACT_DATA;
            case BLACK_WHITE:
                return DBConstantValues.TB_NAME_BLACK_WHITE;
            case DATA_TYPE:
                return DBConstantValues.TB_NAME_DATA_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CONF:
            case GROUP:
            case CONTACT:
            case CONTACT_GROUP:
            case CALL_RECORD:
            case CONTACT_DATA:
            case BLACK_WHITE:
            case DATA_TYPE:
                count = db.delete(getTable(uri), selection, selectionArgs);
                break;
//            case USER_ITEM:
//                String id = uri.getPathSegments().get(1);
//                count = db.delete(getTable(uri), BaseColumns._ID
//                        + "="
//                        + id
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
//                                + ')' : ""), selectionArgs);
//                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case CONF:
            case GROUP:
            case CONTACT:
            case CONTACT_GROUP:
            case CALL_RECORD:
            case CONTACT_DATA:
            case BLACK_WHITE:
            case DATA_TYPE:
                count = db.update(getTable(uri), values, selection,
                        selectionArgs);
                break;
//            case USER_ITEM:
//                String id = uri.getPathSegments().get(1);
//                count = db.update(getTable(uri), values, BaseColumns._ID
//                        + "="
//                        + id
//                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection
//                                + ')' : ""), selectionArgs);
//                break;
            default:
                throw new IllegalArgumentException("Unknown URI : " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

}
