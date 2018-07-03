package com.jiaxun.sdk.acl.module.im.itf;

import java.net.URI;

import com.jiaxun.sdk.acl.module.im.callback.AclImEventListener;

/**
 * 说明：即时消息业务功能接口
 *
 * @author  hubin
 *
 * @Date 2015-1-23
 */
public interface AclImService
{
    /**
     * 方法说明 : 注册即时消息回调
     * @param callback 即时消息回调
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int imRegEventListener(AclImEventListener callback);

    /**
     * 方法说明 : 发送消息
     * @param sessionId 会话Id
     * @param msgPriority 优先级
     * @param callerNum 主叫号码
     * @param callerName 主叫名称
     * @param calleeNum 被叫号码
     * @param msgType 消息类型：1：文本 2：文件
     * @param text 消息描述
     * @param uri 文件链接
     * @return CommonConstantEntry.METHOD_SUCCESS：成功调用；
     *         CommonConstantEntry.METHOD_FAILED：方法调用失败；
     *         CommonConstantEntry.PARAM_ERROR：参数错误。
     * @author hubin
     * @Date 2015-1-23
     */
    int imSendMsg(String sessionId, int msgPriority, String callerNum, String callerName, String calleeNum, int msgType, String text, URI uri) throws Exception;

}
