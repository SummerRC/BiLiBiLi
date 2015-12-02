package com.vanco.abplayer.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
/**
 * 左边侧滑
 * @author Administrator
 *
 */
public class LeftSliderLayout extends ViewGroup {

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    /**
     * Constant value for touch state
     * TOUCH_STATE_REST : no touch
     * TOUCH_STATE_SCROLLING : scrolling
     */
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private int mTouchState = TOUCH_STATE_REST;
    
    /**
     * Distance in pixels a touch can wander before we think the user is scrolling
     */
    private int mTouchSlop;
    
    /**
     * Values for saving axis of the last touch event.
     */
    private float mLastMotionX;
    private float mLastMotionY;
    
    /**
     * Values for VelocityTracker to compute current velocity.
     * VELOCITY_UNITS in dp
     * mVelocityUnits in px
     */
    private static final int VELOCITY_UNITS = 1000;
    private int mVelocityUnits;	
    
    /**
     * The minimum velocity for determining the direction.
     * MINOR_VELOCITY in dp
     * mMinorVelocity in px
     */
    private static final float MINOR_VELOCITY = 150.0f;
    private int mMinorVelocity;								
    
    /**
     * The width of Sliding distance from left. 
     * And it should be the same with the width of the View below SliderLayout in a FrameLayout.
     * DOCK_WIDTH in dp
     * mDockWidth in px
     */
    private static final float SLIDING_WIDTH = 180.0f;			
    private int mSlidingWidth;									
    
    /**
     * The default values of shadow.
     * VELOCITY_UNITS in dp
     * mVelocityUnits in px
     */
    private static final float DEF_SHADOW_WIDTH = 10.0f;		
    private int mDefShadowWidth;								

    /**
     * Value for checking a touch event is completed.
     */
    private boolean mIsTouchEventDone = false;				
    
    /**
     * Value for checking slider is open.
     */
    private boolean mIsOpen = false;						
    
    /**
     * Value for saving the last offset of scroller ’ x-axis.
     */
    private int mSaveScrollX = 0;							
    
    /**
     * Value for checking slider is allowed to slide.
     */
    private boolean mEnableSlide = true;					
    
    private View mMainChild = null;
    private OnLeftSliderLayoutStateListener mListener = null;

