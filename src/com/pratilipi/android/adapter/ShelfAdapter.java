package com.pratilipi.android.adapter;

import com.pratilipi.android.R;
import com.pratilipi.android.ui.SplashActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShelfAdapter extends ArrayAdapter<String> {

	SplashActivity activity;
	int layoutResourceId;

	static class ViewHolder {
		ImageView imageView;
		TextView titleTextView;
		TextView titleEnTextView;
	}

	public ShelfAdapter(Context context, int layoutResourceId, String[] list) {
		super(context, layoutResourceId, list);
		this.activity = (SplashActivity) context;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					layoutResourceId, parent, false);

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
		String item = getItem(position);
		String url = "http://lorempixel.com/400/400/sports/" + (position % 10);
		activity.mImageLoader.displayFullImage(url, viewHolder.imageView);
		viewHolder.titleTextView.setText(item);
		viewHolder.titleEnTextView.setText(item);
		return convertView;
	}

}
