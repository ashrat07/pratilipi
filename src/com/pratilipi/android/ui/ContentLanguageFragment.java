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

public class ContentLanguageFragment extends BaseFragment {
	public static final String TAG_NAME = "ContentLanguage";
	private Integer[] _languageList = new Integer[] { R.string.hindi_en, R.string.tamil_en, R.string.gujarati_en };

	private View mRootView;
	private ListView listView;
	ContentMenuLanguageAdapter mAdapter;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_content_language,
				container, false);
		listView = (ListView) mRootView
				.findViewById(R.id.content_language_list_view);
		mAdapter = new ContentMenuLanguageAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, _languageList);
		listView.setAdapter(mAdapter);
		mAdapter.setSelectedItem(AppState.getInstance().getLanguageId());

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				mAdapter.setSelectedItem(position);
				mAdapter.notifyDataSetChanged();
			}
		});
		return mRootView;

	}

}
