package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;
import android.text.style.SuperscriptSpan;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;

public class SuperScriptHandler extends TagNodeHandler {

	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {
		spanStack.pushSpan(new SuperscriptSpan(), start, end);
	}

}
