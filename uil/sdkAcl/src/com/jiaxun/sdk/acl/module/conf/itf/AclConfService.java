package com.jiaxun.sdk.acl.module.conf.itf;

import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;

/**
 * ˵��������ҵ���ܽӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclConfService
{
    /**
     * ����˵�� : ע�����ҵ���¼��ص�
     * @param callback �����¼�
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confRegEventListener(AclConfEventListener callback);

    /**
     * ����˵�� : ��������
     * @param sessionId �ỰId
     * @param callPriority �������ȼ�
     * @param confType 1��ȫ˫�� 2��ͨ�� 3���㲥 
     * @param video ֧����Ƶ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confCreate(String sessionId, int callPriority, int confType, boolean video) throws Exception;

    /**
     * ����˵�� : �رջ���
     * @param sessionId �ỰId
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confClose(String sessionId) throws Exception;

    /**
     * ����˵�� : �������
     * @param sessionId �ỰId
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confEnter(String sessionId) throws Exception;

    /**
     * ����˵�� : �˳�����
     * @param sessionId �ỰId 
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confLeave(String sessionId) throws Exception;

    /**
     * ����˵�� : �����Ա
     * @param sessionId �ỰId
     * @param userNum ����
     * @param userType TERMINAL_USER = 0, �ն��û�
     *                 MOBILEPHONE_USER, �ƶ��û�
     *                 TELPHONE_USER, �̶��û�
     *                 VIDEOPHONE_USER, ���ӵ绰�û�
     *                 BXPHONE_USER, ��Я�豸�û�
     *                 MONITOR_USER, ����û�
     *                 CARPHONE_USER, �����豸�û�
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserAdd(String sessionId,  String userNum) throws Exception;

    /**
     * ����˵�� : ɾ����Ա
     * @param sessionId �ỰId
     * @param userNum �û�����
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserDelete(String sessionId, String userNum) throws Exception;

    /**
     * ����˵�� : ��Ա���Կ���
     * @param sessionId �ỰId
     * @param userNum �û�����
     * @param enable ���� / ��ֹ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserAudioEnable(String sessionId, String userNum, boolean enable) throws Exception;

    /**
     * ����˵�� : ��Ա��Ƶ����
     * @param sessionId �ỰId
     * @param userNum �û�����
     * @param enable ��ʾ / ����ʾ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserVideoEnable(String sessionId, String userNum, boolean enable) throws Exception;

    /**
     * ����˵�� : ��Ա��Ƶ����(�㲥)
     * @param sessionId �ỰId
     * @param userNum �û�����
     * @param tag ��Ƶ��ʶ
     * @param enable �㲥 / ���㲥
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confUserVideoShare(String sessionId, String userNum, String tag, boolean enable) throws Exception;

    /**
     * ����˵�� : ���鱳�����ֿ���
     * @param sessionId �ỰId
     * @param enable ���� / ֹͣ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int confBgmEnable(String sessionId, boolean enable) throws Exception;
    
    /**
     * ����˵�� : ����sip info���͵�DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    void confSipInfoDTMFSend(String sessionId, char c) throws Exception;
    /**
     * ����˵�� : ����inband���͵�DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
     void confInbandDTMFSend(String sessionId, char c) throws Exception;
}
