package com.pratilipi.android.htmlspanner.handlers.attributes;

import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;

import com.pratilipi.android.htmlspanner.HtmlSpanner;
import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.handlers.StyledTextHandler;
import com.pratilipi.android.htmlspanner.style.Style;

public class WrappingStyleHandler extends StyledTextHandler {

	private StyledTextHandler wrappedHandler;

	public WrappingStyleHandler(StyledTextHandler wrappedHandler) {
		super(new Style());
		this.wrappedHandler = wrappedHandler;
	}

	@Override
	public Style getStyle() {
		return wrappedHandler.getStyle();
	}

	@Override
	public void beforeChildren(TagNode node, SpannableStringBuilder builder,
			SpanStack spanStack) {
		if (wrappedHandler != null) {
			wrappedHandler.beforeChildren(node, builder, spanStack);
		}
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, Style useStyle, SpanStack spanStack) {
		if (wrappedHandler != null) {
			wrappedHandler.handleTagNode(node, builder, start, end, useStyle,
					spanStack);
		}
	}

	public StyledTextHandler getWrappedHandler() {
		return this.wrappedHandler;
	}

	@Override
	public void setSpanner(HtmlSpanner spanner) {
		super.setSpanner(spanner);

		if (this.getWrappedHandler() != null) {
			this.getWrappedHandler().setSpanner(spanner);
		}
	}

}
