package com.pratilipi.android.util;

import java.util.ArrayList;
import java.util.List;

import com.pratilipi.android.model.ShelfContent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ShelfContentDataSource {

	private SQLiteDatabase database;
	private ShelfContentSQLiteHelper dbHelper;
	private String[] allColumns = { ShelfContentSQLiteHelper.COLUMN_ID,
			ShelfContentSQLiteHelper.COLUMN_PRATILIPI_ID,
			ShelfContentSQLiteHelper.COLUMN_CONTENT,
			ShelfContentSQLiteHelper.COLUMN_LANGUAGE };

	public ShelfContentDataSource(Context context) {
		dbHelper = ShelfContentSQLiteHelper.getInstance(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ShelfContent createShelfContent(ShelfContent shelfContent) {
		ShelfContent newShelfContent = null;
		ContentValues values = new ContentValues();
		values.put(ShelfContentSQLiteHelper.COLUMN_PRATILIPI_ID,
				shelfContent.pratilipiId);
		values.put(ShelfContentSQLiteHelper.COLUMN_CONTENT,
				shelfContent.content);
		values.put(ShelfContentSQLiteHelper.COLUMN_LANGUAGE,
				shelfContent.language);
		long insertId = database.insert(
				ShelfContentSQLiteHelper.TABLE_SHELF_CONTENT, null, values);
		Cursor cursor = database.query(
				ShelfContentSQLiteHelper.TABLE_SHELF_CONTENT, allColumns,
				ShelfContentSQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			newShelfContent = cursorToShelf(cursor);
		}
		cursor.close();
		return newShelfContent;
	}

	public void deleteContent(long pratilipiId) {
		database.delete(ShelfContentSQLiteHelper.TABLE_SHELF_CONTENT,
				ShelfContentSQLiteHelper.COLUMN_PRATILIPI_ID + " = "
						+ pratilipiId, null);
	}

	public List<ShelfContent> getAllContent() {
		List<ShelfContent> shelfList = new ArrayList<>();
		Cursor cursor = database.query(
				ShelfContentSQLiteHelper.TABLE_SHELF_CONTENT, allColumns, null,
				null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ShelfContent shelfContent = cursorToShelf(cursor);
			shelfList.add(shelfContent);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return shelfList;
	}

	private ShelfContent cursorToShelf(Cursor cursor) {
		ShelfContent shelfContent = new ShelfContent(cursor.getLong(0),
				cursor.getLong(1), cursor.getString(2), cursor.getString(3));
		return shelfContent;
	}

}
