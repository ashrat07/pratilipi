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
import com.pratilipi.android.util.PUtils;

public class MenuLanguageFragment extends BaseFragment {

	public static final String TAG_NAME = "MenuLanguage";

	private Integer[] _languageList = new Integer[] { R.string.english,
			R.string.hindi, R.string.tamil, R.string.gujarati };

	private View mRootView;
	private ListView listView;
	private ContentMenuLanguageAdapter mAdapter;
	private PConstants.MENU_LANGUAGE mLanguageSelected;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_menu_language,
				container, false);
		listView = (ListView) mRootView
				.findViewById(R.id.menu_language_list_view);
		mAdapter = new ContentMenuLanguageAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, _languageList);
		listView.setAdapter(mAdapter);
		mAdapter.setSelectedItem(AppState.getInstance().getMenuLanguageId());

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				mAdapter.setSelectedItem(position);

				if (position == PConstants.MENU_LANGUAGE.ENGLISH.getId()) {
					mLanguageSelected = PConstants.MENU_LANGUAGE.ENGLISH;
				} else if (position == PConstants.MENU_LANGUAGE.HINDI.getId()) {
					mLanguageSelected = PConstants.MENU_LANGUAGE.HINDI;
				} else if (position == PConstants.MENU_LANGUAGE.TAMIL.getId()) {
					mLanguageSelected = PConstants.MENU_LANGUAGE.TAMIL;
				} else if (position == PConstants.MENU_LANGUAGE.GUJARATI
						.getId()) {
					mLanguageSelected = PConstants.MENU_LANGUAGE.GUJARATI;
				} else {
					mLanguageSelected = PConstants.MENU_LANGUAGE.ENGLISH;
				}

				mParentActivity.mApp.setMenuLanguageId(mLanguageSelected.id);
				mParentActivity.mApp
						.setMenuLanguageTypeface(mLanguageSelected.typeface);
				mParentActivity.mApp
						.setMenuLanguageLocale(mLanguageSelected.locale);
				PUtils.setLocale(mParentActivity, mLanguageSelected.locale);

				mAdapter.notifyDataSetChanged();
			}
		});
		return mRootView;

	}

}
