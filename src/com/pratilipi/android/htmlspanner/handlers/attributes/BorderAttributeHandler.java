package com.pratilipi.android.htmlspanner.handlers.attributes;

import org.htmlcleaner.TagNode;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.handlers.StyledTextHandler;
import com.pratilipi.android.htmlspanner.spans.BorderSpan;
import com.pratilipi.android.htmlspanner.style.Style;

import android.text.SpannableStringBuilder;
import android.util.Log;

public class BorderAttributeHandler extends WrappingStyleHandler {

	public BorderAttributeHandler(StyledTextHandler handler) {
		super(handler);
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, Style useStyle, SpanStack spanStack) {

		if (node.getAttributeByName("border") != null) {
			Log.d("BorderAttributeHandler", "Adding BorderSpan from " + start
					+ " to " + end);
			spanStack.pushSpan(new BorderSpan(useStyle, start, end,
					getSpanner().isUseColoursFromStyle()), start, end);
		}

		super.handleTagNode(node, builder, start, end, useStyle, spanStack);

	}

}
