package com.jiaxun.sdk.acl.module.common.itf;

import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.acl.module.common.callback.AclCommonEventListener;

/**
 * ˵�����ṩ�����ӿڷ���
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclCommonService
{
    /**
     * ����˵�� : ���ù����¼�֪ͨ
     * @param callback �����¼�֪ͨ�ص�
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int regCommonEventListener(AclCommonEventListener callback);

    /**
     * ����˵�� : ����ҹ��ҵ��
     * @param nightService ����/�˳�ҹ��  
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int setNightService(boolean nightService) throws Exception;

    /**
     * ����˵�� : ����ҵ������
     * @param config ҵ������
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int updateAccountConfig(AccountConfig config) throws Exception;

    /**
     * ����˵�� : ����ACLҵ���ܷ���
     * @param config ҵ������
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-2-4
     */
    int startAclService(AccountConfig config) throws Exception;

    /**
     * ����˵�� : ֹͣACLҵ���ܷ���
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-2-4
     */
    int stopAclService() throws Exception;

}
