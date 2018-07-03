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
 * ˵�������϶���GridView
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
    * ������ק��position 
      */
    private int mDragPosition;
    /**
     * �տ�ʼ��ק��item��Ӧ��View
     */
    private View mStartDragItemView = null;
    /**
     * ������ק��item��Ӧ��Bitmap
     */
    private Bitmap mDragBitmap;
    private WindowManager.LayoutParams mWindowLayoutParams;
    /**
     * ������ק�ľ�������ֱ����һ��ImageView
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
                // ����position��ȡ��item����Ӧ��View
                mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
                if (mStartDragItemView == null)
                {
                    break;
                }
                // ����mDragItemView��ͼ����
                mStartDragItemView.setDrawingCacheEnabled(true);
                // ��ȡmDragItemView�ڻ����е�Bitmap����
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                // ��һ���ܹؼ����ͷŻ�ͼ���棬��������ظ��ľ���
                mStartDragItemView.destroyDrawingCache();

//                ((GridView) v).getLocationOnScreen(location);
                mHandler.postDelayed(mLongClickRunnable, 1000);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isTouchInItem(mStartDragItemView, (int) ev.getX(), (int) ev.getY()))
                {
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                // �϶�item
                onDragItem((int) ev.getRawX(), (int) ev.getRawY());
                // �϶������н�ֹGridView����
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

 // ���������Ƿ�Ϊ������Runnable
    private Runnable mLongClickRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            isDrag = true; // ���ÿ�����ק
            int x = moveX - (int) mStartDragItemView.getWidth() / 2;
            int y = moveY - (int) mStartDragItemView.getHeight() / 2;
            // �������ǰ��µĵ���ʾitem����
            createDragImage(mDragBitmap, x, y);
        }
    };

    /** 
    * �Ƿ�����GridView��item���� 
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
     * ����˵�� :ֹͣ�϶�
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
     * �϶�item��������ʵ����item�����λ�ø���
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
            mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); // ���¾����λ��
        }
    }

    /** 
     * �ӽ��������ƶ��϶����� 
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
     * �����϶��ľ���
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY)
    {
        if (bitmap == null)
        {
            return;
        }
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; // ͼƬ֮��������ط�͸��
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        mWindowLayoutParams.x = downX;
        mWindowLayoutParams.y = downY;
        mWindowLayoutParams.alpha = 0.55f; // ͸����
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
