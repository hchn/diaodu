package com.jiaxun.sdk.dcl.model;

/**
 * ˵�����ڰ����� ʵ����
 *
 * @author  HeZhen
 *
 * @Date 2015-7-2
 */
public class BlackWhiteModel
{
    private int id;
    // ��ϵ��ID
    private int contactId = ContactModel.DEFAULT_ID;
    // ����
    private String phoneNum;
    // 0 ������ 1 ������ EnumBWType
    private int type;

    public int getContactId()
    {
        return contactId;
    }

    public void setContactId(int contactId)
    {
        this.contactId = contactId;
    }

    public String getPhoneNum()
    {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum)
    {
        this.phoneNum = phoneNum;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
