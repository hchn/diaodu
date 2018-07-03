package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.DensityUtil;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.setting.ui.adapter.CallRecordScreenAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.view.TopStatusPaneView;
import com.jiaxun.uil.ui.widget.selectdate.DateListener;
import com.jiaxun.uil.util.SearchContacts;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiUtils;

public class CallRecordScreenFrament extends BaseFragment implements OnClickListener
{
    private static final String TAG = CallRecordScreenFrament.class.getName();
    private ArrayList<CallRecordListItem> allCallRecordList;
    private ArrayList<CallRecordListItem> currentCallRecordList;

    private ArrayList<CallRecordListItem> receivedCallRecordListItems;
    private ArrayList<CallRecordListItem> missedCallRecordListItems;
    private ArrayList<CallRecordListItem> dialedCallRecordListItems;

    private ListView screenListView;

    private HorizontalScrollView screenHorizontalScrollView;
    private CallRecordScreenAdapter adapter;

    private SearchContacts sc = null;
    // 当前来电类型 0:全部 1:已接 2:已拨 3:未接
    private int currentType = 0;
    private LinearLayout callTypeLayout;
    private EditText searchEditText;
    private String searchString = "";
    private int clickId = 0;
    private LinearLayout startTimeLayout;
    private TextView startTimeTextView;

    private LinearLayout endTimeLayout;
    private TextView endTimeTextView;

    private TextView typeTextView;
    private LinearLayout searchLayout;

    private LinearLayout exportLayout;

    private long startTime = 0;

    private long endTime = 0;
    private ImageView confImageView;
    private TextView peerNumTextView;
    private TextView peerNameTextView;
    private TextView callTypeTextView;
    private TextView callStartTimeTextView;
    private TextView connectTimeTextView;
    private TextView releaseTimeTextView;
    private TextView callprorityTextView;
    private TextView releaseReasonTextView;
    private TextView durationTimeTextView;
    private TextView outGoingTypeTextView;
    private TextView outGoingTextView;
    private TextView atdNameTextView;

    public CallRecordScreenFrament()
    {
        Log.info(TAG, "CallRecordScreenFrament::");
        currentCallRecordList = new ArrayList<CallRecordListItem>();
        allCallRecordList = new ArrayList<CallRecordListItem>();
        dialedCallRecordListItems = new ArrayList<CallRecordListItem>();
        receivedCallRecordListItems = new ArrayList<CallRecordListItem>();
        missedCallRecordListItems = new ArrayList<CallRecordListItem>();
        setData();
    }

    @Override
    public int getLayoutView()
    {

        return R.layout.fragment_call_record_screen;
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");

        sc = new SearchContacts();

        callTypeLayout = (LinearLayout) view.findViewById(R.id.calltype_layout);
        searchEditText = (EditText) view.findViewById(R.id.search_callrecord_edittext);
        startTimeLayout = (LinearLayout) view.findViewById(R.id.starttime_layout);
        startTimeTextView = (TextView) view.findViewById(R.id.starttime_textview);

        endTimeLayout = (LinearLayout) view.findViewById(R.id.endtime_layout);
        endTimeTextView = (TextView) view.findViewById(R.id.endtime_textview);

        typeTextView = (TextView) view.findViewById(R.id.type_textview);
        searchLayout = (LinearLayout) view.findViewById(R.id.search_layout);

        exportLayout = (LinearLayout) view.findViewById(R.id.export_layout);
        exportImageView = (ImageView) view.findViewById(R.id.export_imageview);
        screenListView = (ListView) view.findViewById(R.id.screen_listview);
        searchImageView = (ImageView) view.findViewById(R.id.search_imageview);

        screenHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.screen_scrollview);

