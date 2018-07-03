package com.jiaxun.uil.ui.view.presentation;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jiaxun.sdk.util.constant.CommonConstantEntry;
import com.jiaxun.sdk.util.log.Log;
import com.jiaxun.uil.R;
import com.jiaxun.uil.UiApplication;
import com.jiaxun.uil.util.UiConfigEntry;
import com.jiaxun.uil.util.UiEventEntry;
import com.jiaxun.uil.util.UiUtils;
import com.jiaxun.uil.util.eventnotify.EventNotifyHelper;

/**
 * 说明：视频多分屏窗口整体布局，用于视频展现和扩展屏控制
 *
 * @author  HeZhen
 *
 * @Date 2015-7-16
 */
public class VideoFrameView extends RelativeLayout
{
    private static final String TAG = VideoFrameView.class.getName();
    private ArrayList<VideoItem> videoItemList = new ArrayList<VideoItem>();
    private int width;
    private int height;
    // 窗口分屏类型
    private int screenType = -1;
    // 当用于扩展屏控制时为true，普通视频分屏展现时为false
    private boolean isMini = false;
    private HashMap<String, Integer> videoPostionMap = new HashMap<String, Integer>();

    // -----------------控制界面 mini---------------------------
    private boolean isDrag = false;
    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    private Handler mHandler = new Handler();
    private WindowManager.LayoutParams mWindowLayoutParams;
    /**
     * 刚开始拖拽的item对应的View
     */
    private VideoItem mStartDragItemView = null;
    /**
     * 我们拖拽的item对应的Bitmap
     */
    private Bitmap mDragBitmap;
    /**
     * 用于拖拽的镜像，这里直接用一个ImageView
     */
    private ImageView mDragImageView;
    private WindowManager mWindowManager;
    final int[] location = new int[2];
    // ----------------------end---------------------------------

    VideoFrameView relationVideoFrame = null;

    // 双击单屏显示标识
    private boolean isFullScreen;
    // 用于双击单屏展示时，缓存其余视频窗口视频，以便后续还原
    private ArrayList<SurfaceView> cashedVideos;

    public interface OnDoubleClickListener
    {
        public void OnSingleClick(View v);

        public void OnDoubleClick(View v);
    }

    public void setRelationVideoFrame(VideoFrameView vf)
    {
        relationVideoFrame = vf;
    }

    public VideoFrameView getRelationVideoFrame()
    {
        return relationVideoFrame;
    }

    public VideoFrameView(Context context)
    {
        this(context, null);
    }

    public void setMini(boolean isMini)
    {
        this.isMini = isMini;
    }

    public VideoFrameView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        cashedVideos = new ArrayList<SurfaceView>();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (width == 0)
        {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        Log.info(TAG, "onMeasure:: width: " + width + " height:" + height);
//        getLocationOnScreen(location);
//        Log.info(TAG, "getLocationOnScreen: location[0]" + location[0] + " location[1]" + location[1]);
    }

    /**
     * 方法说明 :初始化视频区域高宽
     * @param p
     * @author HeZhen
     * @Date 2015-7-22
     */
    public void init(Point p)
    {
        if (width == 0)
        {
            width = p.x;
            height = p.y;
        }
    }

