package com.jiaxun.sdk.acl.module.device.itf;

import com.jiaxun.sdk.acl.module.device.callback.AclDeviceEventListener;

/**
 * 说明：设备控制接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface AclDeviceService
{
    /**
     * 方法说明 : 注册视频监控事件回调
     * @param callback 监控事件回调
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int deviceRegEventListener(AclDeviceEventListener callback);

    /**
     * 方法说明 : 云镜控制
     * @param deviceNum 被控制设备号码
     * @param command 云镜控制指令
     * @param commandPara1 控制指令为方向控制指令（上、下、左、右、左上、左下、右上、右下等）时，此参数代表横向运动速度，取值范围为[1，9]，1为最低速度，9为最高速度；如果控制指令为预置位相关指令时，此参数代表预置位编号，取值范围为[1，128]；
     * @param commandPara2 方向控制指令（上、下、左、右、左上、左下、右上、右下等）代表纵向运动速度，取值范围为[1，9]，1为最低速度，9为最高速度；
     * @param commandPara3 保留
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int remoteCameraControl(String sessionId, String deviceNum, int command, int commandPara1, int commandPara2, int commandPara3);
}
