package com.jiaxun.sdk.dcl.module.contact.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteException;
import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.dcl.module.contact.itf.DclContactService;
import com.jiaxun.sdk.dcl.util.CharacterParser;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues;
import com.jiaxun.sdk.dcl.util.db.DBHelper;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Contact;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Contact_Data;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Contact_Group;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_DataType;
import com.jiaxun.sdk.dcl.util.db.DBConstantValues.DB_Group;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.log.Log;

/**
 * 说明：联系人业务功能接口
 * 
 * @author hezhen
 * 
 * @Date 2015-1-16
 */
public class DclContactServiceImpl implements DclContactService
{
    private static final String TAG = DclContactServiceImpl.class.getName();

    private static DclContactServiceImpl instance;
    /**
     *所有联系人 
     */
    
    private List<ContactModel> contactList = new CopyOnWriteArrayList<ContactModel>();//Collections.synchronizedList(new ArrayList<ContactModel>());
    private ConcurrentHashMap<Integer, ContactModel> contactMap = new ConcurrentHashMap<Integer, ContactModel>();//Collections.synchronizedMap(new HashMap<Integer, ContactModel>());
    /**
     *通讯录所有组，快捷按键组，快捷按键 
     */
    private ConcurrentHashMap<Integer, GroupModel> allDepMap = new ConcurrentHashMap<Integer, GroupModel>();//Collections.synchronizedMap(new HashMap<Integer, GroupModel>());
    private List<GroupModel> allDepList =  new CopyOnWriteArrayList<GroupModel>();//Collections.synchronizedList(new ArrayList<GroupModel>());
    private List<DataType> dataTypeList =  new CopyOnWriteArrayList<DataType>();//Collections.synchronizedList(new ArrayList<DataType>());
    private DBHelper dbHelper;

    private ContentResolver contentResolver;

    private DclContactServiceImpl()
    {
        this.contentResolver = SdkUtil.getApplicationContext().getContentResolver();
        this.dbHelper = DBHelper.getInstance();
        loadData();
    }

    public static DclContactServiceImpl getInstance()
    {
        if (instance == null)
        {
            instance = new DclContactServiceImpl();
        }
        return instance;
    }

    @Override
    public List<ContactModel> getContactList()
    {
        return contactList;
    }

    @Override
    public ContactModel getContactById(int key)
    {
        return contactMap.get(key);
    }

    @Override
    public GroupModel getDepById(int key)
    {
        return allDepMap.get(key);
    }

    @Override
    public ArrayList<GroupModel> getDepRootList()
    {
        ArrayList<GroupModel> depRootList = new ArrayList<GroupModel>();
        for (GroupModel group : allDepList)
        {
            if (group.getParentId() == GroupModel.DEFAULT_PARENT_ID)
            {
                depRootList.add(group);
            }
        }
        return depRootList;
    }

    @Override
    public List<GroupModel> getDepList()
    {
        return allDepList;
    }

    @Override
    public ArrayList<ContactModel> getSubscribeContacts()
    {
        ArrayList<ContactModel> subscribeContacts = new ArrayList<ContactModel>();
        for (ContactModel contact : contactList)
        {
            if (contact.getSubScribe() == 1)
            {
                subscribeContacts.add(contact);
            }
        }
        return subscribeContacts;
    }

