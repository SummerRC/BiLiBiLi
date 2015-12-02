package com.vanco.abplayer;

import com.minisea.bilibli.R;
import com.umeng.content.UmengAction;
import com.vanco.abplayer.adapter.BankumiTabAdapter;
import com.vanco.abplayer.adapter.DianyingTabAdapter;
import com.vanco.abplayer.adapter.DonghuaTabAdapter;
import com.vanco.abplayer.adapter.KejiTabAdapter;
import com.vanco.abplayer.adapter.MusicTabAdapter;
import com.vanco.abplayer.adapter.RankAdapter;
import com.vanco.abplayer.adapter.YouxiTabAdapter;
import com.vanco.abplayer.adapter.YuleTabAdapter;
import com.vanco.abplayer.util.ArrayUtils;
import com.viewpagerindicator.PageIndicator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 动画界面
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class DonghuaActivity extends ActionBarActivity {
	public int mAreaType;
	private View backButton;
	private TextView titleText;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donghua_tab);
		backButton = findViewById(R.id.logobutton);
		titleText = (TextView) findViewById(R.id.textViewTitle);
		FragmentPagerAdapter adapter = null;
		mAreaType = getIntent().getIntExtra("AreaType", 1);
		Log.d("QAQ","----->"+mAreaType+titleText.getText());
		switch (mAreaType){
		case 1:
			titleText.setText("番剧");
			adapter = new BankumiTabAdapter(getSupportFragmentManager());
			break;
		case 2:
			titleText.setText("动画");
			adapter = new DonghuaTabAdapter(getSupportFragmentManager());
			break;
		case 3:
			titleText.setText("音乐");
			adapter = new MusicTabAdapter(getSupportFragmentManager());
			break;
		case 4:
			titleText.setText("游戏");
			adapter = new YouxiTabAdapter(getSupportFragmentManager());
			break;
		case 5:
			titleText.setText("科学·技术");
			adapter = new KejiTabAdapter(getSupportFragmentManager());
			break;
		case 6:
			titleText.setText("娱乐");
			adapter = new YuleTabAdapter(getSupportFragmentManager());
			break;
		case 7:
			titleText.setText("电影");
			adapter = new DianyingTabAdapter(getSupportFragmentManager());
			break;
		case 8:
			titleText.setText("排行榜");
			adapter = new RankAdapter(getSupportFragmentManager());
			break;
		default:
			titleText.setText("电影");
			adapter = new DianyingTabAdapter(getSupportFragmentManager());
			break;
		}

		// 视图切换器
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		pager.setOffscreenPageLimit(1);
		pager.setAdapter(adapter);

		// 页面指示器
		PageIndicator indicator = (PageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
