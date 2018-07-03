package com.jiaxun.setting.ui.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.model.EthernetItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.UiConfigEntry;

/**
 * 说明：以太网口设置
 *
 * @author  zhangxd
 *
 * @Date 2015-7-9
 */
public class NetworkSettingFragment extends BaseFragment
{
    private static final String TAG = NetworkSettingFragment.class.getName();

    private EditText ipEditText;

    private EditText maskEditText;

    private EditText gateEditText;
    private TextView whichEthernet;
    private ToggleButton autoToggleButton;
    private LinearLayout ipLayout;
    private EthernetItem ethEthernetItem;
    private Button cancelButton;

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_network_setting;
    }

    @Override
    public void initComponentViews(View view)
    {
        // TODO Auto-generated method stub
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        whichEthernet = (TextView) view.findViewById(R.id.textViewWhichInfo);
        autoToggleButton = (ToggleButton) view.findViewById(R.id.autoToggleButton);
        ipEditText = (EditText) view.findViewById(R.id.editTextIP);

        maskEditText = (EditText) view.findViewById(R.id.editTextMask);

        gateEditText = (EditText) view.findViewById(R.id.editTextGate);

        ipLayout = (LinearLayout) view.findViewById(R.id.ipLayout);
        ethEthernetItem = getArguments().getParcelable(CommonConstantEntry.DATA_OBJECT);

        if ((ethEthernetItem != null))
        {
            if (ethEthernetItem.getKey().equals(UiConfigEntry.PREF_ETHERNET1))
            {
                whichEthernet.setText("以太网口1设置");
            }
            else
            {
                whichEthernet.setText("以太网口2设置");
            }
        }
        cancelButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                ((SettingActivity) getActivity()).backWireSettingFragment(v.getId());
            }
        });
        autoToggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                // TODO Auto-generated method stub

                if (isChecked)
                {

                    ipEditText.setEnabled(false);
                    maskEditText.setEnabled(false);
                    gateEditText.setEnabled(false);
                }
                else
                {
                    ipEditText.setEnabled(true);
                    maskEditText.setEnabled(true);
                    gateEditText.setEnabled(true);
                }
            }

        });
    }
}
