package com.pratilipi.android.util;

import java.util.HashMap;

import org.json.JSONObject;

import com.pratilipi.android.http.HttpPut;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.model.Login;

public class RegisterManagerListner implements HttpResponseListener {
	IHttpResponseHelper registerHelper;
	private AppState mAppState;
	private String userName;
	private String userPwd;
	
	public void registerUserRequest(String userName, String userPwd,
			String userEmail, IHttpResponseHelper registerHelper) {
		this.registerHelper = registerHelper;
		HttpPut registerPutRequest = new HttpPut(this, PConstants.REGISTER_URL);
		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.REGISTER_URL);
		requestHashMap.put("email", userEmail);
		requestHashMap.put("name", userName);
		requestHashMap.put("password", userPwd);
		registerPutRequest.run(requestHashMap);

	}

	@Override
	public Boolean setPutStatus(JSONObject finalResult, String putUrl,
			int responseCode) {
		if (putUrl.equals(PConstants.REGISTER_URL)) {
			if (finalResult != null) {
				try {
					if (finalResult.has("accessToken")
							&& finalResult.getString("accessToken") != null) {
						mAppState = AppState.getInstance();
						//Assign access token and Login credentials to appstate
						mAppState.setAccessToken(finalResult
								.getString("accessToken"));
						Login loginObject = new Login(userName, userPwd, "");
						mAppState.setUserCredentials(loginObject);
						if (registerHelper != null)
							registerHelper.responseSuccess();

					}
				} catch (Exception e) {
					if (registerHelper != null)
						registerHelper
								.responseFailure("Please check internet connection.");

				}
			} else if (registerHelper != null)
			{
				registerHelper
						.responseFailure("Please check internet connection.");
				return null;
		}}
		return null;
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
