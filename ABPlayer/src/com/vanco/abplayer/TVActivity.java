package com.vanco.abplayer;


import java.util.Timer;
import java.util.TimerTask;

import com.minisea.bilibli.R;
import com.vanco.abplayer.fragment.OnlineFragment;
//import com.vanco.abplayer.util.AdPublic;


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

public class TVActivity extends ActionBarActivity{
	private View backButton;
	private TextView titleText;
	private OnlineFragment online = new OnlineFragment();
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// todo something....
//				AdPublic.addAd(TVActivity.this);
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
	                    .add(R.id.container, online)
	                    .commit();
	        }
	        backButton = findViewById(R.id.logobutton);
			titleText = (TextView) findViewById(R.id.textViewTitle);
			titleText.setText("网络电视");
			backButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
//			AdPublic.addAd(TVActivity.this);
//			// 启动定时器
//	 	timer.schedule(task, 0, AdPublic.time);   
	    }


	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
	    @Override
	    public void onBackPressed() {
			if (online.onBackPressed())
				return;
			else
				super.onBackPressed();
	    }
}
