package com.jiaxun.uil.module.contact.itf;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;

public interface UilContactService
{
    /**
     * 方法说明 :获取联系人列表
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ArrayList<ContactModel> getContactList();

    /**
     * 方法说明 :获取订阅用户
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ArrayList<ContactModel> getSubscribeContactList();

    /**
     * 方法说明 :通过ID获取用户
     * @param id 用户ID
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ContactModel getContactById(int id);

    /**
     * 方法说明 :通过手机号码获得联系人
     * @param phoneNum
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ContactModel getContactByPhoneNum(String phoneNum);

    /**
     * 方法说明 :
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ArrayList<String> getSubscribePhoneNums();

    /**
     * 方法说明 :通过Id获得部门组
     * @param id
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    GroupModel getDepById(int id);

    /**
     * 方法说明 :获得按键根组列表
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ArrayList<GroupModel> getKeyGroupRootList();

    /**
     * 方法说明 :获得通讯录根组列表
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ArrayList<GroupModel> getGroupRootList();

    /**
     * 方法说明 :获得按键列表
     * @return
     * @author HeZhen
     * @Date 2015-8-25
     */
    ArrayList<GroupModel> getKeyList();

    /**
     * 方法说明 :添加单个联系人
     * @param contactModel
     * @author HeZhen
     * @Date 2015-8-25
     */
    void addContact(ContactModel contactModel);

    /**
     * 方法说明 :批量添加联系人
     * @param contactModels
     * @author HeZhen
     * @Date 2015-8-25
     */
    void addContacts(ArrayList<ContactModel> contactModels);

    /**
     * 方法说明 :修改单个联系人
     * @param contactModel
     * @author HeZhen
     * @Date 2015-8-25
     */
    void modifyContact(ContactModel contactModel,ArrayList<GroupModel> parentGroups);

    /**
     * 方法说明 :批量修改联系人
     * @param contactList
     * @author HeZhen
     * @Date 2015-8-25
     */
    void modifyContacts(ArrayList<ContactModel> contactList);

    /**
     * 方法说明 :删除单个联系人
     * @param contactModel
     * @author HeZhen
     * @Date 2015-8-25
     */
    void removeContact(ContactModel contactModel);

    /**
     * 方法说明 :批量删除联系人
     * @param contactList
     * @author HeZhen
     * @Date 2015-8-25
     */
    void removeContacts(ArrayList<ContactModel> contactList);

    /**
     * 方法说明 :添加单个部门组
     * @param groupModel
     * @author HeZhen
     * @Date 2015-8-25
     */
    void addDep(GroupModel groupModel);

    /**
     * 方法说明 :批量添加组
     * @param groupList
     * @author HeZhen
     * @Date 2015-8-25
     */
    void addDeps(ArrayList<GroupModel> groupList);

    /**
     * 方法说明 :修改单个组
     * @param groupModel
     * @author HeZhen
     * @Date 2015-8-25
     */
    void modifyDep(GroupModel groupModel);

    /**
     * 方法说明 :
     * @param groupList
     * @author HeZhen
     * @Date 2015-8-25
     */
    void modifyDeps(ArrayList<GroupModel> groupList);

    /**
     * 方法说明 :
     * @param groupModel
     * @author HeZhen
     * @Date 2015-8-25
     */
    void removeDep(GroupModel groupModel);

    /**
     * 方法说明 :
     * @param groupModels
     * @author HeZhen
     * @Date 2015-8-25
     */
    void removeDepAndKeys(ArrayList<GroupModel> groupModels);

    void clearAllContacts();

    void clearAllData();

    /**
     * 方法说明 :导入联系人
     * @param url
     * @author HeZhen
     * @Date 2015-8-25
     */
    void importContacts(String url);

    /**
     * 方法说明 :导出联系人
     * @param pathName
     * @param fileName
     * @author HeZhen
     * @Date 2015-8-25
     */
    void exportContacts(String pathName, String fileName);

    /**
     * 方法说明 :导入按键
     * @param url
     * @author HeZhen
     * @Date 2015-8-25
     */
    void importKeys(String url);

    /**
     * 方法说明 :导出按键
     * @param pathName
     * @param fileName
     * @author HeZhen
     * @Date 2015-8-25
     */
    void exportKeys(String pathName, String fileName);

    /**
     * 方法说明 : 通讯录编辑，同时删除组和联系人
     * @param depList
     * @param contactList
     * @author HeZhen
     * @Date 2015-8-25
     */
    void removeDepAndContacts(ArrayList<Integer> depList, ArrayList<Integer> contactList);

    /**
     * 方法说明 :位置调整时，同时修改联系人和组 
     * @param contactModels
     * @param groupModels
     * @author HeZhen
     * @Date 2015-8-25
     */
    void modifyContactAndDep(ArrayList<ContactModel> contactModels, ArrayList<GroupModel> groupModels);
    
    /**
     * 方法说明 :获取联系人类型
     * @return
     * @author hz
     * @Date 2015-9-28
     */
    ArrayList<String> getContactTypes();
    
    /**
     * 方法说明 :获取号码类型
     * @return
     * @author hz
     * @Date 2015-9-28
     */
    ArrayList<String> getPhoneTypes();
    
    /**
     * 方法说明 :调度监控号码类型
     * @return
     * @author hz
     * @Date 2015-10-13
     */
    ArrayList<String> getVDPhoneTypes();
}
