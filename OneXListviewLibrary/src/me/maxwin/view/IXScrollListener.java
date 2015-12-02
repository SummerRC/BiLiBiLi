package me.maxwin.view;

import android.view.View;

/**
 * @author youxiachai
 * @date   2013-5-3
 * you can listen ListView.OnScrollListener or this one. it will invoke
 * onXScrolling when header/footer scroll back.
 */
public interface IXScrollListener {
	/**
	 * @param view
	 */
	public void onXScrolling(View view);
}
