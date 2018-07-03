/**
 * obtain all kind of sessions objects form Session pools,
 * and recycle session objects
 */
package com.jiaxun.sdk.scl.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import android.text.TextUtils;

import com.jiaxun.sdk.acl.model.AccountConfig;
import com.jiaxun.sdk.scl.media.audio.AudioNetEq;
import com.jiaxun.sdk.scl.media.video.LocalVideoView;
import com.jiaxun.sdk.scl.model.MediaConfig;
import com.jiaxun.sdk.scl.model.ServiceConfig;
import com.jiaxun.sdk.scl.module.device.impl.SclDeviceServiceImpl;
import com.jiaxun.sdk.scl.session.conf.ConfSession;
import com.jiaxun.sdk.scl.session.scall.SCallSession;
import com.jiaxun.sdk.scl.session.vs.VsSession;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;

public class SessionManager
{
    private final static String TAG = SessionManager.class.getName();

    private final static int MAX_SCALL_SESSION = 100;
    private final static int MAX_CONF_SESSION = 100;
    private final static int MAX_VS_SESSION = 100;
//    private final static int MAX_SESSION = 300;

    // todo: add other sessions
    private static LinkedList<SCallSession> scallPool = null;
    private static LinkedList<ConfSession> confPool = null;
    private static LinkedList<VsSession> vsPool = null;

    // Session
    private static HashMap<String, Session> activeSessionMap;

    private static SessionManager sm;

    private LocalVideoView localVideoView;

    private ServiceConfig serviceConfig;
    private MediaConfig mediaConfig;
    private AccountConfig accountConfig;
    // 闭铃状态标识
    public boolean isCloseRing;
    // 存在通话中会话
    public boolean hasIncallSession;
    // 存在保持中会话
    public boolean hasHoldSession;
    // 存在被保持中会话
    public boolean hasRemoteHoldSession;
    // 存在呼出中会话
    public boolean hasRingbackSession;
    // 存在振铃中会话
    public boolean hasRingingSession;
    // 存在监控会话
    public boolean hasVsSession;

    private SessionManager()
    {
        // todo: init session pool
        scallPool = new LinkedList<SCallSession>();
        for (int i = 0; i < MAX_SCALL_SESSION; i++)
        {
            scallPool.add(new SCallSession());
        }

        confPool = new LinkedList<ConfSession>();
        for (int i = 0; i < MAX_CONF_SESSION; i++)
        {
            confPool.add(new ConfSession());
        }

        vsPool = new LinkedList<VsSession>();
        for (int i = 0; i < MAX_VS_SESSION; i++)
        {
            vsPool.add(new VsSession());
        }

        activeSessionMap = new HashMap<String, Session>();

        AudioNetEq.init();// 音频网络适应性库初始化
    }

    public void initLocalCamera()
    {
        if (localVideoView == null && mediaConfig != null)
        {
            localVideoView = new LocalVideoView(SdkUtil.getApplicationContext(), mediaConfig.videoWidth, mediaConfig.videoHeight, mediaConfig.videoFrameRate,
                    mediaConfig.videoBitRate);
        }
        SclDeviceServiceImpl.getInstance().getSclDeviceEventListener().onLocalCameraReady(localVideoView);
    }

    public LocalVideoView getLoacalCameraView()
    {
        return localVideoView;
    }

    public static SessionManager getInstance()
    {
        if (sm == null)
        {
            sm = new SessionManager();
        }

        return sm;
    }

    public ArrayList<Session> getActiveSessions()
    {
        return new ArrayList<Session>(activeSessionMap.values());
    }

    public Session getActiveSession(String sessionId)
    {
        Log.info(TAG, "ActiveSession: " + activeSessionMap.size() + " sessionId:" + sessionId);
        return activeSessionMap.get(sessionId);
    }

