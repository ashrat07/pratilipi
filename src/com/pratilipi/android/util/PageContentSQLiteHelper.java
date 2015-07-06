package com.pratilipi.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PageContentSQLiteHelper extends SQLiteOpenHelper {

	private static PageContentSQLiteHelper sInstance;

	public static final String TABLE_PAGE_CONENT = "page_content";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PRATILIPI_ID = "pratilipi_id";
	public static final String COLUMN_PAGE_NO = "page_no";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_LANGUAGE = "language";

	private static final String DATABASE_NAME = "page_content.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_PAGE_CONENT + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_PRATILIPI_ID
			+ " long, " + COLUMN_PAGE_NO + " int, " + COLUMN_CONTENT
			+ " text not null, " + COLUMN_LANGUAGE + " text not null, unique ("
			+ COLUMN_PAGE_NO + ", " + COLUMN_PRATILIPI_ID
			+ ") on conflict replace);";

	public static synchronized PageContentSQLiteHelper getInstance(
			Context context) {
		if (sInstance == null) {
			sInstance = new PageContentSQLiteHelper(
					context.getApplicationContext());
		}
		return sInstance;
	}

	private PageContentSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ShelfContentSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_CONENT);
		onCreate(db);
	}

}
