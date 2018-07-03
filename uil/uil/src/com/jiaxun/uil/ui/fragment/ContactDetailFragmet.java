package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.scl.model.SCallModel;
import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.ui.adapter.ContactDetailAdapter;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：通讯录联系人的详细信息
 *
 * @author  hezhen
 *
 * @Date 2015-3-11
 */
public class ContactDetailFragmet extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{
    public static final String TAG = ContactDetailFragmet.class.getSimpleName();
//    public static final String ARG_ITEM_ID = "item_id";
    private ImageView detailIcon;// 用户详情里面用户图标
    private TextView userTypeName;
    private TextView userName;// 用户名称
    private ListView phoneList;//
    private ContactDetailAdapter contactDetailAdapter;
    private ContactModel mContactModel;
    private Button turnCallBtn;
    private RelativeLayout turnCallRelay;
    private boolean isTurnCall;
    private int countNum = 0;
    private ArrayList<String> phones;
    private String phoneNum;// 当前呼叫的号码
    private boolean isAutoHangup = false; // 轮呼中自动挂断
    private boolean isOnlyShow;
    private int mContactId;

    public ContactDetailFragmet()
    {
        phones = new ArrayList<String>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v)
    {
        if (v == turnCallBtn)
        {
            Log.info(TAG, "click:turnCall");
            countNum = 0;
            isTurnCall = true;
            phoneNum = phones.get(countNum);
            EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CONTACT_MAKE_TURN_CALL);
            ServiceUtils.makeCall(parentActivity, phoneNum);
            startOnekeyCallTime();
        }
    }

    @Override
    public void initComponentViews(View view)
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_MODIFY_CONTACT);
        int contactId = -1;
        if (getArguments() != null)
        {
            contactId = getArguments().getInt(CommonConstantEntry.DATA_CONTACT_ID,-1);
            isOnlyShow = getArguments().getBoolean(CommonConstantEntry.DATA_ENABLE);
        }

        detailIcon = (ImageView) view.findViewById(R.id.detail_user_image);
        userTypeName = (TextView) view.findViewById(R.id.detail_user_typename);
        userName = (TextView) view.findViewById(R.id.detail_user_name);
        turnCallBtn = (Button) view.findViewById(R.id.btn_turn_call);
        turnCallRelay = (RelativeLayout)view.findViewById(R.id.relay_turn_call);
        phoneList = (ListView) view.findViewById(R.id.detail_list);
        contactDetailAdapter = new ContactDetailAdapter(getActivity());
        phoneList.setAdapter(contactDetailAdapter);
        turnCallBtn.setOnClickListener(this);
//        if (phones.size() <= 1)
//        {
//            turnCallRelay.setVisibility(View.GONE);
//        }
       initData(contactId);
    }

    @Override
    public void onDestroy()
    {
        Log.info(TAG, "onDestroy");
        super.onDestroy();
        release();
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_MODIFY_CONTACT);
        if (!isTurnCall)
        {
            EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CONTACT_MAKE_TURN_CALL);
            UiApplication.applicationHandler.removeCallbacks(runnable);
        }
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_contact_detail;
    }

    private void initData(int contactId)
    {
        mContactId = contactId;
        ContactModel contact = UiApplication.getContactService().getContactById(contactId);
        if (contact != null)
        {
            mContactModel = contact;
        }
        else
        {
            mContactModel = new ContactModel();
        }

        viewSet();

        ArrayList<ContactNum> phoneNumList = mContactModel.getPhoneNumList();
        phones.clear();
        for (ContactNum contactNum : phoneNumList)
        {
            phones.add(contactNum.getNumber());
        }
        if (turnCallRelay != null)
        {
            if (isOnlyShow)
            {
                turnCallRelay.setVisibility(View.GONE);
                contactDetailAdapter.setOnlyShow(isOnlyShow);
            }else
            {
                turnCallRelay.setVisibility(phones.size() <= 1 ? View.GONE : View.VISIBLE); 
            }
        }
       
    }

    private void viewSet()
    {
        if (mContactModel != null)
        {
            String name = mContactModel.getName();
            if (userName != null)
            {
                userTypeName.setText(mContactModel.getTypeName());
                userName.setText(name);
                List<BaseListItem> listItems = new ArrayList<BaseListItem>();
                List<ContactNum> dataSrc = mContactModel.getPhoneNumList();
                for(ContactNum contactNum : dataSrc)
                {
                    BaseListItem baseListItem = new BaseListItem();
                    String typeName = "";
                    String phoneNum = contactNum.getNumber();
                    if(phoneNum.equals(mContactModel.getConfNum()))
                    {
                        typeName = "*";
                    }
                    baseListItem.setName(typeName + contactNum.getTypeName());
                    baseListItem.setContent(contactNum.getNumber());
                    baseListItem.setData1(contactNum.getTypeName());
                    listItems.add(baseListItem);
                }
                contactDetailAdapter.setData(listItems);
            }
        }
    }

    private void startOnekeyCallTime()
    {
        long timeLen = UiUtils.loadOneKeyMultNumTime(parentActivity);
        UiApplication.applicationHandler.removeCallbacks(runnable);
        if (timeLen != 0)
        {
            UiApplication.applicationHandler.postDelayed(runnable, timeLen);
        }
    }

    private Runnable runnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (isTurnCall)
            {
                if (!TextUtils.isEmpty(phoneNum))
                {
                    String sessionId = null;
                    for (CallListItem callListItem : ServiceUtils.getCurrentCallList())
                    {
                        if (phoneNum.equals(((SCallModel) callListItem.getCallModel()).getPeerNum()))
                        {
                            sessionId = callListItem.getCallModel().getSessionId();
                            break;
                        }
                    }
                    UiApplication.getSCallService().sCallRelease(sessionId, CommonConstantEntry.CALL_END_HUANGUP);
                    isAutoHangup = true;
                }
            }
        }
    };

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.CONTACT_MAKE_TURN_CALL && isTurnCall)
        {
            if (isAutoHangup)
            {
                isAutoHangup = false;
            }
            else
            {
                int reason = ((Integer) args[0]).intValue();
                // 被叫主动挂断 主叫主动挂断 不继续呼叫 被叫无相应
                if (reason == CommonConstantEntry.CALL_END_PEER_BUSY || reason == CommonConstantEntry.CALL_END_HUANGUP
                        || reason == CommonConstantEntry.CALL_END_OFFLINE ||  reason == 999)//999 通话成功建立后 结束轮呼
                {
                    isTurnCall = false;
                    countNum = 0;
                    release();
                    return;
                }
            }

            if (phones != null)
            {
                countNum++;
                if (phones.size() > countNum)
                {
                    phoneNum = phones.get(countNum);
                    ServiceUtils.makeCall(parentActivity, phoneNum);
                    startOnekeyCallTime();
                }
                else
                {
                    isTurnCall = false;
                    countNum = 0;
                    release();
                }
            }
        }
        else if (id == UiEventEntry.SETTING_MODIFY_CONTACT)
        {
            initData(mContactId);
        }
    }

}
