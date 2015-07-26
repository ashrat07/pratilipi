package com.pratilipi.android.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ashish on 7/11/15.
 */
public class StoreHomeDataSource {

	private SQLiteDatabase database;
	private StoreHomeSQLiteHelper dbHelper;
	private String[] allColumns = { StoreHomeSQLiteHelper.COLUMN_ID,
			StoreHomeSQLiteHelper.COLUMN_CONTENT,
			StoreHomeSQLiteHelper.COLUMN_LANGUAGE };

	public StoreHomeDataSource(Context context) {
		dbHelper = StoreHomeSQLiteHelper.getInstance(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public String createStoreHomeContent(String content, String language) {
		String newContent = null;
		ContentValues values = new ContentValues();
		values.put(StoreHomeSQLiteHelper.COLUMN_CONTENT, content);
		values.put(StoreHomeSQLiteHelper.COLUMN_LANGUAGE, language);
		long insertId = database.insert(StoreHomeSQLiteHelper.TABLE_STORE_HOME,
				null, values);
		Cursor cursor = database.query(StoreHomeSQLiteHelper.TABLE_STORE_HOME,
				allColumns, StoreHomeSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			newContent = cursor.getString(1);
		}
		cursor.close();
		return newContent;
	}

	public void deleteContent(String language) {
		database.delete(StoreHomeSQLiteHelper.TABLE_STORE_HOME,
				StoreHomeSQLiteHelper.COLUMN_LANGUAGE + " = " + language, null);
	}

	public String getContent(String language) {
		String content = null;
		Cursor cursor = database.query(StoreHomeSQLiteHelper.TABLE_STORE_HOME,
				allColumns, StoreHomeSQLiteHelper.COLUMN_LANGUAGE + " = ?",
				new String[] { language }, null, null, null);

		if (cursor.moveToFirst()) {
			content = cursor.getString(1);
		}
		// make sure to close the cursor
		cursor.close();
		return content;
	}

}
