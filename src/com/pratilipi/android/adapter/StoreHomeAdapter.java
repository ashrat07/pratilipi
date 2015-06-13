package com.pratilipi.android.adapter;

import java.util.List;

import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.model.StoreListing;
import com.pratilipi.android.ui.BookSummaryFragment;
import com.pratilipi.android.ui.ProfileFragment;
import com.pratilipi.android.ui.SplashActivity;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StoreHomeAdapter extends ArrayAdapter<StoreListing> {

	SplashActivity activity;
	int resource;

	static class ViewHolder {
		TextView headerTextView;
		View viewAllTextView;
		LinearLayout contentScrollLayout;
	}

	public StoreHomeAdapter(Context context, int resource,
			List<StoreListing> list) {
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
			viewHolder.headerTextView = (TextView) convertView
					.findViewById(R.id.header_text_view);
			viewHolder.viewAllTextView = (TextView) convertView
					.findViewById(R.id.view_all_text_view);
			viewHolder.contentScrollLayout = (LinearLayout) convertView
					.findViewById(R.id.content_scroll_layout);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		StoreListing storeListing = getItem(position);
		viewHolder.headerTextView.setText(storeListing.name);
		viewHolder.viewAllTextView
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						activity.showNextView(new ProfileFragment());
					}
				});

		for (final Book book : storeListing.content) {
			View view = View.inflate(activity, R.layout.layout_book_preview,
					null);
			ImageView imageView = (ImageView) view
					.findViewById(R.id.image_view);
			TextView titleTextView = (TextView) view
					.findViewById(R.id.title_text_view);
			TextView mrpTextView = (TextView) view
					.findViewById(R.id.mrp_text_view);
			TextView sellingPriceTextView = (TextView) view
					.findViewById(R.id.selling_price_text_view);

			activity.mImageLoader.displayImage(book.coverImageUrl, imageView);
			titleTextView.setText(book.title);
			mrpTextView.setText("`999");
			mrpTextView.setPaintFlags(mrpTextView.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
			sellingPriceTextView.setText("`499");

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
