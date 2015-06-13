package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ProfileAdapter;

public class ProfileFragment extends BaseFragment {

	public static final String TAG_NAME = "Profile";

	private Integer[] profileItemsList = new Integer[] { R.string.reset_content,
			R.string.reset_menu, R.string.about };

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

		TextView userName = (TextView) mRootView.findViewById(R.id.user_name);
		TextView memberSince = (TextView) mRootView
				.findViewById(R.id.member_since);
		TextView userShelfCount = (TextView) mRootView
				.findViewById(R.id.user_shelf_count);
		ImageView userImageView = (ImageView) mRootView
				.findViewById(R.id.profile_img);
		mListView = (ListView) mRootView.findViewById(R.id.profile_list_view);

		userName.setText("DEMO USER");
		memberSince.setText("Member Since");
		userShelfCount.setText("17 Books in shelf");
		mParentActivity.mImageLoader.displayFullImage(
				"http://lorempixel.com/200/200/people/", userImageView);

		ProfileAdapter adapter = new ProfileAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, profileItemsList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				switch (position) {
				case 0:
				case 1:
					Bundle bundle = new Bundle();
					bundle.putInt("MENU_TYPE", position);
					mParentActivity.showNextView(new ProfileLanguageFragment(),
							bundle);
					break;

				case 2:
					mParentActivity.showNextView(new AboutFragment());
					break;
				}
			}
		});
		return mRootView;
	}
}
