package com.vanco.abplayer.fragment;

import java.util.ArrayList;
import java.util.List;

import com.minisea.bilibli.R;
import com.vanco.abplayer.adapter.AreaGridAdapter;
import com.vanco.abplayer.adapter.GameListAdapter;
import com.vanco.abplayer.model.AreaItem;
import com.vanco.abplayer.model.GameItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class GamesCenterFragment extends Fragment{
	private ListView gameListView;
	private ArrayList<GameItem> gameList = new ArrayList<GameItem>();
	private int[] gameimages = new int[]{R.drawable.hxzj_gamecenter_smallbanner,R.drawable.wcat_list,
			R.drawable.xwy_list,R.drawable.mlk,R.drawable.img_bh2,
			R.drawable.w};
	private String[] gametexts = new String[]{"幻想战姬","白猫计划","侠物语","梅露可物语","崩坏学院2","世界2"};
	private String[] gamepaths = new String[]{"http://hxzj.biligame.com/","http://bmjh.biligame.com/",
			"http://xwy.biligame.com/","http://mlk.biligame.com/","http://teos2.biligame.com/",
			"http://sj2.biligame.com/"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	   	 for (int i = 0; i < gameimages.length; i++) {
	   		    GameItem item = new GameItem();
				item.setImg(gameimages[i]);
				item.setText(gametexts[i]);
				item.setPath(gamepaths[i]);
				gameList.add(item);
			}
	}

     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_gamecenter, container, false);
         gameListView = (ListView)rootView.findViewById(R.id.GameListView);
         gameListView.setAdapter(new GameListAdapter(getActivity(), gameList));
         return rootView;
     }

}
