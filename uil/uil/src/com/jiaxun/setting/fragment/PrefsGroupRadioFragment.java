package com.jiaxun.setting.fragment;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.model.PrefsGroupRadioItem;
import com.jiaxun.setting.model.RadioGroupType;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.UiConfigEntry;

/**
 * 单选
 */
public class PrefsGroupRadioFragment extends BaseFragment implements android.view.View.OnClickListener
{
    private static final String TAG = PrefsGroupRadioFragment.class.getName();
    private Button saveButton;
    private Button cancelButton;
    private TextView textViewWhichInfo;
    private RadioGroup radioGroup;
    private PrefsGroupRadioItem prefsGroupRadioItem;
    private String value;
    private String[][] options;
    private String stringValue = null;
    private int intValue = 0;
    private Map<Integer, String> radioOptions = new HashMap<Integer, String>();

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_edit_info_group_radio;
    }

    @Override
    public void initComponentViews(View view)
    {
        saveButton = (Button) view.findViewById(R.id.saveButton);

        cancelButton = (Button) view.findViewById(R.id.cancelButton);

        textViewWhichInfo = (TextView) view.findViewById(R.id.textViewWhichInfo);

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup1);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                value = radioOptions.get(checkedId);
                Log.info(TAG, "id" + checkedId + "Value:" + value);
            }
        });
        prefsGroupRadioItem = getArguments().getParcelable(CommonConstantEntry.DATA_OBJECT);
        options = prefsGroupRadioItem.getRadioItems();

        if (prefsGroupRadioItem.getKey().equals(UiConfigEntry.PREF_LANGUAGE))
        {
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.endsWith("zh"))
                stringValue = ConfigHelper.getDefaultConfigHelper(getActivity()).getString(prefsGroupRadioItem.getKey(),
                        prefsGroupRadioItem.getDefaultValueString());
            else if (language.endsWith("en"))
                stringValue = ConfigHelper.getDefaultConfigHelper(getActivity()).getString(prefsGroupRadioItem.getKey(),
                        prefsGroupRadioItem.getDefaultValueString());
        }
        else
        {
            switch (prefsGroupRadioItem.getStoreType())
            {
                case RadioGroupType.DATA_STRING:
                    stringValue = ConfigHelper.getDefaultConfigHelper(getActivity()).getString(prefsGroupRadioItem.getKey(),
                            prefsGroupRadioItem.getDefaultValueString());
                    break;
                case RadioGroupType.DATA_INTEGER:
                    intValue = ConfigHelper.getDefaultConfigHelper(getActivity())
                            .getInt(prefsGroupRadioItem.getKey(), prefsGroupRadioItem.getDefaultValueInt());
                    break;
                default:
                    break;
            }
        }
        textViewWhichInfo.setText(prefsGroupRadioItem.getName());
        int[] attr = new int[3];
        attr[0] = R.attr.fontSmall;
        attr[1] = R.attr.fontMedium;
        attr[2] = R.attr.fontBig;
        // 获得当前主题字体大小
        TypedArray ta = getActivity().getTheme().obtainStyledAttributes(attr);
        float big = ta.getDimension(R.styleable.FontSize_fontBig, 15);
        float medium = ta.getDimension(R.styleable.FontSize_fontMedium, 15);
        float small = ta.getDimension(R.styleable.FontSize_fontSmall, 15);
        RadioButton radioButton;
        for (int i = 0; i < options.length; i++)
        {
            radioButton = new RadioButton(this.getActivity());
            radioButton.setText(options[i][1]);
            radioButton.setId(i);
            radioButton.setTextColor(Color.BLACK);
            // 设置字体大小
            if (options[i][0].equals("FONT_SMALL"))
            {
                radioButton.setTextSize(small);
            }
            else if (options[i][0].equals("FONT_MIDDLE"))
            {
                radioButton.setTextSize(medium);
            }
            else if (options[i][0].equals("FONT_LARGE"))
            {
                radioButton.setTextSize(big);
            }
            else
            {
                radioButton.setTextSize(medium);
            }
            switch (prefsGroupRadioItem.getStoreType())
            {
                case RadioGroupType.DATA_STRING:

                    if (stringValue.equals(options[i][0]))
                        radioButton.setChecked(true);
                    break;
                case RadioGroupType.DATA_INTEGER:

                    if (intValue == (Integer.parseInt(options[i][0])))
                        radioButton.setChecked(true);

                    break;
                default:
                    break;
            }

            radioOptions.put(radioButton.getId(), options[i][0]);
            radioGroup.addView(radioButton);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.saveButton:
                Log.info(TAG, prefsGroupRadioItem.getKey() + ":" + value);
                boolean result;
                switch (prefsGroupRadioItem.getStoreType())
                {
                    case RadioGroupType.DATA_STRING:

                        if ((TextUtils.isEmpty(stringValue)))
                        {
                            // 原先值为空，直接保存
                            result = ConfigHelper.getDefaultConfigHelper(getActivity()).putString(prefsGroupRadioItem.getKey(), value);
                            if (prefsGroupRadioItem.getItemCallBack() != null)
                                prefsGroupRadioItem.getItemCallBack().onCallBackResult(result);
                        }
                        else if ((!TextUtils.isEmpty(stringValue)) && (!value.equals(stringValue)))
                        {
                            // 原先值不为空，当选择值与原先值不相等时才保存
                            result = ConfigHelper.getDefaultConfigHelper(getActivity()).putString(prefsGroupRadioItem.getKey(), value);
                            if (prefsGroupRadioItem.getItemCallBack() != null)
                                prefsGroupRadioItem.getItemCallBack().onCallBackResult(result);
                        }
                        break;
                    case RadioGroupType.DATA_INTEGER:
                        result = ConfigHelper.getDefaultConfigHelper(getActivity()).putInt(prefsGroupRadioItem.getKey(), Integer.parseInt(value));
                        if (prefsGroupRadioItem.getItemCallBack() != null)
                            prefsGroupRadioItem.getItemCallBack().onCallBackResult(result);
                        break;
                    default:
                        break;
                }

                break;

            case R.id.cancelButton:
                break;

            default:
                break;
        }

        ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
    }

}
