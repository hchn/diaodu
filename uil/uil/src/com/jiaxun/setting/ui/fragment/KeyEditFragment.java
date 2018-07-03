package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.adapter.ContactAdapter;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.fragment.ContactSelectFragment;
import com.jiaxun.uil.ui.widget.ContactDetailPopupWindow;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.enums.EnumContactEditType;
import com.jiaxun.uil.util.enums.EnumKeyEditType;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：修改按键、显示按键详情
 *
 * @author  HeZhen
 *
 * @Date 2015-5-15
 */
public class KeyEditFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{

    private static final String TAG = KeyEditFragment.class.getSimpleName();

    private GridView contactGrid;

    private Button backBtn;
    private TextView deleteBtn;
    private TextView selectBtn;
    private TextView addContactBtn;
    private TextView selectAllBtn;

    private EditText keyNameEditT;

    private Button cancelBtn;
    private Button accomplishBtn;

    private GroupModel keyEntityClone;
    private ContactAdapter keyAdapter;
    /**
     * key 包含的成员列表
     */
    private ArrayList<BaseListItem> dataAdapter;
    /**
     * 当前成员
     */
    private ArrayList<Integer> contactList;
    /**
     * 用于选择删除
     */
    private ArrayList<Integer> selectContactListForDele;

    private EnumContactEditType editType = EnumContactEditType.NORMAL;

    private ContactDetailPopupWindow detailPopup;

    private int operateType = EnumKeyEditType.KEY_DETAIL;

    private boolean selectContactOperate = false;//选择联系人操作

