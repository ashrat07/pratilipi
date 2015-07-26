package com.pratilipi.android.adapter;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.ui.BookSummaryFragment;
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;

public class StoreHomeRecyclerViewAdapter extends
		RecyclerView.Adapter<StoreHomeRecyclerViewAdapter.ViewHolder> {

	private SplashActivity activity;
	private List<Book> list;

	public static class ViewHolder extends RecyclerView.ViewHolder {

		View view;
		ImageView imageView;
		TextView titleTextView;
		TextView freeTextView;
		TextView sellingPriceTextView;

		public ViewHolder(View view, ImageView imageView,
				TextView titleTextView, TextView freeTextView,
				TextView sellingPriceTextView) {
			super(view);
			this.view = view;
			this.imageView = imageView;
			this.titleTextView = titleTextView;
			this.freeTextView = freeTextView;
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
		TextView freeTextView = (TextView) view
				.findViewById(R.id.free_text_view);
		TextView sellingPriceTextView = (TextView) view
				.findViewById(R.id.selling_price_text_view);
		ViewHolder viewHolder = new ViewHolder(view, imageView, titleTextView,
				freeTextView, sellingPriceTextView);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		final Book book = list.get(position);
		viewHolder.view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				bundle.putParcelable("BOOK", book);
				activity.showNextView(new BookSummaryFragment(), bundle);
			}
		});
		activity.mImageLoader.displayImage(book.coverImageUrl,
				viewHolder.imageView);
		viewHolder.titleTextView.setText(Html.fromHtml(book.title
				+ "<br />&#45" + book.author.name));
		viewHolder.titleTextView.setTypeface(FontManager.getInstance().get(
				AppState.getInstance().getContentLanguage()));
		if (Math.random() > 0.5) {
			viewHolder.freeTextView.setVisibility(View.VISIBLE);
			viewHolder.sellingPriceTextView.setVisibility(View.GONE);
		} else {
			viewHolder.freeTextView.setVisibility(View.GONE);
			viewHolder.sellingPriceTextView.setVisibility(View.VISIBLE);
			viewHolder.sellingPriceTextView.setText("`499");
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

}
