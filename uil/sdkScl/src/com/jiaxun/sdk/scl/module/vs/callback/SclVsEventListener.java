package com.jiaxun.sdk.scl.module.vs.callback;

import android.view.SurfaceView;

import com.jiaxun.sdk.scl.model.VsModel;


/**
 * ˵������Ƶ����¼�֪ͨ�ӿ�
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclVsEventListener
{
    /**
     * ����˵�� : ��Ƶ���ҵ��״̬�ı�֪ͨ
     * @param sessionId �ỰId
     * @param status  IDLE / DIAL / CONNECT 
     * @param vsModel ��ض��� 
     * @param reason ԭ��
     * @return void
     * @author hubin
     * @Date 2015-1-30
     */
    void onVsStatusChange(String sessionId, int status, VsModel vsModel, int reason);
    
    /**
     * ����˵�� : ��Ƶ������֪ͨ
     * @param sessionId �ỰId
     * @param videoUrl
     * @param videoView
     * @return void
     * @author hubin
     * @Date 2015-5-25
     */
    void onVsVideoReceived(String sessionId, String videoUrl, SurfaceView videoView);

//    /**
//     * ����˵�� : ���м�¼֪ͨ
//     * @param callRecord ͨ����ϸ��¼
//     * @author hubin
//     * @Date 2015-1-23
//     */
//    void onSclCallRecordReport(CallRecord callRecord);
}
