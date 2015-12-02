package com.vanco.abplayer.fragment;

import java.util.ArrayList;

import com.minisea.bilibli.R;
import com.vanco.abplayer.DonghuaActivity;
import com.vanco.abplayer.TVActivity;
import com.vanco.abplayer.adapter.AreaGridAdapter;
import com.vanco.abplayer.model.AreaItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;

public class SubareaFragment extends Fragment{
	private GridView areaGridView;
	private View rankView;
	private ArrayList<AreaItem> areaList = new ArrayList<AreaItem>();
	private int[] areaimages = new int[]{R.drawable.ic_cate_bangumi,R.drawable.ic_cate_animation,
			R.drawable.ic_cate_music,R.drawable.ic_cate_game,R.drawable.ic_cate_science,
			R.drawable.ic_cate_entertainment,R.drawable.ic_cate_movie,R.drawable.ic_cate_tv};
	private String[] areatexts = new String[]{"番剧","动画","音乐","游戏","科学","娱乐","电影","电视"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	   	 for (int i = 0; i < areaimages.length; i++) {
				AreaItem item = new AreaItem();
				item.setImg(areaimages[i]);
				item.setText(areatexts[i]);
				areaList.add(item);
			}
	}

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_subarea, container, false);
         areaGridView = (GridView)rootView.findViewById(R.id.AreaGridView);
         areaGridView.setAdapter(new AreaGridAdapter(getActivity(), areaList));
         rankView = rootView.findViewById(R.id.RankView);
         rankView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 处理跳转逻辑
				Intent i = new Intent();
				i.putExtra("AreaType",8);
				i.setClass(getActivity(), DonghuaActivity.class);
				getActivity().startActivity(i);
				// 动画过渡
				getActivity().overridePendingTransition(R.anim.push_left_in,R.anim.push_no);
			}
		});
         return rootView;
     }

}
