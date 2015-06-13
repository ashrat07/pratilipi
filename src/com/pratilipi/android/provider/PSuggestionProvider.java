package com.pratilipi.android.provider;

import android.content.SearchRecentSuggestionsProvider;

public class PSuggestionProvider extends SearchRecentSuggestionsProvider {

	public static final String AUTHORITY = "com.pratilipi.android.provider.PSuggestionProvider";
	public static final int MODE = DATABASE_MODE_QUERIES;

	public PSuggestionProvider() {
		setupSuggestions(AUTHORITY, MODE);
	}

}
