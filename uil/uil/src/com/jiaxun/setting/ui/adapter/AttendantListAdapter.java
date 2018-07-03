package com.jiaxun.setting.ui.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.setting.ui.fragment.AttendantFragment;
import com.jiaxun.setting.ui.fragment.AttendantlistFragment;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.AttendantListItem;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog.OnCustomClickListener;
import com.jiaxun.uil.util.DialogUtil;
import com.jiaxun.uil.util.ToastUtil;

/**
 * 说明：调度台用户adapter
 * 
 * @author fuluo
 * 
 * @Date 2015-4-10
 * 
 */
public class AttendantListAdapter extends BaseAdapter implements OnClickListener
{
    Context mContext;
    private LayoutInflater inflater;
    private ViewHolder viewHolder;
    private List<AttendantListItem> items;

    public AttendantListAdapter(List<AttendantListItem> items, Context mContext)
    {
        this.items = items;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount()
    {
        return items.size();
    }

    @Override
    public Object getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.attendant_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.loginName = (TextView) convertView.findViewById(R.id.loginName);
//            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.userRole = (TextView) convertView.findViewById(R.id.userRole);
            viewHolder.delete = (Button) convertView.findViewById(R.id.deleteButton);
            viewHolder.delete.setOnClickListener(this);
            viewHolder.modify = (Button) convertView.findViewById(R.id.modifyButton);
            viewHolder.modify.setOnClickListener(this);
            convertView.setTag(viewHolder);
        }
        AttendantListItem item = (AttendantListItem) getItem(position);
        viewHolder.attendant = item;
        viewHolder.loginName.setText(item.getLogin());
//        viewHolder.userName.setText(item.getName());
        viewHolder.userRole.setText(item.getPriority() == 1 ? "管理员" : "操作员");

        if (!UiApplication.getAtdService().isAtdAdminLogin())
        {// 非管理员帐号登录
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.modify.setVisibility(View.GONE);
        }
        else
        {
            if (item.getLogin().equals("admin"))
            {// 默认管理员不允许删除
                viewHolder.delete.setVisibility(View.GONE);
            }
            else if(UiApplication.atdName.equals(item.getLogin()))
            {
                viewHolder.delete.setVisibility(View.GONE);
            }
            else
            {
                viewHolder.delete.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.delete.setTag(item);
        viewHolder.modify.setTag(item);
        return convertView;
    }

    @Override
    public void onClick(View v)
    {
        if (viewHolder == null)
            return;

        Button button = (Button) v;
        if (button.getText().toString().equals("修改"))
        {
            AttendantFragment attendantAttenFragment = new AttendantFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(CommonConstantEntry.DATA_OBJECT, (AttendantListItem) v.getTag());
            attendantAttenFragment.setArguments(bundle);
            ((SettingActivity) mContext).getFragmentManager().beginTransaction().replace(R.id.container_setting_right, attendantAttenFragment).commit();
        }
        else if (button.getText().toString().equals("删除"))
        {
            final View view = v;
            DialogUtil.showConfirmDialog(mContext, "\t\t\t删除后,将不能使用该用户登录\t\t\t\n\t\t\t\t\t\t\t确认删除？", true, new OnCustomClickListener()
            {

                @Override
                public void onClick(CustomAlertDialog customAlertDialog)
                {
                    boolean isRemove = UiApplication.getAtdService().removeAtd(((AttendantListItem) view.getTag()).getLogin());
                    if (isRemove)
                    {
                        ToastUtil.showToast("已删除");
                        ((SettingActivity) mContext).turnToNewFragment(R.id.container_setting_right, AttendantlistFragment.class, null);
                    }
                    else
                    {
                        ToastUtil.showToast("未删除成功");
                    }
                    customAlertDialog.dismiss();
                }
            });
        }
    }

    public List<AttendantListItem> getItems()
    {
        return items;
    }

    public void setItems(List<AttendantListItem> items)
    {
        this.items = items;
    }

    class ViewHolder
    {
        /** 登陆名 */
        TextView loginName;
        /** 用户名称 */
//        TextView userName;
        /** 用户角色 */
        TextView userRole;
        /** 删除 */
        Button delete;
        /** 修改 */
        Button modify;
        /** 数据对象 */
        AttendantListItem attendant;
    }
}
