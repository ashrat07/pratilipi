package com.pratilipi.android.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.util.LoggerUtils;

public abstract class BaseFragment extends Fragment implements
		HttpResponseListener {

	SplashActivity mParentActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof SplashActivity) {
			mParentActivity = (SplashActivity) activity;
		}
	}

	/**
	 * Set result from a HTTP POST Call
	 * 
	 * @param finalResult
	 *            - JSON Result returned by server
	 * @param postUrl
	 *            - URL on which HTTP Post was created
	 * @return true if result was consumed
	 */
	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {

		if (finalResult == null && mParentActivity != null) {
			LoggerUtils.logWarn("Get Response", "No value returned");
			mParentActivity.showError(mParentActivity.getResources().getString(
					R.string.error_system_issue));
			return true;
		}
		return false;
	}

	/**
	 * Set result from a HTTP GET Call
	 * 
	 * @param finalResult
	 *            - JSON Result returned by server
	 * @param getUrl
	 *            - URL on which HTTP GET was created
	 * @return true if result was consumed
	 */
	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {

		if (finalResult == null && mParentActivity != null) {
			LoggerUtils.logWarn("Get Response", "No value returned");
			mParentActivity.showError(mParentActivity.getResources().getString(
					R.string.error_system_issue));
			return true;
		}
		return false;
	}

	/**
	 * Set result from a HTTP PUT Call
	 * 
	 * @param finalResult
	 *            - JSON Result returned by server
	 * @param getUrl
	 *            - URL on which HTTP GET was created
	 * @return true if result was consumed
	 */
	@Override
	public Boolean setPutStatus(JSONObject finalResult, String getUrl,
			int responseCode) {

		if (finalResult == null && mParentActivity != null) {
			LoggerUtils.logWarn("Get Response", "No value returned");
			mParentActivity.showError(mParentActivity.getResources().getString(
					R.string.error_system_issue));
			return true;
		}
		return false;
	}

	public abstract String getCustomTag();

	/**
	 * introducing a functionality similar to the Activity.onBackPressed()
	 * callbacks
	 */
	public void onBackPressed() {
	}

	public void refreshView() {
	}

	public void onContinueClick() {
	}

	public void toggleFragmentFooter(Boolean b) {
	}

	public void toggleSpinner(Boolean b) {
	}

	/**
	 * Try doing auto login using app state Returns true if successful
	 * */
	public boolean tryAutoLogin() {
		return false;
	}

}
