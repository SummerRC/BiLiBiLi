package com.yixia.zi.provider;


import java.util.HashMap;
import java.util.Map;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Session {
	private static final String TAG = "VPlayer[Session]";
	private ContentResolver mResolver;
	private static final String[] PROJECTION_KV = new String[] { SessionProvider.COL_KEY, SessionProvider.COL_VALUE };
	private static final String[] PROJECTION_VALUE = new String[] { SessionProvider.COL_VALUE };
	private static final String[] PROJECTION_ID = new String[] { SessionProvider.COL_ID };

	public Session(Context ctx) {
		mResolver = ctx.getContentResolver();
	}

	public boolean contains(String key) {
		Cursor c = null;
		try {
			c = mResolver.query(SessionProvider.CONTENT_URI, PROJECTION_VALUE, "key like ?", new String[] { key }, null);
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

	public float getFloat(String key, float defaultValue) {
		try {
			String val = getString(key, String.valueOf(defaultValue));
			return Float.parseFloat(val);
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
			c = mResolver.query(SessionProvider.CONTENT_URI, PROJECTION_VALUE, "key like ?", new String[] { key }, null);
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
			c = mResolver.query(SessionProvider.CONTENT_URI, PROJECTION_KV, null, null, null);
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
		return mResolver.delete(SessionProvider.CONTENT_URI, "key like ?", new String[] { key });
	}

	private void putValue(String key, Object value) {
		Cursor c = null;
		try {
			ContentValues cv = new ContentValues();
			cv.put(SessionProvider.COL_KEY, key);
			cv.put(SessionProvider.COL_VALUE, String.valueOf(value));
			c = mResolver.query(SessionProvider.CONTENT_URI, PROJECTION_ID, "key like ?", new String[] { key }, null);
			if (c != null && c.moveToFirst()) {
				mResolver.update(ContentUris.withAppendedId(SessionProvider.CONTENT_URI, c.getInt(0)), cv, null, null);
			} else {
				mResolver.insert(SessionProvider.CONTENT_URI, cv);
			}
		} catch (Exception e) {
			Log.e(TAG, "putValue(" + key + "," + value + ")", e);
		} finally {
			if (c != null)
				c.close();
		}
	}

}
