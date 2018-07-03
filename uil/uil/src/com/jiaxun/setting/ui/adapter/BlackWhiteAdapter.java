package com.jiaxun.setting.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.util.EnumBWType;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;

/**
 * 说明：黑白名单适配器
 *
 * @author  HeZhen
 *
 * @Date 2015-7-2
 */
public class BlackWhiteAdapter extends BaseAdapter
{
    private ArrayList<BaseListItem> blackList;
    private Context mContext;

    public BlackWhiteAdapter(Context mContext)
    {
        this.mContext = mContext;
        blackList = new ArrayList<BaseListItem>();
    }

    public void initData(ArrayList<BaseListItem> blackList)
    {
        this.blackList.clear();
        if (blackList == null || blackList.size() == 0)
        {
            return;
        }
        this.blackList.addAll(blackList);
    }

    @Override
    public int getCount()
    {
        return blackList == null ? 0 : blackList.size();
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
        ViewHolder mViewHolder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_black_list_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.text = (TextView) convertView.findViewById(R.id.tv_data);
            mViewHolder.delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        BaseListItem baseItem = blackList.get(position);
        String text = baseItem.getName();
        mViewHolder.text.setText(text);
        mViewHolder.delete.setTag(null);
        mViewHolder.delete.setTag(baseItem);
        mViewHolder.delete.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                BaseListItem baseItem = (BaseListItem) v.getTag();
                if (baseItem == null)
                {
                    return;
                }
                if (baseItem.getType() == EnumBWType.BLACK_LIST)
                {
                    UiApplication.getBlackListService().removeBlack(baseItem.getId());
                }
                else
                {
                    UiApplication.getBlackListService().removeWhite(baseItem.getId());
                }
            }
        });
        return convertView;
    }

    class ViewHolder
    {
        TextView text;
        ImageView delete;
    }
}
