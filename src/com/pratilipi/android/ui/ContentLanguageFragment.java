package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ContentMenuLanguageAdapter;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class ContentLanguageFragment extends BaseFragment {

	public static final String TAG_NAME = "ContentLanguage";

	private Integer[] _languageList = new Integer[] { R.string.hindi,
			R.string.tamil, R.string.gujarati };

	private View mRootView;
	private ListView mListView;
	private ContentMenuLanguageAdapter mAdapter;
	private PConstants.CONTENT_LANGUAGE mLanguageSelected;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_content_language,
				container, false);

		mListView = (ListView) mRootView
				.findViewById(R.id.content_language_list_view);
		mAdapter = new ContentMenuLanguageAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, _languageList);
		mListView.setAdapter(mAdapter);
		mAdapter.setSelectedItem(AppState.getInstance().getContentLanguageId());

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				mAdapter.setSelectedItem(position);
				mAdapter.notifyDataSetChanged();

				if (position == PConstants.CONTENT_LANGUAGE.HINDI.getId()) {
					mLanguageSelected = PConstants.CONTENT_LANGUAGE.HINDI;
				} else if (position == PConstants.CONTENT_LANGUAGE.TAMIL
						.getId()) {
					mLanguageSelected = PConstants.CONTENT_LANGUAGE.TAMIL;
				} else if (position == PConstants.CONTENT_LANGUAGE.GUJARATI
						.getId()) {
					mLanguageSelected = PConstants.CONTENT_LANGUAGE.GUJARATI;
				} else {
					mLanguageSelected = PConstants.CONTENT_LANGUAGE.HINDI;
				}

				mParentActivity.mApp.setContentLanguage(mLanguageSelected
						.toString());
				mParentActivity.mApp.setContentLanguageId(mLanguageSelected
						.getId());
				mParentActivity.mApp
						.setContentLanguageHashCode(mLanguageSelected
								.getHashCode());
			}
		});
		return mRootView;
	}

}
