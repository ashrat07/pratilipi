package com.pratilipi.android.ui;

import java.util.Vector;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pratilipi.android.R;
import com.pratilipi.android.provider.PSuggestionProvider;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.LoginManagerListner;
import com.pratilipi.android.util.PStack;
import com.pratilipi.android.util.PThreadPool;
import com.pratilipi.android.util.PopupErrorRunner;
import com.pratilipi.android.util.RegisterManagerListner;

public class SplashActivity extends FragmentActivity implements
		OnBackStackChangedListener {

	private View mProgressBarParent;
	private Menu mMenu;

	public AppState mApp;
	private Handler mUIHandler;
	public PStack mStack;
	private Vector<Runnable> runners = new Vector<>();
	private Boolean isUISaved = false;
	public LoginManagerListner mLoginManager= new LoginManagerListner();
	public RegisterManagerListner mRegisterManager= new RegisterManagerListner();
	
	public ImageLoader mImageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(null);
		FontManager.initialize(getApplicationContext());

		AppState.init(getApplicationContext());
		mApp = AppState.getInstance();

		mUIHandler = new Handler();
		PThreadPool.init(mUIHandler);
		mStack = new PStack(getSupportFragmentManager());

		setContentView(R.layout.activity_splash);
		mProgressBarParent = findViewById(R.id.progress_bar_parent);
		getSupportFragmentManager().addOnBackStackChangedListener(this);
		shouldDisplayHomeUp();

		DisplayImageOptions opt = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true)
				.resetViewBeforeLoading(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).defaultDisplayImageOptions(opt)
				.build();
		ImageLoader.getInstance().init(config);
		mImageLoader = ImageLoader.getInstance();

		handleSearchIntent(getIntent());

		if (TextUtils.isEmpty(mApp.getContentLanguageHashCode())) {
			showNextView(new LanguageSelectionFragment());
		} else {
			showNextView(new StoreFragment());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		handleSearchIntent(intent);
	}

	private void handleSearchIntent(Intent intent) {

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
					this, PSuggestionProvider.AUTHORITY,
					PSuggestionProvider.MODE);
			suggestions.saveRecentQuery(query, null);

			SearchView searchView = (SearchView) mMenu.findItem(R.id.search)
					.getActionView();
			searchView.setQuery(query, false);
			searchView.clearFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

			if (SearchFragment.TAG_NAME.equals(mStack.getTopFragmentName())) {
				SearchFragment fragment = (SearchFragment) mStack
						.getTopFragment();
				fragment.refresh(query);
			} else {
				Bundle bundle = new Bundle();
				bundle.putString("QUERY", query);
				showNextView(new SearchFragment(), bundle);
			}
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
		getMenuInflater().inflate(R.menu.splash_menu, menu);
		mMenu = menu;

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

			if (mStack.getCount() == 2
					&& mMenu.findItem(R.id.search).isActionViewExpanded()) {
				mMenu.findItem(R.id.search).collapseActionView();
			}

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

	public void setActionBarTitle(String title) {
		setTitle(title);
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
