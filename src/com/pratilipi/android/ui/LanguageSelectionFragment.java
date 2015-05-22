package com.pratilipi.android.ui;

import android.os.Bundle;
import android.text.TextUtils;
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
	private Button mGujratiButton;
	private View mGoButton;

	private String mLanguageSelected;
	private String mLanguageIdSelected;

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
		mGujratiButton = (Button) mRootView.findViewById(R.id.gujrati_button);
		mGoButton = mRootView.findViewById(R.id.go_button);

		mLanguageSelected = null;
		mLanguageIdSelected = null;

		mHindiButton.setTag(PConstants.LANGUAGE.HINDI);
		mHindiButton.setOnClickListener(mLanguageSelectionListener);

		mTamilButton.setTag(PConstants.LANGUAGE.TAMIL);
		mTamilButton.setOnClickListener(mLanguageSelectionListener);

		mGujratiButton.setTag(PConstants.LANGUAGE.GUJARATI);
		mGujratiButton.setOnClickListener(mLanguageSelectionListener);

		mGoButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(mLanguageSelected)) {
					mParentActivity.mApp.setLanguage(mLanguageSelected);
					mParentActivity.mApp.setLanguageId(mLanguageIdSelected);
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
						previousSelection.setTypeface(FontManager.getInstance()
								.get("regular"));
					}
				}
				v.setSelected(true);
				((Button) v).setTypeface(FontManager.getInstance().get("bold"));
				PConstants.LANGUAGE language = (PConstants.LANGUAGE) v.getTag();
				mLanguageSelected = language.toString();
				mLanguageIdSelected = language.getId();
			}
		}
	};

}
