package com.yixia.zi.utils;

import android.os.Handler;

import java.lang.ref.WeakReference;

public abstract class WeakHandler<T> extends Handler {
	private WeakReference<T> mOwner;

	public WeakHandler(T owner) {
		mOwner = new WeakReference<T>(owner);
	}

	public T getOwner() {
		return mOwner.get();
	}
}
