package com.pratilipi.android.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppState {

	SharedPreferences mConf;

	private static final String LANGUAGE_CONTENT = "LANGUAGE_CONTENT";
	private static final String LANGUAGE_CONTENT_ID = "LANGUAGE_CONTENT_ID";
	private static final String LANGUAGE_CONTENT_HASH_CODE = "LANGUAGE_CONTENT_HASH_CODE";

	private static final String LANGUAGE_MENU_ID = "LANGUAGE_MENU_ID";
	private static final String LANGUAGE_MENU_TYPEFACE = "LANGUAGE_MENU_TYPEFACE";
	private static final String LANGUAGE_MENU_LOCALE = "LANGUAGE_MENU_LOCALE";

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

}
