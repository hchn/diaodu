package com.jiaxun.setting.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.blutooth.fragment.BtFragment;
import com.jiaxun.setting.fragment.PrefsEditInfoFragment;
import com.jiaxun.setting.fragment.PrefsGroupRadioFragment;
import com.jiaxun.setting.fragment.PrefsSeekBarFragment;
import com.jiaxun.setting.model.PrefItemType;
import com.jiaxun.setting.model.PrefsBaseItem;
import com.jiaxun.setting.model.PrefsGroupRadioItem;
import com.jiaxun.setting.model.PrefsSeekBarItem;
import com.jiaxun.setting.model.PrefsSelectFileItem;
import com.jiaxun.setting.model.PrefsTextItem;
import com.jiaxun.setting.ui.fragment.ContactEditListFragment;
import com.jiaxun.setting.ui.fragment.FileBrowseFragment;
import com.jiaxun.setting.ui.fragment.NetworkSettingFragment;
import com.jiaxun.setting.ui.fragment.PrefsDeviceInfoFragment;
import com.jiaxun.setting.ui.fragment.PrefsListFragment;
import com.jiaxun.setting.ui.fragment.PrefsServiceAccountFragment;
import com.jiaxun.setting.ui.fragment.PresDateSettingFragment;
import com.jiaxun.setting.ui.fragment.PresTimeSettingFragment;
import com.jiaxun.setting.ui.fragment.WirelessFragment;
import com.jiaxun.setting.wifi.ui.fragment.PrefsWifiSettingFragment;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.ui.fragment.ContactDetailFragmet;
import com.jiaxun.uil.ui.screen.BaseActivity;
import com.jiaxun.uil.ui.view.TopStatusPaneView;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog.OnCustomClickListener;
import com.jiaxun.uil.util.DialogUtil;
import com.jiaxun.uil.util.ProgressBarUtil;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：设置界面
 * 
 * @author HeZhen
 * 
 * @Date 2015-4-30
 */
public class SettingActivity extends BaseActivity implements NotificationCenterDelegate
{
    private static final String TAG = SettingActivity.class.getName();
    private FrameLayout containeLeftLayout;
    private FrameLayout containeRightLayout;
    private static int DATA_STRING = 0;

    public static boolean isDntTrouble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.info(TAG, "SettingActivity onCreate::");
        String fontSize = ConfigHelper.getDefaultConfigHelper(this).getString(UiConfigEntry.PREF_FONT_SIZE, "FONT_SMALL");
        if (fontSize.equals("FONT_SMALL"))
        {
            setTheme(R.style.FontSmall);
        }
        else if (fontSize.equals("FONT_MIDDLE"))
        {
            setTheme(R.style.FontMedium);
        }
        else if (fontSize.equals("FONT_LARGE"))
        {
            setTheme(R.style.FontBig);
        }

        super.onCreate(savedInstanceState);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.SETTING_SHOW_CONTACT_DETAIL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.FILE_SELECT_OVER);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.FILE_IMPORT_OVER);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.FILE_EXPORT_OVER);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_FONT_CHANGE_EVENT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_EXIT_SYSYTEM);
    }

    @Override
    protected void onDestroy()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.SETTING_SHOW_CONTACT_DETAIL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.FILE_SELECT_OVER);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.FILE_IMPORT_OVER);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.FILE_EXPORT_OVER);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_FONT_CHANGE_EVENT);
        super.onDestroy();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        if (id == R.id.setting_btn)
