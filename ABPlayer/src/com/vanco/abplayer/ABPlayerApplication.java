package com.vanco.abplayer;

import com.vanco.abplayer.view.FileUtils;

import android.app.Application;
import android.content.Context;
import android.os.Environment;


public class ABPlayerApplication extends Application{
	private static ABPlayerApplication mApplication;

	/** OPlayer SD卡缓存路径 */
	public static final String OPLAYER_CACHE_BASE = Environment.getExternalStorageDirectory() + "/oplayer";
	/** 视频截图缓冲路径 */
	public static final String OPLAYER_VIDEO_THUMB = OPLAYER_CACHE_BASE + "/thumb/";
	/** 首次扫描 */
	public static final String PREF_KEY_FIRST = "application_first";

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;

		init();
	}

	private void init() {
		//创建缓存目录
		FileUtils.createIfNoExists(OPLAYER_CACHE_BASE);
		FileUtils.createIfNoExists(OPLAYER_VIDEO_THUMB);
	}

	public static ABPlayerApplication getApplication() {
		return mApplication;
	}

	public static Context getContext() {
		return mApplication;
	}

	/** 销毁 */
	public void destory() {
		mApplication = null;
	}

}
