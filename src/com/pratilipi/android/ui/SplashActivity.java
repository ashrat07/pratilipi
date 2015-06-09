package com.pratilipi.android.ui;

import java.util.Vector;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.pratilipi.android.R;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.ImageLoader;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PStack;
import com.pratilipi.android.util.PThreadPool;
import com.pratilipi.android.util.PUtils;
import com.pratilipi.android.util.PopupErrorRunner;

public class SplashActivity extends FragmentActivity implements
		OnBackStackChangedListener {

	public View mProgressBarParent;

	public AppState mApp;
	private Handler mUIHandler;
	public PStack mStack;
	private Vector<Runnable> runners = new Vector<>();
	private Boolean isUISaved = false;

	public ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		FontManager.initialize(getApplicationContext());

		AppState.init(getApplicationContext());
		mApp = AppState.getInstance();
		if (mApp.getLanguage().equals(PConstants.LANGUAGE.GUJARATI.toString())) {
			PUtils.setLocale(this, "gu");
		} else if (mApp.getLanguage().equals(
				PConstants.LANGUAGE.TAMIL.toString())) {
			PUtils.setLocale(this, "ta");
		} else if (mApp.getLanguage().equals(
				PConstants.LANGUAGE.HINDI.toString())) {
			PUtils.setLocale(this, "hi");
		}

		mUIHandler = new Handler();
		PThreadPool.init(mUIHandler);
		mStack = new PStack(getSupportFragmentManager());

		setContentView(R.layout.activity_splash);
		mProgressBarParent = findViewById(R.id.progress_bar_parent);
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		shouldDisplayHomeUp();

		mImageLoader = new ImageLoader(this);

		if (TextUtils.isEmpty(mApp.getLanguageHashCode())) {
			showNextView(new LanguageSelectionFragment());
		} else {
			showNextView(new HomeFragment());
		}
	}

	@Override
	public void onBackStackChanged() {
		shouldDisplayHomeUp();
	}

	public void shouldDisplayHomeUp() {
		// Enable Up button only if there are entries in the back stack
		this.getActionBar().setDisplayHomeAsUpEnabled(mStack.getCount() > 1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;

		case R.id.profile:
			this.showNextView(new ProfileFragment());
			return true;

		case R.id.shelf:
			this.showNextView(new ShelfFragment());
			return true;

		default:
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		if (mStack.getCount() <= 1) {
			finish();
		} else {
			BaseFragment fragment = (BaseFragment) mStack.getTopFragment();
			if (fragment != null) {
				fragment.onBackPressed();
			}

			mStack.pop();
		}
	}

	public void setLayoutBackgroundColor(int color) {
		findViewById(R.id.base).setBackgroundColor(color);
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
