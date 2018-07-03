package com.jiaxun.uil.ui.screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.sdk.scl.media.player.VoiceUtil;
import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.ConfModel;
import com.jiaxun.sdk.util.DensityUtil;
import com.jiaxun.sdk.util.SdkUtil;
import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.constant.CommonEventEntry;
import com.jiaxun.sdk.util.exception.AppExcepitonHandler;
import com.jiaxun.sdk.util.httpserver.HttpServerService;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.model.RightTabItem;
import com.jiaxun.uil.ui.adapter.LeftTabAdapter;
import com.jiaxun.uil.ui.adapter.RightTabAdapter;
import com.jiaxun.uil.ui.fragment.CallListFragment;
import com.jiaxun.uil.ui.fragment.CallRecordDetailFragment;
import com.jiaxun.uil.ui.fragment.CallRecordListFragment;
import com.jiaxun.uil.ui.fragment.ConfControlFragment;
import com.jiaxun.uil.ui.fragment.ContactDetailFragmet;
import com.jiaxun.uil.ui.fragment.ContactGridFragment;
import com.jiaxun.uil.ui.fragment.ContactHotKeyFragment;
import com.jiaxun.uil.ui.fragment.ContactListFragment;
import com.jiaxun.uil.ui.fragment.ContactSelectAddFragment;
import com.jiaxun.uil.ui.fragment.LoginFragment;
import com.jiaxun.uil.ui.fragment.VideoFragment;
import com.jiaxun.uil.ui.fragment.VideoPrstCtrlFragment;
import com.jiaxun.uil.ui.fragment.VideoPtzCtrlFragment;
import com.jiaxun.uil.ui.fragment.VsListFragment;
import com.jiaxun.uil.ui.view.BadgeView;
import com.jiaxun.uil.ui.view.TopStatusPaneView;
import com.jiaxun.uil.ui.widget.AdditionalFuncWindow;
import com.jiaxun.uil.ui.widget.DialPadWindow;
import com.jiaxun.uil.util.ContactUtil;
import com.jiaxun.uil.util.FgManager;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.SystemBrightManager;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：应用展示主界面
 * 
 * @author hubin
 * 
 * @Date 2015-2-9
 */
@SuppressLint("InlinedApi")
public class HomeActivity extends BaseActivity implements NotificationCenterDelegate
{
    private static final String TAG = HomeActivity.class.getName();
    private final static String ACTION_LEFTMODULE_NUMBER_DOWN = "jiaxun.action.leftmodule.number.down";
    private final static String ACTION_RIGHTMODULE_NUMBER_DOWN = "jiaxun.action.rightmodule.number.down";
    public final static String ACTION_EXIT_LOGIN = "jiaxun.action.exitlogin";
    private final static String ACTION_MODULE_NUMBER_DOWN = "jiaxun.action.module.number.down";
    private ImageView funcMoreIv;
    private ImageView contactSwitcher;// 通信录入口
    private LinearLayout leftTabContainer;

    private GridView tabListLeft, tabListRight;
    private LeftTabAdapter leftAdapter;
    private RightTabAdapter rightAdapter;

    private List<BaseListItem> leftGroups;

    private List<RightTabItem> rightEsList;
    /** 左侧包括通讯录组列表区和通讯录列表取的窗口容器 */
    private LinearLayout leftContainer;
    /** 右侧包括本地视频区和内容区的窗口容器 */
    private LinearLayout rightContainer;
    /** 本地视频显示区容器 */
    private LinearLayout localVideoContainer;
    /** 本地视频显示区 */
    private FrameLayout localVideoView;
    private ImageView settingImage;
    /** 本地视频view */
    private SurfaceView localPreview;

    private View rightPane;

    private boolean isContactList = false;

    private AdditionalFuncWindow funcMorePopup;
    private DialPadWindow dialPopup;
    /** 临时加 true 快捷拨号 false 通讯录 */
    public boolean isKeyArea = false;
    // 接收触发通话界面变化的广播
    private BroadcastReceiver broadcastReceiver;

    private BadgeView missedBadgeView = null;
    // 联系人 左侧当前选择
    private int leftSelectItem_Contact = 0;
    // 按键 左侧当前选择
    private int leftSelectItem_Key = 0;
    // 保存当前选中的右侧菜单项
    private int cashedRightTabType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.info(TAG, "onCreate HomeActivity");
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

