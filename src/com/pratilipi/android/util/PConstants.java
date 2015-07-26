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

	public static final String STORE_TOP_CONTENT_URL = HOST + "/mobileinit";
	public static final String TOP_CONTENT_URL = HOST + "/pratilipi/list";
	public static final String STORE_CATEGORY_URL = HOST + "/category";
	public static final String CATEGORY_URL = HOST + "/category/pratilipilist";
	public static final String SEARCH_URL = HOST + "/search";
	public static final String LOGIN_URL = HOST + "/oauth";
	public static final String USER_PROFILE_URL = HOST + "/userprofile";
	public static final String REGISTER_URL = HOST + "/register";
	public static final String COVER_IMAGE_URL = HOST
			+ "/pratilipi/cover?pratilipiId={pratilipi_id}&width=200";
	public static final String CONTENT_URL = HOST + "/pratilipi/content";

	// Local Broadcasts
	public static final String LOGIN_DONE_BROADCAST = "LoginDoneBroadcast";
	public static final String LOGIN_FAILED_BROADCAST = "LoginFailedBroadcast";
	public static final String LOGIN_MANAGER_CANCELED_BROADCAST = "LoginManagerCanceledBroadcast";

	public enum CONTENT_LANGUAGE {

		HINDI("hindi", 0, "5130467284090880"), TAMIL("tamil", 1,
				"6319546696728576"), GUJARATI("gujarati", 2, "5965057007550464");

		private final String language;
		private final int id;
		private final String hashCode;

		private CONTENT_LANGUAGE(String language, int id, String hashCode) {
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

	public enum MENU_LANGUAGE {

		ENGLISH(0, "regular", "en"), HINDI(1, "hindi", "hi"), TAMIL(2, "tamil",
				"ta"), GUJARATI(3, "gujarati", "gu");

		public final int id;
		public final String typeface;
		public final String locale;

		private MENU_LANGUAGE(int id, String typeface, String locale) {
			this.id = id;
			this.typeface = typeface;
			this.locale = locale;
		}

		public int getId() {
			return id;
		}

		public String getTypeface() {
			return typeface;
		}

		public String getLocale() {
			return locale;
		}

	}

}
