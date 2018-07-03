package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.ui.adapter.CallRecordListAdapter;

/**
 * ËµÃ÷£º
 *
 * @author  chaimb
 *
 * @Date 2015-7-1
 */
public abstract class BaseCallRecordFragment extends BaseFragment
{

    public ListView callRecordListView;

    public ArrayList<CallRecordListItem> callRecordListItems;

    public CallRecordListAdapter callRecordAdapter;

    public LinearLayout dialedLinearLayout;
    public ImageView showDialImageView;
    public int type;

    public void setType(int type)
    {
        this.type = type;
    }

    public void setCallRecordListItems(ArrayList<CallRecordListItem> callRecordListItems)
    {
        this.callRecordListItems = callRecordListItems;
    }

    public void setDialedLinearLayout(LinearLayout dialedLinearLayout)
    {
        this.dialedLinearLayout = dialedLinearLayout;
    }

    public void setShowDialImageView(ImageView showDialImageView)
    {
        this.showDialImageView = showDialImageView;
    }


    @Override
    public int getLayoutView()
    {
        return 0;
    }

    @Override
    public void initComponentViews(View view)
    {

    }

    public abstract void changeData(ArrayList<CallRecordListItem> callRecordListItems);
    
    public abstract void changePosition(int position);
}