        // FIXME:解决问题：在activity中加载SurfaceView，屏幕会闪一下(黑色)
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        if (!UiApplication.isServiceStarted)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    Log.info(TAG, "onCreate:: start service");
                    // 初始化业务模块服务
                    UiApplication.initSdkServices();
                    // 初始化系统配置
                    UiApplication.initSystemConfig();
                    // 启动系统崩溃保护
                    AppExcepitonHandler.getInstance(UiApplication.getInstance(), HomeActivity.class.getName()).init();
                    // 初始化本地摄像头
                    UiApplication.getDeviceService().initLocalCamera();
                    // 重置服务启动标识
                    UiApplication.isServiceStarted = true;
                }
            }).start();
        }
        // 启动http服务
        initHttpService();
        // 初始化事件总线通知
        initEventNotify();
        initWindowSize();
        // 初始化系统加载页面
        if (UiApplication.isAtdOnline)
        {
            loadAppStartView(UiApplication.atdName);
        }
        else
        {
            loadAtdLoginView();
        }

        initReceiver();
    }

    /**
     * 启动http服务
     */
    private void initHttpService()
    {
        Log.info(TAG, "initHttpService::");
        try
        {
            Intent intent = new Intent(UiApplication.getInstance(), HttpServerService.class);
            SdkUtil.getApplicationContext().stopService(intent);// 停止服务，避免界面被强制停止，服务重新启动导致异常
            SdkUtil.getApplicationContext().startService(intent);// 启动服务
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }

    private void initWindowSize()
    {
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        Log.info(TAG, "width : " + metrics.widthPixels + ",height : " + metrics.heightPixels + ",densityDpi : " + metrics.densityDpi + ",density : "
                + metrics.density);
        UiUtils.screenWidth = metrics.widthPixels;
        UiUtils.screenHeight = metrics.heightPixels;
        UiUtils.titleBarH = (int) (UiUtils.screenHeight / 1200f * 65f);
        UiUtils.homeRightPaneW = (int) ((double) UiUtils.screenWidth / (double) 1280 * (double) 50);
        int w = UiUtils.screenWidth - UiUtils.homeRightPaneW;
        UiUtils.homeLeftPaneW = (int) ((double) UiUtils.screenWidth / (double) 1280 * (double) 110);
        UiUtils.homeLeftContainerW = (int) ((double) UiUtils.screenWidth / (double) 1280 * ((double) 690 + (double) 110 - DensityUtil.dip2px(this, 4)));
        UiUtils.homeRightContainerW = (int) ((double) UiUtils.screenWidth / (double) 1280 * (double) 430);
        UiUtils.settingLeftContainerW = UiUtils.screenWidth / 3;
        UiUtils.settingRightContainerW = UiUtils.screenWidth - UiUtils.settingLeftContainerW;
        UiUtils.homeContainerH = UiUtils.settingContainerH = UiUtils.screenHeight - UiUtils.titleBarH - getStatusBarHeight();
    }

    private int getStatusBarHeight()
    {
        int result = 0;
        if (Build.VERSION.SDK_INT > UiConfigEntry.SDK_LOW_LEVEL)
        {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0)
            {
                result = getResources().getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    private void initReceiver()
    {
        broadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.info(TAG, "onReceive:: " + intent.getAction());
                if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction()))
                {// 耳麦
                    int headsetStatus = intent.getIntExtra("state", 0);
                    Log.info(TAG, "onReceive:: headsetStatus:" + headsetStatus);
                    if (headsetStatus == 1)
                    {
                        TopStatusPaneView.getInstance().setHeadsetNofity(R.drawable.headset);
                        VoiceUtil.setSpeaker(false);
                    }
                    else if (headsetStatus == 0)
                    {
                        TopStatusPaneView.getInstance().setHeadsetNofity(-1);
                        VoiceUtil.setSpeaker(true);
                    }
                }
                else if (ACTION_EXIT_LOGIN.equals(intent.getAction()))
                {
                    loadAtdLoginView();
                }
//                else if(ACTION_LEFTMODULE_NUMBER_DOWN.equals(intent.getAction()))
//                {
//                    String number = intent.getStringExtra("value");
//                    Log.info(TAG, intent.getAction() + "::value:" + number);
//                    loadDialView(intent.getAction(),number,"0");
//                }
//                else if(ACTION_RIGHTMODULE_NUMBER_DOWN.equals(intent.getAction()))
//                {
//                    String number = intent.getStringExtra("value");
//                    Log.info(TAG, intent.getAction() + "::value:" + number);
//                    loadDialView(intent.getAction(),"0",number);
//                }

            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_HEADSET_PLUG);
        intentFilter.addAction(ACTION_RIGHTMODULE_NUMBER_DOWN);
        intentFilter.addAction(ACTION_LEFTMODULE_NUMBER_DOWN);
        intentFilter.addAction(ACTION_EXIT_LOGIN);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void initEventNotify()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SHOW_CONTACT_DETAIL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_SHOW_CALL_LIST);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_ATD_LOGIN);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_OPEN_CONF_CONTROL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SHOW_VS_LIST);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_SHOW_LEFT_PANE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SHOW_REMOTE_VIDEO);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_SERVICE_STATUS);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_PRESENCE_USER_STATUS);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_SHOW);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_HIDE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_CONF_MEMBER_CALL_RECORD);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CONTACT_TEMPCONF_SHOW);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_CAMERA_CONTROL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.EVENT_UPDATE_RIGHT_TAB);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CONF_TEMP);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.ACTION_MOUBLE_NUMBER);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.OPEN_PRESENTATION_CONTROL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CLOSE_PRESENTATION_CONTROL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CALL_RECORD_MISSED_COUNT);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.Font_SIZE_CHANGED);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CLOSE_DIAL);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_NIGHT_SERVICE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_CAMERA_ERADY);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_EXIT_SYSYTEM);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        Log.info(TAG, "onConfigurationChanged::");
        initWindowSize();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onDestroy()
    {
        Log.info(TAG, "onDestroy HomeActivity");
        super.onDestroy();

        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.REFRESH_CONTACT_VIEW);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_SHOW_CONTACT_DETAIL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_SHOW_CALL_LIST);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_ATD_LOGIN);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_OPEN_CONF_CONTROL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_SHOW_VS_LIST);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_SHOW_LEFT_PANE);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_SHOW_REMOTE_VIDEO);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_SERVICE_STATUS);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_PRESENCE_USER_STATUS);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_SHOW);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_LOCAL_VIDEO_HIDE);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_CONF_MEMBER_CALL_RECORD);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CONTACT_TEMPCONF_SHOW);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_CAMERA_CONTROL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.EVENT_UPDATE_RIGHT_TAB);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_CONF_TEMP);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.ACTION_MOUBLE_NUMBER);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.OPEN_PRESENTATION_CONTROL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CLOSE_PRESENTATION_CONTROL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CALL_RECORD_MISSED_COUNT);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CLOSE_DIAL);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_CAMERA_ERADY);
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_EXIT_SYSYTEM);

        unregisterReceiver(broadcastReceiver);
        Intent intent = new Intent(UiApplication.getInstance(), HttpServerService.class);// http服务
        UiApplication.getInstance().stopService(intent);// 停止服务
        UiUtils.cancelLockScreenServer();
