package com.jiaxun.uil.ui.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.ContactModel;
import com.jiaxun.sdk.dcl.model.ContactNum;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * 说明：联系人详情弹出
 *
 * @author  HeZhen
 *
 * @Date 2015-5-18
 */
public class ContactDetailPopupWindow extends PopupWindow
{
    private View detailView;
    private TextView nameTV;
    private TextView phoneTV;
    private String[] phoneType;
    public ContactDetailPopupWindow(Context context)
    {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        detailView = inflater.inflate(R.layout.popup_contact_detail, null);
        nameTV = (TextView) detailView.findViewById(R.id.tv_user_name);
        phoneTV = (TextView) detailView.findViewById(R.id.tv_phonenum);
        phoneType = new String[]{};//context.getResources().getStringArray(R.array.phone_all_categary);
        this.setContentView(detailView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.AnimationPopup);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        this.setOutsideTouchable(true);
    }
    public ContactDetailPopupWindow setDetail(int contactId)
    {
        ContactModel contact = UiApplication.getContactService().getContactById(contactId);
        nameTV.setText(contact.getName());
        
        ArrayList<ContactNum> phones = contact.getPhoneNumList();
         StringBuffer phoneString = new StringBuffer();
        for (ContactNum contactNum : phones)
        {
            phoneString.append(contactNum.getTypeName()).append(":").append(contactNum.getNumber()).append("\n\n");
        }
        phoneTV.setText(phoneString.toString());
        return this;
    }
}
