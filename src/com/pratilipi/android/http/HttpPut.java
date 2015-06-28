package com.pratilipi.android.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pratilipi.android.util.LoggerUtils;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PThreadPool;
import com.pratilipi.android.util.PUtils;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class HttpPut extends
		AsyncTask<HashMap<String, String>, Void, JSONObject> {

	private HttpResponseListener mHttpListner;
	private String mCalledRef = "";
	int mResponseCode = -1;

	public HttpPut(HttpResponseListener httpListner, String calledRef) {
		this.mHttpListner = httpListner;
		this.mCalledRef = calledRef;
	}

	@SafeVarargs
	@Override
	protected final JSONObject doInBackground(HashMap<String, String>... params) {

		JSONObject finalResult = null;
		URL url = null;
		android.os.Process
				.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);
		try {
			HashMap<String, String> requestMap = params[0];
			String requestURL = requestMap.get(PConstants.URL);

			StringBuilder query;
			if (requestURL.contains("?")) {
				query = new StringBuilder("&");
			} else {
				query = new StringBuilder("?");
			}
			for (Map.Entry<String, String> entry : requestMap.entrySet()) {
				if (!PConstants.URL.equals(entry.getKey())) {
					query.append(entry.getKey() + "=" + entry.getValue() + "&");
				}
			}

			url = new URL(requestURL + query);

			HttpURLConnection conn = PUtils.getConnection(url);

			// Connection and socket timeouts
			conn.setConnectTimeout(PConstants.CONNECTION_TIMEOUT_MILLISECONDS);
			conn.setReadTimeout(PConstants.SOCKET_TIMEOUT_MILLISECONDS);

			conn.setRequestMethod("PUT");

			if (params.length > 1) {
				HashMap<String, String> headerMap = params[1];
				if (headerMap != null) {
					for (Map.Entry<String, String> entry : headerMap.entrySet()) {
						conn.addRequestProperty(entry.getKey(),
								entry.getValue());
					}
				}
			}

			LoggerUtils.logWarn("HTTP PUT Request", url.toString());
			conn.connect();
			try {
				mResponseCode = conn.getResponseCode();
				LoggerUtils.logWarn("HTTP PUT Response Code:", ""
						+ mResponseCode);
			} catch (SSLPeerUnverifiedException e) {
				LoggerUtils.logWarn("Exception", Log.getStackTraceString(e));
				return null;
			} catch (IOException e) {
				LoggerUtils.logWarn("Exception", Log.getStackTraceString(e));
				LoggerUtils.logWarn("HTTP PUT Response Code(2):", ""
						+ mResponseCode);
				return null;
			}

			if (conn.getInputStream() != null) {
				InputStream stream = null;
				String encoding = conn.getContentEncoding();
				if ("gzip".equals(encoding)) {
					stream = new GZIPInputStream(conn.getInputStream());
				} else {
					stream = conn.getInputStream();
				}

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream, "UTF-8"));

				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				try {
					finalResult = new JSONObject(builder.toString());
				} catch (JSONException js) {
					finalResult = new JSONObject();
					finalResult.put("array", new JSONArray(builder.toString()));
					LoggerUtils.logWarn("HTTP PUT JsonException",
							Log.getStackTraceString(js));
				}

				if (finalResult != null) {
					LoggerUtils.logWarn("HTTP PUT Response",
							finalResult.toString());
				}

			} else {
				LoggerUtils.logWarn("HTTP PUT Response", "Nothing");
			}

			conn.disconnect();
		} catch (Exception e) {
			int len = e.getStackTrace().length;
			StackTraceElement[] throwStackTrace = new StackTraceElement[len + 1];
			System.arraycopy(e.getStackTrace(), 0, throwStackTrace, 0, len);
			throwStackTrace[len] = new StackTraceElement("HttpGet",
					url.getPath(), url.getPath(), 137);
			e.setStackTrace(throwStackTrace);

			// Crashlytics.logException(e);
			LoggerUtils.logWarn("Exception", Log.getStackTraceString(e));

			// Reset the result in case of exception
			finalResult = null;
		}
		return finalResult;
	}

	@Override
	protected final void onPostExecute(JSONObject finalResult) {
		super.onPostExecute(finalResult);
		if (mHttpListner != null) {
			mHttpListner.setPutStatus(finalResult, mCalledRef, mResponseCode);
		}
	}

	@SafeVarargs
	public final void run(HashMap<String, String>... params) {
		if (Build.VERSION.SDK_INT >= 11) {
			executeOnExecutor(PThreadPool.get().getExecutor(), params);
		} else {
			execute(params);
		}
	}

}
