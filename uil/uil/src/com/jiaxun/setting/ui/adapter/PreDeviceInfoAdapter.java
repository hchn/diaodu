package com.jiaxun.setting.ui.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaxun.sdk.util.MemerInfo;
import com.jiaxun.sdk.util.config.CommonConfigEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.sdk.util.version.VersionClient;
import com.jiaxun.setting.model.DeviceInfo;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * ˵����
 *
 * @author  chaimb
 *
 * @Date 2015-6-5
 */
public class PreDeviceInfoAdapter extends BaseAdapter implements OnClickListener
{

    private String TAG = PreDeviceInfoAdapter.class.getName();
    private static ProgressBar mProgBar;
    private static Button mUpdateButton;
    private List<DeviceInfo> deviceList;
    private Context context;

    final int VIEW_TYPE = 3;
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;

    private LayoutInflater inflater;
    private boolean isUpDate;
    private VersionClient versionClient;
    private ListView mListView;
    private String newVersion;
    private boolean isexists;;

    public PreDeviceInfoAdapter()
    {
        super();

    }

    public boolean isUpDate()
    {
        return isUpDate;
    }

    public void setUpDate(boolean isUpDate)
    {
        this.isUpDate = isUpDate;
    }

    public String getNewVersion()
    {
        return newVersion;
    }

    public void setNewVersion(String newVersion)
    {
        this.newVersion = newVersion;
    }

    public PreDeviceInfoAdapter(Context context, List<DeviceInfo> devicesList, VersionClient versionClient, ListView mListView)
    {
        this.context = context;
        this.deviceList = devicesList;
        inflater = LayoutInflater.from(context);
        this.versionClient = versionClient;
        this.mListView = mListView;
    }

    @Override
    public int getCount()
    {
        if (deviceList != null && deviceList.size() > 0)
        {
            return deviceList.size() + 1;

        }
        else
        {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position == 0)
        {
            return TYPE_2;
        }
        else
        {
            return TYPE_1;
        }

    }

    @Override
    public int getViewTypeCount()
    {
        return VIEW_TYPE;
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
        ViewHolderHead holderHead = null;

        int type = getItemViewType(position);
        if (convertView == null)
        {
            switch (type)
            {
                case TYPE_1:
                    holder = new ViewHolder();
                    convertView = inflater.inflate(R.layout.adapter_device_item, null);
                    holder.typeTextView = (TextView) convertView.findViewById(R.id.device_type_textview);
                    holder.nameTextView = (TextView) convertView.findViewById(R.id.device_name_textview);
                    holder.updateButton = (Button) convertView.findViewById(R.id.updapte_button);
                    holder.updateProgressBar = (ProgressBar) convertView.findViewById(R.id.update_progressbar);
                    convertView.setTag(holder);

                    break;
                case TYPE_2:
                    holderHead = new ViewHolderHead();
                    convertView = inflater.inflate(R.layout.adapter_device_head_item, null);
                    holderHead.headImageView = (ImageView) convertView.findViewById(R.id.device_head_imageview);
                    holderHead.headTextView = (TextView) convertView.findViewById(R.id.device_head_textview);

                    convertView.setTag(holderHead);
                    break;

                default:
                    break;

            }
        }
        else

        {
            switch (type)
            {
                case TYPE_1:
                    holder = (ViewHolder) convertView.getTag();

                    break;
                case TYPE_2:

                    holderHead = (ViewHolderHead) convertView.getTag();
                    break;

                default:
                    break;
            }
        }

        switch (type)
        {
            case TYPE_1:

                DeviceInfo devicesInfo = deviceList.get(position - 1);

                holder.typeTextView.setText(devicesInfo.getType());
                holder.nameTextView.setText(devicesInfo.getName());
                if ("���ݴ洢��Ϣ".equals(devicesInfo.getType()))
                {
                    // Ĭ�� ��ȡSD���Ŀռ�
                    if (MemerInfo.isExternalStorageAvailable())
                    {
                        holder.nameTextView.setText("���ÿռ�:" + MemerInfo.getSdUsedSize(context) + ",���ÿռ�:" + MemerInfo.getSDAvailableSize(context));
                    }
                    else
                    {
                        holder.nameTextView.setText("���ÿռ�:" + MemerInfo.getMemoryUsedSize(context) + ",���ÿռ�:" + MemerInfo.getMemoryAvailableSize(context));
                    }
                }
                if ("�����Ϣ".equals(devicesInfo.getType()))
                {
                    holder.nameTextView.setText(devicesInfo.getName());

                    if (isUpDate)
                    {
                        File file = new File(CommonConfigEntry.DOWNLOAD_PATH + newVersion + ".apk");
                        if (file.exists())
                        {
                            holder.updateButton.setText("��װ");
                            isexists = true;
                        }
                        else
                        {
                            isexists = false;
                        }
                        holder.updateButton.setVisibility(View.VISIBLE);
                        holder.updateButton.setOnClickListener(this);
                        holder.updateButton.setTag(position);
                    }
                }
                if ("����ϵͳ��Ϣ".equals(devicesInfo.getType()))
                {
                    holder.nameTextView.setText(android.os.Build.VERSION.RELEASE);
                }
                if ("��Ʒ���".equals(devicesInfo.getType()))
                {
                    holder.nameTextView.setText(android.os.Build.DISPLAY);
                }
                break;
            case TYPE_2:

                break;

            default:
                break;
        }
//        Log.info(TAG, "position" + position);
        return convertView;
    }

