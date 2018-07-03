package com.jiaxun.uil.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jiaxun.uil.R;
import com.jiaxun.uil.model.TopInfoItem;
import com.jiaxun.uil.ui.adapter.TopInfoAdapter;

/**
 * 
 * 说明：顶部提示信息展示
 *
 * @author  chaimb
 *
 * @Date 2015-8-19
 */
public class TopInfoWindow extends PopupWindow
{
    private View contentView;
    private ListView topInfoListView;
    private TopInfoAdapter topInfoAdapter;
    List<TopInfoItem> topDataList;
    private Context mContext;

    public TopInfoWindow(Context context)
    {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.popup_top_info, null);
        topInfoListView = (ListView) contentView.findViewById(R.id.top_info_listview);
        topDataList = new ArrayList<TopInfoItem>();

        topInfoAdapter = new TopInfoAdapter(mContext, topDataList);
        topInfoListView.setAdapter(topInfoAdapter);

        this.setContentView(contentView);
//        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setAnimationStyle(R.style.AnimationTopPopup);
//        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        this.setOutsideTouchable(true);
        this.setFocusable(true);
    }

    public void setData(List<TopInfoItem> topDataItems)
    {
        topInfoAdapter.addData(topDataItems);
    }

}
