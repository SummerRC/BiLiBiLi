package com.vanco.abplayer;

import java.util.Timer;
import java.util.TimerTask;

import com.minisea.bilibli.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class BiliWebviewActivity extends Activity{
	private ProgressBar progressBar; // 进度条
	private ImageView reLoadImageView; // 重新加载的图片
	private WebView biliWebView;//网页控件

	private ImageView backBtn; // 回退按钮
	private ImageView commentBtn; // 评论按钮

	public static String url;
	private String filename;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// todo something....
//				AdPublic.addAd(BiliWebviewActivity.this);
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bili_webview);

		init();
		initComponent();
		biliWebView.getSettings().setJavaScriptEnabled(true);
		biliWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}
		});
		biliWebView.loadUrl(url);
//		AdPublic.addAd(BiliWebviewActivity.this);
//		// 启动定时器
// 	timer.schedule(task, 0, AdPublic.time);
	}
	

	// 初始化
	private void init() {
		url = getIntent().getExtras().getString("bannerLink");
		filename = url.substring(url.lastIndexOf("/") + 1);
		System.out.println("filename--->" + filename);
	}

	// 初始化组件
	private void initComponent() {
		biliWebView = (WebView)findViewById(R.id.BiliWebView);
		progressBar = (ProgressBar) findViewById(R.id.blogContentPro);
		reLoadImageView = (ImageView) findViewById(R.id.reLoadImage);
		reLoadImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				reLoadImageView.setVisibility(View.INVISIBLE);
				progressBar.setVisibility(View.VISIBLE);

			}
		});

		backBtn = (ImageView) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		commentBtn = (ImageView) findViewById(R.id.comment);
		

	}

}
