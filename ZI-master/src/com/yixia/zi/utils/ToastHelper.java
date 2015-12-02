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
package com.yixia.zi.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.yixia.zi.R;

public class ToastHelper {
	public static void showToast(Context ctx, int resID) {
		showToast(ctx, Toast.LENGTH_SHORT, resID);
	}

	public static void showToast(Context ctx, String text) {
		showToast(ctx, Toast.LENGTH_SHORT, text);
	}

	public static void showToast(Context ctx, int duration, int resID) {
		showToast(ctx, duration, ctx.getString(resID));
	}

	public static void showToast(Context ctx, int duration, String text) {
		Toast toast = Toast.makeText(ctx, text, duration);
		View mNextView = toast.getView();
		if (mNextView != null)
			mNextView.setBackgroundResource(R.drawable.toast_frame);
		toast.show();
	}
}
