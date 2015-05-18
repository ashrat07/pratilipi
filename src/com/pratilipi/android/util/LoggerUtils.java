package com.pratilipi.android.util;

import android.text.TextUtils;
import android.util.Log;

public class LoggerUtils {

	// verbose logs
	public static void logVerbose(String key, String value) {
		if (PConstants.IS_DISPLAY_LOGS && !TextUtils.isEmpty(key)
				&& !TextUtils.isEmpty(value)) {
			Log.v(key, value);
		}
	}

	// debug logs
	public static void logDebug(String key, String value) {
		if (PConstants.IS_DISPLAY_LOGS && !TextUtils.isEmpty(key)
				&& !TextUtils.isEmpty(value)) {
			Log.d(key, value);
		}
	}

	// info logs
	public static void logInfo(String key, String value) {
		if (PConstants.IS_DISPLAY_LOGS && !TextUtils.isEmpty(key)
				&& !TextUtils.isEmpty(value)) {
			Log.i(key, value);
		}
	}

	// warn logs
	public static void logWarn(String key, String value) {
		if (PConstants.IS_DISPLAY_LOGS && !TextUtils.isEmpty(key)
				&& !TextUtils.isEmpty(value)) {
			Log.w(key, value);
		}
	}

	// wtf logs
	public static void logWtf(String key, String value) {
		if (PConstants.IS_DISPLAY_LOGS && !TextUtils.isEmpty(key)
				&& !TextUtils.isEmpty(value)) {
			Log.wtf(key, value);
		}
	}
}
