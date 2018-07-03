package com.jiaxun.sdk.acl.module.scall.itf;

import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;

/**
 * ˵��������ҵ���ܽӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclSCallService
{
    /**
     * ����˵�� : ע�ᵥ���¼��ص�
     * @param callback
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallRegEventListener(AclSCallEventListener callback);
    
    /**
     * ����˵�� : �����µĺ���
     * ���ó���: �û�����������С�
     * @param sessionId ���лỰID
     * @param callPriority �������ȼ�����Χ��0-4��-1��ʾ�������ȼ�
     * @param callerNum ���к���
     * @param callerName �������ƣ�ֵ��Ա��
     * @param funcCode GSM-R������
     * @param calleeNum ���к���
     * @param channel ����ͨ���ţ�1,2��
     * @param audio ֧����Ƶ
     * @param video ֧����Ƶ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallMake(String sessionId, int callPriority, String callerNum, String callerName, String funcCode, String calleeNum, int channel, boolean audio, boolean video) throws Exception;

    /**
     * ����˵�� : ��������֪ͨ
     * ���ó���: �û��յ��º��к���Զ˸�֪����״̬
     * @param sessionId ���лỰID
     * @param name ֵ��Ա���ն�����
     * @param channel ͨ���ţ�1,2��
     * @param sendRbt �����ͻ��壺��183
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallAlerting(String sessionId, String name, int channel, boolean sendRbt) throws Exception;
    
    /**
     * ����˵�� : Ӧ�����
     * ���ó������û���Ӧ���������н���������
     * @param sessionId ���лỰID
     * @param name ֵ��Ա���ն�����
     * @param channel ����ͨ���ţ�1,2��
     * @param audio ֧����Ƶ
     * @param video ֧����Ƶ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallAnswer(String sessionId, String name, int channel, boolean audio, boolean video) throws Exception;

    /**
     * ����˵�� : ����ͨ��
     * ���ó��������йҶϡ�����ȡ�������оܽӵȡ�
     * @param sessionId ���лỰID
     * @param reason �ͷ�ԭ��1:ϵͳæ,2:�һ�,3:�ܾ�,4:�����
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int sCallRelease(String sessionId, int reason) throws Exception;

    /**
     * ����˵�� : ���б���
     * ���ó���: �û�����ĳ������
     * @param sessionId ���лỰID
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
     int sCallHold(String sessionId) throws Exception;

    /**
     * ����˵�� : ���лָ�
     * ���ó���: �û��ָ�ĳ�����С�
     * @param sessionId ���лỰID
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
   int sCallRetrieve(String sessionId) throws Exception;
   
   /**
    * ����˵�� : ����sip info���͵�DTMF
    * @param sessionId
    * @param c
    * @throws Exception
    * @return void
    * @author hubin
    * @Date 2015-2-5
    */
   void sCallSipInfoDTMFSend(String sessionId, char c) throws Exception;
   /**
    * ����˵�� : ����inband���͵�DTMF
    * @param sessionId
    * @param c
    * @throws Exception
    * @return void
    * @author hubin
    * @Date 2015-2-5
    */
    void sCallInbandDTMFSend(String sessionId, char c) throws Exception;
    
//    /**
//     * ����˵�� : Զ�˱���֤ʵ
//     * ���ó���: �յ�Զ�˱������󣬷��ͱ�����Ӧ��
//     * @param sessionId ���лỰID
//     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
//     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
//     *         CommonConstantEntry.PARAM_ERROR����������
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    int sCallRemoteHoldAck(String sessionId) throws Exception;
//
//    /**
//     * ����˵�� : Զ�˽������֤ʵ
//     * ���ó���: �յ�Զ�˽���������󣬷��ͽ��������Ӧ��
//     * @param sessionId ���лỰID
//     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
//     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
//     *         CommonConstantEntry.PARAM_ERROR����������
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    int sCallRemoteRetrieveAck(String sessionId) throws Exception;
}
