package com.pratilipi.android.adapter;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pratilipi.android.ui.PageFragment;

public class TextPagerAdapter extends FragmentPagerAdapter {

	private final static String PAGE_TEXT = "PAGE_TEXT";
	private final static String TYPEFACE = "TYPEFACE";
	private final static String PAGE_NO = "PAGE_NO";
	private final static String TOTAL_PAGES = "TOTAL_PAGES";

	private final List<CharSequence> pageTexts;
	private String typeface;

	public TextPagerAdapter(FragmentManager fm, List<CharSequence> pageTexts,
			String typeface) {
		super(fm);
		this.pageTexts = pageTexts;
		this.typeface = typeface;
	}

	@Override
	public Fragment getItem(int i) {
		Bundle bundle = new Bundle();
		bundle.putCharSequence(PAGE_TEXT, pageTexts.get(i));
		bundle.putString(TYPEFACE, typeface);
		bundle.putInt(PAGE_NO, i + 1);
		bundle.putInt(TOTAL_PAGES, pageTexts.size());
		return PageFragment.newInstance(bundle);
	}

	@Override
	public int getCount() {
		return pageTexts.size();
	}

}
