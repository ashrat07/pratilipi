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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.PConstants;

public class StoreFragment extends BaseFragment {

	public static final String TAG_NAME = "Store";

	private View mRootView;
	private View topReadContentViewAll;
	private LinearLayout topReadContentScrollLayout;
	private List<Book> topReadContentList;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_store, container, false);

		topReadContentViewAll = mRootView.findViewById(R.id.featured_view_all);
		topReadContentScrollLayout = (LinearLayout) mRootView
				.findViewById(R.id.featured_scroll_layout);

		topReadContentViewAll.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mParentActivity.showNextView(new LanguageSelectionFragment());
			}
		});

		if (topReadContentList == null) {
			topReadContentList = new ArrayList<>();
		} else {
			topReadContentList.clear();
		}
		requestFeatured();

		return mRootView;
	}

	public void requestFeatured() {
		HttpGet featuredRequest = new HttpGet(this,
				PConstants.TOP_READ_CONTENT_URL);

		HashMap<String, String> requestHashMap = new HashMap<>();
		requestHashMap.put(PConstants.URL, PConstants.TOP_READ_CONTENT_URL
				.replace(PConstants.PLACEHOLDER_LANGUAGE_ID, AppState
						.getInstance().getLanguageId()));

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
		if (getUrl.equals(PConstants.TOP_READ_CONTENT_URL)) {
			if (finalResult != null) {
				try {
					JSONArray topReadContentArray = finalResult
							.getJSONArray("topReadPratilipiDataList");
					for (int i = 0; i < topReadContentArray.length(); i++) {
						JSONObject topReadContentObj = topReadContentArray
								.getJSONObject(i);
						final Book book = new Book(topReadContentObj);
						topReadContentList.add(book);

						View view = View.inflate(mParentActivity,
								R.layout.layout_book_preview, null);
						ImageView imageView = (ImageView) view
								.findViewById(R.id.image_view);
						TextView titleTextView = (TextView) view
								.findViewById(R.id.title_text_view);
						mParentActivity.mImageLoader.displayImage(
								book.coverImageUrl, imageView);
						titleTextView.setText(book.title);
						view.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								Bundle bundle = new Bundle();
								bundle.putParcelable("BOOK", book);
								mParentActivity.showNextView(
										new BookSummaryFragment(), bundle);
							}
						});
						topReadContentScrollLayout.addView(view);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
