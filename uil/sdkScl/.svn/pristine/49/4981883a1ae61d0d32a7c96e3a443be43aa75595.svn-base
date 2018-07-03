package com.jiaxun.sdk.scl.module.presence.callback;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 说明：用户状态订阅监听器
 *
 * @author  hubin
 *
 * @Date 2015-1-21
 */
public interface SclPresenceEventListener
{
    /**
     * 方法说明 : 订阅响应
     * @param sessionId 会话Id
     * @param result 0：成功 1：无此用户
     * @author hubin
     * @Date 2015-1-23
     */
    void onSubscribeAck(String sessionId, int result);

    /**
     * 方法说明 : 用户状态通知
     * @param presenceMap 已订阅用户号码与状态的map列表
     * @return void
     * @author hubin
     * @Date 2015-5-13
     */
    void onPresenceUserStatus(ArrayList<HashMap<String, Integer>> presenceMap);
}
