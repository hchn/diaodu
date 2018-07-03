package com.jiaxun.setting.ui.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiaxun.uil.R;

/**
 * ËµÃ÷£º
 *
 * @author  HeZhen
 *
 * @Date 2015-6-17
 */
public class FileAdapter extends BaseAdapter
{
    private ArrayList<File> fileData;
    private Context mContext;
    public FileAdapter(Context mContext,ArrayList<File> fileData)
    {
        this.fileData = fileData;
        this.mContext = mContext;
    }
    @Override
    public int getCount()
    {
        return fileData != null ? fileData.size():0;
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
        if(convertView == null)
        {
            mViewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_item_file, null);
            mViewHolder.nameView = (TextView) convertView.findViewById(R.id.tv_filename);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        File file = fileData.get(position);
        Drawable drawable ;
        if (file.isDirectory())
        {
            drawable = mContext.getResources().getDrawable(R.drawable.format_folder);
        }else
        {
            drawable = mContext.getResources().getDrawable(R.drawable.format_text);
        }
        mViewHolder.nameView.setText(file.getName());
        mViewHolder.nameView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        return convertView;
    }
    class ViewHolder
    {
        TextView nameView;
    }
}
