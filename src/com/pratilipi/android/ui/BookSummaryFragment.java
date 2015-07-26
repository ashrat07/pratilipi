package com.pratilipi.android.ui;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.ShelfContent;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.PConstants;
import com.pratilipi.android.util.ShelfContentDataSource;

public class BookSummaryFragment extends BaseFragment {

	public static final String TAG_NAME = "Book Summary";

	private View mRootView;
	private ImageView mImageView;
	private TextView mTitleTextView;
	private TextView mAuthorNameTextView;
	private RatingBar mRatingBar;
	private TextView mStarCountTextView;
	private LinearLayout mGenreLayout;
	private TextView mFreeTextView;
	private View mPriceLayout;
	private TextView mMrpTextView;
	private TextView mSellingPriceTextView;
	private TextView mReadTextView;
	private TextView mBuyNowTextView;
	private TextView mAddToShelfTextView;
	private TextView mSummaryTextView;

	private ShelfContentDataSource mDataSource;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_book_summary, container,
				false);

		mImageView = (ImageView) mRootView.findViewById(R.id.image_view);
		mTitleTextView = (TextView) mRootView
				.findViewById(R.id.title_text_view);
		mAuthorNameTextView = (TextView) mRootView
				.findViewById(R.id.author_name_text_view);
		mRatingBar = (RatingBar) mRootView.findViewById(R.id.rating_bar);
		mStarCountTextView = (TextView) mRootView
				.findViewById(R.id.star_count_text_view);
		mGenreLayout = (LinearLayout) mRootView.findViewById(R.id.genre_layout);
		mFreeTextView = (TextView) mRootView.findViewById(R.id.free_text_view);
		mPriceLayout = mRootView.findViewById(R.id.price_layout);
		mMrpTextView = (TextView) mRootView.findViewById(R.id.mrp_text_view);
		mSellingPriceTextView = (TextView) mRootView
				.findViewById(R.id.selling_price_text_view);
		mReadTextView = (TextView) mRootView.findViewById(R.id.read_text_view);
		mBuyNowTextView = (TextView) mRootView
				.findViewById(R.id.buy_now_text_view);
		mAddToShelfTextView = (TextView) mRootView
				.findViewById(R.id.add_to_shelf_text_view);
		mSummaryTextView = (TextView) mRootView
				.findViewById(R.id.summary_text_view);

		Bundle bundle = getArguments();
		if (bundle != null) {
			final Book book = bundle.getParcelable("BOOK");
			if (book != null) {
				mParentActivity.mImageLoader.displayImage(book.coverImageUrl,
						mImageView);
				mTitleTextView.setText(book.title);
				if (book.language != null) {
					String pratilipiLanguageId = String
							.valueOf(book.language.id);
					if (pratilipiLanguageId
							.equals(PConstants.CONTENT_LANGUAGE.HINDI
									.getHashCode())) {
						mTitleTextView.setTypeface(FontManager.getInstance()
								.get(PConstants.CONTENT_LANGUAGE.HINDI
										.toString()));
					} else if (pratilipiLanguageId
							.equals(PConstants.CONTENT_LANGUAGE.TAMIL
									.getHashCode())) {
						mTitleTextView.setTypeface(FontManager.getInstance()
								.get(PConstants.CONTENT_LANGUAGE.TAMIL
										.toString()));
					} else if (pratilipiLanguageId
							.equals(PConstants.CONTENT_LANGUAGE.GUJARATI
									.getHashCode())) {
						mTitleTextView.setTypeface(FontManager.getInstance()
								.get(PConstants.CONTENT_LANGUAGE.GUJARATI
										.toString()));
					} else {
						mTitleTextView.setTypeface(FontManager.getInstance()
								.get(AppState.getInstance()
										.getContentLanguage()));
					}
				} else {
					mTitleTextView.setTypeface(FontManager.getInstance().get(
							AppState.getInstance().getContentLanguage()));
				}
				mAuthorNameTextView.setText(book.author.name);
				if (book.author != null) {
					String languageId = String.valueOf(book.author.languageId);
					if (languageId.equals(PConstants.CONTENT_LANGUAGE.HINDI
							.getHashCode())) {
						mAuthorNameTextView.setTypeface(FontManager
								.getInstance().get(
										PConstants.CONTENT_LANGUAGE.HINDI
												.toString()));
					} else if (languageId
							.equals(PConstants.CONTENT_LANGUAGE.TAMIL
									.getHashCode())) {
						mAuthorNameTextView.setTypeface(FontManager
								.getInstance().get(
										PConstants.CONTENT_LANGUAGE.TAMIL
												.toString()));
					} else if (languageId
							.equals(PConstants.CONTENT_LANGUAGE.GUJARATI
									.getHashCode())) {
						mAuthorNameTextView.setTypeface(FontManager
								.getInstance().get(
										PConstants.CONTENT_LANGUAGE.GUJARATI
												.toString()));
					} else {
						mAuthorNameTextView.setTypeface(FontManager
								.getInstance().get(
										AppState.getInstance()
												.getContentLanguage()));
					}
				} else {
					mAuthorNameTextView.setTypeface(FontManager.getInstance()
							.get(AppState.getInstance().getContentLanguage()));
				}
				mRatingBar.setRating(book.ratingCount);
				mStarCountTextView.setText("(" + book.starCount + ")");
				mGenreLayout.removeAllViews();
				if (book.genreNameList != null) {
					for (String genre : book.genreNameList) {
						View genreView = inflater.inflate(
								R.layout.layout_genre, null);
						((TextView) genreView).setText(genre);
						mGenreLayout.addView(genreView);
					}
				}
				if (Math.random() < 0.5) {
					mFreeTextView.setVisibility(View.VISIBLE);
					mPriceLayout.setVisibility(View.GONE);
					mReadTextView.setVisibility(View.VISIBLE);
					mBuyNowTextView.setVisibility(View.GONE);
					mReadTextView
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									Gson gson = new Gson();
									ShelfContent shelfContent = new ShelfContent(
											0, book.id, gson.toJson(book),
											mParentActivity.mApp
													.getContentLanguage());

									Intent i = new Intent(mParentActivity,
											ReaderActivity.class);
									i.putExtra("SHELF_CONTENT", shelfContent);
									startActivity(i);
								}
							});
					mAddToShelfTextView
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									Gson gson = new Gson();
									mDataSource = new ShelfContentDataSource(
											mParentActivity);
									mDataSource.open();
									ShelfContent shelfContent = new ShelfContent(
											0, book.id, gson.toJson(book),
											mParentActivity.mApp
													.getContentLanguage());
									mDataSource
											.createShelfContent(shelfContent);
									Toast.makeText(mParentActivity,
											"Added to Shelf",
											Toast.LENGTH_SHORT).show();
								}
							});
				} else {
					mFreeTextView.setVisibility(View.GONE);
					mPriceLayout.setVisibility(View.VISIBLE);
					mMrpTextView.setText("`999");
					mMrpTextView.setPaintFlags(mMrpTextView.getPaintFlags()
							| Paint.STRIKE_THRU_TEXT_FLAG);
					mSellingPriceTextView.setText("`499");
					mReadTextView.setVisibility(View.GONE);
					mBuyNowTextView.setVisibility(View.VISIBLE);
				}
				if (!TextUtils.isEmpty(book.summary)) {
					mSummaryTextView.setText(Html.fromHtml(book.summary));
					mSummaryTextView.setTypeface(FontManager.getInstance().get(
							mParentActivity.mApp.getContentLanguage()));
				}
			}
		}

		return mRootView;
	}

	@Override
	public void onResume() {
		if (mDataSource != null) {
			mDataSource.open();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		if (mDataSource != null) {
			mDataSource.close();
		}
		super.onPause();
	}

}
