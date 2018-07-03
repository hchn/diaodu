package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.handler.CallEventHandler;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.model.ConfMemberItem;
import com.jiaxun.uil.ui.adapter.ConfMemberAdapter;
import com.jiaxun.uil.ui.view.DraggableGridView;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：会议控制界面碎片
 *
 * @author  hubin
 *
 * @Date 2015-4-22
 */
public class ConfControlFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{
    private static final String TAG = ConfControlFragment.class.getName();
    public static final int UPDATE_LIST_TIMER = 0;
    public static final int UPDATE_LIST_ITEM = 1;
//    public static final int SELECT_FROM_CONTACT = 3;
//    public static final int SELECT_FROM_DIAL_PAD = 4;
    // 单呼入会
//    public static final int CALL_TO_CONF = 5;

    private Button mCloseConfBtn;
    private Button mLeaveConfBtn;
    private Button mBgmConfBtn;
    private Button mAddMemberBtn;
    private DraggableGridView membersView;

    private ConfMemberAdapter memberAdapter;

    private Handler mHandler;
    private String confId;
    private boolean isInConf = true;
    private boolean isBgmEnable;

    public ConfControlFragment()
    {
        Log.info(TAG, "ConfControlFragment::");
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CALL_TO_CONF);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CONF_MEMBER_ADD);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CLOSE_CONF_CONTROL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CONF_STATUS);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CONF_BGM);
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");
        mCloseConfBtn = (Button) view.findViewById(R.id.conf_close);
        mLeaveConfBtn = (Button) view.findViewById(R.id.conf_leave);
        mBgmConfBtn = (Button) view.findViewById(R.id.conf_bgm);
        mAddMemberBtn = (Button) view.findViewById(R.id.conf_add);

        mCloseConfBtn.setOnClickListener(this);
        mAddMemberBtn.setOnClickListener(this);
        mLeaveConfBtn.setOnClickListener(this);
        mBgmConfBtn.setOnClickListener(this);

        membersView = (DraggableGridView) view.findViewById(R.id.member_list);
        membersView.setType(CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO);
        memberAdapter = new ConfMemberAdapter(this, CallEventHandler.getConfMemberItems());
        memberAdapter.setListView(membersView);
        membersView.setAdapter(memberAdapter);
        membersView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.info(TAG, "onItemClick:: position:" + position);
                memberAdapter.triggerSelect(position);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.info(TAG, "onCreateView::");
        for (CallListItem callListItem : CallEventHandler.getCallList())
        {
            if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO
                    || callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
            {
                if (callListItem.getStatus() == CommonConstantEntry.CONF_STATE_EXIT)
                {
                    mLeaveConfBtn.setSelected(true);
                    mLeaveConfBtn.setText("入会");
                    mBgmConfBtn.setClickable(false);
                    mAddMemberBtn.setClickable(false);
                    isInConf = false;
                }
                confId = callListItem.getCallModel().getSessionId();
                break;
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE);
        if (UiApplication.getConfigService().isLocalCameralVisible())
        {
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_SHOW);
        }
        super.onStart();
    }

    @Override
    public void onStop()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE);
        if (UiApplication.getConfigService().isLocalCameralVisible())
        {
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_HIDE);
        }
        super.onStop();
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_CALL_TO_CONF);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_CONF_MEMBER_ADD);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_CLOSE_CONF_CONTROL);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_conf_control;
    }

    @Override
    public void onClick(final View v)
    {
        ((Button) v).setClickable(false);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                ((Button) v).setClickable(true);
            }
        }, 500);
        if (TextUtils.isEmpty(confId))
        {
            return;
        }
        switch (v.getId())
        {
            case R.id.conf_close:
                UiApplication.getConfService().confClose(confId);
                // 刷新呼叫列表
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_REMOVE_CONF_FROM_CALL_LIST);
                synchronized (CallEventHandler.getConfMemberItems())
                {
                    for (ConfMemberItem confMemModel : CallEventHandler.getConfMemberItems())
                    {
                        UiUtils.removeRemoteVideo(confMemModel.getContent());
                    }
                }
                break;
            case R.id.conf_add:
                Log.info(TAG, "conf_add::");
                ArrayList<Integer> selectedContactList = new ArrayList<Integer>();
                ArrayList<ConfMemberItem> confMemList = CallEventHandler.getConfMemberItems();
                for(ConfMemberItem confMem : confMemList)
                {
                    String num = confMem.getConfMemModel().getNumber();
                    ContactModel contact = UiApplication.getContactService().getContactByPhoneNum(num);
                    if(contact != null)
                    {
                        selectedContactList.add(contact.getId());
                    }
                }
                Bundle data = new Bundle();
                data.putInt("gridColumns", 5);
                data.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.NOTIFY_CONF_MEMBER_ADD);
                data.putIntegerArrayList("selectedContactList", selectedContactList);
                parentActivity.turnToFragmentStack(R.id.container_right_content, ContactSelectAddFragment.class, data);
                break;
            case R.id.conf_bgm:
                Log.info(TAG, "conf bgm::");
                UiApplication.getConfService().confBgmEnable(confId, !isBgmEnable);
                break;
            case R.id.conf_leave:
                if (isInConf)
                {
                    Log.info(TAG, "confLeave::");
                    UiApplication.getConfService().confLeave(confId);
                }
                else
                {
                    Log.info(TAG, "confEnter");
                    for (CallListItem callListItem : ServiceUtils.getCurrentCallList())
                    {
                        if (callListItem.getCallModel().isConfMember() && callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
                        {
                            ToastUtil.showToast("请先退出" + callListItem.getName() + "的会议，再尝试入会");
                            return;
                        }
                    }
                    ServiceUtils.handleConnectedCalls(confId, ServiceUtils.DEFAULT_CALL_TYPE);
                    UiApplication.getConfService().confEnter(confId);
                }
                break;
            default:
                break;
        }
    }

    private void addConfMember(String memberNum)
    {
        synchronized (CallEventHandler.getConfMemberItems())
        {
            if (!isMemberExist(memberNum))
            {
                ConfMemModel confMemModel = new ConfMemModel();
                confMemModel.setNumber(memberNum);
                ConfMemberItem confMemberItem = new ConfMemberItem();
                confMemberItem.setConfMemModel(confMemModel);
                confMemberItem.setIconRes(R.drawable.usericon);
                confMemberItem.setName(confMemModel.getNumber());
                confMemberItem.setContent(confMemModel.getNumber());
                confMemberItem.setConfId(getConfId());
                CallEventHandler.getConfMemberItems().add(confMemberItem);
                Log.info(TAG, "addConfMember:: memberNum:" + memberNum);
                UiApplication.getConfService().confUserAdd(getConfId(), memberNum);
            }
            else
            {
                ToastUtil.showToast(memberNum + "成员已添加");
            }
        }
    }

    public void setHandler(Handler handler)
    {
        this.mHandler = handler;
    }

    /**
     * 方法说明 : 更新列表单条记录
     * @param confMemberItem
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    private void updateListItem(ConfMemberItem confMemberItem)
    {
        Log.info(TAG, "updateListItem:: confMemberItem:" + confMemberItem.getName() + " status:" + confMemberItem.getConfMemModel().getStatus());
        if (mHandler != null)
        {
            Message msg = mHandler.obtainMessage();
            msg.what = UPDATE_LIST_ITEM;
            msg.obj = confMemberItem;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.NOTIFY_CONF_MEMBER_ADD)
        {
            Log.info(TAG, "MESSAGE_NOTIFY_CONF_MEMBER_ADD");
            int type = (Integer) args[0];
            if (type == UiEventEntry.CONTACT_SELECTEADD_CONTACT)
            {
                int operaCode = (Integer) args[1];
                if (operaCode == 0)
                {
                    parentActivity.backToPreFragment(R.id.container_right_content);
                    return;
                }
                else
                {
                    ArrayList<Integer> selectContactList = (ArrayList<Integer>) args[2];
                    for (Integer contactId : selectContactList)
                    {
                        ContactModel contactModel = UiApplication.getContactService().getContactById(contactId);
                        String confNum = contactModel.getConfNum();
                        if (TextUtils.isEmpty(confNum))
                        {
                            continue;
                        }

                        addConfMember(confNum);
                    }
                }

            }
            else if (type == UiEventEntry.CONTACT_SELECTEADD_DIAL)
            {
                String inputNum = (String) args[1];
                if (TextUtils.isEmpty(inputNum))
                {
                    return;
                }
                else
                {
                    addConfMember(inputNum);
                }
            }

            if (memberAdapter != null)
            {
                memberAdapter.notifyDataSetChanged();
            }

            // 两层返回到原界面
            parentActivity.backToPreFragment(R.id.container_right_content);
            parentActivity.backToPreFragment(R.id.container_right_content);
        }
        else if (id == UiEventEntry.NOTIFY_CALL_TO_CONF)
        {
            String memberNum = (String) args[0];
            synchronized (CallEventHandler.getConfMemberItems())
            {
                for (ConfMemberItem confMemberItem : CallEventHandler.getConfMemberItems())
                {
                    String num = confMemberItem.getConfMemModel().getNumber();
                    if (memberNum.equals(num))
                    {
                        return;
                    }
                }
                ConfMemModel confMemModel = new ConfMemModel();
                confMemModel.setNumber(memberNum);
                ConfMemberItem confMemberItem = new ConfMemberItem();
                confMemberItem.setConfMemModel(confMemModel);
                confMemberItem.setIconRes(R.drawable.usericon);
                confMemberItem.setName(confMemModel.getNumber());
                confMemberItem.setContent(confMemModel.getNumber());
                confMemberItem.setConfId(getConfId());
                CallEventHandler.getConfMemberItems().add(confMemberItem);
            }
        }
        else if (id == UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE)
        {
            if ((Boolean) args[0])
            {
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_SHOW);
            }
            else
            {
                EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_HIDE);
            }
        }
        else if (id == UiEventEntry.NOTIFY_CLOSE_CONF_CONTROL)
        {
            Log.info(TAG, "NOTIFY_CLOSE_CONF_CONTROL: ");
            if (parentActivity != null)
            {
                parentActivity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
//                        parentActivity.backToPreFragment(R.id.container_right_content);
                        parentActivity.removeFragmentFromBackStack(R.id.container_right_content, ConfControlFragment.class);
                    }
                });
            }
        }
        else if (id == UiEventEntry.NOTIFY_CONF_MEMBER_ITEM_CHANGE)
        {
            Log.info(TAG, "NOTIFY_CONF_MEMBER_ITEM_CHANGE");
            updateListItem((ConfMemberItem) args[0]);
        }
        else if (id == UiEventEntry.NOTIFY_CONF_STATUS)
        {
            isInConf = (Boolean) args[0];
            Log.info(TAG, "NOTIFY_CONF_STATUS: isInConf:" + isInConf);
            if (isVisible())
            {
                mLeaveConfBtn.setSelected(!isInConf);
                mLeaveConfBtn.setText(isInConf ? "退会" : "入会");
                mBgmConfBtn.setClickable(isInConf);
                mAddMemberBtn.setClickable(isInConf);
                ToastUtil.showToast(String.format("成功%s会议", isInConf ? "进入" : "离开"));
            }
        }
        else if (id == UiEventEntry.NOTIFY_CONF_BGM)
        {
            isBgmEnable = (Boolean) args[0];
            Log.info(TAG, "NOTIFY_CONF_BGM: isBgmEnable:" + isBgmEnable);
            if (isVisible())
            {
                mBgmConfBtn.setSelected(isBgmEnable);
                mBgmConfBtn.setText(isBgmEnable ? "关提示音" : "开提示音");
                ToastUtil.showToast(String.format("成功%s", isBgmEnable ? "开提示音" : "关提示音"));
            }
        }
    }

    private boolean isMemberExist(String memberNum)
    {
        for (ConfMemberItem confMemModel : CallEventHandler.getConfMemberItems())
        {
            if (confMemModel.getName().equals(memberNum))
            {
                return true;
            }
        }
        return false;
    }

    public String getConfId()
    {
        return confId;
    }
}
