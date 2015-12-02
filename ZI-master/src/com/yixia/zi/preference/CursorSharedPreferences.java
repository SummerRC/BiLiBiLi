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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

import com.yixia.zi.preference.APreference;


public class CursorSharedPreferences implements SharedPreferences {
	private Context mContext;
	private APreference aP;
	private List<OnSharedPreferenceChangeListener> mListeners = new LinkedList<OnSharedPreferenceChangeListener>();

	public CursorSharedPreferences(Context ctx) {
		mContext = ctx;
		aP = new APreference(mContext);
	}

	@Override
	public Map<String, ?> getAll() {
		return aP.getAll();
	}

	@Override
	public String getString(String key, String defValue) {
		return aP.getString(key, defValue);
	}

	@Override
	public int getInt(String key, int defValue) {
		return aP.getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		return aP.getInt(key, (int) defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		return (float) aP.getDouble(key, defValue);
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		return aP.getBoolean(key, defValue);
	}

	@Override
	public boolean contains(String key) {
		return aP.contains(key);
	}

	@Override
	public Editor edit() {
		return new Editor();
	}

	@Override
	public Set<String> getStringSet(String arg0, Set<String> arg1) {
		return null;
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		mListeners.add(listener);
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
		mListeners.remove(listener);
	}

	public class Editor implements SharedPreferences.Editor {
		private Map<String, Object> mValues = new HashMap<String, Object>();

		@Override
		public android.content.SharedPreferences.Editor putString(String key, String value) {
			mValues.put(key, value);
			return this;
		}

		@Override
		public android.content.SharedPreferences.Editor putInt(String key, int value) {
			mValues.put(key, value);
			return this;
		}

		@Override
		public android.content.SharedPreferences.Editor putLong(String key, long value) {
			mValues.put(key, value);
			return this;
		}

		@Override
		public android.content.SharedPreferences.Editor putFloat(String key, float value) {
			mValues.put(key, value);
			return this;
		}

		@Override
		public android.content.SharedPreferences.Editor putBoolean(String key, boolean value) {
			mValues.put(key, value);
			return this;
		}

		@Override
		public android.content.SharedPreferences.Editor clear() {
			mValues.clear();
			return this;
		}

		@Override
		public boolean commit() {
			for (String k : mValues.keySet()) {
				Object value = mValues.get(k);
				aP.put(k, value != null ? value.toString() : null);
			}
			for (OnSharedPreferenceChangeListener listener : mListeners)
				listener.onSharedPreferenceChanged(CursorSharedPreferences.this, null);
			return true;
		}

		@Override
		public void apply() {
			commit();
		}

		@Override
		public android.content.SharedPreferences.Editor putStringSet(String arg0, Set<String> arg1) {
			throw new UnsupportedOperationException("CursorPreference do not support Set<String>");
		}

		@Override
		public android.content.SharedPreferences.Editor remove(String key) {
			mValues.remove(key);
			return this;
		}

	}
}
