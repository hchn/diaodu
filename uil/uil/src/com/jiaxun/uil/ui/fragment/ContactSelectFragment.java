package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactSelectAdapter;
import com.jiaxun.uil.ui.view.GroupLevelBtn;
import com.jiaxun.uil.ui.view.SelectedContactView;
import com.jiaxun.uil.ui.view.GroupLevelBtn.OnGroupBtnClickListener;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：选择联系人界面
 *
 * @author  HeZhen
 *
 * @Date 2015-4-27
 */
public class ContactSelectFragment extends BaseFragment implements View.OnClickListener
{
    private static final String TAG = ContactSelectFragment.class.getName();
    private int mGroupId = GroupModel.DEFAULT_PARENT_ID;
    private ContactSelectAdapter contactAdapter;
    ArrayList<Integer> groupLevelList;
    ArrayList<BaseListItem> dataAdapterList;
    Map<Integer, BaseListItem> dataAdapterMap;
    /**选择的成员*/
    ArrayList<Integer> selectContactList;
    /**原始成员*/
    ArrayList<Integer> selectedContactList;

    private LinearLayout groupTitle;
    private LinearLayout contactSelectLayout;
    private GridView groupGrid;
    private TextView cancelBtn;
    private TextView sureBtn;
    private TextView selectAgainBtn;
    public HorizontalScrollView hsview;
    private int callBackCode;
    
    /**
     * 0：显示全部， 1：只显示组，2：监控用户 ，3：调度用户
     */
    private int showType = 0;

