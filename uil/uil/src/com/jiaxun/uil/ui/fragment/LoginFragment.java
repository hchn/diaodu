package com.jiaxun.uil.ui.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.DialogUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：系统账户登陆碎片
 *
 * @author  hubin
 *
 * @Date 2015-2-28
 */
public class LoginFragment extends BaseFragment implements OnClickListener
{
    private static final String TAG = LoginFragment.class.getName();
    private EditText accountET;
    private EditText passwordET;
    private Button loginBtn;
    private String name;
    private String password;

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonlogin:
                if (!UiApplication.isServiceStarted)
                {
                    DialogUtil.showFailDialog(getActivity(), "系统服务尚未启动完成，请稍等...", false);
                    return;
                }
                name = accountET.getText().toString();
                password = passwordET.getText().toString();
                if (name.equals(""))
                {
                    DialogUtil.showFailDialog(getActivity(), getResources().getString(R.string.faild_account_empty), false);
                }
                else if (password.equals(""))
                {
                    DialogUtil.showFailDialog(getActivity(), getResources().getString(R.string.faild_password_empty), false);
                }
//                else if(accountService.isAtdNameValid(name))
//                {
//                    Toast.makeText(getActivity(), "用户不存在", Toast.LENGTH_LONG).show();
//                }
                else if (UiApplication.getAtdService().isAtdAuthorized(name, password))
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_ATD_LOGIN, name);
                    String date = DateUtils.formatStartTime(System.currentTimeMillis());
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_ATD_LOGIN_DATETIME, date);
                }
                else
                {
                    DialogUtil.showFailDialog(getActivity(), getResources().getString(R.string.faild_account), false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");
        accountET = (EditText) view.findViewById(R.id.edittextaccount);
//        accountET.setText("admin");
        passwordET = (EditText) view.findViewById(R.id.edittextpassword);
//        passwordET.setText("admin");
        loginBtn = (Button) view.findViewById(R.id.buttonlogin);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_login;
    }

}
