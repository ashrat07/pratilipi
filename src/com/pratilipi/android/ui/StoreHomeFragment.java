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
import com.pratilipi.android.adapter.StoreHomeAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.StoreListing;
import com.pratilipi.android.util.PConstants;

public class StoreHomeFragment extends BaseFragment {

	public static final String TAG_NAME = "Store Home";

	private View mRootView;
	private ListView mListView;
	private View mProgressBar;
	private StoreHomeAdapter mAdapter;
	private List<StoreListing> storeListings;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_store_home, container,
				false);

		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		mProgressBar = mRootView.findViewById(R.id.progress_bar);

		if (storeListings == null) {
			storeListings = new ArrayList<>();
		} else {
			storeListings.clear();
		}
		mAdapter = new StoreHomeAdapter(mParentActivity,
				R.layout.layout_store_list_view_item, storeListings);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		mProgressBar.setVisibility(View.VISIBLE);
		requestStoreHomeListings();

		return mRootView;
	}

	private void requestStoreHomeListings() {
		HttpGet storeHomeListingsRequest = new HttpGet(this,
				PConstants.STORE_HOME_LISTINGS_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.STORE_HOME_LISTINGS_URL);
		requestHashMap.put("languageId",
				mParentActivity.mApp.getLanguageHashCode());

		storeHomeListingsRequest.run(requestHashMap);
	}

	@Override
	public Boolean setPostStatus(JSONObject finalResult, String postUrl,
			int responseCode) {
		return null;
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (getUrl.equals(PConstants.STORE_HOME_LISTINGS_URL)) {
			if (finalResult != null) {
				try {
					JSONObject responseObject = finalResult
							.getJSONObject("response");
					if (responseObject != null) {
						JSONArray listingArray = responseObject
								.getJSONArray("elements");
						if (listingArray != null) {
							for (int i = 0; i < listingArray.length(); i++) {
								JSONObject listingObject = listingArray
										.getJSONObject(i);
								String name = listingObject.getString("name");
								String id = listingObject.getString("id");
								JSONArray contentArray = listingObject
										.getJSONArray("content");
								List<Book> content = new ArrayList<>();
								for (int j = 0; j < contentArray.length(); j++) {
									Book book = new Book(
											contentArray.getJSONObject(j));
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
