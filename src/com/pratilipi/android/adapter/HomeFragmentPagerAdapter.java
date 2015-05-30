package com.pratilipi.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pratilipi.android.R;
import com.pratilipi.android.ui.CategoryFragment;
import com.pratilipi.android.ui.ProfileFragment;
import com.pratilipi.android.ui.StoreFragment;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 3;

	private int tabTitles[] = new int[] { R.string.home, R.string.categories,
			R.string.profiles };
	private Context context;

	public HomeFragmentPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment;
		switch (position) {
		case 0:
			fragment = new StoreFragment();
			break;

		case 1:
			fragment = new CategoryFragment();
			break;

		case 2:
			fragment = new ProfileFragment();
			break;

		default:
			fragment = new CategoryFragment();
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return PAGE_COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return context.getResources().getString(tabTitles[position]);
	}

}
