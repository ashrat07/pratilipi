package com.pratilipi.android.widget;

import com.pratilipi.android.R;
import com.pratilipi.android.util.FontManager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class PTextView extends TextView {

	public PTextView(Context context) {
		super(context);
	}

	public PTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setupTypeface(context, attrs, 0);
	}

	public PTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setupTypeface(context, attrs, defStyle);
	}

	private void setupTypeface(Context context, AttributeSet attrs, int defStyle) {

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PTextView, defStyle, 0);
		String fontType = a.getString(R.styleable.PTextView_textStyle);

		setTypeface(FontManager.getInstance().get(fontType));

		// Note: This flag is required for proper typeface rendering
		setPaintFlags(getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);

		a.recycle();
	}
}
