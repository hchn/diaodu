package com.jiaxun.uil.ui.view.presentation;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.CalendarContract.Colors;
import android.view.Display;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

/**
 * 说明：默认显示的扩展屏界面
 *
 * @author  hubin
 *
 * @Date 2015-9-14
 */
public class DefaultPresentation extends Presentation
{
    // 扩展屏上显示界面布局
    private FrameLayout defaultView;

    public DefaultPresentation(Context context, Display display)
    {
        super(context, display);
        defaultView = new FrameLayout(context);
        TextView defaultTV = new TextView(context);
        defaultTV.setText("操作台进行中！！！");
        defaultTV.setTextColor(Color.WHITE);
        defaultTV.setTextSize(100);
        defaultTV.setGravity(Gravity.CENTER);
        defaultView.addView(defaultTV);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Point p = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(p);
        LayoutParams lp = new LayoutParams(p.x, p.y);
        defaultView.setLayoutParams(lp);
        setContentView(defaultView);
    }
}
