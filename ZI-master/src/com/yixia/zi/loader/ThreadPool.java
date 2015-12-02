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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
	private AtomicBoolean mStopped = new AtomicBoolean(Boolean.FALSE);
	private ThreadPoolExecutor mQueue;

	public ThreadPool() {
		mQueue = new ThreadPoolExecutor(10, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), sThreadFactory);
		mQueue.allowCoreThreadTimeOut(true);
	}

	public void start(Runnable run) {
		mQueue.execute(run);
	}

	public void stop() {
		if (!mStopped.get()) {
			mQueue.shutdownNow();
			mStopped.set(Boolean.TRUE);
		}
	}

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, "ThreadPool #" + mCount.getAndIncrement());
		}
	};
}
