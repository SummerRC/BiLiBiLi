package com.yixia.zi.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

@Deprecated
public class Gallery extends android.widget.Gallery {

	public Gallery(Context context) {
		super(context);
	}

	public Gallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public Gallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

}
