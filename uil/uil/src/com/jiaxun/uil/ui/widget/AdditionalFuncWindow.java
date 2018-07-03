package com.jiaxun.uil.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.AdditionalFuncItem;
import com.jiaxun.uil.ui.adapter.AdditionalFuncAdapter;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog.OnCustomClickListener;
import com.jiaxun.uil.util.DialogUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：附加业务功能窗口
 *
 * @author  HeZhen
 *
 * @Date 2015-6-8
 */
public class AdditionalFuncWindow extends PopupWindow
{
    private static final String TAG = AdditionalFuncWindow.class.getName();
    private View contentView;
    private GridView gridView;
    private AdditionalFuncAdapter rightMoreAdapter;
    List<AdditionalFuncItem> funcDataList;
    private Context mContext;

    public AdditionalFuncWindow(Context context)
    {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_func_more, null);
        gridView = (GridView) contentView.findViewById(R.id.gv_func_more);
        funcDataList = new ArrayList<AdditionalFuncItem>();
        rightMoreAdapter = new AdditionalFuncAdapter(context, dataSet());
        gridView.setAdapter(rightMoreAdapter);

        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.AnimationPopup);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setOnItemClickListener(onItemClistListener);
    }

    private OnItemClickListener onItemClistListener = new OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            AdditionalFuncItem funcItem = funcDataList.get(position);
            doFunc(funcItem);
            rightMoreAdapter.notifyDataSetChanged();
        }

    };

    public void refresh()
    {
        dataSet();
        rightMoreAdapter.notifyDataSetChanged();
    }

    private void doFunc(AdditionalFuncItem funcItem)
    {
        if (funcItem == null)
        {
            return;
        }
        Log.info(TAG, "doFunc : " + funcItem.getName());
        funcItem.setChecked(!funcItem.isChecked());
        final boolean check = funcItem.isChecked();
        switch (funcItem.getId())
        {
            case 1:
//                UiApplication.getConfigService().setCloseRingEnabled(check);
                break;
            case 2:
                UiApplication.getConfigService().setEmergencyEnabled(check);
                break;
            case 3:
                UiApplication.getConfigService().setDndEnabled(check);
                break;
            case 4:
                if (check)
                {
                    DialogUtil.showConfirmDialog(UiApplication.getCurrentContext(), "\t\t\t\t是否开启夜服?\t\t\t\t", true, new OnCustomClickListener()
                    {
                        @Override
                        public void onClick(CustomAlertDialog customAlertDialog)
                        {
//                            UiApplication.getConfigService().setNightService(true);
                            UiApplication.getCommonService().setNightService(check);
                            customAlertDialog.dismiss();
//                            dataSet();
//                            rightMoreAdapter.notifyDataSetChanged();
                        }
                    });
                }
                else
                {
                    UiApplication.getCommonService().setNightService(check);
                }
                funcItem.setChecked(false);
                dismiss();
                break;
            case 5:
                UiApplication.getConfigService().setSystemMute(check);
                break;
            case 6:
                funcItem.setChecked(false);
                UiUtils.showLockView(mContext);
                dismiss();
                break;
            case 7:
                UiApplication.getConfigService().setAutoAnswer(check);
                break;
            case 8:
                UiApplication.getConfigService().setVideoCallEnabled(check);
                break;
            case 9:
                funcItem.setChecked(false);
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.CONTACT_TEMPCONF_SHOW);
                dismiss();
                break;
            case 10:
                UiApplication.getConfigService().setLocalCameralVisible(check);
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE, check);
                dismiss();
                break;
            case 11:
                UiApplication.getConfigService().setAudioRecordEnabled(check);
                break;
        }
    }

    private List<AdditionalFuncItem> dataSet()
    {
        funcDataList.clear();
//        funcDataList.add(new FuncDataItem("闭铃", R.drawable.func_closebell_normal, R.drawable.func_closebell_select, UiApplication.getConfigService()
//                .isCloseRingEnabled(), 1));
        funcDataList.add(new AdditionalFuncItem("紧急呼叫", R.drawable.func_sos_normal, R.drawable.func_sos_select, UiApplication.getConfigService()
                .isEmergencyEnabled(), 2));
        funcDataList.add(new AdditionalFuncItem("免打扰", R.drawable.func_undisturb_normal, R.drawable.func_undisturb_select, UiApplication.getConfigService()
                .isDndEnabled(), 3));
        funcDataList.add(new AdditionalFuncItem("夜服", R.drawable.func_night_normal, R.drawable.func_night_select, UiApplication.getConfigService()
                .isNightService(), 4));
//        funcDataList.add(new FuncDataItem("静音", R.drawable.func_mute_normal, R.drawable.func_mute_select, UiApplication.getConfigService().isSystemMute(), 5));
        funcDataList.add(new AdditionalFuncItem("锁屏", R.drawable.func_lock_normal, R.drawable.func_lock_hl, UiApplication.getConfigService()
                .isScreenLocked(), 6));
        funcDataList.add(new AdditionalFuncItem("自动应答", R.drawable.func_mute_normal, R.drawable.func_mute_select, UiApplication.getConfigService()
                .isAutoAnswer(), 7));
        funcDataList.add(new AdditionalFuncItem("视频呼叫", R.drawable.func_mute_normal, R.drawable.func_mute_select, UiApplication.getConfigService()
                .isVideoCallEnabled(), 8));
        funcDataList.add(new AdditionalFuncItem("临时会议", R.drawable.func_tempconf_normal, R.drawable.func_tempconf_select, false, 9));
        funcDataList.add(new AdditionalFuncItem("本地视频", R.drawable.func_local_camera_normal, R.drawable.func_local_camera_hl, UiApplication.getConfigService()
                .isLocalCameralVisible(), 10));
        funcDataList.add(new AdditionalFuncItem("本地录音", R.drawable.func_tempconf_normal, R.drawable.func_tempconf_select, UiApplication.getConfigService()
                .isAudioRecordEnabled(), 11));

        return funcDataList;
    }
}
