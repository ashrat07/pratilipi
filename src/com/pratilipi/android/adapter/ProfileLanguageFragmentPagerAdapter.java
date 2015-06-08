package com.pratilipi.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pratilipi.android.R;
import com.pratilipi.android.ui.ContentMenuLanguageFragment;

public class ProfileLanguageFragmentPagerAdapter extends FragmentPagerAdapter {

	private Context context;

	private static final int PAGE_COUNT = 2;
	private Integer[] tabTitles = new Integer[] { R.string.menu_language,
			R.string.content_language };

	public ProfileLanguageFragmentPagerAdapter(FragmentManager fm,
			Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = new ContentMenuLanguageFragment();
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
