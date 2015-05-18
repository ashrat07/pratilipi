package com.pratilipi.android.util;

import android.app.Fragment;
import android.app.FragmentManager;

public class PStack {

	FragmentManager mFragmentManager;
	Fragment mTopFragment;
	String mTopFragmentName = "";
	String mBottomFragmentName = "";

	public PStack(FragmentManager mgr) {
		init(mgr);
	}

	public void init(FragmentManager mgr) {
		this.mFragmentManager = mgr;
	}

	/**
	 * 0 for current top, -1 for previous, and so on
	 * 
	 * @param
	 * @return
	 */
	public String getFragmentNameRelative(int relativePos) {
		int index = mFragmentManager.getBackStackEntryCount() - 1 + relativePos;
		if (index >= 0) {
			return mFragmentManager.getBackStackEntryAt(index).getName();
		} else {
			return "";
		}
	}

	public int getCount() {
		return mFragmentManager.getBackStackEntryCount();
	}

	public void pop() {

		if (getCount() <= 1) {
			mTopFragment = null;
			mTopFragmentName = "";
			mBottomFragmentName = "";
		} else {
			String name = getFragmentNameRelative(-1);
			mTopFragment = mFragmentManager.findFragmentByTag(name);
			mTopFragmentName = name;
		}
		mFragmentManager.popBackStackImmediate();
	}

	public void popUntil(String name) {
		mFragmentManager.popBackStackImmediate(name, 0);

		String topName = getFragmentNameRelative(0);
		mTopFragment = mFragmentManager.findFragmentByTag(topName);
		mTopFragmentName = topName;
	}

	public void popAll() {
		mTopFragment = null;
		mTopFragmentName = "";
		mFragmentManager.popBackStackImmediate(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public FragmentManager getInstance() {
		return mFragmentManager;
	}

	public Fragment getTopFragment() {
		return mTopFragment;
	}

	public String getTopFragmentName() {
		if (mTopFragment == null) {
			return "";
		}
		return mTopFragmentName;
	}

	public void setTopFragment(Fragment fragment, String fragmentName) {
		this.mTopFragment = fragment;
		this.mTopFragmentName = fragmentName;
		if (getCount() == 0) {
			this.mBottomFragmentName = fragmentName;
		}
	}

	public String getBottomFragmentName() {
		return mBottomFragmentName;
	}
}
