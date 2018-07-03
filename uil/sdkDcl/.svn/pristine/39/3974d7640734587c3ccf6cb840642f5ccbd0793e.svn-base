package com.jiaxun.sdk.dcl.model;

/**
 * 说明： 联系人在组中位置
 *
 * @author  HeZhen
 *
 * @Date 2015-4-3
 */
public class ContactPosInGroup implements Cloneable
{
    //联系人所在组
    private GroupModel parentGroup;
    //联系人所在组位置
    private int position;
    
    public ContactPosInGroup()
    {
    }
    
    public GroupModel getParentGroup()
    {
        return parentGroup;
    }
    public void setParentGroup(GroupModel parentGroup)
    {
        this.parentGroup = parentGroup;
    }
    public int getPosition()
    {
        return position;
    }
    public void setPosition(int position)
    {
        this.position = position;
    }
    @Override
    public ContactPosInGroup clone()
    {
        try
        {
            return (ContactPosInGroup) super.clone();
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
