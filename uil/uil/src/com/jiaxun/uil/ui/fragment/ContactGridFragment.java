package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.util.SearchContacts;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：通讯录网格 样式
 *
 * @author  HeZhen
 *
 * @Date 2015-4-27
 */
public class ContactGridFragment extends BaseContactGridFragment implements View.OnClickListener, NotificationCenterDelegate
{
    private static final String TAG = ContactGridFragment.class.getName();
    private SearchContacts searchContact;
    
    private boolean isSearch = false;

    public ContactGridFragment()
    {
        super();
        searchContact = new SearchContacts();
    }

    @Override
    public void initComponentViews(View view)
    {
        super.initComponentViews(view);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.HOME_LEFTGROUP_SHOWCONTACT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);

        contactAdapter.showContactStatus(true);
        searchEditT.setVisibility(View.VISIBLE);
        searchEditT.setOnClickListener(this);
        searchEditT.setFilters(new InputFilter[] { new InputFilter.LengthFilter(UiConfigEntry.CONTACT_NAME_MAX) });
        switchBtn.setVisibility(View.VISIBLE);
        switchBtn.setOnClickListener(this);
        editClearBtn.setOnClickListener(this);
        View emptyView = view.findViewById(R.id.iv_empty);
        groupGrid.setEmptyView(emptyView);
        groupGrid.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem dataAdapter = dataAdapterList.get(position);
                int dataId = dataAdapter.getId();
                if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_SHOW_CONTACT_DETAIL, dataId);
                }
                else
                {
                    initData(dataId, false);
                }
            }
        });
        searchEditT.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    String key = searchEditT.getText().toString();
                    if (!TextUtils.isEmpty(key))
                    {
                        ArrayList<ContactModel> contactEntities = searchContact.onContactSearch(key);
                        searchAdapter(contactEntities);
                        editClearBtn.setVisibility(View.VISIBLE);
                    }
                }
                return false;
            }
        });
        searchEditT.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void afterTextChanged(Editable s)
            {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String key = s.toString();
                if (TextUtils.isEmpty(key))
                {// 清空
                    searchContact.clear();
                    initData(mGroupId, false);
                    editClearBtn.setVisibility(View.GONE);
                    isSearch = false;
                }
                else
                {
                    ArrayList<ContactModel> contactEntities = searchContact.onContactSearch(key);
                    searchAdapter(contactEntities);
                    editClearBtn.setVisibility(View.VISIBLE);
                    isSearch = true;
                }
            }
        });
        searchEditT.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String str = searchEditT.getText().toString();
                if(!TextUtils.isEmpty(str))
                {
                    ArrayList<ContactModel> contactEntities = searchContact.onContactSearch(str);
                    searchAdapter(contactEntities);
                    editClearBtn.setVisibility(View.VISIBLE);
                    isSearch = true;
                }
            }
        });
        if( mGroupId != GroupModel.DEFAULT_PARENT_ID)
        {
            initData(mGroupId, false);
        }else
        {
            if (groupLevelList != null && groupLevelList.size() > 0)
            {
                mGroupId = groupLevelList.get(0);
            }
            initTopGroupTabs(mGroupId);
        }
      
    }

    @Override
    public void onResume()
    {
        super.onResume();
        searchEditT.clearFocus();
    }

    @Override
    public void onDestroy()
    {
        Log.info(TAG, "onDestroy::");
        super.onDestroy();
        release();
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.HOME_LEFTGROUP_SHOWCONTACT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
//        groupLevelList.clear();
//        mGroupId = GroupModel.defaultParentId;
//        groupTitle.removeAllViews();
//        dataAdapterList.clear();
    }
    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_list;
    }

    public void searchAdapter(ArrayList<ContactModel> contactList)
    {
        dataAdapterList.clear();
        if (contactList == null)
        {
            return;
        }
        for (ContactModel contact : contactList)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setType(BaseListItem.TYPE_CONTACT);
            baseItem.setId(contact.getId());
            baseItem.setName(contact.getName());
            dataAdapterList.add(baseItem);
        }
        contactAdapter.initData(mGroupId, dataAdapterList);
        contactAdapter.notifyDataSetChanged();
    }

    public void initData(int groupIdParam, boolean removeLevelTile, Context context)
    {
        if (groupIdParam == GroupModel.DEFAULT_PARENT_ID)
        {
            dataAdapterList.clear();
            groupLevelList.clear();
            groupTitle.removeAllViews();
            groupTitle.invalidate();
            contactAdapter.initData(groupIdParam, dataAdapterList);
            return;
        }
        dataAdapterList.clear();
        this.mGroupId = groupIdParam;
        GroupModel groupModel = UiApplication.getContactService().getDepById(groupIdParam);
        if(groupModel == null)
        {
            return;
        }
        ArrayList<GroupModel> groups = groupModel.getChildrenDepList();
        for (GroupModel group : groups)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(group.getId());
            baseItem.setName(group.getName());
            baseItem.setPosition(group.getPosition());
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(group.getType());
            dataAdapterList.add(baseItem);
        }
        int groupId = groupModel.getId();
        ArrayList<ContactModel> contacts = groupModel.getChildrenContactList();
        for (ContactModel contact : contacts)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(contact.getId());
            baseItem.setName(contact.getName());
            baseItem.setType(BaseListItem.TYPE_CONTACT);
            ContactPosInGroup posGroup = contact.getPosGroup(groupId);
            if (posGroup != null)
            {
                baseItem.setPosition(posGroup.getPosition());
            }
            dataAdapterList.add(baseItem);
        }
        Collections.sort(dataAdapterList, positionComparator);
        searchContact.setContactAll(UiApplication.getContactService().getContactList());

        super.initData(groupIdParam, removeLevelTile, context);
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.HOME_LEFTGROUP_SHOWCONTACT)
        {
            GroupModel ge = (GroupModel) args[0];
            if (ge != null)
            {
                initTopGroupTabs(ge.getId());
            }
        }
        else if (id == UiEventEntry.REFRESH_CONTACT_VIEW)
        {
            if(isSearch)//如果当前正在搜索则不刷新
            {
                return;
            }
            if (UiApplication.getContactService().getDepById(mGroupId) == null)
            {
                mGroupId = GroupModel.DEFAULT_PARENT_ID;
            }
            initData(mGroupId, false);
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.iv_switch:
                ((HomeActivity) parentActivity).loadContactView(true);
                break;
            case R.id.iv_edit_clear:
                searchEditT.setText("");
                isSearch = false;
                break;
        }
    }
}
