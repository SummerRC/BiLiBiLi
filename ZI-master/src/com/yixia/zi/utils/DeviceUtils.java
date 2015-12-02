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

import java.lang.reflect.Method;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;

public class DeviceUtils {
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Activity ctx) {
		int width;
		Display display = ctx.getWindowManager().getDefaultDisplay();
		try {
			Method mGetRawW = Display.class.getMethod("getRawWidth");
			width = (Integer) mGetRawW.invoke(display);
		} catch (Exception e) {
			width = display.getWidth();
		}
		return width;
	}

	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Activity ctx) {
		int height;
		Display display = ctx.getWindowManager().getDefaultDisplay();
		try {
			Method mGetRawH = Display.class.getMethod("getRawHeight");
			height = (Integer) mGetRawH.invoke(display);
		} catch (Exception e) {
			height = display.getHeight();
		}
		return height;
	}

	public static double getScreenPhysicalSize(Activity ctx) {
		DisplayMetrics dm = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
		return diagonalPixels / (160 * dm.density);
	}
}
