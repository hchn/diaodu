package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.util.ContactUtil;

/**
 * 说明： 选择联系人 适配器
 *
 * @author  HeZhen
 *
 * @Date 2015-5-14
 */
public class ContactSelectAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<BaseListItem> dataAdapterList;

    public ContactSelectAdapter(Context context)
    {
        this.mContext = context;
    }

    public void initData(ArrayList<BaseListItem> dataAdapter)
    {
        this.dataAdapterList = dataAdapter;
        if (this.dataAdapterList == null)
        {
            this.dataAdapterList = new ArrayList<BaseListItem>();
        }
    }

    @Override
    public int getCount()
    {
        return dataAdapterList == null ? 0 : dataAdapterList.size();
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
    public int getItemViewType(int position)
    {
        BaseListItem dataAdapter = dataAdapterList.get(position);
        if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder mViewHolder = null;
        int type = getItemViewType(position);
        BaseListItem dataAdapter = dataAdapterList.get(position);
        if (type == 0)
        {
            if (convertView == null)
            {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_contact_grid, null);
                mViewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
                mViewHolder.checkBox = (ImageView) convertView.findViewById(R.id.check_btn);
                mViewHolder.checkBox.setTag(position);
                convertView.setTag(mViewHolder);
            }
            else
            {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            boolean isVs = false;
            boolean isDs = false;
            if (dataAdapter != null)
            {
                ContactModel contact = UiApplication.getContactService().getContactById(dataAdapter.getId());
                isVs = ContactUtil.isVsByContactType(contact.getTypeName());
                isDs = ContactUtil.isDsByContactType(contact.getTypeName());
            }
            int resId = 0;
            if(isVs)
            {
                resId = R.drawable.contact_vs_ic;
            }else if(isDs)
            {
                resId = R.drawable.contact_ds_ic;
            }else
            {
                resId = R.drawable.contact_comm_ic;
            }
            Drawable drawable = UiApplication.getInstance().getResources()
                    .getDrawable(resId);
            mViewHolder.nameTextView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);

        }
        else if (type == 1)
        {
            if (convertView == null)
            {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_group_item, null);
                mViewHolder.nameTextView = (TextView) convertView.findViewById(R.id.group_name);
                mViewHolder.checkBox = (ImageView) convertView.findViewById(R.id.check_btn);
                mViewHolder.checkBox.setTag(position);
                mViewHolder.checkBox.setVisibility(View.GONE);
                convertView.setTag(mViewHolder);
            }
            else
            {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            GroupModel group = null;
            if (dataAdapter.getType() == BaseListItem.TYPE_DEP )
            {
                group = UiApplication.getContactService().getDepById(dataAdapter.getId());
            }
            if (group != null && dataAdapter.getSubType() == EnumGroupType.KEY)
            {
                boolean moreContact = group.getChildrenContactList().size() > 1;
                Drawable drawable = UiApplication.getInstance().getResources()
                        .getDrawable(moreContact ? R.drawable.contact_moreuser_ic : R.drawable.contact_user_ic);
                mViewHolder.nameTextView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
            }

        }

        if (dataAdapter != null)
        {
            mViewHolder.nameTextView.setText(dataAdapter.getName());

            if (dataAdapter.isSelected())
            {
                mViewHolder.checkBox.setImageResource(R.drawable.btn_check_on_holo_light);
                mViewHolder.checkBox.setEnabled(false);
            }
            else
            {
                mViewHolder.checkBox.setImageResource(dataAdapter.isChecked() ? R.drawable.btn_check_on_holo_light : R.drawable.btn_check_off_holo_light);
            }
        }
        return convertView;
    }

    class ViewHolder
    {
        TextView nameTextView;
        ImageView checkBox;
    }
}