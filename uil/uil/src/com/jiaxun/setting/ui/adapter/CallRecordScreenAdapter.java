package com.jiaxun.setting.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
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
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.view.ConfMemberListView;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallRecordListItem;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-6-17
 */
public class CallRecordScreenAdapter extends BaseAdapter implements OnItemClickListener, OnClickListener
{

    private String TAG = CallRecordScreenAdapter.class.getName();
    private Context context;
    private ArrayList<CallRecordListItem> callRecordListItems;
    private LayoutInflater inflater;
    private ListView screenListView;
    private int currentPosition;

    public CallRecordScreenAdapter()
    {
        super();
    }

    public CallRecordScreenAdapter(Context context, ListView screenListView, ArrayList<CallRecordListItem> callRecordList)
    {
        super();
        this.context = context;
        this.callRecordListItems = callRecordList;
        inflater = LayoutInflater.from(context);
        this.screenListView = screenListView;
        this.screenListView.setOnItemClickListener(this);

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
//        return 1;
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
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_screen_item, null);
            holder.callerNumTextView = (TextView) convertView.findViewById(R.id.caller_num_textview);
            holder.callerNameTextView = (TextView) convertView.findViewById(R.id.caller_name_textview);
            holder.peerNumTextView = (TextView) convertView.findViewById(R.id.peer_num_textview);
            holder.peerNameTextView = (TextView) convertView.findViewById(R.id.peer_name_textview);
            holder.callTypeTextView = (TextView) convertView.findViewById(R.id.call_type_textview);
            holder.startTimeTextView = (TextView) convertView.findViewById(R.id.start_time_textview);
            holder.connectTimeTextView = (TextView) convertView.findViewById(R.id.connect_time_textview);
            holder.releaseTimeTextView = (TextView) convertView.findViewById(R.id.release_time_textview);
            holder.callprorityTextView = (TextView) convertView.findViewById(R.id.call_priority_textview);
            holder.releaseReasonTextView = (TextView) convertView.findViewById(R.id.release_reason_textview);
            holder.durationTimeTextView = (TextView) convertView.findViewById(R.id.duration_time_textview);
            holder.outGoingTypeTextView = (TextView) convertView.findViewById(R.id.out_going_type_textview);
            holder.outGoingTextView = (TextView) convertView.findViewById(R.id.out_going_textview);
            holder.atdNameTextView = (TextView) convertView.findViewById(R.id.atd_name_textview);

            holder.myConfLayout = (LinearLayout) convertView.findViewById(R.id.myconf_layout);
            holder.myConfListView = (ConfMemberListView) convertView.findViewById(R.id.conf_listview);
            holder.confImageView = (ImageView) convertView.findViewById(R.id.conf_imageview);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CallRecord callRecord = callRecordListItems.get(position).getCallRecord();
//        CallRecordListItem callRecordListItem = callRecordListItems.get(position);
        // 时间
        String duration = "0秒";
        holder.startTimeTextView.setText(DateUtils.formatStartTime(callRecord.getStartTime()));
        if (callRecord.getConnectTime() == 0)
        {
            holder.connectTimeTextView.setText(DateUtils.formatStartTime(callRecord.getStartTime()));
            holder.durationTimeTextView.setText(duration);
            if (!(callRecord.isOutGoing()))
            {
                holder.outGoingTypeTextView.setText("未接电话");
            }
            else
            {
                holder.outGoingTypeTextView.setText("已拨电话");
            }
        }
        else
        {
            long durationTime = (callRecord.getReleaseTime() - callRecord.getConnectTime()) / 1000;
            duration = DateUtils.formatDurationTime(durationTime);
            holder.connectTimeTextView.setText(DateUtils.formatStartTime(callRecord.getConnectTime()));
            holder.durationTimeTextView.setText(duration);
            if (!(callRecord.isOutGoing()))
            {
                holder.outGoingTypeTextView.setText("已接电话");
            }
            else
            {
                holder.outGoingTypeTextView.setText("已拨电话");
            }

        }
        holder.releaseTimeTextView.setText(DateUtils.formatStartTime(callRecord.getReleaseTime()));

