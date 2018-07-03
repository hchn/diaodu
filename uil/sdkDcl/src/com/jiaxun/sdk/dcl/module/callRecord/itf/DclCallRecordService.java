package com.jiaxun.sdk.dcl.module.callRecord.itf;

import java.util.ArrayList;

import android.content.Context;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.module.callRecord.callback.DclCallRecordEventListener;

/**
 * ˵����ͨ����¼ҵ���ܽӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface DclCallRecordService
{
    /**
     * ����˵�� : ע��ͨ����¼֪ͨ�ص�
     * @param callRecordEventListener
     * @return void
     * @author hubin
     * @Date 2015-9-10
     */
    void regCallRecordEventListener(DclCallRecordEventListener callRecordEventListener);

    /**
     * ����˵�� : ��ȡ����ͨ����¼
     * @return ArrayList<CallRecord>
     * @author hubin
     * @Date 2015-7-16
     */
    ArrayList<CallRecord> getAllCallRecords();

    /**
     * ����˵�� : ����ͨ����¼
     * @param callRecord
     * @return boolean
     * @author hubin
     * @Date 2015-7-16
     */
    boolean insertCallRecord(CallRecord callRecord);

    /**
     * ����˵�� : ɾ�����ͨ����¼
     * @param callRecords Ҫɾ���ļ�¼
     * @return int
     * @author hubin
     * @Date 2015-7-16
     */
    int removeCallRecords(ArrayList<CallRecord> callRecords);

    /**
     * ����˵�� : ɾ��ȫ��ͨ����¼
     * @return int �����ɹ�����
     * @author hubin
     * @Date 2015-2-9
     */
    int removeAll();

    /**
     * ����˵�� : ��ȡĳһ������������ͨ����¼
     * @param callNum
     * @return ArrayList<CallRecord>
     * @author hubin
     * @Date 2015-7-16
     */
    ArrayList<CallRecord> getCallRecords(String callNum);

    /**
     * ����˵�� : ��ȡָ����������л����Ա��ͨ����¼
     * @param confId ����id
     * @return ArrayList<CallRecord>
     * @author hubin
     * @Date 2015-7-16
     */
    ArrayList<CallRecord> getConfCallRecordList(String confId);

    /**
    * ����˵�� :����ɸѡ������ͨ����¼
     * @param url �ļ�·��
     * @param fileName �ļ�����
     * @param callRecordListItems Ҫ�����ļ�¼
     * @throws Throwable
     * @author chaimb
     * @Date 2015-7-17
     */
    void exportCallRecord(ArrayList<CallRecord> callRecords, String url, String fileName) throws Throwable;

    /**
     * ����˵�� :���ű���¼��
     * @param fileName ¼���ļ�����  ͨ������ʱ���ʽ������Ϊ�ļ�����(yyyyMMddHHmmss)
     * @author chaimb
     * @Date 2015-7-20
     */
    void playLocalRecordFile(Context context, String fileName);

    /**
     * ����˵�� :�ӷ���������ָ��ͨ����¼��¼���ļ�
     * @param context ҳ��������
     * @param peerNum �Զ˺���
     * @param recordId ¼��¼��ID
     * @param recordServer ¼��¼�������    
     * @return
     * @author chaimb
     * @Date 2015-7-21
     */
    boolean downloadRecordFile(Context context, String peerNum, String recordId, String recordServer);

    /**
     * ����˵�� :���ŷ����¼��¼���ļ�
     * @param context ҳ��������
     * @param peerNum �Զ˺���
     * @param recordId ¼��¼��ID
     * @param recordServer ¼��¼�������    
     * @return
     * @author chaimb
     * @Date 2015-7-21
     */
    boolean playRemoteRecordFile(Context context, String peerNum, String recordId, String recordServer);

    /**
     * ����˵�� :ֹͣ�����ļ�
     * @author chaimb
     * @Date 2015-7-21
     */
    void stopDownload(String recordId);
}
