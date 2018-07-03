package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.jiaxun.sdk.dcl.model.BlackWhiteModel;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.util.EnumBWType;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.ui.adapter.BlackWhiteAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.fragment.CallRecordSelectedFragment;
import com.jiaxun.uil.ui.fragment.ContactSelectFragment;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：黑白名单界面
 *
 * @author  HeZhen
 *
 * @Date 2015-7-1
 */
public class BlackWhiteListFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{
    private Button blackBtn = null;
    private Button whiteBtn = null;
    private Button manualBtn = null;
    private Button contactBtn = null;
    private Button recodeBtn = null;
    private ListView dataList = null;
    private BlackWhiteAdapter blackAdapter;
    private ArrayList<BaseListItem> dataAdapter = new ArrayList<BaseListItem>();
    /**
     * 0黑 1白
     */
    private int type = EnumBWType.BLACK_LIST;

    public BlackWhiteListFragment()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.BLACK_WHITE_ADD_CONTACT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.ADD_BLACK_WHITE_DATA);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.BLACK_WHITE_DELETE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.BLACK_WHITE_ADD_RECORD);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.MANUAINPUT_OVER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_blackwhite;
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.BLACK_WHITE_ADD_CONTACT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.ADD_BLACK_WHITE_DATA);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.BLACK_WHITE_DELETE);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.BLACK_WHITE_ADD_RECORD);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.MANUAINPUT_OVER);
    }

    @Override
    public void initComponentViews(View view)
    {
        blackBtn = (Button) view.findViewById(R.id.btn_black);
        whiteBtn = (Button) view.findViewById(R.id.btn_white);
        manualBtn = (Button) view.findViewById(R.id.btn_manual);
        contactBtn = (Button) view.findViewById(R.id.btn_contact);
        recodeBtn = (Button) view.findViewById(R.id.btn_record);
        dataList = (ListView) view.findViewById(R.id.lv_phone_list);

        blackBtn.setOnClickListener(this);
        whiteBtn.setOnClickListener(this);
        manualBtn.setOnClickListener(this);
        contactBtn.setOnClickListener(this);
        recodeBtn.setOnClickListener(this);

        blackAdapter = new BlackWhiteAdapter(parentActivity);
        dataList.setAdapter(blackAdapter);
        blackAdapter.notifyDataSetChanged();

        View emptyView = view.findViewById(R.id.iv_empty);
        dataList.setEmptyView(emptyView);
        initData(type);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_black:
                initData(EnumBWType.BLACK_LIST);
                break;
            case R.id.btn_white:
                initData(EnumBWType.WHITE_LIST);
                break;
            case R.id.btn_manual:
                showManualView();
                break;
            case R.id.btn_contact:
                selectContact();
                break;
            case R.id.btn_record:
                selectRecord();
                break;
        }
    }

    private void showManualView()
    {
        parentActivity.turnToNewFragment(R.id.container_setting_right, ManualInputFragment.class, true, null);
    }

    private void selectContact()
    {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.BLACK_WHITE_ADD_CONTACT);
        parentActivity.turnToNewFragment(R.id.container_setting_right, ContactSelectFragment.class, true, bundle);
    }

    private void selectRecord()
    {
        parentActivity.turnToNewFragment(R.id.container_setting_right, CallRecordSelectedFragment.class, true, null);
    }

    private void initData(int paramType)
    {
        type = paramType;
        boolean black = (type == EnumBWType.BLACK_LIST);
        blackBtn.setBackgroundResource(black ? R.drawable.shape_btn1 : R.drawable.shape_btn2);
        whiteBtn.setBackgroundResource(black ? R.drawable.shape_btn2 : R.drawable.shape_btn1);

        dataAdapter.clear();
        ArrayList<BlackWhiteModel> blackList = UiApplication.getBlackListService().getBWList(black);
        String name;
        for (BlackWhiteModel blackEntity : blackList)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(blackEntity.getId());
            name = "";
            ContactModel contact = UiApplication.getContactService().getContactById(blackEntity.getContactId());
            if (contact != null)
            {
                name = "(" + contact.getName() + ")";
            }
            name += blackEntity.getPhoneNum();
            baseItem.setName(name);
            baseItem.setType(blackEntity.getType());
            dataAdapter.add(baseItem);
        }
        blackAdapter.initData(dataAdapter);
        blackAdapter.notifyDataSetChanged();
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.BLACK_WHITE_ADD_CONTACT)
        {
            parentActivity.backToPreFragment(R.id.container_setting_right);
            int operaCode = (Integer) args[0];
            if(operaCode == 0)
            {
            }else
            {
                ArrayList<Integer> selectContactList = (ArrayList<Integer>) args[1];
                if (selectContactList == null || selectContactList.size() == 0)
                {
                    return;
                }
                ArrayList<BlackWhiteModel> blackList = new ArrayList<BlackWhiteModel>();
                for (Integer contactId : selectContactList)
                {
                    BlackWhiteModel blackWhite = new BlackWhiteModel();
                    blackWhite.setContactId(contactId);
                    ContactModel contact = UiApplication.getContactService().getContactById(contactId);
                    if (contact != null)
                    {
                        blackWhite.setPhoneNum(contact.getPhoneNumList().get(0).getNumber());
                    }
                    blackWhite.setType(type);
                    blackList.add(blackWhite);
                }
                UiApplication.getBlackListService().addNumToBWList(blackList);  
            }
        }
        else if (id == UiEventEntry.ADD_BLACK_WHITE_DATA || id == UiEventEntry.BLACK_WHITE_DELETE)
        {
            initData(type);
        }
        else if (id == UiEventEntry.BLACK_WHITE_ADD_RECORD)
        {
            ArrayList<CallRecordListItem> selectRecordList = (ArrayList<CallRecordListItem>) args[0];
            if (selectRecordList == null || selectRecordList.size() == 0)
            {
                return;
            }
            ArrayList<BlackWhiteModel> blackList = new ArrayList<BlackWhiteModel>();
            for (CallRecordListItem record : selectRecordList)
            {
                BlackWhiteModel blackWhite = new BlackWhiteModel();
                blackWhite.setType(type);
                blackWhite.setPhoneNum(record.getCallRecord().getPeerNum());
                blackList.add(blackWhite);
            }
            UiApplication.getBlackListService().addNumToBWList(blackList);
        }
        else if (id == UiEventEntry.MANUAINPUT_OVER)
        {
            String num = (String) args[0];
            BlackWhiteModel blackWhite = new BlackWhiteModel();
            blackWhite.setPhoneNum(num);
            blackWhite.setType(type);
            UiApplication.getBlackListService().addNumToBWList(blackWhite);
        }
    }
}
