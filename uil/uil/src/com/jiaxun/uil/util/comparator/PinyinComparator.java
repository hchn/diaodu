package com.jiaxun.uil.util.comparator;

import java.util.Comparator;

import com.jiaxun.sdk.dcl.model.ContactModel;

/**
 * 说明：姓名全拼排序
 *
 * @author  fuluo
 *
 * @Date 2015-9-2
 */
public class PinyinComparator implements Comparator<ContactModel>
{
    public int compare(ContactModel o1, ContactModel o2)
    {
        if (o1 == null || o1.getSortLetters() == null || o2 == null || o2.getSortLetters() == null)
        {
            return -1;
        }

        if ("@".equals(o1.getSortLetters()) || "#".equals(o2.getSortLetters()))
        {
            return 1;
        }
        else if ("#".equals(o1.getSortLetters()) || "@".equals(o2.getSortLetters()))
        {
            return -1;
        }
        else
        {
            return o1.getNameLetters().compareTo(o2.getNameLetters());
        }
    }
}
