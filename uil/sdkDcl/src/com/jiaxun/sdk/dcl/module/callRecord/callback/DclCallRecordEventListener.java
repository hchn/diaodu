package com.jiaxun.sdk.dcl.module.callRecord.callback;

import com.jiaxun.sdk.dcl.model.CallRecord;

/**
 * ˵����ͨ����¼֪ͨ�ӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface DclCallRecordEventListener
{
    /**
     * ����˵�� : ���м�¼����֪ͨ
     * @param callRecord ͨ����ϸ��¼
     * @author hubin
     * @Date 2015-1-23
     */
    void onDclCallRecordAdd(CallRecord callRecord);
    
    /**
     * ����˵�� : ���м�¼���֪ͨ
     * @param callRecord ͨ����ϸ��¼
     * @author hubin
     * @Date 2015-1-23
     */
    void onDclCallRecordClear();
    
    /**
     * ����˵�� : ���м�¼֪ͨ
     * @param callRecord ͨ����ϸ��¼
     * @author hubin
     * @Date 2015-1-23
     */
    void onDclCallRecordExport(int result);
}
