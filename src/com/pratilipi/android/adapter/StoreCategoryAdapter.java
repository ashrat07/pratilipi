package com.pratilipi.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StoreCategoryAdapter extends ArrayAdapter<Integer> {

	Context context;
	int resource;

	public StoreCategoryAdapter(Context context, int resource, Integer[] list) {
		super(context, resource, list);
		this.context = context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource,
					parent, false);
		}
		int item = getItem(position);
		((TextView) convertView)
				.setText(context.getResources().getString(item));
		return convertView;
	}

}
