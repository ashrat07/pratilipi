package com.pratilipi.android.ui;

import java.util.Locale;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pratilipi.android.R;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.PConstants;

public class LanguageSelectionFragment extends BaseFragment {

	public static final String TAG_NAME = "Language Selection";

	private View mRootView;
	private Button mHindiButton;
	private Button mTamilButton;
	private Button mGujaratiButton;
	private View mGoButton;

	private String mLanguageSelected;
	private String mLanguageIdSelected;
	private int mPreviousSelectedId;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_language_selection,
				container, false);

		mHindiButton = (Button) mRootView.findViewById(R.id.hindi_button);
		mTamilButton = (Button) mRootView.findViewById(R.id.tamil_button);
		mGujaratiButton = (Button) mRootView.findViewById(R.id.gujarati_button);
		mGoButton = mRootView.findViewById(R.id.go_button);

		mLanguageSelected = null;
		mLanguageIdSelected = null;
		mParentActivity.mApp.setLanguage(mLanguageSelected);

		mHindiButton.setText(mParentActivity.getResources().getString(
				R.string.hindi, new Locale("hi")));
		mHindiButton.setTypeface(FontManager.getInstance().get("hindi"));
		mHindiButton.setTag(PConstants.LANGUAGE.HINDI);
		mHindiButton.setOnClickListener(mLanguageSelectionListener);

		mTamilButton.setText(mParentActivity.getResources().getString(
				R.string.tamil, new Locale("ta")));
		mTamilButton.setTypeface(FontManager.getInstance().get("tamil"));
		mTamilButton.setTag(PConstants.LANGUAGE.TAMIL);
		mTamilButton.setOnClickListener(mLanguageSelectionListener);

		mGujaratiButton.setText(mParentActivity.getResources().getString(
				R.string.gujarati, new Locale("gu")));
		mGujaratiButton.setTypeface(FontManager.getInstance().get("gujarati"));
		mGujaratiButton.setTag(PConstants.LANGUAGE.GUJARATI);
		mGujaratiButton.setOnClickListener(mLanguageSelectionListener);

		mGoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mLanguageSelected)) {
					mParentActivity.mApp.setLanguage(mLanguageSelected);
					mParentActivity.mApp.setLanguageId(mLanguageIdSelected);

					if (mLanguageIdSelected.equals(PConstants.LANGUAGE.GUJARATI
							.getId())) {
						setLocale("gu");
					} else if (mLanguageIdSelected
							.equals(PConstants.LANGUAGE.TAMIL.getId())) {
						setLocale("ta");
					} else if (mLanguageIdSelected
							.equals(PConstants.LANGUAGE.HINDI.getId())) {
						setLocale("hi");
					}

					mParentActivity.mStack.popAll();
					mParentActivity.showNextView(new StoreFragment());

				} else {
					mParentActivity.showError("Please select some language.");
				}
			}
		});

		return mRootView;
	}

	View.OnClickListener mLanguageSelectionListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getTag() != null && !v.getTag().equals(mLanguageSelected)) {

				if (mLanguageSelected != null) {
					Button previousSelection = (Button) mRootView
							.findViewById(mPreviousSelectedId);
					if (previousSelection != null) {
						previousSelection.setSelected(false);
						mPreviousSelectedId = v.getId();
					}
				} else {
					mPreviousSelectedId = v.getId();
					PConstants.LANGUAGE language = (PConstants.LANGUAGE) v
							.getTag();
					mLanguageSelected = language.toString();
					mLanguageIdSelected = language.getId();
				}
				v.setSelected(true);
			}
		}
	};

	private void setLocale(String selectedLanguage) {
		Locale locale = new Locale(selectedLanguage);
		Resources res = getResources();
		DisplayMetrics display = res.getDisplayMetrics();
		Configuration conf = res.getConfiguration();
		conf.locale = locale;
		res.updateConfiguration(conf, display);
	}
}
