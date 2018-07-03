package com.jiaxun.setting.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.jiaxun.uil.R;
import com.jiaxun.uil.ui.fragment.BaseFragment;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper.NotificationCenterDelegate;

/**
 * 说明：手动输入界面
 *
 * @author  HeZhen
 *
 * @Date 2015-7-2
 */
public class ManualInputFragment extends BaseFragment implements OnClickListener,NotificationCenterDelegate
{
    private Button cancelBtn;
    private Button sureBtn;
    private EditText editInput;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EventNotifyHelper.getInstance().addObserver(this, UiEventEntry.ADD_BLACK_WHITE_DATA);
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
        EventNotifyHelper.getInstance().removeObserver(this, UiEventEntry.ADD_BLACK_WHITE_DATA);
    }
    @Override
    public void onClick(View v)
    {
        if(v == sureBtn)
        {
            sureToNext();
        }
        else if(v == cancelBtn)
        {
            parentActivity.backToPreFragment(R.id.container_setting_right);
        }
    }
    
    @Override
    public int getLayoutView()
    {
        return R.layout.fragment_manual_input;
    }

    @Override
    public void initComponentViews(View view)
    {
        editInput = (EditText)view.findViewById(R.id.edit_input);
        cancelBtn = (Button)view.findViewById(R.id.btn_cancel);
        sureBtn = (Button)view.findViewById(R.id.btn_create);
        cancelBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }
    private void sureToNext()
    {
        String text = editInput.getText().toString();
        if(!TextUtils.isEmpty(text))
        {
          EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.MANUAINPUT_OVER, text);
        }
    }
    @Override
    public void didReceivedNotification(int id, Object... args)
    {
      if(id == UiEventEntry.ADD_BLACK_WHITE_DATA)
      {
          parentActivity.backToPreFragment(R.id.container_setting_right);
      }
    }
}
