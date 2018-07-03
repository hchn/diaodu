package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jiaxun.uil.R;
import com.jiaxun.uil.model.RightTabItem;

/**
 * 说明：主界面右办部分的适配器
 *
 * @author  wangxue
 *
 * @Date 2015-3-5
 */
public class RightTabAdapter extends BaseAdapter
{
    public LayoutInflater mInflater;
    private ViewHolder mViewHolder;
    private List<RightTabItem> mDataSource = new ArrayList<RightTabItem>();
    private Context mContext;

    public RightTabAdapter(Context context, List<RightTabItem> DataSrc)
    {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataSource = DataSrc;
    }

    @Override
    public int getCount()
    {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    @Override
    public Object getItem(int position)
    {
        if (mDataSource != null && position >= 0 && position < mDataSource.size())
        {
            return mDataSource.get(position);
        }
        return new RightTabItem();
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView != null)
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = mInflater.inflate(R.layout.adapter_right_pane_item, null, false);
            mViewHolder = new ViewHolder();
            mViewHolder.tab = (ImageView) convertView.findViewById(R.id.right_tab);
            convertView.setTag(mViewHolder);
        }
        RightTabItem rightTabItem = (RightTabItem)getItem(position);
//        mViewHolder.tab.setText(mDataSource.get(position).getName());
//        Log.info("getView", "position:" + position);
        mViewHolder.tab.setImageResource(rightTabItem.getIconRes());
        if (rightTabItem.isSelected())
        {
            mViewHolder.tab.setBackgroundResource(R.drawable.func_right_select);
            mViewHolder.tab.setImageResource(rightTabItem.getSelectIcon());
        }
        else
        {
            mViewHolder.tab.setBackgroundResource(0);
            mViewHolder.tab.setImageResource(rightTabItem.getIconRes());
        }
        mViewHolder.tab.setOnClickListener(rightTabItem.getClickListener());
        return convertView;
    }

    class ViewHolder
    {
        ImageView tab;
    }
}
