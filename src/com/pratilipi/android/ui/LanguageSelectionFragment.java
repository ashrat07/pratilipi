package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pratilipi.android.R;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.PUtils;

public class LanguageSelectionFragment extends BaseFragment {

	public static final String TAG_NAME = "Language Selection";

	private View mRootView;
	private Button mHindiButton;
	private Button mTamilButton;
	private Button mGujaratiButton;
	private View mGoButton;

	private PConstants.LANGUAGE mLanguageSelected;

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

		mHindiButton.setTag(PConstants.LANGUAGE.HINDI);
		mHindiButton.setOnClickListener(mLanguageSelectionListener);

		mTamilButton.setTag(PConstants.LANGUAGE.TAMIL);
		mTamilButton.setOnClickListener(mLanguageSelectionListener);

		mGujaratiButton.setTag(PConstants.LANGUAGE.GUJARATI);
		mGujaratiButton.setOnClickListener(mLanguageSelectionListener);

		mGoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mLanguageSelected != null) {
					mParentActivity.mApp.setLanguage(mLanguageSelected
							.toString());
					mParentActivity.mApp.setLanguageId(mLanguageSelected
							.getId());

					if (mLanguageSelected.equals(PConstants.LANGUAGE.GUJARATI)) {
						PUtils.setLocale(mParentActivity, "gu");
					} else if (mLanguageSelected
							.equals(PConstants.LANGUAGE.TAMIL)) {
						PUtils.setLocale(mParentActivity, "ta");
					} else if (mLanguageSelected
							.equals(PConstants.LANGUAGE.HINDI)) {
						PUtils.setLocale(mParentActivity, "hi");
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
							.findViewWithTag(mLanguageSelected);
					if (previousSelection != null) {
						previousSelection.setSelected(false);
					}
				}
				mLanguageSelected = (PConstants.LANGUAGE) v.getTag();
				v.setSelected(true);
			}
		}
	};
}
