package com.vanco.abplayer.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.minisea.bilibli.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.vanco.abplayer.BiliVideoViewActivity;
import com.vanco.abplayer.adapter.VideoInfoListAdapter;
import com.vanco.abplayer.model.VideoItem;
import com.vanco.abplayer.util.HttpUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class VideoInfoFragment extends Fragment{
	public static String IMAGE_CACHE_PATH = "imageloader/Cache"; // 图片缓存路径	
	private String page = "1";
	boolean isClickable = true;
 
	private View rootView;
	private VideoItem videoinfo;
	
    private ImageView videoImgaeView;
    private TextView titleTextView;
    private TextView upTextView;
    private TextView bofangTextView;
    private TextView danmaguTextView;
    private Button bofangButton;
    private TextView labelTextView;
    private TextView durationTextView;
    private ListView videoInfoListView;
    private ImageView arrowButton;
    private TextView videoNum;
    private ArrayList<String> videoInfoList = new ArrayList<String>();
    
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		videoinfo = (VideoItem) getActivity().getIntent().getSerializableExtra("videoItemdata");
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
	
	final class InJavaScriptLocalObj {  
        public void showSource(String html) {  
            System.out.println("====>html="+html);  
        }  
    }  

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState) {
		 rootView = inflater.inflate(R.layout.fragment_videoinfo, container, false);
		 initData();
		 Log.d("T^T","----->onCreateView");	
		 new VideoInfoTask().execute();
	     return rootView;
	}

	private void initData() {
		videoImgaeView = (ImageView) rootView.findViewById(R.id.imageView);
		titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
		upTextView= (TextView) rootView.findViewById(R.id.authorTextView);
		bofangTextView= (TextView) rootView.findViewById(R.id.playTextView);
		danmaguTextView= (TextView) rootView.findViewById(R.id.video_reviewTextView);
		bofangButton = (Button) rootView.findViewById(R.id.playButton);
		labelTextView= (TextView) rootView.findViewById(R.id.labelTextView);
		durationTextView = (TextView) rootView.findViewById(R.id.durationTextView);
		videoInfoListView = (ListView) rootView.findViewById(R.id.video_info_list);
		arrowButton = (ImageView) rootView.findViewById(R.id.arrowButton);
		videoNum = (TextView) rootView.findViewById(R.id.videoNumTextView);
		
		mImageLoader.displayImage(videoinfo.getPic(), videoImgaeView,options);
		titleTextView.setText(videoinfo.getTitle());
		upTextView.setText("Up主："+videoinfo.getAuthor());
		bofangTextView.setText("播放："+videoinfo.getPlay());
		danmaguTextView.setText("弹幕："+videoinfo.getVideo_review());
		//labelTextView.setText(videoinfo.getVideo_review());
		durationTextView.setText("  "+videoinfo.getDescription());
		
        
        // 实例化广告条
//        AdView adView = new AdView(getActivity(), AdSize.FIT_SCREEN);
//        // 获取要嵌入广告条的布局
//        LinearLayout adLayout=(LinearLayout)rootView.findViewById(R.id.adLayout);
//        // 将广告条加入到布局中
//        adLayout.addView(adView);
       
		
		bofangButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(bofangButton.getText().equals("点击播放")){
					Log.d("T^T","----->点击播放");
					// 处理跳转逻辑
//					Intent i = new Intent();
//					i.setClass(getActivity(), VideoViewActivity.class);
//					i.putExtra("videoPath", videopath);
//					i.putExtra("title", videoinfo.getTitle());
//					startActivity(i);
					Intent intent = new Intent(getActivity(), BiliVideoViewActivity.class);
					// intent.putExtra("path", item.url);
					intent.putExtra("displayName",videoinfo.getTitle());
					intent.putExtra("av",videoinfo.getAid());
					page = "1";
					intent.putExtra("page",page);
					startActivity(intent);
					// 动画过渡
					getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_no);
				}
			}
		});
		arrowButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (isClickable) {
					durationTextView.setMaxLines(durationTextView.getLineCount());
					isClickable = false;
					arrowButton.setImageResource(R.drawable.abcp__expander_close_holo_light);
				} else {
					durationTextView.setMaxLines(2);
					isClickable = true;
					arrowButton.setImageResource(R.drawable.abcp__expander_open_holo_light);
				}
			}
		});

	}


	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	private class VideoInfoTask extends AsyncTask<String, Void, Integer> {
		String label;

		@Override
		protected Integer doInBackground(String... arg0) {
//			String videoHTML = HttpUtil.sendPost("http://www.iippcc.com/bilibili/", "text=av"+videoinfo.getAid()+"&submit=搜索菌");
//			
//			Document doc = Jsoup.parse(videoHTML);
//			Elements links = doc.select("a[href]");
//			for (Element link : links) {
//				if(link.text().equals("分段1")){
//					videopath = link.attr("href");
//				}
//			}
//			
//			Elements labels = doc.getElementsByClass("col-md-7");
//			labels = labels.select("button");
//			//label = labels.get(3).text();
//			cID =  labels.get(6).text().substring(4);
//			
//			
			String listHTML = HttpUtil.getHtmlString("http://www.bilibili.com/mobile/video/av"+videoinfo.getAid()+".html");
			Log.d("QAQ--->","===>列表加载中");
			Document listDoc = Jsoup.parse(listHTML);
			Elements listElements = listDoc.getElementsByClass("li-wrap-content");
			for (int i = 0; i < listElements.size(); i++) {
				Log.d("QAQ--->","===>"+listElements.get(i).text());
				videoInfoList.add(listElements.get(i).text());
			}
			
			Elements labelElements = listDoc.select("[name=keywords]");
			Log.d("QAQ--->","===>"+labelElements.attr("content"));
			label = labelElements.attr("content");

			
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stubs
			super.onPostExecute(result);
			if(label != null){
				bofangButton.setText("点击播放");
				
			}else{
				bofangButton.setText("加载失败");
			}
			if(label != null){
				label = label.substring(20);
				labelTextView.setText("标签："+label);
				if(videoInfoList != null){
					videoInfoListView.setAdapter(new VideoInfoListAdapter(getActivity(),videoInfoList,videoinfo.getAid()));
					videoNum.setText("共有"+videoInfoList.size()+"段视频");
				}
			}
			if(durationTextView.getLineCount() > 2){
				arrowButton.setVisibility(View.VISIBLE);
			}
		}
	}
	
}
