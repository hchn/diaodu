package com.jiaxun.sdk.scl.module.vs.itf;

import com.jiaxun.sdk.scl.module.vs.callback.SclVsEventListener;

/**
 * 说明：视频监控接口
 *
 * @author  hubin
 *
 * @Date 2015-1-16
 */
public interface SclVsService
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
    int vsRegEventListener(SclVsEventListener callback);

    /**
     * 方法说明 : 打开监控视频
     * @param sessionId 会话Id
     * @param priority 呼叫优先级
     * @param videoNum 前端设备号码
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int vsOpen(int priority, String videoNum);

    /**
     * 方法说明 : 关闭监控视频
     * @param sessionId 会话Id
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int vsClose(String sessionId);

}
