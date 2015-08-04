package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;

import com.pratilipi.android.htmlspanner.HtmlSpanner;
import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;

public class WrappingHandler extends TagNodeHandler {

	private TagNodeHandler wrappedHandler;

	public WrappingHandler(TagNodeHandler wrappedHandler) {
		this.wrappedHandler = wrappedHandler;
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {
		wrappedHandler.handleTagNode(node, builder, start, end, spanStack);
	}

	@Override
	public void setSpanner(HtmlSpanner spanner) {
		super.setSpanner(spanner);
		wrappedHandler.setSpanner(spanner);
	}

	protected TagNodeHandler getWrappedHandler() {
		return wrappedHandler;
	}

}
