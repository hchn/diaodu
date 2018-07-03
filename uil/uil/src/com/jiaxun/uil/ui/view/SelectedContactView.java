package com.jiaxun.uil.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;

/**
 * ËµÃ÷£º
 *
 * @author  HeZhen
 *
 * @Date 2015-6-11
 */
public class SelectedContactView extends TextView
{
    public SelectedContactView(Context context)
    {
        this(context,null);
    }

    public SelectedContactView(Context context, AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    public SelectedContactView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setBackgroundResource(R.drawable.btn_selector);
        setTextColor(Color.BLACK);
        setPadding(10, 3, 10, 3);
        setGravity(Gravity.CENTER);
        float fontsize = UiApplication.getInstance().getResources().getDimension(R.dimen.font_size_medium);
        setTextSize(fontsize);
    }
    @Override
    public LayoutParams getLayoutParams()
    {
        LayoutParams param = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        param.setMargins(5, 2, 5, 2);
        return param;
    }
}
