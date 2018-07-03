package com.jiaxun.sdk.acl.line;

import com.jiaxun.sdk.acl.module.conf.itf.AclConfService;
import com.jiaxun.sdk.acl.module.device.itf.AclDeviceService;
import com.jiaxun.sdk.acl.module.im.itf.AclImService;
import com.jiaxun.sdk.acl.module.presence.itf.AclPresenceService;
import com.jiaxun.sdk.acl.module.scall.itf.AclSCallService;
import com.jiaxun.sdk.acl.module.vs.itf.AclVsService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;

/**
 * ˵������·�������ӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-11
 */
public abstract class LineAdapter implements AclSCallService, AclConfService, AclImService, AclPresenceService, AclVsService, AclDeviceService
{
    public int serviceStatus = CommonConstantEntry.SERVICE_STATUS_OFFLINE;

    public int linkStatus = CommonConstantEntry.LINK_STATUS_OK;
    
    /**
     * ����˵�� : ��ȡ����״̬��Ϣ
     * @return int
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract int getServiceStatus();

    /**
     * ����˵�� : ��ȡ��·״̬��Ϣ
     * @return int
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract int getLinkStatus();

    /**
     * ����˵�� : ����Э�����棺ҵ���ע��
     * @return boolean
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract boolean startService() throws Exception;

    /**
     * ����˵�� : ֹͣЭ�����棺ҵ���ע��
     * @return boolean
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract boolean stopService() throws Exception;

    /**
     * ����˵�� : �����������
     * @param sipAccount
     * @param sipPassword
     * @param localIp
     * @param sipServerIp
     * @param sipServerName
     * @param sipServerPort
     * @return boolean
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract boolean setServiceProfile(String sipAccount, String sipPassword, String localIp, String sipServerIp, String sipServerName, int sipServerPort)
            throws Exception;

    /**
     * ����˵�� : ����sip info���͵�DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract void sCallSipInfoDTMFSend(String sessionId, char c) throws Exception;
    /**
     * ����˵�� : ����inband���͵�DTMF
     * @param sessionId
     * @param c
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-5
     */
    public abstract void sCallInbandDTMFSend(String sessionId, char c) throws Exception;

    /**
     * ����˵�� : ����ҹ��ҵ��
     * @param nightService ����/�˳�ҹ��  
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    public abstract void setNightService(boolean nightService) throws Exception;
}
