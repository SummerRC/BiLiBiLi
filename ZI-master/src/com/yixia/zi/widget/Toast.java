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

import com.yixia.zi.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

public class Toast {
	public static int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;
	public static int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;

	public static void showText(Context context, CharSequence text, int duration) {
		android.widget.Toast t = android.widget.Toast.makeText(context, text, duration);
		View v = t.getView();
		if (v != null)
			v.setBackgroundResource(R.drawable.toast_frame);
		t.show();
	}

	public static void showText(Context context, int resId, int duration) throws Resources.NotFoundException {
		showText(context, context.getResources().getText(resId), duration);
	}
}
