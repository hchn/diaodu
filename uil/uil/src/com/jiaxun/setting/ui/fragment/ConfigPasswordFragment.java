package com.jiaxun.setting.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * 说明：操作台用户添加/修改
 * 
 * @author fuluo
 * 
 * @Date 2015-4-10
 * 
 */
public class ConfigPasswordFragment extends Fragment implements OnClickListener
{
    private static String TAG = ConfigPasswordFragment.class.getName();
    private Button config;
    private EditText password;
    private EditText password_confirm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_config_password, container, false);
        config = (Button) view.findViewById(R.id.config);
        config.setOnClickListener(this);
        password = (EditText) view.findViewById(R.id.password);
        password_confirm = (EditText) view.findViewById(R.id.password_confirm);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == config.getId())
        {
            try
            {
                String passwordStr = password.getText().toString();
                String passwordConfrimStr = password_confirm.getText().toString();

                if (!passwordStr.equals(passwordConfrimStr))
                {
                    Toast.makeText(getActivity(), "密码不一致！", Toast.LENGTH_LONG).show();
                    return;
                }
                UiApplication.getConfigService().setConfigPassword(passwordStr);
                Toast.makeText(getActivity(), "操作成功！", Toast.LENGTH_LONG).show();

            }
            catch (Exception e)
            {
                Log.exception(TAG, e);
            }
        }
    }

}
