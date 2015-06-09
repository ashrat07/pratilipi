package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.StoreCategoryAdapter;

public class StoreCategoryFragment extends BaseFragment {

	public static final String TAG_NAME = "Store Category";

	private Integer categories[] = new Integer[] { R.string.classics,
			R.string.horror, R.string.poems, R.string.romance,
			R.string.stories, R.string.ghazals };

	private View mRootView;
	private ListView mListView;
	private StoreCategoryAdapter mAdapter;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_store_category,
				container, false);
		mListView = (ListView) mRootView.findViewById(R.id.list_view);

		mAdapter = new StoreCategoryAdapter(mParentActivity,
				R.layout.layout_categories_list_view_item, categories);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		return mRootView;
	}

}
