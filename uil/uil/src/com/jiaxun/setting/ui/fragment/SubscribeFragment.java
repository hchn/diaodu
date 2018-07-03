package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactBaseAdapter;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.fragment.ContactSelectFragment;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.enums.EnumContactEditType;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：联系人订阅 配置界面
 *
 * @author  HeZhen
 *
 * @Date 2015-5-29
 */
public class SubscribeFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{
    private static final String TAG = SubscribeFragment.class.getSimpleName();
    public Button backBtn;
    public TextView deleteBtn;
    public TextView selectBtn;
    public TextView modifyBtn;
    public TextView addSubscribe;
    public TextView selectAllBtn;
    public GridView groupGrid;
    public View editPopup;
    public View coverView;
    private ContactBaseAdapter contactAdapter;
    public EnumContactEditType editType;
    ArrayList<BaseListItem> dataAdapterList;
    Map<Integer, BaseListItem> dataAdapterMap;
    ArrayList<BaseListItem> selectContactList;
    ArrayList<Integer> selectedContactList;

    public SubscribeFragment()
    {
        editType = EnumContactEditType.NORMAL;
        dataAdapterList = new ArrayList<BaseListItem>();
        dataAdapterMap = new HashMap<Integer, BaseListItem>();
        selectContactList = new ArrayList<BaseListItem>();
        selectedContactList = new ArrayList<Integer>();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_subscribe_edit_list;
    }

    @Override
    public void initComponentViews(View view)
    {
        ((SettingActivity) parentActivity).initViewSize(1, 1);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_SUBSCRIBE_SELECTE_CONTACT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_CONTACT_CANCEL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_MODIFY_CONTACT);

        backBtn = (Button) view.findViewById(R.id.back_btn);
        deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
        selectBtn = (TextView) view.findViewById(R.id.select_btn);
        modifyBtn = (TextView) view.findViewById(R.id.modify_btn);
        addSubscribe = (TextView) view.findViewById(R.id.add_contact_btn);
        selectAllBtn = (TextView) view.findViewById(R.id.select_all_btn);
        backBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        modifyBtn.setOnClickListener(this);
        addSubscribe.setOnClickListener(this);
        selectAllBtn.setOnClickListener(this);
        addSubscribe.setText(R.string.setting_add_subscribe);

        editPopup = view.findViewById(R.id.contact_edit);
        coverView = view.findViewById(R.id.cover_view);
        groupGrid = (GridView) view.findViewById(R.id.group_grid);
        contactAdapter = new ContactBaseAdapter(getActivity());
        groupGrid.setAdapter(contactAdapter);
        initDataAdapter();

