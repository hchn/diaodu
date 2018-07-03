package com.jiaxun.sdk.scl.session.scall;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;
import com.jiaxun.sdk.acl.module.scall.itf.AclSCallService;
import com.jiaxun.sdk.scl.module.scall.callback.SclSCallEventListener;
import com.jiaxun.sdk.scl.module.scall.impl.SclSCallServiceImpl;

/**
 * ˵��������״̬����
 *
 * @author  hubin
 *
 * @Date 2015-4-13
 */
public interface SCallStateHandler extends AclSCallEventListener
{
    AclSCallService sCallService = AclServiceFactory.getAclSCallService();
    SclSCallEventListener callback = SclSCallServiceImpl.getInstance().getSclSCallEventListener();

    /**
     * ����˵�� : �����º���
     * @param sessionId
     * @param callNum
     * @param priority
     * @param channel ͨ��
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void makeCall(String sessionId, String callNum, String callName, int priority, boolean video, int channel) throws Exception;

    /**
     * ����˵�� : �ͷź���
     * @param sessionId
     * @param reason
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void releaseCall(String sessionId, int reason) throws Exception;

    /**
     * ����˵�� : Ӧ�����
     * ���ó������û���Ӧ���������н���������
     * @param sessionId ���лỰID
     * @param name ֵ��Ա���ն�����
     * @param channel ����ͨ���ţ�1,2��
     * @param video ֧����Ƶ
     * @author hubin
     * @Date 2015-1-23
     */
    void answerCall(String sessionId, String name, int channel, boolean video) throws Exception;

    /**
     * ����˵�� : ���ֺ���
     * @param sessionId
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void holdCall(String sessionId) throws Exception;

    /**
     * ����˵�� : �ָ�����
     * @param sessionId
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void retrieveCall(String sessionId) throws Exception;

    /**
     * ����˵�� : ����/�ر�����ý��
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-10
     */
    void setAudioMute(boolean on) throws Exception;

    /**
     * ����˵�� : ����/�ر�����������
     * @param on
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-4-9
     */
    void setAudioSpeaker(boolean on) throws Exception;

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
    
    /**
     * ����˵�� : �������
     * @param sessionId ���лỰID
     * @param enable ����
     * @return void
     * @author hubin
     * @Date 2015-9-9
     */
    void setCloseRing(String sessionId, boolean enable) throws Exception;
}
