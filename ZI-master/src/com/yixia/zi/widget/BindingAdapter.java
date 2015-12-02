package com.yixia.zi.widget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An implementation of {@link BaseAdapter} which uses the new/bind pattern for
 * its views.
 */
public abstract class BindingAdapter extends BaseAdapter {
	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		int type = getItemViewType(position);
		if (convertView == null) {
			convertView = newView(type, parent);
		}
		bindView(position, type, convertView);
		return convertView;
	}

	/** Create a new instance of a view for the specified {@code type}. */
	public abstract View newView(int type, ViewGroup parent);

	/** Bind the data for the specified {@code position} to the {@code view}. */
	public abstract void bindView(int position, int type, View view);
}