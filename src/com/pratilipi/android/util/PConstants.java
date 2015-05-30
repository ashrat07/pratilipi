package com.pratilipi.android.util;

public class PConstants {

	public static final String HOST = "http://www.pratilipi.com/api.pratilipi";

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

	public static final String PLACEHOLDER_LANGUAGE_ID = "{language_id}";
	public static final String PLACEHOLDER_PRATILIPI_ID = "{pratilipi_id}";

	public static final String TOP_READ_CONTENT_URL = HOST
			+ "/mobileinit?languageId={language_id}";
	public static final String COVER_IMAGE_URL = HOST
			+ "/pratilipi/cover?pratilipiId={pratilipi_id}&width=200";

	public enum LANGUAGE {

		HINDI("hindi", "5130467284090880"), TAMIL("tamil", "6319546696728576"), GUJARATI(
				"gujarati", "5965057007550464");

		private final String language;
		private final String id;

		private LANGUAGE(String language, String id) {
			this.language = language;
			this.id = id;
		}

		@Override
		public String toString() {
			return language;
		}

		public String getId() {
			return id;
		}
	}

}
