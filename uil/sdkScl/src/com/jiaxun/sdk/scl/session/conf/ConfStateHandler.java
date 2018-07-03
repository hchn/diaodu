package com.jiaxun.sdk.scl.session.conf;

import java.util.ArrayList;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.module.conf.callback.AclConfEventListener;
import com.jiaxun.sdk.acl.module.conf.itf.AclConfService;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfEventListener;
import com.jiaxun.sdk.scl.module.conf.callback.SclConfUserEventListener;
import com.jiaxun.sdk.scl.module.conf.impl.SclConfServiceImpl;

/**
 * ˵��������״̬����
 *
 * @author  hubin
 *
 * @Date 2015-4-14
 */
public interface ConfStateHandler extends AclConfEventListener
{
    AclConfService confService = AclServiceFactory.getAclConfService();
    SclConfEventListener confCallback = SclConfServiceImpl.getInstance().getSclConfEventListener();
    SclConfUserEventListener userCallback = SclConfServiceImpl.getInstance().getSclConfUserEventListener();
    
    /**
     * ����˵�� : ��������
     * @param sessionId �ỰId
     * @param confName ��������
     * @param callPriority �������ȼ�
     * @param confType 1��ȫ˫�� 2��ͨ�� 3���㲥 
     * @param channel ����ͨ���ţ�1,2��
     * @param video ֧����Ƶ
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    void createConf(String sessionId, String confName, int callPriority, int confType, int channel, boolean video, ArrayList<ConfMemModel> memberList);

    /**
     * ����˵�� : �رջ���
     * @param sessionId �ỰId
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    void closeConf(String sessionId);

    /**
     * ����˵�� : �������
     * @param sessionId �ỰId
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    void returnConf(String sessionId);

    /**
     * ����˵�� : �˳�����
     * @param sessionId �ỰId 
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    void exitConf(String sessionId);

    /**
     * ����˵�� : �����Ա
     * @param sessionId �ỰId
     * @param userNum ����
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    void addUser(String sessionId, String userNum);

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
    void deleteUser(String sessionId, String userNum);

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
    void enableUserAudio(String sessionId, String userNum, boolean enable);

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
    void enableUserVideo(String sessionId, String userNum, boolean enable);

    /**
     * ����˵�� : ��Ա��Ƶ����(�㲥)
     * @param sessionId �ỰId
     * @param userNum �û�����
     * @param enable �㲥 / ���㲥
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    void shareUserVideo(String sessionId, String userNum, boolean enable);

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
    void enableConfBgm(String sessionId, boolean enable);

    /**
     * ����˵�� :  ����/���
     * @param sessionId �ỰId
     * @param mute ����/���
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-30
     */
    void setAudioMute(String sessionId, boolean mute);

    /**
     * ����˵�� :  ��Ƶ����
     * @param sessionId �ỰId
     * @param mute ����/���
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-30
     */
    void setVideoMute(String sessionId, boolean mute);
    
    /**
     * ����˵�� : ���β���
     * @param sessionId
     * @param dtmf
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-9-7
     */
    void sendDtmf(String sessionId, char dtmf) throws Exception;

}
