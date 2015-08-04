package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;

public class NewLineHandler extends WrappingHandler {

	private int numberOfNewLines;

	/**
	 * Creates this handler for a specified number of newlines.
	 * 
	 * @param howMany
	 */
	public NewLineHandler(int howMany, TagNodeHandler wrappedHandler) {
		super(wrappedHandler);
		this.numberOfNewLines = howMany;
	}

	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {

		super.handleTagNode(node, builder, start, end, spanStack);

		for (int i = 0; i < numberOfNewLines; i++) {
			appendNewLine(builder);
		}
	}

}
