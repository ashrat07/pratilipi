package com.pratilipi.android.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.http.HttpGet;
import com.pratilipi.android.http.HttpResponseListener;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.widget.PHeaderScroll;

public class StoreFragment extends BaseFragment {

	public static final String TAG_NAME = "Store";

	private View mRootView;
	private EditText mSearchEditText;
	private View mSearchImageView;
	private LinearLayout mHeaderLayout;
	private ViewPager mViewPager;
	private StorePagerAdapter mPagerAdapter;

	private ArrayList<TextView> mHeaderTabs;
	private PHeaderScroll mHeaderScroll;

	private int pages[] = new int[] { R.string.home, R.string.categories,
			R.string.profiles };

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_store, container, false);

		mSearchEditText = (EditText) mRootView
				.findViewById(R.id.search_edit_text);
		mSearchImageView = mRootView.findViewById(R.id.search_image_view);
		mHeaderLayout = (LinearLayout) mRootView
				.findViewById(R.id.header_layout);
		mViewPager = (ViewPager) mRootView.findViewById(R.id.view_pager);

		mSearchImageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Boolean.TRUE.equals(mSearchImageView.getTag())) {
					mSearchImageView.setTag(Boolean.FALSE);
					mSearchEditText.setVisibility(View.GONE);
				} else {
					mSearchImageView.setTag(Boolean.TRUE);
					mSearchEditText.setVisibility(View.VISIBLE);
				}
			}
		});

		mHeaderTabs = new ArrayList<>();
		for (int i = 0; i < pages.length; i++) {
			TextView titleView = (TextView) View.inflate(mParentActivity,
					R.layout.layout_header_tab_white, null);
			titleView.setText(pages[i]);
			mHeaderTabs.add(titleView);
		}

		mPagerAdapter = new StorePagerAdapter();
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int position) {
						mHeaderScroll.setSelected(position);
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
					}

					@Override
					public void onPageScrollStateChanged(int state) {
					}
				});

		mHeaderScroll = new PHeaderScroll(mHeaderLayout, mViewPager,
				mHeaderTabs);

		return mRootView;
	}

	private class StorePagerAdapter extends PagerAdapter {

		private Integer categories[] = new Integer[] { R.string.classics,
				R.string.horror, R.string.poems, R.string.romance,
				R.string.stories, R.string.ghazals };

		@Override
		public int getCount() {
			return pages.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			LinearLayout layout = new LinearLayout(mParentActivity);
			layout.setOrientation(LinearLayout.VERTICAL);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layout.setLayoutParams(layoutParams);
			switch (position) {
			case 0: {

				StoreHome storeHome = new StoreHome();
				View view = storeHome.getView();

				layout.addView(view);
				break;
			}

			case 1: {
				ListView listView = new ListView(mParentActivity);
				listView.setDivider(null);
				listView.setDividerHeight(0);
				CategoryAdapter adapter = new CategoryAdapter(
						R.layout.layout_categories_list_view_item, categories);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Bundle bundle = new Bundle();
						bundle.putInt("CATEGORY", categories[position]);
						mParentActivity.showNextView(
								new LanguageSelectionFragment(), bundle);
					}
				});

				layout.addView(listView);
				break;
			}

			default:{	
				ProfileFragment profileFragment = new ProfileFragment();
				View profileView= profileFragment.getView(mParentActivity);
				layout.addView(profileView);
				
				
				break;
			}
			}

			container.addView(layout);
			return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((LinearLayout) object);
		}

	}

	private class StoreHome implements HttpResponseListener {

		private View topReadContentViewAll;
		private LinearLayout topReadContentScrollLayout;
		private List<Book> topReadContentList;

		public View getView() {

			View view = View.inflate(mParentActivity,
					R.layout.layout_store_home, null);

			topReadContentViewAll = view.findViewById(R.id.featured_view_all);
			topReadContentScrollLayout = (LinearLayout) view
					.findViewById(R.id.featured_scroll_layout);

			topReadContentViewAll
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							mParentActivity
									.showNextView(new LanguageSelectionFragment());
						}
					});

			if (topReadContentList == null) {
				topReadContentList = new ArrayList<>();
			} else {
				topReadContentList.clear();
			}
			requestFeatured();

			return view;
		}

		public void requestFeatured() {
			HttpGet featuredRequest = new HttpGet(this,
					PConstants.TOP_READ_CONTENT_URL);

			HashMap<String, String> requestHashMap = new HashMap<>();
			requestHashMap.put(PConstants.URL, PConstants.TOP_READ_CONTENT_URL
					.replace(PConstants.PLACEHOLDER_LANGUAGE_ID,
							mParentActivity.mApp.getLanguageId()));

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

	public class CategoryAdapter extends ArrayAdapter<Integer> {

		int layoutResourceId;

		CategoryAdapter(int layoutResourceId, Integer[] list) {
			super(mParentActivity, layoutResourceId, list);
			this.layoutResourceId = layoutResourceId;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mParentActivity).inflate(
						layoutResourceId, parent, false);
			}
			int item = getItem(position);
			((TextView) convertView).setText(mParentActivity.getResources()
					.getString(item));
			return convertView;
		}
	}

}
