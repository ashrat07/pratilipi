package com.pratilipi.android.htmlspanner.css;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.TagNode;

import com.pratilipi.android.htmlspanner.HtmlSpanner;
import com.pratilipi.android.htmlspanner.style.Style;

public class CompiledRule {

	private List<List<CSSCompiler.TagNodeMatcher>> matchers = new ArrayList<List<CSSCompiler.TagNodeMatcher>>();
	private List<CSSCompiler.StyleUpdater> styleUpdaters = new ArrayList<CSSCompiler.StyleUpdater>();

	private HtmlSpanner spanner;

	private String asText;

	CompiledRule(HtmlSpanner spanner,
			List<List<CSSCompiler.TagNodeMatcher>> matchers,
			List<CSSCompiler.StyleUpdater> styleUpdaters, String asText) {

		this.spanner = spanner;
		this.matchers = matchers;
		this.styleUpdaters = styleUpdaters;
		this.asText = asText;
	}

	public String toString() {
		return asText;
	}

	public Style applyStyle(final Style style) {

		Style result = style;

		for (CSSCompiler.StyleUpdater updater : styleUpdaters) {
			result = updater.updateStyle(result, spanner);
		}

		return result;
	}

	public boolean matches(TagNode tagNode) {

		for (List<CSSCompiler.TagNodeMatcher> matcherList : matchers) {
			if (matchesChain(matcherList, tagNode)) {
				return true;
			}
		}

		return false;
	}

	private static boolean matchesChain(
			List<CSSCompiler.TagNodeMatcher> matchers, TagNode tagNode) {

		TagNode nodeToMatch = tagNode;

		for (CSSCompiler.TagNodeMatcher matcher : matchers) {
			if (!matcher.matches(nodeToMatch)) {
				return false;
			}

			nodeToMatch = nodeToMatch.getParent();
		}

		return true;
	}

}
