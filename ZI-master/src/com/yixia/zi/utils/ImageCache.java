/*
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
package com.yixia.zi.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;

import com.yixia.zi.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class holds our bitmap caches (memory and disk).
 */
public class ImageCache {
	private static final String TAG = "ImageCache";

	// Default memory cache size
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 1024 * 5; // 5MB
	private static final int DEFAULT_MEM_CACHE_DIVIDER = 8; // memory class/this = mem cache size

	// Default disk cache size
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

	// Compression settings when writing images to disk cache
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.PNG;
	private static final int DEFAULT_COMPRESS_QUALITY = 75;

	private static final String CACHE_FILENAME_PREFIX = "cache_";

	private static final int DISK_CACHE_INDEX = 0;

	// Constants to easily toggle various caches
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_CLEAR_DISK_CACHE_ON_START = false;

	private LruCache<String, Bitmap> mMemoryCache;
	private DiskLruCache mDiskCache;
	private ImageCacheParams mCacheParams;

	private boolean mPauseDiskAccess = false;

	/**
	 * Creating a new ImageCache object using the specified parameters.
	 * 
	 * @param context The context to use
	 * @param cacheParams The cache parameters to use to initialize the cache
	 */
	public ImageCache(Context context, ImageCacheParams cacheParams) {
		init(context, cacheParams);
	}

	/**
	 * Creating a new ImageCache object using the default parameters.
	 * 
	 * @param context The context to use
	 * @param uniqueName A unique name that will be appended to the cache
	 * directory
	 */
	public ImageCache(Context context, String uniqueName) {
		init(context, new ImageCacheParams(context, uniqueName));
	}

	/**
	 * Find and return an existing ImageCache stored in a {@link RetainFragment},
	 * if not found a new one is created with defaults and saved to a
	 * {@link RetainFragment}.
	 * 
	 * @param activity The calling {@link FragmentActivity}
	 * @param uniqueName A unique name to append to the cache directory
	 * @return An existing retained ImageCache object or a new one if one did not
	 * exist.
	 */
	public static ImageCache findOrCreateCache(final FragmentActivity activity, final String uniqueName) {
		return findOrCreateCache(activity, new ImageCacheParams(activity, uniqueName));
	}

	/**
	 * Find and return an existing ImageCache stored in a {@link RetainFragment},
	 * if not found a new one is created using the supplied params and saved to a
	 * {@link RetainFragment}.
	 * 
	 * @param activity The calling {@link FragmentActivity}
	 * @param cacheParams The cache parameters to use if creating the ImageCache
	 * @return An existing retained ImageCache object or a new one if one did not
	 * exist
	 */
	public static ImageCache findOrCreateCache(final FragmentActivity activity, ImageCacheParams cacheParams) {

		// Search for, or create an instance of the non-UI RetainFragment
		final RetainFragment mRetainFragment = findOrCreateRetainFragment(activity.getSupportFragmentManager());

		// See if we already have an ImageCache stored in RetainFragment
		ImageCache imageCache = (ImageCache) mRetainFragment.getObject();

		// No existing ImageCache, create one and store it in RetainFragment
		if (imageCache == null) {
			imageCache = new ImageCache(activity, cacheParams);
			mRetainFragment.setObject(imageCache);
		}

		return imageCache;
	}

