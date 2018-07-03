package com.jiaxun.setting.ui.fragment;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.widget.selectdate.NumericWheelAdapter;
import com.jiaxun.uil.ui.widget.selectdate.WheelView;
import com.jiaxun.uil.util.ToastUtil;

/**
 * ˵����
 *
 * @author  chaimb
 *
 * @Date 2015-8-20
 */
public class PresTimeSettingFragment extends BaseFragment implements OnClickListener
{

    private Button cancelButton;
    private Button saveButton;

    private TextView whichEthernet;

    private WheelView hourWheelView;
    private WheelView minuteWheelView;
    private static final String TAG = PresTimeSettingFragment.class.getName();

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_timesetting;
    }

    @Override
    public void initComponentViews(View view)
    {
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        whichEthernet = (TextView) view.findViewById(R.id.textViewWhichInfo);
        whichEthernet.setText("ʱ������");

        hourWheelView = (WheelView) view.findViewById(R.id.hour_set_wheelview);
        minuteWheelView = (WheelView) view.findViewById(R.id.minute_set_wheelview);

        initData();
        setListener();
    }

    private void initData()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        int curHour = calendar.get(Calendar.HOUR);
        int curMinute = calendar.get(Calendar.MINUTE);

        hourWheelView.setAdapter(new NumericWheelAdapter(1, 24));
        hourWheelView.setLabel("ʱ");

        minuteWheelView.setAdapter(new NumericWheelAdapter(0, 59));
        minuteWheelView.setLabel("��");

        hourWheelView.setCurrentItem(curHour);
        minuteWheelView.setCurrentItem(curMinute);
    }

    private void setListener()
    {
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.cancelButton:
                ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
                break;
            case R.id.saveButton:
//                ToastUtil.showToast("����ʱ��" + (hourWheelView.getCurrentItem() + 1) + "ʱ" + (minuteWheelView.getCurrentItem()) + "��");

                int hour = hourWheelView.getCurrentItem() + 1;
                int minute = minuteWheelView.getCurrentItem();
                Log.info(TAG, "setTime::hour::" + hour + "minute::" + minute);

                try
                {
                    boolean isSuccess = DateUtils.setTime(hour, minute);
                    if (isSuccess)
                    {
                        ToastUtil.showToast("ʱ�����óɹ�");
                    }
                    else
                    {
                        ToastUtil.showToast("ʱ������ʧ��");
                    }
                }
                catch (IOException e)
                {
                    ToastUtil.showToast("ʱ������ʧ��");
                    Log.exception(TAG, e);
                }
                catch (InterruptedException e)
                {
                    ToastUtil.showToast("ʱ������ʧ��");
                    Log.exception(TAG, e);
                }
                ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
                break;

            default:
                break;
        }
    }

}
