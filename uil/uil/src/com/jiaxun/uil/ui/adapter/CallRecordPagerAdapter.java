package com.jiaxun.uil.ui.adapter;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.ui.fragment.BaseCallRecordFragment;

/**
 * 
 * 说明：列表适配
 *
 * @author  chaimb
 *
 * @Date 2015-5-6
 */

public class CallRecordPagerAdapter extends PagerAdapter
{

    private static final String TAG = CallRecordPagerAdapter.class.getName();

    private List<BaseCallRecordFragment> fragments;

    private FragmentManager fragmentManager;

    public CallRecordPagerAdapter(FragmentManager fragmentManager, List<BaseCallRecordFragment> fragments)
    {
        Log.info(TAG, "CallRecordPageradapter::");
        this.fragmentManager = fragmentManager;
        this.fragments = fragments;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1)
    {
        return arg0 == arg1;
    }

    @Override
    public int getCount()
    {
//        Log.info(TAG, "getCount::" + fragments.size());
        return fragments.size();
    }

    @Override
    public void destroyItem(View container, int position, Object object)
    {
        Log.info("destroy==>>", "destroyItem::position::" + position);
        ((ViewPager) container).removeView(fragments.get(position).getView());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        Log.info(TAG, "instantiateItem::position" + position);
        Fragment fragment = fragments.get(position);
        Log.info(TAG, "fragment.isAdded()::" + fragment.isAdded());
        if (!fragment.isAdded())
        { // 如果fragment还没有added
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commit();
            /**
             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
             * 会在进程的主线程中,用异步的方式来执行。
             * 如果想要立即执行这个等待中的操作,就要调用这个方法(只能在主线程中调用)。
             * 要注意的是,所有的回调和相关的行为都会在这个调用中被执行完成,因此要仔细确认这个方法的调用位置。
             */
            fragmentManager.executePendingTransactions();
        }

        Log.info(TAG, "fragment.getView().getParent()::" + fragment.getView().getParent());
//        if (fragment.getView().getParent() == null)
//        {
////            Log.info(TAG, "container::" + container);
        container.addView(fragment.getView()); // 为viewpager增加布局
//        }

        return fragment.getView();
    }

}
