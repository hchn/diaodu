package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.ui.fragment.ContactSelectFragment;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：添加通讯录联系人/编辑联系人
 *
 * @author  hezhen
 *
 * @Date 2015-3-11
 */
public class ContactAddFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{
    private LinearLayout controlLayout;
    private EditText nameEdit;
    private EditText controlEdit;
    private EditText depEdit;
    private TextView confCheckedTv;
    private Spinner mSpinner;
    private LinearLayout container;
    private TextView addPhoneTv;
    private Button cancelBtn;
    private Button accomplishBtn;
    ArrayList<PhoneItem> phoneItems;
    private ContactModel contactModel;
    //新组和旧组都需要整理数据
    private GroupModel groupModel;
    private GroupModel newGroupModel;
    /**
     * 是否为添加联系人 true: 添加联系人 false:编辑联系人
     */
    boolean isAdd = false;


    ArrayList<String> phoneTypeList = new ArrayList<String>();

    private int typePos = 0; //用户类型
    private String confNum = null;
    private boolean vdPhoneIsConf = false;// vd为入会号码
    private String contactName = null;
    private String groupName = null;
    private String vdPhoneNunm = null;
    
    private int position = 0;
    Drawable checkOn;
    Drawable checkOff;
    
    private boolean isCallBack = false;

