package com.pratilipi.android.util;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.ui.SplashActivity;

import java.io.IOException;

/**
 * Created by ashish on 7/26/15.
 */
public class GoogleManager extends AsyncTask<Void, Void, String> {

	public static final int REQUEST_CODE_RESOLVE_ERR = 9000;
	public static final String USER_RECOVERABLE_AUTH_EXCEPTION = "UserRecoverableAuthException";

	private SplashActivity mActivity;
	private String mEmail;
	private IHttpResponseHelper loginHelper;

	public GoogleManager(SplashActivity activity, String email,
			IHttpResponseHelper loginHelper) {
		this.mActivity = activity;
		this.mEmail = email;
		this.loginHelper = loginHelper;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			String token = GoogleAuthUtil
					.getToken(
							mActivity.getApplicationContext(),
							mEmail,
							"oauth2:https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile");
			return token;
		} catch (UserRecoverableAuthException userRecoverableException) {
			mActivity.startActivityForResult(
					userRecoverableException.getIntent(),
					REQUEST_CODE_RESOLVE_ERR);
			return USER_RECOVERABLE_AUTH_EXCEPTION;
		} catch (IOException e) {
			LoggerUtils.logWarn("error", Log.getStackTraceString(e));
		} catch (GoogleAuthException e) {
			LoggerUtils.logWarn("error", Log.getStackTraceString(e));
		} catch (Exception e) {
			LoggerUtils.logWarn("error", Log.getStackTraceString(e));
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if (USER_RECOVERABLE_AUTH_EXCEPTION.equals(result)) {
			mActivity.hideProgressBar();
		} else if (!TextUtils.isEmpty(result)) {

			// Show this only in case of direct login and not autologin
			if (!mActivity.isBackgroundLogin) {
				mActivity.showProgressBar();
			}

			mActivity.mLoginManager.googleLoginRequest(result, mEmail,
					loginHelper);
		} else {
			mActivity.hideProgressBar();
			mActivity.showError("error_google_login_issue");

			// Same as login failed
			mActivity.mApp.setLoginStatus(AppState.LoginStatus.LOGIN_FAILED);
			PUtils.sendBroadcast(mActivity, PConstants.LOGIN_FAILED_BROADCAST);
		}
	}
}
