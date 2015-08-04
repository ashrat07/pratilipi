package com.pratilipi.android.htmlspanner.handlers.attributes;

import org.htmlcleaner.TagNode;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.handlers.StyledTextHandler;
import com.pratilipi.android.htmlspanner.style.Style;

import android.text.SpannableStringBuilder;

public class AlignmentAttributeHandler extends WrappingStyleHandler {

	public AlignmentAttributeHandler(StyledTextHandler wrapHandler) {
		super(wrapHandler);
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, Style style, SpanStack spanStack) {

		String align = node.getAttributeByName("align");

		if ("right".equalsIgnoreCase(align)) {
			style = style.setTextAlignment(Style.TextAlignment.RIGHT);
		} else if ("center".equalsIgnoreCase(align)) {
			style = style.setTextAlignment(Style.TextAlignment.CENTER);
		} else if ("left".equalsIgnoreCase(align)) {
			style = style.setTextAlignment(Style.TextAlignment.LEFT);
		}

		super.handleTagNode(node, builder, start, end, style, spanStack);
	}

}
