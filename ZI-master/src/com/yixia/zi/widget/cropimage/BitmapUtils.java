/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yixia.zi.widget.cropimage;

import java.io.IOException;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Collection of utility functions used in this package.
 */
public class BitmapUtils {
	
	private static OnClickListener sNullOnClickListener;

	private BitmapUtils() {
	}

	// Rotates the bitmap by the specified degree.
	// If a new bitmap is created, the original bitmap is recycled.
	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return b;
	}

	/*
	 * Compute the sample size as a function of minSideLength
	 * and maxNumOfPixels.
	 * minSideLength is used to specify that minimal width or height of a bitmap.
	 * maxNumOfPixels is used to specify the maximal size in pixels that are tolerable
	 * in terms of memory usage.
	 *
	 * The function returns a sample size based on the constraints.
	 * Both size and minSideLength can be passed in as IImage.UNCONSTRAINED,
	 * which indicates no care of the corresponding constraint.
	 * The functions prefers returning a sample size that
	 * generates a smaller bitmap, unless minSideLength = IImage.UNCONSTRAINED.
	 */

	public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int targetHeight, boolean scaleUp) {
		int deltaX = source.getWidth() - targetWidth;
		int deltaY = source.getHeight() - targetHeight;
		if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
			/*
			 * In this case the bitmap is smaller, at least in one dimension,
			 * than the target.  Transform it by placing as much of the image
			 * as possible into the target and leaving the top/bottom or
			 * left/right (or both) black.
			 */
			Bitmap b2 = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b2);

			int deltaXHalf = Math.max(0, deltaX / 2);
			int deltaYHalf = Math.max(0, deltaY / 2);
			Rect src = new Rect(deltaXHalf, deltaYHalf, deltaXHalf + Math.min(targetWidth, source.getWidth()), deltaYHalf + Math.min(targetHeight, source.getHeight()));
			int dstX = (targetWidth - src.width()) / 2;
			int dstY = (targetHeight - src.height()) / 2;
			Rect dst = new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY);
			c.drawBitmap(source, src, dst, null);
			return b2;
		}
		float bitmapWidthF = source.getWidth();
		float bitmapHeightF = source.getHeight();

		float bitmapAspect = bitmapWidthF / bitmapHeightF;
		float viewAspect = (float) targetWidth / targetHeight;

		if (bitmapAspect > viewAspect) {
			float scale = targetHeight / bitmapHeightF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		} else {
			float scale = targetWidth / bitmapWidthF;
			if (scale < .9F || scale > 1F) {
				scaler.setScale(scale, scale);
			} else {
				scaler = null;
			}
		}

		Bitmap b1;
		if (scaler != null) {
			// this is used for minithumb and crop, so we want to filter here.
			b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), scaler, true);
		} else {
			b1 = source;
		}

		int dx1 = Math.max(0, b1.getWidth() - targetWidth);
		int dy1 = Math.max(0, b1.getHeight() - targetHeight);

		Bitmap b2 = Bitmap.createBitmap(b1, dx1 / 2, dy1 / 2, targetWidth, targetHeight);

		if (b1 != source) {
			b1.recycle();
		}

		return b2;
	}

	/**
	 * Creates a centered bitmap of the desired size. Recycles the input.
	 * 
	 * @param source
	 */
	public static Bitmap extractMiniThumb(Bitmap source, int width, int height) {
		return BitmapUtils.extractMiniThumb(source, width, height, true);
	}

	public static Bitmap extractMiniThumb(Bitmap source, int width, int height, boolean recycle) {
		if (source == null) {
			return null;
		}

		float scale;
		if (source.getWidth() < source.getHeight()) {
			scale = width / (float) source.getWidth();
		} else {
			scale = height / (float) source.getHeight();
		}
		Matrix matrix = new Matrix();
		matrix.setScale(scale, scale);
		Bitmap miniThumbnail = transform(matrix, source, width, height, false);

		if (recycle && miniThumbnail != source) {
			source.recycle();
		}
		return miniThumbnail;
	}

	/**
	 * Creates a byte[] for a given bitmap of the desired size. Recycles the
	 * input bitmap.
	 */

	/**
	 * Create a video thumbnail for a video. May return null if the video is
	 * corrupt.
	 * 
	 * @param filePath
	 */
	public static Bitmap createVideoThumbnail(String filePath) {
		Bitmap bitmap = null;
		/* MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		 try {
		     retriever.setMode(MediaMetadataRetriever.MODE_CAPTURE_FRAME_ONLY);
		     retriever.setDataSource(filePath);
		     bitmap = retriever.captureFrame();
		 } catch (IllegalArgumentException ex) {
		     // Assume this is a corrupt video file
		 } catch (RuntimeException ex) {
		     // Assume this is a corrupt video file.
		 } finally {
		     try {
		         retriever.release();
		     } catch (RuntimeException ex) {
		         // Ignore failures while cleaning up.
		     }
		 }*/
		return bitmap;
	}

	

	/**
	 * Make a bitmap from a given Uri.
	 * 
	 * @param uri
	 */

	@SuppressWarnings("unused")
	private static ParcelFileDescriptor makeInputStream(Uri uri, ContentResolver cr) {
		try {
			return cr.openFileDescriptor(uri, "r");
		} catch (IOException ex) {
			return null;
		}
	}

	public static void debugWhere(String tag, String msg) {
		Log.d(tag, msg + " --- stack trace begins: ");
		StackTraceElement elements[] = Thread.currentThread().getStackTrace();
		// skip first 3 element, they are not related to the caller
		for (int i = 3, n = elements.length; i < n; ++i) {
			StackTraceElement st = elements[i];
			String message = String.format("    at %s.%s(%s:%s)", st.getClassName(), st.getMethodName(), st.getFileName(), st.getLineNumber());
			Log.d(tag, message);
		}
		Log.d(tag, msg + " --- stack trace ends.");
	}

	public static synchronized OnClickListener getNullOnClickListener() {
		if (sNullOnClickListener == null) {
			sNullOnClickListener = new OnClickListener() {
				public void onClick(View v) {
				}
			};
		}
		return sNullOnClickListener;
	}

	public static void Assert(boolean cond) {
		if (!cond) {
			throw new AssertionError();
		}
	}

	public static boolean equals(String a, String b) {
		// return true if both string are null or the content equals
		return a == b || a.equals(b);
	}

	private static class BackgroundJob extends MonitoredActivity.LifeCycleAdapter implements Runnable {

		private final MonitoredActivity mActivity;
		private final ProgressDialog mDialog;
		private final Runnable mJob;
		private final Handler mHandler;
		private final Runnable mCleanupRunner = new Runnable() {
			public void run() {
				mActivity.removeLifeCycleListener(BackgroundJob.this);
				if (mDialog.getWindow() != null)
					mDialog.dismiss();
			}
		};

		public BackgroundJob(MonitoredActivity activity, Runnable job, ProgressDialog dialog, Handler handler) {
			mActivity = activity;
			mDialog = dialog;
			mJob = job;
			mActivity.addLifeCycleListener(this);
			mHandler = handler;
		}

		public void run() {
			try {
				mJob.run();
			} finally {
				mHandler.post(mCleanupRunner);
			}
		}

		@Override
		public void onActivityDestroyed(MonitoredActivity activity) {
			// We get here only when the onDestroyed being called before
			// the mCleanupRunner. So, run it now and remove it from the queue
			mCleanupRunner.run();
			mHandler.removeCallbacks(mCleanupRunner);
		}

		@Override
		public void onActivityStopped(MonitoredActivity activity) {
			mDialog.hide();
		}

		@Override
		public void onActivityStarted(MonitoredActivity activity) {
			mDialog.show();
		}
	}

	public static void startBackgroundJob(MonitoredActivity activity, String title, String message, Runnable job, Handler handler) {
		// Make the progress dialog uncancelable, so that we can gurantee
		// the thread will be done before the activity getting destroyed.
		ProgressDialog dialog = ProgressDialog.show(activity, title, message, true, false);
		new Thread(new BackgroundJob(activity, job, dialog, handler)).start();
	}

	// Returns Options that set the puregeable flag for Bitmap decode.
	public static BitmapFactory.Options createNativeAllocOptions() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		//options.inNativeAlloc = true;
		return options;
	}

	// Thong added for rotate
	public static Bitmap rotateImage(Bitmap src, float degree) {
		// create new matrix
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);
		Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		return bmp;
	}
}
