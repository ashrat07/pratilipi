package com.pratilipi.android.ui;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.HomeFragmentPagerAdapter;
import com.pratilipi.android.widget.SlidingTabLayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends BaseFragment {

	private static final String TAG_NAME = "Home";

	private View mRootView;
	private ViewPager mViewPager;
	private SlidingTabLayout mSlidingTabLayout;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_home, container, false);

		mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);
		mViewPager.setAdapter(new HomeFragmentPagerAdapter(mParentActivity
				.getSupportFragmentManager(), mParentActivity));

		mSlidingTabLayout = (SlidingTabLayout) mRootView
				.findViewById(R.id.sliding_tab_layout);
		mSlidingTabLayout.setCustomTabView(R.layout.layout_header_tab_white, 0);
		mSlidingTabLayout.setDistributeEvenly(true);
		mSlidingTabLayout.setViewPager(mViewPager);
		mSlidingTabLayout
				.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

					@Override
					public int getIndicatorColor(int position) {
						return getResources().getColor(android.R.color.white);
					}
				});
		return mRootView;
	}

}