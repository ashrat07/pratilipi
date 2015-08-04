package com.pratilipi.android.view.bookview;

import android.text.SpannableStringBuilder;
import org.htmlcleaner.TagNode;

import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;
import com.pratilipi.android.htmlspanner.css.CompiledRule;

import java.util.List;

/**
 * Created with IntelliJ IDEA. User: alex Date: 6/22/13 Time: 12:50 PM To change
 * this template use File | Settings | File Templates.
 */
public class CSSLinkHandler extends TagNodeHandler {

	private TextLoader textLoader;

	public CSSLinkHandler(TextLoader textLoader) {
		this.textLoader = textLoader;
	}

	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {

		if (getSpanner().isAllowStyling()) {
			String type = node.getAttributeByName("type");
			String href = node.getAttributeByName("href");

			// Found link tag

			if (type == null || !type.equals("text/css")) {
				// Ignoring link of type
			}

			List<CompiledRule> rules = this.textLoader.getCSSRules(href);

			for (CompiledRule rule : rules) {
				spanStack.registerCompiledRule(rule);
			}
		}
	}

}
