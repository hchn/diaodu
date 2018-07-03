package com.jiaxun.uil.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.Handler;
import android.text.TextUtils;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.util.comparator.GroupPositionComparator;
import com.jiaxun.uil.util.comparator.PinyinComparator;
import com.jiaxun.uil.util.comparator.PositionComparator;

/**
 * 说明：联系人工具类
 *
 * @author  HeZhen
 *
 * @Date 2015-6-10
 */
public class ContactUtil
{
    //导入导出，联系人，按键 记录的编号最大值
    public static int contactMaxNum = 0;
    //最大位置，防止导入的位置不规律
    public static int keyMaxNum = 0;
    private final static Integer lock = 1;
    private static GroupPositionComparator groupPosComparator = new GroupPositionComparator();
    private static PinyinComparator letterComparator = new PinyinComparator();
    private static PositionComparator baseItemComparator = new PositionComparator();
    
    private static ArrayList<String> vdPhoneTypeList;
    private static ArrayList<String> getVdPhoneTypeList()
    {
        if(vdPhoneTypeList == null)
        {
            String[] phoneTypeArray = UiApplication.getInstance().getResources().getStringArray(R.array.vd_phone_categary);
            vdPhoneTypeList = new ArrayList<String>(Arrays.asList(phoneTypeArray));  
        }
        return vdPhoneTypeList;
    }
    /**
     * 方法说明 :获取监控号码类型的名称
     * @return
     * @author hz
     * @Date 2015-9-29
     */
    public static String getVsPhoneTypeName()
    {
        return getVdPhoneTypeList().get(1);
    }
    /**
     * 方法说明 :获取调度号码类型的名称
     * @return
     * @author hz
     * @Date 2015-9-29
     */
    public static String getDsPhoneTypeName()
    {
        return getVdPhoneTypeList().get(0);
    }
    public static void RunOnUIThread(Runnable runnable) {
        synchronized (lock) {
            new Handler(UiApplication.getInstance().getMainLooper()).post(runnable);
        }
    }
    public static void sortGroupByPos(List<GroupModel> GroupModelList)
    {
        if(groupPosComparator == null)
        {
            groupPosComparator = new GroupPositionComparator();
        }
        Collections.sort(GroupModelList, groupPosComparator);
    }

    public static void sortContacts(List<ContactModel> ContactModelList)
    {
        if(letterComparator == null)
        {
            letterComparator = new PinyinComparator();
        }
        Collections.sort(ContactModelList, letterComparator);
    }
    public static void sortBaseListItemByPos(List<BaseListItem> baseItems)
    {
        if(baseItemComparator == null)
        {
            baseItemComparator = new PositionComparator();
        }
        Collections.sort(baseItems, baseItemComparator);
    }
    /**
     * 方法说明 : 判断是否为监控用户
     * @param typeName
     * @return
     * @author hz
     * @Date 2015-9-28
     */
    public static boolean isVsByContactType(String typeName)
    {
        if(TextUtils.isEmpty(typeName))
        {
            return false;
        }
        ArrayList<String> contactTypes = UiApplication.getContactService().getContactTypes();
        if(contactTypes.size() > 1)
        {
            return typeName.equals(contactTypes.get(1));
        }
         return false;
    }
    public static boolean isVsByPhoneType(String typeName)
    {
        if(TextUtils.isEmpty(typeName))
        {
            return false;
        }
        ArrayList<String> phoneTypes = UiApplication.getContactService().getVDPhoneTypes();
        if(phoneTypes.size() > 1)
        {
            return typeName.equals(phoneTypes.get(1));
        }
         return false;
    }
    /**
     * 方法说明 : 判断是否为调度用户
     * @param typeName
     * @return
     * @author hz
     * @Date 2015-9-28
     */
    public static boolean isDsByContactType(String typeName)
    {
        if(TextUtils.isEmpty(typeName))
        {
            return false;
        }
        ArrayList<String> contactTypes = UiApplication.getContactService().getContactTypes();
        if(contactTypes.size() > 0)
        {
            return typeName.equals(contactTypes.get(0));
        }
         return false;
    }
}
