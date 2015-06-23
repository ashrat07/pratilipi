package com.pratilipi.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.model.StoreContent;
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.ui.TopContentFragment;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;

public class StoreHomeAdapter extends ArrayAdapter<StoreContent> {

	SplashActivity activity;
	int resource;

	static class ViewHolder {
		TextView headerTextView;
		TextView viewAllTextView;
		RecyclerView contentRecyclerView;
		StoreHomeRecyclerViewAdapter contentRecyclerViewAdapter;
		LinearLayoutManager layoutManager;
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
			viewHolder.contentRecyclerView = (RecyclerView) convertView
					.findViewById(R.id.content_recycler_view);

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

		viewHolder.contentRecyclerView.setHasFixedSize(true);
		viewHolder.layoutManager = new LinearLayoutManager(activity,
				LinearLayoutManager.HORIZONTAL, false);
		viewHolder.contentRecyclerView
				.setLayoutManager(viewHolder.layoutManager);
		viewHolder.contentRecyclerViewAdapter = new StoreHomeRecyclerViewAdapter(
				activity, storeContent.content);
		viewHolder.contentRecyclerView
				.setAdapter(viewHolder.contentRecyclerViewAdapter);

		return convertView;
	}

}
