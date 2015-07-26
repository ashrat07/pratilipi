package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.StoreCategoryAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Category;
import com.pratilipi.android.util.PConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreCategoryFragment extends BaseFragment {

	public static final String TAG_NAME = "Store Category";
	private static List<Category> mList = new ArrayList<>();

	private View mRootView;
	private ListView mListView;
	private View mProgressBarLayout;
	private StoreCategoryAdapter mAdapter;
	private HttpGet storeCategoryRequest;

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
		mProgressBarLayout = mRootView.findViewById(R.id.progress_bar_layout);

		mAdapter = new StoreCategoryAdapter(mParentActivity,
				R.layout.layout_store_category_list_view_item, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Bundle bundle = new Bundle();
				bundle.putLong("CATEGORY_ID", mList.get(position).id);
				mParentActivity.showNextView(new CategoryFragment(), bundle);
			}
		});

		if (mList.size() == 0) {
			mProgressBarLayout.setVisibility(View.VISIBLE);
			requestStoreCategory();
		}

		return mRootView;
	}

	@Override
	public void onBackPressed() {
		if (storeCategoryRequest != null) {
			storeCategoryRequest.cancel(true);
		}
		super.onBackPressed();
	}

	private void requestStoreCategory() {
		storeCategoryRequest = new HttpGet(this, PConstants.STORE_CATEGORY_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.STORE_CATEGORY_URL);
		requestHashMap.put("languageId",
				mParentActivity.mApp.getContentLanguageHashCode());

		storeCategoryRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.STORE_CATEGORY_URL.equals(getUrl)) {
			mProgressBarLayout.setVisibility(View.GONE);
			if (finalResult != null) {
				try {
					JSONArray categoryArray = finalResult
							.getJSONArray("categoryDataList");
					if (categoryArray != null) {
						for (int i = 0; i < categoryArray.length(); i++) {
							JSONObject obj = categoryArray.getJSONObject(i);
							Category category = new Category(obj);
							mList.add(category);
						}
						mAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public void onDestroy() {
		if (mList != null && mList.size() > 0) {
			mList.clear();
		}
		super.onDestroy();
	}

}
