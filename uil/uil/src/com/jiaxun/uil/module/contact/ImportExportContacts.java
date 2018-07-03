package com.jiaxun.uil.module.contact;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.dcl.model.DataType;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.util.file.FileManager;
import com.jiaxun.sdk.util.file.Row;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.comparator.ContactComparatorByNum;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-5-20
 */
public class ImportExportContacts
{
    private final static String TAG = ImportExportContacts.class.getSimpleName();
    private FileManager fm;
//    private File file;
    // key ： 组名 包括组关系，与父组之间“_”隔开 如：产品一部_新终端
    private Map<String, GroupModel> groupMap;
    private ArrayList<ContactModel> contactList;
    private ArrayList<DataType> dataTypeList;
    // key : 编号
    @Deprecated
    // 导入按键时不能使用，联系人经过手动添加后，数据不全
    private Map<Integer, ContactModel> contactNumMap;

    public ImportExportContacts()
    {
        groupMap = new HashMap<String, GroupModel>();
        contactList = new ArrayList<ContactModel>();
        dataTypeList = new ArrayList<DataType>();
        contactNumMap = new HashMap<Integer, ContactModel>();
        fm = FileManager.getInstance();
//        File dir = Environment.getExternalStorageDirectory();
//        file = new File(dir, "contacts.csv");
    }

    // 此处设置的ID 都不是真实的用户ID，作为临时添加. 但是后面按键 也需要此ID。如果以此ID作为主键的话，手动添加时管理麻烦。
    // 可增加一个编号的字段
    public int readFile(String filepath)
    {
        try
        {
            dataTypeList.clear();
            contactList.clear();
            contactNumMap.clear();
            groupMap.clear();
            File file = new File(filepath);
            List<Row> rowsFromFile = fm.readFile(file);
            rowsFromFile.remove(0);
            ArrayList<String> contactTypeList = new ArrayList<String>();
            ArrayList<String> phoneTypeList = new ArrayList<String>();
            for (Row row : rowsFromFile)
            {
                int column = 0;
                ContactModel contact = new ContactModel();
                String[] slips = row.rebuild();
                //编号
                String num = slipsText(slips, column++);
                if (TextUtils.isEmpty(num))
                {
                    continue;
                }
                contact.setNumber(Integer.valueOf(num));
                contact.setName(slipsText(slips, column++));
                String type = slipsText(slips, column++);
                if (!TextUtils.isEmpty(type))
                {
                    if(!contactTypeList.contains(type))
                    {
                        contactTypeList.add(type); 
                    }
                }
                contact.setTypeName(type);
                StringBuffer groupName = new StringBuffer();
                GroupModel groupModel = null;
                for (int i = column; i <= column + 4; i++)
                {
                    GroupModel ge = groupSet(slips, i, groupName);
                    if (ge != null)
                    {
                        groupModel = ge;
                    }
                }
                column += 5;//5级组
                //入会号码
                contact.setConfNum(slipsText(slips, column++));
                ArrayList<String> phoneNameList = new ArrayList<String>();
                // 号码
                phoneNumSet(contact,phoneTypeList,phoneNameList, slipsText(slips, column++), slipsText(slips, column++));
                phoneNumSet(contact,phoneTypeList,phoneNameList, slipsText(slips, column++), slipsText(slips, column++));
                phoneNumSet(contact,phoneTypeList,phoneNameList, slipsText(slips, column++), slipsText(slips, column++));
                phoneNumSet(contact,phoneTypeList,phoneNameList, slipsText(slips, column++), slipsText(slips, column++));
                phoneNumSet(contact,phoneTypeList,phoneNameList, slipsText(slips, column++), slipsText(slips, column++));
                //如果会议号码不是现有号码，则设置为第一个号码为入会号码
                 if(TextUtils.isEmpty(contact.getConfNum()) || !phoneNameList.contains(contact.getConfNum()))
                {
                    String phoneNum = phoneNameList.size() > 0 ? phoneNameList.get(0) : null;
                    contact.setConfNum(phoneNum);
                }
                if (groupModel != null)
                {
                    // 这么做的前提是，导入的联系人一个联系人只对应一个组。
                    ContactPosInGroup posGroup = new ContactPosInGroup();
                    posGroup.setParentGroup(groupModel);
                    String posStr = slipsText(slips, column++);
                    int p = TextUtils.isEmpty(posStr) ? 0 : Integer.valueOf(posStr);
                    posGroup.setPosition(p > 0 ? (p - 1) : 0);
                    contact.addPosGroup(posGroup);
                }
                contactList.add(contact);
                contactNumMap.put(contact.getNumber(), contact);
            }
            for(String contactTypeName : contactTypeList)
            {
                DataType dataType = new DataType();
                dataType.setDataIdent(DataType.CONTACT_IDENT);
                dataType.setTypeName(contactTypeName);
                dataTypeList.add(dataType);
            }
            for(String phoneTypeName : phoneTypeList)
            {
                DataType dataType = new DataType();
                dataType.setDataIdent(DataType.PHONE_IDENT);
                dataType.setTypeName(phoneTypeName);
                dataTypeList.add(dataType);
            }
        }
        catch (Exception e)
        {
            Log.info(TAG, e.toString());
            return 0;
        }
        return 1;
    }

    /**
     * 方法说明 :截取字段
     * @param src
     * @param count
     * @return
     * @author HeZhen
     * @Date 2015-8-18
     */
    private String slipsText(String[] src, int count)
    {
        if (count >= src.length)
        {
            return null;
        }
        return src[count].trim();
    }

