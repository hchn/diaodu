package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactAdapter;
import com.jiaxun.uil.ui.view.GroupLevelBtn;
import com.jiaxun.uil.ui.view.GroupLevelBtn.OnGroupBtnClickListener;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.enums.EnumContactEditType;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：通讯录编辑 
 *
 * @author  HeZhen
 *
 * @Date 2015-5-8
 */
public class ContactGridEditFragment extends BaseKeyContactEditFragment implements OnClickListener, NotificationCenterDelegate
{
    ArrayList<Integer> selectContactList;

    public ContactGridEditFragment()
    {
        super(false);
        selectContactList = selectDataList;
    }

    @Override
    public void initComponentViews(View view)
    {
        super.initComponentViews(view);
        parentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        contactAdapter = new ContactAdapter(parentActivity);
        groupGrid.setAdapter(contactAdapter);

        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_ADD_NEW_CONTACT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_ADD_NEW_GROUP);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_MODIFY_CONTACT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_MODIFY_GROUP);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_CONTACT_CANCEL);
        
        groupGrid.setOnItemClickListener(new OnItemClickListener()
        {
            private int groupPosition;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem dataAdapter = dataAdapterList.get(position);
                int dataId = dataAdapter.getId();
                if (editType == EnumContactEditType.SELECT)
                {
                    groupPosition = position;
                    dataAdapter.setChecked(!dataAdapter.isChecked());
                    contactAdapter.notifyDataSetChanged();
                    if (dataAdapter.isChecked())
                    {
                        addDataAdapter(dataAdapter);
                    }
                    else
                    {
                        removeDataAdapter(dataAdapter);
                    }
                    if (!isSelectAll())
                    {
                        selectAllBtn.setText(R.string.setting_contact_selectall);
                    }
                    return;
                }
                if (editType == EnumContactEditType.MODIFY)
                {

                    if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
                    {
                        showToModifyContact(mGroupId, dataAdapter.getId());
                    }
                    else
                    {
                        showGroupDetailFragment(dataAdapter.getId(), EnumGroupType.CONTACT_GROUP);
                    }

                    return;
                }
                if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
                {
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.SETTING_SHOW_CONTACT_DETAIL, dataAdapter.getId());
                }
                else
                {
                    initData(dataId, false);
                }
            }
        });
        groupLevelList = new ArrayList<Integer>();
        groupTitleInit();
        showFirstRootGroup();
        setEditType(EnumContactEditType.NORMAL);
    }

    private void addDataAdapter(BaseListItem dataAdapter)
    {
        if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
        {
            selectContactList.add(dataAdapter.getId());
        }
        else
        {
            selectGroupList.add(dataAdapter.getId());
        }
    }

    private void removeDataAdapter(BaseListItem dataAdapter)
    {
        if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
        {
            selectContactList.remove((Integer) dataAdapter.getId());
        }
        else
        {
            selectGroupList.remove((Integer) dataAdapter.getId());
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
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_ADD_NEW_CONTACT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_ADD_NEW_GROUP);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_MODIFY_CONTACT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_MODIFY_GROUP);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_CONTACT_CANCEL);
    }

    @Override
    public int getLayoutView()
    {
        return super.getLayoutView();
    }

    private void showFirstRootGroup()
    {
        mGroupId = GroupModel.DEFAULT_PARENT_ID;
        ArrayList<GroupModel> groupRootList = UiApplication.getContactService().getGroupRootList();
        firstSet(groupRootList.size() > 0, true);
        dataAdapterList.clear();

        for (GroupModel groupModel : groupRootList)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(groupModel.getId());
            baseItem.setName(groupModel.getName());
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(EnumGroupType.CONTACT_GROUP);
            baseItem.setPosition(groupModel.getPosition());
            dataAdapterList.add(baseItem);
        }
        Collections.sort(dataAdapterList, positionComparator);
        selectAllPrefsBaseItem(false);
        contactAdapter.initData(mGroupId, dataAdapterList);
        contactAdapter.notifyDataSetChanged();
    }

    private boolean isSelectAll()
    {
        return (selectGroupList.size() + selectContactList.size()) == dataAdapterList.size();
    }

    private void selectAllPrefsBaseItem(boolean selectAll)
    {
        selectGroupList.clear();
        selectContactList.clear();

        for (BaseListItem baseItem : dataAdapterList)
        {
            baseItem.setChecked(selectAll);
            if (selectAll)
            {
                if (baseItem.getType() == BaseListItem.TYPE_CONTACT)
                {
                    selectContactList.add(baseItem.getId());
                }
                else
                {
                    selectGroupList.add(baseItem.getId());
                }
            }
        }

        if (selectAll)
        {
            selectAllBtn.setText(R.string.setting_contact_selectall_cancel);
        }
        else
        {
            selectAllBtn.setText(R.string.setting_contact_selectall);
        }
        contactAdapter.notifyDataSetChanged();
    }

    public void initData(int groupIdParam, boolean removeLevelTile)
    {
        mGroupId = groupIdParam;
        if (groupIdParam == GroupModel.DEFAULT_PARENT_ID)
        {
            showFirstRootGroup();
            return;
        }
        GroupModel groupModel = UiApplication.getContactService().getDepById(groupIdParam);
        if (groupModel == null)
        {
            return;
        }
        ArrayList<GroupModel> groupRootList = UiApplication.getContactService().getGroupRootList();
        firstSet(groupRootList.size() > 0, false);

        ArrayList<GroupModel> groups = groupModel.getChildrenDepList();
        dataAdapterList.clear();
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
        ArrayList<ContactModel> contacts = groupModel.getChildrenContactList();
        for (ContactModel contact : contacts)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(contact.getId());
            baseItem.setName(contact.getName());
            baseItem.setType(BaseListItem.TYPE_CONTACT);

            ContactPosInGroup posGroup = contact.getPosGroup(groupIdParam);
            if (posGroup != null)
                baseItem.setPosition(posGroup.getPosition());
            dataAdapterList.add(baseItem);
        }
        Collections.sort(dataAdapterList, positionComparator);

        selectAllPrefsBaseItem(false);
        contactAdapter.initData(groupIdParam, dataAdapterList);

        if (removeLevelTile)
        {
            groupLevelList.clear();
            groupTitleInit();
        }
        else if (!groupLevelList.contains(groupIdParam))
        {
            groupLevelList.add(groupIdParam);
            boolean isFirst = groupTitle.getChildCount() == 0;
            GroupLevelBtn groupLevelBtn = new GroupLevelBtn(getActivity(), isFirst);
            groupTitle.addView(groupLevelBtn.initData(groupIdParam), groupLevelBtn.getLayoutParams());

            groupLevelBtn.setOnClickListener(new OnGroupBtnClickListener()
            {
                @Override
                public void onClick(View v, int groupId)
                {
                    turnToPreGroup(groupId);
                }
            });
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    hsview.scrollTo(groupTitle.getChildAt(groupTitle.getChildCount() - 1).getRight(), 0);
                }
            }, 100l);
        }
    }

    @Override
    public void turnToPreGroup(int groupIdParam)
    {
        super.turnToPreGroup(groupIdParam);
        mGroupId = groupIdParam;
        if (groupIdParam == GroupModel.DEFAULT_PARENT_ID)
        {
            showFirstRootGroup();
            groupLevelList.clear();
            groupTitleInit();
            return;
        }
        boolean start = false;
        int startPos = 1;
        ArrayList<Integer> grList = new ArrayList<Integer>();
        for (Integer groupId : groupLevelList)
        {
            if (groupId == groupIdParam)
            {
                start = true;
            }
            if (start)
            {
                grList.add(groupId);
            }
            else
            {
                startPos++;
            }

        }
        groupLevelList.removeAll(grList);
        groupTitle.removeViews(startPos, groupTitle.getChildCount() - startPos);
        initData(groupIdParam, false);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.select_btn:
                setEditType(EnumContactEditType.SELECT);
                break;
            case R.id.delete_btn:
                deleteSelectedData();
                break;
            case R.id.modify_btn:
                setEditType(EnumContactEditType.MODIFY);
                break;
            case R.id.add_group_btn:
                if (groupLevelList.size() < UiConfigEntry.GROUP_LEVEL_MAX)
                {
                    GroupModel groupModel = UiApplication.getContactService().getDepById(mGroupId);
                    int position;
                    if (groupModel == null)
                    {
                        position = UiApplication.getContactService().getGroupRootList().size();
                    }
                    else
                    {
                        position = groupModel.getChildrenContactList().size() + groupModel.getChildrenDepList().size();
                    }
                    hideContact();
                    showGroupAddFragment(true, groupModel == null ? GroupModel.DEFAULT_PARENT_ID : groupModel.getId(), EnumGroupType.CONTACT_GROUP, position);
                }
                else
                {
                    ToastUtil.showToast("最大只能建" + UiConfigEntry.GROUP_LEVEL_MAX + "层");
                }
                break;
            case R.id.add_contact_btn:
                hideContact();
                showToAddContact(mGroupId, dataAdapterList.size());
                break;
            case R.id.select_all_btn:
                selectAllPrefsBaseItem(!isSelectAll());
                break;
            case R.id.back_btn:
                ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
                if (editType == EnumContactEditType.NORMAL)
                {
                    ((SettingActivity) parentActivity).showSettingContactView();
                }
                else
                {
                    setEditType(EnumContactEditType.NORMAL);
                }
                initData(mGroupId, false);
                UiUtils.hideSoftKeyboard(getActivity());
                break;
        }
    }

    private void deleteSelectedData()
    {
        UiApplication.getContactService().removeDepAndContacts(selectGroupList, selectContactList);
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.SETTING_ADD_NEW_CONTACT || id == UiEventEntry.SETTING_ADD_NEW_GROUP || id == UiEventEntry.SETTING_MODIFY_CONTACT)
        {
            showContact();
            parentActivity.clearFragmentStack(R.id.container_setting_right);
            if (args == null || args.length == 0)
            {
                initData(mGroupId, false);
                return;
            }
            GroupModel groupModel = (GroupModel) args[0];
            if (groupModel.getType() != EnumGroupType.KEY_GROUP)
            {
                return;
            }
            if (groupModel.getParentId() == GroupModel.DEFAULT_PARENT_ID)
            {
                showFirstRootGroup();
            }
            else
            {
                initData(groupModel.getId(), false);
            }
        }
        else if(id == UiEventEntry.SETTING_CONTACT_CANCEL)
        {
            showContact();
            parentActivity.clearFragmentStack(R.id.container_setting_right);
        }
        else if (id == UiEventEntry.SETTING_MODIFY_GROUP)
        {
            showContact();
            initData(mGroupId, false);
            parentActivity.clearFragmentStack(R.id.container_setting_right);
        }
        else if (id == UiEventEntry.REFRESH_CONTACT_VIEW)
        {
//            showContact();
            initData(mGroupId, false);
        }
    }
}
