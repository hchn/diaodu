package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.handler.CallEventHandler;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.ui.adapter.CallListAdapter;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：呼叫列表
 *
 * @author  hubin
 *
 * @Date 2015-4-1
 */
public class CallListFragment extends BaseFragment implements NotificationCenterDelegate
{
    private static final String TAG = CallListFragment.class.getName();
    public static final String CALL_LIST_DATA = "CALL_LIST_DATA";
//    public static final int UPDATE_LIST_TIMER = 0;
//    public static final int UPDATE_LIST_ITEM = 1;
    private ListView callListView;
    private CallListAdapter listAdapter;
//    private ArrayList<CallListItem> callList = new ArrayList<CallListItem>();
    private Timer callTimer;

    private boolean isShow = false;
    
    private String callToConfNum = null;

    public CallListFragment()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CALL_LIST_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SCALL_RELEASE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SCALL_AUDIO_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CONF_RELEASE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_SCALL_TO_CONF);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SCALL_TO_CONF);
    }

    @Override
    public void initComponentViews(View view)
    {
        if (CallEventHandler.getCallList().size() > 0)
        {
            isShow = false;
        }
        else
        {
            isShow = true;
        }
        Log.info(TAG, "initComponentViews::");
        callListView = (ListView) view.findViewById(R.id.call_list);
        listAdapter = new CallListAdapter(parentActivity);
        resetListData();
        callListView.setAdapter(listAdapter);
    }

    /**
     * 方法说明 : 更新列表单行记录状态
     * @param callListItem
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    private void updateSingleRow(CallListItem callListItem)
    {
        if (callListView != null)
        {
            int start = callListView.getFirstVisiblePosition();
            for (int i = start, j = callListView.getLastVisiblePosition(); i <= j; i++)
            {
                if (callListItem == callListView.getItemAtPosition(i))
                {
                    View view = callListView.getChildAt(i - start);
                    listAdapter.getView(i, view, callListView);
                    break;
                }
            }
        }
    }

    private void resetListData()
    {
        listAdapter.setListItem((ArrayList<CallListItem>) CallEventHandler.getCallList().clone());
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_call_list;
    }

    /**
     * 方法说明 : 更新列表单条记录
     * @param callListItem
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    private void updateListItem(CallListItem callListItem)
    {
        Log.info(TAG, "updateListItem::");
        updateSingleRow(callListItem);
    }

    private void updateListItems()
    {
        Log.info(TAG, "updateListItems::");
        if (isVisible())
        {
            resetListData();
        }
        else
        {
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_SHOW_CALL_LIST);
        }
    }
    
    @Override
    public void onStart()
    {
        Log.info(TAG, "onStart::");
        super.onStart();
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_REMOVE_CONF_FROM_CALL_LIST);
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_UPDATE_RIGHT_TAB, UiEventEntry.TAB_CALL_LIST);
        if (callTimer == null)
        {
            callTimer = new Timer();
            callTimer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    new Handler(UiApplication.getInstance().getMainLooper()).post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }, 0, 1000);
        }
        if (UiApplication.getConfigService().isLocalCameralVisible())
        {
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_SHOW);
        }
    }

    @Override
    public void onStop()
    {
        isShow = true;
        Log.info(TAG, "onStop::");
        super.onStop();
        if (callTimer != null)
        {
            callTimer.cancel();
        }
        callTimer = null;
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_REMOVE_CONF_FROM_CALL_LIST);

        if (UiApplication.getConfigService().isLocalCameralVisible())
        {
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_LOCAL_VIDEO_HIDE);
        }
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.EVENT_LOCAL_VIDEO_CHANGE)
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
        else if (id == UiEventEntry.NOTIFY_REMOVE_CONF_FROM_CALL_LIST)
        {
            if (isVisible())
            {
                resetListData();
            }
        }
        else if (id == UiEventEntry.NOTIFY_CALL_LIST_CHANGE)
        {
            updateListItems();
        }
        else if (id == UiEventEntry.NOTIFY_CALL_LIST_ITEM_CHANGE)
        {
            updateListItem((CallListItem) args[0]);
        }
        else if (id == UiEventEntry.NOTIFY_SCALL_RELEASE)
        {
            Log.info(TAG, "call_release:: isShow = " + isShow);
            if (!isShow && CallEventHandler.getCallList().size() == 0)
            {
                parentActivity.backToPreFragment(R.id.container_right_content);
            }
            else
            {
                resetListData();
            }
        }
        else if (id == UiEventEntry.NOTIFY_CONF_RELEASE)
        {
            if (isVisible())
            {
                resetListData();
            }
        }
        else if (id == UiEventEntry.NOTIFY_SCALL_AUDIO_CHANGE)
        {
            if (isVisible())
            {
                resetListData();
            }
        }else if(id == UiEventEntry.EVENT_SCALL_TO_CONF)
        {
            callToConfNum = (String) args[0];
            Bundle data = new Bundle();
            data.putInt("gridColumns", 5);
            data.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.NOTIFY_SCALL_TO_CONF);
            parentActivity.turnToFragmentStack(R.id.container_right_content, ContactSelectFragment.class, data);
        }else if(id == UiEventEntry.NOTIFY_SCALL_TO_CONF)
        {
            Log.info(TAG, "NOTIFY_SCALL_TO_CONF");
            parentActivity.backToPreFragment(R.id.container_right_content);
            int operaCode = (Integer)args[0];
            if(operaCode == 0)
            {
                
            }else
            {
                ArrayList<Integer> selectContactList = (ArrayList<Integer>) args[1];
                ArrayList<String> numberList = new ArrayList<String>();
                if (!TextUtils.isEmpty(callToConfNum))
                {
                    numberList.add(callToConfNum);
                }

                for (Integer contactId : selectContactList)
                {
                    ContactModel contactModel = UiApplication.getContactService().getContactById(contactId);
                    String number = contactModel.getConfNum();
                    if (!TextUtils.isEmpty(number) && !numberList.contains(number))
                    {
                        numberList.add(number);
                    }
                }
                ServiceUtils.makeConf(parentActivity, "临时会议", numberList);
            }
            callToConfNum = null;
        }
    }

}
