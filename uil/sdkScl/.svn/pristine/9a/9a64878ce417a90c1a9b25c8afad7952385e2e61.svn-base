package com.jiaxun.sdk.scl.module.im.callback;

import java.net.URI;

/**
 * 说明：即时消息业务通知接口
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclImEventListener
{
    /**
     * 方法说明 : 发送消息响应
     * @param sessionId 会话Id
     * @param result 结果：0：成功 1：失败
     * @author hubin
     * @Date 2015-1-23
     */
    void onImSendMsgAck(String sessionId, int result);

    /**
     * 方法说明 : 新消息通知
     * @param sessionId 会话Id
     * @param msgPriority 优先级
     * @param callerNum 主叫号码
     * @param callerName 主叫名称
     * @param msgType 消息类型：1：文本 2：文件
     * @param text 消息描述
     * @param uri 文件链接
     * @author hubin
     * @Date 2015-1-23
     */
    void onNewMsg(String sessionId, int msgPriority, String callerNum, String callerName, int msgType, String text, URI uri);
}
