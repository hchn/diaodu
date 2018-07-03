package com.jiaxun.uil.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jiaxun.sdk.scl.module.conf.itf.SclConfService;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.BaseListItem;
import com.jiaxun.uil.model.ConfMemberItem;
import com.jiaxun.uil.module.config.impl.ConfigServiceImpl;
import com.jiaxun.uil.module.config.itf.ConfigService;
import com.jiaxun.uil.ui.fragment.ConfControlFragment;
import com.jiaxun.uil.util.ServiceUtils;

/**
 * 说明：呼叫列表适配器
 *
 * @author  hubin
 *
 * @Date 2015-4-1
 */
public class ConfMemberAdapter extends BaseAdapter
{
    private static final String TAG = ConfMemberAdapter.class.getName();

    public List<ConfMemberItem> mListItems;

    private ViewHolder viewHolder;

    protected LayoutInflater inflater;

    protected int whichSelected = 0;

//    private TranslateAnimation mShowAction;
//    private TranslateAnimation mHiddenAction;
    private SclConfService confService;
    private Drawable inConfStatus;
    private Drawable ringStatus;
//    private Drawable idleStatus;
    private GridView mListView;
    private ConfControlFragment parentContainer;

    public ConfMemberAdapter(ConfControlFragment fragment, ArrayList<ConfMemberItem> listItems)
    {
        this.parentContainer = fragment;
        this.inflater = LayoutInflater.from(parentContainer.getActivity());
        this.confService = UiApplication.getConfService();
        parentContainer.setHandler(mHandler);
        inConfStatus = parentContainer.getResources().getDrawable(R.drawable.call_status_connect);
        ringStatus = parentContainer.getResources().getDrawable(R.drawable.call_status_ringing);
//        idleStatus = parentContainer.getResources().getDrawable(R.drawable.call_hold);
        inConfStatus.setBounds(0, 0, inConfStatus.getMinimumWidth(), inConfStatus.getMinimumHeight());
        ringStatus.setBounds(0, 0, ringStatus.getMinimumWidth(), ringStatus.getMinimumHeight());
//        idleStatus.setBounds(0, 0, idleStatus.getMinimumWidth(), idleStatus.getMinimumHeight());

        setListItem(listItems, 0);
    }

    public void setListView(GridView listView)
    {
        this.mListView = listView;
    }

    public int getSelectedPosition()
    {
        return whichSelected;
    }

    public ConfMemberItem getSelectedItem()
    {
        if (mListItems != null && whichSelected >= 0 && whichSelected < mListItems.size())
        {
            return mListItems.get(whichSelected);
        }
        return null;
    }

    public void triggerSelect(int position)
    {
        if (position >= 0 && position < getCount())
        {
            whichSelected = position;
            mListItems.get(position).triggerSelected();
            Log.info(TAG, "position: " + position + "mListItems.get(i):" + mListItems.get(position).isSelected());
            updateSingleRow(mListItems.get(position));
//            for (int i = 0; i < mListItems.size(); i++)
//            {
//                if (i == position)
//                {
//                    mListItems.get(i).triggerSelected();
//                }
//                mListItems.get(i).setSelected(false);
//            }
        }
//        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        if (mListItems == null)
        {
            return 0;
        }
        else
        {
            return mListItems.size();
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
            convertView = inflater.inflate(R.layout.adapter_conf_member, null);
            viewHolder = new ViewHolder();
            viewHolder.confMemberLayout = (LinearLayout) convertView.findViewById(R.id.conf_member_layout);
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.member_icon);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.numberView = (TextView) convertView.findViewById(R.id.member_number);
            viewHolder.memberStatus = (TextView) convertView.findViewById(R.id.conf_member_status);
            viewHolder.audioStatus = (ImageView) convertView.findViewById(R.id.audio_status);
            viewHolder.videoStatus = (ImageView) convertView.findViewById(R.id.video_status);
            viewHolder.shareStatus = (ImageView) convertView.findViewById(R.id.share_status);
            viewHolder.confOpr = (ImageView) convertView.findViewById(R.id.conf_opr);
            convertView.setTag(viewHolder);
        }

