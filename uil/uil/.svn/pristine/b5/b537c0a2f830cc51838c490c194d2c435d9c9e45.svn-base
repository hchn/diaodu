package com.jiaxun.uil.handler;

import android.view.SurfaceView;

import com.jiaxun.sdk.acl.line.sip.event.NotifyEventHandler;
import com.jiaxun.sdk.scl.module.device.callback.SclDeviceEventListener;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：设备控制回调处理
 *
 * @author  hubin
 *
 * @Date 2015-9-16
 */
public class DeviceEventHandler implements SclDeviceEventListener
{
    private static final String TAG = DeviceEventHandler.class.getName();

    @Override
    public void onLocalCameraReady(SurfaceView cameraView)
    {
        Log.info(TAG, "onLocalCameraReady::");
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_CAMERA_ERADY, cameraView);
    }

}
