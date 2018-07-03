package com.jiaxun.uil.util.comparator;

import java.util.Comparator;
import com.jiaxun.uil.model.BaseListItem;

/**
 * Àµ√˜£∫Œª÷√≈≈–Ú
 *
 * @author  HeZhen
 *
 * @Date 2015-6-9
 */
public class PositionComparator implements Comparator<BaseListItem>
{
    @Override
    public int compare(BaseListItem lhs, BaseListItem rhs)
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
