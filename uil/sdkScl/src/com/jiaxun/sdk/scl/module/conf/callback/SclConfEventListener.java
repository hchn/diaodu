package com.jiaxun.sdk.scl.module.conf.callback;

import com.jiaxun.sdk.scl.model.ConfModel;

/**
 * ˵���������¼�֪ͨ�ӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclConfEventListener
{
    /**
     * ����˵�� : ����״̬�ı�֪ͨ
     * @param sessionId �ỰId
     * @param status IDLE / DIAL / CONNECT/ LEAVEACK / LEAVE /ENTERACK
     * @param info ��ϯ���롢��Ա���롢�������ȼ���ͨ����Ϣ��
     * @param reason ԭ��
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onConfStatusChange(String sessionId, int status, ConfModel info, int reason);
    
    /**
     * ����˵�� : ������ʾ��������Ӧ
     * @param sessionId �ỰId
     * @param enable �� / ��
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    void onConfBgmEnable(String sessionId, boolean enable);
    

//    /**
//     * ����˵�� : ���м�¼֪ͨ
//     * @param callRecord ͨ����ϸ��¼
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    void onSclCallRecordReport(CallRecord callRecord);
}
