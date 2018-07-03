package com.jiaxun.uil.util.comparator;

import java.util.Comparator;

import com.jiaxun.sdk.dcl.model.ContactModel;

/**
 * ˵����ͨ�����������ϵ��
 *
 * @author  HeZhen
 *
 * @Date 2015-7-23
 */
public class ContactComparatorByNum implements Comparator<ContactModel>
{
    @Override
    public int compare(ContactModel lhs, ContactModel rhs)
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
