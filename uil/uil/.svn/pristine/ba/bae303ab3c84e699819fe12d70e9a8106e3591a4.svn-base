package com.jiaxun.uil.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.SparseArray;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;

/**
 * 说明：
 *
 * @author  HeZhen
 *
 * @Date 2015-6-1
 */
public class FgManager
{
    private static final String TAG = FgManager.class.getName();
    // 资源栈
    private static Map<Integer, SparseArray<Fragment>> fragmentsStack = new HashMap<Integer, SparseArray<Fragment>>();
    // 回退栈
    private static Map<Integer, ArrayList<Fragment>> fragmentsStackForBack = new HashMap<Integer, ArrayList<Fragment>>();
    private static final int singleTop = 0;
    private static final int singleTask = 1;
    private static final int standard = 2;
    private static final int stackMode = singleTask;
    
    public static void pushToInitStack(Class<? extends Fragment> toFragmentClass)
    { 
        pushToStack(R.id.fragment_stack, toFragmentClass);
    }
    
    /**
     * 方法说明 :从初始化栈中获取fragment
     * @param toFragmentClass
     * @return
     * @author hz
     * @Date 2015-10-14
     */
    private static Fragment getFgFromInitStack(Class<? extends Fragment> toFragmentClass)
    {
        String toTag = toFragmentClass.getSimpleName();
        int toTagHashCode = toTag.hashCode();
        SparseArray<Fragment> fragments = null;
        int stackId = R.id.fragment_stack;
        if (fragmentsStack.containsKey(stackId))
        {
            fragments = fragmentsStack.get(stackId);
            Fragment toFragment = fragments.get(toTagHashCode);
            return toFragment;
        }
        return null;
    }
    private static boolean isContainInitStack(Fragment fragment)
    {
        SparseArray<Fragment> fragments = null;
        int stackId = R.id.fragment_stack;
        if (fragmentsStack.containsKey(stackId))
        {
            fragments = fragmentsStack.get(stackId);
            return fragments.indexOfValue(fragment) != -1;
        }
        return false;
    }
    /*
     * 加入资源栈中，初始化Fragment，可以通过EventNotifyHelper更新fragment的数据信息。但界面信息比如控件尚未初始化，不要使用
     * 。
     * 在界面显示时，把引用数据更新界面即可。
     */
    private static Fragment pushToStack(int containerViewId, Class<? extends Fragment> toFragmentClass)
    {
        String toTag = toFragmentClass.getSimpleName();
        int toTagHashCode = toTag.hashCode();
        Log.info(TAG, "pushToStack:: toTag:" + toTag + ",toTagHashCode = " + toTagHashCode);
        SparseArray<Fragment> fragments = null;
        // 如果资源栈中有这个容器id
        if (fragmentsStack.containsKey(containerViewId))
        {
            fragments = fragmentsStack.get(containerViewId);
        }
        else
        {
            fragments = new SparseArray<Fragment>();
            fragmentsStack.put(containerViewId, fragments);
        }
        Fragment toFragment = fragments.get(toTagHashCode);
        //如果这个fragment不在容器中，存入容器中
        if (toFragment == null)
        {
            //对应Id栈中没有 则初始资源栈中是否存在
            toFragment = getFgFromInitStack(toFragmentClass);
            if(toFragment != null)
            {
                fragments.put(toTagHashCode, toFragment);
                return toFragment;
            }
            try
            {
                toFragment = toFragmentClass.newInstance();
                fragments.put(toTagHashCode, toFragment);
            }
            catch (java.lang.InstantiationException e)
            {
                e.printStackTrace();
                return null;
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
                return null;
            }
        }
        return toFragment;
    }
    public static void removeFragmentFromBackStack(FragmentManager fm,int containerViewId, Class<? extends Fragment> toFragmentClass)
    {
        SparseArray<Fragment> fragments = fragmentsStack.get(containerViewId);
        if(fragments == null || fragments.size() == 0 )
        {
            return;
        }
        ;
        int toTagHashCode = toFragmentClass.getSimpleName().hashCode();
        Fragment fragment = fragments.get(toTagHashCode);
        if(fragment == null)
        {
            return;
        }
        ArrayList<Fragment> fragmentList = null;
        if (fragmentsStackForBack.containsKey(containerViewId))
        {
            fragmentList = fragmentsStackForBack.get(containerViewId);
        }
        if(fragmentList == null)
        {
            return;
        }
        fragmentList.remove(fragment);
        
        int size = fragmentList.size();

        if (size >= 1)
        {
            Fragment fg = fragmentList.get(size - 1);
            turnToFragmentStack(fm, containerViewId, fg.getClass(), null);
        }
        else
        {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerViewId, new Fragment()).commitAllowingStateLoss();
        }
    }
    /**
     * 方法说明 :跳转到Fragment 加入栈中
     * @param fm
     * @param containerViewId
     * @param toFragmentClass
     * @param args
     * @author HeZhen
     * @Date 2015-6-6
     */
    public static void turnToFragmentStack(FragmentManager fm, int containerViewId, Class<? extends Fragment> toFragmentClass, Bundle args)
    {
        // 从初始化的资源栈中 取出，
        Fragment toFragment = getFgFromInitStack(toFragmentClass);
        //初始资源栈中无论有没有都加到对应ID的资源栈中
        toFragment = pushToStack(containerViewId, toFragmentClass);
        if (toFragment == null)
        {
            return;
        }
        ArrayList<Fragment> fragmentList = null;
        if (fragmentsStackForBack.containsKey(containerViewId))
        {
            fragmentList = fragmentsStackForBack.get(containerViewId);
        }
        else
        {
            fragmentList = new ArrayList<Fragment>();
            fragmentsStackForBack.put(containerViewId, fragmentList);
        }
        switch (stackMode)
        {
            case singleTask:
                if (fragmentList.contains(toFragment))
                {
                    fragmentList.remove(toFragment);
                }
                fragmentList.add(toFragment);
                break;
            case standard:
                fragmentList.add(toFragment);
                break;
            case singleTop:
                int size = fragmentList.size();
                if (!(size > 0 && fragmentList.get(size - 1) == toFragment))
                {
                    fragmentList.add(toFragment);
                }
                break;
        }

        // 如果有参数传递，
        if ((args != null) && (!args.isEmpty()))
        {
            if (toFragment.isVisible())
            {
                toFragment.getArguments().putAll(args);
            }
            else
            {
                toFragment.setArguments(args);
            }
        }

        if (!toFragment.isVisible())
        {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerViewId, toFragment);
            ft.commitAllowingStateLoss();
        }
    }
    /**
     * 方法说明 :获取
     * @param containerViewId
     * @return
     * @author hz
     * @Date 2015-10-8
     */
    public static Fragment getPreFragment(int containerViewId)
    {
        if (fragmentsStackForBack.containsKey(containerViewId))
        {
            Fragment fromFragment = null; 
            ArrayList<Fragment> fragments = null;
            fragments = fragmentsStackForBack.get(containerViewId);
            int size = fragments.size();

            if (size > 1)
            {
                fromFragment = fragments.get(size - 2);
            }
            return fromFragment;
        }
        return null;
    }
    /**跳转到上一个Fragment*/
    public static boolean backToPreFragment(FragmentManager fm, int containerViewId)
    {
        Fragment fromFragment = null; 
        ArrayList<Fragment> fragments = null;
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentsStackForBack.containsKey(containerViewId))
        {
            fragments = fragmentsStackForBack.get(containerViewId);
            int size = fragments.size();

            if (size > 1)
            {
                fromFragment = fragments.get(size - 2);
                fragments.remove(size - 1);
            }
            else
            {
                fragments.clear();
                ft.replace(containerViewId, new Fragment()).commitAllowingStateLoss();
                return true;
            }
        }

        if (fromFragment != null)
        {
            ft.replace(containerViewId, fromFragment).commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    /**添加新创建的Fragment,不长久保留数据所以不添加到资源栈中，根据需要 选择是否加入回退栈中*/
    public static void turnToNewFragment(FragmentManager fm, int containerViewId, Class<? extends Fragment> toFragmentClass, boolean addBackStack, Bundle args)
    {
        Fragment toFragment = null;
        try
        {
            toFragment = toFragmentClass.newInstance();
            toFragment.setArguments(args);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(containerViewId, toFragment).commitAllowingStateLoss();
            // 加到回退栈中
            if (addBackStack)
            {
                ArrayList<Fragment> fragmentList = null;
                if (fragmentsStackForBack.containsKey(containerViewId))
                {
                    fragmentList = fragmentsStackForBack.get(containerViewId);
                }
                else
                {
                    fragmentList = new ArrayList<Fragment>();
                    fragmentsStackForBack.put(containerViewId, fragmentList);
                }
                switch (stackMode)
                {
                    case singleTask:
                        if (fragmentList.contains(toFragment))
                        {
                            fragmentList.remove(toFragment);
                        }
                        fragmentList.add(toFragment);
                        break;
                    case standard:
                        fragmentList.add(toFragment);
                        break;
                    case singleTop:
                        int size = fragmentList.size();
                        if (!(size > 0 && fragmentList.get(size - 1) == toFragment))
                        {
                            fragmentList.add(toFragment);
                        }
                        break;
                }
            }
        }
        catch (java.lang.InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**清除某个容器栈中fragment*/
    public static void clearFragmentStack(int containerViewId)
    {
        Log.info(TAG, "clearFragmentStack:: containerViewId:" + containerViewId);
        SparseArray<Fragment> fragments = fragmentsStack.get(containerViewId);
        if (fragments != null)
        {
            int size = fragments.size();
            for (int i = 0; i < size; i++)
            {
                Fragment fragment = fragments.valueAt(i);
                if(isContainInitStack(fragment))
                {
                    continue;
                }
                if (fragment instanceof BaseFragment)
                {
                    ((BaseFragment) fragment).release();
                }
//                fragments.removeAt(i);
                fragment = null;
            }
            fragments.clear();
        }
        ArrayList<Fragment> fragmentList = fragmentsStackForBack.get(containerViewId);
        if (fragmentList != null)
        {
            for (Fragment fragment : fragmentList )
            {
                if(isContainInitStack(fragment))
                {
                    continue;
                }
                if (fragment instanceof BaseFragment)
                {
                    ((BaseFragment) fragment).release();
                }
                fragment = null;
            }
            fragmentList.clear();
        }

    }
}
