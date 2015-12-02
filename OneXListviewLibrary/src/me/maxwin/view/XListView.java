/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create 2013/5/13
 * @author youxiachai
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 * 
 * bugfix: 鍒锋柊,鍔犺浇鏇村,閲嶅鍔犺浇
 * bugfix: item 鏁扮洰涓嶆弧涓�睆骞曠殑鏃跺�涓嶆樉绀烘洿澶氬姞杞芥寜閽� * improvement: 姝ｅ湪鍒锋柊,鎴栬�鍔犺浇鏇村鐨勬椂鍊欎笉搴旇鍙互缁х画鎷� */
package me.maxwin.view;

import com.youxiachai.onexlistview.R;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class XListView extends ListView implements OnScrollListener {

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

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
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
		addHeaderView(mHeaderView, null, false);

		// init footer view
		mFooterView = new XListViewFooter(context);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						System.out.println("GetmHeadViewHeight = " + mHeaderViewHeight);
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
		// 琛ュ厖淇敼
		disablePullLoad();
		disablePullRefreash();
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

	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);

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
		mHeaderViewContent.setVisibility(View.GONE);
		//添加
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
	 * stop refresh, reset header view.
	 */
	public void stopRefresh(String time) {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			mHeaderTimeView.setText(time);
			resetHeaderHeight();
		}
		//添加 设置时间
		mHeaderTimeView.setText(time);
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


	protected void invokeOnScrolling() {
		if (mScrollListener instanceof IXScrollListener) {
			IXScrollListener l = (IXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	protected void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 鏈浜庡埛鏂扮姸鎬侊紝鏇存柊绠ご
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				System.out.println("not mPullRefreshing");
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				System.out.println("mPullRefreshing");
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
			setSelection(0); // scroll to top each time
		
		
	}

	/**
	 * reset header view's height.
	 */
	protected void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		System.out.println("height = " + height);
		System.out.println("mHeadViewHeight = " + mHeaderViewHeight);
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

	protected void resetHeaderHeight(int disy) {
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
		mScrollBack = SCROLLBACK_HEADER;
		Log.d("xlistview", "resetHeaderHeight-->" + (finalHeight - height));
		mScroller.startScroll(0, height, 0, finalHeight - height + 100,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
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

	protected void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
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
		//		Log.d("xlistview", "xlistView-height");
				
				if (getFirstVisiblePosition() == 0
						&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)
						&& !mPullRefreshing) {
					// the first item is showing, header has shown or pull down.
					if(mEnablePullRefresh){
						System.out.println("deltaY = " + deltaY);
						updateHeaderHeight(deltaY / OFFSET_RADIO);
						invokeOnScrolling();
					}
					
				} else if (getLastVisiblePosition() == mTotalItemCount - 1
						&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)
						&& !mPullLoading) {
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
			System.out.println("refresh");
			mPullRefreshing = true;
			mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
			if (mOnRefresh != null) {
				mOnRefresh.onRefresh();
			}
		}
	}
	//添加 开始就刷新
	public void startRefresh(){
		mPullRefreshing = true;
		mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
		if (mOnRefresh != null) {
			mOnRefresh.onRefresh();
		}
	}
	//添加 停止刷新
	public void stopRefresh(){
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}
	//添加 开始不刷新
	public void NotRefreshAtBegin(){
		mHeaderView.setVisiableHeight(0);
	}
	
	//添加 设置时间
	public void setRefreshTime(String time){
		mHeaderTimeView.setText(time);
	}
	//添加 设置加载信息
	public void stopLoadMore(String msg) {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			mFooterView.setText(msg);
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
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

}
