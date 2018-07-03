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

/**
 * 
 * 说明：通话记录--未接
 *
 * @author  chaimb
 *
 * @Date 2015-5-8
 */

public class CallRecordMissedFragment extends BaseCallRecordFragment
{
    private final String TAG = CallRecordMissedFragment.class.getName();

    public CallRecordMissedFragment()
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
        callRecordListView = (ListView) view.findViewById(R.id.call_record_list);

        callRecordAdapter = new CallRecordListAdapter(getActivity(), callRecordListView, type);
        callRecordAdapter.setCallRecordListItems(callRecordListItems);
        callRecordListView.setAdapter(callRecordAdapter);
        setListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setListener()
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
                    callRecordListItems = (ArrayList<CallRecordListItem>) callRecords;
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
