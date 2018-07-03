package com.jiaxun.sdk.acl.module.presence.itf;

import com.jiaxun.sdk.acl.module.presence.callback.AclPresenceEventListener;

/**
 * 说明：用户状态订阅业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclPresenceService
{
    /**
     * 方法说明 : 注册事件监控回调
     * @param callback 状态通知回调
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int presenceRegEventListener(AclPresenceEventListener callback);

    /**
     * 方法说明 : 用户状态订阅：上线、下线
     * @param sessionId 会话Id
     * @param user 目标用户
     * @param on 订阅/取消
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int presenceSubscribe(String[] user, boolean on) throws Exception;
    
    /**
     * 方法说明 : 取消所有订阅
     * @return boolean
     * @author hubin
     * @Date 2015-5-12
     */
    boolean cancelAllSubscribe();
}
