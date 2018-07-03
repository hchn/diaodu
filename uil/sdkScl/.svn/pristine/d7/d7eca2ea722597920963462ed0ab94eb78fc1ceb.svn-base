package com.jiaxun.sdk.scl.session.scall;

import com.jiaxun.sdk.acl.AclServiceFactory;
import com.jiaxun.sdk.acl.module.scall.callback.AclSCallEventListener;
import com.jiaxun.sdk.acl.module.scall.itf.AclSCallService;
import com.jiaxun.sdk.scl.module.scall.callback.SclSCallEventListener;
import com.jiaxun.sdk.scl.module.scall.impl.SclSCallServiceImpl;

/**
 * 说明：单呼状态处理
 *
 * @author  hubin
 *
 * @Date 2015-4-13
 */
public interface SCallStateHandler extends AclSCallEventListener
{
    AclSCallService sCallService = AclServiceFactory.getAclSCallService();
    SclSCallEventListener callback = SclSCallServiceImpl.getInstance().getSclSCallEventListener();

    /**
     * 方法说明 : 发起新呼叫
     * @param sessionId
     * @param callNum
     * @param priority
     * @param channel 通道
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void makeCall(String sessionId, String callNum, String callName, int priority, boolean video, int channel) throws Exception;

    /**
     * 方法说明 : 释放呼叫
     * @param sessionId
     * @param reason
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void releaseCall(String sessionId, int reason) throws Exception;

    /**
     * 方法说明 : 应答呼叫
     * 适用场景：用户响应来呼，进行接听操作。
     * @param sessionId 呼叫会话ID
     * @param name 值班员或终端名称
     * @param channel 呼出通道号（1,2）
     * @param video 支持视频
     * @author hubin
     * @Date 2015-1-23
     */
    void answerCall(String sessionId, String name, int channel, boolean video) throws Exception;

    /**
     * 方法说明 : 保持呼叫
     * @param sessionId
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void holdCall(String sessionId) throws Exception;

    /**
     * 方法说明 : 恢复保持
     * @param sessionId
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-6
     */
    void retrieveCall(String sessionId) throws Exception;

    /**
     * 方法说明 : 开启/关闭声音媒体
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-2-10
     */
    void setAudioMute(boolean on) throws Exception;

    /**
     * 方法说明 : 开启/关闭声音扬声器
     * @param on
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-4-9
     */
    void setAudioSpeaker(boolean on) throws Exception;

    /**
     * 方法说明 : 二次拨号
     * @param sessionId
     * @param dtmf
     * @throws Exception
     * @return void
     * @author hubin
     * @Date 2015-9-7
     */
    void sendDtmf(String sessionId, char dtmf) throws Exception;
    
    /**
     * 方法说明 : 闭铃控制
     * @param sessionId 呼叫会话ID
     * @param enable 开关
     * @return void
     * @author hubin
     * @Date 2015-9-9
     */
    void setCloseRing(String sessionId, boolean enable) throws Exception;
}
