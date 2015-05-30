package com.pratilipi.android.ui;

import com.pratilipi.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileFragment {

	public static final String TAG_NAME = "Profile";

	public View getView(Context mParentActivity) {

		View view = View.inflate(mParentActivity, R.layout.fragment_profile,
				null);
		
		Integer[] profileItemsList = new Integer[] { R.string.reset_menu,
				R.string.reset_content, R.string.about };

		ListView listView = (ListView)view.findViewById(R.id.profile_list_view);
		
		ProfileAdapter adapter = new ProfileAdapter(mParentActivity,
				R.layout.layout_profile_list_view, profileItemsList);
		
		
		
		listView.setAdapter(adapter);
		
		return view;
	}

	class ProfileAdapter extends ArrayAdapter<Integer> {

		int layoutResourceId;
		Context mParentActivity;

		public ProfileAdapter(Context mParentActivity, int resource,
				Integer[] profileItemsList) {
			super(mParentActivity, resource, profileItemsList);
			this.layoutResourceId = resource;
			this.mParentActivity= mParentActivity;
			
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mParentActivity).inflate(
						layoutResourceId, parent, false);
			}
			int item = getItem(position);
			((TextView) convertView).setText(mParentActivity.getResources()
					.getString(item));
			return convertView;
		}

		
	}

}
