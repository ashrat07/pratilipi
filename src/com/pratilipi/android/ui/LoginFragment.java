package com.pratilipi.android.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpPost;
import com.pratilipi.android.model.Login;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class LoginFragment extends BaseFragment {

	public static final String TAG_NAME = "Login";
	private String userName = "";
	private String pwd = "", failureMsg = "";
	private AppState mAppState;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mRootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		Button loginBtnView = (Button) mRootView.findViewById(R.id.login_btn);
		TextView userNameTextView = (TextView) mRootView
				.findViewById(R.id.login_name);
		TextView pwdTextView = (TextView) mRootView
				.findViewById(R.id.login_name);
		userName = userNameTextView.getText().toString();
		pwd = pwdTextView.getText().toString();

		AppState.init(mParentActivity);
		mAppState = AppState.getInstance();

		loginBtnView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mParentActivity.mLoginManager.requestTryLogin(userName, pwd);

			}
		});

		return mRootView;
	}

	
}
