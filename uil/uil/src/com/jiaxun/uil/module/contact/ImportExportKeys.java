package com.jiaxun.uil.module.contact;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.util.file.FileManager;
import com.jiaxun.sdk.util.file.Row;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.ToastUtil;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-6-12
 */
public class ImportExportKeys
{
    private final static String TAG = ImportExportKeys.class.getSimpleName();
    private FileManager fm;
//    private File file;
    //
    private Map<String, GroupModel> keyGroupMap;
    private ArrayList<GroupModel> keyList;

    public ImportExportKeys()
    {
        keyGroupMap = new HashMap<String, GroupModel>();
        keyList = new ArrayList<GroupModel>();
        fm = FileManager.getInstance();
//        File dir = Environment.getExternalStorageDirectory();
//        file = new File(dir, "keys.csv");
    }

    public int readFile(String filePath)
    {
        try
        {
            getKeyList().clear();
            getKeyGroupMap().clear();
            File file = new File(filePath);
            List<Row> rowsFromFile = fm.readFile(file);
            rowsFromFile.remove(0);
            int keyNum = 0;
            for (Row row : rowsFromFile)
            {
                GroupModel keyEntity = new GroupModel();
                String[] slips = row.rebuild();
                keyEntity.setNumber(keyNum);
                keyNum++;
                keyEntity.setName(slipsText(slips, 0));

                StringBuffer groupName = new StringBuffer();
                GroupModel groupModel = null;
                for (int i = 1; i <= 5; i++)
                {
                    GroupModel ge = groupSet(slips, i, groupName);
                    if (ge != null)
                    {
                        groupModel = ge;
                    }
                }
                keyEntity.setParent(groupModel);
                // 成员
                String contactString = slipsText(slips, 6).trim();
                // 成员编号
                String[] contacts = contactString.split("；");
                List<String> contactList = new ArrayList<String>(Arrays.asList(contacts));

                for (String num : contactList)
                {
                    ContactModel contact = new ContactModel();
                    if (!TextUtils.isEmpty(num))
                    {
                        contact.setNumber(Integer.valueOf(num));
                        keyEntity.getChildrenContactList().add(contact);
                    }
                }
                String position = slipsText(slips, 7);
                int p = 0;
                if (!TextUtils.isEmpty(position))
                {
                    p = Integer.valueOf(position);
                }
                keyEntity.setPosition(p > 0 ? (p - 1) : 0);
                getKeyList().add(keyEntity);
            }

        }
        catch (Exception e)
        {
            Log.info(TAG, e.toString());
            return 0;
        }
        return 1;
    }

    private String slipsText(String[] src, int count)
    {
        if (count >= src.length)
        {
            return null;
        }
        return src[count].trim();
    }

    private GroupModel groupSet(String[] src, int count, StringBuffer groupBuffer)
    {
        String groupName = slipsText(src, count);
        if (TextUtils.isEmpty(groupName))
        {
            return null;
        }
        GroupModel group = new GroupModel();
        if (count == 1)
        {
            if (!keyGroupMap.containsKey(groupName))
            {
                group.setName(groupName);
                keyGroupMap.put(groupName, group);
            }
            else
            {
                group = keyGroupMap.get(groupName);
            }
            groupBuffer.append(groupName);
        }
        else if (count > 1)
        {
            groupBuffer.append("_" + groupName);
            String key = groupBuffer.toString();
            if (!keyGroupMap.containsKey(key))
            {
                group.setName(key);
                keyGroupMap.put(key, group);
            }
            else
            {
                return keyGroupMap.get(key);
            }
        }

        return group;
    }

    public boolean exportToCSV(String pathName, String fileName)
    {
        ArrayList<GroupModel> keyEntityList = (ArrayList<GroupModel>) UiApplication.getContactService().getKeyList();
        boolean isError = true;
        String resion = null;
        if (TextUtils.isEmpty(pathName))
        {
        }
        else if(keyEntityList == null || keyEntityList.size() == 0)
        {
            resion = "无数据，无法导出！";
        }
        else if(TextUtils.isEmpty(fileName))
        {
            resion = "文件名不能为空！";
        }else
        {
            isError = false;
        }
        if(isError)
        {
            if(!TextUtils.isEmpty(resion))
            {
                ToastUtil.showUiToast(resion);
            }
            return false;
        }
        List<String> rows = new ArrayList<String>();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("按键名称,").append("一级组,").append("二级组,").append("三级组,").append("四级组,").append("五级组,").append("成员,").append("排位");
        rows.add(stringBuffer.toString());
        Collections.sort(keyEntityList, new KeyComparatorByNum());
        for (GroupModel keyEntity : keyEntityList)
        {
            stringBuffer.setLength(0);
            stringBuffer.append(keyEntity.getName()).append(",");
            GroupModel group = keyEntity.getParent();
            ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
            if (group != null)
            {
                groupList.add(group);
                getParentGroup(groupList, group);
            }
            stringBuffer.append(getGroupName(groupList, 1)).append(",").append(getGroupName(groupList, 2)).append(",").append(getGroupName(groupList, 3))
                    .append(",").append(getGroupName(groupList, 4)).append(",").append(getGroupName(groupList, 5)).append(",");
            ArrayList<ContactModel> contactList = keyEntity.getChildrenContactList();
            for (ContactModel contact : contactList)
            {
                stringBuffer.append(contact.getNumber()).append("；");
            }
            stringBuffer.append(",").append(keyEntity.getPosition() + 1);
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

    public ArrayList<GroupModel> getKeyList()
    {
        return keyList;
    }

    public Map<String, GroupModel> getKeyGroupMap()
    {
        return keyGroupMap;
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

class KeyComparatorByNum implements Comparator<GroupModel>
{
    @Override
    public int compare(GroupModel lhs, GroupModel rhs)
    {
        if (lhs == null || rhs == null)
        {
            return 0;
        }
        if (lhs.getNumber() > rhs.getNumber())
        {
            return 1;
        }
        else if (lhs.getNumber() < rhs.getNumber())
        {
            return -1;
        }
        return 0;
    }
}
