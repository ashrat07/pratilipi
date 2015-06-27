package com.pratilipi.android.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Shelf;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.SystemUiHelper;

public class ReaderActivity extends Activity implements HttpResponseListener {

	private SystemUiHelper mHelper;

	private TextView mTextView;
	private View mProgressBarLayout;

	private Shelf mShelf;

	private float mDownX;
	private float mDownY;
	private Boolean isOnClick;
	private float SCROLL_THRESHOLD = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		}

		setContentView(R.layout.activity_reader);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		mTextView = (TextView) findViewById(R.id.text_view);
		mProgressBarLayout = findViewById(R.id.progress_bar_layout);

		mHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0,
				new SystemUiHelper.OnVisibilityChangeListener() {

					// // Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}
					}
				});

		mTextView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				switch (motionEvent.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mDownX = motionEvent.getX();
					mDownY = motionEvent.getY();
					isOnClick = true;
					break;

				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_UP:
					if (isOnClick) {
						if (mHelper.isShowing()) {
							mHelper.hide();
						} else {
							mHelper.show();
						}
					}
					break;

				case MotionEvent.ACTION_MOVE:
					if (isOnClick
							&& (Math.abs(mDownX - motionEvent.getX()) > SCROLL_THRESHOLD || Math
									.abs(mDownY - motionEvent.getY()) > SCROLL_THRESHOLD)) {
						isOnClick = false;
					}
					break;

				default:
					break;
				}
				return false;
			}
		});

		if (getIntent().getExtras() != null) {
			Shelf shelf = (Shelf) getIntent().getExtras()
					.getParcelable("SHELF");
			if (shelf != null) {
				mShelf = shelf;
				requestContent();
			}
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mHelper.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.reader_menu, menu);
		return true;
	}

	private void requestContent() {
		HttpGet contentRequest = new HttpGet(this, PConstants.CONTENT_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.CONTENT_URL);
		requestHashMap.put("pratilipiId", "" + mShelf.pratilipiId);

		contentRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.CONTENT_URL.equals(getUrl)) {
			mProgressBarLayout.setVisibility(View.GONE);
			if (finalResult != null) {
				try {
					String pageContent = finalResult.getString("pageContent");
					if (pageContent != null) {
						mTextView.setText(Html.fromHtml(pageContent));
						mTextView.setTypeface(FontManager.getInstance().get(
								mShelf.language));
						mTextView
								.setMovementMethod(new ScrollingMovementMethod());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

}
