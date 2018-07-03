package com.jiaxun.sdk.dcl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.jiaxun.sdk.dcl.util.CharacterParser;

/**
 * 说明：联系人实体类
 *
 * @author  hz
 *
 * @Date 2015-7-22
 */
public class ContactModel implements Cloneable
{
    public static final int DEFAULT_ID = -1;

    private int id = DEFAULT_ID;

    private String name;
    //编号，导入时关联按键
    private int number;
    // 用户类型
    private String typeName;
    // 订阅 0 :false ;1: true
    private int subScribe;
    //入会号码
    private String confNum;
    // 电话列表
    private ArrayList<ContactNum> phoneNumList;
    // 联系人所关联组，可属于多组
    private ArrayList<ContactPosInGroup> parentGroupList;
    /**
     * key:组Id，
     */
    private Map<Integer, ContactPosInGroup> parentGroupMap;
    // 全拼
    private String nameLetters;
    // 全拼数组
    private String[] nameLettersArray;
    // 显示数据拼音的首字母
    private String sortLetters = "#";

    private int status;

    private int callStatus;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getCallStatus()
    {
        return callStatus;
    }

    public void setCallStatus(int callStatus)
    {
        this.callStatus = callStatus;
    }

    public String getSortLetters()
    {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters)
    {
        this.sortLetters = sortLetters;
    }

    public String getNameLetters()
    {
        return nameLetters;
    }

    public String[] getNameLettersArray()
    {
        return nameLettersArray;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        CharacterParser cp = new CharacterParser();
        cp.setResource(name);
        String pinyin = cp.getSpelling();
        String sortString = TextUtils.isEmpty(pinyin) ? "#" : pinyin.substring(0, 1).toUpperCase();
        if (!TextUtils.isEmpty(sortString) && sortString.matches("[A-Z]"))
        {
            sortLetters = sortString;
            nameLetters = pinyin;
            nameLettersArray = cp.getSpellings();
        }else
        {
            sortLetters = "#";
            nameLetters = "";
            nameLettersArray = null;
        }
    }

    public ContactModel()
    {
        parentGroupList = new ArrayList<ContactPosInGroup>();
        parentGroupMap = new HashMap<Integer, ContactPosInGroup>();
        phoneNumList = new ArrayList<ContactNum>();
    }

    public ArrayList<ContactNum> getPhoneNumList()
    {
        return phoneNumList;
    }

    public void setPhoneNumList(ArrayList<ContactNum> phoneNumList)
    {
        this.phoneNumList = phoneNumList;
    }

    public void setSubScribe(int subScribe)
    {
        this.subScribe = subScribe;
    }

    public int getSubScribe()
    {
        return subScribe;
    }

    public ArrayList<ContactPosInGroup> getParentGroupList()
    {
        return parentGroupList;
    }
    public void removPosGroup(int posId)
    {
        if(parentGroupMap.containsKey(posId))
        {
            parentGroupList.remove(parentGroupMap.get(posId));
            parentGroupMap.remove(posId);
        }
    }
    public void addPosGroup(ContactPosInGroup posGroup)
    {
        if (!parentGroupMap.containsKey((posGroup.getParentGroup().getId())))
        {
            parentGroupMap.put(posGroup.getParentGroup().getId(), posGroup);
            parentGroupList.add(posGroup);
        }
    }

    public void addPosGroups(ArrayList<ContactPosInGroup> posGroups)
    {
        for (ContactPosInGroup posGroup : posGroups)
        {
            addPosGroup(posGroup);
        }
    }

    public ContactPosInGroup getPosGroup(int key)
    {
        return parentGroupMap.get(key);
    }

    public void clearParentGroups()
    {
        parentGroupList.clear();
        parentGroupMap.clear();
    }

    @Override
    public ContactModel clone()
    {
        try
        {
            ContactModel contactModel = (ContactModel) super.clone();
            contactModel.setPhoneNumList((ArrayList<ContactNum>) phoneNumList.clone());
            contactModel.parentGroupList = (ArrayList<ContactPosInGroup>) parentGroupList.clone();
            contactModel.parentGroupMap.clear();
            for (ContactPosInGroup posInGroup : contactModel.parentGroupList)
            {
                GroupModel group = posInGroup.getParentGroup();
                contactModel.parentGroupMap.put(group.getId(), posInGroup);
            }
            return contactModel;
        }
        catch (CloneNotSupportedException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public String getConfNum()
    {
        return confNum;
    }

    public void setConfNum(String confNum)
    {
        this.confNum = confNum;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
}
