package com.jiaxun.uil.ui.adapter;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaxun.sdk.scl.model.ConfMemModel;
import com.jiaxun.sdk.scl.model.ConfModel;
import com.jiaxun.sdk.scl.model.SCallModel;
import com.jiaxun.sdk.scl.module.conf.itf.SclConfService;
import com.jiaxun.sdk.scl.module.scall.itf.SclSCallService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.handler.CallEventHandler;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.CallListItem;
import com.jiaxun.uil.ui.fragment.ContactSelectFragment;
import com.jiaxun.uil.util.ServiceUtils;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：呼叫列表适配器
 *
 * @author  hubin
 *
 * @Date 2015-4-1
 */
public class CallListAdapter extends BaseAdapter
{
    private static final String TAG = CallListAdapter.class.getName();

    public List<CallListItem> mListItems;

    private ViewHolder viewHolder;

    protected LayoutInflater inflater;

//    protected int whichSelected = 0;

    private SclSCallService scallService;
    private SclConfService confService;
    private Drawable incallStatus;
    private Drawable muteStatus;
    private Drawable incomingCallStatus;
    private Drawable outgoingCallStatus;
    private Drawable holdCallStatus;
    private Drawable holdedCallStatus;
    private Drawable doubleHoldedCallStatus;

    private Drawable answerCall;
    private Drawable hangupCall;
    private Drawable holdCall;
    private Drawable retrieveCall;
    private Drawable mute;
    private Drawable unmute;
    private Drawable speaker;
    private Drawable noSpeaker;
    private Drawable enterConf;
    private Drawable closeConf;

    private Context context;
//    private ListView mListView;
    // 分钟与毫秒的换算关系
    private static final long MINUTE_RELATION = 1000 * 60;
    // 小时与毫秒的换算关系
    private static final long HOUR_RELATION = 1000 * 60 * 60;

    public CallListAdapter(Context mContext)
    {
        this.context = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.scallService = UiApplication.getSCallService();
        this.confService = UiApplication.getConfService();
        incallStatus = getDrawable(R.drawable.call_status_connect);
        muteStatus = getDrawable(R.drawable.player_silence);
        incomingCallStatus = getDrawable(R.drawable.call_status_ringing);
        outgoingCallStatus = getDrawable(R.drawable.call_status_dialing);
        holdCallStatus = getDrawable(R.drawable.call_status_hold);
        holdedCallStatus = getDrawable(R.drawable.call_status_holded);
        doubleHoldedCallStatus = getDrawable(R.drawable.call_status_double_hold);

        answerCall = getDrawable(R.drawable.call_answer);
        hangupCall = getDrawable(R.drawable.call_hangup);
        holdCall = getDrawable(R.drawable.call_hold);
        retrieveCall = getDrawable(R.drawable.call_retrieve);
        mute = getDrawable(R.drawable.call_mute);
        unmute = getDrawable(R.drawable.call_unmute);
        speaker = getDrawable(R.drawable.call_speaker);
        speaker.setBounds(0, 0, speaker.getMinimumWidth(), speaker.getMinimumHeight());
        noSpeaker = getDrawable(R.drawable.call_no_speaker);
        enterConf = getDrawable(R.drawable.conf_enter);
        closeConf = getDrawable(R.drawable.conf_close);
    }