    /**
     * 方法说明 :
     * @param contact
     * @param phoneTypeList
     * @param phoneNameList
     * @param typeName
     * @param num
     * @author hz
     * @Date 2015-9-29
     */
    private void phoneNumSet(ContactModel contact,ArrayList<String> phoneTypeList,ArrayList<String> phoneNameList, String typeName, String num)
    {
        if (TextUtils.isEmpty(typeName) || TextUtils.isEmpty(num))
        {
            return;
        }
        if(!phoneTypeList.contains(typeName))
        {
            phoneTypeList.add(typeName);
        }
        if(!phoneNameList.contains(num))
        {
            phoneNameList.add(num);
        }
        ContactNum contactNum = new ContactNum();
        contactNum.setTypeName(typeName);
        contactNum.setNumber(num);
        contact.getPhoneNumList().add(contactNum);
    }

    private GroupModel groupSet(String[] src, int count, StringBuffer groupBuffer)
    {
        String groupName = slipsText(src, count);
        if (TextUtils.isEmpty(groupName))
        {
            return null;
        }
        GroupModel group = new GroupModel();
        if (count == 3)
        {
            if (!groupMap.containsKey(groupName))
            {
                group.setName(groupName);
                groupMap.put(groupName, group);
            }
            else
            {
                group = groupMap.get(groupName);
            }
            groupBuffer.append(groupName);
        }
        else if (count > 3)
        {
            groupBuffer.append("_" + groupName);
            String key = groupBuffer.toString();
            if (!groupMap.containsKey(key))
            {
                group.setName(key);
                groupMap.put(key, group);
            }
            else
            {
                return groupMap.get(key);
            }
        }

        return group;
    }

    public ArrayList<ContactModel> getContactList()
    {
        return contactList;
    }
    
    public Map<Integer, ContactModel> getContactNumMap()
    {
        return contactNumMap;
    }

    public Map<String, GroupModel> getGroupMap()
    {
        return groupMap;
    }
    public ArrayList<DataType> getDataTypeList()
    {
        return dataTypeList;
    }
    public boolean exportToCSV(String pathName, String fileName)
    {
        ArrayList<ContactModel> contactList = (ArrayList<ContactModel>) UiApplication.getInstance().getContactService().getContactList().clone();
        boolean isError = true;
        String resion = null;
        if (TextUtils.isEmpty(pathName))
        {
        }
        else if (contactList == null || contactList.size() == 0)
        {
            resion = "无数据，无法导出！";
        }
        else if (TextUtils.isEmpty(fileName))
        {
            resion = "文件名不能为空！";
        }
        else
        {
            isError = false;
        }
        if (isError)
        {
            if (!TextUtils.isEmpty(resion))
            {
                ToastUtil.showUiToast(resion);
            }
            return false;
        }
        List<String> rows = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("编号,").append("姓名,").append("用户类型,")
        .append("一级部门,").append("二级部门,").append("三级部门,").append("四级部门,").append("五级部门,")
                .append("入会号码,")
                .append("号码类型,").append("号码1,").append("号码类型,").append("号码2,")
                .append("号码类型,").append("号码3,").append("号码类型,").append("号码4,")
                .append("号码类型,").append("号码5,").append("排位");
        rows.add(stringBuffer.toString());
        Collections.sort(contactList, new ContactComparatorByNum());
        for (ContactModel mContactModel : contactList)
        {
            stringBuffer.setLength(0);
            stringBuffer.append(mContactModel.getNumber()).append(",");
            stringBuffer.append(mContactModel.getName()).append(",");
            stringBuffer.append(mContactModel.getTypeName()).append(",");
//            if (mContactModel.getType() == 0)
//            {
//                stringBuffer.append("普通用户").append(",");
//            }
//            else
//            {
//                stringBuffer.append("监控用户").append(",");
//            }
            ArrayList<ContactPosInGroup> parentPosGroupList = mContactModel.getParentGroupList();
            ContactPosInGroup posGroup = parentPosGroupList.size() > 0 ? parentPosGroupList.get(0) : null;
            ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
            if (posGroup != null)
            {
                GroupModel group = posGroup.getParentGroup();
                if (group != null)
                {
                    groupList.add(group);
                    getParentGroup(groupList, group);
                }
            }
            stringBuffer.append(getGroupName(groupList, 1)).append(",").append(getGroupName(groupList, 2)).append(",").append(getGroupName(groupList, 3))
                    .append(",").append(getGroupName(groupList, 4)).append(",").append(getGroupName(groupList, 5)).append(",");

            stringBuffer.append(mContactModel.getConfNum()).append(",");//入会号码
            ArrayList<ContactNum> phoneNumList = mContactModel.getPhoneNumList();
            
            int restNum = UiConfigEntry.PHONE_NUM_MAX - phoneNumList.size();
            for (ContactNum contactNum : phoneNumList)
            {
                stringBuffer.append(contactNum.getTypeName()).append(",").append(contactNum.getNumber()).append(",");
            }
            while (restNum > 0)
            {
                stringBuffer.append(",").append(",");
                restNum--;
            }

            stringBuffer.append(posGroup.getPosition() + 1);
            rows.add(stringBuffer.toString());
        }
        return fm.writeFile(rows, pathName, fileName + ".csv");
    }

    private String getGroupName(ArrayList<GroupModel> groupList, int level)
    {
        int size = groupList.size();
        int index = size - level;
        String name = null;
        if (index >= 0)
        {
            name = groupList.get(index).getName();
        }
        return name == null ? "" : name;
    }

    private ArrayList<GroupModel> getParentGroup(ArrayList<GroupModel> groupList, GroupModel group)
    {
        GroupModel parentGroup = group.getParent();
        if (parentGroup != null)
        {
            groupList.add(parentGroup);
            getParentGroup(groupList, parentGroup);
        }
        return groupList;
    }
}
