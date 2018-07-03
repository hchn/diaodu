package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactBaseAdapter;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.view.GroupLevelBtn;
import com.jiaxun.uil.ui.view.GroupLevelBtn.OnGroupBtnClickListener;
import com.jiaxun.uil.util.enums.EnumContactEditType;

/**
 * 说明：ContactGridEditFragment KeyAreaEditFragment 父类
 *
 * @author  HeZhen
 *
 * @Date 2015-5-28
 */
public class BaseKeyContactEditFragment extends BaseFragment implements OnClickListener
{
    public Button backBtn;
    public TextView deleteBtn;
    public TextView selectBtn;
    public TextView modifyBtn;
    public TextView addGroupBtn;
    public TextView addContactBtn;
    public TextView selectContactBtn;
    public TextView selectAllBtn;

    public LinearLayout groupTitle;
    public GridView groupGrid;
    public View editPopup;
    public View coverView;

    public HorizontalScrollView hsview;

    public ContactBaseAdapter contactAdapter;

    public EnumContactEditType editType = EnumContactEditType.NORMAL;

    ArrayList<Integer> groupLevelList;
    ArrayList<BaseListItem> dataAdapterList;

    ArrayList<Integer> selectGroupList;
    ArrayList<Integer> selectDataList; // 联系人 或 按键（组）

    protected int mGroupId = GroupModel.DEFAULT_PARENT_ID;
    protected GroupModel tempGroupModel = null;

    private boolean isKeyArea;

    public BaseKeyContactEditFragment(boolean isKeyArea)
    {
        this.isKeyArea = isKeyArea;
        dataAdapterList = new ArrayList<BaseListItem>();
        selectGroupList = new ArrayList<Integer>();
        selectDataList = new ArrayList<Integer>();
    }

    @Override
    public void onClick(View v)
    {

    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_edit_list;
    }

    @Override
    public void initComponentViews(View view)
    {
        ((SettingActivity) parentActivity).initViewSize(1, 1);
        backBtn = (Button) view.findViewById(R.id.back_btn);
        deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
        selectBtn = (TextView) view.findViewById(R.id.select_btn);
        modifyBtn = (TextView) view.findViewById(R.id.modify_btn);
        addGroupBtn = (TextView) view.findViewById(R.id.add_group_btn);
        addContactBtn = (TextView) view.findViewById(R.id.add_contact_btn);
        selectAllBtn = (TextView) view.findViewById(R.id.select_all_btn);
        selectContactBtn = (TextView) view.findViewById(R.id.select_contact_btn);
        hsview = (HorizontalScrollView) view.findViewById(R.id.hscroll_grouplevel);
        backBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        modifyBtn.setOnClickListener(this);
        addGroupBtn.setOnClickListener(this);
        addContactBtn.setOnClickListener(this);
        selectAllBtn.setOnClickListener(this);
        selectContactBtn.setOnClickListener(this);

//        hsview.postDelayed(new Runnable() {
//            public void run() {
//                hsview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        }, 100L);
        groupTitle = (LinearLayout) view.findViewById(R.id.groups_level_title);
        editPopup = view.findViewById(R.id.contact_edit);
        coverView = view.findViewById(R.id.cover_view);
        groupGrid = (GridView) view.findViewById(R.id.group_grid);
        groupGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        
        editPopup.setVisibility(View.VISIBLE);
//        contactAdapter = new ContactBaseAdapter(getActivity());
//        groupGrid.setAdapter(contactAdapter);
    }

    protected void showGroupAddFragment(boolean isAdd, int id, int groupType, int newPosition)
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAdd", isAdd);
        bundle.putInt("id", id);
        bundle.putInt("groupType", groupType);
        bundle.putInt("newPosition", newPosition);
        ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, GroupAddFragment.class, bundle);
    }

    protected void showGroupDetailFragment(int id, int groupType)
    {
        showGroupAddFragment(false, id, groupType, -1);
    }

    protected void showToAddContact(int groupId, int position)
    {
        parentActivity.clearFragmentStack(R.id.container_setting_right);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAdd", true);
        bundle.putInt("groupId", groupId);
        bundle.putInt("position", position);
        ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, ContactAddFragment.class,true, bundle);
    }

    protected void showToModifyContact(int groupId,int id)
    {
        parentActivity.clearFragmentStack(R.id.container_setting_right);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isAdd", false);
        bundle.putInt("groupId", groupId);
        bundle.putInt("contactId", id);
        ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, ContactAddFragment.class,true, bundle);
    }

    protected void setEditType(EnumContactEditType type)
    {
        View contactBtn = isKeyArea ? selectContactBtn : addContactBtn;
        editType = type;
        contactAdapter.setEditType(type);
        contactAdapter.notifyDataSetChanged();
        selectGroupList.clear();
        selectDataList.clear();

        deleteBtn.setVisibility(View.GONE);
        selectBtn.setVisibility(View.GONE);
        modifyBtn.setVisibility(View.GONE);
        addGroupBtn.setVisibility(View.GONE);
        contactBtn.setVisibility(View.GONE);
        selectAllBtn.setVisibility(View.GONE);
        backBtn.setText(R.string.back);

        if(!UiApplication.getAtdService().isAtdAdminLogin())
        {
            return;
        }
        if (editType == EnumContactEditType.NORMAL)
        {
            selectBtn.setVisibility(View.VISIBLE);
            modifyBtn.setVisibility(View.VISIBLE);
            addGroupBtn.setVisibility(View.VISIBLE);
            contactBtn.setVisibility(View.VISIBLE);
            backBtn.setText(R.string.setting_contact_close);
        }
        else if (editType == EnumContactEditType.SELECT)
        {
            deleteBtn.setVisibility(View.VISIBLE);
            selectAllBtn.setVisibility(View.VISIBLE);
        }
        else if (editType == EnumContactEditType.MODIFY)
        {

        }
    }

    // hasGroup : 是否有组；isFirst : 是否是在首页
    protected void firstSet(boolean hasGroup, boolean isFirst)
    {
        selectBtn.setEnabled(hasGroup);
        modifyBtn.setEnabled(hasGroup);
        addContactBtn.setEnabled(hasGroup);
        selectContactBtn.setEnabled(hasGroup);
        addContactBtn.setEnabled(!isFirst);
        selectContactBtn.setEnabled(!isFirst);
    }

    protected void groupTitleInit()
    {
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
    }

    public void turnToPreGroup(int groupId)
    {

    }

    protected void showContact()
    {
        coverView.setVisibility(View.GONE);
    }

    protected void hideContact()
    {
        coverView.setVisibility(View.VISIBLE);
    }
}