    public void putActiveSession(String sessionId, Session session)
    {
        Log.info(TAG, "ActiveSession: sessionId" + sessionId);
        activeSessionMap.put(sessionId, session);
    }

    public void removeActiveSession(String sessionId)
    {
        activeSessionMap.remove(sessionId);
    }

    // obtain session
    public Session obtain(int sessionType, String sessionId)
    {
        // todo: obtain session object from linkedlist according to session Type
        if (sessionType == CommonConstantEntry.SESSION_TYPE_SCALL)
        {
            SCallSession scallSession = scallPool.poll();
            if (scallSession != null)
            {
                scallSession.setSessionType(sessionType);
                scallSession.setSessionId(sessionId);
                putActiveSession(sessionId, scallSession);
            }
            return scallSession;
        }
        else if (sessionType == CommonConstantEntry.SESSION_TYPE_CONF)
        {
            ConfSession confSession = confPool.poll();
            if (confSession != null)
            {
                confSession.setSessionType(sessionType);
                confSession.setSessionId(sessionId);
                putActiveSession(sessionId, confSession);
            }
            return confSession;
        }
        else if (sessionType == CommonConstantEntry.SESSION_TYPE_VS)
        {
            VsSession vsSession = vsPool.poll();
            if (vsSession != null)
            {
                vsSession.setSessionType(sessionType);
                vsSession.setSessionId(sessionId);
                putActiveSession(sessionId, vsSession);
            }
            return vsSession;
        }
        // todo: add other process
        else
        {
            throw new IllegalArgumentException("Illegal sessionType: " + sessionType);
        }
    }

    // recycle session
    public void recycle(Session session)
    {
        if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_SCALL)
        {
            scallPool.add(new SCallSession());
        }
        else if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_CONF)
        {
            confPool.add(new ConfSession());
        }
        else if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_VS)
        {
            vsPool.add(new VsSession());
        }
        removeActiveSession(session.getSessionId());
        session = null;
