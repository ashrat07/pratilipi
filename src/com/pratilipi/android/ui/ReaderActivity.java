package com.pratilipi.android.ui;

import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextPaint;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.TextPagerAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Shelf;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PageSplitter;
import com.pratilipi.android.util.SystemUiHelper;

public class ReaderActivity extends FragmentActivity implements
		HttpResponseListener {

	private SystemUiHelper mHelper;

	private ViewPager mViewPager;
	private View mProgressBarLayout;
	private View mControlView;
	private TextView mChapterTextView;
	private SeekBar mSeekBar;

	private Shelf mShelf;
	private List<CharSequence> mPages;
	private Boolean isOnClick;
	private float mDownX;
	private float SCROLL_THRESHOLD = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		}

		setContentView(R.layout.activity_reader);

		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mProgressBarLayout = findViewById(R.id.progress_bar_layout);
		mControlView = findViewById(R.id.control_view);
		mChapterTextView = (TextView) findViewById(R.id.chapter_text_view);
		mSeekBar = (SeekBar) findViewById(R.id.seek_bar);

		mHelper = new SystemUiHelper(this, SystemUiHelper.LEVEL_IMMERSIVE, 0,
				new SystemUiHelper.OnVisibilityChangeListener() {

					// Cached values.
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

		mViewPager.setPageTransformer(true, new DepthPageTransformer());
		mViewPager.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
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
					if (isOnClick
							&& Math.abs(mDownX - currentX) > SCROLL_THRESHOLD) {
						isOnClick = false;
					}
					break;
				}
				return false;
			}
		});
		mViewPager
				.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						mChapterTextView.setText("Page " + (position + 1)
								+ " of " + mPages.size());
						mSeekBar.setProgress(position);
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (progress < mPages.size()) {
					mViewPager.setCurrentItem(progress, true);
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
		requestHashMap.put("pageNo", "4");

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

						PageSplitter pageSplitter = new PageSplitter(
								mViewPager.getWidth(), mViewPager.getHeight(),
								1, 0);

						TextPaint textPaint = new TextPaint();
						textPaint.setTypeface(FontManager.getInstance().get(
								mShelf.language));
						textPaint.setTextSize(getResources().getDimension(
								R.dimen.text_small));

						pageSplitter.append(Html.fromHtml(pageContent)
								.toString(), textPaint);

						mPages = pageSplitter.getPages();
						mViewPager.setAdapter(new TextPagerAdapter(
								getSupportFragmentManager(), mPages));

						mChapterTextView.setText("Page 1 of " + mPages.size());
						mSeekBar.setMax(mPages.size() - 1);
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

class ZoomOutPageTransformer implements ViewPager.PageTransformer {

	private static final float MIN_SCALE = 0.85f;
	private static final float MIN_ALPHA = 0.5f;

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();
		int pageHeight = view.getHeight();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		} else if (position <= 1) { // [-1,1]
			// Modify the default slide transition to shrink the page as well
			float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
			float vertMargin = pageHeight * (1 - scaleFactor) / 2;
			float horzMargin = pageWidth * (1 - scaleFactor) / 2;
			if (position < 0) {
				view.setTranslationX(horzMargin - vertMargin / 2);
			} else {
				view.setTranslationX(-horzMargin + vertMargin / 2);
			}

			// Scale the page down (between MIN_SCALE and 1)
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

			// Fade the page relative to its size.
			view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE)
					/ (1 - MIN_SCALE) * (1 - MIN_ALPHA));

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}

class DepthPageTransformer implements ViewPager.PageTransformer {

	private static final float MIN_SCALE = 0.75f;

	public void transformPage(View view, float position) {
		int pageWidth = view.getWidth();

		if (position < -1) { // [-Infinity,-1)
			// This page is way off-screen to the left.
			view.setAlpha(0);

		} else if (position <= 0) { // [-1,0]
			// Use the default slide transition when moving to the left page
			view.setAlpha(1);
			view.setTranslationX(0);
			view.setScaleX(1);
			view.setScaleY(1);

		} else if (position <= 1) { // (0,1]
			// Fade the page out.
			view.setAlpha(1 - position);

			// Counteract the default slide transition
			view.setTranslationX(pageWidth * -position);

			// Scale the page down (between MIN_SCALE and 1)
			float scaleFactor = MIN_SCALE + (1 - MIN_SCALE)
					* (1 - Math.abs(position));
			view.setScaleX(scaleFactor);
			view.setScaleY(scaleFactor);

		} else { // (1,+Infinity]
			// This page is way off-screen to the right.
			view.setAlpha(0);
		}
	}
}
