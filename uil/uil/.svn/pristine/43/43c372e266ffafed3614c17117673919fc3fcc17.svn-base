package com.jiaxun.uil.ui.widget;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.uil.R;
import com.jiaxun.uil.util.ServiceUtils;

/**
 * 说明：选择联系人电话列表 （暂留 测试）
 *
 * @author  HeZhen
 *
 * @Date 2015-4-23
 */
public class PhoneListCallDialog extends Dialog
{
    private ListView listView;
    private Button sendCall;
    public PhoneNumAdapter phoneNumAdapter;
    public ContactModel ContactModel;
    private Context mContext;

    public PhoneListCallDialog(Context context)
    {
        super(context);
        this.mContext = context;
        setContentView(R.layout.dialog_phone_list);
        listView = (ListView) this.findViewById(R.id.phone_list);
        sendCall = (Button) this.findViewById(R.id.call_btn);
        sendCall.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String calleeNum = phoneNumAdapter.getSelectPhoneNum();
                ServiceUtils.makeCall(mContext, calleeNum);
                dismiss();
            }
        });
        listView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                phoneNumAdapter.setSelectPosition(position);
                phoneNumAdapter.notifyDataSetChanged();
            }
        });
    }

    public PhoneListCallDialog initPhone(ContactModel ContactModel)
    {
        this.ContactModel = ContactModel;
        ArrayList<ContactNum> phoneList = ContactModel.getPhoneNumList();
        phoneNumAdapter = new PhoneNumAdapter(getContext(), phoneList);
        listView.setAdapter(phoneNumAdapter);
        return this;
    }

}

class PhoneNumAdapter extends BaseAdapter
{
    private Context mContext;
    ArrayList<ContactNum> phoneList;
//    public ArrayList<String> selectPhoneList;
    public int selectPosition;

    public PhoneNumAdapter(Context context, ArrayList<ContactNum> phoneList)
    {
        this.mContext = context;
        this.phoneList = phoneList;
    }

    public String getSelectPhoneNum()
    {
        ContactNum contactNum = phoneList.get(selectPosition);
        return contactNum.getNumber();
    }

    public void setSelectPosition(int selectPosition)
    {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount()
    {
        return phoneList == null ? 0 : phoneList.size();
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
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_phone_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.phoneNum = (TextView) convertView.findViewById(R.id.phone_tv);
            viewHolder.checkImg = (ImageView) convertView.findViewById(R.id.phone_cbox);
            viewHolder.checkImg.setTag(position);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ContactNum contactNum = phoneList.get(position);
        viewHolder.phoneNum.setText(contactNum.getNumber());
        viewHolder.checkImg.setImageResource(selectPosition == position ? R.drawable.checkbox_checked : R.drawable.checkbox_uncheck);
        return convertView;
    }

    class ViewHolder
    {
        TextView phoneNum;
        ImageView checkImg;
    }
}
