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
 * ˵����ͨѶ¼ �б���ʽ �Ҳ�����
 *
 * @author  Hz
 *
 * @Date 2015-5-13
 */
public class AzSideBar extends View {
	// �����¼�
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
		// ��ȡ����ı䱳����ɫ.
		int height = getHeight();// ��ȡ��Ӧ�߶�
		int width = getWidth();// ��ȡ��Ӧ���
		int singleHeight = height / b.length;// ��ȡÿһ����ĸ�ĸ߶�

		for (int i = 0; i < b.length; i++) {
			paint.setColor(Color.WHITE);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(14);
			// ѡ�е�״̬
			if (i == choose) {
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}
			// x��������м�-�ַ�����ȵ�һ��.
			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();// ���û���
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// ���y����
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * b.length);// ���y������ռ�ܸ߶ȵı���*b����ĳ��Ⱦ͵��ڵ��b�еĸ���.
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