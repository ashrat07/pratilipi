package com.pratilipi.android.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.pratilipi.android.R;
import com.pratilipi.android.adapter.ProfileAdapter;
import com.pratilipi.android.util.ImageLoader;

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

		ImageView userImageView = (ImageView) mRootView
				.findViewById(R.id.profile_img);
		String userImgUrl = "http://dummyimage.com/250/EF724B/";
		new ImageLoader(mRootView.getContext()).displayFullImage(userImgUrl,
				userImageView);

		mListView = (ListView) mRootView.findViewById(R.id.profile_list_view);

		ProfileAdapter adapter = new ProfileAdapter(mParentActivity,
				R.layout.layout_list_view_text_item, profileItemsList);
		mListView.setAdapter(adapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				switch (position) {
				case 0:
					mParentActivity.showNextView(new ProfileLanguageFragment());
					break;
				}
			}
		});
		return mRootView;
	}

}
