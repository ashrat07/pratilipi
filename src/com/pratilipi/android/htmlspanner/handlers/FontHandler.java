package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.TagNode;

import com.pratilipi.android.htmlspanner.FontFamily;
import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.css.CSSCompiler;
import com.pratilipi.android.htmlspanner.style.Style;

import android.text.SpannableStringBuilder;

public class FontHandler extends StyledTextHandler {

	public FontHandler() {
		super(new Style());
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, Style style, SpanStack spanStack) {

		if (getSpanner().isAllowStyling()) {

			String face = node.getAttributeByName("face");
			String size = node.getAttributeByName("size");
			String color = node.getAttributeByName("color");

			FontFamily family = getSpanner().getFont(face);

			style = style.setFontFamily(family);

			if (size != null) {
				CSSCompiler.StyleUpdater updater = CSSCompiler.getStyleUpdater(
						"font-size", size);

				if (updater != null) {
					style = updater.updateStyle(style, getSpanner());
				}
			}

			if (color != null && getSpanner().isUseColoursFromStyle()) {

				CSSCompiler.StyleUpdater updater = CSSCompiler.getStyleUpdater(
						"color", color);

				if (updater != null) {
					style = updater.updateStyle(style, getSpanner());
				}
			}
		}

		super.handleTagNode(node, builder, start, end, style, spanStack);
	}

}
