package com.pratilipi.android.htmlspanner;

public interface FontResolver {

	FontFamily getDefaultFont();

	FontFamily getSansSerifFont();

	FontFamily getSerifFont();

	FontFamily getMonoSpaceFont();

	FontFamily getFont(String name);

}
