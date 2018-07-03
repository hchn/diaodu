package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.adapter.CallRecordDetailAdapter;
import com.jiaxun.uil.util.UiEventEntry;

/**
 * 说明：会议成员详情界面
 *
 * @author  chaimb
 *
 * @Date 2015-6-6
 */
public class CallRecordDetailFragment extends BaseFragment implements OnClickListener
{

    private static String TAG = CallRecordDetailFragment.class.getName();
    private ProgressBarReceiver mReceiver;
    private ArrayList<CallRecord> allCallRecords;
    private ArrayList<CallRecord> currentCallRecords;
    private CallRecord callRecord;
    private ListView myListView;

    private TextView backTextView;
    private TextView allDetialTextView;
    private CallRecordDetailAdapter madapter;
    private IntentFilter filter;
    private LinearLayout backLinearlayout;
    private ImageView backImageView;
    private int type;
    private int count;
    private int visiblePosition;

    private class ProgressBarReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            int fileLength = intent.getExtras().getInt(CommonEventEntry.PARAM_PROGRESS_MAX);
            int offset = intent.getExtras().getInt(CommonEventEntry.PARAM_PROGRESS);
            String recordId = intent.getExtras().getString(CommonEventEntry.DOWNLOAD_ID);
            if (madapter != null)
            {
                if (!(TextUtils.isEmpty(recordId)))
                {
                    madapter.updetaProgressBar(fileLength, offset, recordId);
                }
            }
        }
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_conf_member_list;
    }

    @Override
    public void initComponentViews(View view)
    {

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            callRecord = bundle.getParcelable(CommonConstantEntry.DATA_OBJECT);
            type = bundle.getInt(UiEventEntry.CALLRECORD_TYPE);
            count = bundle.getInt(UiEventEntry.CALLRECORD_COUNT);
            visiblePosition = bundle.getInt(UiEventEntry.CALLRECORD_VISIBLEPOSITION);
        }

        myListView = (ListView) view.findViewById(R.id.conf_member_list);
        backTextView = (TextView) view.findViewById(R.id.back_textview);
        allDetialTextView = (TextView) view.findViewById(R.id.all_detail_textview);
        backLinearlayout = (LinearLayout) view.findViewById(R.id.back_linearlayout);
        backImageView = (ImageView) view.findViewById(R.id.back_call_record_imageview);
        backTextView.setVisibility(View.VISIBLE);

        allCallRecords = new ArrayList<CallRecord>();
        currentCallRecords = new ArrayList<CallRecord>();

        int callType = callRecord.getCallType();
        boolean isChairMan = callRecord.isChairman();

        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {// 会议
            backTextView.setText("会议");
            allDetialTextView.setVisibility(View.GONE);

            if (isChairMan)
            {// 会议 而且是主席
                String confId = callRecord.getConfId();
                currentCallRecords = UiApplication.getCallRecordService().getConfCallRecordList(confId);
            }
        }
        else
        {// 单呼和监控
            allDetialTextView.setVisibility(View.VISIBLE);
            backTextView.setText(callRecord.getPeerNum());
            String callNum = callRecord.getPeerNum();
            ArrayList<CallRecord> tempCallRecords = UiApplication.getCallRecordService().getCallRecords(callNum);

            if (type == 1)
            {
                for (CallRecord callRecord : tempCallRecords)
                {
                    long durtion = 0;
                    if (callRecord.getConnectTime() == 0)
                    {
                        durtion = 0;
                    }
                    else
                    {
                        durtion = callRecord.getReleaseTime() - callRecord.getConnectTime();
                    }

                    if (!(callRecord.isOutGoing()) && durtion > 0)
                    {
                        allCallRecords.add(callRecord);
                    }
                }
            }
            else if (type == 2)
            {
                for (CallRecord callRecord : tempCallRecords)
                {
                    long durtion = 0;
                    if (callRecord.getConnectTime() == 0)
                    {
                        durtion = 0;
                    }
                    else
                    {
                        durtion = callRecord.getReleaseTime() - callRecord.getConnectTime();
                    }
                    if (!(callRecord.isOutGoing()) && durtion == 0)
                    {
                        allCallRecords.add(callRecord);
                    }

                }

            }
            else if (type == 3)
            {
                for (CallRecord callRecord : tempCallRecords)
                {
                    if (callRecord.isOutGoing())
                    {
                        allCallRecords.add(callRecord);
                    }
                }

            }
            else
            {// 全部
                allCallRecords = tempCallRecords;
            }

            allCallRecords = sortByTime(allCallRecords);
            long startTime = callRecord.getStartTime();

            int index = 0;
            for (int i = 0; i < allCallRecords.size(); i++)
            {
                if (allCallRecords.get(i).getStartTime() == startTime)
                {
                    index = i;
                    break;
                }
                else
                {
                    Log.info(TAG, "startTime::" + startTime);
                }
            }

            for (int i = index; i < (index + count); i++)
            {
                currentCallRecords.add(allCallRecords.get(i));
            }

            if (allCallRecords.size() == currentCallRecords.size())
            {
                allDetialTextView.setVisibility(View.GONE);
            }
            else
            {
                allDetialTextView.setVisibility(View.VISIBLE);
            }

        }

        currentCallRecords = sortByTime(currentCallRecords);

        madapter = new CallRecordDetailAdapter(getActivity(), currentCallRecords, myListView, callType);
        myListView.setAdapter(madapter);
        setListener();

    }

    @Override
    public void onDestroy()
    {
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mReceiver = new ProgressBarReceiver();
        filter = new IntentFilter(CommonEventEntry.EVENT_PROGRESS_CHANGE);
        getActivity().registerReceiver(mReceiver, filter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setListener()
    {
        backImageView.setOnClickListener(this);
        backLinearlayout.setOnClickListener(this);
        allDetialTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_call_record_imageview:
            case R.id.back_linearlayout:
//                getFragmentManager().popBackStack();
//                parentActivity.backToPreFragment(R.id.container_right_content);
                Bundle bundle = new Bundle();
                bundle.putInt(UiEventEntry.CALLRECORD_VISIBLEPOSITION, visiblePosition);
                bundle.putInt(UiEventEntry.CALLRECORD_TYPE, type);
                parentActivity.turnToFragmentStack(R.id.container_right_content, CallRecordListFragment.class, bundle);
                break;
            case R.id.all_detail_textview:
                madapter.updateData(allCallRecords);
                allDetialTextView.setVisibility(View.GONE);
                break;

            default:
                break;
        }

    }

    private ArrayList<CallRecord> sortByTime(ArrayList<CallRecord> list)
    {
        if (list != null && list.size() > 0)
        {// 通过冒泡排序list

            for (int i = 0; i < list.size() - 1; i++)
            {

                for (int j = i + 1; j < list.size(); j++)
                {
                    if (list.get(i).getStartTime() < list.get(j).getStartTime())
                    {
                        CallRecord temp = null;
                        temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);
                    }
                }
            }
        }
        return list;

    }

}
