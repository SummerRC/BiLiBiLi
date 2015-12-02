package com.vanco.abplayer.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.minisea.bilibli.R;
import com.vanco.abplayer.VideoInfoActivity;
import com.vanco.abplayer.adapter.VideoListAdapter;
import com.vanco.abplayer.model.Page;
import com.vanco.abplayer.model.VideoItem;
import com.vanco.abplayer.util.Constants;
import com.vanco.abplayer.util.HttpUtil;
import com.vanco.abplayer.util.URLUtil;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/**
 * Fragment页面
 * 
 * @author wwj_748
 * @date 2014/8/9
 */
public class DonghuaFragment extends Fragment implements IXListViewRefreshListener,
		IXListViewLoadMore {
	private XListView videoListView;// 视频列表
	private View noBlogView; // 无数据时显示
	private VideoListAdapter adapter;// 列表适配器
	private List<VideoItem> templist;

	private boolean isLoad = false; // 是否加载
	private int videoType = 1; // 视频类别
	private Page page; // 页面引用

	private String refreshDate = ""; // 刷新日期

	public DonghuaFragment(int videoType) {
		super();
		this.videoType = videoType;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		templist = new ArrayList<VideoItem>();
		VideoItem tempItem = new VideoItem();
		tempItem.setAid("7");
		tempItem.setTitle("[示例数据]童年动画主题曲");
		tempItem.setPic("http://i0.hdslb.com/320_180/u_user/53cb3e2f7f3efd6464b82c91ea9a1236.jpg");
		tempItem.setAuthor("根号⑨");
		tempItem.setPlay("23333");
		templist.add(tempItem);
		initComponent();
		if (isLoad == false) {
			isLoad = true;
			// 加载数据库中的数据
			List<VideoItem> list = templist;
			adapter.setList(list);
			adapter.notifyDataSetChanged();

			videoListView.startRefresh(); // 开始刷新

		} else {
			videoListView.NotRefreshAtBegin(); // 不开始刷新
		}
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("NewsFrag", "onCreateView");
		return inflater.inflate(R.layout.fragment_donghua, null);
	}

	// 初始化
	private void init() {
		
		adapter = new VideoListAdapter(getActivity());
		page = new Page();
		page.setPageStart();
	}

	// 初始化组件
	private void initComponent() {
		videoListView = (XListView) getView().findViewById(R.id.videoListView);
		videoListView.setAdapter(adapter);// 设置适配器
		videoListView.setPullRefreshEnable(this);// 设置可下拉刷新
		videoListView.setPullLoadEnable(this);// 设置可上拉加载
		// 设置列表项点击事件
		videoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获得博客列表项
				VideoItem item = (VideoItem) adapter.getItem(position - 1);
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("videoItemdata", item);
				i.setClass(getActivity(), VideoInfoActivity.class);
				i.putExtras(bundle);
				startActivity(i);
				// 动画过渡
				getActivity().overridePendingTransition(R.anim.push_left_in,
						R.anim.push_no);
			}
		});

		noBlogView = getView().findViewById(R.id.noBlogLayout);
	}

	private class MainTask extends AsyncTask<String, Void, Integer> {

		@Override
		protected Integer doInBackground(String... params) {
			List<VideoItem> list=new ArrayList<VideoItem>();
			try {
			JSONObject donghuajson = new JSONObject(HttpUtil.getHtmlString(params[0]));
			//动画数据解析
			JSONArray dougaarray=donghuajson.getJSONArray("list");
			for (int i=0;i<dougaarray.length();i++) {			
				VideoItem item = new VideoItem();		
				item.setAid(dougaarray.getJSONObject(i).getString("aid").toString());
				item.setTypeid(dougaarray.getJSONObject(i).getString("typeid").toString());
				item.setTitle(dougaarray.getJSONObject(i).getString("title").toString());
				item.setSbutitle(dougaarray.getJSONObject(i).optString("sbutitle").toString());
				item.setPlay(dougaarray.getJSONObject(i).getString("play").toString());
				item.setReview(dougaarray.getJSONObject(i).getString("review").toString());
				item.setVideo_review(dougaarray.getJSONObject(i).getString("video_review").toString());
				item.setFavorites(dougaarray.getJSONObject(i).getString("favorites").toString());
				item.setMid(dougaarray.getJSONObject(i).getString("mid").toString());
				item.setAuthor(dougaarray.getJSONObject(i).getString("author").toString());
				item.setDescription(dougaarray.getJSONObject(i).getString("description").toString());
				item.setCreate(dougaarray.getJSONObject(i).getString("create").toString());
				item.setPic(dougaarray.getJSONObject(i).getString("pic").toString());
				item.setCredit(dougaarray.getJSONObject(i).getString("credit").toString());
				item.setCoins(dougaarray.getJSONObject(i).getString("coins").toString());
				item.setDuration(dougaarray.getJSONObject(i).getString("duration").toString());	
				list.add(item);
				//Log.d("TAG--->", "--->"+item.getTitle());
			}
			
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Constants.DEF_RESULT_CODE.ERROR;
			}
//			// 获取网页json数据
//			String temp = HttpUtil.httpGet(params[0]);
//			if (temp == null) {
//				return Constants.DEF_RESULT_CODE.ERROR;
//			}
//			// 解析json获取列表
//			List<VideoItem> list = JsoupUtil.getBlogItemList(blogType, temp);
			
			if (list.size() == 0) {
				return Constants.DEF_RESULT_CODE.NO_DATA;
			}
			// 刷新动作
			if (params[1].equals("refresh")) {
				adapter.setList(list);
				return Constants.DEF_RESULT_CODE.REFRESH;
			} else {// 加载更多
				adapter.addList(list);
				return Constants.DEF_RESULT_CODE.LOAD;
			}

		}

		@Override
		protected void onPostExecute(Integer result) {
			// 通知列表数据更新
			adapter.notifyDataSetChanged();
			switch (result) {
			case Constants.DEF_RESULT_CODE.ERROR: // 错误
				Toast.makeText(getActivity(), "网络信号不佳", Toast.LENGTH_LONG).show();
				videoListView.stopRefresh(getDate());
				videoListView.stopLoadMore();
				break;
			case Constants.DEF_RESULT_CODE.NO_DATA: // 无数据
				// Toast.makeText(getActivity(), "无更多加载内容", Toast.LENGTH_LONG)
				// .show();
				videoListView.stopLoadMore();
				// noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				break;
			case Constants.DEF_RESULT_CODE.REFRESH: // 刷新
				videoListView.stopRefresh(getDate());
				if (adapter.getCount() == 0) {
					noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				}
				break;
			case Constants.DEF_RESULT_CODE.LOAD:
				videoListView.stopLoadMore();
				page.addPage();
				if (adapter.getCount() == 0) {
					noBlogView.setVisibility(View.VISIBLE); // 显示无博客
				}
				break;
			default:
				break;
			}
			super.onPostExecute(result);
		}

	}

	// 加载更多时调用
	@Override
	public void onLoadMore() {
		System.out.println("loadmore");
		new MainTask()
				.execute(
						URLUtil.getVideoListURL(videoType),
						"load");
	}

	@Override
	public void onRefresh() {
		System.out.println("refresh");
		page.setPageStart();
		new MainTask().execute(URLUtil.getRefreshBlogListURL(videoType),
				"refresh");
	}

	public String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm",
				Locale.CHINA);
		return sdf.format(new java.util.Date());
	}

}
