package com.jiaxun.uil.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 说明：高宽固定比例父类布局
 *
 * @author  HeZhen
 *
 * @Date 2015-7-8
 */
public class SquareLayout extends RelativeLayout{

    public SquareLayout(Context context) {
        this(context,null);
    }
    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
        int childWidthSize = getMeasuredWidth();
//        int childHeightSize = getMeasuredHeight();
        double ratio = 1;//107.0/90.0;
        double childHeightSize =  childWidthSize / ratio;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize,  MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int)childHeightSize,  MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
