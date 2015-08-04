package com.pratilipi.android.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jedi.functional.Command;
import jedi.functional.Functor;
import jedi.functional.Functor2;
import jedi.option.Option;
import jedi.option.OptionMatcher;
import roboguice.RoboGuice;
import roboguice.inject.InjectView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.google.inject.Inject;
import com.pratilipi.android.R;
import com.pratilipi.android.util.Configuration;
import com.pratilipi.android.util.PageTurner;
import com.pratilipi.android.util.UiUtils;
import com.pratilipi.android.view.NavigationCallback;
//import com.limecreativelabs.sherlocksupport.ActionBarDrawerToggleCompat;

//import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockFragmentActivity;

/**
 * Superclass for all PageTurner activity classes.
 */
public abstract class PageTurnerActivity extends FragmentActivity {

	@InjectView(R.id.drawer_layout)
	private DrawerLayout mDrawer;

	@InjectView(R.id.left_drawer)
	private ExpandableListView mDrawerOptions;

	private ActionBarDrawerToggle mToggle;

	private NavigationAdapter adapter;

	private CharSequence originalTitle;

	private boolean drawerIsOpen;

	@Inject
	private Configuration config;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {

		Configuration config = RoboGuice.getInjector(this).getInstance(
				Configuration.class);
		PageTurner.changeLanguageSetting(this, config);

		setTheme(getTheme(config));
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		super.onCreate(savedInstanceState);

		setContentView(getMainLayoutResource());

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// set up the drawer's list view with items and click listener

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		initDrawerItems(mDrawerOptions);

		mToggle = new ActionBarDrawerToggle(this, mDrawer,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				PageTurnerActivity.this.onDrawerClosed(view);
			}

			public void onDrawerOpened(View drawerView) {
				PageTurnerActivity.this.onDrawerOpened(drawerView);
			}
		};

		mToggle.setDrawerIndicatorEnabled(true);
		mDrawer.setDrawerListener(mToggle);

		onCreatePageTurnerActivity(savedInstanceState);
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		int action = event.getAction();
		int keyCode = event.getKeyCode();

		if (action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK
				&& isDrawerOpen()) {
			closeNavigationDrawer();
			return true;
		}

