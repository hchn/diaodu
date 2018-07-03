package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;

import android.content.Context;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.dcl.util.EnumGroupType;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.widget.draggv.DragGridBaseAdapter;

/**
 * Àµ√˜£∫∞¥º¸≈‰÷√  ≈‰∆˜
 *
 * @author  HeZhen
 *
 * @Date 2015-6-10
 */
public class ContactKeyAdapter extends ContactBaseAdapter implements DragGridBaseAdapter
{

    public ContactKeyAdapter(Context context)
    {
        super(context);
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition)
    {
        BaseListItem oldListItem = dataAdapterList.get(oldPosition);
        BaseListItem newListItem = dataAdapterList.get(newPosition);
        dataAdapterList.set(oldPosition, newListItem);
        dataAdapterList.set(newPosition, oldListItem);
        
        newListItem.setPosition(oldPosition);
        oldListItem.setPosition(newPosition);
        
    }
    @Override
    public void savePos()
    {
        ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
        for (BaseListItem item : dataAdapterList)
        {
            int subType = item.getSubType();
            if(subType == EnumGroupType.KEY_GROUP || subType == EnumGroupType.KEY)
            {
                GroupModel group = UiApplication.getContactService().getDepById(item.getId());
                GroupModel groupClone = group.clone();
                groupClone.setPosition(item.getPosition());
                groupClone.setChildrenContactList((ArrayList<ContactModel>)group.getChildrenContactList().clone());
                groupClone.setChildrenDepList((ArrayList<GroupModel>)group.getChildrenDepList().clone());
                groupList.add(groupClone); 
            }
        }
        UiApplication.getContactService().modifyDeps(groupList);
    }
    @Override
    public void setHideItem(int hidePosition)
    {
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    @Override
    public void removeItem(int removePosition)
    {

    }

    @Override
    public boolean isCanMove(int position)
    {
        BaseListItem listItem = null;
        if (position >= 0 && position < dataAdapterList.size())
        {
            listItem = dataAdapterList.get(position);
        }

        if (listItem == null)
        {
            return false;
        }
        if (listItem.getSubType() == EnumGroupType.PLACE)
        {
            return false;
        }
        return true;
    }
}
