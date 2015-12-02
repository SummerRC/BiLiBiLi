package com.yixia.zi.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.yixia.zi.R;

public class IndicatorLine extends View {
	
	private static final int AVERAGE = 3;
	private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int mCurrentItem = 0;
	private int mItemWidth;

	public IndicatorLine(Context context) {
		super(context);
		init();
	}

	public IndicatorLine(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		mItemWidth = width / AVERAGE;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(getResources().getColor(R.color.indicator_color));
		mPaint.setAntiAlias(true);
		final float top = getPaddingTop();
		final float bottom = getHeight() - getPaddingBottom();
		int right = mItemWidth * (mCurrentItem + 1);
		canvas.drawRect(0f, top, right, bottom, mPaint);
	}

	public void setCurrentItem(int currentItem) {
		mCurrentItem = currentItem;
		invalidate();
	}

}
