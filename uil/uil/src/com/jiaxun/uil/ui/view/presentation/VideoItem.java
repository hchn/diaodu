package com.jiaxun.uil.ui.view.presentation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.util.ImageUtil;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.fragment.VideoPrstCtrlFragment;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：视频单个 窗口元素布局
 *
 * @author  HeZhen
 *
 * @Date 2015-7-16
 */
public class VideoItem extends RelativeLayout implements OnTouchListener, OnClickListener
{
    private static final String TAG = VideoItem.class.getName();
    private Rect rect;
    private TextView textView;
    private SurfaceView videoSurface;
    private GestureDetector gestureDetector;
    private TextView videoNum;
    private ImageView ptzCtrl;
    private ImageView presentationCtrl;
    private FrameLayout frameLayout;
    // 当前视频窗口控制栏View
    private View videoControl;
    // 摄像头正在被控制时的指令
    private int currentCameraCommand;
    // 多点触控时手势模式
    private int gestureMode;

    private float oldDist;
    // 表示当前窗口处于最大化状态
    private boolean isFullScreen;
    // 当前视频窗口对应业务标识
    private String sessionId;

    public VideoItem(Context context)
    {
        this(context, null);
        gestureDetector = new GestureDetector(context, new CameraCtrlGestureListener());
        gestureDetector.setIsLongpressEnabled(true);
        setOnTouchListener(this);
    }

    public VideoItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        addView(textView);
        textView.setVisibility(View.GONE);
        textView.setGravity(Gravity.CENTER);

        // 构建视频窗口主框架视图
        frameLayout = new FrameLayout(context);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(frameLayout, lp);
        frameLayout.setVisibility(View.GONE);

