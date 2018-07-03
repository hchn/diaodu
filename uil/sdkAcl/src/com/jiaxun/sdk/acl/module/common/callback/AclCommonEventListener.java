package com.jiaxun.sdk.acl.module.common.callback;
/**
 * ˵����ϵͳ���������¼�������
 *
 * @author  hubin
 *
 * @Date 2015-1-11
 */
public interface AclCommonEventListener
{
    /**
     * ����˵�� : ����ҹ�������Ӧ
     * @param enable: ҹ����/��
     * @param result 0���ɹ�, -1��ʧ��
     * @author hubin
     * @Date 2015-1-23
     */
    void onNightServiceAck(boolean enable, int result);

    /**
     * ����˵�� : ��·״̬֪ͨ
     * @param linkStatus 1: OK  2: E1-LOS  3:  E1-AIS  4: E1-RAI  5: U-activating  6: U-nonActive  7: ETH-linkdown
     * @param serviceStatus 1: Active 2:Standby 3:Offline
     * @author hubin
     * @Date 2015-1-23
     */
    void onLineStatusChange(int[] linkStatus, int[] serviceStatus);
    
    /**
     * ����˵�� : ¼��¼��ID֪ͨ
     * @param sessionId �ỰId
     * @param recordId ¼��¼��ID
     * @author hubin
     * @Date 2015-1-23
     */
    void onRecordIdNotify(String sessionId, int recordId);

    /**
     * ����˵�� : ��·�л�,��������·�л��� ��Ҫ�ͷ�ȫ��ҵ��Ự�������¶����û�״̬
     * @param activeLine 0: no avtiveline  1:switch to line1  2:switch to line2 
     * @author hubin
     * @Date 2015-1-23
     */
    void onLineSwitch(int activeLine);
}
