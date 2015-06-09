package com.pratilipi.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.model.Book;
import com.pratilipi.android.ui.SplashActivity;

public class SearchAdapter extends ArrayAdapter<Book> {

	SplashActivity activity;
	int resource;

	static class ViewHolder {
		ImageView imageView;
		TextView titleTextView;
		TextView titleEnTextView;
	}

	public SearchAdapter(Context context, int resource, List<Book> list) {
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
			viewHolder.titleEnTextView = (TextView) convertView
					.findViewById(R.id.title_en_text_view);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Book book = getItem(position);
		activity.mImageLoader.displayImage(book.coverImageUrl,
				viewHolder.imageView);
		viewHolder.titleTextView.setText(book.title);
		viewHolder.titleEnTextView.setText(book.titleEn);

		return convertView;
	}

}