        // 构建视频窗口控制视图
        LayoutInflater inflater = LayoutInflater.from(context);
        videoControl = inflater.inflate(R.layout.view_video_control, null);
        lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        addView(videoControl, lp);
        videoNum = (TextView) videoControl.findViewById(R.id.video_number);
        ptzCtrl = (ImageView) videoControl.findViewById(R.id.ptz_control);
        presentationCtrl = (ImageView) videoControl.findViewById(R.id.presentation_control);
        ptzCtrl.setOnClickListener(this);
        presentationCtrl.setOnClickListener(this);
        videoControl.setVisibility(View.GONE);
    }

    public String getMessage()
    {
        return textView.getText().toString();
    }

    public Rect getRect()
    {
        return rect;
    }

    public boolean isInTouch(int x, int y)
    {
        if (rect == null)
        {
            return false;
        }
        if (x > rect.left && x < rect.right && y > rect.top && y < rect.bottom)
        {
            return true;
        }
        return false;
    }

    /**
     * 方法说明 : 重置窗口显示大小
     * @param rect
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setRect(Rect rect)
    {
        this.rect = rect;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
        lp.leftMargin = rect.left;
        lp.topMargin = rect.top;
        lp.width = rect.right - rect.left - 2;
        lp.height = rect.bottom - rect.top - 2;
        setLayoutParams(lp);

        postInvalidate();
    }

    /**
     * 方法说明 : 用于视频显示窗口，显示视频信息，而不是显示文字
     * @param surface
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setSurFaceView(final SurfaceView surface)
    {
        frameLayout.setVisibility(View.VISIBLE);
        if (videoSurface != null)
        {
            frameLayout.removeView(videoSurface);
            frameLayout.invalidate();
        }
        videoSurface = surface;
        if (surface == null)
        {
            videoNum.setText("");
            videoControl.setVisibility(View.GONE);
            return;
        }

        if (surface.getParent() != null)
        {
            ((ViewGroup) surface.getParent()).removeAllViews();
        }
        frameLayout.addView(surface);
        frameLayout.postInvalidate();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                videoNum.setText((String) surface.getTag(R.id.call_number));
            }
        }, 100);
    }

    public SurfaceView getSurFaceView()
    {
        return videoSurface;
    }

    /**
     * 方法说明 : 用于扩展屏控制窗口，显示文字信息，而不是显示视频
     * @param text
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void showMessage(String text)
    {
        if (!TextUtils.isEmpty(text))
        {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            videoNum.setText(text);
        }
        else
        {
            hideMessage();
        }
    }

    public void hideMessage()
    {
        textView.setText("");
        videoNum.setText("");
        textView.setVisibility(View.GONE);
        videoControl.setVisibility(View.GONE);
    }

    public String getVideoNumber()
    {
        return videoNum.getText().toString();
    }

    /**
     * 说明：云镜控制中单点多点触控手势操作
     *
     * @author hubin
     *
     * @Date 2015-9-14
     */
    class CameraCtrlGestureListener extends SimpleOnGestureListener
    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            // 单点触控情况进行单个方向操作
            if (gestureMode == 1)
            {
                // 横向的距离变化大则左右移动，纵向的变化大则上下移动
                float tanValue = 0;
                if (Math.abs(distanceX) != 0)
                {
                    tanValue = Math.abs(distanceY) / Math.abs(distanceX);
                    // 上下方向
                    if (tanValue >= 2)
                    {
                        // 向上
                        if (distanceY > 0)
                        {
                            Log.info(TAG, "onScroll:: up");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_UPWARD);
                        }
                        // 向下
                        else
                        {
                            Log.info(TAG, "onScroll:: down");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_DOWNWARD);
                        }
                    }
                    // 倾斜方向
                    else if (tanValue < 2 && tanValue > 0.5)
                    {
                        // 左上
                        if (distanceX > 0 && distanceY > 0)
                        {
                            Log.info(TAG, "onScroll:: left_up");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_LEFT_UP_MOVE);
                        }
                        // 左下
                        else if (distanceX > 0 && distanceY < 0)
                        {
                            Log.info(TAG, "onScroll:: left_down");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_LEFT_DOWN_MOVE);
                        }
                        // 右上
                        else if (distanceX < 0 && distanceY > 0)
                        {
                            Log.info(TAG, "onScroll:: right_up");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_RIGHT_UP_MOVE);
                        }
                        // 右下
                        else if (distanceX < 0 && distanceY < 0)
                        {
                            Log.info(TAG, "onScroll:: right_down");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_RIGHT_DOWN_MOVE);
                        }
                    }
                    // 水平方向
                    else
                    {
                        // 向左
                        if (distanceX > 0)
                        {
                            Log.info(TAG, "onScroll:: left");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_TURN_LEFT);
                        }
                        // 向右
                        else
                        {
                            Log.info(TAG, "onScroll:: right");
                            handleCameraControl(CommonEventEntry.CAMERA_CONTROL_TURN_RIGHT);
                        }
                    }
                }
                else
                {
                    // 向上
                    if (distanceY > 0)
                    {
                        Log.info(TAG, "onScroll:: up");
                        handleCameraControl(CommonEventEntry.CAMERA_CONTROL_UPWARD);
                    }
                    // 向下
                    else
                    {
                        Log.info(TAG, "onScroll:: down");
                        handleCameraControl(CommonEventEntry.CAMERA_CONTROL_DOWNWARD);
                    }
                }
            }
            return false;
        }
    }

    /**
     * 方法说明 : 多点触控时两点之间的距离
     * @param event
     * @return float
     * @author hubin
     * @Date 2015-6-16
     */
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /**
     * 方法说明 : 摄像头云镜控制
     * @param command 控制指令
     * @return void
     * @author hubin
     * @Date 2015-6-16
     */
    private void handleCameraControl(int command)
    {
        if (videoSurface == null)
        {
            return;
        }
        int cameraCtrlSpeed = UiApplication.getConfigService().getPtzSpeed();
        String callNum = (String) videoSurface.getTag(R.id.call_number);
        String sessionId = (String) videoSurface.getTag(R.id.session_id);
        Log.info(TAG, "handleCameraControl:: command:" + command + " currentCameraCommand:" + currentCameraCommand + " gestureMode:" + gestureMode
                + " selectedVideoNum:" + callNum + " sessionId:" + sessionId);
        // 首次进行控制，保存控制指令
        if (currentCameraCommand == 0 && command > 0)
        {
            currentCameraCommand = command;
            UiApplication.getDeviceService().remoteCameraControl(sessionId, callNum, command, cameraCtrlSpeed, cameraCtrlSpeed, -1);
        }
        // 离开屏幕，停止控制
        else if (command == 0 && currentCameraCommand > 0)
        {
            UiApplication.getDeviceService().remoteCameraControl(sessionId, callNum, currentCameraCommand - 1, cameraCtrlSpeed, cameraCtrlSpeed, -1);
            currentCameraCommand = 0;
        }
        // 对新的指令，先停止上一个操作，再进行下一个操作
        else if (command != currentCameraCommand && command > 0 && currentCameraCommand > 0)
        {
            UiApplication.getDeviceService().remoteCameraControl(sessionId, callNum, currentCameraCommand - 1, cameraCtrlSpeed, cameraCtrlSpeed, -1);
            currentCameraCommand = command;
            UiApplication.getDeviceService().remoteCameraControl(sessionId, callNum, command, cameraCtrlSpeed, cameraCtrlSpeed, -1);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                Log.info(TAG, "MotionEvent.ACTION_DOWN");
                gestureMode = 1;
                break;
            case MotionEvent.ACTION_UP:
                Log.info(TAG, "MotionEvent.ACTION_UP");
                gestureMode = 0;
                // 离开屏幕，停止控制
                handleCameraControl(0);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // 多点触控，减少触控点
                Log.info(TAG, "MotionEvent.ACTION_POINTER_UP");
                gestureMode -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // 多点触控，增加触控点
                oldDist = spacing(event);
                Log.info(TAG, "MotionEvent.ACTION_POINTER_DOWN, oldDist:" + oldDist);
                gestureMode += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                // 两点触控情况进行放大缩小操作
                if (gestureMode == 2)
                {
                    float newDist = spacing(event);
                    Log.info(TAG, "MotionEvent.ACTION_MOVE, oldDist:" + oldDist + " newDist:" + newDist);
                    if (newDist > oldDist)
                    {
                        handleCameraControl(CommonEventEntry.CAMERA_CONTROL_ZOOM_IN);
                    }
                    else if (newDist < oldDist)
                    {
                        handleCameraControl(CommonEventEntry.CAMERA_CONTROL_ZOOM_OUT);
                    }
                    oldDist = newDist;
                }
                break;
            default:
                break;
        }
        return gestureDetector.onTouchEvent(event);
    }

    /**
     * 方法说明 : 当前窗口是否为最大化状态
     * @return boolean
     * @author hubin
     * @Date 2015-9-14
     */
    public boolean isFullScreen()
    {
        return isFullScreen;
    }

    /**
     * 方法说明 : 设置当前视频窗口最大化状态
     * @param isFullScreen
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setFullScreen(boolean isFullScreen)
    {
        this.isFullScreen = isFullScreen;
    }

    /**
     * 方法说明 : 设置当前视频窗口选中状态
     * @param isSelected
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setVideoSelected(boolean isSelected)
    {
        if (isSelected && (videoSurface != null || !TextUtils.isEmpty(textView.getText())))
        {
            videoControl.setVisibility(View.VISIBLE);
        }
        else
        {
            videoControl.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ptz_control:
                Log.info(TAG, "cameraControl::");
                if (videoSurface != null)
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_CAMERA_CONTROL, videoSurface.getTag(R.id.call_number),
                            videoSurface.getTag(R.id.session_id));
                }
                else if (!TextUtils.isEmpty(getMessage()))
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_CAMERA_CONTROL, getMessage(), getSessionId());
                }
                break;
            case R.id.presentation_control:
                if (videoSurface != null)
                {
                    Log.info(TAG, "single video presentation:: selectedVideoNum:" + videoSurface.getTag(R.id.call_number));
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.OPEN_PRESENTATION_CONTROL);
                    if (UiApplication.presentation != null)
                    {
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (VideoPrstCtrlFragment.getAvailablePrstScreenSize() > 0)
                                {
                                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_ADD_PRESENTATION_VIDEO, videoSurface);
                                    setSurFaceView(null);
                                }
                                else
                                {
                                    ToastUtil.showToast(String.format("扩展屏超出最大视频个数，%s视频显示失败", videoSurface.getTag(R.id.call_number)));
                                }
                            }
                        }, 300);
                    }
                }
                else if (!TextUtils.isEmpty(getMessage()))
                {
                    Log.info(TAG, "presentation back to window:: selectedVideoNum:" + getMessage());
                    // 通知扩展屏移除视频窗口
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.EVENT_REMOVE_PRESENTATION_VIDEO_TO_WINDOW, getMessage());
                    hideMessage();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 方法说明 : 获取当前视频窗口对应视频的业务ID
     * @return String
     * @author hubin
     * @Date 2015-9-14
     */
    public String getSessionId()
    {
        return sessionId;
    }

    /**
     * 方法说明 : 设置当前视频窗口对应视频的业务ID
     * @param sessionId
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    @Override
    public Bitmap getDrawingCache()
    {
        if (videoSurface != null)
        {
//            Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            Canvas bitCanvas = new Canvas(bitmap);
//            videoSurface.draw(bitCanvas);// 在自己创建的canvas上画
            return ImageUtil.zoomDrawable(getResources().getDrawable(R.drawable.player_background), getLayoutParams().width, getLayoutParams().height);
        }
        return super.getDrawingCache();
    }
}
