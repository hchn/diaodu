package com.jiaxun.uil.module.contact.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.module.DclServiceFactory;
import com.jiaxun.sdk.dcl.module.contact.itf.DclContactService;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.module.contact.ImportExportContacts;
import com.jiaxun.uil.module.contact.ImportExportKeys;
import com.jiaxun.uil.module.contact.itf.UilContactService;
import com.jiaxun.uil.util.DispatchQueue;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：联系人业务功能接口
 * 
 * @author hezhen
 * 
 * @Date 2015-1-16
 */
public class UilContactServiceImpl implements UilContactService
{
    private static final String TAG = UilContactServiceImpl.class.getSimpleName();
    private static DclContactService sclContactService;
    private static UilContactServiceImpl instance;

    public DispatchQueue storageQueue = new DispatchQueue("storageQueue");

    private ArrayList<GroupModel> keyList;
    private ArrayList<GroupModel> keyGroupRootList;
    private ArrayList<GroupModel> groupRootList;

    private ArrayList<ContactModel> subscribeContactList;

    private ArrayList<String> subscribeContactPhoneNums;

    // 通过号码获得联系人
    private Map<String, ContactModel> contactByPhoneNum;
    // ==================================================
    // ---------------------start---------------------
    // 通过编号获得联系人。只用于导入导出
    private Map<String, ContactModel> contactByNumMap;
    // 也只用在了导入导出，是否可去掉，待整理
    private ArrayList<GroupModel> keyGroupList;
    private ImportExportContacts importExportContacts;
    private ImportExportKeys importExportKeys;

    // ---------------------end---------------------
    // =================================================
    //用户类型
    private ArrayList<String> contactTypeList;
    //调度监控号码类型
    private ArrayList<String> vdPhoneTypeList;
    //号码类型
    private ArrayList<String> phoneTypeList;

    public UilContactServiceImpl(Context mContext)
    {
        sclContactService = DclServiceFactory.getDclContactService();
        subscribeContactList = new ArrayList<ContactModel>();
        subscribeContactPhoneNums = new ArrayList<String>();
        keyList = new ArrayList<GroupModel>();
        keyGroupList = new ArrayList<GroupModel>();
        keyGroupRootList = new ArrayList<GroupModel>();
        groupRootList = new ArrayList<GroupModel>();
        contactByNumMap = new HashMap<String, ContactModel>();
        contactByPhoneNum = new HashMap<String, ContactModel>();
        importExportContacts = new ImportExportContacts();
        importExportKeys = new ImportExportKeys();
        contactTypeList = new ArrayList<String>();
        phoneTypeList = new ArrayList<String>();
        vdPhoneTypeList = new ArrayList<String>();
        
        refreshData();
    }

    private void refreshData()
    {
        loadDataType();
        loadContactData();
        loadDepData();
        loadDepRootData();
    }
    private void loadDataType()
    {
        contactTypeList.clear();
        phoneTypeList.clear();
        vdPhoneTypeList.clear();
        String[] contactTypeArray = UiApplication.getInstance().getResources().getStringArray(R.array.contact_categary);
        List<String> ctList = new ArrayList<String>(Arrays.asList(contactTypeArray));  
        contactTypeList.addAll(ctList);
        
        String[] vdPhoneTypeArray = UiApplication.getInstance().getResources().getStringArray(R.array.vd_phone_categary);
        List<String> vdPtList = new ArrayList<String>(Arrays.asList(vdPhoneTypeArray));  
        vdPhoneTypeList.addAll(vdPtList);
        
        String[] phoneTypeArray = UiApplication.getInstance().getResources().getStringArray(R.array.phone_categary);
        List<String> ptList = new ArrayList<String>(Arrays.asList(phoneTypeArray));  
        phoneTypeList.addAll(ptList);
        
        
        
        for(DataType dataType : sclContactService.getDataTypeList())
        {
            String typeName = dataType.getTypeName();
            if(dataType.getDataIdent() == DataType.CONTACT_IDENT)
            {
                if(!contactTypeList.contains(typeName))
                {
                    contactTypeList.add(typeName);
                }
            }else if(dataType.getDataIdent() == DataType.PHONE_IDENT)
            {
                if(!phoneTypeList.contains(typeName))
                {
                    phoneTypeList.add(typeName);
                }
            }
        }
    }
    private void loadContactData()
    {
        subscribeContactList.clear();
        contactByPhoneNum.clear();
        contactByNumMap.clear();
        subscribeContactPhoneNums.clear();
        ContactUtil.contactMaxNum = 0;
        List<ContactModel> contactModelList = sclContactService.getContactList();
        ArrayList<ContactNum> numList = null;
        for (ContactModel contactModel : contactModelList)
        {
            // 订阅用户
            if (contactModel.getSubScribe() == 1)
            {
                subscribeContactList.add(contactModel);
            }
            numList = contactModel.getPhoneNumList();
            if (numList == null || numList.size() == 0)
            {
                continue;
            }
            // 号码 联系人
            ContactNum contactNum = numList.get(0);
            if (contactNum != null)
            {
                contactByPhoneNum.put(contactNum.getNumber(), contactModel);
            }
            String num = "" + contactModel.getNumber();
            if (!TextUtils.isEmpty(num))
            {
                contactByNumMap.put(num, contactModel);
            }
            if (ContactUtil.contactMaxNum < contactModel.getNumber())
            {
                ContactUtil.contactMaxNum = contactModel.getNumber();
            }
        }
        for (ContactModel contactModel : subscribeContactList)
        {
            numList = contactModel.getPhoneNumList();
            if(numList == null || numList.size() == 0)
            {
                continue;
            }
            ContactNum contactNum = contactModel.getPhoneNumList().get(0);
            if (contactNum != null)
            {
                String phoneNum = contactNum.getNumber();
                if (!subscribeContactList.contains(phoneNum))
                {
                    subscribeContactPhoneNums.add(contactNum.getNumber());
                }
            }
        }
    }