//        {
//            // finish();
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.putExtra("isExit", false);
//            startActivity(intent);
////            turnToFragmentStack(R.id.container_setting_left, PrefsListFragment.class, null);
//        }
    }

    @Override
    public void initViewData()
    {
        containeLeftLayout = (FrameLayout) findViewById(R.id.container_setting_left);
        containeRightLayout = (FrameLayout) findViewById(R.id.container_setting_right);
    }

    /**
     * 方法说明 :左侧右侧 比例
     * @param left1right2 true 左1右2 false 左2右1
     * @author HeZhen
     * @Date 2015-7-8
     */
    public void initViewSize(double left, double right)
    {
        try
        {
            if (containeLeftLayout == null || containeRightLayout == null)
            {
                return;
            }
            LinearLayout.LayoutParams conLayoutParams = (LayoutParams) containeLeftLayout.getLayoutParams();
            if (conLayoutParams != null)
            {
                double ratio = left / (left + right);
                conLayoutParams.width = (int) (UiUtils.screenWidth * ratio);
                conLayoutParams.height = UiUtils.settingContainerH;
                containeLeftLayout.setLayoutParams(conLayoutParams);
            }

            LinearLayout.LayoutParams conRightLayoutParams = (LayoutParams) containeRightLayout.getLayoutParams();
            if (conRightLayoutParams != null)
            {
                conRightLayoutParams.width = UiUtils.screenWidth - conLayoutParams.width;
                conRightLayoutParams.height = UiUtils.settingContainerH;
                containeRightLayout.setLayoutParams(conRightLayoutParams);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    private void showSettingView()
    {
        Log.info(TAG, "show left PrefsListFragment");
        initViewSize(1, 2);
        turnToFragmentStack(R.id.container_setting_left, PrefsListFragment.class, null);
    }

    public void showSettingContactView()
    {
        Log.info(TAG, "show right contactEditListFragment");
        showSettingView();
        turnToFragmentStack(R.id.container_setting_right, ContactEditListFragment.class, null);
    }

    @Override
    public void initComponentViews()
    {
        showSettingView();
        turnToFragmentStack(R.id.container_setting_right, PrefsDeviceInfoFragment.class, null);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        UiApplication.setCurrentContext(this);

        if (UiApplication.FontChanged)
        {
            TopStatusPaneView.getInstanceNew();
        }
        initTopStatusView();
        TopStatusPaneView.getInstance().setSettingImg(R.drawable.ic_top_home);
        TopStatusPaneView.getInstance().setCompanyLogoImg(R.drawable.logo_setting);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.activity_setting;
    }

    // 在PrefsServiceAccountFragment点击事件的回调
    public void onPrefsItemSelected(PrefsBaseItem selectedItem)
    {
        if (selectedItem.getItemType() == PrefItemType.RADIO_GROUP)
        {
            // 单选类型
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, (PrefsGroupRadioItem) selectedItem);
            turnToNewFragment(R.id.container_setting_right, PrefsGroupRadioFragment.class, bundle);
        }
        else if (selectedItem.getItemType() == PrefItemType.SEEKBAR)
        {
            // 拖动条类型
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, (PrefsSeekBarItem) selectedItem);
            turnToNewFragment(R.id.container_setting_right, PrefsSeekBarFragment.class, bundle);
        }
        else if ((selectedItem.getItemType() == PrefItemType.SERVER_ADDRESS) || (selectedItem.getItemType() == PrefItemType.TEXT)
                || (selectedItem.getItemType() == PrefItemType.PASSWORD))
        {
            // 文本类型
            Bundle bundle = new Bundle();
            switch (selectedItem.getItemType())
            {
                case PrefItemType.SERVER_ADDRESS:
                    bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, (PrefsTextItem) selectedItem);
                    break;

                case PrefItemType.TEXT:
                case PrefItemType.PASSWORD:
                    bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, (PrefsTextItem) selectedItem);
                    break;

                default:
                    break;
            }

            PrefsEditInfoFragment editInfoFragment = new PrefsEditInfoFragment();

            editInfoFragment.setArguments(bundle);
            turnToNewFragment(R.id.container_setting_right, PrefsEditInfoFragment.class, bundle);
        }
        else if (selectedItem.getItemType() == PrefItemType.CONTACT)
        {
            if (!(ConfigHelper.getDefaultConfigHelper(this).getBoolean(UiConfigEntry.PREF_SERVICE_AUTO_DATATIME, false)))
            {

                switch (selectedItem.getSetType())
                {
                    case 0:
                        loadSetDate();
                        break;
                    case 1:
                        loadSetTime();
                        break;
                    default:
                        break;
                }
            }
            else
            {
                return;
            }
        }
        else if (selectedItem.getItemType() == PrefItemType.SELECT_FILE)
        {
            Log.info(TAG, "SD卡 SELECT_FILE::");
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, (PrefsSelectFileItem) selectedItem);
            loadFileView(bundle);
        }
    }

    // 返回系统设置界面
    public void backSystemSettingFragment(int id)
    {
        switch (id)
        {
            case R.id.saveButton:
                // 每次都去打开新的Fragment使得修改后的内容及时呈现出来
                turnToFragmentStack(R.id.container_setting_right, PrefsServiceAccountFragment.class, null);
                break;
            case R.id.cancelButton:
                // 不用每次都去打开新的Fragment因为没修改
                turnToFragmentStack(R.id.container_setting_right, PrefsServiceAccountFragment.class, null);
                break;
            default:
                break;
        }

    }

    // 返回系统设置界面
    public void backWireSettingFragment(int id)
    {
        switch (id)
        {
            case R.id.saveButton:
                // 每次都去打开新的Fragment使得修改后的内容及时呈现出来
                turnToNewFragment(R.id.container_setting_right, WirelessFragment.class, null);
                break;
            case R.id.cancelButton:
                // 不用每次都去打开新的Fragment因为没修改
                turnToFragmentStack(R.id.container_setting_right, WirelessFragment.class, null);
                break;
            default:
                break;
        }

    }

    /**
     * 方法说明 :加载网口设置
     * @author zhangxd
     * @Date 2015-7-17
     */
    public void loadNetworkSettingView(PrefsBaseItem prefsBaseItem)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConstantEntry.DATA_OBJECT, prefsBaseItem);
        turnToFragmentStack(R.id.container_setting_right, NetworkSettingFragment.class, bundle);
        // clearFragmentStack(R.id.container_setting_right);
    }

    /**
     * 方法说明 :加载Wifi设置
     * @author zhangxd
     * @Date 2015-7-9
     */
    public void loadWifiSettingView()
    {
        turnToFragmentStack(R.id.container_setting_right, PrefsWifiSettingFragment.class, null);
    }

    /**
     * 方法说明 :日期设置
     * @author chaimb
     * @Date 2015-8-20
     */
    private void loadSetDate()
    {
        turnToFragmentStack(R.id.container_setting_right, PresDateSettingFragment.class, null);
    }

    /**
     * 方法说明 :时间设置
     * @author chaimb
     * @Date 2015-8-20
     */
    private void loadSetTime()
    {
        turnToFragmentStack(R.id.container_setting_right, PresTimeSettingFragment.class, null);
    }

    /**
     * 方法说明 :加载蓝牙设置
     * @author zhangxd
     * @Date 2015-7-9
     */
    public void loadBluetoothView()
    {
        turnToFragmentStack(R.id.container_setting_right, BtFragment.class, null);
        // clearFragmentStack(R.id.container_setting_right);
    }

    public void loadFileView(Bundle bundle)
    {
        turnToFragmentStack(R.id.container_setting_right, FileBrowseFragment.class, bundle);
    }

    private void importSuccess()
    {
        ProgressBarUtil.dismissProgressDialog();
        ToastUtil.showToast("导入完成");
        clearFragmentStack(R.id.container_setting_right);
        showSettingContactView();
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.REFRESH_CONTACT_VIEW);
    }

    private void exportSuccess()
    {
        ProgressBarUtil.dismissProgressDialog();
        ToastUtil.showToast("导出完成");
        clearFragmentStack(R.id.container_setting_right);
        showSettingContactView();
    }

    private void importContact(final String filePath, int fileType)
    {
        if (!TextUtils.isEmpty(filePath))
        {

            if (fileType == 0)
            {
                DialogUtil.showConfirmDialog(this, "导入将要删除现有所有联系人，\n请先备份，\n是否导入？", true, new OnCustomClickListener()
                {
                    @Override
                    public void onClick(CustomAlertDialog customAlertDialog)
                    {
                        ProgressBarUtil.showProgressDialog(SettingActivity.this, null, "导入联系人...");
                        customAlertDialog.dismiss();
                        isDntTrouble = UiApplication.getConfigService().isDndEnabled();
                        if (!isDntTrouble)// 设置为免打扰，导入成功后还原
                        {
                            UiApplication.getConfigService().setDndEnabled(true);
                        }
                        UiApplication.getContactService().importContacts(filePath);
                    }
                });
            }
            else if (fileType == 1)
            {
                DialogUtil.showConfirmDialog(this, "请先确定对应联系人已被导入，并会删除现有按键，\n是否继续？", true, new OnCustomClickListener()
                {
                    @Override
                    public void onClick(CustomAlertDialog customAlertDialog)
                    {
                        ProgressBarUtil.showProgressDialog(SettingActivity.this, null, "导入按键...");
                        customAlertDialog.dismiss();
                        isDntTrouble = UiApplication.getConfigService().isDndEnabled();
                        if (!isDntTrouble)
                        {
                            UiApplication.getConfigService().setDndEnabled(true);
                        }
                        UiApplication.getContactService().importKeys(filePath);
                    }
                });
            }

        }
    }

    /**
     * author:zhangxd
     * 2015-5-21上午10:17:07
     * 初始化 PreFsServiceAccountFragment 中 lishview对应修改数据
     */

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
       if (id == UiEventEntry.SETTING_SHOW_CONTACT_DETAIL)
        {
            int contactId = (Integer) args[0];
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConstantEntry.DATA_CONTACT_ID, contactId);
            bundle.putBoolean(CommonConstantEntry.DATA_ENABLE, true);
            turnToNewFragment(R.id.container_setting_right, ContactDetailFragmet.class, bundle);
        }
        else if (id == UiEventEntry.FILE_SELECT_OVER)
        {
            String filePath = (String) args[0];
            int fileType = (Integer) args[1];
            if (fileType == 0 || fileType == 1)
            {
                importContact(filePath, fileType);
            }
            else if (fileType == 2 || fileType == 3)
            {// 选择音频
                turnToFragmentStack(R.id.container_setting_right, PrefsServiceAccountFragment.class);
                if (fileType == 2)
                {
                    ServiceUtils.updateMediaConfig();
                }
            }
        }
        else if (id == UiEventEntry.FILE_IMPORT_OVER)
        {
            int code = (Integer) args[0];
            switch (code)
            {
                case 0:
                    ProgressBarUtil.dismissProgressDialog();
                    break;
                case 1:
                    importSuccess();
                    break;
            }
            if (!isDntTrouble)
            {
                UiApplication.getConfigService().setDndEnabled(false);
            }
        }
        else if (id == UiEventEntry.FILE_EXPORT_OVER)
        {
            int code = (Integer) args[0];
            switch (code)
            {
                case 0:
                    ProgressBarUtil.dismissProgressDialog();
                    break;
                case 1:
                    exportSuccess();
                    break;
            }
            if (!isDntTrouble)
            {
                UiApplication.getConfigService().setDndEnabled(true);
            }
        }
        else if (id == UiEventEntry.NOTIFY_FONT_CHANGE_EVENT)
        {
            recreate();
        }
        else if (id == UiEventEntry.NOTIFY_EXIT_SYSYTEM)
        {
            this.finish();
        }
    }
}
