package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：
 *
 * @author  chaimb
 *
 * @Date 2015-9-21
 */
public class ContactSelectAddFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{

    private static final String TAG = ContactSelectAddFragment.class.getName();
    private TextView addressListAddTextView;
    private TextView dialAddTextView;

    private Button cancelButton;
    private Button sureButton;

    private int columns = -1;
    private int callBack = 0;
    private int showType = 0;
    
    ArrayList<Integer> selectedContactList;
    
    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_select_add_contact;
    }

    @Override
    public void initComponentViews(View view)
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CONTACT_SELECTEADD_CONTACT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CONTACT_SELECTEADD_DIAL);
        Log.info(TAG, "initComponentViews::");
        if (getArguments() != null)
        {
            columns = getArguments().getInt("gridColumns");
            callBack = getArguments().getInt(CommonConstantEntry.DATA_TYPE,0);
            showType = getArguments().getInt("showType",0);
            selectedContactList = getArguments().getIntegerArrayList("selectedContactList");
        }

        addressListAddTextView = (TextView) view.findViewById(R.id.address_list_add_textview);
        dialAddTextView = (TextView) view.findViewById(R.id.dial_add_textview);
        cancelButton = (Button) view.findViewById(R.id.btn_cancel);
        sureButton = (Button) view.findViewById(R.id.btn_create);
        sureButton.setVisibility(View.GONE);
//        contactEdit.setVisibility(View.GONE);
//        sureButton.setText("下一步");

        setListener();
    }

    private void setListener()
    {
        cancelButton.setOnClickListener(this);
        sureButton.setOnClickListener(this);
        addressListAddTextView.setOnClickListener(this);
        dialAddTextView.setOnClickListener(this);

    }
    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CONTACT_SELECTEADD_CONTACT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CONTACT_SELECTEADD_DIAL);
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_cancel:
                parentActivity.backToPreFragment(R.id.container_right_content);
                break;
            case R.id.address_list_add_textview:
                // 从通讯录添加
                if (columns != -1)
                {
                    loadContactSelectView(columns);
                }
                break;
            case R.id.dial_add_textview:
//                parentActivity.removeFragmentFromBackStack(R.id.container_right_content, ContactSelectAddFragment.class);
                // 从拨号盘添加
                loadSimpleDial();
                break;

            default:
                break;
        }
    }

    /**
     * 方法说明 :从通讯录添加
     * @param eventType
     * @param columns
     * @author chaimb
     * @Date 2015-9-21
     */
    private void loadContactSelectView(int columns)
    {
        Bundle data = new Bundle();
        data.putInt("gridColumns", columns);
        data.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.CONTACT_SELECTEADD_CONTACT);
        data.putInt("showType", showType);
        data.putIntegerArrayList("selectedContactList", selectedContactList);
        parentActivity.turnToFragmentStack(R.id.container_right_content, ContactSelectFragment.class, data);
    }

    /**
     * 方法说明 : 显示只包含数字输入功能的拨号盘，目前应用于会议追加成员
     * @return void
     * @author hubin
     * @Date 2015-9-21
     */
    private void loadSimpleDial()
    {
        Bundle data = new Bundle();
        data.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.CONTACT_SELECTEADD_DIAL);
        parentActivity.turnToFragmentStack(R.id.container_right_content, DialSimpleFragment.class, data);
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if(id == UiEventEntry.CONTACT_SELECTEADD_CONTACT)
        {
            int operaCode = (Integer)args[0];
            EventNotifyHelper.getInstance().postNotificationName(callBack,UiEventEntry.CONTACT_SELECTEADD_CONTACT,operaCode,operaCode == 0?null:args[1]);
        }else if(id == UiEventEntry.CONTACT_SELECTEADD_DIAL)
        {
            EventNotifyHelper.getInstance().postNotificationName(callBack,UiEventEntry.CONTACT_SELECTEADD_DIAL,args[0]);
        }
    }

}
