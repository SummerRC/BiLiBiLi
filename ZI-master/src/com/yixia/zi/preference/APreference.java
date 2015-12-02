/*
 * Copyright (C) 2011 Cedric Fung (wolfplanet@gmail.com)
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

package com.yixia.zi.preference;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class APreference {
	private static final String TAG = "APreference";
	private ContentResolver mResolver;
	private ContentObserver mObserver;
	private static final String[] PROJECTION_KV = new String[] { PreferenceProvider.COL_KEY, PreferenceProvider.COL_VALUE };
	private static final String[] PROJECTION_VALUE = new String[] { PreferenceProvider.COL_VALUE };
	private static final String[] PROJECTION_ID = new String[] { PreferenceProvider.COL_ID };

	public APreference(Context ctx) {
		mResolver = ctx.getContentResolver();
		mObserver = new PreferenceContentObserver(new Handler());
	}

	public void registerObserver(OnPreferenceChangedListener l) {
		mOnPreferenceChangedListener = l;
		mResolver.registerContentObserver(PreferenceProvider.CONTENT_URI, true, mObserver);
	}
	
	public void registerObserver(Uri uri, OnPreferenceChangedListener l) {
		mOnPreferenceChangedListener = l;
		mResolver.registerContentObserver(uri, true, mObserver);
	}

	public void unregisterObserver() {
		mResolver.unregisterContentObserver(mObserver);
	}

	public boolean contains(String key) {
		Cursor c = null;
		try {
			c = mResolver.query(Uri.withAppendedPath(PreferenceProvider.CONTENT_URI, key), PROJECTION_VALUE, null, null, null);
			if (c != null && c.moveToFirst())
				return true;
			return false;
		} catch (Exception e) {
			Log.e(TAG, "contains(" + key + ")", e);
			return false;
		} finally {
			if (c != null)
				c.close();
		}
	}

	public void put(String key, boolean value) {
		putValue(key, value);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		try {
			String val = getString(key, String.valueOf(defaultValue));
			return Boolean.parseBoolean(val);
		} catch (Exception e) {
			Log.e(TAG, "getBoolean(" + key + "," + defaultValue + ")", e);
			return defaultValue;
		}
	}

	public void put(String key, long value) {
		putValue(key, value);
	}

	public long getLong(String key, long defaultValue) {
		try {
			String val = getString(key, String.valueOf(defaultValue));
			return Long.parseLong(val);
		} catch (Exception e) {
			Log.e(TAG, "getInt(" + key + "," + defaultValue + ")", e);
			return defaultValue;
		}
	}

	public void put(String key, int value) {
		putValue(key, value);
	}

	public int getInt(String key, int defaultValue) {
		try {
			String val = getString(key, String.valueOf(defaultValue));
			return Integer.parseInt(val);
		} catch (Exception e) {
			Log.e(TAG, "getInt(" + key + "," + defaultValue + ")", e);
			return defaultValue;
		}
	}

	public void put(String key, double value) {
		putValue(key, value);
	}

	public double getDouble(String key, double defaultValue) {
		try {
			String val = getString(key, String.valueOf(defaultValue));
			return Double.parseDouble(val);
		} catch (Exception e) {
			Log.e(TAG, "getDouble(" + key + "," + defaultValue + ")", e);
			return defaultValue;
		}
	}

	public void put(String key, String value) {
		putValue(key, value);
	}

	public String getString(String key, String defaultValue) {
		Cursor c = null;
		try {
			c = mResolver.query(Uri.withAppendedPath(PreferenceProvider.CONTENT_URI, key), PROJECTION_VALUE, null, null, null);
			if (c != null && c.moveToFirst())
				return c.getString(0);
			return defaultValue;
		} catch (Exception e) {
			Log.e(TAG, "getString(" + key + "," + defaultValue + ")", e);
			return defaultValue;
		} finally {
			if (c != null)
				c.close();
		}
	}

	public Map<String, ?> getAll() {
		Cursor c = null;
		Map<String, String> map = new HashMap<String, String>();
		try {
			c = mResolver.query(PreferenceProvider.CONTENT_URI, PROJECTION_KV, null, null, null);
			if (c != null && c.moveToFirst()) {
				do {
					map.put(c.getString(0), c.getString(1));
				} while (c.moveToNext());
				return map;
			}
			return null;
		} catch (Exception e) {
			Log.e(TAG, "getAll()", e);
			return null;
		} finally {
			if (c != null)
				c.close();
		}
	}

	public int remove(String key) {
		return mResolver.delete(Uri.withAppendedPath(PreferenceProvider.CONTENT_URI, key), null, null);
	}

	public int clear() {
		return mResolver.delete(PreferenceProvider.CONTENT_URI, null, null);
	}

	private void putValue(String key, Object value) {
		Cursor c = null;
		try {
			ContentValues cv = new ContentValues();
			cv.put(PreferenceProvider.COL_KEY, key);
			cv.put(PreferenceProvider.COL_VALUE, String.valueOf(value));
			c = mResolver.query(Uri.withAppendedPath(PreferenceProvider.CONTENT_URI, key), PROJECTION_ID, null, null, null);
			if (c != null && c.moveToFirst()) {
				mResolver.update(Uri.withAppendedPath(PreferenceProvider.CONTENT_URI, key), cv, null, null);
			} else {
				mResolver.insert(PreferenceProvider.CONTENT_URI, cv);
			}
		} catch (Exception e) {
			Log.e(TAG, "putValue(" + key + "," + value + ")", e);
		} finally {
			if (c != null)
				c.close();
		}
	}

	private class PreferenceContentObserver extends ContentObserver {
		public PreferenceContentObserver(Handler h) {
			super(h);
		}

		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			mOnPreferenceChangedListener.onPreferenceChanged();
		}
	}

	public static interface OnPreferenceChangedListener {
		public void onPreferenceChanged();
	}

	private OnPreferenceChangedListener mOnPreferenceChangedListener;

}
