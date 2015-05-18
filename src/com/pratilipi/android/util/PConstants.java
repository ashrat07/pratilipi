package com.pratilipi.android.util;

public class PConstants {

	public static final String HOST = "https://itunes.apple.com";

	public static final boolean IS_DISPLAY_LOGS = true;

	public static final String URL = "url";

	/**
	 * Setting the time limit on connection establishment in a web service call
	 */
	public static final int CONNECTION_TIMEOUT_MILLISECONDS = 20000;

	/**
	 * Setting the time limit on data read in a web service call
	 */
	public static final int SOCKET_TIMEOUT_MILLISECONDS = 20000;

	public static final String APP_CONFIG = "com.pratilipi.android.appstate";
	public static final String TERM = "{term}";

	public static final String SEARCH_EBOOK_URL = HOST
			+ "/search?term={term}&media=ebook";

	public static final String FEATURED_URL = HOST
			+ "/us/rss/toppaidebooks/limit=15/json";
	public static final String NEW_RELEASES_URL = HOST
			+ "/us/rss/topfreeebooks/limit=15/json";

	public enum LANGUAGE {

		HINDI("hindi"), TAMIL("tamil"), GUJRATI("gujrati");

		private final String language;

		private LANGUAGE(String language) {
			this.language = language;
		}

		@Override
		public String toString() {
			return language;
		}
	}

}
