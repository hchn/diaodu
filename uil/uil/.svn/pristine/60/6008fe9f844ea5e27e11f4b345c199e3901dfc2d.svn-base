package com.jiaxun.setting.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.uil.R;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-6-17
 */
public class CallRecordScreenConfAdapter extends BaseAdapter
{

    private String TAG = CallRecordScreenConfAdapter.class.getName();
    private Context context;
    private ArrayList<CallRecord> callRecords;
    private LayoutInflater inflater;

    public CallRecordScreenConfAdapter()
    {
        super();
    }

    public CallRecordScreenConfAdapter(Context context, ArrayList<CallRecord> callRecordList)
    {
        super();
        this.context = context;
        this.callRecords = callRecordList;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount()
    {
        if (callRecords != null && callRecords.size() > 0)
        {
            return callRecords.size();
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
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.include_screen_item, null);
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

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CallRecord callRecord = callRecords.get(position);

        holder.callerNumTextView.setGravity(Gravity.CENTER);

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
                holder.outGoingTypeTextView.setText("未拨电话");
            }
            else
            {
                holder.outGoingTypeTextView.setText("已拨电话");
            }

        }
        holder.releaseTimeTextView.setText(DateUtils.formatStartTime(callRecord.getReleaseTime()));

        //会议成员
//            int callType = callRecord.getCallType();
//            if(callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
//            {
        holder.callTypeTextView.setText("会议成员");
//            }
//            else
//            {
//                holder.callTypeTextView.setText("单呼");
//            }
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
    }

    /**
     * 更新搜索结果数据
     * 方法说明 :
     * @param callRecordList
     * @author chaimb
     * @Date 2015-6-18
     */

    public void updateDate(ArrayList<CallRecord> callRecordList)
    {
//        if (this.callRecordListItems != null && this.callRecordListItems.size() > 0)
//        {
//            this.callRecordListItems.clear();
//        }
        this.callRecords = callRecordList;
        notifyDataSetChanged();
    }
}