//		int sessionType = session.sessionType;
//		// 
//		if(sessionType == Session.SESSION_SCALL)
//		{
//			sCallPool.addFirst((SCallSession)session);
//		}
//		// todo: add other session process
//		else
//		{
//			throw new IllegalArgumentException("Illegal sessionType: "+ sessionType);
//		}
//				
    }

    public ServiceConfig getServiceConfig()
    {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig)
    {
        try
        {
            Log.info(TAG, "setServiceConfig:: ");
//            if (this.serviceConfig != null && serviceConfig.isNightServiceEnable != this.serviceConfig.isNightServiceEnable)
//            {
//                Log.info(TAG, "setNightService: " + serviceConfig.isNightServiceEnable);
//                AclServiceFactory.getAclCommonService().setNightService(serviceConfig.isNightServiceEnable);
//            }
            this.serviceConfig = serviceConfig;
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    public MediaConfig getMediaConfig()
    {
        return mediaConfig;
    }

    public void setMediaConfig(MediaConfig mediaConfig)
    {
        Log.info(TAG, "setMediaConfig:: ");
        this.mediaConfig = mediaConfig;
    }

    public AccountConfig getAccountConfig()
    {
        return accountConfig;
    }

    public void setAccountConfig(AccountConfig accountConfig)
    {
        Log.info(TAG, "setAccountConfig:: ");
        this.accountConfig = accountConfig;
    }

    /**
     * 方法说明 : 用于发起一路新呼叫或恢复一路原有呼叫，需要将现在进行的呼叫做保持或退出处理
     * @param exceptSessionId 处理时过滤掉的sessionId
     * @throws Exception
     * @return boolean 发出了业务请求，如invite
     * @author hubin
     * @Date 2015-7-17
     * hz delete ，2015/9/14
     */
//    public boolean handleConnectedCalls(String exceptSessionId) throws Exception
//    {
//        if (TextUtils.isEmpty(exceptSessionId))
//        {
//            return false;
//        }
//
//        for (Session session : getActiveSessions())
//        {
//            if (exceptSessionId.equals(session.getSessionId()))
//            {
//                continue;
//            }
//
//            if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_SCALL)
//            {
//                if (((SCallSession) session).currentState instanceof SCallConnectState)
//                {
//                    Log.info(TAG, "handleConnectedCalls holdCall:: number:" + ((SCallSession) session).getCallModel().getPeerNum());
//                    ((SCallSession) session).holdCall(session.getSessionId());
//                    return true;
//                }
//                else if (((SCallSession) session).currentState instanceof SCallRingbackState)
//                {
//                    Log.info(TAG, "handleConnectedCalls releaseCall:: number:" + ((SCallSession) session).getCallModel().getPeerNum());
//                    ((SCallSession) session).releaseCall(session.getSessionId(), CommonConstantEntry.Q850_PREEMPTION);
//                    return false;
//                }
//            }
//            else if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_CONF)
//            {
//                if (((ConfSession) session).currentState instanceof ConfConnectState)
//                {
//                    Log.info(TAG, "handleConnectedCalls leaveConf:: session:" + session.getSessionId());
//                    ((ConfSession) session).exitConf(session.getSessionId());
//                    return true;
//                }
//            }
//        }
//        Log.info(TAG, "handleConnectedCalls:: nothing done ");
//        return false;
//    }

    /**
     * 方法说明 : 更新当前会话状态
     * @param exceptSessionId
     * @return boolean
     * @author hubin
     * @Date 2015-9-14
     */
    public void updateSessionState(String exceptSessionId)
    {
        if (TextUtils.isEmpty(exceptSessionId))
        {
            return;
        }

        hasHoldSession = false;
        hasIncallSession = false;
        hasRemoteHoldSession = false;
        hasRingbackSession = false;
        hasRingingSession = false;
        hasVsSession = false;

        for (Session session : SessionManager.getInstance().getActiveSessions())
        {
            if (exceptSessionId.equals(session.getSessionId()))
            {
                continue;
            }

            if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_SCALL)
            {
                if (session.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
                {
                    hasIncallSession = true;
                }
                else if (session.getStatus() == CommonConstantEntry.SCALL_STATE_HOLD)
                {
                    hasHoldSession = true;
                }
                else if (session.getStatus() == CommonConstantEntry.SCALL_STATE_BOTH_HOLD || session.getStatus() == CommonConstantEntry.SCALL_STATE_REMOTE_HOLD)
                {
                    hasRemoteHoldSession = true;
                }
                else if (session.getStatus() == CommonConstantEntry.SCALL_STATE_RINGBACK)
                {
                    hasRingbackSession = true;
                }
                else if (session.getStatus() == CommonConstantEntry.SCALL_STATE_RINGING)
                {
                    hasRingingSession = true;
                }
            }
            else if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_CONF && session.getStatus() != CommonConstantEntry.CONF_STATE_EXIT)
            {
                if (session.getStatus() == CommonConstantEntry.CONF_STATE_CONNECT)
                {
                    hasIncallSession = true;
                }
                else if (session.getStatus() == CommonConstantEntry.CONF_STATE_EXIT)
                {
                    hasHoldSession = true;
                }
            }
            else if (session.getSessionType() == CommonConstantEntry.SESSION_TYPE_VS)
            {
                if (session.getStatus() == CommonConstantEntry.VS_STATE_OPEN)
                {
                    hasVsSession = true;
                }
            }
        }
    }

    /**
     * 方法说明 : 当前会话是否处于闭铃状态
     * @return boolean
     * @author hubin
     * @Date 2015-9-14
     */
    public boolean isCloseRing()
    {
        return isCloseRing;
    }

    /**
     * 方法说明 : 设置闭铃状态
     * @param isCloseRing
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setCloseRing(boolean isCloseRing)
    {
        this.isCloseRing = isCloseRing;
    }
}
