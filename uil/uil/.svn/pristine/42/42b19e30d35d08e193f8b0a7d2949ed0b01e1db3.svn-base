package com.jiaxun.uil.util;

import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog.OnCustomClickListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;

/**
 * 说明：对话框工具类
 *
 * @author  hubin
 *
 * @Date 2015-6-10
 */
public class DialogUtil
{
    /**
     * 方法说明 : 自定义对话框
     * @param context
     * @param customView
     * @return void
     * @author hubin
     * @Date 2015-6-17
     */
    public static Dialog showCustomDialog(Context context, View customView)
    {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(customView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    /**
     * 方法说明 : 显示简单提示对话框
     * @param context
     * @param content 提示内容
     * @param autoDismiss 是否自动关闭
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showAlertDialog(Context context, String content, boolean autoDismiss)
    {
        CustomAlertDialog dialog = new CustomAlertDialog(context).setTitleText("提示框").setContentText(content);
        dialog.show();
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * 方法说明 : 成功提示对话框
     * @param context
     * @param content 提示内容
     * @param autoDismiss 是否自动关闭
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showSuccessDialog(Context context, String content, boolean autoDismiss)
    {
        CustomAlertDialog dialog = new CustomAlertDialog(context, CustomAlertDialog.SUCCESS_TYPE).setTitleText("成功！").setContentText(content);
        dialog.show();
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * 方法说明 : 失败提示对话框
     * @param context
     * @param content 提示内容
     * @param autoDismiss 是否自动关闭
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showFailDialog(Context context, String content, boolean autoDismiss)
    {
        CustomAlertDialog dialog = new CustomAlertDialog(context, CustomAlertDialog.ERROR_TYPE).setTitleText("失败").setContentText(content);
        dialog.show();
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * 方法说明 : 确认提示对话框，只有确认按钮，没有取消按钮
     * @param context
     * @param content 提示内容
     * @param confirmListener 确认按钮事件
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showConfirmDialog(Context context, String content, OnCustomClickListener confirmListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE).setTitleText("请确认").setContentText(content).setConfirmClickListener(confirmListener)
                .show();
    }

    /**
     * 方法说明 : 确认提示对话框，可选配是否有取消按钮
     * @param context
     * @param content 提示信息
     * @param hasCancelBtn 是否有取消按钮
     * @param confirmListener 确认按钮事件
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showConfirmDialog(Context context, String content, boolean hasCancelBtn, OnCustomClickListener confirmListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE).setTitleText("请确认").setContentText(content).setConfirmClickListener(confirmListener)
                .showCancelButton(hasCancelBtn).show();
    }

    /**
     * 方法说明 : 确认提示对话框，保护确认和取消按钮
     * @param context
     * @param content 提示信息
     * @param confirmListener 确认按钮事件
     * @param cancelListener 取消按钮事件
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showConfirmDialog(Context context, String content, OnCustomClickListener confirmListener, OnCustomClickListener cancelListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE).setTitleText("请确认").setContentText(content).setConfirmClickListener(confirmListener)
                .showCancelButton(true).setCancelClickListener(cancelListener).show();
    }

    /**
     * 方法说明 :选择的dialog，自动消失
     * @param context
     * @param content 提示信息
     * @param confirmText 左边选择信息
     * @param cancelText 右边选择信息
     * @param confirmListener 左边按钮事件
     * @param cancelListener 右边按钮事件
     * @author chaimb
     * @Date 2015-9-9
     */
    public static void showSelectDialog(Context context, String content, String confirmText, String cancelText, OnCustomClickListener confirmListener,
            OnCustomClickListener cancelListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.SELECT_TYPE).setTitleText("请选择").setContentText(content).setConfirmText(confirmText)
                .setCancelText(cancelText).setConfirmClickListener(confirmListener).showCancelButton(true).setCancelClickListener(cancelListener).show();
    }

    /**
     * 方法说明 : 通过确认对话框执行成功后，显示成功提示框
     * @param dialog 确认提示框
     * @param content 提示内容
     * @param autoDismiss 是否自动消失
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void onSuccessDialog(CustomAlertDialog dialog, String content, boolean autoDismiss)
    {
        dialog.setTitleText("成功！").setContentText(content).showCancelButton(false).setCancelClickListener(null).setConfirmClickListener(null)
                .changeAlertType(CustomAlertDialog.SUCCESS_TYPE);
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * 方法说明 : 通过确认对话框执行失败后，显示失败提示框
     * @param dialog 确认提示框
     * @param content 提示内容
     * @param autoDismiss 是否自动消失
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void onFailDialog(CustomAlertDialog dialog, String content, boolean autoDismiss)
    {
        dialog.setTitleText("失败！").setContentText(content).showCancelButton(false).setCancelClickListener(null).setConfirmClickListener(null)
                .changeAlertType(CustomAlertDialog.ERROR_TYPE);
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    private static void autoDismissDialog(final CustomAlertDialog dialog)
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                dialog.dismiss();

            }
        }, 2000);
    }
}
