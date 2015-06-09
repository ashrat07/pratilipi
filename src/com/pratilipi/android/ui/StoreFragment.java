package com.pratilipi.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.StoreAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.StoreListing;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class StoreFragment extends BaseFragment {

	public static final String TAG_NAME = "Store";

	private View mRootView;
	private ListView mListView;
	private View mProgressBar;
	private StoreAdapter mAdapter;
	private List<StoreListing> storeListings;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_store, container, false);

		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		mProgressBar = mRootView.findViewById(R.id.progress_bar);

		if (storeListings == null) {
			storeListings = new ArrayList<>();
		} else {
			storeListings.clear();
		}
		mAdapter = new StoreAdapter(mParentActivity,
				R.layout.layout_store_list_view_item, storeListings);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		mProgressBar.setVisibility(View.VISIBLE);
		requestFeatured();

		return mRootView;
	}

	public void requestFeatured() {
		HttpGet featuredRequest = new HttpGet(this, PConstants.HOME_LISTING_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.HOME_LISTING_URL.replace(
				PConstants.PLACEHOLDER_LANGUAGE_ID, AppState.getInstance()
						.getLanguageHashCode()));

		featuredRequest.run(requestHashMap);
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (getUrl.equals(PConstants.HOME_LISTING_URL)) {
			if (finalResult != null) {
				try {
					JSONObject responseObject = finalResult
							.getJSONObject("response");
					if (responseObject != null) {
						JSONArray listingArray = responseObject
								.getJSONArray("elements");
						if (listingArray != null) {
							for (int index = 0; index < listingArray.length(); index++) {
								JSONObject listingObject = listingArray
										.getJSONObject(index);
								String name = listingObject.getString("name");
								String id = listingObject.getString("id");
								JSONArray contentArray = listingObject
										.getJSONArray("content");
								List<Book> content = new ArrayList<>();
								for (int i = 0; i < contentArray.length(); i++) {
									Book book = new Book(
											contentArray.getJSONObject(i));
									content.add(book);
								}
								StoreListing storeListing = new StoreListing(
										id, name, content);
								storeListings.add(storeListing);
							}
						}
					}
					mProgressBar.setVisibility(View.GONE);
					mAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
