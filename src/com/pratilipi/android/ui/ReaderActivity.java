package com.pratilipi.android.ui;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ChapterAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.Chapter;
import com.pratilipi.android.model.PageContent;
import com.pratilipi.android.model.ShelfContent;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PageContentDataSource;
import com.pratilipi.android.util.SystemUiHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReaderActivity extends Activity implements HttpResponseListener {

	private SystemUiHelper mHelper;

	private DrawerLayout mDrawerLayout;
	private ListView mChapterListView;
	private WebView mWebView;
	private View mControlView;
	private TextView mChapterTextView;
	private SeekBar mSeekBar;

	private ShelfContent mShelfContent;
	private Book mBook;
	private ChapterAdapter mChapterAdapter;
	private ArrayList<Chapter> mIndexList = new ArrayList<>();;

	private int mPratilipiPageNo = 1;
	private int mCurrentPage;
	private int mTotalPages;
	private Boolean isOnClick;
	private float mDownX;
	private float SCROLL_THRESHOLD = 20;

	private HttpGet contentRequest;
	private PageContentDataSource mDataSource;
	private boolean mPageLoaded;
	private String mPageLoadContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);

		// if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
		// getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		// }

		setContentView(R.layout.activity_reader);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		mChapterListView = (ListView) findViewById(R.id.right_drawer);
		mChapterListView.setOnItemClickListener(new DrawerItemClickListener());
		mWebView = (WebView) findViewById(R.id.web_view);
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

		mWebView.getSettings().setJavaScriptEnabled(true);
		JavaScriptInterface jsInterface = new JavaScriptInterface(this);
		mWebView.addJavascriptInterface(jsInterface, "jsInterface");
		mWebView.setOnTouchListener(new View.OnTouchListener() {

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
		mWebView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				mPageLoaded = true;
				if (!TextUtils.isEmpty(mPageLoadContent)) {
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							mWebView.loadUrl("javascript:paginate(\""
									+ mPageLoadContent.replace("\"", "'")
									+ "\")");
						}
					}, 500);
				}
			}
		});

		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			boolean touched;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				touched = false;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				touched = true;
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (touched) {
					mWebView.loadUrl("javascript:goToPage(" + progress + ")");
				}
			}
		});

		mDataSource = new PageContentDataSource(this);
		mDataSource.open();
		if (getIntent().getExtras() != null) {
			ShelfContent shelfContent = (ShelfContent) getIntent().getExtras()
					.getParcelable("SHELF_CONTENT");
			if (shelfContent != null) {
				mShelfContent = shelfContent;
				if (PConstants.CONTENT_LANGUAGE.HINDI.toString().equals(
						mShelfContent.language)) {
					mWebView.loadUrl("file:///android_asset/hindi_wrapper.html");
				} else if (PConstants.CONTENT_LANGUAGE.TAMIL.toString().equals(
						mShelfContent.language)) {
					mWebView.loadUrl("file:///android_asset/tamil_wrapper.html");
				} else if (PConstants.CONTENT_LANGUAGE.GUJARATI.toString()
						.equals(mShelfContent.language)) {
					mWebView.loadUrl("file:///android_asset/gujarati_wrapper.html");
				}
				Gson gson = new Gson();
				mBook = gson.fromJson(shelfContent.content,
						new TypeToken<Book>() {
						}.getType());
				final PageContent pageContent = mDataSource.getPageContent(
						mShelfContent.pratilipiId, mPratilipiPageNo);
				if (pageContent != null) {
					if (mPageLoaded) {
						mWebView.loadUrl("javascript:paginate(\""
								+ mPageLoadContent.replace("\"", "'") + "\")");
					} else {
						mPageLoadContent = pageContent.content;
					}
				} else {
					requestContent();
				}
				if (mBook.index != null) {
					for (Chapter chapter : mBook.index) {
						mIndexList.add(chapter);
					}
				}
				mChapterAdapter = new ChapterAdapter(this,
						R.layout.layout_drawer_list_view_item, mIndexList,
						mShelfContent.language);
				mChapterListView.setAdapter(mChapterAdapter);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.change_font:
			WebSettings webSettings = mWebView.getSettings();
			webSettings.setTextZoom(webSettings.getTextZoom() + 10);
			return true;

		case R.id.index:
			openIndex();
			return true;

		default:
			return false;
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// launchChapter(mTitleChapters.get(position));
			mHelper.hide();
			mDrawerLayout.closeDrawer(mChapterListView);
		}
	}

	public void openIndex() {
		if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
			mDrawerLayout.closeDrawer(Gravity.RIGHT);
		} else {
			mDrawerLayout.openDrawer(Gravity.RIGHT);
		}
	}

	@Override
	public void onBackPressed() {
		if (contentRequest != null) {
			contentRequest.cancel(true);
		}
		super.onBackPressed();
	}

	private void requestContent() {
		contentRequest = new HttpGet(this, PConstants.CONTENT_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.CONTENT_URL);
		requestHashMap.put("pratilipiId", "" + mShelfContent.pratilipiId);
		requestHashMap.put("pageNo", String.valueOf(mPratilipiPageNo));

		contentRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.CONTENT_URL.equals(getUrl)) {
			if (finalResult != null) {
				try {
					final String content = finalResult.getString("pageContent");
					if (content != null) {
						PageContent pageContent = new PageContent(0, mBook.id,
								mPratilipiPageNo, content,
								mShelfContent.language);
						mDataSource.createPageContent(pageContent);
						if (mPratilipiPageNo < 2) {
							if (mPageLoaded) {
								mWebView.loadUrl("javascript:paginate(\""
										+ content.replace("\"", "'") + "\")");
							} else {
								mPageLoadContent = content;
							}
						} else {
							mWebView.loadUrl("javascript:append(\""
									+ content.replace("\"", "'") + "\")");
						}
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

	@Override
	public void onResume() {
		if (mDataSource != null) {
			mDataSource.open();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		if (mDataSource != null) {
			mDataSource.close();
		}
		super.onDestroy();
	}

	class JavaScriptInterface {

		ReaderActivity activity;
		PageContent pageContent;

		public JavaScriptInterface(ReaderActivity activity) {
			this.activity = activity;
		}

		@JavascriptInterface
		public void setCurrentPage(int currentPage) {
			mCurrentPage = currentPage;
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mChapterTextView.setText("Page " + (mCurrentPage + 1)
							+ " out of " + mTotalPages);
					mSeekBar.setProgress(mCurrentPage);
				}
			});
		}

		@JavascriptInterface
		public void setTotalPages(int totalPages) {
			mTotalPages = totalPages;

			if (mPratilipiPageNo < mBook.pageCount) {
				++mPratilipiPageNo;
				pageContent = mDataSource.getPageContent(
						mShelfContent.pratilipiId, mPratilipiPageNo);
				if (pageContent == null) {
					requestContent();
				}
			}
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mChapterTextView.setText("Page " + (mCurrentPage + 1)
							+ " out of " + mTotalPages);
					mSeekBar.setMax(mTotalPages - 1);
					if (pageContent != null) {
						mWebView.loadUrl("javascript:append(\""
								+ pageContent.content.replace("\"", "'")
								+ "\")");
					}
				}
			});
		}
	}

	@Override
	public Boolean setPutStatus(JSONObject finalResult, String putUrl,
			int responseCode) {
		return null;
	}

}
