package com.jiaxun.sdk.dcl.module.callRecord.itf;

import java.util.ArrayList;

import android.content.Context;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.module.callRecord.callback.DclCallRecordEventListener;

/**
 * 说明：通话记录业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface DclCallRecordService
{
    /**
     * 方法说明 : 注册通话记录通知回调
     * @param callRecordEventListener
     * @return void
     * @author hubin
     * @Date 2015-9-10
     */
    void regCallRecordEventListener(DclCallRecordEventListener callRecordEventListener);

    /**
     * 方法说明 : 获取所有通话记录
     * @return ArrayList<CallRecord>
     * @author hubin
     * @Date 2015-7-16
     */
    ArrayList<CallRecord> getAllCallRecords();

    /**
     * 方法说明 : 插入通话记录
     * @param callRecord
     * @return boolean
     * @author hubin
     * @Date 2015-7-16
     */
    boolean insertCallRecord(CallRecord callRecord);

    /**
     * 方法说明 : 删除多个通话记录
     * @param callRecords 要删除的记录
     * @return int
     * @author hubin
     * @Date 2015-7-16
     */
    int removeCallRecords(ArrayList<CallRecord> callRecords);

    /**
     * 方法说明 : 删除全部通话记录
     * @return int 操作成功个数
     * @author hubin
     * @Date 2015-2-9
     */
    int removeAll();

    /**
     * 方法说明 : 获取某一号码的所有相关通话记录
     * @param callNum
     * @return ArrayList<CallRecord>
     * @author hubin
     * @Date 2015-7-16
     */
    ArrayList<CallRecord> getCallRecords(String callNum);

    /**
     * 方法说明 : 获取指定会议的所有会议成员的通话记录
     * @param confId 会议id
     * @return ArrayList<CallRecord>
     * @author hubin
     * @Date 2015-7-16
     */
    ArrayList<CallRecord> getConfCallRecordList(String confId);

    /**
    * 方法说明 :导出筛选出来的通话记录
     * @param url 文件路径
     * @param fileName 文件名字
     * @param callRecordListItems 要导出的记录
     * @throws Throwable
     * @author chaimb
     * @Date 2015-7-17
     */
    void exportCallRecord(ArrayList<CallRecord> callRecords, String url, String fileName) throws Throwable;

    /**
     * 方法说明 :播放本地录音
     * @param fileName 录音文件名字  通话建立时间格式化，作为文件名字(yyyyMMddHHmmss)
     * @author chaimb
     * @Date 2015-7-20
     */
    void playLocalRecordFile(Context context, String fileName);

    /**
     * 方法说明 :从服务器下载指定通话的录音录像文件
     * @param context 页面上下文
     * @param peerNum 对端号码
     * @param recordId 录音录像ID
     * @param recordServer 录音录像服务器    
     * @return
     * @author chaimb
     * @Date 2015-7-21
     */
    boolean downloadRecordFile(Context context, String peerNum, String recordId, String recordServer);

    /**
     * 方法说明 :播放服务端录音录像文件
     * @param context 页面上下文
     * @param peerNum 对端号码
     * @param recordId 录音录像ID
     * @param recordServer 录音录像服务器    
     * @return
     * @author chaimb
     * @Date 2015-7-21
     */
    boolean playRemoteRecordFile(Context context, String peerNum, String recordId, String recordServer);

    /**
     * 方法说明 :停止下载文件
     * @author chaimb
     * @Date 2015-7-21
     */
    void stopDownload(String recordId);
}
