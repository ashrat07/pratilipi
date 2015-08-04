package com.pratilipi.android.htmlspanner.handlers;

import com.pratilipi.android.htmlspanner.style.Style;

public class MonoSpaceHandler extends StyledTextHandler {

	@Override
	public Style getStyle() {
		return new Style().setFontFamily(getSpanner().getFontResolver()
				.getMonoSpaceFont());
	}

}
