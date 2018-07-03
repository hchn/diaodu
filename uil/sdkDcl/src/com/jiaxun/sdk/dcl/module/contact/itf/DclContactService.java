package com.jiaxun.sdk.dcl.module.contact.itf;

import java.util.ArrayList;
import java.util.List;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;

public interface DclContactService
{
    /**
     * 方法说明 : 获取所有联系人列表
     * @return ArrayList<ContactNode>
     * @author hubin
     * @Date 2015-3-24
     */
    List<ContactModel> getContactList();

    /**
     * 方法说明 :通过Id获取联系人
     * @return  ContactModel实体类
     * @author HeZhen
     * @Date 2015-5-19
     */
    ContactModel getContactById(int id);

    /**
     * 方法说明 : 通过Id获取组
     * @param  组ID
     * @return GroupModel实体类
     * @author HeZhen
     * @Date 2015-5-19
     */
    GroupModel getDepById(int id);

    /**
     * 方法说明 :获取根组
     * @return
     * @author HeZhen
     * @Date 2015-8-11
     */
    List<GroupModel> getDepRootList();

    /**
     * 方法说明 :获取所有组列表
     * @return
     * @author HeZhen
     * @Date 2015-7-22
     */
    List<GroupModel> getDepList();

    /**
     * 方法说明 :获取已订阅状态联系人
     * @return  订阅的联系人列表
     * @author HeZhen
     * @Date 2015-5-30
     */
    List<ContactModel> getSubscribeContacts();

    /**
     * 方法说明 :根据号码获取联系人
     * @return
     * @author HeZhen
     * @Date 2015-8-11
     */
    ContactModel getContactByNum(String phoneNum);
    
    /**
     * 方法说明 :获取所有的数据类型
     * @return
     * @author hz
     * @Date 2015-9-28
     */
    List<DataType> getDataTypeList();
    /**
     * 方法说明 :添加联系人
     * @param contactModel 联系人对象
     * @return
     * @author HeZhen
     * @Date 2015-4-20
     */
    boolean addContact(ContactModel contactModel);

    /**
     * 方法说明 :批量添加联系人
     * @param contactModels
     * @return
     * @author HeZhen
     * @Date 2015-8-18
     */
    boolean addContacts(ArrayList<ContactModel> contactModels);

    /**
     * 方法说明 :修改联系人
     * @param contactModel
     *            联系人对象
     * @return
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean modifyContact(ContactModel contactModel);

    /**
     * 方法说明 :批量修改联系人
     * @param  contactList ： 修改的联系人列表
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean modifyContacts(ArrayList<ContactModel> contactList);

    /**
     * 方法说明 :批量删除联系人
     * @param contactList 要删除的联系人列表
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeContacts(ArrayList<ContactModel> contactList);

    /**
     * 方法说明 :删除单个联系人
     * @param contactModel 要删除的联系人
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeContact(ContactModel contactModel);

    /**
     * 方法说明 :添加组
     * @param groupModel 组对象
     * @return boolean
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean addDep(GroupModel groupModel);

    /**
     * 方法说明 :批量添加组
     * @param groupList 要添加的组列表
     * @return boolean
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean addDeps(ArrayList<GroupModel> groupList);

    /**
     * 方法说明 : 修改组
     * @param groupModel 组
     * @return boolean
     * @author hubin
     * @Date 2015-7-17
     */
    boolean modifyDep(GroupModel groupModel);

    /**
     * 方法说明 : 批量修改组
     * @param groupList 组列表
     * @return boolean
     * @author hubin
     * @Date 2015-7-17
     */
    boolean modifyDeps(ArrayList<GroupModel> groupList);

    /**
     * 方法说明 :删除单个组
     * @param groupModel  删除的组
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeDep(GroupModel groupModel);

    /**
     * 方法说明 :批量删除组
     * @param groupModels  删除组列表
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeDeps(ArrayList<GroupModel> groupModels);
    
    /**
     * 方法说明 :批量添加数据类型
     * @param dataTypeList
     * @return
     * @author hz
     * @Date 2015-9-29
     */
    boolean addDataTypes(ArrayList<DataType> dataTypeList);
    
    ArrayList<ContactModel> getAllContactFromDep(GroupModel groupModel);
    
    ArrayList<GroupModel> getAllDepFromDep(GroupModel groupModel);

    /**
     * 方法说明 :清空所有联系人
     * @author HeZhen
     * @Date 2015-7-1
     */
    boolean clearAllContacts();
}
