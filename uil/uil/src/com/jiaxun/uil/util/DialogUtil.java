package com.jiaxun.uil.util;

import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog;
import com.jiaxun.uil.ui.widget.dialog.CustomAlertDialog.OnCustomClickListener;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.Window;

/**
 * ˵�����Ի��򹤾���
 *
 * @author  hubin
 *
 * @Date 2015-6-10
 */
public class DialogUtil
{
    /**
     * ����˵�� : �Զ���Ի���
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
     * ����˵�� : ��ʾ����ʾ�Ի���
     * @param context
     * @param content ��ʾ����
     * @param autoDismiss �Ƿ��Զ��ر�
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showAlertDialog(Context context, String content, boolean autoDismiss)
    {
        CustomAlertDialog dialog = new CustomAlertDialog(context).setTitleText("��ʾ��").setContentText(content);
        dialog.show();
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * ����˵�� : �ɹ���ʾ�Ի���
     * @param context
     * @param content ��ʾ����
     * @param autoDismiss �Ƿ��Զ��ر�
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showSuccessDialog(Context context, String content, boolean autoDismiss)
    {
        CustomAlertDialog dialog = new CustomAlertDialog(context, CustomAlertDialog.SUCCESS_TYPE).setTitleText("�ɹ���").setContentText(content);
        dialog.show();
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * ����˵�� : ʧ����ʾ�Ի���
     * @param context
     * @param content ��ʾ����
     * @param autoDismiss �Ƿ��Զ��ر�
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showFailDialog(Context context, String content, boolean autoDismiss)
    {
        CustomAlertDialog dialog = new CustomAlertDialog(context, CustomAlertDialog.ERROR_TYPE).setTitleText("ʧ��").setContentText(content);
        dialog.show();
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * ����˵�� : ȷ����ʾ�Ի���ֻ��ȷ�ϰ�ť��û��ȡ����ť
     * @param context
     * @param content ��ʾ����
     * @param confirmListener ȷ�ϰ�ť�¼�
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showConfirmDialog(Context context, String content, OnCustomClickListener confirmListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE).setTitleText("��ȷ��").setContentText(content).setConfirmClickListener(confirmListener)
                .show();
    }

    /**
     * ����˵�� : ȷ����ʾ�Ի��򣬿�ѡ���Ƿ���ȡ����ť
     * @param context
     * @param content ��ʾ��Ϣ
     * @param hasCancelBtn �Ƿ���ȡ����ť
     * @param confirmListener ȷ�ϰ�ť�¼�
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showConfirmDialog(Context context, String content, boolean hasCancelBtn, OnCustomClickListener confirmListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE).setTitleText("��ȷ��").setContentText(content).setConfirmClickListener(confirmListener)
                .showCancelButton(hasCancelBtn).show();
    }

    /**
     * ����˵�� : ȷ����ʾ�Ի��򣬱���ȷ�Ϻ�ȡ����ť
     * @param context
     * @param content ��ʾ��Ϣ
     * @param confirmListener ȷ�ϰ�ť�¼�
     * @param cancelListener ȡ����ť�¼�
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void showConfirmDialog(Context context, String content, OnCustomClickListener confirmListener, OnCustomClickListener cancelListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.WARNING_TYPE).setTitleText("��ȷ��").setContentText(content).setConfirmClickListener(confirmListener)
                .showCancelButton(true).setCancelClickListener(cancelListener).show();
    }

    /**
     * ����˵�� :ѡ���dialog���Զ���ʧ
     * @param context
     * @param content ��ʾ��Ϣ
     * @param confirmText ���ѡ����Ϣ
     * @param cancelText �ұ�ѡ����Ϣ
     * @param confirmListener ��߰�ť�¼�
     * @param cancelListener �ұ߰�ť�¼�
     * @author chaimb
     * @Date 2015-9-9
     */
    public static void showSelectDialog(Context context, String content, String confirmText, String cancelText, OnCustomClickListener confirmListener,
            OnCustomClickListener cancelListener)
    {
        new CustomAlertDialog(context, CustomAlertDialog.SELECT_TYPE).setTitleText("��ѡ��").setContentText(content).setConfirmText(confirmText)
                .setCancelText(cancelText).setConfirmClickListener(confirmListener).showCancelButton(true).setCancelClickListener(cancelListener).show();
    }

    /**
     * ����˵�� : ͨ��ȷ�϶Ի���ִ�гɹ�����ʾ�ɹ���ʾ��
     * @param dialog ȷ����ʾ��
     * @param content ��ʾ����
     * @param autoDismiss �Ƿ��Զ���ʧ
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void onSuccessDialog(CustomAlertDialog dialog, String content, boolean autoDismiss)
    {
        dialog.setTitleText("�ɹ���").setContentText(content).showCancelButton(false).setCancelClickListener(null).setConfirmClickListener(null)
                .changeAlertType(CustomAlertDialog.SUCCESS_TYPE);
        if (autoDismiss)
        {
            autoDismissDialog(dialog);
        }
    }

    /**
     * ����˵�� : ͨ��ȷ�϶Ի���ִ��ʧ�ܺ���ʾʧ����ʾ��
     * @param dialog ȷ����ʾ��
     * @param content ��ʾ����
     * @param autoDismiss �Ƿ��Զ���ʧ
     * @return void
     * @author hubin
     * @Date 2015-6-11
     */
    public static void onFailDialog(CustomAlertDialog dialog, String content, boolean autoDismiss)
    {
        dialog.setTitleText("ʧ�ܣ�").setContentText(content).showCancelButton(false).setCancelClickListener(null).setConfirmClickListener(null)
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
