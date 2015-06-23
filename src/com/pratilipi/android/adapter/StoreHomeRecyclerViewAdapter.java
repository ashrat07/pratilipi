package com.pratilipi.android.adapter;

import java.util.List;

import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class StoreHomeRecyclerViewAdapter extends
		RecyclerView.Adapter<StoreHomeRecyclerViewAdapter.ViewHolder> {

	private SplashActivity activity;
	private List<Book> list;

	public static class ViewHolder extends RecyclerView.ViewHolder {

		View view;
		ImageView imageView;
		TextView titleTextView;
		RatingBar ratingBar;
		TextView starCountTextView;
		TextView freeTextView;
		View priceLayout;
		TextView mrpTextView;
		TextView sellingPriceTextView;

		public ViewHolder(View view, ImageView imageView,
				TextView titleTextView, RatingBar ratingBar,
				TextView starCountTextView, TextView freeTextView,
				View priceLayout, TextView mrpTextView,
				TextView sellingPriceTextView) {
			super(view);
			this.view = view;
			this.imageView = imageView;
			this.titleTextView = titleTextView;
			this.ratingBar = ratingBar;
			this.starCountTextView = starCountTextView;
			this.freeTextView = freeTextView;
			this.priceLayout = priceLayout;
			this.mrpTextView = mrpTextView;
			this.sellingPriceTextView = sellingPriceTextView;
		}

	}

	public StoreHomeRecyclerViewAdapter(Context context, List<Book> list) {
		this.activity = (SplashActivity) context;
		this.list = list;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(activity).inflate(
				R.layout.layout_book_preview, parent, false);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
		TextView titleTextView = (TextView) view
				.findViewById(R.id.title_text_view);
		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
		TextView starCountTextView = (TextView) view
				.findViewById(R.id.star_count_text_view);
		TextView freeTextView = (TextView) view
				.findViewById(R.id.free_text_view);
		View priceLayout = view.findViewById(R.id.price_layout);
		TextView mrpTextView = (TextView) view.findViewById(R.id.mrp_text_view);
		TextView sellingPriceTextView = (TextView) view
				.findViewById(R.id.selling_price_text_view);
		ViewHolder viewHolder = new ViewHolder(view, imageView, titleTextView,
				ratingBar, starCountTextView, freeTextView, priceLayout,
				mrpTextView, sellingPriceTextView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		Book book = list.get(position);
		activity.mImageLoader.displayImage(book.coverImageUrl,
				viewHolder.imageView);
		viewHolder.titleTextView.setText(book.title);
		viewHolder.titleTextView.setTypeface(FontManager.getInstance().get(
				AppState.getInstance().getContentLanguage()));
		viewHolder.ratingBar.setRating(book.ratingCount);
		viewHolder.starCountTextView.setText("(" + book.starCount + ")");
		if (Math.random() > 0.5) {
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
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

}
