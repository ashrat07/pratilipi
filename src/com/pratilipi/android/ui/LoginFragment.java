package com.pratilipi.android.ui;

import java.util.Arrays;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.pratilipi.android.R;
import com.pratilipi.android.iHelper.IHttpResponseHelper;
import com.pratilipi.android.util.GoogleManager;

public class LoginFragment extends BaseFragment implements IHttpResponseHelper,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener {

	public static final String TAG_NAME = "Login";

	private View mRootView;
	private SignInButton googleSignInButton;
	private LoginButton facebookLoginButton;
	private EditText mUserNameEditText;
	private EditText mPwdEditText;
	private Button pratilipiLoginButton;
	private String userName = "";
	private String pwd = "";

	public static final int RC_SIGN_IN = 2930;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIsResolving = false;
	private boolean mShouldResolve = false;

	private CallbackManager facebookCallbackManager;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_login, container, false);

		googleSignInButton = (SignInButton) mRootView
				.findViewById(R.id.google_sign_in_button);
		facebookLoginButton = (LoginButton) mRootView
				.findViewById(R.id.facebook_login_button);
		pratilipiLoginButton = (Button) mRootView.findViewById(R.id.login_btn);
		mUserNameEditText = (EditText) mRootView.findViewById(R.id.login_name);
		mPwdEditText = (EditText) mRootView.findViewById(R.id.login_pwd);

		mGoogleApiClient = new GoogleApiClient.Builder(mParentActivity)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_PROFILE).build();
		googleSignInButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// User clicked the sign-in button, so begin the sign-in process
				// and automatically
				// attempt to resolve any errors that occur.
				mShouldResolve = true;
				mGoogleApiClient.connect();

				// TODO: Show a message to the user that we are signing in.
			}
		});

		facebookLoginButton.setReadPermissions(Arrays.asList("public_profile",
				"email"));
		facebookLoginButton.setFragment(this);
		facebookCallbackManager = CallbackManager.Factory.create();
		facebookLoginButton.registerCallback(facebookCallbackManager,
				new FacebookCallback<LoginResult>() {

					@Override
					public void onSuccess(LoginResult loginResult) {
						if (loginResult != null) {
							AccessToken accessToken = loginResult
									.getAccessToken();
							if (accessToken != null) {
								String token = accessToken.getToken();
								String socialId = accessToken.getUserId();
								if (token != null && socialId != null) {
									mParentActivity.mLoginManager
											.facebookLoginRequest(token,
													socialId,
													LoginFragment.this);
								}
							}
						}
					}

					@Override
					public void onCancel() {
						Log.e("facebook", "onCancel");
					}

					@Override
					public void onError(FacebookException e) {
						Log.e("facebook", "onError");
					}
				});

		pratilipiLoginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				userName = mUserNameEditText.getText().toString();
				pwd = mPwdEditText.getText().toString();
				if (userName.isEmpty() || pwd.isEmpty()) {
					mParentActivity
							.showError("Please enter user name and password!!");
					return;
				}
				makeRequest();
			}
		});

		return mRootView;
	}

	@Override
	public void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// Could not connect to Google Play Services. The user needs to select
		// an account,
		// grant permissions or resolve an error in order to sign in. Refer to
		// the javadoc for
		// ConnectionResult to see possible error codes.
		Log.d("google", "onConnectionFailed:" + connectionResult);

		if (!mIsResolving && mShouldResolve) {
			if (connectionResult.hasResolution()) {
				try {
					connectionResult.startResolutionForResult(mParentActivity,
							RC_SIGN_IN);
					mIsResolving = true;
				} catch (IntentSender.SendIntentException e) {
					Log.e("google", "Could not resolve ConnectionResult.", e);
					mIsResolving = false;
					mGoogleApiClient.connect();
				}
			} else {
				// Could not resolve the connection result, show the user an
				// error dialog.
				if (connectionResult != null) {
					mParentActivity.showError(connectionResult.toString());
				}
			}
		} else {
			// Show the signed-out UI
			// TODO: showSignedOutUI();
		}
	}

	@Override
	public void onConnected(Bundle bundle) {
		// onConnected indicates that an account was selected on the device,
		// that the selected
		// account has granted any requested permissions to our app and that we
		// were able to
		// establish a service connection to Google Play services.
		Log.e("google", "onConnected:" + bundle);
		mShouldResolve = false;

		String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
		new GoogleManager(mParentActivity, accountName, this).execute();
		Plus.API.getName();
		// PeopleApi.getCurrentPerson(mGoogleApiClient);
		// Show the signed-in UI
		// TODO: showSignedInUI();
	}

	@Override
	public void onConnectionSuspended(int cause) {
		Log.e("google", "onConnectionSuspended");
		mGoogleApiClient.connect();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			// If the error resolution was not successful we should not resolve
			// further.
			if (resultCode != -1) {
				mShouldResolve = false;
			}

			mIsResolving = false;
			mGoogleApiClient.connect();
		} else {
			facebookCallbackManager.onActivityResult(requestCode, resultCode,
					data);
		}
	}

	@Override
	public void makeRequest() {
		mParentActivity.showProgressBar();
		mParentActivity.mLoginManager
				.pratilipiLoginRequest(userName, pwd, this);
	}

	@Override
	public void responseSuccess() {
		mParentActivity.hideProgressBar();
		Toast.makeText(mParentActivity, "Login Success", Toast.LENGTH_SHORT)
				.show();
		mParentActivity.mStack.pop();
	}

	@Override
	public void responseFailure(String failureMessage) {
		mParentActivity.hideProgressBar();
		mParentActivity.showError(failureMessage);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

}
