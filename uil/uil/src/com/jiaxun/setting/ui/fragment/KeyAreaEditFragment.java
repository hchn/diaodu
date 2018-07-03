package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactKeyAdapter;
import com.jiaxun.uil.ui.view.GroupLevelBtn;
import com.jiaxun.uil.ui.view.GroupLevelBtn.OnGroupBtnClickListener;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.enums.EnumContactEditType;
import com.jiaxun.uil.util.enums.EnumKeyEditType;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：按键区 设置
 *
 * @author  HeZhen
 *
 * @Date 2015-5-13
 */
public class KeyAreaEditFragment extends BaseKeyContactEditFragment implements OnClickListener, NotificationCenterDelegate
{
    ArrayList<Integer> selectKeyList;
    // 空白 位置 key :positio
//    Map<Integer, BaseListItem> placeAdapterMap;
    // 空白位置列表
    ArrayList<BaseListItem> placeAdapterList;

    int allSize = 0;
    public KeyAreaEditFragment()
    {
        super(true);
        selectKeyList = selectDataList;
//        placeAdapterMap = new HashMap<Integer, BaseListItem>();
        placeAdapterList = new ArrayList<BaseListItem>();
    }

    @Override
    public void initComponentViews(View view)
    {
        super.initComponentViews(view);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_ADD_NEW_GROUP);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_CONTACT_CANCEL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_KEY_ADDED);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_KEY_REMOVE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_KEY_EDIT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_MODIFY_GROUP);

        contactAdapter = new ContactKeyAdapter(parentActivity);
        groupGrid.setAdapter(contactAdapter);
        ArrayList<GroupModel> keyGroupRootList = UiApplication.getContactService().getKeyGroupRootList();
        mGroupId = GroupModel.DEFAULT_PARENT_ID;
        firstSet(keyGroupRootList.size() > 0, true);

        groupGrid.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem dataAdapter = dataAdapterList.get(position);
                int dataId = dataAdapter.getId();
                if (editType == EnumContactEditType.SELECT)
                {
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
                int subType = dataAdapter.getSubType();
                if (editType == EnumContactEditType.MODIFY)
                {
                    if (subType == EnumGroupType.KEY_GROUP)
                    {
                        showGroupDetailFragment(dataAdapter.getId(), EnumGroupType.KEY_GROUP);

                    }
                    else if (subType == EnumGroupType.KEY)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putInt("keyId", dataAdapter.getId());
                        bundle.putInt("type", EnumKeyEditType.KEY_MODIFY);
                        ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
                        ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, KeyEditFragment.class, bundle);
                    }
                    return;
                }
                if (subType == EnumGroupType.KEY_GROUP)
                {
                    initData(dataId, false);
                }
                else if (subType == EnumGroupType.KEY)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", EnumKeyEditType.KEY_DETAIL);
                    bundle.putInt("keyId", dataAdapter.getId());
                    ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
                    ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, KeyEditFragment.class, bundle);
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
        int subType = dataAdapter.getSubType();
        if (subType == EnumGroupType.KEY_GROUP)
        {
            selectGroupList.add(dataAdapter.getId());
        }
        else if(subType == EnumGroupType.KEY)
        {
            selectKeyList.add(dataAdapter.getId());
        }
    }

    private void removeDataAdapter(BaseListItem dataAdapter)
    {
        int subType = dataAdapter.getSubType();
        Integer dataId = dataAdapter.getId();
        if (subType == EnumGroupType.KEY_GROUP)
        {
            if (selectGroupList != null && selectGroupList.contains(dataId))
            {
                selectGroupList.remove(dataId);
            }
        }
        else if (subType == EnumGroupType.KEY)
        {
            if (selectKeyList != null && selectKeyList.contains(dataId))
            {
                selectKeyList.remove(dataId);
            }
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
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_CONTACT_CANCEL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_ADD_NEW_GROUP);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_KEY_ADDED);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_KEY_REMOVE);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_KEY_EDIT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_MODIFY_GROUP);
    }

    private void showFirstRootGroup()
    {
        ArrayList<GroupModel> groups = UiApplication.getContactService().getKeyGroupRootList();
        firstSet(groups.size() > 0, mGroupId == GroupModel.DEFAULT_PARENT_ID);
        dataAdapterList.clear();
        allSize = groups.size();
        for (GroupModel group : groups)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(group.getId());
            baseItem.setName(group.getName());
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(EnumGroupType.KEY_GROUP);
            dataAdapterList.add(baseItem);
        }
        selectAllPrefsBaseItem(false);
        contactAdapter.initData(mGroupId, dataAdapterList);
    }

    private boolean isSelectAll()
    {
        return (selectGroupList.size() + selectKeyList.size()) == allSize;
    }

    private void selectAllPrefsBaseItem(boolean selectAll)
    {
        selectGroupList.clear();
        selectKeyList.clear();

        for (BaseListItem prefsBaseItem : dataAdapterList)
        {
            prefsBaseItem.setChecked(selectAll);
            if (selectAll)
            {
                int subType = prefsBaseItem.getSubType();
                if (subType == EnumGroupType.KEY_GROUP)
                {
                    selectGroupList.add(prefsBaseItem.getId());
                }
                else if(subType == EnumGroupType.KEY)
                {
                    selectKeyList.add(prefsBaseItem.getId());
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
        mGroupId = groupIdParam;
        ArrayList<GroupModel> keyGroupRootList = UiApplication.getContactService().getKeyGroupRootList();
        firstSet(keyGroupRootList.size() > 0, false);
        dataAdapterList.clear();
        placeAdapterList.clear();
        ArrayList<BaseListItem> itemList = new ArrayList<BaseListItem>();
        ArrayList<GroupModel> groupList = groupModel.getChildrenDepList();
        allSize = groupList.size();
        int maxPos = 0;
        for (GroupModel group : groupList)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(group.getId());
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(group.getType());
            baseItem.setName(group.getName());
            baseItem.setPosition(group.getPosition());
            itemList.add(baseItem);
            if (maxPos < group.getPosition())
            {
                maxPos = group.getPosition();
            }
        }
        maxPos++;
        int placesSize = maxPos > UiConfigEntry.KEY_PLACE_DEFAULT ? maxPos : UiConfigEntry.KEY_PLACE_DEFAULT;
//        ArrayList<BaseListItem> groupPlaces = new ArrayList<BaseListItem>();
        // 初始化所有的位置
        for (int i = 0; i < placesSize; i++)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(EnumGroupType.PLACE);
            baseItem.setPosition(i);
            dataAdapterList.add(baseItem);
            placeAdapterList.add(baseItem);
        }
        // 从placeAdapterList移除到已占用的位置。剩下的是空白位置
        for (BaseListItem baseListItem : itemList)
        {
            int position = baseListItem.getPosition();
            if (dataAdapterList.size() > position)
            {
                placeAdapterList.remove(dataAdapterList.get(position));
            }
        }
        // 将占用的位置替换到 总位置列表相应位置。
        for (BaseListItem baseListItem : itemList)
        {
            int position = baseListItem.getPosition();
            dataAdapterList.set(position, baseListItem);
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
                    hsview.scrollTo(groupTitle.getChildAt(groupTitle.getChildCount()-1).getRight(), 0);
                }
            }, 100l);
        }
    }

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
            if (groupId == mGroupId)
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
                    hideContact();
                    showGroupAddFragment(true, mGroupId, EnumGroupType.KEY_GROUP, getNewPlacePostion());
                }
                else
                {
                    ToastUtil.showToast("最大只能建" + UiConfigEntry.GROUP_LEVEL_MAX + "层");
                }
                break;
            case R.id.select_contact_btn:
                hideContact();
                Bundle bundle = new Bundle();
                bundle.putInt("type", EnumKeyEditType.KEY_ADD);
                bundle.putInt("keyId", mGroupId);
                bundle.putInt("position", getNewPlacePostion());
                ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
                ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, KeyEditFragment.class, bundle);
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
                break;
        }
    }

    private void deleteSelectedData()
    {
        ArrayList<GroupModel> selectData = new ArrayList<GroupModel>();
        ArrayList<Integer> allData = new ArrayList<Integer>();
        allData.addAll(selectKeyList);
        allData.addAll(selectGroupList);
        for (Integer groupId : allData)
        {
            GroupModel groupModel = UiApplication.getContactService().getDepById(groupId);
            selectData.add(groupModel);
        }
        UiApplication.getContactService().removeDepAndKeys(selectData);
    }

    public int getLayoutView()
    {
        return R.layout.fragment_key_edit_list;
    }

    // 下个空位的坐标
    private int getNewPlacePostion()
    {
        if (placeAdapterList.size() > 0)
        {
            return placeAdapterList.get(0).getPosition();
        }
        else
        {
            return dataAdapterList.size();
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        ArrayList<GroupModel> keyGroupRootList = UiApplication.getContactService().getKeyGroupRootList();
        firstSet(keyGroupRootList.size() > 0, mGroupId == GroupModel.DEFAULT_PARENT_ID);
        if (id == UiEventEntry.SETTING_ADD_NEW_GROUP)
        {
            showContact();
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
            if (groupModel.getParent() == null)
            {
                showFirstRootGroup();
            }
            else
            {
                initData(groupModel.getId(), false);
            }
        }
        else if (id == UiEventEntry.SETTING_KEY_ADDED||id == UiEventEntry.SETTING_KEY_REMOVE )
        {
            showContact();
            initData(mGroupId, false);
            contactAdapter.notifyDataSetChanged();
            parentActivity.clearFragmentStack(R.id.container_setting_right);
        }
        else if (id == UiEventEntry.SETTING_CONTACT_CANCEL)
        {
            showContact();
            parentActivity.clearFragmentStack(R.id.container_setting_right);
        }
        else if (id == UiEventEntry.SETTING_KEY_EDIT || id == UiEventEntry.SETTING_MODIFY_GROUP)
        {
            showContact();
            initData(mGroupId, false);
            ((SettingActivity) parentActivity).clearFragmentStack(R.id.container_setting_right);
        } 
    }
}
