package com.pratilipi.android.http;

import org.json.JSONObject;

public interface HttpResponseListener {

	Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode);

	Boolean setGetStatus(JSONObject finalResult, String getUrl, int responseCode);
	
	Boolean setPutStatus(JSONObject finalResult, String putUrl, int responseCode);

}
