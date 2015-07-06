package com.pratilipi.android.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ProfileAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.model.Login;
import com.pratilipi.android.model.UserProfile;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class ProfileFragment extends BaseFragment implements
		IHttpResponseHelper {

	public static final String TAG_NAME = "Profile";
	private AppState mAppState;
	private static String userName = "";
	private static String pwd = "";
	private UserProfile userProfileObject;
	private Button loginBtn;
	private TextView userNameView;
	private TextView memberSinceView;
	private TextView userShelfCountView;
	private ImageView userImageView;
	private Integer[] profileItemsList = new Integer[] {
			R.string.reset_content_language, R.string.reset_menu_language,
			R.string.about };

	private View mRootView;
	private ListView mListView;

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
		if (mAppState.getAccessToken().isEmpty()) {
			Login loginObj = mAppState.getUserCredentials();
			if (loginObj != null && loginObj.loginName != null
					&& !loginObj.loginName.isEmpty()
					&& loginObj.loginPassword != null
					&& !loginObj.loginPassword.isEmpty()) {
				userName = loginObj.loginName;
				pwd = loginObj.loginPassword;
				makeRequest();
			}
		} else {
			makeUserProfileRequest();
		}

		String imgURI = "http://lorempixel.com/200/200/people/"; // Default URL
		userNameView = (TextView) mRootView.findViewById(R.id.user_name);
		memberSinceView = (TextView) mRootView.findViewById(R.id.member_since);
		userShelfCountView = (TextView) mRootView
				.findViewById(R.id.user_shelf_count);
		userImageView = (ImageView) mRootView.findViewById(R.id.profile_img);
		mListView = (ListView) mRootView.findViewById(R.id.profile_list_view);

		loginBtn = (Button) mRootView.findViewById(R.id.progile_login_btn);
		loginBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mParentActivity.showNextView(new LoginFragment());

			}
		});

		if (userProfileObject != null) {
			if (userProfileObject.firstName != null)
				userNameView.setText(userProfileObject.firstName);
			if (userProfileObject.memberDOJ != null)
				memberSinceView.setText(userProfileObject.memberDOJ.toString());

			// Check not required for below as it will take default values
			imgURI = userProfileObject.userImgUrl;
			userShelfCountView.setText(userProfileObject.shelfBookCount);
			mParentActivity.mImageLoader.displayImage(imgURI, userImageView);

			// setting visibility
			userNameView.setVisibility(View.VISIBLE);
			memberSinceView.setVisibility(View.VISIBLE);
			userShelfCountView.setVisibility(View.VISIBLE);
			userImageView.setVisibility(View.VISIBLE);
			loginBtn.setVisibility(View.GONE);
		} else
			loginBtn.setVisibility(View.VISIBLE);

		ProfileAdapter adapter = new ProfileAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, profileItemsList);
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
			HttpGet userProfileGet = new HttpGet(this,
					PConstants.USERPROFILE_URL);
			HashMap<String, String> requestParameters = new HashMap<>();
			requestParameters.put(PConstants.URL, PConstants.USERPROFILE_URL);
			requestParameters.put("accessToken", mAppState.getAccessToken());
			mParentActivity.showProgressBar();
			userProfileGet.run(requestParameters);
		} else {
			mParentActivity.showNextView(new LoginFragment());
		}
	}

	@Override
	public void responseSuccess() {
		loginBtn.setVisibility(View.GONE);
		makeUserProfileRequest();
	}

	@Override
	public void responseFailure(String failureMessage) {
		// TODO Auto-generated method stub
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, failureMessage, 5).show();
	}

	@Override
	public void makeRequest() {
		mParentActivity.showProgressBar();
		mParentActivity.mLoginManager.loginRequest(userName, pwd, this);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		mParentActivity.hideProgressBar();
		if (getUrl.equals(PConstants.USERPROFILE_URL)) {
			try {
				if (finalResult != null) {
					if (finalResult.has("userData")
							&& finalResult.getString("userData") != null) {
						JSONObject userDataJSON = finalResult
								.getJSONObject("userData");
						if (userDataJSON != null) {
							if (userDataJSON.has("id")
									&& userDataJSON.getString("id") != null) {
								userProfileObject = new UserProfile();
								userProfileObject.id = userDataJSON
										.getString("id");
								if (userDataJSON.has("id")
										&& userDataJSON.getString("id") != null)
									userProfileObject.id = userDataJSON
											.getString("id");
								if (userDataJSON.has("hasPassword")
										&& userDataJSON
												.getString("hasPassword") != null)
									userProfileObject.hasPassword = userDataJSON
											.getBoolean("hasPassword");
								if (userDataJSON.has("firstName")
										&& userDataJSON.getString("firstName") != null)
									userProfileObject.firstName = userDataJSON
											.getString("firstName");
								if (userDataJSON.has("hasFirstName")
										&& userDataJSON
												.getString("hasFirstName") != null)
									userProfileObject.hasFirstName = userDataJSON
											.getBoolean("hasFirstName");
								if (userDataJSON.has("hasLastName")
										&& userDataJSON
												.getString("hasLastName") != null)
									userProfileObject.hasLastName = userDataJSON
											.getBoolean("hasLastName");
								if (userDataJSON.has("name")
										&& userDataJSON.getString("name") != null)
									userProfileObject.name = userDataJSON
											.getString("name");
								if (userDataJSON.has("hasName")
										&& userDataJSON.getString("hasName") != null)
									userProfileObject.hasName = userDataJSON
											.getBoolean("hasName");
								if (userDataJSON.has("email")
										&& userDataJSON.getString("email") != null)
									userProfileObject.email = userDataJSON
											.getString("email");
								if (userDataJSON.has("hasEmail")
										&& userDataJSON.getString("hasEmail") != null)
									userProfileObject.hasEmail = userDataJSON
											.getBoolean("hasEmail");
								if (userDataJSON.has("hasDateOfBirth")
										&& userDataJSON
												.getString("hasDateOfBirth") != null)
									userProfileObject.hasDOB = userDataJSON
											.getBoolean("hasDateOfBirth");
								if (userDataJSON.has("hasGender")
										&& userDataJSON.getString("hasGender") != null)
									userProfileObject.hasGender = userDataJSON
											.getBoolean("hasGender");
								if (userDataJSON.has("hasFacebookRefreshToken")
										&& userDataJSON
												.getString("hasFacebookRefreshToken") != null)
									userProfileObject.hasFBRefreshToken = userDataJSON
											.getBoolean("hasFacebookRefreshToken");
								if (userDataJSON.has("hasGoogleRefreshToken")
										&& userDataJSON
												.getString("hasGoogleRefreshToken") != null)
									userProfileObject.hasGoogleRefreshToken = userDataJSON
											.getBoolean("hasGoogleRefreshToken");
								if (userDataJSON.has("hasProfilePicUrl")
										&& userDataJSON
												.getString("hasProfilePicUrl") != null)
									userProfileObject.hasProfilePicUrl = userDataJSON
											.getBoolean("hasProfilePicUrl");
								if (userDataJSON.has("status")
										&& userDataJSON.getString("status") != null)
									userProfileObject.status = userDataJSON
											.getString("status");

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

		return null;
	}

}
