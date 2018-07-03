package com.jiaxun.setting.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsSeekBarItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.SystemBrightManager;
import com.jiaxun.uil.util.UiConfigEntry;

/**
 * 说明：设置拖动条
 * 
 * @author zhangxd
 * 
 * @Date 2015-2-28
 */
public class PrefsSeekBarFragment extends Fragment implements android.view.View.OnClickListener
{
    private static final String TAG = PrefsSeekBarFragment.class.getName();

    private Button saveButton;
    private Button cancelButton;

    private TextView textViewWhichInfo;
    private SeekBar seekBar;
    private PrefsSeekBarItem prefsSeekBarItem;
    private ConfigHelper configHelper = ConfigHelper.getDefaultConfigHelper(getActivity());
    private int tmpValue;
    private int nowValue;
    private int maxValue=0;
    private int minValue=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_seekbar_edit, container, false);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        seekBar = (SeekBar) view.findViewById(R.id.valueSeekBar);
        textViewWhichInfo = (TextView) view.findViewById(R.id.textViewWhichInfo);
        prefsSeekBarItem = getArguments().getParcelable(CommonConstantEntry.DATA_OBJECT);
        if ((prefsSeekBarItem != null))
        {
            setting(prefsSeekBarItem);
        }
        saveButton.setVisibility(View.INVISIBLE);
        cancelButton.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                nowValue = tmpValue;
                if(prefsSeekBarItem.getKey().equals(UiConfigEntry.PREF_SERVICE_SCREEN_BRIGHTNESS))
                {
                    Context context = UiApplication.getInstance();
                    boolean isAutoBrightness = SystemBrightManager.isAutoBrightness(context);
                    if (isAutoBrightness)
                    { // 自动调整亮度
                      // SystemBrightManager.setBrightness(activity, -1);
                    }
                    else
                    {
                        SystemBrightManager.saveBrightness(context, nowValue);
                    } 
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                tmpValue = progress+minValue;
                SystemBrightManager.setBrightness(getActivity(), tmpValue);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v)
    {
        ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
    }

    private void setting(final PrefsSeekBarItem item)
    {
        // 设置标题显示修改说明内容
        textViewWhichInfo.setText(item.getName());
        seekBar.setMax(item.getMaxValue());
        maxValue=item.getMaxValue();
        minValue=item.getMinValue();
        int deviation=maxValue-minValue;
        if((deviation)>=0)
        {
            seekBar.setMax(deviation);
        }
        if(item.getKey().equals(UiConfigEntry.PREF_SERVICE_SCREEN_BRIGHTNESS))
        {
            int systemBrightness=SystemBrightManager.getBrightness(getActivity());
            nowValue = systemBrightness;
           
        }else
        {
            nowValue = configHelper.getInt(item.getKey(), item.getDefaultValue());
        }
        seekBar.setProgress(nowValue-minValue);
        tmpValue = nowValue;
    }

}
