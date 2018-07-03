package com.jiaxun.uil.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiaxun.uil.R;
import com.jiaxun.uil.model.AdditionalFuncItem;

/**
 * 说明：补充业务功能元素适配器
 *
 * @author  HeZhen
 *
 * @Date 2015-6-8
 */
public class AdditionalFuncAdapter extends BaseAdapter
{
    private Context mContext;
    private List<AdditionalFuncItem> dataSrc;
    public AdditionalFuncAdapter(Context context,List<AdditionalFuncItem> dataSrc)
    {
        this.mContext = context;
        this.dataSrc = dataSrc;
    }
    @Override
    public int getCount()
    {
        return dataSrc != null ? dataSrc.size() : 0;
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
        ViewHolder mViewHolder = null;
        if (convertView == null)
        {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_func_more_item, null);
            mViewHolder.nameTv = (TextView) convertView.findViewById(R.id.tv_tab);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        AdditionalFuncItem funcDataItem = dataSrc.get(position);
        mViewHolder.nameTv.setText(funcDataItem.getName());
        Drawable drawable = mContext.getResources().getDrawable(funcDataItem.isChecked()?funcDataItem.getIconResSelect():funcDataItem.getIconResNormal());
        mViewHolder.nameTv.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        mViewHolder.nameTv.setTextColor(funcDataItem.isChecked()?Color.parseColor("#54BCDA"):Color.parseColor("#000000"));
        return convertView;
    }
    class ViewHolder
    {
        TextView nameTv;
    }
}
