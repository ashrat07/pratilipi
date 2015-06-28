package com.pratilipi.android.util;

import java.util.HashMap;

import org.json.JSONObject;

import com.pratilipi.android.http.HttpPost;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.model.Login;

public class LoginManagerListner implements HttpResponseListener {
	private AppState mAppState;
	private String userName;
	private String pwd;
	private IHttpResponseHelper loginHelper;

	public void loginRequest(String userName, String pwd, IHttpResponseHelper loginHelper) {
		this.userName = userName;
		this.pwd = pwd;
		this.loginHelper = loginHelper;
		HttpPost loginRequest = new HttpPost(this, PConstants.LOGIN_URL);
		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.LOGIN_URL);
		requestHashMap.put("userId", userName);
		requestHashMap.put("userSecret", pwd);
		loginRequest.run(requestHashMap);  

	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		if (postUrl.equals(PConstants.LOGIN_URL)) {
			if (finalResult != null) {
				try {
					if (finalResult.has("message")) {
						if (loginHelper != null) {
							loginHelper.responseFailure(finalResult.getString("message"));
						}
					} else if (finalResult.has("accessToken")
							&& finalResult.getString("accessToken") != null) {
						//Assign access token and Login credentials to appstate
						mAppState = AppState.getInstance();
						mAppState.setAccessToken(finalResult
								.getString("accessToken"));
						Login loginObject = new Login(userName, pwd, "");
						mAppState.setUserCredentials(loginObject);
						if (loginHelper != null) {
							 loginHelper.responseSuccess();
						}
					}
				} catch (Exception e) {
					loginHelper.responseFailure("Exception "+e);
				}
			}
			else
				if (loginHelper != null) 
					 loginHelper.responseFailure("Please check your internet connection.");
		}
		if (loginHelper != null) 
			 loginHelper.responseSuccess();
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setPutStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
