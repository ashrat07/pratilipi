package com.pratilipi.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.SearchAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.util.PConstants;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class SearchFragment extends BaseFragment {

	public static final String TAG_NAME = "Search";

	private View mRootView;
	private ListView mListView;
	private SearchAdapter mAdapter;
	private List<Book> mSearchList;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater
				.inflate(R.layout.fragment_search, container, false);
		mListView = (ListView) mRootView.findViewById(R.id.list_view);

		if (mSearchList == null) {
			mSearchList = new ArrayList<>();
		} else {
			mSearchList.clear();
		}
		mAdapter = new SearchAdapter(mParentActivity,
				R.layout.layout_search_list_view_item, mSearchList);
		mListView.setAdapter(mAdapter);

		View headerView = inflater.inflate(
				R.layout.layout_search_list_view_header, container, false);
		mListView.addHeaderView(headerView);

		mParentActivity.showProgressBar();
		Bundle bundle = getArguments();
		if (bundle != null) {
			String query = bundle.getString("QUERY");
			requestSearch(query);
		}

		return mRootView;
	}

	private void requestSearch(String query) {
		HttpGet searchRequest = new HttpGet(this, PConstants.SEARCH_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.SEARCH_URL);
		requestHashMap.put("query", query);
		requestHashMap.put("resultCount", "10");

		searchRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.SEARCH_URL.equals(getUrl)) {
			if (finalResult != null) {
				try {
					JSONArray dataArray = finalResult
							.getJSONArray("pratilipiDataList");
					if (dataArray != null) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataObj = dataArray.getJSONObject(i);
							Book book = new Book(dataObj);
							mSearchList.add(book);
						}
						mParentActivity.hideProgressBar();
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
