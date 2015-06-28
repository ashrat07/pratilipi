package com.pratilipi.android.adapter;

import java.util.List;

import com.pratilipi.android.ui.PageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TextPagerAdapter extends FragmentPagerAdapter {

	private final List<CharSequence> pageTexts;

	public TextPagerAdapter(FragmentManager fm, List<CharSequence> pageTexts) {
		super(fm);
		this.pageTexts = pageTexts;
	}

	@Override
	public Fragment getItem(int i) {
		return PageFragment.newInstance(pageTexts.get(i));
	}

	@Override
	public int getCount() {
		return pageTexts.size();
	}

}