    @Override
    public ContactModel getContactByNum(String phoneNum)
    {
        if (TextUtils.isEmpty(phoneNum) || !phoneNum.matches("[0-9]*"))
        {
            return null;
        }
        synchronized (contactList)
        {
            for (ContactModel contact : contactList)
            {
                ArrayList<ContactNum> phoneList = contact.getPhoneNumList();
                for (ContactNum contactNum : phoneList)
                {
                    if (phoneNum.equals(contactNum.getNumber()))
                    {
                        return contact;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean addContact(ContactModel contactModel)
    {
        Log.info(TAG, "addContact::contactModel:" + contactModel);
        if (contactModel == null)
        {
            return false;
        }
        ArrayList<ContactModel> contactModels = new ArrayList<ContactModel>();

        contactModels.add(contactModel);
        return addContacts(contactModels);
    }

    @Override
    public boolean addContacts(ArrayList<ContactModel> contactModels)
    {
        Log.info(TAG, "addContact::contactModels:");
        if (contactModels == null || contactModels.size() == 0)
        {
            return false;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try
        {
            int groupId = -1;
            int position = -1;
            for (ContactModel contactModel : contactModels)
            {
                ops.clear();
                if (contactModel.getParentGroupList().size() > 0)
                {
                    ContactPosInGroup posInGroup = contactModel.getParentGroupList().get(0);
                    GroupModel parentGroupModel = null;

                    if (posInGroup != null)
                    {
                        position = posInGroup.getPosition();
                        parentGroupModel = posInGroup.getParentGroup();
                    }
                    if (parentGroupModel != null)
                    {
                        groupId = parentGroupModel.getId();
                    }
                }

                // 添加联系人
                String name = contactModel.getName();
                ContentValues value = new ContentValues();
                value.put(DB_Contact.NAME, contactModel.getName());
                value.put(DB_Contact.TYPE_NAME, contactModel.getTypeName());
                value.put(DB_Contact.NUMBER, contactModel.getNumber());
                value.put(DB_Contact.CONF_NUM, contactModel.getConfNum());
                value.put(DB_Contact.SUBSCRIBE, 0);
                ops.add(ContentProviderOperation.newInsert(DB_Contact.CONTENT_URI).withValues(value).withYieldAllowed(true).build());
                ContentProviderResult[] results = null;

                results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);

                int resultId;
                if (results != null && results.length > 0)
                {
                    resultId = getId(results[0].toString());
                }
                else
                {
                    return false;
                }
                // 添加到联系人和组关系表
                ops.clear();
                ContentValues contactGroupValue = new ContentValues();
                contactGroupValue.put(DB_Contact_Group.CONTACT_ID, resultId);
                contactGroupValue.put(DB_Contact_Group.GROUP_ID, groupId);
                contactGroupValue.put(DB_Contact_Group.CONTACT_POSITION, position);
                ops.add(ContentProviderOperation.newInsert(DB_Contact_Group.CONTENT_URI).withValues(contactGroupValue).withYieldAllowed(true).build());
                // 添加联系人数据，电话号码等（后期 邮箱传真等）
                for (ContactNum contactNum : contactModel.getPhoneNumList())
                {
                    ContentValues contactDataValue = new ContentValues();
                    contactDataValue.put(DB_Contact_Data.CONTACT_ID, resultId);
                    contactDataValue.put(DB_Contact_Data.DATA_TYPE, contactNum.getTypeName());
                    contactDataValue.put(DB_Contact_Data.DATA, contactNum.getNumber());
                    ops.add(ContentProviderOperation.newInsert(DB_Contact_Data.CONTENT_URI).withValues(contactDataValue).withYieldAllowed(true).build());
                }
                results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
                // 更新本地缓存
                contactModel.setId(resultId);
                // 本地缓存更新
                contactList.add(contactModel);
                contactMap.put(resultId, contactModel);
                GroupModel parentGroup = allDepMap.get(groupId);
                if (parentGroup != null)
                {
                    parentGroup.getChildrenContactList().add(contactModel);
                }

            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }

        return true;
    }

    @Override
    public boolean modifyContact(final ContactModel contactModel)
    {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        contactList.add(contactModel);
        return modifyContacts(contactList);
    }

    @Override
    public boolean modifyContacts(final ArrayList<ContactModel> contactList)
    {
        Log.info(TAG, "modifyContact::contactList:");
        if (contactList == null || contactList.size() == 0)
        {
            return false;
        }
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for (ContactModel mContactModel : contactList)
            {
                if(mContactModel == null)
                {
                    continue;
                }
                ContentValues value = new ContentValues();
                value.put(DB_Contact.NAME, mContactModel.getName());
                value.put(DB_Contact.TYPE_NAME, mContactModel.getTypeName());
                value.put(DB_Contact.NUMBER, mContactModel.getNumber());
                value.put(DB_Contact.SUBSCRIBE, mContactModel.getSubScribe());
                value.put(DB_Contact.CONF_NUM, mContactModel.getConfNum());
                String contactId = "" + mContactModel.getId();
                ops.add(ContentProviderOperation.newUpdate(DB_Contact.CONTENT_URI).withSelection(DB_Contact._ID + "=?", new String[] { contactId })
                        .withValues(value).withYieldAllowed(true).build());
                /**********************/
                // 删除联系人号码
                ops.add(ContentProviderOperation.newDelete(DB_Contact_Data.CONTENT_URI)
                        .withSelection(DB_Contact_Data.CONTACT_ID + "=?", new String[] { contactId }).build());
                // 再重新添加号码
                ArrayList<ContactNum> contactNumList = mContactModel.getPhoneNumList();
                for (ContactNum contactNum : contactNumList)
                {
                    ContentValues contactDataValue = new ContentValues();
                    contactDataValue.put(DB_Contact_Data.CONTACT_ID, mContactModel.getId());
                    contactDataValue.put(DB_Contact_Data.DATA_TYPE, contactNum.getTypeName());
                    contactDataValue.put(DB_Contact_Data.DATA, contactNum.getNumber());
                    ops.add(ContentProviderOperation.newInsert(DB_Contact_Data.CONTENT_URI).withValues(contactDataValue).build());
                }
                // 删除联系人组关系
                ops.add(ContentProviderOperation.newDelete(DB_Contact_Group.CONTENT_URI)
                        .withSelection(DB_Contact_Group.CONTACT_ID + "=?", new String[] { contactId }).build());
                // 重新添加联系人组关系
                ArrayList<ContactPosInGroup> posGroups = mContactModel.getParentGroupList();
                for (ContactPosInGroup posGroup : posGroups)
                {
                    int groupId = posGroup.getParentGroup().getId();
                    ContentValues contactDataValue = new ContentValues();
                    contactDataValue.put(DB_Contact_Group.CONTACT_ID, Integer.valueOf(contactId));
                    contactDataValue.put(DB_Contact_Group.GROUP_ID, groupId);
                    contactDataValue.put(DB_Contact_Group.CONTACT_POSITION, posGroup.getPosition());
                    ops.add(ContentProviderOperation.newInsert(DB_Contact_Group.CONTENT_URI).withValues(contactDataValue).build());
                }
            }
            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            // 更新本地数据
            for (ContactModel mContactModel : contactList)
            {
                if(mContactModel == null)
                {
                    continue;
                }
                ContactModel contact = contactMap.get(mContactModel.getId());
                if (contact == null)
                {
                    continue;
                }
                contact.setName(mContactModel.getName());
                contact.setTypeName(mContactModel.getTypeName());
                contact.setNumber(mContactModel.getNumber());
                contact.setSubScribe(mContactModel.getSubScribe());
                contact.setConfNum(mContactModel.getConfNum());
                if (contact != mContactModel)
                {
                    contact.getPhoneNumList().clear();
                    contact.getPhoneNumList().addAll(mContactModel.getPhoneNumList());
                    contact.clearParentGroups();
                    contact.addPosGroups(mContactModel.getParentGroupList());
                }
            }
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean removeContact(ContactModel contactModel)
    {
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        contactList.add(contactModel);
        return removeContacts(contactList);
    }

    @Override
    public boolean removeContacts(ArrayList<ContactModel> contactList)
    {
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (contactList == null || contactList.size() == 0)
        {
            return false;
        }
        try
        {
            for (ContactModel mContactModel : contactList)
            {
                if(mContactModel == null)
                {
                    continue;
                }
                int contactId = mContactModel.getId();
                // 删除联系人
                ops.add(ContentProviderOperation.newDelete(DB_Contact.CONTENT_URI).withSelection(DB_Contact._ID + "=?", new String[] { "" + contactId })
                        .build());
                // 删除 组联系人关系
                ops.add(ContentProviderOperation.newDelete(DB_Contact_Group.CONTENT_URI)
                        .withSelection(DB_Contact_Group.CONTACT_ID + "=?", new String[] { "" + contactId }).withYieldAllowed(true).build());
                // 删除关联 Data
                ops.add(ContentProviderOperation.newDelete(DB_Contact_Data.CONTENT_URI)
                        .withSelection(DB_Contact_Data.CONTACT_ID + "=?", new String[] { "" + contactId }).withYieldAllowed(true).build());
            }
            contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            for (ContactModel contactModel : contactList)
            {
                if(contactModel == null)
                {
                    continue;
                }
                int contactId = contactModel.getId();
                ContactModel contact = contactMap.get(contactId);
                ArrayList<ContactPosInGroup> parentGroups = contact.getParentGroupList();
                for (ContactPosInGroup posGroup : parentGroups)
                {
                    posGroup.getParentGroup().getChildrenContactList().remove(contact);
                }
                this.contactList.remove(contact);
                contactMap.remove(contactId);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    public boolean addDeps(ArrayList<GroupModel> groupList)
    {
        if (groupList == null || groupList.size() == 0)
        {
            return false;
        }
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for (GroupModel groupModel : groupList)
            {
                if(groupModel == null)
                {
                    continue;
                }
                ops.clear();
                ContentValues value = new ContentValues();
                value.put(DB_Group.GROUP_NAME, groupModel.getName());
                value.put(DB_Group.PARENT_ID, groupModel.getParentId());
                value.put(DB_Group.GROUP_TYPE, groupModel.getType());
                value.put(DB_Group.POSITION, groupModel.getPosition());
                value.put(DB_Group.GROUP_NUM, groupModel.getNumber());
                ops.add(ContentProviderOperation.newInsert(DB_Group.CONTENT_URI).withValues(value).withYieldAllowed(true).build());
                ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);

                int resultId = getId(results[0].toString());
                groupModel.setId(resultId);
                allDepMap.put(resultId, groupModel);
                allDepList.add(groupModel);
                ops.clear();
                ArrayList<ContactModel> selectContacts = (ArrayList<ContactModel>) groupModel.getChildrenContactList().clone();
                groupModel.getChildrenContactList().clear();
                for (ContactModel contactModel : selectContacts)
                {
                    if(contactModel == null)
                    {
                        continue;
                    }
                    ContactModel contact = contactMap.get(contactModel.getId());
                    if(contact == null)
                    {
                        continue;
                    }
                    groupModel.getChildrenContactList().add(contact);
                    ArrayList<ContactPosInGroup> parentGroupList = contact.getParentGroupList();
                    boolean isContain = false;
                    for (ContactPosInGroup posInGroup : parentGroupList)
                    {
                        if (posInGroup.getParentGroup() == groupModel)
                        {
                            isContain = true;
                            break;
                        }
                    }
                    ContactPosInGroup posGroup = null;
                    if (!isContain)
                    {
                        ContactPosInGroup pg = new ContactPosInGroup();
                        pg.setParentGroup(groupModel);
                        parentGroupList.add(pg);
                    }
                    else
                    {
                        posGroup = contact.getPosGroup(resultId);
                    }
                    ContentValues value1 = new ContentValues();
                    value1.put(DB_Contact_Group.CONTACT_ID, contact.getId());
                    value1.put(DB_Contact_Group.GROUP_ID, resultId);
                    value1.put(DB_Contact_Group.CONTACT_POSITION, posGroup == null ? 0 : posGroup.getPosition());
                    ops.add(ContentProviderOperation.newInsert(DB_Contact_Group.CONTENT_URI).withValues(value1).withYieldAllowed(true).build());
                }
                results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;

    };

    @Override
    public boolean addDep(GroupModel groupModel)
    {
        Log.info(TAG, "addGroup::groupModel:" + groupModel);
        if (groupModel == null)
        {
            return false;
        }
        ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
        groupList.add(groupModel);
        return addDeps(groupList);
    }

    @Override
    public boolean modifyDep(GroupModel groupModel)
    {
        if (groupModel == null)
        {
            return false;
        }
        ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
        groupList.add(groupModel);
        return modifyDeps(groupList);
    }

    @Override
    public boolean modifyDeps(final ArrayList<GroupModel> groupList)
    {
        Log.info(TAG, "modifyDeps::groupList");
        if (groupList == null || groupList.size() == 0)
        {
            return false;
        }
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            for (GroupModel mGroupModel : groupList)
            {
                if(mGroupModel == null)
                {
                    continue;
                }
                int groupId = mGroupModel.getId();
                String id = "" + groupId;
                ContentValues value = new ContentValues();
                value.put(DB_Group.GROUP_NUM, mGroupModel.getNumber());
                value.put(DB_Group.GROUP_NAME, mGroupModel.getName());
                value.put(DB_Group.GROUP_TYPE, mGroupModel.getType());
                value.put(DB_Group.PARENT_ID, mGroupModel.getParentId());
                value.put(DB_Group.POSITION, mGroupModel.getPosition());
                ops.add(ContentProviderOperation.newUpdate(DB_Group.CONTENT_URI).withSelection(DB_Group._ID + "=?", new String[] { id }).withValues(value)
                        .withYieldAllowed(true).build());
                // 删除掉 组与联系人的关系
                ops.add(ContentProviderOperation.newDelete(DB_Contact_Group.CONTENT_URI).withSelection(DB_Contact_Group.GROUP_ID + "=?", new String[] { id })
                        .withYieldAllowed(true).build());
                // 添加现有关系
                ArrayList<ContactModel> contactList = mGroupModel.getChildrenContactList();
                for (ContactModel contact : contactList)
                {
                    if(contact == null)
                    {
                        continue;
                    }
                    ContactPosInGroup posGroup = contact.getPosGroup(groupId);
                    ContentValues value1 = new ContentValues();
                    value1.put(DB_Contact_Group.GROUP_ID, groupId);
                    value1.put(DB_Contact_Group.CONTACT_ID, contact.getId());
                    value1.put(DB_Contact_Group.CONTACT_POSITION, posGroup == null ? 0 : posGroup.getPosition());
                    ops.add(ContentProviderOperation.newInsert(DB_Contact_Group.CONTENT_URI).withValues(value1).withYieldAllowed(true).build());
                }

            }

            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);

            // 更新内存数据
            for (GroupModel mGroupModel : groupList)
            {
                if(mGroupModel == null)
                {
                    continue;
                }
                GroupModel group = allDepMap.get(mGroupModel.getId());
                if (group != null)
                {
                    group.setNumber(mGroupModel.getNumber());
                    group.setName(mGroupModel.getName());
                    group.setType(mGroupModel.getType());
                    group.setParentId(mGroupModel.getParentId());
                    group.setPosition(mGroupModel.getPosition());
                    if (group != mGroupModel)
                    {
                        group.getChildrenContactList().clear();
                        group.getChildrenContactList().addAll(mGroupModel.getChildrenContactList());
                    }
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    @Override
    public boolean removeDep(GroupModel groupModel)
    {
        ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
        groupList.add(groupModel);
        return removeDeps(groupList);
    }

    @Override
    public boolean removeDeps(ArrayList<GroupModel> groupModels)
    {
        Log.info(TAG, "removeDeps::groupModels:");
        if (groupModels == null || groupModels.size() == 0)
        {
            return false;
        }
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            ArrayList<GroupModel> allGroups = new ArrayList<GroupModel>();

            for (GroupModel groupModel : groupModels)
            {
                if(groupModel == null)
                {
                    continue;
                }
                // 获取所有子组
                allGroups.add(groupModel);
                allGroups.addAll(getAllDepFromDep(groupModel));
            }

            // 删除组, 组和联系人关系
            for (GroupModel group : allGroups)
            {
                String groupId = "" + group.getId();
                // 删除组
                ops.add(ContentProviderOperation.newDelete(DB_Group.CONTENT_URI).withSelection(DB_Group._ID + "=?", new String[] { groupId }).build());
                // 删除组 联系人 关系
                ops.add(ContentProviderOperation.newDelete(DB_Contact_Group.CONTENT_URI)
                        .withSelection(DB_Contact_Group.GROUP_ID + "=?", new String[] { groupId }).build());
            }
            contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            // 更新本地内存数据
            for (GroupModel group : allGroups)
            {
                int parentId = group.getParentId();
                int id = group.getId();
                if (allDepMap.containsKey(id))
                {
                    GroupModel g = allDepMap.get(id);
                    allDepList.remove(g);
                    allDepMap.remove(id);

                }
                GroupModel parentGroup = allDepMap.get(parentId);
                if (parentGroup != null)
                {
                    if (parentGroup.getChildrenDepList().contains(group))
                    {
                        parentGroup.getChildrenDepList().remove(group);
                    }
                }
                ArrayList<ContactModel> contactList = group.getChildrenContactList();
                for (ContactModel contact : contactList)
                {
                    ContactPosInGroup posGroup = contact.getPosGroup(group.getId());
                    contact.getParentGroupList().remove(posGroup);
                }
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    }

    /**
     * 方法说明 : 获取一个组下所有联系人（包括子组联系人）
     * 
     * @author HeZhen
     * @Date 2015-5-6
     */
    public void getContactAllChildren(GroupModel mGroupModel, ArrayList<ContactModel> contactList)
    {
        contactList.addAll(mGroupModel.getChildrenContactList());
        for (GroupModel group : mGroupModel.getChildrenDepList())
        {// 遍历
            getContactAllChildren(group, contactList);// 递归子组
        }
    }

    /**
     * 方法说明 : 获取一个组下所有子组
     * 
     * @author HeZhen
     * @Date 2015-5-6
     */
    private void getGroupAllChildrenDepList(GroupModel mGroupModel, ArrayList<GroupModel> groupList)
    {
        groupList.addAll(mGroupModel.getChildrenDepList());
        for (GroupModel group : mGroupModel.getChildrenDepList())
        {// 遍历
            getGroupAllChildrenDepList(group, groupList);// 递归子组
        }
    }

    /**
     * 方法说明 : 加载组数据
     * @author HeZhen
     * @Date 2015-5-13
     */
    public void loadDeps()
    {
        Log.info(TAG, "loadDeps...");

        allDepMap.clear();
        allDepList.clear();

        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        int num = 0;
        Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_GROUP, null);
        // 大组 左侧显示
        while (cursor.moveToNext())
        {
            num = cursor.getInt(cursor.getColumnIndex(DB_Group.GROUP_NUM));
            GroupModel group = new GroupModel();
            group.setId(cursor.getInt(cursor.getColumnIndex(DB_Group._ID)));
            group.setName(cursor.getString(cursor.getColumnIndex(DB_Group.GROUP_NAME)));
            group.setParentId(cursor.getInt(cursor.getColumnIndex(DB_Group.PARENT_ID)));
            group.setType(cursor.getInt(cursor.getColumnIndex(DB_Group.GROUP_TYPE)));
            group.setPosition(cursor.getInt(cursor.getColumnIndex(DB_Group.POSITION)));
            group.setNumber(num);
            allDepMap.put(group.getId(), group);
            allDepList.add(group);
        }
        cursor.close();
    }
    /**
     * 方法说明 :加载数据类型 DB数据
     * @author hz
     * @Date 2015-9-28
     */
    private void loadDataType()
    {
        Log.info(TAG, "loadDataType...");
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        // 读取联系人
        synchronized (dbHelper)
        {
            dataTypeList.clear();
            DataType dataType = null;
            int id;
            int dataIdent;
            String typeName;
            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_DATA_TYPE, null);
            try
            {
                while (cursor.moveToNext())
                {
                    dataType = new DataType();

                    id = cursor.getInt(cursor.getColumnIndex(DB_DataType._ID));
                    dataIdent = cursor.getInt(cursor.getColumnIndex(DB_DataType.DATA_IDENT));
                    typeName = cursor.getString(cursor.getColumnIndex(DB_DataType.TYPE_NAME));
                    dataType.setId(id);
                    dataType.setDataIdent(dataIdent);
                    dataType.setTypeName(typeName);
                    dataTypeList.add(dataType);
                }
            }
            catch (Exception e)
            {
                Log.info(TAG, e.toString());
            }
            finally
            {
                if (cursor != null)
                    cursor.close();
            }
        }
    
    }
    /**
     * 方法说明 :加载联系人表数据
     * @author HeZhen
     * @Date 2015-5-13
     */
    public void loadContacts()
    {
        Log.info(TAG, "loadContacts...");
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        // 读取联系人
        synchronized (dbHelper)
        {
            contactMap.clear();
            contactList.clear();
            ContactModel contactModel;
            int id;
            int num;
            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_CUSTOM_CONTACT, null);
            try
            {
                while (cursor.moveToNext())
                {
                    contactModel = new ContactModel();

                    id = cursor.getInt(cursor.getColumnIndex(DB_Contact._ID));
                    num = cursor.getInt(cursor.getColumnIndex(DB_Contact.NUMBER));
                    contactModel.setId(id);
                    contactModel.setNumber(num);
                    contactModel.setName(cursor.getString(cursor.getColumnIndex(DB_Contact.NAME)));
                    contactModel.setTypeName(cursor.getString(cursor.getColumnIndex(DB_Contact.TYPE_NAME)));

                    contactModel.setSubScribe(cursor.getInt(cursor.getColumnIndex(DB_Contact.SUBSCRIBE)));
                    contactModel.setConfNum(cursor.getString(cursor.getColumnIndex(DB_Contact.CONF_NUM)));
                    contactList.add(contactModel);
                    contactMap.put(id, contactModel);
                }
                cursor.close();
                // 读取联系人数据表
                for (ContactModel contact : contactList)
                {
                    ArrayList<ContactNum> phoneNumList = new ArrayList<ContactNum>();
                    int contactId = contact.getId();
                    cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_CONTACT_DATA + " where " + DB_Contact_Data.CONTACT_ID + "=" + contactId,
                            null);
                    String dataType;
                    String data;
                    while (cursor.moveToNext())
                    {
                        ContactNum contactNum = new ContactNum();
                        id = cursor.getInt(cursor.getColumnIndex(DB_Contact_Data._ID));
                        dataType = cursor.getString(cursor.getColumnIndex(DB_Contact_Data.DATA_TYPE));
                        data = cursor.getString(cursor.getColumnIndex(DB_Contact_Data.DATA));
                        contactNum.setId(id);
                        contactNum.setTypeName("" + dataType);
                        contactNum.setNumber(data);
                        phoneNumList.add(contactNum);
                    }
                    contact.setPhoneNumList(phoneNumList);
                    cursor.close();
                }
            }
            catch (Exception e)
            {
            }
            finally
            {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    /**
     * 方法说明 : 加载本地联系人 组 关系数据
     * @author HeZhen
     * @Date 2015-5-13
     */
    public void loadContactDep()
    {
        Log.info(TAG, "loadContactDep");
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        synchronized (dbHelper)
        {
            for (ContactModel contactModel : contactList)
            {
                contactModel.clearParentGroups();
            }
            for (GroupModel groupModel : allDepList)
            {
                groupModel.getChildrenContactList().clear();
            }
            //
            int groupId;
            int contactId;
            int position;
            ContactModel contactModel;
            GroupModel groupModel;

            Cursor cursor = db.rawQuery("select * from " + DBConstantValues.TB_NAME_CONTACT_GROUP, null);
            while (cursor.moveToNext())
            {
                groupId = cursor.getInt(cursor.getColumnIndex(DB_Contact_Group.GROUP_ID));
                contactId = cursor.getInt(cursor.getColumnIndex(DB_Contact_Group.CONTACT_ID));
                position = cursor.getInt(cursor.getColumnIndex(DB_Contact_Group.CONTACT_POSITION));
                contactModel = contactMap.get(contactId);
                groupModel = allDepMap.get(groupId);
                if (contactModel != null && groupModel != null)
                {
                    groupModel.getChildrenContactList().add(contactModel);
                    ContactPosInGroup posInGroup = new ContactPosInGroup();
                    posInGroup.setParentGroup(groupModel);
                    posInGroup.setPosition(position);
                    contactModel.addPosGroup(posInGroup);
                }
            }
            cursor.close();
        }
    }

    public void loadData()
    {
        try
        {
            loadDataType();
            loadContacts();
            loadDeps();
            loadContactDep();
        }
        catch (Exception e)
        {
            Log.info(TAG, e.toString());
            // 加载数据失败后
            clearAllContacts();
        }

    }
    
    private int getId(String result)
    {
        return Integer.valueOf(result.substring(result.lastIndexOf("/") + 1, result.length() - 1).trim());
    }

    @Override
    public boolean clearAllContacts()
    {
        Log.info(TAG, "clearAll");
        try
        {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            // 删除掉按键中所有联系人
            ops.add(ContentProviderOperation.newDelete(DB_Contact.CONTENT_URI).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation.newDelete(DB_Group.CONTENT_URI).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation.newDelete(DB_Contact_Group.CONTENT_URI).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation.newDelete(DB_Contact_Data.CONTENT_URI).withYieldAllowed(true).build());
            ops.add(ContentProviderOperation.newDelete(DB_DataType.CONTENT_URI).withYieldAllowed(true).build());

            ContentProviderResult[] results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
            dataTypeList.clear();
            allDepList.clear();
            allDepMap.clear();
            contactList.clear();
            contactMap.clear();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (OperationApplicationException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<ContactModel> getAllContactFromDep(GroupModel groupModel)
    { // 按键组的话，获得的联系人可能会有重复数据，需要注意
        ArrayList<ContactModel> allContacts = new ArrayList<ContactModel>();
        getContactAllChildren(groupModel, allContacts);
        return allContacts;
    }

    @Override
    public ArrayList<GroupModel> getAllDepFromDep(GroupModel groupModel)
    {
        ArrayList<GroupModel> allDeps = new ArrayList<GroupModel>();
        getGroupAllChildrenDepList(groupModel, allDeps);
        return allDeps;
    }

    @Override
    public List<DataType> getDataTypeList()
    {
        return dataTypeList;
    }

    @Override
    public boolean addDataTypes(ArrayList<DataType> dataTypeList)
    {
        Log.info(TAG, "addDataTypes");
        if (dataTypeList == null || dataTypeList.size() == 0)
        {
            return false;
        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        try
        {
            for (DataType dataType : dataTypeList)
            {
                ops.clear();
                ContentValues value = new ContentValues();
                value.put(DB_DataType.DATA_IDENT, dataType.getDataIdent());
                value.put(DB_DataType.TYPE_NAME, dataType.getTypeName());
                ops.add(ContentProviderOperation.newInsert(DB_DataType.CONTENT_URI).withValues(value).withYieldAllowed(true).build());
                ContentProviderResult[] results = null;
                results = contentResolver.applyBatch(DBConstantValues.AUTHORITY, ops);
                
                int resultId;
                if (results != null && results.length > 0)
                {
                    resultId = getId(results[0].toString());
                }
                else
                {
                    return false;
                }
                dataType.setId(resultId);
                this.dataTypeList.add(dataType);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
            return false;
        }
        return true;
    
    }
}
