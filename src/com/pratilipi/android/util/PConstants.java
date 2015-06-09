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

	public static final String STORE_HOME_LISTINGS_URL = HOST + "/mobileinit";
	public static final String SEARCH_URL = HOST + "/search";
	public static final String COVER_IMAGE_URL = HOST
			+ "/pratilipi/cover?pratilipiId={pratilipi_id}&width=200";

	public enum LANGUAGE {

		HINDI("hindi", 0, "5130467284090880"), TAMIL("tamil", 1,
				"6319546696728576"), GUJARATI("gujarati", 2, "5965057007550464");

		private final String language;
		private final int id;
		private final String hashCode;

		private LANGUAGE(String language, int id, String hashCode) {
			this.language = language;
			this.id = id;
			this.hashCode = hashCode;
		}

		@Override
		public String toString() {
			return language;
		}

		public int getId() {
			return id;
		}

		public String getHashCode() {
			return hashCode;
		}
	}

}
