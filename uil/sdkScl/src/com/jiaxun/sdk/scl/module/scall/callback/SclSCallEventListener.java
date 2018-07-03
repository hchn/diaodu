package com.jiaxun.sdk.scl.module.scall.callback;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.SCallModel;

/**
 * ˵���������¼�֪ͨ�ӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclSCallEventListener
{
    /**
     * ����˵�� : ����״̬�ı�֪ͨ
     * @param sessionId �ỰId
     * @param status ����״̬��DIALP/ROCEEDING/RINGBACK/CONNECT
     *               ����״̬��RINGING/CONNECT
     *               ���������IDLE/HOLDACK/HOLD/REMOTEHOLD
     * @param info ���к��롢���к��롢�������ȼ������й����롢���й����롢������롢Ӧ����롢�ͷ�ԭ��ͨ����Ϣ��
     * @param reason ԭ��
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onSCallStatusChange(String sessionId, int status, SCallModel info, int reason);

    /**
     * ����˵�� : ������Ƶ����֪ͨ
     * @param sessionId �ỰId
     * @param localVideoView ������Ƶ����
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
//    void onSCallLocalVideoStarted(String sessionId, SurfaceView localVideoView);

    /**
     * ����˵�� : Զ����Ƶ����֪ͨ
     * @param sessionId �ỰId
     * @param remoteVideoView Զ����Ƶ����
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onSCallremoteVideoStarted(String sessionId, String number, SurfaceView remoteVideoView);
    
    /**
     * ����˵�� : Զ����Ƶ�ر�֪ͨ
     * @param sessionId �ỰId
     * @param remoteVideoView Զ����Ƶ����
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onSCallremoteVideoStoped(String sessionId, String number);
    
    /**
     * ����˵�� : Զ����������֪ͨ
     * @param sessionId �ỰId
     * @param enable ��/��
     * @return void
     * @author hubin
     * @Date 2015-9-21
     */
    void onSclCallAudioEnable(String sessionId, boolean enable);

}
