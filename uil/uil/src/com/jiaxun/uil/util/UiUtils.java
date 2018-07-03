package com.jiaxun.uil.util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：
 *
 * @author  jiaxun
 *
 * @Date 2015-3-5
 */
public class UiUtils
{
    private final static String TAG = UiUtils.class.getName();
    public static int screenWidth;
    public static int screenHeight;
    public static int homeLeftContainerW;
    public static int homeRightContainerW;
    public static int homeLeftPaneW;
    public static int homeRightPaneW;
    public static int homeContainerH;

    public static int settingLeftContainerW;
    public static int settingRightContainerW;
    public static int settingContainerH;

    public static int titleBarH;

//    public static Timer lockScreenTimer;
//    public static TimerTask lockScreenTimerTask;
    /**锁屏开关*/
    public static boolean screenLockOpen = false;
    private static boolean screenIsLocked = false;
    public static long lockTimeLength = 0;
    public static Object object = null;
    public static final String[][] lockScreenTime = { { "TIME_30S", "30s" }, { "TIME_1M", "1m" }, { "TIME_15M", "15m" }, { "TIME_30M", "30m" },
            { "TIME_60M", "1h" } };

    public static final String[][] oneKeyMultNums = { { "default", "默认间隔" }, { "TIME_10s", "10s" }, { "TIME_15s", "15s" }, { "TIME_20s", "20s" }, };

    /**
     * @Title: hideSoftKeyboard
     * @Description: 隐藏键盘
     */
    public static void hideSoftKeyboard(Context context, View view)
    {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive())
        {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void hideSoftKeyboard(Context context)
    {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive() && ((Activity) context).getCurrentFocus() != null)
        {
            manager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void showSoftKeyboard(EditText view)
    {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void addRemoteVideo(int sessionType, String sessionId, String number, SurfaceView remoteVideo)
    {
        Log.info(TAG, "addRemoteVideo:: number:" + number);
//        remoteVideo.setTag(number);
        if (remoteVideo == null)
        {
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_SHOW_REMOTE_VIDEO);
        }
        else
        {
            remoteVideo.setTag(R.id.session_type, sessionType);
            remoteVideo.setTag(R.id.session_id, sessionId);
            remoteVideo.setTag(R.id.call_number, number);
            remoteVideo.setTag(R.id.video_status, true);
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_SHOW_REMOTE_VIDEO, remoteVideo);
        }
    }

    public static void closeVideo(SurfaceView surfaceView)
    {
        if (surfaceView == null)
        {
            return;
        }
        int sessionType = (Integer) surfaceView.getTag(R.id.session_type);
        switch (sessionType)
        {
            case CommonConstantEntry.SESSION_TYPE_SCALL:
                // TODO
                break;
            case CommonConstantEntry.SESSION_TYPE_CONF:
                UiApplication.getConfService().confUserVideoEnable((String) surfaceView.getTag(R.id.session_id), (String) surfaceView.getTag(R.id.call_number),
                        false);
                break;
            case CommonConstantEntry.SESSION_TYPE_VS:
                UiApplication.getVsService().closeVs((String) surfaceView.getTag(R.id.session_id));
                break;

            default:
                break;
        }
    }

    public static void removeRemoteVideo(String number)
    {
        Log.info(TAG, "removeRemoteVideo:: number:" + number);
        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.EVENT_REMOVE_REMOTE_VIDEO, number);
    }

