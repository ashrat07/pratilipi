package com.pratilipi.android.adapter;

import java.util.List;

import com.pratilipi.android.model.Chapter;
import com.pratilipi.android.util.FontManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ChapterAdapter extends ArrayAdapter<Chapter> {

	Context context;
	int resource;
	String language;

	public ChapterAdapter(Context context, int resource, List<Chapter> list,
			String language) {
		super(context, resource, list);
		this.context = context;
		this.resource = resource;
		this.language = language;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource,
					parent, false);
		}
		Chapter chapter = getItem(position);
		((TextView) convertView).setText(chapter.title);
		((TextView) convertView).setTypeface(FontManager.getInstance().get(
				language));
		return convertView;
	}
}