    private Drawable getDrawable(int resid)
    {
        Drawable drawable = context.getResources().getDrawable(resid);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    @Override
    public int getCount()
    {
        if (mListItems != null && mListItems.size() > 0)
        {
            return mListItems.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Object getItem(int position)
    {
        if (mListItems != null && position >= 0 && position < mListItems.size())
        {
            return mListItems.get(position);
        }
        return new BaseListItem();
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.adapter_call_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.caller_icon);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.caller_name);
            viewHolder.numberView = (TextView) convertView.findViewById(R.id.caller_number);
            viewHolder.statusView = (TextView) convertView.findViewById(R.id.call_status);
//            viewHolder.statusIconView = (ImageView) convertView.findViewById(R.id.call_status_icon);
            viewHolder.timerView = (TextView) convertView.findViewById(R.id.call_list_timer);
            viewHolder.priorityView = (TextView) convertView.findViewById(R.id.call_priority);
            viewHolder.dropDownView = (LinearLayout) convertView.findViewById(R.id.sub_operation);
            viewHolder.closeBellBtn = (Button) convertView.findViewById(R.id.closebell);
            viewHolder.answerBtn = (Button) convertView.findViewById(R.id.answer);
            viewHolder.hangupBtn = (Button) convertView.findViewById(R.id.hangup);
            viewHolder.holdTB = (Button) convertView.findViewById(R.id.hold);
            viewHolder.muteTB = (Button) convertView.findViewById(R.id.mute);
            viewHolder.speakerTB = (Button) convertView.findViewById(R.id.speaker);
            viewHolder.intoConfBtn = (Button) convertView.findViewById(R.id.intoconf);
            viewHolder.call2Conf = (Button) convertView.findViewById(R.id.call2conf);
            convertView.setTag(viewHolder);
        }

        try
        {
            viewHolder.index = position;
            if (mListItems.size() > 0)
            {
                final CallListItem item = mListItems.get(position);
                // 显示通话对象
                viewHolder.nameView.setText(item.getName());
                viewHolder.numberView.setText(item.getContent());

                if (item.getCallModel().getPriority() > 0)
                {
                    // 显示优先级
                    viewHolder.priorityView.setText(item.getCallModel().getPriority() + "级");
                }

                // 显示记录icon
                int iconRes = item.getIconRes();
                if (iconRes > 0)
                {
                    viewHolder.iconView.setImageResource(iconRes);
                }
                else if (item.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || item.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
                {
                    viewHolder.iconView.setImageResource(R.drawable.conf_icon);
                }
                else
                {
                    viewHolder.iconView.setImageResource(R.drawable.user_icon);
                }

                // 显示操作区，初始化监听
                handleClickListener(item);
                // 初始化状态和业务控制区
                handleStatus(item);
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        return convertView;
    }

    private void handleClickListener(final CallListItem item)
    {
        if (item.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO || item.getType() == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO)
        {
            viewHolder.closeBellBtn.setVisibility(View.GONE);
            viewHolder.answerBtn.setText("控制");
            viewHolder.answerBtn.setCompoundDrawables(null, enterConf, null, null);
            viewHolder.hangupBtn.setText("关闭");
            viewHolder.hangupBtn.setCompoundDrawables(null, closeConf, null, null);
            viewHolder.answerBtn.setVisibility(View.VISIBLE);
            viewHolder.hangupBtn.setVisibility(View.VISIBLE);
            viewHolder.holdTB.setVisibility(View.GONE);
            viewHolder.muteTB.setVisibility(View.GONE);
            viewHolder.speakerTB.setVisibility(View.GONE);
            viewHolder.intoConfBtn.setVisibility(View.GONE);
            viewHolder.call2Conf.setVisibility(View.GONE);
            viewHolder.answerBtn.setOnClickListener(item.getAnswerConfListener());
            viewHolder.hangupBtn.setOnClickListener(item.getHangupConfListener());
        }
        else
        {
            viewHolder.answerBtn.setText("接听");
            viewHolder.hangupBtn.setText("挂断");
            viewHolder.answerBtn.setVisibility(View.VISIBLE);
            viewHolder.answerBtn.setCompoundDrawables(null, answerCall, null, null);
            viewHolder.hangupBtn.setVisibility(View.VISIBLE);
            viewHolder.hangupBtn.setCompoundDrawables(null, hangupCall, null, null);
            viewHolder.holdTB.setVisibility(View.VISIBLE);
            viewHolder.muteTB.setVisibility(View.VISIBLE);
            viewHolder.speakerTB.setVisibility(View.VISIBLE);
            viewHolder.closeBellBtn.setVisibility(View.GONE);
            viewHolder.intoConfBtn.setVisibility(View.GONE);
            viewHolder.call2Conf.setVisibility(View.GONE);
            final String confId = CallEventHandler.getConfSessionId();
            if (confId != null)
            {
                viewHolder.holdTB.setVisibility(View.GONE);
                viewHolder.muteTB.setVisibility(View.GONE);
                viewHolder.speakerTB.setVisibility(View.GONE);
                if (item.getStatus() != CommonConstantEntry.SCALL_STATE_RINGBACK)// 呼出振铃时不显示入会
                { //
                    viewHolder.intoConfBtn.setVisibility(View.VISIBLE);
                }
                viewHolder.intoConfBtn.setOnClickListener(item.getIntoConfListener());
            }
            if (item.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
            {
                viewHolder.call2Conf.setVisibility(confId != null ? View.GONE : View.VISIBLE);
                viewHolder.closeBellBtn.setVisibility(View.VISIBLE);
                viewHolder.closeBellBtn.setSelected(((SCallModel) item.getCallModel()).isCloseRing());
                viewHolder.call2Conf.setOnClickListener(item.getCallTransConfListener());
            }
            else if (item.getStatus() == CommonConstantEntry.SCALL_STATE_HOLD)
            {
                ((SCallModel) item.getCallModel()).setCloseRing(false);
            }
            viewHolder.closeBellBtn.setOnClickListener(item.getCloseBellListener());
            viewHolder.answerBtn.setOnClickListener(item.getAnswerListener());
            viewHolder.hangupBtn.setOnClickListener(item.getHangupListener());
            viewHolder.holdTB.setOnClickListener(item.getHoldListener());
            viewHolder.muteTB.setOnClickListener(item.getMuteListener());
            viewHolder.speakerTB.setOnClickListener(item.getSpeakerListener());
        }
    }

    private void handleStatus(final CallListItem item)
    {
        switch (item.getStatus())
        {
            case CommonConstantEntry.SCALL_STATE_PROCEEDING:
                viewHolder.statusView.setText("呼出");
                viewHolder.statusView.setCompoundDrawables(null, null, null, outgoingCallStatus);
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getStartTime()));
                viewHolder.answerBtn.setVisibility(View.GONE);
                viewHolder.holdTB.setVisibility(View.GONE);
                break;
            case CommonConstantEntry.SCALL_STATE_RINGING:
                viewHolder.statusView.setText("呼入");
                viewHolder.statusView.setCompoundDrawables(null, null, null, incomingCallStatus);
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getStartTime()));
                viewHolder.answerBtn.setVisibility(View.VISIBLE);
                viewHolder.holdTB.setVisibility(View.GONE);
                break;
            case CommonConstantEntry.SCALL_STATE_CONNECT:
                viewHolder.statusView.setText("通话");
                if (item.getCallModel().isConfMember() && !item.isAudioEnabled())
                {
                    viewHolder.statusView.setCompoundDrawables(null, null, null, muteStatus);
                }
                else
                {
                    viewHolder.statusView.setCompoundDrawables(null, null, null, incallStatus);
                }
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getConnectTime()));
                viewHolder.answerBtn.setVisibility(View.GONE);
                viewHolder.holdTB.setCompoundDrawables(null, holdCall, null, null);
                viewHolder.holdTB.setText("保持");
                viewHolder.holdTB.setVisibility(View.VISIBLE);
                viewHolder.holdTB.setSelected(false);
                break;
            case CommonConstantEntry.SCALL_STATE_HOLD:
                viewHolder.statusView.setText("保持");
                viewHolder.statusView.setCompoundDrawables(null, null, null, holdCallStatus);
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getConnectTime()));
                viewHolder.answerBtn.setVisibility(View.GONE);
                viewHolder.holdTB.setVisibility(View.VISIBLE);
                viewHolder.holdTB.setCompoundDrawables(null, retrieveCall, null, null);
                viewHolder.holdTB.setText("恢复");
                viewHolder.holdTB.setSelected(true);
                break;
            case CommonConstantEntry.SCALL_STATE_REMOTE_HOLD:
                viewHolder.statusView.setText("被保持");
                viewHolder.statusView.setCompoundDrawables(null, null, null, holdedCallStatus);
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getConnectTime()));
                viewHolder.answerBtn.setVisibility(View.GONE);
                viewHolder.holdTB.setVisibility(View.VISIBLE);
                viewHolder.holdTB.setCompoundDrawables(null, holdCall, null, null);
                viewHolder.holdTB.setText("保持");
                viewHolder.holdTB.setSelected(false);
                break;
            case CommonConstantEntry.SCALL_STATE_BOTH_HOLD:
                viewHolder.statusView.setText("双向保持");
                viewHolder.statusView.setCompoundDrawables(null, null, null, doubleHoldedCallStatus);
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getConnectTime()));
                viewHolder.answerBtn.setVisibility(View.GONE);
                viewHolder.holdTB.setVisibility(View.VISIBLE);
                viewHolder.holdTB.setCompoundDrawables(null, retrieveCall, null, null);
                viewHolder.holdTB.setText("恢复");
                viewHolder.holdTB.setSelected(true);
                break;
            case CommonConstantEntry.CONF_STATE_EXIT:
                viewHolder.statusView.setText("离会");
                viewHolder.statusView.setCompoundDrawables(null, null, null, holdCallStatus);
                break;
            case CommonConstantEntry.CONF_STATE_CONNECT:
                viewHolder.statusView.setText("会议中");
                viewHolder.statusView.setCompoundDrawables(null, null, null, incallStatus);
                viewHolder.timerView.setText(getCallTime(item.getCallModel().getConnectTime()));
                break;
            default:
                break;
        }
    }

    private String getCallTime(long startTimer)
    {
        final long timeSpace = Calendar.getInstance().getTimeInMillis() - startTimer;
        final long hour = timeSpace / HOUR_RELATION;
        final long minute = timeSpace / MINUTE_RELATION - hour * 60;
        final long second = timeSpace / 1000 - hour * 60 * 60 - minute * 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void setListItem(List<? extends BaseListItem> listItems)
    {
        if (listItems != null)
        {
            mListItems = (List<CallListItem>) listItems;
            final long delay = 1000L;
            for (final CallListItem listItem : mListItems)
            {
                final String sessionId = listItem.getCallModel().getSessionId();
                listItem.setAnswerConfListener(new OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:answerConf:: sessionId:"+ sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_OPEN_CONF_CONTROL, listItem.getCallModel(),
                                listItem.getStatus());
                    }
                });
                listItem.setHangupConfListener(new OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:hangupConf:: sessionId:"+ sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        confService.confClose(sessionId);
                        EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_REMOVE_CONF_FROM_CALL_LIST);
                        for (ConfMemModel confMemModel : ((ConfModel) listItem.getCallModel()).getMemList())
                        {
                            UiUtils.removeRemoteVideo(confMemModel.getNumber());
                        }
                    }
                });

                listItem.setIntoConfListener(new OnClickListener()
                {

                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:callintoConf:: sessionId:"+ sessionId);
                        listItem.setIntoConf(true);
                        answserCall(sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        ServiceUtils.handleConnectedCalls(sessionId, ServiceUtils.CALL2CONF2, listItem);
                    }
                });

                listItem.setCallTransConfListener(new OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:callTransConf:: sessionId:"+ sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        listItem.setTransferToConf(true);
                        EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.EVENT_SCALL_TO_CONF, listItem.getName());
                    }
                });
                listItem.setCloseBellListener(new OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:closeBell:: sessionId:"+ sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        boolean enable = !((SCallModel) listItem.getCallModel()).isCloseRing();
                        viewHolder.closeBellBtn.setSelected(enable);
                        UiApplication.getSCallService().sCallCloseRing(sessionId, enable);
                    }
                });
                listItem.setAnswerListener(new OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:answserCall:: sessionId:" + sessionId);
//                      for (CallListItem callListItem : ServiceUtils.getCurrentCallList())
//                      {
//                          if (callListItem.getCallModel().isConfMember() && callListItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
//                          {
//                              ToastUtil.showToast("正在开会中，请先退出会议再接听");
//                              return;
//                          }
//                      }
                        ServiceUtils.handleConnectedCalls(sessionId, ServiceUtils.DEFAULT_CALL_TYPE);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        answserCall(sessionId);
                    }
                });
                listItem.setHoldListener(new OnClickListener()
                {

                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:holdTB:: sessionId:" + sessionId);
                        if (listItem.getCallModel().isConfMember() && listItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
                        {
                            ToastUtil.showToast("正在开会中，不能保持");
                            return;
                        }

                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);

                        if ("保持".equals(((Button) v).getText()))
                        {
                            Log.info(TAG, "hold Call::sessionId:" + sessionId);
                            scallService.sCallHold(sessionId);
//                            ((Button) v).setText("恢复");
//                            ((Button) v).setCompoundDrawables(null, retrieveCall, null, null);
//                            v.setSelected(true);
                        }
                        else
                        {
                            Log.info(TAG, "retrieve Call::sessionId:" + sessionId);
                            ServiceUtils.handleConnectedCalls(sessionId, ServiceUtils.DEFAULT_CALL_TYPE);
                            scallService.sCallRetrieve(sessionId);
//                            ((Button) v).setText("保持");
//                            ((Button) v).setCompoundDrawables(null, holdCall, null, null);
//                            v.setSelected(false);
                        }
                    }
                });
                listItem.setHangupListener(new OnClickListener()
                {

                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:hangup Call::sessionId:" + sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);
                        scallService.sCallRelease(sessionId, CommonConstantEntry.CALL_END_HUANGUP);
                    }
                });
                listItem.setMuteListener(new OnClickListener()
                {
                    @Override
                    public void onClick(final View v)
                    {
                        Log.info(TAG, "click:mute:: sessionId:"+ sessionId);
                        ((Button) v).setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ((Button) v).setClickable(true);
                            }
                        }, delay);

                        if (!v.isSelected())
                        {
                            Log.info(TAG, "unmute Call::sessionId:" + sessionId);
                            scallService.sCallAudioEnable(sessionId, true);
                            ((Button) v).setText("静音关");
                            ((Button) v).setCompoundDrawables(null, unmute, null, null);
                            v.setSelected(true);
                        }
                        else
                        {
                            Log.info(TAG, "mute Call::sessionId:" + sessionId);
                            scallService.sCallAudioEnable(sessionId, false);
                            ((Button) v).setText("静音开");
                            ((Button) v).setCompoundDrawables(null, mute, null, null);
                            v.setSelected(false);
                        }

                    }
                });
                listItem.setSpeakerListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!v.isSelected())
                        {
                            Log.info(TAG, "click:speaker on Call::sessionId:" + sessionId);
                            ((Button) v).setText("听筒");
                            ((Button) v).setCompoundDrawables(null, speaker, null, null);
                            v.setSelected(true);
                        }
                        else
                        {
                            Log.info(TAG, "speaker off Call::sessionId:" + sessionId);
                            ((Button) v).setText("免提");
                            ((Button) v).setCompoundDrawables(null, noSpeaker, null, null);
                            v.setSelected(false);
                        }
                    }
                });
            }
            notifyDataSetChanged();
        }
        else
        {
            Log.info(TAG, "setListItem listItems == null!");
        }
    }

    class ViewHolder
    {
        ImageView iconView;
        TextView nameView;
        TextView numberView;
        TextView statusView;
        TextView timerView;
        TextView priorityView;
        LinearLayout dropDownView;
        Button closeBellBtn;
        Button answerBtn;
        Button hangupBtn;
        Button holdTB;
        Button muteTB;
        Button speakerTB;
        Button intoConfBtn;
        Button call2Conf;
        int index;
    }

    public void answserCall(String sessionId)
    {
        try
        {
            Log.info(TAG, "answserCall:: sessionId:" + sessionId);
            scallService.sCallAnswer(sessionId, null, 1, true);
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }
    }
}
