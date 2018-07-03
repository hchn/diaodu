package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 
 * 说明：通话记录列表适配器
 * 
 * @author chaimb
 * 
 * @Date 2015-5-6
 */

public class CallRecordListAdapter extends BaseAdapter implements OnClickListener, OnItemClickListener
{

    private final static String TAG = CallRecordListAdapter.class.getName();
    private ArrayList<CallRecordListItem> callRecordListItems;

    private Context context;

    private LayoutInflater inflater;

    private ListView mListView;

    private int type;

    public CallRecordListAdapter()
    {
        super();
    }

    public CallRecordListAdapter(Context context, ListView mListView, int type)
    {
        super();
        this.context = context;
        this.mListView = mListView;
        this.type = type;
        inflater = LayoutInflater.from(context);
    }

    public ArrayList<CallRecordListItem> getCallRecordListItems()
    {
        return callRecordListItems;
    }

    public void setCallRecordListItems(ArrayList<CallRecordListItem> callRecordListItems)
    {
        this.callRecordListItems = callRecordListItems;
    }

    @Override
    public int getCount()
    {
        if (callRecordListItems != null && callRecordListItems.size() > 0)
        {
            return callRecordListItems.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_call_record_list_item, null);
            holder.callRecordListItemLinearLayout = (LinearLayout) convertView.findViewById(R.id.call_record_list_item_linearlayout);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.name_textview);
            holder.numTextView = (TextView) convertView.findViewById(R.id.num_textview);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.time_textview);
            holder.detailImageView = (ImageView) convertView.findViewById(R.id.detail_imageview);
            holder.headerImageView = (ImageView) convertView.findViewById(R.id.header_imageview);
            holder.detailRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.detail_relativelayout);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CallRecord callRecord = callRecordListItems.get(position).getCallRecord();
        // 会议列表设置
        int callType = callRecord.getCallType();
        boolean isChairMan = callRecord.isChairman();

        // 会议
        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {
            if (isChairMan)
            {// 会议 而且是主席
                holder.headerImageView.setImageResource(R.drawable.conf_icon);
                holder.nameTextView.setText(callRecord.getConfName());
                holder.numTextView.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.numTextView.setVisibility(View.VISIBLE);
            if (callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
            { // 监控
                holder.headerImageView.setImageResource(R.drawable.vs_icon);
            }
            else
            {
                holder.headerImageView.setImageResource(R.drawable.user_icon);
            }

            String peerName = "";

            if (UiApplication.getInstance().getContactService().getContactByPhoneNum(callRecord.getPeerNum()) != null)
            {
                peerName = UiApplication.getInstance().getContactService().getContactByPhoneNum(callRecord.getPeerNum()).getName();
            }
            else
            {
                peerName = callRecord.getPeerNum();
            }
            int count = callRecordListItems.get(position).getCount();

            if (count == 1)
            {
                holder.nameTextView.setText(peerName);
            }
            else
            {
                holder.nameTextView.setText(peerName + "(" + count + ")");
            }
            holder.numTextView.setText(callRecord.getPeerNum());
        }

        // 设置时间
        String time = DateUtils.formatDateTime(callRecord.getStartTime());
        holder.timeTextView.setText(time);

        Drawable drawable = null;
        if (!(callRecord.isOutGoing()) && callRecord.getConnectTime() == 0)
        {// 未接
            drawable = context.getResources().getDrawable(R.drawable.call_miss_icon);
        }
        else if (callRecord.isOutGoing())
        {// 已拨
            drawable = context.getResources().getDrawable(R.drawable.call_outgoing_icon);
        }
        else
        {// 已接
            drawable = context.getResources().getDrawable(R.drawable.call_incoming_icon);
        }
        holder.numTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        holder.numTextView.setCompoundDrawablePadding(5);// 设置图片和text之间的间距

        // 获取下载录音录像文件id
        Bundle bundle = new Bundle();

        bundle.putParcelable("callRecord", callRecord);
        bundle.putInt("count", callRecordListItems.get(position).getCount());
        holder.detailImageView.setTag(bundle);
        holder.detailRelativeLayout.setTag(bundle);

        holder.detailImageView.setOnClickListener(this);
        holder.detailRelativeLayout.setOnClickListener(this);

//        if (callRecordListItems.size() == 1)
//        {
//
//            holder.callRecordListItemLinearLayout.setBackgroundResource(R.drawable.call_record_item_one_selector);
//        }
//        else
//        {
//
//            if (position == callRecordListItems.size() - 1)
//            {
//                holder.callRecordListItemLinearLayout.setBackgroundResource(R.drawable.call_record_item_down_selector);
//            }
//            if (position == 0)
//            {
//                holder.callRecordListItemLinearLayout.setBackgroundResource(R.drawable.call_record_item_selector);
//            }
//        }

//        Log.info(TAG, position + "");

        mListView.setOnItemClickListener(this);
        return convertView;
    }

    class ViewHolder
    {
        LinearLayout callRecordListItemLinearLayout;
        ImageView headerImageView;
        ImageView detailImageView;
        TextView nameTextView;
        TextView numTextView;
        TextView timeTextView;

        RelativeLayout detailRelativeLayout;
        // 详情view

    }

    /**
     * 
     * 方法说明 :更新数据
     * 
     * @param callRecordListItem
     * @author chaimb 
     * @Date 2015-5-29
     */
    public void addData(CallRecordListItem callRecordListItem)
    {
        this.callRecordListItems.add(0, callRecordListItem);
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.detail_imageview:
            case R.id.detail_relativelayout:

                int visiblePosition = mListView.getFirstVisiblePosition();
                Bundle itemBundle = (Bundle) v.getTag();
                CallRecord callRecord = itemBundle.getParcelable("callRecord");
                int count = itemBundle.getInt("count");
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_CONF_MEMBER_CALL_RECORD, callRecord, type, visiblePosition, count);
                break;
            default:
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // TODO Auto-generated method stub
        CallRecord caRecord = callRecordListItems.get(position).getCallRecord();
        int callType = caRecord.getCallType();
        boolean isChairMan = caRecord.isChairman();
        String confId = caRecord.getConfId();
        ArrayList<CallRecord> confRecord = UiApplication.getCallRecordService().getConfCallRecordList(confId);
        mListView.setEnabled(false);
        // 避免频繁点击
        new Handler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                mListView.setEnabled(true);

            }
        }, 1000);
        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {// 发起会议
            if (isChairMan)
            {
                ArrayList<String> numberList = new ArrayList<String>();
                for (CallRecord record : confRecord)
                {
                    String number = record.getPeerNum();
                    if (!TextUtils.isEmpty(number))
                    {
                        if (!(numberList.contains(number)))
                        {// 去除重复数据
                            numberList.add(number);
                        }
                    }
                }
                ServiceUtils.makeConf(context, caRecord.getConfName(), numberList);
            }
            else
            {
                Log.info(TAG, "isChairMan::" + isChairMan);
            }

        }
        else if (callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
        {// 发起视频监控
            UiApplication.getVsService().addVsUser(callRecordListItems.get(position).getCallRecord().getPeerNum());

        }
        else
        {// 发起单呼
            ServiceUtils.makeCall(context, caRecord.getPeerNum());
        }
    }

}
