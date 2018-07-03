package com.jiaxun.uil.ui.widget.selectdate;

import java.util.Calendar;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.uil.R;

/**
 * ˵����
 *
 * @author  chaimb
 *
 * @Date 2015-6-18
 */
public abstract class DateListener implements OnClickListener, TimeContent
{

    private Context context;
    private WheelView yearWheelView;
    private WheelView monWheelView;
    private WheelView dayWheelView;

    private Button confirmButton;
    private Button backButton;
    private View view;

    private TextView timeTextView;

    public DateListener(Context context)
    {
        super();
        this.context = context;
        initView();
        initDate();
        setListener();

    }

    private void initView()
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popwindow_date, null);
        yearWheelView = (WheelView) view.findViewById(R.id.year_wheelview);
        monWheelView = (WheelView) view.findViewById(R.id.month_wheelview);
        dayWheelView = (WheelView) view.findViewById(R.id.day_wheelview);

        backButton = (Button) view.findViewById(R.id.back_button);
        confirmButton = (Button) view.findViewById(R.id.confirm_button);

        timeTextView = (TextView) view.findViewById(R.id.time_textview);
    }

    private void setListener()
    {
        backButton.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
    }

    private void initDate()
    {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;
        int curDay = c.get(Calendar.DATE);
        // ��
        yearWheelView.setAdapter(new NumericWheelAdapter(2000, 2099));
        yearWheelView.setLabel("��");
        // ���ò�ѭ������
//        yearWheelView.setCyclic(true);
        yearWheelView.addScrollingListener(scrollListener);
        // ��
        monWheelView.setAdapter(new NumericWheelAdapter(1, 12));
        monWheelView.setLabel("��");
        // ���ò�ѭ������
//        monWheelView.setCyclic(true);
        monWheelView.addScrollingListener(scrollListener);
        // ��
        initDay(curYear, curMonth);
        dayWheelView.setLabel("��");
        dayWheelView.addScrollingListener(scrollListener);
        // ���ò�ѭ������
//        dayWheelView.setCyclic(true);

        // ����Ĭ��ֵ(�����2000��1��ΪminValue,���õĲ���ȷ��ֵ�������α�)
        yearWheelView.setCurrentItem(curYear - 2000);
        monWheelView.setCurrentItem(curMonth - 1);
        dayWheelView.setCurrentItem(curDay - 1);
    }

    PopupWindow datePopupWindow;

    public void showDate(View v)
    {
        if (datePopupWindow == null)
        {
            datePopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            datePopupWindow.setFocusable(true);
            datePopupWindow.setOutsideTouchable(true);
            datePopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));// �����������ʧ
            datePopupWindow.showAsDropDown(v);
        }
        else
        {
            datePopupWindow.showAsDropDown(v);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_button:
                datePopupWindow.dismiss();
                break;

            case R.id.confirm_button:
//                Calendar c = Calendar.getInstance();
                String current_year = (2000 + yearWheelView.getCurrentItem()) + "";
                String current_month = (monWheelView.getCurrentItem() + 1) + "";
                String current_day = (dayWheelView.getCurrentItem() + 1) + "";
//                if (current_day.length() == 1) {
//                    current_day = "0" + current_day;
//                }
//                if (current_month.length() == 1) {
//                    current_month = "0" + current_month;
//                }
                String searchdate = current_year + "." + current_month + "." + current_day;
                getTimerStr(searchdate);
                datePopupWindow.dismiss();
                break;

            default:
                break;
        }

    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener()
    {

        @Override
        public void onScrollingStarted(WheelView wheel)
        {
        }

        @Override
        public void onScrollingFinished(WheelView wheel)
        {
            int n_year = yearWheelView.getCurrentItem() + 2000;// ��
            int n_month = monWheelView.getCurrentItem() + 1;// ��
            initDay(n_year, n_month);

            timeTextView.setText(n_year + "." + n_month + "." + (dayWheelView.getCurrentItem() + 1));
        }
    };

    /**
     * ����˵�� :��ʼ��Ĭ����ʾʱ��
     * @param timeStr
     * @author chaimb
     * @Date 2015-9-22
     */
    public void initTime(long time)
    {
        if (timeTextView != null)
        {
            String date = DateUtils.formatCallStartTime(time);
            timeTextView.setText(date);
            String[] result = date.split("\\.");
            int curYear = Integer.parseInt(result[0]);
            int curMonth = Integer.parseInt(result[1]);
            int curDay = Integer.parseInt(result[2]);

            yearWheelView.setCurrentItem(curYear - 2000);
            monWheelView.setCurrentItem(curMonth - 1);
            dayWheelView.setCurrentItem(curDay - 1);
        }
    }

    /**
     * ���������������¶�����
     * 
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month)
    {
        int day = 30;
        boolean flag = false;
        switch (year % 4)
        {// �����Ƿ�������
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
     * ����˵�� :��ʼ������
     * @param curYear
     * @param curMonth
     * @author chaimb
     * @Date 2015-8-20
     */
    private void initDay(int curYear, int curMonth)
    {
        dayWheelView.setAdapter(new NumericWheelAdapter(1, getDay(curYear, curMonth), "%02d"));
    }
}