	/**
	 * Instantiates a new LeftSliderLayout.
	 *
	 * @param context the associated Context
	 * @param attrs AttributeSet
	 */
    public LeftSliderLayout(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

	/**
	 * Instantiates a new LeftSliderLayout.
	 *
	 * @param context the associated Context
	 * @param attrs AttributeSet
	 * @param defStyle Style
	 */
    public LeftSliderLayout(Context context, AttributeSet attrs, int defStyle) {
    	super(context, attrs, defStyle);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        
        /**
         * Convert values in dp to values in px;
         */
        final float fDensity = getResources().getDisplayMetrics().density;
        mVelocityUnits = (int) (VELOCITY_UNITS * fDensity + 0.5f);
        mMinorVelocity = (int) (MINOR_VELOCITY * fDensity + 0.5f);
        mSlidingWidth = (int) (SLIDING_WIDTH * fDensity + 0.5f);
        mDefShadowWidth = (int) (DEF_SHADOW_WIDTH * fDensity + 0.5f);
    }
    
    @Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    	
    	// check Measure Mode is Exactly.
    	final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("LeftSliderLayout only canmCurScreen run at EXACTLY mode!");
        }
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
        	throw new IllegalStateException("LeftSliderLayout only can run at EXACTLY mode!");
        }

        // measure child views
        int nCount = getChildCount();
        for (int i = 2; i < nCount; i++) {
        	removeViewAt(i);
        }
        nCount = getChildCount();
        if (nCount > 0) {
        	if (nCount > 1) {
        		mMainChild = getChildAt(1);
        		getChildAt(0).measure(widthMeasureSpec, heightMeasureSpec);
			} else {
				mMainChild = getChildAt(0);
			}
        	mMainChild.measure(widthMeasureSpec, heightMeasureSpec);
		}
        
        // Set the scrolled position 
        scrollTo(mSaveScrollX, 0);
    }
    
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int nCount = getChildCount();
		if (nCount <= 0) {
			return;
		}
		
		// Set the size and position of Main Child
		if (mMainChild != null) {
			mMainChild.layout(
				l,
				t,
				l + mMainChild.getMeasuredWidth(),
				t + mMainChild.getMeasuredHeight());
		}
		
		// Set the size and position of Shadow Child
		if (nCount > 1) {
			int nLeftChildWidth = 0;
			View leftChild = getChildAt(0);
			ViewGroup.LayoutParams layoutParams = leftChild.getLayoutParams();
			if (layoutParams.width == ViewGroup.LayoutParams.FILL_PARENT
					|| layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
				nLeftChildWidth = mDefShadowWidth;
			} else {
				nLeftChildWidth = layoutParams.width;
			}
			leftChild.layout(
					l - nLeftChildWidth,
					t,
					l,
					t + leftChild.getMeasuredHeight());
		}
	}
	
    @Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
    }

    @Override
	public boolean onTouchEvent(MotionEvent event) { 
    	
    	int nCurScrollX = getScrollX();
    	
    	// check touch point is in the rectangle of Main Child
    	if (mMainChild != null
    			&& mTouchState != TOUCH_STATE_SCROLLING
    			&& mIsTouchEventDone) {
			Rect rect = new Rect();
			mMainChild.getHitRect(rect);
			if (!rect.contains((int)event.getX() + nCurScrollX, (int)event.getY())) {
				return false;
			}
		}
    	
    	if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);
        
        final int action = event.getAction();
        final float x = event.getX();
        
        switch (action) {
        case MotionEvent.ACTION_DOWN: {
        	if (!mScroller.isFinished()) {
        		mScroller.abortAnimation();
        	}
        	
        	mIsTouchEventDone = false;
            mLastMotionX = x;
            break;
        }

        case MotionEvent.ACTION_MOVE: {
        	// check slider is allowed to slide.
        	if (!mEnableSlide) {
				break;
			}
        	
        	// compute the x-axis offset from last point to current point
            int deltaX = (int) (mLastMotionX - x);
            if (nCurScrollX + deltaX < getMinScrollX()) {
        		deltaX = getMinScrollX() - nCurScrollX;
        		mLastMotionX = mLastMotionX - deltaX;
			} else if (nCurScrollX + deltaX > getMaxScrollX()) {
        		deltaX = getMaxScrollX() - nCurScrollX;
        		mLastMotionX = mLastMotionX - deltaX;
			} else {
				mLastMotionX = x;
			}
        	
            // Move view to the current point
        	if (deltaX != 0) {
        		scrollBy(deltaX, 0);
			}
        	
        	// Save the scrolled position 
        	mSaveScrollX = getScrollX();
        	break;
        }

        case MotionEvent.ACTION_CANCEL: 
        case MotionEvent.ACTION_UP: {
        	
        	// check slider is allowed to slide.
        	if (!mEnableSlide) {
				break;
			}
        	
        	final VelocityTracker velocityTracker = mVelocityTracker;
        	velocityTracker.computeCurrentVelocity(mVelocityUnits);
        	
        	// Set open or close state, when get ACTION_UP or ACTION_CANCEL event.
        	if (nCurScrollX < 0) {
        		int velocityX = (int) velocityTracker.getXVelocity();
        		if (velocityX > mMinorVelocity) {
	            	scrollByWithAnim(getMinScrollX() - nCurScrollX);
	            	setState(true);
	            }
	            else if (velocityX < -mMinorVelocity) {
	            	scrollByWithAnim(-nCurScrollX);
	            	setState(false);
	            } else {
	            	if (nCurScrollX >= getMinScrollX() / 2) {
	            		scrollByWithAnim(- nCurScrollX);
						setState(false);
					} else {
						scrollByWithAnim(getMinScrollX() - nCurScrollX);
	            		setState(true);
					}
	            }
			} else {
				if (nCurScrollX > 0) {
					scrollByWithAnim(-nCurScrollX);
				}
				setState(false);
			}
        	
            if (mVelocityTracker != null) {
            	mVelocityTracker.recycle();
            	mVelocityTracker = null;
            }

            mTouchState = TOUCH_STATE_REST;
            mIsTouchEventDone = true;
            break;
        }
        
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	
        final int action = ev.getAction();
    	
		if (mListener != null && !mListener.OnLeftSliderLayoutInterceptTouch(ev)) {
			return false;
		}
        
        if ((action == MotionEvent.ACTION_MOVE)
      		  && (mTouchState != TOUCH_STATE_REST)) {
                 return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
                 mLastMotionX = x;
                 mLastMotionY = y;
                 mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING; 
                 break;

        case MotionEvent.ACTION_MOVE:
                 final int xDiff = (int) Math.abs(mLastMotionX - x);
                 if (xDiff > mTouchSlop) { 
                          if (Math.abs(mLastMotionY - y) / Math.abs(mLastMotionX - x) < 1)
                                   mTouchState = TOUCH_STATE_SCROLLING;
                }
                break;

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
                 mTouchState = TOUCH_STATE_REST;
                 break;
        }
        return mTouchState != TOUCH_STATE_REST;
    }
    
    /**
     * With the horizontal scroll of the animation
     * 
     * @param nDx x-axis offset
     */
    void scrollByWithAnim(int nDx) {
    	if (nDx == 0) {
			return;
		}
    	
		mScroller.startScroll(getScrollX(), 0, nDx, 0,
				Math.abs(nDx));

		invalidate();
    }

    /**
     * Get distance of the maximum horizontal scroll
     * 
     * @return distance in px
     */
    private int getMaxScrollX() {
    	return 0;
    }
    
    /**
     * Get distance of the minimum horizontal scroll
     * @return distance in px
     */
    private int getMinScrollX() {
    	return -mSlidingWidth;
    }
    
    
    /**
     * Open LeftSlideLayout
     */
    public void open() {
    	if (mEnableSlide) {
        	scrollByWithAnim(getMinScrollX() - getScrollX());
        	setState(true);
		}
    }
    
    /**
     * Close LeftSlideLayout
     */
    public void close() {
    	if (mEnableSlide) {
	    	scrollByWithAnim((-1) * getScrollX());
	    	setState(false);
    	}
    }
    
    /**
     * Determine whether LeftSlideLayout is open
     * 
     * @return true-open，false-close
     */
    public boolean isOpen() {
    	return mIsOpen;
    }
    
    /**
     * Set state of LeftSliderLayout
     * 
     * @param bIsOpen the new state
     */
    private void setState(boolean bIsOpen) {
    	boolean bStateChanged = false;
    	if (mIsOpen && !bIsOpen) {
			bStateChanged = true;
		} else if (!mIsOpen && bIsOpen) {
			bStateChanged = true;
		}
    	
    	mIsOpen = bIsOpen;
    	
    	if (bIsOpen) {
    		mSaveScrollX = getMaxScrollX();
		} else {
			mSaveScrollX = 0;
		}
    	
    	if (bStateChanged && mListener != null) {
			mListener.OnLeftSliderLayoutStateChanged(bIsOpen);
		}
    }
    
    /**
     * enable slide action of LeftSliderLayout 
     * 
     * @param bEnable
     */
    public void enableSlide(boolean bEnable) {
		mEnableSlide = bEnable;
	}
    
    /**
     * Set listener to LeftSliderLayout
     */
    public void setOnLeftSliderLayoutListener(OnLeftSliderLayoutStateListener listener) {
    	mListener = listener;
    }

    /**
     * LeftSliderLayout Listener
     *
     */
    public interface OnLeftSliderLayoutStateListener { 
        
    	/**
    	 * Called when LeftSliderLayout’s state has been changed.
    	 * 
    	 * @param bIsOpen the new state
    	 */
    	public void OnLeftSliderLayoutStateChanged(boolean bIsOpen);
    	
    	/**
    	 * Called when LeftSliderLayout has got onInterceptTouchEvent.
    	 * 
    	 * @param ev Touch Event
    	 * @return true - LeftSliderLayout need to manage the InterceptTouchEvent.
    	 *         false - LeftSliderLayout don't need to manage the InterceptTouchEvent.
    	 */
    	public boolean OnLeftSliderLayoutInterceptTouch(MotionEvent ev);
   }
}
