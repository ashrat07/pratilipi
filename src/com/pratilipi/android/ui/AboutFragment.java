package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pratilipi.android.R;

public class AboutFragment extends BaseFragment {

	public static final String TAG_NAME = "About";

	private View mRootView;
	private View mProgressBarLayout;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_about, container, false);

		WebView webView = (WebView) mRootView.findViewById(R.id.web_view);
		mProgressBarLayout = mRootView.findViewById(R.id.progress_bar_layout);

		webView.loadUrl("http://www.pratilipi.com/about/pratilipi");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				mProgressBarLayout.setVisibility(View.GONE);
			}
		});

		mProgressBarLayout.setVisibility(View.VISIBLE);

		return mRootView;
	}

}
