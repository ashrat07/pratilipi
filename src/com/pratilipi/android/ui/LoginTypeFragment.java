package com.pratilipi.android.ui;

import com.pratilipi.android.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LoginTypeFragment extends BaseFragment {

	private String TAG="Login types";
	private View mLoginTypeView;
	
	@Override
	public String getCustomTag() {
		return TAG;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mLoginTypeView= inflater.inflate(R.layout.fragment_login_types, null);
		return mLoginTypeView;
	}

}
