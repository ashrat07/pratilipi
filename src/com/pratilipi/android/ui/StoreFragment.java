package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.StoreFragmentPagerAdapter;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.widget.SlidingTabLayout;

public class StoreFragment extends BaseFragment {

	private static final String TAG_NAME = "Store";

	private View mRootView;
	private ViewPager mViewPager;
	private StoreFragmentPagerAdapter mPagerAdapter;
	private SlidingTabLayout mSlidingTabLayout;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_store, container, false);

		mSlidingTabLayout = (SlidingTabLayout) mRootView
				.findViewById(R.id.sliding_tab_layout);
		mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);

		mPagerAdapter = new StoreFragmentPagerAdapter(
				getChildFragmentManager(), mParentActivity);
		mViewPager.setAdapter(mPagerAdapter);

		if (mParentActivity.mApp.getMenuLanguageTypeface().equals(
				PConstants.MENU_LANGUAGE.HINDI.typeface)) {
			mSlidingTabLayout.setCustomTabView(
					R.layout.layout_header_tab_red_hindi, 0);
		} else if (mParentActivity.mApp.getMenuLanguageTypeface().equals(
				PConstants.MENU_LANGUAGE.TAMIL.typeface)) {
			mSlidingTabLayout.setCustomTabView(
					R.layout.layout_header_tab_red_tamil, 0);
		} else if (mParentActivity.mApp.getMenuLanguageTypeface().equals(
				PConstants.MENU_LANGUAGE.GUJARATI.typeface)) {
			mSlidingTabLayout.setCustomTabView(
					R.layout.layout_header_tab_red_gujarati, 0);
		} else {
			mSlidingTabLayout.setCustomTabView(R.layout.layout_header_tab_red,
					0);
		}
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mViewPager);
		mSlidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

					@Override
					public int getIndicatorColor(int position) {
						return getResources().getColor(R.color.background_red);
					}
				});
		return mRootView;
	}
}