        try
        {
           
            viewHolder.index = position;
            final ConfMemberItem item = mListItems.get(position);
            viewHolder.nameView.setText(item.getName());
            viewHolder.numberView.setText(item.getContent());
            // 显示成员通话状态
            switch (item.getConfMemModel().getStatus())
            {
                case CommonConstantEntry.CONF_MEMBER_STATE_CONNECT:
                    viewHolder.confOpr.setImageResource(R.drawable.conf_member_opr_out);
//                    viewHolder.memberStatus.setText("会议中");
//                    viewHolder.memberStatus.setCompoundDrawables(null, null, inConfStatus, null);
                    viewHolder.numberView.setCompoundDrawables(null, null, inConfStatus, null);
                    break;
                case CommonConstantEntry.CONF_MEMBER_STATE_IDLE:
                    viewHolder.confOpr.setImageResource(R.drawable.conf_member_opr_in);
//                    viewHolder.memberStatus.setText("离会中");
//                    viewHolder.memberStatus.setCompoundDrawables(null, null, null, null);
                    viewHolder.numberView.setCompoundDrawables(null, null, null, null);
                    break;
                case CommonConstantEntry.CONF_MEMBER_STATE_RING:
                    viewHolder.confOpr.setImageResource(R.drawable.conf_member_opr_out);
//                    viewHolder.memberStatus.setText("振铃中");
//                    viewHolder.memberStatus.setCompoundDrawables(null, null, ringStatus, null);
                    viewHolder.numberView.setCompoundDrawables(null, null, ringStatus, null);
                    break;
                default:
                    break;
            }

            // 显示成员媒体控制状态
            final String sessionId = parentContainer.getConfId();
            final String userNum = item.getConfMemModel().getNumber();
            if (item.getConfMemModel().isAudioEnabled())
            {
                viewHolder.audioStatus.setImageResource(R.drawable.conf_member_status_audio_on);
            }
            else
            {
                viewHolder.audioStatus.setImageResource(R.drawable.conf_member_status_audio_off);
            }

            if (item.getConfMemModel().isVideoEnabled())
            {
                viewHolder.videoStatus.setImageResource(R.drawable.conf_member_status_video_on);
            }
            else
            {
                viewHolder.videoStatus.setImageResource(R.drawable.conf_member_status_video_off);
            }

            if (item.getConfMemModel().isVideoShared())
            {
                viewHolder.shareStatus.setImageResource(R.drawable.conf_member_status_share_on);
            }
            else
            {
                viewHolder.shareStatus.setImageResource(R.drawable.conf_member_status_share_off);
            }
            
            
            if (UiApplication.getConfigService().isVideoCallEnabled())
            {
                viewHolder.confMemberLayout.setBackgroundResource(R.drawable.conf_member_status_bg);
                viewHolder.videoStatus.setVisibility(View.VISIBLE);
            }
            else
            {
                viewHolder.confMemberLayout.setBackgroundResource(R.drawable.conf_member_novideo_bg);
                viewHolder.videoStatus.setVisibility(View.GONE);
            }

            // 注册成员离会入会操作事件
            viewHolder.confOpr.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (item.getConfMemModel().getStatus() == CommonConstantEntry.CONF_MEMBER_STATE_IDLE)
                    {
                        Log.info(TAG, "member_remove: false" + " userNum:" + userNum);
                        ServiceUtils.handleConnectedCalls(sessionId,ServiceUtils.DEFAULT_CALL_TYPE);
                        confService.confUserAdd(sessionId, userNum);
                    }
                    else if (item.getConfMemModel().getStatus() == CommonConstantEntry.CONF_MEMBER_STATE_CONNECT
                            || item.getConfMemModel().getStatus() == CommonConstantEntry.CONF_MEMBER_STATE_RING)
                    {
                        Log.info(TAG, "member_remove: true" + " userNum:" + userNum);
                        confService.confUserDelete(sessionId, userNum);
                    }
                }
            });

            // 只有成员通话中时注册音视频控制事件
            if (item.getConfMemModel().getStatus() == CommonConstantEntry.CONF_MEMBER_STATE_CONNECT)
            {
                viewHolder.audioStatus.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (item.getConfMemModel().isAudioEnabled())
                        {
                            Log.info(TAG, "member_audio_mute: false" + " userNum:" + userNum);
                            confService.confUserAudioEnable(sessionId, userNum, false);
//                            item.getConfMemModel().setAudioEnabled(false);
                        }
                        else
                        {
                            Log.info(TAG, "member_audio_mute: true" + " userNum:" + userNum);
                            confService.confUserAudioEnable(sessionId, userNum, true);
//                            item.getConfMemModel().setAudioEnabled(true);
                        }
//                        updateSingleRow(item);
                    }
                });

                viewHolder.videoStatus.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (item.getConfMemModel().isVideoEnabled())
                        {
                            Log.info(TAG, "member_video_mute: false" + " userNum:" + userNum);
                            confService.confUserVideoEnable(sessionId, userNum, false);
//                            item.getConfMemModel().setVideoEnabled(false);
                        }
                        else
                        {
                            Log.info(TAG, "member_video_mute: true" + " userNum:" + userNum);
                            confService.confUserVideoEnable(sessionId, userNum, true);
//                            item.getConfMemModel().setVideoEnabled(true);
                        }
