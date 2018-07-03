package com.jiaxun.setting.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.jiaxun.sdk.dcl.module.attendant.itf.DclAtdService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.AttendantListItem;
import com.jiaxun.uil.util.InputFilterUtil;
import com.jiaxun.uil.util.ToastUtil;

/**
 * 说明：操作台用户添加/修改
 * 
 * @author fuluo
 * 
 * @Date 2015-4-10
 * 
 */
public class AttendantFragment extends Fragment implements OnClickListener
{
    private static String TAG = AttendantFragment.class.getName();

    /**  支持数字、字母、下划线、@符号   1-20位*/
    private String RE_NAME = "[A-Za-z0-9_@]{1,20}$";
    /**  支持数字、字母、下划线    1-20位*/
    private String RE_PASSWORD = "[A-Za-z0-9_]{1,20}$";

    private Button config;
    private Button cancle;
//    private EditText name;
    private EditText login;
    private EditText password;
    private Spinner role;

    private DclAtdService uilAtdService;
    private AttendantListItem attendant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_attendant, container, false);
        config = (Button) view.findViewById(R.id.config);
        config.setOnClickListener(this);
        cancle = (Button) view.findViewById(R.id.cancle);
        cancle.setOnClickListener(this);
//        name = (EditText) view.findViewById(R.id.name);
        login = (EditText) view.findViewById(R.id.login);
        password = (EditText) view.findViewById(R.id.password);
        role = (Spinner) view.findViewById(R.id.role);

        login.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(RE_NAME) });
        password.setFilters(new InputFilter[] { InputFilterUtil.getInputFilter(RE_PASSWORD) });

        uilAtdService = UiApplication.getAtdService();

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            attendant = (AttendantListItem) bundle.getSerializable(CommonConstantEntry.DATA_OBJECT);
//            name.setText(attendant.getName());
            login.setText(attendant.getLogin());
            password.setText(attendant.getPassword());
            role.setSelection(attendant.getPriority());

            if (attendant.getLogin().equals("admin"))
            {// 默认管理员只允许修改密码
                login.setEnabled(false);
//                name.setEnabled(false);
                role.setEnabled(false);
            }
            else
            {
                role.setEnabled(true);
            }
        }

        setListener();
        return view;
    }

    private void setListener()
    {
//        login.addTextChangedListener(new TextWatcher()
//        {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                if (s.length() >= UiConfigEntry.ATTENDANT_NAME_MAX)
//                {
//                    ToastUtil.showToast("用户名不能大于20个字符");
//                }
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        });
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == config.getId())
        {
            try
            {
//                String nameStr = name.getText().toString();
//                if (TextUtils.isEmpty(nameStr))
//                {
//                    ToastUtil.showToast("名字不能为空！");
//                    return;
//                }
                String loginStr = login.getText().toString();
                if (TextUtils.isEmpty(loginStr))
                {
                    ToastUtil.showToast("用户名不能为空！");
                    return;
                }
                String passwordStr = password.getText().toString();
                if (TextUtils.isEmpty(passwordStr))
                {
                    ToastUtil.showToast("密码不能为空！");
                    return;
                }
                int roleValue = (int) role.getSelectedItemId();

                int success = 0;
                if (attendant == null)
                {
                    if (uilAtdService.isAtdNameValid(loginStr) || "admin".equals(loginStr))
                    {
                        ToastUtil.showToast("用户名已经使用！");
                        return;
                    }

                    AttendantListItem attendant = new AttendantListItem();
//                    attendant.setName(nameStr);
                    attendant.setLogin(loginStr);
                    attendant.setPassword(passwordStr);
                    attendant.setPriority(roleValue);
                    success = uilAtdService.addAtd(attendant.getAttendant());
                }
                else
                {

//                    attendant.setName(nameStr);
                    attendant.setLogin(loginStr);
                    attendant.setPassword(passwordStr);
                    attendant.setPriority(roleValue);
                    success = uilAtdService.modifyAtdInfo(attendant.getAttendant());
                }

                if (success == CommonConstantEntry.METHOD_SUCCESS)
                {
                    ToastUtil.showToast("操作成功！");
                    AttendantlistFragment userlistFragment = new AttendantlistFragment();
                    getFragmentManager().beginTransaction().replace(R.id.container_setting_right, userlistFragment).commit();
                }
                else if (success == CommonConstantEntry.METHOD_FAILED)
                {
                    ToastUtil.showToast("操作失败！");
                }
                else if (success == CommonConstantEntry.OUT_ATTEND_COUNT)
                {
                    ToastUtil.showToast("添加用户超出数量限制！");
                }
                else
                {

                }
            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
        else if (v.getId() == cancle.getId())
        {
            AttendantlistFragment userlistFragment = new AttendantlistFragment();
            getFragmentManager().beginTransaction().replace(R.id.container_setting_right, userlistFragment).commit();
        }
    }

}
