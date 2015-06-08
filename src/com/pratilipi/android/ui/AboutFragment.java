package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.pratilipi.android.R;

public class AboutFragment extends BaseFragment{

	public static final String TAG_NAME= "about";
	
	
	
	@Override
	public String getCustomTag() {
		
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View mRootView= inflater.inflate(R.layout.fragment_about,container, false);
		WebView webView = (WebView) mRootView.findViewById(R.id.web_view);
		webView.loadUrl("http://www.pratilipi.com/about/pratilipi");
		
		return mRootView;
	}
	
}
