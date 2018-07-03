package com.jiaxun.sdk.dcl.module.contact.itf;

import java.util.ArrayList;
import java.util.List;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;

public interface DclContactService
{
    /**
     * ����˵�� : ��ȡ������ϵ���б�
     * @return ArrayList<ContactNode>
     * @author hubin
     * @Date 2015-3-24
     */
    List<ContactModel> getContactList();

    /**
     * ����˵�� :ͨ��Id��ȡ��ϵ��
     * @return  ContactModelʵ����
     * @author HeZhen
     * @Date 2015-5-19
     */
    ContactModel getContactById(int id);

    /**
     * ����˵�� : ͨ��Id��ȡ��
     * @param  ��ID
     * @return GroupModelʵ����
     * @author HeZhen
     * @Date 2015-5-19
     */
    GroupModel getDepById(int id);

    /**
     * ����˵�� :��ȡ����
     * @return
     * @author HeZhen
     * @Date 2015-8-11
     */
    List<GroupModel> getDepRootList();

    /**
     * ����˵�� :��ȡ�������б�
     * @return
     * @author HeZhen
     * @Date 2015-7-22
     */
    List<GroupModel> getDepList();

    /**
     * ����˵�� :��ȡ�Ѷ���״̬��ϵ��
     * @return  ���ĵ���ϵ���б�
     * @author HeZhen
     * @Date 2015-5-30
     */
    List<ContactModel> getSubscribeContacts();

    /**
     * ����˵�� :���ݺ����ȡ��ϵ��
     * @return
     * @author HeZhen
     * @Date 2015-8-11
     */
    ContactModel getContactByNum(String phoneNum);
    
    /**
     * ����˵�� :��ȡ���е���������
     * @return
     * @author hz
     * @Date 2015-9-28
     */
    List<DataType> getDataTypeList();
    /**
     * ����˵�� :�����ϵ��
     * @param contactModel ��ϵ�˶���
     * @return
     * @author HeZhen
     * @Date 2015-4-20
     */
    boolean addContact(ContactModel contactModel);

    /**
     * ����˵�� :���������ϵ��
     * @param contactModels
     * @return
     * @author HeZhen
     * @Date 2015-8-18
     */
    boolean addContacts(ArrayList<ContactModel> contactModels);

    /**
     * ����˵�� :�޸���ϵ��
     * @param contactModel
     *            ��ϵ�˶���
     * @return
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean modifyContact(ContactModel contactModel);

    /**
     * ����˵�� :�����޸���ϵ��
     * @param  contactList �� �޸ĵ���ϵ���б�
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean modifyContacts(ArrayList<ContactModel> contactList);

    /**
     * ����˵�� :����ɾ����ϵ��
     * @param contactList Ҫɾ������ϵ���б�
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeContacts(ArrayList<ContactModel> contactList);

    /**
     * ����˵�� :ɾ��������ϵ��
     * @param contactModel Ҫɾ������ϵ��
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeContact(ContactModel contactModel);

    /**
     * ����˵�� :�����
     * @param groupModel �����
     * @return boolean
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean addDep(GroupModel groupModel);

    /**
     * ����˵�� :���������
     * @param groupList Ҫ��ӵ����б�
     * @return boolean
     * @author HeZhen
     * @Date 2015-4-17
     */
    boolean addDeps(ArrayList<GroupModel> groupList);

    /**
     * ����˵�� : �޸���
     * @param groupModel ��
     * @return boolean
     * @author hubin
     * @Date 2015-7-17
     */
    boolean modifyDep(GroupModel groupModel);

    /**
     * ����˵�� : �����޸���
     * @param groupList ���б�
     * @return boolean
     * @author hubin
     * @Date 2015-7-17
     */
    boolean modifyDeps(ArrayList<GroupModel> groupList);

    /**
     * ����˵�� :ɾ��������
     * @param groupModel  ɾ������
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeDep(GroupModel groupModel);

    /**
     * ����˵�� :����ɾ����
     * @param groupModels  ɾ�����б�
     * @return
     * @author HeZhen
     * @Date 2015-7-21
     */
    boolean removeDeps(ArrayList<GroupModel> groupModels);
    
    /**
     * ����˵�� :���������������
     * @param dataTypeList
     * @return
     * @author hz
     * @Date 2015-9-29
     */
    boolean addDataTypes(ArrayList<DataType> dataTypeList);
    
    ArrayList<ContactModel> getAllContactFromDep(GroupModel groupModel);
    
    ArrayList<GroupModel> getAllDepFromDep(GroupModel groupModel);

    /**
     * ����˵�� :���������ϵ��
     * @author HeZhen
     * @Date 2015-7-1
     */
    boolean clearAllContacts();
}
