package com.pratilipi.android.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.json.JSONObject;

import com.pratilipi.android.util.LoggerUtils;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PThreadPool;
import com.pratilipi.android.util.PUtils;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

public class HttpPost extends
		AsyncTask<HashMap<String, String>, Void, JSONObject> {

	private HttpResponseListener mListener;
	String mRef = "";
	int mResponseCode = -1;

	public HttpPost(HttpResponseListener listener, String callRef) {
		this.mListener = listener;
		this.mRef = callRef;
	}

	public HttpPost(String callRef) {
		this.mRef = callRef;
	}

	@SafeVarargs
	@Override
	protected final JSONObject doInBackground(HashMap<String, String>... params) {

		JSONObject finalResult;
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

			conn.setRequestMethod("POST");

			if (params.length > 1) {
				HashMap<String, String> headerMap = params[1];
				if (headerMap != null) {
					for (Map.Entry<String, String> entry : headerMap.entrySet()) {
						conn.addRequestProperty(entry.getKey(),
								entry.getValue());
					}
				}
			}
			conn.setDoInput(true);
			conn.setDoOutput(true);

			if (params.length > 2) {
				HashMap<String, String> payloadMap = params[2];
				if (payloadMap != null) {
					String payload = "";
					for (Map.Entry<String, String> entry : payloadMap
							.entrySet()) {
						payload += entry.getKey() + "=" + entry.getValue()
								+ "&";
					}
					DataOutputStream wr = new DataOutputStream(
							conn.getOutputStream());
					wr.writeBytes(payload);
					wr.flush();
					wr.close();
				}
			}

			if (params.length > 3) {
				HashMap<String, String> jsonPayloadMap = params[3];
				if (jsonPayloadMap != null) {
					String jsonPayload = jsonPayloadMap.get("JSON");
					DataOutputStream wr = new DataOutputStream(
							conn.getOutputStream());
					wr.writeBytes(jsonPayload);
					wr.flush();
					wr.close();
				}
			}

			LoggerUtils.logWarn("HTTP POST Request", url.toString());
			conn.connect();

			try {
				try {
					// Will throw IOException if server responds with 401.
					mResponseCode = conn.getResponseCode();
				} catch (IOException e) {
					// Will return 401, because now connection has the correct
					// internal state.
					mResponseCode = conn.getResponseCode();
				}
				LoggerUtils.logWarn("HTTP POST Response Code:", ""
						+ mResponseCode);
			} catch (SSLPeerUnverifiedException e) {
				// Crashlytics.logException(e);
				LoggerUtils.logWarn("Exception", Log.getStackTraceString(e));
			} catch (IOException e) {
				// Crashlytics.logException(e);
				LoggerUtils.logWarn("Exception", Log.getStackTraceString(e));
				LoggerUtils.logWarn("HTTP POST Response Code(2):", ""
						+ mResponseCode);
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			if (mResponseCode == 302) {
				LoggerUtils.logWarn("HTTP POST Redirect",
						conn.getHeaderField("Location"));
			}

			if (builder.length() > 0) {
				finalResult = new JSONObject(builder.toString());
				LoggerUtils.logWarn("HTTP POST Response",
						finalResult.toString());
			} else {
				finalResult = null;
			}
			conn.disconnect();
		} catch (Exception e) {
			int len = e.getStackTrace().length;
			StackTraceElement[] throwStackTrace = new StackTraceElement[len + 1];
			System.arraycopy(e.getStackTrace(), 0, throwStackTrace, 0, len);
			throwStackTrace[len] = new StackTraceElement("HttpPost",
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
		if (mListener != null) {
			mListener.setPostStatus(finalResult, mRef, mResponseCode);
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
