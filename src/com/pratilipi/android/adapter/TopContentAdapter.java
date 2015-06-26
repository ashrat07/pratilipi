package com.pratilipi.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Paint;
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
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;

public class TopContentAdapter extends ArrayAdapter<Book> {

	SplashActivity activity;
	int resource;

	static class ViewHolder {
		ImageView imageView;
		TextView titleTextView;
		TextView titleEnTextView;
		TextView authorNameTextView;
		RatingBar ratingBar;
		TextView starCountTextView;
		TextView freeTextView;
		View priceLayout;
		TextView mrpTextView;
		TextView sellingPriceTextView;
		LinearLayout genreLayout;
	}

	public TopContentAdapter(Context context, int resource, List<Book> list) {
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
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image_view);
			viewHolder.titleTextView = (TextView) convertView
					.findViewById(R.id.title_text_view);
			viewHolder.titleTextView.setTypeface(FontManager.getInstance().get(
					AppState.getInstance().getContentLanguage()));
			viewHolder.titleEnTextView = (TextView) convertView
					.findViewById(R.id.title_en_text_view);
			viewHolder.authorNameTextView = (TextView) convertView
					.findViewById(R.id.author_name_text_view);
			viewHolder.ratingBar = (RatingBar) convertView
					.findViewById(R.id.rating_bar);
			viewHolder.starCountTextView = (TextView) convertView
					.findViewById(R.id.star_count_text_view);
			viewHolder.freeTextView = (TextView) convertView
					.findViewById(R.id.free_text_view);
			viewHolder.priceLayout = convertView
					.findViewById(R.id.price_layout);
			viewHolder.mrpTextView = (TextView) convertView
					.findViewById(R.id.mrp_text_view);
			viewHolder.sellingPriceTextView = (TextView) convertView
					.findViewById(R.id.selling_price_text_view);
			viewHolder.genreLayout = (LinearLayout) convertView
					.findViewById(R.id.genre_layout);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Book book = getItem(position);

		activity.mImageLoader.displayImage(book.coverImageUrl,
				viewHolder.imageView);
		viewHolder.titleTextView.setText(book.title);
		viewHolder.titleEnTextView.setText(book.titleEn);
		viewHolder.authorNameTextView.setText(book.author.nameEn);
		viewHolder.ratingBar.setRating(book.ratingCount);
		viewHolder.starCountTextView.setText("(" + book.starCount + ")");
		if (Math.random() < 0.5) {
			viewHolder.freeTextView.setVisibility(View.VISIBLE);
			viewHolder.priceLayout.setVisibility(View.GONE);
		} else {
			viewHolder.freeTextView.setVisibility(View.GONE);
			viewHolder.priceLayout.setVisibility(View.VISIBLE);
			viewHolder.mrpTextView.setText("`999");
			viewHolder.mrpTextView.setPaintFlags(viewHolder.mrpTextView
					.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
			viewHolder.sellingPriceTextView.setText("`499");
		}
		viewHolder.genreLayout.removeAllViews();
		if (book.genreNameList != null) {
			for (String genre : book.genreNameList) {
				View genreView = LayoutInflater.from(activity).inflate(
						R.layout.layout_genre, null);
				((TextView) genreView).setText(genre);
				viewHolder.genreLayout.addView(genreView);
			}
		}

		return convertView;
	}

}
