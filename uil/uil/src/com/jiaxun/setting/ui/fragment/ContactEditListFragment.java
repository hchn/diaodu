package com.jiaxun.setting.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jiaxun.setting.adapter.SettingAdapter;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsRightItem;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * 说明：联系人配置列表
 * 
 * @author HeZhen
 * 
 * @Date 2015-5-29
 */
public class ContactEditListFragment extends ListFragment
{
    private List<PrefsBaseItem> settingData = null;
    private SettingAdapter settingAdapter = null;

    protected SettingActivity parentActivity;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initData();
        settingAdapter = new SettingAdapter(getActivity(), settingData);
        setListAdapter(settingAdapter);
        parentActivity = (SettingActivity) getActivity();
    }

    public void initData()
    {
        settingData = new ArrayList<PrefsBaseItem>();
        // settingData.add(new PrefsGroupItem(ViewHolderGroup.ITEM_VIEW_TYPE,
        // "帐户设置"));
        settingData.add(new PrefsRightItem("联系人配置", -1, null));
        settingData.add(new PrefsRightItem("按键配置", -1, null));
        settingData.add(new PrefsRightItem("用户状态订阅配置", -1, null));
        if (UiApplication.getAtdService().isAtdAdminLogin())
        {// 管理员帐号登录
            settingData.add(new PrefsRightItem("导入联系人", -1, null));
            settingData.add(new PrefsRightItem("导入按键", -1, null));
            settingData.add(new PrefsRightItem("导出联系人", -1, null));
            settingData.add(new PrefsRightItem("导出按键", -1, null));
//            settingData.add(new PrefsContactItem("黑白名单", -1));
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        if (parentActivity == null)
        {
            return;
        }
        switch (position)
        {
            case 0:
                parentActivity.turnToFragmentStack(R.id.container_setting_left, ContactGridEditFragment.class, null);
                parentActivity.clearFragmentStack(R.id.container_setting_right);
                break;
            case 1:
                parentActivity.turnToNewFragment(R.id.container_setting_left, KeyAreaEditFragment.class, null);
                parentActivity.clearFragmentStack(R.id.container_setting_right);
                break;
            case 2:
                parentActivity.turnToFragmentStack(R.id.container_setting_left, SubscribeFragment.class, null);
                parentActivity.clearFragmentStack(R.id.container_setting_right);
                break;
            case 3:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("fileType", 0);
                parentActivity.loadFileView(bundle2);
                break;
            case 4:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("fileType", 1);
                parentActivity.loadFileView(bundle3);
                break;
            case 5:
                Bundle bundle = new Bundle();
                bundle.putInt("fileType", 0);
                ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, FileSaveFragment.class, bundle);
                break;
            case 6:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("fileType", 1);
                ((SettingActivity) parentActivity).turnToNewFragment(R.id.container_setting_right, FileSaveFragment.class, bundle1);
                break;
            case 7:
//                ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, BlackWhiteListFragment.class, null);
                break;
        }
    }
}