    public static UilContactServiceImpl getInstance(Context mContext)
    {
        if (instance == null)
        {
            instance = new UilContactServiceImpl(mContext);
        }
        return instance;
    }

    @Override
    public ArrayList<ContactModel> getContactList()
    {
        ArrayList<ContactModel> list= new ArrayList<ContactModel>();
        list.addAll(sclContactService.getContactList());
        return list;
//        return new ArrayList<ContactModel>(Arrays.asList(sclContactService.getContactList().toArray(new ContactModel[sclContactService.getContactList().size()])));
    }

    @Override
    public ArrayList<ContactModel> getSubscribeContactList()
    {
        return subscribeContactList;
    }

    @Override
    public ContactModel getContactById(int id)
    {
        return sclContactService.getContactById(id);
    }

    @Override
    public ContactModel getContactByPhoneNum(String phoneNum)
    {
        return contactByPhoneNum.get(phoneNum);
    }

    @Override
    public ArrayList<String> getSubscribePhoneNums()
    {
        return subscribeContactPhoneNums;
    }

    @Override
    public GroupModel getDepById(int id)
    {
        return sclContactService.getDepById(id);
    }

    @Override
    public ArrayList<GroupModel> getKeyGroupRootList()
    {
        return keyGroupRootList;
    }

    @Override
    public ArrayList<GroupModel> getGroupRootList()
    {
        return groupRootList;
    }

    @Override
    public ArrayList<GroupModel> getKeyList()
    {
        return keyList;
    }

