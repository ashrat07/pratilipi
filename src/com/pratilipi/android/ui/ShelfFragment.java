package com.pratilipi.android.ui;

import com.pratilipi.android.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShelfFragment extends BaseFragment {

	public static final String TAG_NAME = "Shelf";

	private View mRootView;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_shelf, container, false);
		return mRootView;
	}

}
