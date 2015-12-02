package com.yixia.zi.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class ProcessHelper {
	public static boolean isProcessRunning(Context ctx, String name) {
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
		for (RunningAppProcessInfo app : apps) {
			if (app.processName.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