    /**
     * 方法说明 : 设置视频窗口分屏，重置各窗口宽高
     * @param type
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void setScreenSize(int type)
    {
        Log.info(TAG, "setScreenType:: type:" + type);
        this.screenType = type;
        int row = 0;
        int column = 0;
        int screenCount = 0; // 分屏个数
        switch (type)
        {
            case UiEventEntry.SCREEN_TYPE_AUTO:
            case UiEventEntry.SCREEN_TYPE_1:
                row = 1;
                column = 1;
                screenCount = 1;
                break;
            case UiEventEntry.SCREEN_TYPE_4:
                row = 2;
                column = 2;
                screenCount = 4;
                break;
            case UiEventEntry.SCREEN_TYPE_6:
                row = 3;
                column = 3;
                screenCount = 6;
                break;
            case UiEventEntry.SCREEN_TYPE_8:
                row = 4;
                column = 4;
                screenCount = 8;
                break;
            case UiEventEntry.SCREEN_TYPE_9:
                row = 3;
                column = 3;
                screenCount = 9;
                break;
            default:
                return;
        }

        // 将已有的视频显示View直接缓存，后续修改显示宽高，而不再次创建
        ArrayList<VideoItem> videoItems = new ArrayList<VideoItem>();
        for (VideoItem videoItem : videoItemList)
        {
            // 当单个视频窗口最大化显示时，只将需要最大化显示的视频窗口缓存，删除其余空窗口
            if (isFullScreen())
            {
                if ((videoItem.getSurFaceView() != null || !TextUtils.isEmpty(videoItem.getMessage())) && videoItem.isFullScreen())
                {
                    videoItems.add(videoItem);
                }
                else
                {
                    removeView(videoItem);
                }
            }
            // 将所有已有视频的窗口缓存，删除其余空窗口
            else
            {
                if (videoItem.getSurFaceView() != null || !TextUtils.isEmpty(videoItem.getMessage()))
                {
                    videoItem.setFullScreen(false);
                    videoItems.add(videoItem);
                }
                else
                {
                    removeView(videoItem);
                }
            }
        }

//        videoItems.addAll(cashedVideoItems);
        videoItemList.clear();
//        removeAllViews();
        int cellWidth = width / column;
        int cellHeight = height / row;

        for (int i = 0; i < screenCount; i++)
        {
            VideoItem vitem;
            // 优先重置缓存视频窗口
            if (videoItems.size() > 0)
            {
                vitem = getPreferedItem(videoItems);
            }
            // 无缓存视频后，对分屏窗口添加新的空白窗口
            else
            {
                vitem = new VideoItem(getContext());
                registerDoubleClickListener(vitem, doubleClickListener);
                addView(vitem);
            }
            vitem.setTag(i);// 作为编号

            // 计算分屏宽高
            Rect mRect = new Rect();
            // 对于非对称屏幕分屏，特殊处理
            if (type == UiEventEntry.SCREEN_TYPE_6 || type == UiEventEntry.SCREEN_TYPE_8)
            {
                if (i == 0)
                {
                    mRect.set(0, 0, cellWidth * (column - 1), cellHeight * (row - 1));
                }
                else if (i < row)
                {
                    mRect.set((column - 1) * cellWidth, (i - 1) * cellHeight, width, i * cellHeight);
                }
                else
                {
                    mRect.set((i - row) * cellWidth, (row - 1) * cellHeight, (i - row + 1) * cellWidth, height);
                }
            }
            else
            {
                mRect.set((i % column) * cellWidth, (i / column) * cellHeight, (i % column + 1) * cellWidth, ((i / column) + 1) * cellHeight);
            }

            // 重置视频窗口
            resetVideoItem(vitem, mRect);
            videoItemList.add(vitem);
        }
        postInvalidate();
    }

    /**
     * 方法说明 : 挑出当前需要优先显示的视频窗口，并将其从缓存中移除
     * @param videoItems
     * @return VideoItem
     * @author hubin
     * @Date 2015-8-26
     */
    private VideoItem getPreferedItem(ArrayList<VideoItem> videoItems)
    {
        VideoItem preferredItem;
        for (VideoItem videoItem : videoItems)
        {
            if (videoItem.isFullScreen())
            {
                preferredItem = videoItem;
                videoItems.remove(videoItem);
                return videoItem;
            }
        }
        preferredItem = videoItems.get(0);
        videoItems.remove(0);
        return preferredItem;
    }

    /**
     * 方法说明 : 用于当窗口双击全屏后，再次双击返回到原来窗口模式
     * @param screenSize
     * @return void
     * @author hubin
     * @Date 2015-10-9
     */
    public void fullScreenToNormal(int screenSize)
    {
        setFullScreen(false);
        setScreenSize(screenSize);
        if (isMini && getRelationVideoFrame() != null)
        {
            getRelationVideoFrame().setScreenSize(screenSize);
        }
        for (SurfaceView surfaceView : cashedVideos)
        {
            addVideoItem(surfaceView);
            if (isMini && getRelationVideoFrame() != null)
            {
                getRelationVideoFrame().addVideoItem(surfaceView);
            }
        }
        cashedVideos.clear();
    }

