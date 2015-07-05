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

public class RegisterFragment extends BaseFragment implements IHttpResponseHelper {

	public static String TAG_NAME = "Register";
	private View mRegisterView;
	private EditText userNameEditView;
	private EditText userPwdEditView;
	private EditText userEmailEditView;
	String userName="", userEmail="",userPwd="";
	
	
	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 mRegisterView= (View)inflater.inflate(R.layout.fragment_register, container,false);
		 Button registerBtn= (Button) mRegisterView.findViewById(R.id.register_btn);
		 userNameEditView= (EditText) mRegisterView.findViewById(R.id.register_name);
		 userPwdEditView= (EditText)mRegisterView.findViewById(R.id.register_pwd);
		 userEmailEditView= (EditText)mRegisterView.findViewById(R.id.register_email);
		 
		 registerBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			    userName = "Test2";// commented for testing: userNameEditView.getText().toString().trim().toLowerCase();
			    userEmail = "Test2@gmail.com";//userEmailEditView.getText().toString().trim().toLowerCase();
			    userPwd = "test2";//userPwdEditView.getText().toString().trim().toLowerCase();
				if(userName.isEmpty())
				{
					mParentActivity.showError("User name cannot be kept empty, please enter a user name.");
					return;
				}
				else if(userPwd.isEmpty())
				{
					mParentActivity.showError("Password cannot be kept empty, please enter a password.");
					return;
				}
				else if(userEmail.isEmpty())
				{
					mParentActivity.showError("Email id cannot be kept empty, please enter an email id.");
					return;
				}
				makeRequest();
				
			}
		});
		return mRegisterView;
	}

	@Override
	public void responseSuccess() {
		// TODO Auto-generated method stub
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, "Registered", 10).show();
	}

	@Override
	public void responseFailure(String failureMessage) {
		// TODO Auto-generated method stub
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, "Registration Failure: "+failureMessage, 10).show();
	}

	@Override
	public void makeRequest() {
		mParentActivity.showProgressBar();
		mParentActivity.mRegisterManager.registerUserRequest(userName, userPwd, userEmail, this);
		
	}

	@Override
	public Boolean setPutStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
