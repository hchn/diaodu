package com.jiaxun.uil.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiaxun.sdk.dcl.model.GroupModel;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * 说明：通讯录  标题组层级 快捷索引
 *
 * @author  HeZhen
 *
 * @Date 2015-4-29
 */
public class GroupLevelBtn extends TextView
{
    private int groupId = GroupModel.DEFAULT_PARENT_ID;
    private OnGroupBtnClickListener onGroupBtnClickListener;
    boolean isFirst;

    public GroupLevelBtn(Context context, boolean isFirst)
    {
        this(context, null);
        this.isFirst = isFirst;
//        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
        setPadding(20, 5, 20, 5);
        setBackgroundResource(isFirst ? R.drawable.contact_arrow_1_selector : R.drawable.contact_arrow_selector);
        setTextAppearance(context, R.style.fontStyleWhiteMedium);
    }

    public GroupLevelBtn(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setSingleLine();
        setEllipsize(TruncateAt.MARQUEE);
        setHorizontallyScrolling(true);
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public LayoutParams getLayoutParams()
    {
        LayoutParams param = new LayoutParams(145, LayoutParams.WRAP_CONTENT);
        if (!isFirst)
        {
            param.setMargins(-10, 0, 0, 0);
        }
        return param;
    }

//    @Override
//    public boolean isFocused()
//    {
//        return true;
//    }
    public GroupLevelBtn initData(int groupId)
    {
        this.groupId = groupId;
        GroupModel groupModel = UiApplication.getContactService().getDepById(groupId);

        if (groupModel != null)
        {
            setText(groupModel.getName());
        }
        return this;
    }

    public void setOnClickListener(OnGroupBtnClickListener l)
    {
        this.onGroupBtnClickListener = l;
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onGroupBtnClickListener.onClick(v, groupId);
            }
        });
    }

    public interface OnGroupBtnClickListener
    {
        void onClick(View v, int groupId);
    }
}
