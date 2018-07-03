package com.jiaxun.sdk.dcl.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：提供数据库创建更新等操作服务
 *
 * @author  hubin
 *
 * @Date 2015-3-17
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String TAG = DBHelper.class.getName();
    
    public static final int DB_VERSION = 1; // 数据库版本

    public static final String DB_NAME = "t30.db"; // 数据库名称

    private static DBHelper mDBHelper;

    public synchronized static DBHelper getInstance()
    {
        if (mDBHelper == null)
        {
            mDBHelper = new DBHelper(SdkUtil.getApplicationContext());
        }
        return mDBHelper;
    }
    
    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_USER + " ("
                + DBConstantValues.DB_User._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_User.NAME + " TEXT,"
                 + DBConstantValues.DB_User.LOGIN + " TEXT,"
                + DBConstantValues.DB_User.PASSWORD + " TEXT,"
                + DBConstantValues.DB_User.PRIORITY + " INTEGER,"
                + DBConstantValues.DB_User.LAST_LOGIN_TIME + " TEXT,"
                + DBConstantValues.DB_User.LAST_LOGOFF_TIME + " TEXT,"
                + DBConstantValues.DB_User.LOGIN_TIMES + " INTEGER)");
        
        db.execSQL("insert into " + DBConstantValues.TB_NAME_USER + "(" + DBConstantValues.DB_User.NAME + ", " + DBConstantValues.DB_User.LOGIN
                + ", " + DBConstantValues.DB_User.PASSWORD + "," + DBConstantValues.DB_User.PRIORITY + ") values('管理员','admin','admin',1)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_CUSTOM_CONTACT + " ("
                + DBConstantValues.DB_Contact._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_Contact.USER_ID + " STRING,"
                + DBConstantValues.DB_Contact.NAME + " STRING,"
                + DBConstantValues.DB_Contact.NUMBER + " INTEGER,"
                + DBConstantValues.DB_Contact.SUBSCRIBE + " INTEGER,"
                + DBConstantValues.DB_Contact.CONF_NUM + " TEXT,"
                + DBConstantValues.DB_Contact.TYPE_NAME + " STRING)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_GROUP + " ("
                + DBConstantValues.DB_Group._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_Group.GROUP_NAME + " STRING,"
                + DBConstantValues.DB_Group.GROUP_TYPE + " INTEGER,"
                + DBConstantValues.DB_Group.GROUP_NUM + " INTEGER,"
                + DBConstantValues.DB_Group.POSITION + " INTEGER,"
                + DBConstantValues.DB_Group.PARENT_ID + /*" INTEGER,"
                + DBConstantValues.DB_Group.ROOT_ID +*/ " INTEGER)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_CONTACT_GROUP + " ("
                + DBConstantValues.DB_Contact_Group._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_Contact_Group.GROUP_ID + " INTEGER,"
                + DBConstantValues.DB_Contact_Group.CONTACT_ID + " INTEGER,"
                + DBConstantValues.DB_Contact_Group.CONTACT_POSITION + " INTEGER)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_CONTACT_DATA + " ("
                + DBConstantValues.DB_Contact_Data._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_Contact_Data.CONTACT_ID + " STRING,"
                + DBConstantValues.DB_Contact_Data.DATA_TYPE + " STRING,"
                + DBConstantValues.DB_Contact_Data.DATA + " TEXT)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_BLACK_WHITE + " ("
                + DBConstantValues.DB_Contact_Data._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_BlackWhite.CONTACT_ID + " STRING,"
                + DBConstantValues.DB_BlackWhite.NUMBER + " STRING,"
                + DBConstantValues.DB_BlackWhite.TYPE + " INTEGER)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_CALL_RECORD + " ("
                + DBConstantValues.DB_Call_Record._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_Call_Record.CALLER_NUM + " NVARCHAR(28),"
                + DBConstantValues.DB_Call_Record.CALLER_NAME + " NVARCHAR(28),"
                + DBConstantValues.DB_Call_Record.PEER_NUM + " NVARCHAR(28),"
                + DBConstantValues.DB_Call_Record.PEER_NAME + " NVARCHAR(28),"
                + DBConstantValues.DB_Call_Record.FUNC_CODE + " NVARCHAR(28),"
                + DBConstantValues.DB_Call_Record.CALL_TYPE + " INTEGER,"
                + DBConstantValues.DB_Call_Record.CALL_PRIORITY + " INTEGER,"
                + DBConstantValues.DB_Call_Record.RELEASE_REASON + " INTEGER,"
                + DBConstantValues.DB_Call_Record.CALL_START_TIME + " INTEGER,"
                + DBConstantValues.DB_Call_Record.CONNECT_START_TIME + " INTEGER,"
                + DBConstantValues.DB_Call_Record.RELEASE_TIME + " INTEGER,"
                + DBConstantValues.DB_Call_Record.DURATION + " INTEGER,"
                + DBConstantValues.DB_Call_Record.OUTGOING + " INTEGER,"
                + DBConstantValues.DB_Call_Record.ATD_NAME + " NVARCHAR,"
                + DBConstantValues.DB_Call_Record.CHAIRMAN + " INTEGER,"
                + DBConstantValues.DB_Call_Record.CONF_ID + " INTEGER,"
                + DBConstantValues.DB_Call_Record.CONFNAME + " NVARCHAR,"
                + DBConstantValues.DB_Call_Record.RECORD_TASK_ID + " NVARCHAR,"
                + DBConstantValues.DB_Call_Record.RECORD_SERVER + " NVARCHAR,"
                + DBConstantValues.DB_Call_Record.RECORD_FILE + " NVARCHAR,"
                + DBConstantValues.DB_Call_Record.USER_TYPE + " INTEGER,"
                + DBConstantValues.DB_Call_Record.CIRCUIT_SWITCH + " INTEGER)");
        
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstantValues.TB_NAME_DATA_TYPE + " ("
                + DBConstantValues.DB_DataType._ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstantValues.DB_DataType.DATA_IDENT + " INTEGER,"
                + DBConstantValues.DB_DataType.TYPE_NAME + " STRING)");
//        
//        db.execSQL("insert into " + DBConstantValues.TB_NAME_DATA_TYPE + "(" 
//                + DBConstantValues.DB_DataType.DATA_IDENT + ", "
//                + DBConstantValues.DB_DataType.TYPE_NAME + 
//                ") values("+DataType.CONTACT_IDENT+",'"+DataType.CONTACT_TYPE_1+"')");
//        db.execSQL("insert into " + DBConstantValues.TB_NAME_DATA_TYPE + "(" 
//                + DBConstantValues.DB_DataType.DATA_IDENT + ", "
//                + DBConstantValues.DB_DataType.TYPE_NAME +
//                ") values("+DataType.CONTACT_IDENT+",'"+DataType.CONTACT_TYPE_2+"')");
////      
//        db.execSQL("insert into " + DBConstantValues.TB_NAME_DATA_TYPE + "(" 
//                + DBConstantValues.DB_DataType.DATA_IDENT + ", "
//                + DBConstantValues.DB_DataType.TYPE_NAME + 
//                ") values("+DataType.PHONE_IDENT+",'"+DataType.PHONE_TYPE_1+"')");
//        db.execSQL("insert into " + DBConstantValues.TB_NAME_DATA_TYPE + "(" 
//                + DBConstantValues.DB_DataType.DATA_IDENT + ", "
//                + DBConstantValues.DB_DataType.TYPE_NAME + 
//                ") values("+DataType.PHONE_IDENT+",'"+DataType.PHONE_TYPE_2+"')");
      
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.info(TAG, "oldVersion:" + oldVersion + " , newVersion:" + newVersion);
//        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_USER);
//        db.execSQL("DROP TABLE IF EXISTS " + TB_NAME_CUSTOM_CONTACT);
    }

}