        confImageView = (ImageView) view.findViewById(R.id.conf_imageview);
        callerNumTextView = (TextView) view.findViewById(R.id.caller_num_textview);
        callerNameTextView = (TextView) view.findViewById(R.id.caller_name_textview);
        peerNumTextView = (TextView) view.findViewById(R.id.peer_num_textview);
        peerNameTextView = (TextView) view.findViewById(R.id.peer_name_textview);
        callTypeTextView = (TextView) view.findViewById(R.id.call_type_textview);
        callStartTimeTextView = (TextView) view.findViewById(R.id.start_time_textview);
        connectTimeTextView = (TextView) view.findViewById(R.id.connect_time_textview);
        releaseTimeTextView = (TextView) view.findViewById(R.id.release_time_textview);
        callprorityTextView = (TextView) view.findViewById(R.id.call_priority_textview);
        releaseReasonTextView = (TextView) view.findViewById(R.id.release_reason_textview);
        durationTimeTextView = (TextView) view.findViewById(R.id.duration_time_textview);
        outGoingTypeTextView = (TextView) view.findViewById(R.id.out_going_type_textview);
        outGoingTextView = (TextView) view.findViewById(R.id.out_going_textview);
        atdNameTextView = (TextView) view.findViewById(R.id.atd_name_textview);

        confImageView.setVisibility(View.INVISIBLE);
        callerNumTextView.setText("主叫号码");
        callerNumTextView.setGravity(Gravity.CENTER);

        callerNameTextView.setText("主叫名称");
        callerNameTextView.setGravity(Gravity.CENTER);

        peerNumTextView.setText("被叫号码");
        peerNumTextView.setGravity(Gravity.CENTER);

        peerNameTextView.setText("被叫名称");
        peerNameTextView.setGravity(Gravity.CENTER);

        callTypeTextView.setText("呼叫类型");
        callTypeTextView.setGravity(Gravity.CENTER);

        callStartTimeTextView.setText("呼叫开始时间");
        callStartTimeTextView.setGravity(Gravity.CENTER);

        connectTimeTextView.setText("通话开始时间");
        connectTimeTextView.setGravity(Gravity.CENTER);

        releaseTimeTextView.setText("通话结束时间");
        releaseTimeTextView.setGravity(Gravity.CENTER);

        callprorityTextView.setText("呼叫级别");
        callprorityTextView.setGravity(Gravity.CENTER);

        releaseReasonTextView.setText("释放原因");
        releaseReasonTextView.setGravity(Gravity.CENTER);

        durationTimeTextView.setText("通话时长");
        durationTimeTextView.setGravity(Gravity.CENTER);

        outGoingTypeTextView.setText("呼叫状态");
        outGoingTypeTextView.setGravity(Gravity.CENTER);

        outGoingTextView.setText("呼叫方向");
        outGoingTextView.setGravity(Gravity.CENTER);

        atdNameTextView.setText("值班员名称");
        atdNameTextView.setGravity(Gravity.CENTER);

        // 默认时间
        startTimeTextView.setText(DateUtils.formatCallStartTime(DateUtils.getTimeMilliseconds("2000.01.01 00:00:00")));
        startTime = DateUtils.getTimeMilliseconds("2000.01.01 00:00:01");
        Log.info(TAG, "startTime::" + startTime);
        String endTimeDay = DateUtils.formatCallStartTime(System.currentTimeMillis());
        endTimeTextView.setText(endTimeDay);

        endTime = DateUtils.getTimeMilliseconds(endTimeDay + " 23:59:59");

        LinearLayout.LayoutParams params = (LayoutParams) screenHorizontalScrollView.getLayoutParams();
        params.width = (int) ((DensityUtil.getDisplayWidthPixels(getActivity()) / 3) * 2);
        params.height = DensityUtil.getDisplayHeightPixels(getActivity()) - TopStatusPaneView.getInstance().getView().getHeight();
        screenHorizontalScrollView.setLayoutParams(params);

        adapter = new CallRecordScreenAdapter(getActivity(), screenListView, currentCallRecordList);

