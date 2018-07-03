package com.jiaxun.uil.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * 说明：Toast工具类
 *
 * @author  hubin
 *
 * @Date 2015-6-11
 */
public class ToastUtil
{
    /**
     * 方法说明 : 位于屏幕右上角位置显示Toast提示
     * @param activity
     * @param title Toast标题
     * @param content Toast内容
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showToast(String title, final String content)
    {
        if (UiApplication.getCurrentContext() != null)
        {
            ((Activity) UiApplication.getCurrentContext()).runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    LayoutInflater inflater = LayoutInflater.from(UiApplication.getCurrentContext());
                    View layout = inflater.inflate(R.layout.toast_custom_layout,
                            (ViewGroup) ((Activity) UiApplication.getCurrentContext()).findViewById(R.id.toast_content_view));
//        ((TextView) layout.findViewById(R.id.toast_title)).setText(title);
                    ((TextView) layout.findViewById(R.id.toast_content)).setText(content);
                    Toast toast = Toast.makeText(UiApplication.getCurrentContext(), "", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.RIGHT | Gravity.TOP, UiUtils.homeRightPaneW, UiUtils.titleBarH);
                    toast.setView(layout);
                    toast.show();
                }
            });
        }
    }
    public static void showToast(Context context, String title, int textId)
    {
        String content = context.getResources().getString(textId);
        showToast(title, content);
    }

    public static void showToast(String content)
    {
        showToast("提示", content);
    }
    public static void showUiToast(final String content)
    {
        new Handler(UiApplication.getInstance().getMainLooper()).post(new Runnable()
        {
            @Override
            public void run()
            {
                showToast("提示", content);
            }
        });
      
    }
}
