package com.vanco.abplayer;

import com.minisea.bilibli.R;
import com.umeng.content.UmengAction;

import java.util.Timer;
import java.util.TimerTask;

import com.vanco.abplayer.adapter.MainTabAdapter;
import com.vanco.abplayer.util.ArrayUtils;
import com.viewpagerindicator.PageIndicator;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends ActionBarActivity 
{
	private Timer timer = new Timer(true);
	
	private Handler handler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			super.handleMessage(msg);
			if (msg.what == 1) {
				// todo something....
//				AdPublic.addAd(MainActivity.this);
			}
		}
	};
	
	private TimerTask task = new TimerTask()
	{
		public void run() 
		{
			Message msg = new Message();
			msg.what = 1;
			handler.sendMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FragmentPagerAdapter adapter = new MainTabAdapter(getSupportFragmentManager());
		// 视图切换器
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);
		// 页面指示器
		PageIndicator indicator = (PageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		UmengAction.initTimer(this,  ArrayUtils.getTimerForAd());
//		ad();
//		AdPublic.addAd(MainActivity.this);
//		// 启动定时器
//		timer.schedule(task, 0, AdPublic.time);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * On key up.
	 * 
	 * @param keyCode
	 *            the key code
	 * @param event
	 *            the event
	 * @return true, if successful
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			show_existDialog();
			return false;
		} else {
			return true;
		}
	}

	private void show_existDialog() {
		// 弹出退出对话框
		Builder dialog = new AlertDialog.Builder(MainActivity.this)
				.setMessage("您确定要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// 退出程序
						finish();
					}
				})
				.setNegativeButton("支持我",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {/*
																				 * SpotManager
																				 * .
																				 * getInstance
																				 * (
																				 * MainActivity
																				 * .
																				 * this
																				 * )
																				 * .
																				 * showSpotAds
																				 * (
																				 * MainActivity
																				 * .
																				 * this
																				 * ,
																				 * new
																				 * SpotDialogListener
																				 * (
																				 * )
																				 * {
																				 * 
																				 * @
																				 * Override
																				 * public
																				 * void
																				 * onShowSuccess
																				 * (
																				 * )
																				 * {
																				 * Log
																				 * .
																				 * i
																				 * (
																				 * "YoumiAdDemo"
																				 * ,
																				 * "展示成功"
																				 * )
																				 * ;
																				 * }
																				 * 
																				 * @
																				 * Override
																				 * public
																				 * void
																				 * onShowFailed
																				 * (
																				 * )
																				 * {
																				 * Log
																				 * .
																				 * i
																				 * (
																				 * "YoumiAdDemo"
																				 * ,
																				 * "展示失败"
																				 * )
																				 * ;
																				 * }
																				 * 
																				 * @
																				 * Override
																				 * public
																				 * void
																				 * onSpotClosed
																				 * (
																				 * )
																				 * {
																				 * Log
																				 * .
																				 * i
																				 * (
																				 * "YoumiAdDemo"
																				 * ,
																				 * "展示关闭"
																				 * )
																				 * ;
																				 * }
																				 * 
																				 * }
																				 * )
																				 * ;
																				 */
							}
						});
		dialog.show();
	}

	public void onBackPressed() {
		// 如果有需要，可以点击后退关闭插播广告。
		// if (!SpotManager.getInstance(this).disMiss()) {
		// // 弹出退出窗口，可以使用自定义退屏弹出和回退动画,参照demo,若不使用动画，传入-1
		// super.onBackPressed();
		// }
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		UmengAction.initTimer(this,  ArrayUtils.getTimerForAd());
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		// SpotManager.getInstance(this).onStop();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// SpotManager.getInstance(this).onDestroy();
		super.onDestroy();
	}

}
