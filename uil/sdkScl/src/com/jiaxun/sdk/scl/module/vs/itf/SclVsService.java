package com.jiaxun.sdk.scl.module.vs.itf;

import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;

/**
 * ˵������Ƶ��ؽӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface SclVsService
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
    int vsRegEventListener(SclVsEventListener callback);

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
    int vsOpen(int priority, String videoNum);

    /**
     * ����˵�� : �رռ����Ƶ
     * @param sessionId �ỰId
     * @return CommonConstantEntry.METHOD_SUCCESS���ɹ����ã�
     *         CommonConstantEntry.METHOD_FAILED����������ʧ�ܣ�
     *         CommonConstantEntry.PARAM_ERROR����������
     * @author hubin
     * @Date 2015-1-23
     */
    int vsClose(String sessionId);

}
