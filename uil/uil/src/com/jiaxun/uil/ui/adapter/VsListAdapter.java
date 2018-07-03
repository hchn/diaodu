package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.VsListItem;
import com.jiaxun.uil.ui.fragment.VsListFragment;
import com.jiaxun.uil.util.enums.EnumVsEditType;

/**
 * 说明：视频监控列表适配器
 *
 * @author  zhangxd
 *
 * @Date 2015-5-29
 */
public class VsListAdapter extends BaseAdapter
{
    private static final String TAG = VsListAdapter.class.getName();
    public List<VsListItem> mListItems;
    private ViewHolder viewHolder;
    private EnumVsEditType editType = EnumVsEditType.NORMAL;
    protected LayoutInflater inflater;

    protected int whichSelected = 0;

    private Context context;
    private GridView gridView;

    private VsListFragment parentContainer;

    public VsListAdapter(Context context, VsListFragment parentContainer, ArrayList<VsListItem> listItems, GridView gridView)
    {

        this.context = context;
        this.mListItems = listItems;
        this.parentContainer = parentContainer;
        parentContainer.setHandler(mHandler);
        this.gridView = gridView;
        this.inflater = LayoutInflater.from(this.context);

        setListItem(listItems, 0);

    }

    public void setEditType(EnumVsEditType type)
    {
        editType = type;
    }

    public EnumVsEditType getEditType()
    {
        return editType;
    }

    @Override
    public int getCount()
    {
        if (mListItems == null)
        {
            return 0;
        }
        else
        {
            return mListItems.size();
        }
    }

    public int getSelectedPosition()
    {
        return whichSelected;
    }

    public VsListItem getSelectedItem()
    {
        if (mListItems != null && whichSelected >= 0 && whichSelected < mListItems.size())
        {
            return mListItems.get(whichSelected);
        }
        return null;
    }

    @Override
    public Object getItem(int position)
    {
        if (mListItems != null && position >= 0 && position < mListItems.size())
        {
            return mListItems.get(position);
        }
        return new BaseListItem();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Log.info(TAG, "getView position:" + position);
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.adapter_vs_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.statusView = convertView.findViewById(R.id.relay_status);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
            viewHolder.iv_status = (ImageView) convertView.findViewById(R.id.iv_status);
            convertView.setTag(viewHolder);
        }
        VsListItem vsListItem = mListItems.get(position);
        viewHolder.userName.setText(vsListItem.getUserName());

        // 如果是通话中
        if (vsListItem.isOpened())
        {
            viewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_calling);
        }
        else
        {
            viewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_default);
        }
        // 正在打开监控
        if (vsListItem.isOpening())
        {
            viewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_caller);
        }
        // 在不在线
        switch (vsListItem.getStatus())
        {
            case CommonConstantEntry.USER_STATUS_ONLINE:
                viewHolder.iv_status.setBackgroundResource(R.drawable.ic_online);
                break;
            case CommonConstantEntry.USER_STATUS_OFFLINE:
                viewHolder.iv_status.setBackgroundResource(R.drawable.ic_outline);
                break;
            default:
                break;
        }

        // 如果是普通状态
        if (editType == EnumVsEditType.NORMAL)
        {
            viewHolder.iv_selected.setVisibility(View.GONE);
        }
        else
        {
            // 如果是选中删除用户状态
            viewHolder.iv_selected.setVisibility(View.VISIBLE);
            // 如果item被选中
            if (vsListItem.isSelected())
            {
                viewHolder.iv_selected.setBackgroundResource(R.drawable.btn_check_on_holo_light);;
            }
            else
            {
                viewHolder.iv_selected.setBackgroundResource(R.drawable.btn_check_off_holo_light);;
            }
        }
        return convertView;
    }

    @SuppressWarnings("unchecked")
    public void setListItem(List<? extends VsListItem> listItems)
    {
        if (listItems != null)
        {
            mListItems = (List<VsListItem>) listItems;
            notifyDataSetChanged();
        }
        else
        {
            Log.info(TAG, "setListItem listItems == null!");
        }
    }

    public void setListItem(List<? extends BaseListItem> listItems, int selectedPosition)
    {
        try
        {
            if (listItems != null)
            {
                mListItems = (List<VsListItem>) listItems;
                if (mListItems != null && mListItems.size() > 0)
                {
                    whichSelected = selectedPosition;
                }
                notifyDataSetChanged();
            }
            else
            {
                Log.info(TAG, "setListItem listItems == null!");
            }
        }
        catch (Exception e)
        {
            Log.error(TAG, e.toString());
        }
    }

    /**
      * 方法说明 :只更新某个item
      * @param itemIndex
      * @author zhangxd
      * @Date 2015-6-19
      */
    public void updateView(VsListItem vsListItem)
    {
        if (vsListItem != null)
        {
            int start = gridView.getFirstVisiblePosition();
            for (int i = start, j = gridView.getLastVisiblePosition(); i <= j; i++)
            {
                if (vsListItem == gridView.getItemAtPosition(i))
                {
                    View view = gridView.getChildAt(i - start);
                    getView(i, view, gridView);
                    break;
                }
            }
        }

    }

    class ViewHolder
    {
        TextView userName;   //用户名
        ImageView iv_status;//在线状态
        ImageView iv_selected;//是否选中
        View statusView;;
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case VsListFragment.UPDATE_LIST_ITEM:
                    // 更新列表单条记录状态
                    updateView((VsListItem) msg.obj);
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
