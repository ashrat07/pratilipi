package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pratilipi.android.R;
import com.pratilipi.android.widget.SlidingTabLayout;

public class ProfileLanguageFragment extends BaseFragment {

	public static final String TAG_NAME = "Profile Language";

	private View mRootView;
	private SlidingTabLayout mSlidingTab;
	private ViewPager mViewPager;

	private Integer[] tabIds = new Integer[] { R.string.content_language,
			R.string.menu_language };

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(
				R.layout.fragment_profile_language_selection, container, false);
		mViewPager = (ViewPager) mRootView
				.findViewById(R.id.profile_language_select_pager);
		mViewPager.setAdapter(new ProfileLanguageFragmentPagerAdapter(
				mParentActivity.getSupportFragmentManager(), mParentActivity));

		mSlidingTab = (SlidingTabLayout) mRootView
				.findViewById(R.id.profile_language_select_slide);
		mSlidingTab.setViewPager(mViewPager);

		return mRootView;
	}
}
