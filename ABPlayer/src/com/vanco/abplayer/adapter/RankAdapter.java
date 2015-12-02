package com.vanco.abplayer.adapter;

import com.vanco.abplayer.fragment.RankFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * FragmentPager适配器
 * 
 * @author wwj_748
 * @2014/8/9
 */
public class RankAdapter extends FragmentPagerAdapter {
	// 内容标题
	public static final String[] DONG_HUA_TITLE = new String[] { "全区", "新番",
			"动画", "音乐", "游戏", "科学", "娱乐", "电影" };
	
	public RankAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}


	// 获取项
	@Override
	public Fragment getItem(int position) {
		System.out.println("Fragment position:" + position);
		
		switch (position) {
		case 0:
			return new RankFragment(10070);
		case 1:
			return new RankFragment(100733);
		case 2:
			return new RankFragment(10071);
		case 3:
			return new RankFragment(10073);
		case 4:
			return new RankFragment(10074);
		case 5:
			return new RankFragment(100736);
		case 6:
			return new RankFragment(10075);
		case 7:
			return new RankFragment(100723);
		default:
			return new RankFragment(1);
		}
		
		// MainFragment fragment = new MainFragment(position);
		// return fragment;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// 返回页面标题
		return DONG_HUA_TITLE[position % DONG_HUA_TITLE.length].toUpperCase();
	}

	@Override
	public int getCount() {
		// 页面个数
		return DONG_HUA_TITLE.length;
	}

}
