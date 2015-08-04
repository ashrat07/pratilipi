package com.pratilipi.android.htmlspanner.handlers;

import org.htmlcleaner.ContentNode;
import org.htmlcleaner.TagNode;

import android.text.SpannableStringBuilder;

import com.pratilipi.android.htmlspanner.FontFamily;
import com.pratilipi.android.htmlspanner.SpanStack;
import com.pratilipi.android.htmlspanner.TagNodeHandler;
import com.pratilipi.android.htmlspanner.TextUtil;
import com.pratilipi.android.htmlspanner.spans.FontFamilySpan;

public class PreHandler extends TagNodeHandler {

	private void getPlainText(StringBuffer buffer, Object node) {
		if (node instanceof ContentNode) {

			ContentNode contentNode = (ContentNode) node;
			String text = TextUtil.replaceHtmlEntities(contentNode.getContent()
					.toString(), true);

			buffer.append(text);

		} else if (node instanceof TagNode) {
			TagNode tagNode = (TagNode) node;
			for (Object child : tagNode.getChildren()) {
				getPlainText(buffer, child);
			}
		}
	}

	@Override
	public void handleTagNode(TagNode node, SpannableStringBuilder builder,
			int start, int end, SpanStack spanStack) {

		StringBuffer buffer = new StringBuffer();
		getPlainText(buffer, node);

		builder.append(buffer.toString());

		FontFamily monoSpace = getSpanner().getFontResolver()
				.getMonoSpaceFont();
		spanStack.pushSpan(new FontFamilySpan(monoSpace), start,
				builder.length());
		appendNewLine(builder);
		appendNewLine(builder);
	}

	@Override
	public boolean rendersContent() {
		return true;
	}

}
