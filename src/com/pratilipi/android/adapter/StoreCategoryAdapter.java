package com.pratilipi.android.adapter;

import java.util.List;

import com.pratilipi.android.model.Category;
import com.pratilipi.android.ui.SplashActivity;
import com.pratilipi.android.util.AppState;
import com.pratilipi.android.util.FontManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StoreCategoryAdapter extends ArrayAdapter<Category> {

	Context context;
	int resource;

	public StoreCategoryAdapter(Context context, int resource,
			List<Category> list) {
		super(context, resource, list);
		this.context = (SplashActivity) context;
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource,
					parent, false);
		}
		Category category = getItem(position);
		((TextView) convertView).setText(category.name);
		((TextView) convertView).setTypeface(FontManager.getInstance().get(
				AppState.getInstance().getMenuLanguageTypeface()));
		return convertView;
	}

}
