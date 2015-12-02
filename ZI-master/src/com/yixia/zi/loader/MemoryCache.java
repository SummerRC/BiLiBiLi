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
package com.yixia.zi.loader;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;

public class MemoryCache<T> {
	private HashMap<T, SoftReference<Bitmap>> cache = new HashMap<T, SoftReference<Bitmap>>();

	public Bitmap get(T id) {
		if (!cache.containsKey(id))
			return null;
		SoftReference<Bitmap> ref = cache.get(id);
		return ref.get();
	}

	public void put(T id, Bitmap bitmap) {
		cache.put(id, new SoftReference<Bitmap>(bitmap));
	}

	public void clear() {
		cache.clear();
	}
}