package com.jiaxun.uil.util;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.UiApplication;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * ËµÃ÷£º
 *
 * @author  HeZhen
 *
 * @Date 2015-6-16
 */
public class ProgressBarUtil
{
    private static final String TAG = ProgressBarUtil.class.getSimpleName();
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context, String title, String message)
    {
        Log.info(TAG, "showProgressDialog");

        if (progressDialog == null)
        {
            progressDialog = ProgressDialog.show(context, title, message, true);
        }

    }
    public static void showProgressDialog(Context context, String title, int textId)
    {
        String message = UiApplication.getInstance().getResources().getString(textId);
        showProgressDialog(context, title, message);
    }
    public static void dismissProgressDialog()
    {
        Log.info(TAG, "dismissProgressDialog");

        if (progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