        screenListView.setAdapter(adapter);

        if (currentCallRecordList != null && currentCallRecordList.size() > 0)
        {
            adapter.updateDate(currentCallRecordList);
        }

        initPopWindow();

        setListener();
    }

    private void initPopWindow()
    {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeView = inflater.inflate(R.layout.call_record_select_type, null);

        allTypeTextView = (TextView) typeView.findViewById(R.id.call_record_all_textview);
        receivedTypeTextView = (TextView) typeView.findViewById(R.id.call_record_received_textview);
        missedTypeTextView = (TextView) typeView.findViewById(R.id.call_record_missed_textview);
        dialedTypeTextView = (TextView) typeView.findViewById(R.id.call_record_dial_textview);
        allTypeTextView.setOnClickListener(this);
        receivedTypeTextView.setOnClickListener(this);
        missedTypeTextView.setOnClickListener(this);
        dialedTypeTextView.setOnClickListener(this);
    }

    private void setData()
    {

        Log.info(TAG, "setData::");
        allCallRecordList.clear();
        receivedCallRecordListItems.clear();
        missedCallRecordListItems.clear();
        dialedCallRecordListItems.clear();

        for (CallRecord callRecord : UiApplication.getCallRecordService().getAllCallRecords())
        {
            allCallRecordList.add(0, ServiceUtils.setCallRecordList(callRecord));
        }

        Log.info(TAG, " " + allCallRecordList.size());

        for (CallRecordListItem allCallRecordListItem : allCallRecordList)
        {
            if (!(allCallRecordListItem.getCallRecord().isOutGoing()) && allCallRecordListItem.getCallRecord().getConnectTime() > 0)
            {
                receivedCallRecordListItems.add(allCallRecordListItem);
            }

            if (!(allCallRecordListItem.getCallRecord().isOutGoing()) && allCallRecordListItem.getCallRecord().getConnectTime() == 0)
            {
                missedCallRecordListItems.add(allCallRecordListItem);
            }

            if (allCallRecordListItem.getCallRecord().isOutGoing())
            {
                dialedCallRecordListItems.add(allCallRecordListItem);
            }
        }
    }

    private void setListener()
    {
        startTimeLayout.setOnClickListener(this);
        endTimeLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        callTypeLayout.setOnClickListener(this);
        exportLayout.setOnClickListener(this);
        exportImageView.setOnClickListener(this);
        searchImageView.setOnClickListener(this);

        searchEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                searchString = s.toString();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private MyDateListener listener;
    private View typeView;

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.search_imageview:
                Log.info(TAG, "search");
                setData();
                if (currentType == 1)
                {
                    currentCallRecordList = receivedCallRecordListItems;
                }
                else if (currentType == 2)
                {
                    currentCallRecordList = missedCallRecordListItems;
                }
                else if (currentType == 3)
                {
                    currentCallRecordList = dialedCallRecordListItems;
                }
                else
                {
                    currentCallRecordList = allCallRecordList;
                }
                sc.clear();
                sc.setRecordAll(currentCallRecordList);

                if (endTime != 0 && startTime != 0 && !("".equals(searchString)))
                {// 时间关键字都有

                    if (endTime < startTime)
                    {
                        ToastUtil.showToast("", "结束时间不能小于开始时间，请重新选择");
                        return;
                    }
                    else
                    {
                        currentCallRecordList = sc.onRecordSearch(searchString, startTime, endTime);
                        adapter.updateDate(currentCallRecordList);
                    }
                }
                else if ("".equals(searchString) && startTime != 0 && endTime != 0)
                {

                    if (endTime < startTime)
                    {
                        ToastUtil.showToast("", "结束时间不能小于开始时间");
                        return;
                    }
                    else
                    {

                        currentCallRecordList = sc.onRecordSearch(startTime, endTime);
                        adapter.updateDate(currentCallRecordList);
                    }
                }
                else
                {
                    adapter.updateDate(currentCallRecordList);
                }

                break;
            case R.id.calltype_layout:
                showSelectTypePop(callTypeLayout);
                break;
            case R.id.export_layout:
            case R.id.export_imageview:
                // 导出通话记录
                if (currentCallRecordList != null && currentCallRecordList.size() > 0)
                {
                    exportCallRecord(currentCallRecordList);
                }
                else
                {
                    ToastUtil.showToast("", "筛选记录为空,请重新选择筛选条件");
                    return;
                }
                break;
            case R.id.starttime_layout:
                clickId = R.id.starttime_layout;
                if (listener == null)
                {
                    listener = new MyDateListener(getActivity());
                }
                listener.showDate(startTimeLayout);
                listener.initTime(startTime);
                break;
            case R.id.endtime_layout:
                clickId = R.id.endtime_layout;
                if (listener == null)
                {
                    listener = new MyDateListener(getActivity());
                }
                listener.showDate(endTimeLayout);
                listener.initTime(endTime);
                break;

            case R.id.call_record_all_textview:
                currentType = 0;

                typeTextView.setText("全部");

                allTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.black));
                receivedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                missedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                dialedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                typePopupWindow.dismiss();
                break;
            case R.id.call_record_received_textview:
                currentType = 1;

                receivedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.black));
                allTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                missedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                dialedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                typeTextView.setText("已接");
                typePopupWindow.dismiss();
                break;
            case R.id.call_record_missed_textview:
                currentType = 2;

                missedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.black));
                allTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                allTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                receivedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                dialedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                typeTextView.setText("未接");
                typePopupWindow.dismiss();
                break;
            case R.id.call_record_dial_textview:
                currentType = 3;

                dialedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.black));
                allTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                missedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                receivedTypeTextView.setTextColor(getActivity().getResources().getColor(R.color.calltype_bg));
                typeTextView.setText("已拨");
                typePopupWindow.dismiss();
                break;

            default:
                break;
        }

    }

    /**
     * 导出筛选出的通话记录
     * 方法说明 :
     * @param callRecordListItems
     * @author chaimb
     * @Date 2015-6-18
     */
    private void exportCallRecord(ArrayList<CallRecordListItem> callRecordListItems)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("fileType", 2);
        UiUtils.object = callRecordListItems;
        ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, FileSaveFragment.class, bundle);

    }

    private PopupWindow typePopupWindow = null;
    private TextView callerNumTextView;
    private TextView callerNameTextView;
    private TextView receivedTypeTextView;
    private TextView allTypeTextView;
    private TextView missedTypeTextView;
    private TextView dialedTypeTextView;
    private ImageView exportImageView;
    private ImageView searchImageView;

    private void showSelectTypePop(View view)
    {

        if (typePopupWindow == null)
        {
            typePopupWindow = new PopupWindow(typeView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            typePopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            typePopupWindow.setOutsideTouchable(true);
            typePopupWindow.setFocusable(true);
            typePopupWindow.showAsDropDown(view);

        }
        else
        {
            typePopupWindow.setFocusable(true);
            typePopupWindow.showAsDropDown(view);
        }
    }

    class MyDateListener extends DateListener
    {

        public MyDateListener(Context context)
        {
            super(context);
        }

        @Override
        public void getTimerStr(String timeStr)
        {
            if (clickId == R.id.starttime_layout)
            {
                if (startTimeTextView != null)
                {
                    startTime = DateUtils.getTimeMilliseconds(timeStr + " 00:00:00");
                    startTimeTextView.setText(timeStr);
                }
            }
            else if (clickId == R.id.endtime_layout)
            {
                if (endTimeTextView != null)
                {
                    endTime = DateUtils.getTimeMilliseconds(timeStr + " 23:59:59");
                    endTimeTextView.setText(timeStr);
                }
            }
            else
            {
                // 没有选中
            }
        }

    }

}
