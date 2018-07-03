package com.jiaxun.uil.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.DateUtils;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.file.FileManager;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.ToastUtil;

/**
 * 说明：通话记录详情适配器
 *
 * @author  chaimb
 *
 * @Date 2015-6-6
 */
public class CallRecordDetailAdapter extends BaseAdapter implements OnClickListener
{
    private final static String TAG = CallRecordDetailAdapter.class.getName();
    private List<CallRecord> confCallRecords;

    private Context context;

    private FileManager fileManager;

    private LayoutInflater inflater;

    private ListView mListView;

    private int callType;

    private Map<String, Integer> progressBarMap = new HashMap<String, Integer>();

    public CallRecordDetailAdapter()
    {
        super();
    }

    public CallRecordDetailAdapter(Context context, List<CallRecord> confCallRecords, ListView mListView, int callType)
    {
        super();
        fileManager = FileManager.getInstance();
        this.context = context;
        this.confCallRecords = confCallRecords;
        inflater = LayoutInflater.from(context);
        this.mListView = mListView;
        this.callType = callType;
    }

    @Override
    public int getCount()
    {
        if (confCallRecords != null && confCallRecords.size() > 0)
        {
            return confCallRecords.size();
        }
        else
        {

            return 0;
        }
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_conf_member_call_record_item, null);
            holder.confNameAndNumTextView = (TextView) convertView.findViewById(R.id.conf_nameandnum_textview);
            holder.confTimeTextView = (TextView) convertView.findViewById(R.id.conf_time_textview);
            holder.userImageView = (ImageView) convertView.findViewById(R.id.user_icon);

