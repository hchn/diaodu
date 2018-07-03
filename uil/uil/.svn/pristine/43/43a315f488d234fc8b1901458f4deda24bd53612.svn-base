package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：快捷按键区
 *
 * @author  HeZhen
 *
 * @Date 2015-4-27
 */
public class ContactHotKeyFragment extends BaseContactGridFragment implements NotificationCenterDelegate
{
    private static final String TAG = ContactHotKeyFragment.class.getSimpleName();

    public ContactHotKeyFragment()
    {
        super();
    }

    @Override
    public void initComponentViews(View view)
    {
        super.initComponentViews(view);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.HOME_LEFTGROUP_SHOWKEYAREA);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        View emptyView = view.findViewById(R.id.iv_empty);
        groupGrid.setEmptyView(emptyView);
        groupGrid.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                try
                {
                    BaseListItem dataAdapter = dataAdapterList.get(position);
                    int dataId = dataAdapter.getId();
                    int subType = dataAdapter.getSubType();
                    if (subType == EnumGroupType.KEY)
                    {
                        GroupModel keyEntity = UiApplication.getContactService().getDepById(dataId);
                        ArrayList<ContactModel> contactList = keyEntity.getChildrenContactList();
                        if (contactList.size() == 1)
                        {
                            int contactId = contactList.get(0).getId();
                            ContactModel contactModel = UiApplication.getContactService().getContactById(contactId);
                            if (contactModel == null)
                            {
                                return;
                            }
                            ;
                            String num = contactModel.getConfNum();
                            ContactNum confItem = null;
                            ArrayList<ContactNum> contactNumList = contactModel.getPhoneNumList();
                            for (ContactNum cn : contactNumList)
                            {
                                if (num.equals(cn.getNumber()))
                                {
                                    confItem = cn;
                                    break;
                                }
                            }
                            if (confItem != null && ContactUtil.isVsByPhoneType(confItem.getTypeName()))
                            {
                                UiApplication.getVsService().addVsUser(num);
                            }
                            else
                            {
                                ServiceUtils.makeCall(parentActivity, num);
                            }

                        }
                        else
                        {
                            boolean commonCall = false;
                            ArrayList<String> numberList = new ArrayList<String>();
                            ContactNum confItem = null;
                            ArrayList<ContactNum> contactNumList = null;
                            for (ContactModel contact : contactList)
                            {
                                if(contact == null)
                                {
                                    continue;
                                }
                                String number = contact.getConfNum();
                                contactNumList = contact.getPhoneNumList();
                                for (ContactNum cn : contactNumList)
                                {
                                    if (number.equals(cn.getNumber()))
                                    {
                                        confItem = cn;
                                        break;
                                    }
                                }
                                if(!TextUtils.isEmpty(number)&&!numberList.contains(number))
                                {
                                    numberList.add(number);
                                }
                                if (!ContactUtil.isVsByPhoneType(confItem.getTypeName()))// 只要有一个不是监控用户则普通呼叫
                                {
                                    commonCall = true;
                                }
                            }
                            // 一组联系人中如果有普通用户，则发起会议，否则发起监控
                            if (commonCall)
                            {
                                ServiceUtils.makeConf(parentActivity, keyEntity.getName(), numberList);
                            }
                            else
                            {
                                UiApplication.getVsService().addVsUsers(numberList);
                            }
                        }
//                    EventNotifyHelper.getInstance().postNotificationName(EventNotifyHelper.SHOW_CONTACT_DETAIL, dataId);
                    }
                    else if (subType == EnumGroupType.KEY_GROUP)
                    {
                        initData(dataId, false);
                    }
                }
                catch (Exception e)
                {
                    initData(mGroupId, false);
                    Log.info(TAG, e.toString());
                }
            }
        });

        if (mGroupId != GroupModel.DEFAULT_PARENT_ID)
        {
            initData(mGroupId, false);
        }
        else
        {
            if (groupLevelList != null && groupLevelList.size() > 0)
            {
                mGroupId = groupLevelList.get(0);
            }
            initTopGroupTabs(mGroupId);
        }
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
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.HOME_LEFTGROUP_SHOWKEYAREA);
//        groupLevelList.clear();
//        mGroupId = GroupModel.DEFAULT_PARENT_ID;
//        groupTitle.removeAllViews();
//        dataAdapterList.clear();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_list;
    }

    public void initData(int groupIdParam, boolean removeLevelTile, Context context)
    {
        GroupModel groupModel = UiApplication.getContactService().getDepById(groupIdParam);
        if (groupModel == null)
        {
            dataAdapterList.clear();
            groupLevelList.clear();
            groupTitle.removeAllViews();
            groupTitle.invalidate();
            contactAdapter.initData(groupIdParam, dataAdapterList);
            return;
        }
        mGroupId = groupIdParam;
        dataAdapterList.clear();
        ArrayList<BaseListItem> itemList = new ArrayList<BaseListItem>();
        ArrayList<GroupModel> groupList = groupModel.getChildrenDepList();
        for (GroupModel group : groupList)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(group.getId());
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(group.getType());
            baseItem.setName(group.getName());
            baseItem.setPosition(group.getPosition());
            itemList.add(baseItem);
        }
        int size = itemList.size();
        int length = size > UiConfigEntry.KEY_PLACE_DEFAULT ? size : UiConfigEntry.KEY_PLACE_DEFAULT;
        ArrayList<BaseListItem> groupPlaces = new ArrayList<BaseListItem>();
        for (int i = 0; i < length; i++)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(EnumGroupType.PLACE);
            baseItem.setPosition(i);
            groupPlaces.add(baseItem);
        }
        for (BaseListItem baseListItem : itemList)
        {
            groupPlaces.set(baseListItem.getPosition(), baseListItem);
        }
        dataAdapterList.addAll(groupPlaces);

        Collections.sort(dataAdapterList, positionComparator);

        super.initData(groupIdParam, removeLevelTile, context);
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.REFRESH_CONTACT_VIEW)
        {
            if (UiApplication.getContactService().getDepById(mGroupId) == null)
            {
                mGroupId = GroupModel.DEFAULT_PARENT_ID;
            }
            initData(mGroupId, false);
        }
        else if (id == UiEventEntry.HOME_LEFTGROUP_SHOWKEYAREA)
        {
            GroupModel ge = (GroupModel) args[0];
            if (ge != null)
            {
                groupLevelList.clear();
                groupTitle.removeAllViews();
                dataAdapterList.clear();

                turnToPreGroup(ge.getId(), true);
            }
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.iv_switch:
                break;
        }
    }
}
