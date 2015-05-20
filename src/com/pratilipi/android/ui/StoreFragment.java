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

	private String pages[] = new String[] { "HOME", "CATEGORIES", "PROFILE" };

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

		private String categories[] = new String[] { "Classics", "Horror",
				"Poems", "Romance", "Stories", "Ghazals" };

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
				ArrayAdapter<String> adapter = new ArrayAdapter<>(
						mParentActivity,
						R.layout.layout_categories_list_view_item, categories);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Bundle bundle = new Bundle();
						bundle.putString("CATEGORY", categories[position]);
						mParentActivity.showNextView(
								new LanguageSelectionFragment(), bundle);
					}
				});

				layout.addView(listView);
				break;
			}

			default:
				TextView textView = new TextView(mParentActivity);
				textView.setTextColor(Color.BLACK);
				textView.setTextSize(30);
				textView.setTypeface(Typeface.DEFAULT_BOLD);
				textView.setText(String.valueOf(position));

				layout.addView(textView);
				break;
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

		private View featuredViewAll;
		private LinearLayout featuredScrollLayout;
		private View newReleasesViewAll;
		private LinearLayout newReleasesScrollLayout;

		private List<Book> featuredList;
		private List<Book> newReleasesList;
		private Book book;

		public View getView() {

			View view = View.inflate(mParentActivity,
					R.layout.layout_store_home, null);

			featuredViewAll = view.findViewById(R.id.featured_view_all);
			featuredScrollLayout = (LinearLayout) view
					.findViewById(R.id.featured_scroll_layout);
			newReleasesViewAll = view.findViewById(R.id.new_releases_view_all);
			newReleasesScrollLayout = (LinearLayout) view
					.findViewById(R.id.new_releases_scroll_layout);

			featuredViewAll.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mParentActivity
							.showNextView(new LanguageSelectionFragment());
				}
			});
			newReleasesViewAll.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					mParentActivity
							.showNextView(new LanguageSelectionFragment());
				}
			});

			if (featuredList == null) {
				featuredList = new ArrayList<>();
			} else {
				featuredList.clear();
			}
			requestFeatured();

			if (newReleasesList == null) {
				newReleasesList = new ArrayList<>();
			} else {
				newReleasesList.clear();
			}
			requestNewReleases();

			return view;
		}

		public void requestFeatured() {
			HttpGet featuredRequest = new HttpGet(this, PConstants.FEATURED_URL);

			HashMap<String, String> requestHashMap = new HashMap<>();
			requestHashMap.put(PConstants.URL,
					PConstants.FEATURED_URL.replace(PConstants.TERM, "data"));

			featuredRequest.run(requestHashMap);
		}

		public void requestNewReleases() {
			HttpGet newReleasedRequest = new HttpGet(this,
					PConstants.NEW_RELEASES_URL);

			HashMap<String, String> requestHashMap = new HashMap<>();
			requestHashMap.put(PConstants.URL, PConstants.NEW_RELEASES_URL
					.replace(PConstants.TERM, "data"));

			newReleasedRequest.run(requestHashMap);
		}

		@Override
		public Boolean setPostStatus(JSONObject finalResult, String postUrl,
				int responseCode) {
			return null;
		}

		@Override
		public Boolean setGetStatus(JSONObject finalResult, String getUrl,
				int responseCode) {
			if (getUrl.equals(PConstants.FEATURED_URL)) {
				if (finalResult != null) {
					try {
						JSONObject feed = finalResult.getJSONObject("feed");
						JSONArray entries = feed.getJSONArray("entry");
						for (int i = 0; i < entries.length(); i++) {
							JSONObject entry = entries.getJSONObject(i);
							String imageURL = entry.getJSONArray("im:image")
									.getJSONObject(2).getString("label");
							String title = entry.getJSONObject("title")
									.getString("label");
							String name = entry.getJSONObject("im:name")
									.getString("label");
							String artist = entry.getJSONObject("im:artist")
									.getString("label");
							String summary = entry.getJSONObject("summary")
									.getString("label");
							String category = entry.getJSONObject("category")
									.getJSONObject("attributes")
									.getString("label");
							book = new Book(imageURL, title, name, artist,
									summary, category, "");
							featuredList.add(book);

							View view = View.inflate(mParentActivity,
									R.layout.layout_book_preview, null);
							ImageView imageView = (ImageView) view
									.findViewById(R.id.image_view);
							TextView titleTextView = (TextView) view
									.findViewById(R.id.title_text_view);
							mParentActivity.mImageLoader.displayImage(
									book.imageURL, imageView);
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
							featuredScrollLayout.addView(view);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if (getUrl.equals(PConstants.NEW_RELEASES_URL)) {
				if (finalResult != null) {
					try {
						JSONObject feed = finalResult.getJSONObject("feed");
						JSONArray entries = feed.getJSONArray("entry");
						for (int i = 0; i < entries.length(); i++) {
							JSONObject entry = entries.getJSONObject(i);
							String imageURL = entry.getJSONArray("im:image")
									.getJSONObject(2).getString("label");
							String title = entry.getJSONObject("title")
									.getString("label");
							String name = entry.getJSONObject("im:name")
									.getString("label");
							String artist = entry.getJSONObject("im:artist")
									.getString("label");
							String summary = entry.getJSONObject("summary")
									.getString("label");
							String category = entry.getJSONObject("category")
									.getJSONObject("attributes")
									.getString("label");
							book = new Book(imageURL, title, name, artist,
									summary, category, "");
							newReleasesList.add(book);

							View view = View.inflate(mParentActivity,
									R.layout.layout_book_preview, null);
							ImageView imageView = (ImageView) view
									.findViewById(R.id.image_view);
							TextView titleTextView = (TextView) view
									.findViewById(R.id.title_text_view);
							mParentActivity.mImageLoader.displayImage(
									book.imageURL, imageView);
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
							newReleasesScrollLayout.addView(view);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

	}

}
