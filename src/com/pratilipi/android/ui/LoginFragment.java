package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pratilipi.android.R;
import com.pratilipi.android.iHelper.IHttpResponseHelper;

public class LoginFragment extends BaseFragment implements IHttpResponseHelper {

	public static final String TAG_NAME = "Login";

	private View mRootView;
	private EditText mUserNameEditText;
	private EditText mPwdEditText;
	private Button mRegisterButton;

	private String userName = "";
	private String pwd = "";

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_login, container, false);
		Button loginBtnView = (Button) mRootView.findViewById(R.id.login_btn);
		mUserNameEditText = (EditText) mRootView.findViewById(R.id.login_name);
		mPwdEditText = (EditText) mRootView.findViewById(R.id.login_pwd);
		mRegisterButton = (Button) mRootView.findViewById(R.id.login_register);

		loginBtnView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = mUserNameEditText.getText().toString();
				pwd = mPwdEditText.getText().toString();
				if (userName.isEmpty() || pwd.isEmpty()) {
					mParentActivity
							.showError("Please enter user name and password!!");
					return;
				}
				makeRequest();
			}
		});

		mRegisterButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mParentActivity.showNextView(new RegisterFragment(), null);
			}
		});

		return mRootView;
	}

	@Override
	public void makeRequest() {
		mParentActivity.showProgressBar();
		mParentActivity.mLoginManager.loginRequest(userName, pwd, this);
	}

	@Override
	public void responseSuccess() {
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, "Login Success", Toast.LENGTH_SHORT)
				.show();
		mParentActivity.mStack.pop();
	}

	@Override
	public void responseFailure(String failureMessage) {
		mParentActivity.hideProgressBar();
		mParentActivity.showError(failureMessage);
	}

}
