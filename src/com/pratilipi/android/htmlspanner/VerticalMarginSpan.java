package com.pratilipi.android.htmlspanner;

import android.graphics.Paint;
import android.text.style.LineHeightSpan;

public class VerticalMarginSpan implements LineHeightSpan {

	private final Float factor;
	private final Integer absolute;

	public VerticalMarginSpan(Float margin) {
		this.factor = margin;
		this.absolute = null;
	}

	public VerticalMarginSpan(Integer value) {
		this.absolute = value;
		this.factor = null;
	}

	@Override
	public void chooseHeight(CharSequence text, int start, int end,
			int spanstartv, int v, Paint.FontMetricsInt fm) {

		int height = Math.abs(fm.descent - fm.ascent);

		if (factor != null) {
			height = (int) (height * factor);
		} else if (absolute != null) {
			height = absolute;
		}

		fm.descent = fm.ascent + height;

	}

}
