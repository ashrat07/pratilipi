package com.pratilipi.android.ui;

import com.pratilipi.android.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ProfileFragment extends BaseFragment {

	public static final String TAG_NAME = "Profile";

	private Integer[] profileItemsList = new Integer[] { R.string.reset_menu,
			R.string.reset_content, R.string.about };

	private View mRootView;
	private ListView mListView;

	@Override
	public String getCustomTag() {
		return TAG_NAME;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_profile, container,
				false);

		mListView = (ListView) mRootView.findViewById(R.id.profile_list_view);

		ProfileAdapter adapter = new ProfileAdapter(mParentActivity,
				R.layout.layout_profile_list_view, profileItemsList);
		mListView.setAdapter(adapter);

		return mRootView;
	}

	class ProfileAdapter extends ArrayAdapter<Integer> {

		int layoutResourceId;
		Context mParentActivity;

		public ProfileAdapter(Context mParentActivity, int resource,
				Integer[] profileItemsList) {
			super(mParentActivity, resource, profileItemsList);
			this.layoutResourceId = resource;
			this.mParentActivity = mParentActivity;

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
