package com.jiaxun.sdk.dcl.module.callRecord.impl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import android.text.TextUtils;

import com.jiaxun.sdk.dcl.handler.DataLooperHandler;
import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.module.callRecord.callback.DclCallRecordEventListener;
import com.jiaxun.sdk.dcl.module.callRecord.itf.DclCallRecordService;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Call_Record;
import com.jiaxun.sdk.dcl.util.db.DBHelper;
import com.jiaxun.sdk.util.MemerInfo;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.file.FileManager;
import com.jiaxun.sdk.util.httpdownload.HttpDownload;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：通话记录业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public class DclCallRecordServiceImpl implements DclCallRecordService
{
    private static final String TAG = DclCallRecordServiceImpl.class.getName();
    private static DclCallRecordServiceImpl instance;

//    private HttpDownload httpDownload;

    private FileManager fileManager;

    private Map<String, HttpDownload> downloadFileMap = new HashMap<String, HttpDownload>();

    // 录音录像存放路径
//    public final static String RECORDFILEPATH = Environment.getExternalStorageDirectory() + "/T30/recordFile";
//    public final static String RECORDFILEPATH = CommonConfigEntry. + "/T30/recordFile";

    // 所有通话记录的内存list
    private ArrayList<CallRecord> callRecordLists = new ArrayList<CallRecord>();

    // 所有相同号码的记录及同一会议的成员
    private Map<String, ArrayList<CallRecord>> callRecordMap = new HashMap<String, ArrayList<CallRecord>>();

    private DBHelper mDBHelper;

    private DclCallRecordEventListener callRecordEventListener;

    private Context context;

    private ExportCallRecord exportCallRecord;
    private String resultUrl;

    private DclCallRecordServiceImpl()
    {
        this.context = SdkUtil.getApplicationContext();
        this.mDBHelper = DBHelper.getInstance();
        this.fileManager = FileManager.getInstance();
        File file = new File(CommonConfigEntry.TEST_RECORD_FILE_PATH);
        if (!file.exists())
        {
            file.mkdirs();
        }
        loadDBCallRecords();
    }

    public static DclCallRecordServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new DclCallRecordServiceImpl();
        }
        return instance;
    }

    @Override
    public void regCallRecordEventListener(DclCallRecordEventListener callRecordEventListener)
    {
        this.callRecordEventListener = callRecordEventListener;
    }

    public DclCallRecordEventListener getCallRecordEventListener()
    {
        return callRecordEventListener;
    }

    private ExportCallRecord getExportCallRecord()
    {
        if (exportCallRecord == null)
        {
            exportCallRecord = new ExportCallRecord();
        }
        return exportCallRecord;
    }

    private void loadDBCallRecords()
    {
        Log.info(TAG, "loadDBCallRecords");
        // 加载数据库获取呼叫记录
        synchronized (mDBHelper)
        {
            SQLiteDatabase db = mDBHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_CALL_RECORD + " order by "
                    + DBConstantValues.DB_Call_Record.CALL_START_TIME + " desc", null);

            while (cursor.moveToNext())
            {

                CallRecord callRecord = new CallRecord(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CALLER_NAME)),
                        cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CALLER_NUM)));
                callRecord.setPeerNum(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.PEER_NUM)));
                callRecord.setPeerName(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.PEER_NAME)));
                callRecord.setFuncCode(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.FUNC_CODE)));
                callRecord.setCallType(cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CALL_TYPE)));
                callRecord.setCallPriority(cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CALL_PRIORITY)));
                callRecord.setReleaseReason(cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.RELEASE_REASON)));
                callRecord.setStartTime(cursor.getLong(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CALL_START_TIME)));
                callRecord.setConnectTime(cursor.getLong(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CONNECT_START_TIME)));
                callRecord.setReleaseTime(cursor.getLong(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.RELEASE_TIME)));
                int outgoing = cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.OUTGOING));
                if (outgoing == 0)
                {
                    callRecord.setOutGoing(false);
                }
                else if (outgoing == 1)
                {
                    callRecord.setOutGoing(true);
                }
                else
                {
                    // 数据为null时
                }
                callRecord.setAtdName(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.ATD_NAME)));
                int chairman = cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CHAIRMAN));
                if (chairman == 0)
                {
                    callRecord.setChairman(false);
                }
                else if (chairman == 1)
                {
                    callRecord.setChairman(true);
                }
                else
                {
                    // 数据为null时
                }
                callRecord.setConfId(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CONF_ID)));
                callRecord.setConfName(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CONFNAME))); // 会议名称
                callRecord.setRecordTaskId(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.RECORD_TASK_ID)));
                callRecord.setRecordServer(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.RECORD_SERVER)));
                callRecord.setRecordFile(cursor.getString(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.RECORD_FILE)));
                callRecord.setUserType(cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.USER_TYPE)));
                int circuitswitch = cursor.getInt(cursor.getColumnIndex(DBConstantValues.DB_Call_Record.CIRCUIT_SWITCH));
                if (circuitswitch == 0)
                {
                    callRecord.setCircuitSwitch(false);
                }
                else if (circuitswitch == 1)
                {
                    callRecord.setCircuitSwitch(true);
                }
                else
                {
                    // 数据为null时
                }

                // 加载数据库时区分会议和单呼

                // 判断是不是会议
                int callType = callRecord.getCallType();

                if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                {
                    if (!(callRecord.isChairman()))
                    {// 不是主席
                        addRecordToMap(callRecord);
                    }
                    else
                    {// 是会议加入记录内存列表
                        callRecordLists.add(0, callRecord);
                    }
                }
                else
                {

//                    if (callRecordList != null && callRecordList.size() > 0)
//                    {
//                        if (peerNum.equals(callRecordList.get(0).getPeerNum()))
//                        {// 与上一个号码相同
//                            int count = callRecordList.get(0).getCount();
//                            count++;
//                            callRecord.setCount(count);
//                            callRecordList.set(0, callRecord);
//                        }
//                        else
//                        {// 与上一个号码不相同
//                            callRecordList.add(0, callRecord);
//                        }
//                    }
//                    else
//                    {
//                        callRecordList.add(0, callRecord);
//                        Log.info(TAG, "callRecordList is null!!");
//                    }

                    callRecordLists.add(0, callRecord);
                    addRecordToMap(callRecord);
                }
            }
            cursor.close();
        }
    }

    // 获取全部呼叫记录(统计相邻相同记录次数)
    @Override
    public ArrayList<CallRecord> getAllCallRecords()
    {
        Log.info(TAG, "getAllCallRecords");

        return (ArrayList<CallRecord>) callRecordLists.clone();
    }

    @Override
    public boolean insertCallRecord(CallRecord callRecord)
    {
        // 向数据库中的表中插入记录
        Log.info(TAG, "insertCallRecord");
        if (callRecord == null)
        {
            return false;
        }

        synchronized (mDBHelper)
        {

            // 根据内存数据的size得到记录
            long count = callRecordLists.size();
            Log.info(TAG, "count::" + count);

            if (count >= CommonConfigEntry.CALLRECORD_NUM_MAX)
            {
                ArrayList<ContentProviderOperation> opsDel = new ArrayList<ContentProviderOperation>();
                CallRecord cRecord = callRecordLists.get(CommonConfigEntry.CALLRECORD_NUM_MAX - 1);
                int callType = cRecord.getCallType();

                if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                {// 会议
                    String confId = cRecord.getConfId();
                    // 得到相同会议id的会议成员list
                    ArrayList<CallRecord> confList = callRecordMap.get(confId);
                    if (confList != null && confList.size() > 0)
                    {
                        for (CallRecord call : confList)
                        {
                            String peerNum = call.getPeerNum();
                            // 把得到的list加入要删除的事务中
                            opsDel.add(ContentProviderOperation.newDelete(DB_Call_Record.CONTENT_URI)
                                    .withSelection(DB_Call_Record.PEER_NUM + "=?", new String[] { peerNum }).withYieldAllowed(true).build());
                        }
                    }
                    else
                    {
                        Log.info(TAG, "confList is null");
                    }

                }
                else
                {// 监控或单呼
                    String peerNum = cRecord.getPeerNum();
                    opsDel.add(ContentProviderOperation.newDelete(DB_Call_Record.CONTENT_URI)
                            .withSelection(DB_Call_Record.PEER_NUM + "=?", new String[] { peerNum }).withYieldAllowed(true).build());
                }
                try
                {// 执行删除操作
                    context.getContentResolver().applyBatch(DBConstantValues.AUTHORITY, opsDel);
                    callRecordLists.remove(CommonConfigEntry.CALLRECORD_NUM_MAX - 1);
                }
                catch (RemoteException e)
                {
                    Log.exception(TAG, e);
                }
                catch (OperationApplicationException e)
                {
                    Log.exception(TAG, e);
                }
            }
        }

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentValues value = new ContentValues();
        value.put(DB_Call_Record.CALLER_NUM, callRecord.getCallerNum()); // 本端号码
        value.put(DB_Call_Record.CALLER_NAME, callRecord.getCallerName()); // 本端名称
        value.put(DB_Call_Record.PEER_NUM, callRecord.getPeerNum()); // 对端号码
        value.put(DB_Call_Record.PEER_NAME, callRecord.getPeerName()); // 对端名称
        value.put(DB_Call_Record.FUNC_CODE, callRecord.getFuncCode()); // 功能号码
        value.put(DB_Call_Record.CALL_TYPE, callRecord.getCallType()); // 呼叫类型（单呼、组呼等）
        value.put(DB_Call_Record.CALL_PRIORITY, callRecord.getCallPriority()); // 呼叫等级（紧急、普通呼叫等）
        value.put(DB_Call_Record.RELEASE_REASON, callRecord.getReleaseReason()); // 释放原因
        value.put(DB_Call_Record.CALL_START_TIME, callRecord.getStartTime()); // 呼叫开始时间
        value.put(DB_Call_Record.CONNECT_START_TIME, callRecord.getConnectTime()); // 通话开始时间
        value.put(DB_Call_Record.RELEASE_TIME, callRecord.getReleaseTime()); // 呼叫结束时间
        value.put(DB_Call_Record.OUTGOING, callRecord.isOutGoing()); // 呼叫方向（呼入、呼出）
        value.put(DB_Call_Record.ATD_NAME, callRecord.getAtdName()); // 值班员名字
        value.put(DB_Call_Record.CHAIRMAN, callRecord.isChairman()); // 是否是主席
        value.put(DB_Call_Record.CONF_ID, callRecord.getConfId()); // ?
        value.put(DB_Call_Record.CONFNAME, callRecord.getConfName()); // 会议名称
        value.put(DB_Call_Record.RECORD_TASK_ID, callRecord.getRecordTaskId()); // 录音录像id
        value.put(DB_Call_Record.RECORD_SERVER, callRecord.getRecordServer()); // 录音录像文件服务器地址
        value.put(DB_Call_Record.RECORD_FILE, callRecord.getRecordFile()); // 录音文件URI
        value.put(DB_Call_Record.USER_TYPE, callRecord.getUserType()); // 对方用户类型（监控，用户）
        value.put(DB_Call_Record.CIRCUIT_SWITCH, callRecord.isCircuitSwitch()); // 是否电路域电话
        ops.add(ContentProviderOperation.newInsert(DB_Call_Record.CONTENT_URI).withValues(value).withYieldAllowed(true).build());
        try
        {
            context.getContentResolver().applyBatch(DBConstantValues.AUTHORITY, ops);

            // 判断是不是会议
            int callType = callRecord.getCallType();
            if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {
                if (!(callRecord.isChairman()))
                {// 不是主席
                    Log.info(TAG, "conference member::callType::" + callType);
                    addRecordToMap(callRecord);

                }
                else
                {// 是主席
                    callRecordLists.add(0, callRecord);
                    callRecordEventListener.onDclCallRecordAdd(callRecord);
                }
            }
            else
            {

                callRecordLists.add(0, callRecord);
                addRecordToMap(callRecord);
                callRecordEventListener.onDclCallRecordAdd(callRecord);
            }

        }
        catch (RemoteException e)
        {
            Log.exception(TAG, e);
        }
        catch (OperationApplicationException e)
        {
            Log.exception(TAG, e);
        }
        return true;
    }

    @Override
    public int removeCallRecords(ArrayList<CallRecord> callRecordList)
    {
        // 删除多条记录
        Log.info(TAG, "removeCallRecords");
        ArrayList<ContentProviderOperation> ops = null;
        if (callRecordList == null)
        {
            return 0;
        }
        else
        {
            try
            {
                // 删除数据库中的记录
                ops = new ArrayList<ContentProviderOperation>();
                for (int i = 0; i < callRecordList.size(); i++)
                {

                    String peerNum = "" + callRecordList.get(i).getPeerNum();
                    ops.add(ContentProviderOperation.newDelete(DB_Call_Record.CONTENT_URI)
                            .withSelection(DB_Call_Record.PEER_NUM + "=?", new String[] { peerNum }).withYieldAllowed(true).build());

                }

                context.getContentResolver().applyBatch(DBConstantValues.AUTHORITY, ops);
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
            // 移除本地内存中的记录
            for (CallRecord callRecordListItem : callRecordList)
            {
                callRecordList.remove(callRecordListItem);
            }

        }
        if (ops != null && ops.size() > 0)
        {
            return ops.size();
        }
        else
        {
            return 0;
        }

    }

    @Override
    public int removeAll()
    {
        // 删除全部记录
        Log.info(TAG, "removeAll");
        int result = 0;

        // 清除表中所有记录
        synchronized (mDBHelper)
        {
            SQLiteDatabase db = mDBHelper.getWritableDatabase();

            result = db.delete(DBConstantValues.TB_NAME_CALL_RECORD, null, null);

        }
        // 清除所有内存记录
        callRecordLists.clear();
        return result;
    }

    /**
     * 
     * 方法说明 :向map中加入单呼或监控记录或会议成员list
     * @param callRecord
     * @author chaimb
     * @Date 2015-7-8
     */
    private void addRecordToMap(CallRecord callRecord)
    {
//        Log.info(TAG, "addRecordToMap");
        if (callRecord == null)
        {// 添加对话为空，直接返回
            return;
        }
        int callType = callRecord.getCallType();
        String peerNum = callRecord.getPeerNum();
        String confId = callRecord.getConfId();

        boolean isExists = false;
        ArrayList<CallRecord> tempList = new ArrayList<CallRecord>();
        tempList.add(callRecord);

        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {// 会议
            if (callRecordMap == null || callRecordMap.size() == 0)
            {
                callRecordMap.put(confId, tempList);
            }
            else
            {
                for (Map.Entry<String, ArrayList<CallRecord>> entry : callRecordMap.entrySet())
                {
                    if (confId.equals(entry.getKey()))
                    {
                        entry.getValue().add(0, callRecord);
                        isExists = true;
                        break;
                    }
                }
                if (!isExists)
                {
                    callRecordMap.put(confId, tempList);
                }
            }
        }
        else
        {// 监控或单呼
            if (callRecordMap == null || callRecordMap.size() == 0)
            {
                callRecordMap.put(peerNum, tempList);
            }
            else
            {
                for (Map.Entry<String, ArrayList<CallRecord>> entry : callRecordMap.entrySet())
                {
                    if (peerNum.equals(entry.getKey()))
                    {
                        entry.getValue().add(0, callRecord);
                        isExists = true;
                        break;
                    }
                }
                if (!isExists)
                {
                    callRecordMap.put(peerNum, tempList);
                }
            }
        }
    }

    private String getRecordFileUrl(final String recordId, final String recordServer, final String peerNum)
    {

        Thread thread = new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                HttpResponse httpResponse = null;
                String urlRequest = recordServer + "/ListFile?dir=" + recordId + "&usrname=" + peerNum;
                Log.info(TAG, urlRequest);

                try
                {
                    HttpGet httpGet = new HttpGet(urlRequest);
                    httpResponse = new DefaultHttpClient().execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200)
                    {
                        Log.info(TAG, "Internet request success!!");
                        String result = EntityUtils.toString(httpResponse.getEntity());

                        // 根据结果得到在线播放下载链接
                        resultUrl = getRecordFile(result);

                    }
                }
                catch (Exception e)
                {
                    Log.exception(TAG, e);
                }
            }
        });
        thread.start();

        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            Log.exception(TAG, e);
        }
        return resultUrl;
    }

    private String getRecordFile(String result)
    {
        if ("".equals(result) || result == null)
        {
            return "";
        }
        Log.info(TAG, result);
        String downloadRecordFile = "";
        String downloadRecordFileSize = "";
        // 用"&"符号进行切割得到结果（String数组）
        String downloadtemp[] = result.split("&");
        // 下载的临时链接
        String downloadRecordTemp = "";
        // 下载文件的临时大小
        String downloadRecordSizeTemp = "";
        // 得到包含"*.mp4"的字符串
        for (int i = 0; i < downloadtemp.length; i++)
        {
            if (downloadtemp[i].contains(".mp4"))
            {
                downloadRecordTemp = downloadtemp[i];
//                break;
            }
            if (downloadtemp[i].contains(".idx"))
            {
                downloadRecordSizeTemp = downloadtemp[i];
//                break;
            }
        }
        if (downloadRecordTemp == null || "".equals(downloadRecordTemp))
        {
            // 为空 不执行下载
            return "";
        }

        String downloadsizetemp[] = downloadRecordSizeTemp.split("filename");

        // 得到下载录音录像的大小
        for (int i = 0; i < downloadsizetemp.length; i++)
        {
            if (downloadsizetemp[i].contains("size"))
            {
                downloadRecordFileSize = downloadsizetemp[i];
                break;
            }
        }

        // 用"="符号进行切割包含"*.mp4"的字符串得到mp4文件路径
        String downloadtempone[] = downloadRecordTemp.split("=");
        for (int i = 0; i < downloadtempone.length; i++)
        {
            if (downloadtempone[i].contains(".mp4"))
            {
                downloadRecordFile = downloadtempone[i];
                break;
            }
        }
        if (downloadRecordFile == null || "".equals(downloadRecordFile))
        {
            // 为空 不执行下载
            return "";
        }
        Log.info(TAG, downloadRecordFile);

        return downloadRecordFile + "&" + downloadRecordFileSize;
    }

    /**
     * 返回单个文件大小
     * 方法说明 :
     * @param file
     * @return
     * @author chaimb
     * @Date 2015-6-16
     */
    private long getFileSizes(File file)
    {
        long size = 0;
        if (file.exists())
        {
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream(file);
                size = fis.available();
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
        return size;
    }

    /**
     * 对已经下载的录音录像按照时间进行排序
     * 方法说明 :
     * @return
     * @author chaimb
     * @Date 2015-6-16
     */
    private File[] sortTimeRecordFile()
    {
        Log.info(TAG, "sortTimeRecordFile::");
        File recordFiles[] = fileManager.getFileLists(CommonConfigEntry.TEST_RECORD_FILE_PATH);
        for (int i = 0; i < recordFiles.length; i++)
        {
            String filename = recordFiles[i].getName();

            if (!recordFiles[i].isDirectory() && filename.trim().toLowerCase().endsWith(".mp4"))
            {

                for (int j = i; j < recordFiles.length; j++)
                {
                    if (recordFiles[i].lastModified() > recordFiles[j].lastModified())
                    {
                        File temp = recordFiles[i];
                        recordFiles[i] = recordFiles[j];
                        recordFiles[j] = temp;
                    }
                }
            }
        }

//        for (int i = 0; i < recordFiles.length; i++)
//        {
//            Log.info(TAG, recordFiles[i].getName() + "时间:" + recordFiles[i].lastModified());
//        }

        return recordFiles;
    }

    private boolean checkSpaceForRecord(long spaceSize)
    {

        Log.info(TAG, "delRecordFile" + spaceSize);

        if (spaceSize <= 0)
        {
            return true;
        }

        if (spaceSize < MemerInfo.getSdAvailableSize(context))
        {// SD卡内存够用
            return true;
        }
        else
        {// SD卡内存不足时，删除最早的录音录像文件（循环覆盖方式）
            File[] recordFiles = sortTimeRecordFile();
            long currentSize = 0;
            for (int i = 0; i < recordFiles.length; i++)
            {
                currentSize += getFileSizes(recordFiles[i]);
//                String filename = recordFiles[i].getName();
//                Log.info(TAG, "filename=" + filename + "size=" + currentSize);
                if (spaceSize < currentSize)
                {
                    recordFiles[i].delete();
                    return true;
                }
                else
                {
                    recordFiles[i].delete();
//                    currentSize += getFileSizes(recordFiles[i + 1]);
                }
            }
        }

        return false;
    }

    @Override
    public ArrayList<CallRecord> getCallRecords(String callNum)
    {
        Log.info(TAG, "getSubCallRecordList::peerNum::" + callNum);
        ArrayList<CallRecord> subRecordList = new ArrayList<CallRecord>();

        if ("".equals(callNum) || callNum == null)
        {// 如果参数为空，直接返回null
            return null;
        }

        for (Map.Entry<String, ArrayList<CallRecord>> entry : callRecordMap.entrySet())
        {
            if (callNum.equals(entry.getKey()))
            {
                subRecordList = entry.getValue();
                break;
            }
        }
        return subRecordList;
    }

    @Override
    public ArrayList<CallRecord> getConfCallRecordList(String confId)
    {
        Log.info(TAG, "getSubCallRecordList::confId::" + confId);
        if ("".equals(confId) || confId == null)
        {// 如果参数为空，直接返回null
            return null;
        }
        ArrayList<CallRecord> confRecordList = new ArrayList<CallRecord>();
        for (Map.Entry<String, ArrayList<CallRecord>> entry : callRecordMap.entrySet())
        {
            if (confId.equals(entry.getKey()))
            {
                confRecordList = entry.getValue();
                break;
            }
        }
        return confRecordList;
    }

    @Override
    public void exportCallRecord(final ArrayList<CallRecord> callRecords, final String url, final String fileName) throws Throwable
    {
        Log.info(TAG, "exportContacts::url:" + url + ",fileName:" + fileName);
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(fileName))
        {
            DataLooperHandler.getInstance().post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (getExportCallRecord().exportToCSV(callRecords, url, fileName))
                    {
                        callRecordEventListener.onDclCallRecordExport(1);
                    }
                }
            });
        }
        else
        {
            callRecordEventListener.onDclCallRecordExport(0);
        }
    }

    @Override
    public void playLocalRecordFile(Context context, String fileName)
    {
        Log.info(TAG, "fileName::" + fileName);
        if (TextUtils.isEmpty(fileName))
        {// 文件名字为空，直接返回
            return;
        }

        if (fileManager.hasLocalFile(CommonConfigEntry.TEST_RECORDEINGS_PATH, fileName, ".wav"))
        {
            Intent intent = new Intent();
            intent.setAction("com.jiaxun.action_video_player");
            intent.addCategory("com.jiaxun.category_video_player");
            intent.putExtra("filePath", CommonConfigEntry.TEST_RECORDEINGS_PATH + fileName + ".wav");
            context.startActivity(intent);
        }
        else
        {
            Log.error(TAG, "record file not exist!");
        }

    }

    @Override
    public boolean downloadRecordFile(Context context, String peerNum, String recordId, String recordServer)
    {
        Log.info(TAG, "downloadRecordFile::recordId" + recordId);
        String recordFileName = peerNum + recordId;
        if (TextUtils.isEmpty(recordFileName))
        {// 文件名字为空，直接返回
            return false;
        }
        if (fileManager.hasLocalFile(CommonConfigEntry.TEST_RECORD_FILE_PATH, recordFileName, ".wav"))
        {
            Log.error(TAG, "record file already exist!");
            return false;
        }

        // 录音录像id为空不进行下载
        if (TextUtils.isEmpty(recordId))
        {
            Log.error(TAG, "recordId is null!");
            return false;
        }
        // 服务器地址为空不进行下载
        if (TextUtils.isEmpty(recordServer))
        {
            Log.error(TAG, "recordServer is null!");
            return false;
        }

        String downloadRecordFile = getRecordFileUrl(recordId, recordServer, peerNum);
        if (TextUtils.isEmpty(downloadRecordFile))
        {// 下载路径为空
            Log.error(TAG, "download url is null!");
            return false;
        }
        else
        {
            String[] downFileSize = downloadRecordFile.split("&size=");
            if (downFileSize == null || downFileSize.length == 0)
            {// 得到切割后的服务器地址数组为空，直接返回
                Log.info(TAG, "downFileSize is null");
            }
            String downloadFileSize = "";
            if (downFileSize.length > 1)
            {
                downloadFileSize = downFileSize[1];
            }
            String downloadFile = downloadRecordFile.split("&")[0];
            String downloadUrl = recordServer + "/Download?filename=" + downloadFile;
            Log.info(TAG, downloadUrl);

            String file = CommonConfigEntry.TEST_RECORD_FILE_PATH + "/" + recordFileName + ".mp4";

            long recordSize = 0;
            if (TextUtils.isEmpty(downloadFileSize))
            {
                recordSize = 0;
            }
            else
            {
                recordSize = Long.parseLong(downloadFileSize.replaceAll("\\D+", "").replaceAll("\r", "").replaceAll("\n", "").trim());
            }
            if (checkSpaceForRecord(recordSize))
            {

                HttpDownload httpDownload = new HttpDownload(context);
                httpDownload.download(downloadUrl, file, recordId);
                downloadFileMap.put(recordId, httpDownload);
                return true;
            }
            else
            {
                return false;
            }

        }
    }

    @Override
    public boolean playRemoteRecordFile(Context context, String peerNum, String recordId, String recordServer)
    {
        String recordFileName = peerNum + recordId;
        if (TextUtils.isEmpty(recordFileName))
        {// 文件名字为空，直接返回
            return false;
        }
        if (fileManager.hasLocalFile(CommonConfigEntry.TEST_RECORD_FILE_PATH, recordFileName, ".mp4"))
        {// 本地播放
            String filePath = CommonConfigEntry.TEST_RECORD_FILE_PATH + "/" + recordFileName + ".mp4";
            Intent intent = new Intent();
            intent.setAction("com.jiaxun.action_video_player");
            intent.addCategory("com.jiaxun.category_video_player");
            intent.putExtra("filePath", filePath);
            context.startActivity(intent);
        }
        else
        {// 在线播放
            String recordPlayFile = getRecordFileUrl(recordId, recordServer, peerNum);

            if (TextUtils.isEmpty(recordPlayFile))
            {
                return false;
            }
            if (TextUtils.isEmpty(recordServer))
            {
                return false;
            }
            // 根据":"切割得到服务器地址相关数组
            String[] serverArray = recordServer.split(":");

            if (serverArray == null || serverArray.length == 0)
            {// 得到切割后的服务器地址数组为空，直接返回
                Log.info(TAG, "serverArray is null");
                return false;
            }
            // 根据"rss_mediadata/"切割得到文件名相关数组
            String[] downRecordFileName = recordPlayFile.split("rss_mediadata/");
            if (downRecordFileName == null || downRecordFileName.length == 0)
            {// 得到切割后文件名相关数组为空，直接返回
                Log.info(TAG, "downRecordFileName is null");
                return false;
            }

            // 得到文件名
            String[] fileName = downRecordFileName[1].split("&");
            if (fileName == null || fileName.length == 0)
            {// 得到文件名数组为空，直接返回
                Log.info(TAG, "fileName is null");
                return false;
            }

            // 得到服务器地址
            String server = serverArray[1];
            if (server == null || "".equals(server))
            {
                // 得到服务器地址为空不进行在线播放,直接返回
                return false;
            }
            Log.info(TAG, server);

            String recordFilePath = "rtsp:" + server + "/" + fileName[0];
            Log.info(TAG, "recordFilePath::" + recordFilePath);
            // 在线播放
            if (recordFilePath != null && !("".equals(recordFilePath)))
            {
                Intent intent = new Intent();
                intent.setAction("com.jiaxun.action_video_player");
                intent.addCategory("com.jiaxun.category_video_player");
                intent.putExtra("filePath", recordFilePath);
                context.startActivity(intent);
            }
        }
        return false;
    }

    @Override
    public void stopDownload(String recordId)
    {
        Log.info(TAG, "stopDownload::recordId::" + recordId);
        if (TextUtils.isEmpty(recordId))
        {
            Log.info(TAG, "recordId is null");
            return;
        }
        HttpDownload httpDownload = downloadFileMap.get(recordId);
        if (httpDownload != null)
        {
            downloadFileMap.remove(recordId);
            httpDownload.halt();
        }

    }

}
