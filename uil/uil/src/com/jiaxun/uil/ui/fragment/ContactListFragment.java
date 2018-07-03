package com.jiaxun.uil.ui.fragment;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.screen.HomeActivity;
import com.jiaxun.uil.ui.view.AzSideBar;
import com.jiaxun.uil.ui.view.AzSideBar.OnTouchingLetterChangedListener;
import com.jiaxun.uil.ui.widget.PhoneListCallDialog;
import com.jiaxun.uil.ui.widget.sticky.StickyGridHeadersGridView;
import com.jiaxun.uil.ui.widget.sticky.StickyGridHeadersSimpleArrayAdapter;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：通讯录  列表样式
 *
 * @author  HeZhen
 *
 * @Date 2015-4-17
 */
public class ContactListFragment extends BaseFragment implements NotificationCenterDelegate,View.OnClickListener
{
    private static final String KEY_LIST_POSITION = "key_list_position";

    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    
    private int mActivatedPosition = ListView.INVALID_POSITION;

    private int mFirstVisible;

    private GridView mGridView;

    private Toast mToast;

    private StickyGridHeadersSimpleArrayAdapter simpleAdapter;

    private ArrayList<ContactModel> mContacts;
    
    private AzSideBar indexBar;
    
    private ImageView switchBtn;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        return inflater.inflate(R.layout.fragment_contact_gridlist, container, false);
    }
    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        release();
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
    }

//    @Override
//    public void onHeaderClick(AdapterView<?> parent, View view, long id)
//    {
//        String text = "Header " + ((TextView) view.findViewById(android.R.id.text1)).getText() + " was tapped.";
//        if (mToast == null)
//        {
//            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
//        }
//        else
//        {
//            mToast.setText(text);
//        }
//        mToast.show();
//    }
//
//    @Override
//    public boolean onHeaderLongClick(AdapterView<?> parent, View view, long id)
//    {
//        String text = "Header " + ((TextView) view.findViewById(android.R.id.text1)).getText() + " was long pressed.";
//        if (mToast == null)
//        {
//            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
//        }
//        else
//        {
//            mToast.setText(text);
//        }
//        mToast.show();
//        return true;
//    }
//    public interface OnAddContactListener
//    {
//        void onClick();
//    }
//    public void setAddContactCallBack(OnAddContactListener onClickListener)
//    {
//        this.onAddClickListener = onClickListener;
//    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION)
        {
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        switchBtn = (ImageView)view.findViewById(R.id.iv_switch);
        switchBtn.setOnClickListener(this);
        mGridView = (GridView) view.findViewById(R.id.asset_grid);
        mGridView.setOnItemClickListener(new OnItemClickListener()
        { 
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                int contactId = mContacts.get(position).getId();
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_SHOW_CONTACT_DETAIL, contactId);
            }
        });
        //hztest
        mGridView.setOnItemLongClickListener(new OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
               new PhoneListCallDialog(getActivity()).initPhone(mContacts.get(position)).show();
                return false;
            }
        });

        indexBar = (AzSideBar) view.findViewById(R.id.sideBar);
        TextView dialog = (TextView) view.findViewById(R.id.dialog);
        indexBar.setTextView(dialog);
        indexBar.setOnTouchingLetterChangedListener(new sideBarTouchEvent());
        
        
        simpleAdapter = new StickyGridHeadersSimpleArrayAdapter(parentActivity, R.layout.header,
                R.layout.adapter_contact_grid);
        mGridView.setAdapter(simpleAdapter);
        simpleAdapter.setGridClumns(mGridView.getNumColumns());
        refresh();
        
//        simpleAdapter.setEditOptions(new EditOptions()
//        {
//            @Override
//            public void deleteContact(int contactId)
//            {
//                Integer id = new Integer(contactId); 
//                if(mContacts.contains(id)){
//                    mContacts.remove(id);
//                    simpleAdapter.notifyDataSetChanged();
//                }
//            }
//        });
        
        ((StickyGridHeadersGridView) mGridView).setAreHeadersSticky(false);
        if (savedInstanceState != null)
        {
            mFirstVisible = savedInstanceState.getInt(KEY_LIST_POSITION);
        }

        mGridView.setSelection(mFirstVisible);

        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION))
        {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }

//        ((StickyGridHeadersGridView) mGridView).setOnHeaderClickListener(this);
//        ((StickyGridHeadersGridView) mGridView).setOnHeaderLongClickListener(this);

        setHasOptionsMenu(true);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setActivateOnItemClick(boolean activateOnItemClick)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        {
            mGridView.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
        }
    }

    @SuppressLint("NewApi")
    private void setActivatedPosition(int position)
    {
        if (position == ListView.INVALID_POSITION)
        {
            mGridView.setItemChecked(mActivatedPosition, false);
        }
        else
        {
            mGridView.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }
    private void refresh()
    {
        mContacts = UiApplication.getContactService().getContactList();
        ContactUtil.sortContacts(mContacts);
        simpleAdapter.initData(mContacts);
        
    }
    //暂时废弃，列表显示所有联系人
//    @Deprecated
//    private void initData(GroupModel GroupModel)
//    {
//        if (GroupModel == null)
//        {
//         return;   
//        }
////        this.GroupModel = GroupModel;
//        if (mContacts == null)
//        {
//            mContacts = new ArrayList<ContactModel>();
//        }else
//        {
//            mContacts.clear();
//        }
//        UilContactServiceImpl.getInstance().getContactAllChildren(GroupModel, mContacts);
//        UilContactServiceImpl.getInstance().sortContacts(mContacts);
//        if (simpleAdapter != null)
//        {
//            simpleAdapter.initData(mContacts);
//        }
//        simpleAdapter.notifyDataSetChanged();
//    }

    private class sideBarTouchEvent implements OnTouchingLetterChangedListener
    {
        @Override
        public void onTouchingLetterChanged(String s)
        {
            simpleAdapter.setGridClumns(mGridView.getNumColumns());
            int position = simpleAdapter.getPositionForSection(s.charAt(0));
            if (position != -1)
            {
                mGridView.setSelection(position);
            }
        }
    }
    
    @Override
    public void initComponentViews(View view)
    {
    }
    
    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_gridlist;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch(id)
        {
            case R.id.iv_switch:
                ((HomeActivity)parentActivity).loadContactView(false);
                break;
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if(id == UiEventEntry.REFRESH_CONTACT_VIEW)
        {
            refresh();
        }
    }
}
