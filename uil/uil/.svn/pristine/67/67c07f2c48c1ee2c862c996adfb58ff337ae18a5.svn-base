package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.ui.adapter.CallRecordListAdapter;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 
 * 说明：通话记录--全部
 *
 * @author  chaimb
 *
 * @Date 2015-5-8
 */

public class CallRecordAllFragment extends BaseCallRecordFragment
{
    private static final String TAG = CallRecordAllFragment.class.getName();

    public CallRecordAllFragment()
    {
        super();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_call_record_list;
    }

    @Override
    public void initComponentViews(View view)
    {
        Log.info(TAG, "initComponentViews::");
        callRecordListView = (ListView) view.findViewById(R.id.call_record_list);
        callRecordAdapter = new CallRecordListAdapter(getActivity(), callRecordListView, type);
        callRecordAdapter.setCallRecordListItems(callRecordListItems);
        callRecordListView.setAdapter(callRecordAdapter);
        setlistener();

    }

    @Override
    public void onStop()
    {
        Log.info(TAG, "onStop::");
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        Log.info(TAG, "onDestroy::");
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.info(TAG, "onCreateView::");
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_UPDATE_RIGHT_TAB, UiEventEntry.TAB_CALL_RECORD);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 
     * 方法说明 :监听事件
     * @author chaimb
     * @Date 2015-5-12
     */
    private void setlistener()
    {

        callRecordListView.setOnScrollListener(new OnScrollListener()
        {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
                switch (scrollState)
                {
                    case OnScrollListener.SCROLL_STATE_IDLE: // 停止
                        break;
                    case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:// 正在滑动
                        dialedLinearLayout.setVisibility(View.GONE);
                        showDialImageView.setImageResource(R.drawable.open_dialed_imageview);
                        break;
                    case OnScrollListener.SCROLL_STATE_FLING:// 正在滚动

                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {

            }

        });

    }

    /**
     * 刷新UI
     * 方法说明 :
     * @author chaimb
     * @Date 2015-5-15
     */
    public void changeData(final ArrayList<CallRecordListItem> callRecords)
    {
        Log.info(TAG, "changeData::callRecords::" + callRecords.size());
        if (callRecordAdapter != null && isVisible())
        {
            new Handler().post(new Runnable()
            {
                @Override
                public void run()
                {
                    callRecordListView.requestLayout();
                    callRecordListItems = callRecords;
                    callRecordAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void changePosition(int position)
    {
        if (callRecordListView != null)
        {
            callRecordListView.setSelection(position);
        }
    }

}
