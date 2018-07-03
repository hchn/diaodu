package com.jiaxun.sdk.scl.module.common.callbcak;

/**
 * ˵����ͨ���¼�����
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclCommonEventListener
{
    /**
     * ����˵�� : ����ҹ�������Ӧ
     * @param result 0���ɹ�, -1��ʧ��
     * @author hubin
     * @Date 2015-1-23
     */
    void onSclNightServiceAck(boolean enable, int result);

    /**
     * ����˵�� : ��·״̬֪ͨ
     * @param linkStatus 1: OK  2: E1-LOS  3:  E1-AIS  4: E1-RAI  5: U-activating  6: U-nonActive  7: ETH-linkdown
     * @param serviceStatus 0:Offline 1: Active 2:Standby
     * @author hubin
     * @Date 2015-1-23
     */
    void onSclLineStatusChange(int[] linkStatus, int[] serviceStatus);

}