//        if (UiApplication.presentation != null)
//        {
//            UiApplication.presentation.cancel();
//        }
//        if (UiApplication.defaultPresentation != null)
//        {
//            UiApplication.defaultPresentation.cancel();
//        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.info(TAG, "onResume::");
        if (UiApplication.FontChanged)
        {
            UiApplication.FontChanged = false;
            recreate();
        }
        else
        {
            UiApplication.setCurrentContext(this);
            if (UiApplication.isAtdOnline)
            {
                initTopStatusView();
            }
        }
        TopStatusPaneView.getInstance().setSettingImg(R.drawable.ic_top_setting);
        TopStatusPaneView.getInstance().setCompanyLogoImg(R.drawable.logo);
    }

    /**
     * 方法说明 :添加左Tab栏数据，来自于组层级列表根节点
     * 
     * @author hubin
     * @Date 2015-3-12
     * 
     * @add 查询所有组 组层级 parentID 为-1(暂定)
     */
    private void initLeftPaneData()
    {
        leftGroups.clear();
        ArrayList<GroupModel> groups = isKeyArea ? UiApplication.getContactService().getKeyGroupRootList() : UiApplication.getContactService()
                .getGroupRootList();
        for (GroupModel group : groups)
        {
            BaseListItem baseListItem = new BaseListItem();
            baseListItem.setId(group.getId());
            baseListItem.setName(group.getName());
            baseListItem.setPosition(group.getPosition());
            leftGroups.add(baseListItem);
        }
        ContactUtil.sortBaseListItemByPos(leftGroups);
        updateLeftPaneSelectState(isKeyArea ? leftSelectItem_Key : leftSelectItem_Contact);
        if (leftAdapter != null)
        {
            leftAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 方法说明 :添加右面布局的假数据
     * 
     * @author wangxue
     * @Date 2015-3-12
     */
    private void addRightlist()
    {
        RightTabItem rightItem = new RightTabItem();
        rightItem.setIconRes(R.drawable.func_right_call_ic);
        rightItem.setSelectIcon(R.drawable.func_right_call_ic_select);
        rightItem.setName("呼叫");
        rightItem.setType(UiEventEntry.TAB_CALL_LIST);
        rightItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (UiApplication.getVsService().isLoopStarted())
                {
                    ToastUtil.showToast("轮巡中，不能切换");
                }
                else
                {
                    loadCallListView();
                }
            }
        });
        rightEsList.add(rightItem);
        RightTabItem rightItem2 = new RightTabItem();
        rightItem2.setIconRes(R.drawable.func_right_control_ic);
        rightItem2.setSelectIcon(R.drawable.func_right_control_ic_select);
        rightItem2.setName("监控");
        rightItem2.setType(UiEventEntry.TAB_VS_LIST);
        rightItem2.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadVsView();
            }
        });
        rightEsList.add(rightItem2);

        RightTabItem rightItem4 = new RightTabItem();
        rightItem4.setIconRes(R.drawable.func_right_calllog_ic);
        rightItem4.setSelectIcon(R.drawable.func_right_calllog_ic_select);
        rightItem4.setName("通话记录");
        rightItem4.setType(UiEventEntry.TAB_CALL_RECORD);
        rightItem4.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (UiApplication.getVsService().isLoopStarted())
                {
                    ToastUtil.showToast("轮巡中，不能切换");
                }
                else
                {
                    loadCallRecordView();
                }
            }
        });
        rightEsList.add(rightItem4);

        if (UiConfigEntry.PRESEMTATION_SWITCH && Build.VERSION.SDK_INT >= UiConfigEntry.SDK_LOW_LEVEL)
        {
            RightTabItem rightItem5 = new RightTabItem();
            rightItem5.setIconRes(R.drawable.func_right_prst_ic);
            rightItem5.setSelectIcon(R.drawable.func_right_prst_ic_select);
            rightItem5.setName("扩展屏");
            rightItem5.setType(UiEventEntry.TAB_PRESENTATION);
            rightItem5.setClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (UiApplication.getVsService().isLoopStarted())
                    {
                        ToastUtil.showToast("轮巡中，不能切换");
                    }
                    else
                    {
                        loadPresentationView();
                    }
                }
            });
            rightEsList.add(rightItem5);
        }

        RightTabItem rightItem6 = new RightTabItem();
        rightItem6.setIconRes(R.drawable.func_right_dial_ic);
        rightItem6.setSelectIcon(R.drawable.func_right_dial_ic_select);
        rightItem6.setName("拨号盘");
        rightItem6.setType(UiEventEntry.TAB_DIAL);
        rightItem6.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (UiApplication.getVsService().isLoopStarted())
                {
                    ToastUtil.showToast("轮巡中，不能切换");
                }
                else
                {
                    loadDialView(true, "");
                }
            }
        });
        rightEsList.add(rightItem6);
    }

    @SuppressLint({ "InlinedApi", "NewApi" })
    public void loadPresentationView()
    {
        Log.info(TAG, "loadPresentationView:;");
        if (UiApplication.presentation != null && UiApplication.presentation.isShowing())
        {
            turnToFragmentStack(R.id.container_right_content, VideoPrstCtrlFragment.class);
        }
        else
        {
            ToastUtil.showToast("请连接扩展屏！");
        }
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.activity_home_main;
    }

    private void initFragmentStack()
    {
        FgManager.pushToInitStack(ConfControlFragment.class);
        FgManager.pushToInitStack(CallListFragment.class);
        FgManager.pushToInitStack(VsListFragment.class);
        FgManager.pushToInitStack(CallRecordListFragment.class);
        FgManager.pushToInitStack(VideoPrstCtrlFragment.class);
        FgManager.pushToInitStack(VideoFragment.class);
    }

    @Override
    public void initComponentViews()
    {
        Log.info(TAG, "start initComponentViews::");
        // 初始化内容显示容器
        leftTabContainer = (LinearLayout) findViewById(R.id.layout_left);
        leftContainer = (LinearLayout) findViewById(R.id.container_left);
        rightContainer = (LinearLayout) findViewById(R.id.container_right);
        localVideoContainer = (LinearLayout) findViewById(R.id.local_video_container);
        localVideoView = (FrameLayout) findViewById(R.id.local_video);

        rightPane = findViewById(R.id.layout_right);
        // 初始化左侧Tab栏组件
        contactSwitcher = (ImageView) findViewById(R.id.contact_switcher);
        contactSwitcher.setOnClickListener(this);

        tabListLeft = (GridView) findViewById(R.id.tab_left_list);
        leftAdapter = new LeftTabAdapter(this, leftGroups);
        tabListLeft.setAdapter(leftAdapter);
        tabListLeft.setSelector(new ColorDrawable(Color.TRANSPARENT));
        tabListLeft.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                onLeftPaneItemSelected(arg2);
            }
        });
        funcMoreIv = (ImageView) findViewById(R.id.iv_more);
        funcMoreIv.setOnClickListener(this);
        // 初始化右侧Tab栏组件
        tabListRight = (GridView) findViewById(R.id.tab_right_list);
        rightAdapter = new RightTabAdapter(this, rightEsList);
        tabListRight.setAdapter(rightAdapter);
        addRightlist();
        Log.info(TAG, "finish initComponentViews::");
    }

    // false 未登录 true 登录后
    private void initWindows(boolean login)
    {
        LinearLayout.LayoutParams conLayoutParams = (LayoutParams) leftContainer.getLayoutParams();
        if (!login)
        {
            conLayoutParams.width = UiUtils.screenWidth;
            conLayoutParams.height = LayoutParams.MATCH_PARENT;
            leftContainer.setLayoutParams(conLayoutParams);
            return;
        }
        conLayoutParams.width = UiUtils.homeLeftContainerW;
        conLayoutParams.height = LayoutParams.MATCH_PARENT;
        leftContainer.setLayoutParams(conLayoutParams);

        LinearLayout.LayoutParams conRightLayoutParams = (LayoutParams) rightContainer.getLayoutParams();
        conRightLayoutParams.width = UiUtils.homeRightContainerW;
        conRightLayoutParams.height = LayoutParams.MATCH_PARENT;
        rightContainer.setLayoutParams(conRightLayoutParams);

        LinearLayout.LayoutParams rightParams = (LayoutParams) rightPane.getLayoutParams();
        rightParams.width = UiUtils.homeRightPaneW;
        rightParams.height = LayoutParams.MATCH_PARENT;
        rightPane.setLayoutParams(rightParams);

        LinearLayout.LayoutParams leftTabParams = (LayoutParams) leftTabContainer.getLayoutParams();
        leftTabParams.width = UiUtils.homeLeftPaneW;
        leftTabParams.height = LayoutParams.MATCH_PARENT;
        leftTabContainer.setLayoutParams(leftTabParams);

    }

    private void updateRightTabBg(int selectedTab)
    {
        for (RightTabItem rightTabItem : rightEsList)
        {
            if (rightTabItem.getType() == selectedTab)
            {
                rightTabItem.setSelected(true);
            }
            else
            {
                rightTabItem.setSelected(false);
            }
        }
        if (rightAdapter != null)
        {
            rightAdapter.notifyDataSetChanged();
        }
    }

    private int updateLeftPaneSelectState(int index)
    {
        if (index < 0 || index >= leftGroups.size())
        {
            return -1;
        }
        for (BaseListItem baseItem : leftGroups)
        {
            baseItem.setSelected(false);
        }
        BaseListItem baseItem = leftGroups.get(index);

        if (baseItem == null)
        {
            return -1;
        }
        baseItem.setSelected(true);

        if (leftAdapter != null)
        {
            leftAdapter.notifyDataSetChanged();
        }
        int id = baseItem.getId();
        return id;
    }

    private void onLeftPaneItemSelected(int index)
    {
        int id = updateLeftPaneSelectState(index);

        if (id < 0)
        {
            return;
        }
        if (isKeyArea)
        {
            leftSelectItem_Key = index;
            GroupModel GroupModel = UiApplication.getContactService().getDepById(id);
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.HOME_LEFTGROUP_SHOWKEYAREA, GroupModel);
        }
        else
        {
            leftSelectItem_Contact = index;
            GroupModel GroupModel = UiApplication.getContactService().getDepById(id);
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.HOME_LEFTGROUP_SHOWCONTACT, GroupModel);
        }
    }

    @Override
    public void initViewData()
    {
        leftGroups = new ArrayList<BaseListItem>();
        rightEsList = new ArrayList<RightTabItem>();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.contact_switcher:
                switchContactAndKey();
                break;
            case R.id.iv_more:
                showMoreFunc();
                break;
            default:
                break;
        }
    }

    private void showMoreFunc()
    {
        int x = tabListRight.getWidth();// + DensityUtil.dip2px(this, 4);
        funcMorePopup.setWidth(rightContainer.getMeasuredWidth());
        funcMorePopup.showAtLocation(rightContainer, Gravity.BOTTOM | Gravity.RIGHT, x, 0);
        funcMorePopup.refresh();
    }

    /**
     * 方法说明 : 显示拨号盘
     * @param isManual 从硬件装置输入号码自动唤醒拨号盘为false，从终端界面点击唤醒拨号盘为true
     * @return void
     * @author hubin
     * @Date 2015-9-11
     */
    private void loadDialView(boolean isManual, String dialNum)
    {
        dialPopup.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        if (isManual)
        {
            dialPopup.hideRightDial();
        }
        dialPopup.setEditNum(dialNum);
        dialPopup.showAtLocation(rightContainer, Gravity.CENTER_HORIZONTAL, 0, 0);
        updateRightTabBg(UiEventEntry.TAB_DIAL);
    }

    /**
     * 
     * 方法说明 :关闭拨号盘
     * @author chaimb
     * @Date 2015-9-8
     */
    private void closeDial()
    {
        if (dialPopup != null)
        {
            dialPopup.dismiss();
        }
    }

    private void switchContactAndKey()
    {
        isKeyArea = !isKeyArea;
        switchContactAndKey(isKeyArea);
    }

    private void switchContactAndKey(boolean isQuickDial)
    {
        contactSwitcher.setImageResource(isQuickDial ? R.drawable.contact_left_switch_2 : R.drawable.contact_left_switch_1);
        turnToFragmentStack(R.id.container_left_content, isQuickDial ? ContactHotKeyFragment.class : isContactList ? ContactListFragment.class
                : ContactGridFragment.class);
        leftTabContainer.setVisibility(isContactList ? View.GONE : View.VISIBLE);
        initLeftPaneData();
    }

    /**
     * 方法说明 :值班员登录界面
     * @author hubin
     * @Date 2015-3-12
     */
    public void loadAtdLoginView()
    {
        UiApplication.isAtdOnline = false;
        UiApplication.atdName = "";
        initWindows(false);
        rightContainer.setVisibility(View.GONE);
        if (settingImage != null)
        {
            settingImage.setVisibility(View.GONE);
        }
        findViewById(R.id.layout_left).setVisibility(View.GONE);
        findViewById(R.id.layout_right).setVisibility(View.GONE);
        removeTopStatusView();
        turnToFragmentStack(R.id.container_left_content, LoginFragment.class);
    }

    /**
     * 方法说明 : 值班员登录后系统初始界面
     * @param atdName 值班员名称
     * @return void
     * @author hubin
     * @Date 2015-5-18
     */
    private void loadAppStartView(final String atdName)
    {
        Log.info(TAG, "loadAppStartView:: atdName:" + atdName);
        initWindows(true);
        initLeftPaneData();
        UiApplication.initPresentation(this);
        // 初始化碎片管理基础栈
        initFragmentStack();
        initTopStatusView();
        funcMorePopup = new AdditionalFuncWindow(this);
        dialPopup = new DialPadWindow(this);
        ((FrameLayout) findViewById(R.id.container_left_content)).removeAllViews();
        rightContainer.setVisibility(View.VISIBLE);
        findViewById(R.id.layout_left).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_right).setVisibility(View.VISIBLE);
        // 设置值班员名称
        TopStatusPaneView.getInstance().setAccountName(atdName);
        UiUtils.hideSoftKeyboard(HomeActivity.this);
        loadContactView(isContactList);
        loadCallListView();

        if (settingImage != null)
        {
            settingImage.setVisibility(View.VISIBLE);
        }
        // 启动SDK业务服务，同时向服务器注册
        ServiceUtils.startSdkService();
        UiUtils.startLockScreenServer();
    }

    /**
     * 方法说明 : 打开单呼列表窗口
     * 
     * @return void
     * @author hubin
     * @Date 2015-5-15
     */
    private void loadCallListView()
    {
        Intent intent = new Intent();
        intent.setAction(CommonEventEntry.EVENT_TYPE_CALL);
        intent.putExtra(CommonEventEntry.MESSAGE_TYPE_CALL, true);
        this.sendBroadcast(intent);
        turnToFragmentStack(R.id.container_right_content, CallListFragment.class);
    }

    /**
     * 方法说明 : 会议控制窗口
     * 
     * @param confModel
     * @return void
     * @author hubin
     * @Date 2015-5-18
     */
    private void loadConfControlView(ConfModel confModel, int status)
    {
        // 打开远端视频窗口和会议控制窗口
        turnToFragmentStack(R.id.container_right_content, ConfControlFragment.class);
    }

    /**
     * 方法说明 : 呼叫视频窗口
     * 
     * @return void
     * @author hubin
     * @Date 2015-5-18
     */
    private void loadVideoView()
    {
        // 打开远端视频窗口
        turnToFragmentStack(R.id.container_left_content, VideoFragment.class);
        leftTabContainer.setVisibility(View.GONE);
    }

    /**
     * 方法说明 : 打开视频监控列表窗口
     * 
     * @return void
     * @author zhangxd
     * @Date 2015-5-29
     */
    private void loadVsView()
    {
        turnToFragmentStack(R.id.container_right_content, VsListFragment.class);
    }

    /**
     * 方法说明 : 打开云镜控制
     * 
     * @return void
     * @author zhangxd
     * @Date 2015-6-15
     */
    private void loadPtzControl(String number, String sessionId)
    {
        Bundle data = new Bundle();
        data.putString(CommonConstantEntry.DATA_SESSION_ID, sessionId);
        data.putString(CommonConstantEntry.DATA_NUMBER, number);
        turnToNewFragment(R.id.container_right_content, VideoPtzCtrlFragment.class, data);
        // turnToNewFragment(R.id.container_right_content,
        // PtzControlFragement.class, data);
    }

    /**
     * 方法说明 : 联系人窗口
     * 
     * @param showList
     *            作为List显示或者Grid显示
     * @return void
     * @author hubin
     * @Date 2015-5-18
     */
    public void loadContactView(final boolean showList)
    {
        isContactList = showList;
        leftTabContainer.setVisibility(View.VISIBLE);
        switchContactAndKey(false);
        turnToFragmentStack(R.id.container_left_content, showList ? ContactListFragment.class : ContactGridFragment.class);
        leftTabContainer.setVisibility(isContactList ? View.GONE : View.VISIBLE);
    }

    /**
     * 方法说明 : 通话记录窗口
     * 
     * @return void
     * @author hubin
     * @Date 2015-5-18
     */
    private void loadCallRecordView()
    {
        turnToFragmentStack(R.id.container_right_content, CallRecordListFragment.class);
    }

    // 会议成员列表
    private void loadConfMemberCallRecordView(CallRecord callRecord, int type, int visiblePosition, int count)
    {
        Bundle data = new Bundle();
        data.putParcelable(CommonConstantEntry.DATA_OBJECT, callRecord);
        data.putInt(UiEventEntry.CALLRECORD_TYPE, type);
        data.putInt(UiEventEntry.CALLRECORD_VISIBLEPOSITION, visiblePosition);
        data.putInt(UiEventEntry.CALLRECORD_COUNT, count);
        turnToFragmentStack(R.id.container_right_content, CallRecordDetailFragment.class, data);
    }

    private void loadContactSelectView(int callBack, int columns)
    {
        Bundle data = new Bundle();
        data.putInt("gridColumns", columns);
        data.putInt(CommonConstantEntry.DATA_TYPE, callBack);
        turnToFragmentStack(R.id.container_right_content, ContactSelectAddFragment.class, data);
    }

    private void onUserStatusChanged(ArrayList<HashMap<String, Integer>> presenceMap)
    {
        Log.info(TAG, "onUserStatusChanged::");
        if (presenceMap == null || presenceMap.size() == 0)
        {
            return;
        }
        for (HashMap<String, Integer> statusMap : presenceMap)
        {
            for (Map.Entry<String, Integer> statusEntry : statusMap.entrySet())
            {
                int status = statusEntry.getValue();
                String phoneNum = statusEntry.getKey();
                refreshContactStatus(phoneNum, status, false);
            }
        }
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.REFRESH_CONTACT_VIEW);
    }

    public void refreshContactStatus(String phoneNum, int status, boolean callStatus)
    {
        ContactModel contactModel = UiApplication.getContactService().getContactByPhoneNum(phoneNum);
        if (contactModel != null)
        {
            if (callStatus)
            {
                contactModel.setCallStatus(status);
            }
            else
            {
                contactModel.setStatus(status);
            }
        }
    }

    /**
     * 方法说明 : 处理注册状态通知
     * @param linkStatus
     * @param serviceStatus
     * @return void
     * @author hubin
     * @Date 2015-9-8
     */
    private void handleServiceStatus(int[] linkStatus, int[] serviceStatus)
    {
        Log.info(TAG, "handleServiceStatus:: linkStatus: " + Arrays.toString(linkStatus) + " serviceStatus: " + Arrays.toString(serviceStatus));
        if (serviceStatus == null || linkStatus == null)
        {
            return;
        }

        // 主用上线提示
        if (serviceStatus.length > 0
                && (serviceStatus[0] == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus[0] == CommonConstantEntry.SERVICE_STATUS_STANDBY))
        {
            TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_ACTIVE, UiApplication.getConfigService().getAccountNumber());
            TopStatusPaneView.getInstance().setServiceServerNotify(R.drawable.phone);
            if (!UiApplication.isCallServerOnline)
            {
                UiApplication.isCallServerOnline = true;
                // 处理连续用户状态订阅操作
                UiApplication.getPresenceService().removeAllSubscribe();
                addSubscribeMem();
            }
        }
        // 备用上线提示
        else if (serviceStatus.length > 1
                && (serviceStatus[1] == CommonConstantEntry.SERVICE_STATUS_ACTIVE || serviceStatus[1] == CommonConstantEntry.SERVICE_STATUS_STANDBY))
        {
            TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_STANDBY, UiApplication.getConfigService().getAccountNumber());
            TopStatusPaneView.getInstance().setServiceServerNotify(R.drawable.carmera);
            if (!UiApplication.isCallServerOnline)
            {
                UiApplication.isCallServerOnline = true;
                // 临时处理连续用户状态订阅操作
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            UiApplication.getPresenceService().removeAllSubscribe();
                            Thread.sleep(500);
                            addSubscribeMem();
                        }
                        catch (Exception e)
                        {
                            Log.exception(TAG, e);
                        }
                    }
                }.start();
            }
        }
        // 下线提示
        else
        {
            UiApplication.isCallServerOnline = false;
            TopStatusPaneView.getInstance().setServiceServerNotify(R.drawable.speaker);
            if (serviceStatus.length > 0)
            {
                switch (serviceStatus[0])
                {
                    case CommonConstantEntry.SERVICE_STATUS_OFFLINE:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_FORBIDUSER:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：非法用户");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_PASSWORDERROR:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：密码错误");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_LOCKEDUSER:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：用户闭锁");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_FORBIDUPLINE:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：禁止上线");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_HEARTBEAT_HALT:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：心跳中断");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_NETWORK_DISABLED:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：网络不可用");
                        break;
                    case CommonConstantEntry.SERVICE_STATUS_TIMEOUT:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线：超时");
                        break;
                    default:
                        TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线");
                        break;
                }

                // 清除掉订阅用户的状态
                EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.REFRESH_CONTACT_VIEW);

                // 释放所有单呼和会议
                for (CallListItem callListItem : ServiceUtils.getCurrentCallList())
                {
                    if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_AUDIO
                            || callListItem.getType() == CommonConstantEntry.CALL_TYPE_SINGLE_VIDEO)
                    {
                        UiApplication.getSCallService().sCallRelease(callListItem.getCallModel().getSessionId(), CommonConstantEntry.CALL_END_OFFLINE);
                    }
                    else if (callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO
                            || callListItem.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
                    {
                        UiApplication.getConfService().confClose(callListItem.getCallModel().getSessionId());
                        removeFragmentFromBackStack(R.id.container_right_content, ConfControlFragment.class);
//                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_CLOSE_CONF_CONTROL);
                        for (ConfMemModel confMemModel : ((ConfModel) callListItem.getCallModel()).getMemList())
                        {
                            UiUtils.removeRemoteVideo(confMemModel.getNumber());
                        }
                    }
                }

                // 关闭所有视频监控
                UiApplication.getVsService().deleteAllVsUsers();
            }
            else
            {
                TopStatusPaneView.getInstance().setServiceNumber(CommonConstantEntry.SERVICE_STATUS_OFFLINE, "离线");
            }
        }
    }

    private void addSubscribeMem()
    {
        ArrayList<String> phoneNums = UiApplication.getContactService().getSubscribePhoneNums();
        String[] usersArray = phoneNums.toArray(new String[phoneNums.size()]);
        if (usersArray.length > 0)
        {
            UiApplication.getPresenceService().presenceSubscribe(usersArray, true);
        }
    }

    @Override
    public void didReceivedNotification(final int id, final Object... args)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (id == UiEventEntry.NOTIFY_ATD_LOGIN)
                {
                    Log.info(TAG, "ATD_LOGIN: " + (String) args[0]);
                    UiApplication.isAtdOnline = true;
                    loadAppStartView((String) args[0]);
                    UiApplication.atdName = (String) args[0];
                    ServiceUtils.updateServiceConfig();
                }
                else if (id == UiEventEntry.NOTIFY_SHOW_CONTACT_DETAIL)
                {
                    Log.info(TAG, "SHOW_CONTACT_DETAIL: ");
                    clearFragmentStack(R.id.container_right_content);
                    Bundle data = new Bundle();
                    data.putInt(CommonConstantEntry.DATA_CONTACT_ID, (Integer) args[0]);
                    data.putBoolean(CommonConstantEntry.DATA_ENABLE, false);
                    turnToFragmentStack(R.id.container_right_content, ContactDetailFragmet.class, data);
                }
                else if (id == UiEventEntry.NOTIFY_SHOW_VS_LIST)
                {
                    loadVsView();
                    Log.info(TAG, "showVSFragment");
                }
                else if (id == UiEventEntry.EVENT_SHOW_CALL_LIST)
                {
                    Log.info(TAG, "showCallListFragment");
                    loadCallListView();
                }
                else if (id == UiEventEntry.NOTIFY_OPEN_CONF_CONTROL)
                {
                    Log.info(TAG, "NOTIFY_SHOW_CONF_CONTROL: ");
                    loadConfControlView((ConfModel) args[0], (Integer) args[1]);
                }
                else if (id == UiEventEntry.EVENT_SHOW_LEFT_PANE)
                {
                    Log.info(TAG, "EVENT_SHOW_LEFT_PANE: ");
                    if (!isContactList)
                    {
                        leftTabContainer.setVisibility(View.VISIBLE);
                    }
                }
                else if (id == UiEventEntry.NOTIFY_SHOW_REMOTE_VIDEO)
                {
                    Log.info(TAG, "NOTIFY_SHOW_REMOTE_VIDEO: ");
                    loadVideoView();

                    if (args.length > 0)
                    {
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.EVENT_ADD_REMOTE_VIDEO, (SurfaceView) args[0]);
                    }
                }
                else if (id == UiEventEntry.NOTIFY_SERVICE_STATUS)
                {
                    Log.info(TAG, "MESSAGE_NOTIFY_SERVICE_STATUS: " + (int[]) args[0]);
                    handleServiceStatus((int[]) args[0], (int[]) args[1]);
                }
                else if (id == UiEventEntry.NOTIFY_PRESENCE_USER_STATUS)
                {
                    Log.info(TAG, "NOTIFY_PRESENCE_USER_STATUS: ");
                    onUserStatusChanged((ArrayList<HashMap<String, Integer>>) args[0]);
                }
                else if (id == UiEventEntry.EVENT_LOCAL_VIDEO_SHOW)
                {
                    Log.info(TAG, "EVENT_LOCAL_VIDEO_SHOW: ");
                    localVideoContainer.setVisibility(View.VISIBLE);
                }
                else if (id == UiEventEntry.EVENT_LOCAL_VIDEO_HIDE)
                {
                    Log.info(TAG, "EVENT_LOCAL_VIDEO_HIDE: ");
                    localVideoContainer.setVisibility(View.GONE);
                }
                else if (id == UiEventEntry.EVENT_CONF_MEMBER_CALL_RECORD)
                {
                    CallRecord callRecord = (CallRecord) args[0];
                    int type = (Integer) args[1];
                    int visiblePosition = (Integer) args[2];
                    int count = (Integer) args[3];
                    loadConfMemberCallRecordView(callRecord, type, visiblePosition, count);
                }
                else if (id == UiEventEntry.REFRESH_CONTACT_VIEW)
                {
                    initLeftPaneData();
                }
                else if (id == UiEventEntry.CONTACT_TEMPCONF_SHOW)
                {
                    // 临时会议
                    loadContactSelectView(UiEventEntry.NOTIFY_CONF_TEMP, 5);
                }
                else if (id == UiEventEntry.EVENT_CAMERA_CONTROL)
                {
                    loadPtzControl((String) args[0], (String) args[1]);
                }
                else if (id == UiEventEntry.EVENT_UPDATE_RIGHT_TAB)
                {
                    if (args.length > 0)
                    {
                        cashedRightTabType = (Integer) args[0];
                    }
                    updateRightTabBg(cashedRightTabType);
                }
                else if (id == UiEventEntry.NOTIFY_CONF_TEMP)
                {
                    Log.info(TAG, "NOTIFY_CONF_TEMP");
                    boolean commonCall = false;

                    int type = (Integer) args[0];
                    ArrayList<String> numberList = new ArrayList<String>();
                    if (type == UiEventEntry.CONTACT_SELECTEADD_CONTACT)
                    {
                        int operaCode = (Integer) args[1];
                        if (operaCode == 0) // 取消
                        {
                            backToPreFragment(R.id.container_right_content);
                        }
                        else
                        {
                            ArrayList<Integer> selectContactList = (ArrayList<Integer>) args[2];
                            if (selectContactList != null && selectContactList.size() == 0)
                            {
                                return;
                            }
                            for (Integer contactId : selectContactList)
                            {
                                ContactModel contactModel = UiApplication.getContactService().getContactById(contactId);
                                String confNum = contactModel.getConfNum();
                                if (!TextUtils.isEmpty(confNum) && !numberList.contains(confNum))
                                {
                                    numberList.add(confNum);
                                    if (!ContactUtil.isVsByContactType(contactModel.getTypeName()))
                                    {
                                        commonCall = true;
                                    }
                                }
                            }
                            backToPreFragment(R.id.container_right_content);
                            backToPreFragment(R.id.container_right_content);
                        }
                    }
                    else if (type == UiEventEntry.CONTACT_SELECTEADD_DIAL)
                    {// 从拨号盘发起
                        String number = (String) args[1];
                        numberList.add(number);
                        commonCall = true;
                        backToPreFragment(R.id.container_right_content);
                        backToPreFragment(R.id.container_right_content);
                    }
                    // 一组联系人中如果有普通用户，则发起会议，否则发起监控
                    if (commonCall)
                    {
                        ServiceUtils.makeConf(HomeActivity.this, "临时会议", numberList);
                    }
                    else
                    {
                        UiApplication.getVsService().addVsUsers(numberList);
                    }
                }
                else if (id == UiEventEntry.ACTION_MOUBLE_NUMBER)
                {// 进入拨号盘
                    Log.info(TAG, "UiEventEntry.ACTION_MOUBLE_NUMBER::");
//                    Bundle data = (Bundle) args[0];

                    // 通过T30终端进入拨号盘
//                    Intent intent = new Intent(getApplicationContext(), DialActivity.class);
//                    intent.putExtras(data);
//                    startActivity(intent);
//                    loadDialView(false);
                    EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.ACTION_MOUBLE_NUMBER);
                }
                else if (id == UiEventEntry.OPEN_PRESENTATION_CONTROL)
                {
                    loadPresentationView();
                }
                else if (id == UiEventEntry.CLOSE_PRESENTATION_CONTROL)
                {
//                    UiApplication.presentation.dismiss();
                }
                else if (id == UiEventEntry.CALL_RECORD_MISSED_COUNT)
                {
                    Log.info(TAG, "CALL_RECORD_MISSED_COUNT::");
                    int missedCount = (Integer) args[0];
                    showPointOut(missedCount);
                }
                else if (id == UiEventEntry.Font_SIZE_CHANGED)
                {
                    recreate();
                }
                else if (id == UiEventEntry.CLOSE_DIAL)
                {
                    closeDial();
                }
                else if (id == UiEventEntry.NOTIFY_CAMERA_ERADY)
                {
                    localPreview = (SurfaceView) args[0];
                    updateLocalCamera(true);
                }
                else if (id == UiEventEntry.NOTIFY_NIGHT_SERVICE)
                {
                    Log.info(TAG, "NOTIFY_NIGHT_SERVICE");
                    boolean enable = (Boolean) args[0];
                    int result = (Integer) args[1];
                    boolean isAutoBrightness = SystemBrightManager.isAutoBrightness(HomeActivity.this);
                    if (result == CommonConstantEntry.RESPONSE_SUCCESS)
                    {
                        UiApplication.getConfigService().setNightService(enable);
                        if (enable)
                        {
                            TopStatusPaneView.getInstance().setNightServiceNofity(R.drawable.signal);
                            // 设置夜服屏幕亮度
                            if (isAutoBrightness)
                            { // 自动调整亮度
                                SystemBrightManager.stopAutoBrightness(HomeActivity.this);
                            }
                            else
                            {
                                SystemBrightManager.setBrightness(HomeActivity.this, UiConfigEntry.NIGHT_ON);
                            }

                            SystemBrightManager.saveBrightness(HomeActivity.this, UiConfigEntry.NIGHT_ON);
                        }
                        else
                        {
                            TopStatusPaneView.getInstance().setNightServiceNofity(-1);
                            // 恢复原始屏幕亮度
                            if (isAutoBrightness)
                            { // 自动调整亮度
                                SystemBrightManager.stopAutoBrightness(HomeActivity.this);
                            }
                            else
                            {
                                SystemBrightManager.setBrightness(HomeActivity.this, UiConfigEntry.NIGHT_OFF);
                            }

                            SystemBrightManager.saveBrightness(HomeActivity.this, UiConfigEntry.NIGHT_OFF);
                        }
                    }
                    else if (result == CommonConstantEntry.RESPONSE_FAILED)
                    {
                        ToastUtil.showToast("夜服设置失败，请联系管理员");
                    }
                }
                else if (id == UiEventEntry.NOTIFY_EXIT_SYSYTEM)
                {// 命令退出系统通知
                    exitSystem();
                }
            }
        });
    }

    /**
     * 方法说明 :通过拨号盘命令退出T30，进入安卓系统
     * @author chaimb
     * @Date 2015-9-21
     */
    private void exitSystem()
    {
        // 有业务进行时不允许退出登录
        if (ServiceUtils.getCurrentCallList().size() > 0 || UiApplication.getVsService().getOpenVsCount() > 0)
        {
            ToastUtil.showToast("请先关闭当前业务，再进行操作");
            return;
        }
        this.finish();
        // 退出App
        System.exit(0);
    }

    private void showPointOut(int missedCount)
    {
        Log.info(TAG, "showPointOut::missedCount==>>" + missedCount);
        // 得到要设置的item的view
        View view = tabListRight.getChildAt(2);
        if (view != null)
        {
            ImageView callrecordView = (ImageView) view.findViewById(R.id.right_tab);
            if (missedBadgeView == null)
            {
                missedBadgeView = new BadgeView(this, callrecordView);
            }
            if (missedCount == 0)
            {
                missedBadgeView.hide();
            }
            else
            {
                missedBadgeView.setText("" + missedCount);
                missedBadgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
                missedBadgeView.show();
            }
        }
        else
        {
            Log.info(TAG, "no right gridview!");
        }
    }

    private void updateLocalCamera(boolean show)
    {
        if (localPreview != null)
        {
            if (show)
            {
                if ((FrameLayout) localPreview.getParent() != null)
                {
                    ((FrameLayout) localPreview.getParent()).removeAllViews();
                }
                localVideoView.removeAllViews();
                localVideoView.addView(localPreview);
            }
            else
            {
                localVideoView.removeAllViews();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        String dialNum = ServiceUtils.getDialNum(keyCode);
        Log.info(TAG, dialNum);
        if (!(TextUtils.isEmpty(dialNum)))
        {
            loadDialView(true, dialNum);
        }
        return super.onKeyDown(keyCode, event);
    }

}
