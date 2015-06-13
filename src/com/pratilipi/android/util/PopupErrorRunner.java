package com.pratilipi.android.util;

import com.pratilipi.android.R;

import android.app.Activity;
import android.graphics.drawable.ShapeDrawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupErrorRunner implements Runnable {

	private Activity mActivity;
	private PopupWindow mPopup;
	private SpannableString mBody;
	private SpannableString mHeader;

	public PopupErrorRunner(Activity activity, SpannableString header,
			SpannableString body) {
		this.mActivity = activity;
		this.mHeader = header;
		this.mBody = body;
	}

	public void dismiss() {
		if (mPopup != null) {
			mPopup.dismiss();
		}
	}

	public boolean isShowing() {
		return mPopup != null && mPopup.isShowing();
	}

	@Override
	public void run() {
		View layout = View.inflate(mActivity, R.layout.layout_system_error,
				null);
		if (layout != null) {
			mPopup = new PopupWindow(layout,
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
		}

		if (!TextUtils.isEmpty(mHeader)) {
			((TextView) mPopup.getContentView().findViewById(R.id.header))
					.setText(mHeader);
		}

		if (!TextUtils.isEmpty(mBody)) {
			((TextView) mPopup.getContentView().findViewById(R.id.body))
					.setText(mBody);
		} else {
			mPopup.getContentView().findViewById(R.id.body)
					.setVisibility(View.GONE);
		}

		mPopup.getContentView().setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mPopup != null && mPopup.isShowing()) {
					mPopup.dismiss();
					return true;
				}
				return false;
			}
		});

		mPopup.setAnimationStyle(R.style.ErrorAnimation);
		mPopup.setBackgroundDrawable(new ShapeDrawable());
		mPopup.setOutsideTouchable(true);
		mPopup.setFocusable(true);
	}

}