        editPopup.setVisibility(View.VISIBLE);
        setEditType(EnumContactEditType.NORMAL);
        groupGrid.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem baseItem = dataAdapterList.get(position);
                if (editType == EnumContactEditType.SELECT)
                {
                    baseItem.setChecked(!baseItem.isChecked());
                    contactAdapter.notifyDataSetChanged();
                    if (baseItem.isChecked())
                    {
                        selectContactList.add(baseItem);
                    }
                    else if (selectContactList.contains(baseItem))
                    {
                        selectContactList.remove(baseItem);
                    }
                    updateSelectAllBtn(isSelectAll());
                    return;
                }
            }
        });
    }
    private void initDataAdapter()
    {
        selectContactList.clear();
        selectedContactList.clear();
        dataAdapterList.clear();
        dataAdapterMap.clear();
        ArrayList<ContactModel> subscribeContacts = UiApplication.getContactService().getSubscribeContactList();
        for (ContactModel contact : subscribeContacts)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(contact.getId());
            baseItem.setName(contact.getName());
            baseItem.setType(BaseListItem.TYPE_CONTACT);
            dataAdapterList.add(baseItem);
            dataAdapterMap.put(contact.getId(), baseItem);
        }
        contactAdapter.initData(GroupModel.DEFAULT_PARENT_ID, dataAdapterList);
        contactAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        release();
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_SUBSCRIBE_SELECTE_CONTACT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_CONTACT_CANCEL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_MODIFY_CONTACT);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.select_btn:
                if(dataAdapterList.size() > 0)
                {
                    setEditType(EnumContactEditType.SELECT);
                }
                break;
            case R.id.delete_btn:
                if (deleSubscribeMem(selectContactList))
                {
                    initDataAdapter();
                }
                break;
            case R.id.back_btn:
                if (editType == EnumContactEditType.NORMAL)
                {
                    ((SettingActivity) parentActivity).showSettingContactView();
                }
                else
                {
                    setEditType(EnumContactEditType.NORMAL);
                }
                break;
            case R.id.add_contact_btn:
                selectedContactList.clear();
                for (BaseListItem baseListItem : dataAdapterList)
                {
                    selectedContactList.add((Integer) baseListItem.getId());
                }
                hideContact();
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.SETTING_SUBSCRIBE_SELECTE_CONTACT);
                bundle.putIntegerArrayList("selectedContactList", selectedContactList);
                bundle.putInt("showType", 3);// 只选调度用户
                ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, ContactSelectFragment.class, bundle);
                break;
            case R.id.select_all_btn:
                selectAllPrefsBaseItem(!isSelectAll());
                break;
        }
    }

    /**
     * 方法说明 :
     * @param addOrDele true add;false delete
     * @param selectContactList
     * @author HeZhen
     * @Date 2015-6-11
     */
    private void setSubscribeMemDB(boolean addOrDele, ArrayList<Integer> selectContactList)
    {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        for (Integer contactId : selectContactList)
        {
            ContactModel contact = UiApplication.getContactService().getContactById(contactId).clone();
            contact.setSubScribe(addOrDele ? 1 : 0);
            contactList.add(contact);
        }
        UiApplication.getContactService().modifyContacts(contactList);
    }

    protected void setEditType(EnumContactEditType type)
    {
        editType = type;
        contactAdapter.setEditType(type);
        contactAdapter.notifyDataSetChanged();

        deleteBtn.setVisibility(View.GONE);
        selectBtn.setVisibility(View.GONE);
        modifyBtn.setVisibility(View.GONE);
        addSubscribe.setVisibility(View.GONE);
        selectAllBtn.setVisibility(View.GONE);
        backBtn.setText(R.string.back);
        if(!UiApplication.getAtdService().isAtdAdminLogin())
        {
            return;
        }
        if (editType == EnumContactEditType.NORMAL)
        {
            selectBtn.setVisibility(View.VISIBLE);
            addSubscribe.setVisibility(View.VISIBLE);
            backBtn.setText(R.string.setting_contact_close);
        }
        else if (editType == EnumContactEditType.SELECT)
        {
            deleteBtn.setVisibility(View.VISIBLE);
            selectAllBtn.setVisibility(View.VISIBLE);
            updateSelectAllBtn(false);
        }
    }

    protected void showContact()
    {
        coverView.setVisibility(View.GONE);
    }

    protected void hideContact()
    {
        coverView.setVisibility(View.VISIBLE);
    }

    private boolean deleSubscribeMem(ArrayList<BaseListItem> selectContactList)
    {
        if (selectContactList == null || selectContactList.size() == 0)
        {
            return false;
        }
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<Integer> contactList = new ArrayList<Integer>();
        for (BaseListItem baseItem : selectContactList)
        {
            int contactId = baseItem.getId();
            ContactModel ContactModel = UiApplication.getContactService().getContactById(contactId);
            // 订阅 号码 为 调度号码
            String phoneNum = ContactModel.getPhoneNumList().get(0).getNumber();
            if (!TextUtils.isEmpty(phoneNum))
            {
                users.add(phoneNum);
            }
            contactList.add(contactId);
        }
        String[] usersArray = users.toArray(new String[users.size()]);
        int result = UiApplication.getPresenceService().presenceSubscribe(usersArray, true);
        if (result == CommonConstantEntry.RESPONSE_SUCCESS)
        {
            setSubscribeMemDB(false, contactList);
            dataAdapterList.removeAll(selectContactList);
            return true;
        }
        return false;
    }

    private boolean addSubscribeMem(ArrayList<Integer> selectContactList)
    {
        Log.info(TAG, "addSubscribeMem:selectContactList = "+selectContactList);
        if (selectContactList == null || selectContactList.size() == 0)
        {
            return false;
        }
        ArrayList<String> users = new ArrayList<String>();
        ArrayList<BaseListItem> selectContacts = new ArrayList<BaseListItem>();
        for (Integer contactId : selectContactList)
        {
            ContactModel contactModel = UiApplication.getContactService().getContactById(contactId);
            // 订阅 号码 为 用户号码//第一个为调度号码
            String phoneNum = contactModel.getPhoneNumList().get(0).getNumber();
            if (!TextUtils.isEmpty(phoneNum))
            {
                users.add(phoneNum);
            }
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(contactId);
            baseItem.setName(contactModel.getName());
            baseItem.setType(BaseListItem.TYPE_CONTACT);
            selectContacts.add(baseItem);
        }
        String[] usersArray = users.toArray(new String[users.size()]);
        int result = UiApplication.getPresenceService().presenceSubscribe(usersArray, true);
        if (result == CommonConstantEntry.RESPONSE_SUCCESS)
        {
            setSubscribeMemDB(true, selectContactList);
            dataAdapterList.addAll(selectContacts);
            return true;
        }
        return false;
    }

    private boolean isSelectAll()
    {
        return selectContactList.size() == dataAdapterList.size();
    }

    private void selectAllPrefsBaseItem(boolean selectAll)
    {
        selectContactList.clear();
        for (BaseListItem baseItem : dataAdapterList)
        {
            baseItem.setChecked(selectAll);
            if (selectAll)
            {
                selectContactList.add(baseItem);
            }
        }
        updateSelectAllBtn(selectAll);
        contactAdapter.notifyDataSetChanged();
    }
    /**
     * 方法说明 :更新全选按钮
     * @param selectAll
     * @author hz
     * @Date 2015-10-9
     */
    private void updateSelectAllBtn(boolean selectAll)
    {
        if (selectAll)
        {
            selectAllBtn.setText(R.string.setting_contact_selectall_cancel);
        }
        else
        {
            selectAllBtn.setText(R.string.setting_contact_selectall);
        }
    }
    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.SETTING_SUBSCRIBE_SELECTE_CONTACT)
        {
            int operaCode = (Integer)args[0];
            if(operaCode == 0)//取消操作
            {
                showContact();
                parentActivity.backToPreFragment(R.id.container_setting_right);
            }else
            {
                ArrayList<Integer> selectContactList = (ArrayList<Integer>) args[1];
                if (addSubscribeMem(selectContactList))
                {
                    showContact();
//                    initDataAdapter();
                    ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
                } 
            }
        }
        else if (id == UiEventEntry.SETTING_CONTACT_CANCEL)
        {
            showContact();
        }
        else if (id == UiEventEntry.SETTING_MODIFY_CONTACT)
        {
//            showContact();
            initDataAdapter();
//            ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
        
        }
    }
}
