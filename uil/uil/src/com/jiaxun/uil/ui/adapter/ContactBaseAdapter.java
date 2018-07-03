package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.sdk.util.DensityUtil;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.enums.EnumContactEditType;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-6-10
 */
public class ContactBaseAdapter extends BaseAdapter
{
    private Context mContext;
    private EnumContactEditType editType = EnumContactEditType.NORMAL;
    boolean showContactStatus = false;
    protected ArrayList<BaseListItem> dataAdapterList;
    protected int mHidePosition = -1;
    protected int mGroupId;
    private int nameWidth = 0;

//    protected GroupModel groupModel;
    public ContactBaseAdapter(Context context)
    {
        this.mContext = context;
        if (this.dataAdapterList == null)
        {
            this.dataAdapterList = new ArrayList<BaseListItem>();
        }
    }

    public void showContactStatus(boolean showContactStatus)
    {
        this.showContactStatus = showContactStatus;
    }

    /**
     * 方法说明 :
     * @param type 0,正常 ；1，选择状态 ；2，修改状态
     * @author HeZhen
     * @Date 2015-5-8
     */
    public void setEditType(EnumContactEditType type)
    {
        editType = type;
    }

    public void initData(int groupIdParam, ArrayList<BaseListItem> dataAdapter)
    {
        mGroupId = groupIdParam;
        if (dataAdapter != null)
        {
            dataAdapterList.clear();
            dataAdapterList.addAll(dataAdapter);
        }
        notifyDataSetChanged();
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
        else if (dataAdapter.getType() == BaseListItem.TYPE_DEP)
        {
            if (dataAdapter.getSubType() == EnumGroupType.KEY)
            {
                return 0;
            }
            else if (dataAdapter.getSubType() == EnumGroupType.PLACE)
            {
                return 2;
            }
            return 1;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder mViewHolder = null;
        int type = getItemViewType(position);
        BaseListItem dataAdapter = dataAdapterList.get(position);
        Log.info("", "getView");
        if (type == 0)
        {
            if (convertView == null)
            {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_contact_grid, null);
                mViewHolder.statusView = convertView.findViewById(R.id.relay_status);
                mViewHolder.nameTextView = (TextView) convertView.findViewById(R.id.tv_name);
                mViewHolder.imageIco = (ImageView) convertView.findViewById(R.id.iv_status);
                mViewHolder.checkBox = (ImageView) convertView.findViewById(R.id.check_btn);
                mViewHolder.checkBox.setTag(position);
                convertView.setTag(mViewHolder);
            }
            else
            {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            Drawable drawable = null;
            if (dataAdapter != null && dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
            {
                ContactModel contact = UiApplication.getContactService().getContactById(dataAdapter.getId());
                boolean isVs = false;
                boolean isDs = false;
                if (contact != null)
                {
                     isVs = ContactUtil.isVsByContactType(contact.getTypeName());
                     isDs = ContactUtil.isDsByContactType(contact.getTypeName());
                }
                if (showContactStatus && contact != null)
                {
                    int status = contact.getStatus();
                    int callStatus = contact.getCallStatus();
                    mViewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_default);
                    mViewHolder.imageIco.setVisibility(UiApplication.isCallServerOnline && contact.getSubScribe() == 1 ? View.VISIBLE : View.GONE);

                    if (UiApplication.isCallServerOnline)
                    {
                        switch (status)
                        {
                            case CommonConstantEntry.USER_STATUS_ONLINE:
                                mViewHolder.imageIco.setImageResource(R.drawable.ic_online);
                                break;
                            case CommonConstantEntry.USER_STATUS_OFFLINE:
                                mViewHolder.imageIco.setImageResource(R.drawable.ic_outline);
                                break;
                        }
                        switch (callStatus)
                        {
                            case CommonConstantEntry.SCALL_STATE_RINGBACK:
                                mViewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_caller);
                                break;
                            case CommonConstantEntry.SCALL_STATE_CONNECT:
                                mViewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_calling);
                                break;
                            case CommonConstantEntry.SCALL_STATE_RINGING:
                                mViewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_callee);
                                break;
                            default:
                                mViewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_default);
                        }
                    }
                    else
                    {
                        mViewHolder.statusView.setBackgroundResource(R.drawable.ic_bg_default);
                    }
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
                drawable = UiApplication.getInstance().getResources()
                        .getDrawable(resId);
            }

            if (dataAdapter != null && dataAdapter.getSubType() == EnumGroupType.KEY)
            {
                GroupModel keyEntity = UiApplication.getContactService().getDepById(dataAdapter.getId());
                if (keyEntity != null)
                {
                    boolean moreContact = keyEntity.getChildrenContactList().size() > 1;
                    drawable = UiApplication.getInstance().getResources()
                            .getDrawable(moreContact ? R.drawable.contact_moreuser_ic : R.drawable.contact_user_ic);
                }
            }
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
                convertView.setTag(mViewHolder);
            }
            else
            {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
        }
        else if (type == 2)
        {
            if (convertView == null)
            {
                mViewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_group_item, null);
                mViewHolder.nameTextView = (TextView) convertView.findViewById(R.id.group_name);
                mViewHolder.checkBox = (ImageView) convertView.findViewById(R.id.check_btn);
                mViewHolder.nameTextView.setVisibility(View.INVISIBLE);
                mViewHolder.checkBox.setVisibility(View.INVISIBLE);
                mViewHolder.statusView = convertView.findViewById(R.id.relay_bg);
                mViewHolder.statusView.setBackgroundResource(R.drawable.no_key_btn_bg);
                convertView.setTag(mViewHolder);
            }
            else
            {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }
        if (position == mHidePosition)
        {
            convertView.setVisibility(View.INVISIBLE);
        }
        else
        {
            convertView.setVisibility(View.VISIBLE);
        }
        if (nameWidth <= 0)
        {
            if(Build.VERSION.SDK_INT < UiConfigEntry.SDK_LOW_LEVEL)
            {
                nameWidth = DensityUtil.dip2px(mContext, 50);
            }else{
                final View view = convertView;
                ViewTreeObserver vto = view.getViewTreeObserver();   
                vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
                    @Override  
                    public void onGlobalLayout() { 
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this); 
                        nameWidth = view.getMeasuredWidth() - DensityUtil.dip2px(mContext, 14);
                        notifyDataSetChanged();
                    }   
                });
            }
        }
        if (dataAdapter != null && mViewHolder != null)
        {
            String name = dataAdapter.getName();
            if (!TextUtils.isEmpty(name))
            {
                String ellipSipSizeStr = (String) TextUtils.ellipsize(name, (TextPaint) mViewHolder.nameTextView.getPaint(), nameWidth, TruncateAt.END);
                mViewHolder.nameTextView.setText(ellipSipSizeStr);
            }
            if (editType == EnumContactEditType.SELECT)
            {
                mViewHolder.checkBox.setVisibility(View.VISIBLE);
                mViewHolder.checkBox.setImageResource(dataAdapter.isChecked() ? R.drawable.btn_check_on_holo_light : R.drawable.btn_check_off_holo_light);
            }
            else
            {
                mViewHolder.checkBox.setVisibility(View.GONE);
            }
        }

        return convertView;
    }

    class ViewHolder
    {
        View statusView;
        TextView nameTextView;
        ImageView imageIco;
        ImageView checkBox;
    }
}