//                        updateSingleRow(item);
                    }
                });

                viewHolder.shareStatus.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (item.getConfMemModel().isVideoShared())
                        {
                            Log.info(TAG, "member_video_share: false" + " userNum:" + userNum);
                            confService.confUserVideoShare(sessionId, userNum, false);
//                            item.getConfMemModel().setVideoShared(false);
                        }
                        else
                        {
                            Log.info(TAG, "member_video_share: true" + " userNum:" + userNum);
                            confService.confUserVideoShare(sessionId, userNum, true);
//                            item.getConfMemModel().setVideoShared(true);
                        }
//                        updateSingleRow(item);
                    }
                });
            }
        }
        catch (Exception e)
        {
            Log.exception(TAG, e);
        }

        return convertView;
    }

    public void setListItem(List<? extends ConfMemberItem> listItems)
    {
        if (listItems != null)
        {
            mListItems = (List<ConfMemberItem>) listItems;
            notifyDataSetChanged();
        }
        else
        {
            Log.info(TAG, "setListItem listItems == null!");
        }
    }

    public void setListItem(List<? extends ConfMemberItem> listItems, int selectedPosition)
    {
        try
        {
            if (listItems != null)
            {
                mListItems = (List<ConfMemberItem>) listItems;
                notifyDataSetChanged();
            }
            else
            {
                Log.info(TAG, "setListItem listItems == null!");
            }
        }
        catch (Exception e)
        {
            Log.error(TAG, e.toString());
        }
    }

    class ViewHolder
    {
        LinearLayout confMemberLayout;
        
        ImageView iconView;
        TextView nameView;
        TextView numberView;

        TextView memberStatus;

        ImageView audioStatus;
        ImageView videoStatus;
        ImageView shareStatus;
        ImageView confOpr;

        int index;
    }

    /**
     * 方法说明 : 更新列表单行记录状态
     * @param confMemberItem
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    private void updateSingleRow(ConfMemberItem confMemberItem)
    {
        if (mListView != null)
        {
            int start = mListView.getFirstVisiblePosition();
            for (int i = start, j = mListView.getLastVisiblePosition(); i <= j; i++)
            {
                if (confMemberItem == mListView.getItemAtPosition(i))
                {
                    View view = mListView.getChildAt(i - start);
                    getView(i, view, mListView);
                    break;
                }
            }
        }
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case ConfControlFragment.UPDATE_LIST_ITEM:
                    // 更新列表单条记录状态
                    updateSingleRow((ConfMemberItem) msg.obj);
                    break;
                case ConfControlFragment.UPDATE_LIST_TIMER:
                    notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };
}
