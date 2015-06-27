package com.pratilipi.android.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Shelf;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.SystemUiHelper;

public class ReaderActivity extends Activity implements HttpResponseListener {

	private SystemUiHelper mHelper;

	private ViewFlipper mViewFlipper;
	private View mProgressBarLayout;
	private View mControlView;
	private TextView mChapterTextView;
	private SeekBar mSeekBar;

	private Shelf mShelf;

	private Boolean isOnClick;
	private int totalPages;
	private int currentPage;
	private float mDownX;
	private float SCROLL_THRESHOLD = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		}

		setContentView(R.layout.activity_reader);

		mViewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
		mProgressBarLayout = findViewById(R.id.progress_bar_layout);
		mControlView = findViewById(R.id.control_view);
		mChapterTextView = (TextView) findViewById(R.id.chapter_text_view);
		mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

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
								mControlsHeight = mControlView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							mControlView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							mControlView.setVisibility(visible ? View.VISIBLE
									: View.GONE);
						}
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

						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						int screenWidth = dm.widthPixels;
						int screenHeight = dm.heightPixels;

						while (pageContent != null && pageContent.length() != 0) {
							totalPages++;

							// creating new textviews for every page
							TextView contentTextView = new TextView(this);
							contentTextView
									.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
							contentTextView
									.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
							contentTextView.setMaxHeight(screenHeight);
							contentTextView.setMaxWidth(screenWidth);
							contentTextView.setPadding(20, 20, 20, 20);

							float textSize = contentTextView.getTextSize();
							Paint paint = new Paint();
							paint.setTextSize(textSize);

							int numChars = 0;
							int lineCount = 0;
							int maxLineCount = screenHeight
									/ contentTextView.getLineHeight();
							contentTextView.setLines(maxLineCount);

							while ((lineCount < maxLineCount)
									&& (numChars < pageContent.length())) {
								numChars = numChars
										+ paint.breakText(
												pageContent.substring(numChars),
												true, screenWidth, null);
								lineCount++;
							}

							// retrieve the String to be displayed in the
							// current textbox
							String toBeDisplayed = pageContent.substring(0,
									numChars);
							pageContent = pageContent.substring(numChars);
							contentTextView.setText(Html
									.fromHtml(toBeDisplayed));
							mViewFlipper.addView(contentTextView, 0);

							numChars = 0;
							lineCount = 0;
						}

						currentPage = 0;
						mSeekBar.setMax(totalPages);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		// when user first touches the screen to swap
		case MotionEvent.ACTION_DOWN: {
			mDownX = event.getX();
			isOnClick = true;
			break;
		}

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
			float currentX = event.getX();
			if (isOnClick && Math.abs(mDownX - currentX) > SCROLL_THRESHOLD) {
				isOnClick = false;
				if (mDownX < currentX) {
					// If no more View/Child to flip
					if (mViewFlipper.getDisplayedChild() == 0)
						break;

					// set the required Animation type to ViewFlipper
					// The Next screen will come in form Left and current Screen
					// will go OUT from Right
					mViewFlipper.setInAnimation(this, R.anim.in_from_left);
					mViewFlipper.setOutAnimation(this, R.anim.out_to_right);
					// Show the next Screen
					mViewFlipper.showNext();

					mChapterTextView.setText("Page " + --currentPage);
				}

				// if right to left swipe on screen
				if (mDownX > currentX) {
					if (mViewFlipper.getDisplayedChild() == 1)
						break;
					// set the required Animation type to ViewFlipper
					// The Next screen will come in form Right and current
					// Screen
					// will go OUT from Left
					mViewFlipper.setInAnimation(this, R.anim.in_from_right);
					mViewFlipper.setOutAnimation(this, R.anim.out_to_left);
					// Show The Previous Screen
					mViewFlipper.showPrevious();

					mChapterTextView.setText("Page " + ++currentPage);
				}
			}
			break;
		}
		return false;
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

}
