package com.vanco.abplayer.adapter;


import com.vanco.abplayer.fragment.DonghuaFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * FragmentPager适配器
 * 
 * @author wwj_748
 * @2014/8/9
 */
public class DonghuaTabAdapter extends FragmentPagerAdapter {
	// 内容标题
	public static final String[] DONG_HUA_TITLE = new String[] { "全区动态", "MAD·AMV",
			"MMD·3D", "动画短片", "综合" };
	
	public DonghuaTabAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}


	// 获取项
	@Override
	public Fragment getItem(int position) {
		System.out.println("Fragment position:" + position);
		
		switch (position) {
		case 0:
			return new DonghuaFragment(1);
		case 1:
			return new DonghuaFragment(24);
		case 2:
			return new DonghuaFragment(25);
		case 3:
			return new DonghuaFragment(47);
		case 4:
			return new DonghuaFragment(27);
		default:
			return new DonghuaFragment(1);
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
