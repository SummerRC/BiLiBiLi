package com.vanco.abplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minisea.bilibli.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.vanco.abplayer.model.VideoItem;

/**
 * 视频列表适配器
 * 
 * @author wwj_748
 * @date 2012/8/9
 */
public class VideoListAdapter extends BaseAdapter {
	private ViewHolder holder; // 视图容器
	private LayoutInflater layoutInflater; // 布局加载器
	private Context context; // 上下文对象
	private List<VideoItem> list; // 博客列表

	private ImageLoader imageLoader = ImageLoader.getInstance();// 得到图片加载器
	private DisplayImageOptions options; // 显示图像设置

	public VideoListAdapter(Context context) {
		super();
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		list = new ArrayList<VideoItem>();

		// 图片加载器初始化
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.bili_default_image_tv_with_bg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.bili_default_image_tv_with_bg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.bili_default_image_tv_with_bg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory() // 设置下载的图片是否缓存在内存中
				.cacheOnDisc() // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(1)) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象
	}

	public void setList(List<VideoItem> list) {
		this.list = list;
	}

	public void addList(List<VideoItem> list) {
		this.list.addAll(list);
	}

	public void clearList() {
		this.list.clear();
	}

	public List<VideoItem> getList() {
		return list;
	}

	public void removeItem(int position) {
		if (list.size() > 0) {
			list.remove(position);
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null ) {
			// 装载布局文件blog_list_item.xml
			convertView = layoutInflater.inflate(R.layout.video_list_item, null);
			holder = new ViewHolder();
			// holder.id = (TextView) convertView.findViewById(R.id.id);
			holder.title = (TextView) convertView.findViewById(R.id.list_item_title);
			holder.uptext = (TextView) convertView.findViewById(R.id.TextView_up);
			holder.bofangtext = (TextView) convertView.findViewById(R.id.TextView_bofang);
			holder.img = (ImageView) convertView.findViewById(R.id.list_item_image);
			convertView.setTag(holder); // 表示给View添加一个格外的数据，
		} else {
			holder = (ViewHolder) convertView.getTag();// 通过getTag的方法将数据取出来
		}
		VideoItem item = list.get(position); // 获取当前数据
		if (item != null) {
			// 显示标题内容
			holder.title.setText(item.getTitle());
			holder.uptext.setText("UP主："+item.getAuthor());
			holder.bofangtext.setText("播放："+item.getPlay());
			if (item.getPic() != null) {
				holder.img.setVisibility(View.VISIBLE);
				// 异步加载图片
				imageLoader
						.displayImage(item.getPic(), holder.img, options);
			} else {
				// 
				holder.img.setVisibility(View.VISIBLE); 
			}
		}

		return convertView;
	}

	private class ViewHolder {
		TextView title;
		ImageView img;
		TextView uptext;
		TextView bofangtext;
	}

}