    public static void showLockView(final Context context)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lock_screen, null);
        TextView noticeTv = (TextView) view.findViewById(R.id.tv_notice);
        String noticeText = context.getResources().getString(R.string.dialog_lockscreen_notice);
        final EditText passwordEt = (EditText) view.findViewById(R.id.et_password);
        noticeText = String.format(noticeText, UiApplication.getAtdService().getLoginedAttendant().getLogin(), DateUtils.getNowTime());
        noticeTv.setText(noticeText);
        final Dialog dialog = DialogUtil.showCustomDialog(context, view);
        dialog.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
            {
            	if(keyCode == KeyEvent.KEYCODE_BACK)
            	{
            		return true;
            	}
                return false;
            }
        });
        screenIsLocked = true;
        view.findViewById(R.id.btn_unlock).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String password = passwordEt.getText().toString().trim();
                if (UiApplication.getAtdService().isAtdLoginedPasswordValid(password))
                {
                    dialog.dismiss();
                    screenIsLocked = false;
                    startLockScreenServer();
                }
                else
                {
                    ToastUtil.showToast("密码错误!");
                }
            }
        });
    }

    private static void loadLockScreenTimeLength(Context mContact)
    {
        String lockTime = ConfigHelper.getDefaultConfigHelper(mContact).getString(UiConfigEntry.PREF_LOCK_TIME, UiConfigEntry.DEFAULT_LOCK_TIME);
        if ("TIME_30S".equals(lockTime))
        {
            UiUtils.lockTimeLength = 30 * 1000l;
        }
        else if ("TIME_1M".equals(lockTime))
        {
            UiUtils.lockTimeLength = 60 * 1000l;
        }
        else if ("TIME_15M".equals(lockTime))
        {
            UiUtils.lockTimeLength = 15 * 60 * 1000l;
        }
        else if ("TIME_30M".equals(lockTime))
        {
            UiUtils.lockTimeLength = 30 * 60 * 1000l;
        }
        else if ("TIME_60M".equals(lockTime))
        {
            UiUtils.lockTimeLength = 60 * 60 * 1000l;
        }
        else
        {
            UiUtils.lockTimeLength = 30 * 1000l;
        }
    }

    /**
     * 方法说明 :一键多号 呼叫时间间隔
     * @param mContact
     * @author HeZhen
     * @Date 2015-9-17
     */
    public static long loadOneKeyMultNumTime(Context mContact)
    {
        long timeLength = 0l;
        String lockTime = ConfigHelper.getDefaultConfigHelper(mContact).getString(UiConfigEntry.PREF_ONEKEY_MULTNUM, "");
        if ("default".equals(lockTime))
        {
            timeLength = 0;
        }
        else if ("TIME_10s".equals(lockTime))
        {
            timeLength = 10 * 1000l;
        }
        else if ("TIME_15s".equals(lockTime))
        {
            timeLength = 15 * 1000l;
        }
        else if ("TIME_20s".equals(lockTime))
        {
            timeLength = 20 * 1000l;
        }
        return timeLength;
    }

    /**
     * 
     * 方法说明 :隐藏软键盘，并显示光标
     * @param context
     * @param ed
     * @author chaimb
     * @Date 2015-6-24
     */
    public static void hideSoftInputMethod(Context context, EditText ed)
    {
        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16)
        {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        }
        else if (currentVersion >= 14)
        {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null)
        {
            ed.setInputType(InputType.TYPE_NULL);
        }
        else
        {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try
            {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            }
            catch (Exception e)
            {

                ed.setInputType(InputType.TYPE_NULL);
                Log.exception(TAG, e);
            }
        }
    }

    /**
         * 遍历布局，并禁用所有子控件
         * 
         * @param viewGroup
         *            布局对象
         */
    public static void disableSubControls(ViewGroup viewGroup)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup)
            {
                if (v instanceof Spinner)
                {
                    Spinner spinner = (Spinner) v;

                    spinner.setEnabled(false);

                }
                else if (v instanceof ListView)
                {
                    ((ListView) v).setEnabled(false);
                }
                else
                {
                    disableSubControls((ViewGroup) v);
                }
            }
            else if (v instanceof EditText)
            {
                ((EditText) v).setEnabled(false);

            }
            else if (v instanceof Button)
            {
                ((Button) v).setEnabled(false);
            }
        }
    }

    public static void enableSubControls(ViewGroup viewGroup)
    {
        for (int i = 0; i < viewGroup.getChildCount(); i++)
        {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup)
            {
                if (v instanceof Spinner)
                {
                    Spinner spinner = (Spinner) v;
                    spinner.setClickable(true);
                    spinner.setEnabled(true);

                }
                else if (v instanceof ListView)
                {
                    ((ListView) v).setClickable(true);
                    ((ListView) v).setEnabled(true);

                }
                else
                {
                    disableSubControls((ViewGroup) v);
                }
            }
            else if (v instanceof EditText)
            {
                ((EditText) v).setEnabled(true);
                ((EditText) v).setClickable(true);

            }
            else if (v instanceof Button)
            {
                ((Button) v).setEnabled(true);
            }
        }
    }

    /**
     * 方法说明 :得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1  
     * @param s
     * @return  得到的字符串长度  
     * @author HeZhen
     * @Date 2015-7-23
     */
    public static double getLength(String s)
    {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < s.length(); i++)
        {
            String temp = s.substring(i, i + 1);
            if (temp.matches(chinese))
            {
                valueLength += 2;
            }
            else
            {
                valueLength += 1;
            }
        }
        return Math.ceil(valueLength);
    }

    public static void startLockScreenServer()
    {
        cancelLockScreenServer();
        screenLockOpen = ConfigHelper.getDefaultConfigHelper(UiApplication.getCurrentContext()).getBoolean(UiConfigEntry.PREF_LOCK_ENABLED, UiConfigEntry.DEFAULT_LOCK_ENABLED);
        if (screenLockOpen)
        {
            loadLockScreenTimeLength(UiApplication.getCurrentContext());
            UiApplication.applicationHandler.postDelayed(runnable, lockTimeLength);
        }
    }

    public static void cancelLockScreenServer()
    {
        UiApplication.applicationHandler.removeCallbacks(runnable);
    }

    private static Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            // 如果有通话
            if (ServiceUtils.getCurrentCallList().size() > 0)
            {
                startLockScreenServer();
                return;
            }
            if (!UiUtils.screenIsLocked && screenLockOpen)
            {
                UiUtils.showLockView(UiApplication.getCurrentContext());
            }

        }
    };
}
