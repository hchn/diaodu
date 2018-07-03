package com.jiaxun.setting.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.model.PrefItemType;
import com.jiaxun.setting.model.PrefsSeekBarItem;
import com.jiaxun.setting.model.PrefsTextItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.util.InputFilterUtil;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：设置配置信息
 * 
 * @author zhangxd
 * 
 * @Date 2015-2-28
 */
public class PrefsEditInfoFragment extends Fragment implements android.view.View.OnClickListener
{
    private static final String TAG = PrefsEditInfoFragment.class.getName();
    private Button saveButton;
    private Button cancelButton;
    private EditText editText;
    private TextView textViewWhichInfo;
    private PrefsTextItem prefsTextItem;
    private ConfigHelper configHelper = ConfigHelper.getDefaultConfigHelper(getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        editText = (EditText) view.findViewById(R.id.editText);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
        editText.requestFocus();
        textViewWhichInfo = (TextView) view.findViewById(R.id.textViewWhichInfo);
        prefsTextItem = getArguments().getParcelable(CommonConstantEntry.DATA_OBJECT);
        saveButton.setEnabled(false);
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        // 以下设置不能再onCreateView里面不生效，除非延时
        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if ((prefsTextItem != null))
        {
            editText.setText("");
            setting();
        }
        else
        {
            // Toast.makeText(getActivity(), "空指針", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        editText.setText("");
    }

    @Override
    public void onClick(View v)
    {
        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus()
                .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId())
        {
            case R.id.saveButton:
                if (!TextUtils.isEmpty(editText.getText()))
                {
                    switch (prefsTextItem.getItemType())
                    {
                        case PrefItemType.TEXT:
                        case PrefItemType.PASSWORD:
                        case PrefItemType.SERVER_ADDRESS:
                            // 判断主备服务器地址是否一样，一样的话提示用户不能保存
                            if (prefsTextItem.getKey().equals(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_IP))
                            {
                                String slaveIP = configHelper.getString(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_IP, "");
                                if (!TextUtils.isEmpty(slaveIP))
                                {
                                    if (slaveIP.equals(editText.getText().toString()))
                                    {
                                        ToastUtil.showToast("主备服务器地址一样，无法保存");
                                        break;
                                    }
                                }
                            }
                            else if (prefsTextItem.getKey().equals(UiConfigEntry.PREF_SERVICE_SLAVE_SERVER_IP))
                            {
                                String masterIP = configHelper.getString(UiConfigEntry.PREF_SERVICE_MASTER_SERVER_IP, "");
                                if (!TextUtils.isEmpty(masterIP))
                                {
                                    if (masterIP.equals(editText.getText().toString()))
                                    {
                                        ToastUtil.showToast("主备服务器地址一样，无法保存");
                                        break;
                                    }
                                }
                            }

                        case PrefItemType.RADIO_GROUP:
                            configHelper.putString(prefsTextItem.getKey(), editText.getText().toString());
                            if (prefsTextItem != null && prefsTextItem.getItemCallBack() != null)
                                prefsTextItem.getItemCallBack().onCallBackResult(true);
                            if (prefsTextItem.getKey().equals(UiConfigEntry.SYSTEM_NAME))
                            {
                                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_SYSTME_NAME_CHANGE, editText.getText().toString());
                            }
                            ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
                            break;
                        default:
                            break;
                    }
                }
                break;
            case R.id.cancelButton:
                ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
                break;
            default:
                break;
        }
    }

    /**
     * 方法说明 : 文本类型
     * 
     * @param name
     *            终端显示名称
     * @return boolean
     * @author hubin
     * @Date 2015-3-3
     */
    private boolean isDisplayNameValid(String name)
    {

        if (TextUtils.isEmpty(name))
        {
            return false;
        }
        return true;
    }

    /**
     * 方法说明 : 支持IPV4，支持域名
     * 
     * @param address
     * @return boolean
     * @author hubin
     * @Date 2015-3-3
     */
    private boolean isServerAddressValid(String address, String regex)
    {
        if (TextUtils.isEmpty(address) || !address.matches(regex))
        {
            return false;
        }
        return true;
    }

    /**
     * 数值合法验证
     */
    private boolean isIntValid(String port, PrefsSeekBarItem item)
    {
        try
        {
            int value = Integer.valueOf(port);
            if ((value < item.getMinValue()) || (value > item.getMaxValue()))
            {
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private void setting()
    {
        // 设置标题显示修改说明内容
        textViewWhichInfo.setText(prefsTextItem.getName());
        // 限制输入字符个数
        switch (prefsTextItem.getItemType())
        {
            case PrefItemType.TEXT:
                String reg = ((PrefsTextItem) prefsTextItem).getRegex();
                if (!TextUtils.isEmpty(reg))
                {
                    editText.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(reg) });
                }
                else
                {
                    // 如果正则表达式为空，不受限制,长度限制20位（可输入任意字符）
                    editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(UiConfigEntry.SYSTEM_LENGTH_MAX) });
                    editText.setMaxEms(20);
                }
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case PrefItemType.PASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(((PrefsTextItem) prefsTextItem).getRegex()) });
                break;
            case PrefItemType.SERVER_ADDRESS:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            default:
                break;
        }

        // 设置输入提示
        String valueStr = configHelper.getString(prefsTextItem.getKey(), "");

        if (valueStr.equals(""))
        {
            editText.setText("");
            editText.setHint(prefsTextItem.getHint());
        }
        else
        {
            editText.setText(valueStr);
            editText.setSelection(editText.length());
//            currentIndex = editText.getSelectionStart();
        }

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // refreshEditPrefs(numberETP, isNumberValid(s.toString()));

                if (TextUtils.isEmpty(s.toString()))
                {
                    editText.setHint(prefsTextItem.getHint());
                }
                switch (prefsTextItem.getItemType())
                {
                    case PrefItemType.TEXT:
                    case PrefItemType.PASSWORD:
                        if (isDisplayNameValid(s.toString()))
                        {
                            saveButton.setEnabled(true);
                        }
                        else
                        {
                            saveButton.setEnabled(false);
                        }
                        break;

                    case PrefItemType.SERVER_ADDRESS:
                        if (isServerAddressValid(s.toString(), (prefsTextItem).getRegex()))
                        {
                            saveButton.setEnabled(true);
                        }
                        else
                        {
                            saveButton.setEnabled(false);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
