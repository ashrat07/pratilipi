package com.pratilipi.android.ui;

import java.util.Vector;

import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;

import com.pratilipi.android.R;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.ImageLoader;
import com.pratilipi.android.util.PStack;
import com.pratilipi.android.util.PThreadPool;
import com.pratilipi.android.util.PopupErrorRunner;

public class SplashActivity extends Activity {

	public View mProgressBarParent;

	public AppState mApp;
	private Handler mUIHandler;
	public PStack mStack;
	private Vector<Runnable> runners = new Vector<>();
	private Boolean isUISaved = false;

	public ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FontManager.initialize(getApplicationContext());

		AppState.init(getApplicationContext());
		mApp = AppState.getInstance();
		mUIHandler = new Handler();
		PThreadPool.init(mUIHandler);

		mStack = new PStack(getFragmentManager());

		setContentView(R.layout.activity_splash);

		mProgressBarParent = findViewById(R.id.progress_bar_parent);

		mImageLoader = new ImageLoader(this);

		if (TextUtils.isEmpty(mApp.getLanguage())) {
			showNextView(new LanguageSelectionFragment());
		} else {
			showNextView(new StoreFragment());
		}
	}

	@Override
	public void onBackPressed() {
		if (mStack.getCount() > 1) {
			BaseFragment fragment = (BaseFragment) mStack.getTopFragment();
			if (fragment != null) {
				fragment.onBackPressed();
			}

			doTransaction(new Runnable() {

				@Override
				public void run() {
					mStack.pop();
				}
			});
		}
	}

	public void setLayoutBackgroundColor(int color) {
		findViewById(R.id.base).setBackgroundColor(color);
	}

	public Boolean setPostStatus(JSONObject finalResult, String url,
			int responseCode) {
		return null;
	}

	public Boolean setGetStatus(JSONObject finalResult, String url,
			int responseCode) {
		return null;
	}

	public void showProgressBar() {
		if (mProgressBarParent != null
				&& mProgressBarParent.getVisibility() != View.VISIBLE) {
			mProgressBarParent.setVisibility(View.VISIBLE);
		}
	}

	public void hideProgressBar() {
		if (mProgressBarParent != null
				&& mProgressBarParent.getVisibility() == View.VISIBLE) {
			mProgressBarParent.setVisibility(View.GONE);
		}
	}

	public void showNextView(BaseFragment fragment, Bundle bundle) {
		if (bundle != null)
			fragment.setArguments(bundle);
		showNextView(fragment);
	}

	public void showNextView(BaseFragment fragment, Bundle bundle,
			boolean disableAnimation) {
		fragment.setArguments(bundle);
		showNextView(fragment, disableAnimation);
	}

	public void showNextView(BaseFragment fragment, boolean disableAnimation) {
		if (mStack.getTopFragment() == null
				|| !fragment.getCustomTag().equals(mStack.getTopFragmentName())) {
			Runnable runner = new Transaction(fragment, disableAnimation);
			doTransaction(runner);
		}
	}

	/**
	 * Adds the fragment in navigation if not existing
	 * 
	 * @param fragment
	 */
	public void showNextView(BaseFragment fragment) {
		boolean disableAnimation = false;
		if (mStack.getTopFragment() == null
				|| !fragment.getCustomTag().equals(mStack.getTopFragmentName())) {
			Runnable runner = new Transaction(fragment, disableAnimation);
			doTransaction(runner);
		}
	}

	class Transaction implements Runnable {

		BaseFragment mFragment;
		boolean mDisableAnimation = false;
		boolean noBackEntry = false;

		Transaction(BaseFragment fragment, boolean disableAnimation) {
			this.mFragment = fragment;
			this.mDisableAnimation = disableAnimation;
		}

		@Override
		public void run() {
			FragmentTransaction trans;
			trans = mStack.getInstance().beginTransaction();
			trans.replace(R.id.container, mFragment, mFragment.getCustomTag());
			if (!isUISaved) {
				trans.addToBackStack(mFragment.getCustomTag())
						.commitAllowingStateLoss();
			}
			getFragmentManager().executePendingTransactions();
			mStack.setTopFragment(mFragment, mFragment.getCustomTag());
		}
	}

	private void doTransaction(Runnable runner) {
		doTransaction(runner, 0);
	}

	public void doTransaction(Runnable runner, long delayInMillis) {
		if (isUISaved) {
			runners.add(runner);
		} else if (delayInMillis == 0) {
			mUIHandler.post(runner);
		} else {
			mUIHandler.postDelayed(runner, delayInMillis);
		}
	}

	public void showError(String body) {
		showError(new SpannableString(body));
	}

	public void showError(SpannableString body) {
		showError(new SpannableString(""), body);
	}

	public void showError(SpannableString header, SpannableString body) {
		doTransaction(new PopupErrorRunner(this, header, body));
	}
}
