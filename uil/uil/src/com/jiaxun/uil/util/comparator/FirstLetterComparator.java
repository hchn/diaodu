package com.jiaxun.uil.util.comparator;

import java.util.Comparator;

import com.jiaxun.sdk.dcl.model.ContactModel;

/**
 * ËµÃ÷£ºÊ××ÖÄ¸ÅÅÐò
 *
 * @author  HeZhen
 *
 * @Date 2015-6-9
 */
public class FirstLetterComparator implements Comparator<ContactModel>
{
    @Override
    public int compare(ContactModel lhs, ContactModel rhs)
    {
        if (lhs == null || rhs == null)
        {
            return 0;
        }
        if (lhs.getSortLetters().charAt(0) > rhs.getSortLetters().charAt(0))
        {
            return 1;
        }
        else if (lhs.getSortLetters().charAt(0) < rhs.getSortLetters().charAt(0))
        {
            return -1;
        }
        return 0;
    }
}
