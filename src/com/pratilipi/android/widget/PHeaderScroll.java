package com.pratilipi.android.widget;

import java.util.ArrayList;

import com.pratilipi.android.R;

import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PHeaderScroll {

	LinearLayout mLayout;
	ViewPager mPager;
	Integer mCurrentSelected = 0;
	ArrayList<String> mTabNames = new ArrayList<String>();

	public PHeaderScroll(LinearLayout layout, ViewPager pager,
			ArrayList<TextView> tabs) {
		this.mLayout = layout;
		this.mPager = pager;

		if (mTabNames.size() > 0) {
			mTabNames.clear();
		}

		if (mLayout.getChildCount() > 0) {
			mLayout.removeAllViews();
		}

		int i = 0;
		for (TextView tab : tabs) {
			tab.setOnClickListener(new HandleClick(i));
			mTabNames.add(tab.getText().toString());
			mLayout.addView(tab, i);
			i++;
		}

		// initialise the tab 0
		setDrawable(true);
	}

	public int getSelected() {
		return mCurrentSelected;
	}

	public String setSelected(int pos) {
		setDrawable(false);
		mCurrentSelected = pos;
		return setDrawable(true);
	}

	private String setDrawable(boolean selected) {
		View v = mLayout.getChildAt(mCurrentSelected);
		if (v != null) {
			v.setSelected(selected);
			if (selected) {
				((HorizontalScrollView) mLayout.getParent()).smoothScrollTo(
						v.getLeft() - 25, 0);
				((TextView) v.findViewById(R.id.header_tab_text_view))
						.setText(Html.fromHtml("<b>"
								+ mTabNames.get(mCurrentSelected) + "</b>"));
			} else {
				((TextView) v.findViewById(R.id.header_tab_text_view))
						.setText(mTabNames.get(mCurrentSelected));
			}
			return ((TextView) v).getText().toString();
		}
		return null;
	}

	class HandleClick implements View.OnClickListener {

		int mIndex;

		public HandleClick(int i) {
			mIndex = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(mIndex);
		}

	}

}
