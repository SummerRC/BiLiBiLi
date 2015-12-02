package com.vanco.abplayer.adapter;

import java.util.List;

import com.minisea.bilibli.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.vanco.abplayer.DonghuaActivity;
import com.vanco.abplayer.VideoInfoActivity;
import com.vanco.abplayer.model.AreaItem;
import com.vanco.abplayer.model.GameItem;
import com.vanco.abplayer.model.VideoItem;
import com.vanco.abplayer.util.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameListAdapter extends BaseAdapter{
	private Context mContext;
	private List<GameItem> mList;
	GameItem gameItem;
	public GameListAdapter(Context mContext,List<GameItem> mList){
		this.mContext = mContext;
		this.mList = mList;	
	}
	

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public GameItem getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		gameItem = getItem(position);
		convertView = LayoutInflater.from(mContext).inflate(R.layout.game_list_item, null);
		ImageView gameImageView = (ImageView) convertView.findViewById(R.id.game_item_image);
		TextView gameTextView = (TextView) convertView.findViewById(R.id.game_item_title);
		Button gameButton = (Button) convertView.findViewById(R.id.GameButton);
		gameImageView.setImageResource(gameItem.getImg());
		gameTextView.setText(gameItem.getText());
		gameButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 处理跳转逻辑
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mList.get(position).getPath()));
				ToastUtils.showToast("(｀・ω・´)转跳到"+mList.get(position).getText()+"官网");
				//i.setClass(mContext, DonghuaActivity.class);
				mContext.startActivity(intent);
				// 动画过渡
				((Activity) mContext).overridePendingTransition(R.anim.push_left_in,R.anim.push_no);
			}
		});
		
		
		return convertView;
	}

}
