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

package com.yixia.zi.widget.colorpicker;

import android.app.Dialog;
import android.content.Context;

import com.yixia.zi.R;

public class ColorPickerDialog extends Dialog {
	private ColorPickerView mPicker;

	public ColorPickerDialog(Context context, int theme, int initColor, OnColorChangedListener l) {
		super(context, theme);

		setContentView(R.layout.colorpicker);
		mChangedListener = l;

		mPicker = (ColorPickerView) findViewById(R.id.color_picker_view);
		mPicker.setColor(initColor);
		mPicker.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {
			@Override
			public void onColorChanged(int color) {
				if (mChangedListener != null)
					mChangedListener.onColorChanged(color);
			}
		});
		setCanceledOnTouchOutside(true);
	}

	private OnColorChangedListener mChangedListener;

	public static interface OnColorChangedListener {
		public void onColorChanged(int color);
	}
}
