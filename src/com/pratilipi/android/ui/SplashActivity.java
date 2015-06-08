package com.pratilipi.android.ui;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.ImageLoader;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PStack;
import com.pratilipi.android.util.PThreadPool;
import com.pratilipi.android.util.PUtils;
import com.pratilipi.android.util.PopupErrorRunner;

public class SplashActivity extends FragmentActivity {

	public View mProgressBarParent;
	private Menu menu;
	private List<String> items = Arrays.asList("One", "Two", "Two and a half",
			"Three", "Four");

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

		mImageLoader = new ImageLoader(this);

		if (TextUtils.isEmpty(mApp.getLanguageHashCode())) {
			showNextView(new LanguageSelectionFragment());
		} else {
			showNextView(new HomeFragment());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);
		this.menu = menu;

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));

		menu.findItem(R.id.profile).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showNextView(new ProfileFragment());
						return false;
					}
				});

		menu.findItem(R.id.shelf).setOnMenuItemClickListener(
				new MenuItem.OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						showNextView(new ShelfFragment());
						return false;
					}
				});

		return true;
	}

	private void loadHistory(String query) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			// Cursor
			String[] columns = new String[] { "_id", "text" };
			Object[] temp = new Object[] { 0, "default" };

			MatrixCursor cursor = new MatrixCursor(columns);

			for (int i = 0; i < items.size(); i++) {
				temp[0] = i;
				temp[1] = items.get(i);
				cursor.addRow(temp);
			}

			// SearchView
			SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

			final SearchView search = (SearchView) menu.findItem(R.id.search)
					.getActionView();
			search.setSearchableInfo(manager
					.getSearchableInfo(getComponentName()));

			search.setSuggestionsAdapter(new ExampleAdapter(this, cursor, items));

		}

	}

	public class ExampleAdapter extends CursorAdapter {

		private List<String> items;
		private TextView text;

		public ExampleAdapter(Context context, Cursor cursor, List<String> items) {
			super(context, cursor, false);
			this.items = items;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			text.setText(items.get(cursor.getPosition()));
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.item, parent, false);
			text = (TextView) view.findViewById(R.id.text);
			return view;
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
