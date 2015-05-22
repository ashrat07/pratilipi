package com.pratilipi.android.ui;

import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class BookSummaryFragment extends BaseFragment {

	public static final String TAG_NAME = "Book Summary";

	private View mRootView;
	private ImageView mImageView;
	private TextView mNameTextView;
	private TextView mArtistTextView;
	private TextView mSummaryTextView;

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
		mNameTextView = (TextView) mRootView.findViewById(R.id.name_text_view);
		mArtistTextView = (TextView) mRootView
				.findViewById(R.id.artist_text_view);
		mSummaryTextView = (TextView) mRootView
				.findViewById(R.id.summary_text_view);

		Bundle bundle = getArguments();
		if (bundle != null) {
			Book book = bundle.getParcelable("BOOK");
			if (book != null) {
				mParentActivity.mImageLoader.displayImage(book.coverImageUrl,
						mImageView);
				mNameTextView.setText(book.title);
				mArtistTextView.setText(book.author.fullName);
				if (!TextUtils.isEmpty(book.summary)) {
					mSummaryTextView.setText(Html.fromHtml(book.summary));
				}
			}
		}

		return mRootView;
	}

}
