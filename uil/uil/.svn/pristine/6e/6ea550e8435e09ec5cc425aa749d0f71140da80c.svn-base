package com.jiaxun.uil.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：可拖动的GridView
 *
 * @author  hubin
 *
 * @Date 2015-9-22
 */
public class DraggableGridView extends GridView
{
    private static final String TAG = DraggableGridView.class.getName();
    private int type;
    private boolean isDrag = false;
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    private WindowManager mWindowManager;
    private Handler mHandler = new Handler();
    final int[] location = new int[2];
    /** 
    * 正在拖拽的position 
      */
    private int mDragPosition;
    /**
     * 刚开始拖拽的item对应的View
     */
    private View mStartDragItemView = null;
    /**
     * 我们拖拽的item对应的Bitmap
     */
    private Bitmap mDragBitmap;
    private WindowManager.LayoutParams mWindowLayoutParams;
    /**
     * 用于拖拽的镜像，这里直接用一个ImageView
     */
    private ImageView mDragImageView;
    public DraggableGridView(Context context)
    {
        this(context, null);
    }

    public DraggableGridView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DraggableGridView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                moveX = (int) ev.getRawX();
                moveY = (int) ev.getRawY();
                mDragPosition = pointToPosition(mDownX, mDownY);
                Log.info(TAG, "mDownX:" + mDownX + " mDownY:" + mDownY + " moveX:" + moveX + " moveY:" + moveY + " mDragPosition:" + mDragPosition);
                if (mDragPosition == AdapterView.INVALID_POSITION)
                {
                    return false;
                }
                // 根据position获取该item所对应的View
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                if (mStartDragItemView == null)
                {
                    break;
                }
                // 开启mDragItemView绘图缓存
                mStartDragItemView.setDrawingCacheEnabled(true);
                // 获取mDragItemView在缓存中的Bitmap对象
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                // 这一步很关键，释放绘图缓存，避免出现重复的镜像
                mStartDragItemView.destroyDrawingCache();

//                ((GridView) v).getLocationOnScreen(location);
                mHandler.postDelayed(mLongClickRunnable, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isTouchInItem(mStartDragItemView, (int) ev.getX(), (int) ev.getY()))
                {
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                // 拖动item
                onDragItem((int) ev.getRawX(), (int) ev.getRawY());
                // 拖动过程中禁止GridView滑动
                if (isDrag)
                {
                    return false;
                }
                else
                {
                    break;
                }
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mLongClickRunnable);
                onStopDrag((int) ev.getRawX(), (int) ev.getRawY());
                isDrag = false;
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

 // 用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            isDrag = true; // 设置可以拖拽
            int x = moveX - (int) mStartDragItemView.getWidth() / 2;
            int y = moveY - (int) mStartDragItemView.getHeight() / 2;
            // 根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, x, y);
        }
    };

    /** 
    * 是否点击在GridView的item上面 
     * @param itemView 
     * @param x 
     * @param y 
    * @return 
    */
    private boolean isTouchInItem(View dragView, int x, int y)
    {
        if (dragView == null)
        {
            return false;
        }
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if (x < leftOffset || x > leftOffset + dragView.getWidth())
        {
            return false;
        }

        if (y < topOffset || y > topOffset + dragView.getHeight())
        {
            return false;
        }

        return true;
    }

    /**
     * 方法说明 :停止拖动
     * @param x
     * @param y
     * @author HeZhen
     * @Date 2015-7-22
     */
    private void onStopDrag(int x, int y)
    {
        Log.info(TAG, "onStopDrag:: x:" + x + " y:" + y);
        if (isDrag)
        {
            EventNotifyHelper.getInstance().postUiNotification(UiEventEntry.NOTIFY_VIDEO_SWITCH, x, y, mDragPosition, type);
//        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
//        if (view != null)
//        {
//            view.setVisibility(View.VISIBLE);
//        }
            removeDragImage();
        }
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新
     * @param x
     * @param y
     */
    private void onDragItem(int x, int y)
    {
        if (isDrag && mDragImageView != null)
        {
//            getLocationOnScreen(location);
            mWindowLayoutParams.x = x - (int) mStartDragItemView.getWidth() / 2;
            mWindowLayoutParams.y = y - (int) mStartDragItemView.getHeight() / 2;
            mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); // 更新镜像的位置
        }
    }

    /** 
     * 从界面上面移动拖动镜像 
     */
    private void removeDragImage()
    {
        if (mDragImageView != null)
        {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 创建拖动的镜像
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY)
    {
        if (bitmap == null)
        {
            return;
        }
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // 图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        mWindowLayoutParams.x = downX;
        mWindowLayoutParams.y = downY;
        mWindowLayoutParams.alpha = 0.55f; // 透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
}
