package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.dcl.model.ContactPosInGroup;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.ui.widget.draggv.DragGridBaseAdapter;

/**
 * 说明：联系人适配器 ContactGridFragment 
 *
 * @author  HeZhen
 *
 * @Date 2015-4-29
 */
public class ContactAdapter extends ContactBaseAdapter implements DragGridBaseAdapter
{
    public ContactAdapter(Context context)
    {
        super(context);
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition)
    {
        BaseListItem oldListItem = dataAdapterList.get(oldPosition);
        if (oldPosition < newPosition)
        {
            for (int i = oldPosition; i < newPosition; i++)
            {
                dataAdapterList.get(i).setPosition(i + 1);
                dataAdapterList.get(i + 1).setPosition(i);
                Collections.swap(dataAdapterList, i, i + 1);
            }
        }
        else if (oldPosition > newPosition)
        {
            for (int i = oldPosition; i > newPosition; i--)
            {
                dataAdapterList.get(i).setPosition(i - 1);
                dataAdapterList.get(i - 1).setPosition(i);
                Collections.swap(dataAdapterList, i, i - 1);
            }
        }
        dataAdapterList.set(newPosition, oldListItem);
    }

    @Override
    public void savePos()
    {
        // 改变位置，更新数据库
        ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
        ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
        int count = 0;
        ContactModel contactClone;
        GroupModel groupClone;
        for (BaseListItem baseItem : dataAdapterList)
        {
            if (baseItem.getType() == BaseListItem.TYPE_CONTACT)
            {
                contactClone = UiApplication.getContactService().getContactById(baseItem.getId()).clone();
                contactClone.setPhoneNumList((ArrayList<ContactNum>)contactClone.getPhoneNumList().clone());
                if(mGroupId != GroupModel.DEFAULT_PARENT_ID)
                {
                    ContactPosInGroup posGroup = contactClone.getPosGroup(mGroupId);
                    if(posGroup != null)
                    {
                        posGroup.setPosition(baseItem.getPosition());
                    }
                }
                contactList.add(contactClone);
            }
            else if (baseItem.getType() == BaseListItem.TYPE_DEP)
            {
                groupClone = UiApplication.getContactService().getDepById(baseItem.getId()).clone();
                groupClone.setPosition(baseItem.getPosition());
                groupList.add(groupClone);
            }
        }
        UiApplication.getContactService().modifyContactAndDep(contactList, groupList);
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
        return true;
    }
}