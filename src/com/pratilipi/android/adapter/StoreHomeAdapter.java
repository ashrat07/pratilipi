package com.pratilipi.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.StoreContent;
import com.pratilipi.android.ui.BookSummaryFragment;
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.ui.TopContentFragment;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;
import com.pratilipi.android.util.PConstants;

public class StoreHomeAdapter extends ArrayAdapter<StoreContent> {

	SplashActivity activity;
	int resource;

	static class ViewHolder {
		TextView headerTextView;
		TextView viewAllTextView;
		LinearLayout contentScrollLayout;
	}

	public StoreHomeAdapter(Context context, int resource,
			List<StoreContent> list) {
		super(context, resource, list);
		this.activity = (SplashActivity) context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(resource,
					parent, false);

			viewHolder = new ViewHolder();
			Typeface typeface = FontManager.getInstance().get(
					AppState.getInstance().getMenuLanguageTypeface());
			viewHolder.headerTextView = (TextView) convertView
					.findViewById(R.id.header_text_view);
			viewHolder.headerTextView.setTypeface(typeface);
			viewHolder.viewAllTextView = (TextView) convertView
					.findViewById(R.id.view_all_text_view);
			viewHolder.viewAllTextView.setTypeface(typeface);
			viewHolder.contentScrollLayout = (LinearLayout) convertView
					.findViewById(R.id.content_scroll_layout);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final StoreContent storeContent = getItem(position);
		viewHolder.headerTextView.setText(storeContent.name);
		viewHolder.viewAllTextView
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Bundle bundle = new Bundle();
						bundle.putString("CATEGORY", storeContent.id);
						activity.showNextView(new TopContentFragment(), bundle);
					}
				});

		for (final Book book : storeContent.content) {
			View view = View.inflate(activity, R.layout.layout_book_preview,
					null);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.image_view);
			TextView titleTextView = (TextView) view
					.findViewById(R.id.title_text_view);
			RatingBar ratingBar = (RatingBar) view
					.findViewById(R.id.rating_bar);
			TextView starCountTextView = (TextView) view
					.findViewById(R.id.star_count_text_view);
			TextView freeTextView = (TextView) view
					.findViewById(R.id.free_text_view);
			View priceLayout = view.findViewById(R.id.price_layout);
			TextView mrpTextView = (TextView) view
					.findViewById(R.id.mrp_text_view);
			TextView sellingPriceTextView = (TextView) view
					.findViewById(R.id.selling_price_text_view);

			String coverImageUrl = PConstants.COVER_IMAGE_URL.replace(
					PConstants.PLACEHOLDER_PRATILIPI_ID, "" + book.id);
			activity.mImageLoader.displayImage(coverImageUrl, imageView);
			titleTextView.setText(book.title);
			titleTextView.setTypeface(FontManager.getInstance().get(
					AppState.getInstance().getContentLanguage()));
			ratingBar.setRating(book.ratingCount);
			starCountTextView.setText("(" + book.starCount + ")");
			if (Math.random() > 0.5) {
				freeTextView.setVisibility(View.VISIBLE);
				priceLayout.setVisibility(View.GONE);
			} else {
				freeTextView.setVisibility(View.GONE);
				priceLayout.setVisibility(View.VISIBLE);
				mrpTextView.setText("`999");
				mrpTextView.setPaintFlags(mrpTextView.getPaintFlags()
						| Paint.STRIKE_THRU_TEXT_FLAG);
				sellingPriceTextView.setText("`499");
			}

			view.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putParcelable("BOOK", book);
					activity.showNextView(new BookSummaryFragment(), bundle);
				}
			});
			viewHolder.contentScrollLayout.addView(view);
		}

		return convertView;
	}

}
