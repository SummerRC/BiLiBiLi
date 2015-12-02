package com.yixia.zi.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.yixia.zi.R;
import com.yixia.zi.utils.WeakHandler;

public class SegmentSeekBar extends SeekBar {
	private static final int MSG_UPDATE = 42;
	private Handler mHandler;
	private RectF mBounds;
	private Paint mPaint;
	private double[] mSegments;

	public SegmentSeekBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SegmentSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SegmentSeekBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context ctx) {
		mBounds = new RectF(0, 0, 0, 0);
		mHandler = new WeakHandler<Context>(ctx) {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == MSG_UPDATE) {
					invalidate();
				}
			}
		};

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(ctx.getResources().getColor(R.color.seekbar_buffer_color));
	}

	public void setSegments(double[] segments) {
		if (segments != null && segments.length > 0) {
			mSegments = segments;
			mHandler.removeMessages(MSG_UPDATE);
			mHandler.sendEmptyMessage(MSG_UPDATE);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mSegments == null) {
			return;
		}
		for (int i = 0; i < mSegments.length; i += 2) {
			double begin = mSegments[i];
			double end = mSegments[i + 1];

			int available = getWidth() - getPaddingLeft() - getPaddingRight();
			int length = (int) ((end - begin) * available);

			mBounds.left = (int) (begin * available);
			mBounds.right = mBounds.left + length;

			mBounds.top = getHeight() / 2 - 4;
			mBounds.bottom = getHeight() / 2 + 2;

			canvas.save();
			canvas.translate(getPaddingLeft(), getPaddingTop());
			canvas.drawRect(mBounds, mPaint);
			canvas.restore();
		}
	}

	public static class SavedState extends BaseSavedState {
		private double[] mSegments;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			in.readDoubleArray(mSegments);
		}

		@Override
		public void writeToParcel(Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeDoubleArray(mSegments);
		}

		public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {
			@Override
			public SavedState createFromParcel(Parcel parcel) {
				return new SavedState(parcel);
			}

			@Override
			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};

	}

	@Override
	public Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();
		if (isSaveEnabled()) {
			SavedState ss = new SavedState(superState);
			ss.mSegments = mSegments;
			return ss;
		}
		return superState;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (!(state instanceof SavedState)) {
			super.onRestoreInstanceState(state);
			return;
		}

		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		mSegments = ss.mSegments;
	}
}
