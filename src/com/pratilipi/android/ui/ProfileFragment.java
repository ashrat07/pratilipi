package com.pratilipi.android.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ProfileAdapter;
import com.pratilipi.android.http.HttpPost;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.model.Login;
import com.pratilipi.android.model.UserProfile;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class ProfileFragment extends BaseFragment implements
		IHttpResponseHelper {

	public static final String TAG_NAME = "Profile";

	private View mRootView;
	private View mUserProfileLayout;
	private ImageView mUserImageView;
	private TextView mUserNameTextView;
	private TextView mMemberSinceTextView;
	private TextView mUserShelfCountTextView;
	private Button mLoginButton;
	private ListView mListView;
	private Integer[] mProfileItemsList = new Integer[] {
			R.string.reset_content_language, R.string.reset_menu_language,
			R.string.about };

	private AppState mAppState;
	private static String mUserName = "";
	private static String mPwd = "";
	private UserProfile mUserProfile;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_profile, container,
				false);

		mAppState = AppState.getInstance();
		// checks for access token. Does auto login if access token is empty
		// else make user request
		if (TextUtils.isEmpty(mAppState.getAccessToken())) {
			Login loginObj = mAppState.getUserCredentials();
			if (loginObj != null && loginObj.loginName != null
					&& !loginObj.loginName.isEmpty()
					&& loginObj.loginPassword != null
					&& !loginObj.loginPassword.isEmpty()) {
				mUserName = loginObj.loginName;
				mPwd = loginObj.loginPassword;
				makeRequest();
			}
		} else {
			makeUserProfileRequest();
		}

		mUserProfileLayout = mRootView.findViewById(R.id.user_profile_layout);
		mUserImageView = (ImageView) mRootView.findViewById(R.id.profile_img);
		mUserNameTextView = (TextView) mRootView.findViewById(R.id.user_name);
		mMemberSinceTextView = (TextView) mRootView
				.findViewById(R.id.member_since);
		mUserShelfCountTextView = (TextView) mRootView
				.findViewById(R.id.user_shelf_count);
		mListView = (ListView) mRootView.findViewById(R.id.profile_list_view);

		mLoginButton = (Button) mRootView.findViewById(R.id.progile_login_btn);
		mLoginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mParentActivity.showNextView(new LoginFragment());

			}
		});

		if (mUserProfile != null) {
			if (mUserProfile.firstName != null)
				mUserNameTextView.setText(mUserProfile.firstName);
			if (mUserProfile.memberDOJ != null)
				mMemberSinceTextView.setText(mUserProfile.memberDOJ.toString());

			// Check not required for below as it will take default values
			String imgURI = mUserProfile.userImgUrl;
			mUserShelfCountTextView.setText(mUserProfile.shelfBookCount);
			mParentActivity.mImageLoader.displayImage(imgURI, mUserImageView);

			// setting visibility
			mUserProfileLayout.setAlpha(1);
			mLoginButton.setVisibility(View.GONE);
		} else {
			mUserProfileLayout.setAlpha(0.2f);
			mLoginButton.setVisibility(View.VISIBLE);
		}

		ProfileAdapter adapter = new ProfileAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, mProfileItemsList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				switch (position) {
				case 0:
				case 1:
					Bundle bundle = new Bundle();
					bundle.putInt("MENU_TYPE", position);
					mParentActivity.showNextView(new ProfileLanguageFragment(),
							bundle);
					break;

				case 2:
					mParentActivity.showNextView(new AboutFragment());
					break;
				}
			}
		});
		return mRootView;
	}

	private void makeUserProfileRequest() {
		if (!mAppState.getAccessToken().isEmpty()) {
			HttpPost userProfileRequest = new HttpPost(this,
					PConstants.USER_PROFILE_URL);

			HashMap<String, String> requestParameters = new HashMap<>();
			requestParameters.put(PConstants.URL, PConstants.USER_PROFILE_URL);
			requestParameters.put("accessToken", mAppState.getAccessToken());

			userProfileRequest.run(requestParameters);
		} else {
			mParentActivity.showNextView(new LoginFragment());
		}
	}

	@Override
	public void responseSuccess() {
		mLoginButton.setVisibility(View.GONE);
		makeUserProfileRequest();
	}

	@Override
	public void responseFailure(String failureMessage) {
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, failureMessage, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void makeRequest() {
		mParentActivity.showProgressBar();
		mParentActivity.mLoginManager.loginRequest(mUserName, mPwd, this);
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {

		mParentActivity.hideProgressBar();
		if (postUrl.equals(PConstants.USER_PROFILE_URL)) {
			try {
				if (finalResult != null) {
					if (finalResult.has("userData")) {
						JSONObject userDataJSON = finalResult
								.getJSONObject("userData");
						if (userDataJSON != null) {
							if (userDataJSON.has("id")) {
								mUserProfile = new UserProfile();
								mUserProfile.id = userDataJSON.getString("id");
								if (userDataJSON.has("id")) {
									mUserProfile.id = userDataJSON
											.getString("id");
								}
								if (userDataJSON.has("firstName")) {
									mUserProfile.firstName = userDataJSON
											.getString("firstName");
								}
								if (userDataJSON.has("name"))
									mUserProfile.name = userDataJSON
											.getString("name");
								if (userDataJSON.has("email")) {
									mUserProfile.email = userDataJSON
											.getString("email");
								}
								if (userDataJSON.has("status")) {
									mUserProfile.status = userDataJSON
											.getString("status");
								}

								mLoginButton.setVisibility(View.GONE);
								mUserProfileLayout.setAlpha(1);
								return true;
							}// end of userJSON has id check
						}// end of userJSON Object null check
					}// end of has user data check
				}// end of final result check
				responseFailure("Request is successfull but no valid response found");
				return false;
			} catch (Exception e) {
				responseFailure("Request unsuccessfull due to " + e);
			}

		} else {
			responseFailure("Request URL not found");
		}
		return super.setPostStatus(finalResult, postUrl, responseCode);
	}

}
