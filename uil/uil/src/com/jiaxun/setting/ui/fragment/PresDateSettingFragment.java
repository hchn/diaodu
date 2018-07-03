package com.jiaxun.setting.ui.fragment;

import java.io.IOException;
import java.util.Calendar;

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
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-8-20
 */
public class PresDateSettingFragment extends BaseFragment implements OnClickListener
{

    private static final String TAG = PresDateSettingFragment.class.getName();
    private Button cancelButton;
    private Button saveButton;

    private TextView whichEthernet;

    private WheelView yearWheelView;
    private WheelView monWheelView;
    private WheelView dayWheelView;

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_datesetting;
    }

    @Override
    public void initComponentViews(View view)
    {
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        whichEthernet = (TextView) view.findViewById(R.id.textViewWhichInfo);
        whichEthernet.setText("日期设置");

        yearWheelView = (WheelView) view.findViewById(R.id.year_set_wheelview);
        monWheelView = (WheelView) view.findViewById(R.id.month_set_wheelview);
        dayWheelView = (WheelView) view.findViewById(R.id.day_set_wheelview);

        initDate();

        setListener();
    }

    private void initDate()
    {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 月份的下标是从0开始的
        int curDate = c.get(Calendar.DATE);
        // 年
        yearWheelView.setAdapter(new NumericWheelAdapter(2000, 2099));
        yearWheelView.setLabel("年");
        // 设置不循环滚动
//        yearWheelView.setCyclic(true);
        // 月
        monWheelView.setAdapter(new NumericWheelAdapter(1, 12));
        monWheelView.setLabel("月");
        // 设置不循环滚动
//        monWheelView.setCyclic(true);
        // 日
        initDay(curYear, curMonth);
        dayWheelView.setLabel("日");
        // 设置不循环滚动
//        dayWheelView.setCyclic(true);

        // 设置默认值(下面的2000，1即为minValue,设置的不是确切值，而是游标)
        yearWheelView.setCurrentItem(curYear - 2000);
        monWheelView.setCurrentItem(curMonth - 1);
        dayWheelView.setCurrentItem(curDate - 1);
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
//                ToastUtil.showToast("设置日期" + (yearWheelView.getCurrentItem() + 2000) + "年" + (monWheelView.getCurrentItem() + 1) + "月"
//                        + (dayWheelView.getCurrentItem() + 1) + "日");

                int year = yearWheelView.getCurrentItem() + 2000;
                int month = monWheelView.getCurrentItem() + 1;
                int day = dayWheelView.getCurrentItem() + 1;
                Log.info(TAG, "setDate::year::" + year + "month::" + month + "day::" + day);
                try
                {
                    boolean isSuccess = DateUtils.setDate(year, month, day);
                    if (isSuccess)
                    {
                        ToastUtil.showToast("日期设置成功");
                    }
                    else
                    {
                        ToastUtil.showToast("日期设置失败");
                    }
                }
                catch (IOException e)
                {
                    Log.exception(TAG, e);
                    ToastUtil.showToast("日期设置失败");
                }
                catch (InterruptedException e)
                {
                    Log.exception(TAG, e);
                    ToastUtil.showToast("日期设置失败");
                }
                ((SettingActivity) getActivity()).backSystemSettingFragment(v.getId());
                break;

            default:
                break;
        }
    }

    /**
     * 
     * 方法说明 :根据年月算出这个月多少天
     * @param year
     * @param month
     * @return
     * @author chaimb
     * @Date 2015-8-20
     */
    private int getDay(int year, int month)
    {
        int day = 30;
        boolean flag = false;
        switch (year % 4)
        {// 计算是否是闰年
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     * 
     * 方法说明 :初始化天数
     * @param curYear  当前年
     * @param curMonth 当前月
     * @author chaimb
     * @Date 2015-8-20
     */
    private void initDay(int curYear, int curMonth)
    {
        dayWheelView.setAdapter(new NumericWheelAdapter(1, getDay(curYear, curMonth), "%02d"));
    }

}
