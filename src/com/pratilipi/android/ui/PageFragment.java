package com.pratilipi.android.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratilipi.android.R;
import com.pratilipi.android.util.FontManager;

public class PageFragment extends Fragment {

	private final static String PAGE_TEXT = "PAGE_TEXT";
	private final static String TYPEFACE = "TYPEFACE";
	private final static String PAGE_NO = "PAGE_NO";
	private final static String TOTAL_PAGES = "TOTAL_PAGES";

	public static PageFragment newInstance(Bundle bundle) {
		PageFragment frag = new PageFragment();
		frag.setArguments(bundle);
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_reader_page, container,
				false);
		TextView contentTextView = (TextView) view
				.findViewById(R.id.content_text_view);
		TextView progressTextView = (TextView) view
				.findViewById(R.id.progress_text_view);
		contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.text_small));
		Bundle bundle = getArguments();
		if (bundle != null) {
			CharSequence text = bundle.getCharSequence(PAGE_TEXT);
			String typeface = bundle.getString(TYPEFACE);
			int pageNo = bundle.getInt(PAGE_NO);
			int totalPages = bundle.getInt(TOTAL_PAGES);
			contentTextView.setText(text);
			contentTextView
					.setTypeface(FontManager.getInstance().get(typeface));
			progressTextView.setText("Page " + pageNo + " of " + totalPages);
		}
		return view;
	}
}
