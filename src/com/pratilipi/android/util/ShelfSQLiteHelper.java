package com.pratilipi.android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ShelfSQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_SHELF = "shelf";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PRATILIPI_ID = "pratilipi_id";
	public static final String COLUMN_CONTENT = "content";
	public static final String COLUMN_LANGUAGE = "language";

	private static final String DATABASE_NAME = "shelf.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table " + TABLE_SHELF
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PRATILIPI_ID + " long unique, " + COLUMN_CONTENT
			+ " text not null, " + COLUMN_LANGUAGE + " text not null);";

	public ShelfSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ShelfSQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHELF);
		onCreate(db);
	}

}
