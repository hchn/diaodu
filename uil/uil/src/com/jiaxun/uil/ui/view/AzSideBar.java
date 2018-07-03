package com.jiaxun.uil.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.jiaxun.uil.R;

/**
 * 说明：通讯录 列表样式 右侧索引
 *
 * @author  Hz
 *
 * @Date 2015-5-13
 */
public class AzSideBar extends View {
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	private int choose = -1;
	private Paint paint = new Paint();

	private TextView mTextDialog;
	private boolean isRest = false;

	public void setTextView(TextView mTextDialog) {
		isRest = false;
		this.mTextDialog = mTextDialog;
	}


	public AzSideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AzSideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AzSideBar(Context context) {
		super(context);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 获取焦点改变背景颜色.
		int height = getHeight();// 获取对应高度
		int width = getWidth();// 获取对应宽度
		int singleHeight = height / b.length;// 获取每一个字母的高度

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(14);
			// 选中的状态
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x坐标等于中间-字符串宽度的一半.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();// 重置画笔
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
		switch (action) {
			case MotionEvent.ACTION_UP:
				setBackground(new ColorDrawable(0x00000000));
				choose = -1;//
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;
	
			default:
				if (!isRest) {
					setBackgroundResource(R.color.gray);
					if (oldChoose != c) {
						if (c >= 0 && c < b.length) {
							if (listener != null) {
								listener.onTouchingLetterChanged(b[c]);
							}
							if (mTextDialog != null) {
								mTextDialog.setText(b[c]);
								mTextDialog.setVisibility(View.VISIBLE);
							}
							choose = c;
							invalidate();
						}
					}
				}
				break;
		}
		return true;
	}

	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}
	
	public void setReset(){
		isRest = true;
		setBackgroundDrawable(new ColorDrawable(0x00000000));
		choose = -1;
		invalidate();
		if (mTextDialog != null) {
			mTextDialog.setVisibility(View.INVISIBLE);
		}
	}
	
	public boolean getDiaLogStatus(){
		int status = mTextDialog.getVisibility();
		switch (status) {
			case View.GONE:
				return false;
			case View.INVISIBLE:
				return false;
			case View.VISIBLE:
				return true;
			default:
				return false;
		}
	}
	
	public boolean getBooleanRest(){
		return isRest;
	}
	
	public void setBooleanRest(boolean value){
		isRest = value;
	}
}