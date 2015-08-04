package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;
import android.text.style.URLSpan;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;

public class LinkHandler extends TagNodeHandler {

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {

		final String href = node.getAttributeByName("href");
		spanStack.pushSpan(new URLSpan(href), start, end);
	}

}
