package com.pratilipi.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.CategoryAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.util.PConstants;

public class CategoryFragment extends BaseFragment {

	public static final String TAG_NAME = "Category";
	private static List<Book> mList = new ArrayList<>();

	private View mRootView;
	private ListView mListView;
	private View mEmptyMessageView;
	private View mHeaderView;
	private View mFooterView;
	private CategoryAdapter mAdapter;
	private Long mCategoryId;
	private String mCursor;
	private boolean mLoadNext = false;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_category, container,
				false);
		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		mEmptyMessageView = mRootView.findViewById(R.id.empty_message_view);
		mHeaderView = inflater.inflate(
				R.layout.layout_top_content_list_view_header, new LinearLayout(
						mParentActivity));
		mFooterView = inflater.inflate(
				R.layout.layout_top_content_list_view_footer, new LinearLayout(
						mParentActivity));

		mListView.addHeaderView(mHeaderView);
		mListView.addFooterView(mFooterView);
		mAdapter = new CategoryAdapter(mParentActivity,
				R.layout.layout_category_list_view_item, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Book book = mList.get(position - 1);
				Bundle bundle = new Bundle();
				bundle.putParcelable("BOOK", book);
				mParentActivity.showNextView(new BookSummaryFragment(), bundle);
			}
		});
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount) && mLoadNext) {
					mLoadNext = false;
					requestCategory();
				}
			}
		});

		Bundle bundle = getArguments();
		if (bundle != null) {
			Long categoryId = bundle.getLong("CATEGORY_ID");
			mCategoryId = categoryId;
			if (mList.size() == 0) {
				mParentActivity.showProgressBar();
				requestCategory();
			} else {
				mListView.setVisibility(View.VISIBLE);
				if (mCursor != null && !TextUtils.isEmpty(mCursor)) {
					mFooterView.setVisibility(View.VISIBLE);
				} else {
					mFooterView.setVisibility(View.GONE);
				}
			}
		}

		return mRootView;
	}

	private void requestCategory() {
		HttpGet categoryRequest = new HttpGet(this, PConstants.CATEGORY_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.CATEGORY_URL);
		requestHashMap.put("languageId",
				mParentActivity.mApp.getContentLanguageHashCode());
		requestHashMap.put("categoryId", "" + mCategoryId);
		requestHashMap.put("resultCount", "10");
		if (mCursor != null && !TextUtils.isEmpty(mCursor)) {
			requestHashMap.put("cursor", mCursor);
		}

		categoryRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.CATEGORY_URL.equals(getUrl)) {
			mParentActivity.hideProgressBar();
			if (finalResult != null) {
				try {
					JSONArray dataArray = finalResult
							.getJSONArray("pratilipiDataList");
					if (dataArray != null) {
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject dataObj = dataArray.getJSONObject(i);
							Book book = new Book(dataObj);
							mList.add(book);
						}
						if (mList.size() > 0) {
							mListView.setVisibility(View.VISIBLE);
							mEmptyMessageView.setVisibility(View.GONE);
						} else {
							mListView.setVisibility(View.GONE);
							mEmptyMessageView.setVisibility(View.VISIBLE);
						}
						mAdapter.notifyDataSetChanged();
					}
					if (finalResult.has("cursor")) {
						mLoadNext = true;
						mCursor = finalResult.getString("cursor");
					} else {
						mLoadNext = false;
						mCursor = null;
						mFooterView.setVisibility(View.GONE);
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
