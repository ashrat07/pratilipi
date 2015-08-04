package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;
import android.text.style.SubscriptSpan;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;

public class SubScriptHandler extends TagNodeHandler {

	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {

		spanStack.pushSpan(new SubscriptSpan(), start, end);
	}

}