    /**
    * 方法说明 : 注册双击和单击监听器
    * @param view
    * @param listener
    * @return void
    * @author hubin
    * @Date 2015-6-16
    */
    private void registerDoubleClickListener(View view, final OnDoubleClickListener listener)
    {
        if (listener == null)
        {
            view.setOnClickListener(null);
            return;
        }

        view.setOnClickListener(new View.OnClickListener()
        {
            private static final int DOUBLE_CLICK_TIME = 350; // 双击间隔时间350毫秒
            private boolean waitDouble = true;

            public void onClick(final View v)
            {
                if (waitDouble)
                {
                    waitDouble = false;
                    // 等待双击时间，否则执行单击事件
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (!waitDouble)
                            {
                                waitDouble = true;
                                // 如果过了等待事件还是预执行双击状态，则视为单击
                                listener.OnSingleClick(v);
                            }
                        }
                    }, DOUBLE_CLICK_TIME);
                }
                else
                {
                    waitDouble = true;
                    listener.OnDoubleClick(v); // 执行双击
                }
            }
        });
    }

    private OnDoubleClickListener doubleClickListener = new OnDoubleClickListener()
    {
        @Override
        public void OnSingleClick(View v)
        {
            Log.info(TAG, "OnSingleClick:: id:" + v.getTag());
            if (!UiApplication.getVsService().isLoopStarted())
            {
                // 单机重置选中状态
                for (VideoItem vi : videoItemList)
                {
                    vi.setVideoSelected(false);
                }
                ((VideoItem) v).setVideoSelected(true);
                SurfaceView surfaceView = ((VideoItem) v).getSurFaceView();
                if (surfaceView != null)
                {
                    String number = (String) ((surfaceView).getTag(R.id.call_number));
                    String sessionId = (String) ((surfaceView).getTag(R.id.session_id));
                    EventNotifyHelper.getInstance().postNotificationName(UiEventEntry.NOTIFY_PTZ_NUMBER_CHANGE, number, sessionId);
                }
            }
        }

        @Override
        public void OnDoubleClick(View v)
        {
            Log.info(TAG, "OnDoubleClick:: id:" + v.getTag());
            // 不处理默认1分屏的双击场景
            if (screenType == UiEventEntry.SCREEN_TYPE_1 || (screenType == UiEventEntry.SCREEN_TYPE_AUTO && videoItemList.size() == 1))
            {
                return;
            }
            // 默认不是一分屏时，双击恢复到默认分屏
            else if (videoItemList.size() == 1)
            {
                fullScreenToNormal(screenType);
            }
            // 双击从默认分屏进入一分屏
            else if (((VideoItem) v).getSurFaceView() != null || !TextUtils.isEmpty(((VideoItem) v).getMessage()))
            {
                setFullScreen(true);
                for (VideoItem videoItem : videoItemList)
                {
                    // 对已有视频填充窗口进行视频缓存，或文字信息填充的窗口缓存其对应扩展屏视频
                    if ((videoItem.getSurFaceView() != null && !((VideoItem) v).getSurFaceView().getTag(R.id.call_number)
                            .equals(videoItem.getSurFaceView().getTag(R.id.call_number)))
                            || (!TextUtils.isEmpty(videoItem.getMessage()) && !videoItem.getMessage().equals(((VideoItem) v).getMessage())))
                    {
                        if (isMini)
                        {
                            // 缓存扩展屏对应视频
                            for (VideoItem videoItem2 : getRelationVideoFrame().getVideoItems())
                            {
                                if (videoItem2.getSurFaceView() != null && videoItem.getMessage().equals(videoItem2.getSurFaceView().getTag(R.id.call_number)))
                                {
                                    cashedVideos.add(videoItem2.getSurFaceView());
                                }
                            }
                        }
                        else
                        {
                            // 缓存当前窗口对应视频
                            cashedVideos.add(videoItem.getSurFaceView());
                        }
                    }
                }
                ((VideoItem) v).setFullScreen(true);
                // 缓存窗口个数，用于后续重置
                int tmp = screenType;
                setScreenSize(UiEventEntry.SCREEN_TYPE_1);
                if (isMini && getRelationVideoFrame() != null)
                {
                    getRelationVideoFrame().setScreenSize(UiEventEntry.SCREEN_TYPE_1);
                }
                screenType = tmp;
            }
        }
    };

    /**
     * 方法说明 : 设置选中号码对应的视频窗口为选中状态
     * @param number
     * @return void
     * @author hubin
     * @Date 2015-10-9
     */
    public void resetSelectedItem(String number)
    {

        for (VideoItem vi : videoItemList)
        {
            if (TextUtils.isEmpty(number))
            {
                vi.setVideoSelected(false);
            }
            else
            {
                if (number.equals(vi.getVideoNumber()))
                {
                    vi.setVideoSelected(true);
                }
                else
                {
                    vi.setVideoSelected(false);
                }
            }

        }
    }

    /**
     * 方法说明 :重置视频窗口大小和显示内容
     * @param vitem
     * @author HeZhen
     * @Date 2015-7-22
     */
    public void resetVideoItem(VideoItem vitem, Rect rect)
    {
        vitem.setRect(rect);
        if (isMini)
        {
            vitem.setBackgroundResource(R.drawable.selector_video_item);
        }
        else
        {
            if (UiConfigEntry.DEFAULT_VIDEO_BG.equals(UiApplication.getConfigService().getVideoBg()))
            {
                vitem.setBackgroundResource(R.drawable.player_background);
            }
            else
            {
                vitem.setBackground(new BitmapDrawable(getResources(), UiApplication.getConfigService().getVideoBg()));
            }
        }
    }

    public ArrayList<VideoItem> getVideoItems()
    {
        return videoItemList;
    }

    private boolean addVideoItemToEmpty(SurfaceView surfaceView)
    {
        for (int i = 0; i < videoItemList.size(); i++)
        {
            if (isMini && TextUtils.isEmpty(videoItemList.get(i).getMessage()))
            {
                Log.info(TAG, "addVideoItem:: showMessage:" + (String) surfaceView.getTag(R.id.call_number));
                videoItemList.get(i).showMessage((String) surfaceView.getTag(R.id.call_number));
                videoItemList.get(i).setSessionId((String) surfaceView.getTag(R.id.session_id));
                return true;
            }
            else if (!isMini && videoItemList.get(i).getSurFaceView() == null)
            {
                Log.info(TAG, "addVideoItem:: setSurFaceView:" + (String) surfaceView.getTag(R.id.call_number));
                videoItemList.get(i).setSurFaceView(surfaceView);
                return true;
            }
        }
        return false;
    }

    private boolean addVideoItemToPosition(SurfaceView surfaceView, int position)
    {
        if (position >= 0 && position < videoItemList.size())
        {
            if (isMini)
            {
                Log.info(TAG, "addVideoItem:: showMessage:" + (String) surfaceView.getTag(R.id.call_number));
                videoItemList.get(position).showMessage((String) surfaceView.getTag(R.id.call_number));
                videoItemList.get(position).setSessionId((String) surfaceView.getTag(R.id.session_id));
                return true;
            }
            else
            {
                Log.info(TAG, "addVideoItem:: setSurFaceView:" + (String) surfaceView.getTag(R.id.call_number));
                videoItemList.get(position).setSurFaceView(surfaceView);
                return true;
            }
        }
        return false;
    }

    /**
     * 方法说明 : 向视频分窗口中添加新视频，如果为扩展屏控制窗口则添加文字信息
     * @param surfaceView
     * @return boolean
     * @author hubin
     * @Date 2015-9-14
     */
    public boolean addVideoItem(SurfaceView surfaceView)
    {
        if (surfaceView == null)
        {
            return false;
        }
        String number = (String) surfaceView.getTag(R.id.call_number);
        int position = -1;

        // 对于有位置信息的视频，插入到指定位置
        if (videoPostionMap.containsKey(number))
        {
            position = videoPostionMap.get(number);
            videoPostionMap.remove(number);
            // 对于有效位置，插入到指定位置
            if (addVideoItemToPosition(surfaceView, position))
            {
                return true;
            }
            else
            {
                return addVideoItemToEmpty(surfaceView);
            }
        }
        else
        {
            return addVideoItemToEmpty(surfaceView);
        }
    }

    /**
     * 方法说明 : 从视频分窗口中移除视频，如果为扩展屏控制窗口则移除文字信息
     * @param userNum
     * @return boolean
     * @author hubin
     * @Date 2015-9-14
     */
    public boolean removeVideoItem(String userNum)
    {
        if (TextUtils.isEmpty(userNum))
        {
            return false;
        }

        for (int i = 0; i < videoItemList.size(); i++)
        {
            if (isMini && userNum.equals(videoItemList.get(i).getMessage()))
            {
                videoItemList.get(i).hideMessage();
                return true;
            }
            else if (!isMini && videoItemList.get(i).getSurFaceView() != null && userNum.equals(videoItemList.get(i).getSurFaceView().getTag(R.id.call_number)))
            {
                videoItemList.get(i).setSurFaceView(null);
                return true;
            }
        }
        return false;
    }

    /**
     * 方法说明 : 清除所有视频窗口内显示信息
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    public void cleanVideoItems()
    {
        for (VideoItem videoItem : videoItemList)
        {
            if (isMini && !TextUtils.isEmpty(videoItem.getMessage()))
            {
                videoItem.hideMessage();
            }
            else if (!isMini && videoItem.getSurFaceView() != null)
            {
                videoItem.setSurFaceView(null);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
//        if (isMini)
//        {
        Log.info(TAG, "dispatchTouchEvent:: isMini:" + isMini);
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                Log.info(TAG, "ACTION_DOWN:: mDownX:" + mDownX + " mDownY:" + mDownY);

                for (VideoItem vi : videoItemList)
                {
                    if (vi.isInTouch(mDownX, mDownY))
                    {
                        mStartDragItemView = vi;
                        break;
                    }
                }
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
                getLocationOnScreen(location);
//                    Log.info(TAG, "getLocationOnScreen: location[0]" + location[0] + " location[1]" + location[1]);
                mHandler.postDelayed(mLongClickRunnable, 400);
                break;
            case MotionEvent.ACTION_MOVE:
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int moveX = x - mDownX;
                int moveY = y - mDownY;

                Log.info(TAG, "ACTION_MOVE:: moveX" + moveX + " moveY:" + moveY);
                if (isMini)
                {
                    if (!(mStartDragItemView != null && mStartDragItemView.isInTouch(x, y)))
                    {
                        mHandler.removeCallbacks(mLongClickRunnable);
                    }
                }
                else if (Math.abs(moveX) > 5 || Math.abs(moveY) > 5)
                {
                    Log.info(TAG, "ACTION_MOVE:: removeCallbacks");
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                // 拖动item
                onDragItem(moveX, moveY);
                break;
            case MotionEvent.ACTION_UP:
                Log.info(TAG, "ACTION_UP");
                mHandler.removeCallbacks(mLongClickRunnable);
                onStopDrag((int) ev.getX(), (int) ev.getY());
                isDrag = false;
                break;
        }
//        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 方法说明 : 用于会议或监控时拖动成员到视频窗口，需要切换视频窗口中的视频
     * @param x 目标横向坐标
     * @param y 目标纵向坐标
     * @param targetVideo 目标视频
     * @param targetNum 目标号码
     * @return boolean
     * @author hubin
     * @Date 2015-9-24
     */
    public boolean onSwitchVideo(int x, int y, SurfaceView targetVideo, String targetNum)
    {
        Log.info(TAG, "onSwitchVideo:: x:" + x + " y:" + y + " targetNum:" + targetNum);
        getLocationOnScreen(location);
        for (int i = 0; i < videoItemList.size(); i++)
        {
            if (videoItemList.get(i).isInTouch(x - location[0], y - location[1]))
            {
                // 处理目标区域的原有视频
                if (videoItemList.get(i).getSurFaceView() != null)
                {
                    // 拖动到自己视频区域或者单呼区域不做处理
                    if (videoItemList.get(i).getSurFaceView().getTag(R.id.call_number).equals(targetNum)
                            || (Integer) videoItemList.get(i).getSurFaceView().getTag(R.id.session_type) == CommonConstantEntry.SESSION_TYPE_SCALL)
                    {
                        return false;
                    }
                    UiUtils.closeVideo(videoItemList.get(i).getSurFaceView());
                }

                // 如果替换视频已存在，则直接替换，否则需要记录打开后的视频位置
                if (targetVideo != null)
                {
                    removeVideoItem(targetNum);
                    videoItemList.get(i).setSurFaceView(targetVideo);
                }
                else
                {
                    // 视频尚未发起，记录视频位置
                    videoPostionMap.put(targetNum, i);
                }
                return true;
            }
        }
        return false;
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
        if (isDrag)
        {
            Log.info(TAG, "onStopDrag:: x:" + x + " y:" + y);
            VideoItem temp = null;
            for (VideoItem vi : videoItemList)
            {
                if (vi.isInTouch(x, y))
                {
                    temp = vi;
                    break;
                }
            }
            if (temp != null && !temp.equals(mStartDragItemView))
            {
                switchVideoItem(temp, mStartDragItemView);
                if (isMini)
                {
                    VideoItem temp1 = null;
                    VideoItem temp2 = null;
                    int count = 0;
                    for (VideoItem vi : getRelationVideoFrame().videoItemList)
                    {
                        if (vi.getTag() == temp.getTag())
                        {
                            temp1 = vi;
                            count++;
                        }
                        else if (vi.getTag() == mStartDragItemView.getTag())
                        {
                            temp2 = vi;
                            count++;
                        }
                        if (count >= 2)
                        {
                            break;
                        }
                    }
                    switchVideoItem(temp1, temp2);
                }
            }
            removeDragImage();
        }
    }

    /**
     * 方法说明 : 位置切换
     * @param temp1
     * @param temp2
     * @return void
     * @author hubin
     * @Date 2015-9-14
     */
    private void switchVideoItem(VideoItem temp1, VideoItem temp2)
    {
        Log.info(TAG, "switchVideoItem:: ");
        if (temp1 == null || temp2 == null)
        {
            return;
        }
        SurfaceView sv1 = temp1.getSurFaceView();
        SurfaceView sv2 = temp2.getSurFaceView();
        temp2.setSurFaceView(sv1);
        temp1.setSurFaceView(sv2);

        String text1 = temp1.getMessage();
        String text2 = temp2.getMessage();
        String sessionId1 = temp1.getSessionId();
        String sessionId2 = temp2.getSessionId();
        temp2.showMessage(text1);
        temp1.showMessage(text2);
        temp2.setSessionId(sessionId1);
        temp1.setSessionId(sessionId2);
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

    // 用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            isDrag = true; // 设置可以拖拽
            int x = (int) mStartDragItemView.getX() + location[0];
            int y = (int) mStartDragItemView.getY() + location[1];
            // 根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, x, y);
        }
    };

    /**
     * 拖动item，在里面实现了item镜像的位置更新
     * @param x
     * @param y
     */
    private void onDragItem(int moveX, int moveY)
    {
        if (isDrag && mDragImageView != null)
        {
            getLocationOnScreen(location);
//            Log.info(TAG, "getLocationOnScreen: location[0]" + location[0] + " location[1]" + location[1]);
            mWindowLayoutParams.x = (moveX + (int) mStartDragItemView.getX()) + location[0];
            mWindowLayoutParams.y = (moveY + (int) mStartDragItemView.getY()) + location[1];
            mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); // 更新镜像的位置
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

    public boolean isFullScreen()
    {
        return isFullScreen;
    }

    public void setFullScreen(boolean isFullScreen)
    {
        this.isFullScreen = isFullScreen;
    }
}
