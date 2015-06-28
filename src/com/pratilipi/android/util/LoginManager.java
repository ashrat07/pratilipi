package com.pratilipi.android.util;

import java.util.HashMap;

import org.json.JSONObject;

import com.pratilipi.android.http.HttpPost;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Login;

public class LoginManager implements HttpResponseListener {
	private AppState mAppState;
	private  String userName;
	private String pwd;
	
	public void requestTryLogin(String userName, String pwd) {
		this.userName= userName;
		this.pwd= pwd;
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
		if (postUrl.equals(PConstants.LOGIN_URL) && finalResult != null) {
			try {
				JSONObject responseJSON = finalResult.getJSONObject("response");
				if (responseJSON != null) {

					if (responseJSON.has("message")) {
						// TODO show error
					} else if (responseJSON.has("accessToken") && responseJSON.getString("accessToken").toLowerCase().trim() != "null") {
						mAppState=AppState.getInstance();
						mAppState.setAccessToken(responseJSON
								.getString("accessToken"));
						Login loginObject= new Login(userName, pwd, "");
						mAppState.setUserCredentials(loginObject);
					}
				}
			} catch (Exception e) {
			}
		}
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
