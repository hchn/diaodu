package com.jiaxun.uil.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiaxun.uil.ui.screen.BaseActivity;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.comparator.PositionComparator;

/**
 * 说明: 所有普通碎片类的基类
 *
 * @author  hubin
 *
 * @Date 2015-2-28
 */

public abstract class BaseFragment extends Fragment
{
    private View layoutView;

    protected BaseActivity parentActivity;

    public PositionComparator positionComparator;

    public BaseFragment()
    {
        positionComparator = new PositionComparator();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        parentActivity = (BaseActivity) activity;
    }
    public View getRootView()
    {
        return layoutView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutView = inflater.inflate(getLayoutView(), null);
        if (layoutView == null)
        {
            return;
        }
//        layoutView.setBackgroundResource(R.drawable.right_pane_bg);
        initComponentViews(layoutView);
    }
    @Override
    public void onResume()
    {
        UiUtils.hideSoftKeyboard(parentActivity);
        super.onResume();
    }
    public void release()
    {
        
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return layoutView;
    }

    public abstract int getLayoutView();

    public abstract void initComponentViews(View view);

}
