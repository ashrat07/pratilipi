package com.pratilipi.android.ui;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pratilipi.android.R;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.util.AppState;

public class LoginFragment extends BaseFragment implements IHttpResponseHelper {

	public static final String TAG_NAME = "Login";
	private String userName = "";
	private String pwd = "";
	private EditText userNameTextView;
	private EditText pwdTextView;
	private View mRootView;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_login, container, false);
		Button loginBtnView = (Button) mRootView.findViewById(R.id.login_btn);
		userNameTextView = (EditText) mRootView.findViewById(R.id.login_name);
		pwdTextView = (EditText) mRootView.findViewById(R.id.login_pwd);
		Button registerBtnView = (Button)mRootView.findViewById(R.id.login_register);

		loginBtnView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = userNameTextView.getText().toString();
				pwd = pwdTextView.getText().toString();
				if (userName.isEmpty() || pwd.isEmpty()) {
					mParentActivity
							.showError("Please enter user name and password!!");
					return;
				}
				makeRequest();

			}
		});

		registerBtnView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
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
		// TODO Auto-generated method stub
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, "Login Success", 10).show();;
	}

	@Override
	public void responseFailure(String failureMessage) {
		mParentActivity.hideProgressBar();
		mParentActivity.showError(failureMessage);
	}


}
