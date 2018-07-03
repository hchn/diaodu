package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.jiaxun.sdk.scl.module.device.itf.SclDeviceService;
import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：云镜控制
 *
 * @author  zhangxd
 *
 * @Date 2015-6-15
 */
public class VideoPtzCtrlFragment extends BaseFragment implements OnItemSelectedListener, NotificationCenterDelegate, OnTouchListener,
        OnSeekBarChangeListener, Button.OnClickListener
{
    private static final String TAG = VideoPtzCtrlFragment.class.getName();
    private String sessionId;
    private Button leftUp;
    private Button up;
    private Button rightUp;
    private Button left;
    private Button right;
    private Button leftDown;
    private Button down;
    private Button rightDown;
    private Button zoomOut;// 缩小
    private Button zoomIn;// 放大
    private Button telefocus;// 远焦
    private Button proximityFocused;// 近焦
    private Button returnBack;
    private Button button_cruise;// 查看预设位
    private SeekBar speedSeekBar;
    private Spinner numberSpinner;
    private SclDeviceService sclDeviceService;
    private int ptzSpeed;
    private int tmpPtzSpeed;
    private HashMap<String, String> numbersAndSessionid;
    private ConfigHelper configHelper = ConfigHelper.getDefaultConfigHelper(getActivity());
    private String controlNumber;
    private ArrayAdapter<String> arrayAdapter;
    private int NO_CONTROL_USER = 0;
    private LinearLayout linearControl, linearSpeed;
    private ArrayList<String> numbers;
    private int SPEED_MAX=8;
    private int SPEED_MIN=1;
    public VideoPtzCtrlFragment()
    {
        sclDeviceService = UiApplication.getDeviceService();
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getLayoutView()
    {
        // TODO Auto-generated method stub
        return R.layout.fragment_ptzcontrol;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initComponentViews(View view)
    { // 读取存储速度值
        ptzSpeed = configHelper.getInt(UiConfigEntry.PREF_CONF_PTZ_SPEED, UiConfigEntry.DEFAULT_PREF_CONF_PTZ_SPEED);
        tmpPtzSpeed = ptzSpeed;
        linearControl = (LinearLayout) view.findViewById(R.id.linearLayoutControl);
        linearSpeed = (LinearLayout) view.findViewById(R.id.linearLayoutSpeed);
        leftUp = (Button) view.findViewById(R.id.button_leftUp);
        up = (Button) view.findViewById(R.id.button_up);
        rightUp = (Button) view.findViewById(R.id.button_rightUp);
        left = (Button) view.findViewById(R.id.button_left);
        right = (Button) view.findViewById(R.id.button_right);
        leftDown = (Button) view.findViewById(R.id.button_leftDown);
        down = (Button) view.findViewById(R.id.button_down);
        rightDown = (Button) view.findViewById(R.id.button_rightDown);
        zoomOut = (Button) view.findViewById(R.id.button_zoomOut);
        zoomIn = (Button) view.findViewById(R.id.button_zoomIn);
        telefocus = (Button) view.findViewById(R.id.button_telefocus);
        proximityFocused = (Button) view.findViewById(R.id.button_proximityFocused);
        button_cruise = (Button) view.findViewById(R.id.button_cruise);
        returnBack = (Button) view.findViewById(R.id.returnBack);
        returnBack.setOnClickListener(this);
        speedSeekBar = (SeekBar) view.findViewById(R.id.seekBar_speed);
        numberSpinner = (Spinner) view.findViewById(R.id.spinner_number);
        // 默认不自动调用
        numberSpinner.setSelection(0, true);
        arrayAdapter=new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item);
        numbers=new ArrayList<String>();
        numbersAndSessionid= new HashMap<String, String>();
        if (getArguments() != null)
        {
            String sessionId = getArguments().getString(CommonConstantEntry.DATA_SESSION_ID);
            String number = getArguments().getString(CommonConstantEntry.DATA_NUMBER);
            setControlPara(number, sessionId);
            update(number,false);
        }
        numberSpinner.setOnItemSelectedListener(this);
        speedSeekBar.setMax(SPEED_MAX);
        speedSeekBar.setProgress(ptzSpeed);
        setTouchListener();
    }

    @Override
    public void onStart()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CLOSE_PTZ_VIEW);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_PTZ_NUMBER_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_VIDEO_NUMBER_CHANGE);
        super.onStart();
    }

    @Override
    public void onStop()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CLOSE_PTZ_VIEW);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_PTZ_NUMBER_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_VIDEO_NUMBER_CHANGE);
        super.onStop();
    }

    public void setControlPara(String number, String sessionId)
    {
        if (!TextUtils.isEmpty(number))
        {
            this.controlNumber = number;
        }else
        {
            this.controlNumber = "";
        }
        if (!TextUtils.isEmpty(sessionId))
        {
            this.sessionId = sessionId;
        }else
        {
            this.sessionId = "";
        }
    }

    public void setTouchListener()
    {
        leftUp.setOnTouchListener(this);
        up.setOnTouchListener(this);
        rightUp.setOnTouchListener(this);
        left.setOnTouchListener(this);
        right.setOnTouchListener(this);
        leftDown.setOnTouchListener(this);
        down.setOnTouchListener(this);
        rightDown.setOnTouchListener(this);
        zoomOut.setOnTouchListener(this);
        zoomIn.setOnTouchListener(this);
        telefocus.setOnTouchListener(this);
        proximityFocused.setOnTouchListener(this);
        speedSeekBar.setOnTouchListener(this);

//        button_lookPreSetting.setOnClickListener(this);
//        button_setForSetting.setOnClickListener(this);
//        button_cruise.setOnTouchListener(this);
        speedSeekBar.setOnSeekBarChangeListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (TextUtils.isEmpty(controlNumber) || TextUtils.isEmpty(sessionId))
        {
            return false;
        }

        // TODO Auto-generated method stub
        switch (v.getId())
        {
            case R.id.button_leftUp:

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_LEFT_UP_MOVE, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_LEFT_UP_MOVE_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }
                break;
            case R.id.button_up:

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_UPWARD, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_UPWARD_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }
                break;
            case R.id.button_rightUp:

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_RIGHT_UP_MOVE, ptzSpeed, ptzSpeed, 2);
                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_RIGHT_UP_MOVE_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }

                break;
            case R.id.button_left:

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_TURN_LEFT, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_TURN_LEFT_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }
                break;
            case R.id.button_right:

                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_TURN_RIGHT, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_TURN_RIGHT_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }
                break;
            case R.id.button_leftDown:

                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_LEFT_DOWN_MOVE, ptzSpeed, ptzSpeed, 2);
                        break;
                    case MotionEvent.ACTION_UP:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_LEFT_DOWN_MOVE_STOP, ptzSpeed, ptzSpeed, 2);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.button_down:
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_DOWNWARD, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_DOWNWARD_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }

                break;
            case R.id.button_zoomIn:

                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_ZOOM_IN, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_ZOOM_IN_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }

                break;
            case R.id.button_zoomOut:
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_ZOOM_OUT, ptzSpeed, ptzSpeed, 2);

                        break;
                    case MotionEvent.ACTION_UP:

                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_ZOOM_OUT_STOP, ptzSpeed, ptzSpeed, 2);

                        break;
                    default:
                        break;
                }

                break;
            case R.id.button_proximityFocused:
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_PROXIMITY_FOCUSED, ptzSpeed, ptzSpeed, 2);
                        break;
                    case MotionEvent.ACTION_UP:
                        sclDeviceService
                                .remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_PROXIMITY_FOCUSED_STOP, ptzSpeed, ptzSpeed, 2);
                        break;
                    default:
                        break;
                }

                break;
            case R.id.button_rightDown:
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_RIGHT_DOWN_MOVE, ptzSpeed, ptzSpeed, 2);
                        break;
                    case MotionEvent.ACTION_UP:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_RIGHT_DOWN_MOVE_STOP, ptzSpeed, ptzSpeed, 2);
                        break;
                    default:
                        break;
                }

                break;

            case R.id.button_telefocus:
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_TELEFOCUS, ptzSpeed, ptzSpeed, 2);
                        break;
                    case MotionEvent.ACTION_UP:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_TELEFOCUS_STOP, ptzSpeed, ptzSpeed, 2);
                        break;
                    default:
                        break;
                }
            case R.id.button_cruise:
                // TODO Auto-generated method stub
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_LIGHT_TRACE_START, ptzSpeed, ptzSpeed, 2);
                        break;
                    case MotionEvent.ACTION_UP:
                        sclDeviceService.remoteCameraControl(sessionId, controlNumber, CommonEventEntry.CAMERA_CONTROL_LIGHT_TRACE_STOP, ptzSpeed, ptzSpeed, 2);
                        break;
                    default:
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        tmpPtzSpeed = progress + SPEED_MIN;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        ptzSpeed = tmpPtzSpeed;
        configHelper.putInt(UiConfigEntry.PREF_CONF_PTZ_SPEED, ptzSpeed);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.returnBack:
                parentActivity.backToPreFragment(R.id.container_right_content);
                break;
            default:
                break;
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.CLOSE_PTZ_VIEW)
        {
            parentActivity.backToPreFragment(R.id.container_right_content);
        }
        else if (id == UiEventEntry.NOTIFY_PTZ_NUMBER_CHANGE)
        {
            // 左侧选中云镜改变
            String comingNumber = (String) args[0];
            String sId = (String) args[1];
            for (String eachNumber : numbers)
            {
                if (comingNumber.equals(eachNumber))
                {
                    int location = numbers.indexOf(eachNumber);
                    numberSpinner.setSelection(location);
                    setControlPara(comingNumber, sId);
                    break;
                }
            }
        }
        else if (id == UiEventEntry.NOTIFY_VIDEO_NUMBER_CHANGE)
        {
            // 左侧视频窗口数量改变
            if (!TextUtils.isEmpty(controlNumber))
            {
                update(controlNumber,true);
            }
            else
            {
                numberSpinner.setSelection(NO_CONTROL_USER);
                setControlPara("", "");
            }
        }
    }

    /**
     * 方法说明 :更新spinner数据
     * @param controlNumber 控制号码
     * @author zhangxd
     * @Date 2015-10-10
     */
    private void update(String controlNumber,boolean set)
    {
        for (int i = 0; i < numbers.size(); i++)
        {
            arrayAdapter.remove(numbers.get(i));
        }
        numbers.clear();
        numbersAndSessionid.clear();
        numbers.add("无");
        numbersAndSessionid.put("无", "");
        ArrayList<SurfaceView> svList = VideoFragment.getVideoList();
        if ((svList != null) && (svList.size() > 0))
        {
            for (SurfaceView surfaceView : svList)
            {
                String number = (String) surfaceView.getTag(R.id.call_number);
                String sId = (String) surfaceView.getTag(R.id.session_id);
                if (!TextUtils.isEmpty(number))
                    if ((!TextUtils.isEmpty(number)) && (!TextUtils.isEmpty(sId)))
                    {
                        numbersAndSessionid.put(number, sId);
                        numbers.add(number);
                    }
            }
        }
        arrayAdapter.addAll(numbers);
        arrayAdapter.notifyDataSetChanged();
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberSpinner.setAdapter(arrayAdapter);
        numberSpinner.setSelection(NO_CONTROL_USER);
        boolean hasControlNumber=false;
        for (String eachNumber : numbers)
        {
            if (controlNumber.equals(eachNumber))
            {
                hasControlNumber=true;
                int location = numbers.indexOf(eachNumber);
                numberSpinner.setSelection(location);
                break;
            }
        }
        if(set)
        {
            if(!hasControlNumber){
                setControlPara("", "");
                hasControlNumber(false);
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        if (position == NO_CONTROL_USER)
        {
            Log.info(TAG, "onItemSelected:: position == NO_CONTROL_USER");
            setControlPara("", "");
            hasControlNumber(false);
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VIDEO_PTZ_CHANGE, "");
        }
        else
        {
            Log.info(TAG, "onItemSelected::");
            hasControlNumber(true);
            String number = numbers.get(position);
            if (!number.equals(controlNumber))
            {
                String sId=numbersAndSessionid.get(number);
                setControlPara(number, sId);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VIDEO_PTZ_CHANGE, number);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
        // TODO Auto-generated method stub

    }
    private void hasControlNumber(boolean has)
    {
        if(has)
        {
            linearControl.setVisibility(View.VISIBLE);
            linearSpeed.setVisibility(View.VISIBLE);
        }else
        {
            linearControl.setVisibility(View.INVISIBLE);
            linearSpeed.setVisibility(View.INVISIBLE);
        }
    }
}
