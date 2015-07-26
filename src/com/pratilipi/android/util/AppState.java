package com.pratilipi.android.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.pratilipi.android.model.Login;

public class AppState {

	SharedPreferences mConf;

	private static final String LANGUAGE_CONTENT = "LANGUAGE_CONTENT";
	private static final String LANGUAGE_CONTENT_ID = "LANGUAGE_CONTENT_ID";
	private static final String LANGUAGE_CONTENT_HASH_CODE = "LANGUAGE_CONTENT_HASH_CODE";

	private static final String LANGUAGE_MENU_ID = "LANGUAGE_MENU_ID";
	private static final String LANGUAGE_MENU_TYPEFACE = "LANGUAGE_MENU_TYPEFACE";
	private static final String LANGUAGE_MENU_LOCALE = "LANGUAGE_MENU_LOCALE";
	private static final String USER_NAME = "USER_NAME";
	private static final String PASSWORD = "PASSWORD";
	private static final String LOGIN_TYPE = "LOGIN_TYPE";
	private static final String LOGGED_IN = "LOGGED_IN";
	private static final String GPLUS_LOGIN = "GPLUS_LOGIN";
	private static final String GPLUS_EMAIL = "GPLUS_EMAIL";

	private static String ACCESS_TOKEN = "";

	private static AppState instance;

	public synchronized static void init(Context context) {
		if (instance == null)
			instance = new AppState(context);
	}

	public AppState(Context context) {
		if (this.mConf == null) {
			this.mConf = context.getSharedPreferences(PConstants.APP_CONFIG,
					Context.MODE_PRIVATE);
		}
	}

	public static AppState getInstance() {
		if (instance == null)
			throw new RuntimeException("AppState has not been initialized");
		return instance;
	}

	public static AppState getInstance(Context context) {
		if (instance == null)
			instance = new AppState(context);
		return instance;
	}

	public Boolean setContentLanguage(String language) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putString(LANGUAGE_CONTENT, language);
		return ed.commit();
	}

	public String getContentLanguage() {
		return mConf.getString(LANGUAGE_CONTENT, "regular");
	}

	public Boolean setContentLanguageId(int languageId) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putInt(LANGUAGE_CONTENT_ID, languageId);
		return ed.commit();
	}

	public int getContentLanguageId() {
		return mConf.getInt(LANGUAGE_CONTENT_ID, 0);
	}

	public Boolean setContentLanguageHashCode(String languageHashCode) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putString(LANGUAGE_CONTENT_HASH_CODE, languageHashCode);
		return ed.commit();
	}

	public String getContentLanguageHashCode() {
		return mConf.getString(LANGUAGE_CONTENT_HASH_CODE, "");
	}

	public Boolean setMenuLanguageId(int languageId) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putInt(LANGUAGE_MENU_ID, languageId);
		return ed.commit();
	}

	public int getMenuLanguageId() {
		return mConf.getInt(LANGUAGE_MENU_ID, 0);
	}

	public Boolean setMenuLanguageTypeface(String languageTypeface) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putString(LANGUAGE_MENU_TYPEFACE, languageTypeface);
		return ed.commit();
	}

	public String getMenuLanguageTypeface() {
		return mConf.getString(LANGUAGE_MENU_TYPEFACE, "regular");
	}

	public Boolean setMenuLanguageLocale(String languageLocale) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putString(LANGUAGE_MENU_LOCALE, languageLocale);
		return ed.commit();
	}

	public String getMenuLanguageLocale() {
		return mConf.getString(LANGUAGE_MENU_LOCALE, "en");
	}

	public String getAccessToken() {
		return ACCESS_TOKEN;
	}

	public void setAccessToken(String accessToken) {
		ACCESS_TOKEN = accessToken;
	}

	public void setUserCredentials(Login userCredentials) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putString(USER_NAME, userCredentials.loginName);
		ed.putString(PASSWORD, userCredentials.loginPassword);
		ed.putString(LOGIN_TYPE, userCredentials.loginType);
		ed.commit();
	}

	public Login getUserCredentials() {
		String userName = mConf.getString(USER_NAME, null);
		String password = mConf.getString(PASSWORD, null);
		String loginType = mConf.getString(LOGIN_TYPE, null);
		Login login = new Login(userName, password, loginType);
		return login;
	}

	public enum LoginStatus {
		PENDING, SESSION_SUCCESS, SESSION_FAILED, LOGIN_SUCCESS, LOGIN_FAILED
	}

	private LoginStatus loginStatus = LoginStatus.PENDING;

	public LoginStatus getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Boolean isGPlusLogin() {
		return mConf.contains(GPLUS_LOGIN);
	}

	public Boolean setGPlusLogin(Boolean login) {
		if (setLoggedIn(login)) {
			if (login) {
				SharedPreferences.Editor ed = mConf.edit();
				ed.putBoolean(GPLUS_LOGIN, login);
				return ed.commit();
			} else {
				SharedPreferences.Editor ed = mConf.edit();
				ed.remove(GPLUS_LOGIN);
				return ed.commit();
			}
		} else {
			return false;
		}
	}

	public Boolean setLoggedIn(Boolean login) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putBoolean(LOGGED_IN, login);
		return ed.commit();
	}

	public String getGPlusEmail() {
		return mConf.getString(GPLUS_EMAIL, "");
	}

	public Boolean setGPlusEmail(String email) {
		SharedPreferences.Editor ed = mConf.edit();
		ed.putString(GPLUS_EMAIL, email);
		return ed.commit();
	}
}
