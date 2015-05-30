package com.pratilipi.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryAdapter extends ArrayAdapter<Integer> {

	Context context;
	int layoutResourceId;

	public CategoryAdapter(Context context, int layoutResourceId, Integer[] list) {
		super(context, layoutResourceId, list);
		this.context = context;
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					layoutResourceId, parent, false);
		}
		int item = getItem(position);
		((TextView) convertView)
				.setText(context.getResources().getString(item));
		return convertView;
	}

}
