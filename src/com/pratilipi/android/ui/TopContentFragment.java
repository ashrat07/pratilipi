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
import com.pratilipi.android.adapter.TopContentAdapter;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class TopContentFragment extends BaseFragment {

	public static final String TAG_NAME = "Store Listing";
	private static List<Book> mList = new ArrayList<>();

	private View mRootView;
	private ListView mListView;
	private View mEmptyMessageView;
	private View mFooterView;
	private View mProgressViewLayout;

	private TopContentAdapter mAdapter;
	private Long mCategoryId;
	private String mCursor;
	private boolean mLoadNext = false;
	private HttpGet topContentRequest;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_top_content, container,
				false);
		mListView = (ListView) mRootView.findViewById(R.id.list_view);
		mEmptyMessageView = mRootView.findViewById(R.id.empty_message_view);
		mFooterView = inflater.inflate(
				R.layout.layout_top_content_list_view_footer, new LinearLayout(
						mParentActivity));
		mProgressViewLayout = mRootView.findViewById(R.id.progress_bar_layout);

		mListView.addFooterView(mFooterView);
		mAdapter = new TopContentAdapter(mParentActivity,
				R.layout.layout_top_content_list_view_item, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				Book book = mList.get(position);
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
					requestTopContent();
				}
			}
		});

		Bundle bundle = getArguments();
		if (bundle != null) {
			Long categoryId = bundle.getLong("CATEGORY_ID");
			mCategoryId = categoryId;
			if (mList.size() == 0) {
				mProgressViewLayout.setVisibility(View.VISIBLE);
				requestTopContent();
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

	@Override
	public void onStop() {
		if (topContentRequest != null) {
			topContentRequest.cancel(true);
		}
		super.onStop();
	}

	private void requestTopContent() {
		HttpGet topContentRequest = new HttpGet(this,
				PConstants.TOP_CONTENT_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.TOP_CONTENT_URL);
		requestHashMap.put("languageId", AppState.getInstance()
				.getContentLanguageHashCode());
		requestHashMap.put("state", "PUBLISHED");
		requestHashMap.put("categoryId", String.valueOf(mCategoryId));
		requestHashMap.put("resultCount", "10");
		if (mCursor != null && !TextUtils.isEmpty(mCursor)) {
			requestHashMap.put("cursor", mCursor);
		}

		topContentRequest.run(requestHashMap);
	}

	@Override
	public Boolean setGetStatus(JSONObject finalResult, String getUrl,
			int responseCode) {
		if (PConstants.TOP_CONTENT_URL.equals(getUrl)) {
			mProgressViewLayout.setVisibility(View.GONE);
			if (finalResult != null) {
				try {
					JSONArray dataArray = finalResult
							.getJSONArray("pratilipiList");
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
