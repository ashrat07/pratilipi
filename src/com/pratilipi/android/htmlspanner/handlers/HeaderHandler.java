package com.pratilipi.android.htmlspanner.handlers;

import com.pratilipi.android.htmlspanner.style.Style;
import com.pratilipi.android.htmlspanner.style.StyleValue;

public class HeaderHandler extends StyledTextHandler {

	private final StyleValue size;
	private final StyleValue margin;

	/**
	 * Creates a HeaderHandler which gives
	 * 
	 * @param size
	 */
	public HeaderHandler(float size, float margin) {
		this.size = new StyleValue(size, StyleValue.Unit.EM);
		this.margin = new StyleValue(margin, StyleValue.Unit.EM);
	}

	@Override
	public Style getStyle() {
		return super.getStyle().setFontSize(size)
				.setFontWeight(Style.FontWeight.BOLD)
				.setDisplayStyle(Style.DisplayStyle.BLOCK)
				.setMarginBottom(margin).setMarginTop(margin);

	}

}
