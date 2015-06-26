package com.pratilipi.android.ui;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Shelf;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.SystemUiHider;

public class ReaderActivity extends Activity implements HttpResponseListener {

	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = false;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_FULLSCREEN;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private WebView mWebView;

	private Shelf mShelf;

	private float mDownX;
	private float mDownY;
	private Boolean isOnClick;
	private float SCROLL_THRESHOLD = 30;

	// private static final int MAX_CLICK_DURATION = 200;
	// private long startClickTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_reader);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		mWebView = (WebView) findViewById(R.id.web_view);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, mWebView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
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

						// if (visible && AUTO_HIDE) {
						// // Schedule a hide().
						// delayedHide(AUTO_HIDE_DELAY_MILLIS);
						// }
					}
				});

		mWebView.setOnTouchListener(new View.OnTouchListener() {

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
						if (TOGGLE_ON_CLICK) {
							mSystemUiHider.toggle();
						} else {
							mSystemUiHider.show();
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

		// mWebView.setOnTouchListener(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// switch (event.getAction()) {
		// case MotionEvent.ACTION_DOWN: {
		// startClickTime = Calendar.getInstance().getTimeInMillis();
		// break;
		// }
		//
		// case MotionEvent.ACTION_UP: {
		// long clickDuration = Calendar.getInstance()
		// .getTimeInMillis() - startClickTime;
		// if (clickDuration < MAX_CLICK_DURATION) {
		// if (TOGGLE_ON_CLICK) {
		// mSystemUiHider.toggle();
		// } else {
		// mSystemUiHider.show();
		// }
		// }
		// }
		//
		// default:
		// break;
		//
		// }
		// return false;
		// }
		// });

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);

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

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
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
			try {
				String pageContent = finalResult.getString("pageContent");
				String font;
				if (PConstants.CONTENT_LANGUAGE.HINDI.toString().equals(
						mShelf.language)) {
					font = "Mangal.ttf";
				} else if (PConstants.CONTENT_LANGUAGE.TAMIL.toString().equals(
						mShelf.language)) {
					font = "Tamil.ttf";
				} else if (PConstants.CONTENT_LANGUAGE.GUJARATI.toString()
						.equals(mShelf.language)) {
					font = "Gujarati.ttf";
				} else {
					font = "Montserrat-Regular.ttf";
				}

				this.copyFile(this, font);
				mWebView.loadDataWithBaseURL(null,
						getHtmlData(this, font, pageContent), "text/html",
						"utf-8", "about:blank");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private boolean copyFile(Context context, String font) {
		boolean status = false;
		try {
			FileOutputStream out = context.openFileOutput(font,
					Context.MODE_PRIVATE);
			InputStream in = context.getAssets().open("fonts/" + font);
			// Transfer bytes from the input file to the output file
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			// Close the streams
			out.close();
			in.close();
			status = true;
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}

	private String getHtmlData(Context context, String font, String data) {
		String head = "<head><style>@font-face { font-family: 'customFont'; src: url('file://"
				+ context.getFilesDir().getAbsolutePath()
				+ "/"
				+ font
				+ "');}body {font-family: 'customFont';}</style></head>";
		String htmlData = "<html>" + head + "<body>" + data + "</body></html>";
		return htmlData;
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

}
