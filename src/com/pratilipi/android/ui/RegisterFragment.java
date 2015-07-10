package com.pratilipi.android.ui;

import org.json.JSONObject;

import com.pratilipi.android.R;
import com.pratilipi.android.iHelper.IHttpResponseHelper;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterFragment extends BaseFragment implements
		IHttpResponseHelper {

	public static String TAG_NAME = "Register";
	private View mRegisterView;
	private EditText mUserNameEditText;
	private EditText mUserPwdEditText;
	private EditText mUserEmailEditText;
	String userName = "", userEmail = "", userPwd = "";

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRegisterView = (View) inflater.inflate(R.layout.fragment_register,
				container, false);
		Button registerBtn = (Button) mRegisterView
				.findViewById(R.id.register_btn);
		mUserNameEditText = (EditText) mRegisterView
				.findViewById(R.id.register_name);
		mUserPwdEditText = (EditText) mRegisterView
				.findViewById(R.id.register_pwd);
		mUserEmailEditText = (EditText) mRegisterView
				.findViewById(R.id.register_email);

		registerBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = mUserNameEditText.getText().toString().trim();
				userEmail = mUserEmailEditText.getText().toString().trim();
				userPwd = mUserPwdEditText.getText().toString().trim();
				if (userName.isEmpty()) {
					mParentActivity
							.showError("User name cannot be kept empty, please enter a user name.");
					return;
				} else if (userPwd.isEmpty()) {
					mParentActivity
							.showError("Password cannot be kept empty, please enter a password.");
					return;
				} else if (userEmail.isEmpty()) {
					mParentActivity
							.showError("Email id cannot be kept empty, please enter an email id.");
					return;
				}
				makeRequest();
			}
		});
		return mRegisterView;
	}

	@Override
	public void responseSuccess() {
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, "Registered", 10).show();
	}

	@Override
	public void responseFailure(String failureMessage) {
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity,
				"Registration Failure: " + failureMessage, 10).show();
	}

	@Override
	public void makeRequest() {
		mParentActivity.showProgressBar();
		mParentActivity.mRegisterManager.registerUserRequest(userName, userPwd,
				userEmail, this);

	}

	@Override
	public Boolean setPutStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		return null;
	}

}
