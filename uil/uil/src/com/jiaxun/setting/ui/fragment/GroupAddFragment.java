package com.jiaxun.setting.ui.fragment;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：通讯录 添加组/更新组 （联系人组，按键组）
 *
 * @author  HeZhen
 *
 * @Date 2015-4-15
 */
public class GroupAddFragment extends BaseFragment implements OnClickListener
{
    private EditText groupEdit;
    private GroupModel groupModel;
    boolean isAdd = false;
    int groupType = EnumGroupType.CONTACT_GROUP;

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void initComponentViews(View view)
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            groupType = bundle.getInt("groupType");
            // 是否为添加。true 添加新组 false 组详情
            isAdd = bundle.getBoolean("isAdd");
            // 添加组所在父组
            int groupId = bundle.getInt("id");
            GroupModel parentGroup = null;
            int newPosition = bundle.getInt("newPosition");
            if (groupType == EnumGroupType.CONTACT_GROUP || groupType == EnumGroupType.KEY_GROUP)
            {
                parentGroup = UiApplication.getContactService().getDepById(groupId);
            }
            if (isAdd)
            {
                if (groupModel == null)
                {
                    groupModel = new GroupModel();
                }

                if (parentGroup == null)
                {
                    groupModel.setParent(null);
                }
                else
                {
                    groupModel.setParent(parentGroup);
                    groupModel.setParentId(groupId);
                }
                groupModel.setPosition(newPosition);
            }
            else
            {
                groupModel = parentGroup.clone();
            }

        }
//        View view1 = view.findViewById(R.id.local_pic);
//        View view2 = view.findViewById(R.id.u_pic);
//        localPic = (GridView) view1.findViewById(R.id.gridview);
//        uPic = (GridView) view2.findViewById(R.id.gridview);
        groupEdit = (EditText) view.findViewById(R.id.edit_group_name);
        groupEdit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(UiConfigEntry.GROUP_NAME_MAX) });
        if (!isAdd && groupModel != null)
        {
            groupEdit.setText(groupModel.getName());
        }
        else
        {
//            ArrayList<Integer> picList = new ArrayList<Integer>();
//            for (int i = 0; i < 12; i++)
//            {
//                picList.add(R.drawable.head_default);
//            }
//            AddNewGroupHeadAdapter addNewGroupHeadAdapter = new AddNewGroupHeadAdapter(getActivity(), picList);
//            localPic.setAdapter(addNewGroupHeadAdapter);
        }

        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_create).setOnClickListener(this);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_addgroup;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_cancel:
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.SETTING_CONTACT_CANCEL);
                UiUtils.hideSoftKeyboard(getActivity());
                break;
            case R.id.btn_create:
                if (isAdd)
                {
                    addANewGroup();
                }
                else
                {
                    updateGroupModel();
                }
                UiUtils.hideSoftKeyboard(getActivity());
                break;
        }
    }

    private boolean verifyInput(String groupName)
    {
        if (TextUtils.isEmpty(groupName))
        {
            ToastUtil.showToast(parentActivity, null, R.string.notice_name_not_null);
            return false;
        }
        if (UiUtils.getLength(groupName) > UiConfigEntry.GROUP_NAME_MAX)
        {
            String content = String.format(UiApplication.getInstance().getResources().getString(R.string.notice_name_max), UiConfigEntry.GROUP_NAME_MAX);
            ToastUtil.showToast(content);
            return false;
        }
        return true;
    }

    /**
     * 方法说明 :执行 添加新组操作
     * @author HeZhen
     * @Date 2015-5-13
     */
    private void addANewGroup()
    {
        String groupName = groupEdit.getText().toString();
        if (!verifyInput(groupName))
        {
            return;
        }
        groupModel.setName(groupName);
        groupModel.setType(groupType);
        UiApplication.getContactService().addDep(groupModel);
    }

    /**
     * 方法说明 :执行 更新组操作
     * @author HeZhen
     * @Date 2015-5-13
     */
    private void updateGroupModel()
    {
        String groupName = groupEdit.getText().toString();
        if (!verifyInput(groupName))
        {
            return;
        }
        groupModel.setName(groupName);
        UiApplication.getContactService().modifyDep(groupModel);
    }
}