    @Override
    public void addContact(final ContactModel contactModel)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.addContact(contactModel))
                {
                    loadContactData();
                    // 用户最大编号
                    if (ContactUtil.contactMaxNum < contactModel.getNumber())
                    {
                        ContactUtil.contactMaxNum = contactModel.getNumber();
                    }
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_ADD_NEW_CONTACT);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public void addContacts(ArrayList<ContactModel> contactModels)
    {

    }

    @Override
    public void modifyContact(final ContactModel contactModel , final ArrayList<GroupModel> parentGroups)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.modifyContact(contactModel))
                {
                    sclContactService.modifyDeps(parentGroups);
                    refreshData();
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_MODIFY_CONTACT);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public void modifyContacts(final ArrayList<ContactModel> contactList)
    {

        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.modifyContacts(contactList))
                {
                    refreshData();
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_MODIFY_CONTACT);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });

    }

    @Override
    public void removeContact(ContactModel contactModel)
    {

    }

    @Override
    public void removeContacts(ArrayList<ContactModel> contactList)
    {

    }

    private void loadDepRootData()
    {
        groupRootList.clear();
        keyGroupRootList.clear();
        List<GroupModel> depRootList = sclContactService.getDepRootList();
        for (GroupModel groupModel : depRootList)
        {
            if (groupModel.getType() == EnumGroupType.CONTACT_GROUP)
            {
                groupRootList.add(groupModel);
            }
            else if (groupModel.getType() == EnumGroupType.KEY_GROUP)
            {
                keyGroupRootList.add(groupModel);
            }
        }
    }

    private void loadDepData()
    {
        keyGroupList.clear();
        keyList.clear();
        List<GroupModel> allDeps = sclContactService.getDepList();
        for (GroupModel groupModel : allDeps)
        {
            if (groupModel.getType() == EnumGroupType.KEY_GROUP)
            {
                keyGroupList.add(groupModel);
            }
            else if (groupModel.getType() == EnumGroupType.KEY)
            {
                keyList.add(groupModel);
            }

            int parentId = groupModel.getParentId();
            GroupModel parentGroup = sclContactService.getDepById(parentId);
            if (parentGroup != null)
            {
                if (!parentGroup.getChildrenDepList().contains(groupModel))
                {
                    parentGroup.getChildrenDepList().add(groupModel);
                }
                groupModel.setParentId(parentId);
                groupModel.setParent(parentGroup);
            }
        }
    }

    @Override
    public void addDep(final GroupModel groupModel)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.addDep(groupModel))
                {
                    refreshData();
                    if (groupModel.getType() == EnumGroupType.KEY)
                    {
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_KEY_ADDED);
                    }
                    else
                    {
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_ADD_NEW_GROUP);
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                    }
                }
            }
        });
    }

    @Override
    public void addDeps(final ArrayList<GroupModel> groupList)
    {
        if (groupList == null || groupList.size() == 0)
        {
            return;
        }
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.addDeps(groupList))
                {
                    refreshData();
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_ADD_NEW_GROUP);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public void modifyDep(final GroupModel groupModel)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.modifyDep(groupModel))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_MODIFY_GROUP);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public void modifyDeps(final ArrayList<GroupModel> groupList)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.modifyDeps(groupList))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public void removeDep(final GroupModel groupModel)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                ArrayList<ContactModel> allContacts = new ArrayList<ContactModel>();
                allContacts = sclContactService.getAllContactFromDep(groupModel);
                if (sclContactService.removeDep(groupModel))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
                if (sclContactService.removeContacts(allContacts))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public void removeDepAndKeys(final ArrayList<GroupModel> groupModels)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (sclContactService.removeDeps(groupModels))
                {
                    refreshData();
//                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.SETTING_KEY_REMOVE);
                }
            }
        });
    }

    @Override
    public void clearAllContacts()
    {

    }

    @Override
    public void clearAllData()
    {

    }

    @Override
    public void importContacts(final String url)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                Log.info(TAG, "..读取联系人文件");
                if (importExportContacts.readFile(url) == 0)
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_IMPORT_OVER, 0);
                    ToastUtil.showToast("请确认导入的文件格式正确！");
                    return;
                }
                Log.info(TAG, "..删除所有联系人");
                sclContactService.clearAllContacts();
                //把数据类型添加到DB
                sclContactService.addDataTypes(importExportContacts.getDataTypeList());
                refreshData();
                
                // 插入组 无parentID
                Map<String, GroupModel> groupMap = importExportContacts.getGroupMap();
                ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
                for (Map.Entry<String, GroupModel> groupEntry : groupMap.entrySet())
                {
                    GroupModel group = groupEntry.getValue();
                    group.setType(EnumGroupType.CONTACT_GROUP);
                    groupList.add(group);
                }
                if (!sclContactService.addDeps(groupList))
                {
                    return;
                }
                refreshData();
                Log.info(TAG, "..添加组");
                // 组关系是通过组名称来体现的，如： 父组_子组_子子组..
                // 整理组关系
                for (GroupModel groupModel : groupList)
                {
                    String groupName = groupModel.getName();
                    String parentGroupName = null;
                    int lastIndex = groupName.lastIndexOf("_");
                    if (lastIndex > 0)
                    {
                        parentGroupName = groupName.substring(0, lastIndex);
                    }
                    if (TextUtils.isEmpty(parentGroupName))
                    {
                        groupModel.setParentId(GroupModel.DEFAULT_PARENT_ID);
                    }
                    else
                    {
                        int id = groupMap.get(parentGroupName).getId();
                        groupModel.setParentId(id);
                    }
                }
                // 提取组名字
                for (GroupModel groupModel : groupList)
                {
                    String groupName = groupModel.getName();
                    int lastIndex = groupName.lastIndexOf("_");
                    String gName = null;
                    if (lastIndex > 0)
                    {
                        gName = groupName.substring(lastIndex + 1);
                    }
                    if (!TextUtils.isEmpty(gName))
                    {
                        groupModel.setName(gName);
                    }
                }
                // 将整理出的数据更新到DB
                sclContactService.modifyDeps(groupList);
                // 重新加载组数据
                refreshData();
                Log.info(TAG, "..更新组关系");
                // 整理好组关系，组的位置都还是0，初始化组位置
                // 导入组 是没有位置的，需要手动设置，并更新保存到DB
                int rootPos = 0;
                for (GroupModel groupModel : groupList)
                {
                    ArrayList<GroupModel> groups = groupModel.getChildrenDepList();
                    int position = 0;
                    for (GroupModel childgroup : groups)
                    {
                        childgroup.setPosition(position);
                        position++;
                    }
                    if (groupModel.getId() == GroupModel.DEFAULT_PARENT_ID)
                    {
                        groupModel.setPosition(rootPos);
                        rootPos++;
                    }
                }
                sclContactService.modifyDeps(groupList);
                // 重新加载组数据
                refreshData();
                Log.info(TAG, "..更新组位置");
                // 整理出联系人 此时是没有ID的，插入DB中
                ArrayList<ContactModel> contactModels = importExportContacts.getContactList();
                sclContactService.addContacts(contactModels);
                // 读取本地联系人，
                refreshData();
                Log.info(TAG, "..添加联系人");
                Map<Integer, ContactModel> contactNumMap = importExportContacts.getContactNumMap();
                // 通过读取本地联系人获得联系人ID，设置导入读取的联系人ID
                // 把号码与联系人的关系理清。号码加入DB
                for (ContactModel contactModel : contactModels)
                {
                    ContactModel contact = contactNumMap.get(contactModel.getNumber());
                    contactModel.setId(contact.getId());
                    int contactNumber = Integer.valueOf(contactModel.getNumber());
                    if (ContactUtil.contactMaxNum < contactNumber)
                    {
                        ContactUtil.contactMaxNum = contactNumber;
                    }
                }
                sclContactService.modifyContacts(contactModels);
                refreshData();
                Log.info(TAG, "..更新联系人");
                Log.info(TAG, "..导入结束");
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_IMPORT_OVER, 1);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
            }
        });
    }

    @Override
    public void exportContacts(final String pathName, final String fileName)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (importExportContacts.exportToCSV(pathName, fileName))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_EXPORT_OVER, 1);
                }
                else
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_EXPORT_OVER, 0);
                }
            }
        });
    }

    private ArrayList<GroupModel> getKeyGroups()
    {
        List<GroupModel> allDeps = sclContactService.getDepList();
        ArrayList<GroupModel> allKeyGroups = new ArrayList<GroupModel>();
        for (GroupModel groupModel : allDeps)
        {
            if (groupModel.getType() == EnumGroupType.KEY_GROUP)
            {
                allKeyGroups.add(groupModel);
            }
        }
        return allKeyGroups;
    }

    @Override
    public void importKeys(final String url)
    {

        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (importExportKeys.readFile(url) == 0)
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_IMPORT_OVER, 0);
                    ToastUtil.showToast("请确认导入的文件格式正确！");
                    return;
                }
                // 导入之前删除目前所有按键和按键组
                ArrayList<GroupModel> keyAndKeyGroupList = new ArrayList<GroupModel>();
                keyAndKeyGroupList.addAll(keyGroupList);
                keyAndKeyGroupList.addAll(getKeyList());

                sclContactService.removeDeps(keyAndKeyGroupList);
                // 重新加载组数据
                refreshData();

                Map<String, GroupModel> keyGroupMap = importExportKeys.getKeyGroupMap();
                ArrayList<GroupModel> paramKeyGroupList = new ArrayList<GroupModel>();
                for (Map.Entry<String, GroupModel> groupEntry : keyGroupMap.entrySet())
                {
                    GroupModel groupModel = groupEntry.getValue();
                    groupModel.setType(EnumGroupType.KEY_GROUP);
                    paramKeyGroupList.add(groupModel);
                }
                if (!sclContactService.addDeps(paramKeyGroupList))
                {
                    return;
                }
                // 读取组数据
                refreshData();
                // 根据带有组关系的名称整理出组关系
                for (GroupModel groupModel : paramKeyGroupList)
                {
                    String groupName = groupModel.getName();
                    String parentGroupName = null;
                    int lastIndex = groupName.lastIndexOf("_");
                    if (lastIndex > 0)
                    {
                        parentGroupName = groupName.substring(0, lastIndex);
                    }
                    if (TextUtils.isEmpty(parentGroupName))
                    {
                        groupModel.setParentId(GroupModel.DEFAULT_PARENT_ID);
                    }
                    else
                    {
                        int id = keyGroupMap.get(parentGroupName).getId();
                        groupModel.setParentId(id);
                    }
                }
                // 添加按键 按键与组的关系
                ArrayList<GroupModel> keysList = importExportKeys.getKeyList();
                loadContactData();
                for (GroupModel groupModel : keysList)
                {
                    ArrayList<ContactModel> contactList = groupModel.getChildrenContactList();
                    for (ContactModel contactModel : contactList)
                    {
                        ContactModel contact = contactByNumMap.get("" + contactModel.getNumber());
                        if (contact != null)
                            contactModel.setId(contact.getId());
                    }
                    String groupName = groupModel.getParent().getName();
                    int id = keyGroupMap.get(groupName).getId();
                    groupModel.setParentId(id);
                    groupModel.setType(EnumGroupType.KEY);
                }
                sclContactService.addDeps(keysList);
                refreshData();
                // 去掉关系提取组名称
                for (GroupModel groupModel : paramKeyGroupList)
                {
                    String groupName = groupModel.getName();
                    int lastIndex = groupName.lastIndexOf("_");
                    String gName = null;
                    if (lastIndex > 0)
                    {
                        gName = groupName.substring(lastIndex + 1);
                    }
                    if (!TextUtils.isEmpty(gName))
                    {
                        groupModel.setName(gName);
                    }
                }
                // 将整理出的数据更新到DB
                sclContactService.modifyDeps(paramKeyGroupList);
                // 重新加载组数据
                refreshData();
                // 按键在导入文档中设定了位置，无需代码设置
