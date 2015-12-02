package com.vanco.abplayer.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minisea.bilibli.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.vanco.abplayer.BiliWebviewActivity;
import com.vanco.abplayer.model.BannerItem;
import com.vanco.abplayer.model.VideoItem;
import com.vanco.abplayer.util.HttpUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class HomePageFragment extends Fragment{
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
	public View rootView;
	private ViewPager adViewPager;
	private List<ImageView> imageViews;// 滑动的图片集合

	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;
	
	private int currentItem = 0; // 当前图片的索引号
	// 定义的五个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	private View dot5;
	//新番区控件
	ImageView bangumiImageView01;
	TextView bagumiTextView_title01;
	TextView bagumiTextView01_baofang;
	TextView bagumiTextView01_danmugu;
	
	ImageView bangumiImageView02;
	TextView bagumiTextView_title02;
	TextView bagumiTextView02_baofang;
	TextView bagumiTextView02_danmugu;
	
	ImageView bangumiImageView03;
	TextView bagumiTextView_title03;
	TextView bagumiTextView03_baofang;
	TextView bagumiTextView03_danmugu;
	
	ImageView bangumiImageView04;
	TextView bagumiTextView_title04;
	TextView bagumiTextView04_baofang;
	TextView bagumiTextView04_danmugu;
	
	private List<VideoItem> videoItemList;

	private ScheduledExecutorService scheduledExecutorService;

	// 异步加载图片
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	// 轮播banner的数据
	private List<BannerItem> adList;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			adViewPager.setCurrentItem(currentItem);
		};
	};
	
	
    public HomePageFragment() {
    	
    }
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		initImageLoader();
		// 获取图片加载实例
		mImageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.top_banner_android)
				.showImageForEmptyUri(R.drawable.top_banner_android)
				.showImageOnFail(R.drawable.top_banner_android)
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).build();


	}

	private void initImageLoader() {
		File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
				.getOwnCacheDirectory(this.getActivity().getApplicationContext(),
						IMAGE_CACHE_PATH);

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this.getActivity()).defaultDisplayImageOptions(defaultOptions)
				.memoryCache(new LruMemoryCache(12 * 1024 * 1024))
				.memoryCacheSize(12 * 1024 * 1024)
				.discCacheSize(32 * 1024 * 1024).discCacheFileCount(100)
				.discCache(new UnlimitedDiscCache(cacheDir))
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.tasksProcessingOrder(QueueProcessingType.LIFO).build();

		ImageLoader.getInstance().init(config);
	}
	
	private void initAdData() {
		// 广告数据
		//adList = getBannerAd();

		imageViews = new ArrayList<ImageView>();
		

		// 点
		dots = new ArrayList<View>();
		dotList = new ArrayList<View>();
		dot0 = rootView.findViewById(R.id.v_dot0);
		dot1 = rootView.findViewById(R.id.v_dot1);
		dot2 = rootView.findViewById(R.id.v_dot2);
		dot3 = rootView.findViewById(R.id.v_dot3);
		dot4 = rootView.findViewById(R.id.v_dot4);		
		dot5 = rootView.findViewById(R.id.v_dot5);		
		dots.add(dot0);
		dots.add(dot1);
		dots.add(dot2);
		dots.add(dot3);
		dots.add(dot4);
		dots.add(dot5);
		addDynamicView();

		adViewPager = (ViewPager) rootView.findViewById(R.id.vp);
		adViewPager.setAdapter(new HomePageADAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		adViewPager.setOnPageChangeListener(new HomePageADChangeListener());
		
	}
	
	private void initBangumiData() {
		bangumiImageView01 = (ImageView) rootView.findViewById(R.id.BangumiImageView01);
		bagumiTextView_title01 = (TextView) rootView.findViewById(R.id.BangumiTextView01);
		bagumiTextView01_baofang = (TextView) rootView.findViewById(R.id.BangumiImageView01_baofang);
		bagumiTextView01_danmugu = (TextView) rootView.findViewById(R.id.BangumiImageView01_danmugu);
		
		bangumiImageView02 = (ImageView) rootView.findViewById(R.id.BangumiImageView02);
		bagumiTextView_title02 = (TextView) rootView.findViewById(R.id.BangumiTextView02);
		bagumiTextView02_baofang = (TextView) rootView.findViewById(R.id.BangumiImageView02_baofang);
		bagumiTextView02_danmugu = (TextView) rootView.findViewById(R.id.BangumiImageView02_danmugu);
		
		bangumiImageView03 = (ImageView) rootView.findViewById(R.id.BangumiImageView03);
		bagumiTextView_title03 = (TextView) rootView.findViewById(R.id.BangumiTextView03);
		bagumiTextView03_baofang = (TextView) rootView.findViewById(R.id.BangumiImageView03_baofang);
		bagumiTextView03_danmugu = (TextView) rootView.findViewById(R.id.BangumiImageView03_danmugu);
		
		bangumiImageView04 = (ImageView) rootView.findViewById(R.id.BangumiImageView04);
		bagumiTextView_title04 = (TextView) rootView.findViewById(R.id.BangumiTextView04);
		bagumiTextView04_baofang = (TextView) rootView.findViewById(R.id.BangumiImageView04_baofang);
		bagumiTextView04_danmugu = (TextView) rootView.findViewById(R.id.BangumiImageView04_danmugu);
		
		mImageLoader.displayImage(videoItemList.get(0).getPic(), bangumiImageView01,options);
		bagumiTextView_title01.setText(videoItemList.get(0).getTitle().toString());
		bagumiTextView01_baofang.setText(videoItemList.get(0).getPlay().toString());
		bagumiTextView01_danmugu.setText(videoItemList.get(0).getVideo_review().toString());
		
		mImageLoader.displayImage(videoItemList.get(1).getPic(), bangumiImageView02,options);
		bagumiTextView_title02.setText(videoItemList.get(1).getTitle().toString());
		bagumiTextView02_baofang.setText(videoItemList.get(1).getPlay().toString());
		bagumiTextView02_danmugu.setText(videoItemList.get(1).getVideo_review().toString());
		
		mImageLoader.displayImage(videoItemList.get(2).getPic(), bangumiImageView03,options);
		bagumiTextView_title03.setText(videoItemList.get(2).getTitle().toString());
		bagumiTextView03_baofang.setText(videoItemList.get(2).getPlay().toString());
		bagumiTextView03_danmugu.setText(videoItemList.get(2).getVideo_review().toString());
		
		mImageLoader.displayImage(videoItemList.get(3).getPic(), bangumiImageView04,options);
		bagumiTextView_title04.setText(videoItemList.get(3).getTitle().toString());
		bagumiTextView04_baofang.setText(videoItemList.get(3).getPlay().toString());
		bagumiTextView04_danmugu.setText(videoItemList.get(3).getVideo_review().toString());
		
	}

	private void addDynamicView() {
		// 动态添加图片和下面指示的圆点
		// 初始化图片资源
		for (int i = 0; i < adList.size(); i++) {
			ImageView imageView = new ImageView(this.getActivity());
			// 异步加载图片
			mImageLoader.displayImage(adList.get(i).getImg(), imageView,
					options);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageViews.add(imageView);
			dots.get(i).setVisibility(View.VISIBLE);
			dotList.add(dots.get(i));
		}
	}


	private void startAd() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每两秒切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2,
				TimeUnit.SECONDS);
	}

	private class ScrollTask implements Runnable {

		@Override
		public void run() {
			synchronized (adViewPager) {
				currentItem = (currentItem + 1) % imageViews.size();
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// 当Activity不可见的时候停止切换
		scheduledExecutorService.shutdown();
	}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
		Log.d("win","hhhhhhhhhhhh");		
		new MainTask().execute("0");
		
        return rootView;
    }
    
   
	
	private class HomePageADAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return adList.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView iv = imageViews.get(position);
			((ViewPager) container).addView(iv);
			// 在这个方法里面设置图片的点击事件
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 处理跳转逻辑
					BannerItem item = (BannerItem) adList.get(position);
					Intent i = new Intent();
					i.setClass(getActivity(), BiliWebviewActivity.class);
					i.putExtra("bannerLink", item.getLink());
					startActivity(i);
					// 动画过渡
					getActivity().overridePendingTransition(R.anim.push_left_in,
							R.anim.push_no);
					Log.e("position", "" + position);
				}
			});
			return iv;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}
	
	private class HomePageADChangeListener implements OnPageChangeListener {

		private int oldPosition = 0;

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(position).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = position;
		}
	}
	private class MainTask extends AsyncTask<String, Void, Integer> {
		ArrayList<BannerItem> Listtemp = new ArrayList<BannerItem>();
		ArrayList<VideoItem> bangumiListtemp = new ArrayList<VideoItem>();

		@Override
		protected Integer doInBackground(String... params) {
			
			JSONObject bannerjson;
			JSONObject bangumijson;
			
			try {
				bannerjson = new JSONObject(HttpUtil.getHtmlString("http://www.bilibili.com/index/slideshow.json"));
				JSONArray array=bannerjson.getJSONArray("list");
				for (int i=0;i<array.length();i++) {
					
					BannerItem item = new BannerItem();		
					item.setImg(array.getJSONObject(i).getString("img").toString());
					item.setTitle(array.getJSONObject(i).getString("title").toString());
					item.setLink(array.getJSONObject(i).getString("link").toString());
					item.setAd(false);
					Listtemp.add(item);

				}
				
				bangumijson = new JSONObject(HttpUtil.getHtmlString("http://www.bilibili.com/index/ding.json"));
				//Log.i("gg",bangumijson.toString());
				JSONObject bangumiarray=bangumijson.getJSONObject("bangumi");
				for (int i=0;i<bangumiarray.length();i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(bangumiarray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(bangumiarray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(bangumiarray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(bangumiarray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(bangumiarray.getJSONObject(i+"").getString("play").toString());
					item.setReview(bangumiarray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(bangumiarray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(bangumiarray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(bangumiarray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(bangumiarray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(bangumiarray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(bangumiarray.getJSONObject(i+"").getString("create").toString());
					item.setPic(bangumiarray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(bangumiarray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(bangumiarray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(bangumiarray.getJSONObject(i+"").getString("duration").toString());	
					bangumiListtemp.add(item);
					Log.i("tag", bangumiarray.getJSONObject(i+"").getString("pic").toString());

				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Listtemp.get(Listtemp.size()-1).setAd(true);
			return null;
			

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			adList = Listtemp;
			videoItemList = bangumiListtemp;
			initAdData();
			initBangumiData();
			startAd();

//			adViewPager.notifyAll();

		}

	}

}