            holder.playRecordRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.play_record_relativelayout);

            holder.downloadButton = (Button) convertView.findViewById(R.id.download_button);
            holder.playRecordFileButton = (Button) convertView.findViewById(R.id.play_record_button);
            holder.playLocalRecordFileButton = (Button) convertView.findViewById(R.id.play_local_listen_button);

            holder.myProgressBar = (ProgressBar) convertView.findViewById(R.id.my_progressbar);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        CallRecord callRecord = confCallRecords.get(position);
        String startTime = DateUtils.formatStartTime(callRecord.getStartTime());
        String durationTime = "";
        long connectTime = callRecord.getConnectTime();
        long release = callRecord.getReleaseTime();
        if (callRecord.getConnectTime() == 0)
        {
            durationTime = "0秒";
        }
        else
        {
            durationTime = DateUtils.formatDurationTime((release - connectTime) / 1000);

        }

        if (callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_AUDIO || callType == CommonConstantEntry.CALL_TYPE_CONFERENCE_VIDEO)
        {// 会议
            if (TextUtils.isEmpty(callRecord.getPeerName()))
            {
                holder.confNameAndNumTextView.setText(callRecord.getPeerNum() + "\t\t" + callRecord.getPeerNum());
            }
            else
            {
                holder.confNameAndNumTextView.setText(callRecord.getPeerName() + "\t\t" + callRecord.getPeerNum());
            }

            holder.confTimeTextView.setText("入会：" + startTime + "\t\t" + "时长：" + durationTime);

        }
        else
        {// 单呼和监控

            String callType = "";
            if (callRecord.isOutGoing())
            {

                callType = "已拨";
            }
            else if (connectTime > 0)
            {

                callType = "已接";
            }
            else
            {
                callType = "未接";

            }

            holder.confNameAndNumTextView.setText(startTime + "\t" + callType);
            holder.confTimeTextView.setText("时长：" + durationTime);
        }

        if (callType == CommonConstantEntry.CALL_TYPE_VIDEO_SURVEILLANCE)
        {// 监控用户
            holder.userImageView.setBackgroundResource(R.drawable.vs_icon);
        }
        else
        {
            holder.userImageView.setBackgroundResource(R.drawable.user_icon);
        }

        // 通话时间为0秒，隐藏下载播放按钮
        String recordTaskId = callRecord.getRecordTaskId();

        if (connectTime == 0)
        {
            holder.playRecordRelativeLayout.setVisibility(View.GONE);
        }
        else
        {
            holder.playRecordRelativeLayout.setVisibility(View.VISIBLE);
            String localFileName = DateUtils.getFormatTime(connectTime);
            if (fileManager.hasLocalFile(CommonConfigEntry.TEST_RECORDEINGS_PATH, localFileName, ".wav"))
            {// 判断有没有本地录音
                holder.playLocalRecordFileButton.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.playLocalRecordFileButton.setVisibility(View.GONE);
            }

            if (TextUtils.isEmpty(recordTaskId))
            {
                holder.downloadButton.setVisibility(View.GONE);
                holder.playRecordFileButton.setVisibility(View.GONE);
            }
            else
            {
                String recordFileName = callRecord.getPeerNum() + recordTaskId;
                holder.playRecordFileButton.setVisibility(View.VISIBLE);

                if (fileManager.hasLocalFile(CommonConfigEntry.TEST_RECORD_FILE_PATH, recordFileName, ".mp4"))
                {// 判断本地有没有录音录像
                    holder.downloadButton.setVisibility(View.GONE);
                }
                else
                {
                    holder.downloadButton.setVisibility(View.VISIBLE);
                }
            }
        }

        // 获取下载录音录像文件id
        Bundle bundle = new Bundle();
        bundle.putString("recordTaskId", recordTaskId);
        bundle.putString("recordServer", callRecord.getRecordServer());
        bundle.putString("peerNum", callRecord.getPeerNum());
        bundle.putInt("position", position);
        holder.downloadButton.setTag(bundle);
        holder.playRecordFileButton.setTag(bundle);

        holder.playLocalRecordFileButton.setTag(connectTime);

        holder.downloadButton.setOnClickListener(this);
        holder.playRecordFileButton.setOnClickListener(this);
        holder.playLocalRecordFileButton.setOnClickListener(this);


        return convertView;
    }

    class ViewHolder
    {
        ImageView userImageView;
        TextView confNameAndNumTextView;
        TextView confTimeTextView;

        RelativeLayout playRecordRelativeLayout;
        LinearLayout onlineLinearLayout;
        /** 下载 */
        Button downloadButton;
        /** 录音录像*/
        Button playRecordFileButton;

        Button localPlayRecordFileButton;
        /**本地调听*/
        Button playLocalRecordFileButton;

        ProgressBar myProgressBar;

    }

    @Override
    public void onClick(final View v)
    {
        final long delay = 1000L;
        switch (v.getId())
        {
            case R.id.download_button:

                Log.info(TAG, "download_button::");
                ((Button) v).setClickable(false);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((Button) v).setClickable(true);
                    }
                }, delay);

                Bundle bundle = (Bundle) v.getTag();

                String recordId = bundle.getString("recordTaskId");
                String recordServer = bundle.getString("recordServer");
                int position = bundle.getInt("position");
                String peerNum = bundle.getString("peerNum");

                Button downButton = (Button) v.findViewById(R.id.download_button);
                if (downButton.getText().toString().trim().equals("取消下载"))
                {
                    downButton.setText("下载");
                    // 取消下载
                    UiApplication.getCallRecordService().stopDownload(recordId);
                    hidepro(position, recordId);
                }
                else
                {
                    if (UiApplication.getCallRecordService().downloadRecordFile(context, peerNum, recordId, recordServer))
                    {
                        upDateView(position);
                        progressBarMap.put(recordId, position);
                    }
                    else
                    {
                        Log.info(TAG, "callrecord file is non-existent");
                    }
                }

                break;
            case R.id.play_record_button:
                Log.info(TAG, "play_record_button::");
                ((Button) v).setClickable(false);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((Button) v).setClickable(true);
                    }
                }, delay);
                Bundle itemBundle = (Bundle) v.getTag();
                String recordFileId = itemBundle.getString("recordTaskId");
                String recordFileServer = itemBundle.getString("recordServer");
                String peeritemNum = itemBundle.getString("peerNum");
                UiApplication.getCallRecordService().playRemoteRecordFile(context, peeritemNum, recordFileId, recordFileServer);
                break;

            case R.id.play_local_listen_button:
                Log.info(TAG, "play_local_listen_button::");
                ((Button) v).setClickable(false);
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ((Button) v).setClickable(true);
                    }
                }, delay);
                long connectTime = (Long) v.getTag();
                String localFileName = DateUtils.getFormatTime(connectTime);
                UiApplication.getCallRecordService().playLocalRecordFile(context, localFileName);

                break;
            default:
                break;
        }
    }

    private void hidepro(int currentPosition, String recordId)
    {
        int visiblePosition = mListView.getFirstVisiblePosition();
        // 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (currentPosition - visiblePosition >= 0)
        {
            // 得到要更新的item的view
            View view = mListView.getChildAt(currentPosition - visiblePosition);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.myProgressBar = (ProgressBar) view.findViewById(R.id.my_progressbar);
            if (viewHolder.myProgressBar != null)
            {
                viewHolder.myProgressBar.setVisibility(View.GONE);
                // 取消下载后设置进度值为0
                viewHolder.myProgressBar.setProgress(0);
                progressBarMap.remove(recordId);
            }
        }

    }

    private void upDateView(int currentPosition)
    {

        int visiblePosition = mListView.getFirstVisiblePosition();
        // 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
        if (currentPosition - visiblePosition >= 0)
        {
            // 得到要更新的item的view
            View view = mListView.getChildAt(currentPosition - visiblePosition);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.myProgressBar = (ProgressBar) view.findViewById(R.id.my_progressbar);
            viewHolder.downloadButton = (Button) view.findViewById(R.id.download_button);
            if (viewHolder.myProgressBar != null)
            {
                viewHolder.myProgressBar.setVisibility(View.VISIBLE);
                // 取消下载

                viewHolder.downloadButton.setText("取消下载");

            }
        }
    }

    /**
     * 下载视频更新progressbar
     * 方法说明 :
     * @param fileLength
     * @param offset
     * @author chaimb
     * @Date 2015-6-8
     */
    public void updetaProgressBar(int fileLength, int offset, String recordId)
    {
        if (TextUtils.isEmpty(recordId) && progressBarMap == null)
        {
            Log.info(TAG, "recordId is null！");
            return;
        }
        if (progressBarMap.get(recordId) != null)
        {

            int currentPosition = progressBarMap.get(recordId);

            int visiblePosition = mListView.getFirstVisiblePosition();
            // 只有当要更新的view在可见的位置时才更新，不可见时，跳过不更新
            if (currentPosition - visiblePosition >= 0)
            {
                // 得到要更新的item的view
                View view = mListView.getChildAt(currentPosition - visiblePosition);
                ViewHolder viewHolder = (ViewHolder) view.getTag();
                viewHolder.myProgressBar = (ProgressBar) view.findViewById(R.id.my_progressbar);
                viewHolder.downloadButton = (Button) view.findViewById(R.id.download_button);
                if (viewHolder.myProgressBar != null)
                {
                    if (fileLength != 0)
                        viewHolder.myProgressBar.setMax(fileLength);

                    viewHolder.myProgressBar.setProgress(offset);
                    if (offset == fileLength)
                    {
                        ToastUtil.showToast("下载完成");
                        viewHolder.downloadButton.setText("下载");
                        viewHolder.downloadButton.setVisibility(View.GONE);
                        viewHolder.myProgressBar.setVisibility(View.GONE);
                        viewHolder.myProgressBar.setProgress(0);
                    }

                }
            }
        }
    }

    public void updateData(List<CallRecord> confCallRecords)
    {
        this.confCallRecords = confCallRecords;
        this.notifyDataSetChanged();
    }
    
}
