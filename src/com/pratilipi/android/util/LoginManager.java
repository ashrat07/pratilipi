package com.pratilipi.android.util;

import com.pratilipi.android.http.HttpPost;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.model.Login;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginManager implements HttpResponseListener {

	private AppState mAppState;
	private String userName;
	private String pwd;
	private String loginType;
	private IHttpResponseHelper loginHelper;

	public void pratilipiLoginRequest(String userName, String pwd,
			IHttpResponseHelper loginHelper) {
		this.userName = userName;
		this.pwd = pwd;
		this.loginType = "pratilipi";
		this.loginHelper = loginHelper;

		HttpPost loginRequest = new HttpPost(this, PConstants.LOGIN_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.LOGIN_URL);
		requestHashMap.put("userId", userName);
		requestHashMap.put("userSecret", pwd);

		loginRequest.run(requestHashMap);
	}

	public void googleLoginRequest(String accessToken, String socialId,
			IHttpResponseHelper loginHelper) {
		this.loginType = "google";
		this.loginHelper = loginHelper;

		HttpPost googleLogin = new HttpPost(this, PConstants.LOGIN_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.LOGIN_URL);
		requestHashMap.put("token", accessToken);
		requestHashMap.put("socialId", socialId);
		requestHashMap.put("loginType", "google");

		googleLogin.execute(requestHashMap);
	}

	public void facebookLoginRequest(String accessToken, String socialId,
			IHttpResponseHelper loginHelper) {
		this.loginType = "facebook";
		this.loginHelper = loginHelper;

		HttpPost facebookLogin = new HttpPost(this, PConstants.LOGIN_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.LOGIN_URL);
		requestHashMap.put("token", accessToken);
		requestHashMap.put("socialId", socialId);
		requestHashMap.put("loginType", "facebook");

		facebookLogin.execute(requestHashMap);
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		if (postUrl.equals(PConstants.LOGIN_URL)) {
			if (finalResult != null) {
				try {
					if (finalResult.has("message")) {
						if (loginHelper != null) {
							loginHelper.responseFailure(finalResult
									.getString("message"));
						}
					} else if (finalResult.has("accessToken")) {
						String accessToken = finalResult
								.getString("accessToken");
						if (accessToken != null) {
							mAppState = AppState.getInstance();
							mAppState.setAccessToken(finalResult
									.getString("accessToken"));

							switch (loginType) {
							case "google":
								userName = finalResult.getString("userName");
								mAppState.setUserCredentials(new Login(
										userName, "", "google"));
								break;

							case "facebook":
								userName = finalResult.getString("userName");
								mAppState.setUserCredentials(new Login(
										userName, "", "facebook"));
								break;

							default:
								mAppState.setUserCredentials(new Login(
										userName, pwd, "pratilipi"));
								break;

							}
							if (loginHelper != null) {
								loginHelper.responseSuccess();
								return true;
							}
						}
					}
				} catch (Exception e) {
					loginHelper.responseFailure("Exception " + e);
				}
			} else if (loginHelper != null) {
				loginHelper
						.responseFailure("Please check your internet connection.");
			}
		}
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		return null;
	}

	@Override
	public Boolean setPutStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		return null;
	}

}
