package com.jiaxun.uil.ui.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.sdk.scl.model.VsModel;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.VsListItem;
import com.jiaxun.uil.module.surveillance.impl.UilVsServiceImpl;
import com.jiaxun.uil.module.surveillance.itf.UilVsService;
import com.jiaxun.uil.ui.adapter.VsListAdapter;
import com.jiaxun.uil.ui.view.DraggableGridView;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.enums.EnumVsEditType;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：监控列表界面
 * 
 * @author zhangxd
 * 
 * @Date 2015-5-27
 */
public class VsListFragment extends BaseFragment implements OnClickListener, OnItemClickListener, OnItemSelectedListener, NotificationCenterDelegate
{
    private static final String TAG = VsListFragment.class.getName();
    private DraggableGridView groupGrid;
    public static VsListAdapter vsListAdapter;
    private Spinner spinner_time;
    private Handler mHandler;
    /**
     * 普通状态
     */
    private LinearLayout linearLayout_normal;
    /**
     * 删除用户状态
     */
    private LinearLayout linearLayout_delete;
    /**
     * 添加监控用户
     */
    private Button add_vs_user;
    /**
     *  轮巡开启按钮
     */
    private Button buttonStart;
    /**
     * 确定删除选中用户
     */
    private Button sureDelete;
    /**
     * 取消删除选中用户
     */
    private Button cancelDelete;
    /**
     *  删除全部用户
     */
    private Button selectAll;
    private Button removeVsUser;
    private Button finishVs;
    public static final int UPDATE_LIST_ITEM = 1;
    private UilVsService uilVsService;
    private ArrayList<VsListItem> vsList;
    private int itemIndex = -1;
    private int timePeriod = 15;// 默认15秒
    public VsListFragment()
    {
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_VS_STATUS_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.UPDATE_VSLIST);
        uilVsService = UiApplication.getVsService();
        vsList = uilVsService.getVsUserList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_UPDATE_RIGHT_TAB, UiEventEntry.TAB_VS_LIST);
        // 注册窗口改变观察者
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.VIDEO_WINDOW_CHANGE);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.NOTIFY_VS_MEMBER_ADD);
        if (uilVsService != null)
        {
            if (uilVsService.isLoopStarted())
            {
                buttonStart.setText("停止");
                spinner_time.setEnabled(false);
            }
            else
            {
                buttonStart.setText("开始");
                spinner_time.setEnabled(true);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initComponentViews(View view)
    {
        groupGrid = (DraggableGridView) view.findViewById(R.id.group_grid);
        groupGrid.setType(CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE);
        add_vs_user = (Button) view.findViewById(R.id.add_vs_user);

        buttonStart = (Button) view.findViewById(R.id.buttonStart);
        sureDelete = (Button) view.findViewById(R.id.sureDelete);
        cancelDelete = (Button) view.findViewById(R.id.cancelDelete);
        selectAll = (Button) view.findViewById(R.id.selectAll);
        removeVsUser = (Button) view.findViewById(R.id.removeVsUser);
        finishVs = (Button) view.findViewById(R.id.vs_finish);
        spinner_time = (Spinner) view.findViewById(R.id.spinner_time);
        linearLayout_normal = (LinearLayout) view.findViewById(R.id.linearLayout_normal);
        linearLayout_delete = (LinearLayout) view.findViewById(R.id.linearLayout_delete);
        // switchBtn.setOnClickListener(this);
        vsListAdapter = new VsListAdapter(getActivity(), this, vsList, groupGrid);
        groupGrid.setAdapter(vsListAdapter);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item);
        String screenTime[] = getResources().getStringArray(R.array.circletime);// 资源文件
        for (int i = 0; i < screenTime.length; i++)
        {
            arrayAdapter.add(screenTime[i]);
        }
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_time.setAdapter(arrayAdapter);
        groupGrid.setOnItemClickListener(this);
        add_vs_user.setOnClickListener(this);
        buttonStart.setOnClickListener(this);
        sureDelete.setOnClickListener(this);
        cancelDelete.setOnClickListener(this);
        selectAll.setOnClickListener(this);
        removeVsUser.setOnClickListener(this);
        finishVs.setOnClickListener(this);
        spinner_time.setOnItemSelectedListener(this);
    }
    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.NOTIFY_VS_MEMBER_ADD);
    }
    @Override
    public void onDestroy()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.VIDEO_WINDOW_CHANGE);
        super.onDestroy();
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_vs_list;
    }

    /**
     * 方法说明 : 看是否监控列表中存在这个号码的用户
     * @param number
     * @param status
     * @param vsModel
     * @return 
     * @author zhangxd
     * @Date 2015-6-12
     */
    private VsListItem getExistedCall(String number, int status, VsModel vsModel)
    {
        VsListItem vsListItem = null;
        itemIndex = -1;
        if (TextUtils.isEmpty(number))
        {
            return null;
        }
        for (int i = 0; i < vsList.size(); i++)
        {
            vsListItem = vsList.get(i);
            if (number.equals(vsListItem.getVsModel().getVideoNum()))
            {
                itemIndex = i;
                break;
            }
            else
            {
                vsListItem = null;
            }
        }
        return vsListItem;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.removeVsUser:
                if (uilVsService.isLoopStarted())
                {
                    ToastUtil.showToast("提示", "请停止轮巡后，再移除用户");
                }
                else
                {
//                    if (vsListAdapter.getEditType() == EnumVsEditType.SELECT)
//                    {
//                        removeVsUser.setText("移除用户");
//                        vsListAdapter.setEditType(EnumVsEditType.NORMAL);
//                        vsListAdapter.notifyDataSetChanged();
//                        linearLayout_normal.setVisibility(View.VISIBLE);
//                        linearLayout_delete.setVisibility(View.GONE);
//                        for (int i = 0; i < vsList.size(); i++)
//                        {
//                            vsList.get(i).setSelected(false);
//                        }
//                        vsListAdapter.notifyDataSetChanged();
//                    }
//                    else 
                    if (vsListAdapter.getEditType() == EnumVsEditType.NORMAL)
                    {
                        linearLayout_normal.setVisibility(View.GONE);
                        linearLayout_delete.setVisibility(View.VISIBLE);
                        vsListAdapter.setEditType(EnumVsEditType.SELECT);
                        vsListAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.vs_finish:
                if (uilVsService.isLoopStarted())
                {
                    ToastUtil.showToast("提示", "请停止轮巡后，结束监控");
                }
                else
                {
                    closeAllVideo();
                }
                break;
            case R.id.cancelDelete:
                if (vsListAdapter.getEditType() == EnumVsEditType.SELECT)
                {
                    linearLayout_normal.setVisibility(View.VISIBLE);
                    linearLayout_delete.setVisibility(View.GONE);
                    vsListAdapter.setEditType(EnumVsEditType.NORMAL);
                    vsListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.sureDelete:
                ArrayList<String> deleteUserNumberList = new ArrayList<String>();
                for (VsListItem vsListItem : vsList)
                {
                    if (vsListItem.isSelected())
                    {
                        deleteUserNumberList.add(vsListItem.getVsModel().getVideoNum());
                    }
                }
                uilVsService.deleteVsUsers(deleteUserNumberList);
                // 如果删除后发现监控列表为空，按钮恢复原始状态
                if (vsList.size() == 0)
                {
                    vsListAdapter.setEditType(EnumVsEditType.NORMAL);
                    vsListAdapter.notifyDataSetChanged();
                    linearLayout_normal.setVisibility(View.VISIBLE);
                    linearLayout_delete.setVisibility(View.GONE);
                }
                vsListAdapter.notifyDataSetChanged();
                break;
            case R.id.selectAll:
                for (int i = 0; i < vsList.size(); i++)
                {
                    vsList.get(i).setSelected(true);
                }
                vsListAdapter.notifyDataSetChanged();
                break;
            case R.id.add_vs_user:
                if (uilVsService.isLoopStarted())
                {
                    ToastUtil.showToast("提示", "请停止轮巡后，再添加用户");
                }
                else
                {
                    ArrayList<Integer> selectedContactList = new ArrayList<Integer>();
                    for(VsListItem vsListItem : vsList)
                    {
                        String num = vsListItem.getVsModel().getVideoNum();
                        ContactModel contact = UiApplication.getContactService().getContactByPhoneNum(num);
                        if(contact != null)
                        {
                            selectedContactList.add(contact.getId()); 
                        }
                    }
//                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_VS_MEMBER_ADD);
                    Bundle data = new Bundle();
                    data.putInt("gridColumns", 5);
                    data.putInt(CommonConstantEntry.DATA_TYPE, UiEventEntry.NOTIFY_VS_MEMBER_ADD);
                    data.putInt("showType", 2);// 只选监控用户
                    data.putIntegerArrayList("selectedContactList", selectedContactList);
                    parentActivity.turnToFragmentStack(R.id.container_right_content, ContactSelectAddFragment.class, data);
                }
                break;
            case R.id.buttonStart:
                // 如果还没开始轮巡
                if (!uilVsService.isLoopStarted())
                {
                    int windowsCount = UiApplication.getConfigService().getVideoWindowCount();
                    // 自适应屏幕按9分屏
                    if (windowsCount == UiEventEntry.SCREEN_TYPE_AUTO)
                    {
                        windowsCount = UiEventEntry.SCREEN_TYPE_9;
                    }
                    if (vsList.size() == 0)
                    {
                        // 没有用户时提示用户
                        ToastUtil.showToast("提示", "没有监控用户，请添加用户后再进行轮巡");
                    }
                    else
                    {
                        if (vsList.size() > windowsCount)
                        {
                            closeAllVideo();
                            uilVsService.vsLoopStart(timePeriod);
                            buttonStart.setText("停止");
                            spinner_time.setEnabled(false);
                        }
                        else
                        {
                            ToastUtil.showToast("提示", "窗口数已经够用,不用轮巡");
                        }
                    }
                }
                else
                {
                    uilVsService.vsLoopStop();
                    buttonStart.setText("启动");
                    spinner_time.setEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3)
    {
        // 选择删除状态
        if (vsListAdapter.getEditType() == EnumVsEditType.SELECT)
        {
            vsList.get(postion).triggerSelected();
            vsListAdapter.notifyDataSetChanged();
        }// 普通状态
        else
        {
            if (!uilVsService.isLoopStarted())
            {
                VsListItem vsListItem = vsList.get(postion);
                VsModel vsModel = vsListItem.getVsModel();
                if (!vsListItem.isOpening())
                {
                    // 这个号码已经在窗口打开
                    if (vsListItem.isOpened())
                    {
                        Log.info(TAG, "Close" + " number: " + vsModel.getSessionId());
                        uilVsService.closeVs(vsModel.getSessionId());
                    }
                    else
                    {
                        Log.info(TAG, "Open:" + " number: " + vsModel.getVideoNum());
                        int windowsCount = UiApplication.getConfigService().getVideoWindowCount();
                        // 自适应屏幕按9分屏
                        if (windowsCount == UiEventEntry.SCREEN_TYPE_AUTO)
                        {
                            windowsCount = UiEventEntry.SCREEN_TYPE_9;
                        }
                        int openedUserCount = uilVsService.getOpenVsCount();
                        // 如果有可用窗口再打开
                        if (openedUserCount < windowsCount)
                        {
                            vsListItem.setOpening(true);
                            vsListAdapter.updateView(vsListItem);
                            // ServiceUtils.makeVSCall(vsList.get(postion).getVsModel().getVideoNum());
                            uilVsService.openVs(vsModel.getVideoNum());
                        }
                    }
                }
                else
                {
                    // 如果正在打开
                    uilVsService.closeVs(vsModel.getSessionId());
                }

            }
            else
            {
                ToastUtil.showToast("提示", "轮巡中");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        // TODO Auto-generated method stub
        switch (arg2)
        {
            case 0:
                timePeriod = 15;
                break;
            case 1:
                timePeriod = 30;
                break;
            case 2:
                timePeriod = 60;
                break;
            default:
                timePeriod = 15;
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void didReceivedNotification(int id, Object... args)
    {
        if (id == UiEventEntry.VIDEO_WINDOW_CHANGE)
        {
            if (uilVsService.isLoopStarted())
            {
                uilVsService.vsLoopStop();
                int windowsCount = UiApplication.getConfigService().getVideoWindowCount();
                // 自适应屏幕按9分屏
                if (windowsCount == UiEventEntry.SCREEN_TYPE_AUTO)
                {
                    windowsCount = UiEventEntry.SCREEN_TYPE_9;
                }
                if (vsList.size() > windowsCount)
                {
                    closeAllVideo();
                    uilVsService.vsLoopStart(timePeriod);
                }
                else
                {
                    buttonStart.setText("启动");
                    spinner_time.setEnabled(true);
                    ToastUtil.showToast("提示", "窗口数已经够用,不用轮巡");
                }
            }
        }
        else if (id == UiEventEntry.UPDATE_VSLIST)
        {
            // 在没有打开监控界面时候把监控界面打开
            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_SHOW_VS_LIST);
            if (vsListAdapter != null)
            {
                vsListAdapter.notifyDataSetChanged();
            }
        }
        else if (id == UiEventEntry.NOTIFY_VS_STATUS_CHANGE)
        {
            String sessionId = (String) args[0];
            int status = (Integer) args[1];
            final VsModel vsModel = (VsModel) args[2];
            int reason = (Integer) args[3];
            Log.info(TAG, "onVsStatusChange::sessionId: " + sessionId + ", status:" + status + " reason:" + reason);
            VsListItem vsListItem = getExistedCall(vsModel.getVideoNum(), status, vsModel);
            switch (status)
            {
                case CommonConstantEntry.VS_STATE_CLOSE:
                    Log.info(TAG, "number" + vsModel.getVideoNum() + "，vs over\r\n");
                    if (itemIndex != -1)
                    {
                        vsListItem.setOpened(false);
                        vsListItem.setOpened(false);
                        vsListItem.setOpening(false);
                        // 更新ui
                        if (vsListAdapter != null)
                        {
                            updateListItem(vsListItem);
                        }
                        // 移除窗口
                        if (vsListItem != null)
                        {
                            UiUtils.removeRemoteVideo(vsListItem.getVsModel().getVideoNum());
                        }
                    }

                    break;
                case CommonConstantEntry.VS_STATE_OPEN:
                    Log.info(TAG, "number：" + vsModel.getVideoNum() + "，vs start\r\n");
                    if (vsListItem != null)
                    {
                        // ToastUtil.showToast("通知", vsModel.getVideoNum() +
                        // ":视频监控开启 ");
                        vsListItem.setVsModel(vsModel);
                        vsListItem.setOpening(false);
                        vsListItem.setOpened(true);
                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_SHOW_VS_LIST);
                        if (vsListAdapter != null)
                        {
                            updateListItem(vsListItem);
                        }
                    }
                    else
                    {
                        Log.error(TAG, "vsListItem null");
                    }
                    break;
                case CommonConstantEntry.VS_STATE_OPEN_ACK:
                    if (itemIndex != -1)
                    {
                        // 赋给id，以便于在结束监控时候，如果正在打开视频（对方尚未接听），要关闭时需要会话id
                        vsListItem.getVsModel().setSessionId(sessionId);
                    }
                    Log.info(TAG, "number：" + vsModel.getVideoNum() + "，vs request,wait\r\n");
                    break;
                default:
                    break;
            }
        }else if(id == UiEventEntry.NOTIFY_VS_MEMBER_ADD)
        {
          ArrayList<String> addVsNumberList = new ArrayList<String>();

          int type = (Integer) args[0];

          if (type == UiEventEntry.CONTACT_SELECTEADD_CONTACT)
          {
              int operaCode = (Integer) args[1];
              if(operaCode == 0)
              {
                  parentActivity.backToPreFragment(R.id.container_right_content);
              }else
              {
                  parentActivity.backToPreFragment(R.id.container_right_content);
                  parentActivity.backToPreFragment(R.id.container_right_content);
                  ArrayList<Integer> selectContactListVs = (ArrayList<Integer>) args[2];
                  for (Integer contactID : selectContactListVs)
                  {
                      ContactModel contactModel = UiApplication.getContactService().getContactById(contactID);
                      String num = null;
                      if(contactModel.getPhoneNumList().size() > 0)
                      {
                          num=contactModel.getPhoneNumList().get(0).getNumber();    
                      }
                      if (!TextUtils.isEmpty(num))
                      {
                          addVsNumberList.add(num);
                      }
                  }
              }
          }
          else if (type == UiEventEntry.CONTACT_SELECTEADD_DIAL)
          {// 从拨号盘发起
              parentActivity.clearFragmentStack(R.id.container_right_content);
              String number = (String) args[1];
              addVsNumberList.add(number);
          }
          UiApplication.getVsService().addVsUsers(addVsNumberList);
        }
    }

    private void closeAllVideo()
    {
        for (VsListItem vsListItem : vsList)
        {
            if (vsListItem.isOpened())
            {
                uilVsService.closeVs(vsListItem.getVsModel().getSessionId());
                vsListItem.setOpened(false);
            }
            else if (vsListItem.isOpening())
            {
                uilVsService.closeVs(vsListItem.getVsModel().getSessionId());
                vsListItem.setOpening(false);
            }
        }
        if (vsListAdapter != null)
        {
            vsListAdapter.notifyDataSetChanged();
        }
    }

    public void setHandler(Handler handler)
    {
        this.mHandler = handler;
    }

    /**
     * 方法说明 :更新单条记录
     * @param callListItem
     * @author zhangxd
     * @Date 2015-7-14
     */
    private void updateListItem(VsListItem vsListItem)
    {
        if (mHandler != null)
        {
            Message msg = new Message();
            msg.what = UPDATE_LIST_ITEM;
            msg.obj = vsListItem;
            mHandler.sendMessage(msg);
        }
    }
}