		return super.dispatchKeyEvent(event);
	}

	protected void closeNavigationDrawer() {
		mDrawer.closeDrawers();
	}

	protected NavigationAdapter getAdapter() {
		return this.adapter;
	}

	protected void initDrawerItems(ExpandableListView expandableListView) {
		if (expandableListView != null) {

			// this.adapter = new NavigationAdapter( this, getMenuItems(config),
			// this::createExpandableListView, 0);
			this.adapter = new NavigationAdapter(
					this,
					getMenuItems(config),
					new Functor2<List<NavigationCallback>, Integer, ExpandableListView>() {

						@Override
						public ExpandableListView execute(
								List<NavigationCallback> items, Integer level) {
							return createExpandableListView(items, level);
						}
					}, 0);

			setClickListeners(expandableListView, this.adapter);
		}
	}

	private ExpandableListView createExpandableListView(
			List<NavigationCallback> items, int level) {
		ExpandableListView e = new ExpandableListView(this) {
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				/*
				 * Adjust height
				 */
				heightMeasureSpec = MeasureSpec.makeMeasureSpec(10000,
						MeasureSpec.AT_MOST);
				super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			}
		};
		// setClickListeners(e, new NavigationAdapter(this, items,
		// this::createExpandableListView, level ));
		setClickListeners(
				e,
				new NavigationAdapter(
						this,
						items,
						new Functor2<List<NavigationCallback>, Integer, ExpandableListView>() {

							@Override
							public ExpandableListView execute(
									List<NavigationCallback> items,
									Integer level) {
								return createExpandableListView(items, level);
							}
						}, level));
		return e;
	}

	private void setClickListeners(ExpandableListView expandableListView,
			final NavigationAdapter adapter) {

		expandableListView.setAdapter(adapter);

		// expandableListView.setOnGroupClickListener(
		// (e, v, groupId, l) -> this.onGroupClick(adapter, groupId) );
		expandableListView
				.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						PageTurnerActivity.this.onGroupClick(adapter,
								groupPosition);
						return false;
					}
				});

		// expandableListView.setOnChildClickListener(
		// (e, v, groupId, childId, l) -> this.onChildClick(adapter, groupId,
		// childId));
		expandableListView
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						PageTurnerActivity.this.onChildClick(adapter,
								groupPosition, childPosition);
						return false;
					}
				});

		// expandableListView.setOnItemLongClickListener(
		// (av, v, position, id) -> this.onItemLongClick(adapter, position,
		// id));
		expandableListView
				.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, int position, long id) {
						PageTurnerActivity.this.onItemLongClick(adapter,
								position, id);
						return false;
					}
				});

		expandableListView.setGroupIndicator(null);
	}

	protected abstract int getMainLayoutResource();

	protected void onCreatePageTurnerActivity(Bundle savedInstanceState) {

	}

	protected void beforeLaunchActivity() {

	}

	protected int getTheme(Configuration config) {
		return config.getTheme();
	}

	protected List<NavigationCallback> getMenuItems(Configuration config) {

		List<NavigationCallback> result = new ArrayList<>();

		if (new File(config.getLastOpenedFile()).exists()) {
			String nowReading = getString(R.string.now_reading,
					config.getLastReadTitle());
			result.add(navigate(nowReading, ReadingActivity.class));
		}

		result.add(navigate(getString(R.string.open_library),
				LibraryActivity.class));
		result.add(navigate(getString(R.string.download), CatalogActivity.class));

		// result.add( new
		// NavigationCallback(getString(R.string.prefs)).setOnClick(this::startPreferences));
		result.add(new NavigationCallback(getString(R.string.prefs))
				.setOnClick(new UiUtils.Action() {

					@Override
					public void perform() {
						startPreferences();
					}
				}));

		return result;
	}

	protected void startPreferences() {
		Intent intent = new Intent(this, PageTurnerPrefsActivity.class);
		beforeLaunchActivity();
		startActivity(intent);
	}

	protected NavigationCallback navigate(String title,
			final Class<? extends PageTurnerActivity> classToStart) {
		// return new NavigationCallback( title ).setOnClick(
		// () -> launchActivity(classToStart));
		return new NavigationCallback(title).setOnClick(new UiUtils.Action() {

			@Override
			public void perform() {
				launchActivity(classToStart);
			}
		});
	}

	@SuppressLint("NewApi")
	public void onDrawerClosed(View view) {
		this.drawerIsOpen = false;
		getActionBar().setTitle(originalTitle);
		invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	}

	@SuppressLint("NewApi")
	public void onDrawerOpened(View drawerView) {

		this.drawerIsOpen = true;
		this.originalTitle = getActionBar().getTitle();

		getActionBar().setTitle(R.string.app_name);
		invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
	}

	protected boolean isDrawerOpen() {
		return drawerIsOpen;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		initDrawerItems(mDrawerOptions);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setProgressBarIndeterminate(true);
		setProgressBarIndeterminateVisibility(false);

		mToggle.syncState();
	}

	public void launchActivity(Class<? extends PageTurnerActivity> activityClass) {
		Intent intent = new Intent(this, activityClass);

		beforeLaunchActivity();

		config.setLastActivity(activityClass);

		startActivity(intent);
		finish();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// I think there's a bug in mToggle.onOptionsItemSelected, because it
		// always returns false.
		// The item id testing is a fix.
		if (mToggle.onOptionsItemSelected(item)
				|| item.getItemId() == android.R.id.home) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	protected boolean onGroupClick(NavigationAdapter adapter, int groupId) {

		// Option<Boolean> group =adapter.findGroup(groupId).map(g -> {
		// if (g.hasChildren()) {
		// return false; //Let the superclass handle it and expand the group
		// } else {
		// g.onClick();
		// closeNavigationDrawer();
		// return true;
		// }
		// });
		Option<Boolean> group = adapter.findGroup(groupId).map(
				new Functor<NavigationCallback, Boolean>() {

					public Boolean execute(NavigationCallback g) {
						if (g.hasChildren()) {
							return false; // Let the superclass handle it and
											// expand the group
						} else {
							g.onClick();
							closeNavigationDrawer();
							return true;
						}
					};
				});

		return group.getOrElse(false);
	}

	protected boolean onChildClick(NavigationAdapter adapter, int groupId,
			int childId) {

		Option<NavigationCallback> childItem = adapter.findChild(groupId,
				childId);

		// childItem.forEach(item -> {
		// if ( ! item.hasChildren() ) {
		// item.onClick();
		// closeNavigationDrawer();
		// }
		// });
		childItem.forEach(new Command<NavigationCallback>() {

			@Override
			public void execute(NavigationCallback item) {
				if (!item.hasChildren()) {
					item.onClick();
					closeNavigationDrawer();
				}
			}
		});

		return false;
	}

	protected boolean onItemLongClick(NavigationAdapter adapter,
			final int position, final long id) {

		if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
			int groupPosition = ExpandableListView.getPackedPositionGroup(id);
			int childPosition = getAdapter().getIndexForChildId(groupPosition,
					ExpandableListView.getPackedPositionChild(id));

			Option<NavigationCallback> childItem = adapter.findChild(
					groupPosition, childPosition);

			// childItem.match(
			// NavigationCallback::onLongClick,
			// () -> LOG.error( "Could not get child-item for " + position +
			// " and id " + id )
			// );
			childItem.match(new OptionMatcher<NavigationCallback>() {

				@Override
				public void caseNone() {
					// Could not get child-item
				}

				@Override
				public void caseSome(NavigationCallback c) {
					c.onLongClick();
				}
			});

			closeNavigationDrawer();
			return true;
		}

		return false;
	}
}
