package com.jiaxun.sdk.dcl.model;

/**
 * 说明：联系人号码对象模型
 *
 * @author  hubin
 *
 * @Date 2015-3-19
 */
public class ContactNum implements Cloneable
{
    private int id = -1;
    
    private String typeName;

    private String number;

    public ContactNum()
    {
    }

    public ContactNum(int id, String typeName, String number)
    {
        this.id = id;
        this.typeName = typeName;
        this.number = number;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    @Override
    public ContactNum clone()
    {
        try
        {
            return (ContactNum) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
