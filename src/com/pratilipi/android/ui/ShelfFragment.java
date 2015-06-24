package com.pratilipi.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ShelfAdapter;

public class ShelfFragment extends BaseFragment {

	public static final String TAG_NAME = "Shelf";

	private View mRootView;
	private ListView mListView;
	private ShelfAdapter mAdapter;
	private final String list[] = new String[] { "JANUARY", "FEBRUARY",
			"MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
			"OCTOBER", "NOVEMBER", "DECEMBER" };

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_shelf, container, false);

		mListView = (ListView) mRootView.findViewById(R.id.list_view);

		mAdapter = new ShelfAdapter(mParentActivity,
				R.layout.layout_shelf_list_view_item, list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Intent i = new Intent(mParentActivity, ReaderActivity.class);
				startActivity(i);
			}
		});

		return mRootView;
	}
}
