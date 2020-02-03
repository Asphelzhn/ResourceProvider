/*
 * Copyright (c) 2016.
 * 个人版权所有
 * kuangmeng.net
 */

package hitamigos.sourceget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomProgressBar extends ProgressBar {
	
	private String mProgressText;
	private Paint mPaint;

	public CustomProgressBar(Context context) {
		super(context);
		initPaint();
	}

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPaint();
	}
	
	private void initPaint() {
		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(50);
	}
	
	public void setProgressText(String text) {
		mProgressText = text;
	}
	
	public String getProgressText() {
		return mProgressText;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    Rect rect = new Rect();
	    mPaint.getTextBounds(mProgressText, 0, mProgressText.length(), rect);
	    int x = (getWidth() / 2) - rect.centerX();
	    int y = (getHeight() / 2) - rect.centerY();
	    canvas.drawText(mProgressText, x, y, this.mPaint);
	}
	
}
