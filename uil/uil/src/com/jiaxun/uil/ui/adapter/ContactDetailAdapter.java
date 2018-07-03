package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ContactUtil;

/**
 * 说明：用户详情的适配器 号码列表
 *
 * @author   hz
 *
 * @Date 2015-3-11
 */
public class ContactDetailAdapter extends BaseAdapter
{
    private final static String TAG = ContactDetailAdapter.class.getSimpleName();
    private ViewHolder mViewHolder;
    public LayoutInflater mInflater;
    List<BaseListItem> listItems;
    public boolean isOnlyShow;
    private Context context;

    public ContactDetailAdapter(Context context)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        listItems = new ArrayList<BaseListItem>();
    }

    public void setOnlyShow(boolean isOnlyShow)
    {
        this.isOnlyShow = isOnlyShow;
    }

    public void setData(List<BaseListItem> listItems)
    {
        this.listItems.clear();
        if(listItems != null)
        {
            this.listItems.addAll(listItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {

        return listItems == null ? 0 : listItems.size();
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
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.adapter_contact_detail, null);
            mViewHolder = new ViewHolder();
            mViewHolder.userType = (TextView) convertView.findViewById(R.id.detail_user_type);
            mViewHolder.userNumber = (TextView) convertView.findViewById(R.id.detail_user_number);
            mViewHolder.phoneCall = (ImageView) convertView.findViewById(R.id.detail_call);
            convertView.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        final BaseListItem baseItem = listItems.get(position);
        if (baseItem == null)
        {
            return convertView;
        }
        mViewHolder.userType.setText(baseItem.getName());
        String num = baseItem.getContent();
        mViewHolder.userNumber.setText(num);
        mViewHolder.phoneCall.setTag(num);
        if (isOnlyShow)
        {
            mViewHolder.phoneCall.setVisibility(View.INVISIBLE);
        }
        mViewHolder.phoneCall.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.info(TAG, "click:call");
                String calleeNum = v.getTag().toString();
                if (ContactUtil.isVsByPhoneType(baseItem.getData1()))
                {
                    UiApplication.getVsService().addVsUser(calleeNum);
                }
                else
                {
                    ServiceUtils.makeCall(context, calleeNum);
                }
            }
        });
        return convertView;
    }

    class ViewHolder
    {
        TextView userType;
        TextView userNumber;
        ImageView phoneCall;
    }
}
