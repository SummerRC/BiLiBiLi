/*
 * Copyright (C) 2013 yixia.com
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

package io.vov.vitamio.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoViewDemo extends Activity {

	/**
	 * TODO: Set the path variable to a streaming video URL or a local media file
	 * path.
	 * http://us-chicago.acgvideo.com/67.159.54.58/1/45/3262388-1.flv?expires=1430688900&ssig=VuoBrnO8dUUG4jyJe3FF2Q&o=401361084&rate=0
	 * /storage/sdcard0/BaiduNetdisk/我的资源/5/33.rmvb
	 */
	private String path = "http://us-chicago.acgvideo.com/67.159.54.58/1/45/3262388-1.flv?expires=1430688900&ssig=VuoBrnO8dUUG4jyJe3FF2Q&o=401361084&rate=0";
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!LibsChecker.checkVitamioLibs(this))
			return;

		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏

		setContentView(R.layout.videoview);
		mVideoView = (VideoView) findViewById(R.id.surface_view);

		if (path == "") {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(VideoViewDemo.this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
			return;
		} else {

			/*
			 * Alternatively,for streaming media you can use
			 * mVideoView.setVideoURI(Uri.parse(URLstring));
			 */
			mVideoView.setVideoPath(path);
			mVideoView.setMediaController(new MediaController(this));
			mVideoView.requestFocus();

		}
	}
}
