package com.jiaxun.setting.ui.fragment;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaxun.sdk.util.config.ConfigHelper;
import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.setting.model.PrefsSelectFileItem;
import com.jiaxun.setting.ui.adapter.FileAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：选择文件
 *
 * @author  HeZhen
 *
 * @Date 2015-6-17
 */
public class FileBrowseFragment extends BaseFragment
{
    private FileAdapter fileAdapter;
    private ArrayList<File> fileData;
    private TextView titleView;
    private ListView listView;
    private File parentFile;

    private ConfigHelper configHelper;

    private RelativeLayout createorcancleLayout;

    private Button createButton;
    private Button cancleButton;

    int fileType = -1;
    private PrefsSelectFileItem prefsSelectFileItem;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        fileData = new ArrayList<File>();
        super.onCreate(savedInstanceState);
        File cardPath = Environment.getExternalStorageDirectory();
        setListToPath(cardPath);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        fileType = bundle.getInt("fileType", -1);
        prefsSelectFileItem = bundle.getParcelable(CommonConstantEntry.DATA_OBJECT);
        if (fileType == -1)
        {
            fileType = prefsSelectFileItem.getFileType();
        }

        configHelper = ConfigHelper.getDefaultConfigHelper(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_file_browse;
    }

    @Override
    public void initComponentViews(View view)
    {
        titleView = (TextView) view.findViewById(R.id.tv_title);
        listView = (ListView) view.findViewById(R.id.listv_file);
        createButton = (Button) view.findViewById(R.id.btn_create);
        createButton.setVisibility(View.GONE);
        cancleButton = (Button) view.findViewById(R.id.btn_cancel);
        createorcancleLayout = (RelativeLayout) view.findViewById(R.id.createorcancle_layout);
        createorcancleLayout.setVisibility(View.VISIBLE);

        fileAdapter = new FileAdapter(parentActivity, fileData);
        listView.setAdapter(fileAdapter);
        listView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                File selectedFile = fileData.get(position);
                if (selectedFile.isDirectory())
                {
                    setListToPath(selectedFile);
                }
                else
                {

                    if (fileType == 0 || fileType == 1)
                    {
                        if (isCSVFile(selectedFile))
                        {
                            String filePath = selectedFile.getAbsolutePath();
                            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.FILE_SELECT_OVER, filePath, fileType);
                        }
                    }
                    else if (fileType == 2)
                    {
                        if (isVoiceFile(selectedFile))
                        {
                            String filePath = selectedFile.getAbsolutePath();
                            int index = filePath.lastIndexOf("/");
                            String tempFilePath = filePath.substring(index + 1, filePath.length());
                            configHelper.putString(prefsSelectFileItem.getKey(), filePath);
                            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.FILE_SELECT_OVER, tempFilePath, fileType);
                        }
                        else
                        {
                            ToastUtil.showToast("请选择标准的音频文件！");
                        }
                    }
                    else if (fileType == 3)
                    {
                        if (isImageFile(selectedFile))
                        {
                            String filePath = selectedFile.getAbsolutePath();
                            int index = filePath.lastIndexOf("/");
                            String tempFilePath = filePath.substring(index + 1, filePath.length());
                            configHelper.putString(prefsSelectFileItem.getKey(), filePath);
                            EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.FILE_SELECT_OVER, tempFilePath, fileType);
                        }
                        else
                        {
                            ToastUtil.showToast("请选择正确的图片！");
                        }
                    }

                }

            }
        });
        titleView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setListToPath(parentFile);
            }
        });

        cancleButton.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                parentActivity.backToPreFragment(R.id.container_setting_right);
//                parentActivity.removeFragmentFromBackStack(R.id.container_setting_right, FileBrowseFragment.class);
            }
        });
    }

    private void setListToPath(File path)
    {
        fileData.clear();
        if (!path.equals(Environment.getExternalStorageDirectory()))
        {
            parentFile = path.getParentFile();
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(Html.fromHtml("Up to <font color=\"red\">" + path.getName() + "</font>"));
        }
        else
        {
            titleView.setVisibility(View.GONE);
            parentFile = null;
        }

        for (File f : path.listFiles())
        {
//            if(f.isDirectory() || isCSVFile(f)) {
            fileData.add(f);
//            }
        }
        fileAdapter.notifyDataSetChanged();
    }

    public boolean isCSVFile(File f)
    {
        return f.isFile() && f.getName().endsWith(".csv");
    }

    private boolean isVoiceFile(File f)
    {
        return f.isFile() && (f.getName().endsWith(".wav") || f.getName().endsWith(".mp3") || f.getName().endsWith(".MP3"));
    }

    private boolean isImageFile(File f)
    {
        return f.isFile() && (f.getName().endsWith(".png") || f.getName().endsWith(".PNG") || f.getName().endsWith(".JPG") || f.getName().endsWith(".JPG"));
    }
}
