package com.pratilipi.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
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
	private StoreAdapter mAdapter;
	private List<StoreListing> storeListingList;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_store, container, false);

		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		if (storeListingList == null) {
			storeListingList = new ArrayList<>();
		} else {
			storeListingList.clear();
		}
		mAdapter = new StoreAdapter(mParentActivity,
				R.layout.layout_store_list_view_item, storeListingList);
		mListView.setAdapter(mAdapter);

		requestFeatured();

		return mRootView;
	}

	public void requestFeatured() {
		HttpGet featuredRequest = new HttpGet(this, PConstants.HOME_LISTING_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.HOME_LISTING_URL.replace(
				PConstants.PLACEHOLDER_LANGUAGE_ID, AppState.getInstance()
						.getLanguageId()));

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
					JSONArray json = new JSONArray(
							finalResult.getString("response"));
					Log.e("json", "" + json);
					for (int index = 0; index < json.length(); index++) {
						JSONObject listObj = json.getJSONObject(index);
						String name = listObj.getString("name");
						String id = listObj.getString("id");
						JSONArray contentArray = new JSONArray(
								listObj.getString("content"));
						List<Book> content = new ArrayList<Book>();
						for (int i = 0; i < contentArray.length(); i++) {
							Book book = new Book(contentArray.getJSONObject(i));
							content.add(book);
						}
						StoreListing storeListing = new StoreListing(id, name,
								content);
						storeListingList.add(storeListing);
					}
					mAdapter.notifyDataSetChanged();
					// mListView.invalidateViews();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