    public KeyEditFragment()
    {
        dataAdapter = new ArrayList<BaseListItem>();
        contactList = new ArrayList<Integer>();
        selectContactListForDele = new ArrayList<Integer>();
        keyEntityClone = new GroupModel();
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
            case R.id.add_contact_btn:
                selectContactOperate = true;
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.SETTING_KEYMEMADD_SELECTE_CONTACT);
                bundle.putIntegerArrayList("selectedContactList", contactList);
                ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, ContactSelectFragment.class, true, bundle);
                break;
            case R.id.select_all_btn:
                selectAllPrefsBaseItem(!isSelectAll());
                break;
            case R.id.back_btn:
                setEditType(EnumContactEditType.NORMAL);
                break;
            case R.id.delete_btn:
                contactList.removeAll(selectContactListForDele);
                updateAdapter();
                selectContactListForDele.clear();
                break;
            case R.id.btn_create:
                accomplishEdit();
                break;
            case R.id.btn_cancel:
                parentActivity.clearFragmentStack(R.id.container_setting_right);
                break;
        }
    }
    private boolean verifyInput(String keyName)
    {
        if (TextUtils.isEmpty(keyName))
        {
            ToastUtil.showToast(parentActivity, null, R.string.notice_name_not_null);
            return false;
        }
        if (UiUtils.getLength(keyName) > UiConfigEntry.GROUP_NAME_MAX)
        {
            String content = String.format(UiApplication.getInstance().getResources().getString(R.string.notice_name_max), UiConfigEntry.GROUP_NAME_MAX);
            ToastUtil.showToast(content);
            return false;
        }
        return true;
    }
    
    private void accomplishEdit()
    {
        Log.info(TAG, "accomplishEdit : keyEntityClone = " + keyEntityClone + ",contactList == " + contactList);
        String keyName = keyNameEditT.getText().toString();
        keyEntityClone.setName(keyName);
        if (!verifyInput(keyName))
        {
            return;
        }
        if (contactList.size() <= 0)
        {
            ToastUtil.showToast("", "请选择联系人");
            return;
        }
        keyEntityClone.getChildrenContactList().clear();
        ArrayList<ContactModel> contactModelList = new ArrayList<ContactModel>();
        for (Integer contactId : contactList)
        {
            ContactModel contact = UiApplication.getContactService().getContactById(contactId);
            if(contact != null)
            {
                contactModelList.add(contact.clone());
            }
        }
        keyEntityClone.getChildrenContactList().addAll(contactModelList);
        if (operateType == EnumKeyEditType.KEY_ADD)
        {
            UiApplication.getContactService().addDep(keyEntityClone);
        }
        else
        {
            UiApplication.getContactService().modifyDep(keyEntityClone);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
    }
    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_key_edit;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (!selectContactOperate)
            release();
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_KEYMEMADD_SELECTE_CONTACT);
    }

    @Override
    public void initComponentViews(View view)
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_KEYMEMADD_SELECTE_CONTACT);
        keyAdapter = new ContactAdapter(parentActivity);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            operateType = bundle.getInt("type", EnumKeyEditType.KEY_DETAIL);
            int keyId = bundle.getInt("keyId", -1);
            GroupModel keyEntity = null;
            if (keyId != -1)
            {
                keyEntity = UiApplication.getContactService().getDepById(keyId);
                if (keyEntity == null)
                {
                    keyEntity = new GroupModel();
                }
            }
            if (operateType == EnumKeyEditType.KEY_MODIFY)
            {
                keyEntityClone = keyEntity.clone();
            }
            else if (operateType == EnumKeyEditType.KEY_ADD)
            {
                int pos = bundle.getInt("position");
                keyEntityClone.setPosition(pos);
                keyEntityClone.setParentId(keyId);
                keyEntityClone.setParent(keyEntity);
                keyEntityClone.setType(EnumGroupType.KEY);
            }
            else if (operateType == EnumKeyEditType.KEY_DETAIL)
            {
                keyEntityClone = keyEntity.clone();
            }
            if (!selectContactOperate)
            {
                dataAdapter.clear();
                contactList.clear();
                selectContactListForDele.clear();

                ArrayList<ContactModel> contacts = keyEntityClone.getChildrenContactList();
                for (ContactModel contact : contacts)
                {
                    int contactId = contact.getId();
                    if (!contactList.contains(contactId))
                    {
                        contactList.add(contactId);
                    }
                }
            }
            selectContactOperate = false;
            updateAdapter();
        }
        detailPopup = new ContactDetailPopupWindow(parentActivity);

        contactGrid = (GridView) view.findViewById(R.id.gv_keys);
        
        keyAdapter.initData(GroupModel.DEFAULT_PARENT_ID, dataAdapter);
        contactGrid.setAdapter(keyAdapter);

        contactGrid.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                BaseListItem baseItem = dataAdapter.get(position);
                if (editType == EnumContactEditType.NORMAL)
                {
                    detailPopup.setWidth(getRootView().getMeasuredWidth());
                    detailPopup.setDetail(baseItem.getId()).showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.RIGHT, 0, 0);
                }
                else if (editType == EnumContactEditType.SELECT)
                {
                    baseItem.setChecked(!baseItem.isChecked());
                    keyAdapter.notifyDataSetChanged();
                    if (baseItem.isChecked())
                    {
                        selectContactListForDele.add(baseItem.getId());
                    }
                    else
                    {
                        selectContactListForDele.remove((Integer) baseItem.getId());
                    }
                    selectAllBtn.setText(isSelectAll() ? R.string.setting_contact_selectall_cancel : R.string.setting_contact_selectall);
                }
            }
        });

        backBtn = (Button) view.findViewById(R.id.back_btn);
        deleteBtn = (TextView) view.findViewById(R.id.delete_btn);
        selectBtn = (TextView) view.findViewById(R.id.select_btn);
        addContactBtn = (TextView) view.findViewById(R.id.add_contact_btn);
        selectAllBtn = (TextView) view.findViewById(R.id.select_all_btn);
        accomplishBtn = (Button) view.findViewById(R.id.btn_create);
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);

        accomplishBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        addContactBtn.setOnClickListener(this);
        selectAllBtn.setOnClickListener(this);

        addContactBtn.setText(R.string.setting_key_add_contact);

        keyNameEditT = (EditText) view.findViewById(R.id.et_key_name);
        String name = keyEntityClone.getName();
        if (!TextUtils.isEmpty(name))
        {
            keyNameEditT.setText(name);
            keyNameEditT.setSelection(name.length());
        }

        setEditType(EnumContactEditType.NORMAL);
        selectAllPrefsBaseItem(false);

        if (operateType == EnumKeyEditType.KEY_DETAIL)
        {
            view.findViewById(R.id.contact_edit).setVisibility(View.GONE);
            accomplishBtn.setVisibility(View.INVISIBLE);
            keyNameEditT.setEnabled(false);
        }
    }

    private void updateAdapter()
    {
        dataAdapter.clear();
        for (Integer contactId : contactList)
        {
            ContactModel contact = UiApplication.getContactService().getContactById(contactId);
            if (contact == null)
            {
                continue;
            }
            BaseListItem baseItem = new BaseListItem();
            baseItem.setId(contactId);
            baseItem.setType(BaseListItem.TYPE_CONTACT);
            baseItem.setName(contact.getName());
            dataAdapter.add(baseItem);
        }
        keyAdapter.initData(GroupModel.DEFAULT_PARENT_ID, dataAdapter);
        keyAdapter.notifyDataSetChanged();
    }

    private boolean isSelectAll()
    {
        return selectContactListForDele.size() == contactList.size();
    }

    private void selectAllPrefsBaseItem(boolean selectAll)
    {
        selectContactListForDele.clear();

        for (BaseListItem baseItem : dataAdapter)
        {
            baseItem.setChecked(selectAll);
            if (selectAll)
            {
                selectContactListForDele.add(baseItem.getId());
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
        keyAdapter.notifyDataSetChanged();
    }

    private void setEditType(EnumContactEditType type)
    {
        editType = type;
        keyAdapter.setEditType(type);
        keyAdapter.notifyDataSetChanged();

        deleteBtn.setVisibility(View.GONE);
        selectBtn.setVisibility(View.GONE);
        addContactBtn.setVisibility(View.GONE);
        selectAllBtn.setVisibility(View.GONE);
        backBtn.setVisibility(View.GONE);

        if (editType == EnumContactEditType.NORMAL)
        {
            selectBtn.setVisibility(View.VISIBLE);
            addContactBtn.setVisibility(View.VISIBLE);
        }
        else if (editType == EnumContactEditType.SELECT)
        {
            backBtn.setVisibility(View.VISIBLE);
            deleteBtn.setVisibility(View.VISIBLE);
            selectAllBtn.setVisibility(View.VISIBLE);
        }
        else if (editType == EnumContactEditType.MODIFY)
        {

        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.SETTING_KEYMEMADD_SELECTE_CONTACT)
        {
            int operaCode = (Integer)args[0];
            if(operaCode == 0)//取消操作
            {
                ((SettingActivity) parentActivity).backToPreFragment(R.id.container_setting_right);
            }else
            {
                ArrayList<Integer> selectContactList = (ArrayList<Integer>) args[1];
                if (selectContactList != null)
                {
                    ((SettingActivity) parentActivity).backToPreFragment(R.id.container_setting_right);
                    contactList.addAll(selectContactList);
                    updateAdapter();
                } 
            }
         
        } 
    }
}
