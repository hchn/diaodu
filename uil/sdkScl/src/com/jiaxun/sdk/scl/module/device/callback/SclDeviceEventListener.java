package com.jiaxun.sdk.scl.module.device.callback;

import android.view.SurfaceView;

/**
 * ˵�����豸���ƻص�
 *
 * @author  hubin
 *
 * @Date 2015-1-30
 */
public interface SclDeviceEventListener
{
    /**
     * ����˵�� : ��������ͷ��Ƶ׼�����
     * @param cameraView
     * @return void
     * @author hubin
     * @Date 2015-7-8
     */
    void onLocalCameraReady(SurfaceView cameraView);

}
