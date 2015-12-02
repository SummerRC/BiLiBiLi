package com.vanco.abplayer.fragment;

import com.minisea.bilibli.R;
import com.vanco.abplayer.adapter.GameListAdapter;
import com.vanco.abplayer.adapter.ItemsAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class BangumiFragment extends Fragment{
	private ListView listViewLeft;
	private ListView listViewRight;
	private ItemsAdapter leftAdapter;
	private ItemsAdapter rightAdapter;

	int[] leftViewsHeights;
	int[] rightViewsHeights;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	 View rootView = inflater.inflate(R.layout.items_list, container, false);
 		listViewLeft = (ListView) rootView.findViewById(R.id.list_view_left);
 		listViewRight = (ListView)  rootView.findViewById(R.id.list_view_right);
 		
 		loadItems();
 		
 		listViewLeft.setOnTouchListener(touchListener);
 		listViewRight.setOnTouchListener(touchListener);		
 		listViewLeft.setOnScrollListener(scrollListener);
 		listViewRight.setOnScrollListener(scrollListener);
    	 
         return rootView;
    	
    }
    
	// Passing the touch event to the opposite list
	OnTouchListener touchListener = new OnTouchListener() {					
		boolean dispatched = false;
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.equals(listViewLeft) && !dispatched) {
				dispatched = true;
				listViewRight.dispatchTouchEvent(event);
			} else if (v.equals(listViewRight) && !dispatched) {
				dispatched = true;
				listViewLeft.dispatchTouchEvent(event);
			}
			
			dispatched = false;
			return false;
		}
	};
	
	/**
	 * Synchronizing scrolling 
	 * Distance from the top of the first visible element opposite list:
	 * sum_heights(opposite invisible screens) - sum_heights(invisible screens) + distance from top of the first visible child
	 */
	OnScrollListener scrollListener = new OnScrollListener() {
		
		@Override
		public void onScrollStateChanged(AbsListView v, int scrollState) {	
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			
			if (view.getChildAt(0) != null) {
				if (view.equals(listViewLeft) ){
					leftViewsHeights[view.getFirstVisiblePosition()] = view.getChildAt(0).getHeight();
					
					int h = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						h += rightViewsHeights[i];
					}
					
					int hi = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						hi += leftViewsHeights[i];
					}
					
					int top = h - hi + view.getChildAt(0).getTop();
					listViewRight.setSelectionFromTop(listViewRight.getFirstVisiblePosition(), top);
				} else if (view.equals(listViewRight)) {
					rightViewsHeights[view.getFirstVisiblePosition()] = view.getChildAt(0).getHeight();
					
					int h = 0;
					for (int i = 0; i < listViewLeft.getFirstVisiblePosition(); i++) {
						h += leftViewsHeights[i];
					}
					
					int hi = 0;
					for (int i = 0; i < listViewRight.getFirstVisiblePosition(); i++) {
						hi += rightViewsHeights[i];
					}
					
					int top = h - hi + view.getChildAt(0).getTop();
					listViewLeft.setSelectionFromTop(listViewLeft.getFirstVisiblePosition(), top);
				}
				
			}
			
		}
	};
	
	private void loadItems(){
		Integer[] leftItems = new Integer[]{R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5};
		Integer[] rightItems = new Integer[]{R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9, R.drawable.c10};
		String[] lefttexts = new String[]{"御神乐学园组曲","摸索吧！部活剧 第三季","怪盗JOKER 第二季","SHOW BY ROCK!!","雨色可可"};
		String[] righttexts = new String[]{"攻壳机动队ARISE ALTERNATIVE ARCHITECTURE","亚尔斯兰战记","JOJO的奇妙冒险",
				"黑子的篮球 第三季","可塑性记忆"};
		leftAdapter = new ItemsAdapter(getActivity(), R.layout.item, leftItems,lefttexts);
		rightAdapter = new ItemsAdapter(getActivity(), R.layout.item, rightItems,righttexts);
		listViewLeft.setAdapter(leftAdapter);
		listViewRight.setAdapter(rightAdapter);
		
		leftViewsHeights = new int[leftItems.length];
		rightViewsHeights = new int[rightItems.length];	
	}

}