    public ContactAddFragment()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CONTACT_SELECT_BELONG_DEP);
        phoneItems = new ArrayList<ContactAddFragment.PhoneItem>();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.tv_add_new_phone:
                if (phoneItems.size() >= UiConfigEntry.PHONE_NUM_MAX)
                {
                    String notice = String.format(UiApplication.getInstance().getResources().getString(R.string.notice_contact_number_max),
                            UiConfigEntry.PHONE_NUM_MAX);
                    ToastUtil.showToast(notice);
                }
                else
                {
                    addNewPhoneNum();
                }
                break;
            case R.id.btn_create:
                contactOperate();
                UiUtils.hideSoftKeyboard(getActivity());
                break;
            case R.id.btn_cancel:
                UiUtils.hideSoftKeyboard(getActivity());
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.SETTING_CONTACT_CANCEL);
                break;
            case R.id.confnum_check_tv:
                updateConfSelectItem(null);
                break;
            case R.id.edit_dep:
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.CONTACT_SELECT_BELONG_DEP);
                bundle.putInt("showType", 1);// 只选调度用户
                ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, ContactSelectFragment.class, bundle);
                break;
        }
    }
    private void setVdConfCheck(boolean isCheck) {
    	vdPhoneIsConf = isCheck;
    	confCheckedTv.setCompoundDrawablesWithIntrinsicBounds(vdPhoneIsConf?checkOn:checkOff, null, null, null);
	}
    private void updateConfSelectItem(PhoneItem pItem)
    {
    		setVdConfCheck(pItem == null);
            for(PhoneItem phoneItem : phoneItems)
            {
            	phoneItem.confIsCheck = phoneItem.equals(pItem);
                phoneItem.setCheckDrawable(phoneItem.confIsCheck?checkOn:checkOff);
            }
    }
    @Override
    public void initComponentViews(View view)
    {
        checkOn = UiApplication.getInstance().getResources().getDrawable(R.drawable.btn_check_on_holo_light);
        checkOff = UiApplication.getInstance().getResources().getDrawable(R.drawable.btn_check_off_holo_light);

        parentActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Bundle bundle = getArguments();
        int groupId = 0;
        int contactId = 0;
        if (bundle != null)
        {
            this.isAdd = bundle.getBoolean("isAdd", true);
            groupId = bundle.getInt("groupId");
            contactId = bundle.getInt("contactId");
            position = bundle.getInt("position");
            groupModel = UiApplication.getContactService().getDepById(groupId);
            groupModel = groupModel.clone();
            ContactModel contact = UiApplication.getContactService().getContactById(contactId);
            contactModel = contact == null? new ContactModel():contact.clone();
        }
        nameEdit = (EditText) view.findViewById(R.id.name_edit);
        mSpinner = (Spinner) view.findViewById(R.id.spinner1);
        container = (LinearLayout) view.findViewById(R.id.container);
        controlLayout = (LinearLayout) view.findViewById(R.id.linlay_control);
        controlEdit = (EditText) view.findViewById(R.id.edt_control_num);
        depEdit = (EditText) view.findViewById(R.id.edit_dep);
        confCheckedTv = (TextView) view.findViewById(R.id.confnum_check_tv);
        addPhoneTv = (TextView) view.findViewById(R.id.tv_add_new_phone);
        cancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        accomplishBtn = (Button) view.findViewById(R.id.btn_create);

        depEdit.setOnClickListener(this);
        addPhoneTv.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        accomplishBtn.setOnClickListener(this);
        confCheckedTv.setOnClickListener(this);
        phoneTypeList = UiApplication.getContactService().getPhoneTypes();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentActivity, R.layout.simple_spinner_item, UiApplication.getContactService()
                .getContactTypes());
        adapter.setDropDownViewResource(R.layout.adapter_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                typePos = position;
                if(isVdContact())
                { 
                	controlLayout.setVisibility(View.VISIBLE);
                }else
                {
                	controlLayout.setVisibility(View.GONE);
                	if(vdPhoneIsConf)
                    {
                    	setVdConfCheck(false);
                    	confNum = null;
                    }
                    vdPhoneNunm = null;
                    controlEdit.setText(null);
                }
//                if(!isCallBack)
//                {
                	 
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
       
       
    }
    @Override
    public void onResume() {
    	super.onResume();
    	 if(!isCallBack)
         {
             if (isAdd)
             {
                 groupName = groupModel.getName();
             }
             else
             {
                 String contactTypeName = contactModel.getTypeName();
                 typePos = UiApplication.getContactService().getContactTypes().indexOf(contactTypeName);
                 contactName = contactModel.getName();
                 groupName = groupModel.getName();
                 
                 ArrayList<ContactNum> phoneList = contactModel.getPhoneNumList();
                 int size = phoneList.size();
                 boolean isvdContact = isVdContact();
                 confNum = contactModel.getConfNum();
                 for (int i = 0; i < size; i++)
                 {
                     ContactNum contactNum = phoneList.get(i);
                     String phoneNum = contactNum.getNumber();
                     if (i == 0 && isvdContact)
                     {
                         vdPhoneNunm = phoneNum;
                         if(confNum.equals(phoneNum))
                         {
                             confNum = phoneNum;
                             vdPhoneIsConf = true;
                         }
                         continue;
                     }
                     PhoneItem phoneItem = new PhoneItem();
                     phoneItems.add(phoneItem);
                     phoneItem.setDeletePhoneItem(new DeletePhoneItem()
                     {
                         @Override
                         public void deleteSelf(PhoneItem phoneItem)
                         {
                             container.removeView(phoneItem.getView());
                             phoneItems.remove(phoneItem);
                         }
                     });
                     phoneItem.setPhoneText(phoneNum);
                     phoneItem.setPhoneType(contactNum.getTypeName());
                     if (!TextUtils.isEmpty(confNum) && confNum.equals(phoneNum))
                     {
                         phoneItem.confIsCheck = true;
                     }
                 }
               
             }
         }
         if (typePos >= 0)
         {
             mSpinner.setSelection(typePos);
         }
         setVdConfCheck(vdPhoneIsConf);
         nameEdit.setText(contactName);
         depEdit.setText(groupName);
         controlEdit.setText(vdPhoneNunm);
         controlLayout.setVisibility(isVdContact() ? View.VISIBLE : View.GONE);
         for(PhoneItem phoneItem : phoneItems)
         {
        	 phoneItem.setCheckDrawable(phoneItem.confIsCheck?checkOn:checkOff);
             phoneItem.setPhoneText(phoneItem.getPhoneNum().getNumber());
             container.addView(phoneItem.getView());
         }
    }
    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CONTACT_SELECT_BELONG_DEP);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        isCallBack = true;
        container.removeAllViews();
        typePos = mSpinner.getSelectedItemPosition();
        contactName = nameEdit.getText().toString();
        groupName = depEdit.getText().toString();
        vdPhoneNunm = controlEdit.getText().toString();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_addcontact;
    }

    /**
     * 方法说明 :添加新号码
     * @author HeZhen
     * @Date 2015-4-22
     */
    private PhoneItem addNewPhoneNum()
    {
        final PhoneItem phoneItem = new PhoneItem();
        container.addView(phoneItem.getView());
        phoneItems.add(phoneItem);
        phoneItem.setDeletePhoneItem(new DeletePhoneItem()
        {
            @Override
            public void deleteSelf(PhoneItem phoneItem)
            {
                container.removeView(phoneItem.getView());
                phoneItems.remove(phoneItem);
            }
        });
        return phoneItem;
    }

    /**
     * 方法说明 :是监控或调度用户
     * @return
     * @author hz
     * @Date 2015-9-29
     */
    private boolean isVdContact()
    {
        return typePos <= 1;
    }

    /**
    * 方法说明 :对联系人进行添加或更新操作
    * @author hz
    * @Date 2015-9-29
    */
    private void contactOperate()
    {
        ArrayList<ContactNum> contactNums;
        contactNums = contactModel.getPhoneNumList();
        
        contactName = nameEdit.getText().toString();

        int typePos = mSpinner.getSelectedItemPosition();
        
        String typeName = UiApplication.getContactService().getContactTypes().get(typePos);

        if (!verifyInputName(contactName))//验证名称
        {
            return;
        }
        else
        {
            contactModel.setName(contactName);
        }
        if(vdPhoneIsConf)
        {
        	confNum = controlEdit.getText().toString();
        }
        if (!verifyConfNum())
        {
            return;
        }
        else
        {
            contactModel.setConfNum(confNum);
        }
        if (TextUtils.isEmpty(typeName))
        {
            return;
        }
        else
        {
            contactModel.setTypeName(typeName);
        }
        if (isVdContact())// 调度和监控用户
        {
            String phoneNum = controlEdit.getText().toString();
            if (TextUtils.isEmpty(phoneNum))
            {
                ToastUtil.showToast(parentActivity, null, R.string.notice_number_not_null);
                return;
            }
            else
            {
                ContactNum contactNum = new ContactNum();
                contactNum.setNumber(phoneNum);
                if (ContactUtil.isVsByContactType(typeName)) // 监控号码
                {
                    contactNum.setTypeName(ContactUtil.getVsPhoneTypeName());
                }
                else if (ContactUtil.isDsByContactType(typeName))
                {
                    contactNum.setTypeName(ContactUtil.getDsPhoneTypeName());
                }
                contactNums.add(contactNum);
            }
        }
        for (PhoneItem phoneItem : phoneItems)
        {
            String pNum = phoneItem.getPhoneNum().getNumber();
            if (!TextUtils.isEmpty(pNum))
            {
                contactNums.add(phoneItem.getPhoneNum());
            }
        }
        if (isAdd)// 如果是添加联系人，则初始化位置和编号
        {
            contactModel.setNumber(ContactUtil.contactMaxNum + 1);
            // 联系人位置加到最后面
            int position = 0;
            for (GroupModel group : groupModel.getChildrenDepList())
            {
                if (position < group.getPosition())
                {
                    position = group.getPosition();
                }
            }
            for (ContactModel contact : groupModel.getChildrenContactList())
            {
                ContactPosInGroup posGroup = contact.getPosGroup(groupModel.getId());
                if (posGroup != null && position < posGroup.getPosition())
                {
                    position = posGroup.getPosition();
                }
            }
            if (position == 0)
            {
                position = groupModel.getChildrenContactList().size() + groupModel.getChildrenDepList().size();
            }
            else
            {
                position++;
            }
            ContactPosInGroup posGroup = new ContactPosInGroup();
            posGroup.setPosition(position);
            posGroup.setParentGroup(groupModel);
            contactModel.addPosGroup(posGroup);
            UiApplication.getContactService().addContact(contactModel);
        }
        else
        {
            ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
            if (groupModel != null)
                groupList.add(groupModel);
            if (newGroupModel != null)
                groupList.add(newGroupModel);
            UiApplication.getContactService().modifyContact(contactModel, groupList);
        }

    }

    /**
     * 方法说明 : 验证用户号码
     * @param phoneNum
     * @return
     * @author HeZhen
     * @Date 2015-7-9
     */
    private boolean verifyInputPhone(String phoneNum)
    {
        if (TextUtils.isEmpty(phoneNum))
        {
            ToastUtil.showToast(parentActivity, null, R.string.notice_number_not_null);
            return false;
        }
        else if (phoneNum.equals(UiApplication.getConfigService().getAccountNumber()))
        {
            ToastUtil.showToast("添加号码非法");
            return false;
        }

        return true;
    }

    /**
     * 方法说明 :验证输入名称
     * @param name
     * @return
     * @author HeZhen
     * @Date 2015-7-9
     */
    private boolean verifyInputName(String name)
    {
        if (TextUtils.isEmpty(name))
        {
            ToastUtil.showToast(parentActivity, null, R.string.notice_name_not_null);
            return false;
        }
        if (UiUtils.getLength(name) > UiConfigEntry.CONTACT_NAME_MAX)
        {
            String content = String.format(UiApplication.getInstance().getResources().getString(R.string.notice_name_max), UiConfigEntry.CONTACT_NAME_MAX);
            ToastUtil.showToast(null, content);
            return false;
        }
        return true;
    }

    private boolean verifyConfNum()
    {
        if (TextUtils.isEmpty(confNum))
        {
            ToastUtil.showToast("参会号码不能为空，请选择参会");
            return false;
        }
        return true;
    }

    /**
     * 说明： 添加新号码 逻辑
     *
     * @Date 2015-5-12
     */
    class PhoneItem
    {
        View view;
        ContactNum phoneNum;
        DeletePhoneItem deletePhoneItem;
        boolean confIsCheck = false;// 会议号码是否选中
        TextView checkText;

        public PhoneItem()
        {
            phoneNum = new ContactNum();
        }

        public ContactNum getPhoneNum()
        {
            if (phoneNum == null)
            {
                phoneNum = new ContactNum();
            }
            return phoneNum;
        }

        public void setPhoneText(String text)
        {
            if (TextUtils.isEmpty(text))
            {
                return;
            }
            ((EditText) getView().findViewById(R.id.phone_edit)).setText(text);
            getPhoneNum().setNumber(text);
        }

        public void setPhoneType(String typeName)
        {
            if (TextUtils.isEmpty(typeName))
            {
                return;
            }
            int seletion = 0;
            int size = UiApplication.getContactService().getPhoneTypes().size();
            for (int i = 0; i < size; i++)
            {
                String tyName = UiApplication.getContactService().getPhoneTypes().get(i);
                if (typeName.equals(tyName))
                {
                    seletion = i;
                    break;
                }
            }
            ((Spinner) getView().findViewById(R.id.type_spinner)).setSelection(seletion > 0 ? seletion : 0);
            getPhoneNum().setTypeName(typeName);
        }

        public View getView()
        {
            if (view == null)
            {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.include_contact_addphone, null);
                Spinner spinner = (Spinner) view.findViewById(R.id.type_spinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(parentActivity, R.layout.simple_spinner_item, phoneTypeList);
                adapter.setDropDownViewResource(R.layout.adapter_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        String typeName = phoneTypeList.get(position);
                        getPhoneNum().setTypeName(typeName);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {

                    }
                });
                ((EditText) view.findViewById(R.id.phone_edit)).addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {
                        String message = s.toString();
                        getPhoneNum().setNumber(message);
                        if (confIsCheck)
                        {
                            confNum = message;
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {
                    }

                    @Override
                    public void afterTextChanged(Editable s)
                    {
                    }
                });
                view.findViewById(R.id.delete).setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (confIsCheck)
                        {
                            confNum = "";
                        }
                        deletePhoneItem.deleteSelf(PhoneItem.this);
                    }
                });
                checkText = (TextView) view.findViewById(R.id.checkedTextView1);
                checkText.setOnClickListener(new OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        confNum = getPhoneNum().getNumber();
                        updateConfSelectItem(PhoneItem.this);
                    }
                });
            }
            return view;
        }

        public void setCheckDrawable(Drawable drawable)
        {
            checkText.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }

        public void setDeletePhoneItem(DeletePhoneItem deletePhoneItem)
        {
            this.deletePhoneItem = deletePhoneItem;
        }
    }

    public interface DeletePhoneItem
    {
        void deleteSelf(PhoneItem phoneItem);
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.CONTACT_SELECT_BELONG_DEP)
        {
            int operaCode = (Integer) args[0];
            if (operaCode == 0)// 取消操作
            {
                parentActivity.backToPreFragment(R.id.container_setting_right);
            }
            else
            {
                int depId = (Integer) args[1];
                parentActivity.backToPreFragment(R.id.container_setting_right);
                newGroupModel = UiApplication.getContactService().getDepById(depId);
                if(newGroupModel == null)
                {
                    return;
                }
                else
                {
                    newGroupModel = newGroupModel.clone();
                }
                groupName = newGroupModel.getName();
//                contactModel.removPosGroup(groupModel.getId());
//                ContactPosInGroup posDep = new ContactPosInGroup();
//                posDep.setParentGroup(newGroupModel);
//                posDep.setPosition(newGroupModel.getChildrenDepList().size() + newGroupModel.getChildrenContactList().size());
//                if (isAdd)
//                {
//                    groupModel = newGroupModel;
//                }
//                else
//                {
//                    groupModel.removContact(contactModel.getId());
//                }
//                contactModel.addPosGroup(posDep);
//                newGroupModel.addContact(contactModel);
            }
        }
    };
}
