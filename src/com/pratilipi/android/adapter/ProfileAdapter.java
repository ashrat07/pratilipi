package com.pratilipi.android.adapter;

import com.pratilipi.android.util.FontManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProfileAdapter extends ArrayAdapter<Integer> {
	
	int layOutResourceId;
	Context context;
	
	public ProfileAdapter(Context context, int layOutResourceId, Integer[] list)
	{
		super(context, layOutResourceId, list);
		this.context= context;
		this.layOutResourceId= layOutResourceId;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView == null)
		{
			convertView = LayoutInflater.from(context).inflate(layOutResourceId, parent,false);
		}
		int item = getItem(position);
		((TextView) convertView)
				.setText(context.getResources().getString(item));
		((TextView) convertView).setTypeface(FontManager.getInstance().get("regular"));
		return convertView;
	}
}
