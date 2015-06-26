package com.pratilipi.android.ui;

import java.util.ArrayList;
import java.util.List;

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
import com.pratilipi.android.model.Shelf;
import com.pratilipi.android.util.ShelfDataSource;

public class ShelfFragment extends BaseFragment {

	public static final String TAG_NAME = "Shelf";
	private static List<Shelf> mList = new ArrayList<>();

	private View mRootView;
	private ListView mListView;
	private View mEmptyMessageView;
	private ShelfAdapter mAdapter;
	private ShelfDataSource mDataSource;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_shelf, container, false);

		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		mEmptyMessageView = mRootView.findViewById(R.id.empty_message_view);

		mDataSource = new ShelfDataSource(mParentActivity);
		mDataSource.open();
		mList = mDataSource.getAllContent();

		mListView.setEmptyView(mEmptyMessageView);
		mAdapter = new ShelfAdapter(mParentActivity,
				R.layout.layout_shelf_list_view_item, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Intent i = new Intent(mParentActivity, ReaderActivity.class);
				i.putExtra("SHELF", mList.get(position));
				startActivity(i);
			}
		});

		return mRootView;
	}

	@Override
	public void onResume() {
		if (mDataSource != null) {
			mDataSource.open();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (mDataSource != null) {
			mDataSource.close();
		}
		super.onPause();
	}

}
