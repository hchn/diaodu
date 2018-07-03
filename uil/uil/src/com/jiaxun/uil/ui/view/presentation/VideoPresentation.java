package com.jiaxun.uil.ui.view.presentation;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

import com.jiaxun.uil.UiApplication;

/**
 * 说明：扩展屏显示
 *
 * @author  HeZhen
 *
 * @Date 2015-7-13
 */
@SuppressLint("NewApi")
public class VideoPresentation extends Presentation
{
    private static final String TAG = VideoPresentation.class.getName();
    // 扩展屏上显示界面布局
    private VideoFrameView vf;

    public VideoPresentation(Context context, Display display)
    {
        super(context, display);
        vf = new VideoFrameView(context);
    }

    public VideoFrameView getVf()
    {
        return vf;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Point p = new Point();
        getWindow().getWindowManager().getDefaultDisplay().getSize(p);
        getVf().init(p);
        setContentView(getVf());
        vf.setScreenSize(UiApplication.getConfigService().getPresentationWindowCount());
    }

}
