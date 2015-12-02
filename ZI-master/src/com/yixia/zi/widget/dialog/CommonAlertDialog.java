package com.yixia.zi.widget.dialog;

import com.yixia.zi.R;
import com.yixia.zi.utils.DeviceUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonAlertDialog extends Dialog implements android.view.View.OnClickListener {

	private Context mContext;
	private Drawable mTitleIcon;
	private String mTitle;
	private String mDescription;
	private String mOkText;
	private String mCancelText;
	private OnDialogItemClick mOnDialogItemClick;
	private TextView mSummary;
	
	public CommonAlertDialog(Context context, int theme, int title, int titleIcon, String description, int okText, int cancelText, OnDialogItemClick onDialogItemClick) {
		super(context, theme);
		this.mContext = context;
		this.mTitle = context.getString(title);
		if (titleIcon != 0) 
			this.mTitleIcon = context.getResources().getDrawable(titleIcon);
		this.mDescription = description;
		this.mOkText = context.getString(okText);
		if (cancelText != 0) {
			this.mCancelText = context.getString(cancelText);
		}
		this.mOnDialogItemClick = onDialogItemClick;
	}
	
	public CommonAlertDialog(Context context, int theme, String title, Drawable titleIcon, String description, String okText, String cancelText, OnDialogItemClick onDialogItemClick) {
		super(context, theme);
		this.mContext = context;
		this.mTitle = title;
		this.mTitleIcon = titleIcon;
		this.mDescription = description;
		this.mOkText = okText;
		this.mCancelText = cancelText;
		this.mOnDialogItemClick = onDialogItemClick;
	}

	public CommonAlertDialog(Context context, int theme, String title, String description, String okText, String cancelText, OnDialogItemClick onDialogItemClick) {
		this(context, theme, title, null, description, okText, cancelText, onDialogItemClick);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_dialog_layout);
		setCanceledOnTouchOutside(true);
		ImageView icon = ((ImageView) findViewById(R.id.title_icon));
		((TextView) findViewById(android.R.id.title)).setText(mTitle);
		mSummary = (TextView) findViewById(android.R.id.summary);
		mSummary.setText(mDescription);
		mSummary.setMovementMethod(ScrollingMovementMethod.getInstance());
		if (mTitleIcon != null) {
			icon.setImageDrawable(mTitleIcon);
			icon.setVisibility(View.VISIBLE);
		}
		Button okButton = (Button) findViewById(R.id.btn_ok);
		okButton.setText(TextUtils.isEmpty(mOkText) ? okButton.getText() : mOkText);
		okButton.setOnClickListener(this);
		Button cancelButton = (Button) findViewById(R.id.btn_cancel);
		if (TextUtils.isEmpty(mCancelText)) {
			cancelButton.setVisibility(View.GONE);
		} else {
			cancelButton.setText(TextUtils.isEmpty(mCancelText) ? cancelButton.getText() : mCancelText);
			cancelButton.setOnClickListener(this);
		}
		resize();
	}

	public void resize() {
		Activity ctx = (Activity) mContext;
		int screenWidth = DeviceUtils.getScreenWidth(ctx);
		int screenHeight = DeviceUtils.getScreenHeight(ctx);
		int width = (int) (screenWidth * (screenWidth < screenHeight ? 0.8 : 0.6));
		if (width > 600)
			width = 600;
		if (screenWidth < screenHeight)
			mSummary.setMaxLines(15);
		else {
			mSummary.setMaxLines(6);
		}
		getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (mOnDialogItemClick != null && v.getId() == R.id.btn_ok) {
			mOnDialogItemClick.onConfirmClick(v);
		}
	}

	public interface OnDialogItemClick {
		public void onConfirmClick(View v);
	}
}
