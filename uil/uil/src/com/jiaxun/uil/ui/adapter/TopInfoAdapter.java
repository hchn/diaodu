package com.jiaxun.uil.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.TopInfoItem;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.ui.view.TopStatusPaneView;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-8-19
 */
public class TopInfoAdapter extends BaseAdapter implements OnClickListener
{

    private List<TopInfoItem> topDataItems;
    private Context context;

    LayoutInflater inflater;

    public TopInfoAdapter(Context context, List<TopInfoItem> topDataItems)
    {
        this.context = context;
        this.topDataItems = topDataItems;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return topDataItems.size() > 0 ? topDataItems.size() : 0;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_top_info_item, null);
            holder.topInfoImageView = (ImageView) convertView.findViewById(R.id.top_info_imageview);
            holder.topInfoTextView = (TextView) convertView.findViewById(R.id.top_info_textview);
            holder.exitButton = (Button) convertView.findViewById(R.id.exit_login_button);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        TopInfoItem topDataItem = topDataItems.get(position);
        holder.topInfoTextView.setText(topDataItem.getName());

        if (topDataItem.getId() == 1)
        {
            holder.exitButton.setVisibility(View.VISIBLE);
            holder.exitButton.setOnClickListener(this);
        }
        else
        {
            holder.exitButton.setVisibility(View.GONE);

        }

        return convertView;
    }

    private class ViewHolder
    {
        ImageView topInfoImageView;
        TextView topInfoTextView;
        Button exitButton;

    }

    public void addData(List<TopInfoItem> topDataItems)
    {
        this.topDataItems = topDataItems;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.exit_login_button:
                TopStatusPaneView.getInstance().hidePop();
                // 有业务进行时不允许退出登录
                if (ServiceUtils.getCurrentCallList().size() > 0 || UiApplication.getVsService().getOpenVsCount() > 0)
                {
                    ToastUtil.showToast("请先关闭当前业务，再进行操作");
                    return;
                }
                if (!UiApplication.getCurrentContext().getClass().getName().contains("HomeActivity"))
                {
                    ((SettingActivity) UiApplication.getCurrentContext()).finish();
                    Intent intent = new Intent();
                    intent.setAction(HomeActivity.ACTION_EXIT_LOGIN);
                    ((SettingActivity) UiApplication.getCurrentContext()).sendBroadcast(intent);
                }
                else
                {
                    ((HomeActivity) UiApplication.getCurrentContext()).loadAtdLoginView();
                }
                break;

            default:
                break;
        }

    }

}