	/**
	 * Initialize the cache, providing all parameters.
	 * 
	 * @param context The context to use
	 * @param cacheParams The cache parameters to initialize the cache
	 */
	private void init(Context context, ImageCacheParams cacheParams) {
		mCacheParams = cacheParams;
		final File diskCacheDir = getDiskCacheDir(context, cacheParams.uniqueName);

		if (cacheParams.diskCacheEnabled) {
			if (!diskCacheDir.exists()) {
				diskCacheDir.mkdir();
			}

			if (getUsableSpace(diskCacheDir) > cacheParams.diskCacheSize) {
				try {
					mDiskCache = DiskLruCache.open(diskCacheDir, 1, 1, cacheParams.diskCacheSize);
				} catch (final IOException e) {
					Log.e(TAG, "init - " + e);
				}
			}
		}

		// Set up memory cache
		if (cacheParams.memoryCacheEnabled) {
			mMemoryCache = new LruCache<String, Bitmap>(cacheParams.memCacheSize) {
				/**
				 * Measure item size in bytes rather than units which is more practical
				 * for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return getBitmapSize(bitmap);
				}
			};
		}
	}

	public synchronized void addBitmapToCache(String data, Bitmap bitmap) {
		if (data == null || bitmap == null) {
			return;
		}

		// Add to memory cache
		if (mMemoryCache != null && mMemoryCache.get(data) == null) {
			mMemoryCache.put(data, bitmap);
		}

		// Add to disk cache
		if (mDiskCache != null) {
			final String key = hashKeyForDisk(data);
			try {
				if (mDiskCache.get(key) == null) {
					final DiskLruCache.Editor editor = mDiskCache.edit(key);
					if (editor != null) {
						final OutputStream out = editor.newOutputStream(DISK_CACHE_INDEX);
						bitmap.compress(mCacheParams.compressFormat, mCacheParams.compressQuality, out);
						editor.commit();
					}
				}
			} catch (final IOException e) {
				Log.e(TAG, "addBitmapToCache - " + e);
			} catch (IllegalStateException illegalStateException) {
				Log.e(TAG, "addBitmapToCache - " + illegalStateException);
			}
		}
	}

	/**
	 * Get from memory cache.
	 * 
	 * @param data Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromMemCache(String data) {
		if (mMemoryCache != null) {
			final Bitmap memBitmap = mMemoryCache.get(data);
			if (memBitmap != null) {
				if (BuildConfig.DEBUG) {
					Log.d(TAG, "Memory cache hit");
				}
				return memBitmap;
			}
		}
		return null;
	}

	/**
	 * Get from disk cache.
	 * 
	 * @param data Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromDiskCache(String data) {
		final String key = hashKeyForDisk(data);
		if (mDiskCache != null) {
			try {
				final DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
				if (snapshot != null) {
					while (mPauseDiskAccess) {
					}
					return BitmapFactory.decodeStream(snapshot.getInputStream(DISK_CACHE_INDEX));
				}
			} catch (final IOException e) {
				Log.e(TAG, "getBitmapFromDiskCache - " + e);
			}

		}
		return null;
	}

	public void close() {
		if (mDiskCache != null) {
			try {
				if (!mDiskCache.isClosed()) {
					// Should really close() here but need to synchronize up other methods that
					// access mDiskCache first.
					mDiskCache.flush();
				}
			} catch (IOException ignored) {
			}
		}
	}

	public void clearCaches() {
		try {
			if (mDiskCache != null) {
				mDiskCache.delete();
			}
		} catch (IOException e) {
			Log.e(TAG, "clearCaches() - " + e);
		}
		mMemoryCache.evictAll();
	}

	public void setPauseDiskCache(boolean pause) {
		mPauseDiskAccess = pause;
	}

	/**
	 * A holder class that contains cache parameters.
	 */
	public static class ImageCacheParams {
		public String uniqueName;
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public long diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean clearDiskCacheOnStart = DEFAULT_CLEAR_DISK_CACHE_ON_START;
		public String cacheFilenamePrefix = CACHE_FILENAME_PREFIX;
		public int memoryClass = 0;

		public ImageCacheParams(String uniqueName) {
			this.uniqueName = uniqueName;
		}

		public ImageCacheParams(Context context, String uniqueName) {
			this.uniqueName = uniqueName;
			final ActivityManager activityManager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
			memoryClass = activityManager.getMemoryClass();
			memCacheSize = memoryClass / DEFAULT_MEM_CACHE_DIVIDER * 1024 * 1024;
		}
	}

	/**
	 * Get the size in bytes of a bitmap.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public static int getBitmapSize(Bitmap bitmap) {
		if (UIUtils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 * 
	 * @param context The context to use
	 * @param uniqueName A unique directory name to append to the cache dir
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {

		// Check if media is mounted or storage is built-in, if so, try and use external cache dir
		// otherwise use internal cache dir
		String cachePath = "";
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !isExternalStorageRemovable()) {
			cachePath = getExternalCacheDir(context).getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 * otherwise.
	 */
	public static boolean isExternalStorageRemovable() {
		return Environment.isExternalStorageRemovable();
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context The context to use
	 * @return The external cache dir
	 */
	public static File getExternalCacheDir(Context context) {
		return context.getExternalCacheDir();
	}

	/**
	 * Check how much usable space is available at a given path.
	 * 
	 * @param path The path to check
	 * @return The space available in bytes
	 */
	public static long getUsableSpace(File path) {
		return path.getUsableSpace();
	}

	/**
	 * A hashing method that changes a string (like a URL) into a hash suitable
	 * for using as a disk filename.
	 */
	public static String hashKeyForDisk(String key) {
		String cacheKey;
		try {
			final MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
			mDigest.update(key.getBytes());
			cacheKey = bytesToHexString(mDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}

		return cacheKey;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * Locate an existing instance of this Fragment or if not found, create and
	 * add it using FragmentManager.
	 * 
	 * @param fm The FragmentManager manager to use.
	 * @return The existing instance of the Fragment or the new instance if just
	 * created.
	 */
	public static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
		// Check to see if we have retained the worker fragment.
		RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

		// If not retained (or first time running), we need to create and add
		// it.
		if (mRetainFragment == null) {
			mRetainFragment = new RetainFragment();
			fm.beginTransaction().add(mRetainFragment, TAG).commit();
		}

		return mRetainFragment;
	}

	/**
	 * A simple non-UI Fragment that stores a single Object and is retained over
	 * configuration changes. In this sample it will be used to retain the
	 * ImageCache object.
	 */
	public static class RetainFragment extends Fragment {
		private Object mObject;

		/**
		 * Empty constructor as per the Fragment documentation
		 */
		public RetainFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Make sure this Fragment is retained over a configuration change
			setRetainInstance(true);
		}

		/**
		 * Store a single object in this Fragment.
		 * 
		 * @param object The object to store
		 */
		public void setObject(Object object) {
			mObject = object;
		}

		/**
		 * Get the stored object.
		 * 
		 * @return The stored object
		 */
		public Object getObject() {
			return mObject;
		}
	}
}
