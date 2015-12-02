package com.youxiachai.onexlistview;

import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.IXScrollListener;
import me.maxwin.view.XListViewFooter;
import me.maxwin.view.XListViewHeader;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AbsListView;
import com.huewu.pla.lib.internal.PLA_AbsListView.OnScrollListener;

/**
 * @author youxiachai
 * @date 2013-5-3
 */
public class XMultiColumnListView extends MultiColumnListView implements
		OnScrollListener {
	protected float mLastY = -1; // save event y
	protected Scroller mScroller; // used for scroll back
	protected OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	protected IXListViewLoadMore mLoadMore;
	protected IXListViewRefreshListener mOnRefresh;
	// -- header view
	protected XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	protected RelativeLayout mHeaderViewContent;
	protected TextView mHeaderTimeView;
	protected int mHeaderViewHeight; // header view's height
	protected boolean mEnablePullRefresh = true;
	protected boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	protected XListViewFooter mFooterView;
	protected boolean mEnablePullLoad;
	protected boolean mPullLoading;
	protected boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	protected int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	protected int mScrollBack;
	protected final static int SCROLLBACK_HEADER = 0;
	protected final static int SCROLLBACK_FOOTER = 1;

	protected final static int SCROLL_DURATION = 400; // scroll back duration
	protected final static int PULL_LOAD_MORE_DELTA = 50; // when pull up >=
															// 50px
															// at bottom,
															// trigger
															// load more.
	protected final static float OFFSET_RADIO = 1.8f; // support iOS like pull
														// feature.

	public XMultiColumnListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XMultiColumnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XMultiColumnListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	protected void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
		// 默认关闭所有操作
		disablePullLoad();
		disablePullRefreash();
		// setPullRefreshEnable(mEnablePullRefresh);
		// setPullLoadEnable(mEnablePullLoad);
	}

	public void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	protected void invokeOnScrolling() {
		if (mScrollListener instanceof IXScrollListener) {
			IXScrollListener l = (IXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	protected void startLoadMore() {
		if (mEnablePullLoad
				&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA
				&& !mPullLoading) {
			mPullLoading = true;
			mFooterView.setState(XListViewFooter.STATE_LOADING);
			if (mLoadMore != null) {
				mLoadMore.onLoadMore();
			}
		}
	}

	protected void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	protected void updateFooterHeight(float delta) {
		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
													// more.
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);

		// setSelection(mTotalItemCount - 1); // scroll to bottom
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			// if not inflate screen ,footerview not add
			if(getAdapter() != null){
				if (getLastVisiblePosition() != (getAdapter().getCount() - 1)) {
					mIsFooterReady = true;
					addFooterView(mFooterView);
				}
			}
			
		}
	}

	/**
	 * reset header view's height.
	 */
	public void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		Log.d("xlistview", "resetHeaderHeight-->" + (finalHeight - height));
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}
	
	/* 
	 * 神奇的bug....
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		
		//莫名其妙的bug....
		//updateHeaderHeight(10);
		postDelayed(new Runnable() {
			@Override
			public void run() {
			//	resetHeaderHeight();
				mScroller.startScroll(0, 0, 0, 0,
						SCROLL_DURATION);
//				// trigger computeScroll
				invalidate();
			}
		}, 100);
		
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(IXListViewRefreshListener refreshListener) {
		mEnablePullRefresh = true;
		mHeaderViewContent.setVisibility(View.VISIBLE);
		this.mOnRefresh = refreshListener;

	}
	
	public void disablePullRefreash() {
		mEnablePullRefresh = false;
		// disable, hide the content
		mHeaderViewContent.setVisibility(View.INVISIBLE);
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(IXListViewLoadMore loadMoreListener) {
		mEnablePullLoad = true;
		this.mLoadMore = loadMoreListener;
		mPullLoading = false;
		mFooterView.show();
		mFooterView.setState(XListViewFooter.STATE_NORMAL);
		// both "pull up" and "click" will invoke load more.
		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startLoadMore();
			}
		});

	}
	
	public void disablePullLoad() {
		mEnablePullLoad = false;
		mFooterView.hide();
		mFooterView.setOnClickListener(null);
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh(String time) {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			mHeaderTimeView.setText(time);
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			Log.d("xlistview", "getFirstVisiblePosition()-->"
					+ getFirstVisiblePosition() + "getVisiableHeight()"
					+ mHeaderView.getVisiableHeight() + "deltaY->" + deltaY);
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)  && !mPullRefreshing) {
				// the first item is showing, header has shown or pull down.
				if(mEnablePullRefresh){
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
				}
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)  && !mPullLoading) {
				// last item, already pulled up or want to pull up.
				if(mEnablePullLoad){
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				startOnRefresh();
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.

				startLoadMore();
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	protected void startOnRefresh() {
		if (mEnablePullRefresh
				&& mHeaderView.getVisiableHeight() > mHeaderViewHeight
				&& !mPullRefreshing) {
			mPullRefreshing = true;
			mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (mOnRefresh != null) {
				mOnRefresh.onRefresh();
			}
		}
	}

	@Override
	public void onScrollStateChanged(PLA_AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(PLA_AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}


}
