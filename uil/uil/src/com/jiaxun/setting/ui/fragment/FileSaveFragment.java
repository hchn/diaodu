package com.jiaxun.setting.ui.fragment;

import java.io.File;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.CallRecord;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.setting.ui.SettingActivity;
import com.jiaxun.setting.ui.adapter.FileAdapter;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.model.CallRecordListItem;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.ProgressBarUtil;
import com.jiaxun.uil.util.ToastUtil;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：文件导出
 *
 * @author  HeZhen
 *
 * @Date 2015-6-17
 */
public class FileSaveFragment extends BaseFragment implements OnClickListener, NotificationCenterDelegate
{
    private static final String TAG = FileSaveFragment.class.getName();
    private EditText fileNameEdit;
    private TextView filePathTv;
    // 文件目录
    private FileAdapter fileAdapter;
    private ArrayList<File> fileData;
    private TextView titleView;
    private ListView listView;
    private File parentFile;
    // 0 通讯录 1 按键 2 通话记录
    int fileType = -1;
    String filePath;
    private ArrayList<CallRecord> callRecords = new ArrayList<CallRecord>();
    private ArrayList<CallRecordListItem> callRecordListItems;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        fileData = new ArrayList<File>();
        super.onCreate(savedInstanceState);
        File cardPath = Environment.getExternalStorageDirectory();
        setListToPath(cardPath);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.CALL_RECORD_EXPORT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.info(TAG, "onCreateView::");
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            fileType = bundle.getInt("fileType", -1);
//            callRecordListItems = (ArrayList<CallRecordListItem>) bundle.getSerializable("callRecordListItem");

            if (fileType == 2)
            {
                callRecordListItems = (ArrayList<CallRecordListItem>) UiUtils.object;
                for (CallRecordListItem callRecordListItem : callRecordListItems)
                {
                    callRecords.add(callRecordListItem.getCallRecord());
                }
                UiUtils.object = null;
            }

        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        release();
    }

    @Override
    public void release()
    {
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.CALL_RECORD_EXPORT);
    }

    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_file_save;
    }

    @Override
    public void initComponentViews(View view)
    {
        fileNameEdit = (EditText) view.findViewById(R.id.edit_file_name);
        filePathTv = (TextView) view.findViewById(R.id.tv_file_path);

        titleView = (TextView) view.findViewById(R.id.tv_title);
        listView = (ListView) view.findViewById(R.id.listv_file);
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
        view.findViewById(R.id.btn_create).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    private void setListToPath(File path)
    {
        if (path == null)
        {
            return;
        }
        fileData.clear();
        filePath = path.getAbsolutePath();
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
            fileData.add(f);
        }
        fileAdapter.notifyDataSetChanged();
        if (filePathTv != null)
        {
            String text = getResources().getString(R.string.file_path) + ":" + filePath;
            filePathTv.setText(text);
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_create:
                saveFile();
                break;
            case R.id.btn_cancel:
//                parentActivity.clearFragmentStack(R.id.container_setting_right);
                if (fileType == 2)
                {
                    parentActivity.backToPreFragment(R.id.container_setting_right);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("callRecordListItem", callRecordListItems);
//                    ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, CallRecordScreenFrament.class, bundle);
                }
                else
                {
                    ((SettingActivity) parentActivity).showSettingContactView();
                }
                break;
        }
    }

    private void saveFile()
    {
        String fileName = fileNameEdit.getText().toString();
        if (TextUtils.isEmpty(fileName))
        {
            ToastUtil.showToast(parentActivity, null, R.string.notice_name_not_null);
            return;
        }
        try
        {
            if (fileType == 0)
            {
                SettingActivity.isDntTrouble = UiApplication.getConfigService().isDndEnabled();
                if(!SettingActivity.isDntTrouble)//设置为免打扰，导出成功后还原
                {
                    UiApplication.getConfigService().setDndEnabled(true);
                }
                ProgressBarUtil.showProgressDialog(parentActivity, null, "导出联系人...");
                UiApplication.getContactService().exportContacts(filePath, fileName);
            }
            else if (fileType == 1)
            {
                SettingActivity.isDntTrouble = UiApplication.getConfigService().isDndEnabled();
                if(!SettingActivity.isDntTrouble)//设置为免打扰，导出成功后还原
                {
                    UiApplication.getConfigService().setDndEnabled(true);
                }
                ProgressBarUtil.showProgressDialog(parentActivity, null, "导出按键...");
                UiApplication.getContactService().exportKeys(filePath, fileName);
            }
            else if (fileType == 2)
            {
                ProgressBarUtil.showProgressDialog(parentActivity, "", "导出通话记录...");
                UiApplication.getCallRecordService().exportCallRecord(callRecords, filePath, fileName);
            }

        }
        catch (Throwable e)
        {
            ProgressBarUtil.dismissProgressDialog();
            e.printStackTrace();
        }
    }

    @Override
    public void didReceivedNotification(final int id, final Object... args)
    {
        parentActivity.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                if (id == UiEventEntry.CALL_RECORD_EXPORT)
                {
                    int code = (Integer) args[0];
                    if (code == 1)
                    {
                        ToastUtil.showToast("导出完成");
                        parentActivity.backToPreFragment(R.id.container_setting_right);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("callRecordListItem", callRecordListItems);
//                        ((SettingActivity) parentActivity).turnToFragmentStack(R.id.container_setting_right, CallRecordScreenFrament.class, bundle);
                    }
                    else if (code == 0)
                    {
                        ToastUtil.showToast("导出失败");
                    }
                    ProgressBarUtil.dismissProgressDialog();
                }
            }
        });
    }
}
