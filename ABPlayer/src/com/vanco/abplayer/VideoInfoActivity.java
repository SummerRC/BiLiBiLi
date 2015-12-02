package com.vanco.abplayer;

import com.minisea.bilibli.R;
import com.umeng.content.UmengAction;

import java.util.Timer;
import java.util.TimerTask;

import com.vanco.abplayer.fragment.VideoInfoFragment;
import com.vanco.abplayer.model.VideoItem;
import com.vanco.abplayer.util.ArrayUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VideoInfoActivity extends ActionBarActivity{
	private View backButton;
	private TextView titleText;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// todo something....
//				AdPublic.addAd(VideoInfoActivity.this);
			}
		}
	};
	private Timer timer = new Timer(true);
	private TimerTask task = new TimerTask() {
		public void run() {
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}
	};
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
		 requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_video_info);
	        if (savedInstanceState == null) {
	            getSupportFragmentManager().beginTransaction()
	                    .add(R.id.container, new VideoInfoFragment())
	                    .commit();
	        }
	        backButton = findViewById(R.id.logobutton);
			titleText = (TextView) findViewById(R.id.textViewTitle);
			titleText.setText("视频详情");
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			UmengAction.initTimer(this,  ArrayUtils.getTimerForAd());
//			AdPublic.addAd(VideoInfoActivity.this);
//			// 启动定时器
//	 	timer.schedule(task, 0, AdPublic.time);        
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

}
