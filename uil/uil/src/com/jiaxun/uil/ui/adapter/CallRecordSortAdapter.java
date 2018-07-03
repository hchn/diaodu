package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.CallRecordListItem;

public class CallRecordSortAdapter extends BaseAdapter implements OnItemClickListener
{

    private List<BaseListItem> list;

    private Context context;

    private LayoutInflater inflater;

    private ListView searchListView;

    private static final String TAG = CallRecordSortAdapter.class.getName();

    public CallRecordSortAdapter(Context context, ListView searchListView, ArrayList<BaseListItem> list)
    {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.searchListView = searchListView;
        this.searchListView.setOnItemClickListener(this);
    }

    @Override
    public int getCount()
    {
        if (list != null && list.size() > 0)
        {
            return list.size();
        }
        else
        {
            return 0;
        }
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
        ViewHolder holder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.adapter_search_call_record_list_item, null);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.user_imageview);
            holder.nameTextView = (TextView) convertView.findViewById(R.id.name_textview);
            holder.numTextView = (TextView) convertView.findViewById(R.id.num_textview);
            holder.callImageView = (ImageView) convertView.findViewById(R.id.call_imageview);

            holder.contactRelativelayout = (RelativeLayout) convertView.findViewById(R.id.contact_relativelayout);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.callImageView.setVisibility(View.GONE);
        BaseListItem baseItem = list.get(position);
        if (baseItem != null && baseItem.getType() == BaseListItem.TYPE_CONTACT)
        {
            ContactModel contactModel = UiApplication.getContactService().getContactById(baseItem.getId());

            if (contactModel != null)
            {
                String name = contactModel.getName();
                String number = "";
                if(contactModel.getPhoneNumList().size() > 0)
                {
                    number = contactModel.getPhoneNumList().get(0).getNumber();                    
                }
                if (TextUtils.isEmpty(name))
                {
                    holder.nameTextView.setText(number);
                }
                else
                {
                    holder.nameTextView.setText(name);

                }
                holder.numTextView.setText(number);

                int type = 0;//contactModel.getType();//hzcontact
                if (type == 0)
                {// 普通用户
                    holder.userImageView.setImageResource(R.drawable.user_icon);
                }
                else if (type == 1)
                {// 监控用户
                    holder.userImageView.setImageResource(R.drawable.vs_icon);
                }
                else
                {// 默认其他类型按普通用户显示
                    holder.userImageView.setImageResource(R.drawable.user_icon);
                }

            }
            else
            {
                Log.info(TAG, "contactModel is null");
            }

        }
        else if (list.get(position) instanceof CallRecordListItem)
        {
            CallRecordListItem callRecordListItem = (CallRecordListItem) list.get(position);
            if (callRecordListItem != null)
            {
                String name = callRecordListItem.getCallRecord().getPeerName();
                String number = callRecordListItem.getCallRecord().getPeerNum();
                if (TextUtils.isEmpty(name))
                {

                    holder.nameTextView.setText(number);
                }
                else
                {
                    holder.nameTextView.setText(name);

                }
                holder.numTextView.setText(number);

                int callType = callRecordListItem.getCallRecord().getCallType();
                if (callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
                { // 监控通话记录
                    holder.userImageView.setImageResource(R.drawable.vs_icon);
                }
                else
                {// 普通用户通话记录
                    holder.userImageView.setImageResource(R.drawable.user_icon);
                }

            }
        }

//        if (position == list.size() - 1 && position != 0)
//        {
//            holder.contactRelativelayout.setBackground(context.getResources().getDrawable(R.drawable.search_item_contact_background_down));
//        }
        return convertView;
    }

    class ViewHolder
    {
        ImageView userImageView;
        TextView nameTextView;
        TextView numTextView;
        ImageView callImageView;

        RelativeLayout contactRelativelayout;

    }

    public void updateListView(List<BaseListItem> list)
    {
        this.list = list;
        notifyDataSetChanged();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
    {
//        if (list.get(position) instanceof ContactModel)
//        {
//            ContactModel ContactModel = (ContactModel) list.get(position);
//            ServiceUtils.makeCall(ContactModel.getPhoneNumList().get(0).getNumber());
//        }
//        if (list.get(position) instanceof CallRecordListItem)
//        {
//            CallRecordListItem callRecordListItem = (CallRecordListItem) list.get(position);
//            ServiceUtils.makeCall(callRecordListItem.getCallRecord().getPeerNum());
//        }
    }

}
