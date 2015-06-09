package com.pratilipi.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pratilipi.android.R;
import com.pratilipi.android.ui.StoreCategoryFragment;
import com.pratilipi.android.ui.StoreHomeFragment;

public class StoreFragmentPagerAdapter extends FragmentPagerAdapter {

	private final int PAGE_COUNT = 2;
	private final int tabTitles[] = new int[] { R.string.home,
			R.string.categories };

	private Context context;

	public StoreFragmentPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	@Override
	public Fragment getItem(int position) {
		Fragment fragment;
		switch (position) {
		case 0:
			fragment = new StoreHomeFragment();
			break;

		case 1:
			fragment = new StoreCategoryFragment();
			break;

		default:
			fragment = new StoreCategoryFragment();
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
