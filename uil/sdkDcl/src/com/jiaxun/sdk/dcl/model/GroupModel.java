package com.jiaxun.sdk.dcl.model;

import java.util.ArrayList;

import com.jiaxun.sdk.dcl.util.EnumGroupType;

/**
 * 说明：通讯录 组 实体类，包括： 组  快捷组 按键
 *
 * @author  HeZhen
 *
 * @Date 2015-4-3
 */
public class GroupModel implements Cloneable
{
    // 默认父组ID
    public static int DEFAULT_PARENT_ID = -1;

    private int id;

    private String name;
    /**编号*/
    private int number;

    private int parentId = DEFAULT_PARENT_ID;

    private int position;
    /**组 类型*/
    private int type = EnumGroupType.GROUP_NULL;

    /**下一级子组,childrenDepList 部门组 ,按键*/
    private ArrayList<GroupModel> childrenDepList;
    private ArrayList<ContactModel> childrenContactList;

    /**父组*/
    private GroupModel parent;

    public ArrayList<GroupModel> getChildrenDepList()
    {
        return childrenDepList;
    }

    public ArrayList<ContactModel> getChildrenContactList()
    {
        return childrenContactList;
    }

    public void setChildrenContactList(ArrayList<ContactModel> childrenContactList)
    {
        this.childrenContactList = childrenContactList;
    }

    public void setChildrenDepList(ArrayList<GroupModel> childrenDepList)
    {
        this.childrenDepList = childrenDepList;
    }

    public GroupModel getParent()
    {
        return parent;
    }

    public void setParent(GroupModel parent)
    {
        this.parent = parent;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public GroupModel()
    {
        childrenDepList = new ArrayList<GroupModel>();
        childrenContactList = new ArrayList<ContactModel>();
        setId(DEFAULT_PARENT_ID);
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public int getParentId()
    {
        return parentId;
    }

    public void setParentId(int parentId)
    {
        this.parentId = parentId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
    public void addContact(ContactModel mContact)
    {
        for(ContactModel contact : childrenContactList)
        {
            if(mContact.getId() == contact.getId())
            {
                return;
            }
        }
        childrenContactList.add(mContact);
    }
    public void removContact(int contactId)
    {
        for(ContactModel contact : childrenContactList)
        {
            if(contactId == contact.getId())
            {
                childrenContactList.remove(contact);
                break;
            }
        }
    }
    @Override
    public GroupModel clone()
    {
        try
        {
            GroupModel groupClone = (GroupModel) super.clone();
            groupClone.setChildrenDepList((ArrayList<GroupModel>) childrenDepList.clone());
            groupClone.setChildrenContactList((ArrayList<ContactModel>) childrenContactList.clone());
            return groupClone;
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
