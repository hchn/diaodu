package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.uil.R;
import com.jiaxun.uil.model.BaseListItem;

/**
 * 说明：主界面左面的布局的适配器
 *
 * @author   
 *
 * @Date 2015-3-5
 */
public class LeftTabAdapter extends BaseAdapter
{
    public LayoutInflater mInflater;
    private ViewHolder mViewHolder;
    private List<BaseListItem> mDataSource;
    private Context context;

    public LeftTabAdapter(Context context, List<BaseListItem> dataSrc)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDataSource = dataSrc;
        if (mDataSource == null)
        {
            mDataSource = new ArrayList<BaseListItem>();
        }
    }

    class ViewHolder
    {
        TextView formname;
        ImageView delete;
    }

    @Override
    public int getCount()
    {

        return mDataSource == null ? 0 : mDataSource.size();
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
        if (convertView != null)
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = mInflater.inflate(R.layout.adapter_left_pane_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.formname = (TextView) convertView.findViewById(R.id.tv_left_group_name);
            mViewHolder.delete = (ImageView)convertView.findViewById(R.id.iv_delete);
            convertView.setTag(mViewHolder);
        }
        BaseListItem group = mDataSource.get(position);
//        boolean isContactEdit = UiApplication.getInstance().isContactEdit;
//        mViewHolder.delete.setVisibility(isContactEdit?View.VISIBLE:View.GONE);
        mViewHolder.formname.setText(group.getName());

        if (group.isSelected())
        {
            convertView.setBackgroundResource(R.drawable.contact_left_btn_pressed); 
        }
        else
        {
            convertView.setBackgroundResource(0);
        }

        return convertView;
    }

}
