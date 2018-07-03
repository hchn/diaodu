package com.jiaxun.sdk.acl.module.vs.itf;

import com.jiaxun.sdk.acl.module.vs.callback.AclVsEventListener;

/**
 * ˵������Ƶ���ҵ���ܽӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclVsService
{
    /**
     * ����˵�� : ע����Ƶ����¼��ص�
     * @param callback ����¼��ص�
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int vsRegEventListener(AclVsEventListener callback);

    /**
     * ����˵�� : �򿪼����Ƶ
     * @param sessionId �ỰId
     * @param priority �������ȼ�
     * @param videoNum ǰ���豸����
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int vsOpen(String sessionId, int priority, String videoNum) throws Exception;

    /**
     * ����˵�� : �رռ����Ƶ
     * @param sessionId �ỰId
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int vsClose(String sessionId) throws Exception;

}
