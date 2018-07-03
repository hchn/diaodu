package com.jiaxun.setting.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ConfMemberListView extends ListView
{
    

    public ConfMemberListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public ConfMemberListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ConfMemberListView(Context context)
    {
        super(context);
    }
    
    
    // 重新获取listview 的高度
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
