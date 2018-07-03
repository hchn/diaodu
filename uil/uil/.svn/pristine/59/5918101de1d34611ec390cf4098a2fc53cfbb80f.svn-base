package com.jiaxun.uil.util.comparator;

import java.util.Comparator;

import com.jiaxun.sdk.dcl.model.GroupModel;

/**
 * 说明：位置排序  小到大
 *
 * @author  HeZhen
 *
 * @Date 2015-6-9
 */
public class GroupPositionComparator implements Comparator<GroupModel>
{
    @Override
    public int compare(GroupModel lhs, GroupModel rhs)
    {
        if (lhs == null || rhs == null)
        {
            return 0;
        }
        if (lhs.getPosition() > rhs.getPosition())
        {
            return 1;
        }
        else if (lhs.getPosition() < rhs.getPosition())
        {
            return -1;
        }
        return 0;
    }
}
