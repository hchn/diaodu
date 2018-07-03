package com.jiaxun.sdk.scl.module.common.callbcak;

/**
 * 说明：通用事件监听
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclCommonEventListener
{
    /**
     * 方法说明 : 设置夜服结果响应
     * @param result 0：成功, -1：失败
     * @author hubin
     * @Date 2015-1-23
     */
    void onSclNightServiceAck(boolean enable, int result);

    /**
     * 方法说明 : 线路状态通知
     * @param linkStatus 1: OK  2: E1-LOS  3:  E1-AIS  4: E1-RAI  5: U-activating  6: U-nonActive  7: ETH-linkdown
     * @param serviceStatus 0:Offline 1: Active 2:Standby
     * @author hubin
     * @Date 2015-1-23
     */
    void onSclLineStatusChange(int[] linkStatus, int[] serviceStatus);

}