    public ContactSelectFragment()
    {
        dataAdapterList = new ArrayList<BaseListItem>();
        dataAdapterMap = new HashMap<Integer, BaseListItem>();
        selectContactList = new ArrayList<Integer>();
        selectedContactList = new ArrayList<Integer>();
        groupLevelList = new ArrayList<Integer>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        selectContactList.clear();
        selectedContactList.clear();
        mGroupId = -1;
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            callBackCode = bundle.getInt(CommonConstantEntry.DATA_TYPE);
            showType = bundle.getInt("showType",0);
            selectedContactList = bundle.getIntegerArrayList("selectedContactList");
            int gridColumns = bundle.getInt("gridColumns", 6);
            groupGrid.setNumColumns(gridColumns);
        }
        if (selectedContactList == null)
        {
            selectedContactList = new ArrayList<Integer>();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initComponentViews(View view)
    {
        groupTitle = (LinearLayout) view.findViewById(R.id.groups_level_title);
        contactSelectLayout = (LinearLayout) view.findViewById(R.id.linelay_select_contact);
        cancelBtn = (TextView) view.findViewById(R.id.cancel_btn);
        sureBtn = (TextView) view.findViewById(R.id.sure_btn);
        selectAgainBtn = (TextView) view.findViewById(R.id.select_again_btn);
        hsview = (HorizontalScrollView) view.findViewById(R.id.hscroll_grouplevel);
        
        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        selectAgainBtn.setOnClickListener(this);
        groupGrid = (GridView) view.findViewById(R.id.group_grid);

        contactAdapter = new ContactSelectAdapter(parentActivity);
        groupGrid.setAdapter(contactAdapter);
        groupGrid.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem dataAdapter = dataAdapterList.get(position);
                if (dataAdapter.getType() == BaseListItem.TYPE_CONTACT)
                {

                    if (dataAdapter.isSelected())
                    {
                        return;
                    }
                    dataAdapter.setChecked(!dataAdapter.isChecked());
                    contactAdapter.notifyDataSetChanged();
                    if (dataAdapter.isChecked())
                    {
                        selectContactList.add(dataAdapter.getId());
                    }
                    else
                    {
                        selectContactList.remove((Integer) dataAdapter.getId());
                    }
                    updateSelectContactLayout();
                }
                else
                {
                    if (dataAdapter.getSubType() == EnumGroupType.KEY)
                    {

                    }
                    else
                    {
                        initData(dataAdapter.getId(), false);
                    }
                }

            }
        });
        showRootGroup();
    }

    private void updateSelectContactLayout()
    {
        contactSelectLayout.removeAllViews();
        for (Integer contactId : selectContactList)
        {
            ContactModel contact = UiApplication.getContactService().getContactById(contactId);
            SelectedContactView textView = new SelectedContactView(parentActivity);
            textView.setTag(contactId);
            textView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Integer contactId = (Integer) v.getTag();
                    BaseListItem baseItem = dataAdapterMap.get(contactId);
                    if (baseItem != null)
                    {
                        baseItem.setChecked(false);
                    }
                    selectContactList.remove(contactId);
                    updateSelectContactLayout();
                    contactAdapter.notifyDataSetChanged();
                }
            });
            textView.setText(contact.getName());
            contactSelectLayout.addView(textView);
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_select;
    }

    /**
     * 方法说明 :显示根组
     * @author HeZhen
     * @Date 2015-5-30
     */
    private void showRootGroup()
    {
        groupLevelList.clear();
        dataAdapterList.clear();
        dataAdapterMap.clear();
        groupTitle.removeAllViews();
        GroupLevelBtn groupLevelBtn = new GroupLevelBtn(getActivity(), true);
        groupLevelBtn.setText("首页");
        groupTitle.addView(groupLevelBtn.initData(GroupModel.DEFAULT_PARENT_ID));
        groupLevelBtn.setOnClickListener(new OnGroupBtnClickListener()
        {
            @Override
            public void onClick(View v, int groupId)
            {
                turnToPreGroup(groupId);
            }
        });
        ArrayList<GroupModel> groupRootList = UiApplication.getContactService().getGroupRootList();
        sortGroupDataAdapter(groupRootList);
        contactAdapter.initData(dataAdapterList);
        contactAdapter.notifyDataSetChanged();
    }

    /**
     * 方法说明 :取消选择
     * @author HeZhen
     * @Date 2015-5-14
     */
    private void cancelSelectPrefsBaseItem()
    {
        for (Integer contactId : selectContactList)
        {
            BaseListItem baseItem = dataAdapterMap.get(contactId);
            if (baseItem != null)
            {
                baseItem.setChecked(false);
            }
        }
        selectContactList.clear();
        updateSelectContactLayout();
    }

    /**
     * 方法说明 :选择完成，将选择联系人传出
     * @author HeZhen
     * @Date 2015-5-30
     */
    private void sureToNext()
    {
        if(callBackCode != 0)
        {
            if(showType == 1)
            {
                EventNotifyHelper.getInstance().postNotificationName(callBackCode,1,mGroupId);
            }else
            {
                EventNotifyHelper.getInstance().postNotificationName(callBackCode,1,selectContactList.clone());
            }
        }
        cancelSelectPrefsBaseItem();
    }

    public void initData(int groupIdParam, boolean removeLevelTile)
    {
        mGroupId = groupIdParam;
        if (groupIdParam == GroupModel.DEFAULT_PARENT_ID)
        {
            showRootGroup();
            return;
        }
        dataAdapterList.clear();
        dataAdapterMap.clear();
        GroupModel groupModel = UiApplication.getContactService().getDepById(groupIdParam);
        if (groupModel == null)
        {
            return;
        }
        sortGroupDataAdapter(groupModel.getChildrenDepList());
        sortContactDataAdapter(groupModel.getChildrenContactList());

        contactAdapter.initData(dataAdapterList);
        contactAdapter.notifyDataSetChanged();

        if (removeLevelTile)
        {
            groupLevelList.clear();
        }
        else if (!groupLevelList.contains(groupIdParam))
        {
            groupLevelList.add(groupIdParam);
            boolean isFirst = groupTitle.getChildCount() == 0;
            GroupLevelBtn groupLevelBtn = new GroupLevelBtn(parentActivity, isFirst);
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

    private void sortGroupDataAdapter(ArrayList<GroupModel> GroupModels)
    {
        for (GroupModel group : GroupModels)
        {
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(group.getId());
            baseItem.setName(group.getName());
            baseItem.setType(BaseListItem.TYPE_DEP);
            baseItem.setSubType(EnumGroupType.CONTACT_GROUP);
            dataAdapterList.add(baseItem);
            dataAdapterMap.put(baseItem.getId(), baseItem);
        }
    }

    private void sortContactDataAdapter(ArrayList<ContactModel> ContactModels)
    {
        if(showType == 1)
        {
            return;
        }
        for (ContactModel contactModel : ContactModels)
        {
            if(showType == 2 && !ContactUtil.isVsByContactType(contactModel.getTypeName()))
            {
                continue;
            }else if(showType == 3 && !ContactUtil.isDsByContactType(contactModel.getTypeName()))
            {
                continue;
            }
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(contactModel.getId());
            baseItem.setName(contactModel.getName());
            baseItem.setType(BaseListItem.TYPE_CONTACT);
            dataAdapterList.add(baseItem);
            dataAdapterMap.put(baseItem.getId(), baseItem);
            if (selectContactList.contains(baseItem.getId()))
            {
                baseItem.setChecked(true);
            }
            else
            {
                baseItem.setChecked(false);
            }
            if (selectedContactList.contains(baseItem.getId()))
            {
                baseItem.setSelected(true);
            }
            else
            {
                baseItem.setSelected(false);
            }
        }

    }

    public void turnToPreGroup(int groupIdParam)
    {
        mGroupId = groupIdParam;

        if (groupIdParam == GroupModel.DEFAULT_PARENT_ID)
        {
            showRootGroup();
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
    public void onClick(final View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.cancel_btn:
                EventNotifyHelper.getInstance().postNotificationName(callBackCode,0,null);//0，cancel操作
                break;
            case R.id.sure_btn:
                v.setClickable(false);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        v.setClickable(true);
                    }
                }, 1000l);
                sureToNext();
                break;
            case R.id.select_again_btn:
                cancelSelectPrefsBaseItem();
                showRootGroup();
                break;
        }
    }
}
