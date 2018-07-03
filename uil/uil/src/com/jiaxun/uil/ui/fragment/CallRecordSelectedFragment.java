package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.setting.ui.adapter.CallRecordSelectAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：选择通话记录 添加到~黑白名单
 *
 * @author  HeZhen
 *
 * @Date 2015-7-6
 */
public class CallRecordSelectedFragment extends BaseFragment implements OnClickListener
{
    private ListView listView;
    private Button cancelBtn;
    private Button sureBtn;
    private CallRecordSelectAdapter adapter;

    private ArrayList<CallRecordListItem> selectRecords;

    private ArrayList<CallRecordListItem> callRecordList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        selectRecords = new ArrayList<CallRecordListItem>();
        callRecordList = new ArrayList<CallRecordListItem>();
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_call_record_list;
    }

    @Override
    public void initComponentViews(View view)
    {
        listView = (ListView) view.findViewById(R.id.call_record_list);
        view.findViewById(R.id.include_top).setVisibility(View.VISIBLE);
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        sureBtn = (Button) view.findViewById(R.id.btn_create);
        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        callRecordListClone();
        adapter = new CallRecordSelectAdapter(parentActivity, callRecordList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                CallRecordListItem record = (CallRecordListItem) adapter.getItem(position);
                record.setChecked(!record.isChecked());
                adapter.notifyDataSetChanged();
                if (record.isChecked())
                {
                    if (!selectRecords.contains(record))
                    {
                        selectRecords.add(record);
                    }
                }
                else
                {
                    if (selectRecords.contains(record))
                    {
                        selectRecords.remove(record);
                    }
                }
            }
        });
    }

    private void callRecordListClone()
    {
        ArrayList<CallRecord> callRecords = UiApplication.getCallRecordService().getAllCallRecords();
        ArrayList<CallRecordListItem> callRecordListItems = new ArrayList<CallRecordListItem>();

        for (CallRecord callRecord : callRecords)
        {
            CallRecordListItem callRecordListItem = ServiceUtils.setCallRecordList(callRecord);
            callRecordListItems.add(callRecordListItem);
        }
        callRecordList.clear();
        for (CallRecordListItem callRecord : callRecordListItems)
        {
            callRecordList.add((CallRecordListItem) callRecord.clone());
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v == cancelBtn)
        {
            parentActivity.backToPreFragment(R.id.container_setting_right);
        }
        else if (v == sureBtn)
        {
            sureToNext();
        }
    }

    private void sureToNext()
    {
        parentActivity.backToPreFragment(R.id.container_setting_right);
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.BLACK_WHITE_ADD_RECORD, selectRecords.clone());
    }
}
