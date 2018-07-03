package com.jiaxun.sdk.acl.module.common.callback;
/**
 * 说明：系统服务链接事件监听器
 *
 * @author  hubin
 *
 * @Date 2015-1-11
 */
public interface AclCommonEventListener
{
    /**
     * 方法说明 : 设置夜服结果响应
     * @param enable: 夜服开/关
     * @param result 0：成功, -1：失败
     * @author hubin
     * @Date 2015-1-23
     */
    void onNightServiceAck(boolean enable, int result);

    /**
     * 方法说明 : 线路状态通知
     * @param linkStatus 1: OK  2: E1-LOS  3:  E1-AIS  4: E1-RAI  5: U-activating  6: U-nonActive  7: ETH-linkdown
     * @param serviceStatus 1: Active 2:Standby 3:Offline
     * @author hubin
     * @Date 2015-1-23
     */
    void onLineStatusChange(int[] linkStatus, int[] serviceStatus);
    
    /**
     * 方法说明 : 录音录像ID通知
     * @param sessionId 会话Id
     * @param recordId 录音录像ID
     * @author hubin
     * @Date 2015-1-23
     */
    void onRecordIdNotify(String sessionId, int recordId);

    /**
     * 方法说明 : 线路切换,当发生线路切换， 需要释放全部业务会话，并重新订阅用户状态
     * @param activeLine 0: no avtiveline  1:switch to line1  2:switch to line2 
     * @author hubin
     * @Date 2015-1-23
     */
    void onLineSwitch(int activeLine);
}
