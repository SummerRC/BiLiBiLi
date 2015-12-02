package com.vanco.abplayer.adapter;

import java.util.List;

import com.minisea.bilibli.R;
import com.vanco.abplayer.BiliVideoViewActivity;
import com.vanco.abplayer.util.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoInfoListAdapter extends BaseAdapter{
	private Context mContext;
	private List<String> mList;
	String videoItem;
	String av;
	String page;
	public VideoInfoListAdapter(Context mContext,List<String> mList,String av){
		this.mContext = mContext;
		this.mList = mList;
		this.av = av;
	}
	

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public String getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		videoItem = getItem(position);
		convertView = LayoutInflater.from(mContext).inflate(R.layout.video_info_list_item, null);
		TextView titleTextView = (TextView) convertView.findViewById(R.id.title);
		View itemView = convertView.findViewById(R.id.linearlayout_row);
		titleTextView.setText(mList.get(position));

		itemView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 处理跳转逻辑
				//ToastUtils.showToast("(｀・ω・´) "+mList.get(position));
				page = String.valueOf(position+1);
				//i.setClass(mContext, DonghuaActivity.class);
				//mContext.startActivity(intent);
				// 动画过渡
				//((Activity) mContext).overridePendingTransition(R.anim.push_left_in,R.anim.push_no);
				Intent intent = new Intent(mContext, BiliVideoViewActivity.class);
				intent.putExtra("displayName",mList.get(position));
				intent.putExtra("av",av);
				intent.putExtra("page",page);
				mContext.startActivity(intent);
				((Activity) mContext).overridePendingTransition(R.anim.push_left_in,R.anim.push_no);
			}
		});
		
		
		return convertView;
	}

}
