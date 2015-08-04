package com.pratilipi.android.htmlspanner.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

import com.pratilipi.android.htmlspanner.HtmlSpanner;

public class ListItemSpan implements LeadingMarginSpan {

	private final int mNumber;

	private static final int BULLET_RADIUS = 3;
	private static final int NUMBER_RADIUS = 5;

	// Gap should be about 1em
	public static final int STANDARD_GAP_WIDTH = HtmlSpanner.HORIZONTAL_EM_WIDTH;

	public ListItemSpan() {
		mNumber = -1;
	}

	public ListItemSpan(int number) {
		mNumber = number;
	}

	public int getLeadingMargin(boolean first) {
		if (mNumber != -1) {
			return 2 * NUMBER_RADIUS + STANDARD_GAP_WIDTH;
		} else {
			return 2 * BULLET_RADIUS + STANDARD_GAP_WIDTH;
		}
	}

	public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top,
			int baseline, int bottom, CharSequence text, int start, int end,
			boolean first, Layout l) {
		if (((Spanned) text).getSpanStart(this) == start) {
			Paint.Style style = p.getStyle();

			p.setStyle(Paint.Style.FILL);

			if (mNumber != -1) {
				c.drawText(mNumber + ".", x + dir, baseline, p);
			} else {
				c.drawText("\u2022", x + dir, baseline, p);
			}

			p.setStyle(style);
		}
	}

}
