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
public class KejiTabAdapter extends FragmentPagerAdapter {
	// 内容标题
	public static final String[] DONG_HUA_TITLE = new String[] { "全区动态", "纪录片",
		"科普人文", "野生技术", "公开课" , "军事", "数码"};
	
	public KejiTabAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}


	// 获取项
	@Override
	public Fragment getItem(int position) {
		System.out.println("Fragment position:" + position);
		
		switch (position) {
		case 0:
			return new DonghuaFragment(36);
		case 1:
			return new DonghuaFragment(37);
		case 2:
			return new DonghuaFragment(124);
		case 3:
			return new DonghuaFragment(122);
		case 4:
			return new DonghuaFragment(39);
		case 5:
			return new DonghuaFragment(96);
		case 6:
			return new DonghuaFragment(95);
		default:
			return new DonghuaFragment(36);
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
