package com.vanco.abplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import com.minisea.bilibli.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vanco.abplayer.VideoViewActivity;
import com.vanco.abplayer.adapter.ArrayAdapter;
import com.vanco.abplayer.model.OnlineVideo;
import com.vanco.abplayer.util.XmlReaderHelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class OnlineFragment extends Fragment implements OnItemClickListener {

	/** 缓存视频列表 */
	private static ArrayList<String[]> mOnlineList = new ArrayList<String[]>();
	/** 缓存视频LOGO列表 */
	private static ArrayList<Integer> mOnlineLogoList = new ArrayList<Integer>();
	
	private ListView mListView;
	/** 历史记录 */
	private List<String> mHistory = new ArrayList<String>();
	/** 显示当前正在加载的url */
	private TextView mUrl;
	private String mTitle;
	private ArrayList<OnlineVideo> tvs;
	private int level = 1;
	private DataAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tvs = XmlReaderHelper.getAllCategory(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)  {
		View mView = inflater.inflate(R.layout.fragment_online, container,false);
        // 实例化广告条
//        AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);
//        // 获取要嵌入广告条的布局
//        LinearLayout adLayout=(LinearLayout)mView.findViewById(R.id.adLayout);
//        // 将广告条加入到布局中
//        adLayout.addView(adView);
		mListView = (ListView) mView.findViewById(android.R.id.list);
		mListView.setOnItemClickListener(this);
		mAdapter = new DataAdapter(getActivity(),tvs);
		mListView.setAdapter(mAdapter);
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		final OnlineVideo item = mAdapter.getItem(position);
		switch (level) {
		case 1:// 顶级
			level = 2;
			mAdapter.replace(XmlReaderHelper.getVideos(getActivity(),item.id));
			mListView.setAdapter(mAdapter);
			break;
		case 2:
			// clearAndLoad(item.url);
			Intent intent = new Intent(getActivity(), VideoViewActivity.class);
			intent.setData(Uri.parse(item.url));
		    //intent.putExtra("path", item.url);
			intent.putExtra("displayName",item.title);
			startActivity(intent);
			break;
		}
	}
	
	public boolean onBackPressed() {
		switch (level) {
		case 1:
			return false;
		case 2://
			level = 1;
			mAdapter.replace(tvs);
			break;
		}
		mListView.setAdapter(mAdapter);
		return true;
	}

	/** 数据适配 */
	private class DataAdapter extends ArrayAdapter<OnlineVideo> {
		private ImageLoader imageLoader = ImageLoader.getInstance();// 得到图片加载器
		private DisplayImageOptions options; // 显示图像设置

		public DataAdapter(Context ctx,ArrayList<OnlineVideo> root) {
			super(ctx, root);
			// 图片加载器初始化
			imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
			// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
			options = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.ica_light_ic_action_important) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.ica_light_ic_action_important) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.ica_light_ic_action_important) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory() // 设置下载的图片是否缓存在内存中
					.cacheOnDisc() // 设置下载的图片是否缓存在SD卡中
					.build(); // 创建配置过得DisplayImageOption对象
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final OnlineVideo item = getItem(position);
			if (convertView == null) {
				final LayoutInflater mInflater = getActivity()
						.getLayoutInflater();
				convertView = mInflater.inflate(R.layout.fragment_online_item,
						null);
			}
			ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
			if (item.iconId > 0)
				thumbnail.setImageResource(item.iconId);
			else
			{
				thumbnail.setImageDrawable(null);
				// 异步加载图片
				imageLoader.displayImage(item.icon_url, thumbnail, options);
			}	
			((TextView) convertView.findViewById(R.id.title))
					.setText(item.title);

			return convertView;
		}

	}


	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		}
}

/*
 * private boolean loadVideo(final String url) { if (StringUtils.isEmpty(url))
 * return false;
 * 
 * mCurrentUrl = url;
 * 
 * new AsyncTask<Void, Void, OnlineVideo>() {
 * 
 * @Override protected OnlineVideo doInBackground(Void... params) {
 * Log.d("Youku", url); if (url.startsWith("http://m.youku.com")) { return
 * VideoHelper.getYoukuVideo(url); } return null; }
 * 
 * @Override protected void onPostExecute(OnlineVideo result) {
 * super.onPostExecute(result); if (result != null) { Intent intent = new
 * Intent(getActivity(), VideoPlayerActivity.class); intent.putExtra("path",
 * result.url); intent.putExtra("title", result.title); startActivity(intent); }
 * else { mWebView.loadUrl(url); } } }.execute(); return true; }
 */
