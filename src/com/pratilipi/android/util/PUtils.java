package com.pratilipi.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class PUtils {

	private static int mVersionCode = -1;
	private static String mVersionName = "1.1";

	public static void setAppVersions(Context context) {

		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			mVersionCode = packageInfo.versionCode;
			mVersionName = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package code: " + e);
		}
	}

	public static String getAppVersion() {
		return mVersionName;
	}

	public static int getAppVersionCode() {
		return mVersionCode;
	}

	public static HttpURLConnection getConnection(URL url) {

		try {
			if (url.toString().contains("https://*")) {
				return (HttpsURLConnection) url.openConnection();
			} else {
				return (HttpURLConnection) url.openConnection();
			}
		} catch (IOException e) {
			LoggerUtils.logWarn("DriverApp", Log.getStackTraceString(e));
		}

		return null;
	}

	public static boolean haveNetworkConnection(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

			if (activeNetwork != null
					&& activeNetwork.isConnectedOrConnecting()) {
				return true;
			}

			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo) {
				if (ni.getType() == ConnectivityManager.TYPE_WIFI
						&& ni.isConnected()) {
					return true;
				}
				if (ni.getType() == ConnectivityManager.TYPE_MOBILE
						&& ni.isConnected()) {
					return true;
				}
			}
		} catch (Exception e) {
			LoggerUtils.logWarn("DriverApp", Log.getStackTraceString(e));
		}
		return false;
	}

	public static void showKeypad(Context context, View v, boolean show) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (show) {
			imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
		} else {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	public static void showForceKeypad(Context context, View v, boolean show) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (show) {
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_IMPLICIT_ONLY);
		} else {
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		}
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static int convertDpToPixel(float dp, Context context) {
		return convertDpToPixel(dp, context.getResources());
	}

	public static int convertDpToPixel(float dp, Resources resources) {
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return (int) (dp * (metrics.densityDpi / 160f));
	}

	public static SpannableStringBuilder getRupeeEncodedString(String text) {
		Typeface font = FontManager.getInstance().get("rupee");
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
		if (text.contains("`")) {
			int index = text.indexOf("`");
			int subStringIndex = 0;
			do {
				stringBuilder.setSpan(new CustomTypefaceSpan("", font), index,
						index + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				String subString = text.substring(index + 1);
				subStringIndex = subString.indexOf("`");
				index += subStringIndex + 1;
			} while (subStringIndex > 0); // text.substring(count).indexOf("`")
		}
		return stringBuilder;
	}

	public static SpannableStringBuilder getRupeeEncodedString(String prefix,
			String suffix) {
		Typeface font = FontManager.getInstance().get("rupee");
		int index = prefix.length();
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
				prefix + "`" + suffix);
		stringBuilder.setSpan(new CustomTypefaceSpan("", font), index,
				index + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		return stringBuilder;
	}

	public static void copyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024 * 64;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception e) {
			LoggerUtils.logWarn("FreeCharge", Log.getStackTraceString(e));
		}
	}

	public static void setLocale(Context context, String selectedLanguage) {
		Locale locale = new Locale(selectedLanguage);
		Resources res = context.getResources();
		DisplayMetrics display = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		res.updateConfiguration(conf, display);
	}

	public static void sendBroadcast(Context context, String action) {
		sendBroadcast(context, action, null);
	}

	public static void sendBroadcast(Context context, String action,
			Bundle extras) {
		try {
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(action);
			if (extras != null)
				broadcastIntent.putExtras(extras);

			// Use LocalBroadcastManager instead of context broadcast
			LocalBroadcastManager lbm = LocalBroadcastManager
					.getInstance(context);
			lbm.sendBroadcast(broadcastIntent);
		} catch (Exception e) {
			LoggerUtils.logWarn("Error sending broadcast",
					action + "\n" + e.getMessage());
		}
	}

	public static void unregisterReceiver(Context context,
			BroadcastReceiver receiver) {
		if (receiver != null) {
			LocalBroadcastManager lbm = LocalBroadcastManager
					.getInstance(context);
			lbm.unregisterReceiver(receiver);
		}
	}
}