//                for (GroupModel groupModel : keyGroupList)
//                {
//                    GroupModel group = getDepById(groupModel.getId());
//                    if (group != null)
//                    {
//                        ArrayList<GroupModel> groups = group.getChildrenDepList();
//                        int position = 0;
//                        for (GroupModel childgroup : groups)
//                        {
//                            childgroup.setPosition(position);
//                            position++;
//                        }
//                    }
//                }
                int position = 0;
                ArrayList<GroupModel> groupRootList = getKeyGroupRootList();
                for (GroupModel group : groupRootList)
                {
                    group.setPosition(position);
                    position++;
                }
                sclContactService.modifyDeps(keyGroupList);
                // 重新加载组数据
                refreshData();
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_IMPORT_OVER, 1);
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
            }
        });

    }

    @Override
    public void exportKeys(final String pathName, final String fileName)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                if (importExportKeys.exportToCSV(pathName, fileName))
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_EXPORT_OVER, 1);
                }
                else
                {
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.FILE_EXPORT_OVER, 0);
                }
            }
        });

    }

    // 修改位置
    @Override
    public void modifyContactAndDep(final ArrayList<ContactModel> contactModels, final ArrayList<GroupModel> groupModels)
    {
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                sclContactService.modifyContacts(contactModels);
                sclContactService.modifyDeps(groupModels);
                refreshData();
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
            }
        });
    }

    @Override
    public void removeDepAndContacts(final ArrayList<Integer> depList, final ArrayList<Integer> contactList)
    {
        Log.info(TAG, "depList == " + depList + ",contactList == " + contactList);
        storageQueue.postRunnable(new Runnable()
        {
            @Override
            public void run()
            {
                ArrayList<GroupModel> groups = new ArrayList<GroupModel>();
                ArrayList<ContactModel> contacts = new ArrayList<ContactModel>();
                for (Integer depId : depList)
                {
                    GroupModel group = getDepById(depId);
                    if (group != null)
                    {
                        groups.add(group);
                        groups.addAll(sclContactService.getAllDepFromDep(group));
                    }
                }
                for (Integer contactId : contactList)
                {
                    ContactModel contact = getContactById(contactId);
                    if (contact != null)
                    {
                        contacts.add(contact);
                    }
                }
                for(GroupModel groupModel : groups)
                {
                    contacts.addAll(groupModel.getChildrenContactList());
                }

                boolean refresh = false;
                if (sclContactService.removeDeps(groups))
                {
                    refresh = true;
                }
                if (sclContactService.removeContacts(contacts))
                {
                    refresh = true;
                }
                if (refresh)
                {
                    refreshData();
                    EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);
                }
            }
        });
    }

    @Override
    public ArrayList<String> getContactTypes()
    {   
        return contactTypeList;
    }

    @Override
    public ArrayList<String> getPhoneTypes()
    {
        return phoneTypeList;
    }
    @Override
    public ArrayList<String> getVDPhoneTypes()
    {
        return vdPhoneTypeList;
    }
}
