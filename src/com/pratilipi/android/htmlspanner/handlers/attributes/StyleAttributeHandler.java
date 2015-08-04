package com.pratilipi.android.htmlspanner.handlers.attributes;

import org.htmlcleaner.TagNode;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.css.CSSCompiler;
import com.pratilipi.android.htmlspanner.handlers.StyledTextHandler;
import com.pratilipi.android.htmlspanner.style.Style;

import android.text.SpannableStringBuilder;
import android.util.Log;

public class StyleAttributeHandler extends WrappingStyleHandler {

	public StyleAttributeHandler(StyledTextHandler wrapHandler) {
		super(wrapHandler);
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, Style useStyle, SpanStack spanStack) {

		String styleAttr = node.getAttributeByName("style");

		if (getSpanner().isAllowStyling() && styleAttr != null) {
			super.handleTagNode(node, builder, start, end,
					parseStyleFromAttribute(useStyle, styleAttr), spanStack);
		} else {
			super.handleTagNode(node, builder, start, end, useStyle, spanStack);
		}

	}

	private Style parseStyleFromAttribute(Style baseStyle, String attribute) {
		Style style = baseStyle;

		String[] pairs = attribute.split(";");
		for (String pair : pairs) {

			String[] keyVal = pair.split(":");

			if (keyVal.length != 2) {
				Log.e("StyleAttributeHandler", "Could not parse attribute: "
						+ attribute);
				return baseStyle;
			}

			String key = keyVal[0].toLowerCase().trim();
			String value = keyVal[1].toLowerCase().trim();

			CSSCompiler.StyleUpdater updater = CSSCompiler.getStyleUpdater(key,
					value);

			if (updater != null) {
				style = updater.updateStyle(style, getSpanner());
			}

		}

		return style;
	}

}
