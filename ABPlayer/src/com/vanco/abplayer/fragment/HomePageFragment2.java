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
import com.vanco.abplayer.DonghuaActivity;
import com.vanco.abplayer.adapter.GridAdapter;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class HomePageFragment2 extends Fragment
{
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径
	private boolean isLoad = false;
	public View rootView;
	private ViewPager adViewPager;
	private GridView bangumiGridView;
	private GridView dougaGridView;
	private GridView musicGridView;
	private GridView danceGridView;
	private GridView entGridView;
	private GridView movieGridView;
	private GridView kejiGridView;
	
	private View donghuaView;
	private View bankumiView;
	private View musicView;
	private View kejiView;
	private View yuleView;
	private View dianyingView;
	private View gameView;
	
	private List<ImageView> imageViews;// 滑动的图片集合

	private List<View> dots; // 图片标题正文的那些点
	private List<View> dotList;
	
	private int currentItem = 0; // 当前图片的索引号
	// 定义的六个指示点
	private View dot0;
	private View dot1;
	private View dot2;
	private View dot3;
	private View dot4;
	private View dot5;
	
	private List<VideoItem> videoItemList;
	private List<VideoItem> dougaItemList;
	private List<VideoItem> musicItemList;
	private List<VideoItem> danceItemList;
	private List<VideoItem> entItemList;
	private List<VideoItem> movieItemList;
	private List<VideoItem> kejiItemList;

	private ScheduledExecutorService scheduledExecutorService;

	// 异步加载图片
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;

	// 轮播banner的数据
	private List<BannerItem> adList;

	private Handler handler = new Handler() 
	{
		public void handleMessage(android.os.Message msg) 
		{
			adViewPager.setCurrentItem(currentItem);
		};
	};
	
    
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
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
		initTestData();
		MainTask homePageTask = new MainTask();
		homePageTask.execute("0");
	}
	
	private void initTestData() 
	{
		videoItemList = new ArrayList<VideoItem>();
		for (int i = 0; i < 4; i++) {
			VideoItem tempItem = new VideoItem();
			tempItem.setAid("7");
			tempItem.setTitle("[示例数据]童年动画主题曲");
			tempItem.setPic("http://i0.hdslb.com/320_180/u_user/53cb3e2f7f3efd6464b82c91ea9a1236.jpg");
			tempItem.setAuthor("根号⑨");
			tempItem.setPlay("23333");
			tempItem.setVideo_review("23333");
			videoItemList.add(tempItem);
		}
		dougaItemList = videoItemList;
		musicItemList = videoItemList;
		danceItemList = videoItemList;
		entItemList = videoItemList;
		movieItemList = videoItemList;
		kejiItemList = videoItemList;
		
		ArrayList<BannerItem> Listtemp = new ArrayList<BannerItem>();
		for (int i = 0; i < 6; i++) {
			BannerItem tempItem = new BannerItem();
			tempItem.setAd(false);
			tempItem.setImg("http://i0.hdslb.com/promote/1f451b6b07a1984be5619f865edd5449.jpg");
			tempItem.setLink("http://www.bilibili.com");
			tempItem.setTitle("[示例数据]");
			Listtemp.add(tempItem);
		}
		Listtemp.get(0).setAd(true);
		adList = Listtemp;
		

	}

	private void initImageLoader() 
	{
		File cacheDir = com.nostra13.universalimageloader.utils.StorageUtils
				.getOwnCacheDirectory(this.getActivity().getApplicationContext(),IMAGE_CACHE_PATH);

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
		

		adViewPager = (ViewPager) rootView.findViewById(R.id.vp);
		
		bangumiGridView = (GridView)rootView.findViewById(R.id.BangumiGridView);
		dougaGridView = (GridView)rootView.findViewById(R.id.DonghuaGridView);
		kejiGridView = (GridView)rootView.findViewById(R.id.KejiGridView);
		movieGridView = (GridView)rootView.findViewById(R.id.MovieGridView);
		entGridView = (GridView)rootView.findViewById(R.id.EntGridView);
		danceGridView = (GridView)rootView.findViewById(R.id.DanceGridView);
		musicGridView = (GridView)rootView.findViewById(R.id.MusicGridView);
		
		addDynamicView();
		adViewPager.setAdapter(new HomePageADAdapter());// 设置填充ViewPager页面的适配器
		// 设置一个监听器，当ViewPager中的页面改变时调用
		adViewPager.setOnPageChangeListener(new HomePageADChangeListener());
		GridAdapter bangumiAdapter = new GridAdapter(getActivity(), videoItemList, mImageLoader);
		bangumiGridView.setAdapter(bangumiAdapter);
		dougaGridView.setAdapter(new GridAdapter(getActivity(), dougaItemList, mImageLoader));		
		musicGridView.setAdapter(new GridAdapter(getActivity(), musicItemList, mImageLoader));		
		danceGridView.setAdapter(new GridAdapter(getActivity(), danceItemList, mImageLoader));		
		entGridView.setAdapter(new GridAdapter(getActivity(), entItemList, mImageLoader));		
		movieGridView.setAdapter(new GridAdapter(getActivity(), movieItemList, mImageLoader));		
		kejiGridView.setAdapter(new GridAdapter(getActivity(), kejiItemList, mImageLoader));
		
		donghuaView = rootView.findViewById(R.id.DonghuaView);
		donghuaView.setOnClickListener(mDonghuaViewListener);
		bankumiView = rootView.findViewById(R.id.BangumiView);
		bankumiView.setOnClickListener(mBankumiViewListener);
		musicView = rootView.findViewById(R.id.MusicView);
		musicView.setOnClickListener(mMusicViewListener);
		gameView = rootView.findViewById(R.id.DanceView);
		gameView.setOnClickListener(mGameViewListener);
		yuleView = rootView.findViewById(R.id.EntView);
		yuleView.setOnClickListener(mYuleViewListener);
		dianyingView = rootView.findViewById(R.id.MovieView);
		dianyingView.setOnClickListener(mDianyingViewListener);
		kejiView = rootView.findViewById(R.id.KejiView);
		kejiView.setOnClickListener(mKejiViewListener);
		
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
			if(dots.size() > i){
				dots.get(i).setVisibility(View.VISIBLE);
				dotList.add(dots.get(i));
			}
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
		//scheduledExecutorService.shutdown();
	}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_homepage, container, false);
		Log.d("win","hhhhhhhhhhhh");
		initAdData();
        return rootView;
    }
    
   
	
	private class HomePageADAdapter extends PagerAdapter{

		@Override
		public int getCount() {
			return dots.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			if(imageViews.size() <= position){
				return new ImageView(getActivity());
			}
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
	private class MainTask extends AsyncTask<String, Void, Integer> 
	{
		ArrayList<BannerItem> Listtemp = new ArrayList<BannerItem>();
		ArrayList<VideoItem> bangumiListtemp = new ArrayList<VideoItem>();
		ArrayList<VideoItem> dougaListtemp = new ArrayList<VideoItem>();
		ArrayList<VideoItem> musicListtemp = new ArrayList<VideoItem>();
		ArrayList<VideoItem> danceListtemp = new ArrayList<VideoItem>();
		ArrayList<VideoItem> entListtemp = new ArrayList<VideoItem>();
		ArrayList<VideoItem> movieListtemp = new ArrayList<VideoItem>();
		ArrayList<VideoItem> kejiListtemp = new ArrayList<VideoItem>();
		
		public MainTask() {
			// TODO Auto-generated constructor stub
			Log.d("T^T","----->MainTask");	
		}

		@Override
		protected Integer doInBackground(String... params) {
			Log.d("T^T","----->doinbackgroud");	
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
				for (int i=0;i<4;i++) {			
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
				}
				//动画数据解析
				JSONObject dougaarray=bangumijson.getJSONObject("douga");
				for (int i=0;i<4;i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(dougaarray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(dougaarray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(dougaarray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(dougaarray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(dougaarray.getJSONObject(i+"").getString("play").toString());
					item.setReview(dougaarray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(dougaarray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(dougaarray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(dougaarray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(dougaarray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(dougaarray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(dougaarray.getJSONObject(i+"").getString("create").toString());
					item.setPic(dougaarray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(dougaarray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(dougaarray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(dougaarray.getJSONObject(i+"").getString("duration").toString());	
					dougaListtemp.add(item);
				}
				//音乐数据解析
				JSONObject musicarray=bangumijson.getJSONObject("music");
				for (int i=0;i<4;i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(musicarray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(musicarray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(musicarray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(musicarray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(musicarray.getJSONObject(i+"").getString("play").toString());
					item.setReview(musicarray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(musicarray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(musicarray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(musicarray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(musicarray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(musicarray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(musicarray.getJSONObject(i+"").getString("create").toString());
					item.setPic(musicarray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(musicarray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(musicarray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(musicarray.getJSONObject(i+"").getString("duration").toString());	
					musicListtemp.add(item);
				}
				//舞蹈数据解析
				JSONObject dancearray=bangumijson.getJSONObject("game");
				for (int i=0;i<4;i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(dancearray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(dancearray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(dancearray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(dancearray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(dancearray.getJSONObject(i+"").getString("play").toString());
					item.setReview(dancearray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(dancearray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(dancearray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(dancearray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(dancearray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(dancearray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(dancearray.getJSONObject(i+"").getString("create").toString());
					item.setPic(dancearray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(dancearray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(dancearray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(dancearray.getJSONObject(i+"").getString("duration").toString());	
					danceListtemp.add(item);
				}
				//娱乐数据解析
				JSONObject entarray=bangumijson.getJSONObject("ent");
				for (int i=0;i<4;i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(entarray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(entarray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(entarray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(entarray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(entarray.getJSONObject(i+"").getString("play").toString());
					item.setReview(entarray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(entarray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(entarray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(entarray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(entarray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(entarray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(entarray.getJSONObject(i+"").getString("create").toString());
					item.setPic(entarray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(entarray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(entarray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(entarray.getJSONObject(i+"").getString("duration").toString());	
					entListtemp.add(item);
				}
				//电影数据解析
				JSONObject moviearray=bangumijson.getJSONObject("movie");
				for (int i=0;i<4;i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(moviearray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(moviearray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(moviearray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(moviearray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(moviearray.getJSONObject(i+"").getString("play").toString());
					item.setReview(moviearray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(moviearray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(moviearray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(moviearray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(moviearray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(moviearray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(moviearray.getJSONObject(i+"").getString("create").toString());
					item.setPic(moviearray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(moviearray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(moviearray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(moviearray.getJSONObject(i+"").getString("duration").toString());	
					movieListtemp.add(item);
				}
				//科技数据解析
				JSONObject kejiarray=bangumijson.getJSONObject("technology");
				for (int i=0;i<4;i++) {			
					VideoItem item = new VideoItem();		
					item.setAid(kejiarray.getJSONObject(i+"").getString("aid").toString());
					item.setTypeid(kejiarray.getJSONObject(i+"").getString("typeid").toString());
					item.setTitle(kejiarray.getJSONObject(i+"").getString("title").toString());
					item.setSbutitle(kejiarray.getJSONObject(i+"").optString("sbutitle").toString());
					item.setPlay(kejiarray.getJSONObject(i+"").getString("play").toString());
					item.setReview(kejiarray.getJSONObject(i+"").getString("review").toString());
					item.setVideo_review(kejiarray.getJSONObject(i+"").getString("video_review").toString());
					item.setFavorites(kejiarray.getJSONObject(i+"").getString("favorites").toString());
					item.setMid(kejiarray.getJSONObject(i+"").getString("mid").toString());
					item.setAuthor(kejiarray.getJSONObject(i+"").getString("author").toString());
					item.setDescription(kejiarray.getJSONObject(i+"").getString("description").toString());
					item.setCreate(kejiarray.getJSONObject(i+"").getString("create").toString());
					item.setPic(kejiarray.getJSONObject(i+"").getString("pic").toString());
					item.setCredit(kejiarray.getJSONObject(i+"").getString("credit").toString());
					item.setCoins(kejiarray.getJSONObject(i+"").getString("coins").toString());
					item.setDuration(kejiarray.getJSONObject(i+"").getString("duration").toString());	
					kejiListtemp.add(item);
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
			Log.d("T^T","----->onpostexcute");	
			adList = Listtemp;
			videoItemList = bangumiListtemp;
			dougaItemList = dougaListtemp;
			musicItemList = musicListtemp;
			danceItemList = danceListtemp;
			entItemList = entListtemp;
			movieItemList = movieListtemp;
			kejiItemList = kejiListtemp;
			initAdData();
//			initBangumiData();
			startAd();

//			adViewPager.notifyAll();

		}

	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mImageLoader.destroy();
		super.onDestroy();
	}
	
	private View.OnClickListener mDonghuaViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",2);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);

		}
	};
	private View.OnClickListener mBankumiViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",1);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);
			
		}
	};
	private View.OnClickListener mMusicViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",3);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);
			
		}
	};
	private View.OnClickListener mKejiViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",5);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);
			
		}
	};
	private View.OnClickListener mYuleViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",6);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);
			
		}
	};
	private View.OnClickListener mDianyingViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",7);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);
			
		}
	};
	private View.OnClickListener mGameViewListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// 处理跳转逻辑
			Intent i = new Intent();
			i.putExtra("AreaType",4);
			i.setClass(getActivity(), DonghuaActivity.class);
			startActivity(i);
			// 动画过渡
			getActivity().overridePendingTransition(R.anim.push_left_in,
					R.anim.push_no);
			
		}
	};

}
