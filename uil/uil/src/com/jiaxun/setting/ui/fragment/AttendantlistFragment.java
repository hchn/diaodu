package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.Attendant;
import com.jiaxun.sdk.dcl.module.attendant.itf.DclAtdService;
import com.jiaxun.setting.ui.adapter.AttendantListAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.AttendantListItem;

/**
 * 说明：操作台用户列表
 * 
 * @author fuluo
 * 
 * @Date 2015-4-10
 * 
 */
public class AttendantlistFragment extends Fragment implements OnClickListener
{
    private ListView listview;
    private Button adduser;
    private TextView attendCountTextView;

    private List<AttendantListItem> listElements = new ArrayList<AttendantListItem>();
    private AttendantListAdapter adapter;

    private DclAtdService uilAtdService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_attendant_list, container, false);

        uilAtdService = UiApplication.getAtdService();

        for (Attendant attendant : uilAtdService.getAttendants())
        {
            AttendantListItem attendantListItem = new AttendantListItem();
            attendantListItem.setAttendant(attendant);
            listElements.add(attendantListItem);
        }
        adapter = new AttendantListAdapter(listElements, getActivity());
        listview = (ListView) view.findViewById(R.id.userList);
        listview.setAdapter(adapter);

        adduser = (Button) view.findViewById(R.id.adduser);
        attendCountTextView = (TextView) view.findViewById(R.id.attend_count_textview);
        if (UiApplication.getAtdService().isAtdAdminLogin())
        {// 管理员帐号登录
            adduser.setVisibility(View.VISIBLE);
            adduser.setOnClickListener(this);
        }
        else
        {
            adduser.setVisibility(View.GONE);
        }
        
        attendCountTextView.setText("用户数：" + listElements.size() /*+ "个"*/);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == adduser.getId())
        {
            AttendantFragment attendantAttenFragment = new AttendantFragment();
            getFragmentManager().beginTransaction().replace(R.id.container_setting_right, attendantAttenFragment).commit();
        }
    }

}
