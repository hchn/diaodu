package com.jiaxun.setting.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.uil.R;
import com.jiaxun.uil.model.CallRecordListItem;

/**
 * ËµÃ÷£º
 *
 * @author  HeZhen
 *
 * @Date 2015-7-6
 */
public class CallRecordSelectAdapter extends BaseAdapter
{
    private ArrayList<CallRecordListItem> recordList;
    private Context mContext;

    public CallRecordSelectAdapter(Context mContext, ArrayList<CallRecordListItem> recordList)
    {
        this.mContext = mContext;
        this.recordList = recordList;
    }

    @Override
    public int getCount()
    {
        return recordList == null ? 0 : recordList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return recordList == null ?null : recordList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder mViewHolder = null;
        if (convertView == null)
        {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_call_record_select_item, null);
            mViewHolder.text = (TextView) convertView.findViewById(R.id.tv_record);
            mViewHolder.check = (ImageView) convertView.findViewById(R.id.iv_check);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        CallRecordListItem record = recordList.get(position);
        String text = "("+record.getCallRecord().getPeerName() +")"+ record.getCallRecord().getPeerNum();
        mViewHolder.text.setText(text);
        mViewHolder.check.setImageResource(record.isChecked()?R.drawable.checkbox_checked:R.drawable.checkbox_uncheck);
        return convertView;
    }
    class ViewHolder
    {
        TextView text;
        ImageView check;
    }
}