        // 呼叫类型
        int callType = callRecord.getCallType();
        if (callType == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO || callType == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO)
        {//单呼
            holder.callTypeTextView.setText("单呼");
            holder.confImageView.setVisibility(View.INVISIBLE);
        }
        else if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {//会议
            String confId = callRecord.getConfId();
            holder.callTypeTextView.setText("会议");
            holder.confImageView.setVisibility(View.VISIBLE);
//            holder.confImageView.setBackgroundResource(R.drawable.setting_record_conf_icon);
            ArrayList<CallRecord> confList = UiApplication.getCallRecordService().getConfCallRecordList(confId);
            
            if (confList != null && confList.size() > 0)
            {
                CallRecordScreenConfAdapter caConfAdapter = new CallRecordScreenConfAdapter(context, confList);
                holder.myConfListView.setAdapter(caConfAdapter);
            }
        }
        else
        {//监控
            holder.confImageView.setVisibility(View.INVISIBLE);
            holder.callTypeTextView.setText("监控");
        }
        // 呼叫等级
        holder.callprorityTextView.setText(callRecord.getCallPriority() + "");
        // 释放原因
        holder.releaseReasonTextView.setText(SdkUtil.parseReleaseReason(callRecord.getReleaseReason()));
        // 值班员名称
        holder.atdNameTextView.setText(callRecord.getAtdName());
        // 号码和呼叫方向
        if (callRecord.isOutGoing())
        {
            holder.callerNumTextView.setText(callRecord.getCallerNum());
            holder.callerNameTextView.setText(callRecord.getCallerName());
            holder.peerNumTextView.setText(callRecord.getPeerNum());
            if (callRecord.getPeerName() == null || "".equals(callRecord.getPeerName()))
            {
                holder.peerNameTextView.setText(callRecord.getPeerNum());
            }
            else
            {
                holder.peerNameTextView.setText(callRecord.getPeerName());
            }
            holder.outGoingTextView.setText("呼出");
        }
        else
        {
            holder.peerNumTextView.setText(callRecord.getCallerNum());
            holder.peerNameTextView.setText(callRecord.getCallerName());
            holder.callerNumTextView.setText(callRecord.getPeerNum());
            if (callRecord.getPeerName() == null || "".equals(callRecord.getPeerName()))
            {
                holder.callerNameTextView.setText(callRecord.getPeerNum());
            }
            else
            {
                holder.callerNameTextView.setText(callRecord.getPeerName());
            }
            holder.outGoingTextView.setText("呼入");
        }

//        Log.info(TAG, "position" + position);
        return convertView;
    }

    class ViewHolder
    {
        TextView callerNumTextView;
        TextView callerNameTextView;
        TextView peerNumTextView;
        TextView peerNameTextView;
        TextView callTypeTextView;
        TextView startTimeTextView;
        TextView connectTimeTextView;
        TextView releaseTimeTextView;
        TextView callprorityTextView;
        TextView releaseReasonTextView;
        TextView durationTimeTextView;
        TextView outGoingTypeTextView;
        TextView outGoingTextView;
        TextView atdNameTextView;

        LinearLayout myConfLayout;
        ConfMemberListView myConfListView;
        ImageView confImageView;
    }

    /**
     * 更新搜索结果数据
     * 方法说明 :
     * @param callRecordList
     * @author chaimb
     * @Date 2015-6-18
     */

    public void updateDate(ArrayList<CallRecordListItem> callRecordList)
    {
        if (callRecordList != null && callRecordList.size() > 0)
        {
            this.callRecordListItems = callRecordList;
            screenListView.setVisibility(View.VISIBLE);
            notifyDataSetChanged();
        }
        else
        {
            screenListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Log.info(TAG, position + "");

        currentPosition = position;
        upDateItem(position);
        notifyDataSetChanged();

    }

    /**
     * 显示会议成员详情
     * 方法说明 :
     * @param position
     * @author chaimb
     * @Date 2015-6-24
     */
    private void upDateItem(int position)
    {
        int visiblePosition = screenListView.getFirstVisiblePosition();
        // 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (position - visiblePosition >= 0)
        {
            // 得到要更新的item的view
            View view = screenListView.getChildAt(position - visiblePosition);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.myConfLayout = (LinearLayout) view.findViewById(R.id.myconf_layout);
//            viewHolder.myConfLayout.setVisibility(View.VISIBLE);

            if (currentPosition == position)
            {
                if (viewHolder.myConfLayout.getVisibility() == View.GONE)
                {
                    viewHolder.myConfLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    viewHolder.myConfLayout.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.conf_imageview:
                int position = (Integer) v.getTag();
                currentPosition = position;
//                upDateItem(position);
                break;

            default:
                break;
        }

    }
}
