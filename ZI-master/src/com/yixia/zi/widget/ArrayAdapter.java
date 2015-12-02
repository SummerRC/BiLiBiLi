/*
 * Copyright (C) 2012 YIXIA.COM
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
package com.yixia.zi.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class ArrayAdapter<T> extends BaseAdapter {

	protected ArrayList<T> mObjects;
	protected LayoutInflater mInflater;

	public ArrayAdapter(final Context ctx, final ArrayList<T> l) {
		mObjects = l == null ? new ArrayList<T>() : l;
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayAdapter(final Context ctx, final T... l) {
		mObjects = new ArrayList<T>();
		mObjects.addAll(Arrays.asList(l));
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public ArrayAdapter(final Context ctx, final List<T> l) {
		mObjects = new ArrayList<T>();
		if (l != null)
			mObjects.addAll(l);
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public ArrayAdapter(final Context ctx, final Collection<T> l) {
		mObjects = new ArrayList<T>();
		if (l != null)
			mObjects.addAll(l);
		mInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public T getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void add(T item) {
		this.mObjects.add(item);
	}

	public void addAll(T... items) {
		ArrayList<T> values = this.mObjects;
		for (T item : items) {
			values.add(item);
		}
		this.mObjects = values;
	}

	public void addAll(Collection<? extends T> collection) {
		mObjects.addAll(collection);
	}

	public void clear() {
		mObjects.clear();
	}

	public final ArrayList<T> getAll() {
		return mObjects;
	}
}
