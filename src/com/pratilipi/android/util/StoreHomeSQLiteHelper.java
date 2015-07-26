package com.pratilipi.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ashish on 7/11/15.
 */
public class StoreHomeSQLiteHelper extends SQLiteOpenHelper {

	public static StoreHomeSQLiteHelper sInstance;

	public static final String TABLE_STORE_HOME = "store_home";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_LANGUAGE = "language";

	private static final String DATABASE_NAME = "store_home.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_STORE_HOME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_CONTENT
			+ " text not null, " + COLUMN_LANGUAGE + " text not null);";

	public static synchronized StoreHomeSQLiteHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new StoreHomeSQLiteHelper(
					context.getApplicationContext());
		}
		return sInstance;
	}

	private StoreHomeSQLiteHelper(Context context) {
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE_HOME);
		onCreate(db);
	}
}
