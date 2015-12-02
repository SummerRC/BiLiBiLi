package com.vanco.abplayer.adapter;

import java.util.List;

import com.minisea.bilibli.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vanco.abplayer.VideoInfoActivity;
import com.vanco.abplayer.model.VideoItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{
	private Context mContext;
	private List<VideoItem> mList;
	private ImageLoader mImageLoader;
	
	public GridAdapter(Context mContext,List<VideoItem> mList,ImageLoader mImageLoader){
		this.mContext = mContext;
		this.mList = mList;
		this.mImageLoader = mImageLoader;
		
	}
	

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public VideoItem getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder;
		VideoItem videoitem = getItem(position);
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.homepage_item, null);
			mHolder = new ViewHolder();
			mHolder.icon = (ImageView) convertView.findViewById(R.id.list_item_image);
			mHolder.msg = (TextView) convertView.findViewById(R.id.list_item_title);
			mHolder.bofang = (TextView) convertView.findViewById(R.id.BangumiImageView_baofang);
			mHolder.danmagu = (TextView) convertView.findViewById(R.id.BangumiImageView_danmugu);
			
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mImageLoader.displayImage(videoitem.getPic(), mHolder.icon);
		mHolder.msg.setText(videoitem.getTitle());
		mHolder.bofang.setText(videoitem.getPlay());
		mHolder.danmagu.setText(videoitem.getVideo_review());
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 处理跳转逻辑
				VideoItem item = (VideoItem) mList.get(position);
				Intent i = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("videoItemdata", item);
				i.setClass(mContext, VideoInfoActivity.class);
				i.putExtras(bundle);
				mContext.startActivity(i);
				// 动画过渡
				((Activity) mContext).overridePendingTransition(R.anim.push_left_in,
						R.anim.push_no);
			}
		});
		
		
		return convertView;
	}
	
	private class ViewHolder{
		private ImageView icon;
		private TextView msg;
		private TextView bofang;
		private TextView danmagu;
		
	}

}