    class ViewHolder
    {
        TextView typeTextView;
        TextView nameTextView;

        Button updateButton;
        ProgressBar updateProgressBar;
    }

    class ViewHolderHead
    {

        ImageView headImageView;
        TextView headTextView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.updapte_button:
                if (isexists)
                {
                    versionClient.installerApk(CommonConfigEntry.DOWNLOAD_PATH + newVersion + ".apk", context);
                }
                else
                {

                    // m6100Ϊ���� ��Ϊt30
                    String server = UiApplication.getConfigService().getMasterServerIp();// ��ʱȡ����������ַ
                    versionClient.downloadApk(server, 50021, "t30");
                    int position = (Integer) v.getTag();
                    updateItem(position);
                }
                break;

            default:
                break;
        }
    }

    private void updateItem(int position)
    {
        int visiblePosition = mListView.getFirstVisiblePosition();
        // ֻ�е�Ҫ���µ�view�ڿɼ���λ��ʱ�Ÿ��£����ɼ�ʱ������������
        if (position - visiblePosition >= 0)
        {
            // �õ�Ҫ���µ�item��view
            View view = mListView.getChildAt(position - visiblePosition);
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            viewHolder.updateProgressBar = (ProgressBar) view.findViewById(R.id.update_progressbar);
            viewHolder.updateButton = (Button) view.findViewById(R.id.updapte_button);
            if (viewHolder.updateProgressBar != null)
            {
                PreDeviceInfoAdapter.mProgBar = viewHolder.updateProgressBar;
                PreDeviceInfoAdapter.mUpdateButton = viewHolder.updateButton;
                viewHolder.updateProgressBar.setVisibility(View.VISIBLE);
                // ȡ������

//                viewHolder.downloadButton.setText("ȡ������");

            }
        }
    }

    /**
     * �����°汾progressbar
     * ����˵�� :
     * @param fileLength
     * @param offset
     * @author chaimb
     * @Date 2015-6-11
     */
    public void updetaProgressBar(int fileLength, int offset)
    {
        Log.info(TAG, "updetaProgressBar");
        if (mProgBar != null)
        {

            if (fileLength != 0)
                mProgBar.setMax(fileLength);

            mProgBar.setProgress(offset);
            if (offset == fileLength)
            {
                Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
                mProgBar.setVisibility(View.GONE);
                mUpdateButton.setVisibility(View.GONE);
                // ��װ�ļ�
                versionClient.installerApk(CommonConfigEntry.DOWNLOAD_PATH + newVersion + ".apk", context);
            }
        }
    }

}
