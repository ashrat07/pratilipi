package com.pratilipi.android.adapter;

import com.pratilipi.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContentMenuLanguageAdapter extends ArrayAdapter<Integer> {

	private Context context;
	private int layoutResourceId;
	private int selectedItem;

	public ContentMenuLanguageAdapter(Context context, int resource,
			Integer[] objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutResourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = LayoutInflater.from(context).inflate(
					layoutResourceId, parent, false);

		int item = getItem(position);
		((TextView) convertView)
				.setText(context.getResources().getString(item));

		((TextView) convertView).setBackground(context.getResources().getDrawable(R.drawable.language_list_selector));
		
		if (position == selectedItem) {
			((TextView) convertView).setCompoundDrawables(
					null,
					null,
					context.getResources().getDrawable(
							R.drawable.arrow_right), null);
		} else {
			((TextView) convertView).setCompoundDrawables(null, null, context
					.getResources().getDrawable(R.drawable.ic_launcher), null);
		}

		return convertView;
	}

	public void setSelectedItem(int position) {
		selectedItem = position;
	}
}
